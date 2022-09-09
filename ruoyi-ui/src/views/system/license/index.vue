<template>
  <div>
    <el-row style="margin-top: 100px" v-if="serverInfo">
      <el-col :span="12" :offset="6">
        <el-card class="box-card" shadow="never">
          <el-descriptions title="服务器特征信息" border :column="3">
            <el-descriptions-item label="机器码" :span="3">
              {{ serverInfo.sn }}
            </el-descriptions-item>
            <el-descriptions-item label="服务器名称">
              {{ serverInfo.hostName }}
            </el-descriptions-item>
            <el-descriptions-item label="服务器域名">
              {{ serverInfo.hostDomain }}
            </el-descriptions-item>
            <el-descriptions-item label="服务器IP">
              {{ serverInfo.hostIp }}
            </el-descriptions-item>
          </el-descriptions>
          <el-descriptions
            title="服务器授权信息"
            border
            :column="2"
            style="margin-top: 30px"
          >
            <el-descriptions-item label="授权类型">
              {{ licenseInfo.licenseType }}
            </el-descriptions-item>
            <el-descriptions-item label="授权用户">
              {{ licenseInfo.licenseTo }}
            </el-descriptions-item>
            <el-descriptions-item label="软件限制">
              {{
                licenseInfo.appLimit == "*" ? "无限制" : licenseInfo.appLimit
              }}
            </el-descriptions-item>
            <el-descriptions-item label="在线限制">
              {{
                licenseInfo.maxOnline == "*" ? "无限制" : licenseInfo.maxOnline
              }}
            </el-descriptions-item>
            <el-descriptions-item label="授权域名">
              {{
                licenseInfo.licenseDomain == "*"
                  ? "无限制"
                  : licenseInfo.licenseDomain
              }}
            </el-descriptions-item>
            <el-descriptions-item label="授权IP">
              {{
                licenseInfo.licenseIp == "*" ? "无限制" : licenseInfo.licenseIp
              }}
            </el-descriptions-item>
            <el-descriptions-item label="授权期限" :span="2">
              {{ licenseInfo.datetime }}
            </el-descriptions-item>
          </el-descriptions>
          <div style="margin-top: 10px; text-align: center">
            <el-button @click="reload">重载授权</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {getLicenseInfo, loadLicense} from "@/api/system/license";

export default {
  name: "License",
  data() {
    return {
      serverInfo: null,
      licenseInfo: null,
    };
  },
  created() {
    this.getInfo();
  },
  methods: {
    /** 查询软件列表 */
    getInfo() {
      this.loading = true;
      getLicenseInfo().then((response) => {
        this.serverInfo = response.data.serverInfo;
        this.licenseInfo = response.data.licenseInfo;
        this.loading = false;
      });
    },
    reload() {
      this.loading = true;
      loadLicense().then((response) => {
        this.$modal.msgSuccess(response.msg);
        this.getInfo();
        this.loading = false;
      });
    },
  },
};
</script>
