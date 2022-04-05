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
                            :end-val="1000"
                            :start-val="0"
                            class="card-num"
                            prefix=""
                          />
                        </span>
                        个
                      </el-tag>
                      <el-divider direction="vertical"></el-divider>
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
                  :increase="500"
                  :value="1000"
                  icon="el-icon-user"
                  tag=""
                  title="用户总数"
                ></show-card-app>
              </el-col>
              <el-col :lg="8" :sm="24" :xs="24" style="margin-top: 10px">
                <show-card-app
                  :increase="50"
                  :value="100"
                  icon="el-icon-user"
                  tag=""
                  title="VIP用户数"
                ></show-card-app>
              </el-col>
              <el-col :lg="8" :sm="24" :xs="24" style="margin-top: 10px">
                <show-card-app
                  :increase="50"
                  :value="100"
                  icon="el-icon-user"
                  increaseUnit="当前在线"
                  tag=""
                  title="今日登录用户"
                  unit="人"
                ></show-card-app>
              </el-col>
            </el-row>
            <el-row :gutter="10">
              <el-col :lg="8" :sm="24" :xs="24" style="margin-top: 10px">
                <show-card-app
                  :increase="50"
                  :value="100"
                  icon="el-icon-tickets"
                  increaseUnit="今日制卡"
                  tag=""
                  title="总制充值卡数量"
                  unit="张"
                ></show-card-app>
              </el-col>
              <el-col :lg="8" :sm="24" :xs="24" style="margin-top: 10px">
                <show-card-app
                  :increase="500"
                  :value="1000"
                  icon="el-icon-tickets"
                  increaseUnit="今日激活"
                  tag=""
                  title="已激活充值卡数量"
                  unit="张"
                ></show-card-app>
              </el-col>
              <el-col :lg="8" :sm="24" :xs="24" style="margin-top: 10px">
                <show-card-app
                  :increase="50"
                  :value="100"
                  icon="el-icon-tickets"
                  tag=""
                  title="未激活充值卡数量"
                  unit="张"
                ></show-card-app>
              </el-col>
            </el-row>
            <el-row :gutter="10">
              <el-col :lg="8" :sm="24" :xs="24" style="margin-top: 10px">
                <show-card-app
                  :increase="50"
                  :value="100"
                  icon="el-icon-user"
                  increaseUnit="今日制码"
                  tag=""
                  title="总登录码数量"
                ></show-card-app>
              </el-col>
              <el-col :lg="8" :sm="24" :xs="24" style="margin-top: 10px">
                <show-card-app
                  :increase="500"
                  :value="1000"
                  icon="el-icon-user"
                  increaseUnit="今日激活"
                  tag=""
                  title="已激活登录码数量"
                ></show-card-app>
              </el-col>
              <el-col :lg="8" :sm="24" :xs="24" style="margin-top: 10px">
                <show-card-app
                  :increase="50"
                  :value="100"
                  icon="el-icon-user"
                  tag=""
                  title="未激活登录码数量"
                ></show-card-app>
              </el-col>
            </el-row>
            <el-row :gutter="10">
              <el-col :lg="12" :sm="24" :xs="24" style="margin-top: 10px">
                <el-card shadow="never">
                  <div class="card-title">
                    <span>近七日新增用户数</span>
                  </div>
                  <div style="margin-top: 20px">
                    <bar-chart :data="barData"></bar-chart>
                  </div>
                </el-card>
              </el-col>
              <el-col :lg="12" :sm="24" :xs="24" style="margin-top: 10px">
                <el-card shadow="never">
                  <div class="card-title">
                    <span>近七日激活充值卡</span>
                  </div>
                  <div style="margin-top: 20px">
                    <pie-chart :data="pieData"></pie-chart>
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
                v-for="(item, index) in d['feeAppList']"
                :key="index"
                :lg="8"
                :sm="12"
                :xs="24"
                style="margin-top: 10px"
              >
                <show-app-user
                  :data="[
                    item['feeTotal'],
                    item['feeToday'],
                    item['feeYesterday'],
                    item['feeWeek'],
                  ]"
                  :tag="'TOP' + (index + 1)"
                  :title="item['appName']"
                  showMode="0"
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
import ShowCardSmall from "@/views/dashboard/ShowCardSmall";
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
    ShowCardSmall,
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
        feeAppList: [
          {
            appName: "123",
            feeTotal: 0,
            feeToday: 0,
            feeYesterday: 0,
            feeWeek: 0,
          },
        ],
      },
      pieData: [{value: 320, name: "APP1"}],
      barData: [{data: [10, 20, 30, 45, 58, 46, 17], appName: "123"}],
    };
  },
  methods: {
    getDashboardInfo() {
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
