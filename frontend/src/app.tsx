import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { Patients } from './pages/patients.ts'
import { Doctors } from './pages/doctors.ts'
import { Nurses } from './pages/nurses.ts'
import { Departments } from './pages/departments.ts'
import { Stations } from './pages/stations.ts'
import { Drugs } from './pages/drugs.ts'

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/patients" replace />} />
        <Route path="/patients" element={<Patients />} />
        <Route path="/doctors" element={<Doctors />} />
        <Route path="/nurses" element={<Nurses />} />
        <Route path="/departments" element={<Departments />} />
        <Route path="/stations" element={<Stations />} />
        <Route path="/drugs" element={<Drugs />} />
      </Routes>
    </BrowserRouter>
  )
}