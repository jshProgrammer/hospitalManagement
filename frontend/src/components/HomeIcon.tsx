import icon from '../assets/HomeIcon.png'
import { NavLink } from 'react-router-dom'

export default function HomeIcon() {
  return (
    <NavLink
      to="/"
      className="border-border bg-background hover:bg-highlight mb-2 flex items-center justify-center rounded-lg border p-3 transition-colors"
    >
      <img src={icon} alt="Hospital Management" className="h-24 w-24 object-contain" />
    </NavLink>
  )
}