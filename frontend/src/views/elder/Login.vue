<script setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()

const loginForm = reactive({
  account: '',
  password: '',
})

const handleLogin = () => {
  if (!loginForm.account) {
    ElMessage.warning('请输入账号')
    return
  }

  if (!loginForm.password) {
    ElMessage.warning('请输入密码')
    return
  }

  // 临时模拟登录
  localStorage.setItem('elderAccessToken', 'elder-dev-token')

  localStorage.setItem(
    'elderUserInfo',
    JSON.stringify({
      userId: 50001,
      realName: '张建国',
      userType: 3,
    }),
  )

  ElMessage.success('登录成功')
  router.push('/elder')
}
</script>

<template>
  <div class="login-page">
    <div class="login-card">
      <div class="title-box">
        <h1>智慧医养大数据公共服务平台</h1>
        <p>老人服务端</p>
      </div>

      <el-form :model="loginForm" size="large" @keyup.enter="handleLogin">
        <el-form-item>
          <el-input v-model="loginForm.account" placeholder="请输入手机号或身份证号" clearable />
        </el-form-item>

        <el-form-item>
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            show-password
          />
        </el-form-item>

        <el-button type="primary" class="login-button" @click="handleLogin"> 登录 </el-button>
      </el-form>

      <div class="footer-text">智慧养老 · 健康陪伴</div>
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
  background: linear-gradient(135deg, #f1f8f5, #edf4fb);
}

.login-card {
  width: 420px;
  padding: 42px 46px;
  box-sizing: border-box;
  background: #ffffff;
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
  color: #25384b;
}

.title-box p {
  margin-top: 12px;
  color: #7b8a97;
}

.login-button {
  width: 100%;
}

.footer-text {
  margin-top: 26px;
  text-align: center;
  color: #a0a8b0;
  font-size: 13px;
}
</style>
