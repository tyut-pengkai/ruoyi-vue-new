<template>
  <div class="login">
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
      <h3 class="title">{{title}}</h3>
      <el-form-item prop="email">
        <el-input
          v-model="loginForm.email"
          type="text"
          auto-complete="off"
          placeholder="请输入邮箱"
        >
          <svg-icon slot="prefix" icon-class="email" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="emailCode">
        <el-input
          v-model="loginForm.emailCode"
          auto-complete="off"
          placeholder="邮箱验证码"
          style="width: 63%"
          @keyup.enter.native="handleEmailLogin"
        >
          <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
        </el-input>
        <el-button :disabled="emailCodeBtnDisabled" @click="sendEmailCode" style="width: 35%; margin-left: 2%">{{ emailCodeBtnText }}</el-button>
      </el-form-item>
      <el-form-item style="width:100%;">
        <el-button
          :loading="loading"
          size="medium"
          type="primary"
          style="width:100%;"
          @click.native.prevent="handleEmailLogin"
        >
          <span v-if="!loading">登 录</span>
          <span v-else>登 录 中...</span>
        </el-button>
        <div style="padding-top: 10px;">
            <span style="font-size: 12px; color: #999;">提示：未注册的邮箱将自动创建账户并登录</span>
        </div>
        <div style="float: right;" v-if="register">
          <router-link class="link-type" :to="'/register'">立即注册</router-link>
          <span style="margin: 0 10px;">|</span>
          <router-link class="link-type" :to="'/login'">账号密码登录</router-link>
        </div>
      </el-form-item>
    </el-form>
    <!--  底部  -->
    <div class="el-login-footer">
      <span>Copyright © 2025 aivi.vip All Rights Reserved.</span>
    </div>
  </div>
</template>

<script>
import { sendEmailCode, emailLogin } from '@/api/register'
import { setToken } from '@/utils/auth'

export default {
  name: "EmailLogin",
  data() {
    return {
      title: process.env.VUE_APP_TITLE + " - 邮箱验证码登录",
      loginForm: {
        email: '',
        emailCode: ''
      },
      loginRules: {
        email: [
          { required: true, trigger: "blur", message: "请输入邮箱" },
          { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
        ],
        emailCode: [
          { required: true, trigger: "blur", message: "请输入邮箱验证码" }
        ]
      },
      loading: false,
      // 注册开关
      register: true,
      redirect: undefined,
      emailCodeBtnText: '获取验证码',
      emailCodeBtnDisabled: false,
      emailCodeTimer: null,
      emailCodeCountdown: 60
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  methods: {
    handleEmailLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          emailLogin({
            username: this.loginForm.email,
            code: this.loginForm.emailCode
          }).then(res => {
            // 存储token
            setToken(res.token)
            this.$store.commit('SET_TOKEN', res.token)
            this.$router.push({ path: this.redirect || "/" }).catch(()=>{})
            // 获取用户信息
            // this.$store.dispatch('GetInfo').then(() => {
            //   this.$router.push({ path: this.redirect || "/" }).catch(()=>{})
            // }).catch(() => {
            //   this.loading = false
            // })
          }).catch(() => {
            this.loading = false
          })
        }
      })
    },
    sendEmailCode() {
      if (!this.loginForm.email) {
        this.$message.error('请输入邮箱');
        return;
      }
      this.emailCodeBtnDisabled = true;
      sendEmailCode({ email: this.loginForm.email }).then(() => {
        this.$message.success('验证码已发送，请查收邮箱');
        this.startEmailCodeTimer();
      }).catch(() => {
        this.emailCodeBtnDisabled = false;
      })
    },
    startEmailCodeTimer() {
      this.emailCodeCountdown = 60;
      this.emailCodeBtnText = `${this.emailCodeCountdown}s后重试`;
      this.emailCodeBtnDisabled = true;
      this.emailCodeTimer = setInterval(() => {
        this.emailCodeCountdown--;
        if (this.emailCodeCountdown <= 0) {
          clearInterval(this.emailCodeTimer);
          this.emailCodeBtnText = '获取验证码';
          this.emailCodeBtnDisabled = false;
        } else {
          this.emailCodeBtnText = `${this.emailCodeCountdown}s后重试`;
        }
      }, 1000);
    }
  },
  beforeDestroy() {
    if (this.emailCodeTimer) {
      clearInterval(this.emailCodeTimer);
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
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
  z-index: 1;
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
.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
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
</style> 