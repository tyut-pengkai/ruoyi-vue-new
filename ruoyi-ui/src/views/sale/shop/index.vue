<template>
  <div class="home">
    <el-alert title="公告" type="success" :closable="false">
      <p>本站为INAMS演示站。所有商品仅供测试。并无实际商品。请悉知。</p>
      <p>

      </p>
    </el-alert>
    <el-card class="box-card" style="margin-top: 15px">
      <div class="my-title">
        <img src="../../../assets/images/sort.svg"/>&nbsp;<span>选择分类</span>
      </div>
      <card-select
        style="margin-top: 15px"
        v-on:card-click="handleCategorySelect"
        :data="categoryData"
        :cardKey="categoryId"
      >
      </card-select>
      <el-divider></el-divider>

    </el-card>


  </div>
</template>

<script>
import CardSelect from "./card/CardSelect";
//import CardSelectShop from "./card/CardSelectShop";
//import CardPay from "./card/CardPay";
export default {
  components: { CardSelect },
  data() {
    return {
      categoryId: 1,
      categoryData: [
        { id: 1, name: "测试分类", count: 2 },
        { id: 2, name: "[官方]推荐服务", count: 3 },
        { id: 3, name: "[官方]话费充值", count: 198 },
        { id: 4, name: "[官方]影视专区", count: 185 },
        { id: 5, name: "[官方]点卷专区", count: 185 },
      ],
    };
  },
  methods: {
    handleCategorySelect(id) {
    // 拉取当前选择目录下的商品列表
      console.log(id);
    },
  }
}
</script>

<style>
.my-title span {
  font-weight: 600;
  font-size: 18px;
  color: #545454;
}

.my-title img {
    vertical-align: bottom;
}
</style>
