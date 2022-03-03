<template>
  <div>
    订单编号：{{ orderNo }}
    <div align="center" style="margin-top: 20px">
      <el-button @click="paySuccess" type="primary">点击模拟支付</el-button>
    </div>
  </div>
</template>

<script>
import {notify} from "@/api/sale/saleShop";

export default {
  data() {
    return {
      orderNo: null,
    };
  },
  created() {
    this.orderNo = this.$route.query && this.$route.query.orderNo;
  },
  methods: {
    paySuccess() {
      var data = {orderNo: this.orderNo};
      notify(data)
        .then((response) => {
          if (response.code == 200) {
            this.$modal
              .confirm("订单支付成功，是否关闭当前页面？")
              .then(function () {
                window.close();
              })
              .catch(() => {
              });
          }
        })
        .finally(() => {
        });
    },
  },
};
</script>

<style scoped>
</style>
