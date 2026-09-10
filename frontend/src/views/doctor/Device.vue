<script setup>
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'

import { getDoctorDevices, getDoctorDeviceDetail } from '@/api/device'

const queryForm = reactive({
  deviceName: '',
  deviceType: '',
  onlineStatus: '',
})

const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const loading = ref(false)

const detailVisible = ref(false)
const currentDevice = ref({})

const tableData = ref([])

const loadData = async () => {
  loading.value = true

  try {
    const result = await getDoctorDevices({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      deviceName: queryForm.deviceName || undefined,
      deviceType: queryForm.deviceType || undefined,
      onlineStatus: queryForm.onlineStatus === '' ? undefined : queryForm.onlineStatus,
    })

    tableData.value = result.data.list || []
    total.value = result.data.total || 0
  } catch (error) {
    console.error('查询设备失败：', error)
  } finally {
    loading.value = false
  }
}

const deviceTypeText = (type) => {
  const map = {
    1: '智能床垫',
    2: '手环',
    3: '呼叫器',
    4: '门磁',
    5: '雷达',
  }

  return map[type] || '-'
}

const statusText = (status) => {
  const map = {
    1: '正常',
    2: '维修中',
    3: '报废',
  }

  return map[status] || '-'
}

const handleSearch = () => {
  pageNum.value = 1
  loadData()
}

const handleReset = () => {
  queryForm.deviceName = ''
  queryForm.deviceType = ''
  queryForm.onlineStatus = ''

  pageNum.value = 1
  loadData()
}

const handleDetail = async (row) => {
  try {
    const result = await getDoctorDeviceDetail(row.id)

    currentDevice.value = result.data || {}
    detailVisible.value = true
  } catch (error) {
    ElMessage.error('获取设备详情失败')
    console.error(error)
  }
}

const handlePageChange = () => {
  loadData()
}

loadData()
</script>

<template>
  <div class="device-page">
    <div class="page-header">
      <div>
        <h2>设备管理</h2>
        <p>查看所辖老人绑定的设备及在线状态</p>
      </div>
    </div>

    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="queryForm">
        <el-form-item label="设备名称">
          <el-input v-model="queryForm.deviceName" placeholder="请输入设备名称" clearable />
        </el-form-item>

        <el-form-item label="设备类型">
          <el-select
            v-model="queryForm.deviceType"
            placeholder="全部"
            clearable
            class="type-select"
          >
            <el-option label="智能床垫" :value="1" />
            <el-option label="手环" :value="2" />
            <el-option label="呼叫器" :value="3" />
            <el-option label="门磁" :value="4" />
            <el-option label="雷达" :value="5" />
          </el-select>
        </el-form-item>

        <el-form-item label="在线状态">
          <el-select
            v-model="queryForm.onlineStatus"
            placeholder="全部"
            clearable
            class="status-select"
          >
            <el-option label="在线" :value="1" />
            <el-option label="离线" :value="0" />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSearch"> 查询 </el-button>

          <el-button @click="handleReset"> 重置 </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card" shadow="never">
      <el-table v-loading="loading" :data="tableData" style="width: 100%" empty-text="暂无设备">
        <el-table-column prop="deviceSn" label="设备编号" width="150" />

        <el-table-column prop="deviceName" label="设备名称" width="150" show-overflow-tooltip />

        <el-table-column label="设备类型" width="120">
          <template #default="{ row }">
            {{ deviceTypeText(row.deviceType) }}
          </template>
        </el-table-column>

        <el-table-column prop="model" label="型号" width="120" show-overflow-tooltip />

        <el-table-column prop="brand" label="品牌" width="120" show-overflow-tooltip />

        <el-table-column prop="elderName" label="绑定老人" width="120" />

        <el-table-column label="在线状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.onlineStatus === 1 ? 'success' : 'danger'">
              {{ row.onlineStatus === 1 ? '在线' : '离线' }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="电量" width="160">
          <template #default="{ row }">
            <el-progress :percentage="row.batteryLevel || 0" :stroke-width="10" />
          </template>
        </el-table-column>

        <el-table-column label="设备状态" width="100">
          <template #default="{ row }">
            {{ statusText(row.status) }}
          </template>
        </el-table-column>

        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleDetail(row)"> 查看详情 </el-button>
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

    <el-dialog v-model="detailVisible" title="设备详情" width="560px" style="max-width: 92vw">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="设备编号">
          {{ currentDevice.deviceSn }}
        </el-descriptions-item>

        <el-descriptions-item label="设备名称">
          {{ currentDevice.deviceName }}
        </el-descriptions-item>

        <el-descriptions-item label="设备类型">
          {{ deviceTypeText(currentDevice.deviceType) }}
        </el-descriptions-item>

        <el-descriptions-item label="型号">
          {{ currentDevice.model }}
        </el-descriptions-item>

        <el-descriptions-item label="品牌">
          {{ currentDevice.brand }}
        </el-descriptions-item>

        <el-descriptions-item label="绑定老人">
          {{ currentDevice.elderName || '-' }}
        </el-descriptions-item>

        <el-descriptions-item label="绑定老人电话">
          {{ currentDevice.elderPhone || '-' }}
        </el-descriptions-item>

        <el-descriptions-item label="在线状态">
          {{ currentDevice.onlineStatus === 1 ? '在线' : '离线' }}
        </el-descriptions-item>

        <el-descriptions-item label="剩余电量">
          {{ currentDevice.batteryLevel }}%
        </el-descriptions-item>

        <el-descriptions-item label="购买日期">
          {{ currentDevice.purchaseDate || '-' }}
        </el-descriptions-item>

        <el-descriptions-item label="价格">
          {{ currentDevice.price ? `¥${currentDevice.price}` : '-' }}
        </el-descriptions-item>

        <el-descriptions-item label="所属机构ID" :span="2">
          {{ currentDevice.orgId || '-' }}
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<style scoped>
.device-page {
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

/* 主按钮统一医生端主色（仅本页 scoped 生效） */
.device-page :deep(.el-button--primary) {
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

.status-select {
  width: 130px;
}

/* ===== 表格 ===== */
.table-card {
  margin-bottom: 20px;
}

.device-page :deep(.el-table) {
  border-radius: 12px;
  overflow: hidden;
}

.device-page :deep(.el-table th.el-table__cell) {
  background: rgba(15, 118, 110, 0.06);
  color: #1f3b4d;
  font-weight: 600;
}

.device-page :deep(.el-table td.el-table__cell) {
  padding: 12px 0;
}

.device-page :deep(.el-table__body tr:hover > td.el-table__cell) {
  background: rgba(15, 118, 110, 0.045);
}

.device-page :deep(.el-table .el-button.is-link) {
  font-size: 13px;
}

/* 电量进度条统一医生端主色 */
.device-page :deep(.el-progress-bar__inner) {
  background-color: #0f766e;
}

.device-page :deep(.el-progress-bar__outer) {
  background-color: rgba(15, 118, 110, 0.12);
}

/* ===== 分页 ===== */
.pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}

.device-page :deep(.el-pagination.is-background .el-pager li.is-active) {
  background-color: #0f766e;
}

/* ===== 详情弹窗 ===== */
.device-page :deep(.el-descriptions__label.el-descriptions__cell.is-bordered-label) {
  background: rgba(15, 118, 110, 0.05);
  color: #35566a;
  font-weight: 500;
}

.device-page :deep(.el-descriptions__content.el-descriptions__cell.is-bordered-content) {
  color: #1f3b4d;
}

/* ===== 响应式：小于 900px ===== */
@media (max-width: 900px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    padding: 18px;
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

  .type-select,
  .status-select {
    width: 100%;
  }

  .pagination {
    justify-content: center;
  }
}
</style>
