<template>
  <div class="page dashboard-page">
    <div class="page-header">
      <h2 class="page-title">仪表盘</h2>
    </div>
    <div class="metric-grid">
      <div v-for="item in metrics" :key="item.label" class="metric" :class="item.tone">
        <div class="metric-top">
          <div class="metric-label">{{ item.label }}</div>
          <el-icon><component :is="item.icon" /></el-icon>
        </div>
        <div class="metric-value">{{ item.value }}</div>
      </div>
    </div>

    <div class="dashboard-grid">
      <section class="panel spotlight">
        <div>
          <p class="section-kicker">Collection</p>
          <h3>馆藏运行状态</h3>
          <p class="spotlight-text">当前馆藏、借阅和逾期数据已接入远程 MySQL，管理端可直接处理图书流转。</p>
        </div>
        <div class="spotlight-meter">
          <span></span>
        </div>
      </section>

      <section class="panel quick-list">
        <p class="section-kicker">Workflow</p>
        <h3>今日处理</h3>
        <div class="task-row">
          <span>待审批借阅</span>
          <strong>{{ overview.pendingBorrowCount ?? 0 }}</strong>
        </div>
        <div class="task-row">
          <span>逾期提醒</span>
          <strong>{{ overview.overdueCount ?? 0 }}</strong>
        </div>
        <div class="task-row">
          <span>可用图书</span>
          <strong>{{ overview.bookCount ?? 0 }}</strong>
        </div>
      </section>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { Collection, Reading, Tickets, Warning } from '@element-plus/icons-vue'
import { getOverview } from '../api/statistics'

const overview = ref({})

const metrics = computed(() => [
  { label: '图书数量', value: overview.value.bookCount ?? 0, icon: Reading, tone: 'teal' },
  { label: '用户数量', value: overview.value.userCount ?? 0, icon: Collection, tone: 'gold' },
  { label: '当前借出', value: overview.value.activeBorrowCount ?? 0, icon: Tickets, tone: 'blue' },
  { label: '逾期未还', value: overview.value.overdueCount ?? 0, icon: Warning, tone: 'red' }
])

onMounted(async () => {
  overview.value = await getOverview()
})
</script>

<style scoped>
.dashboard-grid {
  display: grid;
  grid-template-columns: minmax(0, 1.5fr) minmax(300px, 0.85fr);
  gap: 16px;
  margin-top: 18px;
}

.metric-top {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.metric-top .el-icon {
  width: 42px;
  height: 42px;
  color: #fff;
  border-radius: 8px;
  background: #176b87;
  font-size: 21px;
}

.metric.gold .el-icon {
  background: #c58b2b;
}

.metric.blue .el-icon {
  background: #315f9f;
}

.metric.red .el-icon {
  background: #b54a43;
}

.section-kicker {
  margin: 0 0 8px;
  color: #176b87;
  font-size: 12px;
  font-weight: 800;
  text-transform: uppercase;
}

h3 {
  margin: 0;
  color: #172033;
  font-size: 20px;
  font-weight: 780;
}

.spotlight {
  min-height: 230px;
  display: flex;
  justify-content: space-between;
  gap: 24px;
  background:
    linear-gradient(135deg, rgba(255, 255, 255, 0.94), rgba(235, 246, 249, 0.9)),
    #fff;
}

.spotlight-text {
  max-width: 520px;
  margin: 18px 0 0;
  color: #5e6b7b;
  line-height: 1.8;
}

.spotlight-meter {
  width: 150px;
  min-width: 150px;
  height: 150px;
  align-self: center;
  display: grid;
  place-items: center;
  border-radius: 999px;
  background: conic-gradient(#176b87 0 72%, #e5edf2 72% 100%);
}

.spotlight-meter span {
  width: 104px;
  height: 104px;
  display: block;
  border-radius: 999px;
  background: #fff;
  box-shadow: inset 0 0 0 1px #dbe4ec;
}

.quick-list {
  min-height: 230px;
}

.task-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  padding: 15px 0;
  border-bottom: 1px solid #e4ebf1;
}

.task-row:last-child {
  border-bottom: none;
}

.task-row span {
  color: #5e6b7b;
  font-weight: 650;
}

.task-row strong {
  color: #172033;
  font-size: 20px;
}

@media (max-width: 960px) {
  .dashboard-grid {
    grid-template-columns: 1fr;
  }

  .spotlight {
    flex-direction: column;
  }
}
</style>
