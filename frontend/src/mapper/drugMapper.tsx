import type { Drug, DrugApi } from '../types/Drug.tsx'

export function mapDrug(drug: DrugApi): Drug {
  return {
    id: drug.id,
    stock: drug.stock,
    name: drug.name,
    activeIngredient: drug.activeIngredient,
    type: drug.type,
  }
}