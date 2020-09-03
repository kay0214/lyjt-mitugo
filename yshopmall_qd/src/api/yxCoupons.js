import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/yxCoupons',
    method: 'post',
    data
  })
}

export function del(ids) {
  let idsStr = ''
  ids.map(item => {
    idsStr = idsStr ? `${idsStr},${item}` : item
  })
  return request({
    url: `api/yxCoupons/delCoupon/${idsStr}`,
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/yxCoupons',
    method: 'put',
    data
  })
}

// 设为热销
export function popular(id) {
  return request({
    url: 'api/yxCoupons/popular/' + id,
    method: 'post'
  })
}

// 上下架
export function pufOff(id) {
  return request({
    url: 'api/yxCoupons/pufOff/' + id,
    method: 'post'
  })
}
// 卡券分类
export function categoryTree() {
  return request({
    url: 'api/yxCouponsCategory/categoryTree',
    method: 'get'
  })
}

export default { add, edit, del, popular, pufOff, categoryTree }
