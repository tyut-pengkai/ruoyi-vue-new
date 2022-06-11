<template>
  <div class="app-container">
    <el-form
      v-show="showSearch"
      ref="queryForm"
      :inline="true"
      :model="queryParams"
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
          clearable
          placeholder="请选择允许发展下级"
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
          range-separator="-"
          start-placeholder="开始日期"
          style="width: 240px"
          type="daterange"
          value-format="yyyy-MM-dd"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="代理状态" prop="status">
        <el-select
          v-model="queryParams.status"
          clearable
          placeholder="请选择代理状态"
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
          v-hasPermi="['agent:agentUser:add']"
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
          v-hasPermi="['agent:agentUser:edit']"
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
          v-hasPermi="['agent:agentUser:remove']"
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
          v-hasPermi="['agent:agentUser:export']"
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
      :data="agentList"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      default-expand-all
      row-key="agentId"
      @selection-change="handleSelectionChange"
    >
      <el-table-column align="center" type="selection" width="55"/>
      <el-table-column align="center" label="代理ID" prop="agentId"/>
      <!-- <el-table-column label="用户ID" align="center" prop="userId" /> -->
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
      <!-- <el-table-column label="上级代理ID" align="center" prop="parentAgentId" /> -->
      <!-- <el-table-column
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
      </el-table-column> -->
      <el-table-column
        align="center"
        label="允许发展下级"
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
        align="center"
        label="代理过期时间"
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
        align="center"
        class-name="small-padding fixed-width"
        label="操作"
      >
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['agent:agentUser:edit']"
            icon="el-icon-edit"
            size="mini"
            type="text"
            @click="handleUpdate(scope.row)"
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
            v-hasPermi="['agent:agentUser:remove']"
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

    <!-- 添加或修改代理管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="500px">
      <el-form ref="form" :model="form" :rules="rules">
        <el-form-item label="上级代理" prop="parentAgentId">
          <el-select
            v-model="form.parentAgentId"
            :clearable="true"
            filterable
            placeholder="请选择"
            prop="parentAgentId"
          >
            <el-option
              v-for="item in isAgentList"
              :key="item.agentId"
              :disabled="item.disabled"
              :label="item.nickName + '(' + item.userName + ')'"
              :value="item.agentId"
            >
            </el-option>
          </el-select>
          <!-- <treeselect
            v-model="form.parentAgentId"
            :options="agentOptions"
            :normalizer="normalizer"
            :show-count="true"
            placeholder="选择上级菜单"
          /> -->
        </el-form-item>
        <el-form-item label="用户名称" prop="userId">
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
            v-model="form.expireTime"
            clearable
            placeholder="请选择代理过期时间"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="代理状态" prop="status">
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
  listAgents,
  listAgentUser,
  listNonAgents,
  updateAgentUser,
} from "@/api/agent/agentUser";
// import Treeselect from "@riophae/vue-treeselect";
// import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "Agent",
  dicts: ["sys_yes_no", "sys_normal_disable"],
  // components: { Treeselect },
  data() {
    return {
      // 可选代理列表
      isAgentList: [],
      nonAgentList: [],
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
      // 代理管理表格数据
      agentList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 备注时间范围
      daterangeExpireTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        parentAgentId: null,
        userName: null,
        enableAddSubagent: null,
        expireTime: null,
        status: null,
      },
      // 表单参数
      form: {
        enableAddSubagent: "Y",
        status: "0",
      },
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
      // agentOptions: [],
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
    /** 查询代理管理列表 */
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
        this.agentList = response.rows;
        this.total = response.total;
        this.loading = false;
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
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.daterangeExpireTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.agentId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.loading = true;
      this.getAgentsList("-1");
      this.getNonAgentsList();
      this.open = true;
      this.title = "添加代理管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const agentId = row.agentId || this.ids;
      this.loading = true;
      this.getAgentsList(agentId);
      getAgentUser(agentId).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改代理管理";
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
      const agentIds = row.agentId || this.ids;
      this.$modal
        .confirm("是否确认删除数据项？")
        .then(function () {
          return delAgentUser(agentIds);
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
        "agent/agent/export",
        {
          ...this.queryParams,
        },
        `agent_${new Date().getTime()}.xlsx`
      );
    },
    getAgentsList(agentId) {
      this.loading = true;
      listAgents(agentId).then((response) => {
        this.isAgentList = response.rows;
        this.loading = false;
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
    // /** 转换菜单数据结构 */
    // normalizer(node) {
    //   if (node.children && !node.children.length) {
    //     delete node.children;
    //   }
    //   return {
    //     id: node.agentId,
    //     label: node.nickName + "(" + node.userName + ")",
    //     children: node.children,
    //   };
    // },
    // /** 查询菜单下拉树结构 */
    // getTreeselect() {
    //   listAgentUser().then((response) => {
    //     this.agentOptions = [];
    //     const root = {
    //       agentId: 0,
    //       nickName: "根代理",
    //       userName: "无上级",
    //       children: [],
    //     };
    //     root.children = this.handleTree(response.data, "agentId");
    //     this.agentOptions.push(root);
    //   });
    // },
  },
};
</script>
