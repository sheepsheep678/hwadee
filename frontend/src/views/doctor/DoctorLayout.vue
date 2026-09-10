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

        <div class="logo-text">
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

        <el-menu-item index="/doctor/assessments">
          <span>评估管理</span>
        </el-menu-item>

        <el-menu-item index="/doctor/focus">
          <span>重点随访</span>
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
            <el-avatar class="user-avatar">医</el-avatar>

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
  background: linear-gradient(180deg, #f7fbfa 0%, #eef6ff 100%);
}

.sidebar {
  width: 230px;
  min-height: 100vh;
  background: rgba(255, 255, 255, 0.92);
  border-right: 1px solid rgba(226, 232, 240, 0.9);
  flex-shrink: 0;
}

.logo-box {
  height: 80px;
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 0 20px;
  border-bottom: 1px solid rgba(226, 232, 240, 0.8);
}

.logo-icon {
  width: 42px;
  height: 42px;
  border-radius: 16px;
  background: linear-gradient(135deg, #0f766e, #2b7fd4);
  color: #ffffff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  font-weight: 700;
  box-shadow: 0 8px 18px rgba(15, 118, 110, 0.25);
}

.logo-text {
  display: flex;
  flex-direction: column;
}

.logo-title {
  font-size: 17px;
  font-weight: 700;
  color: #1f3b4d;
}

.logo-subtitle {
  margin-top: 4px;
  font-size: 12px;
  color: #7b8a97;
}

.doctor-menu {
  border-right: none;
  padding: 14px 12px;
  background: transparent;
}

.doctor-menu :deep(.el-menu-item) {
  position: relative;
  height: 46px;
  margin-bottom: 6px;
  border-radius: 12px;
  color: #526777;
  font-size: 14px;
}

.doctor-menu :deep(.el-menu-item:hover) {
  background: rgba(15, 118, 110, 0.06);
  color: #0f766e;
}

.doctor-menu :deep(.el-menu-item.is-active) {
  background: rgba(15, 118, 110, 0.1);
  color: #0f766e;
  font-weight: 600;
}

.doctor-menu :deep(.el-menu-item.is-active::before) {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 3px;
  height: 20px;
  border-radius: 0 3px 3px 0;
  background: #0f766e;
}

.main-area {
  flex: 1;
  min-width: 0;
}

.topbar {
  position: sticky;
  top: 0;
  z-index: 10;
  height: 68px;
  padding: 0 28px;
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid rgba(226, 232, 240, 0.8);
}

.system-name {
  position: relative;
  padding-left: 14px;
  font-size: 18px;
  font-weight: 600;
  color: #1f3b4d;
}

.system-name::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #0f766e;
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
  color: #1f3b4d;
  font-size: 14px;
}

.doctor-text span {
  color: #7b8a97;
  font-size: 12px;
  margin-top: 3px;
}

.user-avatar {
  background: linear-gradient(135deg, #0f766e, #2b7fd4);
  color: #ffffff;
  font-weight: 600;
  box-shadow: 0 6px 14px rgba(15, 118, 110, 0.28);
}

.page-content {
  width: 100%;
  max-width: 1280px;
  margin: 0 auto;
  padding: 28px 32px;
  box-sizing: border-box;
}

/* 子页面卡片基础样式（仅视觉基线，不改动子页面逻辑） */
.page-content :deep(.el-card) {
  border: 1px solid rgba(226, 232, 240, 0.9);
  border-radius: 16px;
  box-shadow: 0 8px 24px rgba(15, 118, 110, 0.06);
}

.page-content :deep(.el-card__header) {
  border-bottom: 1px solid rgba(226, 232, 240, 0.8);
  font-weight: 600;
}

@media (max-width: 900px) {
  .sidebar {
    width: 200px;
  }

  .logo-box {
    padding: 0 16px;
  }

  .logo-title {
    font-size: 16px;
  }

  .doctor-menu {
    padding: 12px 10px;
  }

  .topbar {
    height: 60px;
    padding: 0 16px;
  }

  .system-name {
    font-size: 16px;
  }

  .doctor-text {
    display: none;
  }

  .page-content {
    padding: 20px 16px;
  }
}
</style>
