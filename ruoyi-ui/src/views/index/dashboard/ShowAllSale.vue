<template>
  <div>
    <show-card-index
      title="总销售额"
      tag=""
      :value="totalFee"
      type="p"
      info="基于系统订单数据统计"
      infoIcon="el-icon-warning-outline"
      icon=""
      :bottomText="'日均销售额： ¥' + feePerDay"
    >
      <div style="min-height: 55px;margin-top: 25px; margin-bottom: 5px">
        <div>
          <span v-for="(item, index) in [0,2,3]" style="margin-right: 5px">
            {{unit[item]}}同比：{{tb[item]}}
            <i v-if="tb[item] !== '-' && Number(tb[item].substring(0, tb[item].length-1)) > 0" class="el-icon-caret-top"/>
            <i v-else-if="tb[item] !== '-' && Number(tb[item].substring(0, tb[item].length-1)) < 0" class="el-icon-caret-bottom"/>
          </span>
        </div>
        <div>
          <span v-for="(item, index) in [0,2,3]" style="margin-right: 5px">
            {{unit[item]}}环比：{{hb[item]}}
            <i v-if="tb[item] !== '-' && Number(hb[item].substring(0, tb[item].length-1)) > 0" class="el-icon-caret-top"/>
            <i v-else-if="tb[item] !== '-' && Number(hb[item].substring(0, tb[item].length-1)) < 0" class="el-icon-caret-bottom"/>
          </span>
        </div>
      </div>
    </show-card-index>
  </div>
</template>
<script>
import ShowCardIndex from '@/views/dashboard/ShowCardIndex'

export default {
  name: "ShowAllSale",
  components: { ShowCardIndex },
  props: {
    totalFee: {
      type: Number|String,
      default: 0,
    },
    feePerDay: {
      type: Number|String,
      default: 0,
    },
    tb: {
      type: Array,
      default: ['-', '-', '-', '-'],
    },
    hb: {
      type: Array,
      default: ['-', '-', '-', '-'],
    },
  },
  data() {
    return {
      unit: ['日', '周', '月', '年']
    }
  }
}
</script>
