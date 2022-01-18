<template>
  <div class="app-container">
    <el-form
      v-show="showSearch"
      ref="queryForm"
      :inline="true"
      :model="queryParams"
    >
      <el-form-item label="版本名称" prop="versionName">
        <el-input
          v-model="queryParams.versionName"
          clearable
          placeholder="请输入版本名称"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="版本号" prop="versionNo">
        <el-input
          v-model="queryParams.versionNo"
          clearable
          placeholder="请输入版本号"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="版本状态" prop="status">
        <el-select
          v-model="queryParams.status"
          clearable
          placeholder="请选择版本状态"
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
      <el-form-item label="软件MD5" prop="md5">
        <el-input
          v-model="queryParams.md5"
          clearable
          placeholder="请输入软件MD5"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="daterangeCreateTime"
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
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:appVersion:add']"
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
          v-hasPermi="['system:appVersion:edit']"
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
          v-hasPermi="['system:appVersion:remove']"
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
          v-hasPermi="['system:appVersion:export']"
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
      :data="appVersionList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column
        align="center"
        fixed="left"
        type="selection"
        width="55"
      />
      <el-table-column align="center" label="" type="index"/>
      <el-table-column align="center" label="软件名称">
        <template slot-scope="scope">
          {{ scope.row.app.appName }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="版本名称">
        <template slot-scope="scope">
          {{ scope.row.versionName }}({{ scope.row.versionNo }})
        </template>
      </el-table-column>
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="更新日志"
        prop="updateLog"
      />
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="下载地址"
        prop="downloadUrl"
      />
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="软件MD5"
        prop="md5"
      />
      <el-table-column align="center" label="创建者" prop="createBy"/>
      <el-table-column
        align="center"
        label="创建时间"
        prop="createTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="备注" prop="remark"/>
      <el-table-column align="center" label="版本状态" prop="status">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_normal_disable"
            :value="scope.row.status"
          />
        </template>
      </el-table-column>
      <el-table-column align="center" label="强制更新" prop="forceUpdate">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_yes_no"
            :value="scope.row.forceUpdate"
          />
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        class-name="small-padding fixed-width"
        fixed="right"
        label="操作"
      >
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['system:appVersion:edit']"
            icon="el-icon-edit"
            size="mini"
            type="text"
            @click="handleUpdate(scope.row)"
          >修改
          </el-button>
          <el-button
            v-hasPermi="['system:appVersion:remove']"
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

    <!-- 添加或修改软件版本信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="800px">
      <el-form ref="form" :model="form" :rules="rules">
        <div v-if="app">
          <el-form-item>
            <el-col :span="12">
              <el-form-item label="所属软件">
                {{ this.app.appName }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="计费类型">
                <dict-tag
                  :options="dict.type.sys_bill_type"
                  :value="app.billType"
                />
              </el-form-item>
            </el-col>
          </el-form-item>
        </div>
        <el-form-item>
          <el-col :span="12">
            <el-form-item label="版本名称" prop="versionName">
              <el-input
                v-model="form.versionName"
                placeholder="请输入版本名称"
                style="width: 200px"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="版本号" prop="versionNo">
              <el-input-number
                v-model="form.versionNo"
                :min="1"
                controls-position="right"
              />
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item label="更新日志" prop="updateLog">
          <el-input
            v-model="form.updateLog"
            autosize
            placeholder="请输入内容"
            type="textarea"
          />
        </el-form-item>
        <el-form-item label="下载地址" prop="downloadUrl">
          <el-input v-model="form.downloadUrl" placeholder="请输入内容">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-col :span="12">
            <el-form-item label="版本状态">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in dict.type.sys_normal_disable"
                  :key="dict.value"
                  :label="dict.value"
                >{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="强制更新">
              <el-radio-group v-model="form.forceUpdate">
                <el-radio
                  v-for="dict in dict.type.sys_yes_no"
                  :key="dict.value"
                  :label="dict.value"
                >{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item label="软件MD5" prop="md5">
          <el-input v-model="form.md5" placeholder="请输入软件MD5"/>
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
  addAppVersion,
  delAppVersion,
  exportAppVersion,
  getAppVersion,
  listAppVersion,
  updateAppVersion,
} from "@/api/system/appVersion";
import {getApp} from "@/api/system/app";

export default {
  name: "AppVersion",
  dicts: ["sys_normal_disable", "sys_bill_type", "sys_yes_no"],
  data() {
    return {
      // 软件
      app: null,
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
      // 软件版本信息表格数据
      appVersionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 备注时间范围
      daterangeCreateTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        appId: null,
        versionName: null,
        versionNo: null,
        status: null,
        md5: null,
        createTime: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        versionName: [
          {required: true, message: "版本名称不能为空", trigger: "blur"},
        ],
        versionNo: [
          {required: true, message: "版本号不能为空", trigger: "blur"},
        ],
        downloadUrl: [
          {required: false, message: "下载地址不能为空", trigger: "blur"},
        ],
        status: [
          {required: true, message: "版本状态不能为空", trigger: "blur"},
        ],
        md5: [{required: false, message: "软件MD5不能为空", trigger: "blur"}],
      },
    };
  },
  created() {
    const appId = this.$route.params && this.$route.params.appId;
    // const appName = this.$route.query && this.$route.query.appName;
    if (appId != undefined && appId != null) {
      getApp(appId).then((response) => {
        this.app = response.data;
        const title = "版本管理";
        const appName = this.app.appName;
        const route = Object.assign({}, this.$route, {
          title: `${title}-${appName}`,
        });
        this.$store.dispatch("tagsView/updateVisitedView", route);
        this.getList();
      });
    } else {
      this.$modal.alertError("未获取到当前软件信息");
    }
  },
  methods: {
    /** 查询软件版本信息列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeCreateTime && "" != this.daterangeCreateTime) {
        this.queryParams.params["beginCreateTime"] =
          this.daterangeCreateTime[0];
        this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
      }
      this.queryParams.appId = this.app.appId;
      listAppVersion(this.queryParams).then((response) => {
        this.appVersionList = response.rows;
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
        appVersionId: undefined,
        appId: this.app.appId,
        versionName: undefined,
        versionNo: undefined,
        updateLog: undefined,
        downloadUrl: undefined,
        status: "0",
        delFlag: undefined,
        md5: undefined,
        remark: undefined,
        forceUpdate: "N",
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
      this.daterangeCreateTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.appVersionId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加软件版本信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const appVersionId = row.appVersionId || this.ids;
      getAppVersion(appVersionId).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改软件版本信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.appVersionId != null) {
            updateAppVersion(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAppVersion(this.form).then((response) => {
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
      const appVersionIds = row.appVersionId || this.ids;
      this.$modal
        .confirm(
          '是否确认删除软件版本信息编号为"' + appVersionIds + '"的数据项？'
        )
        .then(function () {
          return delAppVersion(appVersionIds);
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
      const queryParams = this.queryParams;
      this.$modal
        .confirm("是否确认导出所有软件版本信息数据项？")
        .then(() => {
          this.exportLoading = true;
          return exportAppVersion(queryParams);
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
