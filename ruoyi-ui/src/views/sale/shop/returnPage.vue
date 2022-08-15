<template>
  <div>
    <el-result
      icon="success"
      subTitle="请点击下方按钮根据提示进行操作"
      title="支付完成"
    >
      <template slot="extra">
        <el-button
          :loading="loading"
          onclick="window.close()"
          size="medium"
          type="primary"
        >获取支付结果
        </el-button
        >
      </template>
    </el-result>
  </div>
</template>
<script>
import {callReturn} from "@/api/sale/saleShop";

export default {
  data() {
    return {
      loading: true,
    };
  },
  created() {
    var code = this.$route.params.code;
    var url = decodeURIComponent(location.search);
    callReturn(code, url)
      .then((response) => {
        if (response == "success") {
        }
      })
      .finally(() => {
        this.loading = false;
      });
  },
};
</script>
