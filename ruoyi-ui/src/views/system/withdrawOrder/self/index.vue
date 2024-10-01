<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="订单编号" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          placeholder="请输入提现订单编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="交易编号" prop="tradeNo">
        <el-input
          v-model="queryParams.tradeNo"
          placeholder="请输入交易编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="提现账号" prop="userId">
        <el-input
          v-model="queryParams.userName"
          placeholder="请输入提现账号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="申请金额" prop="applyFee">
        <el-input
          v-model="queryParams.applyFee"
          placeholder="请输入提现申请金额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="到账金额(预计)" prop="actualFee">
        <el-input
          v-model="queryParams.actualFee"
          placeholder="请输入实际提现金额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="手续费" prop="handlingFee">
        <el-input
          v-model="queryParams.handlingFee"
          placeholder="请输入提现手续费"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!--      <el-form-item label="是否人工" prop="manualTransfer">-->
      <!--        <el-select v-model="queryParams.manualTransfer" placeholder="请选择是否人工转账" clearable>-->
      <!--          <el-option-->
      <!--            v-for="dict in dict.type.sys_yes_no"-->
      <!--            :key="dict.value"-->
      <!--            :label="dict.label"-->
      <!--            :value="dict.value"-->
      <!--          />-->
      <!--        </el-select>-->
      <!--      </el-form-item>-->
      <!--      <el-form-item label="错误代码" prop="errorCode">-->
      <!--        <el-input-->
      <!--          v-model="queryParams.errorCode"-->
      <!--          placeholder="请输入错误代码"-->
      <!--          clearable-->
      <!--          @keyup.enter.native="handleQuery"-->
      <!--        />-->
      <!--      </el-form-item>-->
      <el-form-item label="收款平台" prop="receiveMethod">
        <el-select v-model="queryParams.receiveMethod" placeholder="请选择收款平台" clearable>
          <el-option
            v-for="dict in dict.type.sys_receive_method"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="收款账号" prop="receiveAccount">
        <el-input
          v-model="queryParams.receiveAccount"
          placeholder="请输入收款账号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="提现状态" prop="withdrawStatus">
        <el-select v-model="queryParams.withdrawStatus" placeholder="请选择提现状态" clearable>
          <el-option
            v-for="dict in dict.type.sys_cash_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="失败描述" prop="errorMessage">
        <el-input
          v-model="queryParams.errorMessage"
          placeholder="请输入交易失败描述"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="交易时间">
        <el-date-picker
          v-model="daterangeTradeTime"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!--      <el-col :span="1.5">-->
      <!--        <el-button-->
      <!--          type="primary"-->
      <!--          plain-->
      <!--          icon="el-icon-plus"-->
      <!--          size="mini"-->
      <!--          @click="handleAdd"-->
      <!--          v-hasPermi="['system:withdrawOrder:add']"-->
      <!--        >新增</el-button>-->
      <!--      </el-col>-->
      <!--      <el-col :span="1.5">-->
      <!--        <el-button-->
      <!--          type="success"-->
      <!--          plain-->
      <!--          icon="el-icon-edit"-->
      <!--          size="mini"-->
      <!--          :disabled="single"-->
      <!--          @click="handleUpdate"-->
      <!--          v-hasPermi="['system:withdrawOrder:edit']"-->
      <!--        >修改</el-button>-->
      <!--      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:withdrawOrder:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:withdrawOrder:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="withdrawOrderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="编号" align="center" prop="id" />
      <el-table-column label="订单编号" align="center" prop="orderNo" :show-overflow-tooltip="true"/>
      <!--      <el-table-column label="交易编号" align="center" prop="tradeNo" />-->
      <el-table-column label="提现账号" align="center" prop="userId" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span>{{ scope.row.user.nickName }}({{ scope.row.user.userName }})</span>
        </template>
      </el-table-column>
      <el-table-column label="申请金额" align="center" prop="applyFee">
        <template slot-scope="scope">
          <span>{{ parseMoney(scope.row.applyFee) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="到账金额(预计)" align="center" prop="actualFee">
        <template slot-scope="scope">
          <span>{{ parseMoney(scope.row.actualFee) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="手续费" align="center" prop="handlingFee">
        <template slot-scope="scope">
          <span>{{ parseMoney(scope.row.handlingFee) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="收款平台" align="center" prop="receiveMethod">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_receive_method" :value="scope.row.receiveMethod"/>
        </template>
      </el-table-column>
      <el-table-column label="收款账号" align="center" prop="receiveAccount" show-overflow-tooltip/>
      <!--      <el-table-column label="是否人工" align="center" prop="manualTransfer">-->
      <!--        <template slot-scope="scope">-->
      <!--          <dict-tag :options="dict.type.sys_yes_no" :value="scope.row.manualTransfer"/>-->
      <!--        </template>-->
      <!--      </el-table-column>-->
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="提现状态" align="center" prop="withdrawStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_cash_status" :value="scope.row.withdrawStatus"/>
        </template>
      </el-table-column>
      <!--      <el-table-column label="错误代码" align="center" prop="errorCode" />-->
      <el-table-column label="失败描述" align="center" prop="errorMessage" :show-overflow-tooltip="true"/>
      <el-table-column label="交易时间" align="center" prop="tradeTime" width="180" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.tradeTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="300">
        <template slot-scope="scope">
          <el-button
            icon="el-icon-view"
            size="mini"
            type="text"
            @click="handleUpdate(scope.row)"
          >详情
          </el-button>
          <el-button
            icon="el-icon-delete"
            size="mini"
            type="text"
            @click="handleCancel(scope.row)"
            :disabled="scope.row.withdrawStatus !== '0'"
          >撤销
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:withdrawOrder:remove']"
          >删除</el-button>
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

    <!-- 添加或修改提现记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules">
        <el-form-item>
          <el-col :span="12">
            <el-form-item label="订单编号" prop="orderNo">
              {{ form.orderNo }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="提现账号" prop="userId">
              <span>{{ form.userId ? form.user.nickName + "(" + form.user.userName + ")" : "[未登录]" }}</span>
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item>
          <el-col :span="12">
            <el-form-item label="申请金额" prop="applyFee">
              <span>{{ parseMoney(form.applyFee) }}元 </span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手续费" prop="handlingFee">
              <span>{{ parseMoney(form.handlingFee) }}元 </span>
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item>
          <el-col :span="12">
            <!--            <el-form-item label="收款平台" prop="receiveMethod">-->
            <!--              <el-select v-model="form.receiveMethod" placeholder="请选择收款平台">-->
            <!--                <el-option-->
            <!--                  v-for="dict in dict.type.sys_receive_method"-->
            <!--                  :key="dict.value"-->
            <!--                  :label="dict.label"-->
            <!--                  :value="dict.value"-->
            <!--                ></el-option>-->
            <!--              </el-select>-->
            <!--            </el-form-item>-->
            <el-form-item label="收款平台" prop="receiveMethod">
              <dict-tag :options="dict.type.sys_receive_method" :value="form.receiveMethod" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="收款账号" prop="receiveAccount">
              {{ form.receiveAccount }}
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item>
          <el-col :span="12">
            <el-form-item label="到账金额(应打款金额)" prop="actualFee">
              <span>{{ parseMoney(form.actualFee) }}元 </span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="真实姓名" prop="realName">
              {{ form.realName }}
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item>
          <el-col :span="12">
            <el-form-item label="提现状态" prop="withdrawStatus">
<!--              <el-select v-model="form.withdrawStatus" placeholder="请选择提现状态" :disabled="withdrawStatusOld !== '0'">-->
<!--                <el-option-->
<!--                  v-for="dict in dict.type.sys_cash_status"-->
<!--                  :key="dict.value"-->
<!--                  :label="dict.label"-->
<!--                  :value="dict.value"-->
<!--                ></el-option>-->
<!--              </el-select>-->
              <dict-tag :options="dict.type.sys_cash_status" :value="form.withdrawStatus" />
            </el-form-item>
          </el-col>
          <el-col :span="12">

          </el-col>
        </el-form-item>
        <div v-if="form.withdrawStatus === '1'">
          <el-form-item label="交易号" prop="tradeNo">
<!--            <el-input v-model="form.tradeNo" placeholder="请输入交易号"/>-->
            {{ form.tradeNo }}
          </el-form-item>
        </div>
        <div v-if="form.withdrawStatus === '2'">
          <el-form-item label="失败描述" prop="errorMessage">
<!--            <el-input v-model="form.errorMessage" placeholder="请输入交易失败描述"/>-->
<!--            <el-button size="mini" round v-for="(reason, index) in reasonList" :key="index" @click="fillReason(reason)">{{ reason }}</el-button>-->
            {{ form.errorMessage }}
          </el-form-item>
        </div>
        <!--            <el-form-item label="错误代码" prop="errorCode">-->
        <!--              <el-input v-model="form.errorCode" placeholder="请输入错误代码" />-->
        <!--            </el-form-item>-->
<!--        <el-form-item label="备注" prop="remark">-->
<!--          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />-->
<!--        </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
<!--        <el-button type="primary" @click="submitForm">确 定</el-button>-->
        <el-button @click="cancel">关 闭</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { listWithdrawOrderSelf, getWithdrawOrderSelf, delWithdrawOrder, cancelWithdrawOrder, addWithdrawOrder, updateWithdrawOrder } from "@/api/system/withdrawOrder";
import {parseMoney} from "@/utils/my";

export default {
  name: "WithdrawOrder",
  dicts: ['sys_yes_no', 'sys_receive_method', 'sys_cash_status'],
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
      // 提现记录表格数据
      withdrawOrderList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 删除标志时间范围
      daterangeTradeTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        orderNo: null,
        handlingFee: null,
        applyFee: null,
        actualFee: null,
        withdrawStatus: null,
        manualTransfer: null,
        tradeTime: null,
        tradeNo: null,
        errorCode: null,
        errorMessage: null,
        receiveMethod: null,
        receiveAccount: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        // userId: [
        //   { required: true, message: "提现账号不能为空", trigger: "blur" }
        // ],
        // orderNo: [
        //   { required: true, message: "提现订单编号不能为空", trigger: "blur" }
        // ],
        // handlingFee: [
        //   { required: true, message: "提现手续费不能为空", trigger: "blur" }
        // ],
        // applyFee: [
        //   { required: true, message: "提现申请金额不能为空", trigger: "blur" }
        // ],
        // actualFee: [
        //   { required: true, message: "实际提现金额不能为空", trigger: "blur" }
        // ],
        withdrawStatus: [
          { required: true, message: "提现状态不能为空", trigger: "change" }
        ],
        // manualTransfer: [
        //   { required: true, message: "是否人工转账不能为空", trigger: "change" }
        // ],
        // receiveMethod: [
        //   { required: true, message: "收款平台不能为空", trigger: "change" }
        // ],
        // receiveAccount: [
        //   { required: true, message: "收款账号不能为空", trigger: "blur" }
        // ],
      },
      reasonList: [
        '提现账户未实名认证',
        '提现账户已被冻结',
        '收款账户信息不一致',
        '提现次数已达上限',
        '提现时间不在允许范围内',
        '第三方支付平台故障',
      ],
      withdrawStatusOld: '0',
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询提现记录列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeTradeTime && '' !== this.daterangeTradeTime) {
        this.queryParams.params["beginTradeTime"] = this.daterangeTradeTime[0];
        this.queryParams.params["endTradeTime"] = this.daterangeTradeTime[1];
      }
      listWithdrawOrderSelf(this.queryParams).then(response => {
        this.withdrawOrderList = response.rows;
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
        orderNo: null,
        handlingFee: null,
        applyFee: null,
        actualFee: null,
        withdrawStatus: null,
        manualTransfer: null,
        tradeTime: null,
        tradeNo: null,
        errorCode: null,
        errorMessage: null,
        receiveMethod: null,
        receiveAccount: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        delFlag: null
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
      this.daterangeTradeTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加提现记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getWithdrawOrderSelf(id).then(response => {
        this.form = response.data;
        this.withdrawStatusOld = this.form.withdrawStatus;
        this.open = true;
        this.title = "查看提现详情";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            if(this.form.withdrawStatus !== '0') {
              this.$modal.confirm('变更提现状态后系统将实时调整对应用户账户余额，此操作无法撤销，是否继续？').then(() => {
                updateWithdrawOrder(this.form).then(response => {
                  this.$modal.msgSuccess("变更成功");
                  this.open = false;
                  this.getList();
                });
              }).catch(() => {
              });
            } else {
              this.open = false;
            }
          } else {
            addWithdrawOrder(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleCancel(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认撤销提现记录编号为"' + ids + '"的数据项？').then(function() {
        return cancelWithdrawOrder(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("撤销成功");
      }).catch(() => {
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除提现记录编号为"' + ids + '"的数据项？').then(function() {
        return delWithdrawOrder(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/withdrawOrder/export', {
        ...this.queryParams
      }, `withdrawOrder_${new Date().getTime()}.xlsx`)
    },
    parseMoney(val) {
      return parseMoney(val);
    },
    fillReason(reason) {
      this.form.errorMessage = reason; // 追加理由并换行
    }
  }
};
</script>
