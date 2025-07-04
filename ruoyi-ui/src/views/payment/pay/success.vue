<template>
  <div class="payment-success">
    <div class="success-card">
      <h2 class="title">支付确认</h2>
      <div v-if="loading" class="loading">
        <el-spinner type="loading" size="32px"></el-spinner>
        <p>正在获取订单信息...</p>
      </div>
      <div v-else class="info-list">
        <div class="info-item">
          <span class="label">订单号</span>
          <span class="value">{{ orderNo }}</span>
        </div>
        <div class="info-item">
          <span class="label">交易状态</span>
          <span class="value" :class="status === 'COMPLETED' ? 'success' : 'warning'">
            {{ getStatusText(status) }}
          </span>
        </div>
        <div class="info-item">
          <span class="label">账号</span>
          <span class="value">{{ account }}</span>
        </div>
        <div class="info-item">
          <span class="label">订单项目</span>
          <span class="value">{{ membershipPlan }}</span>
        </div>
        <div class="info-item">
          <span class="label">金额</span>
          <span class="value">{{ amount }} {{ currency }}</span>
        </div>
        <div class="info-item">
          <span class="label">支付方式</span>
          <div class="payment-method">
            <img v-if="paymentMethod === 'paypal'" src="@/assets/images/payment/paypal.png" alt="PayPal" class="payment-logo">
            <span>{{ paymentMethod }}</span>
          </div>
        </div>
      </div>
      <div class="actions">
        <el-button type="primary" @click="goToHome">返回首页</el-button>
        <el-button @click="viewOrder">查看订单</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { getPaymentCallback } from "@/api/payment/pay";

export default {
  name: 'PaymentSuccess',
  data() {
    return {
      loading: true,
      orderNo: '',
      status: '',
      account: '',
      membershipPlan: '',
      amount: '',
      currency: '',
      paymentMethod: ''
    }
  },
  created() {
    // 从URL参数获取token和PayerID
    const token = this.$route.query.token;
    const payerId = this.$route.query.PayerID;
    
    if (token && payerId) {
      this.getOrderInfo(token, payerId);
    } else {
      this.$message.error('缺少必要的支付参数');
      this.loading = false;
    }
  },
  methods: {
    async getOrderInfo(token, payerId) {
      try {
        const response = await getPaymentCallback('success', 'paypal',{
          token: token,
          PayerID: payerId
        });
 
        
        if (response.code === 200) {
          const orderInfo = response.data;
          this.orderNo = orderInfo.paymentId;
          this.status = orderInfo.status;
          this.account = orderInfo.payer_email;
          this.membershipPlan = orderInfo.packageName;
          this.amount = orderInfo.amount;
          this.currency = orderInfo.currency;
          this.paymentMethod = orderInfo.paymentMethod;
        } else {
          this.$message.error(response.msg || '获取订单信息失败');
        }
      } catch (error) {
        console.error('获取订单信息出错:', error);
        this.$message.error('获取订单信息失败');
      } finally {
        this.loading = false;
      }
    },
    getStatusText(status) {
      const statusMap = {
        'COMPLETED': '交易成功',
        'APPROVED': '等待确认',
        'PENDING': '处理中',
        'FAILED': '交易失败'
      };
      return statusMap[status] || status;
    },
    goToHome() {
      this.$router.push('/');
    },
    viewOrder() {
      this.$router.push(`/payment/order?orderNo=${this.orderNo}`);
    }
  }
}
</script>

<style lang="scss" scoped>
.payment-success {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f7fa;
  padding: 20px;

  .success-card {
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    padding: 30px;
    width: 100%;
    max-width: 600px;

    .title {
      text-align: center;
      color: #303133;
      margin-bottom: 30px;
      font-size: 24px;
    }

    .loading {
      text-align: center;
      padding: 40px 0;
      color: #909399;
    }

    .info-list {
      .info-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 15px 0;
        border-bottom: 1px solid #ebeef5;

        &:last-child {
          border-bottom: none;
        }

        .label {
          color: #606266;
          font-size: 14px;
        }

        .value {
          color: #303133;
          font-size: 14px;
          font-weight: 500;

          &.success {
            color: #67c23a;
          }

          &.warning {
            color: #e6a23c;
          }
        }

        .payment-method {
          display: flex;
          align-items: center;
          gap: 8px;

          .payment-logo {
            height: 20px;
            width: auto;
          }
        }
      }
    }

    .actions {
      margin-top: 30px;
      display: flex;
      justify-content: center;
      gap: 16px;
    }
  }
}
</style> 