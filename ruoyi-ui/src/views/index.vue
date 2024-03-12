<template>
  <div class="app-container home">
    <!--  授权过期提示  -->
    <span class="index-page">
      <el-alert
        v-if="remainTimeSeconds / 86400 <= 10"
        style="margin-bottom: 20px"
        title="您的授权即将到期"
        :description="'您的授权将于' + expireTime + '到期（剩余 ' + remainTimeReadable + '），请您尽快续费以免影响您的正常使用'"
        type="warning"
        :closable="false"
        :show-icon="true">
      </el-alert>
    </span>
    <!--  头部  -->
    <el-card shadow="never" style="margin-bottom: 10px;padding-top: 5px;margin-top: -8px">
      <div style="margin-top: 2px;">
        <span style="font-size: 22px; color: #343844;">欢迎您，{{ nickName }}，请开始一天的工作吧</span>
        <span style="float: right; margin-top: 4px;">
          <div>
            <span style="color: #6b7687;">
              <b>主程序版本：</b>
              <span>{{ version }}</span>
            </span>
            <span style="margin-left: 10px;color: #6b7687;">
              <b>数据库版本：</b>
              <span>{{ dbVersion }}</span>
            </span>
          </div>
          <div style="float: right; margin-top: 24px;" v-if="checkRole(['admin', 'sadmin'])">
            <el-link style="margin-left: 10px" type="primary" @click="checkUpdate">更新</el-link>
            <el-link style="margin-left: 10px" type="primary" @click="doRestart">重启</el-link>
            <el-link style="margin-left: 10px" type="primary" @click="handleExport">导出日志</el-link>
          </div>
        </span>
      </div>
      <div style="font-size: 16px; color: #6b7687; margin-top: 24px;">&nbsp;{{weatherInfo}}</div>
    </el-card>
    <!--  主体部分  -->
    <div>
      <el-row :gutter="10" style="margin-top: 10px">
        <el-col :lg="18" :md="24" :sm="24" :xs="24">
          <el-card style="margin-bottom: 10px" shadow="never">
            <div slot="header" class="clearfix">
              <span>系统公告</span>
            </div>
            <el-collapse>
              <div v-if="userNotice && userNotice['content']">
                <el-collapse-item name="1">
                  <template slot="title">
                    <el-tag size="mini" style="margin-right: 5px"><i class="el-icon-bell"></i>系统消息</el-tag>{{ userNotice['title'] || '暂无消息' }}
                  </template>
                  <div class="ql-container ql-bubble">
                    <div class="ql-editor">
                      <div>
                        <div v-if="userNotice && userNotice['content']">
                          <span v-html="userNotice['content']"></span>
                        </div>
                        <!-- <div v-else>暂无公告</div> -->
                      </div>
                    </div>
                  </div>
                </el-collapse-item>
              </div>
              <div v-if="checkRole(['agent']) && agentNotice && agentNotice['content']">
                <el-collapse-item name="2">
                  <template slot="title">
                    <el-tag size="mini" style="margin-right: 5px"><i class="el-icon-bell"></i>系统消息(仅代理可见)</el-tag>{{ agentNotice['title'] || '暂无消息' }}
                  </template>
                  <div class="ql-container ql-bubble">
                    <div class="ql-editor">
                      <div>
                        <div v-if="agentNotice && agentNotice['content']">
                          <span v-html="agentNotice['content']"></span>
                        </div>
                        <!-- <div v-else>暂无公告</div> -->
                      </div>
                    </div>
                  </div>
                </el-collapse-item>
              </div>
            </el-collapse>
          </el-card>
          <div v-if="checkRole(['admin', 'sadmin'])">
            <fast-entrance/>
            <dashboard/>
          </div>
        </el-col>
        <el-col :lg="6" :md="24" :sm="24" :xs="24">
          <div v-if="checkRole(['admin', 'sadmin'])">
            <update-log/>
            <contract-info style="margin-top: 10px;"/>
          </div>
        </el-col>
      </el-row>
    </div>
    <el-dialog
      :title="updateTitle"
      :visible.sync="dialogTableVisible"
      custom-class="customClass"
      style="margin-top: 10vh; height: 80%"
      width="1000px"
    >
      <div
        v-if="update"
        align="center"
        style="font-weight: 1000; font-size: large"
      >
        <i class="el-icon-warning"></i>发现新的版本，是否立即更新？
      </div>
      <div v-else align="center" style="font-weight: 1000; font-size: large">
        <i class="el-icon-success"></i>恭喜您，当前已是最新版本
      </div>
      <div class="update_conter" style="margin-top: 20px">
        <div class="update_version">
          <div>
            最新版本：<a class="hylink">{{ lastestVersion }}</a>
          </div>
          <div>更新日期：{{ updateDate }}</div>
        </div>
        <div class="update_logs">
          <div v-html="updateLog"></div>
        </div>
      </div>
      <div v-show="update">
        <div class="update_conter" style="margin-top: 20px">
          <span style="color: red"
          >注意：更新完毕后如有必要本系统将自动重启，届时所有功能将会暂停约2~3分钟，可能造成部分软件用户连接异常或掉线，请在非高峰时段进行更新。</span
          >
        </div>
        <div class="hy-form-btn">
          <el-button
            class="btn"
            round
            style="background-color: #f9f9f9"
            @click="dialogTableVisible = false"
          >忽略本次更新
          </el-button>
          <el-button class="btn" round type="success" @click="doUpdate"
          >立即更新
          </el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getWeatherInfo } from '@/utils/weather'
import { getSimpleLicenseInfo } from "@/api/system/license";
import {getSysInfo} from "@/api/common";
import {getWebsiteConfig} from "@/api/system/website";
import {getNotice} from "@/api/system/index";
import {checkUpdate, doUpdate, getStatus, doRestart, getRestartStatus, exportErrorLog } from "@/api/system/update";
import { checkPermi, checkRole } from "@/utils/permission"; // 权限判断函数
import { mapGetters } from "vuex";
import UpdateLog from '@/views/index/updateLog';
import FastEntrance from '@/views/index/fastEntrance';
import ContractInfo from '@/views/index/contractInfo';
import Dashboard from '@/views/index/dashboard'


export default {
  name: "Index",
  components: { Dashboard, FastEntrance, UpdateLog, ContractInfo },
  computed: {
    ...mapGetters(['nickName']),
  },
  data() {
    return {
      // 版本号
      version: null,
      dbVersion: null,
      // 用户公告
      userNotice: null,
      // 代理公告
      agentNotice: null,
      dialogTableVisible: false,
      updateTitle: "",
      updateLog: "",
      lastestVersion: "",
      updateDate: "",
      update: false,
      timer: null,
      remainTime: 300,
      timer2: null,
      remainTime2: 300,
      exportLoading: false,
      remainTimeReadable: "",
      remainTimeSeconds: 9999999,
      expireTime: null,
      weatherInfo: "",
    };
  },
  methods: {
    checkPermi,
    checkRole,
    getNotice,
    goTarget(href) {
      window.open(href, "_blank");
    },
    getSysInfo() {
      getWeatherInfo().then((res)=>{
        this.weatherInfo = res;
      });
      getSysInfo().then((res) => {
        this.version = res.version + "(" + res.versionNo + ")";
        this.dbVersion = res.dbVersion + "(" + res.dbVersionNo + ")";
      });
      getWebsiteConfig().then((res) => {
        if(!res.data.domain) {
          this.$modal
            .confirm("检测到您尚未配置验证域名，可能影响到验证功能的正常使用，是否立即配置？")
            .then(() => {
              this.$tab.openPage("网站设置", "/sysConfig/website");
            })
            .catch(() => {
            });
        }
      });
    },
    checkUpdate() {
      this.$modal.loading("正在检查更新，请稍后...");
      checkUpdate()
        .then((res) => {
          // console.log(res);
          this.update = res.data.update || res.data.updateDb; //false; //
          if (this.update) {
            this.updateTitle = "检测到新版本";
          } else {
            this.updateTitle = "未发现新版本";
          }
          let updateInfo = res.data.updateInfo;
          this.updateLog = updateInfo.updateLog;
          this.lastestVersion =
            updateInfo.versionName + "(" + updateInfo.versionNo + ")";
          this.updateDate = updateInfo.updateDate;
          this.dialogTableVisible = true;
        })
        .finally(() => {
          this.$modal.closeLoading();
        });
    },
    doUpdate() {
      this.$modal
        .confirm("是否确认更新？")
        .then(() => {
          this.dialogTableVisible = false;
          // console.log("确认");
          doUpdate()
            .then((res) => {
              if (this.timer) {
                clearInterval(this.timer);
              }
              this.timer = setInterval(this.getStatus, 2000);
            })
            .finally(() => {
            });
        })
        .catch(() => {
        });
    },
    doRestart() {
      this.$modal
        .confirm("是否确认重启验证系统？")
        .then(() => {
          this.dialogTableVisible = false;
          // console.log("确认");
          doRestart()
            .then((res) => {
              if (this.timer2) {
                clearInterval(this.timer2);
              }
              this.timer2 = setInterval(this.getRestartStatus, 2000);
            })
            .finally(() => {
            });
        })
        .catch(() => {
        });
    },
    getStatus() {
      if (this.remainTime >= 0) {
        getStatus().then((res) => {
          console.log(res);
          this.$modal.msg(res.msg);
          if (res.finish == 1) {
            clearInterval(this.timer);
          }
        });
        --this.remainTime;
      } else {
        clearInterval(this.timer);
        this.$modal.alert("获取更新结果超时，请登录服务器后台查看");
      }
    },
    getRestartStatus() {
      if (this.remainTime2 >= 0) {
        getRestartStatus().then((res) => {
          console.log(res);
          this.$modal.msg(res.msg);
          if (res.finish == 1) {
            clearInterval(this.timer2);
          }
        });
        --this.remainTime2;
      } else {
        clearInterval(this.timer2);
        this.$modal.alert("获取重启结果超时，请登录服务器后台查看");
      }
    },
    /** 导出按钮操作 */
    handleExport() {
      this.$modal
        .confirm("是否确认导出错误日志？")
        .then(() => {
          this.exportLoading = true;
          return exportErrorLog();
        })
        .then((response) => {
          this.$download.name(response.msg);
          this.exportLoading = false;
        })
        .catch(() => { });
    },
    getSimpleLicenseInfo,
  },
  created() {
    getSimpleLicenseInfo().then((res) => {
      this.expireTime = res.data.expireTime;
      this.remainTimeReadable = res.data.remainTimeReadable;
      this.remainTimeSeconds = res.data.remainTimeSeconds;
    });
    this.getSysInfo();
    getNotice({type: 1}).then((res) => {
      this.userNotice = res.data;
    });
    if (checkRole(["agent"])) {
      getNotice({type: 2}).then((res) => {
        this.agentNotice = res.data;
      });
    }
  },
};
</script>

<style lang="scss" scoped>
.home {

  background-color: #f5f7f9;

  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }

  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }

  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }
}
</style>
<style>
.el-alert__content {
  width: 100%;
}

.customClass {
  max-width: 500px;
}

.update_conter {
  background: #f9f9f9;
  border-radius: 4px;
  padding: 20px;
  margin: 15px;
}

.update_version {
  font-size: 12px;
  margin-bottom: 10px;
  text-align: center;
}

.update_logs {
  font-size: 12px;
  color: #555;
  max-height: 200px;
  overflow: auto;
}

.update_conter span {
  display: block;
  font-size: 12px;
  color: #666;
}

.update_version div {
  font-size: 13.5px !important;
  font-weight: 700;
  text-align: left;
}

.hylink {
  color: #20a53a;
  font-style: inherit;
}

.hy-form-btn {
  text-align: center;
  padding: 10px 0;
}

.hy-form-btn .btn {
  display: inline-block;
  line-height: 38px;
  height: 40px;
  border-radius: 20px;
  width: 140px;
  padding: 0;
  margin-right: 30px;
  font-size: 13.5px;
}

.index-page .el-alert__title {
  font-size: 18px;
}

.index-page .el-alert .el-alert__description {
  font-size: 15px;
  margin: 5px 0 0 0;
}

.index-page .el-alert__icon.is-big {
  font-size: 32px;
  width: 32px;
}

.index-page .el-alert {
  padding: 12px 20px;
}
</style>

