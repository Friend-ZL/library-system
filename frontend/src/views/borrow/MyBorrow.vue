<template>
  <div class="page">
    <div class="page-header">
      <h2 class="page-title">我的借阅</h2>
    </div>
    <div class="panel">
      <el-table :data="records" border>
        <el-table-column prop="id" label="编号" width="80" />
        <el-table-column prop="bookId" label="图书ID" width="100" />
        <el-table-column prop="borrowDate" label="借出时间" min-width="170" />
        <el-table-column prop="dueDate" label="应还时间" min-width="170" />
        <el-table-column prop="returnDate" label="归还时间" min-width="170" />
        <el-table-column label="状态" width="110">
          <template #default="{ row }">
            <el-tag>{{ statusText(row.status) }}</el-tag>
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
import { getMyBorrows } from '../../api/borrow'

const statusMap = {
  APPLYING: '申请中',
  BORROWED: '已借出',
  RETURNED: '已归还',
  OVERDUE: '已逾期',
  REJECTED: '已拒绝'
}
const query = reactive({ page: 1, size: 10 })
const records = ref([])
const total = ref(0)

const statusText = (value) => statusMap[value] || value
const load = async () => {
  const data = await getMyBorrows(query)
  records.value = data.records
  total.value = data.total
}

onMounted(load)
</script>

<style scoped>
.el-pagination {
  margin-top: 14px;
  justify-content: flex-end;
}
</style>
