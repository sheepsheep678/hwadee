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
      <div class="welcome-text">
        <h2>工作台</h2>
        <p>欢迎使用智慧医养大数据公共服务平台</p>
      </div>

      <div class="welcome-badge">医生工作端</div>
    </div>

    <el-row :gutter="20">
      <el-col v-for="item in cards" :key="item.path" :xs="24" :sm="12" :lg="8">
        <el-card class="module-card" shadow="hover" @click="router.push(item.path)">
          <div class="module-icon">{{ item.title.charAt(0) }}</div>

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

/* ===== 顶部标题区 ===== */
.welcome {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 24px;
  padding: 22px 26px;
  border: 1px solid rgba(226, 232, 240, 0.9);
  border-radius: 16px;
  background: linear-gradient(135deg, rgba(15, 118, 110, 0.08), rgba(43, 127, 212, 0.08));
}

.welcome h2 {
  margin: 0;
  font-size: 22px;
  font-weight: 700;
  color: #1f3b4d;
}

.welcome p {
  margin-top: 8px;
  font-size: 14px;
  color: #5b7b8a;
}

.welcome-badge {
  flex: none;
  padding: 6px 14px;
  border: 1px solid rgba(15, 118, 110, 0.18);
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.8);
  font-size: 13px;
  color: #0f766e;
}

/* ===== 模块卡片 ===== */
.module-card {
  position: relative;
  overflow: hidden;
  min-height: 172px;
  cursor: pointer;
  margin-bottom: 20px;
  transition: transform 0.25s, box-shadow 0.25s;
}

.module-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: var(--accent, #0f766e);
}

.module-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 16px 32px rgba(15, 118, 110, 0.14);
}

.module-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 44px;
  height: 44px;
  border-radius: 14px;
  background: var(--accent-soft, rgba(15, 118, 110, 0.12));
  font-size: 18px;
  font-weight: 700;
  color: var(--accent, #0f766e);
}

.module-card h3 {
  margin: 14px 0 10px;
  font-size: 18px;
  font-weight: 600;
  color: #1f3b4d;
}

.module-card p {
  margin: 0;
  font-size: 14px;
  line-height: 1.6;
  color: #7b8a97;
}

.enter {
  margin-top: 20px;
  font-size: 14px;
  font-weight: 500;
  color: var(--accent, #0f766e);
  transition: letter-spacing 0.25s;
}

.module-card:hover .enter {
  letter-spacing: 0.5px;
}

/* 每个模块的主题色（仅视觉区分，不改动数据与跳转） */
.dashboard .el-col:nth-child(1) .module-card {
  --accent: #0f766e;
  --accent-soft: rgba(15, 118, 110, 0.12);
}

.dashboard .el-col:nth-child(2) .module-card {
  --accent: #2b7fd4;
  --accent-soft: rgba(43, 127, 212, 0.12);
}

.dashboard .el-col:nth-child(3) .module-card {
  --accent: #0ea5a4;
  --accent-soft: rgba(14, 165, 164, 0.14);
}

.dashboard .el-col:nth-child(4) .module-card {
  --accent: #3b82f6;
  --accent-soft: rgba(59, 130, 246, 0.12);
}

.dashboard .el-col:nth-child(5) .module-card {
  --accent: #14b8a6;
  --accent-soft: rgba(20, 184, 166, 0.14);
}

@media (max-width: 900px) {
  .welcome {
    padding: 18px;
  }

  .welcome h2 {
    font-size: 20px;
  }

  .module-card {
    min-height: 150px;
  }
}
</style>
