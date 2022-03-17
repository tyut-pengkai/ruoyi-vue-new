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
                        <count-to :decimals="2" :duration="2600" :end-val="10240" :start-val="0" class="card-num"
                                  prefix="￥ "/>
                      </span>
                      <el-divider direction="vertical"></el-divider>
                      <el-tag style="margin-top: -5px; line-height: 30px;">平台总下单数</el-tag>
                      <span>
                        <count-to :duration="2600" :end-val="10240" :start-val="0" class="card-num" suffix=" 笔"/>
                      </span>
                      <el-divider direction="vertical"></el-divider>
                      <el-tag style="margin-top: -5px; line-height: 30px;">平台总成交数</el-tag>
                      <span>
                        <count-to :duration="2600" :end-val="10240" :start-val="0" class="card-num" suffix=" 笔"/>
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
                <show-card :value="102400" tag="今日" title="今日成交" type="p"></show-card>
              </el-col>
              <el-col :lg="6" :sm="24" :xs="24" style="margin-top: 10px;">
                <show-card :value="102400" tag="昨日" title="昨日成交" type="p"></show-card>
              </el-col>
              <el-col :lg="6" :sm="24" :xs="24" style="margin-top: 10px;">
                <show-card :value="102400" tag="七日" title="近七日成交" type="p"></show-card>
              </el-col>
              <el-col :lg="6" :sm="24" :xs="24" style="margin-top: 10px;">
                <show-card :value="102400" tag="今日" title="今日下单（含未付款）" type="p"></show-card>
              </el-col>
            </el-row>
            <el-row :gutter="10">
              <el-col :lg="6" :sm="12" :xs="24" style="margin-top: 10px;">
                <show-card :value="10" tag="今日" title="今日成交" type="s"></show-card>
              </el-col>
              <el-col :lg="6" :sm="12" :xs="24" style="margin-top: 10px;">
                <show-card :value="20" tag="昨日" title="昨日成交" type="s"></show-card>
              </el-col>
              <el-col :lg="6" :sm="12" :xs="24" style="margin-top: 10px;">
                <show-card :value="30" tag="七日" title="近七日成交" type="s"></show-card>
              </el-col>
              <el-col :lg="6" :sm="12" :xs="24" style="margin-top: 10px;">
                <show-card :value="10" tag="今日" title="今日下单（含未付款）" type="s"></show-card>
              </el-col>
            </el-row>
            <el-row :gutter="10">
              <el-col :lg="8" :sm="24" :xs="24" style="margin-top: 10px;">
                <el-card shadow="never">
                  <div class="card-title">
                    <span>近七天交易流水</span>
                  </div>
                  <div style="margin-top: 20px">
                    <bar-chart height="260px"/>
                  </div>
                </el-card>
              </el-col>
              <el-col :lg="8" :sm="24" :xs="24" style="margin-top: 10px;">
                <el-card shadow="never">
                  <div class="card-title">
                    <span>近七天软件收益</span>
                  </div>
                  <div style="margin-top: 20px">
                    <pie-chart height="260px"/>
                  </div>
                </el-card>
              </el-col>
              <el-col :lg="8" :sm="24" :xs="24" style="margin-top: 10px;">
                <el-card shadow="never">
                  <div class="card-title">
                    <span>近七天收款类型统计</span>
                  </div>
                  <div style="margin-top: 20px">
                    <pie-chart height="260px"/>
                  </div>
                </el-card>
              </el-col>
            </el-row>
            <el-row :gutter="10">
              <!-- <el-col :lg="8" :sm="12" :xs="24" style="margin-top: 10px;">
                <show-app-sale title="平台全部软件数据统计" :data="[1,2,3,4]"></show-app-sale>
              </el-col> -->
              <el-col v-for="(item, index) in [0,1,2]" :key="index" :lg="8" :sm="12" :xs="24" style="margin-top: 10px;">
                <show-app-sale :data="[1000,2000,3000,4000]" :tag="'TOP'+(index+1)"
                               :title="'APP'+(index+1)"></show-app-sale>
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
import ShowAppSale from './dashboard/ShowAppSale'
import PieChart from "./dashboard/PieChart"
import BarChart from "./dashboard/BarChart"

export default {
  name: "Index",
  components: {
    CountTo,
    ShowCard,
    ShowAppSale,
    PieChart,
    BarChart
  },
  data() {
    return {
      showMode: false,
      saleData: {},
    };
  },
  methods: {

  },
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
