import request from './request'

// 分页查询老人档案
export function getElderProfiles(params) {
  return request({
    url: '/doctor/elder/profiles',
    method: 'get',
    params,
  })
}

// 查询老人档案详情
export function getElderProfileDetail(id) {
  return request({
    url: `/doctor/elder/profiles/${id}`,
    method: 'get',
  })
}

// 新增老人档案
export function addElderProfile(data) {
  return request({
    url: '/doctor/elder/profiles',
    method: 'post',
    data,
  })
}

// 修改老人档案
export function updateElderProfile(id, data) {
  return request({
    url: `/doctor/elder/profiles/${id}`,
    method: 'put',
    data,
  })
}

// 删除老人档案
export function deleteElderProfile(id) {
  return request({
    url: `/doctor/elder/profiles/${id}`,
    method: 'delete',
  })
}
