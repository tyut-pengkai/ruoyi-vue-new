<template>
  <el-row :gutter="10">
    <el-col
      v-for="item in data"
      :key="item.id"
      :lg="6"
      :md="8"
      :sm="12"
      :xl="6"
      :xs="12"
    >
      <div class="my-card-group">
        <el-card
          shadow="hover"
          class="my-box-card"
          :class="{ 'my-box-card-select': cardKeyOn == item.id }"
          @click.native="handleSelect(item.id)"
        >
          <div class="my-card-title">
            <p>{{ item.name }}</p>
            <span class="my-card-span">{{ des }}: {{ item.count }}</span>
          </div>
        </el-card>
      </div>
    </el-col>
  </el-row>
</template>

<script>
export default {
  name: "CardCategory",
  data: () => ({
    cardKeyOn: null,
  }),
  props: {
    data: {
      type: Array,
      default: function () {
        return [];
      },
    },
    des: {
      type: String,
      default: "商品数量",
    },
    cardKey: {
      type: Number,
      default: null,
    },
  },
  mounted() {
    this.cardKeyOn = this.$props.cardKey;
  },
  methods: {
    handleSelect(key) {
      this.cardKeyOn = key;
      this.$emit("card-click", key);
    },
  },
};
</script>

<style scoped>
.my-card-group :hover {
  cursor: pointer;
}

.my-card-group {
  margin-top: 4px;
}

.my-box-card {
  border-radius: 10px;
  background: #f8f8f8 !important;
  height: 72px;
}

.my-box-card-select {
  background-image: linear-gradient(
    120deg,
    #3c8ce7 15%,
    #00eaff 80%
  ) !important;
}

.my-box-card .my-card-title p {
  margin: 0 0 5px 0;
  font-size: 12px;
  font-weight: bold;
  color: #545454;
}

.my-box-card .my-card-title .my-card-span {
  font-weight: bold;
  color: #545454;
  font-size: 12px;
}

.my-box-card-select .my-card-title p {
  color: #fff !important;
}

.my-box-card-select .my-card-title .my-card-span {
  color: #fff !important;
  opacity: 0.76;
}
</style>
