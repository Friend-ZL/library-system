<template>
  <div class="login-page">
    <section class="login-visual">
      <div class="visual-copy">
        <div class="brand-lockup">
          <div class="brand-mark">L</div>
          <span>图书管理系统</span>
        </div>
        <h1>馆藏流转与借阅管理</h1>
      </div>
      <div class="shelf-scene" aria-hidden="true">
        <div class="shelf-row">
          <span class="book tall teal"></span>
          <span class="book gold"></span>
          <span class="book wide"></span>
          <span class="book slim"></span>
          <span class="book tall"></span>
        </div>
        <div class="shelf-row second">
          <span class="book gold tall"></span>
          <span class="book slim teal"></span>
          <span class="book"></span>
          <span class="book wide gold"></span>
        </div>
      </div>
    </section>

    <section class="login-panel">
      <div class="panel-heading">
        <p>Welcome back</p>
        <h2>登录控制台</h2>
      </div>
      <el-form :model="form" size="large" @keyup.enter="submit">
        <el-form-item>
          <el-input v-model="form.username" placeholder="用户名" :prefix-icon="User" />
        </el-form-item>
        <el-form-item>
          <el-input v-model="form.password" placeholder="密码" type="password" show-password :prefix-icon="Lock" />
        </el-form-item>
        <el-button type="primary" size="large" :loading="loading" @click="submit">登录</el-button>
      </el-form>
      <p class="hint">默认管理员：admin / admin123</p>
    </section>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { Lock, User } from '@element-plus/icons-vue'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const auth = useAuthStore()
const loading = ref(false)
const form = reactive({ username: 'admin', password: 'admin123' })

const submit = async () => {
  loading.value = true
  try {
    await auth.login(form)
    router.push('/')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: grid;
  grid-template-columns: minmax(0, 1.15fr) minmax(380px, 0.85fr);
  background:
    linear-gradient(135deg, rgba(18, 57, 76, 0.96), rgba(18, 33, 47, 0.92)),
    #12394c;
}

.login-visual {
  position: relative;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 56px;
  color: #fff;
}

.login-visual::before {
  content: "";
  position: absolute;
  inset: 0;
  background:
    linear-gradient(90deg, rgba(255, 255, 255, 0.08) 1px, transparent 1px),
    linear-gradient(180deg, rgba(255, 255, 255, 0.07) 1px, transparent 1px);
  background-size: 58px 58px;
  mask-image: linear-gradient(180deg, rgba(0, 0, 0, 0.7), transparent 85%);
}

.visual-copy {
  position: relative;
  z-index: 1;
}

.brand-lockup {
  display: inline-flex;
  align-items: center;
  gap: 12px;
  font-size: 17px;
  font-weight: 750;
}

.brand-mark {
  width: 44px;
  height: 44px;
  display: grid;
  place-items: center;
  border-radius: 8px;
  background: linear-gradient(145deg, #e2ad55, #1b7898);
  box-shadow: 0 16px 34px rgba(0, 0, 0, 0.25);
  font-size: 22px;
  font-weight: 820;
}

h1 {
  max-width: 620px;
  margin: 82px 0 0;
  font-size: clamp(40px, 5vw, 72px);
  line-height: 1.04;
  font-weight: 820;
}

.shelf-scene {
  position: relative;
  z-index: 1;
  width: min(620px, 88%);
}

.shelf-row {
  display: flex;
  align-items: flex-end;
  gap: 12px;
  height: 136px;
  padding: 0 18px 16px;
  border-bottom: 8px solid rgba(226, 173, 85, 0.8);
}

.shelf-row.second {
  width: 74%;
  margin-top: 24px;
  margin-left: 15%;
  opacity: 0.82;
}

.book {
  width: 58px;
  height: 104px;
  display: block;
  border-radius: 5px 5px 2px 2px;
  background: linear-gradient(90deg, #e7edf2 0 14%, #ffffff 14% 18%, #ccd8e1 18% 100%);
  box-shadow: 0 16px 28px rgba(0, 0, 0, 0.24);
}

.book.tall {
  height: 126px;
}

.book.slim {
  width: 34px;
}

.book.wide {
  width: 78px;
}

.book.teal {
  background: linear-gradient(90deg, #176b87 0 16%, #d7edf3 16% 21%, #0f536d 21% 100%);
}

.book.gold {
  background: linear-gradient(90deg, #c58b2b 0 16%, #ffe4a3 16% 21%, #9b6a1f 21% 100%);
}

.login-panel {
  align-self: center;
  justify-self: center;
  width: min(440px, calc(100vw - 34px));
  padding: 38px;
  background: rgba(255, 255, 255, 0.94);
  border: 1px solid rgba(255, 255, 255, 0.7);
  border-radius: 8px;
  box-shadow: 0 28px 80px rgba(0, 0, 0, 0.26);
  backdrop-filter: blur(16px);
}

.panel-heading {
  margin-bottom: 26px;
}

.panel-heading p {
  margin: 0 0 8px;
  color: #176b87;
  font-size: 13px;
  font-weight: 760;
  text-transform: uppercase;
}

h2 {
  margin: 0;
  color: #172033;
  font-size: 28px;
  font-weight: 800;
}

.el-button {
  width: 100%;
  height: 44px;
}

.hint {
  margin: 18px 0 0;
  color: #6b7788;
  font-size: 13px;
  text-align: center;
}

@media (max-width: 860px) {
  .login-page {
    grid-template-columns: 1fr;
    padding: 28px 0;
  }

  .login-visual {
    min-height: 340px;
    padding: 34px;
  }

  h1 {
    margin-top: 42px;
    font-size: 40px;
  }

  .shelf-scene {
    display: none;
  }
}
</style>
