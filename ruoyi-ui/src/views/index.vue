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
                    <el-button :loading="refreshLoading" icon="el-icon-refresh" plain size="small"
                               style="float: right; margin-left: 20px; margin-top: -7px" @click="refresh">刷新
                    </el-button>
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
                <show-card :value="d['feeTodayAll']-d['feeToday']" tag="今日" title="今日下单未付款" type="p"></show-card>
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
                <show-card :value="d['tradeTodayAll']-d['tradeToday']" tag="今日" title="今日下单未付款" type="s"></show-card>
              </el-col>
            </el-row>
            <el-row :gutter="10">
              <!-- <el-carousel :interval="10000" :autoplay="false" arrow="always" height="340px"> -->
              <!-- <el-carousel-item> -->
              <el-col :lg="10" :sm="24" :xs="24" style="margin-top: 10px;">
                <el-card shadow="never">
                  <div class="card-title">
                    <span>近七天交易流水</span>
                  </div>
                  <div style="margin-top: 20px">
                    <bar-chart :data="barData"></bar-chart>
                  </div>
                </el-card>
              </el-col>
              <el-col :lg="7" :sm="24" :xs="24" style="margin-top: 10px;">
                <!-- <el-card shadow="never">
                  <div class="card-title">
                    <span>近七天收款趋势统计</span>
                  </div>
                  <div style="margin-top: 20px">
                    <line-chart :data="lineData"></line-chart>
                  </div>
                </el-card> -->
                <el-card shadow="never">
                  <div class="card-title">
                    <span>近七天软件收益</span>
                  </div>
                  <div style="margin-top: 20px">
                    <pie-chart :data="pieData"></pie-chart>
                  </div>
                </el-card>
              </el-col>
              <el-col :lg="7" :sm="24" :xs="24" style="margin-top: 10px;">
                <el-card shadow="never">
                  <div class="card-title">
                    <span>近七天收款类型统计</span>
                  </div>
                  <div style="margin-top: 20px">
                    <pie-chart :data="pie2Data"></pie-chart>
                  </div>
                </el-card>
              </el-col>
              <!-- </el-carousel-item> -->
              <!-- <el-carousel-item>
                <el-col :lg="12" :sm="24" :xs="24" style="margin-top: 10px;">
                  <el-card shadow="never">
                    <div class="card-title">
                      <span>近七天收款类型统计</span>
                    </div>
                    <div style="margin-top: 20px">
                      <pie-chart></pie-chart>
                    </div>
                  </el-card>
                </el-col>
              </el-carousel-item> -->
              <!-- </el-carousel> -->
            </el-row>
            <el-row :gutter="10">
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
import BarChart from "./dashboard/MyBarChart"
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
      refreshLoading: false,
      showMode: false,
      d: {},
      pieData: [
        // { value: 320, name: 'APP1' },
      ],
      pie2Data: [
        // { value: 320, name: 'APP1' },
      ],
      barData: [
        // {data: [0, 0, 0, 0, 0, 0, 0], appName: "123"},
      ],
      lineData: [
        // 0, 0, 0, 0, 0, 0, 0
      ],
    };
  },
  created() {
    this.getDashboardInfo();
  },
  methods: {
    getDashboardInfo() {
      this.$modal.loading("正在加载数据，请稍后...");
      getDashboardInfo().then(response => {
        this.d = response.data;
        this.pieData = [];
        if (this.d['feeAppList']) {
          for (var item of this.d['feeAppList']) {
            if (item['feeWeek'] > 0) {
              this.pieData.push({'value': item['feeWeek'], 'name': item['appName']});
            }
          }
        }
        this.pie2Data = [];
        if (this.d['payModeList']) {
          for (var item of this.d['payModeList']) {
            if (item['totalCount'] > 0) {
              this.pie2Data.push({'value': item['totalCount'], 'name': item['payMode']});
            }
          }
        }
        this.barData = [];
        this.lineData = [];
        if (this.d['feeAppWeekList']) {
          this.barData = this.d['feeAppWeekList'];
          for (var i = 0; i < 7; i++) {
            this.lineData.push(0);
            for (var item of this.barData) {
              this.lineData[i] = this.lineData[i] + item['data'][i];
            }
          }
        }
        this.$modal.closeLoading();
        if (this.refreshLoading) {
          this.refreshLoading = false;
          this.$modal.msgSuccess("数据已刷新");
        }
      });
    },
    refresh() {
      this.refreshLoading = true;
      this.getDashboardInfo();
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
