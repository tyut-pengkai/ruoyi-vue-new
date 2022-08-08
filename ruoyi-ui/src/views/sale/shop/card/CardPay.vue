<template>
  <el-row :gutter="10">
    <el-col
      v-for="item in data"
      :key="item.id"
      :lg="5"
      :md="8"
      :sm="12"
      :xl="5"
      :xs="12"
    >
      <div class="my-card-group">
        <el-card
          :class="{ 'my-box-card-select': cardKeyOn == item.id }"
          class="my-box-card"
          shadow="hover"
          @click.native="handleSelect(item.id)"
        >
          <div class="my-card-pay">
            <div :class="{ addon: addon }" class="my-card-title">
              <img :src="handleImgPath(item.img)"/>
              <p>{{ item.name }}</p>
            </div>
          </div>
        </el-card>
      </div>
    </el-col>
  </el-row>
</template>

<script>
export default {
  name: "CardPay",
  data: () => ({
    cardKeyOn: 0,
  }),
  props: {
    data: {
      type: Array,
      default: function () {
        return [];
      },
    },
    cardKey: {
      type: Number,
      default: 0,
    },
    addon: {
      type: Boolean,
      default: false,
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
    handleImgPath(img) {
      return require("../../../../assets/images/" + img + ".svg");
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
  background: #fff !important;
  height: 52px;
}

.my-box-card-select {
  border-color: #3c8ce7;
}

.my-card-pay p {
  display: inline-block;
  margin-left: 3px !important;
}

.my-card-pay .my-card-title p {
  margin: 3px 0 5px 0;
  font-size: 12px;
  font-weight: bold;
  color: #545454;
}

.my-card-pay .my-card-title i {
  font-size: 24px;
}

.my-card-pay .my-card-title {
  align-items: center;
  text-align: center;
  display: flex;
  /* margin-bottom: 12px; */
  /* margin-top: -10px; */
}

.addon {
  margin-top: -10px;
}
</style>
