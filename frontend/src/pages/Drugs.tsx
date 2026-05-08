import EmptyState from '../components/EmptyState.tsx'
import MainPage from '../layout/MainPage.tsx'

export function Drugs() {
  return (
    <MainPage title="Drugs" description="Medication catalog and inventory overview">
      <EmptyState
        title="No drugs listed"
        detail="Medication records will appear here once they are added."
      />
    </MainPage>
  )
}
