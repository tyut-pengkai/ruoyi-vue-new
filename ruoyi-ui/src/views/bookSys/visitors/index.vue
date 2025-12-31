<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="姓名" prop="fullName">
        <el-input
          v-model="queryParams.fullName"
          placeholder="请输入姓名"
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
      <el-form-item label="身份证号" prop="idCard">
        <el-input
          v-model="queryParams.idCard"
          placeholder="请输入身份证号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="性别(1:男/2:女/0:未知)" prop="gender">
        <el-select v-model="queryParams.gender" placeholder="请选择性别(1:男/2:女/0:未知)" clearable>
          <el-option
            v-for="dict in dict.type.sys_user_sex"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="照片路径" prop="photoPath">
        <el-input
          v-model="queryParams.photoPath"
          placeholder="请输入照片路径"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="车牌号" prop="carPlate">
        <el-input
          v-model="queryParams.carPlate"
          placeholder="请输入车牌号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否黑名单" prop="blacklistFlag">
        <el-input
          v-model="queryParams.blacklistFlag"
          placeholder="请输入是否黑名单"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="黑名单原因" prop="blacklistReason">
        <el-input
          v-model="queryParams.blacklistReason"
          placeholder="请输入黑名单原因"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="来访次数" prop="visitCount">
        <el-input
          v-model="queryParams.visitCount"
          placeholder="请输入来访次数"
          clearable
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['bookSys:visitors:add']"
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
          v-hasPermi="['bookSys:visitors:edit']"
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
          v-hasPermi="['bookSys:visitors:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['bookSys:visitors:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="visitorsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="访客ID" align="center" prop="visitorId" />
      <el-table-column label="姓名" align="center" prop="fullName" />
      <el-table-column label="联系电话" align="center" prop="phone" />
      <el-table-column label="身份证号" align="center" prop="idCard" />
      <el-table-column label="性别(1:男/2:女/0:未知)" align="center" prop="gender">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_user_sex" :value="scope.row.gender"/>
        </template>
      </el-table-column>
      <el-table-column label="照片路径" align="center" prop="photoPath" />
      <el-table-column label="车牌号" align="center" prop="carPlate" />
      <el-table-column label="是否黑名单" align="center" prop="blacklistFlag" />
      <el-table-column label="黑名单原因" align="center" prop="blacklistReason" />
      <el-table-column label="备注信息" align="center" prop="notes" />
      <el-table-column label="来访次数" align="center" prop="visitCount" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['bookSys:visitors:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['bookSys:visitors:remove']"
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

    <!-- 添加或修改访客信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="姓名" prop="fullName">
          <el-input v-model="form.fullName" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="form.idCard" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="性别(1:男/2:女/0:未知)" prop="gender">
          <el-radio-group v-model="form.gender">
            <el-radio
              v-for="dict in dict.type.sys_user_sex"
              :key="dict.value"
              :label="parseInt(dict.value)"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="照片路径" prop="photoPath">
          <el-input v-model="form.photoPath" placeholder="请输入照片路径" />
        </el-form-item>
        <el-form-item label="车牌号" prop="carPlate">
          <el-input v-model="form.carPlate" placeholder="请输入车牌号" />
        </el-form-item>
        <el-form-item label="是否黑名单" prop="blacklistFlag">
          <el-input v-model="form.blacklistFlag" placeholder="请输入是否黑名单" />
        </el-form-item>
        <el-form-item label="黑名单原因" prop="blacklistReason">
          <el-input v-model="form.blacklistReason" placeholder="请输入黑名单原因" />
        </el-form-item>
        <el-form-item label="备注信息" prop="notes">
          <el-input v-model="form.notes" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="来访次数" prop="visitCount">
          <el-input v-model="form.visitCount" placeholder="请输入来访次数" />
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
          <el-table-column label="审核人ID" prop="approverId" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.approverId" placeholder="请输入审核人ID" />
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
import { listVisitors, getVisitors, delVisitors, addVisitors, updateVisitors } from "@/api/bookSys/visitors"

export default {
  name: "Visitors",
  dicts: ['sys_user_sex'],
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
      // 访客信息表格数据
      visitorsList: [],
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
        fullName: null,
        phone: null,
        idCard: null,
        gender: null,
        photoPath: null,
        carPlate: null,
        blacklistFlag: null,
        blacklistReason: null,
        notes: null,
        visitCount: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        fullName: [
          { required: true, message: "姓名不能为空", trigger: "blur" }
        ],
        phone: [
          { required: true, message: "联系电话不能为空", trigger: "blur" }
        ],
        idCard: [
          { required: true, message: "身份证号不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询访客信息列表 */
    getList() {
      this.loading = true
      listVisitors(this.queryParams).then(response => {
        this.visitorsList = response.rows
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
        visitorId: null,
        fullName: null,
        phone: null,
        idCard: null,
        gender: null,
        photoPath: null,
        carPlate: null,
        blacklistFlag: null,
        blacklistReason: null,
        notes: null,
        visitCount: null,
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
      this.ids = selection.map(item => item.visitorId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加访客信息"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const visitorId = row.visitorId || this.ids
      getVisitors(visitorId).then(response => {
        this.form = response.data
        this.visitRecordsList = response.data.visitRecordsList
        this.open = true
        this.title = "修改访客信息"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.visitRecordsList = this.visitRecordsList
          if (this.form.visitorId != null) {
            updateVisitors(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addVisitors(this.form).then(response => {
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
      const visitorIds = row.visitorId || this.ids
      this.$modal.confirm('是否确认删除访客信息编号为"' + visitorIds + '"的数据项？').then(function() {
        return delVisitors(visitorIds)
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
      obj.residentId = ""
      obj.visitPurpose = ""
      obj.expectedArrival = ""
      obj.actualArrival = ""
      obj.actualLeave = ""
      obj.status = ""
      obj.approvalStatus = ""
      obj.approverId = ""
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
      this.download('bookSys/visitors/export', {
        ...this.queryParams
      }, `visitors_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
