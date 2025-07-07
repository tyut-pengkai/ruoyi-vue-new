<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="医院名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入医院名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="医院名称" prop="nameCn">
        <el-input
          v-model="queryParams.nameCn"
          placeholder="请输入医院名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="医生数量" prop="doctorCount">
        <el-input
          v-model="queryParams.doctorCount"
          placeholder="请输入医生数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="平均评分" prop="starAvg">
        <el-input
          v-model="queryParams.starAvg"
          placeholder="请输入平均评分"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="评论数量" prop="reviewCount">
        <el-input
          v-model="queryParams.reviewCount"
          placeholder="请输入评论数量"
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
          v-hasPermi="['mmclub:hospitals:add']"
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
          v-hasPermi="['mmclub:hospitals:edit']"
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
          v-hasPermi="['mmclub:hospitals:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['mmclub:hospitals:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="hospitalsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="医院名称" align="center" prop="name" />
      <el-table-column label="医院名称" align="center" prop="nameCn" />
      <el-table-column label="页面描述" align="center" prop="description" />
      <el-table-column label="页面描述" align="center" prop="descriptionCn" />
      <el-table-column label="医院图片" align="center" prop="image" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.image" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="医院logo图片" align="center" prop="logoImage" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.logoImage" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="地址" align="center" prop="address" />
      <el-table-column label="地址" align="center" prop="addressCn" />
      <el-table-column label="医生数量" align="center" prop="doctorCount" />
      <el-table-column label="平均评分" align="center" prop="starAvg" />
      <el-table-column label="评论数量" align="center" prop="reviewCount" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['mmclub:hospitals:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['mmclub:hospitals:remove']"
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

    <!-- 添加或修改医院管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="医院ID" prop="id">
          <el-input v-model="form.id" placeholder="请输入医院ID" />
        </el-form-item>
        <el-form-item label="医院名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入医院名称" />
        </el-form-item>
        <el-form-item label="医院名称" prop="nameCn">
          <el-input v-model="form.nameCn" placeholder="请输入医院名称" />
        </el-form-item>
        <el-form-item label="页面描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="页面描述" prop="descriptionCn">
          <el-input v-model="form.descriptionCn" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="医院图片" prop="image">
          <image-upload v-model="form.image"/>
        </el-form-item>
        <el-form-item label="医院logo图片" prop="logoImage">
          <image-upload v-model="form.logoImage"/>
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="form.address" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="地址" prop="addressCn">
          <el-input v-model="form.addressCn" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="医生数量" prop="doctorCount">
          <el-input v-model="form.doctorCount" placeholder="请输入医生数量" />
        </el-form-item>
        <el-form-item label="平均评分" prop="starAvg">
          <el-input v-model="form.starAvg" placeholder="请输入平均评分" />
        </el-form-item>
        <el-form-item label="评论数量" prop="reviewCount">
          <el-input v-model="form.reviewCount" placeholder="请输入评论数量" />
        </el-form-item>
        <el-form-item label="是否删除：1-未删除，0-已删除" prop="deleted">
          <el-input v-model="form.deleted" placeholder="请输入是否删除：1-未删除，0-已删除" />
        </el-form-item>
        <el-divider content-position="center">医院分类关系映射信息</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddMHospitalCategory">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteMHospitalCategory">删除</el-button>
          </el-col>
        </el-row>
        <el-table :data="mHospitalCategoryList" :row-class-name="rowMHospitalCategoryIndex" @selection-change="handleMHospitalCategorySelectionChange" ref="mHospitalCategory">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="分类ID" prop="categoryId" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.categoryId" placeholder="请输入分类ID" />
            </template>
          </el-table-column>
          <el-table-column label="排序顺序" prop="sortOrder" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.sortOrder" placeholder="请输入排序顺序" />
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
import { listHospitals, getHospitals, delHospitals, addHospitals, updateHospitals } from "@/api/mmclub/hospitals"

export default {
  name: "Hospitals",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedMHospitalCategory: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 医院管理表格数据
      hospitalsList: [],
      // 医院分类关系映射表格数据
      mHospitalCategoryList: [],
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
        doctorCount: null,
        starAvg: null,
        reviewCount: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        id: [
          { required: true, message: "医院ID不能为空", trigger: "blur" }
        ],
        name: [
          { required: true, message: "医院名称不能为空", trigger: "blur" }
        ],
        nameCn: [
          { required: true, message: "医院名称不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询医院管理列表 */
    getList() {
      this.loading = true
      listHospitals(this.queryParams).then(response => {
        this.hospitalsList = response.rows
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
        description: null,
        descriptionCn: null,
        image: null,
        logoImage: null,
        address: null,
        addressCn: null,
        doctorCount: null,
        starAvg: null,
        reviewCount: null,
        createTime: null,
        updateTime: null,
        deleted: null
      }
      this.mHospitalCategoryList = []
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
      this.title = "添加医院管理"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getHospitals(id).then(response => {
        this.form = response.data
        this.mHospitalCategoryList = response.data.mHospitalCategoryList
        this.open = true
        this.title = "修改医院管理"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.mHospitalCategoryList = this.mHospitalCategoryList
          if (this.form.id != null) {
            updateHospitals(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addHospitals(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除医院管理编号为"' + ids + '"的数据项？').then(function() {
        return delHospitals(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
	/** 医院分类关系映射序号 */
    rowMHospitalCategoryIndex({ row, rowIndex }) {
      row.index = rowIndex + 1
    },
    /** 医院分类关系映射添加按钮操作 */
    handleAddMHospitalCategory() {
      let obj = {}
      obj.categoryId = ""
      obj.sortOrder = ""
      this.mHospitalCategoryList.push(obj)
    },
    /** 医院分类关系映射删除按钮操作 */
    handleDeleteMHospitalCategory() {
      if (this.checkedMHospitalCategory.length == 0) {
        this.$modal.msgError("请先选择要删除的医院分类关系映射数据")
      } else {
        const mHospitalCategoryList = this.mHospitalCategoryList
        const checkedMHospitalCategory = this.checkedMHospitalCategory
        this.mHospitalCategoryList = mHospitalCategoryList.filter(function(item) {
          return checkedMHospitalCategory.indexOf(item.index) == -1
        })
      }
    },
    /** 复选框选中数据 */
    handleMHospitalCategorySelectionChange(selection) {
      this.checkedMHospitalCategory = selection.map(item => item.index)
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('mmclub/hospitals/export', {
        ...this.queryParams
      }, `hospitals_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
