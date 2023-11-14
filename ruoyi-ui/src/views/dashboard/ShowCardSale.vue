<template>
  <div>
    <el-card shadow="never">
      <div slot="header" class="card-title">
        <i :class="icon" style="margin-right: 5px"></i>
        <span>{{ title }}</span>
        <el-tag v-if="tag" :hit="true" :type="tagType" effect="plain" size="mini" style="float: right;">{{ tag }}
        </el-tag>
      </div>
      <div>
        <el-row>
          <el-col :span="11">
            <span class="card-s-p-fix">￥ </span>
            <count-to :decimals="2" :duration="2600" :end-val="value" :start-val="0" class="card-num"/>
          </el-col>
          <el-col :span="2" style="font-size: 36px;">
            <el-divider direction="vertical"></el-divider>
          </el-col>
          <el-col :span="11">
            <count-to :decimals="0" :duration="2600" :end-val="trade" :start-val="0" class="card-num"/>
            <span class="card-s-p-fix"> 笔</span>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>
<script>
import CountTo from 'vue-count-to'

export default {
  name: "ShowCardSale",
  components: {
    CountTo,
  },
  props: {
    title: {
      type: String,
      default: "",
    },
    tag: {
      type: String,
      default: "",
    },
    tagType: {
      type: String,
      default: "success",
    },
    value: {
      type: Number,
      default: 0,
    },
    icon: {
      type: String,
      default: "",
    },
    trade: {
      type: Number,
      default: 0,
    }
  },
  computed: {}
}
</script>
<style lang="scss" scoped>
.card-title {
  font-size: 14px;
  margin-top: -5px;
  color: #595959;
}

.card-s-p-fix {
  font-size: 20px;
  font-weight: bold;
  color: #666;
}

.card-num {
  color: #666;
  background: #fff;
  box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
  border-color: rgba(0, 0, 0, .05);
  font-size: 36px;
}
</style>
