import request from '@/utils/request'

export function list(data) {
  return request({
    url: 'api/agent',
    method: 'get',
    data
  })
}

export default { list }
