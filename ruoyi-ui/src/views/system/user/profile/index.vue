<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="6" :xs="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>个人信息</span>
          </div>
          <div>
            <div class="text-center">
              <userAvatar :user="user" />
            </div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                <svg-icon icon-class="user" />用户名称
                <div class="pull-right">{{ user.userName }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="phone" />手机号码
                <div class="pull-right">{{ user.phonenumber }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="email" />用户邮箱
                <div class="pull-right">{{ user.email }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="tree"/>
                所属部门
                <div v-if="user.dept" class="pull-right">
                  {{ user.dept.deptName }} / {{ postGroup }}
                </div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="peoples"/>
                所属角色
                <div class="pull-right">{{ roleGroup }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="date"/>
                创建日期
                <div class="pull-right">{{ user.createTime }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="date"/>
                最后登录
                <div class="pull-right">
                  {{ parseTime(user.loginDate) }} / {{ user.loginIp }}
                </div>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="18" :xs="24">
        <el-card style="max-height: 58px">
          <el-tag
          >账户余额
            <span>
              <count-to
                :decimals="2"
                :duration="2600"
                :end-val="user.availablePayBalance"
                :start-val="0"
                class="card-num"
                prefix="￥ "
              />
            </span>
            <!-- <el-divider direction="vertical"></el-divider>
            账户余额(赠)
            <span>
              <count-to
                :decimals="2"
                :duration="2600"
                :end-val="user.availableFreeBalance"
                :start-val="0"
                class="card-num"
                prefix="￥ "
              />
            </span> -->
          </el-tag>
          <el-divider direction="vertical"></el-divider>
          <el-link type="primary" @click="handleCharge()">充值</el-link>
          <!-- <el-divider direction="vertical"></el-divider>
          <el-link type="primary">提现</el-link> -->
          <el-tag style="margin-left: 50px" type="info">
            账户余额冻结
            <span>
              <count-to
                :decimals="2"
                :duration="2600"
                :end-val="user.freezeFreeBalance"
                :start-val="0"
                class="card-num"
                prefix="￥ "
              />
            </span>
            <!-- <el-divider direction="vertical"></el-divider>
            账户余额冻结(赠)
            <span>
              <count-to
                :decimals="2"
                :duration="2600"
                :end-val="user.freezeFreeBalance"
                :start-val="0"
                class="card-num"
                prefix="￥ "
              />
            </span> -->
          </el-tag>
        </el-card>
        <el-card style="margin-top: 15px">
          <div slot="header" class="clearfix">
            <span>基本资料</span>
          </div>
          <el-tabs v-model="activeTab">
            <el-tab-pane label="基本资料" name="userinfo">
              <userInfo :user="user"/>
            </el-tab-pane>
            <el-tab-pane label="修改密码" name="resetPwd">
              <resetPwd/>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>

    <!-- 充值对话框 -->
    <el-dialog
      :visible.sync="openC"
      append-to-body
      title="余额充值"
      width="800px"
    >
      <el-form ref="formC" :model="formC" :rules="rulesC" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="充值金额" prop="amount">
              <el-input-number
                v-model="formC.amount"
                :min="0.01"
                :precision="2"
                :step="0.01"
                controls-position="right"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="支付方式" prop="payMode">
              <card-pay
                :addon="true"
                :cardKey="payId"
                :data="payData"
                :span="6"
                v-on:card-click="handlePaySelect"
              ></card-pay>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFormC">提 交</el-button>
        <el-button @click="cancelC">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import userAvatar from "./userAvatar";
import userInfo from "./userInfo";
import resetPwd from "./resetPwd";
import {getUserProfile} from "@/api/system/user";
import CountTo from "vue-count-to";
import CardPay from "@/views/sale/shop/card/CardPay1";
import {createChargeOrder, getPayStatus, getShopConfig,} from "@/api/sale/saleShop";

export default {
  name: "Profile",
  components: {userAvatar, userInfo, resetPwd, CountTo, CardPay},
  data() {
    return {
      user: {},
      roleGroup: {},
      postGroup: {},
      activeTab: "userinfo",
      // 充值
      openC: false,
      formC: {amount: 100},
      rulesC: {
        amount: [{required: true, message: "金额不能为空", trigger: "blur"}],
      },
      payId: null,
      payData: [
        // { id: 0, name: "账户积分", code: "balance", img: "pay-jifen" },
        // {id: 0, name: "支付宝当面付", code: "alipay_qr", img: "pay-alipay"},
        // {id: 1, name: "微信支付", code: "wechat", img: "pay-wechat"},
        // { id: 2, name: "银联支付", code: "yinlian", img: "pay-yinlian" },
        // { id: 3, name: "PayPal", code: "paypal", img: "pay-paypal" },
      ],
      // 商店配置
      shopConfig: null,
    };
  },
  created() {
    this.getUser();
    this.getShopConfig();
  },
  methods: {
    getUser() {
      getUserProfile().then((response) => {
        this.user = response.data;
        this.roleGroup = response.roleGroup;
        this.postGroup = response.postGroup;
      });
    },
    // 充值
    /** 充值按钮操作 */
    handleCharge() {
      this.openC = true;
    },
    // 取消按钮
    cancelC() {
      this.openC = false;
    },
    /** 提交按钮 */
    submitFormC: function () {
      this.$refs["formC"].validate((valid) => {
        if (valid) {
          this.$modal
            .confirm("是否确认充值？")
            .then(() => {
              // console.log("确认");
              this.formC["payMode"] = this.payData[this.payId].code;
              this.$modal.loading("正在提交，请稍后...");
              createChargeOrder(this.formC).then((response) => {
                if (response.code == 200) {
                  this.orderNo = response.orderNo;
                  const newPage = this.$router.resolve({
                    path: "/billOrder",
                    query: {
                      orderNo: this.orderNo,
                      // payMode: this.payData[this.payId].name
                    },
                  });
                  window.open(newPage.href, "_blank");
                  this.$modal
                    .confirm("是否已成功支付？")
                    .then(() => {
                      getPayStatus({orderNo: this.orderNo}).then(
                        (response) => {
                          if (response.code == 200 && response.msg === "1") {
                            this.getUser();
                          } else {
                            this.$alert(
                              "尚未收到您的付款信息（若已支付，请3分钟后在【个人中心】中再次查询您的余额）",
                              "系统提示",
                              {
                                confirmButtonText: "确定",
                                callback: (action) => {
                                },
                              }
                            );
                          }
                        }
                      );
                    })
                    .catch(() => {
                    });
                }
              });
            })
            .catch(() => {
            })
            .finally(() => {
              this.$modal.closeLoading();
            });
        }
      });
    },
    handlePaySelect(id) {
      this.payId = id;
    },
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
  },
};
</script>
