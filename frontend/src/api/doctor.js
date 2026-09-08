import request from './request'

// 医生登录
export function doctorLogin(data) {
  return request({
    url: '/doctor/auth/login',
    method: 'post',
    data,
  })
}

// 医生退出登录
export function doctorLogout() {
  return request({
    url: '/doctor/auth/logout',
    method: 'post',
  })
}

// 获取医生个人信息
export function getDoctorProfile() {
  return request({
    url: '/doctor/profile/me',
    method: 'get',
  })
}
