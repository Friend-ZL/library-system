import request from './request'

export const applyBorrow = (bookId) => request.post('/borrows/apply', { bookId })
export const getBorrows = (params) => request.get('/borrows', { params })
export const getMyBorrows = (params) => request.get('/borrows/my', { params })
export const approveBorrow = (id) => request.put(`/borrows/${id}/approve`)
export const rejectBorrow = (id) => request.put(`/borrows/${id}/reject`)
export const returnBorrow = (id) => request.put(`/borrows/${id}/return`)
