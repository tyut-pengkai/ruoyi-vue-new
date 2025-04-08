<template>
  <div class="home">
    <!-- <el-alert :closable="false" title="公告" type="info">
      <p>感谢您购买红叶网络验证与软件管理系统</p>
    </el-alert> -->
    <el-card class="box-card" style="max-width: 90vw; margin-top: 15px">
      <div class="my-title">
        <img src="../../../assets/images/category.svg"/>&nbsp;
        <span>充值续费</span>
      </div>
      <el-tabs style="margin-top: 20px" @tab-click="tabChange">
        <el-tab-pane label="使用登录码充值" v-if="authType === '0' || authType === 'all'">
          <div style="max-width: 90vw; width: 500px; margin: 20px auto">
            <el-form ref="formLoginCode" :model="formLoginCode" :rules="rules">
              <el-form-item label="目标软件" prop="appId">
                <el-select
                  v-model="formLoginCode.appId"
                  filterable
                  placeholder="请选择"
                  prop="appId"
                >
                  <el-option
                    v-for="item in appList"
                    :key="item.appId"
                    :label="item.appName"
                    :value="item.appId"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="充值到的登录码" prop="loginCode">
                <el-input
                  v-model="formLoginCode.loginCode"
                  clearable
                  show-word-limit
                  style="max-width: 75vw"
                ></el-input>
              </el-form-item>
              <el-form-item label="用于充值的新登录码" prop="loginCodeNew">
                <el-input
                  v-model="formLoginCode.loginCodeNew"
                  clearable
                  show-word-limit
                  style="max-width: 75vw"
                ></el-input>
              </el-form-item>
              <div align="center">
                <el-button
                  :loading="loading"
                  round
                  type="primary"
                  @click="submitForm('formLoginCode', 2)"
                >
                  确认充值
                </el-button>
                <!-- <el-button @click="resetForm('formLoginCode')">清空输入</el-button> -->
              </div>
            </el-form>
          </div>
        </el-tab-pane>
        <el-tab-pane label="使用卡密充值" v-if="authType === '1' || authType === 'all'">
          <div style="max-width: 90vw; width: 500px; margin: 20px auto">
            <el-form ref="formCard" :model="formCard" :rules="rules">
              <el-form-item label="目标软件" prop="appId">
                <el-select
                  v-model="formCard.appId"
                  filterable
                  placeholder="请选择"
                  prop="appId"
                >
                  <el-option
                    v-for="item in appList"
                    :key="item.appId"
                    :label="item.appName"
                    :value="item.appId"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-col :span="19">
                  <el-form-item label="用户账号" prop="username">
                    <el-input
                      v-model="formCard.username"
                      clearable
                      show-word-limit
                      style="max-width: 75vw"
                    >
                    </el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="5">
                  <el-form-item prop="validPassword">
                    <el-checkbox
                      v-model="formCard.validPassword"
                      false-label="0"
                      label="校验密码"
                      style="margin-top: 35px; margin-left: 20px"
                      true-label="1"
                    ></el-checkbox>
                  </el-form-item>
                </el-col>
              </el-form-item>
              <el-form-item
                v-show="formCard.validPassword == '1'"
                label="用户密码"
                prop="password"
              >
                <el-input
                  v-model="formCard.password"
                  clearable
                  show-word-limit
                  style="max-width: 75vw"
                ></el-input>
              </el-form-item>
              <el-form-item label="充值卡号" prop="cardNo">
                <el-input
                  v-model="formCard.cardNo"
                  clearable
                  show-word-limit
                  style="max-width: 75vw"
                ></el-input>
              </el-form-item>
              <el-form-item label="充值卡密码" prop="cardPass">
                <el-input
                  v-model="formCard.cardPass"
                  clearable
                  show-word-limit
                  style="max-width: 75vw"
                ></el-input>
              </el-form-item>
              <div align="center">
                <el-button
                  :loading="loading"
                  round
                  type="primary"
                  @click="submitForm('formCard', 1)"
                >
                  确认充值
                </el-button>
                <!-- <el-button @click="resetForm('formCard')">清空输入</el-button> -->
              </div>
            </el-form>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import { chargeCard, getAppAuthType, listApp } from '@/api/sale/saleShop'

export default {
  name: "ChargeCenter",
  data() {
    return {
      appList: [],
      loading: false,
      card: null,
      formCard: {
        appId: null,
        username: "",
        validPassword: "0",
        password: "",
        cardNo: null, // "tbkgdzgaN3jhnnT23JKj",
        cardPass: null, // "ZN6w395WnnXJrq3uxoAg",
      },
      formLoginCode: {
        appId: null,
        loginCode: null, // "OSURn3OhatUVX56PpmsH",
        loginCodeNew: null, // "OSURn3OhatUVX56PpmsH",
      },
      rules: {
        appId: [{required: true, message: "请选择目标软件", trigger: "blur"}],
        username: [
          {
            required: true,
            message: "请输入要充值的用户账号",
            trigger: "blur",
          },
        ],
        validPassword: [
          {required: true, message: "请选择是否要校验密码", trigger: "blur"},
        ],
        cardNo: [
          {required: true, message: "请输入充值卡号", trigger: "blur"},
        ],
        cardPass: [
          {
            required: false,
            message: "请输入充值卡密码",
            trigger: "blur",
          },
        ],
        loginCode: [
          {required: true, message: "请输入登录码", trigger: "blur"},
        ],
        loginCodeNew: [
          {required: true, message: "请输入新登录码", trigger: "blur"},
        ],
      },
      appUrl: null,
      cardUrl: null,
      authType: 'all',
      filterAppId: 0,
    };
  },
  created() {
    this.appUrl = this.$route.params.appUrl;
    this.cardUrl = this.$route.params.cardUrl;
    if((this.appUrl && this.appUrl !== '') || (this.cardUrl && this.cardUrl !== '')) {
      getAppAuthType({shopUrl: this.appUrl, cardUrl: this.cardUrl}).then((response) => {
        this.authType = response.data;
        this.filterAppId = response.appId;
        this.loadAppList();
      })
    } else {
      this.loadAppList();
    }
  },
  methods: {
    loadAppList() {
      if(this.authType === 'all') {
        this.getAppList(1, 0);
      } else {
        this.getAppList(this.authType, this.filterAppId);
        if(this.authType === '0') {
          this.formLoginCode.appId = this.filterAppId
        } else if(this.authType === '1') {
          this.formCard.appId = this.filterAppId
        }
      }
    },
    /** 查询软件列表 */
    getAppList(authType, filterAppId) {
      listApp({authType: authType, enableFeCharge: "Y", appId: filterAppId}).then((response) => {
        this.appList = response.rows;
      });
    },
    chargeCard(type) {
      const queryParams = {
        appId: this.formCard.appId,
        username: this.formCard.username,
        validPassword: this.formCard.validPassword,
        password: this.formCard.password,
        cardNo: this.formCard.cardNo,
        cardPass: this.formCard.cardPass,
        queryType: type,
      };
      this.loading = true;
      chargeCard(queryParams)
        .then((response) => {
          this.$alert(response.msg, "系统提示");
        })
        .finally(() => {
          this.loading = false;
        });
    },
    chargeLoginCode(type) {
      const queryParams = {
        appId: this.formLoginCode.appId,
        username: this.formLoginCode.loginCode,
        cardNo: this.formLoginCode.loginCodeNew,
        validPassword: "0",
        queryType: type,
      };
      this.loading = true;
      chargeCard(queryParams)
        .then((response) => {
          this.$alert(response.msg, "系统提示");
        })
        .finally(() => {
          this.loading = false;
        });
    },
    submitForm(formName, type) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (formName == "formCard") {
            this.chargeCard(type);
          } else if (formName == "formLoginCode") {
            this.chargeLoginCode(type);
          }
        } else {
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    tabChange(data) {
      this.getAppList(data.index == 0 ? "1" : "0");
    },
  },
  beforeRouteLeave(to, from, next) {
    // console.log("/chargeCenter to----", to); //跳转后路由
    // console.log("/chargeCenter from----", from); //跳转前路由
    if(from.params.appUrl && from.params.appUrl !== '' && to.path.indexOf('/a/') === -1) {
      // console.log("/chargeCenter newto.path----", to.path + '/a/' + from.params.appUrl);
      next({'path': to.path + '/a/' + from.params.appUrl})
    }
    if(from.params.cardUrl && from.params.cardUrl !== '' && to.path.indexOf('/c/') === -1) {
      // console.log("/chargeCenter newto.path----", to.path + '/c/' + from.params.cardUrl);
      next({'path': to.path + '/c/' + from.params.cardUrl})
    }
    next();
  },
};
</script>

<style>
body {
  margin: 0;
}

a {
  text-decoration: None;
}

.home {
  width: 90vw;
  max-width: 1100px;
}

.my-title span {
  font-weight: 600;
  font-size: 18px;
  color: #545454;
}

.my-title img {
  vertical-align: bottom;
}

.customClass {
  max-width: 1000px;
}
</style>
