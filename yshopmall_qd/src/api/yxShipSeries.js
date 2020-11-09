import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/yxShipSeries',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/yxShipSeries/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/yxShipSeries',
    method: 'put',
    data
  })
}

export function changeStatus(id) {
  return request({
    url: 'api/yxShipSeries/changeStatus/' + id,
    method: 'post'
  })
}
export default { add, edit, del }
