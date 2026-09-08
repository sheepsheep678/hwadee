<script setup>
import { ref } from 'vue'

import { getMyDevices } from '@/api/elderUser'

const USE_MOCK = true

const devices = ref([
  {
    id: 1,
    deviceSn: 'DEV20260001',
    deviceName: '智能健康手环',
    deviceType: 2,
    model: 'HB-2026A',
    brand: '华康',
    onlineStatus: 1,
    batteryLevel: 86,
    status: 1,
  },
  {
    id: 2,
    deviceSn: 'DEV20260002',
    deviceName: '智能床垫',
    deviceType: 1,
    model: 'SM-100',
    brand: '安养',
    onlineStatus: 1,
    batteryLevel: 100,
    status: 1,
  },
  {
    id: 3,
    deviceSn: 'DEV20260003',
    deviceName: '紧急呼叫器',
    deviceType: 3,
    model: 'SOS-X1',
    brand: '康护',
    onlineStatus: 0,
    batteryLevel: 23,
    status: 1,
  },
])

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
  if (USE_MOCK) {
    return
  }

  try {
    const result = await getMyDevices()
    devices.value = result.data || []
  } catch (error) {
    console.error('获取老人设备失败：', error)
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

    <el-row :gutter="20">
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

            <el-progress :percentage="device.batteryLevel" :stroke-width="10" />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-empty v-if="devices.length === 0" description="暂无绑定设备" />
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
