<template>
  <el-card style="margin-bottom: 10px" shadow="never">
    <div slot="header" class="clearfix">
      <span>快捷入口</span>
    </div>
    <el-row :gutter="20">
      <el-col
        v-for="(card, key) in toolCards"
        :key="key"
        :span="4"
        :xs="12"
        class="quick-entrance-items"
      >
        <el-card style="width: 150px;height:150px" shadow="hover" @click.native="toTarget(card.label, card.url)">
          <div class="quick-entrance-item">
            <div class="quick-entrance-item-icon" :style="{ backgroundColor: card.bg, marginTop: '15px' }">
              <svg-icon style="width: 24px; height:24px" :icon-class="card.icon" :style="{ color: card.color }" />
            </div>
            <p>{{ card.label }}</p>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </el-card>
</template>

<script>
export default {
  name: "FastEntrance",
  data() {
    return {
      toolCards: [
        {
          label: '在线用户',
          icon: 'monitor',
          url: '/online/online',
          color: '#ff9c6e',
          bg: 'rgba(255, 156, 110,.3)'
        },
        {
          label: '软件管理',
          icon: 'modular',
          url: '/verify/app',
          color: '#69c0ff',
          bg: 'rgba(105, 192, 255,.3)'
        },
        {
          label: '账号管理',
          icon: 'good',
          url: '/verify/user',
          color: '#b37feb',
          bg: 'rgba(179, 127, 235,.3)'
        },
        {
          label: '软件用户',
          icon: 'success',
          url: '/verify/appUser',
          color: '#ffd666',
          bg: 'rgba(255, 214, 102,.3)'
        },
        {
          label: '交易订单',
          icon: 'file',
          url: '/sale/saleOrder',
          color: '#ff85c0',
          bg: 'rgba(255, 133, 192,.3)'
        },
        {
          label: '个人中心',
          icon: 'home',
          url: '/user/profile',
          color: '#5cdbd3',
          bg: 'rgba(92, 219, 211,.3)'
        }],
    };
  },
  methods: {
    toTarget(name, url) {
      console.log(name, url)
      this.$tab.openPage(name, url);
    }
  }
}
</script>

<style scoped lang="scss">
.quick-entrance-items {
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  color: #333;
}

.quick-entrance-item {
  margin-top: 5px;
  border-radius: 4px;
  cursor: pointer;
  height: auto;
  text-align: center;
}

.quick-entrance-item-icon {
  width: 64px;
  height: 64px!important;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto;
}
</style>
