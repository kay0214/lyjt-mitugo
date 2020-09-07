import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/yxCouponOrder',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/yxCouponOrder/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/yxCouponOrder',
    method: 'put',
    data
  })
}

export function refund(data) {
  return request({
    url: 'api/yxCouponOrder/yxStoreOrder/refund',
    method: 'post',
    data
  })
}

export default { add, edit, del }
