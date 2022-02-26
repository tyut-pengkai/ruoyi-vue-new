<template>
  <div>
    订单编号：{{ orderId }}
    <el-button @click="paySuccess">支付成功</el-button>
  </div>
</template>

<script>
import {notify} from "@/api/sale/saleShop";

export default {
  data() {
    return {
      orderId: null,
    };
  },
  created() {
    this.orderId = this.$route.query && this.$route.query.orderId;
  },
  methods: {
    paySuccess() {
      var data = {orderId: this.orderId};
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
