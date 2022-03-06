<template>
  <div class="home">
    <el-alert :closable="false" title="公告" type="success">
      <p>只支持查询最近5笔订单</p>
    </el-alert>
    <el-card class="box-card" style="margin-top: 15px">
      <div class="my-title">
        <img src="../../../assets/images/category.svg"/>&nbsp;
        <span>订单查询</span>
      </div>
      <el-tabs style="margin-top: 20px">
        <el-tab-pane label="订单号查询">订单号查询</el-tab-pane>
        <el-tab-pane label="联系方式查询">联系方式查询</el-tab-pane>
        <el-tab-pane label="浏览器缓存查询">浏览器缓存查询</el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
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
