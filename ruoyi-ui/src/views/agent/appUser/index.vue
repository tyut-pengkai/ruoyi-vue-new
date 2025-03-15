<template>
  <div class="app-container">
    <el-form
      v-show="showSearch"
      ref="queryForm"
      :inline="true"
      :model="queryParams"
    >
      <el-form-item label="所属软件" prop="appId">
        <el-select
          v-model="queryParams.appId"
          clearable
          filterable
          placeholder="请选择"
          prop="appId"
          @change="changeSearchApp"
        >
          <el-option
            v-for="item in appList"
            :key="item.appId"
            :disabled="item.disabled"
            :label="
              '[' +
              (item.authType == '0' ? '账号' : '单码') +
              (item.billType == '0' ? '计时' : '计点') +
              '] ' +
              item.appName
            "
            :value="item.appId"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="用户账号/单码" prop="userName">
        <el-input
          v-model="queryParams.userName"
          clearable
          placeholder="请输入用户账号或单码"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="软件ID" prop="appId">
        <el-input
          v-model="queryParams.appId"
          placeholder="请输入软件ID"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="用户状态" prop="status">
        <el-select
          v-model="queryParams.status"
          clearable
          placeholder="请选择用户状态"
          size="small"
        >
          <el-option
            v-for="dict in dict.type.sys_normal_disable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="是否为VIP" prop="status">
        <el-select
          v-model="queryParams.isVip"
          clearable
          placeholder="请选择是否为VIP"
          size="small"
        >
          <el-option
            v-for="dict in dict.type.sys_yes_no"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="最后登录时间" prop="lastLoginTime">
        <el-date-picker
          v-model="daterangeLastLoginTime"
          end-placeholder="结束日期"
          range-separator="-"
          size="small"
          start-placeholder="开始日期"
          style="width: 240px"
          type="daterange"
          value-format="yyyy-MM-dd"
        ></el-date-picker>
      </el-form-item>

      <el-form-item label="过期时间">
        <el-date-picker
          v-model="daterangeExpireTime"
          end-placeholder="结束日期"
          range-separator="-"
          size="small"
          start-placeholder="开始日期"
          style="width: 240px"
          type="daterange"
          value-format="yyyy-MM-dd"
        ></el-date-picker>
      </el-form-item>

      <el-form-item label="剩余点数" prop="point">
        <el-input
          v-model="queryParams.point"
          clearable
          placeholder="请输入剩余点数"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <!-- <el-form-item label="创建者" prop="createBy">
        <el-input
          v-model="queryParams.createBy"
          placeholder="请输入创建者"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="备注" prop="remark">
        <el-input
          v-model="queryParams.remark"
          placeholder="请输入备注"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item prop="">
        <el-button
          icon="el-icon-search"
          size="mini"
          type="primary"
          @click="handleQuery"
        >搜索
        </el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
        >重置
        </el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!-- <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['agent:appUser:add']"
          >新增</el-button
        >
      </el-col> -->
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['agent:appUser:edit']"
          :disabled="single"
          icon="el-icon-edit"
          plain
          size="mini"
          type="success"
          @click="handleUpdate"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['agent:appUser:remove']"
          :disabled="multiple || !$auth.hasAgentPermi('enableDeleteAppUser')"
          icon="el-icon-delete"
          plain
          size="mini"
          type="danger"
          @click="handleDelete"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['agent:appUser:export']"
          :loading="exportLoading"
          icon="el-icon-download"
          plain
          size="mini"
          type="warning"
          @click="handleExport"
        >导出
        </el-button>
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="appUserList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column align="center" type="selection" width="55"/>
      <!-- <el-table-column label="" type="index" align="center" /> -->
      <el-table-column align="center" label="编号" prop="appUserId"/>
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="所属软件"
      >
        <template slot-scope="scope">
          {{ scope.row.app.appName }}
        </template>
      </el-table-column>
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="账号/单码"
      >
        <template slot-scope="scope">
          <div v-if="scope.row.app.authType == '0'">
            {{ scope.row.user.nickName }}({{ scope.row.user.userName }})
          </div>
          <div v-else>
            {{ "[单码]" + scope.row.loginCode }}
          </div>
        </template>
      </el-table-column>
      <el-table-column align="center" label="登录用户数限制">
        <template slot-scope="scope">
          <span>
            {{ scope.row.currentOnlineU }}/{{
              scope.row.effectiveLoginLimitU == -1
                ? "无限制"
                : scope.row.effectiveLoginLimitU
            }}
          </span>
          <span>
            <el-tooltip placement="top">
              <div slot="content">
                优先级由高到低<br/>
                充值卡/单码：
                {{
                  scope.row.cardLoginLimitU == -2
                    ? "不生效"
                    : scope.row.cardLoginLimitU == -1
                      ? "无限制"
                      : scope.row.cardLoginLimitU
                }}<br/>
                软件用户：
                {{
                  scope.row.loginLimitU == -2
                    ? "不生效"
                    : scope.row.loginLimitU == -1
                      ? "无限制"
                      : scope.row.loginLimitU
                }}
                <br/>
                软件：{{
                  scope.row.app.loginLimitU == -1
                    ? "无限制"
                    : scope.row.app.loginLimitU
                }}
              </div>
              <i
                class="el-icon-info"
                style="margin-left: 0px; margin-right: 10px"
              ></i>
            </el-tooltip>
          </span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="登录设备数限制">
        <template slot-scope="scope">
          <span>
            {{ scope.row.currentOnlineM }}/{{
              scope.row.effectiveLoginLimitM == -1
                ? "无限制"
                : scope.row.effectiveLoginLimitM
            }}
          </span>
          <span>
            <el-tooltip placement="top">
              <div slot="content">
                优先级由高到低<br/>
                充值卡/单码：
                {{
                  scope.row.cardLoginLimitM == -2
                    ? "不生效"
                    : scope.row.cardLoginLimitM == -1
                      ? "无限制"
                      : scope.row.cardLoginLimitM
                }}<br/>
                软件用户：
                {{
                  scope.row.loginLimitM == -2
                    ? "不生效"
                    : scope.row.loginLimitM == -1
                      ? "无限制"
                      : scope.row.loginLimitM
                }}
                <br/>
                软件：{{
                  scope.row.app.loginLimitM == -1
                    ? "无限制"
                    : scope.row.app.loginLimitM
                }}
              </div>
              <i
                class="el-icon-info"
                style="margin-left: 0px; margin-right: 10px"
              ></i>
            </el-tooltip>
          </span>
        </template>
      </el-table-column>
      <!-- <el-table-column label="登录用户数限制" align="center"
        ><template slot-scope="scope">
          <span>
            {{
              scope.row.loginLimitU == -2
                ? "不生效"
                : scope.row.loginLimitU == -1
                ? "无限制"
                : scope.row.loginLimitU
            }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="登录设备数限制" align="center">
        <template slot-scope="scope">
          <span>
            {{
              scope.row.loginLimitM == -2
                ? "不生效"
                : scope.row.loginLimitM == -1
                ? "无限制"
                : scope.row.loginLimitM
            }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="登录用户数限制(卡)" align="center"
        ><template slot-scope="scope">
          <span>
            {{
              scope.row.cardLoginLimitU == -2
                ? "不生效"
                : scope.row.cardLoginLimitU == -1
                ? "无限制"
                : scope.row.cardLoginLimitU
            }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="登录设备数限制(卡)" align="center">
        <template slot-scope="scope">
          <span>
            {{
              scope.row.cardLoginLimitM == -2
                ? "不生效"
                : scope.row.cardLoginLimitM == -1
                ? "无限制"
                : scope.row.cardLoginLimitM
            }}
          </span>
        </template>
      </el-table-column> -->
      <!-- <el-table-column label="赠送余额" align="center" prop="freeBalance" />
        <el-table-column label="支付余额" align="center" prop="payBalance" />
        <el-table-column label="赠送消费" align="center" prop="freePayment" />
        <el-table-column label="支付消费" align="center" prop="payPayment" /> -->
      <el-table-column align="center" label="最后登录时间" width="180">
        <template slot-scope="scope">
          <span>
            {{
              scope.row.lastLoginTime == null
                ? "从未登录过"
                : parseTime(scope.row.lastLoginTime)
            }}
          </span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="登录次数">
        <template slot-scope="scope">
          <span>{{
              scope.row.loginTimes ? scope.row.loginTimes + "次" : "从未登录过"
            }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="过期时间/剩余点数" width="180">
        <template slot-scope="scope">
          <div v-if="scope.row.app.billType == '0'">
            {{ parseTime(scope.row.expireTime) }}
          </div>
          <div v-else>
            {{ scope.row.point }}
          </div>
        </template>
      </el-table-column>
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="备注"
        prop="remark"
      />
      <el-table-column align="center" label="用户状态">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            active-value="0"
            inactive-value="1"
            @change="handleStatusChange(scope.row)"
            :disabled="!$auth.hasAgentPermiOr(['enableUpdateAppUserStatus0', 'enableUpdateAppUserStatus1'])"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        class-name="small-padding fixed-width"
        label="操作"
        width="260"
      >
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['agent:appUser:edit']"
            icon="el-icon-edit"
            size="mini"
            type="text"
            @click="handleUpdate(scope.row)"
          >修改
          </el-button>
          <el-button
              v-hasPermi="['agent:appUser:resetPwd']"
              icon="el-icon-key"
              size="mini" type="text"
              @click="handleResetPwd(scope.row)"
              v-if="scope.row.user"
              :disabled="!$auth.hasAgentPermi('enableUpdateUserPassword')">重置密码
          </el-button>
          <el-button
            v-hasPermi="['agent:appUser:remove']"
            icon="el-icon-delete"
            size="mini"
            type="text"
            @click="handleDelete(scope.row)"
            :disabled="!$auth.hasAgentPermi('enableDeleteAppUser')">删除
          </el-button>
          <el-dropdown
            size="mini"
            @command="(command) => handleCommand(command, scope.row)"
          >
            <span class="el-dropdown-link">
              <i class="el-icon-d-arrow-right el-icon--right"></i>更多
            </span>
            <el-dropdown-menu slot="dropdown">
              <!-- <el-dropdown-item
                command="handleDeviceCode"
                icon="el-icon-monitor"
                v-hasPermi="['agent:deviceCode:list']"
              >
                设备码管理old
              </el-dropdown-item> -->
              <el-dropdown-item
                v-hasPermi="['agent:appUserDeviceCode:list']"
                command="handleAppUserDeviceCode"
                icon="el-icon-monitor"
              >
                设备码管理
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :limit.sync="queryParams.pageSize"
      :page.sync="queryParams.pageNum"
      :total="total"
      @pagination="getList"
    />

    <!-- 添加或修改软件用户对话框 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="open"
      append-to-body
      width="800px"
    >
      <el-form ref="form" :model="form" :rules="rules">
        <!-- 新增 -->
        <div v-if="form.appUserId == null">
          <el-form-item prop="">
            <el-col :span="12">
              <el-form-item label="所属软件" prop="appId">
                <el-select
                  v-model="form.appId"
                  filterable
                  placeholder="请选择"
                  prop="appId"
                  @change="changeSelectedApp"
                >
                  <el-option
                    v-for="item in appList"
                    :key="item.appId"
                    :disabled="item.disabled"
                    :label="
                      '[' +
                      (item.authType == '0' ? '账号' : '单码') +
                      (item.billType == '0' ? '计时' : '计点') +
                      '] ' +
                      item.appName
                    "
                    :value="item.appId"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="计费类型" prop="billType">
                <div v-if="app">
                  <dict-tag
                    :options="dict.type.sys_bill_type"
                    :value="app.billType"
                  />
                </div>
                <div v-else>请先选择软件</div>
              </el-form-item>
            </el-col>
          </el-form-item>
          <el-form-item label="所属账号/单码" prop="">
            <div v-if="app">
              <div v-if="app.authType === '0'">
                <el-select
                  v-model="form.userId"
                  filterable
                  placeholder="请选择"
                  prop="userId"
                >
                  <el-option
                    v-for="item in userList"
                    :key="item.userId"
                    :disabled="item.disabled"
                    :label="item.nickName + '(' + item.userName + ')'"
                    :value="item.userId"
                  >
                  </el-option>
                </el-select>
              </div>
              <div v-if="app.authType === '1'">
                <el-input
                  v-model="form.loginCode"
                  clearable
                  placeholder="请输入单码"
                >
                </el-input>
              </div>
            </div>
            <div v-else>请先选择软件</div>
          </el-form-item>
          <!-- end -->
        </div>
        <!-- 修改 -->
        <div v-if="form.appUserId && form.app">
          <el-form-item prop="">
            <el-col :span="8">
              <el-form-item label="所属软件" prop="appName">
                {{ form.app.appName }}
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <div v-if="form.app.authType === '0'">
                <el-form-item label="所属账号" prop="userId">
                  {{ form.user.nickName }}({{ form.user.userName }})
                </el-form-item>
              </div>
              <div v-if="form.app.authType === '1'">
                <el-form-item label="单码" prop="loginCode">
                  {{ form.loginCode }}
                </el-form-item>
              </div>
            </el-col>
            <el-col :span="8">
              <el-form-item label="计费类型" prop="billType">
                <dict-tag
                  :options="dict.type.sys_bill_type"
                  :value="form.app.billType"
                />
              </el-form-item>
            </el-col>
          </el-form-item>
          <el-form-item prop="">
            <el-col :span="8">
              <el-form-item label="总登录次数" prop="loginTimes">
                {{ form.loginTimes ? form.loginTimes + "次" : "从未登录过" }}
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="最近登录时间" prop="lastLoginTime">
                <div v-if="form.lastLoginTime">
                  {{ form.lastLoginTime }}
                </div>
                <div v-else>从未登录过</div>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="最近登录IP" prop="loginIp">
                <div v-if="form.loginIp">
                  {{ form.loginIp }}
                </div>
                <div v-else>从未登录过</div>
              </el-form-item>
            </el-col>
          </el-form-item>
          <!-- end -->
        </div>
        <el-form-item prop="">
          <el-col :span="12">
            <el-form-item label="登录用户数限制" prop="loginLimitU">
              <span>
                <el-tooltip
                  content="登录用户数量限制，整数，-1为不限制，-2为不生效，默认为-2"
                  placement="top"
                >
                  <i
                    class="el-icon-question"
                    style="margin-left: -12px; margin-right: 10px"
                  ></i>
                </el-tooltip>
              </span>
               <el-input-number
                v-model="form.loginLimitU"
                controls-position="right"
                :min="-2"
                :disabled="!$auth.hasAgentPermi('enableUpdateAppUserLoginLimitU')"
              />
<!--              <span>{{ form.loginLimitU }}</span>-->
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="登录设备数限制" prop="loginLimitM">
              <span>
                <el-tooltip
                  content="登录设备数量限制，整数，-1为不限制，-2为不生效，默认为-2"
                  placement="top"
                >
                  <i
                    class="el-icon-question"
                    style="margin-left: -12px; margin-right: 10px"
                  ></i>
                </el-tooltip>
              </span>
               <el-input-number
                v-model="form.loginLimitM"
                controls-position="right"
                :min="-2"
                :disabled="!$auth.hasAgentPermi('enableUpdateAppUserLoginLimitM')"
              />
<!--              <span>{{ form.loginLimitM }}</span>-->
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item prop="">
          <el-col :span="12">
            <el-form-item label="登录用户数限制(卡)" prop="cardLoginLimitU">
              <span>
                <el-tooltip
                  content="由充值卡/单码生效的登录用户数量限制，整数，-1为不限制，-2为不生效，默认为-2"
                  placement="top"
                >
                  <i
                    class="el-icon-question"
                    style="margin-left: -12px; margin-right: 10px"
                  ></i>
                </el-tooltip>
              </span>
               <el-input-number
                v-model="form.cardLoginLimitU"
                :min="-2"
                controls-position="right"
                :disabled="!$auth.hasAgentPermi('enableUpdateCardLoginLimitU')"
              />
<!--              <span>{{ form.cardLoginLimitU }}</span>-->
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="登录设备数限制(卡)" prop="cardLoginLimitM">
              <span>
                <el-tooltip
                  content="由充值卡/单码生效的登录设备数量限制，整数，-1为不限制，-2为不生效，默认为-2"
                  placement="top"
                >
                  <i
                    class="el-icon-question"
                    style="margin-left: -12px; margin-right: 10px"
                  ></i>
                </el-tooltip>
              </span>
               <el-input-number
                v-model="form.cardLoginLimitM"
                :min="-2"
                controls-position="right"
                :disabled="!$auth.hasAgentPermi('enableUpdateCardLoginLimitM')"
              />
<!--              <span>{{ form.cardLoginLimitM }}</span>-->
            </el-form-item>
          </el-col>
        </el-form-item>
        <!-- <el-form-item prop="">
          <el-col :span="12">
            <el-form-item label="赠送余额" prop="freeBalance">
              <el-input-number
                v-model="form.freeBalance"
                controls-position="right"
                :precision="2"
                :step="0.01"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="支付余额" prop="payBalance">
              <el-input-number
                v-model="form.payBalance"
                controls-position="right"
                :precision="2"
                :step="0.01"
              />
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item prop="">
          <el-col :span="12">
            <el-form-item label="赠送消费" prop="freePayment">
              <el-input-number
                v-model="form.freePayment"
                :precision="2"
                :step="0.01"
                controls-position="right"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="支付消费" prop="payPayment">
              <el-input-number
                v-model="form.payPayment"
                controls-position="right"
                :precision="2"
                :step="0.01"
              />
            </el-form-item>
          </el-col>
        </el-form-item> -->
        <el-form-item prop="">
          <el-col :span="12">
            <el-form-item label="用户状态" prop="status">
              <el-radio-group v-model="form.status" :disabled="!$auth.hasAgentPermiOr(['enableUpdateAppUserStatus0', 'enableUpdateAppUserStatus1'])">
                <el-radio
                  v-for="dict in dict.type.sys_normal_disable"
                  :key="dict.value"
                  :label="dict.value"
                >{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <div v-if="form.appUserId == null && app">
            <el-col :span="12">
              <div v-if="app.billType === '0'">
                <el-form-item label="过期时间" prop="expireTime">
                   <el-date-picker
                    clearable
                    size="small"
                    v-model="form.expireTime"
                    type="datetime"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    placeholder="选择过期时间"
                    :disabled="!$auth.hasAgentPermi('enableUpdateAppUserTime')"
                  >
                  </el-date-picker>
<!--                  <span>{{ form.expireTime }}</span>-->
                </el-form-item>
              </div>
              <div v-if="app.billType === '1'">
                <el-form-item label="剩余点数" prop="point">
                   <el-input-number
                    v-model="form.point"
                    controls-position="right"
                    :disabled="!$auth.hasAgentPermi('enableUpdateAppUserPoint')"
                  />
<!--                  <span>{{ form.point }}</span>-->
                </el-form-item>
              </div>
            </el-col>
          </div>
          <div v-if="form.appUserId && form.app">
            <el-col :span="12">
              <div v-if="form.app.billType === '0'">
                <el-form-item label="过期时间" prop="expireTime">
                   <el-date-picker
                    clearable
                    size="small"
                    v-model="form.expireTime"
                    type="datetime"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    placeholder="选择过期时间"
                    :disabled="!$auth.hasAgentPermi('enableUpdateAppUserTime')"
                  >
                  </el-date-picker>
<!--                  <span>{{ form.expireTime }}</span>-->
                </el-form-item>
              </div>
              <div v-if="form.app.billType === '1'">
                <el-form-item label="剩余点数" prop="point">
                   <el-input-number
                    v-model="form.point"
                    controls-position="right"
                    :disabled="!$auth.hasAgentPermi('enableUpdateAppUserPoint')"
                  />
<!--                  <span>{{ form.point }}</span>-->
                </el-form-item>
              </div>
            </el-col>
          </div>
        </el-form-item>
        <el-form-item prop="">
          <el-col :span="12">
            <el-form-item label="剩余解绑次数" prop="unbindTimes">
              <!-- <el-input-number
                v-model="form.unbindTimes"
                :min="0"
                controls-position="right"
              /> -->
              <span>{{ form.unbindTimes }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12"></el-col>
        </el-form-item>
        <el-form-item label="充值卡/单码自定义参数" prop="cardCustomParams">
          <el-input
            v-model="form.cardCustomParams"
            placeholder="请输入内容"
            type="textarea"
            :disabled="!$auth.hasAgentPermi('enableUpdateAppUserCustomParams')"
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form.remark"
            placeholder="请输入内容"
            type="textarea"
            :disabled="!$auth.hasAgentPermi('enableUpdateAppUserRemark')"
          />
        </el-form-item>
        <div v-if="form.appUserId">
          <el-form-item prop="">
            <el-col :span="12">
              <el-form-item label="所属代理" prop="agentId">
                <span v-if="form.agentUser">
                  {{ form.agentUser.nickName }} ({{ form.agentUser.userName }})
                </span>
              </el-form-item>
            </el-col>
          </el-form-item>
          <el-form-item prop="">
            <el-col :span="12">
              <el-form-item label="创建人" prop="createBy">
                {{ form.createBy }}
              </el-form-item>
              <el-form-item label="创建时间" prop="createTime">
                {{ form.createTime }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="最后更新" prop="updateBy">
                {{ form.updateBy }}
              </el-form-item>
              <el-form-item label="更新时间" prop="updateTime">
                {{ form.updateTime }}
              </el-form-item>
            </el-col>
          </el-form-item>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  addAppUser,
  changeAppUserStatus,
  delAppUser,
  exportAppUser,
  getAppUser,
  listAppUser,
  updateAppUser,
  resetAppUserPwd
} from "@/api/agent/appUser";
// import { getApp } from "@/api/system/app";
import {listAppAll} from "@/api/agent/agentApp";
import {listUserByExceptAppid} from "@/api/system/user";

export default {
  name: "AppUser",
  dicts: ["sys_normal_disable", "sys_bill_type", "sys_yes_no"],
  data() {
    return {
      appList: [],
      appMap: [],
      // 软件数据
      app: null,
      // 添加时的候选用户
      userList: [],
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 软件用户表格数据
      appUserList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 备注时间范围
      daterangeLastLoginTime: [],
      // 备注时间范围
      daterangeExpireTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: this.$store.state.settings.pageSize,
        userId: null,
        appId: null,
        status: null,
        lastLoginTime: null,
        expireTime: null,
        point: null,
        loginCode: null,
        createBy: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        appId: [{required: true, message: "软件不能为空", trigger: "blur"}],
        userId: [
          {required: true, message: "所属账号不能为空", trigger: "blur"},
        ],
        loginCode: [
          {required: true, message: "单码不能为空", trigger: "blur"},
        ],
        status: [{required: true, message: "状态不能为空", trigger: "blur"}],
        loginLimitU: [
          {
            required: true,
            message:
              "登录用户数量限制，整数，-1为不限制，-2为不生效，默认为-2不能为空",
            trigger: "blur",
          },
        ],
        loginLimitM: [
          {
            required: true,
            message:
              "登录设备数量限制，整数，-1为不限制，-2为不生效，默认为-2不能为空",
            trigger: "blur",
          },
        ],
        cardLoginLimitU: [
          {
            required: true,
            message:
              "由充值卡/单码生效的登录用户数量限制，整数，-1为不限制，-2为不生效，默认为-2不能为空",
            trigger: "blur",
          },
        ],
        cardLoginLimitM: [
          {
            required: true,
            message:
              "由充值卡/单码生效的登录设备数量限制，整数，-1为不限制，-2为不生效，默认为-2不能为空",
            trigger: "blur",
          },
        ],
        freeBalance: [
          {required: true, message: "赠送余额不能为空", trigger: "blur"},
        ],
        payBalance: [
          {required: true, message: "支付余额不能为空", trigger: "blur"},
        ],
        freePayment: [
          {required: true, message: "赠送消费不能为空", trigger: "blur"},
        ],
        payPayment: [
          {required: true, message: "支付消费不能为空", trigger: "blur"},
        ],
        expireTime: [
          {required: true, message: "过期时间不能为空", trigger: "blur"},
        ],
        point: [
          {required: true, message: "剩余点数不能为空", trigger: "blur"},
        ],
      },
    };
  },
  created() {
    // const appId = this.$route.params && this.$route.params.appId;
    // const appId = this.$route.query && this.$route.query.appId;
    this.getAppList();
    // const appName = this.$route.query && this.$route.query.appName;
    // const title = "软件用户管理";
    //     // const appName = this.app.appName;
    //     const route = Object.assign({}, this.$route, {
    //       title: `${title}`,
    //     });
    //     this.$store.dispatch("tagsView/updateVisitedView", route);
    // if (appId != undefined && appId != null) {
    //   getApp(appId).then((response) => {
    //     this.app = response.data;
    //     this.getList();
    //   });
    // } else {
    this.getList();
    // this.$modal.alertError("未获取到当前软件信息");
    // }
  },
  methods: {
    /** 查询软件用户列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (this.queryParams["isVip"]) {
        this.queryParams.params["isVip"] = this.queryParams["isVip"];
      }
      if (
        null != this.daterangeLastLoginTime &&
        "" != this.daterangeLastLoginTime
      ) {
        this.queryParams.params["beginLastLoginTime"] =
          this.daterangeLastLoginTime[0];
        this.queryParams.params["endLastLoginTime"] =
          this.daterangeLastLoginTime[1];
      }
      if (null != this.daterangeExpireTime && "" != this.daterangeExpireTime) {
        this.queryParams.params["beginExpireTime"] =
          this.daterangeExpireTime[0];
        this.queryParams.params["endExpireTime"] = this.daterangeExpireTime[1];
      }
      if (this.app) {
        this.queryParams.appId = this.app.appId;
      }
      listAppUser(this.queryParams).then((response) => {
        this.appUserList = response.rows;
        this.total = response.total;
        this.loading = false;
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
        appUserId: undefined,
        userId: undefined,
        appId: undefined,
        status: "0",
        loginLimitU: -2,
        loginLimitM: -2,
        cardLoginLimitU: -2,
        cardLoginLimitM: -2,
        freeBalance: 0,
        payBalance: 0,
        freePayment: 0,
        payPayment: 0,
        lastLoginTime: undefined,
        loginTimes: 0,
        expireTime: undefined,
        point: 0,
        loginCode: undefined,
        remark: undefined,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.daterangeLastLoginTime = [];
      this.daterangeExpireTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.appUserId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      if (this.app) {
        this.form.appId = this.app.appId;
      }
      if (this.app && this.app.authType === "0") {
        this.loading = true;
        listUserByExceptAppid(this.app.appId).then((response) => {
          this.userList = response.rows;
          this.loading = false;
        });
      }
      this.open = true;
      this.title = "添加软件用户";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const appUserId = row.appUserId || this.ids;
      getAppUser(appUserId).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改软件用户";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.appUserId != null) {
            updateAppUser(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            this.form.appId = this.app.appId;
            addAppUser(this.form).then((response) => {
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
      const appUserIds = row.appUserId || this.ids;
      this.$modal
        .confirm("是否确认删除数据项？")
        .then(function () {
          return delAppUser(appUserIds);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal
        .confirm("是否确认导出所有软件用户数据项？")
        .then(() => {
          this.exportLoading = true;
          return exportAppUser(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
          this.exportLoading = false;
        })
        .catch(() => {
        });
    },
    handleDeviceCode: function (row) {
      const appUserId = row.appUserId;
      this.$router.push({
        path: "/app/deviceCode/" + appUserId,
      });
    },
    handleAppUserDeviceCode: function (row) {
      const appUserId = row.appUserId;
      this.$router.push({
        path: "/agent/appUserDeviceCode",
        query: {
          appUserId: appUserId,
        },
      });
    },
    // 更多操作触发
    handleCommand(command, row) {
      switch (command) {
        case "handleDeviceCode":
          this.handleDeviceCode(row);
          break;
        case "handleAppUserDeviceCode":
          this.handleAppUserDeviceCode(row);
          break;
        default:
          break;
      }
    },
    // 状态修改
    handleStatusChange(row) {
      let text = row.status === "0" ? "启用" : "停用";
      this.$modal
        .confirm(
          "确认要" +
          text +
          '"' +
          (row.user
            ? row.user.nickName + "(" + row.user.userName + ")"
            : row.loginCode) +
          '"吗？'
        )
        .then(function () {
          return changeAppUserStatus(row.appUserId, row.status);
        })
        .then(() => {
          this.$modal.msgSuccess(text + "成功");
        })
        .catch(function () {
          row.status = row.status === "0" ? "1" : "0";
        });
    },
    getAppList() {
      this.loading = true;
      let queryParams = {};
      queryParams.params = {};
      listAppAll(queryParams).then((response) => {
        this.appList = response.rows;
        for (let app of this.appList) {
          this.appMap[app["appId"]] = app;
        }
        this.loading = false;
      });
    },
    changeSelectedApp(appId) {
      this.app = this.appMap[appId];
      if (this.app.authType === "0") {
        listUserByExceptAppid(this.app.appId).then((response) => {
          this.userList = response.rows;
        });
      }
    },
    changeSearchApp(appId) {
      this.loading = true;
      this.app = this.appMap[appId];
      this.getList();
      this.loading = false;
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
        resetAppUserPwd(row.userId, row.appId, value).then((response) => {
          this.$modal.msgSuccess("修改成功，新密码是：" + value);
        });
      })
      .catch(() => {});
    },
  },
};
</script>
