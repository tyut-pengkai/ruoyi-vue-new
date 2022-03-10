<template>
  <div class="home">
    <el-card class="box-card" style="margin-top: 15px">
      <div class="my-title">
        <img src="../../../assets/images/category.svg"/>&nbsp;
        <span>扫码支付</span>
      </div>
      <div align="center" style="margin-top: 20px">
        <span class="my-font">支付方式：[{{ payMode }}]，请打开APP扫码支付！有效期3分钟</span>
      </div>
      <div align="center" style="margin-top: 10px">
        <span class="my-font">订单编号：</span>{{ orderNo }}
      </div>
      <div align="center" style="margin-top: 20px; margin-bottom: 20px;">
        <div v-if="htmlText">
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
        <div v-if="qrText">
          <div style="width:300px; padding: 15px; border-style: solid; border-radius: 20px; border-color: #3C8CE7;">
            <div>应付金额：<span class="my-price">￥{{ actualFee }}</span></div>
            <vue-qr :logoSrc="logoUrl" :size="200" :text="qrText"></vue-qr>
          </div>
        </div>
      </div>

      <!-- <div align="center" style="margin-top: 20px">
        <el-button @click="paySuccess" type="primary">点击模拟支付</el-button>
      </div> -->
    </el-card>
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
      actualFee: null,
      payMode: null,
      htmlText: null,
      logoUrl: require("../../../assets/logo/logo.png"),
      qrText: null,
    };
  },
  created() {
    this.orderNo = this.$route.query && this.$route.query.orderNo;
    this.payMode = this.$route.query && this.$route.query.payMode;

    var data = {orderNo: this.orderNo};
    paySaleOrder(data)
      .then((response) => {
        if (response.code == 200) {
          this.response = response;
          this.actualFee = response.actualFee;
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
.my-font {
  font-weight: 600;
  font-size: 18px;
  color: #3C8CE7;
}

.my-title span {
  font-weight: 600;
  font-size: 18px;
  color: #545454;
}

.my-title img {
  vertical-align: bottom;
}

.my-price {
  font-weight: 600;
  color: #3c8ce7;
  margin-right: 5px;
  font-size: 18px;
}
</style>
