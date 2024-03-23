<template>
  <div>
    <el-card shadow="never">
      <div slot="header" class="card-title">
        <span style="margin-left: 5px">数据统计</span>
      </div>
      <div style="min-height: 170px;">
        <my-line-chart title="用户统计" :data="dataInner" :xTitle="xTitle" height="220px"></my-line-chart>
      </div>
      <div style="margin-top: 20px" align="center">
        <el-row>
          <el-col :span="12" :push="2">
            <el-radio-group v-model="radio" size="small" @input="handleRadioSelect">
              <el-radio-button label="totalNum" >全部用户数</el-radio-button>
              <el-radio-button label="vipNum">VIP用户数</el-radio-button>
              <el-radio-button label="loginNum">登录用户数</el-radio-button>
              <el-radio-button label="maxOnlineNum">最高在线数</el-radio-button>
            </el-radio-group>
          </el-col>
          <el-col :span="10" :pull="0">
            <el-checkbox-group v-model="checkbox" size="small">
              <el-checkbox-button label="bar" key="type">柱状图/折线图</el-checkbox-button>
              <el-checkbox-button label="stack" key="stack">是否堆叠显示</el-checkbox-button>
            </el-checkbox-group>
          </el-col>
        </el-row>
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
import { deepClone } from '../../../utils'

export default {
  name: "ShowAppUser",
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
      radio: 'totalNum',
      dataInner: [],
      checkbox: []
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
  methods: {
    handleRadioSelect(value) {
      this.dataInner = this.data.filter(item => item.stack === value)
      if(!this.checkbox.includes('stack')) {
        this.dataInner = deepClone(this.dataInner)
        this.dataInner.forEach(item => item.stack = null)
      }
      if(this.checkbox.includes('bar')) {
        this.dataInner = deepClone(this.dataInner)
        this.dataInner.forEach(item => item.type = 'bar')
      } else {
        this.dataInner = deepClone(this.dataInner)
        this.dataInner.forEach(item => item.type = 'line')
      }
    },
  },
  created() {
    // this.$nextTick(()=>{
    //   this.handleRadioSelect(this.radio)
    // })
  },
  watch: {
    data: {
      deep: true,
      handler(newValue, oldValue) {
        if (oldValue !== newValue) {
          this.$nextTick(() => {
            this.handleRadioSelect(this.radio)
          })
        }
      },
    },
    checkbox: {
      handler(newValue, oldValue) {
        if (oldValue !== newValue) {
          this.$nextTick(() => {
            this.handleRadioSelect(this.radio)
          })
        }
      },
    }
  }
}
</script>
