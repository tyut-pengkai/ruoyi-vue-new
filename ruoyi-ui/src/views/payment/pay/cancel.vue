<template>
  <div class="app-container">
    <div class="payment-cancel-container">
      <div class="loading-wrapper" v-if="loading">
        <el-spinner type="spinner" size="45"></el-spinner>
        <p>正在处理支付取消请求...</p>
      </div>
    </div>
  </div>
</template>

<script>
import { getPaymentCallback } from "@/api/payment/pay";

export default {
  name: "PaymentCancel",
  data() {
    return {
      loading: true
    };
  },
  created() {
    const token = this.$route.query.token;  //token=paypal订单ID
    if (token) {
      this.handlePaymentCancel(token);
    } else {
      this.$router.push('/payment/pay');
    }
  },
  methods: {
    handlePaymentCancel(token) {
      const params = {
        token: token   //token=paypal订单ID
      };
      getPaymentCallback('cancel', 'paypal', params)
        .then(response => {
          const { orderNo } = response.data;
          // 重定向到支付页面，带上orderNo参数
          this.$router.push({
            path: '/payment/pay',
            query: { orderNo }
          });
        })
        .catch(error => {
          this.$modal.msgError('处理支付取消失败：' + error);
          // 出错时也跳转到支付页面
          this.$router.push('/payment/pay');
        });
    }
  }
};
</script>

<style lang="scss" scoped>
.payment-cancel-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;

  .loading-wrapper {
    text-align: center;

    p {
      margin-top: 20px;
      color: #606266;
    }
  }
}
</style> 