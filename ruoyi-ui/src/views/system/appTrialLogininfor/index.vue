<template>
  <div class="app-container">
    <el-form
      v-show="showSearch"
      ref="queryForm"
      :inline="true"
      :model="queryParams"
    >
      <el-form-item label="用户名" prop="userName">
        <el-input
          v-model="queryParams.userName"
          clearable
          placeholder="请输入用户名"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="用户ID" prop="appUserId">
        <el-input
          v-model="queryParams.appUserId"
          clearable
          placeholder="请输入用户ID"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="APP名" prop="appName">
        <el-input
          v-model="queryParams.appName"
          clearable
          placeholder="请输入APP名"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="登录IP" prop="ipaddr">
        <el-input
          v-model="queryParams.ipaddr"
          clearable
          placeholder="请输入登录IP地址"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="APP版本" prop="appVersion">
        <el-input
          v-model="queryParams.appVersion"
          clearable
          placeholder="请输入APP版本"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="登录地点" prop="loginLocation">
        <el-input
          v-model="queryParams.loginLocation"
          clearable
          placeholder="请输入登录地点"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备码" prop="deviceCode">
        <el-input
          v-model="queryParams.deviceCode"
          clearable
          placeholder="请输入设备码"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="浏览器类型" prop="browser">
        <el-input
          v-model="queryParams.browser"
          clearable
          placeholder="请输入浏览器类型"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="操作系统" prop="os">
        <el-input
          v-model="queryParams.os"
          clearable
          placeholder="请输入操作系统"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="登录状态" prop="status">
        <el-select
          v-model="queryParams.status"
          clearable
          placeholder="请选择登录状态"
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
      <!-- <el-form-item label="提示消息" prop="msg">
        <el-input
          v-model="queryParams.msg"
          clearable
          placeholder="请输入提示消息"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="访问时间">
        <el-date-picker
          v-model="daterangeLoginTime"
          end-placeholder="结束日期"
          range-separator="-"
          size="small"
          start-placeholder="开始日期"
          style="width: 240px"
          type="daterange"
          value-format="yyyy-MM-dd"
        ></el-date-picker>
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
      <!-- <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:appTrialLogininfor:add']"
          icon="el-icon-plus"
          plain
          size="mini"
          type="primary"
          @click="handleAdd"
        >新增
        </el-button
        >
      </el-col> -->
      <!-- <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:appTrialLogininfor:edit']"
          :disabled="single"
          icon="el-icon-edit"
          plain
          size="mini"
          type="success"
          @click="handleUpdate"
        >修改
        </el-button
        >
      </el-col> -->
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:appTrialLogininfor:remove']"
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
          v-hasPermi="['system:appTrialLogininfor:remove']"
          icon="el-icon-delete"
          plain
          size="mini"
          type="danger"
          @click="handleClean"
        >清空
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:appTrialLogininfor:export']"
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
      :data="appTrialLogininforList"
      :default-sort="defaultSort"
      @selection-change="handleSelectionChange"
      @sort-change="handleSortChange"
    >
      <el-table-column align="center" type="selection" width="55"/>
      <el-table-column align="center" label="访问编号" prop="infoId"/>
      <el-table-column
        :show-overflow-tooltip="true"
        :sort-orders="['descending', 'ascending']"
        align="center"
        label="用户名称"
        prop="userName"
        sortable="custom"
      />
      <!-- <el-table-column align="center" label="用户ID" prop="appUserId"/> -->
      <el-table-column align="center" label="APP名" prop="appName"/>
      <el-table-column align="center" label="登录IP" prop="ipaddr"/>
      <el-table-column align="center" label="APP版本" prop="appVersion"/>
      <el-table-column align="center" label="登录地点" prop="loginLocation"/>
      <el-table-column align="center" label="设备码" prop="deviceCode"/>
      <!-- <el-table-column align="center" label="浏览器类型" prop="browser" />
      <el-table-column align="center" label="操作系统" prop="os" /> -->
      <el-table-column align="center" label="登录状态" prop="status">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_common_status"
            :value="scope.row.status"
          />
        </template>
      </el-table-column>
      <el-table-column align="center" label="提示消息" prop="msg"/>
      <el-table-column
        :sort-orders="['descending', 'ascending']"
        align="center"
        label="登录日期"
        prop="loginTime"
        sortable="custom"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.loginTime) }}</span>
        </template>
      </el-table-column>
      <!-- <el-table-column
        align="center"
        class-name="small-padding fixed-width"
        label="操作"
        fixed="right"
      >
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['system:appTrialLogininfor:edit']"
            icon="el-icon-edit"
            size="mini"
            type="text"
            @click="handleUpdate(scope.row)"
            >修改
          </el-button>
          <el-button
            v-hasPermi="['system:appTrialLogininfor:remove']"
            icon="el-icon-delete"
            size="mini"
            type="text"
            @click="handleDelete(scope.row)"
            >删除
          </el-button>
        </template>
      </el-table-column> -->
    </el-table>

    <pagination
      v-show="total > 0"
      :limit.sync="queryParams.pageSize"
      :page.sync="queryParams.pageNum"
      :total="total"
      @pagination="getList"
    />

    <!-- 添加或修改系统访问记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="form.userName" placeholder="请输入用户名"/>
        </el-form-item>
        <el-form-item label="用户ID" prop="appUserId">
          <el-input v-model="form.appUserId" placeholder="请输入用户ID"/>
        </el-form-item>
        <el-form-item label="APP名" prop="appName">
          <el-input v-model="form.appName" placeholder="请输入APP名"/>
        </el-form-item>
        <el-form-item label="APP版本" prop="appVersion">
          <el-input v-model="form.appVersion" placeholder="请输入APP版本"/>
        </el-form-item>
        <el-form-item label="登录IP地址" prop="ipaddr">
          <el-input v-model="form.ipaddr" placeholder="请输入登录IP地址"/>
        </el-form-item>
        <el-form-item label="登录地点" prop="loginLocation">
          <el-input v-model="form.loginLocation" placeholder="请输入登录地点"/>
        </el-form-item>
        <el-form-item label="设备码" prop="deviceCode">
          <el-input v-model="form.deviceCode" placeholder="请输入设备码"/>
        </el-form-item>
        <el-form-item label="浏览器类型" prop="browser">
          <el-input v-model="form.browser" placeholder="请输入浏览器类型"/>
        </el-form-item>
        <el-form-item label="操作系统" prop="os">
          <el-input v-model="form.os" placeholder="请输入操作系统"/>
        </el-form-item>
        <el-form-item label="登录状态">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.sys_normal_disable"
              :key="dict.value"
              :label="dict.value"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="提示消息" prop="msg">
          <el-input v-model="form.msg" placeholder="请输入提示消息"/>
        </el-form-item>
        <el-form-item label="访问时间" prop="loginTime">
          <el-date-picker
            v-model="form.loginTime"
            clearable
            placeholder="选择访问时间"
            size="small"
            type="date"
            value-format="yyyy-MM-dd HH:mm:ss"
          >
          </el-date-picker>
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
  addAppTrialLogininfor,
  cleanTrialLogininfor,
  delAppTrialLogininfor,
  exportAppTrialLogininfor,
  getAppTrialLogininfor,
  listAppTrialLogininfor,
  updateAppTrialLogininfor,
} from "@/api/system/appTrialLogininfor";

export default {
  name: "AppTrialLogininfor",
  dicts: ["sys_common_status"],
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
      // 系统访问记录表格数据
      appTrialLogininforList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 访问时间时间范围
      daterangeLoginTime: [],
      // 默认排序
      defaultSort: {prop: "loginTime", order: "descending"},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: this.$store.state.settings.pageSize,
        userName: null,
        appUserId: null,
        appName: null,
        ipaddr: null,
        appVersion: null,
        loginLocation: null,
        deviceCode: null,
        browser: null,
        os: null,
        status: null,
        msg: null,
        loginTime: null,
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
    /** 查询系统访问记录列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeLoginTime && "" != this.daterangeLoginTime) {
        this.queryParams.params["beginLoginTime"] = this.daterangeLoginTime[0];
        this.queryParams.params["endLoginTime"] = this.daterangeLoginTime[1];
      }
      listAppTrialLogininfor(this.queryParams).then((response) => {
        this.appTrialLogininforList = response.rows;
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
        infoId: null,
        userName: null,
        appUserId: null,
        appName: null,
        ipaddr: null,
        appVersion: null,
        loginLocation: null,
        deviceCode: null,
        browser: null,
        os: null,
        status: "0",
        msg: null,
        loginTime: null,
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
      this.daterangeLoginTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.infoId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 排序触发事件 */
    handleSortChange(column, prop, order) {
      this.queryParams.orderByColumn = column.prop;
      this.queryParams.isAsc = column.order;
      this.getList();
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加系统访问记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const infoId = row.infoId || this.ids;
      getAppTrialLogininfor(infoId).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改系统访问记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.infoId != null) {
            updateAppTrialLogininfor(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAppTrialLogininfor(this.form).then((response) => {
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
      const infoIds = row.infoId || this.ids;
      this.$modal
        .confirm("是否确认删除数据项？")
        .then(function () {
          return delAppTrialLogininfor(infoIds);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {
        });
    },
    /** 清空按钮操作 */
    handleClean() {
      this.$modal
        .confirm("是否确认清空所有登录日志数据项？")
        .then(function () {
          return cleanTrialLogininfor();
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("清空成功");
        })
        .catch(() => {
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal
        .confirm("是否确认导出所有系统访问记录数据项？")
        .then(() => {
          this.exportLoading = true;
          return exportAppTrialLogininfor(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
          this.exportLoading = false;
        })
        .catch(() => {
        });
    },
  },
};
</script>
