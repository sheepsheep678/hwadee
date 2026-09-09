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
            style="width: 150px"
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
            style="width: 130px"
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

    <el-card shadow="never">
      <el-table v-loading="loading" :data="tableData" style="width: 100%">
        <el-table-column prop="deviceSn" label="设备编号" width="150" />

        <el-table-column prop="deviceName" label="设备名称" width="150" />

        <el-table-column label="设备类型" width="120">
          <template #default="{ row }">
            {{ deviceTypeText(row.deviceType) }}
          </template>
        </el-table-column>

        <el-table-column prop="model" label="型号" width="120" />

        <el-table-column prop="brand" label="品牌" width="120" />

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

    <el-dialog v-model="detailVisible" title="设备详情" width="560px">
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

.search-card {
  margin-bottom: 20px;
}

.pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}
</style>
