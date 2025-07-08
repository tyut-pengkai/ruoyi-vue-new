<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="地区名称(韩文)" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入地区名称(韩文)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="地区名称(中文)" prop="nameCn">
        <el-input
          v-model="queryParams.nameCn"
          placeholder="请输入地区名称(中文)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="地区代码" prop="regionCode">
        <el-input
          v-model="queryParams.regionCode"
          placeholder="请输入地区代码"
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
          v-hasPermi="['mmclub:regions:add']"
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
          v-hasPermi="['mmclub:regions:edit']"
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
          v-hasPermi="['mmclub:regions:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['mmclub:regions:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="regionsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="id" />
      <el-table-column label="地区名称(韩文)" align="center" prop="name" />
      <el-table-column label="地区名称(中文)" align="center" prop="nameCn" />
      <el-table-column label="上级地区ID" align="center" prop="parentId" />
      <el-table-column label="地区层级" align="center" prop="level" />
      <el-table-column label="地区代码" align="center" prop="regionCode" />
      <el-table-column label="排序顺序" align="center" prop="sortOrder" />
      <el-table-column label="状态" align="center" prop="status" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['mmclub:regions:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['mmclub:regions:remove']"
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

    <!-- 添加或修改地区管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="地区名称(韩文)" prop="name">
          <el-input v-model="form.name" placeholder="请输入地区名称(韩文)" />
        </el-form-item>
        <el-form-item label="地区名称(中文)" prop="nameCn">
          <el-input v-model="form.nameCn" placeholder="请输入地区名称(中文)" />
        </el-form-item>
        <el-form-item label="上级地区ID" prop="parentId">
          <el-input v-model="form.parentId" placeholder="请输入上级地区ID" />
        </el-form-item>
        <el-form-item label="地区层级" prop="level">
          <el-input v-model="form.level" placeholder="请输入地区层级" />
        </el-form-item>
        <el-form-item label="地区代码" prop="regionCode">
          <el-input v-model="form.regionCode" placeholder="请输入地区代码" />
        </el-form-item>
        <el-form-item label="排序顺序" prop="sortOrder">
          <el-input v-model="form.sortOrder" placeholder="请输入排序顺序" />
        </el-form-item>
        <el-form-item label="删除标识：1-未删除，0-已删除" prop="deleted">
          <el-input v-model="form.deleted" placeholder="请输入删除标识：1-未删除，0-已删除" />
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
import { listRegions, getRegions, delRegions, addRegions, updateRegions } from "@/api/mmclub/regions"

export default {
  name: "Regions",
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
      // 地区管理表格数据
      regionsList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        nameCn: null,
        regionCode: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "地区名称(韩文)不能为空", trigger: "blur" }
        ],
        nameCn: [
          { required: true, message: "地区名称(中文)不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询地区管理列表 */
    getList() {
      this.loading = true
      listRegions(this.queryParams).then(response => {
        this.regionsList = response.rows
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
        id: null,
        name: null,
        nameCn: null,
        parentId: null,
        level: null,
        regionCode: null,
        sortOrder: null,
        status: null,
        createTime: null,
        updateTime: null,
        deleted: null
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加地区管理"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getRegions(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改地区管理"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateRegions(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addRegions(this.form).then(response => {
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
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除地区管理编号为"' + ids + '"的数据项？').then(function() {
        return delRegions(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('mmclub/regions/export', {
        ...this.queryParams
      }, `regions_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
