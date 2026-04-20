import icon from '../assets/HomeIcon.png'
import { NavLink } from 'react-router-dom'

export default function HomeIcon() {
  return (
    <NavLink
      to="/"
      className="bg-primary mb-3 flex items-center justify-center rounded-xl shadow-sm transition-all transition-transform duration-150 hover:scale-105 hover:shadow-md active:scale-95 active:shadow-inner"
    >
      <img src={icon} alt="Hospital Management" className="h-28 w-28 object-contain" />
    </NavLink>
  )
}