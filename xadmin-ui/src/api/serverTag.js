import request from '@/utils/request'

export function list(data) {
  return request({
    url: 'api/serverTag',
    method: 'get',
    data
  })
}
export function all(data) {
  return request({
    url: 'api/serverTag/all',
    method: 'get',
    data
  })
}

export function add(data) {
  return request({
    url: 'api/serverTag',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/serverTag/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/serverTag',
    method: 'put',
    data
  })
}

export default { list, add, edit, del, all }
