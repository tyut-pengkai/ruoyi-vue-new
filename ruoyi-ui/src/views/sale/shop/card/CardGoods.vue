<template>
  <el-row :gutter="10">
    <el-col
      v-for="item in data"
      :key="item.id"
      :lg="8"
      :md="12"
      :sm="24"
      :xl="8"
      :xs="24"
    >
      <div class="my-card-group">
        <el-card
          shadow="hover"
          class="my-box-card"
          :class="{ 'my-box-card-select': cardKey == item.id }"
          @click.native="handleSelect(item.id)"
        >
          <div class="my-card-title">
            <p>{{ item.name }}</p>
            <span class="my-card-span" v-if="item.max">
              ￥ {{ item.min }} - {{ item.max }}
            </span>
            <span class="my-card-span" v-else>￥ {{ item.min }}</span>
            <el-tag
              effect="dark"
              size="mini"
              style="margin-left: 5px"
              v-for="item_t in item.tags"
              :key="item_t"
            >
              {{ item_t }}
            </el-tag>
            <div class="my-card-num">
              <div class="my-card-p">
                <p></p>
              </div>
              <span style="margin-left: 100px; font-size: 12px">
                剩余 {{ item.num >= 1000 ? "999+" : item.num }} 件
              </span>
            </div>
          </div>
        </el-card>
      </div>
    </el-col>
  </el-row>
</template>

<script>
export default {
  name: "CardGoods",
  props: {
    data: {
      type: Array,
      default: function () {
        return [];
      },
    },
    cardKey: {
      type: Number,
      default: null,
    },
  },
  methods: {
    handleSelect(key) {
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
  background: #fff !important;
  height: 90px;
}

.my-box-card-select {
  border-color: #3c8ce7;
}

.my-box-card .my-card-title p {
  margin: 0 0 5px 0;
  font-size: 12px;
  font-weight: bold;
  color: #545454;
}

.my-box-card .my-card-title .my-card-span {
  font-weight: 700;
  color: #3c8ce7;
  margin-right: 5px;
  font-size: 14px;
}

.my-card-num {
  margin-bottom: 12px;
}

.my-card-p {
  display: inline-block;
  margin-top: 10px;
  width: 53px;
  height: 5px;
  background: #f8f8f8;
  position: relative;
  border-radius: 3px;
}

.my-card-p p {
  display: inline-block;
  position: absolute;
  width: 100%;
  height: 5px;
  background: linear-gradient(55deg, #65d69e, #31dd92);
  border-radius: 3px;
}
</style>
