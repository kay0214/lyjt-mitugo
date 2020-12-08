import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/yxCouponsPriceConfig',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/yxCouponsPriceConfig/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/yxCouponsPriceConfig',
    method: 'put',
    data
  })
}

export function setPriceConfig(data) {
  return request({
    url: 'api/yxCouponsPriceConfig/setPriceConfig/',
    method: 'post',
    data
  })
}

export function getPriceConfigList(id) {
  return request({
    url: 'api/yxCouponsPriceConfig/getPriceConfigList/' + id,
    method: 'get'
  })
}

export function delAllPriceConfig(id) {
  return request({
    url: 'api/yxCouponsPriceConfig/delAllPriceConfig/' + id,
    method: 'get'
  })
}
export default { add, edit, del }
