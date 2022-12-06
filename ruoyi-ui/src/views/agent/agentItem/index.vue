<template>
  <div class="app-container">
    <el-form
      v-show="showSearch"
      ref="queryForm"
      :inline="true"
      :model="queryParams"
      size="small"
    >
      <el-form-item label="代理名称" prop="agentName">
        <el-input
          v-model="queryParams.agentName"
          clearable
          placeholder="请输入代理名称"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="卡类ID" prop="templateId">
        <el-input
          v-model="queryParams.templateId"
          clearable
          placeholder="请输入卡类ID"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="代理该卡过期时间">
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
          v-hasPermi="['agent:agentItem:add']"
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
          v-hasPermi="['agent:agentItem:edit']"
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
          v-hasPermi="['agent:agentItem:remove']"
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
          v-hasPermi="['agent:agentItem:export']"
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
      :data="agentItemList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column align="center" type="selection" width="55" />
      <el-table-column align="center" label="编号" prop="id" />
      <!-- <el-table-column align="center" label="代理ID" prop="agentId"/> -->
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="代理名称"
        prop="agentId"
      >
        <template slot-scope="scope">
          {{ scope.row.user.nickName }}({{ scope.row.user.userName }})
        </template>
      </el-table-column>
      <!-- <el-table-column
        label="卡类别1充值卡2单码"
        align="center"
        prop="templateType"
      /> -->
      <el-table-column align="center" label="卡类类别" prop="templateType">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_template_type"
            :value="scope.row.templateType"
          />
        </template>
      </el-table-column>
      <!-- <el-table-column align="center" label="卡类ID" prop="templateId" /> -->
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="卡类名称"
        prop="templateId"
      >
        <template slot-scope="scope">
          [{{ scope.row.templateInfo.appName }}]{{
            scope.row.templateInfo.templateName
          }}
        </template>
      </el-table-column>
      <!-- <el-table-column align="center" label="零售价格" prop="price" /> -->
      <el-table-column align="center" label="零售价格" prop="price">
        <template slot-scope="scope">
          <span>{{ parseMoney(scope.row.price) }}元</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="代理价格" prop="agentPrice">
        <template slot-scope="scope">
          <span>{{ parseMoney(scope.row.agentPrice) }}元</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="代理该卡过期时间"
        prop="expireTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.expireTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="备注" prop="remark" />
      <el-table-column
        align="center"
        class-name="small-padding fixed-width"
        label="操作"
      >
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['agent:agentItem:edit']"
            icon="el-icon-edit"
            size="mini"
            type="text"
            @click="handleUpdate(scope.row)"
            >修改
          </el-button>
          <el-button
            v-hasPermi="['agent:agentItem:remove']"
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

    <!-- 添加或修改代理卡类关联对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="800px">
      <el-form ref="form" :model="form" :rules="rules">
        <!-- <el-form-item label="代理ID" prop="agentId">
          <el-input v-model="form.agentId" placeholder="请输入代理ID" />
        </el-form-item> -->
        <el-form-item label="代理账号" prop="agentId">
          <treeselect
            v-model="form.agentId"
            :normalizer="normalizer"
            :options="agentUserOptions"
            placeholder="请选择代理账号"
          />
        </el-form-item>
        <!-- <el-form-item label="卡类别1充值卡2单码" prop="templateType">
          <el-input v-model="form.templateType" placeholder="请输入卡类别" />
        </el-form-item> -->
        <el-form-item label="代理卡类" prop="tid">
          <!-- <el-input v-model="form.templateId" placeholder="请输入卡类ID" /> -->
          <el-select
            v-model="form.tid"
            :clearable="true"
            filterable
            placeholder="请选择代理卡类"
            prop="tid"
          >
            <el-option
              v-for="item in templateList"
              :key="item.id"
              :disabled="item.disabled"
              :label="
                '[' +
                item.appName +
                ']' +
                item.templateName +
                '|零售' +
                item.price +
                '元'
              "
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <!-- <el-form-item label="代理价格" prop="agentPrice">
          <el-input v-model="form.agentPrice" placeholder="请输入代理价格" />
        </el-form-item> -->
        <el-form-item label="代理价格" label-width="80px" prop="agentPrice">
          <el-input-number
            v-model="form.agentPrice"
            :min="0"
            :precision="2"
            :step="0.01"
            controls-position="right"
          />
          <span>元</span>
        </el-form-item>
        <el-form-item label="代理该卡过期时间" prop="expireTime">
          <el-date-picker
            v-model="form.expireTime"
            clearable
            placeholder="请选择代理该卡过期时间"
            :picker-options="pickerOptions"
            value-format="yyyy-MM-dd HH:mm:ss"
            type="datetime"
          >
          </el-date-picker>
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
  addAgentItem,
  delAgentItem,
  getAgentItem,
  grantableTemplate,
  listAgentItem,
  updateAgentItem,
} from "@/api/agent/agentItem";
import {listAgentUser} from "@/api/agent/agentUser";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {parseMoney} from "@/utils/my";

export default {
  name: "AgentItem",
  dicts: ["sys_template_type"],
  components: {
    Treeselect,
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
      // 代理卡类关联表格数据
      agentItemList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 备注时间范围
      daterangeExpireTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: this.$store.state.settings.pageSize,
        agentId: null,
        templateType: null,
        templateId: null,
        expireTime: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        agentId: [
          { required: true, message: "代理账号不能为空", trigger: "blur" },
        ],
        // templateType: [
        //   {
        //     required: true,
        //     message: "卡类别1充值卡2单码不能为空",
        //     trigger: "change",
        //   },
        // ],
        tid: [{ required: true, message: "代理卡类不能为空", trigger: "blur" }],
        agentPrice: [
          { required: true, message: "代理价格不能为空", trigger: "blur" },
        ],
      },
      // 代理用户树选项
      agentUserOptions: [],
      // 可代理卡类
      templateList: [],
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
            text: "1年后(360天)",
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() + 3600 * 1000 * 24 * 30 * 12);
              picker.$emit("pick", date);
            },
          },
        ],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询代理卡类关联列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeExpireTime && "" != this.daterangeExpireTime) {
        this.queryParams.params["beginExpireTime"] =
          this.daterangeExpireTime[0];
        this.queryParams.params["endExpireTime"] = this.daterangeExpireTime[1];
      }
      if (
        null != this.queryParams.agentName &&
        "" != this.queryParams.agentName
      ) {
        this.queryParams.params["agentName"] = this.queryParams.agentName;
      }
      listAgentItem(this.queryParams).then((response) => {
        this.agentItemList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
      this.getGrantableTemplate();
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
          user: { nickName: "根节点", userName: "不可选" },
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
        id: null,
        agentId: null,
        templateType: null,
        templateId: null,
        agentPrice: null,
        expireTime: null,
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
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.getTreeselect();
      this.getGrantableTemplate();
      this.open = true;
      this.title = "添加代理卡类";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.getTreeselect();
      this.getGrantableTemplate();
      const id = row.id || this.ids;
      getAgentItem(id).then((response) => {
        this.form = response.data;
        for (let item of this.templateList) {
          if (
            this.form.templateType == item.templateType &&
            this.form.templateId == item.templateId
          ) {
            this.form.tid = item.id;
            break;
          }
        }
        this.open = true;
        this.title = "修改代理卡类";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.agentId > 0) {
            if (this.form.tid != null) {
              this.form.templateId =
                this.templateList[this.form.tid].templateId;
              this.form.templateType =
                this.templateList[this.form.tid].templateType;
            }
            if (this.form.id != null) {
              updateAgentItem(this.form).then((response) => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addAgentItem(this.form).then((response) => {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              });
            }
          } else {
            this.$modal.msgError("代理不可选择根节点");
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal
        .confirm('是否确认删除代理卡类关联编号为"' + ids + '"的数据项？')
        .then(function () {
          return delAgentItem(ids);
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
        "agent/agentItem/export",
        {
          ...this.queryParams,
        },
        `agentItem_${new Date().getTime()}.xlsx`
      );
    },
    getGrantableTemplate() {
      grantableTemplate().then((response) => {
        this.templateList = [];
        let dataList = response.data;
        for (let data of dataList) {
          this.templateList.push({
            id: this.templateList.length,
            templateId: data.templateId,
            templateType: data.templateType,
            templateName: data.templateName,
            appName: data.appName,
            price: data.price,
          });
        }
      });
    },
    parseMoney(val) {
      return parseMoney(val);
    },
  },
};
</script>
