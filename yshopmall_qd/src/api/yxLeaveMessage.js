import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/yxLeaveMessage',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/yxLeaveMessage/',
    method: 'delete',
    data: ids[0]
  })
}

export function edit(data) {
  return request({
    url: 'api/yxLeaveMessage',
    method: 'put',
    data
  })
}

export default { add, edit, del }
