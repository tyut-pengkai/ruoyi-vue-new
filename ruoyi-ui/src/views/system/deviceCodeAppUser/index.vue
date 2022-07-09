<template>
  <div class="app-container">
    <el-form
      v-show="showSearch"
      ref="queryForm"
      :inline="true"
      :model="queryParams"
      label-width="68px"
    >
      <el-form-item label="APP ID" prop="appUserId">
        <el-input
          v-model="queryParams.appUserId"
          clearable
          placeholder="请输入APP ID"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备码" prop="deviceCodeId">
        <el-input
          v-model="queryParams.deviceCodeId"
          clearable
          placeholder="请输入设备码"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="最后登录时间">
        <el-date-picker
          v-model="daterangeLastLoginTime"
          end-placeholder="结束日期"
          range-separator="-"
          size="small"
          start-placeholder="开始日期"
          style="width: 240px"
          type="daterange"
          value-format="yyyy-MM-dd"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="登录次数" prop="loginTimes">
        <el-input
          v-model="queryParams.loginTimes"
          clearable
          placeholder="请输入登录次数"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select
          v-model="queryParams.status"
          clearable
          placeholder="请选择状态"
          size="small"
        >
          <el-option
            v-for="dict in dict.type.sys_normal_disable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
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
          v-hasPermi="['system:appUserDeviceCode:add']"
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
          v-hasPermi="['system:appUserDeviceCode:edit']"
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
          v-hasPermi="['system:appUserDeviceCode:remove']"
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
          v-hasPermi="['system:appUserDeviceCode:export']"
          :loading="exportLoading"
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
      :data="appUserDeviceCodeList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column align="center" type="selection" width="55" />
      <el-table-column align="center" label="编号" prop="id" />
      <el-table-column align="center" label="APP ID" prop="appUserId" />
      <el-table-column align="center" label="设备码" prop="deviceCodeId" />
      <el-table-column align="center" label="登录次数" prop="loginTimes" />
      <el-table-column
        align="center"
        label="最后登录时间"
        prop="lastLoginTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.lastLoginTime, "{y}-{m}-{d}") }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="状态" prop="status">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_normal_disable"
            :value="scope.row.status"
          />
        </template>
      </el-table-column>
      <el-table-column align="center" label="备注" prop="remark" />
      <el-table-column
        align="center"
        class-name="small-padding fixed-width"
        label="操作"
        fixed="right"
      >
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['system:appUserDeviceCode:edit']"
            icon="el-icon-edit"
            size="mini"
            type="text"
            @click="handleUpdate(scope.row)"
            >修改
          </el-button>
          <el-button
            v-hasPermi="['system:appUserDeviceCode:remove']"
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

    <!-- 添加或修改软件用户与设备码关联对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="APP ID" prop="appUserId">
          <el-input v-model="form.appUserId" placeholder="请输入APP ID" />
        </el-form-item>
        <el-form-item label="设备码" prop="deviceCodeId">
          <el-input v-model="form.deviceCodeId" placeholder="请输入设备码" />
        </el-form-item>
        <el-form-item label="最后登录时间" prop="lastLoginTime">
          <el-date-picker
            v-model="form.lastLoginTime"
            clearable
            placeholder="选择最后登录时间"
            size="small"
            type="date"
            value-format="yyyy-MM-dd HH:mm:ss"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="登录次数" prop="loginTimes">
          <el-input v-model="form.loginTimes" placeholder="请输入登录次数" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.sys_normal_disable"
              :key="dict.value"
              :label="dict.value"
              >{{ dict.label }}
            </el-radio>
          </el-radio-group>
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
  addAppUserDeviceCode,
  delAppUserDeviceCode,
  exportAppUserDeviceCode,
  getAppUserDeviceCode,
  listAppUserDeviceCode,
  updateAppUserDeviceCode,
} from "@/api/system/appUserDeviceCode";

export default {
  name: "AppUserDeviceCode",
  dicts: ["sys_normal_disable"],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
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
      // 软件用户与设备码关联表格数据
      appUserDeviceCodeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 备注时间范围
      daterangeLastLoginTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: this.$store.state.settings.pageSize,
        appUserId: null,
        deviceCodeId: null,
        lastLoginTime: null,
        loginTimes: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询软件用户与设备码关联列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (
        null != this.daterangeLastLoginTime &&
        "" != this.daterangeLastLoginTime
      ) {
        this.queryParams.params["beginLastLoginTime"] =
          this.daterangeLastLoginTime[0];
        this.queryParams.params["endLastLoginTime"] =
          this.daterangeLastLoginTime[1];
      }
      listAppUserDeviceCode(this.queryParams).then((response) => {
        this.appUserDeviceCodeList = response.rows;
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
        appUserId: null,
        deviceCodeId: null,
        lastLoginTime: null,
        loginTimes: null,
        status: "0",
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
      this.daterangeLastLoginTime = [];
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
      this.title = "添加软件用户与设备码关联";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getAppUserDeviceCode(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改软件用户与设备码关联";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateAppUserDeviceCode(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAppUserDeviceCode(this.form).then((response) => {
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
        .confirm("是否确认删除数据项？")
        .then(function () {
          return delAppUserDeviceCode(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal
        .confirm("是否确认导出所有软件用户与设备码关联数据项？")
        .then(() => {
          this.exportLoading = true;
          return exportAppUserDeviceCode(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
          this.exportLoading = false;
        })
        .catch(() => {});
    },
  },
};
</script>
