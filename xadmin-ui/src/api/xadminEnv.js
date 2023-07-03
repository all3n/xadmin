import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/xadminEnv',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/xadminEnv/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/xadminEnv',
    method: 'put',
    data
  })
}
export function get(params) {
  return request({
    url: 'api/xadminEnv',
    method: 'get',
    params: params
  })
}

export default { add, edit, del, get }
