import EmptyState from '../components/EmptyState.tsx'
import MainPage from '../layout/MainPage.tsx'

export function Stations() {
  return (
    <MainPage title="Stations" description="Ward stations, beds, and operational units">
      <EmptyState
        title="No stations configured"
        detail="Station records will appear here once they are added."
      />
    </MainPage>
  )
}
