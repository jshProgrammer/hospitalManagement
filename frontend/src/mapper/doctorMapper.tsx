import type { Doctor, DoctorApi } from '../types/Doctor.tsx'

export function mapDoctor(doctor: DoctorApi): Doctor {
  return {
    id: doctor.id,
    employeeId: doctor.employee.id,
    personId: doctor.employee.person.id,
    gender: doctor.employee.person.gender,
    firstName: doctor.employee.person.firstName,
    lastName: doctor.employee.person.lastName,
    plz: doctor.employee.person.plz,
    city: doctor.employee.person.city,
    street: doctor.employee.person.street,
    streetNo: doctor.employee.person.streetNo,
    country: doctor.employee.person.country,
    birthday: doctor.employee.person.birthday,
    phone: doctor.employee.person.phone,
    email: doctor.employee.person.email,
    department: doctor.employee.department,
    workPhone: doctor.workPhone,
    type: doctor.type,
  }
}