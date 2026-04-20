import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { Patients } from './pages/Patients.tsx'
import { Doctors } from './pages/Doctors.tsx'
import { Nurses } from './pages/Nurses.tsx'
import { Departments } from './pages/Departments.tsx'
import { Stations } from './pages/Stations.tsx'
import { Drugs } from './pages/Drugs.tsx'
import AppLayout from './components/AppLayout.tsx'
import Test from './Test.tsx'

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<AppLayout />}>
          <Route path="/" element={<Navigate to="/patients" replace />} />
          <Route path="/patients" element={<Patients />} />
          <Route path="/doctors" element={<Doctors />} />
          <Route path="/nurses" element={<Nurses />} />
          <Route path="/departments" element={<Departments />} />
          <Route path="/stations" element={<Stations />} />
          <Route path="/drugs" element={<Drugs />} />
          <Route path="/test" element={<Test />} />
        </Route>
      </Routes>
    </BrowserRouter>
  )
}