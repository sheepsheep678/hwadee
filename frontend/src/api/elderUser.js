import request from './request'

// 老人登录
export function elderLogin(data) {
  return request({
    url: '/elder/auth/login',
    method: 'post',
    data,
  })
}

// 老人退出登录
export function elderLogout() {
  return request({
    url: '/elder/auth/logout',
    method: 'post',
  })
}

// 获取我的档案
export function getMyProfile() {
  return request({
    url: '/elder/profile',
    method: 'get',
  })
}

// 修改我的档案
export function updateMyProfile(data) {
  return request({
    url: '/elder/profile',
    method: 'put',
    data,
  })
}

// 获取我的设备
export function getMyDevices() {
  return request({
    url: '/elder/devices',
    method: 'get',
  })
}

// 获取账户信息
export function getMyAccount() {
  return request({
    url: '/elder/profile/account',
    method: 'get',
  })
}

// 修改密码
export function changeElderPassword(data) {
  return request({
    url: '/elder/profile/password',
    method: 'put',
    data,
  })
}
// 获取消息列表
export function getElderMessages(params) {
  return request({
    url: '/elder/profile/messages',
    method: 'get',
    params,
  })
}

// 标记消息已读
export function readElderMessage(id) {
  return request({
    url: `/elder/profile/messages/${id}/read`,
    method: 'put',
  })
}

// 获取未读消息数量
export function getUnreadMessageCount() {
  return request({
    url: '/elder/profile/messages/unread-count',
    method: 'get',
  })
}
