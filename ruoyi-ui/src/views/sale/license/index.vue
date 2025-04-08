<template>
  <div class="home">
    <el-alert :closable="false" title="公告" type="info">
      <p>感谢您购买红叶网络验证与软件管理系统</p>
    </el-alert>
    <el-card class="box-card" style="max-width: 90vw; margin-top: 15px">
      <div class="my-title">
        <img src="../../../assets/images/category.svg"/>&nbsp;
        <span>激活/续费/恢复授权</span>
      </div>
      <el-tabs style="margin-top: 20px">
        <el-tab-pane label="通过网站地址操作（需部署在公网，公网可访问）">
          <el-alert :closable="false" type="info">
            <p>
              以下方式可二选一，v0.0.9版本及以上推荐【在线激活】方式，否则请选择【下载授权】方式
            </p>
            <p>
              点击【在线激活】后您的服务器将自动获得授权（授权绑定服务器，可更换域名）
            </p>
            <p>
              点击【下载授权】后您将获得授权文件(.lic)，请将授权文件重命名为license.lic并覆盖红叶验证安装目录内的同名文件即可（授权绑定服务器，可更换域名）
            </p>
          </el-alert>
          <div style="max-width: 90vw; width: 500px; margin: 20px auto">
            <el-form ref="formByWebUrl" :model="formByWebUrl" :rules="rules">
              <el-form-item label="授权码" prop="loginCode">
                <el-input
                  v-model="formByWebUrl.loginCode"
                  clearable
                  show-word-limit
                  style="max-width: 75vw"
                ></el-input>
              </el-form-item>
              <el-form-item
                label="网站域名/IP地址（请输入以http(s)开头的完整域名或IP）"
                prop="webUrl"
              >
                <el-input
                  v-model="formByWebUrl.webUrl"
                  clearable
                  show-word-limit
                  style="max-width: 75vw"
                ></el-input>
              </el-form-item>
              <div align="center">
                <el-button
                  :loading="exportLoading"
                  round
                  @click="submitForm('formByWebUrl', 0)"
                >下载授权
                </el-button>
                <el-button
                  round
                  type="primary"
                  :loading="injectLoading"
                  @click="submitForm('formByWebUrl', 1)"
                >在线激活(推荐)
                </el-button>
                <!-- <el-button @click="resetForm('formByWebUrl')">清空输入</el-button> -->
              </div>
            </el-form>
          </div>
        </el-tab-pane>
        <el-tab-pane label="通过设备码操作（部署在局域网，公网不可访问）">
          <el-alert :closable="false" type="info">
            <p>
              点击【下载授权】后您将获得授权文件(.lic)，请将授权文件重命名为license.lic并覆盖红叶验证安装目录内的同名文件即可（授权绑定服务器，可更换域名）
            </p>
          </el-alert>
          <div style="max-width: 90vw; width: 500px; margin: 20px auto">
            <el-form
              ref="formByDeviceCode"
              :model="formByDeviceCode"
              :rules="rules"
            >
              <el-form-item label="授权码" prop="loginCode">
                <el-input
                  v-model="formByDeviceCode.loginCode"
                  clearable
                  show-word-limit
                  style="max-width: 75vw"
                ></el-input>
              </el-form-item>
              <el-form-item
                label="设备码（格式如xxxx-xxxx-xxxx-xxxx-xxxx-xxxx-xxxx-xxxx）"
                prop="deviceCode"
              >
                <el-input
                  v-model="formByDeviceCode.deviceCode"
                  clearable
                  maxlength="39"
                  show-word-limit
                  style="max-width: 75vw"
                ></el-input>
              </el-form-item>
              <div align="center">
                <el-button
                  :loading="exportLoading"
                  round
                  type="primary"
                  @click="submitForm('formByDeviceCode')"
                >下载授权
                </el-button>
                <!-- <el-button @click="resetForm('formByDeviceCode')">清空输入</el-button> -->
              </div>
            </el-form>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import {genLicenseFileByDeviceCode, genLicenseFileByWebUrl,} from "@/api/license/licenseRecord";

export default {
  name: "GetLicense",
  data() {
    return {
      formByWebUrl: {
        loginCode: null,
        webUrl: null,
      },
      formByDeviceCode: {
        loginCode: null,
        deviceCode: null,
      },
      rules: {
        loginCode: [
          {required: true, message: "请输入您购买的授权码", trigger: "blur"},
        ],
        deviceCode: [
          {
            required: true,
            message: "请输入您的服务器设备码",
            trigger: "blur",
          },
          {min: 39, max: 39, message: "长度为39个字符", trigger: "blur"},
        ],
        webUrl: [
          {
            required: true,
            message: "请输入您的验证的访问地址",
            trigger: "blur",
          },
        ],
      },
      exportLoading: false,
      injectLoading: false,
    };
  },
  methods: {
    getLicenseByWebUrl(type) {
      const queryParams = {
        loginCode: this.formByWebUrl.loginCode,
        webUrl: this.formByWebUrl.webUrl,
        type: type,
      };
      this.$modal
        .confirm("是否确认激活授权？")
        .then(() => {
          if (type == 0) {
            this.exportLoading = true;
          } else {
            this.injectLoading = true;
          }
          return genLicenseFileByWebUrl(queryParams);
        })
        .then((response) => {
          // console.log(type);
          if (type == 0) {
            this.$download.name(response.msg);
            this.exportLoading = false;
          } else {
            this.$alert(
              response.msg,
              {
                dangerouslyUseHTMLString: true,
              }
            );
            // this.$modal.alertSuccess(response.msg);
            this.injectLoading = false;
          }
        })
        .catch(() => {
          this.exportLoading = false;
          this.injectLoading = false;
        });
    },
    getLicenseByDeviceCode() {
      const queryParams = {
        loginCode: this.formByDeviceCode.loginCode,
        deviceCode: this.formByDeviceCode.deviceCode,
      };
      this.$modal
        .confirm("是否确认下载授权？")
        .then(() => {
          this.exportLoading = true;
          return genLicenseFileByDeviceCode(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
          this.exportLoading = false;
        })
        .catch(() => {
          this.exportLoading = false;
          this.injectLoading = false;
        });
    },
    submitForm(formName, type) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (formName == "formByWebUrl") {
            this.getLicenseByWebUrl(type);
          } else if (formName == "formByDeviceCode") {
            this.getLicenseByDeviceCode();
          }
        } else {
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
  },
  beforeRouteLeave(to, from, next) {
    // console.log("/license to----", to); //跳转后路由
    // console.log("/license from----", from); //跳转前路由
    if(from.params.appUrl && from.params.appUrl !== '' && to.path.indexOf('/a/') === -1) {
      // console.log("/license newto.path----", to.path + '/a/' + from.params.appUrl);
      next({'path': to.path + '/a/' + from.params.appUrl})
    }
    if(from.params.cardUrl && from.params.cardUrl !== '' && to.path.indexOf('/c/') === -1) {
      // console.log("/license newto.path----", to.path + '/c/' + from.params.cardUrl);
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
