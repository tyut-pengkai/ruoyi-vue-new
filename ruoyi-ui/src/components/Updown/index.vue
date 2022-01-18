<template>
  <div>
    <el-collapse-transition name="el-fade-in">
      <div v-show="status">
        <slot></slot>
      </div>
    </el-collapse-transition>
    <div style="text-align: center">
      <el-button type="primary" autofocus size="mini" @click="toggle">
        {{ status ? "收起" : "展开更多" }}
        <i :class="status ? 'el-icon-arrow-up' : 'el-icon-arrow-down'"></i>
      </el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: "Updown",
  components: {},
  props: {},
  data() {
    return {
      status: false,
    };
  },
  computed: {},
  created() {},
  mounted() {},
  watch: {},
  methods: {
    toggle() {
      this.status = !this.status;
    },
  },
};
</script>

<style scoped lang="scss">
</style>