<template>
  <div>hello</div>
</template>

<script>
export default {
  created() {
    const appId = this.$route.params && this.$route.params.appId;
    const appName = this.$route.query && this.$route.query.appName;
    const title = "软件用户";
    const route = Object.assign({}, this.$route, {
      title: `${title}-${appName}`,
    });
    this.$store.dispatch("tagsView/updateVisitedView", route);
  },
};
</script>