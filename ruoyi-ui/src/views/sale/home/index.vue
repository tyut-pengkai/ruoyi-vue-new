<template>
  <div>{{ show }}</div>
</template>
<script>
import {isExternal} from "@/utils/validate";
import { Message } from 'element-ui'

export default {
  data() {
    return {
      show: "",
      navList: [],
    };
  },
  created() {
    if(this.$store.state.settings.enableFrontEnd.length === 0) {
      this.$store.dispatch('settings/GetEnableFrontEnd').then((res) => {
        if (res.data.enableFrontEnd === "Y") {
          this.getNavigation();
        } else {
          this.$router.replace("stop");
        }
      }).catch(err => {
        Message.error(err)
      })
    } else {
      if (this.$store.state.settings.enableFrontEnd === "Y") {
        this.getNavigation();
      } else {
        this.$router.replace("stop");
      }
    }
    // setTimeout(() => {
    //   this.show = "请在后台设置站点首页";
    // }, 1000);
  },
  methods: {
    getNavigation() {
      if(this.$store.state.settings.navList.length === 0) {
        this.$store.dispatch('settings/GetNavList').then((res) => {
          this.navList = res.data;
          let flag = false;
          for (let item of this.navList) {
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
        }).catch(err => {
          Message.error(err)
        })
      } else {
        this.navList = this.$store.state.settings.navList;
        let flag = false;
        for (let item of this.navList) {
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
      }
    },
  },
};
</script>
