#!/usr/bin/env python3
"""
Benchmark read-only, database-backed backend endpoints.

The script can start the Spring Boot backend, discover representative IDs from
collection endpoints, run each read-only endpoint repeatedly, and write JSON and
CSV results that can be compared between code changes.
"""

from __future__ import annotations

import argparse
import csv
import json
import math
import shlex
import signal
import subprocess
import sys
import time
from dataclasses import dataclass, field
from datetime import datetime, timezone
from pathlib import Path
from statistics import mean, median, stdev
from typing import Any
from urllib.error import HTTPError, URLError
from urllib.parse import urlencode, urljoin
from urllib.request import Request, urlopen


DEFAULT_BASE_URL = "http://localhost:8080"
DEFAULT_OUTPUT_DIR = "benchmarks/results"
DEFAULT_PAGE_SIZE = 20
DEFAULT_PAGE_NUMBER = 0
PAGINATION_PAGE = "page"
PAGINATION_CURSOR = "cursor"

@dataclass(frozen=True)
class Endpoint:
    name: str
    path_template: str
    query: dict[str, Any] = field(default_factory=dict)
    sample_key: str | None = None
    skip_without_sample: bool = False
    pagination: str | None = PAGINATION_PAGE


class EndpointTimedOut(RuntimeError):
    def __init__(self, sample: dict[str, Any]):
        super().__init__(sample.get("error", "endpoint timed out"))
        self.sample = sample


BASE_COLLECTIONS = {
    "departments": "/api/departments",
    "stations": "/api/stations",
    "rooms": "/api/rooms",
    "bookings": "/api/bookings",
    "patients": "/api/patients",
    "doctors": "/api/doctors",
    "nurses": "/api/nurses",
    "drugs": "/api/drugs",
    "doses": "/api/doses",
    "medications": "/api/medications",
    "diagnoses": "/api/diagnoses",
}

CURSOR_COLLECTION_KEYS = {
    "patients": "patients",
    "doctors": "doctors",
    "nurses": "nurses",
    "diagnoses": "diagnoses",
}


ENDPOINTS = [
    Endpoint("departments.list", "/api/departments"),
    Endpoint("departments.by_id", "/api/departments/{departments.id}", sample_key="departments", skip_without_sample=True, pagination=None),
    Endpoint("stations.list", "/api/stations"),
    Endpoint("stations.by_id", "/api/stations/{stations.id}", sample_key="stations", skip_without_sample=True, pagination=None),
    Endpoint("rooms.list", "/api/rooms"),
    Endpoint("rooms.by_id", "/api/rooms/{rooms.id}", sample_key="rooms", skip_without_sample=True, pagination=None),
    Endpoint("rooms.by_floor", "/api/rooms/floor/{rooms.floor}", sample_key="rooms", skip_without_sample=True, pagination=None),
    Endpoint("rooms.bookings", "/api/rooms/{rooms.id}/bookings", sample_key="rooms", skip_without_sample=True),
    Endpoint("bookings.list", "/api/bookings"),
    Endpoint("bookings.by_id", "/api/bookings/{bookings.id}", sample_key="bookings", skip_without_sample=True, pagination=None),
    Endpoint("patients.list", "/api/patients", pagination=PAGINATION_CURSOR),
    Endpoint("patients.by_id", "/api/patients/{patients.id}", sample_key="patients", skip_without_sample=True, pagination=None),
    Endpoint("patients.bookings", "/api/patients/{patients.id}/bookings", sample_key="patients", skip_without_sample=True),
    Endpoint("patients.diagnoses", "/api/patients/{patients.id}/diagnoses", sample_key="patients", skip_without_sample=True, pagination=PAGINATION_CURSOR),
    Endpoint("doctors.list", "/api/doctors", pagination=PAGINATION_CURSOR),
    Endpoint("doctors.by_id", "/api/doctors/{doctors.id}", sample_key="doctors", skip_without_sample=True, pagination=None),
    Endpoint("nurses.list", "/api/nurses", pagination=PAGINATION_CURSOR),
    Endpoint("nurses.by_id", "/api/nurses/{nurses.id}", sample_key="nurses", skip_without_sample=True, pagination=None),
    Endpoint("nurses.by_station", "/api/nurses/station/{nurses.stationId}", sample_key="nurses", skip_without_sample=True, pagination=None),
    Endpoint("drugs.list", "/api/drugs"),
    Endpoint("drugs.by_id", "/api/drugs/{drugs.id}", sample_key="drugs", skip_without_sample=True, pagination=None),
    Endpoint("doses.list", "/api/doses"),
    Endpoint("doses.by_id", "/api/doses/{doses.id}", sample_key="doses", skip_without_sample=True, pagination=None),
    Endpoint("medications.list", "/api/medications"),
    Endpoint("medications.by_id", "/api/medications/{medications.id}", sample_key="medications", skip_without_sample=True, pagination=None),
    Endpoint("diagnoses.list", "/api/diagnoses", pagination=PAGINATION_CURSOR),
    Endpoint("diagnoses.by_id", "/api/diagnoses/{diagnoses.id}", sample_key="diagnoses", skip_without_sample=True, pagination=None),
]


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description="Benchmark read-only, database-backed backend endpoints."
    )
    parser.add_argument("--base-url", default=DEFAULT_BASE_URL)
    parser.add_argument("--iterations", type=int, default=30)
    parser.add_argument("--warmup", type=int, default=5)
    parser.add_argument("--timeout", type=float, default=30.0)
    parser.add_argument("--startup-timeout", type=float, default=90.0)
    parser.add_argument("--page-size", type=int, default=DEFAULT_PAGE_SIZE)
    parser.add_argument("--page-number", type=int, default=DEFAULT_PAGE_NUMBER)
    parser.add_argument(
        "--cursor-after",
        type=int,
        default=None,
        help="Cursor value for cursor-paginated endpoints. Omit to benchmark the first cursor page.",
    )
    parser.add_argument("--output-dir", default=DEFAULT_OUTPUT_DIR)
    parser.add_argument("--no-start", action="store_true", help="Use an already running backend.")
    parser.add_argument(
        "--continue-on-timeout",
        action="store_true",
        help="Keep trying the remaining requests for an endpoint after a timeout.",
    )
    parser.add_argument(
        "--gradle-command",
        default="./gradlew bootRun",
        help="Command used to start the backend when it is not already running.",
    )
    return parser.parse_args()


def log(message: str) -> None:
    print(message, flush=True)


def make_url(base_url: str, path: str, query: dict[str, Any] | None = None) -> str:
    url = urljoin(base_url.rstrip("/") + "/", path.lstrip("/"))
    if query:
        cleaned = {key: value for key, value in query.items() if value is not None}
        if cleaned:
            url = f"{url}?{urlencode(cleaned)}"
    return url


def get_json(base_url: str, path: str, query: dict[str, Any] | None, timeout: float) -> tuple[int, Any, int]:
    url = make_url(base_url, path, query)
    request = Request(url, headers={"Accept": "application/json"})
    try:
        with urlopen(request, timeout=timeout) as response:
            body = response.read()
            status = response.status
    except HTTPError as exc:
        body = exc.read()
        status = exc.code
    except URLError as exc:
        raise RuntimeError(f"Could not connect to {url}: {exc}") from exc

    if not body:
        return status, None, 0

    try:
        return status, json.loads(body.decode("utf-8")), len(body)
    except json.JSONDecodeError:
        return status, body.decode("utf-8", errors="replace"), len(body)


def is_backend_ready(base_url: str, timeout: float) -> bool:
    try:
        status, _, _ = get_json(base_url, "/api/departments", {"page": 0, "size": 1}, timeout)
        return status < 500
    except Exception:
        return False


def start_backend(args: argparse.Namespace, output_dir: Path) -> subprocess.Popen[str] | None:
    if args.no_start or is_backend_ready(args.base_url, 2.0):
        log(f"Using backend at {args.base_url}")
        return None

    log(f"Starting backend with: {args.gradle_command}")
    log_path = output_dir / "backend.log"
    log_file = log_path.open("w", encoding="utf-8")
    process = subprocess.Popen(
        shlex.split(args.gradle_command),
        cwd=Path(__file__).resolve().parents[1],
        stdout=log_file,
        stderr=subprocess.STDOUT,
        text=True,
    )
    process._benchmark_log_file = log_file  # type: ignore[attr-defined]

    started_at = time.monotonic()
    while time.monotonic() - started_at < args.startup_timeout:
        if process.poll() is not None:
            raise RuntimeError(f"Backend exited early. See {log_path}")
        if is_backend_ready(args.base_url, 2.0):
            log(f"Backend is ready at {args.base_url}")
            return process
        time.sleep(1)

    stop_backend(process)
    raise RuntimeError(f"Backend did not become ready within {args.startup_timeout:.0f}s. See {log_path}")


def stop_backend(process: subprocess.Popen[str] | None) -> None:
    if process is None or process.poll() is not None:
        if process is not None and hasattr(process, "_benchmark_log_file"):
            process._benchmark_log_file.close()  # type: ignore[attr-defined]
        return

    process.send_signal(signal.SIGTERM)
    try:
        process.wait(timeout=15)
    except subprocess.TimeoutExpired:
        process.kill()
        process.wait(timeout=5)
    finally:
        if hasattr(process, "_benchmark_log_file"):
            process._benchmark_log_file.close()  # type: ignore[attr-defined]


def page_content(payload: Any, collection_key: str | None = None) -> list[Any]:
    if collection_key and isinstance(payload, dict) and isinstance(payload.get(collection_key), list):
        return payload[collection_key]
    if isinstance(payload, dict) and isinstance(payload.get("content"), list):
        return payload["content"]
    if isinstance(payload, list):
        return payload
    return []


def collection_query(collection: str, page_size: int) -> dict[str, Any]:
    if collection in CURSOR_COLLECTION_KEYS:
        return {"limit": max(1, page_size)}
    return {"page": 0, "size": max(1, page_size)}


def endpoint_query(args: argparse.Namespace, endpoint: Endpoint) -> dict[str, Any]:
    if endpoint.pagination == PAGINATION_CURSOR:
        query = {"limit": args.page_size, **endpoint.query}
        if args.cursor_after is not None:
            query["after"] = args.cursor_after
        return query
    if endpoint.pagination == PAGINATION_PAGE:
        return {"page": args.page_number, "size": args.page_size, **endpoint.query}
    return dict(endpoint.query)


def first_present(data: dict[str, Any], names: list[str]) -> Any:
    for name in names:
        if name in data and data[name] is not None:
            return data[name]
    return None


def nested_value(data: dict[str, Any], path: list[str]) -> Any:
    current: Any = data
    for key in path:
        if not isinstance(current, dict):
            return None
        current = current.get(key)
    return current


def extract_sample(collection: str, payload: Any) -> dict[str, Any]:
    rows = [
        row
        for row in page_content(payload, CURSOR_COLLECTION_KEYS.get(collection))
        if isinstance(row, dict)
    ]
    if not rows:
        return {}

    row = rows[0]
    sample = dict(row)
    sample["id"] = first_present(row, ["id", "patientId", "personId"])

    if collection == "rooms":
        sample["floor"] = first_present(row, ["floor", "roomFloor"])
    if collection == "nurses":
        sample["stationId"] = (
            first_present(row, ["stationId", "stationID"])
            or nested_value(row, ["station", "id"])
        )

    return sample


def discover_samples(args: argparse.Namespace) -> dict[str, dict[str, Any]]:
    samples: dict[str, dict[str, Any]] = {}
    for name, path in BASE_COLLECTIONS.items():
        log(f"Discovering sample data: {name}")
        try:
            status, payload, _ = get_json(
                args.base_url,
                path,
                collection_query(name, args.page_size),
                args.timeout,
            )
        except Exception:
            if not args.continue_on_timeout:
                raise
            samples[name] = {}
            continue

        if 200 <= status < 400:
            samples[name] = extract_sample(name, payload)
        else:
            samples[name] = {}
    return samples


def render_path(template: str, samples: dict[str, dict[str, Any]]) -> str | None:
    rendered = template
    while "{" in rendered and "}" in rendered:
        start = rendered.index("{")
        end = rendered.index("}", start)
        token = rendered[start + 1 : end]
        collection, field_name = token.split(".", 1)
        value = samples.get(collection, {}).get(field_name)
        if value is None:
            return None
        rendered = rendered[:start] + str(value) + rendered[end + 1 :]
    return rendered


def timed_request(args: argparse.Namespace, path: str, query: dict[str, Any]) -> dict[str, Any]:
    started = time.perf_counter_ns()
    try:
        status, _, bytes_read = get_json(args.base_url, path, query, args.timeout)
        elapsed_ms = (time.perf_counter_ns() - started) / 1_000_000
        return {
            "status": status,
            "elapsed_ms": elapsed_ms,
            "bytes": bytes_read,
        }
    except Exception as exc:
        elapsed_ms = (time.perf_counter_ns() - started) / 1_000_000
        sample = {
            "status": 0,
            "elapsed_ms": elapsed_ms,
            "bytes": 0,
            "error": str(exc),
        }
        if not args.continue_on_timeout:
            raise EndpointTimedOut(sample) from exc
        return sample


def percentile(values: list[float], percent: float) -> float | None:
    if not values:
        return None
    sorted_values = sorted(values)
    index = (len(sorted_values) - 1) * percent
    lower = math.floor(index)
    upper = math.ceil(index)
    if lower == upper:
        return sorted_values[int(index)]
    weight = index - lower
    return sorted_values[lower] * (1 - weight) + sorted_values[upper] * weight


def summarize(name: str, method: str, path: str, query: dict[str, Any], samples: list[dict[str, Any]]) -> dict[str, Any]:
    timings = [sample["elapsed_ms"] for sample in samples if 200 <= sample["status"] < 400]
    statuses: dict[str, int] = {}
    for sample in samples:
        status_key = str(sample["status"])
        statuses[status_key] = statuses.get(status_key, 0) + 1

    return {
        "name": name,
        "method": method,
        "path": path,
        "query": query,
        "runs": len(samples),
        "status_counts": statuses,
        "avg_ms": mean(timings) if timings else None,
        "median_ms": median(timings) if timings else None,
        "min_ms": min(timings) if timings else None,
        "max_ms": max(timings) if timings else None,
        "p95_ms": percentile(timings, 0.95),
        "stdev_ms": stdev(timings) if len(timings) > 1 else 0.0 if timings else None,
        "avg_bytes": mean([sample["bytes"] for sample in samples]) if samples else None,
        "samples": samples,
    }


def benchmark_endpoint(args: argparse.Namespace, endpoint: Endpoint, path: str) -> dict[str, Any]:
    query = endpoint_query(args, endpoint)

    if args.warmup:
        log(f"  warmup {endpoint.name} ({args.warmup} request(s))")
    try:
        for _ in range(args.warmup):
            timed_request(args, path, query)
    except EndpointTimedOut as exc:
        log(f"  timeout during warmup for {endpoint.name}; moving to next endpoint")
        return summarize(endpoint.name, "GET", path, query, [exc.sample])

    log(f"  measuring {endpoint.name} ({args.iterations} request(s))")
    samples = []
    try:
        for _ in range(args.iterations):
            samples.append(timed_request(args, path, query))
    except EndpointTimedOut as exc:
        log(f"  timeout while measuring {endpoint.name}; moving to next endpoint")
        samples.append(exc.sample)
    return summarize(endpoint.name, "GET", path, query, samples)


def write_results(output_dir: Path, document: dict[str, Any]) -> tuple[Path, Path]:
    timestamp = datetime.now(timezone.utc).strftime("%Y%m%dT%H%M%SZ")
    json_path = output_dir / f"db_endpoint_benchmark_{timestamp}.json"
    csv_path = output_dir / f"db_endpoint_benchmark_{timestamp}.csv"

    json_path.write_text(json.dumps(document, indent=2), encoding="utf-8")

    with csv_path.open("w", newline="", encoding="utf-8") as csv_file:
        fieldnames = [
            "name",
            "method",
            "path",
            "query",
            "runs",
            "status_counts",
            "avg_ms",
            "median_ms",
            "min_ms",
            "max_ms",
            "p95_ms",
            "stdev_ms",
            "avg_bytes",
        ]
        writer = csv.DictWriter(csv_file, fieldnames=fieldnames)
        writer.writeheader()
        for result in document["results"]:
            writer.writerow({
                key: json.dumps(result[key]) if key in {"query", "status_counts"} else result[key]
                for key in fieldnames
            })

    return json_path, csv_path


def main() -> int:
    args = parse_args()
    if args.iterations < 1:
        raise ValueError("--iterations must be at least 1")
    if args.warmup < 0:
        raise ValueError("--warmup must be 0 or greater")
    if args.page_size < 1:
        raise ValueError("--page-size must be at least 1")
    if args.page_number < 0:
        raise ValueError("--page-number must be 0 or greater")
    if args.cursor_after is not None and args.cursor_after < 0:
        raise ValueError("--cursor-after must be 0 or greater")

    output_dir = Path(args.output_dir)
    output_dir.mkdir(parents=True, exist_ok=True)

    backend_process: subprocess.Popen[str] | None = None
    started_by_script = False
    try:
        backend_process = start_backend(args, output_dir)
        started_by_script = backend_process is not None

        samples = discover_samples(args)
        results = []
        skipped = []

        total = len(ENDPOINTS)
        for index, endpoint in enumerate(ENDPOINTS, start=1):
            path = render_path(endpoint.path_template, samples)
            if path is None:
                log(f"[{index}/{total}] Skipping {endpoint.name}: missing sample data")
                skipped.append({
                    "name": endpoint.name,
                    "reason": f"missing sample data for {endpoint.sample_key}",
                })
                continue
            log(f"[{index}/{total}] Benchmarking {endpoint.name} -> {path}")
            results.append(benchmark_endpoint(args, endpoint, path))

        document = {
            "metadata": {
                "created_at": datetime.now(timezone.utc).isoformat(),
                "base_url": args.base_url,
                "iterations": args.iterations,
                "warmup": args.warmup,
                "page_size": args.page_size,
                "page_number": args.page_number,
                "cursor_after": args.cursor_after,
                "read_only": True,
                "started_backend": started_by_script,
                "continue_on_timeout": args.continue_on_timeout,
                "notes": "Only GET endpoints are benchmarked to keep runs repeatable and avoid database mutations.",
            },
            "discovered_samples": samples,
            "skipped": skipped,
            "results": results,
        }
        json_path, csv_path = write_results(output_dir, document)

        print(f"Wrote JSON results: {json_path}")
        print(f"Wrote CSV summary: {csv_path}")
        if skipped:
            print(f"Skipped {len(skipped)} sample-dependent endpoint(s); see JSON for details.")
        return 0
    finally:
        stop_backend(backend_process)


if __name__ == "__main__":
    try:
        raise SystemExit(main())
    except KeyboardInterrupt:
        raise SystemExit(130)
    except Exception as exc:
        print(f"benchmark failed: {exc}", file=sys.stderr)
        raise SystemExit(1)
