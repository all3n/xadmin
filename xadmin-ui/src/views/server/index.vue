<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <label class="el-form-item-label">服务器ID</label>
        <el-input
          v-model="query.serverId"
          clearable
          placeholder="Server ID"
          style="width: 185px;"
          class="filter-item"
          @keyup.enter.native="crud.toQuery"
        />
        <label class="el-form-item-label">地址</label>
        <el-input
          v-model="query.host"
          clearable
          placeholder="host or ip"
          style="width: 185px;"
          class="filter-item"
          @keyup.enter.native="crud.toQuery"
        />
        <label class="el-form-item-label">状态</label>
        <el-select v-model="query.status" :clearable="true" placeholder="状态" class="filter-item" @change="crud.toQuery">
          <el-option label="正常" value="0" />
          <el-option label="禁用" value="1" />
        </el-select>
        <label class="el-form-item-label">地址</label>
        <el-input
          v-model="query.meta"
          clearable
          placeholder="meta json"
          style="width: 185px;"
          class="filter-item"
          @keyup.enter.native="crud.toQuery"
        />
        <rrOperation :crud="crud" />
      </div>
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission">
        <template v-slot:left="query">
          <el-button
            class="filter-item"
            size="mini"
            type="success"
            icon="el-icon-circle-plus-outline"
            @click="batchAddVisable=true"
          >批量添加
          </el-button>
        </template>
      </crudOperation>

      <el-dialog title="批量添加" :visible.sync="batchAddVisable" witdh="600px">
        <el-form :model="form">
          <el-form-item label="服务器列表">
            <el-input v-model="batchAddForm.servers" placeholder="hostname/ip" rows="20" cols="10" type="textarea" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="batchAddVisable = false">取 消</el-button>
          <el-button type="primary" @click="doBatchAdd">确 定</el-button>
        </div>
      </el-dialog>
      <!--表单组件-->
      <el-dialog
        :close-on-click-modal="false"
        :before-close="crud.cancelCU"
        :visible.sync="crud.status.cu > 0"
        :title="crud.status.title"
        width="500px"
      >
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="地址" prop="host">
            <el-input v-model="form.host" placeholder="域名或者ip" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="meta" prop="meta">
            <el-input v-model="form.meta" type="textarea" placeholder="meta json(max 255)" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-switch
              v-model="form.status"
              active-color="#13ce66"
              inactive-color="#ff4949"
              :active-value="0"
              :inactive-value="1"
            />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="text" @click="crud.cancelCU">取消</el-button>
          <el-button :loading="crud.status.cu === 2" type="primary" @click="crud.submitCU">确认
          </el-button>
        </div>
      </el-dialog>
      <!--表格渲染-->
      <el-table
        ref="table"
        v-loading="crud.loading"
        :data="crud.data"
        size="small"
        style="width: 100%;"
        @selection-change="crud.selectionChangeHandler"
        @sort-change="crud.sortChangeHandler"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="serverId" label="服务器id" width="100" sortable />
        <el-table-column prop="host" label="地址" width="400">
          <template v-slot="scope">
            {{ scope.row.host }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" sortable>
          <template v-slot="scope">
            <el-tag
              :type="scope.row.status === 0 ? 'success' : 'danger'"
              disable-transitions
            >{{ scope.row.status === 0 ? '正常' : '禁止' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="meta" label="meta" />
        <el-table-column
          v-if="checkPer(['admin','server:edit','server:del'])"
          label="操作"
          width="150px"
          align="center"
        >
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
import crudServer from '@/api/server'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'

const defaultForm = { serverId: null, host: null, status: 0, meta: null }
export default {
  name: 'Server',
  components: { pagination, crudOperation, rrOperation, udOperation },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  dicts: ['status'],
  cruds() {
    return CRUD({
      title: 'Server',
      url: 'api/server',
      idField: 'serverId',
      sort: 'serverId,desc',
      crudMethod: { ...crudServer }
    })
  },
  data() {
    var validateMeta = (rule, value, callback) => {
      try {
        if (value) {
          JSON.parse(value)
          callback()
        } else {
          callback()
        }
      } catch (e) {
        callback(new Error(e))
      }
    }
    return {
      batchAddForm: {
        servers: ''
      },
      batchAddVisable: false,
      permission: {
        add: ['admin', 'server:add'],
        edit: ['admin', 'server:edit'],
        del: ['admin', 'server:del']
      },
      rules: {
        host: [
          { required: true, message: 'host or ip不能为空', trigger: 'blur' }
        ],
        status: [
          { required: true, message: 'status 0 ok 1 disable不能为空', trigger: 'blur' }
        ],
        meta: [
          { validator: validateMeta, trigger: 'blur' }
        ]
      },
      queryTypeOptions: [
        { key: 'serverId', display_name: 'Server ID' },
        { key: 'host', display_name: 'host or ip' },
        { key: 'status', display_name: 'status 0 ok 1 disable' }
      ]
    }
  },
  computed: {
  },
  mounted: function() {
  },
  methods: {
    doBatchAdd() {
      console.log('batch add')
      var that = this
      crudServer.batchAdd({ 'servers': this.batchAddForm.servers }).then(function(response) {
        that.crud.refresh()
      })
      this.batchAddForm.servers = ''
      this.batchAddVisable = false
    },
    // 钩子：在获取表格数据之前执行，false 则代表不获取数据
    [CRUD.HOOK.beforeRefresh]() {
      return true
    },
    showStatus(row) {
      var st = row.serverStatus
      if (st) {
        var stStr = st.status === 200 ? 'OK' : 'ERROR'
        return stStr + ':' + st.version + ':' + st.updatedTime
      } else {
        return ''
      }
    }
  }
}
</script>

<style scoped>

</style>
