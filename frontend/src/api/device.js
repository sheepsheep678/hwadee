import request from './request'

// 分页查询设备
export function getDoctorDevices(params) {
  return request({
    url: '/doctor/devices',
    method: 'get',
    params,
  })
}

// 查询设备详情
export function getDoctorDeviceDetail(id) {
  return request({
    url: `/doctor/devices/${id}`,
    method: 'get',
  })
}
