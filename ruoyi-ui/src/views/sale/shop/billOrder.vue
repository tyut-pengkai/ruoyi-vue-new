<template>
  <div class="home">
    <el-card class="box-card" style="margin-top: 15px">
      <div class="my-title">
        <img src="../../../assets/images/category.svg"/>&nbsp;
        <span v-if="showType == 'qr'">扫码支付</span>
        <span v-else-if="showType == 'html'">H5支付</span>
        <span v-else-if="showType == 'form'">跳转支付</span>
        <span v-else>自定义支付</span>
      </div>
      <div align="center" style="margin-top: 20px">
        <span class="my-font"
        >支付方式：[{{ payMode }}]，请在3分钟有效期内支付！</span
        >
        <span class="my-discount"> [ {{ remainTimeShow }} ] </span>
      </div>
      <div align="center" style="margin-top: 10px">
        <span class="my-font">订单编号：</span>{{ orderNo }}
      </div>
      <div align="center" style="margin-top: 20px; margin-bottom: 20px">
        <div v-if="htmlText">
          <div ref="htmlText" v-html="htmlText"></div>
        </div>
        <div v-if="form">
          <form method="post" :action="form.url" ref="form">
            <input v-for="(value,key,index) in form.data" :name="key" :value="value" readonly hidden>
            <el-button type="primary" native-type="submit">点击跳转支付</el-button>
          </form>
        </div>
        <div v-if="qrText">
          <div style="
              width: 300px;
              padding: 15px;
              border-style: solid;
              border-radius: 20px;
              border-color: #3c8ce7;
            ">
            <div>
              应付金额：<span class="my-price">￥{{ actualFee }}</span>
            </div>
            <el-skeleton :loading="loading" animated style="width: 200px">
              <template slot="template">
                <el-skeleton-item style="width: 200px; height: 200px" variant="image"/>
              </template>
            </el-skeleton>
            <vue-qr v-if="qrText" :logoSrc="logoUrl" :size="200" :text="qrText"></vue-qr>
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
import vueQr from "vue-qr";
import {getPayStatus, paySaleOrder} from "@/api/sale/saleShop";

export default {
  name: "BillOrder",
  components: {vueQr},
  data() {
    return {
      orderNo: null,
      actualFee: null,
      payMode: null,
      htmlText: null,//div类的界面
      form: null,//表单
      qrText: null,//二维码
      showType: null,
      logoUrl: require("../../../assets/logo/logo.png"),
      loading: true,
      remainTime: 180,
      remainTimeShow: "00:03:00",
      timer: null,
      timerPay: null,
    };
  },
  created() {
    this.orderNo = this.$route.query && this.$route.query.orderNo;
    // this.payMode = this.$route.query && this.$route.query.payMode;

    var data = {orderNo: this.orderNo};
    this.form = null;
    paySaleOrder(data)
      .then((response) => {
        if (response.code == 200) {
          this.response = response;
          this.actualFee = response.actualFee;
          this.payMode = response.payMode;
          this.showType = response.showType;
          var data = response.data;
          if (data.success == true) {
            // 倒计时
            this.remainTime = 180;
            this.remainTimeShow = "00:03:00";
            if (this.timer) {
              clearInterval(this.timer);
            }
            this.timer = setInterval(this.countDown, 1000);
            // 获取支付状态
            this.timerPay = setInterval(this.checkSaleOrderStatus, 10000);
            if (this.showType == "qr") {
              if (data.qrCode) {
                this.qrText = data.qrCode;
              }
            } else if (this.showType == "html") {
              this.htmlText = data.body;
              let regJs = "";
              this.htmlText.replace(
                /<script.*?>([\s\S]+?)<\/script>/gim,
                (_, js) => {
                  // 正则匹配出script中的内容
                  regJs = js;
                }
              );
              this.$nextTick(() => {
                let script = document.createElement("script");
                script.innerHTML = regJs;
                this.$refs.htmlText.append(script);
              });
            } else if (this.showType == "form") {
              this.form = {
                "url": data.url,
                "data": data.data
              }
            } else if (this.showType == "free") {
              clearInterval(this.timer);
              clearInterval(this.timerPay);
              this.$alert("支付成功", "系统提示", {
                confirmButtonText: "确定",
                callback: (action) => {
                  window.close();
                },
              });
            }
            this.loading = false;
          }
        }
      })
      .finally(() => {
      });
  },
  methods: {
    // paySuccess() {
    //   var data = {orderNo: this.orderNo};
    //   notify(data)
    //     .then((response) => {
    //       if (response.code == 200) {
    //         this.$modal
    //           .confirm("订单支付成功，是否关闭当前页面？")
    //           .then(function () {
    //             window.close();
    //           })
    //           .catch(() => {
    //           });
    //       }
    //     })
    //     .finally(() => {
    //     });
    // },
    countDown() {
      if (this.remainTime >= 0) {
        var minutes = Math.floor(this.remainTime / 60);
        var seconds = Math.floor(this.remainTime % 60);
        this.remainTimeShow =
          "00:0" + minutes + ":" + (seconds >= 10 ? seconds : "0" + seconds);
        --this.remainTime;
      } else {
        clearInterval(this.timer);
        clearInterval(this.timerPay);
        this.$alert(
          "订单超时未支付，已自动关闭订单（若已支付，请3分钟后在【查询订单】中再次查询）",
          "系统提示",
          {
            confirmButtonText: "确定",
            callback: (action) => {
              window.close();
            },
          }
        );
      }
    },
    checkSaleOrderStatus() {
      getPayStatus({orderNo: this.orderNo}).then((response) => {
        if (response.code == 200 && response.msg === "1") {
          clearInterval(this.timer);
          clearInterval(this.timerPay);
          this.$alert("支付成功", "系统提示", {
            confirmButtonText: "确定",
            callback: (action) => {
              window.close();
            },
          });
        }
      });
    },
  },
};
</script>

<style scoped>
.my-font {
  font-weight: 600;
  font-size: 18px;
  color: #3c8ce7;
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

.my-discount {
  font-weight: 600;
  color: red;
  font-size: 18px;
}
</style>
