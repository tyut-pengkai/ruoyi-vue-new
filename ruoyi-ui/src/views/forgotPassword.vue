<template>
  <div class="login">
    <el-form ref="form" :model="form" :rules="rules" class="login-form">
      <h3 class="title">{{title}}</h3>
      
      <!-- Step 1 & 2: Email -->
      <el-form-item prop="email">
        <el-input
          v-model="form.email"
          type="text"
          auto-complete="off"
          placeholder="请输入您注册时使用的邮箱"
          :disabled="step === 2"
        >
          <svg-icon slot="prefix" icon-class="email" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      
      <!-- Step 2: Code, New Password, Confirm Password -->
      <div v-if="step === 2">
        <el-form-item prop="code">
          <el-input
            v-model="form.code"
            type="text"
            auto-complete="off"
            placeholder="请输入邮箱验证码"
          >
            <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            type="password"
            auto-complete="off"
            placeholder="请输入新密码"
          >
            <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
          </el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input
            v-model="form.confirmPassword"
            type="password"
            auto-complete="off"
            placeholder="请确认新密码"
          >
            <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
          </el-input>
        </el-form-item>
      </div>
      
      <!-- Buttons -->
      <el-form-item style="width:100%;">
        <el-button
          v-if="step === 1"
          :loading="loading"
          size="medium"
          type="primary"
          style="width:100%;"
          @click.native.prevent="handleSendCode"
        >
          <span v-if="!loading">发送验证码</span>
          <span v-else>发送中...</span>
        </el-button>
        <el-button
          v-if="step === 2"
          :loading="loading"
          size="medium"
          type="primary"
          style="width:100%;"
          @click.native.prevent="handleResetPassword"
        >
          <span v-if="!loading">确认重置</span>
          <span v-else>重置中...</span>
        </el-button>
        <div style="float: right;">
          <router-link class="link-type" :to="'/login'">返回登录</router-link>
        </div>
      </el-form-item>
    </el-form>
    <!--  底部  -->
    <div class="el-login-footer">
      <span>{{footer}}</span>
    </div>
  </div>
</template>

<script>
import { sendResetPasswordCode, resetPasswordByCode } from "@/api/login";

export default {
  name: "ForgotPassword",

  data() {
    const validatePass2 = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入密码"));
      } else if (value !== this.form.password) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };
    return {
      title: "忘记密码",
      footer: process.env.VUE_APP_FOOTER,
      step: 1, // 1: send code, 2: reset password
      form: {
        email: "",
        code: "",
        password: "",
        confirmPassword: ""
      },
      rules: {
        email: [
          { required: true, message: "请输入邮箱地址", trigger: "blur" },
          { type: "email", message: "请输入正确的邮箱地址", trigger: ["blur", "change"] }
        ],
        code: [
            { required: true, message: "请输入验证码", trigger: "blur" }
        ],
        password: [
            { required: true, message: "请输入新密码", trigger: "blur" },
            { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
        ],
        confirmPassword: [
            { required: true, trigger: "blur", validator: validatePass2 }
        ]
      },
      loading: false
    };
  },
  methods: {
    handleSendCode() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.loading = true;
          sendResetPasswordCode({ email: this.form.email }).then(response => {
            this.$message.success("验证码已发送，请注意查收");
            this.step = 2;
            this.loading = false;
          }).catch(() => {
            this.loading = false;
          });
        }
      });
    },
    handleResetPassword() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.loading = true;
          resetPasswordByCode(this.form).then(response => {
            this.$alert("密码重置成功，请使用新密码登录。", "提示", {
              confirmButtonText: "确定",
              callback: action => {
                this.$router.push("/login");
              }
            });
          }).catch(() => {
            this.loading = false;
          });
        }
      });
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-image: url("../assets/images/login-background.jpg");
  background-size: cover;
}
.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #707070;
}
.login-form {
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 25px 25px 5px 25px;
  .el-input {
    height: 38px;
    input {
      height: 38px;
    }
  }
  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
  }
}
.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}
.link-type {
    color: #409eff;
    text-decoration: none;
}
</style> 