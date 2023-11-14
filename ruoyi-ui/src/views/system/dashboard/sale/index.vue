<template>
  <div class="dashboard" style="min-height: 90vh">
    <el-row :gutter="10">
      <el-col :lg="24" :sm="24" :xs="24">
        <div style="margin: 10px">
          <el-row :gutter="10">
            <el-col :span="24">
              <el-card shadow="never">
                <div style="height: 15px">
                  <span style="margin-right: 20px"
                  >上次刷新：{{ parseTime(new Date()) }}</span
                  >
                  <div style="display: inline">
                    <el-tag style="margin-top: -5px; line-height: 30px"
                    >总交易额：
                      <span>
                        <count-to
                          :decimals="2"
                          :duration="2600"
                          :end-val="d['feeTotal']"
                          :start-val="0"
                          class="card-num"
                          prefix="￥ "
                        />
                      </span>
                    </el-tag>
                    <el-divider direction="vertical"></el-divider>
                    <el-tag style="margin-top: -5px; line-height: 30px"
                    >总成交数：
                      <span>
                        <count-to
                          :duration="2600"
                          :end-val="d['tradeTotal']"
                          :start-val="0"
                          class="card-num"
                          suffix=" 笔"
                        />
                      </span>
                    </el-tag>
                    <el-divider direction="vertical"></el-divider>
                    <el-tag style="margin-top: -5px; line-height: 30px"
                    >总下单数(含未付款)：
                      <span>
                        <count-to
                          :duration="2600"
                          :end-val="d['tradeTotalAll']"
                          :start-val="0"
                          class="card-num"
                          suffix=" 笔"
                        />
                      </span>
                    </el-tag>
                  </div>
                  <div style="float: right">
                    <el-switch
                      v-model="showMode"
                      active-text="按月统计"
                      active-value="1"
                      inactive-text="按天统计"
                      inactive-value="0"
                      @change="handleShowModeChange"
                    >
                    </el-switch>
                    <el-button
                      :loading="refreshLoading"
                      icon="el-icon-refresh"
                      plain
                      size="small"
                      style="margin-left: 20px; margin-top: -7px"
                      @click="refresh"
                    >刷新
                    </el-button>
                    <el-checkbox
                      v-model="autoRefresh"
                      style="margin-left: 10px"
                      @change="handleAutoRefreshChange"
                    >
                      自动
                    </el-checkbox>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col :lg="12" :sm="12" :xs="24" style="margin-top: 10px">
              <show-card-sale
                :tag="s[showMode]['feeTodayTag']"
                :title="s[showMode]['feeToday']"
                :trade="d['tradeToday']"
                :value="d['feeToday']"
                icon="el-icon-coin"
                type="p"
              ></show-card-sale>
            </el-col>
            <!-- <el-col :lg="6" :sm="12" :xs="24" style="margin-top: 10px;">
                <show-card-sale :tag="s[showMode]['feeTodayTag']" :title="s[showMode]['feeToday']" :value="d['tradeToday']"
                           type="s" icon="el-icon-sell"></show-card-sale>
              </el-col> -->
            <el-col :lg="12" :sm="12" :xs="24" style="margin-top: 10px">
              <show-card-sale
                :tag="s[showMode]['feeTodayTag']"
                :title="s[showMode]['feeTodayAll']"
                :trade="d['tradeTodayAll'] - d['tradeToday']"
                :value="d['feeTodayAll'] - d['feeToday']"
                icon="el-icon-warning-outline"
                type="p"
              ></show-card-sale>
            </el-col>
            <!-- <el-col :lg="6" :sm="12" :xs="24" style="margin-top: 10px;">
                <show-card-sale :tag="s[showMode]['feeTodayTag']" :title="s[showMode]['feeTodayAll']"
                           :value="d['tradeTodayAll']-d['tradeToday']" type="s" icon="el-icon-warning-outline"></show-card-sale>
              </el-col> -->
          </el-row>
          <el-row :gutter="10">
            <el-col :lg="12" :sm="12" :xs="24" style="margin-top: 10px">
              <show-card-sale
                :tag="s[showMode]['feeYesterdayTag']"
                :title="s[showMode]['feeYesterday']"
                :trade="d['tradeYesterday']"
                :value="d['feeYesterday']"
                icon="el-icon-coin"
                type="p"
              ></show-card-sale>
            </el-col>
            <!-- <el-col :lg="6" :sm="12" :xs="24" style="margin-top: 10px;">
                <show-card-sale :tag="s[showMode]['feeYesterdayTag']" :title="s[showMode]['feeYesterday']"
                           :value="d['tradeYesterday']" type="s" icon="el-icon-sell"></show-card-sale>
              </el-col> -->
            <el-col :lg="12" :sm="12" :xs="24" style="margin-top: 10px">
              <show-card-sale
                :tag="s[showMode]['feeWeekTag']"
                :title="s[showMode]['feeWeek']"
                :trade="d['tradeWeek']"
                :value="d['feeWeek']"
                icon="el-icon-coin"
                type="p"
              ></show-card-sale>
            </el-col>
            <!-- <el-col :lg="6" :sm="12" :xs="24" style="margin-top: 10px;">
                <show-card-sale :tag="s[showMode]['feeWeekTag']" :title="s[showMode]['feeWeek']" :value="d['tradeWeek']"
                           type="s" icon="el-icon-sell"></show-card-sale>
              </el-col> -->
          </el-row>
          <el-row :gutter="10">
            <!-- <el-carousel :interval="10000" :autoplay="false" arrow="always" height="340px"> -->
            <!-- <el-carousel-item> -->
            <el-col :lg="10" :sm="24" :xs="24" style="margin-top: 10px">
              <el-card shadow="never">
                <div class="card-title">
                  <i
                    class="el-icon-data-analysis"
                    style="margin-right: 5px"
                  ></i>
                  <span>{{ s[showMode]["tradeWeek"] }}</span>
                </div>
                <div style="margin-top: -10px">
                  <bar-chart
                    :data="barData"
                    :xTitle="barTitle"
                    height="290px"
                  ></bar-chart>
                </div>
              </el-card>
            </el-col>
            <el-col :lg="7" :sm="24" :xs="24" style="margin-top: 10px">
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
                  <i class="el-icon-pie-chart" style="margin-right: 5px"></i>
                  <span>{{ s[showMode]["feeAppWeek"] }}</span>
                </div>
                <div style="margin-top: 20px">
                  <pie-chart :data="pieData"></pie-chart>
                </div>
              </el-card>
            </el-col>
            <el-col :lg="7" :sm="24" :xs="24" style="margin-top: 10px">
              <el-card shadow="never">
                <div class="card-title">
                  <i class="el-icon-pie-chart" style="margin-right: 5px"></i>
                  <span>{{ s[showMode]["payModeWeek"] }}</span>
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
                      <span>{{s[showMode]['payModeWeek']}}</span>
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
            <el-col
              v-for="(item, index) in d['feeAppList']"
              :key="index"
              :lg="8"
              :sm="12"
              :xs="24"
              style="margin-top: 10px"
            >
              <show-app-sale
                :data="[
                  item['feeTotal'],
                  item['feeToday'],
                  item['feeYesterday'],
                  item['feeWeek'],
                ]"
                :showMode="showMode"
                :tag="'TOP' + (index + 1)"
                :title="item['appName']"
              ></show-app-sale>
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
</template>

<script>
import CountTo from "vue-count-to";
import ShowCardSale from "@/views/dashboard/ShowCardSale";
import {getDashboardInfoSaleView} from "@/api/common";
import ShowAppSale from "@/views/dashboard/ShowAppSale";
import PieChart from "@/views/dashboard/MyPieChart";
import BarChart from "@/views/dashboard/MyBarChart";

export default {
  name: "SaleView",
  components: {
    CountTo,
    ShowCardSale,
    ShowAppSale,
    PieChart,
    BarChart,
  },
  data() {
    return {
      username: this.$store.state.user.name,
      refreshLoading: false,
      showMode: "0",
      autoRefresh: false,
      timer: null,
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
      barTitle: [],
      lineData: [
        // 0, 0, 0, 0, 0, 0, 0
      ],
      s: {
        0: {
          feeToday: "今日成交",
          feeYesterday: "昨日成交",
          feeWeek: "近七日成交",
          feeTodayTag: "今日",
          feeYesterdayTag: "昨日",
          feeWeekTag: "七日",
          feeTodayAll: "今日下单未付款",
          tradeWeek: "近七天交易流水",
          feeAppWeek: "近七天软件收益",
          payModeWeek: "近七天收款类型统计",
        },
        1: {
          feeToday: "本月成交",
          feeYesterday: "上月成交",
          feeWeek: "近半年成交",
          feeTodayTag: "本月",
          feeYesterdayTag: "上月",
          feeWeekTag: "半年",
          feeTodayAll: "本月下单未付款",
          tradeWeek: "近半年交易流水",
          feeAppWeek: "近半年软件收益",
          payModeWeek: "近半年收款类型统计",
        },
      },
    };
  },
  created() {
    this.getDashboardInfo();
  },
  methods: {
    getDashboardInfo() {
      this.$modal.loading("正在加载数据，请稍后...");
      getDashboardInfoSaleView({showMode: this.showMode}).then((response) => {
        this.d = response.data;
        this.pieData = [];
        if (this.d["feeAppList"]) {
          for (var item of this.d["feeAppList"]) {
            if (item["feeWeek"] > 0) {
              this.pieData.push({
                value: item["feeWeek"],
                name: item["appName"],
              });
            }
          }
        }
        this.pie2Data = [];
        if (this.d["payModeList"]) {
          for (var item of this.d["payModeList"]) {
            if (item["totalCount"] > 0) {
              this.pie2Data.push({
                value: item["totalCount"],
                name: item["payMode"],
              });
            }
          }
        }
        this.barData = [];
        this.barTitle = [];
        this.lineData = [];
        if (this.d["feeAppWeekList"]) {
          this.barData = this.d["feeAppWeekList"];
          if (this.barData && this.barData.length > 0) {
            for (var i = 0; i < this.barData[0].length; i++) {
              this.lineData.push(0);
              for (var item of this.barData) {
                this.lineData[i] = this.lineData[i] + item["data"][i];
              }
            }
          }
        }
        this.barTitle = this.d["dateWeekList"];
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
    },
    handleShowModeChange() {
      this.refresh();
    },
    handleAutoRefreshChange() {
      if (this.autoRefresh) {
        this.timer = setInterval(this.refresh, 30000);
      } else {
        if (this.timer) {
          clearInterval(this.timer);
        }
      }
    },
  },
  computed: {},
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
