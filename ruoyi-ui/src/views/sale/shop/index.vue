<template>
  <div class="home">
    <el-alert title="公告" type="success" :closable="false">
      <p>本站为INAMS演示站。所有商品仅供测试。并无实际商品。请悉知。</p>
      <p>

      </p>
    </el-alert>
    <el-card class="box-card" style="margin-top: 15px">
      <div class="my-title">
        <img src="../../../assets/images/category.svg"/>&nbsp;<span>选择分类</span>
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
        <img src="../../../assets/images/goods.svg"/>&nbsp;<span>选择商品</span>
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
        <img src="../../../assets/images/detail.svg"/>&nbsp;<span>商品详情</span>
      </div>
      <div v-show="showGoodsDetail">
        <el-skeleton :throttle="300" :loading="loading" animated>
          <template #template>
            <div style="margin:10px 0;">
              <el-skeleton-item
                variant="text"
                style="height: 28px;margin-bottom: 18px;"
              />
              <el-skeleton-item
                variant="text"
                style="height: 28px;margin-bottom: 18px;"
              />
              <el-skeleton-item
                variant="text"
                style="height: 28px;margin-bottom: 18px;"
              />
              <el-skeleton-item
                variant="text"
                style="height: 28px;margin-bottom: 18px;"
              />
              <el-skeleton-item
                variant="text"
                style="height: 28px;margin-bottom: 18px;"
              />
              <el-skeleton-item
                variant="text"
                style="height: 28px;margin-bottom: 18px;"
              />
            </div>
          </template>
          <template #default>
            <el-alert title="商品公告" type="success" :closable="false" style="margin:10px 0;">
              <p>本项目靠爱发电，如果觉得对您有帮助，何不请作者喝瓶冰阔乐</p>
            </el-alert>
            <el-form
              ref="form"
              style="margin-top: 10px;"
              :model="form"
              label-width="80px"
              size="mini"
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
                  :min="1"
                  :max="selectedGoodsData.num"
                  label="描述文字"
                ></el-input-number>
              </el-form-item>
              <el-form-item label="联系方式">
                <el-input
                  placeholder="请填写您的QQ号或手机号方便查询"
                  :clearable="true"
                  v-model="form.contact"
                ></el-input>
              </el-form-item>
              <el-form-item label="查询密码">
                <el-input
                  type="password"
                  placeholder="请填写设置您的查询密码"
                  :show-password="true"
                  v-model="form.queryPass"
                ></el-input>
              </el-form-item>
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
      type="button"
      class="my-button"
      style="margin-top: 10px;"
      v-show="payButtonShow"
      v-model="inputText"
      @click.native="handleSubmit"
    ></el-input>
  </div>
</template>

<script>
import CardCategory from "./card/CardCategory";
import CardGoods from "./card/CardGoods";
import CardPay from "./card/CardPay";
import {listApp} from "@/api/sale/sale";
import {listCardTemplate} from "@/api/system/cardTemplate";

export default {
  components: { CardCategory, CardGoods, CardPay },
  data() {
    return {
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
        //   id: 0,
        //   name: "测试分类",
        //   max: 5,
        //   min: 0.5,
        //   tags: ["多件优惠", "加群XXX"],
        //   num: 0,
        //   wholesale: []
        // },
        // {
        //   id: 1,
        //   name: "[官方]推荐服务",
        //   max: 100,
        //   min: 50,
        //   tags: ["多件优惠", "量大加微信XXX"],
        //   num: 158,
        //   wholesale: []
        // },
        // {
        //   id: 2,
        //   name: "[官方]话费充值",
        //   min: 80,
        //   tags: ["单次购买100件只需80元/件"],
        //   num: 178,
        //   wholesale: [{
        //     num: 1,
        //     price: 100
        //   },{
        //     num: 10,
        //     price: 90
        //   },{
        //     num: 100,
        //     price: 80
        //   }]
        // },
      ],
      selectedGoodsData: {},
      showGoodsDetail: false,
      loading: false,
      // 详情
      form: {
        contact: "",
        queryPass: "",
        buyNum: 1,
      },
      // 支付
      payId: null,
      payButtonShow: false,
      payData: [
        { id: 0, name: "账户积分", code: 'balance', img: "pay-jifen" },
        { id: 1, name: "支付宝", code: 'alipay', img: "pay-alipay" },
        { id: 2, name: "微信支付", code: 'wechat', img: "pay-wechat" },
        { id: 3, name: "银联支付", code: 'yinlian', img: "pay-yinlian" },
        { id: 4, name: "PayPal", code: 'paypal', img: "pay-paypal" },

      ],
      inputText: "提交订单",
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询软件列表 */
    getList() {
      listApp({}).then((response) => {
        var appList = response.rows;
        for(var app of appList){
          if(app.tplCount > 0) {
            this.categoryData.push({id:this.categoryData.length, appId: app.appId, name:app.appName, count:app.tplCount});
          }
        }
      });
    },
    handleCategorySelect(id) {
      console.log(id);
      this.goodsData = [];
      // 拉取当前选择目录下的商品列表
      listCardTemplate({appId: this.categoryData[id].appId}).then((response) => {
        var ctList = response.rows;
        for(var ct of ctList){
          if(ct.onSale) {
            this.goodsData.push({id: this.goodsData.length, tplId: ct.templateId, name: ct.cardName, min: ct.price, tags: ["多件优惠"], num: 99999, wholesale: []});
          }
        }
      });
      this.categoryId = id;
    },
    handleGoodsSelect(id) {
      console.log(id);
      // 拉取当前选择商品的商品详情
      this.loading = true;
      this.showGoodsDetail = true;
      // 锚点跳转
      location.href = "#goods";
      setTimeout(() => {
        this.loading = false;
      }, 1000);
      // 是否出现下单按钮
      if(this.payId != null){
        this.payButtonShow = true;
      }
      this.selectedGoodsData = this.goodsData[id];
      this.selectedGoodsData.price = this.goodsData[id].min;
      this.selectedGoodsData.totalPrice = this.selectedGoodsData.price;
      this.goodsId = id;
    },
    handleBuyNumChange(value) {
      //更新商品价格
      this.selectedGoodsData.totalPrice = this.selectedGoodsData.price * value;
    },
    handlePaySelect(id) {
      console.log(id);
      //这里检查之前表单是否有误，并判断当前环境是否需要验证。
      if (this.showGoodsDetail == false) {
        this.$notify({
          title: "消息",
          dangerouslyUseHTMLString: true,
          message: "请先选择商品",
          type: "warning",
        });
      } else {
        this.payButtonShow = true;
      }
      this.payId = id;
    },
    handleSubmit() {
      console.log(this.form, this.payData[this.payId].code);
    },
  }
}
</script>

<style>
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
</style>
