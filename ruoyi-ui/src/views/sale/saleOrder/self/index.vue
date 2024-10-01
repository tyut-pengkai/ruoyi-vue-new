<template>
  <div class="app-container">
    <el-form
      v-show="showSearch"
      ref="queryForm"
      :inline="true"
      :model="queryParams"
      label-width="68px"
    >
      <el-form-item label="订单编号" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          clearable
          placeholder="请输入订单编号"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="交易编号" prop="tradeNo">
        <el-input
          v-model="queryParams.tradeNo"
          clearable
          placeholder="请输入交易编号"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户ID"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <!-- <el-form-item label="支付方式" prop="payMode">
        <el-input
          v-model="queryParams.payMode"
          placeholder="请输入支付方式"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="联系方式" prop="contact">
        <el-input
          v-model="queryParams.contact"
          clearable
          placeholder="请输入联系方式"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="查询密码" prop="queryPass">-->
<!--        <el-input-->
<!--          v-model="queryParams.queryPass"-->
<!--          clearable-->
<!--          placeholder="请输入查询密码"-->
<!--          size="small"-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
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
      <!-- <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['sale:saleOrder:add']"
        >新增
        </el-button
        >
      </el-col> -->
      <!-- <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['sale:saleOrder:edit']"
        >修改
        </el-button
        >
      </el-col> -->
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['sale:saleOrder:remove']"
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
          v-hasPermi="['sale:saleOrder:export']"
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
      :data="saleOrderList"
      :expand-row-keys="expands"
      row-key="orderId"
      @row-click="handleRowClick"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="expand">
        <template slot-scope="scope">
          <el-form label-position="left">
            <el-form-item>
              <el-col :span="4"> -</el-col>
              <el-col :span="5">
                <el-form-item label="订单编号">
                  <span>{{ scope.row.orderNo }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item label="支付方式">
                  <dict-tag
                    :options="dict.type.pay_mode"
                    :value="scope.row.payMode"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item label="支付交易号">
                  <span>{{ scope.row.tradeNo }}</span>
                </el-form-item>
              </el-col>
              <!-- <el-col :span="5">
                <el-form-item label="零售价格">
                  <span>{{ parseMoney(scope.row.price) }}元 </span>
                </el-form-item>
              </el-col> -->
            </el-form-item>
            <el-form-item>
              <el-col :span="4"> -</el-col>
              <el-col :span="5">
                <el-form-item label="下单账号">
                  <span>{{
                    scope.row.userId ? scope.row.userId : "[未登录]"
                  }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item label="联系方式">
                  <span>{{ scope.row.contact }}</span>
                </el-form-item>
              </el-col>
<!--              <el-col :span="5">-->
<!--                <el-form-item label="查询密码">-->
<!--                  <span>{{ scope.row.queryPass }}</span>-->
<!--                </el-form-item>-->
<!--              </el-col>-->
              <!-- <el-col :span="5">
                <el-form-item label="面值">
                  <span>{{
                    parseSeconds(scope.row.app.billType, scope.row.quota)
                  }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item label="零售价格">
                  <span>{{ parseMoney(scope.row.price) }}元 </span>
                </el-form-item>
              </el-col> -->
            </el-form-item>
            <el-form-item>
              <el-col :span="4"> -</el-col>
              <el-col :span="5">
                <el-form-item label="支付时间">
                  <span>{{ parseTime(scope.row.paymentTime) }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item label="完成时间">
                  <span>{{ parseTime(scope.row.finishTime) }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item label="关闭时间">
                  <span>{{ parseTime(scope.row.closeTime) }}</span>
                </el-form-item>
              </el-col>
              <!-- <el-col :span="5">
                <el-form-item label="充值规则">
                  <dict-tag
                    :options="dict.type.sys_charge_rule"
                    :value="scope.row.chargeRule"
                  />
                </el-form-item>
              </el-col> -->
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column align="center" type="selection" width="55" />
      <!-- <el-table-column align="center" label="" type="index"/> -->
      <el-table-column align="center" label="编号" prop="orderId" />
      <!-- <el-table-column label="订单ID" align="center" prop="orderId" /> -->
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="订单编号"
        prop="orderNo"
      />
      <el-table-column align="center" label="订单类型" prop="orderType">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_order_type"
            :value="scope.row.orderType"
          />
        </template>
      </el-table-column>
      <!-- <el-table-column label="用户ID" align="center" prop="userId" /> -->
      <el-table-column align="center" label="总价格" prop="totalFee">
        <template slot-scope="scope">
          <span>{{ parseMoney(scope.row.totalFee) }}</span>
        </template>
      </el-table-column>
      <!-- <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="折扣规则"
        prop="discountRule"
      /> -->
      <el-table-column align="center" label="折扣金额" prop="discountFee">
        <template slot-scope="scope">
          <span>{{ parseMoney(scope.row.discountFee) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="应付金额" prop="actualFee">
        <template slot-scope="scope">
          <span>{{ parseMoney(scope.row.actualFee) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="支付方式" prop="payMode">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.pay_mode" :value="scope.row.payMode" />
        </template>
      </el-table-column>
      <el-table-column align="center" label="订单状态" prop="status">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sale_order_status"
            :value="scope.row.status"
          />
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="创建时间"
        prop="createTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <!-- <el-table-column label="联系方式" align="center" prop="contact" />
      <el-table-column label="查询密码" align="center" prop="queryPass" /> -->
      <!-- <el-table-column
        label="支付时间"
        align="center"
        prop="paymentTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.paymentTime) }}</span>
        </template>
      </el-table-column> -->
      <!-- <el-table-column
        label="发货时间"
        align="center"
        prop="deliveryTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.deliveryTime) }}</span>
        </template>
      </el-table-column> -->
      <!-- <el-table-column
        label="完成时间"
        align="center"
        prop="finishTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.finishTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="关闭时间"
        align="center"
        prop="closeTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.closeTime) }}</span>
        </template>
      </el-table-column> -->
      <!-- <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="备注"
        prop="remark"
      /> -->
      <el-table-column
        align="center"
        class-name="small-padding fixed-width"
        label="操作"
      >
        <template slot-scope="scope">
          <el-button
            icon="el-icon-view"
            size="mini"
            type="text"
            @click="handleUpdate(scope.row)"
            >详情
          </el-button>
          <el-button
            v-hasPermi="['sale:saleOrder:edit']"
            :disabled="
              scope.row.status != '0' &&
              scope.row.status != '1' &&
              scope.row.status != '2'
            "
            icon="el-icon-sell"
            size="mini"
            type="text"
            @click="handleDelivery(scope.row)"
            >手动发货
          </el-button>
          <el-button
            v-hasPermi="['sale:saleOrder:remove']"
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

    <!-- 添加或修改销售订单对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="800px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item>
          <el-col :span="12">
            <el-form-item label="订单编号" prop="orderNo">
              {{ form.orderNo }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="下单账号" prop="userId">
              <span>{{
                form.userId
                  ? form.user.nickName + "(" + form.user.userName + ")"
                  : "[未登录]"
              }}</span>
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item>
          <el-col :span="12">
            <el-form-item label="下单时间" prop="createTime">
              {{ form.createTime }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="订单状态" prop="status">
              <dict-tag
                :options="dict.type.sale_order_status"
                :value="form.status"
              />
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item>
          <el-col :span="12">
            <el-form-item label="总价格" prop="totalFee">
              <span>{{ parseMoney(form.totalFee) }}元 </span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="应付金额" prop="actualFee">
              <span>{{ parseMoney(form.actualFee) }}元 </span>
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item>
          <el-col :span="12">
            <el-form-item label="折扣规则" prop="discountRule">
              {{ form.discountRule }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="折扣金额" prop="discountFee">
              <span>{{ parseMoney(form.discountFee) }}元 </span>
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item>
          <el-col :span="12">
            <el-form-item label="支付方式" prop="payMode">
              <dict-tag :options="dict.type.pay_mode" :value="form.payMode" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="发货方式"
              label-width="93px"
              prop="manualDelivery"
            >
              <dict-tag
                :options="dict.type.delivery_type"
                :value="form.manualDelivery"
              />
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item>
          <el-col :span="24">
            <el-form-item label="支付交易号" label-width="93px" prop="tradeNo">
              <span>{{ form.tradeNo }}</span>
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item>
          <el-col :span="12">
            <el-form-item label="联系方式" prop="contact">
              <el-input v-model="form.contact" placeholder="请输入联系方式" />
            </el-form-item>
          </el-col>
<!--          <el-col :span="12">-->
<!--            <el-form-item label="查询密码" prop="queryPass">-->
<!--              <el-input v-model="form.queryPass" placeholder="请输入查询密码" />-->
<!--            </el-form-item>-->
<!--          </el-col>-->
        </el-form-item>
        <el-form-item>
          <el-col :span="12">
            <el-form-item label="支付时间" prop="paymentTime">
              {{ form.paymentTime }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发货时间" prop="deliveryTime">
              {{ form.deliveryTime }}
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item>
          <el-col :span="12">
            <el-form-item label="完成时间" prop="finishTime">
              {{ form.finishTime }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="关闭时间" prop="closeTime">
              {{ form.closeTime }}
            </el-form-item>
          </el-col>
        </el-form-item>
        <!-- <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form.remark"
            placeholder="请输入内容"
            type="textarea"
          />
        </el-form-item> -->
        <el-divider content-position="center">销售订单详情信息</el-divider>
        <!-- <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              icon="el-icon-plus"
              size="mini"
              type="primary"
              @click="handleAddSysSaleOrderItem"
            >添加
            </el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button
              icon="el-icon-delete"
              size="mini"
              type="danger"
              @click="handleDeleteSysSaleOrderItem"
            >删除
            </el-button
            >
          </el-col>
        </el-row> -->
        <!-- <el-table
          ref="sysSaleOrderItem"
          :data="sysSaleOrderItemList"
          :row-class-name="rowSysSaleOrderItemIndex"
          @selection-change="handleSysSaleOrderItemSelectionChange"
        > -->
        <el-table ref="sysSaleOrderItem" :data="sysSaleOrderItemList">
          <el-table-column type="expand">
            <template slot-scope="item">
              <!-- <div v-for="(scope, index) in item.row.goodsList" :key="index">
                {{index}}
              </div> -->
              <el-table :data="item.row.goodsList" max-height="300px">
                <el-table-column align="center" label="" type="index" />
                <el-table-column
                  :label="
                    form.orderType == 1
                      ? item.row.templateType == 1
                        ? '充值卡'
                        : '登录码'
                      : '余额充值'
                  "
                >
                  <template slot-scope="scope">
                    {{ scope.row.cardNo }}
                  </template>
                </el-table-column>
                <!-- <el-table-column label="充值密码" v-if="item.row.templateType == '1'">
                  <template slot-scope="scope">
                    {{ scope.row.cardPass }}
                  </template>
                </el-table-column> -->
                <!-- <el-table-column label="过期时间">
                  <template slot-scope="scope">
                    {{ scope.row.expireTime }}
                  </template>
                </el-table-column> -->
                <el-table-column
                  v-if="item.row.templateType == '1'"
                  label="是否已用"
                >
                  <template slot-scope="scope">
                    <dict-tag
                      :options="dict.type.sys_yes_no"
                      :value="scope.row.isCharged"
                    />
                  </template>
                </el-table-column>
              </el-table>
            </template>
          </el-table-column>
          <!-- <el-table-column type="selection" width="50" align="center"/> -->
          <!-- <el-table-column
            align="center"
            label="序号"
            type="index"
          /> -->
          <el-table-column label="商品类别" prop="templateType">
            <template slot-scope="scope">
              {{
                form.orderType == 1
                  ? scope.row.templateType == 1
                    ? "充值卡"
                    : "登录码"
                  : "余额充值"
              }}
            </template>
          </el-table-column>
          <!-- <el-table-column label="卡类ID" prop="templateId">
            <template slot-scope="scope">
              <el-input
                v-model="scope.row.templateId"
                placeholder="请输入卡类ID"
              />
            </template>
          </el-table-column> -->
          <el-table-column
            :show-overflow-tooltip="true"
            label="商品标题"
            prop="title"
          >
            <template slot-scope="scope">
              {{ scope.row.title }}
            </template>
          </el-table-column>
          <el-table-column label="商品单价" prop="price">
            <template slot-scope="scope">
              {{ parseMoney(scope.row.price) }}元
            </template>
          </el-table-column>
          <el-table-column label="购买数量" prop="num">
            <template slot-scope="scope">
              {{ scope.row.num }}
            </template>
          </el-table-column>
          <el-table-column label="总价格" prop="totalFee">
            <template slot-scope="scope">
              {{ parseMoney(scope.row.totalFee) }}元
            </template>
          </el-table-column>
          <el-table-column label="折扣规则" prop="discountRule">
            <template slot-scope="scope">
              {{ scope.row.discountRule }}
            </template>
          </el-table-column>
          <el-table-column label="折扣金额" prop="discountFee">
            <template slot-scope="scope">
              {{ parseMoney(scope.row.discountFee) }}元
            </template>
          </el-table-column>
          <el-table-column label="应付金额" prop="actualFee">
            <template slot-scope="scope">
              {{ parseMoney(scope.row.actualFee) }}元
            </template>
          </el-table-column>
          <!-- <el-table-column
            label="操作"
            align="center"
            class-name="small-padding fixed-width"
            width="100"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleShowGoods(scope.row)"
                v-hasPermi="['system:card:list']"
                >查看关联商品</el-button
              >
            </template>
          </el-table-column> -->
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <!-- <el-button type="primary" @click="submitForm">确 定</el-button> -->
        <el-button @click="cancel">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  addSaleOrder,
  delSaleOrder,
  getSaleOrderSelf,
  listSaleOrderSelf,
  manualDelivery,
  updateSaleOrder,
} from "@/api/sale/saleOrder";
import {parseMoney} from "@/utils/my";

export default {
  name: "SaleOrder",
  dicts: [
    "sale_order_status",
    "pay_mode",
    "sys_yes_no",
    "delivery_type",
    "sys_order_type",
  ],
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
        pageSize: this.$store.state.settings.pageSize,
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
      rules: {},
      expands: [],
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
      if (
        null != this.daterangePaymentTime &&
        "" != this.daterangePaymentTime
      ) {
        this.queryParams.params["beginPaymentTime"] =
          this.daterangePaymentTime[0];
        this.queryParams.params["endPaymentTime"] =
          this.daterangePaymentTime[1];
      }
      if (
        null != this.daterangeDeliveryTime &&
        "" != this.daterangeDeliveryTime
      ) {
        this.queryParams.params["beginDeliveryTime"] =
          this.daterangeDeliveryTime[0];
        this.queryParams.params["endDeliveryTime"] =
          this.daterangeDeliveryTime[1];
      }
      listSaleOrderSelf(this.queryParams).then((response) => {
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
        remark: null,
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
      this.ids = selection.map((item) => item.orderId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    // /** 新增按钮操作 */
    // handleAdd() {
    //   this.reset();
    //   this.open = true;
    //   this.title = "添加销售订单";
    // },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const orderId = row.orderId || this.ids;
      getSaleOrderSelf(orderId).then((response) => {
        this.form = response.data;
        this.sysSaleOrderItemList = response.data.sysSaleOrderItemList;
        this.open = true;
        this.title = "销售订单详情";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.form.sysSaleOrderItemList = this.sysSaleOrderItemList;
          if (this.form.orderId != null) {
            updateSaleOrder(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addSaleOrder(this.form).then((response) => {
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
      this.$modal
        .confirm("是否确认删除数据项？")
        .then(function () {
          return delSaleOrder(orderIds);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 销售订单详情序号 */
    rowSysSaleOrderItemIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
    // /** 销售订单详情添加按钮操作 */
    // handleAddSysSaleOrderItem() {
    //   let obj = {};
    //   obj.templateType = "";
    //   obj.templateId = "";
    //   obj.num = "";
    //   obj.title = "";
    //   obj.price = "";
    //   obj.totalFee = "";
    //   obj.discountRule = "";
    //   obj.discountFee = "";
    //   obj.actualFee = "";
    //   this.sysSaleOrderItemList.push(obj);
    // },
    // /** 销售订单详情删除按钮操作 */
    // handleDeleteSysSaleOrderItem() {
    //   if (this.checkedSysSaleOrderItem.length == 0) {
    //     this.$modal.msgError("请先选择要删除的销售订单详情数据");
    //   } else {
    //     const sysSaleOrderItemList = this.sysSaleOrderItemList;
    //     const checkedSysSaleOrderItem = this.checkedSysSaleOrderItem;
    //     this.sysSaleOrderItemList = sysSaleOrderItemList.filter(function (
    //       item
    //     ) {
    //       return checkedSysSaleOrderItem.indexOf(item.index) == -1;
    //     });
    //   }
    // },
    // /** 复选框选中数据 */
    // handleSysSaleOrderItemSelectionChange(selection) {
    //   this.checkedSysSaleOrderItem = selection.map((item) => item.index);
    // },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "sale/saleOrder/export",
        {
          ...this.queryParams,
        },
        `saleOrder_${new Date().getTime()}.xlsx`
      );
    },
    parseMoney(val) {
      return parseMoney(val);
    },
    /** 手动发货按钮操作 */
    handleDelivery(row) {
      const orderIds = row.orderId || this.ids;
      this.$modal
        .confirm(
          "本操作将为用户发货并更改订单状态为交易成功，请确保您已在其他渠道收到货款，避免资金损失，是否继续？"
        )
        .then(() => {
          var data = { orderNo: row.orderNo };
          manualDelivery(data)
            .then((response) => {
              if (response.code == 200) {
                this.$modal.msgSuccess("发货成功");
                this.getList();
              }
            })
            .finally(() => {});
        })
        .then(() => {})
        .catch(() => {});
    },
    //在<table>⾥，我们已经设置row的key值设置为每⾏数据id：row-key="cardId"
    handleRowClick(row, column, event) {
      if (column.label == "操作") {
        return;
      }
      Array.prototype.remove = function (val) {
        let index = this.indexOf(val);
        if (index > -1) {
          this.splice(index, 1);
        }
      };
      if (this.expands.indexOf(row.orderId) < 0) {
        this.expands.push(row.orderId);
      } else {
        this.expands.remove(row.orderId);
      }
    },
  },
};
</script>
