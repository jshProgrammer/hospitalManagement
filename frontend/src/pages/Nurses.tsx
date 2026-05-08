import EmptyState from '../components/EmptyState.tsx'
import MainPage from '../layout/MainPage.tsx'

export function Nurses() {
  return (
    <MainPage title="Nurses" description="Nursing teams and ward responsibilities">
      <EmptyState
        title="No nurses registered"
        detail="Nursing staff records will appear here once they are added."
      />
    </MainPage>
  )
}
