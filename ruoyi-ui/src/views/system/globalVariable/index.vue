<template>
  <div class="app-container">
    <el-form
      v-show="showSearch"
      ref="queryForm"
      :inline="true"
      :model="queryParams"
      size="small"
    >
      <el-form-item label="变量名" prop="name">
        <el-input
          v-model="queryParams.name"
          clearable
          placeholder="请输入变量名"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否需要登录" prop="checkToken">
        <el-select
          v-model="queryParams.checkToken"
          clearable
          placeholder="请选择是否需要登录"
        >
          <el-option
            v-for="dict in dict.type.sys_yes_no"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="是否需要VIP" prop="checkVip">
        <el-select
          v-model="queryParams.checkVip"
          clearable
          placeholder="请选择是否需要VIP"
        >
          <el-option
            v-for="dict in dict.type.sys_yes_no"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input
          v-model="queryParams.remark"
          placeholder="请输入备注"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button
          icon="el-icon-search"
          size="mini"
          type="primary"
          @click="handleQuery"
          >搜索
        </el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置
        </el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:globalVariable:add']"
          icon="el-icon-plus"
          plain
          size="mini"
          type="primary"
          @click="handleAdd"
          >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:globalVariable:edit']"
          :disabled="single"
          icon="el-icon-edit"
          plain
          size="mini"
          type="success"
          @click="handleUpdate"
          >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:globalVariable:remove']"
          :disabled="multiple"
          icon="el-icon-delete"
          plain
          size="mini"
          type="danger"
          @click="handleDelete"
          >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:globalVariable:export']"
          icon="el-icon-download"
          plain
          size="mini"
          type="warning"
          @click="handleExport"
          >导出
        </el-button>
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="globalVariableList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column align="center" type="selection" width="55" />
      <el-table-column align="center" label="序号" prop="id"/>
      <el-table-column align="center" label="变量名" prop="name" />
      <el-table-column align="center" label="变量值" prop="value" />
      <el-table-column align="center" label="变量描述" prop="description" />
      <el-table-column align="center" label="是否需要登录" prop="checkToken">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_yes_no"
            :value="scope.row.checkToken"
          />
        </template>
      </el-table-column>
      <el-table-column align="center" label="是否需要VIP" prop="checkVip">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_yes_no"
            :value="scope.row.checkVip"
          />
        </template>
      </el-table-column>
      <el-table-column align="center" label="备注" prop="remark" />
      <el-table-column
        align="center"
        class-name="small-padding fixed-width"
        label="操作"
      >
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['system:globalVariable:edit']"
            icon="el-icon-edit"
            size="mini"
            type="text"
            @click="handleUpdate(scope.row)"
            >修改
          </el-button>
          <el-button
            v-hasPermi="['system:globalVariable:remove']"
            icon="el-icon-delete"
            size="mini"
            type="text"
            @click="handleDelete(scope.row)"
            >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :limit.sync="queryParams.pageSize"
      :page.sync="queryParams.pageNum"
      :total="total"
      @pagination="getList"
    />

    <!-- 添加或修改全局变量对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="500px">
      <el-form ref="form" :model="form" :rules="rules">
        <el-form-item label="变量名" prop="name">
          <el-input v-model="form.name" placeholder="请输入变量名" maxlength="30" show-word-limit/>
        </el-form-item>
        <el-form-item label="变量值" prop="value">
          <el-input
            v-model="form.value"
            placeholder="请输入内容"
            type="textarea"
             maxlength="500" show-word-limit
          />
        </el-form-item>
        <el-form-item label="变量描述" prop="description">
          <el-input
            v-model="form.description"
            placeholder="请输入内容"
            type="textarea"
             maxlength="500" show-word-limit
          />
        </el-form-item>
        <el-form-item label="是否需要登录" prop="checkToken">
          <el-select v-model="form.checkToken" placeholder="请选择是否需要登录">
            <el-option
              v-for="dict in dict.type.sys_yes_no"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否需要VIP" prop="checkVip">
          <el-select v-model="form.checkVip" placeholder="请选择是否需要VIP">
            <el-option
              v-for="dict in dict.type.sys_yes_no"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form.remark"
            placeholder="请输入内容"
            type="textarea"
          />
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
import {
  addGlobalVariable,
  delGlobalVariable,
  getGlobalVariable,
  listGlobalVariable,
  updateGlobalVariable,
} from "@/api/system/globalVariable";

export default {
  name: "GlobalVariable",
  dicts: ["sys_yes_no"],
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
      // 全局变量表格数据
      globalVariableList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: this.$store.state.settings.pageSize,
        name: null,
        value: null,
        description: null,
        checkToken: null,
        checkVip: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [{ required: true, message: "变量名不能为空", trigger: "blur" }],
        value: [{ required: true, message: "变量值不能为空", trigger: "blur" }],
        checkToken: [
          {
            required: true,
            message: "是否需要登录不能为空",
            trigger: "change",
          },
        ],
        checkVip: [
          { required: true, message: "是否需要VIP不能为空", trigger: "change" },
        ],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询全局变量列表 */
    getList() {
      this.loading = true;
      listGlobalVariable(this.queryParams).then((response) => {
        this.globalVariableList = response.rows;
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
        name: null,
        value: null,
        description: null,
        checkToken: null,
        checkVip: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
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
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加全局变量";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getGlobalVariable(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改全局变量";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateGlobalVariable(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addGlobalVariable(this.form).then((response) => {
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
      this.$modal
        .confirm('是否确认删除全局变量编号为"' + ids + '"的数据项？')
        .then(function () {
          return delGlobalVariable(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "system/globalVariable/export",
        {
          ...this.queryParams,
        },
        `globalVariable_${new Date().getTime()}.xlsx`
      );
    },
  },
};
</script>
