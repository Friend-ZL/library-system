<template>
  <div class="page">
    <div class="page-header">
      <h2 class="page-title">用户管理</h2>
      <el-button type="primary" :icon="Plus" @click="openForm">新增用户</el-button>
    </div>
    <div class="panel">
      <div class="toolbar">
        <el-input v-model="query.keyword" placeholder="用户名 / 姓名" clearable @keyup.enter="load" />
        <el-button :icon="Search" @click="load">查询</el-button>
      </div>
      <el-table :data="records" border>
        <el-table-column prop="username" label="用户名" min-width="140" />
        <el-table-column prop="realName" label="姓名" min-width="120" />
        <el-table-column prop="phone" label="手机" min-width="130" />
        <el-table-column prop="email" label="邮箱" min-width="180" />
        <el-table-column prop="role" label="角色" width="120" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-switch :model-value="row.status" :active-value="1" :inactive-value="0" @change="(value) => changeStatus(row, value)" />
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

    <el-dialog v-model="dialogVisible" title="新增用户" width="520px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名"><el-input v-model="form.username" /></el-form-item>
        <el-form-item label="密码"><el-input v-model="form.password" type="password" show-password /></el-form-item>
        <el-form-item label="姓名"><el-input v-model="form.realName" /></el-form-item>
        <el-form-item label="手机"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="邮箱"><el-input v-model="form.email" /></el-form-item>
        <el-form-item label="角色">
          <el-select v-model="form.role">
            <el-option label="普通用户" value="USER" />
            <el-option label="管理员" value="ADMIN" />
          </el-select>
        </el-form-item>
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
import { ElMessage } from 'element-plus'
import { Plus, Search } from '@element-plus/icons-vue'
import { changeUserStatus, createUser, getUsers } from '../../api/user'

const query = reactive({ keyword: '', page: 1, size: 10 })
const records = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const form = reactive({})

const resetForm = () => Object.assign(form, {
  username: '',
  password: '',
  realName: '',
  phone: '',
  email: '',
  role: 'USER',
  status: 1
})
const load = async () => {
  const data = await getUsers(query)
  records.value = data.records
  total.value = data.total
}
const openForm = () => {
  resetForm()
  dialogVisible.value = true
}
const save = async () => {
  await createUser(form)
  dialogVisible.value = false
  ElMessage.success('新增成功')
  load()
}
const changeStatus = async (row, status) => {
  await changeUserStatus(row.id, status)
  ElMessage.success('状态已更新')
  load()
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
