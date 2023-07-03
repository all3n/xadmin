<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission"/>
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0"
                 :title="crud.status.title" width="850px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="100px">
          <el-form-item label="name" prop="name">
            <el-input v-model="form.name" style="width: 670px;"/>
          </el-form-item>
          <el-form-item label="应用" prop="appId">
            <el-select
              style="width: 670px;"
              v-model="form.appId"
              filterable
              remote
              reserve-keyword
              placeholder="请输入关键词"
              :remote-method="fetchAppList"
              :loading="loading_app">
              <el-option
                v-for="item in options_app"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="父配置" prop="parentId">
            <!--            <el-input v-model="form.parentId" style="width: 670px;"/>-->
            <el-select
              style="width: 670px;"
              v-model="form.parentId"
              filterable
              remote
              clearable
              reserve-keyword
              placeholder="请输入关键词"
              :remote-method="fetchConfigList"
              :loading="loading_cfg">
              <el-option
                v-for="item in options_cfg"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="环境" prop="envId">
            <!--            <el-input v-model="form.envId" style="width: 670px;"/>-->
            <el-select
              style="width: 670px;"
              v-model="form.envId"
              filterable
              remote
              reserve-keyword
              placeholder="请输入关键词"
              :remote-method="fetchEnvList"
              :loading="loading_env">
              <el-option
                v-for="item in options_env"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="配置内容" prop="content">
            <el-input v-model="form.content" type="textarea" style="width: 670px; " size="medium"/>
            <!--            <JsonEdit :value="form.content" height="500px;"></JsonEdit>-->
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="text" @click="crud.cancelCU">取消</el-button>
          <el-button :loading="crud.status.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
        </div>
      </el-dialog>
      <!--表格渲染-->
      <el-table ref="table" v-loading="crud.loading" :data="crud.data" size="small" style="width: 100%;"
                @selection-change="crud.selectionChangeHandler" @sort-change="crud.sortChangeHandler">
        <el-table-column type="selection" width="55"/>
        <el-table-column prop="cfgId" label="ID"/>
        <el-table-column prop="name" label="name"/>
        <el-table-column prop="appName" label="app name">
          <template v-slot:default="scope">
            <el-tooltip :content="scope.row.appId.toString()" placement="top">
              <el-tag>{{scope.row.appName}}</el-tag>
            </el-tooltip>
          </template>
        </el-table-column>
<!--        <el-table-column prop="parentId" label="parent id"/>-->
        <el-table-column prop="parentId" label="parent ">
          <template v-slot:default="scope">
            <el-tooltip :content="scope.row.parentId.toString()" placement="top">
              <el-tag>{{scope.row.parentName}}</el-tag>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column prop="envId" label="env id"/>
        <el-table-column prop="content" label="config"/>
        <el-table-column prop="createBy" label="创建者"/>
        <el-table-column prop="updateBy" label="更新者"/>
        <el-table-column prop="createTime" label="创建日期"/>
        <el-table-column prop="updateTime" label="更新时间"/>
        <el-table-column v-if="checkPer(['admin','xadminDeployConfig:edit','xadminDeployConfig:del'])" label="操作"
                         width="150px" align="center">
          <template slot-scope="scope">
            <udOperation
              :data="scope.row"
              :permission="permission"
            />
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <pagination/>
    </div>
  </div>
</template>

<script>
import crudXadminApp from '@/api/xadminApp'
import crudXadminEnv from '@/api/xadminEnv'
import crudXadminDeployConfig from '@/api/xadminDeployConfig'
import CRUD, {presenter, header, form, crud} from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import JsonEdit from '@/components/JsonEdit/index'


const defaultForm = {
  cfgId: null,
  name: null,
  appId: null,
  parentId: null,
  envId: null,
  content: '',
  createBy: null,
  updateBy: null,
  createTime: null,
  updateTime: null
}
export default {
  name: 'XadminDeployConfig',
  components: {pagination, crudOperation, rrOperation, udOperation, JsonEdit},
  mixins: [presenter(), header(), form(defaultForm), crud()],
  cruds() {
    return CRUD({
      title: '部署配置',
      url: 'api/xadminDeployConfig',
      idField: 'cfgId',
      sort: 'cfgId,desc',
      crudMethod: {...crudXadminDeployConfig}
    })
  },
  data() {
    return {
      options_app: [],
      options_env: [],
      options_cfg: [],
      loading_app: false,
      loading_env: false,
      loading_cfg: false,
      permission: {
        add: ['admin', 'xadminDeployConfig:add'],
        edit: ['admin', 'xadminDeployConfig:edit'],
        del: ['admin', 'xadminDeployConfig:del']
      },
      rules: {
        name: [
          {required: true, message: 'name不能为空', trigger: 'blur'}
        ],
        appId: [
          {required: true, message: 'app id不能为空', trigger: 'blur'}
        ],
        parentId: [
          {required: true, message: 'parent id不能为空', trigger: 'blur'}
        ],
        envId: [
          {required: true, message: 'env id不能为空', trigger: 'blur'}
        ],
        // content: [
        //   {required: true, message: 'config不能为空', trigger: 'blur'}
        // ]
      }
    }
  },
  mounted() {
    this.fetchAppList({})
    this.fetchEnvList({})
    this.fetchConfigList({})
  },
  methods: {
    // 钩子：在获取表格数据之前执行，false 则代表不获取数据
    [CRUD.HOOK.beforeRefresh]() {
      return true
    },
    fetchAppList(query) {
      this.loading_app = true;
      crudXadminApp.get({"name": query, "size": 10, "page": 0}).then(res => {
        this.loading_app = false
        var data = []
        for (var k in res.content) {
          data.push({'value': res.content[k].appId, 'label': res.content[k].name})
        }
        this.options_app = data
      })
    },
    fetchEnvList(query) {
      this.loading_env = true;
      crudXadminEnv.get({"name": query, "size": 10, "page": 0}).then(res => {
        this.loading_env = false
        var data = []
        for (var k in res.content) {
          data.push({'value': res.content[k].envId, 'label': res.content[k].name})
        }
        this.options_env = data
      })
    },
    fetchConfigList(query) {
      this.loading_cfg = true;
      crudXadminDeployConfig.get({"name": query, "size": 10, "page": 0}).then(res => {
        this.loading_cfg = false
        var data = []
        data.push({'value': 0, 'label': '顶层配置'})
        for (var k in res.content) {
          data.push({'value': res.content[k].cfgId, 'label': res.content[k].name})
        }
        this.options_cfg = data
      })
    }
  }
}
</script>

<style scoped>

</style>
