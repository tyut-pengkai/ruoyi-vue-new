<template>
  <div class="home">
    <el-alert
      v-if="shopConfig && shopConfig['saleShopNotice']"
      :closable="false"
      :title="shopConfig['saleShopNoticeTitle'] || '公告'"
      style="margin-bottom: 20px"
      type="info"
    >
      <div class="ql-container ql-bubble">
        <div class="ql-editor">
          <div>
            <div v-if="shopConfig && shopConfig['saleShopNotice']">
              <span v-html="shopConfig['saleShopNotice']"></span>
            </div>
            <!-- <div v-else>暂无公告</div> -->
          </div>
        </div>
      </div>
    </el-alert>
    <el-steps
      :active="4"
      :simple="true"
      align-center
      style="margin-bottom: 0px"
    >
      <el-step title="选择商品" icon="el-icon-shopping-cart-2"></el-step>
      <el-step title="确认订单" icon="el-icon-document"></el-step>
      <el-step title="线上支付" icon="el-icon-bank-card"></el-step>
      <el-step title="提取商品" icon="el-icon-sell"></el-step>
    </el-steps>
    <el-card class="box-card" style="margin-top: 15px">
      <div class="my-title">
        <img src="../../../assets/images/category.svg"/>&nbsp;<span
      >选择分类</span
      >
      </div>
      <card-category
        style="margin-top: 15px"
        v-on:card-click="handleCategorySelect"
        :data="categoryData"
        :cardKey="categoryId"
      >
      </card-category>
      <el-divider></el-divider>
      <div class="my-title">
        <img src="../../../assets/images/goods.svg"/>&nbsp;
        <span>选择商品</span>
      </div>
      <card-goods
        style="margin-top: 15px"
        v-on:card-click="handleGoodsSelect"
        :data="goodsData"
        :cardKey="goodsId"
      >
      </card-goods>
      <el-divider v-show="showGoodsDetail" id="goods"></el-divider>
      <div class="my-title" v-show="showGoodsDetail">
        <img src="../../../assets/images/detail.svg"/>&nbsp;<span
      >商品详情</span
      >
      </div>
      <div v-show="showGoodsDetail">
        <el-skeleton :throttle="300" :loading="loading" animated>
          <template #template>
            <div style="margin: 10px 0">
              <el-skeleton-item
                variant="text"
                style="height: 28px; margin-bottom: 18px"
                v-for="(item, index) in [0, 1, 2, 3, 4]"
                :key="index"
              />
            </div>
          </template>
          <template #default>
            <!-- <el-alert
              :closable="false"
              style="margin: 10px 0"
              title="商品公告"
              type="success"
            >
              <p>感谢您的使用</p>
            </el-alert> -->
            <el-form
              ref="form"
              style="margin-top: 10px"
              :model="form"
              label-width="80px"
              size="mini"
              :rules="rules"
            >
              <el-form-item label="商品名称">
                <el-input
                  class="my-title"
                  v-model="selectedGoodsData.name"
                  disabled
                ></el-input>
              </el-form-item>
              <el-form-item label="商品价格">
                <el-input
                  class="my-title"
                  v-model="selectedGoodsData.totalPrice"
                  disabled
                ></el-input>
              </el-form-item>
              <el-form-item label="发货方式">
                <el-tag>自动发货</el-tag>
              </el-form-item>
              <el-form-item label="购买数量">
                <el-input-number
                  v-model="form.buyNum"
                  @change="handleBuyNumChange"
                  :min="selectedGoodsData.minBuyNum"
                  :max="selectedGoodsData.num"
                ></el-input-number>
                <span style="margin-left: 10px" v-show="selectedGoodsData.minBuyNum > 1">
                  <el-tag>本商品{{selectedGoodsData.minBuyNum}}件起拍</el-tag>
                </span>
              </el-form-item>
              <el-form-item label="联系方式" prop="contact">
                <el-input
                  placeholder="请填写您的邮箱或手机号方便查询"
                  :clearable="true"
                  v-model="form.contact"
                  auto-complete="false"
                ></el-input>
              </el-form-item>
              <!-- <el-form-item label="查询密码" prop="queryPass">
                <el-input
                  type="password"
                  placeholder="请填写设置您的查询密码"
                  :show-password="true"
                  v-model="form.queryPass"
                  auto-complete="false"
                ></el-input>
              </el-form-item> -->
            </el-form>
          </template>
        </el-skeleton>
      </div>
    </el-card>
    <el-card style="margin-top: 15px">
      <div class="my-title">
        <img src="../../../assets/images/pay.svg"/>&nbsp;
        <span>支付方式</span>
      </div>
      <card-pay
        style="margin-top: 15px"
        :data="payData"
        :cardKey="payId"
        v-on:card-click="handlePaySelect"
      ></card-pay>
    </el-card>
    <el-input
      id="pay"
      type="button"
      class="my-button"
      style="margin-top: 10px"
      v-show="payButtonShow"
      v-model="inputText"
      @click.native="handleConfirmOrder"
    ></el-input>
    <el-divider id="pay"></el-divider>

    <!-- Form -->
    <!-- <el-button type="text" @click="dialogFormVisible = true">模拟订单展示</el-button> -->

    <el-dialog
      :before-close="handleCancelPay"
      :title="dialogTitle"
      :visible.sync="dialogFormVisible"
      custom-class="customClass"
      style="margin-top: 10vh"
      width="90vw"
    >
      <el-steps
        :active="activeStep"
        align-center
        finish-status="success"
        style="margin-bottom: 30px"
      >
        <el-step title="选择商品"></el-step>
        <el-step title="确认订单"></el-step>
        <el-step title="线上支付"></el-step>
        <el-step title="提取商品"></el-step>
      </el-steps>
      <el-form
        v-if="
          categoryData &&
          goodsData &&
          payData &&
          categoryId != null &&
          goodsId != null &&
          payId != null
        "
        :model="form"
      >
        <el-form-item label="商品名称" label-width="120px">
          [ {{ categoryData[categoryId].name }} ] {{ goodsData[goodsId].name }}
        </el-form-item>
        <el-form-item label="商品单价" label-width="120px">
          <span class="my-price">￥{{ selectedGoodsData.price }}</span>
          <el-tag size="small">× {{ form.buyNum }}</el-tag>
        </el-form-item>
        <el-form-item label="商品总价" label-width="120px">
          <span class="my-price">￥{{ selectedGoodsData.totalPrice }}</span>
        </el-form-item>
        <!-- <el-form-item label="优惠金额" label-width="120px">
          <el-tag effect="plain" size="small" type="danger">立减</el-tag> <span class="my-discount">-￥10.00</span>  <br>
          <el-tag effect="plain" size="small" type="danger">优惠券</el-tag> <span class="my-discount">-￥10.00</span>
        </el-form-item> -->
        <el-form-item label="联系方式" label-width="120px">
          {{ form.contact }}
        </el-form-item>
        <el-form-item label="支付方式" label-width="120px">
          {{ payData[payId].name }}
        </el-form-item>
        <el-divider></el-divider>
        <div v-if="orderShow">
          <el-form-item label="" label-width="80px">
            <span class="my-discount">[ 订单已提交，请在5分钟内支付 ]</span>
          </el-form-item>
          <el-form-item label="订单编号" label-width="80px">
            {{ orderNo }}
            <span class="my-discount"> [ {{ remainTimeShow }} ] </span>
          </el-form-item>
        </div>
        <el-row>
          <el-col :span="18" :offset="4">
            <el-form-item class="my-total" label="应付金额" label-width="80px">
              <span>￥{{ selectedGoodsData.totalPrice }}</span>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" style="margin-top: -40px">
        <el-button @click="handleCancelPay">取 消</el-button>
        <el-button type="primary" @click="handlePay"
        >{{ submitButtonText }}
        </el-button>
      </div>
    </el-dialog>

    <!-- Table -->
    <!-- <el-button type="text" @click="dialogTableVisible = true">
      模拟商品展示
    </el-button> -->
    <el-dialog
      style="margin-top: 10vh; height: 80%"
      :visible.sync="dialogTableVisible"
      custom-class="customClass"
      :title="'您购买的商品如下，请妥善保存，订单编号：' + orderNo"
      width="1000px"
    >
      <item-data :itemData="itemData"></item-data>
    </el-dialog>
  </div>
</template>

<script>
import "quill/dist/quill.core.css";
import "quill/dist/quill.snow.css";
import "quill/dist/quill.bubble.css";
import Cookies from "js-cookie";
import CardCategory from "./card/CardCategory";
import CardGoods from "./card/CardGoods";
import CardPay from "./card/CardPay";
import ItemData from "./card/ItemData";
import {checkStock, createSaleOrder, getCardList, getShopConfig, listApp, listCategory,} from "@/api/sale/saleShop";

export default {
  name: "Shop",
  components: {CardCategory, CardGoods, CardPay, ItemData},
  data() {
    return {
      dialogFormVisible: false,
      // 分类
      categoryId: null,
      categoryData: [
        // { id: 0, name: "测试分类", count: 2 },
        // { id: 1, name: "[官方]推荐服务", count: 3 },
        // { id: 2, name: "[官方]话费充值", count: 198 },
        // { id: 3, name: "[官方]影视专区", count: 185 },
        // { id: 4, name: "[官方]点卷专区", count: 185 },
      ],
      // 商品
      goodsId: null,
      goodsData: [
        // {
        //   id: 0, name: "测试分类", max: 5, min: 0.5, tags: ["多件优惠", "加群XXX"], num: 0, wholesale: []
        // },
        // {
        //   id: 1, name: "[官方]话费充值", min: 80, tags: ["单次购买100件只需80元/件"], num: 178,
        //   wholesale: [{
        //     num: 1, price: 100
        //   },{
        //     num: 10, price: 90
        //   },{
        //     num: 100, price: 80
        //   }]
        // },
      ],
      selectedGoodsData: {},
      showGoodsDetail: false,
      loading: false,
      // 详情
      formCopy: {},
      form: {
        contact: "",
        queryPass: "",
        buyNum: 1,
      },
      // 下单
      payId: null,
      payButtonShow: false,
      payData: [
        // {id: 0, name: "账户积分", code: "balance", img: "pay-jifen"},
        // {id: 0, name: "支付宝", code: "alipay_qr", img: "pay-alipay"},
        // {id: 1, name: "微信支付", code: "wechat", img: "pay-wechat"},
        // { id: 2, name: "银联支付", code: 'yinlian', img: "pay-yinlian" },
        // { id: 3, name: "PayPal", code: 'paypal', img: "pay-paypal" },
      ],
      inputText: "提交订单",
      dialogTitle: "确认订单",
      submitButtonText: "确认订单",
      activeStep: 1,
      // 支付
      orderShow: false,
      orderNo: null,
      remainTime: 300,
      remainTimeShow: "00:05:00",
      timer: null,

      // 支付成功
      dialogTableVisible: false,
      itemData: [
        // {
        //   title: "XXX软件-天卡",
        //   templateType: "1",
        //   goodsList: [
        //     {
        //       cardNo: "tbkgdzgaN3jhnnT23JKj",
        //       cardPass: "ZN6w395WnnXJrq3uxoAg",
        //       expireTime: "2022-03-02 23:31:16",
        //       chargeRule: "0",
        //     },
        //     {
        //       cardNo: "tbkgdzgaN3jhnnT23JKj",
        //       cardPass: "ZN6w395WnnXJrq3uxoAg",
        //       expireTime: "2022-03-02 23:31:16",
        //       chargeRule: "0",
        //     },
        //     {
        //       cardNo: "tbkgdzgaN3jhnnT23JKj",
        //       cardPass: "ZN6w395WnnXJrq3uxoAg",
        //       expireTime: "2022-03-02 23:31:16",
        //       chargeRule: "0",
        //     },
        //   ],
        // },
        // {
        //   title: "XXX软件-天卡",
        //   templateType: "2",
        //   goodsList: [
        //     {
        //       cardNo: "tbkgdzgaN3jhnnT23JKj",
        //       cardPass: "ZN6w395WnnXJrq3uxoAg",
        //       expireTime: "2022-03-02 23:31:16",
        //       chargeRule: "0",
        //     },
        //     {
        //       cardNo: "tbkgdzgaN3jhnnT23JKj",
        //       cardPass: "ZN6w395WnnXJrq3uxoAg",
        //       expireTime: "2022-03-02 23:31:16",
        //       chargeRule: "0",
        //     },
        //     {
        //       cardNo: "tbkgdzgaN3jhnnT23JKj",
        //       cardPass: "ZN6w395WnnXJrq3uxoAg",
        //       expireTime: "2022-03-02 23:31:16",
        //       chargeRule: "0",
        //     },
        //   ],
        // },
      ],
      // 商店配置
      shopConfig: null,
      rules: {
        contact: [
          {
            required: true,
            message: "联系方式是为您售后时的重要依据，不能为空",
            trigger: "blur",
          },
        ],
        // queryPass: [
        //   {
        //     required: true,
        //     message: "查询密码是您查询已购商品的重要依据，不能为空",
        //     trigger: "blur",
        //   },
        // ],
      },
      appUrl: null, // 用于限制要选择的软件和卡类
      cardUrl: null, // 用于限制要选择的软件和卡类
    };
  },
  created() {
    this.appUrl = this.$route.params && this.$route.params.appUrl;
    this.cardUrl = this.$route.params && this.$route.params.cardUrl;
    this.getShopConfig();
    this.getList();
  },
  methods: {
    /** 获取商店配置 */
    getShopConfig() {
      getShopConfig({}).then((response) => {
        this.shopConfig = response.data;
        for (var payMode of this.shopConfig["payModeList"]) {
          this.payData.push(
            Object.assign({id: this.payData.length}, payMode)
          );
        }
      });
    },
    /** 查询软件列表 */
    getList() {
      listApp({'shopUrl': this.appUrl, 'cardUrl': this.cardUrl}).then((response) => {
        var appList = response.rows;
        for (var app of appList) {
          if (app.tplCount > 0) {
            this.categoryData.push({
              id: this.categoryData.length,
              appId: app.appId,
              name: app.appName,
              count: app.tplCount,
            });
          }
        }
      });
    },
    handleCategorySelect(id) {
      this.goodsData = [];
      this.showGoodsDetail = false;
      this.goodsId = null;
      // 拉取当前选择目录下的商品列表
      listCategory({'appId': this.categoryData[id].appId, 'shopUrl': this.cardUrl}).then((response) => {
        var ctList = response.rows;
        for (var ct of ctList) {
          this.goodsData.push({
            id: this.goodsData.length,
            templateId: ct.templateId,
            name: ct.cardName,
            min: ct.price,
            // tags: ["多件优惠"],
            num: ct.cardCount,
            minBuyNum: ct.minBuyNum,
            wholesale: [], //批发
          });
        }
      });
      this.categoryId = id;
    },
    handleGoodsSelect(id) {
      // 拉取当前选择商品的商品详情
      this.loading = true;
      this.showGoodsDetail = true;
      // 锚点跳转
      // location.href = "#goods";
      setTimeout(() => {
        this.loading = false;
      }, 1000);
      // 是否出现下单按钮
      if (this.payId != null) {
        this.payButtonShow = true;
      }
      this.selectedGoodsData = this.goodsData[id];
      if (this.selectedGoodsData.num >= 1000) {
        this.selectedGoodsData.num = 1000;
      }
      if(this.selectedGoodsData.minBuyNum > this.form.buyNum) {
        this.form.buyNum = this.selectedGoodsData.minBuyNum;
      }
      this.selectedGoodsData.price = this.goodsData[id].min;
      this.selectedGoodsData.totalPrice = this.selectedGoodsData.price;
      this.goodsId = id;
    },
    handleBuyNumChange(value) {
      //更新商品价格
      this.selectedGoodsData.totalPrice =
        (this.selectedGoodsData.price * 100 * value) / 100;
    },
    handlePaySelect(id) {
      // 锚点跳转
      // location.href = "#pay";
      // window.scrollTo(0, document.documentElement.clientHeight);
      //这里检查之前表单是否有误，并判断当前环境是否需要验证。
      if (this.showGoodsDetail == false) {
        this.$notify({
          title: "消息",
          dangerouslyUseHTMLString: true,
          message: "请先选择商品",
          type: "warning",
          offset: 100,
        });
      } else {
        this.payButtonShow = true;
        this.payId = id;
      }
    },
    handleConfirmOrder() {
      //这里检查之前表单是否有误，并判断当前环境是否需要验证。
      if (this.showGoodsDetail == false) {
        this.$notify({
          title: "消息",
          dangerouslyUseHTMLString: true,
          message: "请先选择商品",
          type: "warning",
          offset: 300,
        });
        // } else if (!this.form["contact"] || !this.form["queryPass"]) {
      } else if (!this.form["contact"]) {
        this.$notify({
          title: "消息",
          dangerouslyUseHTMLString: true,
          // message: "联系方式或查询密码不能为空",
          message: "联系方式不能为空",
          type: "warning",
          offset: 300,
        });
      } else {
        this.orderNo = null;
        this.dialogTitle = "确认订单";
        this.activeStep = 1;
        this.submitButtonText = "确认订单";
        this.orderShow = false;
        if (this.timer) {
          clearInterval(this.timer);
        }

        this.form["payMode"] = this.payData[this.payId].code;
        this.form["appId"] = this.categoryData[this.categoryId].appId;
        this.form["templateId"] = this.goodsData[this.goodsId].templateId;

        checkStock(JSON.stringify(this.form))
          .then((response) => {
            if (response.code == 200) {
              this.dialogFormVisible = true;
            }
          })
          .finally(() => {
          });
      }
    },

    countDown() {
      if (this.remainTime >= 0) {
        var minutes = Math.floor(this.remainTime / 60);
        var seconds = Math.floor(this.remainTime % 60);
        this.remainTimeShow =
          "00:0" + minutes + ":" + (seconds >= 10 ? seconds : "0" + seconds);
        --this.remainTime;
      } else {
        clearInterval(this.timer);
        this.dialogFormVisible = false;
        this.$modal.alert("订单超时未支付，已自动关闭订单");
      }
    },

    handlePay() {
      //这里检查之前表单是否有误，并判断当前环境是否需要验证。
      if (this.showGoodsDetail == false) {
        this.$notify({
          title: "消息",
          dangerouslyUseHTMLString: true,
          message: "请先选择商品",
          type: "warning",
          offset: 300,
        });
      } else {
        if (this.activeStep == 1) {
          this.form["payMode"] = this.payData[this.payId].code;
          this.form["appId"] = this.categoryData[this.categoryId].appId;
          this.form["templateId"] = this.goodsData[this.goodsId].templateId;

          this.$modal.loading("正在提交，请稍后...");
          createSaleOrder(this.form)
            .then((response) => {
              if (response.code == 200) {
                this.orderNo = response.orderNo;
                // 存储到COOKIE
                var orderListStr = Cookies.get("orderList");
                var orderList = [];
                if (orderListStr) {
                  orderList = JSON.parse(orderListStr);
                }
                orderList.unshift({
                  orderNo: this.orderNo,
                  queryPass: this.form.queryPass,
                });
                if (orderList.length > 5) {
                  orderList = orderList.slice(0, 5);
                }
                Cookies.set("orderList", JSON.stringify(orderList));
              }
            })
            .finally(() => {
              setTimeout(() => {
                this.dialogTitle = "线上支付";
                this.activeStep = 2;
                this.submitButtonText = "立即支付";
                this.remainTime = 300;
                this.remainTimeShow = "00:05:00";
                if (this.timer) {
                  clearInterval(this.timer);
                }
                this.timer = setInterval(this.countDown, 1000);
                this.orderShow = true;
                this.$modal.closeLoading();
              }, 1000);
            });
        } else if (this.activeStep == 2) {
          const newPage = this.$router.resolve({
            path: "/billOrder",
            query: {
              orderNo: this.orderNo,
              // payMode: this.payData[this.payId].name
            },
          });
          window.open(newPage.href, "_blank");
          if (this.timer) {
            clearInterval(this.timer);
          }
          this.dialogFormVisible = false;
          this.$modal
            .confirm("是否已成功支付？")
            .then(() => {
              // console.log("确认");
              var data = {
                orderNo: this.orderNo,
                queryPass: this.form.queryPass,
              };
              getCardList(data)
                .then((response) => {
                  if (response.code == 200) {
                    // console.log(response)
                    var itemList = response.itemList;
                    this.itemData = [].concat(itemList);
                    this.dialogTableVisible = true;
                  }
                })
                .finally(() => {
                });
            })
            .catch(() => {
            });
        }
      }
    },
    handleCancelPay() {
      this.dialogFormVisible = false;
      if (this.timer) {
        clearInterval(this.timer);
      }
    },
  },
  beforeRouteLeave(to, from, next) {
    // console.log("/shop to----", to); //跳转后路由
    // console.log("/shop from----", from); //跳转前路由
    if(from.params.appUrl && from.params.appUrl !== '' && to.path.indexOf('/a/') === -1) {
      // console.log("/shop newto.path----", to.path + '/a/' + from.params.appUrl);
      next({'path': to.path + '/a/' + from.params.appUrl})
    }
    if(from.params.cardUrl && from.params.cardUrl !== '' && to.path.indexOf('/c/') === -1) {
      // console.log("/shop newto.path----", to.path + '/c/' + from.params.cardUrl);
      next({'path': to.path + '/c/' + from.params.cardUrl})
    }
    next();
  },
};
</script>

<style>
.el-alert__content {
  width: 100%;
}

.my-title span {
  font-weight: 600;
  font-size: 18px;
  color: #545454;
}

.my-title img {
  vertical-align: bottom;
}

.my-button .el-input__inner {
  background-color: #2e56cee1;
  color: #fff;
  cursor: pointer;
}

.my-button .el-input__inner:hover {
  background-color: #2e56cec7;
}

.my-button .el-input__inner:active {
  background-color: #2e56ce;
}

.my-price {
  font-weight: 600;
  color: #3c8ce7;
  margin-right: 5px;
  font-size: 14px;
}

.my-discount {
  font-weight: 600;
  color: red;
  font-size: 14px;
}

.my-total {
  float: right;
}

.my-total span {
  font-weight: 800;
  color: red;
  font-size: 24px;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both;
}

.customClass {
  max-width: 500px;
}
</style>
