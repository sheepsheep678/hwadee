<script setup>
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'

import { getMyProfile, updateMyProfile } from '@/api/elderUser'

const editMode = ref(false)
const loading = ref(false)

// 基本档案
const profile = reactive({
  id: null,
  elderNo: '',
  name: '',
  gender: 1,
  birthDate: '',
  idCard: '',
  phone: '',
  livingType: 1,
  maritalStatus: 1,
  education: '',
  address: '',
  medicareNo: '',
  govAidType: '',
  photoUrl: '',
  status: 1,
})

// 健康档案
const healthRecords = ref([])

// 家属联系人
const familyContacts = ref([])

// 重点标签
const tags = ref([])

// 可编辑表单
const editForm = reactive({
  phone: '',
  address: '',
  photoUrl: '',
})

const loadProfile = async () => {
  loading.value = true

  // 从登录信息里取老人档案ID
  let elderId = null

  const raw = localStorage.getItem('elderUserInfo')

  if (raw) {
    try {
      elderId = JSON.parse(raw).elderId
    } catch (e) {
      /* ignore */
    }
  }

  if (!elderId) {
    ElMessage.warning('未能获取老人档案信息，请重新登录')
    loading.value = false
    return
  }

  try {
    const result = await getMyProfile(elderId)

    if (result.data) {
      Object.assign(profile, result.data)

      healthRecords.value = result.data.healthRecords || []

      familyContacts.value = result.data.familyContacts || []

      tags.value = result.data.tags || []
    }
  } catch (error) {
    console.error('获取老人档案失败：', error)
  } finally {
    loading.value = false
  }
}

const genderText = (gender) => {
  return gender === 1 ? '男' : '女'
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

const maritalStatusText = (status) => {
  const map = {
    1: '已婚',
    2: '未婚',
    3: '丧偶',
    4: '离异',
  }

  return map[status] || '-'
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

const handleEdit = () => {
  editForm.phone = profile.phone
  editForm.address = profile.address
  editForm.photoUrl = profile.photoUrl

  editMode.value = true
}

const handleCancel = () => {
  editMode.value = false
}

const handleSave = async () => {
  if (!editForm.phone) {
    ElMessage.warning('请输入联系电话')
    return
  }

  try {
    await updateMyProfile({
      phone: editForm.phone,
      address: editForm.address,
      photoUrl: editForm.photoUrl,
    })

    profile.phone = editForm.phone
    profile.address = editForm.address
    profile.photoUrl = editForm.photoUrl

    ElMessage.success('档案修改成功')
    editMode.value = false
  } catch (error) {
    console.error('修改老人档案失败：', error)
  }
}

loadProfile()
</script>

<template>
  <div class="profile-page">
    <div class="page-header">
      <div>
        <h2>我的档案</h2>
        <p>查看个人基本信息、健康档案和家属联系人</p>
      </div>

      <el-button v-if="!editMode" type="primary" @click="handleEdit"> 编辑可修改信息 </el-button>
    </div>

    <!-- 基本档案 -->
    <el-card v-loading="loading" shadow="never" class="section-card">
      <template #header>
        <div class="card-title">基本档案</div>
      </template>

      <div v-if="tags.length > 0" class="tags-box">
        <el-tag v-for="(tag, index) in tags" :key="index" type="warning" class="tag-item">
          {{ tag }}
        </el-tag>
      </div>

      <div v-if="!editMode">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="老人编号">
            {{ profile.elderNo }}
          </el-descriptions-item>

          <el-descriptions-item label="姓名">
            {{ profile.name }}
          </el-descriptions-item>

          <el-descriptions-item label="性别">
            {{ genderText(profile.gender) }}
          </el-descriptions-item>

          <el-descriptions-item label="出生日期">
            {{ profile.birthDate }}
          </el-descriptions-item>

          <el-descriptions-item label="身份证号">
            {{ profile.idCard }}
          </el-descriptions-item>

          <el-descriptions-item label="联系电话">
            {{ profile.phone }}
          </el-descriptions-item>

          <el-descriptions-item label="居住方式">
            {{ livingTypeText(profile.livingType) }}
          </el-descriptions-item>

          <el-descriptions-item label="婚姻状况">
            {{ maritalStatusText(profile.maritalStatus) }}
          </el-descriptions-item>

          <el-descriptions-item label="文化程度">
            {{ profile.education || '-' }}
          </el-descriptions-item>

          <el-descriptions-item label="医保号">
            {{ profile.medicareNo || '-' }}
          </el-descriptions-item>

          <el-descriptions-item label="居住地址" :span="2">
            {{ profile.address || '-' }}
          </el-descriptions-item>

          <el-descriptions-item label="政府救助类型" :span="2">
            {{ profile.govAidType || '-' }}
          </el-descriptions-item>
        </el-descriptions>
      </div>

      <!-- 编辑区域 -->
      <div v-else>
        <el-alert
          title="为保证档案安全，姓名、身份证号、出生日期等核心信息不能自行修改，如需修改请联系医生。"
          type="info"
          :closable="false"
          show-icon
          class="edit-tip"
        />

        <el-form :model="editForm" label-width="110px" style="max-width: 700px">
          <el-form-item label="联系电话">
            <el-input v-model="editForm.phone" />
          </el-form-item>

          <el-form-item label="居住地址">
            <el-input v-model="editForm.address" type="textarea" :rows="3" />
          </el-form-item>

          <el-form-item label="照片地址">
            <el-input v-model="editForm.photoUrl" placeholder="请输入照片URL" />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="handleSave"> 保存 </el-button>

            <el-button @click="handleCancel"> 取消 </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <!-- 健康档案 -->
    <el-card shadow="never" class="section-card">
      <template #header>
        <div class="card-title">健康档案</div>
      </template>

      <el-table :data="healthRecords" style="width: 100%">
        <el-table-column label="记录类型" width="110">
          <template #default="{ row }">
            {{ recordTypeText(row.recordType) }}
          </template>
        </el-table-column>

        <el-table-column prop="diseaseName" label="疾病 / 事项" width="150" />

        <el-table-column prop="icdCode" label="ICD-10" width="100" />

        <el-table-column prop="diagnoseDate" label="确诊日期" width="120" />

        <el-table-column prop="hospital" label="就诊医院" min-width="180" />

        <el-table-column prop="medication" label="当前用药" width="150" />

        <el-table-column prop="detail" label="详情" min-width="200" />
      </el-table>
    </el-card>

    <!-- 家属联系人 -->
    <el-card shadow="never" class="section-card">
      <template #header>
        <div class="card-title">家属联系人</div>
      </template>

      <el-table :data="familyContacts" style="width: 100%">
        <el-table-column prop="name" label="姓名" width="120" />

        <el-table-column prop="relation" label="关系" width="120" />

        <el-table-column prop="phone" label="联系电话" width="150" />

        <el-table-column label="紧急联系人" width="120">
          <template #default="{ row }">
            <el-tag :type="row.isPrimary === 1 ? 'danger' : 'info'">
              {{ row.isPrimary === 1 ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="address" label="地址" min-width="180" />
      </el-table>
    </el-card>
  </div>
</template>

<style scoped>
.profile-page {
  width: 100%;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
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

.section-card {
  margin-bottom: 20px;
}

.card-title {
  font-size: 17px;
  font-weight: 600;
  color: #303133;
}

.edit-tip {
  margin-bottom: 24px;
}

.tags-box {
  margin-bottom: 16px;
}

.tag-item {
  margin-right: 8px;
}
</style>
