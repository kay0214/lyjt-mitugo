import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/yxCommissionRate',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/yxCommissionRate/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/yxCommissionRate',
    method: 'put',
    data
  })
}

export function get() {
  return request({
    url: 'api/yxCommissionRate',
    method: 'get'
  })
}

export function updateRate(data) {
  return request({
    url: 'api/updateRate',
    method: 'post',
    data
  })
}

export function getExtractSet() {
  return request({
    url: 'api/getExtractSet',
    method: 'post'
  })
}

export function updateExtractSet(data) {
  return request({
    url: 'api/updateExtractSet',
    method: 'post',
    data
  })
}

export default { add, edit, del }
