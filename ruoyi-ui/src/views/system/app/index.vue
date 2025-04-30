<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="软件名称" prop="appName">
        <el-input
          v-model="queryParams.appName"
          placeholder="请输入软件名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="软件描述" prop="description">
        <el-input
          v-model="queryParams.description"
          placeholder="请输入软件描述"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="软件状态" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="请选择软件状态"
          clearable
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
      <el-form-item label="绑定模式" prop="bindType">
        <el-select
          v-model="queryParams.bindType"
          placeholder="请选择绑定模式"
          clearable
          size="small"
        >
          <el-option
            v-for="dict in dict.type.sys_bind_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="开启计费" prop="isCharge">
        <el-select
          v-model="queryParams.isCharge"
          placeholder="请选择是否开启计费"
          clearable
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
      <el-form-item label="认证类型" prop="authType">
        <el-select
          v-model="queryParams.authType"
          placeholder="请选择认证类型"
          clearable
          size="small"
        >
          <el-option
            v-for="dict in dict.type.sys_auth_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="计费类型" prop="billType">
        <el-select
          v-model="queryParams.billType"
          placeholder="请选择计费类型"
          clearable
          size="small"
        >
          <el-option
            v-for="dict in dict.type.sys_bill_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="软件作者" prop="createBy">
        <el-input
          v-model="queryParams.createBy"
          placeholder="请输入软件作者"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="创建时间">
        <el-date-picker
          v-model="daterangeCreateTime"
          size="small"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
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
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:app:add']"
          >新增</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:app:edit']"
          >修改</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:app:remove']"
          >删除</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['system:app:export']"
          >导出</el-button
        >
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="appList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column align="center" type="selection" width="55" />
      <!-- <el-table-column label="" type="index" align="center" /> -->
      <el-table-column align="center" label="编号" prop="appId" />
      <!-- <el-table-column label="软件图标" align="center" prop="icon" /> -->
      <el-table-column
        label="软件名称"
        align="center"
        prop="appName"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          {{ scope.row.appName }}
          <span v-if="scope.row.description">
            <el-tooltip :content="scope.row.description" placement="top">
              <i
                class="el-icon-info"
                style="margin-left: 0px; margin-right: 10px"
              ></i>
            </el-tooltip>
          </span>
        </template>
      </el-table-column>
      <!-- <el-table-column
        label="软件描述"
        align="center"
        prop="description"
        :show-overflow-tooltip="true"
      /> -->
      <el-table-column label="认证类型" align="center" prop="authType">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_auth_type"
            :value="scope.row.authType"
          />
        </template>
      </el-table-column>
      <el-table-column label="计费类型" align="center" prop="billType">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_bill_type"
            :value="scope.row.billType"
          />
        </template>
      </el-table-column>
      <el-table-column
        label="绑定模式"
        align="center"
        prop="bindType"
        width="200px"
      >
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_bind_type"
            :value="scope.row.bindType"
          />
        </template>
      </el-table-column>
      <!-- <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="软件作者"
      >
        <template slot-scope="scope">
          {{
            scope.row.developer
              ? scope.row.developer.nickName +
                "(" +
                scope.row.developer.userName +
                ")"
              : "[用户不存在]"
          }}
        </template>
      </el-table-column> -->
      <!-- <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column> -->
      <el-table-column
        label="备注"
        align="center"
        prop="remark"
        :show-overflow-tooltip="true"
      />
      <!-- <el-table-column label="软件状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_normal_disable"
            :value="scope.row.status"
          />
        </template>
      </el-table-column> -->
      <el-table-column label="软件状态" align="center" key="status">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            active-value="0"
            inactive-value="1"
            @change="handleStatusChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <!-- <el-table-column label="开启计费" align="center" prop="isCharge">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_yes_no"
            :value="scope.row.isCharge"
          />
        </template>
      </el-table-column> -->
      <el-table-column label="开启计费" align="center" key="isCharge">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isCharge"
            active-value="Y"
            inactive-value="N"
            @change="handleChargeStatusChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        width="200"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <!-- <el-button
            size="mini"
            type="text"
            icon="el-icon-user"
            @click="handleAppUser(scope.row)"
            v-hasPermi="['system:appUser:list']"
            >用户</el-button
          > -->
          <!-- <el-button
            size="mini"
            type="text"
            icon="el-icon-bank-card"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:card:list']"
            >卡密</el-button
          > -->
          <!-- <el-button
            size="mini"
            type="text"
            icon="el-icon-edit-outline"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:cardTemplate:list']"
            >卡类</el-button
          > -->
          <el-button
            size="mini"
            type="text"
            icon="el-icon-setting"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:app:edit']"
            >配置</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-finished"
            @click="handleVersionManage(scope.row)"
            v-hasPermi="['system:appVersion:list']"
            >版本</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:app:remove']"
            ><span style="color: #f00">删除</span></el-button
          >
          <el-dropdown
            size="mini"
            @command="(command) => handleCommand(command, scope.row)"
            v-hasPermi="[
              'system:appUser:list',
              'system:appVersion:list',
              'system:card:list',
              'system:cardTemplate:list',
            ]"
          >
            <span class="el-dropdown-link">
              <i class="el-icon-d-arrow-right el-icon--right"></i>更多
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item
                command="handleAppUser"
                icon="el-icon-user"
                v-hasPermi="['system:appUser:list']"
                >用户管理</el-dropdown-item
              >
              <!-- <el-dropdown-item
                command="handleVersionManage"
                icon="el-icon-finished"
                v-hasPermi="['system:appVersion:list']"
                >版本管理</el-dropdown-item
              > -->
              <div v-if="scope.row.authType === '0'">
                <el-dropdown-item
                  command="handleCardTemplate"
                  icon="el-icon-edit-outline"
                  v-hasPermi="['system:cardTemplate:list']"
                  >卡类管理
                </el-dropdown-item>
                <el-dropdown-item
                  command="handleCardManage"
                  icon="el-icon-bank-card"
                  v-hasPermi="['system:card:list']"
                  >卡密管理</el-dropdown-item
                >
              </div>
              <div v-if="scope.row.authType === '1'">
                <el-dropdown-item
                  command="handleLoginCodeTemplate"
                  icon="el-icon-edit-outline"
                  v-hasPermi="['system:loginCodeTemplate:list']"
                  >单码类别
                </el-dropdown-item>
                <el-dropdown-item
                  command="handleLoginCodeManage"
                  icon="el-icon-bank-card"
                  v-hasPermi="['system:loginCode:list']"
                  >单码管理</el-dropdown-item
                >
              </div>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改软件对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="800px"
      append-to-body
      :close-on-click-modal="false"
    >
      <el-form ref="form" :model="form" :rules="rules" label-position="right">
        <el-tabs type="border-card" ref="tabs" v-model="tabIdx">
          <!-- 基本配置 -->
          <el-tab-pane label="基本信息">
            <el-form-item label="软件名称" prop="appName">
              <el-input v-model="form.appName" placeholder="请输入软件名称" maxlength="30" show-word-limit/>
            </el-form-item>
            <el-form-item>
              <el-alert v-show="!form.appId" title="注：如需快速接入APK请选择：[认证类型]单码登录、[计费类型]计时模式" type="info" :closable="false" style="padding: 0 5px;margin-bottom:5px"></el-alert>
              <el-col :span="12">
                <el-form-item label="认证类型" prop="authType">
                  <el-select
                    v-model="form.authType"
                    placeholder="请选择认证类型"
                    :disabled="form.appId != null"
                  >
                    <el-option
                      v-for="dict in dict.type.sys_auth_type"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="计费类型" prop="billType">
                  <el-select
                    v-model="form.billType"
                    placeholder="请选择计费类型"
                    :disabled="form.appId != null"
                  >
                    <el-option
                      v-for="dict in dict.type.sys_bill_type"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-form-item>
            <el-form-item>
              <el-col :span="12">
                <el-form-item label="登录扣点策略" prop="loginReducePointStrategy" v-if="form.billType === '1'">
                  <el-select
                    v-model="form.loginReducePointStrategy"
                    placeholder="请选择登录扣点策略"
                    :disabled="form.billType !== '1'"
                  >
                    <el-option
                      v-for="dict in dict.type.sys_login_reduce_point_strategy"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-form-item>
            <el-form-item label="软件描述" prop="description">
              <el-input
                v-model="form.description"
                type="textarea"
                placeholder="请输入软件描述"
                maxlength="500"
                show-word-limit
              />
            </el-form-item>
            <el-divider></el-divider>
            <updown>
              <el-form-item label="前台商城购卡地址" label-width="140px" prop="shopUrl">
                <el-col :span="20">
                  <el-input v-model="form.shopUrl" placeholder="请输入购卡地址" maxlength="50" show-word-limit>
                    <template slot="prepend">{{ getShopUrlPrefix() }}</template>
                  </el-input>
                </el-col>
                <el-col :span="4">
                  <el-button
                    size="small"
                    icon="el-icon-refresh"
                    circle style="margin-left: 5px"
                    @click="getRandomString('shopUrl', 10)"
                  >
                  </el-button>
                  <el-button
                    id="copyButton"
                    size="small"
                    icon="el-icon-document-copy"
                    circle style="margin-left: 5px"
                    @click="doCopy('shopUrl')"
                  >
                  </el-button>
                </el-col>
              </el-form-item>
              <el-form-item>
                <el-col :span="12">
                  <el-form-item label="软件状态" prop="status">
                    <!-- <el-select
                      v-model="form.status"
                      placeholder="请选择软件状态"
                    >
                      <el-option
                        v-for="dict in dict.type.sys_normal_disable"
                        :key="dict.value"
                        :label="dict.label"
                        :value="dict.value"
                      ></el-option>
                    </el-select> -->
                    <el-radio-group v-model="form.status">
                      <el-radio
                        v-for="dict in dict.type.sys_normal_disable"
                        :key="dict.value"
                        :label="dict.value"
                        >{{ dict.label }}</el-radio
                      >
                    </el-radio-group>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="开启计费" prop="isCharge">
                    <el-select
                      v-model="form.isCharge"
                      placeholder="请选择是否开启计费"
                    >
                      <el-option
                        v-for="dict in dict.type.sys_yes_no"
                        :key="dict.value"
                        :label="dict.label"
                        :value="dict.value"
                      ></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-form-item>

              <el-form-item>
                <el-col :span="12">
                  <el-form-item label="首次登录赠送" prop="freeQuotaReg">
                    <span>
                      <el-tooltip
                        content="首次登录赠送免费时间或点数，单位秒或点"
                        placement="top"
                      >
                        <i
                          class="el-icon-question"
                          style="margin-left: -12px; margin-right: 10px"
                        ></i>
                      </el-tooltip>
                    </span>
                    <el-input-number
                      v-model="form.freeQuotaReg"
                      controls-position="right"
                      :min="0"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="开启前台充值" prop="enableFeCharge">
                    <el-select
                      v-model="form.enableFeCharge"
                      placeholder="请选择是否开启前台充值"
                    >
                      <el-option
                        v-for="dict in dict.type.sys_yes_no"
                        :key="dict.value"
                        :label="dict.label"
                        :value="dict.value"
                      ></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-form-item>
              <el-form-item prop="">
                <el-col :span="12">
                  <el-form-item label="商城展示顺序" prop="sort">
                <span>
                  <el-tooltip
                    content="商城展示顺序，整数，数字越大越靠后，默认值为0"
                    placement="top"
                  >
                    <i
                      class="el-icon-question"
                      style="margin-left: -12px; margin-right: 10px"
                    ></i>
                  </el-tooltip>
                </span>
                    <el-input-number
                      v-model="form.sort"
                      controls-position="right"
                    />
                  </el-form-item>
                </el-col>
              </el-form-item>
              <el-form-item label="自定义软件主页" prop="idxUrl">
                <span>
                    <el-tooltip
                      content="用于配置软件(官方模板)的跳转官网主页地址，可通过软件信息API获取"
                      placement="top"
                    >
                      <i
                        class="el-icon-question"
                        style="margin-left: -12px; margin-right: 10px"
                      ></i>
                    </el-tooltip>
                  </span>
                <el-input v-model="form.idxUrl" placeholder="请输入软件主页" maxlength="100" show-word-limit/>
              </el-form-item>
              <el-form-item label="自定义购卡地址" prop="customBuyUrl">
                <span>
                    <el-tooltip
                      content="用于配置软件(官方模板)的跳转购卡页面，可通过软件信息API获取"
                      placement="top"
                    >
                      <i
                        class="el-icon-question"
                        style="margin-left: -12px; margin-right: 10px"
                      ></i>
                    </el-tooltip>
                  </span>
                <el-input
                  v-model="form.customBuyUrl"
                  placeholder="请输入购卡地址"
                  maxlength="100"
                  show-word-limit
                />
              </el-form-item>
              <!-- <el-form-item label="软件图标">
                <imageUpload v-model="form.icon" :limit="1" /> -->
              <!-- <app-icon /> -->
              <!-- </el-form-item> -->
              <el-form-item label="软件公告" prop="welcomeNotice">
                <el-input
                  v-model="form.welcomeNotice"
                  placeholder="请输入内容"
                  type="textarea"
                  maxlength="500"
                  show-word-limit
                />
              </el-form-item>
              <el-form-item label="停机公告" prop="offNotice">
                <el-input
                  v-model="form.offNotice"
                  placeholder="请输入内容"
                  type="textarea"
                  maxlength="500"
                  show-word-limit
                />
              </el-form-item>
              <el-form-item label="备注" prop="remark">
                <el-input
                  v-model="form.remark"
                  type="textarea"
                  placeholder="请输入内容"
                />
              </el-form-item>
              <div v-if="form.appId">
                <el-form-item>
                  <el-col :span="12">
                    <el-form-item label="创建人" prop="createBy">{{
                      form.createBy
                    }}</el-form-item>
                    <el-form-item label="创建时间" prop="createTime"
                      >{{ form.createTime }}
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="最后更新" prop="updateBy">{{
                      form.updateBy
                    }}</el-form-item>
                    <el-form-item label="更新时间" prop="updateTime"
                      >{{ form.updateTime }}
                    </el-form-item>
                  </el-col>
                </el-form-item>
              </div>
            </updown>
          </el-tab-pane>
          <!-- 通信安全 -->
          <el-tab-pane label="通信安全">
            <el-alert
              :closable="false"
              show-icon
              title="注意：请在软件发布前设置好本页的加密方式及密码，请自行备份密码，密码一旦遗失无法找回"
              type="info"
              style="margin-bottom: 5px"
            >
            </el-alert>
            <el-alert
              :closable="false"
              show-icon
              title="注意：加密方式及密码设定好后请谨慎修改，修改后使用旧密码的软件将无法与系统正常通信"
              type="info"
              style="margin-bottom: 10px"
            >
            </el-alert>
            <el-form-item>
              <el-col :span="12">
                <el-form-item label="数据输入加密" prop="dataInEnc">
                  <el-select
                    v-model="form.dataInEnc"
                    placeholder="请选择加密方式"
                  >
                    <el-option
                      v-for="dict in dict.type.sys_encryp_type"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="密码" prop="dataInPwd" label-width="60px">
                  <el-col :span="20">
                    <el-input
                      v-model="form.dataInPwd"
                      placeholder="请输入加密密码"
                      :disabled="form.dataInEnc === '0' || form.dataInEnc === '1'"
                       maxlength="20"
                       show-word-limit
                    />
                  </el-col>
                  <el-col :span="4">
                    <el-button
                      size="small"
                      icon="el-icon-refresh"
                      circle style="margin-left: 5px"
                      :disabled="form.dataInEnc === '0' || form.dataInEnc === '1'"
                      @click="getRandomString('dataInPwd', 20)"
                    >
                    </el-button>
                  </el-col>
                </el-form-item>
              </el-col>
            </el-form-item>
            <el-form-item>
              <el-col :span="12">
                <el-form-item label="数据输出加密" prop="dataOutEnc">
                  <el-select
                    v-model="form.dataOutEnc"
                    placeholder="请选择加密方式"
                  >
                    <el-option
                      v-for="dict in dict.type.sys_encryp_type"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="密码" prop="dataOutPwd" label-width="60px">
                  <el-col :span="20">
                    <el-input
                      v-model="form.dataOutPwd"
                      placeholder="请输入加密密码"
                      :disabled="
                        form.dataOutEnc === '0' || form.dataOutEnc === '1'
                      "
                       maxlength="20"
                       show-word-limit
                    />
                  </el-col>
                  <el-col :span="4">
                    <el-button
                      size="small"
                      icon="el-icon-refresh"
                      circle style="margin-left: 5px"
                      :disabled="form.dataOutEnc === '0' || form.dataOutEnc === '1'"
                      @click="getRandomString('dataOutPwd', 20)"
                    >
                    </el-button>
                  </el-col>
                </el-form-item>
              </el-col>
            </el-form-item>
            <el-form-item>
              <el-col :span="12">
                <el-form-item label="数据包过期时间" prop="dataExpireTime">
                  <span>
                    <el-tooltip
                      content="数据包过期时间，单位秒，-1为不限制，默认为-1"
                      placement="top"
                    >
                      <i
                        class="el-icon-question"
                        style="margin-left: -12px; margin-right: 10px"
                      ></i>
                    </el-tooltip>
                  </span>
                  <el-input-number
                    v-model="form.dataExpireTime"
                    controls-position="right"
                    :min="-1"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="心跳包时间" prop="heartBeatTime">
                  <span>
                    <el-tooltip
                      content="心跳包时间，单位秒，客户端若在此时间范围内无任何操作将自动下线，-1为不检测，默认为300秒"
                      placement="top"
                    >
                      <i
                        class="el-icon-question"
                        style="margin-left: -12px; margin-right: 10px"
                      ></i>
                    </el-tooltip>
                  </span>
                  <el-input-number
                    v-model="form.heartBeatTime"
                    controls-position="right"
                    :min="-1"
                  />
                </el-form-item>
              </el-col>
            </el-form-item>
            <el-form-item label="API匿名密码" prop="apiPwd" label-width="100px">
              <el-col :span="22">
                <el-input v-model="form.apiPwd" placeholder="请输入API匿名密码" maxlength="20" show-word-limit/>
              </el-col>
              <el-col :span="2">
                <el-button
                  size="small"
                  icon="el-icon-refresh"
                  circle style="margin-left: 5px"
                  @click="getRandomString('apiPwd', 20)"
                >
                </el-button>
              </el-col>
            </el-form-item>
          </el-tab-pane>
          <!-- 绑定设置 -->
          <el-tab-pane label="绑定设置">
            <el-form-item>
              <el-col :span="12">
                <el-form-item label="绑定模式" prop="bindType">
                  <span>
                    <el-tooltip
                      placement="top"
                    >
                      <i
                        class="el-icon-question"
                        style="margin-left: -12px; margin-right: 10px"
                      ></i>
                      <div slot="content">
                        不绑定/无限制：卡密和设备可随意更换，没有任何限制。<br/>
                        用户与设备一对一绑定：卡密和设备都不能更换。<br/>
                        一用户可绑定多个设备：同一卡密可更换设备，但设备不能更换卡密。<br/>
                        多用户可绑定同一设备【推荐：大多数人的需求】：同一设备可更换卡密，但卡密不能更换设备。
                      </div>
                    </el-tooltip>
                  </span>
                  <el-select
                    v-model="form.bindType"
                    placeholder="请选择绑定模式"
                  >
                    <el-option
                      v-for="dict in dict.type.sys_bind_type"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
<!--                <el-form-item label="是否允许解绑" prop="enableUnbind">-->
<!--                  <el-select-->
<!--                    v-model="form.enableUnbind"-->
<!--                    placeholder="请选择是否允许解绑"-->
<!--                  >-->
<!--                    <el-option-->
<!--                      v-for="dict in dict.type.sys_yes_no"-->
<!--                      :key="dict.value"-->
<!--                      :label="dict.label"-->
<!--                      :value="dict.value"-->
<!--                    ></el-option>-->
<!--                  </el-select>-->
<!--                </el-form-item>-->
              </el-col>
            </el-form-item>
            <el-form-item>
              <el-card class="box-card" shadow="never">
                <div slot="header" class="clearfix">
                  <span>
                    是否允许解绑
                    <el-switch
                      v-model="form.enableUnbind"
                      active-value="Y"
                      inactive-value="N"
                    >
                    </el-switch>
                  </span>
                </div>
                <el-form-item>
                  <el-row :gutter="5">
                    <el-col :span="14">
                      <el-card class="box-card" shadow="never" style="height: 240px">
                        <div slot="header" class="clearfix">
                          <span>重置周期</span>
                        </div>
                        <el-form-item label="可重复解绑周期" prop="unbindCycle">
                          <span>
                            <el-tooltip
                                content="每个用户重置解绑次数的时间周期，比如设置为2天，则同一用户每2天可解绑一次，整数，0为不重置，默认为0，最小生效周期为1分钟"
                                placement="top"
                            >
                              <i
                                  class="el-icon-question"
                                  style="margin-left: -12px; margin-right: 10px"
                              ></i>
                            </el-tooltip>
                          </span>
                          <date-duration
                              :seconds="form.unbindCycle"
                              @totalSeconds="handleUnbindCycleQuota"
                          ></date-duration>
                        </el-form-item>
                        <el-form-item
                            label="每周期解绑次数"
                            prop="trialTimes"
                            style="margin-top: 10px; margin-bottom: 10px"
                        >
                          <span>
                            <el-tooltip
                                content="每周期可解绑次数，整数，默认为1，解绑时优先扣除解绑次数，当解绑次数为0时，将根据[允许使用剩余时间/点数解绑]来决定是否可以继续解绑"
                                placement="top"
                            >
                              <i
                                  class="el-icon-question"
                                  style="margin-left: -12px; margin-right: 10px"
                              ></i>
                            </el-tooltip>
                          </span>
                          <el-input-number
                              v-model="form.unbindTimes"
                              :min="1"
                              controls-position="right"
                          />
                        </el-form-item>
                      </el-card>
                   </el-col>
                    <el-col :span="10">
                      <el-card class="box-card" shadow="never">
                        <div slot="header" class="clearfix">
                          <span>
                            允许使用剩余时间/点数解绑
                            <el-tooltip
                                content="解绑时优先扣除解绑次数，当解绑次数为0时，将根据本选项来决定是否可以继续解绑，如果为否，用户将无法继续解绑，如果为是，系统将根据下方配置扣除对应时间/点数"
                                placement="top"
                            >
                              <i
                                  class="el-icon-question"
                                  style="margin-left: -12px; margin-right: 10px"
                              ></i>
                            </el-tooltip>
                            </span>
                          <el-switch
                              v-model="form.enableUnbindByQuota"
                              active-value="Y"
                              inactive-value="N"
                          >
                          </el-switch>
                        </div>
                        <el-form-item label="换绑设备扣除" prop="reduceQuotaUnbind">
                          <span>
                            <el-tooltip
                                content="换绑设备扣减时间或点数，单位秒或点"
                                placement="top"
                            >
                              <i
                                  class="el-icon-question"
                                  style="margin-left: -12px; margin-right: 10px"
                              ></i>
                            </el-tooltip>
                          </span>
                          <el-input-number
                              v-model="form.reduceQuotaUnbind"
                              :min="0"
                              controls-position="right"
                          />
                        </el-form-item>
                        <!-- <el-form-item label="扣除后最少剩余" prop="minQuotaUnbind">
                        <span>
                          <el-tooltip
                            content="解绑扣除后最少剩余的时间或点数，如果扣除后低于此额度，将无法解绑，单位秒或点"
                            placement="top"
                          >
                            <i
                              class="el-icon-question"
                              style="margin-left: -12px; margin-right: 10px"
                            ></i>
                          </el-tooltip>
                        </span>
                        <el-input-number
                          v-model="form.minQuotaUnbind"
                          controls-position="right"
                        />
                        </el-form-item> -->
                        <el-form-item label="允许扣到负数" prop="enableNegative">
                        <span>
                          <el-tooltip
                              content="换绑设备扣减时间或点数，是否允许用户过期(计时模式)或点数为负数(计点模式)，用户过期或点数为负数后无法再次解绑"
                              placement="top"
                          >
                            <i
                                class="el-icon-question"
                                style="margin-left: -12px; margin-right: 10px"
                            ></i>
                          </el-tooltip>
                        </span>
                          <el-select
                              v-model="form.enableNegative"
                              placeholder="请选择是否允许用户过期(计时模式)或点数为负数(计点模式)"
                          >
                            <el-option
                                v-for="dict in dict.type.sys_yes_no"
                                :key="dict.value"
                                :label="dict.label"
                                :value="dict.value"
                            ></el-option>
                          </el-select>
                        </el-form-item>
                      </el-card>
                    </el-col>
                  </el-row>
                </el-form-item>
              </el-card>
            </el-form-item>
          </el-tab-pane>

          <!-- 限开设置 -->
          <el-tab-pane label="限开设置">
            <el-form-item>
              <el-col :span="12">
                <el-form-item label="登录用户数限制" prop="loginLimitU">
                  <span>
                    <el-tooltip
                      content="登录用户数量限制，整数，-1为不限制，默认为-1"
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
                    :min="-1"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="登录机器数限制" prop="loginLimitM">
                  <span>
                    <el-tooltip
                      content="登录机器数量限制，整数，-1为不限制，默认为-1"
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
                    :min="-1"
                  />
                </el-form-item>
              </el-col>
            </el-form-item>
            <el-form-item>
              <el-col :span="12">
                <el-form-item label="达到登录上限后" prop="limitOper">
                  <el-select
                    v-model="form.limitOper"
                    placeholder="请选择达到上限后的操作，默认为提示用户"
                  >
                    <el-option
                      v-for="dict in dict.type.sys_limit_oper"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12" v-show="form.limitOper === '1'">
                <el-form-item label="是否优先注销同一设备的用户" prop="enableFirstLogoutLocalMachine">
                  <span>
                    <el-tooltip
                      content="开启后，超过登录限制后将优先获取当前设备上的登录用户列表并将最早登录的用户注销，如果本设备上没有其他已登录用户，则会获取全局登录用户列表并将最早登录的用户注销"
                      placement="top"
                    >
                      <i
                        class="el-icon-question"
                        style="margin-left: -12px; margin-right: 10px"
                      ></i>
                    </el-tooltip>
                  </span>
                  <el-switch
                    v-model="form.enableFirstLogoutLocalMachine"
                    active-value="Y"
                    inactive-value="N"
                  >
                  </el-switch>
                </el-form-item>
              </el-col>
            </el-form-item>
          </el-tab-pane>
          <!-- 试用设置 -->
          <el-tab-pane label="试用设置">
            <el-form-item>
              <el-col :span="12">
                <el-form-item label="是否开启试用" prop="enableTrial">
                  <el-switch
                    v-model="form.enableTrial"
                    active-value="Y"
                    inactive-value="N"
                  >
                  </el-switch>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="同IP可试用设备数" prop="trialTimesPerIp">
                  <span>
                    <el-tooltip
                      content="每个IP可试用设备数，整数，比如设置为3，则同一局域网内3台设备可各试用一次，-1为不限制，默认为-1"
                      placement="top"
                    >
                      <i
                        class="el-icon-question"
                        style="margin-left: -12px; margin-right: 10px"
                      ></i>
                    </el-tooltip>
                  </span>
                  <el-input-number
                    v-model="form.trialTimesPerIp"
                    :min="-1"
                    controls-position="right"
                  />
                </el-form-item>
              </el-col>
            </el-form-item>
            <el-form-item>
              <el-row :gutter="5">
                <el-col :span="11">
                  <el-card
                    class="box-card"
                    shadow="never"
                    style="height: 264px"
                  >
                    <div slot="header" class="clearfix">
                      <span>按时间试用</span>
                      <span style="float: right">
                        是否启用
                        <el-switch
                          v-model="form.enableTrialByTimeQuantum"
                          active-value="Y"
                          inactive-value="N"
                        >
                        </el-switch>
                      </span>
                    </div>
                    <el-form-item label="可试用时间段">
                      <el-time-picker
                        v-model="trialTimeQuantum"
                        end-placeholder="结束时间"
                        is-range
                        placeholder="选择时间范围"
                        range-separator="-"
                        start-placeholder="开始时间"
                        style="width: 300px"
                        @change="handleTrialTimeQuantum"
                      >
                      </el-time-picker>
                    </el-form-item>
                  </el-card>
                </el-col>
                <el-col :span="13">
                  <el-form-item>
                    <el-card class="box-card" shadow="never">
                      <div slot="header" class="clearfix">
                        <span>按次数试用</span>
                        <span style="float: right">
                          是否启用
                          <el-switch
                            v-model="form.enableTrialByTimes"
                            active-value="Y"
                            inactive-value="N"
                          >
                          </el-switch>
                        </span>
                      </div>
                      <el-form-item label="可重复试用周期" prop="trialCycle">
                        <span>
                          <el-tooltip
                            content="每个设备重置试用次数的时间周期，比如设置为2天，则同一设备每2天可试用一次，整数，0为不重置，默认为0，最小生效周期为1分钟"
                            placement="top"
                          >
                            <i
                              class="el-icon-question"
                              style="margin-left: -12px; margin-right: 10px"
                            ></i>
                          </el-tooltip>
                        </span>
                        <date-duration
                          :seconds="form.trialCycle"
                          @totalSeconds="handleTrialCycleQuota"
                        ></date-duration>
                      </el-form-item>
                      <el-form-item
                        label="每周期试用次数"
                        prop="trialTimes"
                        style="margin-top: 10px; margin-bottom: 10px"
                      >
                        <span>
                          <el-tooltip
                            content="每周期可试用次数，整数，默认为1"
                            placement="top"
                          >
                            <i
                              class="el-icon-question"
                              style="margin-left: -12px; margin-right: 10px"
                            ></i>
                          </el-tooltip>
                        </span>
                        <el-input-number
                          v-model="form.trialTimes"
                          :min="1"
                          controls-position="right"
                        />
                      </el-form-item>
                      <el-form-item label="每次可试用时长" prop="trialTime">
                        <span>
                          <el-tooltip
                            content="每次可试用时长，整数，默认为1小时"
                            placement="top"
                          >
                            <i
                              class="el-icon-question"
                              style="margin-left: -12px; margin-right: 10px"
                            ></i>
                          </el-tooltip>
                        </span>
                        <!-- <el-input-number
                          v-model="form.trialTime"
                          controls-position="right"
                          :min="0"
                        /> -->
                        <date-duration
                          :seconds="form.trialTime"
                          @totalSeconds="handleTrialTimeQuota"
                          :min="1"
                        ></date-duration>
                      </el-form-item>
                      <el-form-item
                        label="试用有效时间内重复登录不增加试用次数"
                        prop="notAddTrialTimesInTrialTime"
                        style="margin-top: 5px"
                      >
                        <el-switch
                          v-model="form.notAddTrialTimesInTrialTime"
                          active-value="Y"
                          inactive-value="N"
                        >
                        </el-switch>
                      </el-form-item>
                    </el-card>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form-item>
          </el-tab-pane>
          <!-- 单码设置 -->
          <!-- <el-tab-pane label="单码设置">
            <el-form-item>
              <el-form-item label="单码前缀" prop="loginCodePrefix">
                <el-input
                  v-model="form.loginCodePrefix"
                  placeholder="请输入单码前缀"
                />
              </el-form-item>
              <el-form-item label="单码后缀" prop="loginCodeSuffix">
                <el-input
                  v-model="form.loginCodeSuffix"
                  placeholder="请输入单码后缀"
                />
              </el-form-item>
            </el-form-item>
            <el-form-item label="单码长度" prop="loginCodeLen">
              <span>
                <el-tooltip
                  content="单码长度，最短为16，最长为48，默认为32"
                  placement="top"
                >
                  <i
                    class="el-icon-question"
                    style="margin-left: -12px; margin-right: 10px"
                  ></i>
                </el-tooltip>
              </span>
              <el-input-number
                v-model="form.loginCodeLen"
                controls-position="right"
                :min="16"
                :max="48"
              />
            </el-form-item>
            <el-form-item>
              <el-form-item label="单码生成规则" prop="loginCodeGenRule">
                <el-select
                  v-model="form.loginCodeGenRule"
                  placeholder="请输入单码生成规则，默认为大小写字母+数字"
                >
                  <el-option
                    v-for="dict in dict.type.sys_gen_rule"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="正则表达式" prop="loginCodeRegex">
                <el-input
                  v-model="form.loginCodeRegex"
                  placeholder="请输入单码生成规则"
                  :disabled="form.loginCodeGenRule !== '7'"
                />
              </el-form-item>
            </el-form-item>
          </el-tab-pane> -->
          <!-- 对接信息 -->
          <el-tab-pane label="接口信息" v-if="form.appId">
            <el-form-item label="API接口地址" prop="apiUrl" label-width="110px">
              <el-col :span="22">
                <el-input
                  placeholder="未获取到相关信息"
                  :value="form.apiUrl"
                  :readonly="true"
                >
                </el-input>
              </el-col>
              <el-col :span="2">
                <el-button
                  id="copyButton"
                  size="small"
                  icon="el-icon-document-copy"
                  circle style="margin-left: 5px"
                  @click="doCopy('apiUrl')"
                >
                </el-button>
              </el-col>
            </el-form-item>
            <el-form-item label="APP KEY" prop="appKey" label-width="110px">
              <el-col :span="22">
                <el-input
                  placeholder="未获取到相关信息"
                  :value="form.appKey"
                  :readonly="true"
                >
                </el-input>
              </el-col>
              <el-col :span="2">
                <el-button
                  id="copyButton"
                  size="small"
                  icon="el-icon-document-copy"
                  circle style="margin-left: 5px"
                  @click="doCopy('appKey')"
                >
                </el-button>
              </el-col>
            </el-form-item>
            <el-form-item label="APP SECRET" prop="appSecret" label-width="110px">
              <el-col :span="22">
                <el-input
                  placeholder="未获取到相关信息"
                  :value="form.appSecret"
                  :readonly="true"
                >
                </el-input>
              </el-col>
              <el-col :span="2">
                <el-button
                  id="copyButton"
                  size="small"
                  icon="el-icon-document-copy"
                  circle style="margin-left: 5px"
                  @click="doCopy('appSecret')"
                >
                </el-button>
              </el-col>
            </el-form-item>
          </el-tab-pane>
          <el-tab-pane v-if="form.appId" label="API匿名信息">
            <el-alert
              :closable="false"
              show-icon
              title="本功能在【通信安全】-【API匿名密码】设定密码后可用，开启功能后请使用匿名API代替原API进行接口调用"
              type="info"
            >
            </el-alert>
            <el-table
              :data="enApiList"
              height="300"
              border
              style="width: 100%; margin-top: 10px"
            >
              <el-table-column label="原API" prop="api"></el-table-column>
              <el-table-column label="匿名API" prop="enApi"></el-table-column>
            </el-table>
          </el-tab-pane>
        </el-tabs>
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
  addApp,
  changeAppChargeStatus,
  changeAppStatus,
  delApp,
  exportApp,
  getApp,
  listApp,
  updateApp
} from '@/api/system/app'
import appIcon from './appIcon'
import Updown from '@/components/Updown'
import DateDuration from '@/components/DateDuration'
import { parseTime } from '@/utils/ruoyi'
import { randomString } from '@/api/common'
import Clipboard from "clipboard";

export default {
  components: {appIcon, Updown, DateDuration},
  name: "App",
  dicts: [
    "sys_normal_disable",
    "sys_bind_type",
    "sys_yes_no",
    "sys_auth_type",
    "sys_bill_type",
    "sys_encryp_type",
    "sys_gen_rule",
    "sys_limit_oper",
    "sys_login_reduce_point_strategy",
  ],
  data() {
    return {
      // API匿名
      enApiList: [],
      // tab激活序号
      tabIdx: "0",
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
      // 软件表格数据
      appList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 备注时间范围
      daterangeCreateTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: this.$store.state.settings.pageSize,
        appName: null,
        description: null,
        status: null,
        bindType: null,
        isCharge: null,
        authType: null,
        billType: null,
        createBy: null,
        createTime: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        appName: [
          { required: true, message: "软件名称不能为空", trigger: "blur" },
        ],
        status: [
          { required: true, message: "软件状态不能为空", trigger: "change" },
        ],
        bindType: [
          { required: true, message: "绑定模式不能为空", trigger: "change" },
        ],
        isCharge: [
          {
            required: true,
            message: "是否开启计费不能为空",
            trigger: "change",
          },
        ],
        freeQuotaReg: [
          {
            required: true,
            message: "首次登录赠送免费时间或点数不能为空",
            trigger: "blur",
          },
        ],
        reduceQuotaUnbind: [
          {
            required: true,
            message: "换绑设备扣减时间或点数不能为空",
            trigger: "blur",
          },
        ],
        authType: [
          { required: true, message: "认证类型不能为空", trigger: "change" },
        ],
        billType: [
          { required: true, message: "计费类型不能为空", trigger: "change" },
        ],
        loginReducePointStrategy: [
          { required: true, message: "登录扣点策略不能为空", trigger: "change" },
        ],
        dataInEnc: [
          {
            required: true,
            message: "数据输入加密方式不能为空",
            trigger: "blur",
          },
        ],
        dataInPwd: [
          {
            required: false,
            message: "数据输入加密密码不能为空",
            trigger: "blur",
          },
        ],
        dataOutEnc: [
          {
            required: true,
            message: "数据输出加密方式不能为空",
            trigger: "blur",
          },
        ],
        dataOutPwd: [
          {
            required: false,
            message: "数据输出加密密码不能为空",
            trigger: "blur",
          },
        ],
        dataExpireTime: [
          {
            required: true,
            message: "数据包过期时间不能为空",
            trigger: "blur",
          },
        ],
        loginLimitU: [
          {
            required: true,
            message: "登录用户数量限制不能为空",
            trigger: "blur",
          },
        ],
        loginLimitM: [
          {
            required: true,
            message: "登录机器数量限制不能为空",
            trigger: "blur",
          },
        ],
        limitOper: [
          {
            required: true,
            message: "达到上限后的操作不能为空",
            trigger: "blur",
          },
        ],
        heartBeatTime: [
          {
            required: true,
            message: "心跳包时间不能为空",
            trigger: "blur",
          },
        ],
        loginCodeLen: [
          {
            required: false,
            message: "单码长度不能为空",
            trigger: "blur",
          },
        ],
        loginCodeGenRule: [
          {
            required: false,
            message: "单码生成规则不能为空",
            trigger: "blur",
          },
        ],
        loginCodeRegex: [
          {
            required: false,
            message: "单码生成规则不能为空",
            trigger: "blur",
          },
        ],
        unbindTimes: [
          {
            required: true,
            message: "解绑次数不能为空",
            trigger: "blur",
          },
        ],
        // minQuotaUnbind: [
        //   {
        //     required: true,
        //     message: "解绑后剩余额度不能为空",
        //     trigger: "blur",
        //   },
        // ],
        enableUnbind: [
          {
            required: true,
            message: "是否允许解绑不能为空",
            trigger: "blur",
          },
        ],
        enableUnbindByQuota: [
          {
            required: true,
            message: "是否允许使用剩余时间/点数解绑不能为空",
            trigger: "blur",
          },
        ],
        enableNegative: [
          {
            required: true,
            message: "是否允许扣到负数不能为空",
            trigger: "blur",
          },
        ],
        enableFeCharge: [
          {
            required: true,
            message: "是否允许前台充值不能为空",
            trigger: "blur",
          },
        ],
      },
      trialTimeQuantum: null,
    };
  },
  created() {
    this.getList();
    this.showTabPane(false, 3);
  },
  methods: {
    /** 查询软件列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeCreateTime && "" != this.daterangeCreateTime) {
        this.queryParams.params["beginCreateTime"] =
          this.daterangeCreateTime[0];
        this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
      }
      listApp(this.queryParams).then((response) => {
        this.appList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 软件状态修改
    handleStatusChange(row) {
      let text = row.status === "0" ? "启用" : "停用（切换为维护状态）";
      this.$modal
        .confirm('确认要"' + text + '""' + row.appName + '"软件吗？')
        .then(function () {
          return changeAppStatus(row.appId, row.status);
        })
        .then(() => {
          this.$modal.msgSuccess(text + "成功");
        })
        .catch(function () {
          row.status = row.status === "0" ? "1" : "0";
        });
    },
    // 软件计费状态修改
    handleChargeStatusChange(row) {
      let text = row.isCharge === "Y" ? "开启" : "关闭";
      this.$modal
        .confirm('确认要"' + text + '""' + row.appName + '"软件的计费模式吗？')
        .then(function () {
          return changeAppChargeStatus(row.appId, row.isCharge);
        })
        .then(() => {
          this.$modal.msgSuccess(text + "成功");
        })
        .catch(function () {
          row.isCharge = row.isCharge === "Y" ? "N" : "Y";
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
        appName: undefined,
        description: undefined,
        apiUrl: undefined,
        status: "0",
        bindType: "3",
        isCharge: "Y",
        idxUrl: undefined,
        freeQuotaReg: 0,
        reduceQuotaUnbind: 0,
        authType: undefined,
        billType: undefined,
        dataInEnc: "0",
        dataInPwd: undefined,
        dataOutEnc: "0",
        dataOutPwd: undefined,
        dataExpireTime: -1,
        loginLimitU: -1,
        loginLimitM: -1,
        limitOper: "0",
        heartBeatTime: 300,
        apiPwd: undefined,
        loginCodePrefix: undefined,
        loginCodeSuffix: undefined,
        loginCodeLen: 32,
        loginCodeGenRule: "0",
        loginCodeRegex: undefined,
        icon: undefined,
        remark: undefined,
        trialTimesPerIp: -1,
        trialCycle: 0,
        trialTimes: 1,
        trialTime: 3600,
        enableTrial: "N",
        enableTrialByTimeQuantum: "N",
        enableTrialByTimes: "N",
        trialTimeQuantum: null,
        enableUnbind: "N",
        unbindTimes: 0,
        // minQuotaUnbind: 0,
        enableUnbindByQuota: "N",
        customBuyUrl: undefined,
        enableNegative: "N",
        enableFeCharge: "Y",
        loginReducePointStrategy: "0",
        unbindCycle: 0,
      };
      this.resetForm("form");
      this.tabIdx = "0";
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.daterangeCreateTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.appId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加软件";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const appId = row.appId || this.ids;
      getApp(appId).then((response) => {
        this.form = response.data;
        if (this.form.trialTimeQuantum) {
          let arr = this.form.trialTimeQuantum.split("-");
          if (arr.length == 2) {
            this.trialTimeQuantum = [
              new Date("1970/01/01" + " " + arr[0]),
              new Date("1970/01/01" + " " + arr[1]),
            ];
          } else {
            this.trialTimeQuantum = null;
          }
        } else {
          this.trialTimeQuantum = null;
        }
        this.enApiList = response.data.enApi;
        this.open = true;
        this.title = "配置软件";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.appId != null) {
            this.form.authType = undefined;
            this.form.billType = undefined;
            updateApp(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addApp(this.form).then((response) => {
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
      const appIds = row.appId || this.ids;
      this.$modal
        .confirm("是否确认删除数据项？")
        .then(function () {
          return delApp(appIds);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal
        .confirm("是否确认导出所有软件数据项？")
        .then(() => {
          this.exportLoading = true;
          return exportApp(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
          this.exportLoading = false;
        })
        .catch(() => {});
    },
    showTabPane(show, idx) {
      this.$nextTick(() => {
        if (show) {
          this.$refs.tabs.$children[0].$refs.tabs[idx].style.display =
            "inline-block";
        } else {
          try {
            this.$refs.tabs.$children[0].$refs.tabs[idx].style.display = "none";
          } catch (error) {}
        }
      });
    },
    handleAppUser: function (row) {
      const appId = row.appId;
      this.$router.push({
        path: "/verify/appUser",
        query: {
          appId: appId,
        },
      });
    },
    handleCardTemplate: function (row) {
      const appId = row.appId;
      this.$router.push({
        path: "/verify/accountMode/cardTemplate",
        query: {
          appId: appId,
        },
      });
    },
    handleCardManage: function (row) {
      const appId = row.appId;
      this.$router.push({
        path: "/verify/accountMode/card",
        query: {
          appId: appId,
        },
      });
    },
    handleLoginCodeTemplate: function (row) {
      const appId = row.appId;
      this.$router.push({
        path: "/verify/cardMode/loginCodeTemplate",
        query: {
          appId: appId,
        },
      });
    },
    handleLoginCodeManage: function (row) {
      const appId = row.appId;
      this.$router.push({
        path: "/verify/cardMode/loginCode",
        query: {
          appId: appId,
        },
      });
    },
    handleVersionManage: function (row) {
      const appId = row.appId;
      this.$router.push({
        path: "/verify/appVersion",
        query: {
          appId: appId,
        },
      });
    },
    // 更多操作触发
    handleCommand(command, row) {
      switch (command) {
        case "handleAppUser":
          this.handleAppUser(row);
          break;
        case "handleVersionManage":
          this.handleVersionManage(row);
          break;
        case "handleCardManage":
          this.handleCardManage(row);
          break;
        case "handleCardTemplate":
          this.handleCardTemplate(row);
          break;
        case "handleLoginCodeManage":
          this.handleLoginCodeManage(row);
          break;
        case "handleLoginCodeTemplate":
          this.handleLoginCodeTemplate(row);
          break;
        default:
          break;
      }
    },
    handleTrialCycleQuota(totalSeconds) {
      this.form.trialCycle = totalSeconds;
    },
    handleTrialTimeQuota(totalSeconds) {
      this.form.trialTime = totalSeconds;
    },
    parseTime(time) {
      return parseTime(time, "{h}:{i}:{s}");
    },
    handleTrialTimeQuantum(timeQuantum) {
      this.form.trialTimeQuantum =
        this.parseTime(timeQuantum[0]) + "-" + this.parseTime(timeQuantum[1]);
    },
    getRandomString(index, length) {
      randomString(length).then(response => {
        this.form[index] = response.msg;
      })
    },
    doCopy(index) {
      var clipboard = new Clipboard("#copyButton", {
        text: () => {
          // 如果想从其它DOM元素内容复制。应该是target:function(){return: };
          if(index === 'shopUrl') {
            if(!this.form[index]) {
              this.$modal.msgError("请先设置软件链接");
              return;
            }
            return this.getShopUrlPrefix() + this.form[index];
          } else {
            return this.form[index];
          }
        },
      });
      clipboard.on("success", (e) => {
        this.$modal.msgSuccess("已成功复制到剪贴板");
        clipboard.destroy();
      });
      clipboard.on("error", (e) => {
        this.$modal.msgError(
          "复制失败，您的浏览器不支持复制，请自行复制"
        );
        clipboard.destroy();
      });
    },
    getShopUrlPrefix() {
      let domain = this.$store.state.settings.domain;
      return domain + (domain.endsWith('/') ? "" : "/") + "shop/a/";
    },
    handleUnbindCycleQuota(totalSeconds) {
      this.form.unbindCycle = totalSeconds;
    },
  },
  watch: {
    // "form.authType": {
    //   handler(newVal, oldVal) {
    //     this.showTabPane(this.form.authType === "1", 3);
    //   },
    // },
  },
};
</script>
