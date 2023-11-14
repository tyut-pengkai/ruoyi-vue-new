<template>
  <div class="dashboard" style="min-height: 90vh">
    <div>
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
                      >账号总数：
                        <span>
                          <count-to
                            :decimals="0"
                            :duration="2600"
                            :end-val="d['userTotal']"
                            :start-val="0"
                            class="card-num"
                            prefix=""
                          />
                        </span>
                        个
                      </el-tag>
                      <!-- <el-divider direction="vertical"></el-divider> -->
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
              <el-col :lg="8" :sm="24" :xs="24" style="margin-top: 10px">
                <show-card-app
                  :increase="d['appUserToday']"
                  :value="d['appUserTotal']"
                  icon="el-icon-user"
                  :increaseUnit="s[showMode]['userTotal']"
                  tag=""
                  title="用户总数"
                ></show-card-app>
              </el-col>
              <el-col :lg="8" :sm="24" :xs="24" style="margin-top: 10px">
                <show-card-app
                  :increase="d['appUserVipToday']"
                  :value="d['appUserVipTotal']"
                  icon="el-icon-user"
                  :increaseUnit="s[showMode]['userTotal']"
                  tag=""
                  title="VIP用户数"
                ></show-card-app>
              </el-col>
              <el-col :lg="8" :sm="24" :xs="24" style="margin-top: 10px">
                <show-card-app
                  :increase="d['onlineTotal']"
                  :value="d['loginAppUserToday']"
                  icon="el-icon-user"
                  increaseUnit="当前在线"
                  tag=""
                  :title="s[showMode]['loginAppUserToday']"
                  unit="人"
                ></show-card-app>
              </el-col>
            </el-row>
            <el-row :gutter="10">
              <el-col :lg="8" :sm="24" :xs="24" style="margin-top: 10px">
                <show-card-app
                  :increase="d['cardToday']"
                  :value="d['cardTotal']"
                  icon="el-icon-tickets"
                  :increaseUnit="s[showMode]['cardTotal']"
                  tag=""
                  title="总制充值卡数量"
                  unit="张"
                ></show-card-app>
              </el-col>
              <el-col :lg="8" :sm="24" :xs="24" style="margin-top: 10px">
                <show-card-app
                  :increase="d['cardActiveToday']"
                  :value="d['cardActive']"
                  icon="el-icon-tickets"
                  :increaseUnit="s[showMode]['activeCardTotal']"
                  tag=""
                  title="已激活充值卡数量"
                  unit="张"
                ></show-card-app>
              </el-col>
              <el-col :lg="8" :sm="24" :xs="24" style="margin-top: 10px">
                <show-card-app
                  :increase="d['cardNoActiveToday']"
                  :value="d['cardNoActive']"
                  icon="el-icon-tickets"
                  :increaseUnit="s[showMode]['userTotal']"
                  tag=""
                  title="未激活充值卡数量"
                  unit="张"
                ></show-card-app>
              </el-col>
            </el-row>
            <el-row :gutter="10">
              <el-col :lg="8" :sm="24" :xs="24" style="margin-top: 10px">
                <show-card-app
                  :increase="d['loginCodeToday']"
                  :value="d['loginCodeTotal']"
                  icon="el-icon-user"
                  :increaseUnit="s[showMode]['loginCodeTotal']"
                  tag=""
                  title="总登录码数量"
                ></show-card-app>
              </el-col>
              <el-col :lg="8" :sm="24" :xs="24" style="margin-top: 10px">
                <show-card-app
                  :increase="d['loginCodeActiveToday']"
                  :value="d['loginCodeActive']"
                  icon="el-icon-user"
                  :increaseUnit="s[showMode]['activeCardTotal']"
                  tag=""
                  title="已激活登录码数量"
                ></show-card-app>
              </el-col>
              <el-col :lg="8" :sm="24" :xs="24" style="margin-top: 10px">
                <show-card-app
                  :increase="d['loginCodeNoActiveToday']"
                  :value="d['loginCodeNoActive']"
                  icon="el-icon-user"
                  :increaseUnit="s[showMode]['userTotal']"
                  tag=""
                  title="未激活登录码数量"
                ></show-card-app>
              </el-col>
            </el-row>
            <el-row :gutter="10">
              <el-col :lg="10" :sm="24" :xs="24" style="margin-top: 10px">
                <el-card shadow="never">
                  <div class="card-title">
                    <i
                      class="el-icon-data-analysis"
                      style="margin-right: 5px"
                    ></i>
                    <span>{{ s[showMode]["increaseUserTitle"] }}</span>
                  </div>
                  <div style="margin-top: 20px">
                    <bar-chart :data="barData" :xTitle="barTitle"></bar-chart>
                  </div>
                </el-card>
              </el-col>
              <el-col :lg="7" :sm="24" :xs="24" style="margin-top: 10px">
                <el-card shadow="never">
                  <div class="card-title">
                    <i class="el-icon-pie-chart" style="margin-right: 5px"></i>
                    <span>{{ s[showMode]["activeAppCardTitle"] }}</span>
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
                    <span>{{ s[showMode]["activeTemplateCardTitle"] }}</span>
                  </div>
                  <div style="margin-top: 20px">
                    <pie-chart :data="pie2Data"></pie-chart>
                  </div>
                </el-card>
              </el-col>
            </el-row>
            <!-- <el-row>
              <el-col :lg="24" :sm="24" :xs="24" style="margin-top: 10px">
                <el-card shadow="never">
                  <div class="card-title">
                    <span>软件用户分布</span>
                  </div>
                  <div style="margin-top: 20px">
                    <china-map></china-map>
                  </div>
                </el-card>
              </el-col>
            </el-row> -->
            <el-row :gutter="10">
              <el-col
                v-for="(item, index) in d['appDataList']"
                :key="index"
                :lg="8"
                :sm="12"
                :xs="24"
                style="margin-top: 10px"
              >
                <show-app-user
                  :data="[
                    item['appUserTotal'],
                    item['appUserVipTotal'],
                    item['loginToday'],
                    item['online'],
                  ]"
                  :tag="'TOP' + (index + 1)"
                  :title="item['appName']"
                  :showMode="showMode"
                ></show-app-user>
              </el-col>
            </el-row>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import ShowCardApp from "@/views/dashboard/ShowCardApp";
import {getDashboardInfoAppView} from "@/api/common";
import ShowAppUser from "@/views/dashboard/ShowAppUser";
import PieChart from "@/views/dashboard/MyPieChart";
import BarChart from "@/views/dashboard/MyBarChart";
import ChinaMap from "@/views/dashboard/ChinaMap";
import CountTo from "vue-count-to";

export default {
  name: "AppView",
  components: {
    CountTo,
    ShowCardApp,
    ShowAppUser,
    PieChart,
    BarChart,
    ChinaMap,
  },
  data() {
    return {
      username: this.$store.state.user.name,
      refreshLoading: false,
      showMode: "0",
      autoRefresh: false,
      timer: null,
      d: {
        // feeAppList: [
        //   {
        //     appName: "123",
        //     feeTotal: 0,
        //     feeToday: 0,
        //     feeYesterday: 0,
        //     feeWeek: 0,
        //   },
        // ],
      },
      pieData: [
        //   { value: 320, name: "APP1" }
      ],
      pie2Data: [
        //   { value: 320, name: "APP1" }
      ],
      barData: [
        //   {data: [10, 20, 30, 45, 58, 46, 17], appName: "123"}
      ],
      barTitle: [],
      s: {
        0: {
          userTotal: "今日新增",
          loginAppUserToday: "今日登录用户",
          cardTotal: "今日制卡",
          activeCardTotal: "今日激活",
          loginCodeTotal: "今日制码",
          increaseUserTitle: "近七日激活卡密(软件)",
          activeAppCardTitle: "近七日激活卡密(软件)",
          activeTemplateCardTitle: "近七日激活卡密(卡类)",
        },
        1: {
          userTotal: "本月新增",
          loginAppUserToday: "本月登录用户",
          cardTotal: "本月制卡",
          activeCardTotal: "本月激活",
          loginCodeTotal: "本月制码",
          increaseUserTitle: "近半年激活卡密(软件)",
          activeAppCardTitle: "近半年激活卡密(软件)",
          activeTemplateCardTitle: "近半年激活卡密(卡类)",
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
      getDashboardInfoAppView({showMode: this.showMode}).then((response) => {
        this.d = response.data;
        this.pieData = [];
        if (this.d["cardActiveList"]) {
          for (var item of this.d["cardActiveList"]) {
            if (item["totalCount"] > 0) {
              this.pieData.push({
                value: item["totalCount"],
                name: item["appName"],
              });
            }
          }
        }
        this.pie2Data = [];
        if (this.d["cardActiveList2"]) {
          for (var item of this.d["cardActiveList2"]) {
            if (item["totalCount"] > 0) {
              this.pie2Data.push({
                value: item["totalCount"],
                name: item["cardName"],
              });
            }
          }
        }
        this.barData = [];
        this.barTitle = [];
        if (this.d["increaseUserWeekList"]) {
          this.barData = this.d["increaseUserWeekList"];
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
