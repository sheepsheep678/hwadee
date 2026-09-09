<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

import { getMyDevices } from '@/api/elderUser'

const devices = ref([])
const loading = ref(false)

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

const loadDevices = async () => {
  // 从登录信息里取老人档案ID
  let elderId = null

  const raw = localStorage.getItem('elderUserInfo')

  if (raw) {
    try {
      const userInfo = JSON.parse(raw)

      elderId = userInfo.elderId
    } catch (e) {
      /* ignore */
    }
  }

  if (!elderId) {
    ElMessage.warning('未能获取老人档案信息，请重新登录')
    return
  }

  loading.value = true

  try {
    const result = await getMyDevices(elderId)
    devices.value = result.data || []
  } catch (error) {
    console.error('获取老人设备失败：', error)
  } finally {
    loading.value = false
  }
}

loadDevices()
</script>

<template>
  <div class="device-page">
    <div class="page-header">
      <div>
        <h2>我的设备</h2>
        <p>查看当前绑定的健康监测设备</p>
      </div>
    </div>

    <el-row v-loading="loading" :gutter="20">
      <el-col v-for="device in devices" :key="device.id" :xs="24" :sm="12" :lg="8">
        <el-card shadow="hover" class="device-card">
          <div class="device-top">
            <div>
              <h3>{{ device.deviceName }}</h3>
              <p>{{ device.deviceSn }}</p>
            </div>

            <el-tag :type="device.onlineStatus === 1 ? 'success' : 'danger'">
              {{ device.onlineStatus === 1 ? '在线' : '离线' }}
            </el-tag>
          </div>

          <el-divider />

          <div class="device-info">
            <div class="info-row">
              <span>设备类型</span>
              <strong>
                {{ deviceTypeText(device.deviceType) }}
              </strong>
            </div>

            <div class="info-row">
              <span>品牌</span>
              <strong>{{ device.brand }}</strong>
            </div>

            <div class="info-row">
              <span>型号</span>
              <strong>{{ device.model }}</strong>
            </div>

            <div class="info-row">
              <span>设备状态</span>
              <strong>
                {{ statusText(device.status) }}
              </strong>
            </div>
          </div>

          <div class="battery-box">
            <div class="battery-title">
              <span>剩余电量</span>
              <span>{{ device.batteryLevel }}%</span>
            </div>

            <el-progress :percentage="device.batteryLevel || 0" :stroke-width="10" />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-empty v-if="!loading && devices.length === 0" description="暂无绑定设备" />
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

.device-card {
  margin-bottom: 20px;
}

.device-top {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
}

.device-top h3 {
  margin: 0;
  font-size: 18px;
  color: #303133;
}

.device-top p {
  margin: 8px 0 0;
  color: #909399;
  font-size: 13px;
}

.device-info {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.info-row {
  display: flex;
  justify-content: space-between;
}

.info-row span {
  color: #909399;
}

.info-row strong {
  color: #303133;
  font-weight: 500;
}

.battery-box {
  margin-top: 22px;
}

.battery-title {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  color: #606266;
  font-size: 14px;
}
</style>
