<template>
  <div :class="className" :style="{height:height,width:width}"/>
</template>

<script>
import * as echarts from 'echarts'
import resize from './mixins/resize'

require('echarts/theme/macarons') // echarts theme

const animationDuration = 1300

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '260px'
    },
    data: {
      type: Array,
      default: function () {
        return [];
      }
    },
    lineData: {
      type: Array,
      default: function () {
        return [];
      }
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
      chart: null
    }
  },
  watch: {
    data: {
      deep: true,
      handler(newValue, oldValue) {
        if (oldValue != newValue) {
          this.$nextTick(() => {
            this.initChart()
          })
        }
      },
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')

      this.chart.setOption({
        tooltip: {
          trigger: 'axis',
          axisPointer: { // 坐标轴指示器，坐标轴触发有效
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          },
          confine: true,  //解决浮窗被截断问题
          appendToBody: true,
          formatter: function (params) {	//数据都包含在params里面
            var zht;
            if (params != null && params.length > 0) {
              zht = "<table>"
              var count = 0;
              for (var i = 0; i < params.length; i++) {
                if (params[i].value > 0) {
                  count++;
                }
              }
              var devideSize = 1; // 每行显示的个数
              if (count >= 80) {
                devideSize = 6;
              } else if (count >= 60) { 
                devideSize = 5;
              } else if (count >= 30) {
                devideSize = 3;
              } else if (count >= 10) {
                devideSize = 2;
              }
              zht += "<tr>";
              for (var i = 0; i < params.length; i++) {
                if (params[i].value > 0) {
                  zht += "<td>" + params[i].marker + params[i].seriesName + ": " + params[i].value + "</td>";
                  if ((i + 1) % devideSize == 0) {						//每行显示5个
                    zht += '<tr/>';
                  }
                }
              }
              zht += "</table>";
            }
            return zht;					//返回出去
          }
        },
        legend: {
          left: 'center',
          bottom: '0',
          data: this.legend
        },
        toolbox: {
          show: true,
          feature: {
            magicType: {type: ['line', 'bar']},
          }
        },
        grid: {
          top: 40,
          left: '2%',
          right: '2%',
          bottom: 70,
          containLabel: true
        },
        xAxis: [{
          type: 'category',
          data: this.xTitle,
          axisTick: {
            alignWithLabel: true
          }
        }],
        yAxis: [{
          type: 'value',
          axisTick: {
            show: false
          }
        }],
        series: this.barData
      })
    }
  },
  computed: {
    barData() {
      var data = [];
      for (var item of this.data) {
        data.push({
          name: item['appName'],
          type: 'bar',
          stack: 'vistors',
          barWidth: '30%',
          data: item['data'],
          animationDuration,
          lineStyle: {
            width: 0.1
          },
          areaStyle: {
            opacity: 0.8,
            // color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            //   {
            //     offset: 0,
            //     color: 'rgb(128, 255, 165)'
            //   },
            //   {
            //     offset: 1,
            //     color: 'rgb(1, 191, 236)'
            //   }
            // ])
          },
        });
      }
      // data.push({
      //   type: 'line',
      //   stack: 'Total',
      //   smooth: true,
      //   lineStyle: {
      //     width: 0
      //   },
      //   showSymbol: false,
      //   areaStyle: {
      //     opacity: 0.8,
      //     color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
      //       {
      //         offset: 0,
      //         color: 'rgb(128, 255, 165)'
      //       },
      //       {
      //         offset: 1,
      //         color: 'rgb(1, 191, 236)'
      //       }
      //     ])
      //   },
      //   emphasis: {
      //     focus: 'series'
      //   },
      //   data: this.lineData
      // },);
      return data;
    },
    legend() {
      var legend = [];
      for (var item of this.data) {
        for (var data of item['data']) {
          if (data > 0) {
            legend.push(item['appName']);
            break;
          }
        }
      }
      return legend;
    },
  }
}
</script>
