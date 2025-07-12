<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="设备编码" prop="deviceCode">
        <el-input
          v-model="queryParams.deviceCode"
          placeholder="请输入设备编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备名称" prop="deviceName">
        <el-input
          v-model="queryParams.deviceName"
          placeholder="请输入设备名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="mac地址" prop="macAddr">
        <el-input
          v-model="queryParams.macAddr"
          placeholder="请输入mac地址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="IP地址" prop="ipAddr">
        <el-input
          v-model="queryParams.ipAddr"
          placeholder="请输入IP地址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所属区域" prop="area">
        <el-input
          v-model="queryParams.area"
          placeholder="请输入所属区域"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="固件版本" prop="otaVersion">
        <el-input
          v-model="queryParams.otaVersion"
          placeholder="请输入固件版本"
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
          v-hasPermi="['device:info:add']"
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
          v-hasPermi="['device:info:edit']"
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
          v-hasPermi="['device:info:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['device:info:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-if="!isAdmin"
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="openBindDeviceDialog"
        >添加绑定设备</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="info" plain icon="el-icon-upload2" size="mini" @click="handleImport" v-hasPermi="['device:info:import']">导入</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="infoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column v-if="isAdmin" label="设备id" align="center" prop="deviceId" />
      <el-table-column label="设备编码" align="center" prop="deviceCode" />
      <el-table-column label="设备名称" align="center" prop="deviceName" />
      <el-table-column label="mac地址" align="center" prop="macAddr" />
      <el-table-column label="当前音色" align="center" prop="agentName" />
      <el-table-column label="IP地址" align="center" prop="ipAddr" />
      <el-table-column label="所属区域" align="center" prop="area" />
      <el-table-column label="在线状态" align="center" prop="onlineStatus" />
      <el-table-column label="固件版本" align="center" prop="otaVersion" />
      <el-table-column label="状态" align="center" prop="status" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['device:info:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['device:info:remove']"
          >删除</el-button>
          <el-button
            v-if="!isAdmin"
            size="mini"
            type="text"
            icon="el-icon-unlock"
            @click="handleUnbind(scope.row)"
          >解绑</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-microphone"
            @click="handleChooseAgent(scope.row)"
            v-hasPermi="['device:info:chooseAgent']"
          >选择音色</el-button>
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

    <!-- 添加或修改设备信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="设备编码" prop="deviceCode">
          <el-input v-model="form.deviceCode" placeholder="请输入设备编码" />
        </el-form-item>
        <el-form-item label="设备名称" prop="deviceName">
          <el-input v-model="form.deviceName" placeholder="请输入设备名称" />
        </el-form-item>
        <el-form-item label="mac地址" prop="macAddr">
          <el-input v-model="form.macAddr" placeholder="请输入mac地址" />
        </el-form-item>
        <el-form-item label="IP地址" prop="ipAddr">
          <el-input v-model="form.ipAddr" placeholder="请输入IP地址" />
        </el-form-item>
        <el-form-item label="所属区域" prop="area">
          <el-input v-model="form.area" placeholder="请输入所属区域" />
        </el-form-item>
        <el-form-item label="固件版本" prop="otaVersion">
          <el-input v-model="form.otaVersion" placeholder="请输入固件版本" />
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

    <el-dialog title="绑定设备" :visible.sync="bindDeviceDialogVisible" width="400px">
      <el-form :model="bindDeviceForm" label-width="120px">
        <el-form-item label="设备编码或mac地址">
          <el-input v-model="bindDeviceForm.deviceMacCode" placeholder="请输入设备编码或mac地址"></el-input>
          <div style="color: #f56c6c; font-size: 12px; margin-top: 0px;">设备编码请查看设备背面的一串数字.</div>

        </el-form-item>
        
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="bindDeviceDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleBindDevice">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <div class="el-upload__tip" slot="tip">
            <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的设备数据
          </div>
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size: 12px; vertical-align: baseline" @click="importTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 选择音色对话框 -->
    <el-dialog :title="chooseAgent.title" :visible.sync="chooseAgent.open" width="600px" append-to-body>
      <el-table ref="agentTable" :data="agentList" @current-change="handleCurrentAgentChange" highlight-current-row :show-header="false">
        <el-table-column width="55">
          <template slot-scope="scope">
              <el-radio :label="scope.row.agentId" v-model="chooseAgent.selectedAgentId">&nbsp;</el-radio>
          </template>
        </el-table-column>
        <el-table-column>
          <template slot-scope="scope">
            <span>{{ scope.row.agentName.replace(/^\d+\s*/, '') }}</span>
          </template>
        </el-table-column>
      </el-table>
      <div v-if="chooseAgent.currentAgentDescription" class="description-box">
          <h4 v-if="chooseAgent.currentAgentName">{{ chooseAgent.currentAgentName }}介绍</h4>
          <p>{{ chooseAgent.currentAgentDescription }}</p>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAgentChoice">确 定</el-button>
        <el-button @click="cancelAgentChoice">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listInfo, getInfo, delInfo, addInfo, updateInfo, bindDeviceToUser, unbindDeviceToUser, bindAgent } from "@/api/device/info"
import { listInfo as listAgent } from "@/api/agent/info"
import store from '@/store'
import { getToken } from '@/utils/auth'

export default {
  name: "Info",
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
      // 设备信息表格数据
      infoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        deviceCode: null,
        deviceName: null,
        macAddr: null,
        ipAddr: null,
        area: null,
        onlineStatus: null,
        otaVersion: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        deviceCode: [
          { required: true, message: "设备编码不能为空", trigger: "blur" }
        ],
        deviceName: [
          { required: true, message: "设备名称不能为空", trigger: "blur" }
        ],
        macAddr: [
          { required: true, message: "mac地址不能为空", trigger: "blur" }
        ]
      },
      bindDeviceDialogVisible: false,
      bindDeviceForm: {
        deviceMacCode: ''
      },
      isAdmin: false,
      upload: {
        open: false,
        title: "设备导入",
        isUploading: false,
        url: process.env.VUE_APP_BASE_API + "/device/info/importData",
        headers: { Authorization: "Bearer " + getToken() },
        updateSupport: false
      },
      // 音色选择对话框
      chooseAgent: {
        open: false,
        title: "选择音色",
        currentDeviceId: null,
        selectedAgentId: null,
        currentAgentName: null,
        currentAgentDescription: null,
      },
      // 智能体列表
      agentList: [],
    }
  },
  created() {
    this.getList()
    // 用store获取用户名判断是否admin
    this.isAdmin = store.getters.name === 'admin';
  },
  methods: {
    /** 查询设备信息列表 */
    getList() {
      this.loading = true
      listInfo(this.queryParams).then(response => {
        this.infoList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 取消选择音色
    cancelAgentChoice() {
      this.chooseAgent.open = false;
      this.chooseAgent.currentDeviceId = null;
      this.chooseAgent.selectedAgentId = null;
      this.chooseAgent.currentAgentDescription = null;
      this.chooseAgent.currentAgentName = null;
    },
    // 表单重置
    reset() {
      this.form = {
        deviceId: null,
        deviceCode: null,
        deviceName: null,
        macAddr: null,
        ipAddr: null,
        area: null,
        onlineStatus: null,
        otaVersion: null,
        status: null,
        delFlag: null,
        createTime: null,
        updateTime: null
      }
      this.resetForm("form")
      this.getList()
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.deviceId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加设备信息"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const deviceId = row.deviceId || this.ids
      getInfo(deviceId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改设备信息"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.deviceId != null) {
            updateInfo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addInfo(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const deviceIds = row.deviceId || this.ids
      this.$modal.confirm('是否确认删除设备信息编号为"' + deviceIds + '"的数据项？').then(function() {
        return delInfo(deviceIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('device/info/export', {
        ...this.queryParams
      }, `info_${new Date().getTime()}.xlsx`)
    },
    openBindDeviceDialog() {
      this.bindDeviceDialogVisible = true;
      this.bindDeviceForm.deviceMacCode = '';
    },
    handleBindDevice() {
      if (!this.bindDeviceForm.deviceMacCode) {
        this.$message.error("请填写设备编码或mac地址");
        return;
      }
      bindDeviceToUser({ deviceMacCode: this.bindDeviceForm.deviceMacCode }).then(res => {
        this.$message.success("绑定成功");
        this.getList();
        this.bindDeviceDialogVisible = false;
      });
    },
    handleUnbind(row) {
      this.$confirm('确定要解绑设备"' + row.deviceName + '"吗?', "警告", {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.unbindDeviceToUser(row.deviceId)
      })
    },
    unbindDeviceToUser(deviceId) {
      unbindDeviceToUser({ deviceId }).then(res => {
        this.$message.success('解绑成功');
        this.getList();
      }).catch(() => {});
    },
    handleImport() {
      this.upload.open = true;
      this.upload.title = "设备导入";
    },
    importTemplate() {
      // 拼接token参数
      const token = getToken();
      this.download('device/info/importTemplate', {}, `device_template_${new Date().getTime()}.xlsx`)
    },
    submitFileForm() {
      this.$refs.upload.submit();
    },
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$message.success(response.msg);
      this.getList();
    },
    // 处理音色单选
    handleCurrentAgentChange(currentRow) {
      if (currentRow) {
        this.chooseAgent.selectedAgentId = currentRow.agentId;
        this.chooseAgent.currentAgentName = currentRow.agentName.replace(/^\d+\s*/, '');
        this.chooseAgent.currentAgentDescription = currentRow.agentDesc;
      } else {
        this.chooseAgent.currentAgentName = null;
        this.chooseAgent.currentAgentDescription = null;
      }
    },
    /** 选择音色按钮操作 */
    handleChooseAgent(row) {
      this.chooseAgent.currentDeviceId = row.deviceId;
      this.chooseAgent.selectedAgentId = row.agentId;
      this.chooseAgent.currentAgentName = null;
      this.chooseAgent.currentAgentDescription = null;
      // 获取可选的智能体列表
      listAgent({ pageNum: 1, pageSize: 999 }).then(response => {
        this.agentList = response.rows;
        if(this.chooseAgent.selectedAgentId) {
            const selectedAgent = this.agentList.find(agent => agent.agentId === this.chooseAgent.selectedAgentId);
            if(selectedAgent) {
                this.chooseAgent.currentAgentName = selectedAgent.agentName.replace(/^\d+\s*/, '');
                this.chooseAgent.currentAgentDescription = selectedAgent.agentDesc;
            }
        }
        this.chooseAgent.open = true;
      });
    },
    /** 提交音色选择 */
    submitAgentChoice() {
      if (!this.chooseAgent.selectedAgentId) {
        this.$modal.msgError("请选择一个音色");
        return;
      }
      const data = {
        deviceId: this.chooseAgent.currentDeviceId,
        agentId: this.chooseAgent.selectedAgentId
      };
      bindAgent(data).then(response => {
        this.$modal.msgSuccess("绑定成功");
        this.cancelAgentChoice();
        this.getList();
      });
    },
  }
}
</script>

<style scoped>
.description-box {
  margin-top: 20px;
  padding: 15px;
  border: 1px solid #e2e2e2;
  border-radius: 4px;
  background-color: #f9f9f9;
}
.description-box h4 {
  margin-top: 0;
  margin-bottom: 10px;
}
.description-box p {
  margin-bottom: 0;
}
</style>
