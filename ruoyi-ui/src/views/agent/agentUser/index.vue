<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" size="small">
      <el-form-item label="代理名称" prop="userName">
        <el-input v-model="queryParams.userName" clearable placeholder="请输入代理名称" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="允许发展下级" prop="enableAddSubagent">
        <el-select v-model="queryParams.enableAddSubagent" placeholder="请选择允许发展下级" clearable>
          <el-option v-for="dict in dict.type.sys_yes_no" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="代理过期时间">
        <el-date-picker v-model="daterangeExpireTime" end-placeholder="结束日期" style="width: 240px" type="daterange"
          range-separator="-" start-placeholder="开始日期" value-format="yyyy-MM-dd"></el-date-picker>
      </el-form-item>
      <el-form-item label="代理状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择代理状态" clearable>
          <el-option v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.label"
            :value="dict.value" />
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
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索
        </el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置
        </el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button v-hasPermi="['agent:agentUser:add']" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          type="primary">新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button icon="el-icon-sort" plain type="info" size="mini" @click="toggleExpandAll">展开/折叠
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-if="refreshTable" v-loading="loading" :data="agentUserList" row-key="agentId"
      :default-expand-all="isExpandAll" :tree-props="{ children: 'children', hasChildren: 'hasChildren' }">
      <!--      <el-table-column label="上级代理ID" prop="parentAgentId" />-->
      <!--      <el-table-column label="用户ID" align="center" prop="userId" />-->
      <el-table-column :show-overflow-tooltip="true" align="center" label="代理名称" prop="userId">
        <template slot-scope="scope">
          {{ scope.row.user.nickName }}({{ scope.row.user.userName }})
        </template>
      </el-table-column>
      <el-table-column label="上级代理" align="center" prop="parentAgentId" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <div v-if="scope.row.parentUser">
            {{ scope.row.parentUser.nickName }}({{
              scope.row.parentUser.userName
            }})
          </div>
        </template>
      </el-table-column>
      <el-table-column label="允许发展子代理" align="center" prop="enableAddSubagent">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_yes_no" :value="scope.row.enableAddSubagent" />
        </template>
      </el-table-column>
      <el-table-column label="代理过期时间" align="center" prop="expireTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.expireTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="代理状态" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column align="center" label="备注" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" v-hasPermi="['agent:agentUser:edit']" @click="handleUpdate(scope.row)"
            icon="el-icon-edit">修改
          </el-button>
          <!-- <el-button
            v-hasPermi="['agent:agentUser:grant']"
            icon="el-icon-edit"
            size="mini"
            type="text"
            @click="handleGrant(scope.row)"
            >授权
          </el-button> -->
          <el-button v-hasPermi="['agent:agentUser:add']" v-hasRole="['sadmin', 'admin']" icon="el-icon-plus" size="mini" type="text"
            @click="handleAdd(scope.row)">新增
          </el-button>
          <el-button size="mini" type="text" v-hasPermi="['agent:agentUser:remove']" @click="handleDelete(scope.row)"
            icon="el-icon-delete">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改代理用户对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="800px">
      <el-form ref="form" :model="form" :rules="rules">
        <el-form-item label="上级代理" prop="parentAgentId">
          <div v-if="checkRole(['sadmin', 'admin'])">
            <treeselect v-model="form.parentAgentId" :normalizer="normalizer" :options="agentUserOptions"
              placeholder="请选择上级代理" />
          </div>
          <div v-else>
            {{ user.nickName + "(" + user.userName + ")" }}
          </div>
        </el-form-item>
        <el-row>
          <el-col :span="10">
            <el-form-item label="代理名称" prop="userId">
              <!--          <el-input v-model="form.userId" placeholder="请输入用户ID" />-->
              <div v-if="form.agentId == null">
                <el-select v-model="form.userId" :clearable="true" filterable :loading="loading"
                  :remote-method="remoteMethod" prop="userId" placeholder="请输入用户名" remote>
                  <el-option v-for="item in nonAgentList" :key="item.userId" :disabled="item.disabled"
                    :label="item.nickName + '(' + item.userName + ')'" :value="item.userId">
                  </el-option>
                </el-select>
              </div>
              <div v-else>{{ form.user.nickName }}({{ form.user.userName }})</div>
            </el-form-item>
          </el-col>
          <el-col :span="14">
            <el-form-item label="代理过期时间(留空长期)" prop="expireTime">
              <el-date-picker clearable v-model="form.expireTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="选择代理过期时间" :picker-options="pickerOptions">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="10">
            <el-form-item label="代理状态">
              <el-radio-group v-model="form.status">
                <el-radio v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.value">{{ dict.label
                }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="14">
            <el-form-item label="允许发展子代理" prop="enableAddSubagent">
              <span>
                <el-tooltip
                  content="如果由允许修改为不允许，已经发展的子代不受影响"
                  placement="top"
                >
                  <i
                    class="el-icon-question"
                    style="margin-left: -12px; margin-right: 10px"
                  ></i>
                </el-tooltip>
              </span>
              <el-radio-group v-model="form.enableAddSubagent">
                <el-radio v-for="dict in dict.type.sys_yes_no" :key="dict.value" :label="dict.value">{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="代理卡密类别/单码类别">
          <el-row>
            <el-col :span="24">
              <!-- 批量操作： -->
              批量：
              <el-select v-model="batchSelectTemplate" multiple filterable collapse-tags
                style="width:200px;margin-right: 3px;" placeholder="批量选择" size="small">
                <el-option v-for="item in templateNameFilters2" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
              <el-input-number v-model="batchAgentPrice" :min="0" :precision="2" :step="0.01" controls-position="right"
                style="width: 120px;margin-right: 3px;" placeholder="代理价格" size="small" />
              <el-date-picker v-model="batchExpireTime" clearable placeholder="过期时间" :picker-options="pickerOptions"
                value-format="yyyy-MM-dd HH:mm:ss" type="datetime" style="width: 180px;margin-right: 3px;" size="small">
              </el-date-picker>
              <el-input v-model="batchRemark" placeholder="备注" style="width: 120px;margin-right: 3px;" size="small" />
              <el-button type="primary" size="small" @click="handleBatchSet">批量填充</el-button>
            </el-col>
          </el-row>
          <el-table ref="templateTable" :data="templateList" tooltip-effect="dark" style="width: 100%" max-height="300"
            :header-row-style="{ height: '30px' }" :header-cell-style="{ background: '#f5f7fa', padding: '0px' }"
            :row-style="{ height: '30px' }" :cell-style="{ padding: '0px' }" size='mini' border height="300"
            @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="40" fixed> </el-table-column>
            <el-table-column label="卡密类别/单码类别名称" width="180" fixed show-overflow-tooltip :filters="templateNameFilters"
              :filter-method="filterHandler">
              <template slot-scope="scope">
                {{
                  "[" +
                  scope.row.appName +
                  "]" +
                  scope.row.templateName
                }}
              </template>
            </el-table-column>
            <el-table-column prop="price" label="零售价格" width="80" show-overflow-tooltip>
              <template slot-scope="scope">
                {{ scope.row.price }}
                <span>元</span>
              </template>
            </el-table-column>
            <el-table-column prop="myPrice" label="我的提卡价格" width="120" show-overflow-tooltip>
                <template slot-scope="scope">
                  {{ scope.row.myPrice ? scope.row.myPrice + '元' : (scope.row.myPrice === 0 ? '0元' : '未授权')}}
                </template>
              </el-table-column>
            <el-table-column prop="agentPrice" label="代理提卡价格" width="150">
              <template slot-scope="scope">
                <el-input-number v-model="scope.row.agentPrice" :min="0" :precision="2" :step="0.01"
                  controls-position="right" style="width: 120px" placeholder="代理提卡价格" size="mini" />
                <!-- <span>元</span> -->
              </template>
            </el-table-column>
            <el-table-column prop="expireTime" label="代理该卡过期时间(留空长期)" width="210">
              <template slot-scope="scope">
                <el-date-picker v-model="scope.row.expireTime" clearable placeholder="请选择时间"
                  :picker-options="pickerOptions" value-format="yyyy-MM-dd HH:mm:ss" type="datetime" style="width: 180px"
                  size="mini">
                </el-date-picker>
              </template>
            </el-table-column>
            <el-table-column prop="remark" label="备注信息" width="130">
              <template slot-scope="scope">
                <el-input v-model="scope.row.remark" placeholder="请输入内容" style="width: 100px;" size="mini" />
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>

        <!-- <el-form-item label="代理卡密类别/单码类别">
          <el-checkbox-group v-model="checkList">
            <el-checkbox
              v-for="(item, idx) in templateList"
              :key="idx"
              :label="idx"
            >
              <div>
                <el-row>
                  <el-col :span="6">
                    <el-form-item>
                      {{
                        "[" +
                        item.appName +
                        "]" +
                        item.templateName +
                        "|零售" +
                        item.price +
                        "元"
                      }}
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="代理价格" prop="agentPrice">
                      <el-input-number
                        v-model="item.agentPrice"
                        :min="0"
                        :precision="2"
                        :step="0.01"
                        controls-position="right"
                        style="width: 120px"
                      />
                      <span>元</span>
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="代理该卡过期时间" prop="expireTime">
                      <el-date-picker
                        v-model="item.expireTime"
                        clearable
                        placeholder="请选择代理该卡过期时间"
                        :picker-options="pickerOptions"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        type="datetime"
                        style="width: 120px"
                      >
                      </el-date-picker>
                    </el-form-item>
                  </el-col>
                  <el-col :span="2">
                    <el-form-item label="备注" prop="remark">
                      <el-input
                        v-model="item.remark"
                        placeholder="请输入内容"
                        style="width: 120px"
                      />
                    </el-form-item>
                  </el-col>
                </el-row>
              </div>
            </el-checkbox>
          </el-checkbox-group> -->
        <!-- <div v-for="(item, idx) in templateList" :key="idx">
            <div>
              <el-checkbox :v-model="form.cid[idx].checked">
                {{
                  "[" +
                  item.appName +
                  "]" +
                  item.templateName +
                  "|零售" +
                  item.price +
                  "元"
                }}
              </el-checkbox>
            </div>
          </div>
        </el-form-item> -->
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
  addAgentUser,
  delAgentUser,
  getAgentUser,
  listAgentUser,
  listNonAgents,
  updateAgentUser,
} from "@/api/agent/agentUser";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { checkPermi, checkRole } from "@/utils/permission"; // 权限判断函数
import { getUserProfile } from "@/api/system/user";
import { grantableTemplate } from "@/api/agent/agentItem";

export default {
  name: "AgentUser",
  dicts: ["sys_yes_no", "sys_normal_disable"],
  components: {
    Treeselect,
  },
  data() {
    return {
      user: {},
      userBak: {},
      // 可选代理列表
      nonAgentList: [],
      // 遮罩层
      loading: false,
      // 显示搜索条件
      showSearch: true,
      // 代理用户表格数据
      agentUserList: [],
      // 代理用户树选项
      agentUserOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否展开，默认全部展开
      isExpandAll: true,
      // 重新渲染表格状态
      refreshTable: true,
      // 备注时间范围
      daterangeExpireTime: [],
      // 查询参数
      queryParams: {
        userName: null,
        enableAddSubagent: null,
        expireTime: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "用户账号不能为空", trigger: "blur" },
        ],
        enableAddSubagent: [
          {
            required: true,
            message: "是否允许发展下级不能为空",
            trigger: "blur",
          },
        ],
        status: [
          { required: true, message: "代理状态不能为空", trigger: "blur" },
        ],
      },
      pickerOptions: {
        shortcuts: [
          {
            text: "长期有效",
            onClick(picker) {
              picker.$emit("pick", new Date("9999-12-31 23:59:59"));
            },
          },
          {
            text: "1小时后",
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() + 3600 * 1000);
              picker.$emit("pick", date);
            },
          },
          {
            text: "1天后",
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() + 3600 * 1000 * 24);
              picker.$emit("pick", date);
            },
          },
          {
            text: "1周后(7天)",
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() + 3600 * 1000 * 24 * 7);
              picker.$emit("pick", date);
            },
          },
          {
            text: "1月后(30天)",
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() + 3600 * 1000 * 24 * 30);
              picker.$emit("pick", date);
            },
          },
          {
            text: "1年后(365天)",
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() + 3600 * 1000 * 24 * 365);
              picker.$emit("pick", date);
            },
          },
        ],
      },
      templateList: [],
      templateSelectionList: [],
      batchSelectTemplate: [],
      batchAgentPrice: null,
      batchExpireTime: null,
      batchRemark: null,
    };
  },
  computed: {
    templateNameFilters() {
      let arr = [];
      let records = [];
      if (this.templateList) {
        for (let item of this.templateList) {
          // let value = "[" +
          //   item.appName +
          //   "]" +
          //   item.templateName;
          let value = item.appName;
          if (records.indexOf(value) == -1) {
            records.push(value);
            arr.push({ "text": value, "value": value });
          }
        }
      }
      return arr;
    },
    templateNameFilters2() {
      let arr = [];
      let records = [];
      if (this.templateList) {
        for (let item of this.templateList) {
          let value = "[" +
            item.appName +
            "]" +
            item.templateName;
          if (records.indexOf(value) == -1) {
            records.push(value);
            arr.push({ "text": value, "value": value });
          }
        }
      }
      return arr;
    }
  },
  created() {
    this.getList();
    this.getUser();
  },
  methods: {
    checkPermi,
    checkRole,
    /** 查询代理用户列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (this.queryParams.userName) {
        this.queryParams.params["userName"] = this.queryParams.userName;
      }
      if (null != this.daterangeExpireTime && "" != this.daterangeExpireTime) {
        this.queryParams.params["beginExpireTime"] =
          this.daterangeExpireTime[0];
        this.queryParams.params["endExpireTime"] = this.daterangeExpireTime[1];
      }
      listAgentUser(this.queryParams).then((response) => {
        this.agentUserList = this.handleTree(
          response.data,
          "agentId",
          "parentAgentId"
        );
        this.loading = false;
      });
    },
    /** 转换代理用户数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.agentId,
        label: node.user
          ? node.user.nickName + "(" + node.user.userName + ")"
          : "",
        children: node.children,
      };
    },
    /** 查询代理用户下拉树结构 */
    getTreeselect() {
      listAgentUser().then((response) => {
        this.agentUserOptions = [];
        const data = {
          agentId: 0,
          user: { nickName: "根节点", userName: "无上级" },
          children: [],
        };
        data.children = this.handleTree(
          response.data,
          "agentId",
          "parentAgentId"
        );
        this.agentUserOptions.push(data);
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
        agentId: null,
        parentAgentId: null,
        userId: null,
        enableAddSubagent: "Y",
        expireTime: null,
        status: "0",
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        cid: [],
      };
      this.resetForm("form");
      this.templateList = [];
      if (this.$refs.templateTable) {
        this.$refs.templateTable.clearSelection();
      }
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.daterangeExpireTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset();
      this.getTreeselect();
      this.getGrantableTemplate({ 'agentId': row.agentId });
      // this.getNonAgentsList();
      this.user = this.userBak;
      if (row != null && row.agentId) {
        this.form.parentAgentId = row.agentId;
      } else {
        this.form.parentAgentId = 0;
      }
      this.open = true;
      this.title = "添加代理用户";
    },
    /** 展开/折叠操作 */
    toggleExpandAll() {
      this.refreshTable = false;
      this.isExpandAll = !this.isExpandAll;
      this.$nextTick(() => {
        this.refreshTable = true;
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.getTreeselect();
      this.getGrantableTemplate({ 'agentId': row.agentId });
      if (row != null) {
        this.form.parentAgentId = row.agentId;
      }
      getAgentUser(row.agentId).then((response) => {
        this.form = response.data;
        if (row != null) {
          this.user = response.data.parentUser;
        }
        this.open = true;
        this.title = "修改代理用户";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.form.templateList = this.templateSelectionList;
          if (this.form.agentId != null) {
            updateAgentUser(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAgentUser(this.form).then((response) => {
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
      this.$modal
        .confirm("是否确认删除数据项？")
        .then(function () {
          return delAgentUser(row.agentId);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => { });
    },
    getNonAgentsList(query) {
      this.loading = true;
      listNonAgents(query).then((response) => {
        this.nonAgentList = response.rows;
        this.loading = false;
      });
    },
    getUser() {
      getUserProfile().then((response) => {
        this.user = response.data;
        this.userBak = this.user;
      });
    },
    remoteMethod(query) {
      if (query !== "") {
        this.getNonAgentsList({ username: query });
      } else {
        this.nonAgentList = [];
      }
    },
    getGrantableTemplate(query) {
      this.templateList = [];
      if (this.$refs.templateTable) {
        this.$refs.templateTable.clearSelection();
      }
      grantableTemplate(query).then((response) => {
        let selectedRowList = [];
        let dataList = response.data;
        for (let data of dataList) {
          let row = {
            id: this.templateList.length,
            agentItemId: data.agentItemId,
            templateId: data.templateId,
            templateType: data.templateType,
            templateName: data.templateName,
            appName: data.appName,
            price: data.price,
            checked: data.checked,
            agentPrice: data.agentPrice || data.myPrice,
            expireTime: data.expireTime,
            remark: data.remark,
            myPrice: data.myPrice,
          };
          this.templateList.push(row);
          if (data.checked) {
            selectedRowList.push(row);
          }
        }
        this.$nextTick(function () {
          for (let row of selectedRowList) {
            this.$refs.templateTable.toggleRowSelection(row, true);
          }
        });
      });
    },
    filterHandler(value, row, column) {
      // let val = "[" +
      //   row.appName +
      //   "]" +
      //   row.templateName;
      let val = row.appName
      return val === value;
    },
    handleSelectionChange(val) {
      this.templateSelectionList = val;
    },
    handleBatchSet() {
      let selectedRowList = [];
      for (let row of this.templateList) {
        let val = "[" + row.appName + "]" + row.templateName;
        if (this.batchSelectTemplate.indexOf(val) != -1) {
          row.checked = true;
          if (this.batchAgentPrice) {
            row.agentPrice = this.batchAgentPrice;
          }
          if (this.batchExpireTime) {
            row.expireTime = this.batchExpireTime;
          }
          if (this.batchRemark) {
            row.remark = this.batchRemark;
          }
          selectedRowList.push(row);
        }
      }
      this.$nextTick(function () {
        for (let row of selectedRowList) {
          this.$refs.templateTable.toggleRowSelection(row, true);
        }
      });
    }
  },
};
</script>
<style>
/**
  选择框不换行
*/
.el-select__tags {
  flex-wrap: nowrap;
  overflow: hidden;
}
</style>
