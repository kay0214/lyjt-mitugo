import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/yxStoreInfo',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/yxStoreInfo/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/yxStoreInfo/updateStoreInfo',
    method: 'put',
    data
  })
}
export function onsale(id, data) {
  return request({
    url: 'api/yxStoreInfo/changeStatus/' + id,
    method: 'post',
    data
  })
}
export default { add, edit, del }
