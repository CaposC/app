<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--用户数据-->
      <el-col
        :span="24"
        :xs="24"
      >
        <el-form
          :model="queryParams"
          ref="queryForm"
          size="small"
          :inline="true"
          v-show="showSearch"
          label-width="68px"
        >
          <el-form-item
            label="用户名称"
            prop="username"
          >
            <el-input
              v-model="queryParams.username"
              placeholder="请输入用户名称"
              clearable
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item
            label="手机号码"
            prop="phone"
          >
            <el-input
              v-model="queryParams.phone"
              placeholder="请输入手机号码"
              clearable
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select
              v-model="queryParams.status"
              placeholder="用户状态"
              clearable
              style="width: 240px"
            >
              <el-option
                v-for="dict in dict.type.sys_normal_disable"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              icon="el-icon-search"
              size="mini"
              @click="handleQuery"
            >搜索
            </el-button>
            <el-button
              icon="el-icon-refresh"
              size="mini"
              @click="resetQuery"
            >重置
            </el-button>
          </el-form-item>
        </el-form>

        <el-row
          :gutter="10"
          class="mb8"
        >
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
              v-hasPermi="['system:user:add']"
            >新增
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="success"
              plain
              icon="el-icon-edit"
              size="mini"
              :disabled="single"
              @click="handleUpdate"
              v-hasPermi="['system:user:edit']"
            >修改
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="danger"
              plain
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
              v-hasPermi="['system:user:remove']"
            >删除
            </el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table
          v-loading="loading"
          :data="userList"
          @selection-change="handleSelectionChange"
        >
          <el-table-column
            type="selection"
            width="50"
            align="center"
          />
          <el-table-column
            label="用户编号"
            align="center"
            key="id"
            prop="id"
            v-if="columns[0].visible"
            width="120"
          />
          <el-table-column
            label="用户名称"
            align="center"
            key="username"
            prop="username"
            v-if="columns[1].visible"
            :show-overflow-tooltip="true"
            width="180"
          />
          <el-table-column
            label="用户昵称"
            align="center"
            key="nickname"
            prop="nickname"
            v-if="columns[2].visible"
            :show-overflow-tooltip="true"
            width="180"
          />
          <el-table-column
            label="手机号码"
            align="center"
            key="phone"
            prop="phone"
            v-if="columns[3].visible"
            width="180"
          />
          <el-table-column
            label="状态"
            align="center"
            key="status"
            v-if="columns[4].visible"
            width="180"
          >
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.status"
                active-value="0"
                inactive-value="1"
                @change="handleStatusChange(scope.row)"
              ></el-switch>
            </template>
          </el-table-column>
          <el-table-column
            label="备注"
            align="center"
            key="remark"
            prop="remark"
            v-if="columns[6].visible"
            width="180"
          />
          <el-table-column
            label="上次登录时间"
            align="center"
            key="loginDate"
            prop="loginDate"
            v-if="columns[7].visible"
            width="180"
          />
          <el-table-column
            label="创建时间"
            align="center"
            prop="createTime"
            v-if="columns[5].visible"
            width="180"
          >
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            align="center"
            width="240"
            class-name="small-padding fixed-width"
          >
            <template
              slot-scope="scope"
              v-if="scope.row.id !== 1"
            >
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['system:user:edit']"
              >修改
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['system:user:remove']"
              >删除
              </el-button>
              <el-dropdown
                size="mini"
                @command="(command) => handleCommand(command, scope.row)"
                v-hasPermi="['system:user:resetPwd', 'system:user:edit']"
              >
                        <span class="el-dropdown-link">
                          <i class="el-icon-d-arrow-right el-icon--right"></i>更多
                        </span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item
                    command="handleResetPwd"
                    icon="el-icon-key"
                    v-hasPermi="['system:user:resetPwd']"
                  >重置密码
                  </el-dropdown-item>
                  <el-dropdown-item
                    command="handleAuthRole"
                    icon="el-icon-circle-check"
                    v-hasPermi="['system:user:edit']"
                  >分配角色
                  </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="queryParams.page"
          :limit.sync="queryParams.size"
          @pagination="getList"
        />
      </el-col>
    </el-row>

    <!-- 添加或修改用户配置对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="600px"
      append-to-body
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="80px"
      >
        <el-row>
          <el-col :span="12">
            <el-form-item
              label="用户昵称"
              prop="nickname"
            >
              <el-input
                v-model="form.nickname"
                placeholder="请输入用户昵称"
                maxlength="30"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item
              label="手机号码"
              prop="phone"
            >
              <el-input
                v-model="form.phone"
                placeholder="请输入手机号码"
                maxlength="11"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item
              v-if="form.id === undefined"
              label="用户名称"
              prop="username"
            >
              <el-input
                v-model="form.username"
                placeholder="请输入用户名称"
                maxlength="30"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              v-if="form.id === undefined"
              label="用户密码"
              prop="password"
            >
              <el-input
                v-model="form.password"
                placeholder="请输入用户密码"
                type="password"
                maxlength="20"
                show-password
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户性别">
              <el-select v-model="form.sex" placeholder="请选择性别">
                <el-option
                  v-for="dict in dict.type.sys_user_sex"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in dict.type.sys_normal_disable"
                  :key="dict.value"
                  :label="dict.value"
                >{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="角色">
              <el-select
                v-model="form.roleIds"
                multiple
                placeholder="请选择角色"
              >
                <el-option
                  v-for="item in roleOptions"
                  :key="item.id"
                  :label="item.roleName"
                  :value="item.id"
                  :disabled="item.status == 1"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input
                v-model="form.remark"
                type="textarea"
                placeholder="请输入内容"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div
        slot="footer"
        class="dialog-footer"
      >
        <el-button
          type="primary"
          @click="submitForm"
        >确 定
        </el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listUser, getUser, delUser, addUser, updateUser, resetUserPwd, changeUserStatus } from '@/api/system/user'
import { getToken } from '@/utils/auth'

export default {
  name: 'User',
  dicts: ['sys_normal_disable', 'sys_user_sex'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 用户表格数据
      userList: null,
      // 弹出层标题
      title: '',
      // 部门树选项
      deptOptions: undefined,
      // 是否显示弹出层
      open: false,
      // 默认密码
      initPassword: undefined,
      // 日期范围
      dateRange: [],
      // 角色选项
      roleOptions: [],
      // 表单参数
      form: {},
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      // 用户导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: '',
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: 'Bearer ' + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + '/system/user/importData'
      },
      // 查询参数
      queryParams: {
        page: 1,
        size: 10,
        username: undefined,
        phone: undefined,
        status: undefined
      },
      // 列信息
      columns: [
        { key: 0, label: `用户编号`, visible: true },
        { key: 1, label: `用户名称`, visible: true },
        { key: 2, label: `用户昵称`, visible: true },
        { key: 3, label: `手机号码`, visible: true },
        { key: 4, label: `状态`, visible: true },
        { key: 5, label: `创建时间`, visible: true },
        { key: 6, label: `备注`, visible: true },
        { key: 7, label: `上次登录时间`, visible: true }
      ],
      // 表单校验
      rules: {
        username: [
          { required: true, message: '用户名称不能为空', trigger: 'blur' },
          { min: 2, max: 20, message: '用户名称长度必须介于 2 和 20 之间', trigger: 'blur' }
        ],
        nickname: [
          { required: true, message: '用户昵称不能为空', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '用户密码不能为空', trigger: 'blur' },
          { min: 5, max: 20, message: '用户密码长度必须介于 5 和 20 之间', trigger: 'blur' }
        ],
        phone: [
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: '请输入正确的手机号码',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true
      listUser(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.userList = response.data.content
          this.total = response.data.totalElements
          this.loading = false
        }
      )
    },
    // 用户状态修改
    handleStatusChange(row) {
      let text = row.status === '0' ? '启用' : '停用'
      this.$modal.confirm('确认要"' + text + '""' + row.username + '"用户吗？').then(function() {
        return changeUserStatus(row.id, row.status)
      }).then(() => {
        this.$modal.msgSuccess(text + '成功')
      }).catch(function() {
        row.status = row.status === '0' ? '1' : '0'
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        username: undefined,
        nickname: undefined,
        password: undefined,
        phone: undefined,
        sex: undefined,
        status: '0',
        remark: undefined,
        postIds: [],
        roleIds: []
      }
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = []
      this.resetForm('queryForm')
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    // 更多操作触发
    handleCommand(command, row) {
      switch (command) {
        case 'handleResetPwd':
          this.handleResetPwd(row)
          break
        case 'handleAuthRole':
          this.handleAuthRole(row)
          break
        default:
          break
      }
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      getUser().then(response => {
        this.roleOptions = response.roles
        this.open = true
        this.title = '添加用户'
        this.form.password = this.initPassword
      })
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getUser(id).then(response => {
        this.form = response.data
        this.roleOptions = response.roles
        this.form.postIds = response.postIds
        this.form.roleIds = response.roleIds
        this.open = true
        this.title = '修改用户'
        this.form.password = ''
      })
    },
    /** 重置密码按钮操作 */
    handleResetPwd(row) {
      this.$prompt('请输入"' + row.username + '"的新密码', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        closeOnClickModal: false,
        inputPattern: /^.{5,20}$/,
        inputErrorMessage: '用户密码长度必须介于 5 和 20 之间'
      }).then(({ value }) => {
        resetUserPwd(row.id, value).then(response => {
          this.$modal.msgSuccess('修改成功，新密码是：' + value)
        })
      }).catch(() => {
      })
    },
    /** 分配角色操作 */
    handleAuthRole: function(row) {
      const id = row.id
      this.$router.push('/system/user-auth/role/' + id)
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id !== undefined) {
            updateUser(this.form).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addUser(this.form).then(response => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除用户编号为"' + ids + '"的数据项？').then(function() {
        return delUser(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {
      })
    }
  }
}
</script>
