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
              <userAvatar />
            </div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                <svg-icon icon-class="user" />用户账号
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
                <svg-icon icon-class="tree" />所属分组
                <div class="pull-right" v-if="user.dept">{{ user.dept.deptName }} / {{ postGroup }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="peoples" />所属角色
                <div class="pull-right">{{ roleGroup }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="date" />创建日期
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
          <el-divider direction="vertical"></el-divider>
          <el-link type="primary" @click="handleTransfer()">转账</el-link>
          <el-divider direction="vertical"></el-divider>
          <el-link type="primary" @click="handleWithdraw()">提现</el-link>
          <el-tag style="margin-left: 50px" type="info">
            账户余额冻结
            <span>
              <count-to
                :decimals="2"
                :duration="2600"
                :end-val="user.freezePayBalance"
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
              <userInfo :user="user" />
            </el-tab-pane>
            <el-tab-pane label="修改密码" name="resetPwd">
              <resetPwd />
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


    <!-- 转账对话框 -->
    <el-dialog
      :visible.sync="openT"
      append-to-body
      title="账号转账"
      width="800px"
    >
      <el-form ref="formT" :model="formT" :rules="rulesT" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="转账金额" prop="amount">
              <el-input-number
                v-model="formT.amount"
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
            <el-form-item label="目的账号" prop="toUserId">
              <el-select v-model="formT.toUserId" :clearable="true" filterable :loading="loading"
                         :remote-method="remoteMethod" prop="toUserId" placeholder="请输入账号" remote>
                <el-option v-for="item in userList" :key="item.userId" :disabled="item.disabled"
                           :label="item.nickName + '(' + item.userName + ')'" :value="item.userId">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="转账原因" prop="remark">
              <el-input
                v-model="formT.remark"
                placeholder="请输入内容"
                type="textarea"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFormT">提 交</el-button>
        <el-button @click="cancelT">取 消</el-button>
      </div>
    </el-dialog>


    <!-- 提现对话框 -->
    <el-dialog
      :visible.sync="openW"
      append-to-body
      title="申请提现"
      width="800px"
    >
      <el-alert title="提现规则" type="info" show-icon :closable="false" style="margin-bottom: 10px">
        <ul>
          <li>最低提现金额为{{ configWithdraw.withdrawCashMin }}元，最高提现金额为{{ configWithdraw.withdrawCashMax }}元</li>
          <li>提现收取手续费{{ configWithdraw.handlingFeeRate }}%，最低收取{{ configWithdraw.handlingFeeMin }}元，最高收取{{ configWithdraw.handlingFeeMax }}元</li>
          <li>提现支持以下方式：{{getWithdrawTypeList}}<!--，{{ configWithdraw.enableAlipay }}，{{ configWithdraw.enableWechat }}，{{ configWithdraw.enableQq }}，{{ configWithdraw.enableBankCard }}--></li>
        </ul>
      </el-alert>
      <el-form ref="formW" :model="formW" :rules="rulesW">
        <el-row>
          <el-col :span="12">
            <el-form-item label="账户余额" prop="">
              <el-tag>
                <span>￥ {{ user.availablePayBalance }}</span>
              </el-tag>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="提现金额" prop="applyFee">
              <el-input-number
                v-model="formW.applyFee"
                :min="configWithdraw.withdrawCashMin || 0.01"
                :max="configWithdraw.withdrawCashMax > user.availablePayBalance ? user.availablePayBalance : (configWithdraw.withdrawCashMax || user.availablePayBalance)"
                :precision="2"
                :step="0.01"
                controls-position="right"
                @change="handleApplyFeeChange"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="'手续费(' + configWithdraw.handlingFeeRate + '%)'" prop="handlingFee">
              <el-tag>
                <span>￥ {{ handlingFee }}</span>
              </el-tag>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="预计到账" prop="actualFee">
              <el-tag>
                <span>￥ {{ expectActualFee }}</span>
              </el-tag>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="收款账号" prop="receiveAccountId">
              <el-select v-model="formW.receiveAccountId" :clearable="true" filterable :loading="loading" prop="receiveAccountId" placeholder="请选择收款账号">
                <el-option v-for="item in receiveAccountList" :key="item.id" :label="item.receiveAccount + '|' + item.realName" :value="item.id">
                  <dict-tag :options="dict.type.sys_receive_method" :value="item.receiveMethod" style="display:inline;margin-right:10px"/>
                  <span>{{ item.receiveAccount }} <el-divider direction="vertical"></el-divider> {{ item.realName }}</span>
                </el-option>
              </el-select>
              <el-link type="primary" style="margin-left: 10px; line-height: normal" @click="handleReceiveAccount">收款账号管理</el-link>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注信息" prop="remark">
              <el-input
                v-model="formW.remark"
                placeholder="请输入内容"
                type="textarea"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFormW">提 交</el-button>
        <el-button @click="cancelW">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import userAvatar from "./userAvatar";
import userInfo from "./userInfo";
import resetPwd from "./resetPwd";
import {listUser, transferUserBalance, getUserProfile, withdrawUserBalance} from "@/api/system/user";
import CountTo from "vue-count-to";
import CardPay from "@/views/sale/shop/card/CardPay1";
import {createChargeOrder, getPayStatus, getShopConfig,} from "@/api/sale/saleShop";
import {getWithdrawConfig,} from "@/api/system/configWithdraw";
import { listWithdrawMethod } from "@/api/system/withdrawMethod";


export default {
  name: "Profile",
  components: {userAvatar, userInfo, resetPwd, CountTo, CardPay},
  dicts: ['sys_receive_method'],
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
      // 转账
      loading: false,
      openT: false,
      formT: {amount: 100},
      rulesT: {
        amount: [{required: true, message: "金额不能为空", trigger: "blur"}],
        toUserId: [{required: true, message: "目的账号不能为空", trigger: "blur"}],
        remark: [{ required: true, message: "转账原因不能为空", trigger: "blur" }],
      },
      userList: [],
      openW: false,
      formW: {
        applyFee: 0.01
      },
      rulesW: {
        applyFee: [{required: true, message: "金额不能为空", trigger: "blur"}],
        receiveAccountId: [{required: true, message: "收款账号不能为空", trigger: "blur"}],
      },
      receiveAccountList: [],
      configWithdraw: {},
      handlingFee: null,
      expectActualFee: null,
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
    remoteMethod(query) {
      if (query !== "") {
        this.loading = true;
        listUser({ userName: query }).then((response) => {
          this.userList = response.rows;
          this.loading = false;
        });
      } else {
        this.userList = [];
      }
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
    // 转账
    /** 充值按钮操作 */
    handleTransfer() {
      this.openT = true;
    },
    // 取消按钮
    cancelT() {
      this.openT = false;
    },
    /** 提交按钮 */
    submitFormT: function () {
      this.$refs["formT"].validate((valid) => {
        if (valid) {
          transferUserBalance(this.formT).then((response) => {
            this.getUser();
            this.$modal.msgSuccess("转账成功");
            this.openT = false;
          });
        }
      });
    },
    // 提现
    /** 提现按钮操作 */
    handleWithdraw() {
      this.loading = true;
      getWithdrawConfig().then((response) => {
        this.configWithdraw = response.data;
        if(this.configWithdraw.enableWithdrawCash === 'Y') {
          this.formW.applyFee = response.data.withdrawCashMin || 0.01;
          this.handleApplyFeeChange(this.formW.applyFee, 0);
          this.loading = true;
          listWithdrawMethod().then(response => {
            this.receiveAccountList = response.rows;
            this.loading = false;
          });
          this.openW = true;
        } else {
          this.$modal.msgError("管理员未开启提现功能");
        }
        this.loading = false;
      });
    },
    // 取消按钮
    cancelW() {
      this.openW = false;
    },
    /** 提交按钮 */
    submitFormW: function () {
      this.$refs["formW"].validate((valid) => {
        if (valid) {
          withdrawUserBalance(this.formW).then((response) => {
            this.getUser();
            this.$modal.msgSuccess("提交申请成功，请等待打款");
            this.openW = false;
          });
        }
      });
    },
    // 下划线转驼峰
    convertToCamelCase(str) {
      return str.replace(/_(.)/g, function(match, group) {
        return group.toUpperCase();
      });
    },
    // 首字母大写
    capitalizeFirstLetter(str) {
      return str.substring(0, 1).toUpperCase() + str.substring(1);
    },
    handleReceiveAccount() {
      this.cancelW();
      this.$router.push({
        path: "/user/withdrawMethod",
        query: {

        },
      });
    },
    handleApplyFeeChange(currentValue, oldValue) {
      let fee = currentValue * this.configWithdraw.handlingFeeRate;
      fee = Math.round(fee) / 100;
      fee = fee < this.configWithdraw.handlingFeeMin ? this.configWithdraw.handlingFeeMin : fee;
      fee = fee > this.configWithdraw.handlingFeeMax ? this.configWithdraw.handlingFeeMax : fee;
      this.handlingFee = fee;
      if(this.user.availablePayBalance >= currentValue + fee) {
        this.expectActualFee = currentValue;
      } else {
        this.expectActualFee = this.user.availablePayBalance - fee;
      }
    }
  },
  computed: {
    getWithdrawTypeList() {
      let types = "";
      for(let dict of this.dict.type.sys_receive_method) {
        // console.log(dict);
        // console.log(this.convertToCamelCase(dict.value));
        // console.log('enable' + this.capitalizeFirstLetter(this.convertToCamelCase(dict.value)));
        if(this.configWithdraw['enable' + this.capitalizeFirstLetter(this.convertToCamelCase(dict.value))] === 'Y') {
          types += dict.label + "、";
        }
      }
      types = types.slice(0, types.length - 1);
      if(types.length === 0) {
        types = '暂无支持的收款平台';
      }
      return types;
    },
  }
};
</script>
