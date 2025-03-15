<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" size="small">
      <el-form-item label="代理名称" prop="userName">
        <el-input v-model="queryParams.userName" clearable placeholder="请输入代理名称" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="代理过期时间">
        <el-date-picker v-model="daterangeExpireTime" end-placeholder="结束日期" style="width: 240px" type="daterange"
          range-separator="-" start-placeholder="开始日期" value-format="yyyy-MM-dd"></el-date-picker>
      </el-form-item>
      <el-form-item label="代理状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择代理状态" clearable>
          <el-option v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="允许发展下级" prop="enableAddSubagent">
        <el-select v-model="queryParams.enableAddSubagent" placeholder="请选择允许发展下级" clearable>
          <el-option v-for="dict in dict.type.sys_yes_no" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
<!--      <el-form-item label="修改下级代理密码" prop="enableUpdateSubagentPassword">-->
<!--        <el-select v-model="queryParams.enableUpdateSubagentPassword" placeholder="请选择修改下级代理密码" clearable>-->
<!--          <el-option-->
<!--              v-for="dict in dict.type.sys_yes_no"-->
<!--              :key="dict.value"-->
<!--              :label="dict.label"-->
<!--              :value="dict.value"-->
<!--          />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="修改下级代理状态" prop="enableUpdateSubagentStatus">-->
<!--        <el-select v-model="queryParams.enableUpdateSubagentStatus" placeholder="请选择修改下级代理状态" clearable>-->
<!--          <el-option-->
<!--              v-for="dict in dict.type.sys_yes_no"-->
<!--              :key="dict.value"-->
<!--              :label="dict.label"-->
<!--              :value="dict.value"-->
<!--          />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="给下级代理加款" prop="enableUnbindAppUser">-->
<!--        <el-select v-model="queryParams.enableUnbindAppUser" placeholder="请选择给下级代理加款" clearable>-->
<!--          <el-option-->
<!--              v-for="dict in dict.type.sys_yes_no"-->
<!--              :key="dict.value"-->
<!--              :label="dict.label"-->
<!--              :value="dict.value"-->
<!--          />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="用户解冻" prop="enableUpdateAppUserStatus0">-->
<!--        <el-select v-model="queryParams.enableUpdateAppUserStatus0" placeholder="请选择用户解冻" clearable>-->
<!--          <el-option-->
<!--              v-for="dict in dict.type.sys_yes_no"-->
<!--              :key="dict.value"-->
<!--              :label="dict.label"-->
<!--              :value="dict.value"-->
<!--          />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="用户冻结" prop="enableUpdateAppUserStatus1">-->
<!--        <el-select v-model="queryParams.enableUpdateAppUserStatus1" placeholder="请选择用户冻结" clearable>-->
<!--          <el-option-->
<!--              v-for="dict in dict.type.sys_yes_no"-->
<!--              :key="dict.value"-->
<!--              :label="dict.label"-->
<!--              :value="dict.value"-->
<!--          />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="修改用户时间" prop="enableUpdateAppUserTime">-->
<!--        <el-select v-model="queryParams.enableUpdateAppUserTime" placeholder="请选择修改用户时间" clearable>-->
<!--          <el-option-->
<!--              v-for="dict in dict.type.sys_yes_no"-->
<!--              :key="dict.value"-->
<!--              :label="dict.label"-->
<!--              :value="dict.value"-->
<!--          />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="修改用户点数" prop="enableUpdateAppUserPoint">-->
<!--        <el-select v-model="queryParams.enableUpdateAppUserPoint" placeholder="请选择修改用户点数" clearable>-->
<!--          <el-option-->
<!--              v-for="dict in dict.type.sys_yes_no"-->
<!--              :key="dict.value"-->
<!--              :label="dict.label"-->
<!--              :value="dict.value"-->
<!--          />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="修改登录用户数量限制" prop="enableUpdateAppUserLoginLimitU">-->
<!--        <el-select v-model="queryParams.enableUpdateAppUserLoginLimitU" placeholder="请选择修改登录用户数量限制" clearable>-->
<!--          <el-option-->
<!--              v-for="dict in dict.type.sys_yes_no"-->
<!--              :key="dict.value"-->
<!--              :label="dict.label"-->
<!--              :value="dict.value"-->
<!--          />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="修改登录机器数量限制" prop="enableUpdateAppUserLoginLimitM">-->
<!--        <el-select v-model="queryParams.enableUpdateAppUserLoginLimitM" placeholder="请选择修改登录机器数量限制" clearable>-->
<!--          <el-option-->
<!--              v-for="dict in dict.type.sys_yes_no"-->
<!--              :key="dict.value"-->
<!--              :label="dict.label"-->
<!--              :value="dict.value"-->
<!--          />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="修改软件用户自定义参数" prop="enableUpdateAppUserCustomParams">-->
<!--        <el-select v-model="queryParams.enableUpdateAppUserCustomParams" placeholder="请选择修改软件用户自定义参数" clearable>-->
<!--          <el-option-->
<!--              v-for="dict in dict.type.sys_yes_no"-->
<!--              :key="dict.value"-->
<!--              :label="dict.label"-->
<!--              :value="dict.value"-->
<!--          />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="修改软件用户备注" prop="enableUpdateAppUserRemark">-->
<!--        <el-select v-model="queryParams.enableUpdateAppUserRemark" placeholder="请选择修改软件用户备注" clearable>-->
<!--          <el-option-->
<!--              v-for="dict in dict.type.sys_yes_no"-->
<!--              :key="dict.value"-->
<!--              :label="dict.label"-->
<!--              :value="dict.value"-->
<!--          />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="查看用户联系方式" prop="enableViewUserContact">-->
<!--        <el-select v-model="queryParams.enableViewUserContact" placeholder="请选择查看用户联系方式" clearable>-->
<!--          <el-option-->
<!--              v-for="dict in dict.type.sys_yes_no"-->
<!--              :key="dict.value"-->
<!--              :label="dict.label"-->
<!--              :value="dict.value"-->
<!--          />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="修改用户联系方式" prop="enableUpdateUserContact">-->
<!--        <el-select v-model="queryParams.enableUpdateUserContact" placeholder="请选择修改用户联系方式" clearable>-->
<!--          <el-option-->
<!--              v-for="dict in dict.type.sys_yes_no"-->
<!--              :key="dict.value"-->
<!--              :label="dict.label"-->
<!--              :value="dict.value"-->
<!--          />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="修改用户密码" prop="enableUpdateUserPassword">-->
<!--        <el-select v-model="queryParams.enableUpdateUserPassword" placeholder="请选择修改用户密码" clearable>-->
<!--          <el-option-->
<!--              v-for="dict in dict.type.sys_yes_no"-->
<!--              :key="dict.value"-->
<!--              :label="dict.label"-->
<!--              :value="dict.value"-->
<!--          />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="修改用户备注" prop="enableUpdateUserRemark">-->
<!--        <el-select v-model="queryParams.enableUpdateUserRemark" placeholder="请选择修改用户备注" clearable>-->
<!--          <el-option-->
<!--              v-for="dict in dict.type.sys_yes_no"-->
<!--              :key="dict.value"-->
<!--              :label="dict.label"-->
<!--              :value="dict.value"-->
<!--          />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="修改卡密时间" prop="enableUpdateCardTime">-->
<!--        <el-select v-model="queryParams.enableUpdateCardTime" placeholder="请选择修改卡密时间" clearable>-->
<!--          <el-option-->
<!--              v-for="dict in dict.type.sys_yes_no"-->
<!--              :key="dict.value"-->
<!--              :label="dict.label"-->
<!--              :value="dict.value"-->
<!--          />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="修改卡密点数" prop="enableUpdateCardPoint">-->
<!--        <el-select v-model="queryParams.enableUpdateCardPoint" placeholder="请选择修改卡密点数" clearable>-->
<!--          <el-option-->
<!--              v-for="dict in dict.type.sys_yes_no"-->
<!--              :key="dict.value"-->
<!--              :label="dict.label"-->
<!--              :value="dict.value"-->
<!--          />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="修改卡密登录用户数量限制" prop="enableUpdateCardLoginLimitU">-->
<!--        <el-select v-model="queryParams.enableUpdateCardLoginLimitU" placeholder="请选择修改卡密登录用户数量限制" clearable>-->
<!--          <el-option-->
<!--              v-for="dict in dict.type.sys_yes_no"-->
<!--              :key="dict.value"-->
<!--              :label="dict.label"-->
<!--              :value="dict.value"-->
<!--          />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="修改卡密机器数量限制" prop="enableUpdateCardLoginLimitM">-->
<!--        <el-select v-model="queryParams.enableUpdateCardLoginLimitM" placeholder="请选择修改卡密机器数量限制" clearable>-->
<!--          <el-option-->
<!--              v-for="dict in dict.type.sys_yes_no"-->
<!--              :key="dict.value"-->
<!--              :label="dict.label"-->
<!--              :value="dict.value"-->
<!--          />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="修改卡密自定义参数" prop="enableUpdateCardCustomParams">-->
<!--        <el-select v-model="queryParams.enableUpdateCardCustomParams" placeholder="请选择修改卡密自定义参数" clearable>-->
<!--          <el-option-->
<!--              v-for="dict in dict.type.sys_yes_no"-->
<!--              :key="dict.value"-->
<!--              :label="dict.label"-->
<!--              :value="dict.value"-->
<!--          />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="修改卡密备注" prop="enableUpdateCardRemark">-->
<!--        <el-select v-model="queryParams.enableUpdateCardRemark" placeholder="请选择修改卡密备注" clearable>-->
<!--          <el-option-->
<!--              v-for="dict in dict.type.sys_yes_no"-->
<!--              :key="dict.value"-->
<!--              :label="dict.label"-->
<!--              :value="dict.value"-->
<!--          />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="卡密解冻" prop="enableUpdateCardStatus0">-->
<!--        <el-select v-model="queryParams.enableUpdateCardStatus0" placeholder="请选择卡密解冻" clearable>-->
<!--          <el-option-->
<!--              v-for="dict in dict.type.sys_yes_no"-->
<!--              :key="dict.value"-->
<!--              :label="dict.label"-->
<!--              :value="dict.value"-->
<!--          />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="卡密冻结" prop="enableUpdateCardStatus1">-->
<!--        <el-select v-model="queryParams.enableUpdateCardStatus1" placeholder="请选择卡密冻结" clearable>-->
<!--          <el-option-->
<!--              v-for="dict in dict.type.sys_yes_no"-->
<!--              :key="dict.value"-->
<!--              :label="dict.label"-->
<!--              :value="dict.value"-->
<!--          />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="批量换卡" prop="enableBatchCardReplace">-->
<!--        <el-select v-model="queryParams.enableBatchCardReplace" placeholder="请选择批量换卡" clearable>-->
<!--          <el-option-->
<!--              v-for="dict in dict.type.sys_yes_no"-->
<!--              :key="dict.value"-->
<!--              :label="dict.label"-->
<!--              :value="dict.value"-->
<!--          />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
      <el-form-item label="备注" prop="remark">
        <el-input
          v-model="queryParams.remark"
          placeholder="请输入备注"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索
        </el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置
        </el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['agent:agentUser:add']"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          type="primary"
          :disabled="!$auth.hasAgentPermi('enableAddSubagent')">新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button icon="el-icon-key" plain type="success" size="mini" @click="handleViewMyPerm">我的权限
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button icon="el-icon-sort" plain type="info" size="mini" @click="toggleExpandAll">展开/折叠
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-if="refreshTable" v-loading="loading" :data="agentUserList" row-key="agentId" border
      :default-expand-all="isExpandAll" :tree-props="{ children: 'children', hasChildren: 'hasChildren' }">
      <!--      <el-table-column label="上级代理ID" prop="parentAgentId" />-->
      <!--      <el-table-column label="用户ID" align="center" prop="userId" />-->
      <el-table-column :show-overflow-tooltip="true" align="left" label="代理名称" prop="userId">
        <template slot-scope="scope">
          {{ scope.row.user.nickName }}({{ scope.row.user.userName }})
        </template>
      </el-table-column>
      <el-table-column label="上级代理" align="left" prop="parentAgentId" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <div v-if="scope.row.parentUser">
            {{ scope.row.parentUser.nickName }}({{
              scope.row.parentUser.userName
            }})
          </div>
        </template>
      </el-table-column>
<!--      <el-table-column label="允许发展子代理" align="center" prop="enableAddSubagent">-->
<!--        <template slot-scope="scope">-->
<!--          <dict-tag :options="dict.type.sys_yes_no" :value="scope.row.enableAddSubagent" />-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="代理过期时间" align="center" prop="expireTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.expireTime) }}</span>
        </template>
      </el-table-column>
<!--      <el-table-column align="center" label="代理状态" prop="status">-->
<!--        <template slot-scope="scope">-->
<!--          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status" />-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column align="center" label="备注" prop="remark" />
      <el-table-column label="代理状态" align="center">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            active-value="0"
            inactive-value="1"
            @change="handleStatusChange(scope.row)"
            :disabled="!$auth.hasAgentPermi('enableUpdateSubagentStatus')"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="允许添加下级代理" align="center">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.enableAddSubagent"
            active-value="Y"
            inactive-value="N"
            @change="handleEnableAddSubagentChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
              v-hasPermi="['agent:agentUser:add']"
              v-hasRole="['sadmin', 'admin']"
              icon="el-icon-plus"
              size="mini" type="text"
              @click="handleAdd(scope.row)"
              :disabled="!$auth.hasAgentPermi('enableAddSubagent')">新增
          </el-button>
          <el-button size="mini" type="text" v-hasPermi="['agent:agentUser:edit']" @click="handleUpdate(scope.row)"
            icon="el-icon-edit">修改
          </el-button>
          <!-- <el-button
            v-hasPermi="['agent:agentUser:grant']"
            icon="el-icon-edit"
            size="mini"
            type="text"
            @click="handleGrant(scope.row)"
            >授权
          </el-button> -->
          <el-button
              v-hasPermi="['agent:agentUser:resetPwd']"
              icon="el-icon-key"
              size="mini" type="text"
              @click="handleResetPwd(scope.row)"
              :disabled="!$auth.hasAgentPermi('enableUpdateSubagentPassword')">重置密码
          </el-button>
          <el-button size="mini" type="text" v-hasPermi="['agent:agentUser:remove']" @click="handleDelete(scope.row)"
            icon="el-icon-delete" :disabled="!$auth.hasAgentPermi('enableDeleteSubagent')">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改代理用户对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="850px">
      <el-steps :active="4" style="margin-bottom: 10px" v-show="form.agentId == null">
        <el-step title="注册账号" description="可由管理创建或代理自行注册账号" icon="el-icon-user"></el-step>
        <el-step title="输入账号" description="输入并选择代理账号" icon="el-icon-edit"></el-step>
        <el-step title="设置权限" description="设置代理相关管理权限" icon="el-icon-key"></el-step>
        <el-step title="设置卡类" description="设置代理可售卡类及价格" icon="el-icon-tickets"></el-step>
      </el-steps>
      <el-form ref="form" :model="form" :rules="rules">
        <el-tabs type="border-card">
          <el-tab-pane label="基本信息">
            <el-form-item label="上级代理" prop="parentAgentId">
              <div v-if="checkRole(['sadmin', 'admin'])">
                <treeselect v-model="form.parentAgentId" :normalizer="normalizer" :options="agentUserOptions"
                            placeholder="请选择上级代理" />
              </div>
              <div v-else>
                {{ user.nickName + "(" + user.userName + ")" }}
              </div>
            </el-form-item>
            <el-row>
              <el-col :span="10">
                <el-form-item label="代理账号" prop="userId">
                  <!--          <el-input v-model="form.userId" placeholder="请输入用户ID" />-->
                  <div v-if="form.agentId == null">
                    <el-select v-model="form.userId" :clearable="true" filterable :loading="loading"
                               :remote-method="remoteMethod" prop="userId" placeholder="请输入账号" remote>
                      <el-option v-for="item in nonAgentList" :key="item.userId" :disabled="item.disabled"
                                 :label="item.nickName + '(' + item.userName + ')'" :value="item.userId">
                      </el-option>
                    </el-select>
                  </div>
                  <div v-else>{{ form.user.nickName }}({{ form.user.userName }})</div>
                </el-form-item>
              </el-col>
              <el-col :span="14">
                <el-form-item label="代理过期时间(留空长期)" prop="expireTime">
                  <el-date-picker clearable v-model="form.expireTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
                                  placeholder="选择代理过期时间" :picker-options="pickerOptions">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="10">
                <el-form-item label="代理状态">
                  <el-radio-group v-model="form.status" :disabled="!$auth.hasAgentPermi('enableUpdateSubagentStatus')">
                    <el-radio v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.value">{{ dict.label
                      }}
                    </el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="14">
                <el-form-item label="允许添加下级代理" prop="enableAddSubagent">
              <span>
                <el-tooltip
                    content="如果由允许修改为不允许，已经添加的子代理不受影响"
                    placement="top"
                >
                  <i
                      class="el-icon-question"
                      style="margin-left: -12px; margin-right: 10px"
                  ></i>
                </el-tooltip>
              </span>
                  <el-radio-group v-model="form.enableAddSubagent">
                    <el-radio v-for="dict in dict.type.sys_yes_no" :key="dict.value" :label="dict.value">{{ dict.label }}
                    </el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-tab-pane>
          <el-tab-pane label="代理权限配置">
            <div style="text-align: center">
              <el-transfer
                  ref="transfer"
                  style="text-align: left; display: inline-block"
                  filterable
                  :filter-method="filterMethod"
                  filter-placeholder="请输入权限名称"
                  :titles="['未允许权限', '已允许权限']"
                  v-model="permValue"
                  :data="permData">
              </el-transfer>
            </div>
          </el-tab-pane>
          <el-tab-pane label="代理卡类配置">
            <el-form-item label="代理卡密类别/单码类别">
              <el-alert
                :closable="false"
                show-icon
                style="margin-bottom: 10px; margin-top: 10px; line-height: normal"
                type="info"
              >
                <template slot="title">
                  <span> 左侧选择框处于勾选状态时代表具有该制卡权限 </span>
                </template>
              </el-alert>
              <el-row>
                <el-col :span="24">
                  <!-- 批量操作： -->
                  批量：
                  <el-select v-model="batchSelectTemplate" multiple filterable collapse-tags
                             style="width:200px;margin-right: 3px;" placeholder="批量选择" size="small">
                    <el-option v-for="item in templateNameFilters2" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                  </el-select>
                  <el-input-number v-model="batchAgentPrice" :min="0" :precision="2" :step="0.01" controls-position="right"
                                   style="width: 120px;margin-right: 3px;" placeholder="代理价格" size="small" />
                  <el-date-picker v-model="batchExpireTime" clearable placeholder="过期时间" :picker-options="pickerOptions"
                                  value-format="yyyy-MM-dd HH:mm:ss" type="datetime" style="width: 180px;margin-right: 3px;" size="small">
                  </el-date-picker>
                  <el-input v-model="batchRemark" placeholder="备注" style="width: 120px;margin-right: 3px;" size="small" />
                  <el-button type="primary" size="small" @click="handleBatchSet">批量填充</el-button>
                </el-col>
              </el-row>
              <el-table ref="templateTable" :data="templateList" tooltip-effect="dark" style="width: 100%" max-height="300"
                        :header-row-style="{ height: '30px' }" :header-cell-style="{ background: '#f5f7fa', padding: '0px' }"
                        :row-style="{ height: '30px' }" :cell-style="{ padding: '0px' }" size='mini' border height="300"
                        @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="40" fixed> </el-table-column>
                <el-table-column label="卡密类别/单码类别名称" width="180" fixed show-overflow-tooltip :filters="templateNameFilters"
                                 :filter-method="filterHandler">
                  <template slot-scope="scope">
                    {{
                      "[" +
                      scope.row.appName +
                      "]" +
                      scope.row.templateName
                    }}
                  </template>
                </el-table-column>
                <el-table-column prop="price" label="零售价格" width="80" show-overflow-tooltip>
                  <template slot-scope="scope">
                    {{ scope.row.price }}
                    <span>元</span>
                  </template>
                </el-table-column>
                <el-table-column prop="myPrice" label="我的提卡价格" width="120" show-overflow-tooltip>
                  <template slot-scope="scope">
                    {{ scope.row.myPrice ? scope.row.myPrice + '元' : (scope.row.myPrice === 0 ? '0元' : '未授权')}}
                  </template>
                </el-table-column>
                <el-table-column prop="agentPrice" label="代理提卡价格" width="150">
                  <template slot-scope="scope">
                    <el-input-number v-model="scope.row.agentPrice" :min="0" :precision="2" :step="0.01"
                                     controls-position="right" style="width: 120px" placeholder="代理提卡价格" size="mini" />
                    <!-- <span>元</span> -->
                  </template>
                </el-table-column>
                <el-table-column prop="expireTime" label="代理该卡过期时间(留空长期)" width="210">
                  <template slot-scope="scope">
                    <el-date-picker v-model="scope.row.expireTime" clearable placeholder="请选择时间"
                                    :picker-options="pickerOptions" value-format="yyyy-MM-dd HH:mm:ss" type="datetime" style="width: 180px"
                                    size="mini">
                    </el-date-picker>
                  </template>
                </el-table-column>
                <el-table-column prop="remark" label="备注信息" width="130">
                  <template slot-scope="scope">
                    <el-input v-model="scope.row.remark" placeholder="请输入内容" style="width: 100px;" size="mini" />
                  </template>
                </el-table-column>
              </el-table>
            </el-form-item>
          </el-tab-pane>
        </el-tabs>
        <!-- <el-form-item label="代理卡密类别/单码类别">
          <el-checkbox-group v-model="checkList">
            <el-checkbox
              v-for="(item, idx) in templateList"
              :key="idx"
              :label="idx"
            >
              <div>
                <el-row>
                  <el-col :span="6">
                    <el-form-item>
                      {{
                        "[" +
                        item.appName +
                        "]" +
                        item.templateName +
                        "|零售" +
                        item.price +
                        "元"
                      }}
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="代理价格" prop="agentPrice">
                      <el-input-number
                        v-model="item.agentPrice"
                        :min="0"
                        :precision="2"
                        :step="0.01"
                        controls-position="right"
                        style="width: 120px"
                      />
                      <span>元</span>
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="代理该卡过期时间" prop="expireTime">
                      <el-date-picker
                        v-model="item.expireTime"
                        clearable
                        placeholder="请选择代理该卡过期时间"
                        :picker-options="pickerOptions"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        type="datetime"
                        style="width: 120px"
                      >
                      </el-date-picker>
                    </el-form-item>
                  </el-col>
                  <el-col :span="2">
                    <el-form-item label="备注" prop="remark">
                      <el-input
                        v-model="item.remark"
                        placeholder="请输入内容"
                        style="width: 120px"
                      />
                    </el-form-item>
                  </el-col>
                </el-row>
              </div>
            </el-checkbox>
          </el-checkbox-group> -->
        <!-- <div v-for="(item, idx) in templateList" :key="idx">
            <div>
              <el-checkbox :v-model="form.cid[idx].checked">
                {{
                  "[" +
                  item.appName +
                  "]" +
                  item.templateName +
                  "|零售" +
                  item.price +
                  "元"
                }}
              </el-checkbox>
            </div>
          </div>
        </el-form-item> -->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>


    <el-dialog title="代理信息" :visible.sync="openMyPerm" append-to-body width="850px">
      <el-table
        :data="tableData"
        :span-method="objectSpanMethod"
        border
        size="mini"
        height="500">
        <el-table-column
          prop="object"
          label="操作对象">
        </el-table-column>
        <el-table-column
          prop="perm"
          label="操作权限">
        </el-table-column>
        <el-table-column
          prop="bool"
          label="已有权限">
          <template slot-scope="scope">
            <i class="el-icon-check" v-if="scope.row.bool"></i>
            <i class="el-icon-close" v-else></i>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="cancelMyPerm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  addAgentUser,
  delAgentUser,
  getAgentUser,
  listAgentUser,
  listNonAgents,
  updateAgentUser,
  resetAgentUserPwd
} from "@/api/agent/agentUser";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { checkPermi, checkRole } from "@/utils/permission"; // 权限判断函数
import { getUserProfile } from "@/api/system/user";
import { grantableTemplate } from "@/api/agent/agentItem";

export default {
  name: "AgentUser",
  dicts: ["sys_yes_no", "sys_normal_disable"],
  components: {
    Treeselect,
  },
  data() {
    return {
      user: {},
      userBak: {},
      // 可选代理列表
      nonAgentList: [],
      // 遮罩层
      loading: false,
      // 显示搜索条件
      showSearch: true,
      // 代理用户表格数据
      agentUserList: [],
      // 代理用户树选项
      agentUserOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否展开，默认全部展开
      isExpandAll: true,
      // 重新渲染表格状态
      refreshTable: true,
      // 备注时间范围
      daterangeExpireTime: [],
      // 查询参数
      queryParams: {
        userName: null,
        enableAddSubagent: null,
        expireTime: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "用户账号不能为空", trigger: "blur" },
        ],
        enableAddSubagent: [
          {
            required: true,
            message: "是否允许发展下级不能为空",
            trigger: "blur",
          },
        ],
        status: [
          { required: true, message: "代理状态不能为空", trigger: "blur" },
        ],
      },
      pickerOptions: {
        shortcuts: [
          {
            text: "长期有效",
            onClick(picker) {
              picker.$emit("pick", new Date("9999-12-31 23:59:59"));
            },
          },
          {
            text: "1小时后",
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() + 3600 * 1000);
              picker.$emit("pick", date);
            },
          },
          {
            text: "1天后",
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() + 3600 * 1000 * 24);
              picker.$emit("pick", date);
            },
          },
          {
            text: "1周后(7天)",
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() + 3600 * 1000 * 24 * 7);
              picker.$emit("pick", date);
            },
          },
          {
            text: "1月后(30天)",
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() + 3600 * 1000 * 24 * 30);
              picker.$emit("pick", date);
            },
          },
          {
            text: "1年后(365天)",
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() + 3600 * 1000 * 24 * 365);
              picker.$emit("pick", date);
            },
          },
        ],
      },
      templateList: [],
      templateSelectionList: [],
      batchSelectTemplate: [],
      batchAgentPrice: null,
      batchExpireTime: null,
      batchRemark: null,
      perms: [
        {'object': '代理', 'name': '[默认]添加下级代理', 'code': 'enableAddSubagent', 'default': true },
        {'object': '代理', 'name': '[默认]修改下级代理密码', 'code': 'enableUpdateSubagentPassword', 'default': true },
        {'object': '代理', 'name': '[默认]修改下级代理状态', 'code': 'enableUpdateSubagentStatus', 'default': true },
        {'object': '代理', 'name': '删除下级代理', 'code': 'enableDeleteSubagent', 'default': false },
        {'object': '用户', 'name': '解绑用户设备', 'code': 'enableUnbindAppUser', 'default': false },
        {'object': '用户', 'name': '[默认]冻结用户', 'code': 'enableUpdateAppUserStatus1', 'default': true },
        {'object': '用户', 'name': '[默认]解冻用户', 'code': 'enableUpdateAppUserStatus0', 'default': true },
        {'object': '用户', 'name': '删除用户', 'code': 'enableDeleteAppUser', 'default': false },
        {'object': '用户', 'name': '修改用户时间', 'code': 'enableUpdateAppUserTime', 'default': false },
        {'object': '用户', 'name': '修改用户点数', 'code': 'enableUpdateAppUserPoint', 'default': false },
        {'object': '用户', 'name': '修改用户登录用户数限制', 'code': 'enableUpdateAppUserLoginLimitU', 'default': false },
        {'object': '用户', 'name': '修改用户登录设备数限制', 'code': 'enableUpdateAppUserLoginLimitM', 'default': false },
        {'object': '用户', 'name': '修改用户自定义数据', 'code': 'enableUpdateAppUserCustomParams', 'default': false },
        {'object': '用户', 'name': '[默认]修改用户备注', 'code': 'enableUpdateAppUserRemark', 'default': true },
        {'object': '用户', 'name': '修改用户密码', 'code': 'enableUpdateUserPassword', 'default': false },
        // {'object': '用户', 'name': '查看用户联系方式', 'code': 'enableViewUserContact', 'default': false },
        // {'object': '用户', 'name': '修改用户联系方式', 'code': 'enableUpdateUserContact', 'default': false },
        // {'object': '用户', 'name': '修改用户账号备注', 'code': 'enableUpdateUserRemark', 'default': false },
        {'object': '卡密', 'name': '[默认]冻结卡密', 'code': 'enableUpdateCardStatus1', 'default': true },
        {'object': '卡密', 'name': '[默认]解冻卡密', 'code': 'enableUpdateCardStatus0', 'default': true },
        {'object': '卡密', 'name': '[默认]批量换卡', 'code': 'enableBatchCardReplace', 'default': true },
        {'object': '卡密', 'name': '[默认]生成卡密', 'code': 'enableAddCard', 'default': true },
        {'object': '卡密', 'name': '删除卡密', 'code': 'enableDeleteCard', 'default': false },
        {'object': '卡密', 'name': '修改卡密时间', 'code': 'enableUpdateCardTime', 'default': false },
        {'object': '卡密', 'name': '修改卡密点数', 'code': 'enableUpdateCardPoint', 'default': false },
        {'object': '卡密', 'name': '修改卡密登录用户数限制', 'code': 'enableUpdateCardLoginLimitU', 'default': false },
        {'object': '卡密', 'name': '修改卡密登录设备数限制', 'code': 'enableUpdateCardLoginLimitM', 'default': false },
        {'object': '卡密', 'name': '修改卡密自定义参数', 'code': 'enableUpdateCardCustomParams', 'default': false },
        {'object': '卡密', 'name': '[默认]修改卡密备注', 'code': 'enableUpdateCardRemark', 'default': true },
      ],
      permValue: [],
      openMyPerm: false,
      spanArr: [],
    };
  },
  computed: {
    templateNameFilters() {
      let arr = [];
      let records = [];
      if (this.templateList) {
        for (let item of this.templateList) {
          // let value = "[" +
          //   item.appName +
          //   "]" +
          //   item.templateName;
          let value = item.appName;
          if (records.indexOf(value) == -1) {
            records.push(value);
            arr.push({ "text": value, "value": value });
          }
        }
      }
      return arr;
    },
    templateNameFilters2() {
      let arr = [];
      let records = [];
      if (this.templateList) {
        for (let item of this.templateList) {
          let value = "[" +
            item.appName +
            "]" +
            item.templateName;
          if (records.indexOf(value) == -1) {
            records.push(value);
            arr.push({ "text": value, "value": value });
          }
        }
      }
      return arr;
    },
    permData() {
      const data = [];
      this.perms.forEach((perm, index) => {
        if(this.$auth.hasAgentPermi(perm['code'])) {
          data.push({
            label: perm['name'],
            key: perm['code']
          });
        }
      });
      return data;
    },
    tableData() {
      const data = [];
      this.perms.forEach((perm, index) => {
        data.push({
          object: perm['object'],
          perm: perm['name'],
          bool: this.$auth.hasAgentPermi(perm['code'])
        });
      });
      return data;
    }
  },
  created() {
    this.getList();
    this.getUser();
  },
  mounted() {
    this.getSpanArr(this.tableData)
  },
  methods: {
    checkPermi,
    checkRole,
    /** 查询代理用户列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (this.queryParams.userName) {
        this.queryParams.params["userName"] = this.queryParams.userName;
      }
      if (null != this.daterangeExpireTime && "" != this.daterangeExpireTime) {
        this.queryParams.params["beginExpireTime"] =
          this.daterangeExpireTime[0];
        this.queryParams.params["endExpireTime"] = this.daterangeExpireTime[1];
      }
      listAgentUser(this.queryParams).then((response) => {
        this.agentUserList = this.handleTree(
          response.data,
          "agentId",
          "parentAgentId"
        );
        this.loading = false;
      });
    },
    /** 转换代理用户数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.agentId,
        label: node.user
          ? node.user.nickName + "(" + node.user.userName + ")"
          : "",
        children: node.children,
      };
    },
    /** 查询代理用户下拉树结构 */
    getTreeselect() {
      listAgentUser().then((response) => {
        this.agentUserOptions = [];
        const data = {
          agentId: 0,
          user: { nickName: "根节点", userName: "无上级" },
          children: [],
        };
        data.children = this.handleTree(
          response.data,
          "agentId",
          "parentAgentId"
        );
        this.agentUserOptions.push(data);
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        agentId: null,
        parentAgentId: null,
        userId: null,
        enableAddSubagent: "Y",
        expireTime: null,
        status: "0",
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        cid: [],
      };
      this.resetForm("form");
      this.templateList = [];
      this.permValue = [];
      if (this.$refs.templateTable) {
        this.$refs.templateTable.clearSelection();
      }
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.daterangeExpireTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset();
      this.getTreeselect();
      this.getGrantableTemplate({ 'agentId': row.agentId });
      // this.getNonAgentsList();
      this.user = this.userBak;
      this.permValue = [].concat(this.perms.filter(i=>i.default).map(i=>i.code));
      if (row != null && row.agentId) {
        this.form.parentAgentId = row.agentId;
      } else {
        this.form.parentAgentId = 0;
      }
      this.$nextTick(function () {
        this.$refs.transfer.$el.querySelectorAll('div.el-transfer-panel').forEach(p=>p.style.width='260px');
      });
      this.open = true;
      this.title = "添加代理用户";
    },
    /** 展开/折叠操作 */
    toggleExpandAll() {
      this.refreshTable = false;
      this.isExpandAll = !this.isExpandAll;
      this.$nextTick(() => {
        this.refreshTable = true;
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      // console.log(row);
      this.reset();
      this.getTreeselect();
      this.getGrantableTemplate({ 'agentId': row.agentId });
      this.form.parentAgentId = row.agentId;
      getAgentUser(row.agentId).then((response) => {
        this.form = response.data;
        this.user = response.data.parentUser;
        for (let perm of this.perms) {
          if(response.data[perm['code']] === 'Y') {
            this.permValue.push(perm['code']);
          }
        }
        this.$nextTick(function () {
          this.$refs.transfer.$el.querySelectorAll('div.el-transfer-panel').forEach(p=>p.style.width='260px');
        });
        this.open = true;
        this.title = "修改代理用户";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.form.templateList = this.templateSelectionList;
          // console.log(this.permValue);
          for (let perm of this.perms) {
            if(this.permValue.indexOf(perm['code']) !== -1) {
              // console.log('Y ---- ' + code);
              this.form[perm['code']] = 'Y';
            } else {
              // console.log('N ---- ' + code);
              this.form[perm['code']] = 'N';
            }
          }
          if (this.form.agentId != null) {
            updateAgentUser(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAgentUser(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$modal
        .confirm("是否确认删除数据项？")
        .then(function () {
          return delAgentUser(row.agentId);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => { });
    },
    getNonAgentsList(query) {
      this.loading = true;
      listNonAgents(query).then((response) => {
        this.nonAgentList = response.rows;
        this.loading = false;
      });
    },
    getUser() {
      getUserProfile().then((response) => {
        this.user = response.data;
        this.userBak = this.user;
      });
    },
    remoteMethod(query) {
      if (query !== "") {
        this.getNonAgentsList({ username: query });
      } else {
        this.nonAgentList = [];
      }
    },
    getGrantableTemplate(query) {
      this.templateList = [];
      if (this.$refs.templateTable) {
        this.$refs.templateTable.clearSelection();
      }
      grantableTemplate(query).then((response) => {
        let selectedRowList = [];
        let dataList = response.data;
        for (let data of dataList) {
          let row = {
            id: this.templateList.length,
            agentItemId: data.agentItemId,
            templateId: data.templateId,
            templateType: data.templateType,
            templateName: data.templateName,
            appName: data.appName,
            price: data.price,
            checked: data.checked,
            agentPrice: data.agentPrice || data.myPrice,
            expireTime: data.expireTime,
            remark: data.remark,
            myPrice: data.myPrice,
          };
          this.templateList.push(row);
          if (data.checked) {
            selectedRowList.push(row);
          }
        }
        this.$nextTick(function () {
          for (let row of selectedRowList) {
            this.$refs.templateTable.toggleRowSelection(row, true);
          }
        });
      });
    },
    filterHandler(value, row, column) {
      // let val = "[" +
      //   row.appName +
      //   "]" +
      //   row.templateName;
      let val = row.appName
      return val === value;
    },
    handleSelectionChange(val) {
      this.templateSelectionList = val;
    },
    handleBatchSet() {
      let selectedRowList = [];
      for (let row of this.templateList) {
        let val = "[" + row.appName + "]" + row.templateName;
        if (this.batchSelectTemplate.indexOf(val) != -1) {
          row.checked = true;
          if (this.batchAgentPrice) {
            row.agentPrice = this.batchAgentPrice;
          }
          if (this.batchExpireTime) {
            row.expireTime = this.batchExpireTime;
          }
          if (this.batchRemark) {
            row.remark = this.batchRemark;
          }
          selectedRowList.push(row);
        }
      }
      this.$nextTick(function () {
        for (let row of selectedRowList) {
          this.$refs.templateTable.toggleRowSelection(row, true);
        }
      });
    },
    // 状态修改
    handleStatusChange(row) {
      let text = row.status === "0" ? "启用" : "停用";
      this.$modal
        .confirm("确认要" + text + '"' + row.user.userName + '"吗？')
        .then(function () {
          return updateAgentUser({'agentId': row.agentId, 'status': row.status});
        })
        .then(() => {
          this.$modal.msgSuccess(text + "成功");
        })
        .catch(function () {
          row.status = row.status === "0" ? "1" : "0";
        });
    },
    handleEnableAddSubagentChange(row) {
      let text = row.enableAddSubagent === "Y" ? "允许" : "不允许";
      this.$modal
        .confirm("确认要" + text + "\"" + row.user.userName + "\"发展子代理吗？")
        .then(function () {
          return updateAgentUser({'agentId': row.agentId, 'enableAddSubagent': row.enableAddSubagent});
        })
        .then(() => {
          this.$modal.msgSuccess(text + "成功");
        })
        .catch(function () {
          row.enableAddSubagent = row.enableAddSubagent === "Y" ? "N" : "Y";
        });
    },
    filterMethod(query, item) {
      return item.label.indexOf(query) > -1;
    },
    /** 重置密码按钮操作 */
    handleResetPwd(row) {
      this.$prompt('请输入"' + row.user.userName + '"的新密码', "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        closeOnClickModal: false,
        inputPattern: /^.{5,20}$/,
        inputErrorMessage: "用户密码长度必须介于 5 和 20 之间",
      })
      .then(({ value }) => {
        resetAgentUserPwd(row.userId, value).then((response) => {
          this.$modal.msgSuccess("修改成功，新密码是：" + value);
        });
      })
      .catch(() => {});
    },
    handleViewMyPerm() {
      this.openMyPerm = true;
    },
    cancelMyPerm() {
      this.openMyPerm = false;
    },
    getSpanArr(data) {
      // data就是我们从后台拿到的数据
      for (var i = 0; i < data.length; i++) {
        if (i === 0) {
          this.spanArr.push(1);
          this.pos = 0;
        } else {
          // 判断当前元素与上一个元素是否相同
          if (data[i]['object'] === data[i - 1]['object']) {
            this.spanArr[this.pos] += 1;
            this.spanArr.push(0);
          } else {
            this.spanArr.push(1);
            this.pos = i;
          }
        }
        // console.log(this.spanArr);
      }
    },
    objectSpanMethod({ row, column, rowIndex, columnIndex }) {
      if (columnIndex === 0) {
        const _row = this.spanArr[rowIndex];
        const _col = _row > 0 ? 1 : 0;
        // console.log(`rowspan:${_row} colspan:${_col}`);
        return {
          // [0,0] 表示这一行不显示， [2,1]表示行的合并数
          rowspan: _row,
          colspan: _col
        };
      }
    }
  },
};
</script>
<style>
/**
  选择框不换行
*/
.el-select__tags {
  flex-wrap: nowrap;
  overflow: hidden;
}
</style>
