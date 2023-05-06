<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <label class="el-form-item-label">GroupID</label>
        <el-input v-model="query.groupId" clearable placeholder="Group ID" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">组名称</label>
        <el-input v-model="query.name" clearable placeholder="group name" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">状态</label>
        <el-select v-model="query.status" :clearable="true" placeholder="状态" class="filter-item" @change="crud.toQuery">
          <el-option label="正常" value="0" />
          <el-option label="禁用" value="1" />
        </el-select>
        <rrOperation :crud="crud" />
      </div>
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="1000px" @open="beforeOpen">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="组名称" prop="name">
            <el-input v-model="form.name" style="width: 370px;" />
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
          <el-form-item>
            <el-tabs v-model="activeName" @tab-click="handleTabClick">
              <el-tab-pane label="服务器组配置" name="groupConfig">
                <div id="transfer" style="text-align: center">
                  <el-transfer
                    v-model="groups_servers"
                    style="text-align: left; display: inline-block; width:900px;"
                    filterable
                    :render-content="renderFunc"
                    :titles="['Source', 'Target']"
                    :format="{
                      noChecked: '${total}',
                      hasChecked: '${checked}/${total}'
                    }"
                    :data="servers_data"
                    @change="handleChange"
                  />
                </div>
              </el-tab-pane>
              <el-tab-pane label="文本配置方式" name="textGroupConfig">
                <el-input
                  v-model="groupTexts"
                  type="textarea"
                  :rows="20"
                  placeholder="请输入内容"
                />
              </el-tab-pane>
            </el-tabs>
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
        <el-table-column prop="groupId" label="组id" width="100" sortable />
        <el-table-column prop="name" label="组名称">
          <template v-slot="scope">
            {{ scope.row.name }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态">
          <template v-slot="scope">
            <el-tag
              :type="scope.row.status === 0 ? 'success' : 'danger'"
              disable-transitions
            >{{ scope.row.status === 0 ? '正常' : '禁止' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="count" label="server count" width="100" />
        <el-table-column v-if="checkPer(['admin','serverGroup:edit','serverGroup:del'])" label="操作" width="150px" align="center">
          <template v-slot="scope">
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
import crudServerGroup from '@/api/serverGroup'
import crudServer from '@/api/server'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'

const defaultForm = { groupId: null, name: null, status: 0 }
export default {
  name: 'ServerGroup',
  components: { pagination, crudOperation, rrOperation, udOperation },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  cruds() {
    return CRUD({ title: 'ServerGroup', url: 'api/serverGroup', idField: 'groupId', sort: 'groupId,desc', crudMethod: { ...crudServerGroup }})
  },
  data() {
    return {
      activeName: 'groupConfig',
      groupTexts: '',
      servers_data: [],
      groups_servers: [],
      renderFunc(h, option) {
        return <span>{ option.key } - { option.label }</span>
      },
      permission: {
        add: ['admin', 'serverGroup:add'],
        edit: ['admin', 'serverGroup:edit'],
        del: ['admin', 'serverGroup:del']
      },
      rules: {
        name: [
          { required: true, message: 'group name不能为空', trigger: 'blur' }
        ],
        status: [
          { required: true, message: 'status 0 ok 1 disable不能为空', trigger: 'blur' }
        ]
      },
      queryTypeOptions: [
        { key: 'groupId', display_name: 'Group ID' },
        { key: 'name', display_name: 'group name' },
        { key: 'status', display_name: 'status 0 ok 1 disable' }
      ]
    }
  },
  computed: {

  },
  mounted: function() {
    var that = this

    crudServer.all(true).then(function(res) {
      var content = res
      for (var i = 0; i < res.length; ++i) {
        that.servers_data.push({
          key: content[i].serverId,
          label: content[i].meta ? content[i].host + ' ' + content[i].meta : content[i].host,
          disabled: content[i].status === 1
        })
      }
    })
  },
  methods: {
    // 钩子：在获取表格数据之前执行，false 则代表不获取数据
    [CRUD.HOOK.beforeRefresh]() {
      return true
    },
    handleChange(value, direction, movedKeys) {
    },
    save_server_group(groupId) {
      crudServerGroup.updateGroup({ 'ids': this.groups_servers, 'groupId': groupId })
    },
    save_server_group_text(groupId) {
      crudServerGroup.updateGroupText({ 'hosts': this.groupTexts, 'groupId': groupId })
    },
    [CRUD.HOOK.beforeToCU]() {
      this.groupTexts = ''
      this.groups_servers = []
    },
    [CRUD.HOOK.afterSubmit](crud, data) {
      var groupId
      if (crud.form.groupId) {
        groupId = crud.form.groupId
      } else {
        groupId = data.groupId
      }
      if (this.activeName === 'groupConfig') {
        crudServerGroup.updateGroup({ 'ids': this.groups_servers, 'groupId': groupId }).then(function(res) {
          crud.toQuery()
        })
      } else {
        crudServerGroup.updateGroupText({ 'hosts': this.groupTexts, 'groupId': groupId }).then(function(res) {
          crud.toQuery()
        })
      }
      return true
    },
    beforeOpen() {
      var that = this
      if (this.form.groupId) {
        crudServerGroup.get(this.form.groupId).then(function(res) {
          if (res) {
            that.groups_servers = []
            var hosts = []
            for (var k in res) {
              that.groups_servers.push(res[k].serverId)
              if (res[k].status === 0) {
                hosts.push(res[k].host)
              }
            }
            that.groupTexts = hosts.join('\n')
          }
        })
      }
    },
    handleTabClick(tab, event) {
      // console.log(tab, event)
      if (tab.name === 'textGroupConfig') {
        //
      }
    }
  }
}
</script>

<style scoped>
#transfer >>> .el-transfer-panel {
  width:350px;
}
</style>
