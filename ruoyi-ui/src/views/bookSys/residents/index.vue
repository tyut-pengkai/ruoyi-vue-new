<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="楼栋号" prop="buildingNumber">
        <el-input
          v-model="queryParams.buildingNumber"
          placeholder="请输入楼栋号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="单元号" prop="unitNumber">
        <el-input
          v-model="queryParams.unitNumber"
          placeholder="请输入单元号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="房间号" prop="roomNumber">
        <el-input
          v-model="queryParams.roomNumber"
          placeholder="请输入房间号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="住户姓名" prop="fullName">
        <el-input
          v-model="queryParams.fullName"
          placeholder="请输入住户姓名"
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
      <el-form-item label="是否为业主(1:业主/0:租客)" prop="isOwner">
        <el-input
          v-model="queryParams.isOwner"
          placeholder="请输入是否为业主(1:业主/0:租客)"
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
          v-hasPermi="['bookSys:residents:add']"
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
          v-hasPermi="['bookSys:residents:edit']"
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
          v-hasPermi="['bookSys:residents:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['bookSys:residents:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="residentsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="住户ID" align="center" prop="residentId" />
      <el-table-column label="楼栋号" align="center" prop="buildingNumber" />
      <el-table-column label="单元号" align="center" prop="unitNumber" />
      <el-table-column label="房间号" align="center" prop="roomNumber" />
      <el-table-column label="住户姓名" align="center" prop="fullName" />
      <el-table-column label="联系电话" align="center" prop="phone" />
      <el-table-column label="身份证号" align="center" prop="idCard" />
      <el-table-column label="是否为业主(1:业主/0:租客)" align="center" prop="isOwner" />
      <el-table-column label="状态(1:正常/0:冻结)" align="center" prop="status" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['bookSys:residents:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['bookSys:residents:remove']"
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

    <!-- 添加或修改住户信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="楼栋号" prop="buildingNumber">
          <el-input v-model="form.buildingNumber" placeholder="请输入楼栋号" />
        </el-form-item>
        <el-form-item label="单元号" prop="unitNumber">
          <el-input v-model="form.unitNumber" placeholder="请输入单元号" />
        </el-form-item>
        <el-form-item label="房间号" prop="roomNumber">
          <el-input v-model="form.roomNumber" placeholder="请输入房间号" />
        </el-form-item>
        <el-form-item label="住户姓名" prop="fullName">
          <el-input v-model="form.fullName" placeholder="请输入住户姓名" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="form.idCard" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="是否为业主(1:业主/0:租客)" prop="isOwner">
          <el-input v-model="form.isOwner" placeholder="请输入是否为业主(1:业主/0:租客)" />
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
import { listResidents, getResidents, delResidents, addResidents, updateResidents } from "@/api/bookSys/residents"

export default {
  name: "Residents",
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
      // 住户信息表格数据
      residentsList: [],
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
        buildingNumber: null,
        unitNumber: null,
        roomNumber: null,
        fullName: null,
        phone: null,
        idCard: null,
        isOwner: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        buildingNumber: [
          { required: true, message: "楼栋号不能为空", trigger: "blur" }
        ],
        unitNumber: [
          { required: true, message: "单元号不能为空", trigger: "blur" }
        ],
        roomNumber: [
          { required: true, message: "房间号不能为空", trigger: "blur" }
        ],
        fullName: [
          { required: true, message: "住户姓名不能为空", trigger: "blur" }
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
    /** 查询住户信息列表 */
    getList() {
      this.loading = true
      listResidents(this.queryParams).then(response => {
        this.residentsList = response.rows
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
        residentId: null,
        buildingNumber: null,
        unitNumber: null,
        roomNumber: null,
        fullName: null,
        phone: null,
        idCard: null,
        isOwner: null,
        status: null,
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
      this.ids = selection.map(item => item.residentId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加住户信息"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const residentId = row.residentId || this.ids
      getResidents(residentId).then(response => {
        this.form = response.data
        this.visitRecordsList = response.data.visitRecordsList
        this.open = true
        this.title = "修改住户信息"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.visitRecordsList = this.visitRecordsList
          if (this.form.residentId != null) {
            updateResidents(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addResidents(this.form).then(response => {
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
      const residentIds = row.residentId || this.ids
      this.$modal.confirm('是否确认删除住户信息编号为"' + residentIds + '"的数据项？').then(function() {
        return delResidents(residentIds)
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
      this.download('bookSys/residents/export', {
        ...this.queryParams
      }, `residents_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
