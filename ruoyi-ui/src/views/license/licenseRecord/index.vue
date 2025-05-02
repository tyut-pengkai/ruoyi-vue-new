<template>
  <div class="app-container">
    <el-form
      v-show="showSearch"
      ref="queryForm"
      :inline="true"
      :model="queryParams"
      size="small"
    >
      <el-form-item label="网站URL" prop="webUrl">
        <el-input
          v-model="queryParams.webUrl"
          clearable
          placeholder="请输入网站URL"
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
      <el-form-item label="姓名" prop="name">
        <el-input
          v-model="queryParams.name"
          clearable
          placeholder="请输入姓名"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="联系方式" prop="contact">
        <el-input
          v-model="queryParams.contact"
          clearable
          placeholder="请输入联系方式"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="授权状态" prop="status">
        <el-select
          v-model="queryParams.status"
          clearable
          placeholder="请选择授权状态"
          size="small"
        >
          <el-option
            v-for="dict in dict.type.sys_license_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="授权起始时间">
        <el-date-picker
          v-model="daterangeStartTime"
          end-placeholder="结束日期"
          range-separator="-"
          start-placeholder="开始日期"
          style="width: 240px"
          type="daterange"
          value-format="yyyy-MM-dd"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="授权终止时间">
        <el-date-picker
          v-model="daterangeEndTime"
          end-placeholder="结束日期"
          range-separator="-"
          start-placeholder="开始日期"
          style="width: 240px"
          type="daterange"
          value-format="yyyy-MM-dd"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="使用的单码" prop="loginCode">
        <el-input
          v-model="queryParams.loginCode"
          clearable
          placeholder="请输入使用的单码"
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
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['license:licenseRecord:add']"
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
          v-hasPermi="['license:licenseRecord:edit']"
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
          v-hasPermi="['license:licenseRecord:remove']"
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
          v-hasPermi="['license:licenseRecord:export']"
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
      :data="licenseRecordList"
      @selection-change="handleSelectionChange"
      :key="isUpdate"
    >
      <el-table-column align="center" type="selection" width="55"/>
      <el-table-column align="center" label="编号" prop="id" width="80"/>
      <el-table-column align="center" label="网站URL" prop="webUrl"/>
      <el-table-column align="center" label="设备码" prop="deviceCode">
        <template slot-scope="scope">
          {{scope.row.deviceCode}} / {{ scope.row.deviceCode === scope.row.extraInfo.serverInfo.sn ? '一致' : (scope.row.extraInfo.serverInfo.sn || '获取失败') }}
        </template>
      </el-table-column>
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="当前版本"
        prop="version"
      >
        <template slot-scope="scope">
          {{scope.row.data.version || '获取失败' }} / {{ (scope.row.data.version || '获取失败') === scope.row.data.dbVersion ? '一致' : (scope.row.data.dbVersion || '获取失败') }}
        </template>
        </el-table-column>
      <el-table-column align="center" label="姓名" prop="name"/>
      <el-table-column align="center" label="联系方式" prop="contact"/>
      <el-table-column align="center" label="授权状态" prop="status">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_license_status"
            :value="scope.row.status"
          />
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="授权起始时间"
        prop="startTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="授权终止时间"
        prop="endTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="使用的单码" prop="loginCode"/>
      <el-table-column align="center" label="备注" prop="remark"/>
      <el-table-column
        align="center"
        class-name="small-padding fixed-width"
        label="操作"
        width="200"
      >
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['license:licenseRecord:query']"
            icon="el-icon-tickets"
            size="mini"
            type="text"
            @click="handleView(scope.row)"
          >详情
          </el-button>
          <el-button
            v-hasPermi="['license:licenseRecord:edit']"
            icon="el-icon-edit"
            size="mini"
            type="text"
            @click="handleUpdate(scope.row)"
          >修改
          </el-button>
          <el-button
            v-hasPermi="['license:licenseRecord:remove']"
            icon="el-icon-delete"
            size="mini"
            type="text"
            @click="handleRemove(scope.row)"
          >解除
          </el-button>
          <el-button
            v-hasPermi="['license:licenseRecord:remove']"
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

    <!-- 添加或修改验证授权用户对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="500px">
      <el-form ref="form" :model="form" :rules="rules">
        <el-form-item label="网站URL" prop="webUrl">
          <el-input v-model="form.webUrl" placeholder="请输入网站URL"/>
        </el-form-item>
        <el-form-item label="设备码" prop="deviceCode">
          <el-input v-model="form.deviceCode" placeholder="请输入设备码"/>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名"/>
        </el-form-item>
        <el-form-item label="联系方式" prop="contact">
          <el-input v-model="form.contact" placeholder="请输入联系方式"/>
        </el-form-item>
        <el-form-item label="授权状态" prop="status">
          <el-select
            v-model="form.status"
            placeholder="请选择授权状态"
            size="small"
          >
            <el-option
              v-for="dict in dict.type.sys_license_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="授权起始时间" prop="startTime">
          <el-date-picker
            v-model="form.startTime"
            clearable
            placeholder="请选择授权起始时间"
            type="date"
            value-format="yyyy-MM-dd HH:mm:ss"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="授权终止时间" prop="endTime">
          <el-date-picker
            v-model="form.endTime"
            clearable
            placeholder="请选择授权终止时间"
            type="date"
            value-format="yyyy-MM-dd HH:mm:ss"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="使用的单码" prop="loginCode">
          <el-input v-model="form.loginCode" placeholder="请输入使用的单码"/>
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

    <el-dialog title="站点详情" :visible.sync="openView" append-to-body width="800px">
      <el-card shadow="never">
        <div>
          <pre>{{ JSON.stringify(detailData, null, 2) }}</pre>
        </div>
      </el-card>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelView">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {
  addLicenseRecord,
  delLicenseRecord,
  getLicenseRecord,
  getSystemInfo,
  listLicenseRecord,
  removeLicenseRecord,
  updateLicenseRecord,
} from "@/api/license/licenseRecord";

export default {
  name: "LicenseRecord",
  dicts: ["sys_license_status"],
  data() {
    return {
      isUpdate: false,
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
      // 验证授权用户表格数据
      licenseRecordList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 备注时间范围
      daterangeStartTime: [],
      // 备注时间范围
      daterangeEndTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: this.$store.state.settings.pageSize,
        webUrl: null,
        deviceCode: null,
        name: null,
        contact: null,
        startTime: null,
        endTime: null,
        loginCode: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
      openView: false,
      detailData: {},
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询验证授权用户列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeStartTime && "" != this.daterangeStartTime) {
        this.queryParams.params["beginStartTime"] = this.daterangeStartTime[0];
        this.queryParams.params["endStartTime"] = this.daterangeStartTime[1];
      }
      if (null != this.daterangeEndTime && "" != this.daterangeEndTime) {
        this.queryParams.params["beginEndTime"] = this.daterangeEndTime[0];
        this.queryParams.params["endEndTime"] = this.daterangeEndTime[1];
      }
      listLicenseRecord(this.queryParams).then((response) => {
        this.total = response.total;
        for (let i = 0; i < response.rows.length; i++) {
          getSystemInfo({webUrl: response.rows[i]["webUrl"]}).then((res) => {
            let initExtraInfo = {
              serverInfo: {},
              simpleInfo: {},
              licenseInfo: {},
            }
            if (res.data) {
              response.rows[i]["data"] = res.data;
              response.rows[i]["extraInfo"] = res.extraInfo || initExtraInfo;
            } else {
              response.rows[i]["data"] = res.msg;
              response.rows[i]["extraInfo"] = initExtraInfo;
            }
            this.isUpdate = !this.isUpdate;
          });
        }
        this.licenseRecordList = response.rows;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    cancelView() {
      this.openView = false;
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        webUrl: null,
        deviceCode: null,
        name: null,
        contact: null,
        startTime: null,
        endTime: null,
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
      this.daterangeStartTime = [];
      this.daterangeEndTime = [];
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
      this.title = "添加验证授权用户";
    },
    /** 查看详情 */
    handleView(row) {
      this.openView = true;
      this.detailData = Object.assign({}, row.data, row.extraInfo);
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getLicenseRecord(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改验证授权用户";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateLicenseRecord(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addLicenseRecord(this.form).then((response) => {
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
        .confirm('是否确认删除验证授权用户编号为"' + ids + '"的数据项？')
        .then(function () {
          return delLicenseRecord(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {
        });
    },
    /** 解除授权按钮操作 */
    handleRemove(row) {
      const ids = row.id || this.ids;
      this.$modal
        .confirm('是否确认解除验证授权用户编号为"' + ids + '"的授权？')
        .then(function () {
          return removeLicenseRecord(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("解除授权成功");
        })
        .catch(() => {
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "license/licenseRecord/export",
        {
          ...this.queryParams,
        },
        `licenseRecord_${new Date().getTime()}.xlsx`
      );
    },
  },
};
</script>
