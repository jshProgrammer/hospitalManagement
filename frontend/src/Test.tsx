import Button from './components/Button.tsx'
import PageHeader from './layout/PageHeader.tsx'

export default function Test() {
  return (
    <div className="bg-background text-text h-full p-8">
      <PageHeader title={'Design Schema Test'} />
      <div className="space-y-8">
        {/* Buttons */}
        <section className="bg-surface shadow-soft border-border rounded-xl border p-6">
          <h2 className="mb-4 text-xl font-semibold">Buttons</h2>

          <div className="flex gap-4">
            <Button label="Primary" onClick={function (): void {}} />
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