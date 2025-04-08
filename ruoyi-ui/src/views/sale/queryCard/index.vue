<template>
  <div class="home">
    <!-- <el-alert :closable="false" title="公告" type="info">
      <p>感谢您购买红叶网络验证与软件管理系统</p>
    </el-alert> -->
    <el-card class="box-card" style="max-width: 90vw; margin-top: 15px">
      <div class="my-title">
        <img src="../../../assets/images/category.svg"/>&nbsp;
        <span>查询登录码/卡密</span>
      </div>
      <el-tabs style="margin-top: 20px">
        <el-tab-pane label="查询登录码" v-if="authType === '0' || authType === 'all'">
          <div style="max-width: 90vw; width: 500px; margin: 20px auto">
            <el-form ref="formLoginCode" :model="formLoginCode" :rules="rules">
              <el-form-item label="登录码" prop="loginCode">
                <el-input
                  v-model="formLoginCode.loginCode"
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
                  查询
                </el-button>
                <!-- <el-button @click="resetForm('formLoginCode')">清空输入</el-button> -->
              </div>
            </el-form>
          </div>
        </el-tab-pane>
        <el-tab-pane label="查询卡密" v-if="authType === '1' || authType === 'all'">
          <div style="max-width: 90vw; width: 500px; margin: 20px auto">
            <el-form ref="formCard" :model="formCard" :rules="rules">
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
                  查询
                </el-button>
                <!-- <el-button @click="resetForm('formCard')">清空输入</el-button> -->
              </div>
            </el-form>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <el-dialog
      v-if="card"
      :title="title"
      :visible.sync="dialogTableVisible"
      custom-class="customClass"
      style="margin-top: 10vh; height: 80%"
      width="auto"
    >
      <el-card style="max-width: 90vw; margin-bottom: 10px">
        <el-form>
          <el-form-item>
            <el-col :span="12">
              <el-form-item :label="title2">
                <span>{{ card.cardNo }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item v-show="card.cardPass" label="充值卡密码">
                <span>{{ card.cardPass }}</span>
              </el-form-item>
            </el-col>
          </el-form-item>
          <el-form-item>
            <el-col :span="12">
              <el-form-item label="所属软件">
                <span>{{ card.appName }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="计费类型">
                <dict-tag
                  :options="dict.type.sys_bill_type"
                  :value="card.billType"
                />
              </el-form-item>
            </el-col>
          </el-form-item>
          <el-form-item>
            <el-col :span="12">
              <el-form-item label="卡密名称">
                <span>{{ card.cardName }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="卡密状态">
                <dict-tag
                  :options="dict.type.sys_normal_disable"
                  :value="card.status"
                />
              </el-form-item>
            </el-col>
          </el-form-item>
          <el-form-item>
            <el-col :span="12">
              <el-form-item label="卡密面值">
                <span>{{ parseSeconds(card.billType, card.quota) }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="是否已用">
                <dict-tag
                  :options="dict.type.sys_yes_no"
                  :value="card.isCharged"
                />
              </el-form-item>
            </el-col>
          </el-form-item>
          <el-form-item>
            <el-col :span="12">
              <el-form-item label="充值过期时间" prop="expireTime">
                <span>{{
                    card.expireTime == "9999-12-31 23:59:59"
                      ? "长期有效"
                      : card.expireTime
                  }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item v-show="card.chargeRule" label="充值规则">
                <dict-tag
                  :options="dict.type.sys_charge_rule"
                  :value="card.chargeRule"
                />
              </el-form-item>
            </el-col>
          </el-form-item>
          <el-form-item>
            <el-col :span="12">
              <el-form-item label="可多开用户数">
                <span>{{
                    card.cardLoginLimitU == -2
                      ? "不生效"
                      : card.cardLoginLimitU == -1
                        ? "不限制"
                        : card.cardLoginLimitU
                  }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="可多开设备数">
                <span>{{
                    card.cardLoginLimitM == -2
                      ? "不生效"
                      : card.cardLoginLimitM == -1
                        ? "不限制"
                        : card.cardLoginLimitM
                  }}</span>
              </el-form-item>
            </el-col>
          </el-form-item>
          <el-form-item>
            <el-col :span="24">
              <div v-show="card.isCharged == 'Y'">
                <el-form-item label="用户过期时间">
                  <span>{{ card.userExpireTime ? card.userExpireTime : "无关联用户，如已被使用，则代表此卡已被用于充值其他登录码" }}</span>
                </el-form-item>
              </div>
            </el-col>
          </el-form-item>
        </el-form>
      </el-card>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogTableVisible = false">
          确 定
        </el-button>
        <el-button @click="dialogTableVisible = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getAppAuthType, queryCard } from '@/api/sale/saleShop'
import {parseSeconds, parseUnit} from "@/utils/my";

export default {
  name: "QueryCard",
  dicts: [
    "sys_yes_no",
    "sys_normal_disable",
    "sys_charge_rule",
    "sys_bill_type",
  ],
  data() {
    return {
      loading: false,
      dialogTableVisible: false,
      card: null,
      title: null,
      title2: null,
      formCard: {
        cardNo: null, //"tbkgdzgaN3jhnnT23JKj", //
        cardPass: null, //"ZN6w395WnnXJrq3uxoAg", //
      },
      formLoginCode: {
        loginCode: null, //"OSURn3OhatUVX56PpmsH",
      },
      rules: {
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
      },
      appUrl: null,
      cardUrl: null,
      authType: 'all',
    };
  },
  methods: {
    queryCard(type) {
      const queryParams = {
        cardNo: this.formCard.cardNo,
        cardPass: this.formCard.cardPass,
        queryType: type,
      };
      this.loading = true;
      queryCard(queryParams)
        .then((response) => {
          this.title = "您所查询的卡密信息如下";
          this.title2 = "充值卡号";
          this.card = response.data;
          this.dialogTableVisible = true;
        })
        .finally(() => {
          this.loading = false;
        });
    },
    queryLoginCode(type) {
      const queryParams = {
        cardNo: this.formLoginCode.loginCode,
        queryType: type,
      };
      this.loading = true;
      queryCard(queryParams)
        .then((response) => {
          this.title = "您所查询的登录码信息如下";
          this.title2 = "登录码";
          this.card = response.data;
          this.dialogTableVisible = true;
        })
        .finally(() => {
          this.loading = false;
        });
    },
    submitForm(formName, type) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (formName == "formCard") {
            this.queryCard(type);
          } else if (formName == "formLoginCode") {
            this.queryLoginCode(type);
          }
        } else {
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    parseSeconds(billType, seconds) {
      if (billType === "0") {
        let parse = parseSeconds(seconds);
        return parse[0] + parseUnit(parse[1]);
      } else {
        return seconds + "点";
      }
    },
  },
  created() {
    this.appUrl = this.$route.params.appUrl;
    this.cardUrl = this.$route.params.cardUrl;
    if((this.appUrl && this.appUrl !== '') || (this.cardUrl && this.cardUrl !== '')) {
      getAppAuthType({shopUrl: this.appUrl, cardUrl: this.cardUrl}).then((response) => {
        this.authType = response.data;
      })
    }
  },
  beforeRouteLeave(to, from, next) {
    // console.log("/queryCard to----", to); //跳转后路由
    // console.log("/queryCard from----", from); //跳转前路由
    if(from.params.appUrl && from.params.appUrl !== '' && to.path.indexOf('/a/') === -1) {
      // console.log("/queryCard newto.path----", to.path + '/a/' + from.params.appUrl);
      next({'path': to.path + '/a/' + from.params.appUrl})
    }
    if(from.params.cardUrl && from.params.cardUrl !== '' && to.path.indexOf('/c/') === -1) {
      // console.log("/queryCard newto.path----", to.path + '/c/' + from.params.cardUrl);
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
