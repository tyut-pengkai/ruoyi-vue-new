<template>
  <div class="app-container">
    <div class="head-container">
      <el-form :inline="true" :model="queryParams" ref="queryForm" size="small" label-width="68px">
        <el-form-item label="数据库名称" prop="databaseName">
          <el-input
            v-model="queryParams.databaseName"
            placeholder="请输入数据库名称"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="small" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="small" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 工具栏 -->
    <div class="toolbar">
      <el-button type="success" icon="el-icon-plus" size="small" @click="handleAdd" v-hasPermi="['database:nameTwo:add']">新增</el-button>
      <el-button type="primary" icon="el-icon-edit" size="small" :disabled="single" @click="handleUpdate" v-hasPermi="['database:nameTwo:edit']">修改</el-button>
      <el-button type="danger" icon="el-icon-delete" size="small" :disabled="multiple" @click="handleDelete" v-hasPermi="['database:nameTwo:remove']">删除</el-button>
      <el-button type="info" icon="el-icon-download" size="small" @click="handleExport" v-hasPermi="['database:nameTwo:export']">导出</el-button>
    </div>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="productList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="产品ID" prop="productId" width="80" align="center" />
      <el-table-column label="数据库名称" prop="databaseName" align="center" />
      <el-table-column label="图标" prop="icon" align="center" width="80">
        <template slot-scope="scope">
          <img :src="scope.row.icon" alt="图标" style="width: 24px; height: 24px;" />
        </template>
      </el-table-column>
      <el-table-column label="关系类型" prop="relationType" align="center" />
      <el-table-column label="创建时间" prop="createTime" align="center" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改数据库产品对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="数据库名称" prop="databaseName">
          <el-input v-model="form.databaseName" placeholder="请输入数据库名称" />
        </el-form-item>
        <el-form-item label="图标" prop="icon">
          <el-input v-model="form.icon" placeholder="请输入图标URL" />
        </el-form-item>
        <el-form-item label="关系类型" prop="relationType">
          <el-input v-model="form.relationType" placeholder="请输入关系类型" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listProduct, getProduct, addProduct, updateProduct, delProduct, exportProduct } from '@/api/database/product'

export default {
  name: 'NameTwo',
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
      // 总条数
      total: 0,
      // 数据库产品列表
      productList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        databaseName: undefined
      },
      // 表单参数
      form: {
        productId: undefined,
        databaseName: undefined,
        icon: undefined,
        relationType: undefined
      },
      // 表单校验
      rules: {
        databaseName: [
          { required: true, message: '数据库名称不能为空', trigger: 'blur' }
        ],
        icon: [
          { required: true, message: '图标不能为空', trigger: 'blur' }
        ],
        relationType: [
          { required: true, message: '关系类型不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询数据库产品列表 */
    getList() {
      this.loading = true
      listProduct(this.queryParams).then(response => {
        this.productList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.productId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加数据库产品'
    },
    /** 修改按钮操作 */
    handleUpdate() {
      const productId = this.ids.length === 1 ? this.ids[0] : undefined
      if (productId === undefined) {
        this.$modal.msgError('请选择一条记录')
        return
      }
      this.reset()
      getProduct(productId).then(response => {
        this.form = response.data
        this.form.databaseName = '王冰堰'
        this.open = true
        this.title = '修改数据库产品'
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.productId != undefined) {
            updateProduct(this.form).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addProduct(this.form).then(response => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 取消按钮 */
    cancel() {
      this.open = false
      this.reset()
    },
    /** 重置表单 */
    reset() {
      this.form = {
        productId: undefined,
        databaseName: undefined,
        icon: undefined,
        relationType: undefined
      }
      this.resetForm('form')
    },
    /** 删除按钮操作 */
    handleDelete() {
      if (this.ids.length === 0) {
        this.$modal.msgError('请选择要删除的数据')
        return
      }
      this.$modal.confirm('确认要删除选中的数据库产品吗？').then(function() {
        return delProduct(this.ids)
      }.bind(this)).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
        this.ids = []
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('database/product/export', { ...this.queryParams }, `database_product_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>