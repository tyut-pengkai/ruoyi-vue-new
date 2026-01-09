<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="访客记录ID" prop="recordId">
        <el-input
          v-model="queryParams.recordId"
          placeholder="请输入访客记录ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="门禁编号" prop="gateId">
        <el-input
          v-model="queryParams.gateId"
          placeholder="请输入门禁编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="门禁名称" prop="gateName">
        <el-input
          v-model="queryParams.gateName"
          placeholder="请输入门禁名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="通行时间" prop="accessTime">
        <el-date-picker clearable
          v-model="queryParams.accessTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择通行时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="验证方式(1:二维码/2:刷脸/3:人工/4:刷卡)" prop="verifyMethod">
        <el-input
          v-model="queryParams.verifyMethod"
          placeholder="请输入验证方式(1:二维码/2:刷脸/3:人工/4:刷卡)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="验证结果" prop="verifyResult">
        <el-input
          v-model="queryParams.verifyResult"
          placeholder="请输入验证结果"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="验证失败原因" prop="failReason">
        <el-input
          v-model="queryParams.failReason"
          placeholder="请输入验证失败原因"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="抓拍照片路径" prop="photoPath">
        <el-input
          v-model="queryParams.photoPath"
          placeholder="请输入抓拍照片路径"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="体温(可选)" prop="temperature">
        <el-input
          v-model="queryParams.temperature"
          placeholder="请输入体温(可选)"
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
          v-hasPermi="['bookSys:logs:add']"
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
          v-hasPermi="['bookSys:logs:edit']"
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
          v-hasPermi="['bookSys:logs:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['bookSys:logs:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="logsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="日志ID" align="center" prop="logId" />
      <el-table-column label="访客记录ID" align="center" prop="recordId" />
      <el-table-column label="门禁编号" align="center" prop="gateId" />
      <el-table-column label="门禁名称" align="center" prop="gateName" />
      <el-table-column label="通行类型(1:进入/2:离开)" align="center" prop="accessType" />
      <el-table-column label="通行时间" align="center" prop="accessTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.accessTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="验证方式(1:二维码/2:刷脸/3:人工/4:刷卡)" align="center" prop="verifyMethod" />
      <el-table-column label="验证结果" align="center" prop="verifyResult" />
      <el-table-column label="验证失败原因" align="center" prop="failReason" />
      <el-table-column label="抓拍照片路径" align="center" prop="photoPath" />
      <el-table-column label="体温(可选)" align="center" prop="temperature" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['bookSys:logs:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['bookSys:logs:remove']"
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

    <!-- 添加或修改通行记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="访客记录ID" prop="recordId">
          <el-input v-model="form.recordId" placeholder="请输入访客记录ID" />
        </el-form-item>
        <el-form-item label="门禁编号" prop="gateId">
          <el-input v-model="form.gateId" placeholder="请输入门禁编号" />
        </el-form-item>
        <el-form-item label="门禁名称" prop="gateName">
          <el-input v-model="form.gateName" placeholder="请输入门禁名称" />
        </el-form-item>
        <el-form-item label="通行时间" prop="accessTime">
          <el-date-picker clearable
            v-model="form.accessTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择通行时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="验证方式(1:二维码/2:刷脸/3:人工/4:刷卡)" prop="verifyMethod">
          <el-input v-model="form.verifyMethod" placeholder="请输入验证方式(1:二维码/2:刷脸/3:人工/4:刷卡)" />
        </el-form-item>
        <el-form-item label="验证结果" prop="verifyResult">
          <el-input v-model="form.verifyResult" placeholder="请输入验证结果" />
        </el-form-item>
        <el-form-item label="验证失败原因" prop="failReason">
          <el-input v-model="form.failReason" placeholder="请输入验证失败原因" />
        </el-form-item>
        <el-form-item label="抓拍照片路径" prop="photoPath">
          <el-input v-model="form.photoPath" placeholder="请输入抓拍照片路径" />
        </el-form-item>
        <el-form-item label="体温(可选)" prop="temperature">
          <el-input v-model="form.temperature" placeholder="请输入体温(可选)" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listLogs, getLogs, delLogs, addLogs, updateLogs } from "@/api/bookSys/logs"

export default {
  name: "Logs",
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
      // 通行记录表格数据
      logsList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        recordId: null,
        gateId: null,
        gateName: null,
        accessType: null,
        accessTime: null,
        verifyMethod: null,
        verifyResult: null,
        failReason: null,
        photoPath: null,
        temperature: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        recordId: [
          { required: true, message: "访客记录ID不能为空", trigger: "blur" }
        ],
        gateId: [
          { required: true, message: "门禁编号不能为空", trigger: "blur" }
        ],
        accessType: [
          { required: true, message: "通行类型(1:进入/2:离开)不能为空", trigger: "change" }
        ],
        verifyMethod: [
          { required: true, message: "验证方式(1:二维码/2:刷脸/3:人工/4:刷卡)不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询通行记录列表 */
    getList() {
      this.loading = true
      listLogs(this.queryParams).then(response => {
        this.logsList = response.rows
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
        logId: null,
        recordId: null,
        gateId: null,
        gateName: null,
        accessType: null,
        accessTime: null,
        verifyMethod: null,
        verifyResult: null,
        failReason: null,
        photoPath: null,
        temperature: null
      }
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
      this.ids = selection.map(item => item.logId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加通行记录"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const logId = row.logId || this.ids
      getLogs(logId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改通行记录"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.logId != null) {
            updateLogs(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addLogs(this.form).then(response => {
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
      const logIds = row.logId || this.ids
      this.$modal.confirm('是否确认删除通行记录编号为"' + logIds + '"的数据项？').then(function() {
        return delLogs(logIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('bookSys/logs/export', {
        ...this.queryParams
      }, `logs_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
