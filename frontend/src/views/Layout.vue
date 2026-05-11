<template>
  <el-container class="shell">
    <el-aside width="248px" class="aside">
      <div class="brand">
        <div class="brand-mark">L</div>
        <div>
          <div class="brand-title">图书管理</div>
          <div class="brand-subtitle">Library Console</div>
        </div>
      </div>
      <el-menu router :default-active="$route.path" class="nav-menu">
        <el-menu-item v-if="auth.isAdmin" index="/dashboard">
          <el-icon><DataLine /></el-icon><span>仪表盘</span>
        </el-menu-item>
        <el-menu-item index="/books">
          <el-icon><Reading /></el-icon><span>图书查询</span>
        </el-menu-item>
        <el-menu-item v-if="auth.isAdmin" index="/categories">
          <el-icon><Collection /></el-icon><span>分类管理</span>
        </el-menu-item>
        <el-menu-item v-if="auth.isAdmin" index="/borrows">
          <el-icon><Tickets /></el-icon><span>借阅审批</span>
        </el-menu-item>
        <el-menu-item index="/my-borrows">
          <el-icon><Notebook /></el-icon><span>我的借阅</span>
        </el-menu-item>
        <el-menu-item v-if="auth.isAdmin" index="/users">
          <el-icon><User /></el-icon><span>用户管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="topbar">
        <div class="topbar-title">馆藏业务工作台</div>
        <div class="topbar-actions">
          <div class="user-chip">
            <el-icon><User /></el-icon>
            <span>{{ auth.user?.realName || auth.user?.username }}</span>
          </div>
          <el-button :icon="SwitchButton" @click="logout">退出</el-button>
        </div>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { Collection, DataLine, Notebook, Reading, SwitchButton, Tickets, User } from '@element-plus/icons-vue'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const auth = useAuthStore()

const logout = () => {
  auth.logout()
  router.push('/login')
}
</script>

<style scoped>
.shell {
  min-height: 100vh;
}

.aside {
  position: relative;
  overflow: hidden;
  background:
    linear-gradient(180deg, #12394c 0%, #18212f 58%, #111827 100%);
  box-shadow: 16px 0 40px rgba(17, 24, 39, 0.16);
}

.aside::after {
  content: "";
  position: absolute;
  right: -80px;
  bottom: -120px;
  width: 220px;
  height: 360px;
  transform: rotate(22deg);
  background: linear-gradient(180deg, rgba(197, 139, 43, 0.16), rgba(23, 107, 135, 0.05));
}

.brand {
  position: relative;
  z-index: 1;
  min-height: 88px;
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 22px 22px 18px;
  color: #fff;
}

.brand-mark {
  width: 42px;
  height: 42px;
  display: grid;
  place-items: center;
  border: 1px solid rgba(255, 255, 255, 0.28);
  border-radius: 8px;
  background: linear-gradient(145deg, #e3b15c, #176b87);
  box-shadow: 0 14px 28px rgba(0, 0, 0, 0.22);
  font-size: 21px;
  font-weight: 800;
}

.brand-title {
  font-size: 19px;
  font-weight: 780;
  line-height: 1.2;
}

.brand-subtitle {
  margin-top: 4px;
  color: rgba(255, 255, 255, 0.6);
  font-size: 12px;
  font-weight: 600;
}

.nav-menu {
  position: relative;
  z-index: 1;
  border-right: none;
  background: transparent;
  padding: 8px 12px;
}

.nav-menu :deep(.el-menu-item) {
  height: 46px;
  margin: 5px 0;
  color: rgba(255, 255, 255, 0.72);
  border-radius: 8px;
  font-weight: 650;
}

.nav-menu :deep(.el-menu-item:hover) {
  color: #fff;
  background: rgba(255, 255, 255, 0.08);
}

.nav-menu :deep(.el-menu-item.is-active) {
  color: #fff;
  background: linear-gradient(90deg, rgba(23, 107, 135, 0.95), rgba(197, 139, 43, 0.78));
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.2);
}

.topbar {
  height: 64px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 16px;
  padding: 0 26px;
  background: rgba(255, 255, 255, 0.78);
  border-bottom: 1px solid rgba(219, 228, 236, 0.9);
  backdrop-filter: blur(14px);
}

.topbar-title {
  color: #1c2a3a;
  font-size: 16px;
  font-weight: 750;
}

.topbar-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-chip {
  height: 36px;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 0 13px;
  color: #1d3144;
  background: #f7fafb;
  border: 1px solid #dbe4ec;
  border-radius: 999px;
  font-size: 13px;
  font-weight: 650;
}

.el-main {
  padding: 0;
}

@media (max-width: 760px) {
  .aside {
    width: 76px !important;
  }

  .brand {
    justify-content: center;
    padding-inline: 10px;
  }

  .brand > div:last-child,
  .nav-menu :deep(.el-menu-item span),
  .topbar-title {
    display: none;
  }
}
</style>
