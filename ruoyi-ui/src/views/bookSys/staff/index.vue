<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户名" prop="username">
        <el-input
          v-model="queryParams.username"
          placeholder="请输入用户名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="密码哈希" prop="passwordHash">
        <el-input
          v-model="queryParams.passwordHash"
          placeholder="请输入密码哈希"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="姓名" prop="fullName">
        <el-input
          v-model="queryParams.fullName"
          placeholder="请输入姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="角色(1:管理员/2:保安/3:前台)" prop="role">
        <el-input
          v-model="queryParams.role"
          placeholder="请输入角色(1:管理员/2:保安/3:前台)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="联系电话" prop="phone">
        <el-input
          v-model="queryParams.phone"
          placeholder="请输入联系电话"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="部门" prop="department">
        <el-input
          v-model="queryParams.department"
          placeholder="请输入部门"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="最后登录时间" prop="lastLogin">
        <el-date-picker clearable
          v-model="queryParams.lastLogin"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择最后登录时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['bookSys:staff:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['bookSys:staff:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['bookSys:staff:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['bookSys:staff:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="staffList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="员工ID" align="center" prop="staffId" />
      <el-table-column label="用户名" align="center" prop="username" />
      <el-table-column label="密码哈希" align="center" prop="passwordHash" />
      <el-table-column label="姓名" align="center" prop="fullName" />
      <el-table-column label="角色(1:管理员/2:保安/3:前台)" align="center" prop="role" />
      <el-table-column label="联系电话" align="center" prop="phone" />
      <el-table-column label="部门" align="center" prop="department" />
      <el-table-column label="是否在职" align="center" prop="status" />
      <el-table-column label="最后登录时间" align="center" prop="lastLogin" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.lastLogin, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['bookSys:staff:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['bookSys:staff:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改物业人员对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码哈希" prop="passwordHash">
          <el-input v-model="form.passwordHash" placeholder="请输入密码哈希" />
        </el-form-item>
        <el-form-item label="姓名" prop="fullName">
          <el-input v-model="form.fullName" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="角色(1:管理员/2:保安/3:前台)" prop="role">
          <el-input v-model="form.role" placeholder="请输入角色(1:管理员/2:保安/3:前台)" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="部门" prop="department">
          <el-input v-model="form.department" placeholder="请输入部门" />
        </el-form-item>
        <el-form-item label="最后登录时间" prop="lastLogin">
          <el-date-picker clearable
            v-model="form.lastLogin"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择最后登录时间">
          </el-date-picker>
        </el-form-item>
        <el-divider content-position="center">访客记录信息</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddVisitRecords">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteVisitRecords">删除</el-button>
          </el-col>
        </el-row>
        <el-table :data="visitRecordsList" :row-class-name="rowVisitRecordsIndex" @selection-change="handleVisitRecordsSelectionChange" ref="visitRecords">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="访客ID" prop="visitorId" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.visitorId" placeholder="请输入访客ID" />
            </template>
          </el-table-column>
          <el-table-column label="被访住户ID" prop="residentId" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.residentId" placeholder="请输入被访住户ID" />
            </template>
          </el-table-column>
          <el-table-column label="来访事由" prop="visitPurpose" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.visitPurpose" placeholder="请输入来访事由" />
            </template>
          </el-table-column>
          <el-table-column label="预计到达时间" prop="expectedArrival" width="240">
            <template slot-scope="scope">
              <el-date-picker clearable v-model="scope.row.expectedArrival" type="date" value-format="yyyy-MM-dd" placeholder="请选择预计到达时间" />
            </template>
          </el-table-column>
          <el-table-column label="实际到达时间" prop="actualArrival" width="240">
            <template slot-scope="scope">
              <el-date-picker clearable v-model="scope.row.actualArrival" type="date" value-format="yyyy-MM-dd" placeholder="请选择实际到达时间" />
            </template>
          </el-table-column>
          <el-table-column label="实际离开时间" prop="actualLeave" width="240">
            <template slot-scope="scope">
              <el-date-picker clearable v-model="scope.row.actualLeave" type="date" value-format="yyyy-MM-dd" placeholder="请选择实际离开时间" />
            </template>
          </el-table-column>
          <el-table-column label="状态(1:预约中/2:已到达/3:已离开/4:已取消)" prop="status" width="150">
            <template slot-scope="scope">
              <el-select v-model="scope.row.status" placeholder="请选择状态(1:预约中/2:已到达/3:已离开/4:已取消)">
                <el-option label="请选择字典生成" value="" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="审核状态(1:待审核/2:已同意/3:已拒绝)" prop="approvalStatus" width="150">
            <template slot-scope="scope">
              <el-select v-model="scope.row.approvalStatus" placeholder="请选择审核状态(1:待审核/2:已同意/3:已拒绝)">
                <el-option label="请选择字典生成" value="" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="拒绝原因" prop="rejectReason" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.rejectReason" placeholder="请输入拒绝原因" />
            </template>
          </el-table-column>
          <el-table-column label="访客通行码" prop="accessCode" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.accessCode" placeholder="请输入访客通行码" />
            </template>
          </el-table-column>
          <el-table-column label="通行码有效期" prop="codeExpiry" width="240">
            <template slot-scope="scope">
              <el-date-picker clearable v-model="scope.row.codeExpiry" type="date" value-format="yyyy-MM-dd" placeholder="请选择通行码有效期" />
            </template>
          </el-table-column>
          <el-table-column label="随行人数" prop="accompanyCount" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.accompanyCount" placeholder="请输入随行人数" />
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listStaff, getStaff, delStaff, addStaff, updateStaff } from "@/api/bookSys/staff"

export default {
  name: "Staff",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedVisitRecords: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 物业人员表格数据
      staffList: [],
      // 访客记录表格数据
      visitRecordsList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        username: null,
        passwordHash: null,
        fullName: null,
        role: null,
        phone: null,
        department: null,
        status: null,
        lastLogin: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        username: [
          { required: true, message: "用户名不能为空", trigger: "blur" }
        ],
        passwordHash: [
          { required: true, message: "密码哈希不能为空", trigger: "blur" }
        ],
        fullName: [
          { required: true, message: "姓名不能为空", trigger: "blur" }
        ],
        phone: [
          { required: true, message: "联系电话不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询物业人员列表 */
    getList() {
      this.loading = true
      listStaff(this.queryParams).then(response => {
        this.staffList = response.rows
        this.total = response.total
        this.loading = false
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
        staffId: null,
        username: null,
        passwordHash: null,
        fullName: null,
        role: null,
        phone: null,
        department: null,
        status: null,
        lastLogin: null,
        createTime: null,
        updateTime: null
      }
      this.visitRecordsList = []
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.staffId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加物业人员"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const staffId = row.staffId || this.ids
      getStaff(staffId).then(response => {
        this.form = response.data
        this.visitRecordsList = response.data.visitRecordsList
        this.open = true
        this.title = "修改物业人员"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.visitRecordsList = this.visitRecordsList
          if (this.form.staffId != null) {
            updateStaff(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addStaff(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const staffIds = row.staffId || this.ids
      this.$modal.confirm('是否确认删除物业人员编号为"' + staffIds + '"的数据项？').then(function() {
        return delStaff(staffIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
	/** 访客记录序号 */
    rowVisitRecordsIndex({ row, rowIndex }) {
      row.index = rowIndex + 1
    },
    /** 访客记录添加按钮操作 */
    handleAddVisitRecords() {
      let obj = {}
      obj.visitorId = ""
      obj.residentId = ""
      obj.visitPurpose = ""
      obj.expectedArrival = ""
      obj.actualArrival = ""
      obj.actualLeave = ""
      obj.status = ""
      obj.approvalStatus = ""
      obj.rejectReason = ""
      obj.accessCode = ""
      obj.codeExpiry = ""
      obj.accompanyCount = ""
      obj.notes = ""
      this.visitRecordsList.push(obj)
    },
    /** 访客记录删除按钮操作 */
    handleDeleteVisitRecords() {
      if (this.checkedVisitRecords.length == 0) {
        this.$modal.msgError("请先选择要删除的访客记录数据")
      } else {
        const visitRecordsList = this.visitRecordsList
        const checkedVisitRecords = this.checkedVisitRecords
        this.visitRecordsList = visitRecordsList.filter(function(item) {
          return checkedVisitRecords.indexOf(item.index) == -1
        })
      }
    },
    /** 复选框选中数据 */
    handleVisitRecordsSelectionChange(selection) {
      this.checkedVisitRecords = selection.map(item => item.index)
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('bookSys/staff/export', {
        ...this.queryParams
      }, `staff_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
