<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const router = useRouter()
const route = useRoute()

const userInfo = computed(() => {
  const value = localStorage.getItem('userInfo')

  if (!value) {
    return {
      realName: '测试医生',
      dept: '全科',
    }
  }

  try {
    return JSON.parse(value)
  } catch {
    return {
      realName: '测试医生',
      dept: '全科',
    }
  }
})

const activeMenu = computed(() => route.path)

const handleLogout = () => {
  localStorage.removeItem('accessToken')
  localStorage.removeItem('refreshToken')
  localStorage.removeItem('userInfo')

  router.push('/doctor/login')
}
</script>

<template>
  <div class="doctor-layout">
    <!-- 左侧菜单 -->
    <aside class="sidebar">
      <div class="logo-box">
        <div class="logo-icon">医</div>

        <div>
          <div class="logo-title">智慧医养</div>
          <div class="logo-subtitle">医生工作端</div>
        </div>
      </div>

      <el-menu :default-active="activeMenu" router class="doctor-menu">
        <el-menu-item index="/doctor">
          <span>工作台</span>
        </el-menu-item>

        <el-menu-item index="/doctor/elder-profiles">
          <span>档案管理</span>
        </el-menu-item>

        <el-menu-item index="/doctor/devices">
          <span>设备管理</span>
        </el-menu-item>

        <el-menu-item index="/doctor/profile">
          <span>个人中心</span>
        </el-menu-item>
      </el-menu>
    </aside>

    <!-- 右侧 -->
    <div class="main-area">
      <header class="topbar">
        <div class="system-name">智慧医养大数据公共服务平台</div>

        <div class="doctor-info">
          <div class="doctor-text">
            <strong>{{ userInfo.realName || '测试医生' }}</strong>
            <span>{{ userInfo.dept || '全科' }}</span>
          </div>

          <el-dropdown>
            <el-avatar> 医 </el-avatar>

            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="router.push('/doctor/profile')">
                  个人中心
                </el-dropdown-item>

                <el-dropdown-item divided @click="handleLogout"> 退出登录 </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </header>

      <main class="page-content">
        <RouterView />
      </main>
    </div>
  </div>
</template>

<style scoped>
.doctor-layout {
  display: flex;
  width: 100%;
  min-height: 100vh;
  background: #f4f7fa;
}

.sidebar {
  width: 230px;
  min-height: 100vh;
  background: #ffffff;
  border-right: 1px solid #e7ebef;
  flex-shrink: 0;
}

.logo-box {
  height: 80px;
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 0 24px;
  border-bottom: 1px solid #eef0f3;
}

.logo-icon {
  width: 42px;
  height: 42px;
  border-radius: 12px;
  background: #409eff;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  font-weight: bold;
}

.logo-title {
  font-size: 18px;
  font-weight: 600;
  color: #263445;
}

.logo-subtitle {
  margin-top: 4px;
  font-size: 12px;
  color: #909399;
}

.doctor-menu {
  border-right: none;
  padding-top: 12px;
}

.main-area {
  flex: 1;
  min-width: 0;
}

.topbar {
  height: 80px;
  padding: 0 30px;
  background: #ffffff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid #e7ebef;
}

.system-name {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
}

.doctor-info {
  display: flex;
  align-items: center;
  gap: 14px;
}

.doctor-text {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.doctor-text strong {
  color: #303133;
  font-size: 14px;
}

.doctor-text span {
  color: #909399;
  font-size: 12px;
  margin-top: 3px;
}

.page-content {
  padding: 24px;
}
</style>
