import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/msUpdate',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/msUpdate/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/msUpdate',
    method: 'put',
    data
  })
}

export function reset(ids) {
  return request({
    url: 'api/msUpdate/reset',
    method: 'post',
    data: ids
  })
}

export default { add, edit, del, reset }
