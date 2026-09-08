<script setup>
import { reactive, ref, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

import {
  getElderProfiles,
  addElderProfile,
  updateElderProfile,
  deleteElderProfile,
} from '@/api/elder'

const USE_MOCK = true

const queryForm = reactive({
  name: '',
  idCard: '',
  phone: '',
  livingType: '',
})

const pageNum = ref(1)
const pageSize = ref(10)

const dialogVisible = ref(false)
const dialogTitle = ref('新增老人档案')

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

const tableData = ref([
  {
    id: 1,
    elderNo: 'ELD20260001',
    name: '张建国',
    gender: 1,
    birthDate: '1952-03-18',
    idCard: '510***********1234',
    phone: '13800138001',
    livingType: 1,
    address: '成都市成华区',
    medicareNo: 'YB20260001',
    status: 1,
  },
  {
    id: 2,
    elderNo: 'ELD20260002',
    name: '李桂芳',
    gender: 2,
    birthDate: '1948-07-26',
    idCard: '510***********5678',
    phone: '13800138002',
    livingType: 4,
    address: '成都市锦江区',
    medicareNo: 'YB20260002',
    status: 1,
  },
  {
    id: 3,
    elderNo: 'ELD20260003',
    name: '王国强',
    gender: 1,
    birthDate: '1956-11-09',
    idCard: '510***********9012',
    phone: '13800138003',
    livingType: 3,
    address: '成都市武侯区',
    medicareNo: 'YB20260003',
    status: 1,
  },
])

const filteredData = computed(() => {
  return tableData.value.filter((item) => {
    const matchName = !queryForm.name || item.name.includes(queryForm.name)

    const matchIdCard = !queryForm.idCard || item.idCard.includes(queryForm.idCard)

    const matchPhone = !queryForm.phone || item.phone.includes(queryForm.phone)

    const matchLiving = !queryForm.livingType || item.livingType === Number(queryForm.livingType)

    return matchName && matchIdCard && matchPhone && matchLiving
  })
})

const loadData = async () => {
  if (USE_MOCK) {
    return
  }

  try {
    const result = await getElderProfiles({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      name: queryForm.name,
      idCard: queryForm.idCard,
      phone: queryForm.phone,
      livingType: queryForm.livingType,
    })

    tableData.value = result.data.list || []
  } catch (error) {
    console.error('查询老人档案失败：', error)
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
    if (USE_MOCK) {
      if (form.id) {
        const index = tableData.value.findIndex((item) => item.id === form.id)

        if (index !== -1) {
          tableData.value[index] = {
            ...form,
          }
        }

        ElMessage.success('修改成功')
      } else {
        tableData.value.push({
          ...form,
          id: Date.now(),
          elderNo: `ELD${Date.now()}`,
        })

        ElMessage.success('新增成功')
      }
    } else {
      if (form.id) {
        await updateElderProfile(form.id, form)
        ElMessage.success('修改成功')
      } else {
        await addElderProfile(form)
        ElMessage.success('新增成功')
      }

      await loadData()
    }

    dialogVisible.value = false
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

    if (USE_MOCK) {
      tableData.value = tableData.value.filter((item) => item.id !== row.id)
    } else {
      await deleteElderProfile(row.id)
      await loadData()
    }

    ElMessage.success('删除成功')
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除老人档案失败：', error)
    }
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
</script>

<template>
  <div class="profile-page">
    <div class="page-header">
      <div>
        <h2>老人档案管理</h2>
        <p>查询和维护老人基本档案信息</p>
      </div>

      <el-button type="primary" @click="handleAdd"> 新增档案 </el-button>
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
            style="width: 130px"
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
      <el-table :data="filteredData" style="width: 100%">
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

        <el-table-column prop="address" label="居住地址" min-width="180" />

        <el-table-column label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '在管' : '非在管' }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
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
          :total="filteredData.length"
        />
      </div>
    </el-card>

    <!-- 新增 / 编辑 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="650px">
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
  </div>
</template>

<style scoped>
.profile-page {
  width: 100%;
}

.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
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

.table-card {
  margin-bottom: 20px;
}

.pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}
</style>
