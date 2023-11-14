<template>
  <div>
    <el-card shadow="never">
      <div slot="header" class="card-title">
        <span style="margin-left: 5px">数据统计</span>
      </div>
      <div style="min-height: 170px;">
        <my-line-chart title="用户统计" :data="data" :xTitle="xTitle" height="170px"></my-line-chart>
      </div>
      <!--这里是放置按钮让其显示在最前面-->
<!--      <div style="padding-left: 120px; z-index:999;float:left;margin-top: -170px">-->
<!--        <el-radio-group v-model="unit" size="small">-->
<!--          <el-radio-button :label="1">单位(日)</el-radio-button>-->
<!--          <el-radio-button :label="2">单位(月)</el-radio-button>-->
<!--        </el-radio-group>-->
<!--      </div>-->
    </el-card>
  </div>
</template>
<script>
import MyLineChart from '@/views/dashboard/MyLineChart'

export default {
  name: "ShowAllPay",
  components: { MyLineChart },
  props: {
    data: {
      type: Array,
      default: [],
    },
    xTitle: {
      type: Array,
      default: function () {
        return [];
      }
    },
  },
  data() {
    return {
      unit: '1',
      // data: [
      //   {
      //     name: 'appName1',
      //     type: 'bar',
      //     data: [120, 132, 101, 300, 90, 230, 400]
      //   },
      //   {
      //     name: 'appName2',
      //     type: 'bar',
      //     data: [220, 1, 191, 234, 290, 330, 310]
      //   },
      //   {
      //     name: 'appName3',
      //     type: 'bar',
      //     data: [20, 1, 19, 23, 29, 33, 31]
      //   },
      //   {
      //     name: 'appName4',
      //     type: 'bar',
      //     data: [20, 10, 19, 23, 35, 33, 66]
      //   },
      // ]
    };
  },
}
</script>
