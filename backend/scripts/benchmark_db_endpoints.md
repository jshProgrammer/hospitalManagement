# Database Endpoint Benchmark

This script measures average response times for the backend's database-related
read-only endpoints.

It only calls `GET` endpoints, so it does not create, update, or delete data.

## Run It

From the backend folder:

```bash
./scripts/benchmark_db_endpoints.py
```

The script starts the backend automatically with:

```bash
./gradlew bootRun
```

## If The Backend Is Already Running

```bash
./scripts/benchmark_db_endpoints.py --no-start
```

## More Accurate Results

Run each endpoint more often:

```bash
./scripts/benchmark_db_endpoints.py --iterations 50 --warmup 10
```

- `--warmup 10` runs each endpoint 10 times before measuring.
- `--iterations 50` measures each endpoint 50 times.

## Default Parameters

If you run the script without options, it uses:

| Option | Default |
| --- | --- |
| `--base-url` | `http://localhost:8080` |
| `--iterations` | `30` |
| `--warmup` | `5` |
| `--timeout` | `30.0` seconds |
| `--startup-timeout` | `90.0` seconds |
| `--page-size` | `20` |
| `--output-dir` | `benchmarks/results` |
| `--gradle-command` | `./gradlew bootRun` |
| `--no-start` | off |
| `--continue-on-timeout` | off |

By default, if a request times out, the script stops testing that endpoint and
moves to the next endpoint. The timeout is still written to the result file.

To keep trying the remaining requests for that same endpoint after a timeout,
use:

```bash
./scripts/benchmark_db_endpoints.py --continue-on-timeout
```

## Output

Results are written to:

```text
benchmarks/results/
```

Each run creates:

- a `.json` file with full detailed data
- a `.csv` file with a simple summary

Use the CSV file if you want to compare average response times in a spreadsheet.

## Different Backend URL

```bash
./scripts/benchmark_db_endpoints.py --base-url http://localhost:8080
```

## Short Test Run

Use this to quickly check that the script works:

```bash
./scripts/benchmark_db_endpoints.py --iterations 1 --warmup 0
```
