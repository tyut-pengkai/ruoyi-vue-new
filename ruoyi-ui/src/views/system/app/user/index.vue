<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户ID"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="软件ID" prop="appId">
        <el-input
          v-model="queryParams.appId"
          placeholder="请输入软件ID"
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
      <el-form-item label="过期时间">
        <el-date-picker
          v-model="daterangeExpireTime"
          size="small"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="剩余点数" prop="point">
        <el-input
          v-model="queryParams.point"
          placeholder="请输入剩余点数"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="登录码" prop="loginCode">
        <el-input
          v-model="queryParams.loginCode"
          placeholder="请输入登录码"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建者" prop="createBy">
        <el-input
          v-model="queryParams.createBy"
          placeholder="请输入创建者"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
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
          v-hasPermi="['system:app_user:add']"
          >新增</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:app_user:edit']"
          >修改</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:app_user:remove']"
          >删除</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['system:app_user:export']"
          >导出</el-button
        >
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="app_userList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column
        type="selection"
        width="55"
        align="center"
        fixed="left"
      />
      <el-table-column label="" type="index" align="center" />
      <div v-if="showTitle.account">
        <el-table-column label="所属账号" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.app.authType === '0'">
              {{ scope.row.user.nickName }}({{ scope.row.user.userName }})
            </span>
            <span v-else>--</span>
          </template>
        </el-table-column>
      </div>
      <div v-if="showTitle.loginCode">
        <el-table-column label="登录码" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.app.authType === '1'">
              {{ scope.row.user.loginCode }}
            </span>
            <span v-else>--</span>
          </template>
        </el-table-column>
      </div>
      <el-table-column label="所属软件" align="center" prop="app.appName" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_normal_disable"
            :value="scope.row.status"
          />
        </template>
      </el-table-column>
      <el-table-column
        label="登录用户数限制"
        align="center"
        prop="loginLimitU"
      />
      <el-table-column
        label="登录机器数限制"
        align="center"
        prop="loginLimitM"
      />
      <el-table-column label="免费余额" align="center" prop="freeBalance" />
      <el-table-column label="支付余额" align="center" prop="payBalance" />
      <el-table-column label="总消费" align="center" prop="totalPay" />
      <el-table-column
        label="最后登录时间"
        align="center"
        prop="lastLoginTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>
            {{ parseTime(scope.row.lastLoginTime, "{y}-{m}-{d} {h}:{m}:{s}") }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="登录次数" align="center" prop="loginTimes">
        <template slot-scope="scope">
          <span>{{ scope.row.loginTimes }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="过期时间"
        align="center"
        prop="expireTime"
        width="180"
        v-if="showTitle.time"
      >
        <template slot-scope="scope">
          <span v-if="scope.row.app.billType === '0'">
            {{ parseTime(scope.row.expireTime, "{y}-{m}-{d} {h}:{m}:{s}") }}
          </span>
          <span v-else>--</span>
        </template>
      </el-table-column>
      <el-table-column
        label="剩余点数"
        align="center"
        prop="point"
        v-if="showTitle.point"
      >
        <template slot-scope="scope">
          <span v-if="scope.row.app.billType === '1'">
            {{ scope.row.point }}
          </span>
          <span v-else>--</span>
        </template>
      </el-table-column>
      <el-table-column label="创建者" align="center" prop="createBy" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
        width="150px"
        min-width="150px"
        fixed="right"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:app_user:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:app_user:remove']"
            >删除</el-button
          >
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

    <!-- 添加或修改软件用户对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules">
        <el-form-item>
          <el-col :span="8">
            <el-form-item
              label="所属账号"
              v-if="form.app && form.app.authType === '0'"
            >
              {{ form.user.nickName }}({{ form.user.userName }})
            </el-form-item>
            <el-form-item
              label="登录码"
              v-if="form.app && form.app.authType === '1'"
            >
              {{ form.user.loginCode }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="所属软件" v-if="form.app">
              {{ form.app.appName }}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="计费类型" v-if="form.app">
              <dict-tag
                :options="dict.type.sys_bill_type"
                :value="form.app.billType"
              />
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item>
          <el-col :span="8">
            <el-form-item label="总登录次数">
              <div v-if="form.loginTimes">{{ form.loginTimes }} 次</div>
              <div v-else>从未登录过</div>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="最近一次登录时间">
              <div v-if="form.lastLoginTime">
                {{ form.lastLoginTime }}
              </div>
              <div v-else>从未登录过</div>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="用户注册时间" v-if="form.createTime">
              {{ parseTime(form.createTime, "{y}-{m}-{d} {h}:{m}:{s}") }}
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item>
          <el-col :span="12">
            <el-form-item label="登录用户数限制" prop="loginLimitU">
              <span>
                <el-tooltip
                  content="登录用户数量限制，整数，-1为不限制，默认为-1"
                  placement="top"
                >
                  <i
                    class="el-icon-question"
                    style="margin-left: -12px; margin-right: 10px"
                  ></i>
                </el-tooltip>
              </span>
              <el-input-number
                v-model="form.loginLimitU"
                controls-position="right"
                :min="-1"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="登录机器数限制" prop="loginLimitM">
              <span>
                <el-tooltip
                  content="登录机器数量限制，整数，-1为不限制，默认为-1"
                  placement="top"
                >
                  <i
                    class="el-icon-question"
                    style="margin-left: -12px; margin-right: 10px"
                  ></i>
                </el-tooltip>
              </span>
              <el-input-number
                v-model="form.loginLimitM"
                controls-position="right"
                :min="-1"
              />
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item>
          <el-col :span="8">
            <el-form-item label="免费余额" prop="freeBalance">
              <el-input-number
                v-model="form.freeBalance"
                controls-position="right"
                :precision="2"
                :step="0.01"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="充值余额" prop="payBalance">
              <el-input-number
                v-model="form.payBalance"
                controls-position="right"
                :precision="2"
                :step="0.01"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="总充值" prop="totalPay">
              <el-input-number
                v-model="form.totalPay"
                controls-position="right"
                :precision="2"
                :step="0.01"
              />
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item>
          <el-col :span="12">
            <el-form-item label="用户状态">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in dict.type.sys_normal_disable"
                  :key="dict.value"
                  :label="dict.value"
                  >{{ dict.label }}</el-radio
                >
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="过期时间"
              prop="expireTime"
              v-if="form.app && form.app.billType === '0'"
            >
              <el-date-picker
                clearable
                size="small"
                v-model="form.expireTime"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="选择过期时间"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item
              label="剩余点数"
              prop="point"
              v-if="form.app && form.app.billType === '1'"
            >
              <el-input-number v-model="form.point" controls-position="right" />
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
  listApp_user,
  getApp_user,
  delApp_user,
  addApp_user,
  updateApp_user,
  exportApp_user,
} from "@/api/system/app_user";

export default {
  name: "App_user",
  dicts: ["sys_normal_disable", "sys_bill_type"],
  data() {
    return {
      // 对应列是否显示
      showTitle: {
        account: false,
        loginCode: false,
        time: false,
        point: false,
      },
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
      // 软件用户表格数据
      app_userList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 备注时间范围
      daterangeLastLoginTime: [],
      // 备注时间范围
      daterangeExpireTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        appId: null,
        status: null,
        lastLoginTime: null,
        expireTime: null,
        point: null,
        loginCode: null,
        createBy: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        status: [{ required: true, message: "状态不能为空", trigger: "blur" }],
        loginLimitU: [
          {
            required: true,
            message: "登录用户数量限制，整数，-1为不限制，默认为-1不能为空",
            trigger: "blur",
          },
        ],
        loginLimitM: [
          {
            required: true,
            message: "登录机器数量限制，整数，-1为不限制，默认为-1不能为空",
            trigger: "blur",
          },
        ],
        freeBalance: [
          { required: true, message: "免费余额不能为空", trigger: "blur" },
        ],
        payBalance: [
          { required: true, message: "支付余额不能为空", trigger: "blur" },
        ],
        totalPay: [
          { required: true, message: "总消费不能为空", trigger: "blur" },
        ],
        expireTime: [
          { required: true, message: "过期时间不能为空", trigger: "blur" },
        ],
        point: [
          { required: true, message: "剩余点数不能为空", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    const appId = this.$route.params && this.$route.params.appId;
    const appName = this.$route.query && this.$route.query.appName;
    const title = "软件用户";
    const route = Object.assign({}, this.$route, {
      title: `${title}-${appName}`,
    });
    this.$store.dispatch("tagsView/updateVisitedView", route);
    this.getList();
  },
  methods: {
    /** 查询软件用户列表 */
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
      if (null != this.daterangeExpireTime && "" != this.daterangeExpireTime) {
        this.queryParams.params["beginExpireTime"] =
          this.daterangeExpireTime[0];
        this.queryParams.params["endExpireTime"] = this.daterangeExpireTime[1];
      }
      listApp_user(this.queryParams).then((response) => {
        this.app_userList = response.rows;
        this.total = response.total;
        if (response.total > 0) {
          this.checkAuthTypeAndBillType(this.app_userList);
        }
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
        appUserId: null,
        userId: null,
        appId: null,
        status: "0",
        loginLimitU: null,
        loginLimitM: null,
        freeBalance: null,
        payBalance: null,
        totalPay: null,
        lastLoginTime: null,
        loginTimes: null,
        pwdErrorTimes: null,
        expireTime: null,
        point: null,
        loginCode: null,
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
      this.daterangeExpireTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.appUserId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加软件用户";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const appUserId = row.appUserId || this.ids;
      getApp_user(appUserId).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改软件用户";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.appUserId != null) {
            updateApp_user(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addApp_user(this.form).then((response) => {
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
      const appUserIds = row.appUserId || this.ids;
      this.$modal
        .confirm('是否确认删除软件用户编号为"' + appUserIds + '"的数据项？')
        .then(function () {
          return delApp_user(appUserIds);
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
        .confirm("是否确认导出所有软件用户数据项？")
        .then(() => {
          this.exportLoading = true;
          return exportApp_user(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
          this.exportLoading = false;
        })
        .catch(() => {});
    },
    /** 判断显示对应的认证模式和计费模式 */
    checkAuthTypeAndBillType(appUserList) {
      for (let item of appUserList) {
        if (item.app && item.app.authType === "0") {
          this.showTitle.account = true;
        } else if (item.app && item.app.authType === "1") {
          this.showTitle.loginCode = true;
        }
        if (item.app && item.app.billType === "0") {
          this.showTitle.time = true;
        } else if (item.app && item.app.billType === "1") {
          this.showTitle.point = true;
        }
      }
    },
  },
};
</script>
