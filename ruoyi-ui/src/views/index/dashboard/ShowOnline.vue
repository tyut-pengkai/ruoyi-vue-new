<template>
  <div>
    <show-card-index
      title="实时在线"
      tag=""
      :value="value"
      type="none"
      info="每次刷新页面时会自动刷新数据"
      infoIcon="el-icon-warning-outline"
      icon=""
      :bottomText= "bottomText"
    >
      <div style="min-height: 332px;margin-top: 10px">
        <my-pie-chart2 :data="data" height="463px" style="margin-bottom: 10px"></my-pie-chart2>
      </div>
    </show-card-index>
  </div>
</template>
<script>
import ShowCardIndex from '@/views/dashboard/ShowCardIndex'
import MyPieChart2 from '@/views/dashboard/MyPieChart2'
import { parseTime } from '../../../utils/ruoyi'

export default {
  name: "ShowOnline",
  components: { MyPieChart2, ShowCardIndex },
  props: {
    data: {
      type: Array,
      default: [],
    },
    value: {
      type: Number | String,
      default: 0,
    }
  },
  data() {
    return {
      // data: [
      //   { value: 320, name: "APP1" },
      //   { value: 200, name: "APP2" },
      //   { value: 100, name: "APP3" },
      //   { value: 33, name: "APP4" },
      //   { value: 88, name: "APP5" },
      // ],
      bottomText: '上次刷新：' + parseTime(new Date()),
    };
  },
}
</script>
