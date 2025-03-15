<template>
  <div class="app-container">
    <el-form
      ref="queryForm"
      :inline="true"
      :model="queryParams"
      label-width="68px"
      size="small"
    >
      <el-form-item label="用户账号" prop="userName">
        <el-input
          v-model="queryParams.userName"
          placeholder="请输入用户账号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="账号分组" prop="deptName">
        <el-input
          v-model="queryParams.deptName"
          placeholder="请输入账号分组"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="登录地址" prop="ipaddr">
        <el-input
          v-model="queryParams.ipaddr"
          placeholder="请输入登录地址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="登录地点" prop="loginLocation">
        <el-input
          v-model="queryParams.loginLocation"
          placeholder="请输入登录地点"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="浏览器" prop="browser">
        <el-input
          v-model="queryParams.browser"
          placeholder="请输入浏览器"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="操作系统" prop="os">
        <el-input
          v-model="queryParams.os"
          placeholder="请输入操作系统"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="软件用户" prop="ifApp">
        <el-select v-model="queryParams.ifApp" placeholder="软件用户" clearable style="width: 240px">
          <el-option v-for="dict in dict.type.sys_yes_no" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="试用用户" prop="ifTrial">
        <el-select v-model="queryParams.ifTrial" placeholder="试用用户" clearable style="width: 240px">
          <el-option v-for="dict in dict.type.sys_yes_no" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="软件信息" prop="appDesc">
        <el-input
          v-model="queryParams.appDesc"
          placeholder="请输入软件信息"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备码" prop="deviceCode">
        <el-input
          v-model="queryParams.deviceCode"
          placeholder="请输入设备码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button
          icon="el-icon-search"
          size="mini"
          type="primary"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
      </el-form-item>
    </el-form>
    <el-table
      v-loading="loading"
      :data="list.slice((pageNum - 1) * pageSize, pageNum * pageSize)"
      style="width: 100%"
    >
      <el-table-column label="序号" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ (pageNum - 1) * pageSize + scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="会话编号"
        prop="tokenId"
      />
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="用户账号"
        prop="userName"
      />
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="账号分组"
        prop="deptName"
      />
      <!-- <el-table-column label="分组名称" align="center" prop="deptName" /> -->
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="登录地址"
        prop="ipaddr"
      />
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="登录地点"
        prop="loginLocation"
      />
      <el-table-column align="center" label="浏览器" prop="browser"/>
      <el-table-column align="center" label="操作系统" prop="os"/>
      <el-table-column align="center" label="软件用户" prop="ifApp">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_yes_no" :value="scope.row.ifApp"/>
        </template>
      </el-table-column>
      <el-table-column align="center" label="试用用户" prop="ifTrial">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_yes_no"
            :value="scope.row.ifTrial"
          />
        </template>
      </el-table-column>
      <el-table-column align="center" label="软件信息" prop="appDesc"/>
      <el-table-column align="center" label="设备码" prop="deviceCode"/>
      <el-table-column
        align="center"
        label="登录时间"
        prop="loginTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.loginTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="过期时间"
        prop="expireTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.expireTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        class-name="small-padding fixed-width"
        label="操作"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleForceLogout(scope.row)"
            v-hasPermi="['monitor:online:forceLogout']"
            >强退
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :limit.sync="pageSize"
      :page.sync="pageNum"
      :total="total"
    />
  </div>
</template>

<script>
import {forceLogout, list} from "@/api/monitor/online";

export default {
  name: "Online",
  dicts: ["sys_yes_no"],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 表格数据
      list: [],
      pageNum: 1,
      pageSize: this.$store.state.settings.pageSize,
      // 查询参数
      queryParams: {
        ipaddr: undefined,
        userName: undefined,
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询登录日志列表 */
    getList() {
      this.loading = true;
      list(this.queryParams).then((response) => {
        this.list = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 强退按钮操作 */
    handleForceLogout(row) {
      this.$modal
        .confirm('是否确认强退名称为"' + row.userName + '"的用户？')
        .then(function () {
          return forceLogout(row.tokenId);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("强退成功");
        })
        .catch(() => {});
    },
  },
};
</script>

