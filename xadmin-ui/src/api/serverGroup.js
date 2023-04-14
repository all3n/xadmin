import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/serverGroup',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/serverGroup/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/serverGroup',
    method: 'put',
    data
  })
}
export function updateGroup(data) {
  return request({
    url: 'api/serverGroup/updateGroup',
    method: 'post',
    data
  })
}
export function updateGroupText(data) {
  return request({
    url: 'api/serverGroup/updateGroupText',
    method: 'post',
    data
  })
}
export function get(groupId) {
  return request({
    url: 'api/serverGroup/hosts?groupId=' + groupId,
    method: 'get'
  })
}
export function search(query) {
  return request({
    url: 'api/serverGroup/search?query=' + query,
    method: 'get'
  })
}

export default { get, add, edit, del, updateGroup, updateGroupText, search }
