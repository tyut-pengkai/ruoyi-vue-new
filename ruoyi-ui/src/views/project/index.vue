<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="项目名称" prop="xmmc">
        <el-input
          v-model="queryParams.xmmc"
          placeholder="请输入项目名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="项目编号" prop="xmbh">
        <el-input
          v-model="queryParams.xmbh"
          placeholder="请输入项目编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="负责人姓名" prop="fzrxm">
        <el-input
          v-model="queryParams.fzrxm"
          placeholder="请输入负责人姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="负责人手机号" prop="fzrsjhm">
        <el-input
          v-model="queryParams.fzrsjhm"
          placeholder="请输入负责人手机号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="项目开始时间" prop="xmkssj">
        <el-date-picker clearable
          v-model="queryParams.xmkssj"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择项目开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="项目结束时间" prop="xmjssj">
        <el-date-picker clearable
          v-model="queryParams.xmjssj"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择项目结束时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="项目状态" prop="smzt">
        <el-select v-model="queryParams.smzt" placeholder="请选择项目状态" clearable>
          <el-option
            v-for="dict in dict.type.project_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="项目描述" prop="smms">
        <el-input
          v-model="queryParams.smms"
          placeholder="请输入项目描述"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:project:add']"
        >新增</el-button>
        <el-button
          type="success"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:project:import']"
        >导入</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :v-show="!single"
          @click="handleUpdate"
          v-hasPermi="['system:project:export']"
        >导出选中数据</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :v-show="!single"
          @click="handleDelete"
          v-hasPermi="['system:project:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          :v-show="!single"
          v-hasPermi="['system:project:export']"
        >启用</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          :v-show="!single"
          v-hasPermi="['system:project:export']"
        >禁用</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="projectList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="${comment}" align="center" prop="id" /> -->
      <el-table-column label="项目名称" align="center" prop="xmmc" />
      <el-table-column label="项目编号" align="center" prop="xmbh" />
      <el-table-column label="负责人姓名" align="center" prop="fzrxm" />
      <el-table-column label="负责人手机号" align="center" prop="fzrsjhm" />
      <el-table-column label="项目开始时间" align="center" prop="xmkssj" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.xmkssj, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="项目结束时间" align="center" prop="xmjssj" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.xmjssj, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="项目状态" align="center" prop="smzt">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.project_status" :value="scope.row.smzt"/>
        </template>
      </el-table-column>
      <el-table-column label="项目描述" align="center" prop="smms" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:project:edit']"
          >项目概括</el-button>
          <el-button
            size="mini"
            type="text"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:project:edit']"
          >申请信息</el-button>
          <el-button
            size="mini"
            type="text"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:project:edit']"
          >禁用</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:project:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:project:remove']"
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

    <!-- 添加或修改项目信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="项目名称" prop="xmmc">
          <el-input v-model="form.xmmc" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="项目编号" prop="xmbh">
          <el-input v-model="form.xmbh" placeholder="请输入项目编号" />
        </el-form-item>
        <el-form-item label="负责人姓名" prop="fzrxm">
          <el-input v-model="form.fzrxm" placeholder="请输入负责人姓名" />
        </el-form-item>
        <el-form-item label="负责人手机号" prop="fzrsjhm">
          <el-input v-model="form.fzrsjhm" placeholder="请输入负责人手机号" />
        </el-form-item>
        <el-form-item label="项目开始时间" prop="xmkssj">
          <el-date-picker clearable
            v-model="form.xmkssj"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择项目开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="项目结束时间" prop="xmjssj">
          <el-date-picker clearable
            v-model="form.xmjssj"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择项目结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="项目状态" prop="smzt">
          <el-select v-model="form.smzt" placeholder="请选择项目状态">
            <el-option
              v-for="dict in dict.type.project_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="项目描述" prop="smms">
          <el-input v-model="form.smms" placeholder="请输入项目描述" />
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
import { listProject, getProject, delProject, addProject, updateProject } from "@/api/project";

export default {
  name: "Project",
  dicts: ['project_status'],
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
      // 项目信息表格数据
      projectList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        xmmc: null,
        xmbh: null,
        fzrxm: null,
        fzrsjhm: null,
        xmkssj: null,
        xmjssj: null,
        smzt: null,
        smms: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询项目信息列表 */
    getList() {
      this.loading = true;
      listProject(this.queryParams).then(response => {
        this.projectList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        xmmc: null,
        xmbh: null,
        fzrxm: null,
        fzrsjhm: null,
        xmkssj: null,
        xmjssj: null,
        smzt: null,
        smms: null,
        createTime: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加项目信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getProject(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改项目信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateProject(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addProject(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除项目信息编号为"' + ids + '"的数据项？').then(function() {
        return delProject(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/project/export', {
        ...this.queryParams
      }, `project_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
