import NavItem from '../components/NavItem'
import PageHeader from '../layout/PageHeader.tsx'

export default function NotFound() {
  return (
    <div className="flex h-full flex-col items-center justify-center gap-6">
      <PageHeader title={'404'} />
      <p className="text-dark text-xl">Page not found</p>
      <NavItem to="/patients" label={'Back to the Startpage'}></NavItem>
    </div>
  )
}