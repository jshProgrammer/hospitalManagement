import NavItem from '../components/NavItem'
import PageHeader from '../layout/PageHeader.tsx'

export default function NotFound() {
  return (
    <div className="flex h-full flex-col items-center justify-center gap-6">
      <PageHeader title="404" description="The requested page could not be found." />
      <NavItem to="/patients" label={'Zur Startseite'}></NavItem>
    </div>
  )
}
