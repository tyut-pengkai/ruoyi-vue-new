<template>
  <footer v-if="visible" class="copyright" :class="{
    'inner': inner,
  }">
    <span>{{ content }}</span>
  </footer>
</template>

<script>
export default {
  props: {
    // 是否内页copyright
    inner: {
      type: Boolean,
      default: true
    }
  },
  computed: {
    visible() {
      return this.$store.state.settings.footerVisible
    },
    content() {
      return this.$store.state.settings.footerContent
    }
  }
}
</script>

<style scoped>
.inner {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 36px;
  padding: 10px 20px;
  text-align: right;
  background-color: #f8f8f8;
  color: #666;
  font-size: 14px;
  border-top: 1px solid #e7e7e7;
  z-index: 999;
}
</style>