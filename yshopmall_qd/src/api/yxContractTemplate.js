import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/yxContractTemplate',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/yxContractTemplate/',
    method: 'delete',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/yxContractTemplate',
    method: 'put',
    data
  })
}

export function getContractTemp() {
  return request({
    url: 'api/yxContractTemplate/getContractTemp',
    method: 'get'
  })
}

export default { add, edit, del }
