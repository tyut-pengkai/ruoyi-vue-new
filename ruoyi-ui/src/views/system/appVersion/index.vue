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

    <el-alert
      :closable="false"
      show-icon
      style="margin-bottom: 5px"
      type="info"
    >
      <template slot="title">
        <span>
          温馨提示：如果需要更新版本时请新建一个版本，并设置一个更大的版本号即可，而不要修改或删除已有的版本，如果版本被删除，那么对应版本的软件将无法使用。
        </span>
      </template>
    </el-alert>

    <el-table
      v-loading="loading"
      :data="appVersionList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column align="center" type="selection" width="55"/>
      <!-- <el-table-column align="center" label="" type="index"/> -->
      <el-table-column align="center" label="编号" prop="appVersionId"/>
      <el-table-column
        align="center"
        :show-overflow-tooltip="true"
        label="软件名称"
      >
        <template slot-scope="scope">
          {{ scope.row.app.appName }}
        </template>
      </el-table-column>
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="版本名称"
      >
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
      <el-table-column align="center" label="创建者" prop="createBy" />
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
      <el-table-column align="center" label="备注" prop="remark" />
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
            icon="el-icon-upload2"
            size="mini"
            type="text"
            @click="handleImport(scope.row)"
            >快速接入
          </el-button>
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
              <span>
                <el-tooltip
                  content="版本名称可随意填写，如测试版v1.0、正式版2.0、国庆特别版等，用于软件显示"
                  placement="top"
                >
                  <i
                    class="el-icon-question"
                    style="margin-left: -12px; margin-right: 10px"
                  ></i>
                </el-tooltip>
              </span>
              <el-input
                v-model="form.versionName"
                placeholder="请输入版本名称"
                style="width: 200px"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="版本号" prop="versionNo">
              <span>
                <el-tooltip
                  content="版本号用于升级，只限填写整数，同一软件版本号不可重复，版本越新，数字应该越大，建议使用发布时间，如20220101"
                  placement="top"
                >
                  <i
                    class="el-icon-question"
                    style="margin-left: -12px; margin-right: 10px"
                  ></i>
                </el-tooltip>
              </span>
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
        <el-form-item label="下载地址（直链）" prop="downloadUrlDirect">
          <el-input v-model="form.downloadUrlDirect" placeholder="请输入内容">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-col :span="12">
            <el-form-item label="版本状态" prop="status">
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
            <el-form-item label="强制更新" prop="forceUpdate">
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
        <el-form-item>
          <el-col :span="12">
            <el-form-item label="校验MD5" prop="checkMd5">
              <el-radio-group v-model="form.checkMd5">
                <el-radio
                  v-for="dict in dict.type.sys_yes_no"
                  :key="dict.value"
                  :label="dict.value"
                  >{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12"></el-col>
        </el-form-item>
        <el-form-item label="软件MD5" prop="md5">
          <el-input v-model="form.md5" placeholder="请输入软件MD5" />
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

    <!--快速接入对话框 -->
    <el-dialog
      :title="upload.title"
      :visible.sync="upload.open"
      append-to-body
      width="450px"
    >
      <el-alert
        :closable="false"
        show-icon
        style="margin-bottom: 10px"
        type="info"
      >
        <template slot="title">
          <span>
            快速接入安全性较低，建议接入后自行加固或加壳后再发布。
            <el-tooltip placement="top">
              <div slot="content">
                本功能主要用于开发者临时接单需要快速接入快速测试的场景，接入软件建议小于20M<br/>目前可支持
                [全部模式] 的exe接入与 [单码计时] 模式的apk接入
              </div>
              <el-link type="primary">更多</el-link>
            </el-tooltip>
          </span>
        </template>
      </el-alert>
      <el-tabs style="width: 400px" type="border-card">
        <el-tab-pane label="APK接入">
          <el-upload
            ref="upload"
            :action="
              upload.url +
              '?versionId=' +
              upload.quickAccessVersionId +
              '&updateMd5=' +
              upload.updateMd5 +
              '&apkOper=' +
              upload.apkOper
            "
            :auto-upload="false"
            :before-upload="onBeforeUpload"
            :disabled="upload.isUploading"
            :headers="upload.headers"
            :limit="1"
            :on-change="handleFileOnChange"
            :on-progress="handleFileUploadProgress"
            :on-success="handleFileSuccess"
            accept=".apk"
            drag
          >
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">
              仅允许拖入或<em>点击上传</em>apk格式文件
            </div>
            <div slot="tip" class="el-upload__tip text-center">
              <!-- <span style="margin-top: 5px">允许导入exe或apk格式文件。</span> -->
              <!-- <div v-show="showExe" slot="tip" class="el-upload__tip">
                <el-checkbox v-model="upload.updateMd5" />
                <span>是否更新MD5</span>
                <span style="margin-left: 15px">
                  <el-tooltip
                    content="仅exe生效，会在已有MD5信息基础上累加"
                    placement="top"
                  >
                    <i
                      class="el-icon-question"
                      style="margin-left: -12px; margin-right: 10px"
                    ></i>
                  </el-tooltip>
                </span>
              </div>
              <div v-show="showApk" slot="tip" class="el-upload__tip"> -->
              <span style="margin-right: 10px">接入类别</span>
              <el-select v-model="upload.apkOper" placeholder="请选择接入类别">
                <el-option
                  v-for="item in apkOperOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
              <span style="margin-left: 15px">
                <el-tooltip
                  content="建议将生成的apk加固后再发布"
                  placement="top"
                >
                  <i
                    class="el-icon-question"
                    style="margin-left: -12px; margin-right: 10px"
                  ></i>
                </el-tooltip>
              </span>
              <!-- <el-checkbox v-model="upload.signApk" />
            是否签名APK<br />(仅apk生效，建议将生成的apk加固后再发布) -->
              <!-- </div> -->
              <!-- <el-link
            type="primary"
            :underline="false"
            style="font-size: 12px; vertical-align: baseline"
            @click="importTemplate"
            >下载模板</el-link
          > -->
              <div style="margin-top: 10px">
                <span style="margin-right: 10px">选择模板</span>
                <el-select
                  v-model="upload.template"
                  placeholder="请选择接入模板"
                  style="width: 86px; margin-right: 10px"
                >
                  <el-option
                    v-for="item in templateOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  >
                  </el-option>
                </el-select>
                <span style="margin-right: 10px">皮肤</span>
                <el-select
                  v-model="upload.skin"
                  placeholder="请选择接入皮肤"
                  style="width: 86px"
                >
                  <el-option
                    v-for="item in skinOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  >
                  </el-option>
                </el-select>
                <span style="margin-left: 15px">
                  <el-tooltip
                    content="每个模板里可提供多个皮肤效果"
                    placement="top"
                  >
                    <i
                      class="el-icon-question"
                      style="margin-left: -12px; margin-right: 10px"
                    ></i>
                  </el-tooltip>
                </span>
              </div>
            </div>
          </el-upload>
        </el-tab-pane>
        <el-tab-pane label="EXE接入">
          <div style="width: 360px; height: 272px">
            <el-alert
              :closable="false"
              show-icon
              style="margin-bottom: 10px"
              type="info"
            >
              <template slot="title">
                <span> 请在官方群共享下载红叶快速接入工具 </span>
              </template>
            </el-alert>
            <div align="center" style="margin-top: 10px">
              <el-input
                v-show="apvStr && apvStr != ''"
                v-model="apvStr"
                :readonly="true"
                :rows="8"
                type="textarea"
              ></el-input>
              <el-button
                id="copyButton"
                style="margin-top: 10px"
                type="primary"
                @click="getQuickAccessParams(upload.quickAccessVersionId)"
              >
                一键复制对接参数
              </el-button>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>

    <!--下载进度条-->
    <el-dialog
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :show-close="false"
      :visible.sync="fileDown.loadDialogStatus"
      title="正在下载，请等待"
      width="20%"
    >
      <div style="text-align: center">
        <el-progress
          :percentage="fileDown.percentage"
          type="circle"
        ></el-progress>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="downloadClose">取消下载</el-button>
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
  getQuickAccessParams,
  listAppVersion,
  updateAppVersion,
} from "@/api/system/appVersion";
import {getToken} from "@/utils/auth";
import {getApp} from "@/api/system/app";
import axios from "axios";
import Clipboard from "clipboard";

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
        pageSize: this.$store.state.settings.pageSize,
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
          { required: true, message: "版本名称不能为空", trigger: "blur" },
        ],
        versionNo: [
          { required: true, message: "版本号不能为空", trigger: "blur" },
        ],
        downloadUrl: [
          { required: false, message: "下载地址不能为空", trigger: "blur" },
        ],
        status: [
          { required: true, message: "版本状态不能为空", trigger: "blur" },
        ],
        forceUpdate: [
          { required: true, message: "是否强制更新不能为空", trigger: "blur" },
        ],
        md5: [{ required: false, message: "软件MD5不能为空", trigger: "blur" }],
        checkMd5: [
          { required: true, message: "是否检查MD5不能为空", trigger: "blur" },
        ],
      },
      // 快速接入参数
      upload: {
        // 是否显示弹出层（快速接入）
        open: false,
        // 弹出层标题（快速接入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 设置上传的请求头部
        headers: {Authorization: "Bearer " + getToken()},
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/system/appVersion/quickAccess",
        // 当前操作的版本ID
        quickAccessVersionId: null,
        // 是否自动更新MD5
        updateMd5: true,
        // 是否自动APK签名
        apkOper: "1",
        // 默认模板
        template: "0",
        // 默认皮肤
        skin: "0",
      },
      fileDown: {
        //弹出框控制的状态
        loadDialogStatus: false,
        //进度条的百分比
        percentage: 0,
        //取消下载时的资源对象
        source: {},
      },
      apkOperOptions: [
        {
          value: "1",
          label: "注入并加签",
        },
        {
          value: "2",
          label: "仅注入",
        },
        {
          value: "3",
          label: "仅加签",
        },
      ],
      templateOptions: [
        {
          value: "0",
          label: "默认",
        },
      ],
      skinOptions: [
        {
          value: "0",
          label: "默认",
        },
      ],
      showExe: false,
      showApk: false,
      apvStr: "",
    };
  },
  created() {
    this.init();
  },
  methods: {
    init() {
      // const appId = this.$route.params && this.$route.params.appId;
      const appId = this.$route.query && this.$route.query.appId;
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
        checkMd5: "N",
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
      this.$modal
        .confirm(
          "是否确认修改数据项？温馨提示：如果需要更新版本，请新建一个版本，并设置一个更大的版本号即可，而不要修改已有的版本。"
        )
        .then(() => {
          this.reset();
          const appVersionId = row.appVersionId || this.ids;
          getAppVersion(appVersionId).then((response) => {
            this.form = response.data;
            this.open = true;
            this.title = "修改软件版本信息";
          });
        })
        .catch(() => {
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
          "是否确认删除数据项？温馨提示：如果需要版本，请新建一个版本，而不要修改或删除已有的版本，如果版本被删除，那么对应版本的软件将无法使用。"
        )
        .then(function () {
          return delAppVersion(appVersionIds);
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
        .confirm("是否确认导出所有软件版本信息数据项？")
        .then(() => {
          this.exportLoading = true;
          return exportAppVersion(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
          this.exportLoading = false;
        })
        .catch(() => {});
    },
    /** 导入按钮操作 */
    handleImport(row) {
      this.upload.title = "快速接入";
      this.upload.open = true;
      this.upload.quickAccessVersionId = row.appVersionId;
      this.apvStr = "";
      getQuickAccessParams(row.appVersionId).then((response) => {
        this.apvStr = response.apvStr;
      });
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      // this.$download.name(response.msg);
      this.downFile(response.msg);
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
    // 校验文件
    onBeforeUpload(file) {
      // const isLt1M = file.size / 1024 / 1024 < 20;
      // if (!isLt1M) {
      //   this.$message.error("上传文件大小不能超过 20MB!");
      // }
      // return isLt1M;
    },
    // 添加文件
    handleFileOnChange(file, fileList) {
      if (file && file.name) {
        String.prototype.endWith = function (endStr) {
          var d = this.length - endStr.length;
          return d >= 0 && this.lastIndexOf(endStr) == d;
        };
        if (file.name.endWith(".exe")) {
          this.showExe = true;
          this.showApk = false;
        } else if (file.name.endWith(".apk")) {
          this.showApk = true;
          this.showExe = false;
        }
      } else {
        this.showApk = false;
        this.showExe = false;
      }
    },
    downFile(name) {
      //这里放参数
      var param = {};
      this.fileDown.loadDialogStatus = true;
      this.fileDown.percentage = 0;
      const instance = this.initInstance();
      instance({
        method: "get",
        withCredentials: true,
        url:
          process.env.VUE_APP_BASE_API +
          "/common/download?fileName=" +
          encodeURI(name) +
          "&delete=false",
        params: param,
        responseType: "blob",
        headers: { Authorization: "Bearer " + getToken() },
      })
        .then((res) => {
          this.fileDown.loadDialogStatus = false;
          console.info(res);
          const content = res.data;
          if (content.size == 0) {
            this.loadClose();
            this.$modal.alert("下载失败");
            return;
          }
          const blob = new Blob([content]);
          const fileName = decodeURI(res.headers["download-filename"]); //替换文件名
          if ("download" in document.createElement("a")) {
            // 非IE下载
            const elink = document.createElement("a");
            elink.download = fileName;
            elink.style.display = "none";
            elink.href = URL.createObjectURL(blob);
            document.body.appendChild(elink);
            elink.click();
            setTimeout(function () {
              URL.revokeObjectURL(elink.href); // 释放URL 对象
              document.body.removeChild(elink);
            }, 100);
          } else {
            // IE10+下载
            navigator.msSaveBlob(blob, fileName);
          }
        })
        .catch((error) => {
          this.fileDown.loadDialogStatus = false;
          console.info(error);
        });
    },
    initInstance() {
      var _this = this;
      //取消时的资源标记
      this.fileDown.source = axios.CancelToken.source();
      const instance = axios.create({
        //axios 这个对象要提前导入 或者替换为你们全局定义的
        onDownloadProgress: function (ProgressEvent) {
          const load = ProgressEvent.loaded;
          const total = ProgressEvent.total;
          const progress = (load / total) * 100;
          // console.log("progress=" + progress);
          //一开始已经在计算了 这里要超过先前的计算才能继续往下
          if (progress > _this.fileDown.percentage) {
            _this.fileDown.percentage = Math.floor(progress);
          }
          if (progress == 100) {
            //下载完成
            _this.fileDown.open = false;
          }
        },
        cancelToken: this.fileDown.source.token, //声明一个取消请求token
      });
      return instance;
    },
    downloadClose() {
      //中断下载
      this.$modal
        .confirm("点击关闭后将中断下载，是否确定关闭？")
        .then(() => {
          //中断下载回调
          this.fileDown.source.cancel("log==客户手动取消下载");
        })
        .catch(() => {
          //取消--什么都不做
        });
    },
    getQuickAccessParams(appVersionId) {
      var clipboard = new Clipboard("#copyButton", {
        text: () => {
          // 如果想从其它DOM元素内容复制。应该是target:function(){return: };
          return this.apvStr;
        },
      });
      clipboard.on("success", (e) => {
        this.$modal.msgSuccess("已成功复制对接参数");
        clipboard.destroy();
      });
      clipboard.on("error", (e) => {
        this.$modal.msgError(
          "复制失败，您的浏览器不支持复制，请自行复制下方的对接参数"
        );
        clipboard.destroy();
      });
    },
  },
  watch: {
    //动态监听路由变化 -以便动态更改导航背景色事件效果等
    $route(to, from) {
      // 对路由变化作出响应...
      // console.log("to.path----", to); //跳转后路由
      // console.log("from----", from); //跳转前路由

      if (
        from.path == "/verify/app" &&
        to.path == "/verify/appVersion" &&
        to.query.appId != this.app.appId
      ) {
        this.init();
      }
    },
  },
};
</script>
