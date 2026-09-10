<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

import { getMyProfile, getMyDevices, getElderMessages, getUnreadMessageCount } from '@/api/elderUser'

const router = useRouter()

const realName = computed(() => {
  const raw = localStorage.getItem('elderUserInfo')

  if (!raw) {
    return ''
  }

  try {
    return JSON.parse(raw).realName || ''
  } catch {
    return ''
  }
})

const cards = [
  {
    title: '我的档案',
    description: '查看个人健康及基本档案',
    path: '/elder/profile',
  },
  {
    title: '我的设备',
    description: '查看已绑定的健康设备',
    path: '/elder/devices',
  },
  {
    title: '个人中心',
    description: '管理账户信息与密码',
    path: '/elder/personal-center',
  },
]

const loading = ref(false)
const healthRecordCount = ref(0)
const historyCount = ref(0)
const allergyCount = ref(0)
const medicationCount = ref(0)
const surgeryCount = ref(0)
const familyHistoryCount = ref(0)
const latestHealthRecord = ref(null)
const deviceCount = ref(0)
const deviceOnlineCount = ref(0)
const unreadCount = ref(0)
const latestMessages = ref([])

const recordTypeText = (type) => {
  const map = {
    1: '既往史',
    2: '过敏史',
    3: '用药史',
    4: '手术史',
    5: '家族史',
  }

  return map[type] || '其他'
}

const toRecordSortKey = (record) => {
  const value = record?.createTime || record?.diagnoseDate || ''

  if (Array.isArray(value)) {
    return value.map((item) => String(item).padStart(2, '0')).join('-')
  }

  return String(value)
}

const summarizeHealthRecords = (records) => {
  const list = Array.isArray(records) ? records : []

  healthRecordCount.value = list.length
  historyCount.value = list.filter((item) => item.recordType === 1).length
  allergyCount.value = list.filter((item) => item.recordType === 2).length
  medicationCount.value = list.filter((item) => item.recordType === 3).length
  surgeryCount.value = list.filter((item) => item.recordType === 4).length
  familyHistoryCount.value = list.filter((item) => item.recordType === 5).length

  const sorted = [...list].sort((a, b) => toRecordSortKey(b).localeCompare(toRecordSortKey(a)))
  latestHealthRecord.value = sorted.length ? sorted[0] : null
}

const getElderId = () => {
  const raw = localStorage.getItem('elderUserInfo')

  if (!raw) {
    return null
  }

  try {
    return JSON.parse(raw).elderId || null
  } catch {
    return null
  }
}

const goPersonalCenter = () => {
  router.push('/elder/personal-center')
}

const loadHomeData = async () => {
  loading.value = true

  const elderId = getElderId()

  if (elderId) {
    try {
      const profile = await getMyProfile(elderId)
      summarizeHealthRecords(profile.data?.healthRecords)
    } catch (error) {
      console.error('首页-健康档案加载失败：', error)
    }

    try {
      const devices = await getMyDevices(elderId)
      const list = devices.data || []
      deviceCount.value = list.length
      deviceOnlineCount.value = list.filter((item) => item.onlineStatus === 1).length
    } catch (error) {
      console.error('首页-设备加载失败：', error)
    }
  } else {
    ElMessage.warning('未能获取老人档案信息，请重新登录')
  }

  try {
    const unread = await getUnreadMessageCount()
    unreadCount.value = unread.data || 0
  } catch (error) {
    console.error('首页-未读消息加载失败：', error)
  }

  try {
    const messages = await getElderMessages({ pageNum: 1, pageSize: 3 })
    latestMessages.value = messages.data?.list || []
  } catch (error) {
    console.error('首页-最近消息加载失败：', error)
  }

  loading.value = false
}

onMounted(loadHomeData)
</script>

<template>
  <div class="home-page">
    <div class="welcome">
      <div class="welcome-text">
        <h2>{{ realName ? `${realName}，您好，欢迎回来` : '您好，欢迎回来' }}</h2>
        <p>这里可以查看您的健康档案和设备信息</p>
      </div>

      <div class="welcome-badge">老人服务端</div>
    </div>

    <el-row :gutter="20" class="stat-row" v-loading="loading">
      <el-col :xs="12" :sm="6">
        <el-card shadow="hover" class="stat-card" @click="router.push('/elder/profile')">
          <div class="stat-icon">档</div>
          <div class="stat-value">{{ healthRecordCount }}</div>
          <div class="stat-label">健康档案</div>
        </el-card>
      </el-col>

      <el-col :xs="12" :sm="6">
        <el-card shadow="hover" class="stat-card" @click="router.push('/elder/devices')">
          <div class="stat-icon">设</div>
          <div class="stat-value">{{ deviceOnlineCount }}/{{ deviceCount }}</div>
          <div class="stat-label">在线设备 / 设备总数</div>
        </el-card>
      </el-col>

      <el-col :xs="12" :sm="6">
        <el-card shadow="hover" class="stat-card" @click="goPersonalCenter">
          <div class="stat-icon">信</div>
          <div class="stat-value">{{ unreadCount }}</div>
          <div class="stat-label">未读消息</div>
        </el-card>
      </el-col>

      <el-col :xs="12" :sm="6">
        <el-card shadow="hover" class="stat-card" @click="goPersonalCenter">
          <div class="stat-icon">讯</div>
          <div class="stat-value">{{ latestMessages.length }}</div>
          <div class="stat-label">最近消息</div>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" class="health-card">
      <template #header>
        <span class="card-title">健康档案摘要</span>
      </template>

      <div class="health-counts">
        <div class="health-count-item">
          <div class="health-count-value">{{ healthRecordCount }}</div>
          <div class="health-count-label">档案总数</div>
        </div>

        <div class="health-count-item">
          <div class="health-count-value">{{ historyCount }}</div>
          <div class="health-count-label">既往史</div>
        </div>

        <div class="health-count-item">
          <div class="health-count-value">{{ allergyCount }}</div>
          <div class="health-count-label">过敏史</div>
        </div>

        <div class="health-count-item">
          <div class="health-count-value">{{ medicationCount }}</div>
          <div class="health-count-label">用药史</div>
        </div>

        <div class="health-count-item">
          <div class="health-count-value">{{ surgeryCount }}</div>
          <div class="health-count-label">手术史</div>
        </div>

        <div class="health-count-item">
          <div class="health-count-value">{{ familyHistoryCount }}</div>
          <div class="health-count-label">家族史</div>
        </div>
      </div>

      <el-divider />

      <div v-if="latestHealthRecord">
        <div class="latest-record-title">最近健康记录</div>

        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="疾病名称">
            {{ latestHealthRecord.diseaseName || '-' }}
          </el-descriptions-item>

          <el-descriptions-item label="就诊医院">
            {{ latestHealthRecord.hospital || '-' }}
          </el-descriptions-item>

          <el-descriptions-item label="诊断日期">
            {{ latestHealthRecord.diagnoseDate || '-' }}
          </el-descriptions-item>

          <el-descriptions-item label="记录类型">
            {{ recordTypeText(latestHealthRecord.recordType) }}
          </el-descriptions-item>

          <el-descriptions-item label="详细信息" :span="2">
            {{ latestHealthRecord.detail || '-' }}
          </el-descriptions-item>
        </el-descriptions>
      </div>

      <el-empty v-else description="暂无健康档案记录" :image-size="60" />
    </el-card>

    <el-card shadow="never" class="message-card" v-if="latestMessages.length">
      <template #header>
        <span class="card-title">最近消息</span>
      </template>

      <div
        v-for="message in latestMessages"
        :key="message.id"
        class="message-item"
        @click="goPersonalCenter"
      >
        <div class="message-main">
          <span class="message-title">
            <span v-if="message.isRead === 0" class="dot">●</span>
            {{ message.title }}
          </span>

          <p class="message-content">{{ message.content }}</p>
        </div>

        <span class="message-time">{{ message.createTime }}</span>
      </div>
    </el-card>

    <el-row :gutter="20" class="nav-row">
      <el-col v-for="item in cards" :key="item.path" :span="8">
        <el-card shadow="hover" class="card" @click="router.push(item.path)">
          <div class="nav-icon">{{ item.title.charAt(0) }}</div>

          <h3>{{ item.title }}</h3>
          <p>{{ item.description }}</p>
          <div class="enter">进入 →</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.home-page {
  width: 100%;
}

/* ===== 欢迎区 ===== */
.welcome {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 24px;
  padding: 22px 26px;
  border: 1px solid rgba(226, 232, 240, 0.9);
  border-radius: 16px;
  background: linear-gradient(135deg, rgba(47, 158, 110, 0.1), rgba(103, 194, 58, 0.08));
}

.welcome h2 {
  margin: 0;
  font-size: 22px;
  font-weight: 700;
  color: #1f3b4d;
}

.welcome p {
  margin: 8px 0 0;
  font-size: 14px;
  color: #5b7b8a;
}

.welcome-badge {
  flex: none;
  padding: 6px 14px;
  border: 1px solid rgba(47, 158, 110, 0.2);
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.8);
  font-size: 13px;
  color: #2f9e6e;
}

/* ===== 统计卡 ===== */
.stat-row {
  margin-bottom: 20px;
}

.stat-card {
  position: relative;
  min-height: 128px;
  cursor: pointer;
  transition: transform 0.25s, box-shadow 0.25s;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 14px 30px rgba(47, 158, 110, 0.14);
}

.stat-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  margin-bottom: 10px;
  border-radius: 12px;
  background: var(--accent-soft, rgba(47, 158, 110, 0.12));
  font-size: 15px;
  font-weight: 700;
  color: var(--accent, #2f9e6e);
}

.stat-value {
  font-size: 24px;
  font-weight: 700;
  line-height: 1.2;
  color: var(--accent, #2f9e6e);
}

.stat-label {
  margin-top: 4px;
  font-size: 13px;
  color: #7b8a97;
}

.stat-row .el-col:nth-child(1) .stat-card {
  --accent: #2f9e6e;
  --accent-soft: rgba(47, 158, 110, 0.12);
}

.stat-row .el-col:nth-child(2) .stat-card {
  --accent: #3fa66a;
  --accent-soft: rgba(63, 166, 106, 0.14);
}

.stat-row .el-col:nth-child(3) .stat-card {
  --accent: #2b7fd4;
  --accent-soft: rgba(43, 127, 212, 0.12);
}

.stat-row .el-col:nth-child(4) .stat-card {
  --accent: #0ea5a4;
  --accent-soft: rgba(14, 165, 164, 0.14);
}

/* ===== 健康档案摘要 ===== */
.health-card {
  margin-bottom: 20px;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: #1f3b4d;
}

.health-counts {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.health-count-item {
  flex: 1 1 120px;
  padding: 14px 12px;
  text-align: center;
  background: rgba(47, 158, 110, 0.06);
  border: 1px solid rgba(47, 158, 110, 0.08);
  border-radius: 12px;
}

.health-counts .health-count-item:nth-child(1) {
  --accent: #2f9e6e;
}

.health-counts .health-count-item:nth-child(2) {
  --accent: #2b7fd4;
}

.health-counts .health-count-item:nth-child(3) {
  --accent: #f0a020;
}

.health-counts .health-count-item:nth-child(4) {
  --accent: #0ea5a4;
}

.health-counts .health-count-item:nth-child(5) {
  --accent: #8b5cf6;
}

.health-counts .health-count-item:nth-child(6) {
  --accent: #64748b;
}

.health-count-value {
  font-size: 20px;
  font-weight: 700;
  color: var(--accent, #2f9e6e);
}

.health-count-label {
  margin-top: 6px;
  font-size: 13px;
  color: #7b8a97;
}

.latest-record-title {
  position: relative;
  margin-bottom: 12px;
  padding-left: 12px;
  font-weight: 600;
  color: #1f3b4d;
}

.latest-record-title::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 3px;
  height: 16px;
  border-radius: 2px;
  background: #2f9e6e;
}

/* ===== 最近消息 ===== */
.message-card {
  margin-bottom: 20px;
}

.message-item {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 16px;
  padding: 12px 14px;
  margin-bottom: 8px;
  border-radius: 12px;
  background: rgba(47, 158, 110, 0.04);
  cursor: pointer;
  transition: background 0.2s, transform 0.2s;
}

.message-item:last-child {
  margin-bottom: 0;
}

.message-item:hover {
  background: rgba(47, 158, 110, 0.1);
  transform: translateX(2px);
}

.message-main {
  min-width: 0;
}

.message-title {
  color: #1f3b4d;
  font-weight: 500;
}

.message-content {
  margin: 6px 0 0;
  font-size: 13px;
  line-height: 1.6;
  color: #7b8a97;
}

.dot {
  margin-right: 4px;
  color: #f0a020;
  font-size: 12px;
}

.message-time {
  flex: none;
  font-size: 12px;
  color: #9db2bd;
}

/* ===== 快捷入口 ===== */
.nav-row .card {
  min-height: 150px;
  cursor: pointer;
  transition: transform 0.25s, box-shadow 0.25s;
}

.nav-row .card:hover {
  transform: translateY(-4px);
  box-shadow: 0 14px 30px rgba(47, 158, 110, 0.14);
}

.nav-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  margin-bottom: 12px;
  border-radius: 14px;
  background: var(--accent-soft, rgba(47, 158, 110, 0.12));
  font-size: 16px;
  font-weight: 700;
  color: var(--accent, #2f9e6e);
}

.nav-row .card h3 {
  margin: 0 0 10px;
  font-size: 18px;
  font-weight: 600;
  color: #1f3b4d;
}

.nav-row .card p {
  margin: 0;
  font-size: 14px;
  color: #7b8a97;
}

.nav-row .card .enter {
  margin-top: 20px;
  font-size: 14px;
  font-weight: 500;
  color: var(--accent, #3fa66a);
}

.nav-row .el-col:nth-child(1) .card {
  --accent: #2f9e6e;
  --accent-soft: rgba(47, 158, 110, 0.12);
}

.nav-row .el-col:nth-child(2) .card {
  --accent: #3fa66a;
  --accent-soft: rgba(63, 166, 106, 0.14);
}

.nav-row .el-col:nth-child(3) .card {
  --accent: #2b7fd4;
  --accent-soft: rgba(43, 127, 212, 0.12);
}

@media (max-width: 900px) {
  .welcome {
    padding: 18px;
  }

  .welcome h2 {
    font-size: 20px;
  }

  .stat-card {
    min-height: 116px;
  }

  .health-count-item {
    flex: 1 1 45%;
  }
}
</style>
