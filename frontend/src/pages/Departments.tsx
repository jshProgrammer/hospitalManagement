import EmptyState from '../components/EmptyState.tsx'
import MainPage from '../layout/MainPage.tsx'

export function Departments() {
  return (
    <MainPage title="Departments" description="Hospital departments and service areas">
      <EmptyState
        title="No departments configured"
        detail="Department records will appear here once they are added."
      />
    </MainPage>
  )
}
