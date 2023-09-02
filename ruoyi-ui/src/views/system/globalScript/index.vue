<template>
  <div class="app-container">
    <el-form
      v-show="showSearch"
      ref="queryForm"
      :inline="true"
      :model="queryParams"
      size="small"
    >
      <el-form-item label="脚本名" prop="name">
        <el-input
          v-model="queryParams.name"
          clearable
          placeholder="请输入脚本名"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="脚本Key" prop="scriptKey">
        <el-input
          v-model="queryParams.scriptKey"
          clearable
          placeholder="请输入脚本Key"
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
      <el-form-item label="脚本语言" prop="language">
        <el-select
          v-model="queryParams.language"
          clearable
          placeholder="请选择脚本语言"
        >
          <el-option
            v-for="dict in dict.type.sys_script_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input
          v-model="queryParams.remark"
          placeholder="请输入备注"
          clearable
          size="small"
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
          v-hasPermi="['system:globalScript:add']"
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
          v-hasPermi="['system:globalScript:edit']"
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
          v-hasPermi="['system:globalScript:remove']"
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
          v-hasPermi="['system:globalScript:export']"
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
      :data="globalScriptList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column align="center" type="selection" width="55" />
      <!-- <el-table-column align="center" label="" type="index"/> -->
      <el-table-column align="center" label="编号" prop="scriptId" />
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="脚本名"
        prop="name"
      >
        <template slot-scope="scope">
          {{ scope.row.name }}
          <span v-if="scope.row.description">
            <el-tooltip :content="scope.row.description" placement="top">
              <i
                class="el-icon-info"
                style="margin-left: 0px; margin-right: 10px"
              ></i>
            </el-tooltip>
          </span>
        </template>
      </el-table-column>
      <!-- <el-table-column label="脚本Key" align="center" prop="scriptKey" /> -->
      <!-- <el-table-column
        label="脚本描述"
        align="center"
        prop="description"
        :show-overflow-tooltip="true"
      /> -->
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
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="脚本内容"
        prop="content"
      />
      <el-table-column align="center" label="脚本语言" prop="language">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_script_type"
            :value="scope.row.language"
          />
        </template>
      </el-table-column>
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
            v-hasPermi="['system:globalScript:edit']"
            icon="el-icon-edit"
            size="mini"
            type="text"
            @click="handleUpdate(scope.row)"
            >修改
          </el-button>
          <el-button
            v-hasPermi="['system:globalScript:remove']"
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

    <!-- 添加或修改全局脚本对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="800px">
      <el-form ref="form" :model="form" :rules="rules">
        <el-form-item prop="">
          <el-col :span="12">
            <el-form-item
              label="脚本名称"
              label-width="70px"
              prop="name"
              style="width: 300px"
            >
              <el-input v-model="form.name" placeholder="请输入脚本名称"  maxlength="30" show-word-limit/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="脚本语言" prop="language">
              <el-select
                v-model="form.language"
                placeholder="请选择脚本语言"
                @change="handleChangeLanguage"
              >
                <el-option
                  v-for="dict in dict.type.sys_script_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item v-if="form.scriptId" label="脚本Key" prop="scriptKey">
          <el-input
            v-model="form.scriptKey"
            :readonly="true"
            placeholder="未获取到相关信息"
          />
        </el-form-item>
        <el-form-item label="脚本描述" prop="description">
          <el-input
            v-model="form.description"
            placeholder="请输入内容"
            type="textarea"
             maxlength="500" show-word-limit
          />
        </el-form-item>
        <el-form-item prop="">
          <el-col :span="12">
            <el-form-item label="是否需要登录" prop="checkToken">
              <el-select
                v-model="form.checkToken"
                placeholder="请选择是否需要登录"
              >
                <el-option
                  v-for="dict in dict.type.sys_yes_no"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否需要VIP" prop="checkVip">
              <el-select
                v-model="form.checkVip"
                placeholder="请选择是否需要VIP"
              >
                <el-option
                  v-for="dict in dict.type.sys_yes_no"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
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
        <el-form-item label="脚本内容">
          <el-form-item label-width="0px" prop="">
            <el-tooltip content="多个脚本参数以空格分隔" placement="top">
              <template slot="content">
                可使用内置变量获取相关信息，形如“${对象名.属性名}”，当属性名为“*”时表示获取该对象下所有属性（json格式），目前支持的对象有：
                <ul>
                  <li>
                    app，可获取软件信息，如${app.appName}可获取当前软件名称
                  </li>
                  <li>
                    appUser，可获取用户信息，如${appUser.userInfo.userName}可获取当前登录用户名，${appUser.loginCode}可获取当前登录单码
                  </li>
                  <li>
                    version，可获取版本信息，如${version.versionName}可获取当前版本名称
                  </li>
                  <li>
                    trialUser，可获取试用用户信息，如${trialUser.expireTime}可获取当前试用到期时间
                  </li>
                  <li>
                    deviceCode，可获取设备码信息，如${deviceCode.deviceCode}可获取当前设备码
                  </li>
                </ul>
                注：当内置变量取值有误时，将返回“ERR_NULL”，脚本调用测试时无法解析内置变量，将全部返回“ERR_NULL”
              </template>
              <i class="el-icon-info" style="margin-left: -10px"></i>
            </el-tooltip>
          </el-form-item>
          <!-- <editor v-model="form.content" :min-height="192" /> -->
          <div style="margin-bottom: 25px">
            <CodeEditor
              v-model="form.content"
              :language="scriptLanguage"
              title="全局远程函数"
            ></CodeEditor>
          </div>
        </el-form-item>
        <el-form-item label="脚本调用测试" prop="">
          <el-col :span="1">
            <el-form-item label-width="0px" prop="">
              <el-tooltip content="多个脚本参数以空格分隔" placement="top">
                <i class="el-icon-info" style="margin-left: -10px"></i>
              </el-tooltip>
            </el-form-item>
          </el-col>
          <el-col :span="17">
            <el-form-item label-width="0px" prop="">
              <el-input
                v-model="scriptParams"
                placeholder="请输入参数"
                style="margin-left: -10px"
              />
            </el-form-item>
          </el-col>
          <el-col :span="2">
            <el-form-item label-width="0px" prop="">
              <el-button
                style="margin-left: 10px"
                type="primary"
                @click="scriptTest"
              >
                调用
              </el-button>
            </el-form-item>
          </el-col>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog
      :visible.sync="dialogVisible"
      custom-class="customClass"
      title="脚本执行结果"
      width="800px"
    >
      <div v-if="scriptTestResult">
        <el-form>
          <el-form-item label="脚本退出代码（0为正常）">
            <el-input :readonly="true" :value="scriptTestResult.exitCode" />
          </el-form-item>
          <el-form-item label="执行结果">
            <el-input
              :readonly="true"
              :value="scriptTestResult.result"
              autosize
              type="textarea"
            />
          </el-form-item>
          <el-form-item label="错误信息">
            <el-input
              :readonly="true"
              :value="scriptTestResult.error"
              autosize
              type="textarea"
            />
          </el-form-item>
          <el-form-item label="原始文本">
            <el-input
              :readonly="true"
              :value="JSON.stringify(scriptTestResult)"
              autosize
              type="textarea"
            />
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogVisible = false"
          >确 定</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  addGlobalScript,
  delGlobalScript,
  getGlobalScript,
  listGlobalScript,
  testGlobalScript,
  updateGlobalScript,
} from "@/api/system/globalScript";
import CodeEditor from "@/components/CodeEditor";

export default {
  name: "GlobalScript",
  dicts: ["sys_script_type", "sys_yes_no"],
  components: {
    CodeEditor,
  },
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
      // 全局脚本表格数据
      globalScriptList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: this.$store.state.settings.pageSize,
        name: null,
        checkToken: null,
        checkVip: null,
        content: null,
        language: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        checkToken: [
          { required: true, message: "是否需要登录不能为空", trigger: "blur" },
        ],
        checkVip: [
          { required: true, message: "是否需要VIP不能为空", trigger: "blur" },
        ],
      },
      scriptParams: "",
      scriptTestResult: null,
      dialogVisible: false,
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询全局脚本列表 */
    getList() {
      this.loading = true;
      listGlobalScript(this.queryParams).then((response) => {
        this.globalScriptList = response.rows;
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
        scriptId: null,
        name: null,
        description: null,
        checkToken: null,
        checkVip: null,
        content: null,
        language: null,
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
      this.ids = selection.map((item) => item.scriptId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加全局脚本";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const scriptId = row.scriptId || this.ids;
      getGlobalScript(scriptId).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改全局脚本";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.scriptId != null) {
            updateGlobalScript(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addGlobalScript(this.form).then((response) => {
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
      const scriptIds = row.scriptId || this.ids;
      this.$modal
        .confirm("是否确认删除数据项？")
        .then(function () {
          return delGlobalScript(scriptIds);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "system/globalScript/export",
        {
          ...this.queryParams,
        },
        `globalScript_${new Date().getTime()}.xlsx`
      );
    },
    handleChangeLanguage(item) {
      this.language = item;
      if (item == "python2" || item == "python3") {
        this.language = "python";
      }
    },
    scriptTest() {
      this.form.scriptParams = this.scriptParams;
      testGlobalScript(this.form).then((response) => {
        this.scriptTestResult = response.data;
        this.dialogVisible = true;
      });
    },
  },
  computed: {
    scriptLanguage: function () {
      if (this.form.language == "1") {
        return "javascript";
      } else if (this.form.language == "2" || this.form.language == "3") {
        return "python";
      } else if (this.form.language == "4") {
        return "php";
      }
    },
  },
};
</script>
