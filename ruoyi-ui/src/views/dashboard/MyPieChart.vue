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
        tooltip: {
          trigger: 'item',
          formatter: '{b} : {c} ({d}%)',
          confine: true,  //解决浮窗被截断问题
          appendToBody: true,
        },
        legend: {
          type: 'scroll', // 设置图例分页类型为滚动
          // left: 'center',
          bottom: '0',
          data: this.legend
        },
        series: [
          {
            name: this.title,
            type: 'pie',
            // roseType: 'radius',
            radius: [10, '50%'],
            center: ['50%', '45%'],
            data: this.data,
            animationEasing: 'cubicInOut',
            animationDuration: 2600
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
