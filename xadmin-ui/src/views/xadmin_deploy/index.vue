<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="app id" prop="appId">
            <el-input v-model="form.appId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="cfg id" prop="cfgId">
            <el-input v-model="form.cfgId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="env id" prop="envId">
            <el-input v-model="form.envId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="revision" prop="revision">
            <el-input v-model="form.revision" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="status" prop="status">
            <el-input v-model="form.status" style="width: 370px;" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="text" @click="crud.cancelCU">取消</el-button>
          <el-button :loading="crud.status.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
        </div>
      </el-dialog>
      <!--表格渲染-->
      <el-table ref="table" v-loading="crud.loading" :data="crud.data" size="small" style="width: 100%;" @selection-change="crud.selectionChangeHandler" @sort-change="crud.sortChangeHandler">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="deployId" label="ID" />
        <el-table-column prop="appId" label="app id" />
        <el-table-column prop="cfgId" label="cfg id" />
        <el-table-column prop="envId" label="env id" />
        <el-table-column prop="revision" label="revision" />
        <el-table-column prop="status" label="status" />
        <el-table-column prop="createBy" label="创建者" />
        <el-table-column prop="updateBy" label="更新者" />
        <el-table-column prop="createTime" label="创建日期" />
        <el-table-column prop="updateTime" label="更新时间" />
        <el-table-column v-if="checkPer(['admin','xadminDeploy:edit','xadminDeploy:del'])" label="操作" width="150px" align="center">
          <template slot-scope="scope">
            <udOperation
              :data="scope.row"
              :permission="permission"
            />
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <pagination />
    </div>
  </div>
</template>

<script>
import crudXadminDeploy from '@/api/xadminDeploy'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'

const defaultForm = { deployId: null, appId: null, cfgId: null, envId: null, revision: null, status: null, createBy: null, updateBy: null, createTime: null, updateTime: null }
export default {
  name: 'XadminDeploy',
  components: { pagination, crudOperation, rrOperation, udOperation },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  cruds() {
    return CRUD({ title: 'XadminDeploy', url: 'api/xadminDeploy', idField: 'deployId', sort: 'deployId,desc', crudMethod: { ...crudXadminDeploy }})
  },
  data() {
    return {
      permission: {
        add: ['admin', 'xadminDeploy:add'],
        edit: ['admin', 'xadminDeploy:edit'],
        del: ['admin', 'xadminDeploy:del']
      },
      rules: {
        appId: [
          { required: true, message: 'app id不能为空', trigger: 'blur' }
        ],
        cfgId: [
          { required: true, message: 'cfg id不能为空', trigger: 'blur' }
        ],
        envId: [
          { required: true, message: 'env id不能为空', trigger: 'blur' }
        ],
        revision: [
          { required: true, message: 'revision不能为空', trigger: 'blur' }
        ],
        status: [
          { required: true, message: 'status不能为空', trigger: 'blur' }
        ]
      }    }
  },
  methods: {
    // 钩子：在获取表格数据之前执行，false 则代表不获取数据
    [CRUD.HOOK.beforeRefresh]() {
      return true
    }
  }
}
</script>

<style scoped>

</style>
