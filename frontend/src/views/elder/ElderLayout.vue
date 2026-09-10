<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const userInfo = computed(() => {
  const value = localStorage.getItem('elderUserInfo')

  if (!value) {
    return {
      realName: '老人用户',
    }
  }

  try {
    return JSON.parse(value)
  } catch {
    return {
      realName: '老人用户',
    }
  }
})

const activeMenu = computed(() => route.path)

const handleLogout = () => {
  localStorage.removeItem('elderAccessToken')
  localStorage.removeItem('elderUserInfo')

  router.push('/elder/login')
}
</script>

<template>
  <div class="elder-layout">
    <aside class="sidebar">
      <div class="logo-box">
        <div class="logo-icon">养</div>

        <div class="logo-text">
          <div class="logo-title">智慧医养</div>
          <div class="logo-subtitle">老人服务端</div>
        </div>
      </div>

      <el-menu :default-active="activeMenu" router class="elder-menu">
        <el-menu-item index="/elder">
          <span>首页</span>
        </el-menu-item>

        <el-menu-item index="/elder/profile">
          <span>我的档案</span>
        </el-menu-item>

        <el-menu-item index="/elder/devices">
          <span>我的设备</span>
        </el-menu-item>

        <el-menu-item index="/elder/personal-center">
          <span>个人中心</span>
        </el-menu-item>
      </el-menu>
    </aside>

    <div class="main-area">
      <header class="topbar">
        <div class="system-name">智慧医养大数据公共服务平台</div>

        <div class="user-info">
          <el-avatar class="user-avatar">老</el-avatar>
          <span>{{ userInfo.realName || '老人用户' }}</span>

          <el-button type="danger" plain class="logout-btn" @click="handleLogout"> 退出 </el-button>
        </div>
      </header>

      <main class="page-content">
        <RouterView />
      </main>
    </div>
  </div>
</template>

<style scoped>
.elder-layout {
  display: flex;
  min-height: 100vh;
  background: linear-gradient(180deg, #f7fcf8 0%, #f0faf4 100%);
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
  background: linear-gradient(135deg, #2f9e6e, #67c23a);
  color: #ffffff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  font-weight: 700;
  box-shadow: 0 8px 18px rgba(47, 158, 110, 0.25);
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

.elder-menu {
  border-right: none;
  padding: 14px 12px;
  background: transparent;
}

.elder-menu :deep(.el-menu-item) {
  position: relative;
  height: 46px;
  margin-bottom: 6px;
  border-radius: 12px;
  color: #526777;
  font-size: 14px;
}

.elder-menu :deep(.el-menu-item:hover) {
  background: rgba(47, 158, 110, 0.08);
  color: #2f9e6e;
}

.elder-menu :deep(.el-menu-item.is-active) {
  background: rgba(47, 158, 110, 0.12);
  color: #2f9e6e;
  font-weight: 600;
}

.elder-menu :deep(.el-menu-item.is-active::before) {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 3px;
  height: 20px;
  border-radius: 0 3px 3px 0;
  background: #2f9e6e;
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
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
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
  background: #2f9e6e;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 14px;
}

.user-info span {
  font-size: 14px;
  color: #1f3b4d;
}

.user-avatar {
  background: linear-gradient(135deg, #2f9e6e, #67c23a);
  color: #ffffff;
  font-weight: 600;
  box-shadow: 0 6px 14px rgba(47, 158, 110, 0.28);
}

.logout-btn.el-button {
  border-radius: 10px;
  border-color: rgba(47, 158, 110, 0.35);
  color: #2f9e6e;
  background: rgba(47, 158, 110, 0.06);
}

.logout-btn.el-button:hover,
.logout-btn.el-button:focus {
  border-color: rgba(47, 158, 110, 0.5);
  color: #2f9e6e;
  background: rgba(47, 158, 110, 0.12);
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
  box-shadow: 0 8px 24px rgba(47, 158, 110, 0.06);
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

  .elder-menu {
    padding: 12px 10px;
  }

  .topbar {
    height: 60px;
    padding: 0 16px;
  }

  .system-name {
    font-size: 16px;
  }

  .page-content {
    padding: 20px 16px;
  }
}
</style>
