<template>
  <div>
    <el-card shadow="never">
      <div slot="header" align="center" class="card-title">
        <span>{{ title }}</span>
        <el-tag
          v-if="tag"
          :hit="true"
          :type="tagType"
          effect="plain"
          size="mini"
          style="float: right"
        >{{ tag }}
        </el-tag>
      </div>
      <div>
        <el-row :gutter="8">
          <el-col :span="12">
            <el-card shadow="never" style="background-color: #f8f8f8">
              <div align="center" class="card-title-app">用户总数</div>
              <div align="center" class="card-title-app-num">
                <count-to
                  :duration="2600"
                  :end-val="data[0]"
                  :start-val="0"
                  class="card-num"
                />
              </div>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card shadow="never" style="background-color: #f8f8f8">
              <div align="center" class="card-title-app">VIP用户数</div>
              <div align="center" class="card-title-app-num">
                <count-to
                  :duration="2600"
                  :end-val="data[1]"
                  :start-val="0"
                  class="card-num"
                />
              </div>
            </el-card>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="12" style="margin-top: 8px">
            <el-card shadow="never" style="background-color: #f8f8f8">
              <div align="center" class="card-title-app">
                {{ showMode == "1" ? "本月" : "今日" }}登录数
              </div>
              <div align="center" class="card-title-app-num">
                <count-to
                  :duration="2600"
                  :end-val="data[2]"
                  :start-val="0"
                  class="card-num"
                />
              </div>
            </el-card>
          </el-col>
          <el-col :span="12" style="margin-top: 8px">
            <el-card shadow="never" style="background-color: #f8f8f8">
              <div align="center" class="card-title-app">当前在线数</div>
              <div align="center" class="card-title-app-num">
                <count-to
                  :duration="2600"
                  :end-val="data[3]"
                  :start-val="0"
                  class="card-num"
                />
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>
<script>
import CountTo from "vue-count-to";

export default {
  name: "ShowAppUser",
  components: {
    CountTo,
  },
  props: {
    title: {
      type: String,
      default: "",
    },
    tag: {
      type: String,
      default: "",
    },
    tagType: {
      type: String,
      default: "success",
    },
    data: {
      type: Array,
      default: function () {
        return [0, 0, 0, 0];
      },
    },
    showMode: {
      type: String,
      default: "0",
    },
  },
};
</script>
<style scoped>
.card-title {
  font-size: 14px;
  margin-top: -3px;
  color: #595959;
}

.card-title-app {
  font-size: 12px;
  margin-top: -8px;
  margin-bottom: 3px;
  color: #999;
}

.card-title-app-num {
  font-style: normal;
  font-size: 30px;
  font-weight: 300;
  color: #009688;
  margin-top: 15px;
}
</style>
