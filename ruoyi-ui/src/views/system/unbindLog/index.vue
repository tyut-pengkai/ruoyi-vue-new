<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="软件用户id" prop="appUserId">
        <el-input
          v-model="queryParams.appUserId"
          placeholder="请输入软件用户id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="软件id" prop="appId">
        <el-input
          v-model="queryParams.appId"
          placeholder="请输入软件id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="首次登录时间">
        <el-date-picker
          v-model="daterangeFirstLoginTime"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="最后登录时间">
        <el-date-picker
          v-model="daterangeLastLoginTime"
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
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="变动面值" prop="changeAmount">
        <el-input
          v-model="queryParams.changeAmount"
          placeholder="请输入变动面值"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="1：用户前台解绑，2：用户软件解绑(API)，3：管理员后台解绑" prop="unbindType">
        <el-select v-model="queryParams.unbindType" placeholder="请选择1：用户前台解绑，2：用户软件解绑(API)，3：管理员后台解绑" clearable>
          <el-option
            v-for="dict in dict.type.sys_unbind_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="解绑描述" prop="unbindDesc">
        <el-input
          v-model="queryParams.unbindDesc"
          placeholder="请输入解绑描述"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备码" prop="deviceCode">
        <el-input
          v-model="queryParams.deviceCode"
          placeholder="请输入设备码"
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
          v-hasPermi="['system:unbindLog:add']"
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
          v-hasPermi="['system:unbindLog:edit']"
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
          v-hasPermi="['system:unbindLog:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:unbindLog:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="unbindLogList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键" align="center" prop="id" />
      <el-table-column label="软件用户id" align="center" prop="appUserId" />
      <el-table-column label="软件id" align="center" prop="appId" />
      <el-table-column label="首次登录时间" align="center" prop="firstLoginTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.firstLoginTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="最后登录时间" align="center" prop="lastLoginTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.lastLoginTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="登录次数" align="center" prop="loginTimes" />
      <el-table-column label="变动面值" align="center" prop="changeAmount" />
      <el-table-column label="1：用户前台解绑，2：用户软件解绑(API)，3：管理员后台解绑" align="center" prop="unbindType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_unbind_type" :value="scope.row.unbindType"/>
        </template>
      </el-table-column>
      <el-table-column label="解绑描述" align="center" prop="unbindDesc" />
      <el-table-column label="用户过期时间后" align="center" prop="expireTimeAfter" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.expireTimeAfter, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="用户过期时间前" align="center" prop="expireTimeBefore" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.expireTimeBefore, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="用户剩余点数后" align="center" prop="pointAfter" />
      <el-table-column label="用户剩余点数前" align="center" prop="pointBefore" />
      <el-table-column label="设备码" align="center" prop="deviceCode" />
      <el-table-column label="设备码id" align="center" prop="deviceCodeId" />
      <el-table-column label="创建者" align="center" prop="createBy" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新者" align="center" prop="updateBy" />
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:unbindLog:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:unbindLog:remove']"
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

    <!-- 添加或修改解绑日志对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="软件用户id" prop="appUserId">
          <el-input v-model="form.appUserId" placeholder="请输入软件用户id" />
        </el-form-item>
        <el-form-item label="软件id" prop="appId">
          <el-input v-model="form.appId" placeholder="请输入软件id" />
        </el-form-item>
        <el-form-item label="首次登录时间" prop="firstLoginTime">
          <el-date-picker clearable
            v-model="form.firstLoginTime"
            type="date"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择首次登录时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="最后登录时间" prop="lastLoginTime">
          <el-date-picker clearable
            v-model="form.lastLoginTime"
            type="date"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择最后登录时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="登录次数" prop="loginTimes">
          <el-input v-model="form.loginTimes" placeholder="请输入登录次数" />
        </el-form-item>
        <el-form-item label="变动面值" prop="changeAmount">
          <el-input v-model="form.changeAmount" placeholder="请输入变动面值" />
        </el-form-item>
        <el-form-item label="1：用户前台解绑，2：用户软件解绑(API)，3：管理员后台解绑" prop="unbindType">
          <el-select v-model="form.unbindType" placeholder="请选择1：用户前台解绑，2：用户软件解绑(API)，3：管理员后台解绑">
            <el-option
              v-for="dict in dict.type.sys_unbind_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="解绑描述" prop="unbindDesc">
          <el-input v-model="form.unbindDesc" placeholder="请输入解绑描述" />
        </el-form-item>
        <el-form-item label="用户过期时间后" prop="expireTimeAfter">
          <el-date-picker clearable
            v-model="form.expireTimeAfter"
            type="date"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择用户过期时间后">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="用户过期时间前" prop="expireTimeBefore">
          <el-date-picker clearable
            v-model="form.expireTimeBefore"
            type="date"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择用户过期时间前">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="用户剩余点数后" prop="pointAfter">
          <el-input v-model="form.pointAfter" placeholder="请输入用户剩余点数后" />
        </el-form-item>
        <el-form-item label="用户剩余点数前" prop="pointBefore">
          <el-input v-model="form.pointBefore" placeholder="请输入用户剩余点数前" />
        </el-form-item>
        <el-form-item label="设备码" prop="deviceCode">
          <el-input v-model="form.deviceCode" placeholder="请输入设备码" />
        </el-form-item>
        <el-form-item label="设备码id" prop="deviceCodeId">
          <el-input v-model="form.deviceCodeId" placeholder="请输入设备码id" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="删除标志" prop="delFlag">
          <el-input v-model="form.delFlag" placeholder="请输入删除标志" />
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
import { listUnbindLog, getUnbindLog, delUnbindLog, addUnbindLog, updateUnbindLog } from "@/api/system/unbindLog";

export default {
  name: "UnbindLog",
  dicts: ['sys_unbind_type'],
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
      // 解绑日志表格数据
      unbindLogList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 删除标志时间范围
      daterangeFirstLoginTime: [],
      // 删除标志时间范围
      daterangeLastLoginTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        appUserId: null,
        appId: null,
        firstLoginTime: null,
        lastLoginTime: null,
        loginTimes: null,
        changeAmount: null,
        unbindType: null,
        unbindDesc: null,
        deviceCode: null,
        remark: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        appUserId: [
          { required: true, message: "软件用户id不能为空", trigger: "blur" }
        ],
        appId: [
          { required: true, message: "软件id不能为空", trigger: "blur" }
        ],
        firstLoginTime: [
          { required: true, message: "首次登录时间不能为空", trigger: "blur" }
        ],
        lastLoginTime: [
          { required: true, message: "最后登录时间不能为空", trigger: "blur" }
        ],
        changeAmount: [
          { required: true, message: "变动面值不能为空", trigger: "blur" }
        ],
        unbindType: [
          { required: true, message: "1：用户前台解绑，2：用户软件解绑(API)，3：管理员后台解绑不能为空", trigger: "change" }
        ],
        unbindDesc: [
          { required: true, message: "解绑描述不能为空", trigger: "blur" }
        ],
        deviceCode: [
          { required: true, message: "设备码不能为空", trigger: "blur" }
        ],
        deviceCodeId: [
          { required: true, message: "设备码id不能为空", trigger: "blur" }
        ],
        delFlag: [
          { required: true, message: "删除标志不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询解绑日志列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeFirstLoginTime && '' != this.daterangeFirstLoginTime) {
        this.queryParams.params["beginFirstLoginTime"] = this.daterangeFirstLoginTime[0];
        this.queryParams.params["endFirstLoginTime"] = this.daterangeFirstLoginTime[1];
      }
      if (null != this.daterangeLastLoginTime && '' != this.daterangeLastLoginTime) {
        this.queryParams.params["beginLastLoginTime"] = this.daterangeLastLoginTime[0];
        this.queryParams.params["endLastLoginTime"] = this.daterangeLastLoginTime[1];
      }
      listUnbindLog(this.queryParams).then(response => {
        this.unbindLogList = response.rows;
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
        appId: null,
        firstLoginTime: null,
        lastLoginTime: null,
        loginTimes: null,
        changeAmount: null,
        unbindType: null,
        unbindDesc: null,
        expireTimeAfter: null,
        expireTimeBefore: null,
        pointAfter: null,
        pointBefore: null,
        deviceCode: null,
        deviceCodeId: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        delFlag: null
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
      this.daterangeFirstLoginTime = [];
      this.daterangeLastLoginTime = [];
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
      this.title = "添加解绑日志";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getUnbindLog(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改解绑日志";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateUnbindLog(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addUnbindLog(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除解绑日志编号为"' + ids + '"的数据项？').then(function() {
        return delUnbindLog(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/unbindLog/export', {
        ...this.queryParams
      }, `unbindLog_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
