import icon from '../assets/HomeIcon.png'
import { NavLink } from 'react-router-dom'

export default function HomeIcon() {
  return (
    <NavLink
      to="/"
      className="border-border bg-surface-muted hover:border-highlight flex items-center gap-3 rounded-lg border p-3 transition-colors"
      aria-label="Hospital Management home"
    >
      <span className="bg-surface border-border flex h-12 w-12 shrink-0 items-center justify-center rounded-lg border">
        <img src={icon} alt="" className="h-9 w-9 object-contain" />
      </span>
      <span>
        <span className="text-dark block text-sm font-semibold">Hospital</span>
        <span className="text-muted block text-xs font-medium">Management</span>
      </span>
    </NavLink>
  )
}
