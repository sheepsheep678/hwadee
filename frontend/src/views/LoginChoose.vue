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
          :class="`role-card--${item.key}`"
          @click="router.push(item.path)"
        >
          <div class="role-illustration">
            <svg v-if="item.key === 'doctor'" viewBox="0 0 160 160" role="img" aria-label="医生形象">
              <circle cx="80" cy="80" r="64" fill="#eaf4ff" />
              <circle
                cx="80"
                cy="80"
                r="54"
                fill="none"
                stroke="rgba(64, 158, 255, 0.2)"
                stroke-width="1.5"
                stroke-dasharray="4 8"
              />

              <rect x="72" y="74" width="16" height="18" rx="7" fill="#f3d6c2" />
              <path
                d="M44 134c3-26 18-38 36-38s33 12 36 38z"
                fill="#ffffff"
                stroke="#cfe2f8"
                stroke-width="2"
              />
              <path d="M64 98l16 16 16-16" fill="#dcebfc" stroke="#cfe2f8" stroke-width="2" />

              <rect x="72" y="110" width="16" height="16" rx="4" fill="#409eff" />
              <rect x="78.5" y="113" width="3" height="10" rx="1.5" fill="#ffffff" />
              <rect x="75" y="116.5" width="10" height="3" rx="1.5" fill="#ffffff" />

              <path
                d="M70 96c-10 5-15 16-15 28"
                fill="none"
                stroke="#2b7fd4"
                stroke-width="3"
                stroke-linecap="round"
              />
              <path
                d="M90 96c10 5 15 16 15 28"
                fill="none"
                stroke="#2b7fd4"
                stroke-width="3"
                stroke-linecap="round"
              />
              <circle cx="105" cy="126" r="5" fill="#2b7fd4" />

              <circle cx="80" cy="58" r="23" fill="#fbe3d0" />
              <path
                d="M57 54c0-14 10-22 23-22s23 8 23 22c-6-6-14-9-23-9s-17 3-23 9z"
                fill="#3b4a5a"
              />
              <circle cx="72" cy="59" r="2.6" fill="#33475a" />
              <circle cx="88" cy="59" r="2.6" fill="#33475a" />
              <path
                d="M73 68c2.6 3 11.4 3 14 0"
                fill="none"
                stroke="#d98f7f"
                stroke-width="2.4"
                stroke-linecap="round"
              />
            </svg>

            <svg v-else viewBox="0 0 160 160" role="img" aria-label="老人形象">
              <circle cx="80" cy="80" r="64" fill="#ecf8ee" />
              <circle
                cx="80"
                cy="80"
                r="54"
                fill="none"
                stroke="rgba(103, 194, 58, 0.22)"
                stroke-width="1.5"
                stroke-dasharray="4 8"
              />

              <rect x="72" y="74" width="16" height="18" rx="7" fill="#f0d3bd" />
              <path
                d="M44 134c3-26 18-38 36-38s33 12 36 38z"
                fill="#8fd06a"
                stroke="#6fbe4a"
                stroke-width="2"
              />
              <path d="M70 100l10 9 10-9" fill="#a8db8a" stroke="#6fbe4a" stroke-width="2" />
              <path
                d="M80 112c-4.5-4-9-1.6-9 2.6 0 3.4 4.6 6.6 9 9.4 4.4-2.8 9-6 9-9.4 0-4.2-4.5-6.6-9-2.6z"
                fill="#ff8a80"
              />

              <path
                d="M108 104c6 6 8 16 8 28"
                fill="none"
                stroke="#b98b4a"
                stroke-width="4"
                stroke-linecap="round"
              />
              <path
                d="M105 104c5 0 7 3 4 5"
                fill="none"
                stroke="#f0d3bd"
                stroke-width="6"
                stroke-linecap="round"
              />

              <circle cx="80" cy="58" r="23" fill="#f7e0cb" />
              <path
                d="M57 55c0-13 10-22 23-22s23 9 23 22c-8-6-15-8-23-8s-15 2-23 8z"
                fill="#dfe6ec"
              />
              <circle cx="58" cy="62" r="6" fill="#dfe6ec" />
              <circle cx="102" cy="62" r="6" fill="#dfe6ec" />
              <circle cx="72" cy="59" r="2.6" fill="#33475a" />
              <circle cx="88" cy="59" r="2.6" fill="#33475a" />
              <circle cx="66" cy="66" r="4" fill="rgba(240, 150, 130, 0.35)" />
              <circle cx="94" cy="66" r="4" fill="rgba(240, 150, 130, 0.35)" />
              <path
                d="M73 68c2.6 3 11.4 3 14 0"
                fill="none"
                stroke="#c98a78"
                stroke-width="2.4"
                stroke-linecap="round"
              />
            </svg>
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
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100vw;
  min-height: 100vh;
  padding: 56px 40px;
  overflow: hidden;
  box-sizing: border-box;
  font-family:
    'PingFang SC', 'Microsoft YaHei', 'Noto Sans SC', 'Helvetica Neue', Arial, sans-serif;
  background:
    radial-gradient(circle at 16% 20%, rgba(45, 212, 191, 0.16), transparent 45%),
    radial-gradient(circle at 84% 18%, rgba(125, 211, 252, 0.18), transparent 42%),
    radial-gradient(circle at 60% 94%, rgba(255, 255, 255, 0.92), transparent 55%),
    linear-gradient(135deg, #f4fbfa 0%, #eef6ff 52%, #ffffff 100%);
}

.choose-box {
  width: 100%;
  max-width: 760px;
}

.title-box {
  text-align: center;
  margin-bottom: 36px;
}

.title-box h1 {
  margin: 0;
  font-size: 30px;
  font-weight: 700;
  color: #123f52;
  letter-spacing: 1px;
}

.title-box p {
  margin-top: 12px;
  font-size: 16px;
  color: #5b7b8a;
}

.cards {
  display: flex;
  gap: 28px;
}

.role-card {
  flex: 1;
  padding: 36px 32px;
  border: 1px solid rgba(255, 255, 255, 0.8);
  border-radius: 24px;
  background: rgba(255, 255, 255, 0.72);
  box-shadow: 0 18px 44px rgba(15, 118, 110, 0.1);
  backdrop-filter: blur(14px);
  -webkit-backdrop-filter: blur(14px);
  text-align: center;
  cursor: pointer;
  transition: transform 0.25s, box-shadow 0.25s;
}

.role-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 24px 56px rgba(15, 118, 110, 0.16);
}

.role-card--doctor {
  --accent: #2b7fd4;
}

.role-card--elder {
  --accent: #67c23a;
}

.role-illustration {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 150px;
  height: 150px;
  margin: 0 auto 18px;
}

.role-illustration svg {
  display: block;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.role-title {
  font-size: 20px;
  font-weight: 600;
  color: #25384b;
}

.role-desc {
  margin-top: 12px;
  min-height: 40px;
  font-size: 14px;
  color: #7b8a97;
}

.role-enter {
  margin-top: 20px;
  font-size: 14px;
  font-weight: 500;
  color: var(--accent, #409eff);
}

.footer-text {
  margin-top: 34px;
  text-align: center;
  font-size: 13px;
  color: #9db2bd;
}

@media (max-width: 768px) {
  .choose-page {
    padding: 32px 18px;
    overflow: auto;
  }

  .choose-box {
    max-width: 420px;
  }

  .title-box {
    margin-bottom: 28px;
  }

  .title-box h1 {
    font-size: 24px;
  }

  .cards {
    flex-direction: column;
  }

  .role-illustration {
    width: 130px;
    height: 130px;
    margin-bottom: 14px;
  }
}
</style>
