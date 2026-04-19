export default function Test() {
  return (
    <div className="min-h-screen bg-background text-text p-8">
      {/* Header */}
      <header className="mb-10 rounded-xl bg-primary p-6 shadow-soft">
        <h1 className="text-3xl font-bold text-text-on-dark">Dark Mode Preview</h1>
        <p className="text-text-secondary">Modernes Farbschema für deine App</p>
      </header>

      <div className="space-y-8">
        {/* Buttons */}
        <section className="rounded-xl bg-surface p-6 shadow-soft border border-border">
          <h2 className="mb-4 text-xl font-semibold">Buttons</h2>

          <div className="flex gap-4">
            <button className="rounded-lg bg-accent px-4 py-2 font-medium text-white transition hover:bg-accent-hover active:bg-accent-active">
              Primary
            </button>

            <button className="rounded-lg border border-border px-4 py-2 text-text hover:bg-primary">
              Secondary
            </button>
          </div>
        </section>

        {/* Cards */}
        <section>
          <h2 className="mb-4 text-xl font-semibold">Cards</h2>

          <div className="grid gap-6 md:grid-cols-3">
            <div className="rounded-xl bg-surface p-4 shadow-soft border border-border">
              <h3 className="font-semibold">Standard</h3>
              <p className="text-text-secondary text-sm">Ruhiger dunkler Container</p>
            </div>

            <div className="rounded-xl bg-highlight p-4 text-white shadow-soft">
              <h3 className="font-semibold">Blue Highlight</h3>
            </div>

            <div className="rounded-xl bg-primary p-4 text-text shadow-soft">
              <h3 className="font-semibold">Deep Background</h3>
            </div>
          </div>
        </section>

        {/* Status */}
        <section className="rounded-xl bg-surface p-6 shadow-soft border border-border">
          <h2 className="mb-4 text-xl font-semibold">Status</h2>

          <div className="flex gap-4">
            <div className="rounded-lg bg-success px-4 py-2 text-white">Success</div>
            <div className="rounded-lg bg-warning px-4 py-2 text-black">Warning</div>
            <div className="rounded-lg bg-error px-4 py-2 text-white">Error</div>
          </div>
        </section>
      </div>
    </div>
  )
}