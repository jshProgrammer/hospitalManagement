import EmptyState from '../components/EmptyState.tsx'
import MainPage from '../layout/MainPage.tsx'

export function Doctors() {
  return (
    <MainPage title="Doctors" description="Clinical staff and medical assignments">
      <EmptyState
        title="No doctors registered"
        detail="Doctor records will appear here once they are added."
      />
    </MainPage>
  )
}
