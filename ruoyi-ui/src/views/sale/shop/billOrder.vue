<template>
  <div>
    订单编号：{{ orderNo }}

    <div v-if="htmlText" align="center" style="margin-top: 20px">
      <!-- <div v-html="htmlText"></div> -->
      <iframe :srcdoc="htmlText"
              border="0"
              frameborder="no"
              height="300"
              marginheight="0"
              marginwidth="0"
              scrolling="no"
              style="overflow:hidden;"
              width="300">
      </iframe>
    </div>
    <div v-if="qrText" align="center" style="margin-top: 20px">
      <vue-qr :logoSrc="logoUrl" :size="200" :text="qrText"></vue-qr>
    </div>

    <!-- <div align="center" style="margin-top: 20px">
      <el-button @click="paySuccess" type="primary">点击模拟支付</el-button>
    </div> -->
  </div>
</template>

<script>
import vueQr from 'vue-qr';
import {notify, paySaleOrder} from "@/api/sale/saleShop";

export default {
  name: "BillOrder",
  components: {vueQr},
  data() {
    return {
      orderNo: null,
      htmlText: null,
      logoUrl: require("../../../assets/logo/logo.png"),
      qrText: null,
    };
  },
  created() {
    this.orderNo = this.$route.query && this.$route.query.orderNo;

    var data = {orderNo: this.orderNo};
    paySaleOrder(data)
      .then((response) => {
        if (response.code == 200) {
          this.response = response;
          var data = response.data;
          if (data.success == true) {
            if (data.qrCode) {
              this.qrText = data.qrCode;
            } else {
              this.htmlText = data.body;
            }
          }
        }
      })
      .finally(() => {
      });
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
