import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/users/createMerchants',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/yxMerchantsDetail/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: '/api/yxMerchantsDetail/add',
    method: 'post',
    data
  })
}

export default { add, edit, del }
