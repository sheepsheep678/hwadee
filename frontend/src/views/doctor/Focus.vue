<script setup>
import { reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

import {
  getTags,
  unbindTag,
  getFollowUpPlans,
  addFollowUpPlan,
  updateFollowUpPlan,
  deleteFollowUpPlan,
  addFollowUpRecord,
  getFollowUpRecords,
} from '@/api/focus'

const activeTab = ref('tags')

// ===== 标签 =====
const tags = ref([])
const unbindForm = reactive({
  elderId: null,
  tagId: null,
})

const tagLevelText = (level) => {
  const map = {
    1: '一般',
    2: '重点',
    3: '紧急',
  }

  return map[level] || '-'
}

const tagLevelType = (level) => {
  const map = {
    1: 'info',
    2: 'warning',
    3: 'danger',
  }

  return map[level] || 'info'
}

const loadTags = async () => {
  try {
    const result = await getTags()
    tags.value = result.data || []
  } catch (error) {
    console.error('获取标签列表失败：', error)
  }
}

const handleUnbind = async () => {
  if (!unbindForm.elderId) {
    ElMessage.warning('请输入老人ID')
    return
  }

  if (!unbindForm.tagId) {
    ElMessage.warning('请选择标签')
    return
  }

  try {
    await unbindTag(unbindForm.elderId, unbindForm.tagId)
    ElMessage.success('标签已解绑')
  } catch (error) {
    console.error('解绑标签失败：', error)
  }
}

// ===== 随访计划 =====
const planQuery = reactive({
  elderId: '',
  status: '',
  planType: '',
})

const planPageNum = ref(1)
const planPageSize = ref(10)
const planTotal = ref(0)
const planLoading = ref(false)
const plans = ref([])

const planDialogVisible = ref(false)
const planDialogTitle = ref('新增随访计划')
const planForm = reactive({
  id: null,
  elderId: null,
  planType: 1,
  startDate: '',
  endDate: '',
  frequency: '',
  status: 1,
})

const planTypeText = (type) => {
  const map = {
    1: '电话',
    2: '上门',
    3: '远程问诊',
  }

  return map[type] || '-'
}

const planStatusText = (status) => {
  const map = {
    1: '进行中',
    2: '已完成',
    3: '已终止',
  }

  return map[status] || '-'
}

const loadPlans = async () => {
  planLoading.value = true

  try {
    const result = await getFollowUpPlans({
      pageNum: planPageNum.value,
      pageSize: planPageSize.value,
      elderId: planQuery.elderId || undefined,
      status: planQuery.status || undefined,
      planType: planQuery.planType || undefined,
    })

    plans.value = result.data.list || []
    planTotal.value = result.data.total || 0
  } catch (error) {
    console.error('查询随访计划失败：', error)
  } finally {
    planLoading.value = false
  }
}

const resetPlanForm = () => {
  Object.assign(planForm, {
    id: null,
    elderId: null,
    planType: 1,
    startDate: '',
    endDate: '',
    frequency: '',
    status: 1,
  })
}

const handlePlanAdd = () => {
  resetPlanForm()
  planDialogTitle.value = '新增随访计划'
  planDialogVisible.value = true
}

const handlePlanEdit = (row) => {
  Object.assign(planForm, row)
  planDialogTitle.value = '编辑随访计划'
  planDialogVisible.value = true
}

const handlePlanSave = async () => {
  if (!planForm.elderId) {
    ElMessage.warning('请输入老人ID')
    return
  }

  try {
    const payload = {
      elderId: planForm.elderId,
      planType: planForm.planType,
      startDate: planForm.startDate || null,
      endDate: planForm.endDate || null,
      frequency: planForm.frequency,
      status: planForm.status,
    }

    if (planForm.id) {
      await updateFollowUpPlan(planForm.id, payload)
      ElMessage.success('修改成功')
    } else {
      await addFollowUpPlan(payload)
      ElMessage.success('新增成功')
    }

    planDialogVisible.value = false
    await loadPlans()
  } catch (error) {
    console.error('保存随访计划失败：', error)
  }
}

const handlePlanDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定删除该随访计划吗？', '删除确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })

    await deleteFollowUpPlan(row.id)
    ElMessage.success('删除成功')
    await loadPlans()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除随访计划失败：', error)
    }
  }
}

const handlePlanSearch = () => {
  planPageNum.value = 1
  loadPlans()
}

const handlePlanReset = () => {
  planQuery.elderId = ''
  planQuery.status = ''
  planQuery.planType = ''

  planPageNum.value = 1
  loadPlans()
}

const handlePlanPageChange = () => {
  loadPlans()
}

// ===== 随访记录 =====
const recordQuery = reactive({
  elderId: '',
})

const recordPageNum = ref(1)
const recordPageSize = ref(10)
const recordTotal = ref(0)
const recordLoading = ref(false)
const records = ref([])

const recordDialogVisible = ref(false)
const recordForm = reactive({
  planId: null,
  elderId: null,
  followDate: '',
  method: 1,
  content: '',
  nextFollowDate: '',
})

const resetRecordForm = () => {
  Object.assign(recordForm, {
    planId: null,
    elderId: null,
    followDate: '',
    method: 1,
    content: '',
    nextFollowDate: '',
  })
}

const loadRecords = async () => {
  recordLoading.value = true

  try {
    const result = await getFollowUpRecords({
      pageNum: recordPageNum.value,
      pageSize: recordPageSize.value,
      elderId: recordQuery.elderId || undefined,
    })

    records.value = result.data.list || []
    recordTotal.value = result.data.total || 0
  } catch (error) {
    console.error('查询随访记录失败：', error)
  } finally {
    recordLoading.value = false
  }
}

const handleRecordAdd = () => {
  resetRecordForm()
  recordDialogVisible.value = true
}

const handleRecordSave = async () => {
  if (!recordForm.elderId) {
    ElMessage.warning('请输入老人ID')
    return
  }

  try {
    await addFollowUpRecord({
      planId: recordForm.planId,
      elderId: recordForm.elderId,
      followDate: recordForm.followDate || null,
      method: recordForm.method,
      content: recordForm.content,
      nextFollowDate: recordForm.nextFollowDate || null,
    })

    ElMessage.success('新增成功')
    recordDialogVisible.value = false
    await loadRecords()
  } catch (error) {
    console.error('保存随访记录失败：', error)
  }
}

const handleRecordSearch = () => {
  recordPageNum.value = 1
  loadRecords()
}

const handleRecordReset = () => {
  recordQuery.elderId = ''

  recordPageNum.value = 1
  loadRecords()
}

const handleRecordPageChange = () => {
  loadRecords()
}

loadTags()
loadPlans()
loadRecords()
</script>

<template>
  <div class="focus-page">
    <div class="page-header">
      <div>
        <h2>重点随访</h2>
        <p>重点人群标签、随访计划与随访记录管理</p>
      </div>
    </div>

    <el-tabs v-model="activeTab" class="focus-tabs">
      <!-- 重点标签 -->
      <el-tab-pane label="重点标签" name="tags">
        <el-card class="search-card" shadow="never">
          <el-form :inline="true" :model="unbindForm">
            <el-form-item label="老人ID">
              <el-input v-model="unbindForm.elderId" placeholder="老人ID" clearable />
            </el-form-item>

            <el-form-item label="解绑标签">
              <el-select v-model="unbindForm.tagId" placeholder="选择标签" clearable style="width: 180px">
                <el-option
                  v-for="tag in tags"
                  :key="tag.id"
                  :label="tag.tagName"
                  :value="tag.id"
                />
              </el-select>
            </el-form-item>

            <el-form-item>
              <el-button type="danger" @click="handleUnbind"> 解绑 </el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card shadow="never">
          <el-table :data="tags" style="width: 100%">
            <el-table-column prop="id" label="标签ID" width="100" />

            <el-table-column prop="tagName" label="标签名称" width="160" />

            <el-table-column label="标签级别" width="120">
              <template #default="{ row }">
                <el-tag :type="tagLevelType(row.tagLevel)">
                  {{ tagLevelText(row.tagLevel) }}
                </el-tag>
              </template>
            </el-table-column>

            <el-table-column prop="description" label="标签描述" min-width="220" />

            <el-table-column label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === 1 ? 'success' : 'info'">
                  {{ row.status === 1 ? '启用' : '停用' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>

      <!-- 随访计划 -->
      <el-tab-pane label="随访计划" name="plans">
        <el-card class="search-card" shadow="never">
          <el-form :inline="true" :model="planQuery">
            <el-form-item label="老人ID">
              <el-input v-model="planQuery.elderId" placeholder="请输入老人ID" clearable />
            </el-form-item>

            <el-form-item label="执行状态">
              <el-select v-model="planQuery.status" placeholder="全部" clearable style="width: 130px">
                <el-option label="进行中" :value="1" />
                <el-option label="已完成" :value="2" />
                <el-option label="已终止" :value="3" />
              </el-select>
            </el-form-item>

            <el-form-item label="计划类型">
              <el-select v-model="planQuery.planType" placeholder="全部" clearable style="width: 130px">
                <el-option label="电话" :value="1" />
                <el-option label="上门" :value="2" />
                <el-option label="远程问诊" :value="3" />
              </el-select>
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="handlePlanSearch"> 查询 </el-button>
              <el-button @click="handlePlanReset"> 重置 </el-button>
              <el-button type="primary" plain @click="handlePlanAdd"> 新增计划 </el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card shadow="never">
          <el-table v-loading="planLoading" :data="plans" style="width: 100%">
            <el-table-column prop="id" label="计划ID" width="90" />

            <el-table-column prop="elderId" label="老人ID" width="100" />

            <el-table-column label="计划类型" width="120">
              <template #default="{ row }">
                {{ planTypeText(row.planType) }}
              </template>
            </el-table-column>

            <el-table-column prop="frequency" label="随访频次" width="140" />

            <el-table-column prop="startDate" label="开始日期" width="120" />

            <el-table-column prop="endDate" label="结束日期" width="120" />

            <el-table-column label="执行状态" width="110">
              <template #default="{ row }">
                <el-tag :type="row.status === 1 ? 'success' : row.status === 2 ? 'info' : 'danger'">
                  {{ planStatusText(row.status) }}
                </el-tag>
              </template>
            </el-table-column>

            <el-table-column label="操作" width="140" fixed="right">
              <template #default="{ row }">
                <el-button link type="primary" @click="handlePlanEdit(row)"> 编辑 </el-button>
                <el-button link type="danger" @click="handlePlanDelete(row)"> 删除 </el-button>
              </template>
            </el-table-column>
          </el-table>

          <div class="pagination">
            <el-pagination
              v-model:current-page="planPageNum"
              v-model:page-size="planPageSize"
              background
              layout="total, prev, pager, next"
              :total="planTotal"
              @current-change="handlePlanPageChange"
            />
          </div>
        </el-card>
      </el-tab-pane>

      <!-- 随访记录 -->
      <el-tab-pane label="随访记录" name="records">
        <el-card class="search-card" shadow="never">
          <el-form :inline="true" :model="recordQuery">
            <el-form-item label="老人ID">
              <el-input v-model="recordQuery.elderId" placeholder="请输入老人ID" clearable />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="handleRecordSearch"> 查询 </el-button>
              <el-button @click="handleRecordReset"> 重置 </el-button>
              <el-button type="primary" plain @click="handleRecordAdd"> 新增记录 </el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card shadow="never">
          <el-table v-loading="recordLoading" :data="records" style="width: 100%">
            <el-table-column prop="id" label="记录ID" width="90" />

            <el-table-column prop="planId" label="计划ID" width="100" />

            <el-table-column prop="elderId" label="老人ID" width="100" />

            <el-table-column prop="followDate" label="随访日期" width="120" />

            <el-table-column label="随访方式" width="120">
              <template #default="{ row }">
                {{ planTypeText(row.method) }}
              </template>
            </el-table-column>

            <el-table-column prop="content" label="随访内容" min-width="220" />

            <el-table-column prop="nextFollowDate" label="下次随访" width="120" />
          </el-table>

          <div class="pagination">
            <el-pagination
              v-model:current-page="recordPageNum"
              v-model:page-size="recordPageSize"
              background
              layout="total, prev, pager, next"
              :total="recordTotal"
              @current-change="handleRecordPageChange"
            />
          </div>
        </el-card>
      </el-tab-pane>
    </el-tabs>

    <!-- 随访计划新增/编辑 -->
    <el-dialog v-model="planDialogVisible" :title="planDialogTitle" width="560px">
      <el-form :model="planForm" label-width="100px">
        <el-form-item label="老人ID">
          <el-input v-model="planForm.elderId" placeholder="请输入老人档案ID" />
        </el-form-item>

        <el-form-item label="计划类型">
          <el-select v-model="planForm.planType" style="width: 100%">
            <el-option label="电话" :value="1" />
            <el-option label="上门" :value="2" />
            <el-option label="远程问诊" :value="3" />
          </el-select>
        </el-form-item>

        <el-form-item label="开始日期">
          <el-date-picker v-model="planForm.startDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>

        <el-form-item label="结束日期">
          <el-date-picker v-model="planForm.endDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>

        <el-form-item label="随访频次">
          <el-input v-model="planForm.frequency" placeholder="如：每周1次" />
        </el-form-item>

        <el-form-item label="执行状态">
          <el-select v-model="planForm.status" style="width: 100%">
            <el-option label="进行中" :value="1" />
            <el-option label="已完成" :value="2" />
            <el-option label="已终止" :value="3" />
          </el-select>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="planDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handlePlanSave">保存</el-button>
      </template>
    </el-dialog>

    <!-- 随访记录新增 -->
    <el-dialog v-model="recordDialogVisible" title="新增随访记录" width="560px">
      <el-form :model="recordForm" label-width="110px">
        <el-form-item label="计划ID">
          <el-input v-model="recordForm.planId" placeholder="选填" />
        </el-form-item>

        <el-form-item label="老人ID">
          <el-input v-model="recordForm.elderId" placeholder="请输入老人档案ID" />
        </el-form-item>

        <el-form-item label="随访日期">
          <el-date-picker v-model="recordForm.followDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>

        <el-form-item label="随访方式">
          <el-select v-model="recordForm.method" style="width: 100%">
            <el-option label="电话" :value="1" />
            <el-option label="上门" :value="2" />
            <el-option label="远程问诊" :value="3" />
          </el-select>
        </el-form-item>

        <el-form-item label="随访内容">
          <el-input v-model="recordForm.content" type="textarea" :rows="3" />
        </el-form-item>

        <el-form-item label="下次随访">
          <el-date-picker v-model="recordForm.nextFollowDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="recordDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleRecordSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.focus-page {
  width: 100%;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  color: #303133;
}

.page-header p {
  margin: 8px 0 0;
  color: #909399;
  font-size: 14px;
}

.focus-tabs {
  background: #ffffff;
  padding: 18px 22px;
  border-radius: 8px;
}

.search-card {
  margin-bottom: 20px;
}

.pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}
</style>
