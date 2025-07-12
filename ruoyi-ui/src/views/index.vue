<template>
  <div class="app-container">
    <div v-if="loading" class="loading-container">
      <i class="el-icon-loading"></i>
      <p>正在加载用户数据...</p>
    </div>
    <div v-else>
      <div v-if="hasDevice">
        <el-card v-for="device in deviceList" :key="device.deviceId" class="device-card">
          <div slot="header" class="clearfix">
            <span>设备: {{ device.deviceName || device.deviceCode }}</span>
            <el-button class="recharge-button-container" type="primary" size="large" @click="goToRecharge">去充值</el-button>
          </div>
          <el-row :gutter="40" class="panel-group">
            <el-col :xs="24" :sm="12" :lg="8" class="card-panel-col">
              <div class="card-panel">
                <div class="card-panel-description">
                  <div class="card-panel-text">通用时长余量 (小时)</div>
                  <el-progress type="dashboard" :percentage="formatPercentage(device.availProHours, 500)" :color="colors" :format="() => (device.availProHours || 0).toFixed(2)"></el-progress>
                </div>
              </div>
            </el-col>
            <el-col :xs="24" :sm="12" :lg="8" class="card-panel-col">
              <div class="card-panel">
                <div class="card-panel-description">
                  <div class="card-panel-text">总时长余量 (小时)</div>
                  <el-progress type="dashboard" :percentage="formatPercentage(device.availProHours + device.availFreeHours, 500)" :color="colors" :format="() => ((device.availProHours || 0) + (device.availFreeHours || 0)).toFixed(2)"></el-progress>
                </div>
              </div>
            </el-col>
            <el-col :xs="24" :sm="12" :lg="8" class="card-panel-col">
              <div class="card-panel">
                <div class="card-panel-description">
                  <div class="card-panel-text">赠送时长余量 (小时)</div>
                  <el-progress type="dashboard" :percentage="formatPercentage(device.availFreeHours, 500)" :color="colors" :format="() => (device.availFreeHours || 0).toFixed(2)"></el-progress>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-card>
        
      </div>

      <div v-else class="no-device-container" >
        <h2>您当前未绑定任何设备</h2>
        <p>请先绑定您的设备以查看时长信息</p>
        <el-button type="primary" icon="el-icon-plus" @click="goToDevicePage">新增绑定设备</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { listDeviceForCurrentUser } from "@/api/device/info";

export default {
  name: "Index",
  data() {
    return {
      loading: true,
      hasDevice: false,
      deviceList: [],
      colors: [
        { color: '#f56c6c', percentage: 20 },
        { color: '#e6a23c', percentage: 40 },
        { color: '#6f7ad3', percentage: 60 },
        { color: '#1989fa', percentage: 80 },
        { color: '#5cb87a', percentage: 100 }
      ]
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    async fetchData() {
      this.loading = true;
      try {
        const deviceListResponse = await listDeviceForCurrentUser();

        if (deviceListResponse && deviceListResponse.rows && deviceListResponse.rows.length > 0) {
          this.hasDevice = true;
          this.deviceList = deviceListResponse.rows.map(device => {
              return {
                  ...device,
                  availProHours: device.availProHours || 0,
                  availFreeHours: device.availFreeHours || 0
              }
          });
        } else {
          this.hasDevice = false;
        }
      } catch (error) {
        console.error("获取数据失败:", error);
        this.$message.error("加载用户数据失败，请稍后再试。");
        this.hasDevice = false;
      } finally {
        this.loading = false;
      }
    },
    goToDevicePage() {
      this.$router.push({ path: "/device/info" });
    },
    goToRecharge() {
      this.$router.push({ path: "/payment/recharge" });
    },
    formatPercentage(current, max) {
      if (max === 0) return 0;
      const percentage = (current / max) * 100;
      return Math.min(percentage, 100);
    }
  },
};
</script>

<style lang="scss" scoped>
.app-container {
  position: relative;
  min-height: calc(100vh - 84px);
  background-color: #f0f2f5;
  padding: 20px;
}
.loading-container, .no-device-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 60vh;
  text-align: center;
  .el-icon-loading {
    font-size: 48px;
    margin-bottom: 20px;
  }
  h2 {
    font-size: 24px;
    color: #333;
  }
  p {
    font-size: 16px;
    color: #666;
    margin-bottom: 20px;
  }
}

.device-card {
  margin-bottom: 20px;
  background-color: #ffffff;
}

.panel-group {
  margin-top: 18px;
  .card-panel-col {
    margin-bottom: 32px;
  }
  .card-panel {
    height: 230px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);
    display: flex;
    justify-content: center;
    align-items: center;
    
    .card-panel-description {
      text-align: center;
      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }
    }
  }
}

.recharge-button-container {
    text-align: center;
    margin-top: 20px;
}
</style> 