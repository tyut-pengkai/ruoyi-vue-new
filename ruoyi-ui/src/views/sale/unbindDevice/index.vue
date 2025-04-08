<template>
  <div class="home">
    <!-- <el-alert :closable="false" title="公告" type="info">
      <p>感谢您购买红叶网络验证与软件管理系统</p>
    </el-alert> -->
    <el-card class="box-card" style="max-width: 90vw; margin-top: 15px">
      <div class="my-title">
        <img src="../../../assets/images/category.svg" />&nbsp;
        <span>解绑设备</span>
      </div>
      <el-tabs style="margin-top: 20px" @tab-click="tabChange">
        <el-tab-pane label="登录码方式登录" v-if="authType === '0' || authType === 'all'">
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
                  查询设备列表
                </el-button>
                <!-- <el-button @click="resetForm('formLoginCode')">清空输入</el-button> -->
              </div>
            </el-form>
          </div>
        </el-tab-pane>
        <el-tab-pane label="账号方式登录" v-if="authType === '1' || authType === 'all'">
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
              <el-form-item label="用户账号" prop="username">
                <el-input
                  v-model="formCard.username"
                  clearable
                  show-word-limit
                  style="max-width: 75vw"
                >
                </el-input>
              </el-form-item>
              <el-form-item label="用户密码" prop="password">
                <el-input
                  v-model="formCard.password"
                  clearable
                  show-word-limit
                  style="max-width: 75vw"
                  type="password"
                ></el-input>
              </el-form-item>
              <div align="center">
                <el-button
                  :loading="loading"
                  round
                  type="primary"
                  @click="submitForm('formCard', 1)"
                >
                  查询设备列表
                </el-button>
                <!-- <el-button @click="resetForm('formCard')">清空输入</el-button> -->
              </div>
            </el-form>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- Table -->
    <!-- <el-button type="text" @click="dialogTableVisible = true">
      模拟订单展示
    </el-button> -->
    <el-dialog
      :visible.sync="dialogTableVisible"
      custom-class="customClass"
      style="margin-top: 10vh; height: 80%"
      title="该用户绑定设备信息如下"
      width="auto"
    >
      <el-card style="max-width: 90vw; margin-bottom: 10px">
        <el-table :data="deviceCodeList" border>
          <el-table-column align="center" label="" type="index" />
          <el-table-column label="设备码">
            <template slot-scope="scope">
              {{ scope.row.deviceCodeStr }}
            </template>
          </el-table-column>
          <el-table-column label="设备码状态">
            <template slot-scope="scope">
              <dict-tag
                :options="dict.type.sys_normal_disable"
                :value="scope.row.status"
              />
            </template>
          </el-table-column>
          <el-table-column label="登录次数">
            <template slot-scope="scope">
              {{ scope.row.loginTimes }}
            </template>
          </el-table-column>
          <el-table-column label="最后登录时间">
            <template slot-scope="scope">
              {{ scope.row.lastLoginTime }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150">
            <template slot-scope="scope">
              <el-button
                size="small"
                type="text"
                @click="handleUnbind(scope.row)"
                >解绑此设备
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </el-dialog>
  </div>
</template>

<script>
import { getAppAuthType, listApp, queryBindDevice, unbindDevice } from '@/api/sale/saleShop'

export default {
  name: "UnbindDevice",
  dicts: ["sys_normal_disable"],
  data() {
    return {
      dialogTableVisible: false,
      deviceCodeList: [],
      appList: [],
      loading: false,
      card: null,
      formCard: {
        appId: null,
        // username: "admin",
        // password: "admin123",
        username: "",
        password: "",
      },
      formLoginCode: {
        appId: null,
        loginCode: null, //"OSURn3OhatUVX56PpmsH",
      },
      rules: {
        appId: [{ required: true, message: "请选择目标软件", trigger: "blur" }],
        username: [
          {
            required: true,
            message: "请输入要解绑的用户账号",
            trigger: "blur",
          },
        ],
        password: [
          {
            required: true,
            message: "请输入要解绑的用户密码",
            trigger: "blur",
          },
        ],
        loginCode: [
          { required: true, message: "请输入要解绑的登录码", trigger: "blur" },
        ],
      },
      appInfo: {
        billType: null,
        reduceQuotaUnbind: null,
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
      listApp({ authType: authType, enableUnbind: "Y", appId: filterAppId}).then((response) => {
        this.appList = response.rows;
      });
    },
    queryDeviceCard(type) {
      const queryParams = {
        appId: this.formCard.appId,
        username: this.formCard.username,
        password: this.formCard.password,
        queryType: type,
      };
      this.loading = true;
      queryBindDevice(queryParams)
        .then((response) => {
          this.deviceCodeList = response.data;
          this.appInfo["billType"] = response.billType;
          this.appInfo["reduceQuotaUnbind"] = response.reduceQuotaUnbind;
          this.appInfo["unbindTimes"] = response.unbindTimes;
          this.appInfo["enableUnbindByQuota"] = response.enableUnbindByQuota;
          this.dialogTableVisible = true;
        })
        .finally(() => {
          this.loading = false;
        });
    },
    queryDeviceLoginCode(type) {
      const queryParams = {
        appId: this.formLoginCode.appId,
        username: this.formLoginCode.loginCode,
        queryType: type,
      };
      this.loading = true;
      queryBindDevice(queryParams)
        .then((response) => {
          this.deviceCodeList = response.data;
          this.appInfo["billType"] = response.billType;
          this.appInfo["reduceQuotaUnbind"] = response.reduceQuotaUnbind;
          this.appInfo["unbindTimes"] = response.unbindTimes;
          this.appInfo["enableUnbindByQuota"] = response.enableUnbindByQuota;
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
            this.queryDeviceCard(type);
          } else if (formName == "formLoginCode") {
            this.queryDeviceLoginCode(type);
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
    makeTip(billType, reduceQuotaUnbind) {
      var tip = "";
      tip += "解绑将扣除您剩余";
      if (billType == 0) {
        tip += "时长";
      } else {
        tip += "点数";
      }
      tip += reduceQuotaUnbind;
      if (billType == 0) {
        tip += "秒";
      } else {
        tip += "点";
      }
      return tip;
    },
    handleUnbind(row) {
      var tip = "";
      if (this.appInfo["unbindTimes"] > 0) {
        tip =
          "提示：您剩余解绑次数为：" +
          this.appInfo["unbindTimes"] +
          "次，次数用完后";
        if (this.appInfo["enableUnbindByQuota"] == "Y") {
          tip += "每次";
          tip += this.makeTip(
            this.appInfo["billType"],
            this.appInfo["reduceQuotaUnbind"]
          );
        } else {
          tip += "将无法再次解绑";
        }
      } else {
        if (this.appInfo["enableUnbindByQuota"] == "Y") {
          if (this.appInfo["reduceQuotaUnbind"] > 0) {
            tip = "提示：本次";
            tip += this.makeTip(
              this.appInfo["billType"],
              this.appInfo["reduceQuotaUnbind"]
            );
          } else {
            tip = "提示：本次解绑将不会扣除您的剩余";
            if (this.appInfo["billType"] == 0) {
              tip += "时长";
            } else {
              tip += "点数";
            }
          }
        } else {
          tip = "提示：无法再次解绑";
        }
      }
      this.$modal
        .confirm("是否确认解绑此设备？" + tip)
        .then(() => {
          // console.log("确认");
          var data = {
            id: row.id,
          };
          unbindDevice(data)
            .then((response) => {
              if (response.code == 200) {
                // console.log(response)
                this.$alert("解绑成功", "系统提示");
              } else {
                this.$alert("解绑失败", "系统提示");
              }
            })
            .finally(() => {});
        })
        .catch(() => {});
    },
  },
  beforeRouteLeave(to, from, next) {
    // console.log("/unbindDevice to----", to); //跳转后路由
    // console.log("/unbindDevice from----", from); //跳转前路由
    if(from.params.appUrl && from.params.appUrl !== '' && to.path.indexOf('/a/') === -1) {
      // console.log("/unbindDevice newto.path----", to.path + '/a/' + from.params.appUrl);
      next({'path': to.path + '/a/' + from.params.appUrl})
    }
    if(from.params.cardUrl && from.params.cardUrl !== '' && to.path.indexOf('/c/') === -1) {
      // console.log("/unbindDevice newto.path----", to.path + '/c/' + from.params.cardUrl);
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
