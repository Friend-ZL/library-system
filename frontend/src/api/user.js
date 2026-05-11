import request from './request'

export const getUsers = (params) => request.get('/users', { params })
export const createUser = (data) => request.post('/users', data)
export const changeUserStatus = (id, status) => request.put(`/users/${id}/status`, null, { params: { status } })
