<template>
  <div id="app">
    <router-view />
    <theme-picker />
  </div>
</template>

<script>
import {getWebsiteConfig} from "@/api/system/website";
import ThemePicker from "@/components/ThemePicker";

export default {
  name: "App",
  components: { ThemePicker },
    metaInfo() {
        return {
      title:
        this.$store.state.settings.dynamicTitle &&
        this.$store.state.settings.title,
      titleTemplate: (title) => {
        // return title ? `${title} - ${process.env.VUE_APP_TITLE}` : process.env.VUE_APP_TITLE
        return title ? `${title} - ${this.title}` : this.title;
      },
      meta: [
        { vmid: "description", name: "description", content: this.description },
        { vmid: "keywords", name: "keywords", content: this.keywords },
      ],
    };
  },
  data() {
    return {
      title: "",
      description: "",
      keywords: "",
    };
  },
  created() {
    this.initData();
  },
  methods: {
    initData() {
      getWebsiteConfig().then((res) => {
        let websiteName = res.data.name || "";
        let websiteShortName = res.data.shortName || "";
        let shopName = res.data.shopName || "";
        let websiteLogo = res.data.logo || "";
        let icp = res.data.icp || "";
        let domain = res.data.domain || "";
        let pageSize = res.data.pageSize || "10";
        pageSize = Number(pageSize);
        let description = res.data.description || "";
        let keywords = res.data.keywords || "";
        this.title = websiteName;
        this.description = description;
        this.keywords = keywords;
        document.title = websiteName;
        let enableFrontEnd = res.data.enableFrontEnd || "Y";
        this.$store.dispatch("settings/setWebsiteName", websiteName);
        this.$store.dispatch("settings/setWebsiteShortName", websiteShortName);
        this.$store.dispatch("settings/setShopName", shopName);
        this.$store.dispatch("settings/setWebsiteLogo", websiteLogo);
        this.$store.dispatch("settings/setIcp", icp);
        this.$store.dispatch("settings/setPageSize", pageSize);
        this.$store.dispatch("settings/setEnableFrontEnd", enableFrontEnd);
        this.$store.dispatch("settings/setDomain", domain);
        if (res.data.favicon) {
          var faviconurl = res.data.favicon; //这里可以是动态的获取的favicon的地址
          var link =
            document.querySelector("link[rel*='icon']") ||
            document.createElement("link");
          link.type = "image/x-icon";
          link.rel = "shortcut icon";
          link.href = process.env.VUE_APP_BASE_API + faviconurl;
          document.getElementsByTagName("head")[0].appendChild(link);
        }
      });
    },
  },
};
</script>
<style scoped>
#app .theme-picker {
  display: none;
}
</style>
