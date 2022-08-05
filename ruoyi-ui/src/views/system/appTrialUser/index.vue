<template>
  <div class="app-container">
    <el-form
      v-show="showSearch"
      ref="queryForm"
      :inline="true"
      :model="queryParams"
      size="small"
    >
      <el-form-item label="软件ID" prop="appId">
        <el-input
          v-model="queryParams.appId"
          clearable
          placeholder="请输入软件ID"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="最后登录时间" prop="lastLoginTime">
        <el-date-picker
          v-model="queryParams.lastLoginTime"
          clearable
          placeholder="请选择最后登录时间"
          type="datetime"
          value-format="yyyy-MM-dd HH:mm:ss"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item label="本周期已试用次数" prop="loginTimes">
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
      </el-form-item>
      <el-form-item label="下次试用时间" prop="nextEnableTime">
        <el-date-picker
          v-model="queryParams.nextEnableTime"
          clearable
          placeholder="请选择下次试用时间"
          type="datetime"
          value-format="yyyy-MM-dd HH:mm:ss"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item label="过期时间" prop="expireTime">
        <el-date-picker
          v-model="queryParams.expireTime"
          clearable
          placeholder="请选择过期时间"
          type="datetime"
          value-format="yyyy-MM-dd HH:mm:ss"
        >
        </el-date-picker>
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
        </el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
        >重置
        </el-button
        >
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:appTrialUser:add']"
          icon="el-icon-plus"
          plain
          size="mini"
          type="primary"
          @click="handleAdd"
        >新增
        </el-button
        >
      </el-col>
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
        </el-button
        >
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
        </el-button
        >
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
        </el-button
        >
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
      <el-table-column align="center" label="软件ID" prop="appId"/>
      <el-table-column align="center" label="状态" prop="status"/>
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
      <el-table-column align="center" label="备注" prop="remark"/>
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
          </el-button
          >
          <el-button
            v-hasPermi="['system:appTrialUser:remove']"
            icon="el-icon-delete"
            size="mini"
            type="text"
            @click="handleDelete(scope.row)"
          >删除
          </el-button
          >
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
        <el-form-item label="软件ID" prop="appId">
          <el-input v-model="form.appId" placeholder="请输入软件ID"/>
        </el-form-item>
        <el-form-item label="最后登录时间" prop="lastLoginTime">
          <el-date-picker
            v-model="form.lastLoginTime"
            clearable
            placeholder="请选择最后登录时间"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="本周期已试用次数" prop="loginTimes">
          <el-input
            v-model="form.loginTimes"
            placeholder="请输入本周期已试用次数"
          />
        </el-form-item>
        <el-form-item label="总已试用次数" prop="loginTimesAll">
          <el-input
            v-model="form.loginTimesAll"
            placeholder="请输入总已试用次数"
          />
        </el-form-item>
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
        <el-form-item label="登录IP" prop="loginIp">
          <el-input v-model="form.loginIp" placeholder="请输入登录IP"/>
        </el-form-item>
        <el-form-item label="设备码" prop="deviceCode">
          <el-input v-model="form.deviceCode" placeholder="请输入设备码"/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form.remark"
            placeholder="请输入内容"
            type="textarea"
          />
        </el-form-item>
        <el-form-item label="删除标志" prop="delFlag">
          <el-input v-model="form.delFlag" placeholder="请输入删除标志"/>
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
  addAppTrialUser,
  delAppTrialUser,
  getAppTrialUser,
  listAppTrialUser,
  updateAppTrialUser,
} from "@/api/system/appTrialUser";

export default {
  name: "AppTrialUser",
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
      // 试用信息表格数据
      appTrialUserList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
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
        loginTimes: [
          {
            required: true,
            message: "单次已试用次数不能为空",
            trigger: "blur",
          },
        ],
        loginIp: [
          {required: true, message: "登录IP不能为空", trigger: "blur"},
        ],
        delFlag: [
          {required: true, message: "删除标志不能为空", trigger: "blur"},
        ],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询试用信息列表 */
    getList() {
      this.loading = true;
      listAppTrialUser(this.queryParams).then((response) => {
        this.appTrialUserList = response.rows;
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
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        delFlag: null,
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
      this.ids = selection.map((item) => item.appTrialUserId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加试用信息";
    },
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
          } else {
            addAppTrialUser(this.form).then((response) => {
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
