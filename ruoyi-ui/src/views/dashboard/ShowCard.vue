<template>
  <div>
    <el-card shadow="never">
      <div slot="header" class="card-title">
        <span>{{ title }}</span>
        <el-tag v-if="tag" :hit="true" :type="tagType" effect="plain" size="mini" style="float: right;">{{ tag }}
        </el-tag>
      </div>
      <div>
        <span v-if="type=='p'" class="card-s-p-fix">￥ </span>
        <count-to :decimals="decimals" :duration="2600" :end-val="value" :start-val="0" class="card-num"/>
        <span v-if="type=='s'" class="card-s-p-fix"> 笔</span>
      </div>
    </el-card>
  </div>
</template>
<script>
import CountTo from 'vue-count-to'

export default {
  name: "ShowCard",
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
    type: {
      type: String,
      default: "",
    }
  },
  computed: {
    decimals() {
      return this.type == 'p' ? 2 : 0;
    }
  }
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
