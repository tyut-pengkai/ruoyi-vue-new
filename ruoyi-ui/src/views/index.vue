<template>
  <div class="dashboard">
    <div style="margin: 10px;">
      <el-row :gutter="10">
        <el-col :lg="24" :sm="24" :xs="24">
          <div style="margin: 10px; margin-top: 0px;">
            <el-row :gutter="10">
              <el-col :span="24">
                <el-card shadow="never">
                  <div style="height: 15px;">
                    <span>
                      <el-tag style="margin-top: -5px; line-height: 30px;">平台总交易额</el-tag>
                      <span>
                        <count-to :decimals="2" :duration="2600" :end-val="d['feeTotal']" :start-val="0"
                                  class="card-num"
                                  prefix="￥ "/>
                      </span>
                      <el-divider direction="vertical"></el-divider>
                      <el-tag style="margin-top: -5px; line-height: 30px;">平台总成交数</el-tag>
                      <span>
                        <count-to :duration="2600" :end-val="d['tradeTotal']" :start-val="0" class="card-num"
                                  suffix=" 笔"/>
                      </span>
                      <el-divider direction="vertical"></el-divider>
                      <el-tag style="margin-top: -5px; line-height: 30px;">平台总下单数（含未付款）</el-tag>
                      <span>
                        <count-to :duration="2600" :end-val="d['tradeTotalAll']" :start-val="0" class="card-num"
                                  suffix=" 笔"/>
                      </span>
                    </span>
                    <el-switch
                      v-model="showMode"
                      active-text="按月统计"
                      inactive-text="按天统计"
                      style="float: right;">
                    </el-switch>
                  </div>
                </el-card>
              </el-col>
              <el-col :lg="6" :sm="24" :xs="24" style="margin-top: 10px;">
                <show-card :value="d['feeToday']" tag="今日" title="今日成交" type="p"></show-card>
              </el-col>
              <el-col :lg="6" :sm="24" :xs="24" style="margin-top: 10px;">
                <show-card :value="d['feeYesterday']" tag="昨日" title="昨日成交" type="p"></show-card>
              </el-col>
              <el-col :lg="6" :sm="24" :xs="24" style="margin-top: 10px;">
                <show-card :value="d['feeWeek']" tag="七日" title="近七日成交" type="p"></show-card>
              </el-col>
              <el-col :lg="6" :sm="24" :xs="24" style="margin-top: 10px;">
                <show-card :value="d['feeTodayAll']" tag="今日" title="今日下单（含未付款）" type="p"></show-card>
              </el-col>
            </el-row>
            <el-row :gutter="10">
              <el-col :lg="6" :sm="12" :xs="24" style="margin-top: 10px;">
                <show-card :value="d['tradeToday']" tag="今日" title="今日成交" type="s"></show-card>
              </el-col>
              <el-col :lg="6" :sm="12" :xs="24" style="margin-top: 10px;">
                <show-card :value="d['tradeYesterday']" tag="昨日" title="昨日成交" type="s"></show-card>
              </el-col>
              <el-col :lg="6" :sm="12" :xs="24" style="margin-top: 10px;">
                <show-card :value="d['tradeWeek']" tag="七日" title="近七日成交" type="s"></show-card>
              </el-col>
              <el-col :lg="6" :sm="12" :xs="24" style="margin-top: 10px;">
                <show-card :value="d['tradeTodayAll']" tag="今日" title="今日下单（含未付款）" type="s"></show-card>
              </el-col>
            </el-row>
            <el-row :gutter="10">
              <el-carousel :interval="10000" arrow="always" height="340px">
                <el-carousel-item>
                  <el-col :lg="12" :sm="24" :xs="24" style="margin-top: 10px;">
                    <el-card shadow="never">
                      <div class="card-title">
                        <span>近七天交易流水</span>
                      </div>
                      <div style="margin-top: 20px">
                        <bar-chart height="260px"/>
                      </div>
                    </el-card>
                  </el-col>
                  <el-col :lg="12" :sm="24" :xs="24" style="margin-top: 10px;">
                    <el-card shadow="never">
                      <div class="card-title">
                        <span>近七天收款趋势统计</span>
                      </div>
                      <div style="margin-top: 20px">
                        <line-chart :data="lineData"></line-chart>
                      </div>
                    </el-card>
                  </el-col>
                </el-carousel-item>
                <el-carousel-item>
                  <el-col :lg="12" :sm="24" :xs="24" style="margin-top: 10px;">
                    <el-card shadow="never">
                      <div class="card-title">
                        <span>近七天软件收益</span>
                      </div>
                      <div style="margin-top: 20px">
                        <pie-chart :data="piedata" height="260px"/>
                      </div>
                    </el-card>
                  </el-col>
                  <el-col :lg="12" :sm="24" :xs="24" style="margin-top: 10px;">
                    <el-card shadow="never">
                      <div class="card-title">
                        <span>近七天收款类型统计</span>
                      </div>
                      <div style="margin-top: 20px">
                        <pie-chart :data="piedata" height="260px"/>
                      </div>
                    </el-card>
                  </el-col>
                </el-carousel-item>
              </el-carousel>
              <!-- <el-col :lg="6" :sm="24" :xs="24" style="margin-top: 10px;">
                <el-card shadow="never">
                  <div class="card-title">
                    <span>近七天交易流水</span>
                  </div>
                  <div style="margin-top: 20px">
                    <bar-chart height="260px"/>
                  </div>
                </el-card>
              </el-col> -->
              <!-- <el-col :lg="7" :sm="24" :xs="24" style="margin-top: 10px;">
                <el-card shadow="never">
                  <div class="card-title">
                    <span>近七天软件收益</span>
                  </div>
                  <div style="margin-top: 20px">
                    <pie-chart height="260px" :data="piedata" />
                  </div>
                </el-card>
              </el-col> -->
              <!-- <el-col :lg="7" :sm="24" :xs="24" style="margin-top: 10px;">
                <el-card shadow="never">
                  <div class="card-title">
                    <span>近七天收款类型统计</span>
                  </div>
                  <div style="margin-top: 20px">
                    <pie-chart height="260px" :data="piedata"/>
                  </div>
                </el-card>
              </el-col> -->
              <!-- <el-col :lg="6" :sm="24" :xs="24" style="margin-top: 10px;">
                <el-card shadow="never">
                  <div class="card-title">
                    <span>近七天收款趋势统计</span>
                  </div>
                  <div style="margin-top: 20px">
                    <line-chart :data="lineData"></line-chart>
                  </div>
                </el-card>
              </el-col> -->
            </el-row>
            <!-- <el-row :gutter="10">
              <el-col :lg="12" :sm="24" :xs="24" style="margin-top: 10px;">
                <el-card shadow="never">
                  <div class="card-title">
                    <span>近七天收款类型统计</span>
                  </div>
                  <div style="margin-top: 20px">
                    <line-chart :data="lineData"></line-chart>
                  </div>
                </el-card>
              </el-col>
            </el-row> -->
            <el-row :gutter="10">
              <!-- <el-col :lg="8" :sm="12" :xs="24" style="margin-top: 10px;">
                <show-app-sale title="平台全部软件数据统计" :data="[1,2,3,4]"></show-app-sale>
              </el-col> -->
              <el-col v-for="(item, index) in d['feeAppList']" :key="index" :lg="8" :sm="12" :xs="24"
                      style="margin-top: 10px;">
                <show-app-sale :data="[item['feeTotal'],item['feeToday'],item['feeYesterday'],item['feeWeek']]"
                               :tag="'TOP'+(index+1)"
                               :title="item['appName']"></show-app-sale>
              </el-col>
            </el-row>
          </div>
        </el-col>
        <!-- <el-col :span="6">
          <div style="margin: 10px;">

          </div>
        </el-col> -->
      </el-row>
    </div>
  </div>
</template>

<script>
import CountTo from 'vue-count-to'
import ShowCard from './dashboard/ShowCard'
import {getDashboardInfo} from '@/api/common'
import ShowAppSale from './dashboard/ShowAppSale'
import PieChart from "./dashboard/MyPieChart"
import BarChart from "./dashboard/BarChart"
import LineChart from "./dashboard/MyLineChart"

export default {
  name: "Index",
  components: {
    CountTo,
    ShowCard,
    ShowAppSale,
    PieChart,
    BarChart,
    LineChart
  },
  data() {
    return {
      showMode: false,
      d: {},
      piedata: [
        // { value: parseMoney(320), name: 'APP1' },
        // { value: 240, name: 'APP2' },
        // { value: 149, name: 'APP3' },
        // { value: 100, name: 'APP4' },
        // { value: 59, name: 'APP5' }
      ],
      lineData: {
        expectedData: [100, 120, 161, 134, 105, 160, 165],
        actualData: [120, 82, 91, 154, 162, 140, 145],
      },
    };
  },
  created() {
    this.getDashboardInfo();
  },
  methods: {
    getDashboardInfo() {
      getDashboardInfo().then(response => {
        this.d = response.data;
        this.piedata = [];
        if (this.d['feeAppList']) {
          for (var item of this.d['feeAppList']) {
            this.piedata.push({'value': item['feeWeek'], 'name': item['appName']});
          }
        }
      });
    }
  },
  computed: {}
};
</script>

<style lang="scss" scoped>
.dashboard {
  background-color: #f5f7f9;
  height: 100%;
}

.card-title {
  font-size: 14px;
  margin-top: -5px;
  color: #595959;
}

.card-title-bold {
  font-size: 16px;
  font-weight: bold;
  color: #595959;
}

</style>
