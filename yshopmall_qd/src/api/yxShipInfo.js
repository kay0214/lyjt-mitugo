import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/yxShipInfo',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/yxShipInfo/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/yxShipInfo',
    method: 'put',
    data
  })
}

export function changeStatus(id) {
  return request({
    url: 'api/yxShipInfo/changeStatus/' + id,
    method: 'post'
  })
}

export default { add, edit, del }
