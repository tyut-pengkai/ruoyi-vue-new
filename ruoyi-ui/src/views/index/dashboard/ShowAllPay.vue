<template>
  <div>
    <show-card-index
      title="支付笔数"
      tag=""
      :value="totalTrade"
      type="s"
      info="基于系统订单数据统计"
      infoIcon="el-icon-warning-outline"
      icon=""
      :bottomText="'全部订单数量：' + totalTradeAll + ' 转化率：' + transRate + '%'"
    >
      <div style="min-height: 55px;">
        <my-bar-chart2 :data="data" :xTitle="xTitle" height="80px" :toolbox="true" style="margin-top: 5px" bar-width="60%"></my-bar-chart2>
      </div>
    </show-card-index>
  </div>
</template>
<script>
import ShowCardIndex from '@/views/dashboard/ShowCardIndex'
import MyBarChart2 from '@/views/dashboard/MyBarChart2'

export default {
  name: "ShowAllPay",
  components: { MyBarChart2, ShowCardIndex },
  props: {
    totalTrade: {
      type: Number,
      default: 0,
    },
    totalTradeAll: {
      type: Number,
      default: 0,
    },
    transRate: {
      type: Number|String,
      default: 0,
    },
    data: {
      type: Array,
      default: [0,0,0,0,0,0,0,0,0,0],
    },
    xTitle: {
      type: Array,
      default: [],
    }
  },
  data() {
    return {
      // data: [
      //   {data: [10, 200, 30, 45, 58, 46, 17, 6, 90, 33]},
      // ],
      // xTitle: ["1", "2"]
    };
  },
}
</script>
