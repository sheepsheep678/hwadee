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

        <div>
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
          <span>{{ userInfo.realName || '老人用户' }}</span>

          <el-button type="danger" plain @click="handleLogout"> 退出 </el-button>
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
  background: #f5f7fa;
}

.sidebar {
  width: 230px;
  background: #ffffff;
  border-right: 1px solid #e7ebef;
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
  background: #67c23a;
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
}

.logo-subtitle {
  margin-top: 4px;
  font-size: 12px;
  color: #909399;
}

.elder-menu {
  border-right: none;
  padding-top: 12px;
}

.main-area {
  flex: 1;
}

.topbar {
  height: 80px;
  padding: 0 30px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: white;
  border-bottom: 1px solid #e7ebef;
}

.system-name {
  font-size: 20px;
  font-weight: 600;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.page-content {
  padding: 24px;
}
</style>
