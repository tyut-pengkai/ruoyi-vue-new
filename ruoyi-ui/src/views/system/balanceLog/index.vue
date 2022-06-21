<template>
  <div class="app-container">
    <el-form
      v-show="showSearch"
      ref="queryForm"
      :inline="true"
      :model="queryParams"
      size="small"
    >
      <el-form-item label="明细所属用户 id" prop="userId">
        <el-input
          v-model="queryParams.userId"
          clearable
          placeholder="请输入明细所属用户 id"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="变动类型" prop="changeType">
        <el-select
          v-model="queryParams.changeType"
          clearable
          placeholder="请选择变动类型"
        >
          <el-option
            v-for="dict in dict.type.sys_balance_change_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="变动描述" prop="changeDesc">
        <el-input
          v-model="queryParams.changeDesc"
          clearable
          placeholder="请输入变动描述"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="关联订单记录ID" prop="saleOrderId">
        <el-input
          v-model="queryParams.saleOrderId"
          clearable
          placeholder="请输入关联订单记录ID"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="关联提现记录ID" prop="withdrawCashId">
        <el-input
          v-model="queryParams.withdrawCashId"
          clearable
          placeholder="请输入关联提现记录ID"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
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
          v-hasPermi="['system:balanceLog:remove']"
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
          icon="el-icon-delete"
          plain
          type="danger"
          size="mini"
          @click="handleClean"
          v-hasPermi="['system:balanceLog:remove']"
        >清空
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:balanceLog:export']"
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
      :data="balanceLogList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column align="center" type="selection" width="55"/>
      <el-table-column align="center" label="ID" prop="id"/>
      <el-table-column align="center" label="用户 id" prop="userId"/>
      <el-table-column
        align="center"
        label="金额来源用户"
        prop="sourceUserId"
      />
      <el-table-column
        align="center"
        label="可用金额变动"
        prop="changeAvailablePayAmount"
      >
        <template slot-scope="scope">
          <span>{{
              (scope.row.changeAvailablePayAmount > 0 ? "+" : "") +
              parseMoney(scope.row.changeAvailablePayAmount)
            }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="冻结金额变动"
        prop="changeFreezePayAmount"
      >
        <template slot-scope="scope">
          <span>{{
              (scope.row.changeFreezePayAmount > 0 ? "+" : "") +
              parseMoney(scope.row.changeFreezePayAmount)
            }}</span>
        </template>
      </el-table-column>
      <!-- <el-table-column
        label="变动可用赠送金额"
        align="center"
        prop="changeAvailableFreeAmount"
      />
      <el-table-column
        label="变动冻结赠送金额"
        align="center"
        prop="changeFreezeFreeAmount"
      />
      <el-table-column
        label="冻结赠送余额后"
        align="center"
        prop="freezeFreeAfter"
      />
      <el-table-column
        label="冻结赠送余额前"
        align="center"
        prop="freezeFreeBefore"
      />
      <el-table-column
        label="可用赠送余额后"
        align="center"
        prop="availableFreeAfter"
      />
      <el-table-column
        label="可用赠送余额前"
        align="center"
        prop="availableFreeBefore"
      /> -->
      <el-table-column
        align="center"
        label="可用余额(变动前)"
        prop="availablePayBefore"
      >
        <template slot-scope="scope">
          <span>{{ parseMoney(scope.row.availablePayBefore) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="可用余额(变动后)"
        prop="availablePayAfter"
      >
        <template slot-scope="scope">
          <span>{{ parseMoney(scope.row.availablePayAfter) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="冻结余额(变动前)"
        prop="freezePayBefore"
      >
        <template slot-scope="scope">
          <span>{{ parseMoney(scope.row.freezePayBefore) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="冻结余额(变动后)"
        prop="freezePayAfter"
      >
        <template slot-scope="scope">
          <span>{{ parseMoney(scope.row.freezePayAfter) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="变动类型" prop="changeType">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_balance_change_type"
            :value="scope.row.changeType"
          />
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        :show-overflow-tooltip="true"
        label="变动描述"
        prop="changeDesc"
      />
      <!-- <el-table-column
        align="center"
        label="关联订单记录ID"
        prop="saleOrderId"
      />
      <el-table-column
        align="center"
        label="关联提现记录ID"
        prop="withdrawCashId"
      /> -->
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
            v-hasPermi="['system:balanceLog:edit']"
            icon="el-icon-edit"
            size="mini"
            type="text"
            @click="handleUpdate(scope.row)"
          >详情
          </el-button>
          <!-- <el-button
            v-hasPermi="['system:balanceLog:remove']"
            icon="el-icon-delete"
            size="mini"
            type="text"
            @click="handleDelete(scope.row)"
            >删除
          </el-button> -->
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

    <!-- 添加或修改余额变动对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="800px">
      <el-form ref="form" :model="form" :rules="rules">
        <el-form-item label="明细所属用户 id" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入明细所属用户 id"/>
        </el-form-item>
        <el-form-item label="金额来源用户" prop="sourceUserId">
          <el-input
            v-model="form.sourceUserId"
            placeholder="请输入金额来源用户"
          />
        </el-form-item>
        <el-form-item label="变动可用充值金额" prop="changeAvailablePayAmount">
          <el-input
            v-model="form.changeAvailablePayAmount"
            placeholder="请输入变动可用充值金额"
          />
        </el-form-item>
        <el-form-item label="变动冻结充值金额" prop="changeFreezePayAmount">
          <el-input
            v-model="form.changeFreezePayAmount"
            placeholder="请输入变动冻结充值金额"
          />
        </el-form-item>
        <el-form-item label="变动类型" prop="changeType">
          <el-select v-model="form.changeType" placeholder="请选择变动类型">
            <el-option
              v-for="dict in dict.type.sys_balance_change_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <!-- <el-form-item label="变动可用赠送金额" prop="changeAvailableFreeAmount">
          <el-input
            v-model="form.changeAvailableFreeAmount"
            placeholder="请输入变动可用赠送金额"
          />
        </el-form-item>
        <el-form-item label="变动冻结赠送金额" prop="changeFreezeFreeAmount">
          <el-input
            v-model="form.changeFreezeFreeAmount"
            placeholder="请输入变动冻结赠送金额"
          />
        </el-form-item>
        <el-form-item label="冻结赠送余额后" prop="freezeFreeAfter">
          <el-input
            v-model="form.freezeFreeAfter"
            placeholder="请输入冻结赠送余额后"
          />
        </el-form-item>
        <el-form-item label="冻结赠送余额前" prop="freezeFreeBefore">
          <el-input
            v-model="form.freezeFreeBefore"
            placeholder="请输入冻结赠送余额前"
          />
        </el-form-item>
        <el-form-item label="可用赠送余额后" prop="availableFreeAfter">
          <el-input
            v-model="form.availableFreeAfter"
            placeholder="请输入可用赠送余额后"
          />
        </el-form-item>
        <el-form-item label="可用赠送余额前" prop="availableFreeBefore">
          <el-input
            v-model="form.availableFreeBefore"
            placeholder="请输入可用赠送余额前"
          />
        </el-form-item> -->
        <el-form-item label="冻结充值余额后" prop="freezePayAfter">
          <el-input
            v-model="form.freezePayAfter"
            placeholder="请输入冻结充值余额后"
          />
        </el-form-item>
        <el-form-item label="冻结充值余额前" prop="freezePayBefore">
          <el-input
            v-model="form.freezePayBefore"
            placeholder="请输入冻结充值余额前"
          />
        </el-form-item>
        <el-form-item label="可用充值余额后" prop="availablePayAfter">
          <el-input
            v-model="form.availablePayAfter"
            placeholder="请输入可用充值余额后"
          />
        </el-form-item>
        <el-form-item label="可用充值余额前" prop="availablePayBefore">
          <el-input
            v-model="form.availablePayBefore"
            placeholder="请输入可用充值余额前"
          />
        </el-form-item>
        <el-form-item label="变动描述" prop="changeDesc">
          <el-input v-model="form.changeDesc" placeholder="请输入变动描述"/>
        </el-form-item>
        <el-form-item label="关联订单记录ID" prop="saleOrderId">
          <el-input
            v-model="form.saleOrderId"
            placeholder="请输入关联订单记录ID"
          />
        </el-form-item>
        <el-form-item label="关联提现记录ID" prop="withdrawCashId">
          <el-input
            v-model="form.withdrawCashId"
            placeholder="请输入关联提现记录ID"
          />
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
  cleanBalancelog,
  delBalanceLog,
  getBalanceLog,
  listBalanceLog,
  updateBalanceLog,
} from "@/api/system/balanceLog";
import {parseMoney} from "@/utils/my";

export default {
  name: "BalanceLog",
  dicts: ["sys_balance_change_type"],
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
      // 余额变动表格数据
      balanceLogList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        changeType: null,
        changeDesc: null,
        saleOrderId: null,
        withdrawCashId: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询余额变动列表 */
    getList() {
      this.loading = true;
      listBalanceLog(this.queryParams).then((response) => {
        this.balanceLogList = response.rows;
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
        userId: null,
        sourceUserId: null,
        changeAvailablePayAmount: null,
        changeFreezePayAmount: null,
        changeType: null,
        changeAvailableFreeAmount: null,
        changeFreezeFreeAmount: null,
        freezeFreeAfter: null,
        freezeFreeBefore: null,
        availableFreeAfter: null,
        availableFreeBefore: null,
        freezePayAfter: null,
        freezePayBefore: null,
        availablePayAfter: null,
        availablePayBefore: null,
        changeDesc: null,
        saleOrderId: null,
        withdrawCashId: null,
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
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getBalanceLog(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "余额变动详情";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateBalanceLog(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
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
        .confirm('是否确认删除余额变动编号为"' + ids + '"的数据项？')
        .then(function () {
          return delBalanceLog(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {
        });
    },
    /** 清空按钮操作 */
    handleClean() {
      this.$modal
        .confirm("是否确认清空所有金额变动日志数据项？")
        .then(function () {
          return cleanBalancelog();
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("清空成功");
        })
        .catch(() => {
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "system/balanceLog/export",
        {
          ...this.queryParams,
        },
        `balanceLog_${new Date().getTime()}.xlsx`
      );
    },
    parseMoney(val) {
      return parseMoney(val);
    },
  },
};
</script>
