<template>
  <div class="app-container">
    <el-form
      v-show="showSearch"
      ref="queryForm"
      :inline="true"
      :model="queryParams"
      size="small"
    >
      <el-form-item label="所属软件" prop="appId">
        <el-select
          v-model="queryParams.appId"
          filterable
          clearable
          placeholder="请选择"
          prop="appId"
          @change="changeSearchApp"
        >
          <el-option
            v-for="item in appList"
            :key="item.appId"
            :disabled="item.disabled"
            :label="
              '[' +
              (item.authType == '0' ? '账号' : '单码') +
              (item.billType == '0' ? '计时' : '计点') +
              '] ' +
              item.appName
            "
            :value="item.appId"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="请选择状态"
          clearable
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
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd HH:mm:ss"
          range-separator="-"
          start-placeholder="开始日期"
          style="width: 240px"
          type="datetimerange"
        ></el-date-picker>
      </el-form-item>
      <!--      <el-form-item label="本周期已试用次数" prop="loginTimes">
        <el-input
          v-model="queryParams.loginTimes"
          clearable
          placeholder="请输入单次已试用次数"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="总已试用次数" prop="loginTimesAll">
        <el-input
          v-model="queryParams.loginTimesAll"
          clearable
          placeholder="请输入总已试用次数"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
      <el-form-item label="下次试用时间">
        <el-date-picker
          v-model="daterangeNextEnableTime"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd HH:mm:ss"
          range-separator="-"
          start-placeholder="开始日期"
          style="width: 240px"
          type="datetimerange"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="过期时间">
        <el-date-picker
          v-model="daterangeExpireTime"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd HH:mm:ss"
          range-separator="-"
          start-placeholder="开始日期"
          style="width: 240px"
          type="datetimerange"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="登录IP" prop="loginIp">
        <el-input
          v-model="queryParams.loginIp"
          clearable
          placeholder="请输入登录IP"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备码" prop="deviceCode">
        <el-input
          v-model="queryParams.deviceCode"
          clearable
          placeholder="请输入设备码"
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
      <!-- <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:appTrialUser:add']"
          icon="el-icon-plus"
          plain
          size="mini"
          type="primary"
          @click="handleAdd"
          >新增
        </el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:appTrialUser:edit']"
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
          v-hasPermi="['system:appTrialUser:remove']"
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
          v-hasPermi="['system:appTrialUser:export']"
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
      :data="appTrialUserList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column align="center" type="selection" width="55"/>
      <el-table-column align="center" label="ID" prop="appTrialUserId"/>
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="所属软件"
      >
        <template slot-scope="scope">
          {{ scope.row.app.appName }}
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
      <el-table-column
        align="center"
        label="最后登录时间"
        prop="lastLoginTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.lastLoginTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="本周期已试用次数"
        prop="loginTimes"
      />
      <el-table-column
        align="center"
        label="总已试用次数"
        prop="loginTimesAll"
      />
      <el-table-column
        align="center"
        label="下次试用时间"
        prop="nextEnableTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.nextEnableTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="过期时间"
        prop="expireTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.expireTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="登录IP" prop="loginIp"/>
      <el-table-column align="center" label="设备码" prop="deviceCode"/>
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="备注"
        prop="remark"
      />
      <el-table-column
        align="center"
        class-name="small-padding fixed-width"
        label="操作"
      >
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['system:appTrialUser:edit']"
            icon="el-icon-edit"
            size="mini"
            type="text"
            @click="handleUpdate(scope.row)"
          >修改
          </el-button>
          <el-button
            v-hasPermi="['system:appTrialUser:remove']"
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

    <!-- 添加或修改试用信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="800px">
      <el-form ref="form" :model="form" :rules="rules">
        <div v-if="form.appTrialUserId && form.app">
          <el-form-item prop="">
            <el-col :span="12">
              <el-form-item label="所属软件" prop="appName">
                {{ form.app.appName }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="计费类型" prop="billType">
                <dict-tag
                  :options="dict.type.sys_bill_type"
                  :value="form.app.billType"
                />
              </el-form-item>
            </el-col>
          </el-form-item>
          <el-form-item prop="">
            <el-col :span="12">
              <el-form-item label="本周期已试用次数" prop="loginTimes">
                {{ form.loginTimes ? form.loginTimes + "次" : "从未登录过" }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="最近登录时间" prop="lastLoginTime">
                <div v-if="form.lastLoginTime">
                  {{ form.lastLoginTime }}
                </div>
                <div v-else>从未登录过</div>
              </el-form-item>
            </el-col>
          </el-form-item>
          <el-form-item prop="">
            <el-col :span="12">
              <el-form-item label="总已试用次数" prop="loginTimesAll">
                {{
                  form.loginTimesAll ? form.loginTimesAll + "次" : "从未登录过"
                }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="下次试用时间" prop="nextEnableTime">
                <el-date-picker
                  v-model="form.nextEnableTime"
                  clearable
                  placeholder="请选择下次试用时间"
                  type="datetime"
                  value-format="yyyy-MM-dd HH:mm:ss"
                >
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-form-item>
          <el-form-item prop="">
            <el-col :span="12">
              <el-form-item label="用户状态" prop="status">
                <el-radio-group v-model="form.status">
                  <el-radio
                    v-for="dict in dict.type.sys_normal_disable"
                    :key="dict.value"
                    :label="dict.value"
                  >{{ dict.label }}
                  </el-radio
                  >
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="过期时间" prop="expireTime">
                <el-date-picker
                  v-model="form.expireTime"
                  clearable
                  placeholder="请选择过期时间"
                  type="datetime"
                  value-format="yyyy-MM-dd HH:mm:ss"
                >
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-form-item>
          <el-form-item prop="">
            <el-col :span="12">
              <el-form-item label="登录IP" prop="loginIp">
                <el-input
                  v-model="form.loginIp"
                  placeholder="请输入登录IP"
                  style="width: 240px"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="设备码" prop="deviceCode">
                <el-input
                  v-model="form.deviceCode"
                  placeholder="请输入设备码"
                  style="width: 240px"
                />
              </el-form-item>
            </el-col>
          </el-form-item>
          <el-form-item label="备注" prop="remark">
            <el-input
              v-model="form.remark"
              placeholder="请输入内容"
              type="textarea"
            />
          </el-form-item>
          <el-form-item prop="">
            <el-col :span="12">
              <el-form-item label="创建人" prop="createBy">
                {{ form.createBy }}
              </el-form-item>
              <el-form-item label="创建时间" prop="createTime">
                {{ form.createTime }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="最后更新" prop="updateBy">
                {{ form.updateBy }}
              </el-form-item>
              <el-form-item label="更新时间" prop="updateTime">
                {{ form.updateTime }}
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
import {delAppTrialUser, getAppTrialUser, listAppTrialUser, updateAppTrialUser,} from "@/api/system/appTrialUser";
import {getApp, listApp} from "@/api/system/app";

export default {
  name: "AppTrialUser",
  dicts: ["sys_normal_disable", "sys_bill_type"],
  data() {
    return {
      appList: [],
      appMap: [],
      // 软件数据
      app: null,
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
      // 试用信息表格数据
      appTrialUserList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 删除标志时间范围
      daterangeLastLoginTime: [],
      // 删除标志时间范围
      daterangeNextEnableTime: [],
      // 删除标志时间范围
      daterangeExpireTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        appId: null,
        status: null,
        lastLoginTime: null,
        loginTimes: null,
        loginTimesAll: null,
        nextEnableTime: null,
        expireTime: null,
        loginIp: null,
        deviceCode: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        appId: [{required: true, message: "软件ID不能为空", trigger: "blur"}],
        status: [{required: true, message: "状态不能为空", trigger: "blur"}],
        expireTime: [
          {required: true, message: "过期时间不能为空", trigger: "blur"},
        ],
        nextEnableTime: [
          {required: true, message: "下次试用时间不能为空", trigger: "blur"},
        ],
        loginIp: [
          {required: true, message: "登录IP不能为空", trigger: "blur"},
        ],
        deviceCode: [
          {required: true, message: "设备码不能为空", trigger: "blur"},
        ],
      },
    };
  },
  created() {
    // const appId = this.$route.params && this.$route.params.appId;
    const appId = this.$route.query && this.$route.query.appId;
    this.getAppList();
    // const appName = this.$route.query && this.$route.query.appName;
    // const title = "软件用户管理";
    //     // const appName = this.app.appName;
    //     const route = Object.assign({}, this.$route, {
    //       title: `${title}`,
    //     });
    //     this.$store.dispatch("tagsView/updateVisitedView", route);
    if (appId != undefined && appId != null) {
      getApp(appId).then((response) => {
        this.app = response.data;
        this.getList();
      });
    } else {
      this.getList();
      // this.$modal.alertError("未获取到当前软件信息");
    }
  },
  methods: {
    /** 查询试用信息列表 */
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
      if (
        null != this.daterangeNextEnableTime &&
        "" != this.daterangeNextEnableTime
      ) {
        this.queryParams.params["beginNextEnableTime"] =
          this.daterangeNextEnableTime[0];
        this.queryParams.params["endNextEnableTime"] =
          this.daterangeNextEnableTime[1];
      }
      if (null != this.daterangeExpireTime && "" != this.daterangeExpireTime) {
        this.queryParams.params["beginExpireTime"] =
          this.daterangeExpireTime[0];
        this.queryParams.params["endExpireTime"] = this.daterangeExpireTime[1];
      }
      if (this.app) {
        this.queryParams.appId = this.app.appId;
      }
      listAppTrialUser(this.queryParams).then((response) => {
        this.appTrialUserList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    getAppList() {
      this.loading = true;
      let queryParams = {};
      queryParams.params = {};
      listApp(queryParams).then((response) => {
        this.appList = response.rows;
        for (let app of this.appList) {
          this.appMap[app["appId"]] = app;
        }
        this.loading = false;
      });
    },
    changeSearchApp(appId) {
      this.loading = true;
      this.app = this.appMap[appId];
      this.getList();
      this.loading = false;
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        appTrialUserId: null,
        appId: null,
        status: "0",
        lastLoginTime: null,
        loginTimes: null,
        loginTimesAll: null,
        nextEnableTime: null,
        expireTime: null,
        loginIp: null,
        deviceCode: null,
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
      this.daterangeNextEnableTime = [];
      this.daterangeExpireTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.appTrialUserId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    // /** 新增按钮操作 */
    // handleAdd() {
    //   this.reset();
    //   this.open = true;
    //   this.title = "添加试用信息";
    // },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const appTrialUserId = row.appTrialUserId || this.ids;
      getAppTrialUser(appTrialUserId).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改试用信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.appTrialUserId != null) {
            updateAppTrialUser(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
            // } else {
            //   addAppTrialUser(this.form).then((response) => {
            //     this.$modal.msgSuccess("新增成功");
            //     this.open = false;
            //     this.getList();
            //   });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const appTrialUserIds = row.appTrialUserId || this.ids;
      this.$modal
        .confirm(
          '是否确认删除试用信息编号为"' + appTrialUserIds + '"的数据项？'
        )
        .then(function () {
          return delAppTrialUser(appTrialUserIds);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "system/appTrialUser/export",
        {
          ...this.queryParams,
        },
        `appTrialUser_${new Date().getTime()}.xlsx`
      );
    },
  },
};
</script>
