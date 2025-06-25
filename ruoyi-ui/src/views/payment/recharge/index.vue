<template>
  <div class="app-container">
    <div class="recharge-title">套餐选择</div>
    
    <div class="device-selector-container">
      <el-select v-model="selectedDeviceId" clearable  placeholder="请选择要充值的设备" style="width: 400px;">
        <el-option
          v-for="device in deviceList"
          :key="device.deviceId"
          :label="device.deviceName + ' (' + device.deviceCode + ')'"
          :value="device.deviceId">
        </el-option>
      </el-select>
    </div>

    <div class="package-list">
      <el-card v-for="pkg in packageList" :key="pkg.packageId" class="package-card" shadow="hover">
        <div slot="header" class="clearfix">
          <span>{{ pkg.name }}</span>
        </div>
        <div class="package-content">
          <div class="price">
            <span class="currency">$</span>
            <span class="amount">{{ pkg.price }}</span>
          </div>
          <div class="hours">时长：{{ pkg.hours }} 小时</div>
          <div class="free-hours" v-if="pkg.freeHours > -1">+ 赠送时长：{{ pkg.freeHours }} 小时</div>
          <div class="description">{{ pkg.remark }}</div>
          <el-button type="primary" @click="handleRecharge(pkg)" class="recharge-button">立即充值</el-button>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import { listPackageForUser } from "@/api/payment/package";
import { createOrder,mockPay } from "@/api/payment/order";
import { listDeviceForCurrentUser } from "@/api/device/info";

export default {
  name: "Recharge",
  data() {
    return {
      packageList: [],
      deviceList: [],
      selectedDeviceId: null,
    };
  },
  created() {
    this.getPackageList();
    this.getDeviceList();
  },
  methods: {
    getPackageList() {
      listPackageForUser().then(response => {
        this.packageList = response.rows;
      });
    },
    getDeviceList() {
      listDeviceForCurrentUser().then(response => {
        this.deviceList = response.rows;
        if (this.deviceList && this.deviceList.length > 0) {
          this.selectedDeviceId = this.deviceList[0].deviceId;
        }
      });
    },
    handleRecharge(pkg) {
      if (!this.selectedDeviceId) {
        this.$message.warning("请先选择一个要充值的设备");
        return;
      }

      this.$confirm(`您确定要充值【${pkg.name}】吗？`, "确认充值", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        const orderRequest = {
          packageId: pkg.packageId,
          deviceId: this.selectedDeviceId
        };
        return createOrder(orderRequest);
      }).then((response) => {
        const order = response.data;
        return this.$alert(`订单创建成功！<br>订单号为：<strong>${order.orderNo}</strong><br>请尽快完成支付。`, "操作成功", {
          dangerouslyUseHTMLString: true,
          confirmButtonText: "去支付",
          type: 'success'
        }).then(() => {
          // 用户点击了"去支付"
          return mockPay(order.orderNo);
        });
      }).then(() => {
        this.$message.success("支付成功！时长已到账。");
      }).catch((err) => {
        // 在浏览器控制台打印详细的错误信息，用于诊断
        //console.error("支付流程捕获到错误:", err);
        // 如果err是 'cancel'，则是用户主动关闭了弹窗，此时可以不提示或用更柔和的方式
        if (err !== 'cancel') {
          // 对于其他错误，比如网络问题或后端返回的错误，使用$message.error
          this.$message.error("操作失败或支付被取消");
        }
      });
    }
  }
};
</script>

<style scoped>
.app-container {
  padding: 20px;
}
.recharge-title {
  font-size: 24px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 20px;
}
.device-selector-container {
  text-align: center;
  margin-bottom: 30px;
}
.package-list {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 20px;
}
.package-card {
  width: 300px;
  text-align: center;
}
.package-content .price {
  font-size: 28px;
  color: #F56C6C;
  margin: 10px 0;
}
.package-content .currency {
  font-size: 16px;
}
.package-content .amount {
  font-weight: bold;
}
.package-content .hours {
  color: #606266;
  margin-bottom: 10px;
}
.package-content .free-hours {
  color: #67C23A;
  font-weight: bold;
  margin-bottom: 10px;
}
.package-content .description {
  color: #909399;
  font-size: 14px;
  min-height: 40px;
  margin-bottom: 20px;
}
.recharge-button {
  width: 100%;
}
</style> 