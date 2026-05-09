export type StationApi = {
  id: number
  name: string
  department: {
    id: number
    name: string
    building: string
  }
}

export type StationPage = {
  content: StationApi[]
  totalPages: number
  totalElements: number
  numberOfElements: number
  size: number
}

export type Station = {
  id: number
  name: string
  departmentId: number
  departmentName: string
  building: string
}