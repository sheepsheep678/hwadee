import request from './request'

// 获取评估模板列表
export function getAssessmentTemplates(params) {
  return request({
    url: '/doctor/assessment/templates',
    method: 'get',
    params,
  })
}

// 提交评估结果
export function submitAssessment(data) {
  return request({
    url: '/doctor/assessment/reports',
    method: 'post',
    data,
  })
}

// 评估报告分页查询
export function getAssessmentReports(params) {
  return request({
    url: '/doctor/assessment/reports',
    method: 'get',
    params,
  })
}

// 评估报告详情
export function getAssessmentReportDetail(id) {
  return request({
    url: `/doctor/assessment/reports/${id}`,
    method: 'get',
  })
}

// 删除评估报告
export function deleteAssessmentReport(id) {
  return request({
    url: `/doctor/assessment/reports/${id}`,
    method: 'delete',
  })
}
