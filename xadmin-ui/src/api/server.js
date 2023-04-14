import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/server',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/server/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/server',
    method: 'put',
    data
  })
}
export function batchAdd(data) {
  return request({
    url: 'api/server/batchAdd',
    method: 'post',
    data
  })
}
export function list(data) {
  return request({
    url: 'api/server',
    method: 'get',
    data
  })
}
export function all(data) {
  return request({
    url: 'api/server/all',
    method: 'get',
    data
  })
}
export default { add, edit, del, batchAdd, list, all }
