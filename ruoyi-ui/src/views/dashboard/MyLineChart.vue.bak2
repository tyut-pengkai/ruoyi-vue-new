<template>
  <div :class="className" :style="{height:height,width:width}"/>
</template>

<script>
import * as echarts from 'echarts'
import resize from './mixins/resize'

require('echarts/theme/macarons') // echarts theme

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    title: {
      type: String,
      default: '100%'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '260px'
    },
    toolbox: {
      type: Boolean,
      default: true
    },
    autoResize: {
      type: Boolean,
      default: true
    },
    data: {
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
        title: {
          text: this.title
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            // Use axis to trigger tooltip
            type: 'shadow' // 'shadow' as default; can also be 'line' or 'shadow'
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
                devideSize = 10;
              } else if (count >= 60) {
                devideSize = 8;
              } else if (count >= 30) {
                devideSize = 6;
              } else if (count >= 10) {
                devideSize = 5;
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
          data: [],
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        toolbox: {
          feature: {
            saveAsImage: {},
            magicType: {type: ['line', 'bar']},
          },
          show: this.toolbox
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: this.xTitle,
        },
        yAxis: {
          type: 'value',
        },
        series: this.data
      });
    },
  }
}
</script>
