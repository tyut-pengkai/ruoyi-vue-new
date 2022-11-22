<template>
  <div>{{ show }}</div>
</template>
<script>
import {getNavInfo} from "@/api/sale/saleShop";
import {isExternal} from "@/utils/validate";

export default {
  data() {
    return {
      show: "",
    };
  },
  created() {
    if (this.$store.state.settings.enableFrontEnd == "Y") {
      this.getNavigation();
    } else {
      this.$router.replace("stop");
    }
    // setTimeout(() => {
    //   this.show = "请在后台设置站点首页";
    // }, 1000);
  },
  methods: {
    getNavigation() {
      getNavInfo().then((res) => {
        let flag = false;
        this.navList = res.data;
        for (let item of res.data) {
          if (item.index) {
            if (isExternal(item.path)) {
              window.location = item.path;
            } else {
              this.$router.replace(item.path);
            }
            flag = true;
            break;
          }
          this.activeIndexNum++;
          this.activeIndex = this.activeIndexNum.toString();
        }
        if (!flag) {
          this.show = "请在后台设置站点首页";
        }
      });
    },
  },
};
</script>
