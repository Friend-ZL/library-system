<template>
  <div class="page">
    <div class="page-header">
      <h2 class="page-title">借阅审批</h2>
    </div>
    <div class="panel">
      <div class="toolbar">
        <el-select v-model="query.status" placeholder="全部状态" clearable>
          <el-option v-for="item in statuses" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <el-button :icon="Search" @click="load">查询</el-button>
      </div>
      <el-table :data="records" border>
        <el-table-column prop="id" label="编号" width="80" />
        <el-table-column prop="userId" label="用户ID" width="100" />
        <el-table-column prop="bookId" label="图书ID" width="100" />
        <el-table-column prop="borrowDate" label="借出时间" min-width="170" />
        <el-table-column prop="dueDate" label="应还时间" min-width="170" />
        <el-table-column prop="returnDate" label="归还时间" min-width="170" />
        <el-table-column label="状态" width="110">
          <template #default="{ row }">
            <el-tag>{{ statusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="230" fixed="right">
          <template #default="{ row }">
            <el-button v-if="row.status === 'APPLYING'" size="small" type="primary" @click="approve(row)">通过</el-button>
            <el-button v-if="row.status === 'APPLYING'" size="small" @click="reject(row)">拒绝</el-button>
            <el-button v-if="['BORROWED', 'OVERDUE'].includes(row.status)" size="small" type="success" @click="returnBook(row)">归还</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        layout="total, prev, pager, next"
        :total="total"
        :page-size="query.size"
        v-model:current-page="query.page"
        @current-change="load"
      />
    </div>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import { approveBorrow, getBorrows, rejectBorrow, returnBorrow } from '../../api/borrow'

const statuses = [
  { value: 'APPLYING', label: '申请中' },
  { value: 'BORROWED', label: '已借出' },
  { value: 'RETURNED', label: '已归还' },
  { value: 'OVERDUE', label: '已逾期' },
  { value: 'REJECTED', label: '已拒绝' }
]
const query = reactive({ status: '', page: 1, size: 10 })
const records = ref([])
const total = ref(0)

const statusText = (value) => statuses.find((item) => item.value === value)?.label || value
const load = async () => {
  const data = await getBorrows(query)
  records.value = data.records
  total.value = data.total
}
const approve = async (row) => {
  await approveBorrow(row.id)
  ElMessage.success('已审批通过')
  load()
}
const reject = async (row) => {
  await rejectBorrow(row.id)
  ElMessage.success('已拒绝')
  load()
}
const returnBook = async (row) => {
  await returnBorrow(row.id)
  ElMessage.success('已归还')
  load()
}

onMounted(load)
</script>

<style scoped>
.el-pagination {
  margin-top: 14px;
  justify-content: flex-end;
}
</style>
