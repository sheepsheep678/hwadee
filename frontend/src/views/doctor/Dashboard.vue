<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const cards = [
  {
    title: '档案管理',
    description: '查看和维护老人档案',
    path: '/doctor/elder-profiles',
  },
  {
    title: '设备管理',
    description: '查看老人绑定设备',
    path: '/doctor/devices',
  },
  {
    title: '评估管理',
    description: '开展能力/健康/体质评估',
    path: '/doctor/assessments',
  },
  {
    title: '重点随访',
    description: '重点人群标签与随访计划',
    path: '/doctor/focus',
  },
  {
    title: '个人中心',
    description: '查看个人信息、排班与绩效',
    path: '/doctor/profile',
  },
]
</script>

<template>
  <div class="dashboard">
    <div class="welcome">
      <h2>工作台</h2>
      <p>欢迎使用智慧医养大数据公共服务平台</p>
    </div>

    <el-row :gutter="20">
      <el-col v-for="item in cards" :key="item.path" :xs="24" :sm="12" :lg="8">
        <el-card class="module-card" shadow="hover" @click="router.push(item.path)">
          <h3>{{ item.title }}</h3>
          <p>{{ item.description }}</p>

          <div class="enter">进入模块 →</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.dashboard {
  width: 100%;
}

.welcome {
  margin-bottom: 24px;
}

.welcome h2 {
  margin: 0;
  color: #303133;
}

.welcome p {
  margin-top: 8px;
  color: #909399;
}

.module-card {
  min-height: 160px;
  cursor: pointer;
  margin-bottom: 20px;
}

.module-card h3 {
  margin: 8px 0 14px;
  font-size: 19px;
  color: #303133;
}

.module-card p {
  color: #909399;
}

.enter {
  margin-top: 28px;
  color: #409eff;
  font-size: 14px;
}
</style>
