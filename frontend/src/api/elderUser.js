import request from './request'

// 老人登录
export function elderLogin(data) {
  return request({
    url: '/elder/auth/login',
    method: 'post',
    data,
  })
}

// 老人注册
export function elderRegister(data) {
  return request({
    url: '/elder/auth/register',
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

// 获取我的档案（含健康档案、家属联系人、标签）
export function getMyProfile(id) {
  return request({
    url: '/elder/profile',
    method: 'get',
    params: { id },
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

// 获取我的设备（需传老人档案ID）
export function getMyDevices(elderId) {
  return request({
    url: '/elder/devices',
    method: 'get',
    params: { elderId },
  })
}

// 获取账户信息
export function getMyAccount() {
  return request({
    url: '/elder/center/account',
    method: 'get',
  })
}

// 修改密码
export function changeElderPassword(data) {
  return request({
    url: '/elder/center/password',
    method: 'put',
    data,
  })
}

// 获取消息列表
export function getElderMessages(params) {
  return request({
    url: '/elder/center/messages',
    method: 'get',
    params,
  })
}

// 标记消息已读
export function readElderMessage(id) {
  return request({
    url: `/elder/center/messages/${id}/read`,
    method: 'put',
  })
}

// 全部消息标记已读
export function markAllElderMessagesRead() {
  return request({
    url: '/elder/center/messages/read-all',
    method: 'put',
  })
}

// 获取未读消息数量
export function getUnreadMessageCount() {
  return request({
    url: '/elder/center/messages/unread-count',
    method: 'get',
  })
}
