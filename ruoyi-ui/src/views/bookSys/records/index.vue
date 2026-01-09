<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="访客ID" prop="visitorId">
        <el-input
          v-model="queryParams.visitorId"
          placeholder="请输入访客ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="被访住户ID" prop="residentId">
        <el-input
          v-model="queryParams.residentId"
          placeholder="请输入被访住户ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="来访事由" prop="visitPurpose">
        <el-input
          v-model="queryParams.visitPurpose"
          placeholder="请输入来访事由"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="预计到达时间" prop="expectedArrival">
        <el-date-picker clearable
          v-model="queryParams.expectedArrival"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择预计到达时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="实际到达时间" prop="actualArrival">
        <el-date-picker clearable
          v-model="queryParams.actualArrival"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择实际到达时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="实际离开时间" prop="actualLeave">
        <el-date-picker clearable
          v-model="queryParams.actualLeave"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择实际离开时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="审核人ID" prop="approverId">
        <el-input
          v-model="queryParams.approverId"
          placeholder="请输入审核人ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="拒绝原因" prop="rejectReason">
        <el-input
          v-model="queryParams.rejectReason"
          placeholder="请输入拒绝原因"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="访客通行码" prop="accessCode">
        <el-input
          v-model="queryParams.accessCode"
          placeholder="请输入访客通行码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="通行码有效期" prop="codeExpiry">
        <el-date-picker clearable
          v-model="queryParams.codeExpiry"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择通行码有效期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="随行人数" prop="accompanyCount">
        <el-input
          v-model="queryParams.accompanyCount"
          placeholder="请输入随行人数"
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
          v-hasPermi="['bookSys:records:add']"
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
          v-hasPermi="['bookSys:records:edit']"
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
          v-hasPermi="['bookSys:records:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['bookSys:records:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="recordsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="记录ID" align="center" prop="recordId" />
      <el-table-column label="访客ID" align="center" prop="visitorId" />
      <el-table-column label="被访住户ID" align="center" prop="residentId" />
      <el-table-column label="来访事由" align="center" prop="visitPurpose" />
      <el-table-column label="预计到达时间" align="center" prop="expectedArrival" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.expectedArrival, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="实际到达时间" align="center" prop="actualArrival" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.actualArrival, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="实际离开时间" align="center" prop="actualLeave" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.actualLeave, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态(1:预约中/2:已到达/3:已离开/4:已取消)" align="center" prop="status" />
      <el-table-column label="审核状态(1:待审核/2:已同意/3:已拒绝)" align="center" prop="approvalStatus" />
      <el-table-column label="审核人ID" align="center" prop="approverId" />
      <el-table-column label="拒绝原因" align="center" prop="rejectReason" />
      <el-table-column label="访客通行码" align="center" prop="accessCode" />
      <el-table-column label="通行码有效期" align="center" prop="codeExpiry" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.codeExpiry, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="随行人数" align="center" prop="accompanyCount" />
      <el-table-column label="备注" align="center" prop="notes" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['bookSys:records:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['bookSys:records:remove']"
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

    <!-- 添加或修改访客记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="访客ID" prop="visitorId">
          <el-input v-model="form.visitorId" placeholder="请输入访客ID" />
        </el-form-item>
        <el-form-item label="被访住户ID" prop="residentId">
          <el-input v-model="form.residentId" placeholder="请输入被访住户ID" />
        </el-form-item>
        <el-form-item label="来访事由" prop="visitPurpose">
          <el-input v-model="form.visitPurpose" placeholder="请输入来访事由" />
        </el-form-item>
        <el-form-item label="预计到达时间" prop="expectedArrival">
          <el-date-picker clearable
            v-model="form.expectedArrival"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择预计到达时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="实际到达时间" prop="actualArrival">
          <el-date-picker clearable
            v-model="form.actualArrival"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择实际到达时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="实际离开时间" prop="actualLeave">
          <el-date-picker clearable
            v-model="form.actualLeave"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择实际离开时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="审核人ID" prop="approverId">
          <el-input v-model="form.approverId" placeholder="请输入审核人ID" />
        </el-form-item>
        <el-form-item label="拒绝原因" prop="rejectReason">
          <el-input v-model="form.rejectReason" placeholder="请输入拒绝原因" />
        </el-form-item>
        <el-form-item label="访客通行码" prop="accessCode">
          <el-input v-model="form.accessCode" placeholder="请输入访客通行码" />
        </el-form-item>
        <el-form-item label="通行码有效期" prop="codeExpiry">
          <el-date-picker clearable
            v-model="form.codeExpiry"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择通行码有效期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="随行人数" prop="accompanyCount">
          <el-input v-model="form.accompanyCount" placeholder="请输入随行人数" />
        </el-form-item>
        <el-form-item label="备注" prop="notes">
          <el-input v-model="form.notes" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-divider content-position="center">通行记录信息</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddAccessLogs">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteAccessLogs">删除</el-button>
          </el-col>
        </el-row>
        <el-table :data="accessLogsList" :row-class-name="rowAccessLogsIndex" @selection-change="handleAccessLogsSelectionChange" ref="accessLogs">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="门禁编号" prop="gateId" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.gateId" placeholder="请输入门禁编号" />
            </template>
          </el-table-column>
          <el-table-column label="门禁名称" prop="gateName" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.gateName" placeholder="请输入门禁名称" />
            </template>
          </el-table-column>
          <el-table-column label="通行类型(1:进入/2:离开)" prop="accessType" width="150">
            <template slot-scope="scope">
              <el-select v-model="scope.row.accessType" placeholder="请选择通行类型(1:进入/2:离开)">
                <el-option label="请选择字典生成" value="" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="通行时间" prop="accessTime" width="240">
            <template slot-scope="scope">
              <el-date-picker clearable v-model="scope.row.accessTime" type="date" value-format="yyyy-MM-dd" placeholder="请选择通行时间" />
            </template>
          </el-table-column>
          <el-table-column label="验证方式(1:二维码/2:刷脸/3:人工/4:刷卡)" prop="verifyMethod" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.verifyMethod" placeholder="请输入验证方式(1:二维码/2:刷脸/3:人工/4:刷卡)" />
            </template>
          </el-table-column>
          <el-table-column label="验证结果" prop="verifyResult" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.verifyResult" placeholder="请输入验证结果" />
            </template>
          </el-table-column>
          <el-table-column label="验证失败原因" prop="failReason" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.failReason" placeholder="请输入验证失败原因" />
            </template>
          </el-table-column>
          <el-table-column label="抓拍照片路径" prop="photoPath" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.photoPath" placeholder="请输入抓拍照片路径" />
            </template>
          </el-table-column>
          <el-table-column label="体温(可选)" prop="temperature" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.temperature" placeholder="请输入体温(可选)" />
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
import { listRecords, getRecords, delRecords, addRecords, updateRecords } from "@/api/bookSys/records"

export default {
  name: "Records",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedAccessLogs: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 访客记录表格数据
      recordsList: [],
      // 通行记录表格数据
      accessLogsList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        visitorId: null,
        residentId: null,
        visitPurpose: null,
        expectedArrival: null,
        actualArrival: null,
        actualLeave: null,
        status: null,
        approvalStatus: null,
        approverId: null,
        rejectReason: null,
        accessCode: null,
        codeExpiry: null,
        accompanyCount: null,
        notes: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        visitorId: [
          { required: true, message: "访客ID不能为空", trigger: "blur" }
        ],
        residentId: [
          { required: true, message: "被访住户ID不能为空", trigger: "blur" }
        ],
        visitPurpose: [
          { required: true, message: "来访事由不能为空", trigger: "blur" }
        ],
        expectedArrival: [
          { required: true, message: "预计到达时间不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询访客记录列表 */
    getList() {
      this.loading = true
      listRecords(this.queryParams).then(response => {
        this.recordsList = response.rows
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
        recordId: null,
        visitorId: null,
        residentId: null,
        visitPurpose: null,
        expectedArrival: null,
        actualArrival: null,
        actualLeave: null,
        status: null,
        approvalStatus: null,
        approverId: null,
        rejectReason: null,
        accessCode: null,
        codeExpiry: null,
        accompanyCount: null,
        notes: null,
        createTime: null,
        updateTime: null
      }
      this.accessLogsList = []
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
      this.ids = selection.map(item => item.recordId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加访客记录"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const recordId = row.recordId || this.ids
      getRecords(recordId).then(response => {
        this.form = response.data
        this.accessLogsList = response.data.accessLogsList
        this.open = true
        this.title = "修改访客记录"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.accessLogsList = this.accessLogsList
          if (this.form.recordId != null) {
            updateRecords(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addRecords(this.form).then(response => {
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
      const recordIds = row.recordId || this.ids
      this.$modal.confirm('是否确认删除访客记录编号为"' + recordIds + '"的数据项？').then(function() {
        return delRecords(recordIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
	/** 通行记录序号 */
    rowAccessLogsIndex({ row, rowIndex }) {
      row.index = rowIndex + 1
    },
    /** 通行记录添加按钮操作 */
    handleAddAccessLogs() {
      let obj = {}
      obj.gateId = ""
      obj.gateName = ""
      obj.accessType = ""
      obj.accessTime = ""
      obj.verifyMethod = ""
      obj.verifyResult = ""
      obj.failReason = ""
      obj.photoPath = ""
      obj.temperature = ""
      this.accessLogsList.push(obj)
    },
    /** 通行记录删除按钮操作 */
    handleDeleteAccessLogs() {
      if (this.checkedAccessLogs.length == 0) {
        this.$modal.msgError("请先选择要删除的通行记录数据")
      } else {
        const accessLogsList = this.accessLogsList
        const checkedAccessLogs = this.checkedAccessLogs
        this.accessLogsList = accessLogsList.filter(function(item) {
          return checkedAccessLogs.indexOf(item.index) == -1
        })
      }
    },
    /** 复选框选中数据 */
    handleAccessLogsSelectionChange(selection) {
      this.checkedAccessLogs = selection.map(item => item.index)
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('bookSys/records/export', {
        ...this.queryParams
      }, `records_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
