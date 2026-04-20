import icon from '../assets/HomeIcon.png'
import { NavLink } from 'react-router-dom'

export default function HomeIcon() {
  return (
    <NavLink
      to="/"
      className="mb-3 flex items-center justify-center transition-transform hover:scale-105"
    >
      <img src={icon} alt="Hospital Management" className="h-28 w-28 object-contain" />
    </NavLink>
  )
}