<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const options = [
  {
    key: 'doctor',
    icon: '医',
    color: '#409eff',
    title: '医生工作端',
    desc: '老人档案、设备、评估、重点随访与绩效',
    path: '/doctor/login',
  },
  {
    key: 'elder',
    icon: '养',
    color: '#67c23a',
    title: '老人服务端',
    desc: '健康档案、我的设备、个人中心与消息',
    path: '/elder/login',
  },
]
</script>

<template>
  <div class="choose-page">
    <div class="choose-box">
      <div class="title-box">
        <h1>智慧医养大数据公共服务平台</h1>
        <p>请选择登录身份</p>
      </div>

      <div class="cards">
        <div
          v-for="item in options"
          :key="item.key"
          class="role-card"
          @click="router.push(item.path)"
        >
          <div class="role-icon" :style="{ background: item.color }">
            {{ item.icon }}
          </div>

          <div class="role-title">{{ item.title }}</div>
          <div class="role-desc">{{ item.desc }}</div>
          <div class="role-enter">进入 →</div>
        </div>
      </div>

      <div class="footer-text">智慧医养 · 健康服务</div>
    </div>
  </div>
</template>

<style scoped>
.choose-page {
  width: 100vw;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #eef8f6, #edf4fb);
}

.choose-box {
  width: 720px;
}

.title-box {
  text-align: center;
  margin-bottom: 40px;
}

.title-box h1 {
  margin: 0;
  font-size: 28px;
  font-weight: 600;
  color: #25384b;
}

.title-box p {
  margin-top: 12px;
  color: #7b8a97;
  font-size: 16px;
}

.cards {
  display: flex;
  gap: 24px;
}

.role-card {
  flex: 1;
  background: #fff;
  border-radius: 18px;
  padding: 40px 32px;
  text-align: center;
  cursor: pointer;
  box-shadow: 0 14px 45px rgba(31, 70, 100, 0.12);
  transition: transform 0.2s, box-shadow 0.2s;
}

.role-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 20px 55px rgba(31, 70, 100, 0.18);
}

.role-icon {
  width: 68px;
  height: 68px;
  margin: 0 auto 20px;
  border-radius: 18px;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 30px;
  font-weight: bold;
}

.role-title {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
}

.role-desc {
  margin-top: 12px;
  color: #909399;
  font-size: 14px;
  min-height: 40px;
}

.role-enter {
  margin-top: 22px;
  color: #409eff;
  font-size: 14px;
}
</style>
