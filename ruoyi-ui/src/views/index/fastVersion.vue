<template>
  <el-card style="margin-bottom: 10px" shadow="never">
    <div slot="header" class="clearfix">
      <span>版本直达</span>
    </div>
    <el-row :gutter="20">
      <el-col
        v-for="(card, key) in toolCardList"
        :key="key"
        :span="4"
        :xs="12"
        class="quick-entrance-items"
      >
        <el-card style="width: 126px;height:126px" shadow="hover" @click.native="toTarget(card.appId)">
          <div class="quick-entrance-item">
            <div class="quick-entrance-item-icon" :style="{ backgroundColor: card.bg }">
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
  props: {
    toolCards: {
      type: Array,
      default: [],
    },
  },
  computed: {
    toolCardList() {
      let arr = [];
      for (let toolCard of this.toolCards) {
        arr.push({
          label: toolCard["appName"],
          icon: 'number',
          appId: toolCard["appId"],
          color: '#5cdbd3',
          bg: 'rgba(92, 219, 211,.3)'
        });
      }
      return arr;
    }
  },
  data() {
    return {
    /*toolCards: [
             {
              label: '版本管理-软件1',
              icon: 'number',
              url: '/verify/appVersion?appId=1',
              color: '#5cdbd3',
              bg: 'rgba(92, 219, 211,.3)'
            },
              {
                label: '版本管理-软件2',
                icon: 'number',
                url: '/verify/appVersion?appId=2',
                color: '#ff9c6e',
                bg: 'rgba(255, 156, 110,.3)'
              },
              {
                label: '版本管理-软件3',
                icon: 'number',
                url: '/verify/appVersion?appId=3',
                color: '#ff85c0',
                bg: 'rgba(255, 133, 192,.3)'
              },
              {
             label: '版本管理-软件4',
                icon: 'number',
                url: '/verify/appVersion?appId=4',
                color: '#69c0ff',
                bg: 'rgba(105, 192, 255,.3)'
              },
              {
                label: '软件5',
                icon: 'number',
                url: '/verify/user',
                color: '#b37feb',
                bg: 'rgba(179, 127, 235,.3)'
              },
              {
                label: '软件6',
                icon: 'number',
                url: '/verify/appUser',
                color: '#ffd666',
                bg: 'rgba(255, 214, 102,.3)'
              },
        ],*/
    };
  },
  methods: {
    toTarget(appId) {
      // console.log(name, url)
      // this.$tab.openPage('版本管理-' + name, url);
      this.$router.push({
        path: "/verify/appVersion",
        query: {
          appId: appId,
        },
      });
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
