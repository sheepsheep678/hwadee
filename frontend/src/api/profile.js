import request from './request'

// 获取医生个人信息
export function getDoctorProfile() {
  return request({
    url: '/doctor/profile/me',
    method: 'get',
  })
}

// 修改医生个人信息
export function updateDoctorProfile(data) {
  return request({
    url: '/doctor/profile/me',
    method: 'put',
    data,
  })
}

// 修改密码
export function changeDoctorPassword(data) {
  return request({
    url: '/doctor/profile/password',
    method: 'put',
    data,
  })
}

// 获取医生排班
export function getDoctorSchedules(params) {
  return request({
    url: '/doctor/profile/schedules',
    method: 'get',
    params,
  })
}

// 获取医生服务记录
export function getDoctorServiceRecords(params) {
  return request({
    url: '/doctor/profile/service-records',
    method: 'get',
    params,
  })
}
