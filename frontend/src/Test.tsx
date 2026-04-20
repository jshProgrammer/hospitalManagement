export default function Test() {
  return (
    <div className="bg-background text-text min-h-screen p-8">
      {/* Header */}
      <header className="bg-primary shadow-soft mb-10 rounded-xl p-6">
        <h1 className="text-text-on-dark text-3xl font-bold">Dark Mode Preview</h1>
        <p className="text-text-secondary">Modernes Farbschema für deine App</p>
      </header>

      <div className="space-y-8">
        {/* Buttons */}
        <section className="bg-surface shadow-soft border-border rounded-xl border p-6">
          <h2 className="mb-4 text-xl font-semibold">Buttons</h2>

          <div className="flex gap-4">
            <button className="bg-accent hover:bg-accent-hover active:bg-accent-active rounded-lg px-4 py-2 font-medium text-white transition">
              Primary
            </button>
          </div>
        </section>

        {/* Cards */}
        <section>
          <h2 className="mb-4 text-xl font-semibold">Cards</h2>

          <div className="grid gap-6 md:grid-cols-3">
            <div className="bg-surface shadow-soft border-border rounded-xl border p-4">
              <h3 className="font-semibold">Standard</h3>
              <p className="text-text-secondary text-sm">Ruhiger dunkler Container</p>
            </div>

            <div className="bg-highlight shadow-soft rounded-xl p-4 text-white">
              <h3 className="font-semibold">Blue Highlight</h3>
            </div>

            <div className="bg-primary text-text shadow-soft rounded-xl p-4">
              <h3 className="font-semibold">Deep Background</h3>
            </div>
          </div>
        </section>

        {/* Status */}
        <section className="bg-surface shadow-soft border-border rounded-xl border p-6">
          <h2 className="mb-4 text-xl font-semibold">Status</h2>

          <div className="flex gap-4">
            <div className="bg-success rounded-lg px-4 py-2 text-white">Success</div>
            <div className="bg-warning rounded-lg px-4 py-2 text-black">Warning</div>
            <div className="bg-error rounded-lg px-4 py-2 text-white">Error</div>
          </div>
        </section>
      </div>
    </div>
  )
}