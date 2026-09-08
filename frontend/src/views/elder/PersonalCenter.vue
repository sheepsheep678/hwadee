<script setup>
import { computed, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'

import {
  getMyAccount,
  changeElderPassword,
  getElderMessages,
  readElderMessage,
  getUnreadMessageCount,
} from '@/api/elderUser'

const USE_MOCK = true

const activeTab = ref('account')

const accountInfo = reactive({
  id: 50001,
  accountNo: 'ELD20260001',
  bindPhone: '13800138001',
  authStatus: 1,
  accountStatus: 1,
  registerChannel: 1,
})

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: '',
})

const messages = ref([
  {
    id: 1,
    msgType: 1,
    title: '健康服务提醒',
    content: '请注意按时测量血压，并记录每日数据。',
    isRead: 0,
    createTime: '2026-09-08 09:00',
  },
  {
    id: 2,
    msgType: 2,
    title: '服务提醒',
    content: '您的家庭医生将于明日上午进行上门随访。',
    isRead: 0,
    createTime: '2026-09-07 16:30',
  },
  {
    id: 3,
    msgType: 1,
    title: '系统通知',
    content: '您的个人健康档案已更新。',
    isRead: 1,
    createTime: '2026-09-06 10:20',
  },
])

const unreadCount = computed(() => {
  return messages.value.filter((item) => item.isRead === 0).length
})

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
    2: '服务提醒',
    3: '审核提醒',
  }

  return map[type] || '其他'
}

const loadAccount = async () => {
  if (USE_MOCK) {
    return
  }

  try {
    const result = await getMyAccount()
    Object.assign(accountInfo, result.data || {})
  } catch (error) {
    console.error('获取账户信息失败：', error)
  }
}

const loadMessages = async () => {
  if (USE_MOCK) {
    return
  }

  try {
    const result = await getElderMessages({
      pageNum: 1,
      pageSize: 10,
    })

    messages.value = result.data.list || []
  } catch (error) {
    console.error('获取消息失败：', error)
  }
}

const loadUnreadCount = async () => {
  if (USE_MOCK) {
    return
  }

  try {
    const result = await getUnreadMessageCount()
    console.log('未读数量：', result.data)
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
    if (!USE_MOCK) {
      await changeElderPassword({
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

const handleRead = async (message) => {
  if (message.isRead === 1) {
    return
  }

  try {
    if (!USE_MOCK) {
      await readElderMessage(message.id)
    }

    message.isRead = 1
    ElMessage.success('已标记为已读')
  } catch (error) {
    console.error('标记消息已读失败：', error)
  }
}

loadAccount()
loadMessages()
loadUnreadCount()
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

            <el-descriptions-item label="绑定手机号">
              {{ accountInfo.bindPhone }}
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

          <el-empty v-if="messages.length === 0" description="暂无消息" />
        </el-card>
      </el-tab-pane>
    </el-tabs>
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
</style>
