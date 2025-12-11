export interface PaginatedResponse<T> {
  pagination: Pagination
  data: T[]
}

export interface Pagination {
  hasPrevious: boolean
  totalPages: number
  totalItems: number
  pageSize: number
  hasNext: boolean
  currentPage: number
}