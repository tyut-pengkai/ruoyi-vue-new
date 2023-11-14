<template>
  <div>
    <fast-version :toolCards="appList"/>
    <el-row :gutter="10">
      <el-col :lg="16" :md="24" :sm="24" :xs="24">
        <el-row :gutter="10" style="margin-bottom: 10px">
          <el-col :lg="12" :md="12" :sm="24" :xs="24">
            <show-all-sale :totalFee="totalFee" :feePerDay="feePerDay" :tb="tb" :hb="hb"/>
          </el-col>
          <el-col :lg="12" :md="12" :sm="24" :xs="24">
            <show-all-pay :totalTrade="totalTrade" :transRate="transRate" :totalTradeAll="totalTradeAll" :data="tradeNumList" :xTitle="dateWeekList"/>
          </el-col>
        </el-row>
        <show-app-user :data="appUserCount" :xTitle="dateWeekList"/>
      </el-col>
      <el-col :lg="8" :md="24" :sm="24" :xs="24">
          <show-online :data="onlineUser" :value="onlineUserNum"/>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import ShowCardIndex from '@/views/dashboard/ShowCardIndex'
import ShowAllSale from '@/views/index/dashboard/ShowAllSale'
import ShowAllPay from '@/views/index/dashboard/ShowAllPay'
import ShowAppUser from '@/views/index/dashboard/ShowAppUser'
import ShowOnline from '@/views/index/dashboard/ShowOnline'
import FastVersion from '@/views/index/fastVersion';

import {getDashboard} from "@/api/system/index";

export default {
  name: 'dashboard',
  components: { ShowOnline, ShowAppUser, ShowAllPay, ShowAllSale, ShowCardIndex, FastVersion },
  data() {
    return {
      totalFee: 0,
      feePerDay: 0,
      tb: ['-', '-', '-', '-'],
      hb: ['-', '-', '-', '-'],
      totalTrade: 0,
      totalTradeAll: 0,
      transRate: 0,
      tradeNumList: [],
      dateWeekList: [],
      appUserCount: [],
      onlineUser: [],
      onlineUserNum: 0,
      appList: [],
    }
  },
  created() {
    this.init();
  },
  methods: {
    init() {
      getDashboard().then((res)=>{
        this.totalFee = res.data.totalFee;
        this.feePerDay = res.data.feePerDay;
        this.tb = res.data.tb.split('|');
        this.hb = res.data.hb.split('|');
        this.totalTrade = res.data.totalTrade;
        this.totalTradeAll = res.data.totalTradeAll;
        this.transRate = res.data.transRate;
        this.tradeNumList = [{"appName":"成交笔数", "data": res.data.tradeNumList}]
        this.dateWeekList = res.data.dateWeekList;
        this.appUserCount = res.data.appUserCount;
        this.onlineUser = res.data.onlineUser;
        this.onlineUserNum = res.data.onlineUserNum;
        this.appList = res.data.appList;
      });
    }
  }
}
</script>

<style scoped lang="scss">

</style>
