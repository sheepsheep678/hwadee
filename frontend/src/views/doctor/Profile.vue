<script setup>
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'

import {
  getDoctorProfile,
  updateDoctorProfile,
  changeDoctorPassword,
  getDoctorSchedules,
  getDoctorServiceRecords,
} from '@/api/profile'

const USE_MOCK = true

const activeTab = ref('info')

const profileForm = reactive({
  id: 30001,
  name: 'Violet',
  phone: '13800138000',
  doctorType: 1,
  title: '主治医师',
  dept: '全科',
  orgId: 1,
})

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: '',
})

const scheduleMonth = ref('2026-09')

const scheduleData = ref([
  {
    id: 1,
    scheduleDate: '2026-09-08',
    timeSlot: 1,
    scheduleType: 1,
    capacity: 20,
    bookedCount: 12,
    status: 1,
  },
  {
    id: 2,
    scheduleDate: '2026-09-09',
    timeSlot: 2,
    scheduleType: 2,
    capacity: 10,
    bookedCount: 6,
    status: 1,
  },
])

const serviceData = ref([
  {
    id: 1,
    elderName: '张建国',
    serviceType: 1,
    serviceItems: '健康巡诊',
    serviceDate: '2026-09-05 09:30',
    duration: 40,
    status: 2,
    rating: 5,
  },
  {
    id: 2,
    elderName: '李桂芳',
    serviceType: 3,
    serviceItems: '远程问诊',
    serviceDate: '2026-09-06 15:00',
    duration: 25,
    status: 2,
    rating: 4,
  },
])

const loadProfile = async () => {
  if (USE_MOCK) {
    return
  }

  try {
    const result = await getDoctorProfile()
    Object.assign(profileForm, result.data || {})
  } catch (error) {
    console.error('获取个人信息失败：', error)
  }
}

const saveProfile = async () => {
  if (!profileForm.name) {
    ElMessage.warning('请输入姓名')
    return
  }

  if (!profileForm.phone) {
    ElMessage.warning('请输入手机号')
    return
  }

  try {
    if (USE_MOCK) {
      localStorage.setItem(
        'userInfo',
        JSON.stringify({
          ...JSON.parse(localStorage.getItem('userInfo') || '{}'),
          realName: profileForm.name,
          dept: profileForm.dept,
          title: profileForm.title,
        }),
      )

      ElMessage.success('个人信息修改成功')
      return
    }

    await updateDoctorProfile(profileForm)
    ElMessage.success('个人信息修改成功')
  } catch (error) {
    console.error('修改个人信息失败：', error)
  }
}

const submitPassword = async () => {
  if (!passwordForm.oldPassword) {
    ElMessage.warning('请输入原密码')
    return
  }

  if (!passwordForm.newPassword) {
    ElMessage.warning('请输入新密码')
    return
  }

  if (passwordForm.newPassword !== passwordForm.confirmPassword) {
    ElMessage.warning('两次输入的新密码不一致')
    return
  }

  try {
    if (!USE_MOCK) {
      await changeDoctorPassword({
        oldPassword: passwordForm.oldPassword,
        newPassword: passwordForm.newPassword,
      })
    }

    passwordForm.oldPassword = ''
    passwordForm.newPassword = ''
    passwordForm.confirmPassword = ''

    ElMessage.success('密码修改成功')
  } catch (error) {
    console.error('修改密码失败：', error)
  }
}

const loadSchedules = async () => {
  if (USE_MOCK) {
    return
  }

  try {
    const result = await getDoctorSchedules({
      month: scheduleMonth.value,
    })

    scheduleData.value = result.data || []
  } catch (error) {
    console.error('获取排班失败：', error)
  }
}

const loadServiceRecords = async () => {
  if (USE_MOCK) {
    return
  }

  try {
    const result = await getDoctorServiceRecords({
      pageNum: 1,
      pageSize: 10,
    })

    serviceData.value = result.data.list || []
  } catch (error) {
    console.error('获取服务记录失败：', error)
  }
}

const doctorTypeText = (type) => {
  const map = {
    1: '家庭医生',
    2: '专科医生',
    3: '康复师',
    4: '护理师',
  }

  return map[type] || '-'
}

const timeSlotText = (type) => {
  const map = {
    1: '上午',
    2: '下午',
    3: '夜间',
  }

  return map[type] || '-'
}

const scheduleTypeText = (type) => {
  const map = {
    1: '门诊',
    2: '上门',
    3: '值班',
  }

  return map[type] || '-'
}

const serviceTypeText = (type) => {
  const map = {
    1: '居家上门',
    2: '机构服务',
    3: '远程问诊',
  }

  return map[type] || '-'
}

loadProfile()
loadSchedules()
loadServiceRecords()
</script>

<template>
  <div class="profile-page">
    <div class="page-header">
      <div>
        <h2>个人中心</h2>
        <p>管理个人资料、密码、排班及服务记录</p>
      </div>
    </div>

    <el-tabs v-model="activeTab" class="profile-tabs">
      <!-- 基本信息 -->
      <el-tab-pane label="基本信息" name="info">
        <el-card shadow="never">
          <el-form :model="profileForm" label-width="100px" style="max-width: 700px">
            <el-form-item label="姓名">
              <el-input v-model="profileForm.name" />
            </el-form-item>

            <el-form-item label="手机号">
              <el-input v-model="profileForm.phone" />
            </el-form-item>

            <el-form-item label="医生类型">
              <el-input :model-value="doctorTypeText(profileForm.doctorType)" disabled />
            </el-form-item>

            <el-form-item label="职称">
              <el-input v-model="profileForm.title" />
            </el-form-item>

            <el-form-item label="科室">
              <el-input v-model="profileForm.dept" />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="saveProfile"> 保存修改 </el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-tab-pane>

      <!-- 修改密码 -->
      <el-tab-pane label="修改密码" name="password">
        <el-card shadow="never">
          <el-form :model="passwordForm" label-width="100px" style="max-width: 600px">
            <el-form-item label="原密码">
              <el-input v-model="passwordForm.oldPassword" type="password" show-password />
            </el-form-item>

            <el-form-item label="新密码">
              <el-input v-model="passwordForm.newPassword" type="password" show-password />
            </el-form-item>

            <el-form-item label="确认密码">
              <el-input v-model="passwordForm.confirmPassword" type="password" show-password />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="submitPassword"> 修改密码 </el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-tab-pane>

      <!-- 我的排班 -->
      <el-tab-pane label="我的排班" name="schedule">
        <el-card shadow="never">
          <div class="toolbar">
            <el-date-picker
              v-model="scheduleMonth"
              type="month"
              value-format="YYYY-MM"
              placeholder="选择月份"
            />

            <el-button type="primary" @click="loadSchedules"> 查询 </el-button>
          </div>

          <el-table :data="scheduleData" style="width: 100%">
            <el-table-column prop="scheduleDate" label="排班日期" width="150" />

            <el-table-column label="时间段" width="120">
              <template #default="{ row }">
                {{ timeSlotText(row.timeSlot) }}
              </template>
            </el-table-column>

            <el-table-column label="排班类型" width="120">
              <template #default="{ row }">
                {{ scheduleTypeText(row.scheduleType) }}
              </template>
            </el-table-column>

            <el-table-column prop="capacity" label="可接待人数" width="130" />

            <el-table-column prop="bookedCount" label="已预约" width="120" />

            <el-table-column label="状态" width="120">
              <template #default="{ row }">
                <el-tag :type="row.status === 1 ? 'success' : 'info'">
                  {{ row.status === 1 ? '正常' : '已取消' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>

      <!-- 服务记录 -->
      <el-tab-pane label="服务记录" name="service">
        <el-card shadow="never">
          <el-table :data="serviceData" style="width: 100%">
            <el-table-column prop="elderName" label="老人" width="120" />

            <el-table-column label="服务类型" width="140">
              <template #default="{ row }">
                {{ serviceTypeText(row.serviceType) }}
              </template>
            </el-table-column>

            <el-table-column prop="serviceItems" label="服务项目" min-width="160" />

            <el-table-column prop="serviceDate" label="服务时间" width="180" />

            <el-table-column prop="duration" label="时长（分钟）" width="130" />

            <el-table-column label="状态" width="120">
              <template #default="{ row }">
                <el-tag :type="row.status === 2 ? 'success' : 'warning'">
                  {{ row.status === 2 ? '已完成' : '待服务' }}
                </el-tag>
              </template>
            </el-table-column>

            <el-table-column prop="rating" label="评价" width="100" />
          </el-table>
        </el-card>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<style scoped>
.profile-page {
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

.profile-tabs {
  background: #ffffff;
  padding: 18px 22px;
  border-radius: 8px;
}

.toolbar {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
}
</style>
