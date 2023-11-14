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
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '260px'
    },
    title: {
      type: String,
      default: ''
    },
    data: {
      type: Array,
      default: function () {
        return [];
      }
    }
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
          text: '实时用户在线数据',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{b} : {c} ({d}%)',
          confine: true,  //解决浮窗被截断问题
          appendToBody: true,
        },
        legend: {
          top: 'bottom',
          padding: [20, 0, 0, 0],
          //type: 'scroll',
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '5%',
          containLabel: true
        },
        series: [
          {
            name: this.title,
            type: 'pie',
            radius: [5, '55%'],
            roseType: 'radius',
            center: ['50%', '60%'],
            data: this.data,
            animationEasing: 'cubicInOut',
            animationDuration: 2600,
            itemStyle: {
              borderRadius: 5
            },
          }
        ]
      })
    }
  },
  computed: {
    legend() {
      var legend = [];
      for (var item of this.data) {
        legend.push(item['name']);
      }
      return legend;
    },
  }
}
</script>
