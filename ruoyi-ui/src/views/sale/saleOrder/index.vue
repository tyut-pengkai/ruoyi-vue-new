<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="订单编号" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          placeholder="请输入订单编号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户ID"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="支付方式" prop="payMode">
        <el-input
          v-model="queryParams.payMode"
          placeholder="请输入支付方式"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="联系方式" prop="contact">
        <el-input
          v-model="queryParams.contact"
          placeholder="请输入联系方式"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="查询密码" prop="queryPass">
        <el-input
          v-model="queryParams.queryPass"
          placeholder="请输入查询密码"
          clearable
          size="small"
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
          v-hasPermi="['sale:saleOrder:add']"
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
          v-hasPermi="['sale:saleOrder:edit']"
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
          v-hasPermi="['sale:saleOrder:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['sale:saleOrder:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="saleOrderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="订单ID" align="center" prop="orderId" />
      <el-table-column label="订单编号" align="center" prop="orderNo" />
      <el-table-column label="用户ID" align="center" prop="userId" />
      <el-table-column label="应付金额" align="center" prop="actualFee" />
      <el-table-column label="总价格" align="center" prop="totalFee" />
      <el-table-column label="折扣规则" align="center" prop="discountRule" />
      <el-table-column label="折扣金额" align="center" prop="discountFee" />
      <el-table-column label="支付方式" align="center" prop="payMode" />
      <el-table-column label="0未付款1已付款2交易关闭3交易成功4交易结束" align="center" prop="status" />
      <el-table-column label="联系方式" align="center" prop="contact" />
      <el-table-column label="查询密码" align="center" prop="queryPass" />
      <el-table-column label="支付时间" align="center" prop="paymentTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.paymentTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="发货时间" align="center" prop="deliveryTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.deliveryTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="订单完成时间" align="center" prop="finishTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.finishTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="订单关闭时间" align="center" prop="closeTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.closeTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['sale:saleOrder:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['sale:saleOrder:remove']"
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

    <!-- 添加或修改销售订单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="订单编号" prop="orderNo">
          <el-input v-model="form.orderNo" placeholder="请输入订单编号" />
        </el-form-item>
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户ID" />
        </el-form-item>
        <el-form-item label="应付金额" prop="actualFee">
          <el-input v-model="form.actualFee" placeholder="请输入应付金额" />
        </el-form-item>
        <el-form-item label="总价格" prop="totalFee">
          <el-input v-model="form.totalFee" placeholder="请输入总价格" />
        </el-form-item>
        <el-form-item label="折扣规则" prop="discountRule">
          <el-input v-model="form.discountRule" placeholder="请输入折扣规则" />
        </el-form-item>
        <el-form-item label="折扣金额" prop="discountFee">
          <el-input v-model="form.discountFee" placeholder="请输入折扣金额" />
        </el-form-item>
        <el-form-item label="支付方式" prop="payMode">
          <el-input v-model="form.payMode" placeholder="请输入支付方式" />
        </el-form-item>
        <el-form-item label="联系方式" prop="contact">
          <el-input v-model="form.contact" placeholder="请输入联系方式" />
        </el-form-item>
        <el-form-item label="查询密码" prop="queryPass">
          <el-input v-model="form.queryPass" placeholder="请输入查询密码" />
        </el-form-item>
        <el-form-item label="支付时间" prop="paymentTime">
          <el-date-picker clearable size="small"
            v-model="form.paymentTime"
            type="date"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="选择支付时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="发货时间" prop="deliveryTime">
          <el-date-picker clearable size="small"
            v-model="form.deliveryTime"
            type="date"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="选择发货时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="订单完成时间" prop="finishTime">
          <el-date-picker clearable size="small"
            v-model="form.finishTime"
            type="date"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="选择订单完成时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="订单关闭时间" prop="closeTime">
          <el-date-picker clearable size="small"
            v-model="form.closeTime"
            type="date"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="选择订单关闭时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-divider content-position="center">销售订单详情信息</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddSysSaleOrderItem">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteSysSaleOrderItem">删除</el-button>
          </el-col>
        </el-row>
        <el-table :data="sysSaleOrderItemList" :row-class-name="rowSysSaleOrderItemIndex" @selection-change="handleSysSaleOrderItemSelectionChange" ref="sysSaleOrderItem">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="1卡类2登录码类" prop="templateType">
            <template slot-scope="scope">
              <el-input v-model="scope.row.templateType" placeholder="请输入1卡类2登录码类" />
            </template>
          </el-table-column>
          <el-table-column label="模板ID" prop="templateId">
            <template slot-scope="scope">
              <el-input v-model="scope.row.templateId" placeholder="请输入模板ID" />
            </template>
          </el-table-column>
          <el-table-column label="购买数量" prop="num">
            <template slot-scope="scope">
              <el-input v-model="scope.row.num" placeholder="请输入购买数量" />
            </template>
          </el-table-column>
          <el-table-column label="商品标题" prop="title">
            <template slot-scope="scope">
              <el-input v-model="scope.row.title" placeholder="请输入商品标题" />
            </template>
          </el-table-column>
          <el-table-column label="商品单价" prop="price">
            <template slot-scope="scope">
              <el-input v-model="scope.row.price" placeholder="请输入商品单价" />
            </template>
          </el-table-column>
          <el-table-column label="总价格" prop="totalFee">
            <template slot-scope="scope">
              <el-input v-model="scope.row.totalFee" placeholder="请输入总价格" />
            </template>
          </el-table-column>
          <el-table-column label="折扣规则" prop="discountRule">
            <template slot-scope="scope">
              <el-input v-model="scope.row.discountRule" placeholder="请输入折扣规则" />
            </template>
          </el-table-column>
          <el-table-column label="折扣金额" prop="discountFee">
            <template slot-scope="scope">
              <el-input v-model="scope.row.discountFee" placeholder="请输入折扣金额" />
            </template>
          </el-table-column>
          <el-table-column label="应付金额" prop="actualFee">
            <template slot-scope="scope">
              <el-input v-model="scope.row.actualFee" placeholder="请输入应付金额" />
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listSaleOrder, getSaleOrder, delSaleOrder, addSaleOrder, updateSaleOrder } from "@/api/sale/saleOrder";

export default {
  name: "SaleOrder",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedSysSaleOrderItem: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 销售订单表格数据
      saleOrderList: [],
      // 销售订单详情表格数据
      sysSaleOrderItemList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 应付金额时间范围
      daterangePaymentTime: [],
      // 应付金额时间范围
      daterangeDeliveryTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNo: null,
        userId: null,
        payMode: null,
        status: null,
        contact: null,
        queryPass: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询销售订单列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangePaymentTime && '' != this.daterangePaymentTime) {
        this.queryParams.params["beginPaymentTime"] = this.daterangePaymentTime[0];
        this.queryParams.params["endPaymentTime"] = this.daterangePaymentTime[1];
      }
      if (null != this.daterangeDeliveryTime && '' != this.daterangeDeliveryTime) {
        this.queryParams.params["beginDeliveryTime"] = this.daterangeDeliveryTime[0];
        this.queryParams.params["endDeliveryTime"] = this.daterangeDeliveryTime[1];
      }
      listSaleOrder(this.queryParams).then(response => {
        this.saleOrderList = response.rows;
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
        orderId: null,
        orderNo: null,
        userId: null,
        actualFee: null,
        totalFee: null,
        discountRule: null,
        discountFee: null,
        payMode: null,
        status: null,
        contact: null,
        queryPass: null,
        createBy: null,
        createTime: null,
        paymentTime: null,
        deliveryTime: null,
        finishTime: null,
        closeTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
      };
      this.sysSaleOrderItemList = [];
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.daterangePaymentTime = [];
      this.daterangeDeliveryTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.orderId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加销售订单";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const orderId = row.orderId || this.ids
      getSaleOrder(orderId).then(response => {
        this.form = response.data;
        this.sysSaleOrderItemList = response.data.sysSaleOrderItemList;
        this.open = true;
        this.title = "修改销售订单";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.sysSaleOrderItemList = this.sysSaleOrderItemList;
          if (this.form.orderId != null) {
            updateSaleOrder(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addSaleOrder(this.form).then(response => {
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
      const orderIds = row.orderId || this.ids;
      this.$modal.confirm('是否确认删除销售订单编号为"' + orderIds + '"的数据项？').then(function() {
        return delSaleOrder(orderIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
	/** 销售订单详情序号 */
    rowSysSaleOrderItemIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
    /** 销售订单详情添加按钮操作 */
    handleAddSysSaleOrderItem() {
      let obj = {};
      obj.templateType = "";
      obj.templateId = "";
      obj.num = "";
      obj.title = "";
      obj.price = "";
      obj.totalFee = "";
      obj.discountRule = "";
      obj.discountFee = "";
      obj.actualFee = "";
      this.sysSaleOrderItemList.push(obj);
    },
    /** 销售订单详情删除按钮操作 */
    handleDeleteSysSaleOrderItem() {
      if (this.checkedSysSaleOrderItem.length == 0) {
        this.$modal.msgError("请先选择要删除的销售订单详情数据");
      } else {
        const sysSaleOrderItemList = this.sysSaleOrderItemList;
        const checkedSysSaleOrderItem = this.checkedSysSaleOrderItem;
        this.sysSaleOrderItemList = sysSaleOrderItemList.filter(function(item) {
          return checkedSysSaleOrderItem.indexOf(item.index) == -1
        });
      }
    },
    /** 复选框选中数据 */
    handleSysSaleOrderItemSelectionChange(selection) {
      this.checkedSysSaleOrderItem = selection.map(item => item.index)
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('sale/saleOrder/export', {
        ...this.queryParams
      }, `saleOrder_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
