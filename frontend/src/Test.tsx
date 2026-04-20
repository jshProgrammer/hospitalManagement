import Button from './components/Button.tsx'
import MainPage from './layout/MainPage.tsx'

export default function Test() {
  return (
    <MainPage
      title={'Design Schema Test'}
      children={
        <div className="bg-background text-dark h-full p-8">
          <div className="space-y-8">
            {/* Buttons */}
            <section className="bg-surface border-border rounded-xl border p-6">
              <h2 className="mb-4 text-xl font-semibold">Buttons</h2>

              <div className="flex gap-4">
                <Button label="Primary" onClick={function (): void {}} />
              </div>
            </section>

            {/* Cards */}
            <section>
              <h2 className="mb-4 text-xl font-semibold">Cards</h2>

              <div className="grid gap-6 md:grid-cols-3">
                <div className="bg-surface border-border rounded-xl border p-4">
                  <h3 className="font-semibold">Standard</h3>
                  <p className="text-dark text-sm">Ruhiger dunkler Container</p>
                </div>

                <div className="bg-highlight text-dark rounded-xl p-4">
                  <h3 className="font-semibold">Blue Highlight</h3>
                </div>
              </div>
            </section>

            {/* Status */}
            <section className="bg-surface border-border rounded-xl border p-6">
              <h2 className="mb-4 text-xl font-semibold">Status</h2>

              <div className="flex gap-4">
                <div className="bg-success text-light rounded-lg px-4 py-2">Success</div>
                <div className="bg-warning text-dark rounded-lg px-4 py-2">Warning</div>
                <div className="bg-error text-light rounded-lg px-4 py-2">Error</div>
              </div>
            </section>
          </div>
        </div>
      }
    />
  )
}