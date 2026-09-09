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

      <div class="link-box">
        <el-link type="primary" @click="openRegister">医生入驻申请</el-link>
        <el-link type="info" @click="router.push('/elder/login')">老人端登录</el-link>
        <el-link type="info" @click="router.push('/')">返回选择</el-link>
      </div>

      <div class="footer-text">智慧医养 · 健康服务</div>
    </div>

    <el-dialog v-model="registerVisible" title="医生入驻申请" width="560px">
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

.link-box {
  display: flex;
  justify-content: space-between;
  margin-top: 16px;
}

.footer-text {
  margin-top: 26px;
  text-align: center;
  color: #a0a8b0;
  font-size: 13px;
}
</style>
