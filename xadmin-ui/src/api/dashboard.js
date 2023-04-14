import request from '@/utils/request'

export function status(data) {
  return request({
    url: 'api/dashboard',
    method: 'get',
    data
  })
}

export default { status }
