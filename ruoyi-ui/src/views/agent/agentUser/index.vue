<template>
  <div class="app-container">
    <el-form
      v-show="showSearch"
      ref="queryForm"
      :model="queryParams"
      :inline="true"
      size="small"
    >
      <el-form-item label="用户名称" prop="userName">
        <el-input
          v-model="queryParams.userName"
          clearable
          placeholder="请输入用户名称"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="允许发展下级" prop="enableAddSubagent">
        <el-select
          v-model="queryParams.enableAddSubagent"
          placeholder="请选择允许发展下级"
          clearable
        >
          <el-option
            v-for="dict in dict.type.sys_yes_no"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="代理过期时间">
        <el-date-picker
          v-model="daterangeExpireTime"
          end-placeholder="结束日期"
          style="width: 240px"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          value-format="yyyy-MM-dd"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="代理状态" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="请选择代理状态"
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
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
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
          v-hasPermi="['agent:agentUser:add']"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          type="primary"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          icon="el-icon-sort"
          plain
          type="info"
          size="mini"
          @click="toggleExpandAll"
        >展开/折叠
        </el-button>
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-if="refreshTable"
      v-loading="loading"
      :data="agentUserList"
      row-key="agentId"
      :default-expand-all="isExpandAll"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
    >
      <!--      <el-table-column label="上级代理ID" prop="parentAgentId" />-->
      <!--      <el-table-column label="用户ID" align="center" prop="userId" />-->
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="用户名称"
        prop="userId"
      >
        <template slot-scope="scope">
          {{ scope.row.user.nickName }}({{ scope.row.user.userName }})
        </template>
      </el-table-column>
      <el-table-column
        label="上级代理"
        align="center"
        prop="parentAgentId"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          <div v-if="scope.row.parentUser">
            {{ scope.row.parentUser.nickName }}({{
              scope.row.parentUser.userName
            }})
          </div>
        </template>
      </el-table-column>
      <el-table-column
        label="允许发展下级"
        align="center"
        prop="enableAddSubagent"
      >
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_yes_no"
            :value="scope.row.enableAddSubagent"
          />
        </template>
      </el-table-column>
      <el-table-column
        label="代理过期时间"
        align="center"
        prop="expireTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.expireTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="代理状态" prop="status">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_normal_disable"
            :value="scope.row.status"
          />
        </template>
      </el-table-column>
      <el-table-column align="center" label="备注" prop="remark"/>
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            v-hasPermi="['agent:agentUser:edit']"
            @click="handleUpdate(scope.row)"
            icon="el-icon-edit"
          >修改
          </el-button>
          <el-button
            v-hasPermi="['agent:agentUser:grant']"
            icon="el-icon-edit"
            size="mini"
            type="text"
            @click="handleGrant(scope.row)"
          >授权
          </el-button>
          <el-button
            v-hasPermi="['agent:agentUser:add']"
            icon="el-icon-plus"
            size="mini"
            type="text"
            @click="handleAdd(scope.row)"
          >新增
          </el-button>
          <el-button
            size="mini"
            type="text"
            v-hasPermi="['agent:agentUser:remove']"
            @click="handleDelete(scope.row)"
            icon="el-icon-delete"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改代理用户对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="500px">
      <el-form ref="form" :model="form" :rules="rules">
        <el-form-item label="上级代理" prop="parentAgentId">
          <treeselect
            v-model="form.parentAgentId"
            :options="agentUserOptions"
            :normalizer="normalizer"
            placeholder="请选择上级代理"
          />
        </el-form-item>
        <el-form-item label="用户名称" prop="userId">
          <!--          <el-input v-model="form.userId" placeholder="请输入用户ID" />-->
          <div v-if="form.agentId == null">
            <el-select
              v-model="form.userId"
              :clearable="true"
              filterable
              placeholder="请选择"
              prop="userId"
            >
              <el-option
                v-for="item in nonAgentList"
                :key="item.userId"
                :disabled="item.disabled"
                :label="item.nickName + '(' + item.userName + ')'"
                :value="item.userId"
              >
              </el-option>
            </el-select>
          </div>
          <div v-else>{{ form.user.nickName }}({{ form.user.userName }})</div>
        </el-form-item>
        <el-form-item label="允许发展下级" prop="enableAddSubagent">
          <el-radio-group v-model="form.enableAddSubagent">
            <el-radio
              v-for="dict in dict.type.sys_yes_no"
              :key="dict.value"
              :label="dict.value"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="代理过期时间" prop="expireTime">
          <el-date-picker
            clearable
            v-model="form.expireTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="选择代理过期时间"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="代理状态">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.sys_normal_disable"
              :key="dict.value"
              :label="dict.value"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form.remark"
            type="textarea"
            placeholder="请输入内容"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 授权代理卡类对话框 -->
    <el-dialog
      :visible.sync="grantOpen"
      append-to-body
      title="可代理卡类授权"
      width="800px"
    >
      <el-transfer
        v-model="grantValue"
        :data="grantData"
        :filter-method="filterMethod"
        :titles="['可选卡类', '已选卡类']"
        filter-placeholder="请输入软件/卡类名称"
        filterable
      >
      </el-transfer>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitGrantForm">确 定</el-button>
        <el-button @click="grantCancel">取 消</el-button>
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

export default {
  name: "AgentUser",
  dicts: ["sys_yes_no", "sys_normal_disable"],
  components: {
    Treeselect,
  },
  data() {
    return {
      // 可选代理列表
      nonAgentList: [],
      // 遮罩层
      loading: true,
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
          {required: true, message: "用户账号不能为空", trigger: "blur"},
        ],
        enableAddSubagent: [
          {
            required: true,
            message: "是否允许发展下级不能为空",
            trigger: "blur",
          },
        ],
        status: [
          {required: true, message: "代理状态不能为空", trigger: "blur"},
        ],
      },
      // 卡类授权
      grantOpen: false,
      grantData: [
        {key: 0, label: "aaa"},
        {key: 1, label: "bbb"},
        {key: 2, label: "ccc"},
      ],
      grantValue: [],
      filterMethod(query, item) {
        return item.label.indexOf(query) > -1;
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
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
          user: {nickName: "根节点", userName: "无上级"},
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
    grantCancel() {
      this.grantOpen = false;
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
      };
      this.resetForm("form");
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
      this.getNonAgentsList();
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
      if (row != null) {
        this.form.parentAgentId = row.agentId;
      }
      getAgentUser(row.agentId).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改代理用户";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
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
        .catch(() => {
        });
    },
    getNonAgentsList() {
      this.loading = true;
      listNonAgents().then((response) => {
        this.nonAgentList = response.rows;
        this.loading = false;
      });
    },
    /** 授权按钮操作 */
    handleGrant(row) {
      const agentId = row.agentId || this.ids;
      this.grantOpen = true;
    },
    /** 提交按钮 */
    submitGrantForm() {
      console.log(this.grantValue);
    },
  },
};
</script>
