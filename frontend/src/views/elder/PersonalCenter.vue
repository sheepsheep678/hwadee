<script setup>
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'

import {
  getMyAccount,
  changeElderPassword,
  getElderMessages,
  readElderMessage,
  markAllElderMessagesRead,
  getUnreadMessageCount,
  getMyAssessmentReports,
  getMyAssessmentReportDetail,
} from '@/api/elderUser'

const activeTab = ref('account')

const accountInfo = reactive({
  id: null,
  accountNo: '',
  bindPhone: '',
  authStatus: 1,
  accountStatus: 1,
  registerChannel: 1,
  elderId: null,
  elderNo: '',
  name: '',
  idCard: '',
  phone: '',
  photoUrl: '',
})

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: '',
})

const messages = ref([])
const unreadCount = ref(0)
const messagesLoading = ref(false)

const reports = ref([])
const reportTotal = ref(0)
const reportLoading = ref(false)
const reportPageNum = ref(1)
const reportPageSize = ref(10)
const reportAssessType = ref(null)
const detailVisible = ref(false)
const detailLoading = ref(false)
const currentReport = ref({})

const authStatusText = (status) => {
  const map = {
    0: '待审核',
    1: '已通过',
    2: '已驳回',
  }

  return map[status] || '-'
}

const accountStatusText = (status) => {
  const map = {
    1: '正常',
    2: '冻结',
    3: '注销',
  }

  return map[status] || '-'
}

const registerChannelText = (channel) => {
  const map = {
    1: '自助注册',
    2: '家属代注册',
    3: '机构录入',
  }

  return map[channel] || '-'
}

const messageTypeText = (type) => {
  const map = {
    1: '系统公告',
    2: '工单提醒',
    3: '审核提醒',
  }

  return map[type] || '其他'
}

const assessTypeText = (type) => {
  const map = {
    1: '老年能力',
    2: '健康风险',
    3: '中医体质',
    4: '膳食营养',
  }

  return map[type] || '-'
}

const loadAccount = async () => {
  try {
    const result = await getMyAccount()
    Object.assign(accountInfo, result.data || {})
  } catch (error) {
    console.error('获取账户信息失败：', error)
  }
}

const loadMessages = async () => {
  messagesLoading.value = true

  try {
    const result = await getElderMessages({
      pageNum: 1,
      pageSize: 10,
    })

    messages.value = result.data.list || []
  } catch (error) {
    console.error('获取消息失败：', error)
  } finally {
    messagesLoading.value = false
  }
}

const loadUnreadCount = async () => {
  try {
    const result = await getUnreadMessageCount()
    unreadCount.value = result.data || 0
  } catch (error) {
    console.error('获取未读消息数量失败：', error)
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

  if (passwordForm.newPassword.length < 6) {
    ElMessage.warning('新密码不能少于 6 位')
    return
  }

  if (passwordForm.newPassword !== passwordForm.confirmPassword) {
    ElMessage.warning('两次输入的新密码不一致')
    return
  }

  try {
    await changeElderPassword({
      oldPassword: passwordForm.oldPassword,
      newPassword: passwordForm.newPassword,
    })

    passwordForm.oldPassword = ''
    passwordForm.newPassword = ''
    passwordForm.confirmPassword = ''

    ElMessage.success('密码修改成功，请重新登录')
  } catch (error) {
    console.error('修改密码失败：', error)
  }
}

const handleRead = async (message) => {
  if (message.isRead === 1) {
    return
  }

  try {
    await readElderMessage(message.id)

    message.isRead = 1
    unreadCount.value = Math.max(0, unreadCount.value - 1)
    ElMessage.success('已标记为已读')
  } catch (error) {
    console.error('标记消息已读失败：', error)
  }
}

const handleReadAll = async () => {
  try {
    await markAllElderMessagesRead()

    messages.value.forEach((item) => {
      item.isRead = 1
    })

    unreadCount.value = 0
    ElMessage.success('全部消息已标记为已读')
  } catch (error) {
    console.error('标记全部已读失败：', error)
  }
}

const loadReports = async () => {
  reportLoading.value = true

  try {
    const params = {
      pageNum: reportPageNum.value,
      pageSize: reportPageSize.value,
    }

    if (reportAssessType.value !== null && reportAssessType.value !== '') {
      params.assessType = reportAssessType.value
    }

    const result = await getMyAssessmentReports(params)

    reports.value = result.data?.list || []
    reportTotal.value = result.data?.total || 0
  } catch (error) {
    console.error('获取评估报告失败：', error)
  } finally {
    reportLoading.value = false
  }
}

const handleReportTypeChange = () => {
  reportPageNum.value = 1
  loadReports()
}

const handleReportPageChange = (page) => {
  reportPageNum.value = page
  loadReports()
}

const handleReportDetail = async (row) => {
  detailVisible.value = true
  detailLoading.value = true
  currentReport.value = {}

  try {
    const result = await getMyAssessmentReportDetail(row.id)
    currentReport.value = result.data || {}
  } catch (error) {
    console.error('获取评估报告详情失败：', error)
  } finally {
    detailLoading.value = false
  }
}

loadAccount()
loadMessages()
loadUnreadCount()
loadReports()
</script>

<template>
  <div class="personal-page">
    <div class="page-header">
      <div>
        <h2>个人中心</h2>
        <p>查看账户信息、修改密码和接收服务消息</p>
      </div>
    </div>

    <el-tabs v-model="activeTab" class="personal-tabs">
      <!-- 账户信息 -->
      <el-tab-pane label="账户信息" name="account">
        <el-card shadow="never">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="账户编号">
              {{ accountInfo.accountNo }}
            </el-descriptions-item>

            <el-descriptions-item label="姓名">
              {{ accountInfo.name || '-' }}
            </el-descriptions-item>

            <el-descriptions-item label="绑定手机号">
              {{ accountInfo.bindPhone || accountInfo.phone || '-' }}
            </el-descriptions-item>

            <el-descriptions-item label="身份证号">
              {{ accountInfo.idCard || '-' }}
            </el-descriptions-item>

            <el-descriptions-item label="审核状态">
              <el-tag :type="accountInfo.authStatus === 1 ? 'success' : 'warning'">
                {{ authStatusText(accountInfo.authStatus) }}
              </el-tag>
            </el-descriptions-item>

            <el-descriptions-item label="账户状态">
              <el-tag :type="accountInfo.accountStatus === 1 ? 'success' : 'danger'">
                {{ accountStatusText(accountInfo.accountStatus) }}
              </el-tag>
            </el-descriptions-item>

            <el-descriptions-item label="注册渠道">
              {{ registerChannelText(accountInfo.registerChannel) }}
            </el-descriptions-item>
          </el-descriptions>
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

      <!-- 消息中心 -->
      <el-tab-pane name="messages">
        <template #label>
          <span>
            消息中心
            <el-badge v-if="unreadCount > 0" :value="unreadCount" class="message-badge" />
          </span>
        </template>

        <el-card shadow="never">
          <div class="message-toolbar">
            <el-button type="primary" plain @click="handleReadAll"> 全部已读 </el-button>
            <el-button @click="loadMessages"> 刷新 </el-button>
          </div>

          <div v-loading="messagesLoading">
            <div
              v-for="message in messages"
              :key="message.id"
              class="message-item"
              :class="{ unread: message.isRead === 0 }"
              @click="handleRead(message)"
            >
              <div class="message-top">
                <div>
                  <el-tag size="small" :type="message.isRead === 0 ? 'danger' : 'info'">
                    {{ messageTypeText(message.msgType) }}
                  </el-tag>

                  <strong class="message-title">
                    {{ message.title }}
                  </strong>
                </div>

                <span class="message-time">
                  {{ message.createTime }}
                </span>
              </div>

              <p class="message-content">
                {{ message.content }}
              </p>

              <span v-if="message.isRead === 0" class="unread-tip"> 点击标记为已读 </span>
            </div>

            <el-empty v-if="!messagesLoading && messages.length === 0" description="暂无消息" />
          </div>
        </el-card>
      </el-tab-pane>

      <!-- 评估报告 -->
      <el-tab-pane label="评估报告" name="assessment">
        <el-card shadow="never">
          <div class="report-toolbar">
            <el-select
              v-model="reportAssessType"
              placeholder="全部类型"
              clearable
              style="width: 180px"
              @change="handleReportTypeChange"
            >
              <el-option label="老年能力" :value="1" />
              <el-option label="健康风险" :value="2" />
              <el-option label="中医体质" :value="3" />
              <el-option label="膳食营养" :value="4" />
            </el-select>

            <el-button @click="loadReports"> 刷新 </el-button>
          </div>

          <el-table v-loading="reportLoading" :data="reports" style="width: 100%">
            <el-table-column prop="reportNo" label="报告编号" width="200" />

            <el-table-column label="评估类型" width="120">
              <template #default="{ row }">
                {{ assessTypeText(row.assessType) }}
              </template>
            </el-table-column>

            <el-table-column prop="score" label="得分" width="100" />

            <el-table-column label="等级" width="120">
              <template #default="{ row }">
                <el-tag type="warning">{{ row.grade }}</el-tag>
              </template>
            </el-table-column>

            <el-table-column prop="assessDate" label="评估日期" width="130" />

            <el-table-column prop="conclusion" label="结论" min-width="200" show-overflow-tooltip />

            <el-table-column label="操作" width="100" fixed="right">
              <template #default="{ row }">
                <el-button link type="primary" @click="handleReportDetail(row)"> 查看 </el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-empty v-if="!reportLoading && reports.length === 0" description="暂无评估报告" />

          <div class="pagination">
            <el-pagination
              v-model:current-page="reportPageNum"
              v-model:page-size="reportPageSize"
              background
              layout="total, prev, pager, next"
              :total="reportTotal"
              @current-change="handleReportPageChange"
            />
          </div>
        </el-card>
      </el-tab-pane>
    </el-tabs>

    <!-- 评估报告详情 -->
    <el-dialog v-model="detailVisible" title="评估报告详情" width="620px">
      <div v-loading="detailLoading">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="报告编号">
            {{ currentReport.reportNo || '-' }}
          </el-descriptions-item>

          <el-descriptions-item label="评估类型">
            {{ assessTypeText(currentReport.assessType) }}
          </el-descriptions-item>

          <el-descriptions-item label="得分">
            {{ currentReport.score ?? '-' }}
          </el-descriptions-item>

          <el-descriptions-item label="等级">
            {{ currentReport.grade || '-' }}
          </el-descriptions-item>

          <el-descriptions-item label="评估日期">
            {{ currentReport.assessDate || '-' }}
          </el-descriptions-item>

          <el-descriptions-item label="评估结论" :span="2">
            {{ currentReport.conclusion || '-' }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
  </div>
</template>

<style scoped>
.personal-page {
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

.personal-tabs {
  padding: 18px 22px;
  background: #ffffff;
  border-radius: 8px;
}

.message-badge {
  margin-left: 8px;
}

.message-toolbar {
  margin-bottom: 16px;
}

.message-item {
  padding: 18px;
  margin-bottom: 14px;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  cursor: pointer;
  transition: 0.2s;
}

.message-item:hover {
  border-color: #c6e2ff;
  background: #f8fbff;
}

.message-item.unread {
  background: #fdf6ec;
  border-color: #faecd8;
}

.message-top {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.message-title {
  margin-left: 10px;
  color: #303133;
}

.message-time {
  color: #909399;
  font-size: 13px;
}

.message-content {
  margin: 12px 0 8px;
  color: #606266;
  line-height: 1.7;
}

.unread-tip {
  color: #e6a23c;
  font-size: 12px;
}

.report-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
}

.pagination {
  margin-top: 16px;
  text-align: right;
}
</style>
