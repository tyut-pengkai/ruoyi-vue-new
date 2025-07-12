<template>
  <div class="register">
    <el-form ref="registerForm" :model="registerForm" :rules="registerRules" class="register-form">
      <h3 class="title">{{title}}</h3>
      <el-form-item prop="username">
        <el-input 
          v-model="registerForm.username" 
          type="text" 
          auto-complete="off" 
          placeholder="账号"
          @blur="checkUsername"
          :class="{ 'is-success': usernameStatus === 'success', 'is-error': usernameStatus === 'error' }"
        >
          <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
          <i slot="suffix" :class="usernameStatus === 'success' ? 'el-icon-check' : usernameStatus === 'error' ? 'el-icon-close' : ''" :style="{ color: usernameStatus === 'success' ? '#67C23A' : usernameStatus === 'error' ? '#F56C6C' : '' }"></i>
        </el-input>
      </el-form-item>
      <el-form-item prop="email">
        <el-input 
          v-model="registerForm.email" 
          type="email" 
          auto-complete="off" 
          placeholder="邮箱"
          @blur="checkEmail"
          :class="{ 'is-success': emailStatus === 'success', 'is-error': emailStatus === 'error' }"
        >
          <svg-icon slot="prefix" icon-class="email" class="el-input__icon input-icon" />
          <i slot="suffix" :class="emailStatus === 'success' ? 'el-icon-check' : emailStatus === 'error' ? 'el-icon-close' : ''" :style="{ color: emailStatus === 'success' ? '#67C23A' : emailStatus === 'error' ? '#F56C6C' : '' }"></i>
        </el-input>
      </el-form-item>
      <el-form-item prop="emailCode">
        <el-input
          v-model="registerForm.emailCode"
          auto-complete="off"
          placeholder="邮箱验证码"
          style="width: 63%"
        >
          <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
        </el-input>
        <div class="register-code">
          <el-button 
            type="primary" 
            size="small" 
            :disabled="emailCodeSending || !registerForm.email || emailStatus !== 'success'"
            @click="sendEmailCode"
            style="width: 100%; height: 38px;"
          >
            {{ emailCodeSending ? '发送中...' : '发送验证码' }}
          </el-button>
        </div>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model="registerForm.password"
          type="password"
          auto-complete="off"
          placeholder="密码"
          @keyup.enter.native="handleRegister"
        >
          <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="confirmPassword">
        <el-input
          v-model="registerForm.confirmPassword"
          type="password"
          auto-complete="off"
          placeholder="确认密码"
          @keyup.enter.native="handleRegister"
        >
          <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="code" v-if="captchaEnabled">
        <el-input
          v-model="registerForm.code"
          auto-complete="off"
          placeholder="验证码"
          style="width: 63%"
          @keyup.enter.native="handleRegister"
        >
          <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
        </el-input>
        <div class="register-code">
          <img :src="codeUrl" @click="getCode" class="register-code-img"/>
        </div>
      </el-form-item>
      <el-form-item style="width:100%;">
        <el-button
          :loading="loading"
          size="medium"
          type="primary"
          style="width:100%;"
          :disabled="usernameStatus !== 'success' || emailStatus !== 'success'"
          @click.native.prevent="handleRegister"
        >
          <span v-if="!loading">注 册</span>
          <span v-else>注 册 中...</span>
        </el-button>
        <div style="float: right;">
          <router-link class="link-type" :to="'/login'">使用已有账户登录</router-link>
        </div>
      </el-form-item>
    </el-form>
    <!--  底部  -->
    <div class="el-register-footer">
      <span>{{footer}}</span>
    </div>
  </div>
</template>

<script>
import { getCodeImg } from "@/api/login"
import { sendEmailCode } from "@/api/email"
import { register, checkUsernameUnique, checkEmailUnique } from "@/api/register"

export default {
  name: "Register",
  data() {
    const equalToPassword = (rule, value, callback) => {
      if (this.registerForm.password !== value) {
        callback(new Error("两次输入的密码不一致"))
      } else {
        callback()
      }
    }
    return {
      title: "注 册",
      footer: process.env.VUE_APP_FOOTER,
      codeUrl: "",
      registerForm: {
        username: "",
        email: "",
        emailCode: "",
        password: "",
        confirmPassword: "",
        code: "",
        uuid: ""
      },
      registerRules: {
        username: [
          { required: true, trigger: "blur", message: "请输入您的账号" },
          { min: 2, max: 20, message: '用户账号长度必须介于 2 和 20 之间', trigger: 'blur' },
          { pattern: /^[a-zA-Z0-9_]+$/, message: '用户名只能包含字母、数字和下划线', trigger: 'blur' }
        ],
        email: [
          { required: true, trigger: "blur", message: "请输入您的邮箱" },
          { type: "email", message: "请输入正确的邮箱地址", trigger: "blur" }
        ],
        emailCode: [
          { required: true, trigger: "blur", message: "请输入邮箱验证码" },
          { len: 6, message: "验证码为6位数字", trigger: "blur" }
        ],
        password: [
          { required: true, trigger: "blur", message: "请输入您的密码" },
          { min: 5, max: 20, message: "用户密码长度必须介于 5 和 20 之间", trigger: "blur" },
          { pattern: /^[^<>"'|\\]+$/, message: "不能包含非法字符：< > \" ' \\\ |", trigger: "blur" }
        ],
        confirmPassword: [
          { required: true, trigger: "blur", message: "请再次输入您的密码" },
          { required: true, validator: equalToPassword, trigger: "blur" }
        ],
        code: [{ required: true, trigger: "change", message: "请输入验证码" }]
      },
      loading: false,
      emailCodeSending: false,
      captchaEnabled: true,
      usernameStatus: '', // success, error, ''
      emailStatus: '' // success, error, ''
    }
  },
  created() {
    this.getCode()
  },
  methods: {
    getCode() {
      getCodeImg().then(res => {
        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled
        if (this.captchaEnabled) {
          this.codeUrl = "data:image/gif;base64," + res.img
          this.registerForm.uuid = res.uuid
        }
      })
    },
    // 校验用户名唯一性
    checkUsername() {
      if (!this.registerForm.username) {
        this.usernameStatus = ''
        return
      }
      
      // 先进行本地校验
      if (!/^[a-zA-Z0-9_]+$/.test(this.registerForm.username)) {
        this.usernameStatus = 'error'
        return
      }
      
      if (this.registerForm.username.length < 2 || this.registerForm.username.length > 20) {
        this.usernameStatus = 'error'
        return
      }
      
      // 调用后端接口校验
      checkUsernameUnique({ username: this.registerForm.username }).then(res => {
        this.usernameStatus = 'success'
      }).catch(() => {
        this.usernameStatus = 'error'
      })
    },
    // 校验邮箱唯一性
    checkEmail() {
      if (!this.registerForm.email) {
        this.emailStatus = ''
        return
      }
      
      // 先进行本地校验
      if (!/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(this.registerForm.email)) {
        this.emailStatus = 'error'
        return
      }
      
      // 调用后端接口校验
      checkEmailUnique({ email: this.registerForm.email }).then(res => {
        this.emailStatus = 'success'
      }).catch(() => {
        this.emailStatus = 'error'
      })
    },
    sendEmailCode() {
      if (!this.registerForm.email) {
        this.$message.error("请先输入邮箱地址")
        return
      }
      
      if (this.emailStatus !== 'success') {
        this.$message.error("请先校验邮箱地址")
        return
      }
      
      this.emailCodeSending = true
      sendEmailCode({ email: this.registerForm.email }).then(res => {
        this.$message.success("验证码发送成功，请查收邮件")
      }).catch(() => {
        this.$message.error("验证码发送失败，请稍后重试")
      }).finally(() => {
        this.emailCodeSending = false
      })
    },
    handleRegister() {
      this.$refs.registerForm.validate(valid => {
        if (valid) {
          // 额外校验用户名和邮箱状态
          if (this.usernameStatus !== 'success') {
            this.$message.error("请先校验用户名")
            return
          }
          
          if (this.emailStatus !== 'success') {
            this.$message.error("请先校验邮箱")
            return
          }
          
          this.loading = true
          register(this.registerForm).then(res => {
            const username = this.registerForm.username
            this.$alert("<font color='red'>恭喜你，您的账号 " + username + " 注册成功！</font>", '系统提示', {
              dangerouslyUseHTMLString: true,
              type: 'success'
            }).then(() => {
              this.$router.push("/login")
            }).catch(() => {})
          }).catch(() => {
            this.loading = false
            if (this.captchaEnabled) {
              this.getCode()
            }
          })
        }
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
.register {
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

.register-form {
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
.register-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.register-code {
  width: 33%;
  height: 38px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-register-footer {
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
.register-code-img {
  height: 38px;
}
</style>
