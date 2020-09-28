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
    url: '/api/yxMerchantsDetail/update',
    method: 'post',
    data
  })
}

export function editByManage(data) {
  return request({
    url: '/api/yxMerchantsDetail/updateByManage',
    method: 'post',
    data
  })
}

export function examine(data) {
  return request({
    url: '/api/yxMerchantsDetail/examine',
    method: 'post',
    data
  })
}

export function update(data) {
  return request({
    url: '/api/users/updateStatus',
    method: 'post',
    data
  })
}

export function modiyfMerWithdrawal(data) {
  return request({
    url: '/api/yxMerchantsDetail/modiyfMerWithdrawal',
    method: 'post',
    data
  })
}

export default { add, edit, del }
