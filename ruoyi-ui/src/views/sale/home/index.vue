<template>
  <div>{{ show }}</div>
</template>
<script>
// import { getNavInfo } from "@/api/sale/saleShop";
// import { isExternal } from "@/utils/validate";

export default {
  data() {
    return {
      show: "",
    };
  },
  created() {
    setTimeout(() => {
      this.show = "请在后台设置站点首页";
    }, 1000);
  },
};
</script>