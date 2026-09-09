import request from './request'

// 标签列表
export function getTags() {
  return request({
    url: '/doctor/focus/tags',
    method: 'get',
  })
}

// 取消老人标签
export function unbindTag(elderId, tagId) {
  return request({
    url: '/doctor/focus/tag-relations',
    method: 'delete',
    params: { elderId, tagId },
  })
}

// 随访计划分页查询
export function getFollowUpPlans(params) {
  return request({
    url: '/doctor/focus/follow-up/plans',
    method: 'get',
    params,
  })
}

// 新增随访计划
export function addFollowUpPlan(data) {
  return request({
    url: '/doctor/focus/follow-up/plans',
    method: 'post',
    data,
  })
}

// 修改随访计划
export function updateFollowUpPlan(id, data) {
  return request({
    url: `/doctor/focus/follow-up/plans/${id}`,
    method: 'put',
    data,
  })
}

// 删除随访计划
export function deleteFollowUpPlan(id) {
  return request({
    url: `/doctor/focus/follow-up/plans/${id}`,
    method: 'delete',
  })
}

// 新增随访记录
export function addFollowUpRecord(data) {
  return request({
    url: '/doctor/focus/follow-up/records',
    method: 'post',
    data,
  })
}

// 随访记录分页查询
export function getFollowUpRecords(params) {
  return request({
    url: '/doctor/focus/follow-up/records',
    method: 'get',
    params,
  })
}
