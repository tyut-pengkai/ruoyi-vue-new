<template>
  <div>
    <el-card shadow="never">
      <div slot="header" class="card-title">
        <i :class="icon" style="margin-right: 5px"></i>
        <span>{{ title }}</span>
        <el-tag v-if="tag" :hit="true" :type="tagType" effect="plain" size="mini" style="float: right;">{{ tag }}</el-tag>
        <div v-if="info">
          <el-tooltip :content="info" placement="top">
            <i :class="infoIcon" style="margin-top: -17px;float: right;"></i>
          </el-tooltip>
        </div>
      </div>
      <div>
        <span v-if="type==='p'" class="card-s-p-fix">￥ </span>
        <count-to :decimals="decimals" :duration="2600" :end-val="value" :start-val="0" class="card-num"/>
        <span v-if="type==='s'" class="card-s-p-fix"> 笔</span>
      </div>
      <div>
        <slot></slot>
        <el-divider></el-divider>
        <span>{{bottomText}}</span>
      </div>
    </el-card>
  </div>
</template>
<script>
import CountTo from 'vue-count-to'

export default {
  name: "ShowCardIndex",
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
    info: {
      type: String,
      default: "",
    },
    infoIcon: {
      type: String,
      default: "info",
    },
    value: {
      type: Number,
      default: 0,
    },
    type: {
      type: String,
      default: "",
    },
    icon: {
      type: String,
      default: "",
    },
    bottomText: {
      type: String,
      default: "",
    }
  },
  computed: {
    decimals() {
      return this.type === 'p' ? 2 : 0;
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
  color: #000000;
}

.card-num {
  color: #000000;
  background: #fff;
  box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
  border-color: rgba(0, 0, 0, .05);
  font-size: 32px;
  font-family: Consolas,serif;
}

.el-divider--horizontal {
  display: block;
  height: 1px;
  width: 100%;
  margin: 0 0 12px 0;
}

::v-deep .el-card__body {
  padding: 5px 20px 12px 20px
}

</style>
