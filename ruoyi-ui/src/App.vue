<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
import {getWebsiteConfig} from "@/api/system/website";

export default {
  name: 'App',
  metaInfo() {
    return {
      title: this.$store.state.settings.dynamicTitle && this.$store.state.settings.title,
      titleTemplate: title => {
        // return title ? `${title} - ${process.env.VUE_APP_TITLE}` : process.env.VUE_APP_TITLE
        return title ? `${title} - ${this.title}` : this.title
      }
    }
  },
  data() {
    return {
      title: ""
    }
  },
  created() {
    this.initData()
  },
  methods: {
    initData() {
      getWebsiteConfig().then((res) => {
        this.title = res.data.name || "";
        document.title = this.title;
        if (res.data.favicon) {
          var faviconurl = res.data.favicon; //这里可以是动态的获取的favicon的地址
          var link = document.querySelector("link[rel*='icon']") || document.createElement('link');
          link.type = 'image/x-icon';
          link.rel = 'shortcut icon';
          link.href = process.env.VUE_APP_BASE_API + faviconurl;
          document.getElementsByTagName('head')[0].appendChild(link);
        }
      });
    }
  }
}
</script>
