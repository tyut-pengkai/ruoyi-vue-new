<template>
  <div class="wx-login-container">
    <el-card class="login-card">
      <div slot="header" class="card-header">
        <span>微信小程序登录示例</span>
      </div>
      
      <el-steps :active="currentStep" finish-status="success" simple>
        <el-step title="获取OpenId" description="检查用户是否存在"></el-step>
        <el-step title="获取手机号" description="绑定手机号"></el-step>
        <el-step title="完善信息" description="完善用户信息"></el-step>
        <el-step title="登录成功" description="完成登录"></el-step>
      </el-steps>

      <div class="step-content">
        <!-- 步骤1：获取OpenId -->
        <div v-if="currentStep === 0" class="step-panel">
          <h3>步骤1：获取OpenId</h3>
          <p>小程序首先调用 wx.login() 获取 code，然后调用后台接口检查用户是否存在</p>
          <el-button type="primary" @click="step1GetOpenId">模拟获取OpenId</el-button>
        </div>

        <!-- 步骤2：获取手机号 -->
        <div v-if="currentStep === 1" class="step-panel">
          <h3>步骤2：获取手机号</h3>
          <p>如果用户存在但没有手机号，需要调用 wx.getPhoneNumber() 获取手机号</p>
          <el-button type="primary" @click="step2GetPhone">模拟获取手机号</el-button>
        </div>

        <!-- 步骤3：完善信息 -->
        <div v-if="currentStep === 2" class="step-panel">
          <h3>步骤3：完善用户信息</h3>
          <p>如果用户不存在，需要调用 wx.getUserInfo() 获取用户信息并创建新用户</p>
          <el-button type="primary" @click="step3CompleteUserInfo">模拟完善用户信息</el-button>
        </div>

        <!-- 步骤4：登录成功 -->
        <div v-if="currentStep === 3" class="step-panel">
          <h3>步骤4：登录成功</h3>
          <p>登录成功，获取到token：{{ token }}</p>
          <el-button type="success" @click="resetLogin">重新开始</el-button>
        </div>
      </div>

      <div class="login-info">
        <h4>登录状态信息：</h4>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="当前步骤">{{ currentStep + 1 }}</el-descriptions-item>
          <el-descriptions-item label="OpenId">{{ loginInfo.openId || '未获取' }}</el-descriptions-item>
          <el-descriptions-item label="是否绑定手机号">{{ loginInfo.hasPhone ? '是' : '否' }}</el-descriptions-item>
          <el-descriptions-item label="登录状态">{{ loginInfo.message || '未开始' }}</el-descriptions-item>
          <el-descriptions-item label="Token">{{ token || '未获取' }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-card>
  </div>
</template>

<script>
import { wxStepLogin, wxCompleteUserInfo } from '@/api/login'

export default {
  name: 'WxLogin',
  data() {
    return {
      currentStep: 0,
      token: '',
      loginInfo: {
        openId: '',
        hasPhone: false,
        message: ''
      },
      // 模拟的微信登录数据
      mockWxData: {
        code: 'mock_code_' + Date.now(),
        encryptedData: 'mock_encrypted_data',
        iv: 'mock_iv',
        rawData: 'mock_raw_data',
        signature: 'mock_signature',
        phoneEncryptedData: 'mock_phone_encrypted_data',
        phoneIv: 'mock_phone_iv',
        hasPhoneInfo: false
      }
    }
  },
  methods: {
    // 步骤1：获取OpenId
    async step1GetOpenId() {
      try {
        this.$message.info('正在获取OpenId...')
        
        // 模拟小程序调用 wx.login() 获取 code
        const loginData = {
          code: this.mockWxData.code,
          hasPhoneInfo: false
        }
        
        const response = await wxStepLogin(loginData)
        const result = response.data
        
        this.loginInfo.openId = result.openId
        this.loginInfo.hasPhone = result.hasPhone
        this.loginInfo.message = result.message
        
        // 根据登录状态决定下一步
        if (result.loginStatus === 1) {
          // 登录成功
          this.token = result.token
          this.currentStep = 3
          this.$message.success('登录成功！')
        } else if (result.loginStatus === 0) {
          // 需要获取手机号
          this.currentStep = 1
          this.$message.warning('需要获取手机号')
        } else if (result.loginStatus === 2) {
          // 需要完善用户信息
          this.currentStep = 2
          this.$message.warning('需要完善用户信息')
        }
      } catch (error) {
        this.$message.error('获取OpenId失败：' + error.message)
      }
    },
    
    // 步骤2：获取手机号
    async step2GetPhone() {
      try {
        this.$message.info('正在获取手机号...')
        
        // 模拟小程序调用 wx.getPhoneNumber() 获取手机号
        const loginData = {
          code: this.mockWxData.code,
          encryptedData: this.mockWxData.encryptedData,
          iv: this.mockWxData.iv,
          rawData: this.mockWxData.rawData,
          signature: this.mockWxData.signature,
          phoneEncryptedData: this.mockWxData.phoneEncryptedData,
          phoneIv: this.mockWxData.phoneIv,
          hasPhoneInfo: true
        }
        
        const response = await wxStepLogin(loginData)
        const result = response.data
        
        if (result.loginStatus === 1) {
          // 登录成功
          this.token = result.token
          this.currentStep = 3
          this.loginInfo.message = result.message
          this.$message.success('登录成功！')
        } else {
          this.$message.error('获取手机号失败')
        }
      } catch (error) {
        this.$message.error('获取手机号失败：' + error.message)
      }
    },
    
    // 步骤3：完善用户信息
    async step3CompleteUserInfo() {
      try {
        this.$message.info('正在完善用户信息...')
        
        // 模拟小程序调用 wx.getUserInfo() 和 wx.getPhoneNumber() 获取完整信息
        const loginData = {
          code: this.mockWxData.code,
          encryptedData: this.mockWxData.encryptedData,
          iv: this.mockWxData.iv,
          rawData: this.mockWxData.rawData,
          signature: this.mockWxData.signature,
          phoneEncryptedData: this.mockWxData.phoneEncryptedData,
          phoneIv: this.mockWxData.phoneIv,
          hasPhoneInfo: true
        }
        
        const response = await wxCompleteUserInfo(loginData)
        this.token = response.data.token
        this.currentStep = 3
        this.loginInfo.message = '登录成功'
        this.$message.success('用户信息完善成功，登录成功！')
      } catch (error) {
        this.$message.error('完善用户信息失败：' + error.message)
      }
    },
    
    // 重新开始
    resetLogin() {
      this.currentStep = 0
      this.token = ''
      this.loginInfo = {
        openId: '',
        hasPhone: false,
        message: ''
      }
      this.$message.info('已重置登录流程')
    }
  }
}
</script>

<style lang="scss" scoped>
.wx-login-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.login-card {
  max-width: 800px;
  margin: 0 auto;
}

.card-header {
  text-align: center;
  font-size: 18px;
  font-weight: bold;
}

.step-content {
  margin: 30px 0;
}

.step-panel {
  text-align: center;
  padding: 30px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  background-color: #fafafa;
  
  h3 {
    margin-bottom: 15px;
    color: #303133;
  }
  
  p {
    margin-bottom: 20px;
    color: #606266;
    line-height: 1.6;
  }
}

.login-info {
  margin-top: 30px;
  
  h4 {
    margin-bottom: 15px;
    color: #303133;
  }
}
</style> 