import type { Nurse, NurseApi } from '../types/Nurse.tsx'

export function mapNurse(nurse: NurseApi): Nurse {
  return {
    id: nurse.id,
    employeeId: nurse.employee.id,
    personId: nurse.employee.person.id,
    gender: nurse.employee.person.gender,
    firstName: nurse.employee.person.firstName,
    lastName: nurse.employee.person.lastName,
    plz: nurse.employee.person.plz,
    city: nurse.employee.person.city,
    street: nurse.employee.person.street,
    streetNo: nurse.employee.person.streetNo,
    country: nurse.employee.person.country,
    birthday: nurse.employee.person.birthday,
    phone: nurse.employee.person.phone,
    email: nurse.employee.person.email,
    department: nurse.employee.department,

    stationId: nurse.station?.id ?? undefined,
    stationName: nurse.station?.name ?? '',
    stationDepartmentId: nurse.station?.department.id ?? undefined,
    stationDepartmentName: nurse.station?.department.name ?? '',
    building: nurse.station?.department.building ?? '',
  }
}