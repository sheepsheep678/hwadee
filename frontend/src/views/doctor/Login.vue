<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { doctorLogin, doctorRegister } from '@/api/doctor'

const router = useRouter()
const loading = ref(false)

const loginForm = reactive({
  phone: '',
  password: '',
})

const registerVisible = ref(false)
const registerLoading = ref(false)

const registerForm = reactive({
  name: '',
  phone: '',
  password: '',
  confirmPassword: '',
  doctorType: 1,
  title: '',
  dept: '',
  orgId: null,
  certType: 1,
  certNo: '',
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

  loading.value = true

  try {
    const result = await doctorLogin({
      phone: loginForm.phone,
      password: loginForm.password,
    })

    const data = result.data

    localStorage.setItem('accessToken', data.accessToken)
    localStorage.setItem('refreshToken', data.refreshToken)
    localStorage.setItem('userInfo', JSON.stringify(data.userInfo || {}))

    ElMessage.success('登录成功')
    router.push('/doctor')
  } catch (error) {
    // 错误提示已由拦截器处理
  } finally {
    loading.value = false
  }
}

const openRegister = () => {
  Object.assign(registerForm, {
    name: '',
    phone: '',
    password: '',
    confirmPassword: '',
    doctorType: 1,
    title: '',
    dept: '',
    orgId: null,
    certType: 1,
    certNo: '',
  })

  registerVisible.value = true
}

const handleRegister = async () => {
  if (!registerForm.name) {
    ElMessage.warning('请输入姓名')
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
    await doctorRegister({
      name: registerForm.name,
      phone: registerForm.phone,
      password: registerForm.password,
      doctorType: registerForm.doctorType,
      title: registerForm.title,
      dept: registerForm.dept,
      orgId: registerForm.orgId,
      qualifications: [
        {
          certType: registerForm.certType,
          certNo: registerForm.certNo,
        },
      ],
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
            <path d="M12 5v14M5 12h14" />
          </svg>
          智慧医养 · 云端平台
        </div>

        <h1>智慧医养大数据公共服务平台</h1>
        <p class="brand-subtitle">AI赋能健康管理，连接医疗与养老服务</p>

        <div class="brand-visual">
          <svg class="brand-ecg" viewBox="0 0 260 48" preserveAspectRatio="none" aria-hidden="true">
            <polyline
              points="0,24 60,24 78,24 90,10 102,38 112,24 160,24 176,24 188,14 200,34 210,24 260,24"
            />
          </svg>

          <svg class="brand-doctor" viewBox="0 0 140 120" role="img" aria-label="医生形象">
            <circle cx="70" cy="62" r="50" fill="rgba(15, 118, 110, 0.08)" />
            <circle
              cx="70"
              cy="62"
              r="44"
              fill="none"
              stroke="rgba(15, 118, 110, 0.18)"
              stroke-width="1.5"
              stroke-dasharray="4 8"
            />

            <rect x="63" y="66" width="14" height="14" rx="6" fill="#f3d6c2" />
            <path
              d="M44 112c3-20 13-30 26-30s23 10 26 30z"
              fill="#ffffff"
              stroke="#cfe2f8"
              stroke-width="2.5"
            />
            <path d="M62 84l8 8 8-8" fill="#eaf4ff" stroke="#cfe2f8" stroke-width="2" />

            <rect x="67" y="92" width="6" height="16" rx="1.5" fill="#0f766e" />
            <rect x="62" y="97" width="16" height="6" rx="1.5" fill="#0f766e" />

            <path
              d="M88 84c6 6 8 16 4 24"
              fill="none"
              stroke="#2b7fd4"
              stroke-width="3"
              stroke-linecap="round"
            />
            <circle cx="93" cy="110" r="5" fill="#2b7fd4" />

            <circle cx="70" cy="52" r="18" fill="#f7e0cb" />
            <path
              d="M52 49c0-11 8-18 18-18s18 7 18 18c-6-5-12-7-18-7s-12 2-18 7z"
              fill="#33526b"
            />
            <circle cx="64" cy="53" r="2" fill="#33475a" />
            <circle cx="76" cy="53" r="2" fill="#33475a" />
            <circle cx="59" cy="58" r="3" fill="rgba(240, 150, 130, 0.35)" />
            <circle cx="81" cy="58" r="3" fill="rgba(240, 150, 130, 0.35)" />
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

      <!-- 右侧悬浮玻璃登录卡片 -->
      <section class="login-card">
        <div class="card-header">
          <h2>欢迎登录</h2>
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

        <div class="link-box">
          <el-link type="primary" @click="openRegister">医生入驻申请</el-link>
          <el-link type="info" @click="router.push('/elder/login')">老人端登录</el-link>
          <el-link type="info" @click="router.push('/')">返回选择</el-link>
        </div>

        <div class="footer-text">智慧医养 · 健康服务</div>
      </section>
    </div>

    <el-dialog v-model="registerVisible" title="医生入驻申请" width="560px" style="max-width: 92vw">
      <el-form :model="registerForm" label-width="100px">
        <el-form-item label="姓名">
          <el-input v-model="registerForm.name" />
        </el-form-item>

        <el-form-item label="手机号">
          <el-input v-model="registerForm.phone" />
        </el-form-item>

        <el-form-item label="医生类型">
          <el-select v-model="registerForm.doctorType" style="width: 100%">
            <el-option label="家庭医生" :value="1" />
            <el-option label="专科医生" :value="2" />
            <el-option label="康复师" :value="3" />
            <el-option label="护理师" :value="4" />
          </el-select>
        </el-form-item>

        <el-form-item label="职称">
          <el-input v-model="registerForm.title" />
        </el-form-item>

        <el-form-item label="科室">
          <el-input v-model="registerForm.dept" />
        </el-form-item>

        <el-form-item label="机构ID">
          <el-input v-model="registerForm.orgId" placeholder="选填" />
        </el-form-item>

        <el-form-item label="资质证书">
          <el-select v-model="registerForm.certType" style="width: 100%; margin-bottom: 10px">
            <el-option label="执业证" :value="1" />
            <el-option label="职称证" :value="2" />
          </el-select>

          <el-input v-model="registerForm.certNo" placeholder="证书编号" />
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
          提交申请
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
  min-height: 100vh;
  padding: 56px 40px;
  overflow: hidden;
  box-sizing: border-box;
  font-family:
    'PingFang SC', 'Microsoft YaHei', 'Noto Sans SC', 'Helvetica Neue', Arial, sans-serif;
  background:
    radial-gradient(circle at 18% 22%, rgba(45, 212, 191, 0.16), transparent 45%),
    radial-gradient(circle at 82% 18%, rgba(125, 211, 252, 0.18), transparent 42%),
    radial-gradient(circle at 60% 92%, rgba(255, 255, 255, 0.92), transparent 55%),
    linear-gradient(135deg, #f4fbfa 0%, #eef6ff 52%, #ffffff 100%);
}

/* ===== 统一背景装饰 ===== */
.tech-grid {
  position: absolute;
  inset: 0;
  background-image:
    repeating-linear-gradient(
      0deg,
      rgba(15, 118, 110, 0.035) 0,
      rgba(15, 118, 110, 0.035) 1px,
      transparent 1px,
      transparent 64px
    ),
    repeating-linear-gradient(
      90deg,
      rgba(15, 118, 110, 0.035) 0,
      rgba(15, 118, 110, 0.035) 1px,
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
  background: radial-gradient(circle, rgba(45, 212, 191, 0.28), transparent 70%);
}

.glow-2 {
  right: -80px;
  bottom: -120px;
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, rgba(147, 197, 253, 0.3), transparent 70%);
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
  stroke: #0f766e;
  stroke-width: 2;
  stroke-linecap: round;
  stroke-linejoin: round;
}

.brand-doctor {
  flex: none;
  width: 130px;
  height: 112px;
  pointer-events: none;
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
  border: 1px solid rgba(15, 118, 110, 0.16);
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.6);
  font-size: 13px;
  color: #0f766e;
  letter-spacing: 1px;
}

.brand-badge svg {
  width: 14px;
  height: 14px;
  fill: none;
  stroke: #0f766e;
  stroke-width: 1.8;
  stroke-linecap: round;
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

.brand-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-top: 26px;
}

.brand-tags span {
  padding: 7px 15px;
  border: 1px solid rgba(15, 118, 110, 0.14);
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.6);
  font-size: 14px;
  color: #12695f;
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
  box-shadow: 0 0 0 1px #0f766e inset;
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
  background: #0f766e;
}

.login-button:hover,
.login-button:focus {
  background: #0d6a63;
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

  .brand-tags {
    margin-top: 20px;
    justify-content: center;
  }

  .brand-visual {
    justify-content: center;
  }

  .brand-doctor {
    width: 110px;
    height: 96px;
  }

  .login-card {
    flex: none;
    width: 100%;
    max-width: 400px;
  }
}
</style>
