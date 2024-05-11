<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
    >
      <el-form-item label="设备码" prop="deviceCodeId">
        <el-input
          v-model="searchDeviceCode"
          placeholder="请输入设备码"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="最后登录时间">
        <el-date-picker
          v-model="daterangeLastLoginTime"
          size="small"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="登录次数" prop="loginTimes">
        <el-input
          v-model="queryParams.loginTimes"
          placeholder="请输入登录次数"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="请选择状态"
          clearable
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
          type="primary"
          icon="el-icon-search"
          size="mini"
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
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['agent:appUserDeviceCode:add']"
          >新增</el-button
        >
      </el-col> -->
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['agent:appUserDeviceCode:edit']"
          >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['agent:appUserDeviceCode:remove']"
          >解绑
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['agent:appUserDeviceCode:export']"
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
      <!-- <el-table-column align="center" label="" type="index"/> -->
      <el-table-column align="center" label="编号" prop="id" />
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="设备码"
        prop="deviceCode"
      >
        <template slot-scope="scope">
          <span>{{ scope.row.deviceCode.deviceCode }}</span>
        </template>
      </el-table-column>

      <el-table-column
        label="最后登录时间"
        align="center"
        prop="lastLoginTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{
            scope.row.lastLoginTime == null
              ? "从未登录过"
              : parseTime(scope.row.lastLoginTime)
          }}</span>
        </template>
      </el-table-column>
      <el-table-column label="登录次数" align="center" prop="loginTimes">
        <template slot-scope="scope">
          {{
            scope.row.loginTimes ? scope.row.loginTimes + "次" : "从未登录过"
          }}
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            active-value="0"
            inactive-value="1"
            @change="handleStatusChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column align="center" label="备注" prop="remark" />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
        fixed="right"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['agent:appUserDeviceCode:edit']"
            >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['agent:appUserDeviceCode:remove']"
            :disabled="!$auth.hasAgentPermi('enableUnbindAppUser')"
            >解绑
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改软件用户与设备码关联对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules">
        <div v-if="form.id">
          <el-form-item>
            <el-col :span="12">
              <el-form-item label="设备码">
                {{ form.deviceCode.deviceCode }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="最后登录" prop="lastLoginTime">
                {{ form.lastLoginTime ? form.lastLoginTime : "从未登录过" }}
              </el-form-item>
            </el-col>
          </el-form-item>
          <el-form-item>
            <el-col :span="12">
              <el-form-item label="状态">
                <el-radio-group v-model="form.status">
                  <el-radio
                    v-for="dict in dict.type.sys_normal_disable"
                    :key="dict.value"
                    :label="dict.value"
                  >
                    {{ dict.label }}
                  </el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="登录次数" prop="loginTimes">
                {{ form.loginTimes ? form.loginTimes + "次" : "从未登录过" }}
              </el-form-item>
            </el-col>
          </el-form-item>
          <el-form-item label="备注" prop="remark">
            <el-input
              v-model="form.remark"
              type="textarea"
              placeholder="请输入内容"
            />
          </el-form-item>

          <el-form-item>
            <el-col :span="12">
              <el-form-item label="创建人" prop="createBy">
                {{ form.createBy }}
              </el-form-item>
              <el-form-item label="创建时间" prop="createTime"
                >{{ form.createTime }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="最后更新" prop="updateBy">
                {{ form.updateBy }}
              </el-form-item>
              <el-form-item label="更新时间" prop="updateTime"
                >{{ form.updateTime }}
              </el-form-item>
            </el-col>
          </el-form-item>
        </div>
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
  changeAppUserDeviceCodeStatus,
  delAppUserDeviceCode,
  exportAppUserDeviceCode,
  getAppUserDeviceCode,
  listAppUserDeviceCode,
  updateAppUserDeviceCode,
} from "@/api/agent/appUserDeviceCode";
import {getAppUser} from "@/api/agent/appUser";

export default {
  name: "AppUserDeviceCode",
  dicts: ["sys_normal_disable"],
  data() {
    return {
      // 软件用户
      appUser: null,
      // 设备码
      searchDeviceCode: null,
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
    // const appUserId = this.$route.params && this.$route.params.appUserId;
    const appUserId = this.$route.query && this.$route.query.appUserId;
    if (appUserId != undefined && appUserId != null) {
      getAppUser(appUserId).then((response) => {
        this.appUser = response.data;
        // const title = "设备码管理";
        // const appName = this.appUser.app.appName;
        // const appUserName =
        //   this.appUser.user.nickName + "(" + this.appUser.user.userName + ")";
        // const route = Object.assign({}, this.$route, {
        //   title: `${title}-${appName}-${appUserName}`,
        // });
        // this.$store.dispatch("tagsView/updateVisitedView", route);
        this.getList();
      });
    } else {
      this.$modal.alertError("未获取到当前用户信息");
    }
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
      if (null != this.searchDeviceCode && "" != this.searchDeviceCode) {
        this.queryParams.params["searchDeviceCode"] = this.searchDeviceCode;
      }
      this.queryParams.appUserId = this.appUser.appUserId;
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
    // handleAdd() {
    //   this.reset();
    //   this.open = true;
    //   this.title = "添加软件用户与设备码关联";
    // },
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
        .confirm("是否确认解绑该设备？")
        .then(function () {
          return delAppUserDeviceCode(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("解绑成功");
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
    // 状态修改
    handleStatusChange(row) {
      let text = row.status === "0" ? "启用" : "停用";
      this.$modal
        .confirm("确认要" + text + '"' + row.deviceCode.deviceCode + '"吗？')
        .then(function () {
          return changeAppUserDeviceCodeStatus(row.id, row.status);
        })
        .then(() => {
          this.$modal.msgSuccess(text + "成功");
        })
        .catch(function () {
          row.status = row.status === "0" ? "1" : "0";
        });
    },
  },
};
</script>
