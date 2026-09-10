<script setup>
import { reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

import {
  getElderProfiles,
  getElderProfileDetail,
  addElderProfile,
  updateElderProfile,
  deleteElderProfile,
  exportElderProfiles,
} from '@/api/elder'

const queryForm = reactive({
  name: '',
  idCard: '',
  phone: '',
  livingType: '',
})

const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const loading = ref(false)

const dialogVisible = ref(false)
const dialogTitle = ref('新增老人档案')

const detailVisible = ref(false)
const currentDetail = ref({})

const form = reactive({
  id: null,
  elderNo: '',
  name: '',
  gender: 1,
  birthDate: '',
  idCard: '',
  phone: '',
  livingType: 1,
  address: '',
  medicareNo: '',
  status: 1,
})

const tableData = ref([])

const loadData = async () => {
  loading.value = true

  try {
    const result = await getElderProfiles({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      name: queryForm.name || undefined,
      idCard: queryForm.idCard || undefined,
      phone: queryForm.phone || undefined,
      livingType: queryForm.livingType || undefined,
    })

    tableData.value = result.data.list || []
    total.value = result.data.total || 0
  } catch (error) {
    console.error('查询老人档案失败：', error)
  } finally {
    loading.value = false
  }
}

const livingTypeText = (type) => {
  const map = {
    1: '居家',
    2: '社区',
    3: '机构',
    4: '独居',
  }

  return map[type] || '-'
}

const genderText = (gender) => {
  return gender === 1 ? '男' : '女'
}

const recordTypeText = (type) => {
  const map = {
    1: '既往史',
    2: '过敏史',
    3: '用药史',
    4: '手术史',
    5: '家族史',
  }

  return map[type] || '-'
}

const resetForm = () => {
  Object.assign(form, {
    id: null,
    elderNo: '',
    name: '',
    gender: 1,
    birthDate: '',
    idCard: '',
    phone: '',
    livingType: 1,
    address: '',
    medicareNo: '',
    status: 1,
  })
}

const handleAdd = () => {
  resetForm()
  dialogTitle.value = '新增老人档案'
  dialogVisible.value = true
}

const handleEdit = (row) => {
  Object.assign(form, row)
  dialogTitle.value = '编辑老人档案'
  dialogVisible.value = true
}

const handleSave = async () => {
  if (!form.name) {
    ElMessage.warning('请输入老人姓名')
    return
  }

  if (!form.idCard) {
    ElMessage.warning('请输入身份证号')
    return
  }

  if (!form.phone) {
    ElMessage.warning('请输入联系电话')
    return
  }

  try {
    if (form.id) {
      await updateElderProfile(form.id, form)
      ElMessage.success('修改成功')
    } else {
      await addElderProfile(form)
      ElMessage.success('新增成功')
    }

    dialogVisible.value = false
    await loadData()
  } catch (error) {
    console.error('保存老人档案失败：', error)
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定删除老人“${row.name}”的档案吗？`, '删除确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })

    await deleteElderProfile(row.id)
    ElMessage.success('删除成功')
    await loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除老人档案失败：', error)
    }
  }
}

const handleDetail = async (row) => {
  try {
    const result = await getElderProfileDetail(row.id)
    currentDetail.value = result.data || {}
    detailVisible.value = true
  } catch (error) {
    console.error('获取老人档案详情失败：', error)
  }
}

const handleExport = async () => {
  try {
    const result = await exportElderProfiles({
      name: queryForm.name || undefined,
      idCard: queryForm.idCard || undefined,
      phone: queryForm.phone || undefined,
      livingType: queryForm.livingType || undefined,
    })

    const url = result.data

    if (url) {
      window.open(url, '_blank')
      ElMessage.success('导出成功')
    } else {
      ElMessage.success('导出任务已提交')
    }
  } catch (error) {
    console.error('导出老人档案失败：', error)
  }
}

const handleSearch = () => {
  pageNum.value = 1
  loadData()
}

const handleReset = () => {
  queryForm.name = ''
  queryForm.idCard = ''
  queryForm.phone = ''
  queryForm.livingType = ''

  pageNum.value = 1
  loadData()
}

const handlePageChange = () => {
  loadData()
}

loadData()
</script>

<template>
  <div class="profile-page">
    <div class="page-header">
      <div>
        <h2>老人档案管理</h2>
        <p>查询和维护老人基本档案信息</p>
      </div>

      <div class="header-actions">
        <el-button @click="handleExport"> 批量导出 </el-button>
        <el-button type="primary" @click="handleAdd"> 新增档案 </el-button>
      </div>
    </div>

    <!-- 查询区域 -->
    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="queryForm">
        <el-form-item label="姓名">
          <el-input v-model="queryForm.name" placeholder="请输入姓名" clearable />
        </el-form-item>

        <el-form-item label="身份证号">
          <el-input v-model="queryForm.idCard" placeholder="请输入身份证号" clearable />
        </el-form-item>

        <el-form-item label="联系电话">
          <el-input v-model="queryForm.phone" placeholder="请输入联系电话" clearable />
        </el-form-item>

        <el-form-item label="居住方式">
          <el-select
            v-model="queryForm.livingType"
            placeholder="全部"
            clearable
            class="living-select"
          >
            <el-option label="居家" :value="1" />
            <el-option label="社区" :value="2" />
            <el-option label="机构" :value="3" />
            <el-option label="独居" :value="4" />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSearch"> 查询 </el-button>

          <el-button @click="handleReset"> 重置 </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格 -->
    <el-card class="table-card" shadow="never">
      <el-table v-loading="loading" :data="tableData" style="width: 100%" empty-text="暂无记录">
        <el-table-column prop="elderNo" label="老人编号" width="150" />

        <el-table-column prop="name" label="姓名" width="100" />

        <el-table-column label="性别" width="80">
          <template #default="{ row }">
            {{ genderText(row.gender) }}
          </template>
        </el-table-column>

        <el-table-column prop="birthDate" label="出生日期" width="120" />

        <el-table-column prop="phone" label="联系电话" width="140" />

        <el-table-column label="居住方式" width="100">
          <template #default="{ row }">
            {{ livingTypeText(row.livingType) }}
          </template>
        </el-table-column>

        <el-table-column prop="address" label="居住地址" min-width="180" show-overflow-tooltip />

        <el-table-column label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '在管' : '非在管' }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleDetail(row)"> 详情 </el-button>

            <el-button link type="primary" @click="handleEdit(row)"> 编辑 </el-button>

            <el-button link type="danger" @click="handleDelete(row)"> 删除 </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="pageNum"
          v-model:page-size="pageSize"
          background
          layout="total, prev, pager, next"
          :total="total"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>

    <!-- 新增 / 编辑 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="650px"
      style="max-width: 92vw"
    >
      <el-form :model="form" label-width="90px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="姓名">
              <el-input v-model="form.name" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="性别">
              <el-radio-group v-model="form.gender">
                <el-radio :value="1">男</el-radio>
                <el-radio :value="2">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="出生日期">
              <el-date-picker
                v-model="form.birthDate"
                type="date"
                value-format="YYYY-MM-DD"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="联系电话">
              <el-input v-model="form.phone" />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="身份证号">
              <el-input v-model="form.idCard" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="居住方式">
              <el-select v-model="form.livingType" style="width: 100%">
                <el-option label="居家" :value="1" />
                <el-option label="社区" :value="2" />
                <el-option label="机构" :value="3" />
                <el-option label="独居" :value="4" />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="医保号">
              <el-input v-model="form.medicareNo" />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="居住地址">
              <el-input v-model="form.address" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false"> 取消 </el-button>

        <el-button type="primary" @click="handleSave"> 保存 </el-button>
      </template>
    </el-dialog>

    <!-- 详情 -->
    <el-dialog v-model="detailVisible" title="老人档案详情" width="760px" style="max-width: 92vw">
      <el-descriptions :column="3" border>
        <el-descriptions-item label="老人编号">
          {{ currentDetail.elderNo }}
        </el-descriptions-item>

        <el-descriptions-item label="姓名">
          {{ currentDetail.name }}
        </el-descriptions-item>

        <el-descriptions-item label="性别">
          {{ genderText(currentDetail.gender) }}
        </el-descriptions-item>

        <el-descriptions-item label="出生日期">
          {{ currentDetail.birthDate }}
        </el-descriptions-item>

        <el-descriptions-item label="联系电话">
          {{ currentDetail.phone }}
        </el-descriptions-item>

        <el-descriptions-item label="居住方式">
          {{ livingTypeText(currentDetail.livingType) }}
        </el-descriptions-item>

        <el-descriptions-item label="身份证号" :span="2">
          {{ currentDetail.idCard }}
        </el-descriptions-item>

        <el-descriptions-item label="医保号">
          {{ currentDetail.medicareNo || '-' }}
        </el-descriptions-item>

        <el-descriptions-item label="居住地址" :span="3">
          {{ currentDetail.address || '-' }}
        </el-descriptions-item>

        <el-descriptions-item label="重点标签" :span="3">
          <el-tag
            v-for="(tag, index) in currentDetail.tags || []"
            :key="index"
            class="tag-item"
            type="warning"
          >
            {{ tag }}
          </el-tag>
          <span v-if="!currentDetail.tags || currentDetail.tags.length === 0">-</span>
        </el-descriptions-item>
      </el-descriptions>

      <div class="sub-title">健康档案</div>

      <el-table
        :data="currentDetail.healthRecords || []"
        style="width: 100%"
        empty-text="暂无记录"
      >
        <el-table-column label="记录类型" width="100">
          <template #default="{ row }">
            {{ recordTypeText(row.recordType) }}
          </template>
        </el-table-column>

        <el-table-column prop="diseaseName" label="疾病 / 事项" width="140" />

        <el-table-column prop="hospital" label="就诊医院" min-width="160" show-overflow-tooltip />

        <el-table-column prop="diagnoseDate" label="确诊日期" width="110" />

        <el-table-column prop="medication" label="当前用药" width="130" />
      </el-table>

      <div class="sub-title">家属联系人</div>

      <el-table
        :data="currentDetail.familyContacts || []"
        style="width: 100%"
        empty-text="暂无记录"
      >
        <el-table-column prop="name" label="姓名" width="110" />

        <el-table-column prop="relation" label="关系" width="110" />

        <el-table-column prop="phone" label="联系电话" width="150" />

        <el-table-column label="紧急联系人" width="120">
          <template #default="{ row }">
            <el-tag :type="row.isPrimary === 1 ? 'danger' : 'info'">
              {{ row.isPrimary === 1 ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="address" label="地址" min-width="160" />
      </el-table>
    </el-dialog>
  </div>
</template>

<style scoped>
.profile-page {
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

.header-actions {
  display: flex;
  gap: 12px;
}

/* 主按钮统一医生端主色（仅本页 scoped 生效） */
.profile-page :deep(.el-button--primary) {
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

.living-select {
  width: 130px;
}

/* ===== 表格 ===== */
.table-card {
  margin-bottom: 20px;
}

.profile-page :deep(.el-table) {
  border-radius: 12px;
  overflow: hidden;
}

.profile-page :deep(.el-table th.el-table__cell) {
  background: rgba(15, 118, 110, 0.06);
  color: #1f3b4d;
  font-weight: 600;
}

.profile-page :deep(.el-table td.el-table__cell) {
  padding: 12px 0;
}

.profile-page :deep(.el-table__body tr:hover > td.el-table__cell) {
  background: rgba(15, 118, 110, 0.045);
}

.profile-page :deep(.el-table .el-button.is-link) {
  font-size: 13px;
}

.profile-page :deep(.el-table .el-button.is-link + .el-button.is-link) {
  margin-left: 12px;
}

.pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}

.profile-page :deep(.el-pagination.is-background .el-pager li.is-active) {
  background-color: #0f766e;
}

/* ===== 详情弹窗 ===== */
.sub-title {
  position: relative;
  margin: 22px 0 12px;
  padding-left: 12px;
  font-size: 16px;
  font-weight: 700;
  color: #1f3b4d;
}

.sub-title::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 16px;
  border-radius: 2px;
  background: #0f766e;
}

.profile-page :deep(.el-descriptions__label.el-descriptions__cell.is-bordered-label) {
  background: rgba(15, 118, 110, 0.05);
  color: #35566a;
  font-weight: 500;
}

.profile-page :deep(.el-descriptions__content.el-descriptions__cell.is-bordered-content) {
  color: #1f3b4d;
}

.tag-item {
  margin-right: 8px;
  margin-bottom: 4px;
}

/* ===== 响应式：小于 900px ===== */
@media (max-width: 900px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    padding: 18px;
  }

  .header-actions {
    width: 100%;
    flex-wrap: wrap;
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

  .living-select {
    width: 100%;
  }

  .pagination {
    justify-content: center;
  }
}
</style>
