import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/xadminDeployConfig',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/xadminDeployConfig/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/xadminDeployConfig',
    method: 'put',
    data
  })
}
export function get(params) {
  return request({
    url: 'api/xadminDeployConfig',
    method: 'get',
    params: params
  })
}

export default { add, edit, del, get }
