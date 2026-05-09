export type DrugApi = {
  id: number
  stock: number
  name: string
  activeIngredient: string
  type: string
}

export type DrugPage = {
  content: DrugApi[]
  totalPages: number
  totalElements: number
  numberOfElements: number
  size: number
}

export type Drug = {
  id: number
  stock: number
  name: string
  activeIngredient: string
  type: string
}