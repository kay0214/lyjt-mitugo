import request from '@/utils/request'
import qs from 'qs'

export function add(data) {
  return request({
    url: 'api/fundsDetail',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/fundsDetail/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/fundsDetail',
    method: 'put',
    data
  })
}

export function post(params) {
  return request({
    url: 'api/yxUserBillAll' + '?' + qs.stringify(params, { indices: false }),
    method: 'post'
  })
}
export default { add, edit, del }
