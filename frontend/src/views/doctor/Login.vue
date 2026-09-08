<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { doctorLogin } from '@/api/doctor'

const router = useRouter()
const loading = ref(false)

const loginForm = reactive({
  phone: '',
  password: '',
})

const handleLogin = async () => {
  if (!loginForm.phone) {
    ElMessage.warning('请输入手机号')
    return
  }

  if (!loginForm.password) {
    ElMessage.warning('请输入密码')
    return
  }

  localStorage.setItem('accessToken', 'dev-token')

  localStorage.setItem(
    'userInfo',
    JSON.stringify({
      userId: 30001,
      realName: 'Violet',
      userType: 2,
      dept: '全科',
      title: '医生',
    }),
  )

  ElMessage.success('登录成功')
  router.push('/doctor')
}
</script>

<template>
  <div class="login-page">
    <div class="login-card">
      <div class="title-box">
        <h1>智慧医养大数据公共服务平台</h1>
        <p>医生工作端</p>
      </div>

      <el-form :model="loginForm" size="large" @keyup.enter="handleLogin">
        <el-form-item>
          <el-input v-model="loginForm.phone" placeholder="请输入医生手机号" clearable />
        </el-form-item>

        <el-form-item>
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            show-password
          />
        </el-form-item>

        <el-button type="primary" class="login-button" :loading="loading" @click="handleLogin">
          登录
        </el-button>
      </el-form>

      <div class="footer-text">智慧医养 · 健康服务</div>
    </div>
  </div>
</template>

<style scoped>
.login-page {
  width: 100vw;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #eef8f6, #edf4fb);
}

.login-card {
  width: 420px;
  padding: 42px 46px;
  box-sizing: border-box;
  background: #fff;
  border-radius: 18px;
  box-shadow: 0 14px 45px rgba(31, 70, 100, 0.12);
}

.title-box {
  text-align: center;
  margin-bottom: 32px;
}

.title-box h1 {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  color: #25384b;
}

.title-box p {
  margin-top: 12px;
  color: #7b8a97;
  font-size: 15px;
}

.login-button {
  width: 100%;
  margin-top: 6px;
}

.footer-text {
  margin-top: 26px;
  text-align: center;
  color: #a0a8b0;
  font-size: 13px;
}
</style>
