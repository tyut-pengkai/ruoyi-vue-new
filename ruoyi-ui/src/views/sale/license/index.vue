<template>
  <div class="home">
    <el-alert :closable="false" title="公告" type="success">
      <p>感谢您购买红叶网络验证与软件管理系统</p>
    </el-alert>
    <el-card class="box-card" style="max-width: 90vw; margin-top: 15px">
      <div class="my-title">
        <img src="../../../assets/images/category.svg"/>&nbsp;
        <span>新增授权</span>
      </div>
      <el-tabs style="margin-top: 20px">
        <el-tab-pane label="通过网站域名授权">
          <el-alert :closable="false" type="success">
            <p>
              兑换后您将获得授权文件(.lic)，请将授权文件重命名为license.lic并覆盖红叶验证安装目录内的同名文件即可（授权绑定服务器，可更换域名）
            </p>
          </el-alert>
          <div style="max-width: 90vw; width: 500px; margin: 20px auto">
            <el-form ref="formByWebUrl" :model="formByWebUrl" :rules="rules">
              <el-form-item label="兑换码" prop="loginCode">
                <el-input
                  v-model="formByWebUrl.loginCode"
                  clearable
                  show-word-limit
                  style="max-width: 75vw"
                ></el-input>
              </el-form-item>
              <el-form-item
                label="网站域名（请输入以http(s)开头的完整域名）"
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
                  round
                  type="primary"
                  @click="submitForm('formByWebUrl')"
                >立即兑换
                </el-button>
                <!-- <el-button @click="resetForm('formByWebUrl')">清空输入</el-button> -->
              </div>
            </el-form>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import {genLicenseFileByWebUrl} from "@/api/license/licenseRecord";

export default {
  name: "GetLicense",
  data() {
    return {
      formByWebUrl: {
        loginCode: null,
        webUrl: null,
      },
      rules: {
        loginCode: [
          {required: true, message: "请输入您购买的兑换码", trigger: "blur"},
        ],
        webUrl: [
          {
            required: true,
            message: "请输入您的验证的访问地址",
            trigger: "blur",
          },
        ],
      },
    };
  },
  methods: {
    getLicenseByWebUrl() {
      const queryParams = {
        loginCode: this.formByWebUrl.loginCode,
        webUrl: this.formByWebUrl.webUrl,
      };
      this.$modal
        .confirm("是否确认兑换？")
        .then(() => {
          this.exportLoading = true;
          return genLicenseFileByWebUrl(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
          this.exportLoading = false;
        })
        .catch(() => {
        });
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (formName == "formByWebUrl") {
            this.getLicenseByWebUrl();
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
