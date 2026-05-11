<template>
  <div class="page">
    <div class="page-header">
      <h2 class="page-title">分类管理</h2>
      <el-button type="primary" :icon="Plus" @click="openForm()">新增分类</el-button>
    </div>
    <div class="panel">
      <el-table :data="records" border>
        <el-table-column prop="name" label="分类名称" />
        <el-table-column prop="parentId" label="父级ID" width="120" />
        <el-table-column prop="sort" label="排序" width="100" />
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button size="small" @click="openForm(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="remove(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑分类' : '新增分类'" width="420px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="名称"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="父级ID"><el-input-number v-model="form.parentId" :min="0" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sort" :min="0" /></el-form-item>
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
import { Plus } from '@element-plus/icons-vue'
import { createCategory, deleteCategory, getCategories, updateCategory } from '../../api/category'

const records = ref([])
const dialogVisible = ref(false)
const form = reactive({})

const resetForm = () => Object.assign(form, { id: null, name: '', parentId: 0, sort: 0 })
const load = async () => { records.value = await getCategories() }
const openForm = (row) => {
  resetForm()
  if (row) Object.assign(form, row)
  dialogVisible.value = true
}
const save = async () => {
  form.id ? await updateCategory(form.id, form) : await createCategory(form)
  dialogVisible.value = false
  ElMessage.success('保存成功')
  load()
}
const remove = async (row) => {
  await ElMessageBox.confirm(`确认删除分类 ${row.name}？`, '删除分类')
  await deleteCategory(row.id)
  ElMessage.success('删除成功')
  load()
}

onMounted(() => {
  resetForm()
  load()
})
</script>
