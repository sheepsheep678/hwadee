<script setup>
import { reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

import {
  getAssessmentTemplates,
  submitAssessment,
  getAssessmentReports,
  getAssessmentReportDetail,
  deleteAssessmentReport,
} from '@/api/assessment'

const activeTab = ref('reports')

// ===== 报告列表 =====
const reportQuery = reactive({
  elderId: '',
  assessType: '',
})

const reportPageNum = ref(1)
const reportPageSize = ref(10)
const reportTotal = ref(0)
const reportLoading = ref(false)
const reports = ref([])

const detailVisible = ref(false)
const currentReport = ref({})

// ===== 发起评估 =====
const templates = ref([])
const submitForm = reactive({
  elderId: null,
  assessType: 1,
  templateId: null,
  assessDate: '',
  answers: [],
})

const submitLoading = ref(false)

const assessTypeText = (type) => {
  const map = {
    1: '老年能力',
    2: '健康风险',
    3: '中医体质',
    4: '膳食营养',
  }

  return map[type] || '-'
}

const loadReports = async () => {
  reportLoading.value = true

  try {
    const result = await getAssessmentReports({
      pageNum: reportPageNum.value,
      pageSize: reportPageSize.value,
      elderId: reportQuery.elderId || undefined,
      assessType: reportQuery.assessType || undefined,
    })

    reports.value = result.data.list || []
    reportTotal.value = result.data.total || 0
  } catch (error) {
    console.error('查询评估报告失败：', error)
  } finally {
    reportLoading.value = false
  }
}

const loadTemplates = async () => {
  try {
    const result = await getAssessmentTemplates({
      assessType: submitForm.assessType,
    })

    templates.value = result.data || []
  } catch (error) {
    console.error('获取评估模板失败：', error)
  }
}

const handleAssessTypeChange = () => {
  submitForm.templateId = null
  loadTemplates()
}

const handleReportSearch = () => {
  reportPageNum.value = 1
  loadReports()
}

const handleReportReset = () => {
  reportQuery.elderId = ''
  reportQuery.assessType = ''

  reportPageNum.value = 1
  loadReports()
}

const handleReportDetail = async (row) => {
  try {
    const result = await getAssessmentReportDetail(row.id)
    currentReport.value = result.data || {}
    detailVisible.value = true
  } catch (error) {
    console.error('获取评估报告详情失败：', error)
  }
}

const handleReportDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定删除报告“${row.reportNo}”吗？`, '删除确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })

    await deleteAssessmentReport(row.id)
    ElMessage.success('删除成功')
    await loadReports()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除评估报告失败：', error)
    }
  }
}

const addAnswerRow = () => {
  submitForm.answers.push({ itemId: null, answer: '', score: null })
}

const removeAnswerRow = (index) => {
  submitForm.answers.splice(index, 1)
}

const handleSubmit = async () => {
  if (!submitForm.elderId) {
    ElMessage.warning('请输入老人ID')
    return
  }

  if (!submitForm.templateId) {
    ElMessage.warning('请选择评估模板')
    return
  }

  if (!submitForm.assessDate) {
    ElMessage.warning('请选择评估日期')
    return
  }

  if (!submitForm.answers || submitForm.answers.length === 0) {
    ElMessage.warning('请至少填写一项评估答案')
    return
  }

  submitLoading.value = true

  try {
    await submitAssessment({
      elderId: submitForm.elderId,
      templateId: submitForm.templateId,
      assessType: submitForm.assessType,
      assessDate: submitForm.assessDate,
      answers: submitForm.answers.map((item) => ({
        itemId: item.itemId,
        answer: item.answer,
        score: item.score,
      })),
    })

    ElMessage.success('评估提交成功')
    submitForm.answers = []
    submitForm.elderId = null
    submitForm.templateId = null
    submitForm.assessDate = ''

    activeTab.value = 'reports'
    await loadReports()
  } catch (error) {
    console.error('提交评估失败：', error)
  } finally {
    submitLoading.value = false
  }
}

const handleReportPageChange = () => {
  loadReports()
}

loadReports()
loadTemplates()
</script>

<template>
  <div class="assessment-page">
    <div class="page-header">
      <div>
        <h2>评估管理</h2>
        <p>开展老人能力 / 健康风险 / 中医体质 / 膳食营养评估</p>
      </div>
    </div>

    <el-tabs v-model="activeTab" class="assessment-tabs">
      <!-- 评估报告 -->
      <el-tab-pane label="评估报告" name="reports">
        <el-card class="search-card" shadow="never">
          <el-form :inline="true" :model="reportQuery">
            <el-form-item label="老人ID">
              <el-input
                v-model="reportQuery.elderId"
                placeholder="请输入老人档案ID（数字）"
                clearable
              />
            </el-form-item>

            <el-form-item label="评估类型">
              <el-select v-model="reportQuery.assessType" placeholder="全部" clearable class="type-select">
                <el-option label="老年能力" :value="1" />
                <el-option label="健康风险" :value="2" />
                <el-option label="中医体质" :value="3" />
                <el-option label="膳食营养" :value="4" />
              </el-select>
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="handleReportSearch"> 查询 </el-button>
              <el-button @click="handleReportReset"> 重置 </el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card class="table-card" shadow="never">
          <el-table
            v-loading="reportLoading"
            :data="reports"
            style="width: 100%"
            empty-text="暂无评估报告"
          >
            <el-table-column prop="reportNo" label="报告编号" width="200" show-overflow-tooltip />

            <el-table-column prop="elderId" label="老人ID" width="100" />

            <el-table-column label="评估类型" width="120">
              <template #default="{ row }">
                {{ assessTypeText(row.assessType) }}
              </template>
            </el-table-column>

            <el-table-column prop="score" label="总分" width="100" />

            <el-table-column prop="grade" label="等级" width="120">
              <template #default="{ row }">
                <el-tag type="warning">{{ row.grade }}</el-tag>
              </template>
            </el-table-column>

            <el-table-column prop="assessDate" label="评估日期" width="120" />

            <el-table-column prop="assessorId" label="评估医生ID" width="120" />

            <el-table-column label="操作" width="140" fixed="right">
              <template #default="{ row }">
                <el-button link type="primary" @click="handleReportDetail(row)"> 详情 </el-button>
                <el-button link type="danger" @click="handleReportDelete(row)"> 删除 </el-button>
              </template>
            </el-table-column>
          </el-table>

          <div class="pagination">
            <el-pagination
              v-model:current-page="reportPageNum"
              v-model:page-size="reportPageSize"
              background
              layout="total, prev, pager, next"
              :total="reportTotal"
              @current-change="handleReportPageChange"
            />
          </div>
        </el-card>
      </el-tab-pane>

      <!-- 发起评估 -->
      <el-tab-pane label="发起评估" name="submit">
        <el-card class="submit-card" shadow="never">
          <el-form :model="submitForm" label-width="100px" style="max-width: 760px">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="老人ID">
                  <el-input
                    v-model="submitForm.elderId"
                    placeholder="请输入老人档案ID，如 300001"
                  />
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <el-form-item label="评估类型">
                  <el-select
                    v-model="submitForm.assessType"
                    style="width: 100%"
                    @change="handleAssessTypeChange"
                  >
                    <el-option label="老年能力" :value="1" />
                    <el-option label="健康风险" :value="2" />
                    <el-option label="中医体质" :value="3" />
                    <el-option label="膳食营养" :value="4" />
                  </el-select>
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <el-form-item label="评估模板">
                  <el-select v-model="submitForm.templateId" placeholder="请选择模板" style="width: 100%">
                    <el-option
                      v-for="tpl in templates"
                      :key="tpl.id"
                      :label="`${tpl.templateName}（v${tpl.version}）`"
                      :value="tpl.id"
                    />
                  </el-select>
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <el-form-item label="评估日期">
                  <el-date-picker
                    v-model="submitForm.assessDate"
                    type="date"
                    value-format="YYYY-MM-DD"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-divider content-position="left">评估答案</el-divider>

            <el-table :data="submitForm.answers" style="width: 100%" empty-text="请添加评估答案">
              <el-table-column label="题目ID" width="160">
                <template #default="{ row }">
                  <el-input v-model="row.itemId" placeholder="题目ID" />
                </template>
              </el-table-column>

              <el-table-column label="答案" min-width="200">
                <template #default="{ row }">
                  <el-input v-model="row.answer" placeholder="答案内容" />
                </template>
              </el-table-column>

              <el-table-column label="得分" width="140">
                <template #default="{ row }">
                  <el-input v-model="row.score" placeholder="得分" />
                </template>
              </el-table-column>

              <el-table-column label="操作" width="80">
                <template #default="{ $index }">
                  <el-button link type="danger" @click="removeAnswerRow($index)"> 删除 </el-button>
                </template>
              </el-table-column>
            </el-table>

            <el-button class="add-answer" @click="addAnswerRow"> 添加答案项 </el-button>

            <div class="submit-bar">
              <el-button type="primary" :loading="submitLoading" @click="handleSubmit">
                提交评估
              </el-button>
            </div>
          </el-form>
        </el-card>
      </el-tab-pane>
    </el-tabs>

    <!-- 报告详情 -->
    <el-dialog
      v-model="detailVisible"
      title="评估报告详情"
      width="620px"
      style="max-width: 92vw"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="报告编号">
          {{ currentReport.reportNo }}
        </el-descriptions-item>

        <el-descriptions-item label="评估类型">
          {{ assessTypeText(currentReport.assessType) }}
        </el-descriptions-item>

        <el-descriptions-item label="总分">
          {{ currentReport.score }}
        </el-descriptions-item>

        <el-descriptions-item label="评估等级">
          {{ currentReport.grade }}
        </el-descriptions-item>

        <el-descriptions-item label="评估日期">
          {{ currentReport.assessDate }}
        </el-descriptions-item>

        <el-descriptions-item label="评估医生ID">
          {{ currentReport.assessorId }}
        </el-descriptions-item>

        <el-descriptions-item label="评估结论" :span="2">
          {{ currentReport.conclusion || '-' }}
        </el-descriptions-item>

        <el-descriptions-item label="明细得分" :span="2">
          <pre class="json-box">{{ currentReport.resultJson || '-' }}</pre>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<style scoped>
.assessment-page {
  width: 100%;
}

/* ===== 页头 ===== */
.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 24px;
  padding: 22px 26px;
  border: 1px solid rgba(226, 232, 240, 0.9);
  border-radius: 16px;
  background: linear-gradient(135deg, rgba(15, 118, 110, 0.08), rgba(43, 127, 212, 0.08));
}

.page-header h2 {
  position: relative;
  margin: 0;
  padding-left: 14px;
  font-size: 22px;
  font-weight: 700;
  color: #1f3b4d;
}

.page-header h2::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 20px;
  border-radius: 2px;
  background: #0f766e;
}

.page-header p {
  margin: 8px 0 0;
  font-size: 14px;
  color: #5b7b8a;
}

/* ===== tabs 容器 ===== */
.assessment-tabs {
  padding: 20px 24px;
  border: 1px solid rgba(226, 232, 240, 0.9);
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.96);
}

.assessment-tabs :deep(.el-tabs__item.is-active) {
  color: #0f766e;
}

.assessment-tabs :deep(.el-tabs__active-bar) {
  background-color: #0f766e;
}

/* 主按钮统一医生端主色（仅本页 scoped 生效） */
.assessment-page :deep(.el-button--primary) {
  --el-button-bg-color: #0f766e;
  --el-button-border-color: #0f766e;
  --el-button-hover-bg-color: #0d6a63;
  --el-button-hover-border-color: #0d6a63;
  --el-button-active-bg-color: #0b5e58;
  --el-button-active-border-color: #0b5e58;
}

/* ===== 查询区 ===== */
.search-card {
  margin-bottom: 20px;
}

.search-card :deep(.el-form--inline .el-form-item) {
  margin-right: 18px;
  margin-bottom: 12px;
}

.search-card :deep(.el-input__wrapper),
.search-card :deep(.el-select__wrapper) {
  border-radius: 10px;
}

.type-select {
  width: 150px;
}

/* ===== 表格（报告列表 / 答案明细） ===== */
.table-card,
.submit-card {
  margin-bottom: 20px;
}

.assessment-page :deep(.el-table) {
  border-radius: 12px;
  overflow: hidden;
}

.assessment-page :deep(.el-table th.el-table__cell) {
  background: rgba(15, 118, 110, 0.06);
  color: #1f3b4d;
  font-weight: 600;
}

.assessment-page :deep(.el-table td.el-table__cell) {
  padding: 12px 0;
}

.assessment-page :deep(.el-table__body tr:hover > td.el-table__cell) {
  background: rgba(15, 118, 110, 0.045);
}

.assessment-page :deep(.el-table .el-button.is-link) {
  font-size: 13px;
}

.assessment-page :deep(.el-table .el-button.is-link + .el-button.is-link) {
  margin-left: 12px;
}

/* 答案表格内嵌输入框 */
.assessment-page :deep(.el-table .el-input__wrapper) {
  border-radius: 8px;
}

/* ===== 分割线（评估答案标题） ===== */
.assessment-page :deep(.el-divider__text) {
  font-size: 15px;
  font-weight: 600;
  color: #0f766e;
}

/* ===== 添加答案 / 提交区 ===== */
.add-answer {
  width: 100%;
  margin-top: 12px;
  border: 1px dashed rgba(15, 118, 110, 0.4);
  border-radius: 10px;
  color: #0f766e;
}

.add-answer:hover,
.add-answer:focus {
  border-color: #0f766e;
  color: #0f766e;
  background: rgba(15, 118, 110, 0.05);
}

.submit-bar {
  margin-top: 20px;
  padding-top: 16px;
  border-top: 1px solid rgba(226, 232, 240, 0.8);
  text-align: right;
}

/* ===== 分页 ===== */
.pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}

.assessment-page :deep(.el-pagination.is-background .el-pager li.is-active) {
  background-color: #0f766e;
}

/* ===== 详情弹窗 ===== */
.assessment-page :deep(.el-descriptions__label.el-descriptions__cell.is-bordered-label) {
  background: rgba(15, 118, 110, 0.05);
  color: #35566a;
  font-weight: 500;
}

.assessment-page :deep(.el-descriptions__content.el-descriptions__cell.is-bordered-content) {
  color: #1f3b4d;
}

.json-box {
  margin: 0;
  max-height: 240px;
  padding: 10px 12px;
  overflow: auto;
  border-radius: 8px;
  background: rgba(15, 118, 110, 0.04);
  font-family: Consolas, Monaco, monospace;
  font-size: 13px;
  color: #35566a;
  white-space: pre-wrap;
  word-break: break-all;
}

/* ===== 响应式：小于 900px ===== */
@media (max-width: 900px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    padding: 18px;
  }

  .assessment-tabs {
    padding: 16px;
  }

  .search-card :deep(.el-form--inline .el-form-item) {
    width: 100%;
    margin-right: 0;
  }

  .search-card :deep(.el-form--inline .el-form-item .el-form-item__content) {
    width: 100%;
  }

  .search-card :deep(.el-input),
  .search-card :deep(.el-select) {
    width: 100%;
  }

  .type-select {
    width: 100%;
  }

  .submit-card :deep(.el-form .el-col) {
    flex: 0 0 100%;
    max-width: 100%;
  }

  .pagination {
    justify-content: center;
  }
}
</style>
