import request from '@/utils/request'

//   获取名称配置
export function getNameConfigData() {
  return request({
    url: 'api/getIndexSystemGroupData',
    method: 'post',
    data: {}
  })
}
//   更新名称配置
export function updateNameConfigData(data) {
  return request({
    url: 'api/saveIndexSystemGroupData',
    method: 'post',
    data
  })
}
