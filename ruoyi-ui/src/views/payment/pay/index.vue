<template>
  <div class="app-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>订单信息</span>
      </div>
      <div class="order-info" v-loading="loading">
        <!-- 订单基本信息 -->
        <div class="info-section">
          <div class="info-item">
            <span class="label">订单号：</span>
            <span class="value">{{ order.orderNo }}</span>
          </div>
          <div class="info-item">
            <span class="label">创建时间：</span>
            <span class="value">{{ order.createTime }}</span>
          </div>
          <div class="info-item">
            <span class="label">支付状态：</span>
            <span class="value" :class="statusClass">{{ statusText }}</span>
          </div>
        </div>

        <!-- 商品信息 -->
        <div class="info-section">
          <div class="section-title">商品信息</div>
          <div class="info-item">
            <span class="label">套餐名称：</span>
            <span class="value">{{ order.packageName }}</span>
          </div>
          <div class="info-item">
            <span class="label">充值设备：</span>
            <span class="value">{{ order.deviceName }}</span>
          </div>
          <div class="info-item">
            <span class="label">充值时长：</span>
            <span class="value">{{ order.hours }} 小时</span>
          </div>
          <div class="info-item" v-if="order.freeHours > 0">
            <span class="label">赠送时长：</span>
            <span class="value highlight">+{{ order.freeHours }} 小时</span>
          </div>
        </div>

        <!-- 支付信息 -->
        <div class="info-section">
          <div class="section-title">支付信息</div>
          <div class="info-item">
            <span class="label">支付金额：</span>
            <span class="value price">${{ order.amount }} {{ order.currency }}</span>
          </div>
          <div class="payment-methods" v-if="!isPaid">
            <div class="section-title">选择支付方式</div>
            <div class="method-list">
              <div 
                v-for="method in paymentMethods" 
                :key="method.id"
                class="method-item"
                :class="{ active: selectedMethod === method.id }"
                @click="selectPaymentMethod(method.id)"
              >
                <img :src="method.icon" :alt="method.name">
                <span>{{ method.name }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 操作按钮 -->
        <div class="action-section">
          <el-button @click="goBack">返回</el-button>
          <el-button 
            type="primary" 
            @click="handlePay" 
            :disabled="!selectedMethod || isPaid"
            v-if="!isPaid"
          >
            立即支付
          </el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getOrder, processPayment } from "@/api/payment/pay";

export default {
  name: "Pay",
  data() {
    return {
      loading: false,
      order: {},
      selectedMethod: null,
      paymentMethods: [
        {
          id: 'paypal',
          name: 'PayPal',
          icon: require('@/assets/images/payment/paypal.png')
        }
      ]
    };
  },
  computed: {
    isPaid() {
      return this.order.status === 'PAID';
    },
    statusClass() {
      const statusMap = {
        'UNPAID': 'status-unpaid',
        'PAID': 'status-paid',
        'CANCELLED': 'status-cancelled'
      };
      return statusMap[this.order.status] || '';
    },
    statusText() {
      const statusMap = {
        'UNPAID': '待支付',
        'PAID': '已支付',
        'CANCELLED': '已取消'
      };
      return statusMap[this.order.status] || this.order.status;
    }
  },
  created() {
    const orderNo = this.$route.query.orderNo;
    if (orderNo) {
      this.getOrderInfo(orderNo);
    } else {
      this.$message.error('订单号不能为空');
      this.goBack();
    }
  },
  methods: {
    getOrderInfo(orderNo) {
      this.loading = true;
      getOrder(orderNo).then(response => {
        this.order = response.data;
        if (this.isPaid) {
          this.$message.success('该订单已支付完成');
        }
      }).catch(error => {
        this.$message.error('获取订单信息失败');
        console.error('获取订单信息失败:', error);
      }).finally(() => {
        this.loading = false;
      });
    },
    selectPaymentMethod(methodId) {
      this.selectedMethod = methodId;
    },
    handlePay() {
      if (!this.selectedMethod) {
        this.$message.warning('请选择支付方式');
        return;
      }

      this.loading = true;
      processPayment({
        orderNo: this.order.orderNo,
        paymentMethod: this.selectedMethod
      }).then(response => {
        // 处理支付平台返回的URL或其他响应
        if (response.data.approvalUrl) {
          window.location.href = response.data.approvalUrl;
        }
      }).catch(error => {
        this.$message.error('发起支付失败');
        console.error('发起支付失败:', error);
      }).finally(() => {
        this.loading = false;
      });
    },
    goBack() {
      this.$router.push('/payment/recharge');
    }
  }
};
</script>

<style scoped>
.box-card {
  margin: 20px auto;
  max-width: 800px;
}

.order-info {
  padding: 20px;
}

.info-section {
  margin-bottom: 30px;
  padding: 20px;
  border: 1px solid #EBEEF5;
  border-radius: 4px;
}

.section-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 15px;
  color: #303133;
}

.info-item {
  display: flex;
  margin-bottom: 15px;
  line-height: 24px;
}

.info-item .label {
  width: 100px;
  color: #606266;
}

.info-item .value {
  flex: 1;
  color: #303133;
}

.info-item .value.price {
  color: #F56C6C;
  font-size: 20px;
  font-weight: bold;
}

.info-item .value.highlight {
  color: #67C23A;
  font-weight: bold;
}

.status-unpaid {
  color: #E6A23C;
}

.status-paid {
  color: #67C23A;
}

.status-cancelled {
  color: #909399;
}

.payment-methods {
  margin-top: 20px;
}

.method-list {
  display: flex;
  gap: 20px;
}

.method-item {
  padding: 15px;
  border: 1px solid #DCDFE6;
  border-radius: 4px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 10px;
  transition: all 0.3s;
}

.method-item:hover {
  border-color: #409EFF;
}

.method-item.active {
  border-color: #409EFF;
  background-color: #ECF5FF;
}

.method-item img {
  height: 24px;
}

.action-section {
  margin-top: 30px;
  text-align: center;
}
</style> 