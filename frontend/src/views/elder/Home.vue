<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const realName = computed(() => {
  const raw = localStorage.getItem('elderUserInfo')

  if (!raw) {
    return ''
  }

  try {
    return JSON.parse(raw).realName || ''
  } catch {
    return ''
  }
})

const cards = [
  {
    title: '我的档案',
    description: '查看个人健康及基本档案',
    path: '/elder/profile',
  },
  {
    title: '我的设备',
    description: '查看已绑定的健康设备',
    path: '/elder/devices',
  },
  {
    title: '个人中心',
    description: '管理账户信息与密码',
    path: '/elder/personal-center',
  },
]
</script>

<template>
  <div>
    <div class="welcome">
      <h2>{{ realName ? `${realName}，您好，欢迎回来` : '您好，欢迎回来' }}</h2>
      <p>这里可以查看您的健康档案和设备信息</p>
    </div>

    <el-row :gutter="20">
      <el-col v-for="item in cards" :key="item.path" :span="8">
        <el-card shadow="hover" class="card" @click="router.push(item.path)">
          <h3>{{ item.title }}</h3>
          <p>{{ item.description }}</p>
          <div class="enter">进入 →</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.welcome {
  margin-bottom: 24px;
}

.welcome h2 {
  margin: 0;
}

.welcome p {
  color: #909399;
}

.card {
  cursor: pointer;
  min-height: 150px;
}

.card p {
  color: #909399;
}

.enter {
  margin-top: 25px;
  color: #67c23a;
}
</style>
