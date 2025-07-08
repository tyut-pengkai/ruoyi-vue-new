<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="医院ID" prop="hospitalId">
        <el-input
          v-model="queryParams.hospitalId"
          placeholder="请输入医院ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="医生姓名" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入医生姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="医生姓名" prop="nameCn">
        <el-input
          v-model="queryParams.nameCn"
          placeholder="请输入医生姓名"
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
          v-hasPermi="['mmclub:doctors:add']"
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
          v-hasPermi="['mmclub:doctors:edit']"
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
          v-hasPermi="['mmclub:doctors:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['mmclub:doctors:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="doctorsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="医院ID" align="center" prop="hospitalId" />
      <el-table-column label="医生姓名" align="center" prop="name" />
      <el-table-column label="医生姓名" align="center" prop="nameCn" />
      <el-table-column label="头像照片URL" align="center" prop="profilePhoto" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.profilePhoto" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="评论数量" align="center" prop="reviewCount" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['mmclub:doctors:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['mmclub:doctors:remove']"
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

    <!-- 添加或修改医生管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="医生ID" prop="id">
          <el-input v-model="form.id" placeholder="请输入医生ID" />
        </el-form-item>
        <el-form-item label="医院ID" prop="hospitalId">
          <el-input v-model="form.hospitalId" placeholder="请输入医院ID" />
        </el-form-item>
        <el-form-item label="医生姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入医生姓名" />
        </el-form-item>
        <el-form-item label="医生姓名" prop="nameCn">
          <el-input v-model="form.nameCn" placeholder="请输入医生姓名" />
        </el-form-item>
        <el-form-item label="头像照片URL" prop="profilePhoto">
          <image-upload v-model="form.profilePhoto"/>
        </el-form-item>
        <el-form-item label="评论数量" prop="reviewCount">
          <el-input v-model="form.reviewCount" placeholder="请输入评论数量" />
        </el-form-item>
        <el-form-item label="是否删除：1-未删除，0-已删除" prop="deleted">
          <el-input v-model="form.deleted" placeholder="请输入是否删除：1-未删除，0-已删除" />
        </el-form-item>
        <el-divider content-position="center">医生专业领域关系映射信息</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddMDoctorSpecialty">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteMDoctorSpecialty">删除</el-button>
          </el-col>
        </el-row>
        <el-table :data="mDoctorSpecialtyList" :row-class-name="rowMDoctorSpecialtyIndex" @selection-change="handleMDoctorSpecialtySelectionChange" ref="mDoctorSpecialty">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="专业领域ID" prop="specialtyId" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.specialtyId" placeholder="请输入专业领域ID" />
            </template>
          </el-table-column>
          <el-table-column label="专业水平：1-初级，2-中级，3-高级，4-专家级" prop="proficiencyLevel" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.proficiencyLevel" placeholder="请输入专业水平：1-初级，2-中级，3-高级，4-专家级" />
            </template>
          </el-table-column>
          <el-table-column label="该领域从业年限" prop="yearsExperience" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.yearsExperience" placeholder="请输入该领域从业年限" />
            </template>
          </el-table-column>
          <el-table-column label="相关认证/资质" prop="certification" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.certification" placeholder="请输入相关认证/资质" />
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
import { listDoctors, getDoctors, delDoctors, addDoctors, updateDoctors } from "@/api/mmclub/doctors"

export default {
  name: "Doctors",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedMDoctorSpecialty: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 医生管理表格数据
      doctorsList: [],
      // 医生专业领域关系映射表格数据
      mDoctorSpecialtyList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        hospitalId: null,
        name: null,
        nameCn: null,
        reviewCount: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        id: [
          { required: true, message: "医生ID不能为空", trigger: "blur" }
        ],
        hospitalId: [
          { required: true, message: "医院ID不能为空", trigger: "blur" }
        ],
        name: [
          { required: true, message: "医生姓名不能为空", trigger: "blur" }
        ],
        nameCn: [
          { required: true, message: "医生姓名不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询医生管理列表 */
    getList() {
      this.loading = true
      listDoctors(this.queryParams).then(response => {
        this.doctorsList = response.rows
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
        hospitalId: null,
        name: null,
        nameCn: null,
        profilePhoto: null,
        reviewCount: null,
        createTime: null,
        updateTime: null,
        deleted: null
      }
      this.mDoctorSpecialtyList = []
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
      this.title = "添加医生管理"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getDoctors(id).then(response => {
        this.form = response.data
        this.mDoctorSpecialtyList = response.data.mDoctorSpecialtyList
        this.open = true
        this.title = "修改医生管理"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.mDoctorSpecialtyList = this.mDoctorSpecialtyList
          if (this.form.id != null) {
            updateDoctors(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addDoctors(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除医生管理编号为"' + ids + '"的数据项？').then(function() {
        return delDoctors(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
	/** 医生专业领域关系映射序号 */
    rowMDoctorSpecialtyIndex({ row, rowIndex }) {
      row.index = rowIndex + 1
    },
    /** 医生专业领域关系映射添加按钮操作 */
    handleAddMDoctorSpecialty() {
      let obj = {}
      obj.specialtyId = ""
      obj.proficiencyLevel = ""
      obj.yearsExperience = ""
      obj.certification = ""
      obj.sortOrder = ""
      this.mDoctorSpecialtyList.push(obj)
    },
    /** 医生专业领域关系映射删除按钮操作 */
    handleDeleteMDoctorSpecialty() {
      if (this.checkedMDoctorSpecialty.length == 0) {
        this.$modal.msgError("请先选择要删除的医生专业领域关系映射数据")
      } else {
        const mDoctorSpecialtyList = this.mDoctorSpecialtyList
        const checkedMDoctorSpecialty = this.checkedMDoctorSpecialty
        this.mDoctorSpecialtyList = mDoctorSpecialtyList.filter(function(item) {
          return checkedMDoctorSpecialty.indexOf(item.index) == -1
        })
      }
    },
    /** 复选框选中数据 */
    handleMDoctorSpecialtySelectionChange(selection) {
      this.checkedMDoctorSpecialty = selection.map(item => item.index)
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('mmclub/doctors/export', {
        ...this.queryParams
      }, `doctors_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
