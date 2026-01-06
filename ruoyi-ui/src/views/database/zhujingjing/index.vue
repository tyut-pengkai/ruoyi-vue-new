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
      <el-button type="success" icon="el-icon-plus" size="small" @click="handleAdd" v-hasPermi="['database:zhujingjing:add']">新增</el-button>
      <el-button type="primary" icon="el-icon-edit" size="small" :disabled="single" @click="handleUpdate" v-hasPermi="['database:zhujingjing:edit']">修改</el-button>
      <el-button type="danger" icon="el-icon-delete" size="small" :disabled="multiple" @click="handleDelete" v-hasPermi="['database:zhujingjing:remove']">删除</el-button>
      <el-button type="info" icon="el-icon-download" size="small" @click="handleExport" v-hasPermi="['database:zhujingjing:export']">导出</el-button>
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
      <el-table-column label="类型" align="center">
        <template slot-scope="scope">
          <el-tag v-for="type in formatTypeList(scope.row.type)" :key="type" size="small" style="margin-right: 5px;">{{ type }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="数据库描述" prop="description" align="center" min-width="200">
        <template slot-scope="scope">
          <div v-html="scope.row.description" style="max-height: 80px; overflow: hidden; text-overflow: ellipsis;"></div>
        </template>
      </el-table-column>
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
          <el-upload
            class="avatar-uploader"
            :action="uploadUrl"
            :show-file-list="false"
            :on-success="handleIconUpload"
            :before-upload="beforeIconUpload"
          >
            <img v-if="form.icon" :src="form.icon" class="avatar" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
          <div class="el-upload__tip">请上传大小不超过5MB，格式为png/jpg/jpeg的文件</div>
        </el-form-item>
        <el-form-item label="类型" prop="typeList">
          <el-checkbox-group v-model="form.typeList">
            <el-checkbox label="关系型" />
            <el-checkbox label="非关系型" />
            <el-checkbox label="单机" />
            <el-checkbox label="分布式" />
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="数据库描述" prop="description">
          <editor v-model="form.description" :min-height="150" :toolbar="true" />
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
import { listZhujingjing, getZhujingjing, addZhujingjing, updateZhujingjing, delZhujingjing, exportZhujingjing } from '@/api/database/zhujingjing'
import Editor from '@/components/Editor'

export default {
  name: 'Zhujingjing',
  components: {
    Editor
  },
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
      // 上传地址
      uploadUrl: process.env.VUE_APP_BASE_API + '/common/upload',
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
        type: '',
        typeList: [],
        description: ''
      },
      // 表单校验
      rules: {
        databaseName: [
          { required: true, message: '数据库名称不能为空', trigger: 'blur' }
        ],
        icon: [
          { required: true, message: '图标不能为空', trigger: 'blur' }
        ],
        typeList: [
          { required: true, message: '请至少选择一个类型', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询朱晶晶列表 */
    getList() {
      this.loading = true
      listZhujingjing(this.queryParams).then(response => {
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
      getZhujingjing(productId).then(response => {
        this.form = response.data
        // 将类型字符串转换为数组
        if (this.form.type) {
          this.form.typeList = this.form.type.split(',')
        }
        this.open = true
        this.title = '修改朱晶晶'
      })
    },
    /** 图标上传成功处理 */
    handleIconUpload(res, file) {
      if (res.code === 200) {
        this.form.icon = res.fileName
      } else {
        this.$message.error(res.msg)
      }
    },
    /** 图标上传前处理 */
    beforeIconUpload(file) {
      const isLt5M = file.size / 1024 / 1024 < 5
      if (!isLt5M) {
        this.$message.error('上传头像图片大小不能超过 5MB!')
      }
      return isLt5M
    },
    /** 格式化类型列表 */
    formatTypeList(typeStr) {
      return typeStr ? typeStr.split(',') : []
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          // 将类型数组转换为字符串
          this.form.type = this.form.typeList.join(',')
          if (this.form.productId != undefined) {
            updateZhujingjing(this.form).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addZhujingjing(this.form).then(response => {
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
        type: '',
        typeList: [],
        description: ''
      }
      this.resetForm('form')
    },
    /** 删除按钮操作 */
    handleDelete() {
      if (this.ids.length === 0) {
        this.$modal.msgError('请选择要删除的数据')
        return
      }
      this.$modal.confirm('确认要删除选中的朱晶晶吗？').then(function() {
        return delZhujingjing(this.ids)
      }.bind(this)).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
        this.ids = []
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('database/product/export', { ...this.queryParams }, `zhujingjing_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

<style scoped>
.avatar-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 100px;
  height: 100px;
}

.avatar-uploader:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  line-height: 100px;
  text-align: center;
}

.avatar {
  width: 100px;
  height: 100px;
  display: block;
}
</style>