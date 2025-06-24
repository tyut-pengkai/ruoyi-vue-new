<template>
  <div class="app-container">
    <div class="recharge-title">套餐选择</div>
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
          <div class="description">{{ pkg.remark }}</div>
          <el-button type="primary" @click="handleRecharge(pkg)" class="recharge-button">立即充值</el-button>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import { listPackageForUser } from "@/api/payment/package";
import { createOrder } from "@/api/payment/order";

export default {
  name: "Recharge",
  data() {
    return {
      packageList: [],
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      listPackageForUser().then(response => {
        this.packageList = response.rows;
      });
    },
    handleRecharge(pkg) {
      this.$confirm(`您确定要充值【${pkg.name}】吗？`, "确认充值", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        return createOrder({ packageId: pkg.packageId });
      }).then((response) => {
        const order = response.data;
        this.$alert(`订单创建成功！<br>订单号为：<strong>${order.orderNo}</strong><br>请尽快完成支付。`, "操作成功", {
          dangerouslyUseHTMLString: true,
          type: 'success'
        });
      }).catch(() => {
        // 用户取消或API调用失败
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