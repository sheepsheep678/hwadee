<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { elderLogin, elderRegister } from '@/api/elderUser'

const router = useRouter()
const loading = ref(false)

const loginForm = reactive({
  account: '',
  password: '',
})

const registerVisible = ref(false)
const registerLoading = ref(false)

const registerForm = reactive({
  name: '',
  idCard: '',
  phone: '',
  password: '',
  confirmPassword: '',
  registerChannel: 1,
})

const handleLogin = async () => {
  if (!loginForm.account) {
    ElMessage.warning('请输入账号')
    return
  }

  if (!loginForm.password) {
    ElMessage.warning('请输入密码')
    return
  }

  loading.value = true

  try {
    const result = await elderLogin({
      account: loginForm.account,
      password: loginForm.password,
    })

    const data = result.data

    localStorage.setItem('elderAccessToken', data.accessToken)
    localStorage.setItem('elderUserInfo', JSON.stringify(data.userInfo || {}))

    ElMessage.success('登录成功')
    router.push('/elder')
  } catch (error) {
    // 错误提示已由拦截器处理
  } finally {
    loading.value = false
  }
}

const openRegister = () => {
  Object.assign(registerForm, {
    name: '',
    idCard: '',
    phone: '',
    password: '',
    confirmPassword: '',
    registerChannel: 1,
  })

  registerVisible.value = true
}

const handleRegister = async () => {
  if (!registerForm.name) {
    ElMessage.warning('请输入姓名')
    return
  }

  if (!registerForm.idCard) {
    ElMessage.warning('请输入身份证号')
    return
  }

  if (!registerForm.phone) {
    ElMessage.warning('请输入手机号')
    return
  }

  if (!registerForm.password || registerForm.password.length < 6) {
    ElMessage.warning('密码不能少于 6 位')
    return
  }

  if (registerForm.password !== registerForm.confirmPassword) {
    ElMessage.warning('两次输入的密码不一致')
    return
  }

  registerLoading.value = true

  try {
    await elderRegister({
      name: registerForm.name,
      idCard: registerForm.idCard,
      phone: registerForm.phone,
      password: registerForm.password,
      confirmPassword: registerForm.confirmPassword,
      registerChannel: registerForm.registerChannel,
    })

    ElMessage.success('注册成功，请等待审核')
    registerVisible.value = false
  } catch (error) {
    // 错误提示已由拦截器处理
  } finally {
    registerLoading.value = false
  }
}
</script>

<template>
  <div class="login-page">
    <!-- 统一浅色医疗科技背景 -->
    <div class="tech-grid"></div>
    <div class="glow glow-1"></div>
    <div class="glow glow-2"></div>

    <div class="login-content">
      <!-- 左侧品牌展示 -->
      <section class="brand-panel">
        <div class="brand-badge">
          <svg viewBox="0 0 24 24" aria-hidden="true">
            <path
              d="M12 19c-5-3.2-8-6-8-9.4A4.6 4.6 0 0 1 12 6a4.6 4.6 0 0 1 8 3.6c0 3.4-3 6.2-8 9.4z"
            />
          </svg>
          智慧医养 · 云端平台
        </div>

        <h1>智慧医养大数据公共服务平台</h1>
        <p class="brand-subtitle">科技守护银龄生活，让养老服务更安心</p>

        <div class="brand-visual">
          <svg class="brand-ecg" viewBox="0 0 260 48" preserveAspectRatio="none" aria-hidden="true">
            <polyline
              points="0,24 60,24 78,24 90,10 102,38 112,24 160,24 176,24 188,14 200,34 210,24 260,24"
            />
          </svg>

          <svg class="brand-elder" viewBox="0 0 140 120" role="img" aria-label="老年人形象">
            <circle cx="70" cy="62" r="50" fill="rgba(47, 158, 110, 0.08)" />
            <path
              d="M70 18c-3-5-11-5-13 1-2 6 3 10 13 17 10-7 15-11 13-17-2-6-10-6-13-1z"
              fill="rgba(47, 158, 110, 0.18)"
              stroke="#3fa66a"
              stroke-width="2"
              stroke-linejoin="round"
            />

            <rect x="63" y="66" width="14" height="14" rx="6" fill="#f3d6c2" />
            <path
              d="M44 112c3-20 13-30 26-30s23 10 26 30z"
              fill="#8fd9b0"
              stroke="#3fa66a"
              stroke-width="2.5"
            />
            <path
              d="M70 88c-2-2-5-1-5 1.5 0 2 2.5 4 5 5.6 2.5-1.6 5-3.6 5-5.6 0-2.5-3-3.5-5-1.5z"
              fill="#ff8a80"
            />
            <path
              d="M96 74c5 5 6 14 6 26"
              fill="none"
              stroke="#b98b4a"
              stroke-width="3.5"
              stroke-linecap="round"
            />

            <circle cx="70" cy="52" r="18" fill="#f7e0cb" />
            <path
              d="M52 49c0-11 8-18 18-18s18 7 18 18c-6-5-12-7-18-7s-12 2-18 7z"
              fill="#dfe6ec"
            />
            <circle cx="53" cy="54" r="5" fill="#dfe6ec" />
            <circle cx="87" cy="54" r="5" fill="#dfe6ec" />
            <circle cx="64" cy="53" r="2" fill="#33475a" />
            <circle cx="76" cy="53" r="2" fill="#33475a" />
            <circle cx="59" cy="58" r="3" fill="rgba(240, 150, 130, 0.4)" />
            <circle cx="81" cy="58" r="3" fill="rgba(240, 150, 130, 0.4)" />
            <path
              d="M64 59c2 2.4 10 2.4 12 0"
              fill="none"
              stroke="#c98a78"
              stroke-width="2"
              stroke-linecap="round"
            />
          </svg>
        </div>

        <div class="brand-tags">
          <span>智慧健康档案</span>
          <span>老年能力评估</span>
          <span>医养协同服务</span>
          <span>健康风险预警</span>
        </div>
      </section>

      <!-- 右侧登录玻璃卡片 -->
      <section class="login-card">
        <div class="card-header">
          <h2>欢迎登录</h2>
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

          <el-button type="primary" class="login-button" :loading="loading" @click="handleLogin">
            登录
          </el-button>
        </el-form>

        <div class="link-box">
          <el-link type="primary" @click="openRegister">老人注册</el-link>
          <el-link type="info" @click="router.push('/doctor/login')">医生端登录</el-link>
          <el-link type="info" @click="router.push('/')">返回选择</el-link>
        </div>

        <div class="footer-text">智慧养老 · 健康陪伴</div>
      </section>
    </div>

    <el-dialog v-model="registerVisible" title="老人注册" width="560px" style="max-width: 92vw">
      <el-form :model="registerForm" label-width="100px">
        <el-form-item label="姓名">
          <el-input v-model="registerForm.name" />
        </el-form-item>

        <el-form-item label="身份证号">
          <el-input v-model="registerForm.idCard" />
        </el-form-item>

        <el-form-item label="手机号">
          <el-input v-model="registerForm.phone" />
        </el-form-item>

        <el-form-item label="注册渠道">
          <el-select v-model="registerForm.registerChannel" style="width: 100%">
            <el-option label="自助注册" :value="1" />
            <el-option label="家属代注册" :value="2" />
            <el-option label="机构录入" :value="3" />
          </el-select>
        </el-form-item>

        <el-form-item label="密码">
          <el-input v-model="registerForm.password" type="password" show-password />
        </el-form-item>

        <el-form-item label="确认密码">
          <el-input v-model="registerForm.confirmPassword" type="password" show-password />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="registerVisible = false">取消</el-button>
        <el-button type="primary" :loading="registerLoading" @click="handleRegister">
          注册
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.login-page {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100vw;
  min-height: 100vh;
  padding: 56px 40px;
  overflow: hidden;
  box-sizing: border-box;
  font-family:
    'Microsoft YaHei', 'PingFang SC', 'Noto Sans SC', 'Helvetica Neue', Arial, sans-serif;
  background:
    radial-gradient(circle at 18% 22%, rgba(47, 158, 110, 0.16), transparent 45%),
    radial-gradient(circle at 82% 18%, rgba(125, 211, 252, 0.16), transparent 42%),
    radial-gradient(circle at 60% 94%, rgba(255, 255, 255, 0.92), transparent 55%),
    linear-gradient(135deg, #f4fbfa 0%, #eef6ff 52%, #ffffff 100%);
}

/* ===== 统一背景装饰 ===== */
.tech-grid {
  position: absolute;
  inset: 0;
  background-image:
    repeating-linear-gradient(
      0deg,
      rgba(47, 158, 110, 0.035) 0,
      rgba(47, 158, 110, 0.035) 1px,
      transparent 1px,
      transparent 64px
    ),
    repeating-linear-gradient(
      90deg,
      rgba(47, 158, 110, 0.035) 0,
      rgba(47, 158, 110, 0.035) 1px,
      transparent 1px,
      transparent 64px
    );
  pointer-events: none;
}

.glow {
  position: absolute;
  border-radius: 50%;
  filter: blur(90px);
  pointer-events: none;
}

.glow-1 {
  top: -140px;
  left: -60px;
  width: 380px;
  height: 380px;
  background: radial-gradient(circle, rgba(63, 166, 106, 0.26), transparent 70%);
}

.glow-2 {
  right: -80px;
  bottom: -120px;
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, rgba(147, 197, 253, 0.26), transparent 70%);
}

/* ===== 内容区：左品牌 + 右玻璃卡片 ===== */
.login-content {
  position: relative;
  z-index: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 64px;
  width: 100%;
  max-width: 1120px;
}

.brand-panel {
  flex: 1 1 520px;
  max-width: 560px;
  padding: 28px 24px;
  border-radius: 24px;
  background: rgba(255, 255, 255, 0.28);
}

.brand-badge {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 6px 16px;
  border: 1px solid rgba(47, 158, 110, 0.18);
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.62);
  font-size: 13px;
  color: #2f9e6e;
  letter-spacing: 1px;
}

.brand-badge svg {
  width: 14px;
  height: 14px;
  fill: none;
  stroke: #2f9e6e;
  stroke-width: 1.8;
  stroke-linecap: round;
  stroke-linejoin: round;
}

.brand-panel h1 {
  margin: 20px 0 0;
  font-size: 40px;
  line-height: 1.34;
  font-weight: 700;
  color: #0f3d4f;
  letter-spacing: 0.5px;
}

.brand-subtitle {
  margin: 16px 0 0;
  font-size: 16px;
  line-height: 1.7;
  color: #4c6b7a;
}

.brand-visual {
  display: flex;
  align-items: center;
  gap: 18px;
  margin-top: 22px;
}

.brand-ecg {
  flex: 1;
  height: 36px;
  opacity: 0.35;
}

.brand-ecg polyline {
  fill: none;
  stroke: #3fa66a;
  stroke-width: 2;
  stroke-linecap: round;
  stroke-linejoin: round;
}

.brand-elder {
  flex: none;
  width: 130px;
  height: 112px;
  pointer-events: none;
}

.brand-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-top: 26px;
}

.brand-tags span {
  padding: 7px 15px;
  border: 1px solid rgba(47, 158, 110, 0.16);
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.62);
  font-size: 14px;
  color: #1f7a55;
}

/* ===== 玻璃登录卡片 ===== */
.login-card {
  flex: 0 0 400px;
  width: 400px;
  padding: 44px 38px;
  box-sizing: border-box;
  border: 1px solid rgba(255, 255, 255, 0.75);
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.7);
  box-shadow: 0 16px 40px rgba(15, 118, 110, 0.1);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
}

.card-header h2 {
  margin: 0;
  font-size: 26px;
  font-weight: 600;
  color: #123f52;
}

.card-header p {
  margin: 10px 0 0;
  font-size: 15px;
  color: #6b8a9a;
}

.login-card :deep(.el-form) {
  margin-top: 32px;
}

.login-card :deep(.el-input__wrapper) {
  padding: 4px 14px;
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.95);
  box-shadow: 0 0 0 1px #d9e7ec inset;
}

.login-card :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #2f9e6e inset;
}

.login-card :deep(.el-input__inner) {
  height: 46px;
}

.login-button {
  width: 100%;
  height: 46px;
  margin-top: 8px;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  letter-spacing: 2px;
  background: linear-gradient(135deg, #2f9e6e, #3fa66a);
}

.login-button:hover,
.login-button:focus {
  background: linear-gradient(135deg, #28875d, #379a5f);
}

.link-box {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  gap: 8px;
  margin-top: 20px;
}

.footer-text {
  margin-top: 26px;
  text-align: center;
  font-size: 13px;
  color: #9db2bd;
}

/* ===== 响应式：小于 900px 时上下排布 ===== */
@media (max-width: 900px) {
  .login-page {
    padding: 36px 20px;
  }

  .login-content {
    flex-direction: column;
    gap: 32px;
  }

  .brand-panel {
    flex: none;
    max-width: 520px;
    padding: 20px 16px;
    text-align: center;
  }

  .brand-panel h1 {
    margin-top: 18px;
    font-size: 28px;
  }

  .brand-subtitle {
    margin-top: 14px;
    font-size: 15px;
  }

  .brand-visual {
    justify-content: center;
  }

  .brand-elder {
    width: 110px;
    height: 96px;
  }

  .brand-tags {
    margin-top: 20px;
    justify-content: center;
  }

  .login-card {
    flex: none;
    width: 100%;
    max-width: 400px;
  }
}
</style>
