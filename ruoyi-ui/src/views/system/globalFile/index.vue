<template>
  <div class="app-container">
    <el-form
      v-show="showSearch"
      ref="queryForm"
      :inline="true"
      :model="queryParams"
      size="small"
    >
      <el-form-item label="文件名" prop="name">
        <el-input
          v-model="queryParams.name"
          clearable
          placeholder="请输入文件名"
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
          v-hasPermi="['system:globalFile:add']"
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
          v-hasPermi="['system:globalFile:edit']"
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
          v-hasPermi="['system:globalFile:remove']"
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
          v-hasPermi="['system:globalFile:export']"
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
      :data="globalFileList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column align="center" type="selection" width="55"/>
      <el-table-column align="center" label="文件ID" prop="id"/>
      <el-table-column align="center" label="文件名" prop="name"/>
      <!-- <el-table-column label="文件路径" align="center" prop="value" /> -->
      <el-table-column align="center" label="文件描述" prop="description"/>
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
      <el-table-column align="center" label="备注" prop="remark"/>
      <el-table-column
        align="center"
        class-name="small-padding fixed-width"
        label="操作"
      >
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['system:globalFile:edit']"
            icon="el-icon-edit"
            size="mini"
            type="text"
            @click="handleUpload(scope.row)"
          >上传
          </el-button
          >
          <el-button
            icon="el-icon-edit"
            size="mini"
            type="text"
            @click="handleDownload(scope.row)"
          >下载
          </el-button
          >
          <el-button
            v-hasPermi="['system:globalFile:edit']"
            icon="el-icon-edit"
            size="mini"
            type="text"
            @click="handleUpdate(scope.row)"
          >修改
          </el-button
          >
          <el-button
            v-hasPermi="['system:globalFile:remove']"
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

    <!-- 添加或修改远程文件对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="500px">
      <el-form ref="form" :model="form" :rules="rules">
        <el-form-item label="文件名" prop="name">
          <el-input v-model="form.name" placeholder="请输入文件名"/>
        </el-form-item>
        <!-- <el-form-item label="文件路径" prop="value">
          <el-input v-model="form.value" type="textarea" placeholder="请输入内容" />
        </el-form-item> -->
        <el-form-item label="文件描述" prop="description">
          <el-input
            v-model="form.description"
            placeholder="请输入内容"
            type="textarea"
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

    <!--快速接入对话框 -->
    <el-dialog
      :title="upload.title"
      :visible.sync="upload.open"
      append-to-body
      width="400px"
    >
      <el-upload
        ref="upload"
        :action="upload.url + '?globalFileId=' + upload.globalFileId"
        :auto-upload="false"
        :before-upload="onBeforeUpload"
        :disabled="upload.isUploading"
        :headers="upload.headers"
        :limit="1"
        :on-change="handleFileOnChange"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        accept=".apk,.exe"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">拖入或<em>点击上传</em>文件</div>
      </el-upload>
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
  addGlobalFile,
  delGlobalFile,
  downloadGlobalFile,
  getGlobalFile,
  listGlobalFile,
  updateGlobalFile,
} from "@/api/system/globalFile";
import {getToken} from "@/utils/auth";
import axios from "axios";

export default {
  name: "GlobalFile",
  dicts: ["sys_yes_no"],
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
      // 远程文件表格数据
      globalFileList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
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
        name: [{required: true, message: "文件名不能为空", trigger: "blur"}],
        value: [
          {required: true, message: "文件路径不能为空", trigger: "blur"},
        ],
        checkToken: [
          {
            required: true,
            message: "是否需要登录不能为空",
            trigger: "change",
          },
        ],
        checkVip: [
          {required: true, message: "是否需要VIP不能为空", trigger: "change"},
        ],
      },
      // 文件上传参数
      upload: {
        // 是否显示弹出层（快速接入）
        open: false,
        // 弹出层标题（快速接入）
        title: "",
        // 当前操作的文件ID
        globalFileId: null,
        // 是否禁用上传
        isUploading: false,
        // 设置上传的请求头部
        headers: {Authorization: "Bearer " + getToken()},
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/system/globalFile/upload",
      },
      fileDown: {
        //弹出框控制的状态
        loadDialogStatus: false,
        //进度条的百分比
        percentage: 0,
        //取消下载时的资源对象
        source: {},
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询远程文件列表 */
    getList() {
      this.loading = true;
      listGlobalFile(this.queryParams).then((response) => {
        this.globalFileList = response.rows;
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
      this.title = "添加远程文件";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getGlobalFile(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改远程文件";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.$refs.upload.submit();
          if (this.form.id != null) {
            updateGlobalFile(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addGlobalFile(this.form).then((response) => {
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
        .confirm('是否确认删除远程文件编号为"' + ids + '"的数据项？')
        .then(function () {
          return delGlobalFile(ids);
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
        "system/globalFile/export",
        {
          ...this.queryParams,
        },
        `globalFile_${new Date().getTime()}.xlsx`
      );
    },
    /** 导入按钮操作 */
    handleUpload(row) {
      this.upload.title = "上传文件";
      this.upload.open = true;
      this.upload.globalFileId = row.id;
    },
    /** 下载按钮操作 */
    handleDownload(row) {
      downloadGlobalFile(row.id).then((response) => {
        this.downFile(response.msg);
      });
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.$modal.msgSuccess(response.msg);
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
          process.env.VUE_APP_BASE_API + "/common/globalFileDownload/" + name,
        params: param,
        responseType: "blob",
        headers: {Authorization: "Bearer " + getToken()},
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
  },
};
</script>
