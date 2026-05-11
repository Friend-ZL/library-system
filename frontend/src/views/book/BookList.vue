<template>
  <div class="page">
    <div class="page-header">
      <h2 class="page-title">图书管理</h2>
      <el-button v-if="auth.isAdmin" type="primary" :icon="Plus" @click="openForm()">新增图书</el-button>
    </div>
    <div class="panel">
      <div class="toolbar">
        <el-input v-model="query.keyword" placeholder="书名 / 作者 / ISBN" clearable @keyup.enter="load" />
        <el-button :icon="Search" @click="load">查询</el-button>
      </div>
      <el-table :data="records" border>
        <el-table-column prop="title" label="书名" min-width="180" />
        <el-table-column prop="isbn" label="ISBN" width="150" />
        <el-table-column prop="author" label="作者" width="130" />
        <el-table-column prop="publisher" label="出版社" width="160" />
        <el-table-column prop="totalCount" label="总数" width="80" />
        <el-table-column prop="availableCount" label="可借" width="80" />
        <el-table-column label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '上架' : '下架' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button v-if="!auth.isAdmin" size="small" type="primary" @click="borrow(row)">借阅</el-button>
            <el-button v-if="auth.isAdmin" size="small" @click="openForm(row)">编辑</el-button>
            <el-button v-if="auth.isAdmin" size="small" type="danger" @click="remove(row)">删除</el-button>
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

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑图书' : '新增图书'" width="680px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="书名"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="ISBN"><el-input v-model="form.isbn" /></el-form-item>
        <el-form-item label="作者"><el-input v-model="form.author" /></el-form-item>
        <el-form-item label="出版社"><el-input v-model="form.publisher" /></el-form-item>
        <el-form-item label="出版日期"><el-date-picker v-model="form.publishDate" value-format="YYYY-MM-DD" /></el-form-item>
        <el-form-item label="总库存"><el-input-number v-model="form.totalCount" :min="0" /></el-form-item>
        <el-form-item label="可借数量"><el-input-number v-model="form.availableCount" :min="0" /></el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.status" :active-value="1" :inactive-value="0" />
        </el-form-item>
        <el-form-item label="简介"><el-input v-model="form.description" type="textarea" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search } from '@element-plus/icons-vue'
import { applyBorrow } from '../../api/borrow'
import { createBook, deleteBook, getBooks, updateBook } from '../../api/book'
import { useAuthStore } from '../../stores/auth'

const auth = useAuthStore()
const query = reactive({ keyword: '', page: 1, size: 10 })
const records = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const form = reactive({})

const resetForm = () => Object.assign(form, {
  id: null,
  title: '',
  isbn: '',
  author: '',
  publisher: '',
  publishDate: '',
  totalCount: 1,
  availableCount: 1,
  status: 1,
  description: ''
})

const load = async () => {
  const data = await getBooks(query)
  records.value = data.records
  total.value = data.total
}

const openForm = (row) => {
  resetForm()
  if (row) Object.assign(form, row)
  dialogVisible.value = true
}

const save = async () => {
  if (form.id) {
    await updateBook(form.id, form)
  } else {
    await createBook(form)
  }
  dialogVisible.value = false
  ElMessage.success('保存成功')
  load()
}

const remove = async (row) => {
  await ElMessageBox.confirm(`确认删除《${row.title}》？`, '删除图书')
  await deleteBook(row.id)
  ElMessage.success('删除成功')
  load()
}

const borrow = async (row) => {
  await applyBorrow(row.id)
  ElMessage.success('借阅申请已提交')
}

onMounted(() => {
  resetForm()
  load()
})
</script>

<style scoped>
.el-pagination {
  margin-top: 14px;
  justify-content: flex-end;
}
</style>
