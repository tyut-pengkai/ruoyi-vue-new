<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
    >
      <el-form-item label="所属软件" prop="appId">
        <el-select
          v-model="queryParams.appId"
          filterable
          clearable
          placeholder="请选择"
          prop="appId"
          @change="changeSearchApp"
        >
          <el-option
            v-for="item in appList"
            :key="item.appId"
            :label="
              '[' +
              (item.authType == '0' ? '账号' : '单码') +
              (item.billType == '0' ? '计时' : '计点') +
              '] ' +
              item.appName
            "
            :value="item.appId"
            :disabled="item.disabled"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="卡类" prop="cardName">
<!--        <el-input-->
<!--          v-model="queryParams.cardName"-->
<!--          placeholder="请输入卡密名称"-->
<!--          clearable-->
<!--          size="small"-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
        <el-select
          v-model="queryParams.templateId"
          clearable
          filterable
          placeholder="请选择"
          prop="templateId"
          style="width: 150px"
        >
          <el-option
            v-for="item in templateList"
            :key="item.templateId"
            :disabled="item.disabled"
            :label="
                    item.templateId != 0
                      ? '[' + item.app.appName + ']' + item.cardName
                      : item.cardName
                  "
            :value="item.templateId"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="充值卡号" prop="cardNo">
        <el-input
          v-model="queryParams.cardNo"
          placeholder="请输入充值卡号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="卡密面值" prop="quota">
        <el-input
          v-model="queryParams.quota"
          placeholder="请输入卡密面值"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="卡密价格" prop="price">
        <el-input
          v-model="queryParams.price"
          placeholder="请输入卡密价格"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="充值过期" prop="">
        <el-date-picker
          v-model="daterangeExpireTime"
          size="small"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="是否售出" prop="isSold">
        <el-select
          v-model="queryParams.isSold"
          placeholder="请选择是否售出"
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
      <el-form-item label="是否上架" prop="onSale">
        <el-select
          v-model="queryParams.onSale"
          placeholder="请选择是否上架"
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
      <el-form-item label="是否已用" prop="isCharged">
        <el-select
          v-model="queryParams.isCharged"
          placeholder="请选择是否已用"
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
      <el-form-item label="是否代理制卡" prop="isAgent">
        <el-select
          v-model="queryParams.isAgent"
          clearable
          placeholder="请选择是否代理制卡"
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
      <el-form-item label="所属代理" prop="agentName">
        <el-input
          v-model="queryParams.agentName"
          clearable
          placeholder="请输入代理名称"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="卡密状态" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="请选择卡密状态"
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
      <el-form-item label="充值规则" prop="chargeRule">
        <el-select
          v-model="queryParams.chargeRule"
          placeholder="请选择充值规则"
          clearable
          size="small"
        >
          <el-option
            v-for="dict in dict.type.sys_charge_rule"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="批次号" prop="batchNo">
<!--        <el-input-->
<!--          v-model="queryParams.batchNo"-->
<!--          clearable-->
<!--          placeholder="请输入批次号"-->
<!--          size="small"-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
        <el-select v-model="queryParams.batchNo" clearable placeholder="请选择">
          <el-option
            v-for="item in batchNoList"
            :key="item.batchNo"
            :label="item.batchNo"
            :value="item.batchNo">
            <span style="float: left">{{ item.batchNo }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px"> 共 {{ item.num }} 个</span>
          </el-option>
        </el-select>
      </el-form-item>
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
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
        >搜索
        </el-button>
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
          v-hasPermi="['system:card:add']"
          >新增</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleBatchAdd"
          v-hasPermi="['system:card:add']"
          >批量制卡</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="primary"
            plain
            icon="el-icon-refresh"
            size="mini"
            @click="handleReplace"
            v-hasPermi="['system:card:replace']"
        >批量换卡</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:card:edit']"
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
          v-hasPermi="['system:card:remove']"
        >删除
        </el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:card:import']"
          icon="el-icon-upload2"
          plain
          size="mini"
          type="info"
          @click="handleImport"
        >导入
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['system:card:export']"
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
      :data="cardList"
      row-key="cardId"
      :expand-row-keys="expands"
      @row-click="handleRowClick"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="expand">
        <template slot-scope="scope">
          <el-form label-position="left">
            <el-form-item>
              <el-col :span="4"> -</el-col>
              <el-col :span="5">
                <el-form-item label="卡号">
                  <span>{{ scope.row.cardNo }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item label="密码">
                  <span>{{ scope.row.cardPass }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item label="面值">
                  <span>{{
                    parseSeconds(scope.row.app.billType, scope.row.quota)
                  }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item label="零售价格">
                  <span>{{ parseMoney(scope.row.price) }}元 </span>
                </el-form-item>
              </el-col>
            </el-form-item>
            <el-form-item>
              <el-col :span="4"> -</el-col>
              <el-col :span="5">
                <el-form-item label="是否售出">
                  <dict-tag
                    :options="dict.type.sys_yes_no"
                    :value="scope.row.isSold"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item label="是否上架">
                  <dict-tag
                    :options="dict.type.sys_yes_no"
                    :value="scope.row.onSale"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item label="是否已用">
                  <dict-tag
                    :options="dict.type.sys_yes_no"
                    :value="scope.row.isCharged"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item label="充值规则">
                  <dict-tag
                    :options="dict.type.sys_charge_rule"
                    :value="scope.row.chargeRule"
                  />
                </el-form-item>
              </el-col>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column align="center" type="selection" width="55" />
      <!-- <el-table-column label="" type="index" align="center" /> -->
      <el-table-column align="center" label="编号" prop="cardId" />
      <el-table-column
        label="所属软件"
        align="center"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          {{ scope.row.app.appName }}
        </template>
      </el-table-column>
      <el-table-column
        label="卡名称"
        align="center"
        prop="cardName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="卡号"
        prop="cardNo"
      >
        <template slot-scope="scope">
          <span>{{ scope.row.cardNo }} </span>
        </template>
      </el-table-column>
      <!-- <el-table-column label="密码" align="center" prop="cardPass" /> -->
      <!-- <el-table-column label="面值" align="center" prop="quota">
        <template slot-scope="scope">
          <span>{{ parseSeconds(scope.row.app.billType, scope.row.quota) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="零售价格" align="center" prop="price">
        <template slot-scope="scope">
          <span>{{ parseMoney(scope.row.price) }}元 </span>
        </template>
      </el-table-column> -->
      <el-table-column align="center" label="登录用户数限制(卡)">
        <template slot-scope="scope">
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
      <el-table-column align="center" label="登录设备数限制(卡)">
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
      </el-table-column>
      <el-table-column
        label="充值过期"
        align="center"
        prop="expireTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{
              !scope.row.expireTime ||
              scope.row.expireTime == "9999-12-31 23:59:59"
                ? "长期有效"
                : parseTime(scope.row.expireTime)
            }}</span>
        </template>
      </el-table-column>
<!--      <el-table-column align="center" label="卡密状态" prop="status">-->
<!--        <template slot-scope="scope">-->
<!--          <dict-tag-->
<!--            :options="dict.type.sys_normal_disable"-->
<!--            :value="scope.row.status"-->
<!--          />-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column align="center" label="是否售出" prop="isSold">-->
<!--        <template slot-scope="scope">-->
<!--          <dict-tag :options="dict.type.sys_yes_no" :value="scope.row.isSold" />-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="是否上架" align="center" prop="onSale">-->
<!--        <template slot-scope="scope">-->
<!--          <dict-tag :options="dict.type.sys_yes_no" :value="scope.row.onSale"/>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="是否已用" align="center" prop="isCharged">-->
<!--        <template slot-scope="scope">-->
<!--          <dict-tag-->
<!--            :options="dict.type.sys_yes_no"-->
<!--            :value="scope.row.isCharged"-->
<!--          />-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column align="center" label="代理制卡" prop="isAgent">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_yes_no"
            :value="scope.row.isAgent"
          />
        </template>
      </el-table-column>
      <el-table-column
        label="所属代理"
        align="center"
        prop="agentUser.nickName"
        :show-overflow-tooltip="true">
        <template slot-scope="scope">
          {{scope.row.agentUser && scope.row.agentUser.userName ? scope.row.agentUser.nickName + '(' + scope.row.agentUser.userName + ')' : ''}}
        </template>
      </el-table-column>
      <!-- <el-table-column label="充值规则" align="center" prop="chargeRule">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_charge_rule"
            :value="scope.row.chargeRule"
          />
        </template>
      </el-table-column> -->
      <el-table-column
        label="备注"
        align="center"
        prop="remark"
        :show-overflow-tooltip="true"
      />
      <el-table-column label="卡密状态" align="center">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            active-value="0"
            inactive-value="1"
            @change="handleStatusChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="是否售出" align="center">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isSold"
            active-value="Y"
            inactive-value="N"
            @change="handleIsSoldChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="是否上架" align="center">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.onSale"
            active-value="Y"
            inactive-value="N"
            @change="handleOnSaleChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="是否已用" align="center">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isCharged"
            active-value="Y"
            inactive-value="N"
            @change="handleIsChargedChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
        width="130"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:card:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:card:remove']"
            >删除</el-button
          >
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

    <!-- 添加或修改卡密对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="800px"
      append-to-body
      :close-on-click-modal="false"
    >
      <el-form ref="form" :model="form" :rules="rules">
        <!-- 新增 -->
        <div v-if="form.cardId == null">
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
                    :label="
                      '[' +
                      (item.authType == '0' ? '账号' : '单码') +
                      (item.billType == '0' ? '计时' : '计点') +
                      '] ' +
                      item.appName
                    "
                    :value="item.appId"
                    :disabled="item.disabled"
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
        </div>
        <!-- 修改 -->
        <div v-if="form.cardId">
          <el-form-item prop="">
            <el-col :span="12">
              <el-form-item label="所属软件" label-width="80px" prop="">
                {{ form.app.appName }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="计费类型" label-width="80px" prop="">
                <dict-tag
                  :options="dict.type.sys_bill_type"
                  :value="form.app.billType"
                />
              </el-form-item>
            </el-col>
          </el-form-item>
        </div>
        <el-form-item label="卡密名称" prop="cardName" label-width="80px">
          <el-input v-model="form.cardName" placeholder="请输入卡密名称" maxlength="50" show-word-limit :disabled="form.isCharged === 'Y'"/>
        </el-form-item>
        <el-form-item label="充值卡号" prop="cardNo" label-width="80px">
          <el-input v-model="form.cardNo" placeholder="请输入卡号" maxlength="100" show-word-limit :disabled="form.isCharged === 'Y'"/>
        </el-form-item>
        <el-form-item label="充值密码" prop="cardPass" label-width="80px">
          <el-input v-model="form.cardPass" placeholder="请输入密码" maxlength="100" show-word-limit :disabled="form.isCharged === 'Y'"/>
        </el-form-item>
        <el-form-item prop="">
          <el-col :span="12">
            <el-form-item label="卡密状态" prop="status">
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
            <el-form-item label="充值过期" prop="expireTime">
              <el-date-picker
                clearable
                size="small"
                v-model="form.expireTime"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="选择过期时间"
                :picker-options="pickerOptions"
                :disabled="form.isCharged === 'Y'"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item prop="">
          <div v-if="form.cardId == null">
            <el-col :span="12">
              <el-form-item
                label="卡密面值"
                prop="quota"
                label-width="80px"
                style="width: 300px"
              >
                <div v-if="app">
                  <div v-if="app.billType === '0'">
                    <date-duration
                      @totalSeconds="handleQuota"
                      :seconds="form.quota"
                      :disabled="form.isCharged === 'Y'"
                    ></date-duration>
                  </div>
                  <div v-if="app.billType === '1'">
                    <el-input-number
                      v-model="form.quota"
                      controls-position="right"
                      :min="0"
                      :disabled="form.isCharged === 'Y'"
                    />
                    <span style="margin-left: 6px">点</span>
                  </div>
                </div>
                <div v-else>请先选择软件</div>
              </el-form-item>
            </el-col>
          </div>
          <div v-if="form.cardId && form.app">
            <el-col :span="12">
              <el-form-item
                label="卡密面值"
                prop="quota"
                label-width="80px"
                style="width: 300px"
              >
                <div v-if="form.app.billType === '0'">
                  <date-duration
                    @totalSeconds="handleQuota"
                    :seconds="form.quota"
                    :disabled="form.isCharged === 'Y'"
                  ></date-duration>
                </div>
                <div v-if="form.app.billType === '1'">
                  <el-input-number
                    v-model="form.quota"
                    controls-position="right"
                    :min="0"
                    :disabled="form.isCharged === 'Y'"
                  />
                  <span style="margin-left: 6px">点</span>
                </div>
              </el-form-item>
            </el-col>
          </div>
          <el-col :span="12">
            <el-form-item label="零售价格" label-width="80px" prop="price">
              <el-input-number
                v-model="form.price"
                controls-position="right"
                :precision="2"
                :step="0.01"
                :min="0"
                :disabled="form.isCharged === 'Y'"
              />
              <span>元</span>
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
                :disabled="form.isCharged === 'Y'"
              />
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
                :disabled="form.isCharged === 'Y'"
              />
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item prop="">
          <el-col :span="12">
            <el-form-item label="是否上架" prop="onSale">
              <el-select v-model="form.onSale" placeholder="请选择是否上架" :disabled="form.isCharged === 'Y'">
                <el-option
                  v-for="dict in dict.type.sys_yes_no"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否售出" prop="isSold">
              <el-select v-model="form.isSold" placeholder="请选择是否售出" :disabled="form.isCharged === 'Y'">
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
            <el-form-item label="是否已用" prop="isCharged">
              <el-select v-model="form.isCharged" placeholder="请选择是否已用">
                <el-option
                  v-for="dict in dict.type.sys_yes_no"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
            <!-- <el-form-item label="卡类ID" prop="templateId">
              <el-input v-model="form.templateId" placeholder="请输入卡类ID" />
            </el-form-item> -->
          </el-col>
          <el-col :span="12">
            <el-form-item label="使用日期" prop="chargeTime">
              <el-date-picker
                v-model="form.chargeTime"
                clearable
                placeholder="选择使用日期"
                size="small"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item prop="">
          <el-col :span="12">
            <el-form-item label="充值规则" prop="chargeRule">
              <el-select v-model="form.chargeRule" placeholder="请选择充值规则" :disabled="form.isCharged === 'Y'">
                <el-option
                  v-for="dict in dict.type.sys_charge_rule"
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
            <el-form-item label="是否代理制卡" prop="isAgent">
              <el-select
                v-model="form.isAgent"
                placeholder="请选择是否代理制卡"
                :disabled="form.isCharged === 'Y'"
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
          <el-col :span="12">
            <div v-if="form.cardId">
              <el-form-item label="所属代理" prop="agentId">
                <span v-if="form.agentUser && form.agentUser.userName">
                  {{ form.agentUser.nickName }} ({{ form.agentUser.userName }})
                </span>
              </el-form-item>
            </div>
          </el-col>
        </el-form-item>
        <el-form-item label="充值卡自定义参数" prop="cardCustomParams">
          <el-input
            v-model="form.cardCustomParams"
            placeholder="请输入内容"
            type="textarea"
             maxlength="2000" show-word-limit
            :disabled="form.isCharged === 'Y'"
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form.remark"
            type="textarea"
            placeholder="请输入内容"
          />
        </el-form-item>
        <div v-if="form.cardId">
          <el-form-item prop="">
            <el-col :span="12">
              <el-form-item label="制卡批次" prop="batchNo"
              >{{ form.batchNo }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="所属代理" prop="agentId">
                <span v-if="form.agentUser && form.agentUser.userName">
                  {{ form.agentUser.nickName }} ({{ form.agentUser.userName }})
                </span>
              </el-form-item>
            </el-col>
          </el-form-item>
          <el-form-item prop="">
            <el-col :span="12">
              <el-form-item label="创建人" prop="createBy"
              >{{ form.createBy }}
              </el-form-item>
              <el-form-item label="创建时间" prop="createTime"
              >{{ form.createTime }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="最后更新" prop="updateBy"
              >{{ form.updateBy }}
              </el-form-item>
              <el-form-item label="更新时间" prop="updateTime"
              >{{ form.updateTime }}
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

    <!-- 批量制卡对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="batchOpen"
      width="800px"
      append-to-body
      :close-on-click-modal="false"
    >
      <el-alert
        :closable="false"
        show-icon
        style="margin-bottom: 10px"
        title="批量制卡任务将在后台执行，如果制卡数量过多可能需要等待后台执行完毕才会在列表中显示，请不要重复操作"
        type="info"
      />
      <el-form ref="formBatch" :model="formBatch" :rules="rulesBatch">
        <el-form-item prop="">
          <el-col :span="12">
            <el-form-item label="所属软件" prop="appId">
              <el-select
                v-model="formBatch.appId"
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
        <el-form-item label="选择卡类" prop="templateId">
          <el-select
            v-model="formBatch.templateId"
            placeholder="请选择"
            @change="changeSelectedCard"
          >
            <el-option
              v-for="item in cardTemplateList"
              :key="item.templateId"
              :label="
                item.cardName +
                '|' +
                parseSeconds(item.app.billType, item.quota) +
                '|' +
                item.price +
                '元'
              "
              :value="item.templateId"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="">
          <el-col :span="12">
            <el-form-item
              label="制卡数量"
              prop="genQuantity"
              label-width="80px"
            >
              <el-input-number
                v-model="formBatch.genQuantity"
                controls-position="right"
                :min="1"
                :max="10000"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否上架" prop="onSale">
              <el-select
                v-model="formBatch.onSale"
                placeholder="请选择是否上架"
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
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="formBatch.remark"
            type="textarea"
            placeholder="请输入内容"
          />
        </el-form-item>
      </el-form>
      <div v-show="cardStr">
        <el-divider content-position="left"
          >本次生成结果如下(下方左侧为详细数据，右侧为简略数据)
        </el-divider>
        <el-row>
          <el-col :span="12">
            <el-input
              v-model="cardStr"
              :autosize="{ minRows: 2, maxRows: 4 }"
              placeholder=""
              type="textarea"
            >
            </el-input>
          </el-col>
          <el-col :span="12">
            <el-input
              v-model="cardSimpleStr"
              :autosize="{ minRows: 2, maxRows: 4 }"
              placeholder=""
              type="textarea"
            >
            </el-input>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <div align="right" style="margin-top: 5px">
              <el-button id="copyButton" plain @click="copy(1)">
                复制文本
              </el-button>
              <el-button id="saveButton" plain @click="save(1)">
                下载保存
              </el-button>
            </div>
          </el-col>
          <el-col :span="12">
            <div align="right" style="margin-top: 5px">
              <el-button id="copyButton" plain @click="copy(2)">
                复制文本
              </el-button>
              <el-button id="saveButton" plain @click="save(2)">
                下载保存
              </el-button>
            </div>
          </el-col>
        </el-row>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button
          :loading="batchLoading"
          type="primary"
          @click="submitFormBatch"
        >
          确 定
        </el-button>
        <el-button @click="cancelBatch">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 卡密导入对话框 -->
    <el-dialog
      :title="upload.title"
      :visible.sync="upload.open"
      append-to-body
      width="400px"
    >
      <el-upload
        ref="upload"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :auto-upload="false"
        :disabled="upload.isUploading"
        :headers="upload.headers"
        :limit="1"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        accept=".xlsx, .xls"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div slot="tip" class="el-upload__tip text-center">
          <!-- <div class="el-upload__tip" slot="tip">
            <el-checkbox v-model="upload.updateSupport" />
            是否更新已经存在的用户数据
          </div> -->
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link
            :underline="false"
            style="font-size: 12px; vertical-align: baseline"
            type="primary"
            @click="importTemplate"
          >下载模板
          </el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 批量换卡对话框 -->
    <el-dialog
      title="批量换卡"
      :visible.sync="batchReplaceOpen"
      width="800px"
      append-to-body
      :close-on-click-modal="false"
    >
      <el-alert
          :closable="false"
          show-icon
          style="margin-bottom: 10px"
          title="换卡成功后将自动生成一张新卡，旧卡将被删除，换卡仅限未激活使用的卡，如果已经激活使用，则无法换卡"
          type="info"
      />
      <el-form ref="formReplace" :model="formReplace" :rules="rulesReplace" >
        <el-form-item prop="">
          <el-col :span="12">
            <el-form-item label="所属软件" prop="appId">
              <el-select
                v-model="formReplace.appId"
                filterable
                placeholder="请选择"
                prop="appId"
                @change="changeSelectedAppReplace"
              >
                <el-option label="自动检测所属软件" :value="0">自动检测所属软件</el-option>
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
              <div v-else-if="formReplace.appId === 0">自动检测</div>
              <div v-else>请先选择软件</div>
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item>
          <el-col :span="12">
            <el-form-item label="换卡模式" prop="changeMode">
              <span>
                <el-tooltip
                    content="账号登录模式只支持整卡更换，如果卡已经被使用，则不支持换卡"
                    placement="top"
                >
                  <i
                      class="el-icon-question"
                      style="margin-left: -12px; margin-right: 10px"
                  ></i>
                </el-tooltip>
              </span>
              <el-radio-group v-model="formReplace.changeMode" disabled>
                <el-radio
                    v-for="dict in dict.type.sys_yes_no"
                    :key="dict.value"
                    :label="dict.value"
                >{{ dict.label === '是' ? '换整卡' : '换残卡' }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item label="操作内容（输入格式：每行一条数据）" prop="content">
          <el-input
            v-model="formReplace.content"
            :rows="10"
            placeholder="输入格式：每行一条数据"
            type="textarea"
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="formReplace.remark"
            type="textarea"
            placeholder="请输入内容"
          />
        </el-form-item>
      </el-form>
      <div v-show="result">
        <el-divider content-position="left">以下为换卡结果，新卡请自行复制保存</el-divider>
        <el-input
          v-model="result"
          :rows="20"
          style="max-width: 1400px"
          type="textarea"
        >
        </el-input>
        <div align="right" style="margin-top: 5px">
          <el-button id="copyButton" plain @click="copy(3)">
            复制文本
          </el-button>
          <el-button id="saveButton" plain @click="save(3)">
            下载保存
          </el-button>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button
          :loading="batchLoading"
          type="primary"
          @click="submitFormReplace"
        >
          确 定
        </el-button>
        <el-button @click="cancelReplace">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {addCard, delCard, exportCard, getCard, listCard, updateCard, getBatchNoList, batchReplace} from "@/api/system/card";
import {getApp, listAppAll} from "@/api/system/app";
import DateDuration from "@/components/DateDuration";
import Updown from "@/components/Updown";
import {parseMoney, parseSeconds, parseUnit} from "@/utils/my";
import {getCardTemplate, listCardTemplateAll,} from "@/api/system/cardTemplate";
import Clipboard from "clipboard";
import {getToken} from "@/utils/auth";

export default {
  name: "Card",
  dicts: [
    "sys_yes_no",
    "sys_normal_disable",
    "sys_charge_rule",
    "sys_bill_type",
  ],
  components: { DateDuration, Updown },
  data() {
    return {
      batchLoading: false,
      appList: [],
      appMap: [],
      // 卡类数据
      cardTemplateList: [],
      // 软件数据
      app: null,
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
      // 卡密表格数据
      cardList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示批量制卡弹出层
      batchOpen: false,
      // 备注时间范围
      daterangeExpireTime: [],
      // 卡密导入参数
      upload: {
        // 是否显示弹出层（卡密导入）
        open: false,
        // 弹出层标题（卡密导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的卡密数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: {Authorization: "Bearer " + getToken()},
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/system/card/importData",
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: this.$store.state.settings.pageSize,
        appId: null,
        cardName: null,
        cardNo: null,
        quota: null,
        price: null,
        expireTime: null,
        isSold: null,
        onSale: null,
        isCharged: null,
        isAgent: null,
        status: null,
        chargeRule: null,
      },
      // 表单参数
      form: {},
      // 表单参数
      formBatch: {},
      // 表单校验
      rules: {
        appId: [{required: true, message: "软件不能为空", trigger: "blur"}],
        cardName: [
          {required: true, message: "卡名称不能为空", trigger: "blur"},
        ],
        cardNo: [{required: true, message: "卡号不能为空", trigger: "blur"}],
        // cardPass: [
        //   { required: true, message: "密码不能为空", trigger: "blur" },
        // ],
        quota: [{required: true, message: "额度不能为空", trigger: "blur"}],
        price: [{required: true, message: "价格不能为空", trigger: "blur"}],
        expireTime: [
          {required: true, message: "过期时间不能为空", trigger: "blur"},
        ],
        isSold: [
          {required: true, message: "是否售出不能为空", trigger: "change"},
        ],
        onSale: [
          {required: true, message: "是否上架不能为空", trigger: "change"},
        ],
        isCharged: [
          {required: true, message: "是否已用不能为空", trigger: "change"},
        ],
        // isAgent: [
        //   {
        //     required: true,
        //     message: "是否代理制卡不能为空",
        //     trigger: "change",
        //   },
        // ],
        status: [
          {required: true, message: "卡密状态不能为空", trigger: "blur"},
        ],
        chargeRule: [
          {required: true, message: "充值规则不能为空", trigger: "change"},
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
      },
      rulesBatch: {
        appId: [{ required: true, message: "软件不能为空", trigger: "blur" }],
        templateId: [
          { required: true, message: "卡类不能为空", trigger: "change" },
        ],
        onSale: [
          { required: true, message: "是否上架不能为空", trigger: "change" },
        ],
        genQuantity: [
          { required: true, message: "制卡数量不能为空", trigger: "blur" },
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
      expands: [],
      cardStr: "",
      cardSimpleStr: "",
      batchNoList: [],
      // 批量换卡
      formReplace: {
        appId: 0,
        changeMode: 'Y',
      },
      batchReplaceOpen: false,
      rulesReplace:{
        content: [
          {required: true, message: "操作内容不能为空", trigger: "blur"},
        ],
        changeMode: [
          {required: true, message: "换卡模式不能为空", trigger: "blur"},
        ],
      },
      result: "",
      templateList: [],
    };
  },
  created() {
    // const appId = this.$route.params && this.$route.params.appId;
    const appId = this.$route.query && this.$route.query.appId;
    this.getAppList();
    if (appId != undefined && appId != null) {
      getApp(appId).then((response) => {
        this.app = response.data;
        // const title = "卡密管理";
        // const appName = this.app.appName;
        // const route = Object.assign({}, this.$route, {
        //   title: `${title}-${appName}`,
        // });
        // this.$store.dispatch("tagsView/updateVisitedView", route);
        this.getList();
      });
    } else {
      // this.$modal.alertError("未获取到当前软件信息");
      this.getList();
    }
    getBatchNoList().then((response) => {
      this.batchNoList = response.data;
    });
    listCardTemplateAll({}).then((response) => {
      this.templateList = response.rows;
    });
  },
  methods: {
    /** 获取批次号列表 **/
    getBatchNoList,
    /** 查询卡密列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeExpireTime && "" != this.daterangeExpireTime) {
        this.queryParams.params["beginExpireTime"] = this.daterangeExpireTime[0];
        this.queryParams.params["endExpireTime"] = this.daterangeExpireTime[1];
      }
      if (null != this.queryParams.agentName && "" !== this.queryParams.agentName) {
        this.queryParams.params["agentName"] = this.queryParams.agentName;
      }
      if (this.app) {
        this.queryParams.appId = this.app.appId;
      }
      listCard(this.queryParams).then((response) => {
        this.cardList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 取消按钮
    cancelBatch() {
      this.batchOpen = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        cardId: undefined,
        appId: undefined,
        cardName: undefined,
        cardNo: undefined,
        cardPass: undefined,
        quota: undefined,
        price: undefined,
        expireTime: undefined,
        isSold: "N",
        onSale: "Y",
        isCharged: "N",
        templateId: undefined,
        status: "0",
        chargeRule: "0",
        remark: undefined,
        cardLoginLimitU: -2,
        cardLoginLimitM: -2,
        chargeTime: undefined,
      };
      this.resetForm("form");
    },
    resetBatch() {
      this.formBatch = {
        cardId: undefined,
        appId: undefined,
        cardName: undefined,
        cardNo: undefined,
        cardPass: undefined,
        quota: undefined,
        price: undefined,
        expireTime: undefined,
        isSold: undefined,
        onSale: "Y",
        isCharged: undefined,
        templateId: undefined,
        status: undefined,
        chargeRule: undefined,
        remark: undefined,
        genQuantity: 1,
      };
      this.resetForm("formBatch");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.daterangeExpireTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.cardId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      if (this.app) {
        this.form.appId = this.app.appId;
      }
      this.open = true;
      this.title = "添加卡密";
    },
    /**批量制卡按钮操作 */
    handleBatchAdd() {
      this.resetBatch();
      let queryParams = {};
      if (this.app) {
        queryParams.appId = this.app.appId;
        this.formBatch.appId = this.app.appId;
      }
      listCardTemplateAll(queryParams).then((response) => {
        this.cardTemplateList = response.rows;
      });
      this.batchOpen = true;
      this.cardStr = "";
      this.cardSimpleStr = "";
      this.title = "批量制卡";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const cardId = row.cardId || this.ids;
      getCard(cardId).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改卡密";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.cardId != null) {
            updateCard(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            this.form.appId = this.app.appId;
            addCard(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 提交按钮 */
    submitFormBatch() {
      this.$refs["formBatch"].validate((valid) => {
        if (valid) {
          this.batchLoading = true;
          this.formBatch.appId = this.app.appId;
          addCard(this.formBatch).then((response) => {
            this.$modal.msgSuccess("新增成功");
            // this.batchOpen = false;
            if (
              response.data &&
              response.data instanceof Array &&
              response.data.length > 0
            ) {
              var content = "";
              var contentSimple = "";
              content +=
                "========" +
                response.cardName +
                "（共" +
                response.data.length +
                "张）========\n";
              contentSimple = content;
              for (var index = 0; index < response.data.length; index++) {
                var goods = response.data[index];
                if (goods) {
                  content += "第" + (parseInt(index) + 1) + "张\n";
                  content += "充值卡号：" + goods.cardNo + "\n";
                  content += "充值密码：" + goods.cardPass + "\n";
                  contentSimple += goods.cardNo + " " + goods.cardPass + "\n";
                  if (
                    goods.expireTime &&
                    goods.expireTime != "9999-12-31 23:59:59"
                  ) {
                    content +=
                      "充值过期（请在此时间前充值）：" +
                      (!goods.expireTime ||
                      goods.expireTime == "9999-12-31 23:59:59"
                        ? "长期有效"
                        : goods.expireTime) +
                      "\n";
                  }
                  if (goods.chargeRule && goods.chargeRule != "0") {
                    content +=
                      "充值规则：" +
                      this.chargeRuleFormat(goods.chargeRule) +
                      "\n";
                  }
                  content += "\n";
                }
              }
              this.cardStr = content;
              this.cardSimpleStr = contentSimple;
            }
            this.getList();
            this.batchLoading = false;
          });
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const cardIds = row.cardId || this.ids;
      this.$modal
        .confirm("是否确认删除数据项？")
        .then(function () {
          return delCard(cardIds);
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
      if (null != this.daterangeExpireTime && "" !== this.daterangeExpireTime) {
        queryParams.params["beginExpireTime"] = this.daterangeExpireTime[0];
        queryParams.params["endExpireTime"] = this.daterangeExpireTime[1];
      }
      if (null != this.queryParams.agentName && "" !== this.queryParams.agentName) {
        queryParams.params["agentName"] = this.queryParams.agentName;
      }
      if (this.app) {
        queryParams.appId = this.app.appId;
      }
      this.$modal
        .confirm("是否确认导出所有卡密数据项？")
        .then(() => {
          this.exportLoading = true;
          return exportCard(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
          this.exportLoading = false;
        })
        .catch(() => {
        });
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "卡密导入";
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      this.download(
        "system/card/importTemplate",
        {},
        `card_template_${new Date().getTime()}.xlsx`
      );
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert(
        "<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
        response.msg +
        "</div>",
        "导入结果",
        {dangerouslyUseHTMLString: true}
      );
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
    handleQuota(totalSeconds) {
      this.form.quota = totalSeconds;
    },
    parseSeconds(billType, seconds) {
      if (billType === "0") {
        let parse = parseSeconds(seconds);
        return parse[0] + parseUnit(parse[1]);
      } else {
        return seconds + "点";
      }
    },
    parseMoney(val) {
      return parseMoney(val);
    },
    getAppList() {
      this.loading = true;
      let queryParams = {};
      queryParams.params = {};
      queryParams.authType = "0";
      listAppAll(queryParams).then((response) => {
        this.appList = response.rows;
        for (let app of this.appList) {
          this.appMap[app["appId"]] = app;
        }
        this.loading = false;
      });
    },
    changeSelectedApp(appId) {
      this.cardTemplateList = [];
      this.formBatch.templateId = null;
      this.app = this.appMap[appId];
      let queryParams = {};
      if (this.app) {
        queryParams.appId = this.app.appId;
        listCardTemplateAll(queryParams).then((response) => {
          this.cardTemplateList = response.rows;
        });
      }
    },
    changeSelectedCard(templateId) {
      if (templateId) {
        getCardTemplate(templateId).then((response) => {
          this.formBatch.onSale = response.data.onSale;
        });
      }
    },
    changeSearchApp(appId) {
      this.loading = true;
      this.app = this.appMap[appId];
      this.getList();
      let queryParams = {};
      if (appId > 0) {
        queryParams = {
          appId: appId,
        };
      }
      listCardTemplateAll(queryParams).then((response) => {
        this.templateList = response.rows;
      });
      this.loading = false;
    },
    //在<table>⾥，我们已经设置row的key值设置为每⾏数据id：row-key="cardId"
    handleRowClick(row, column, event) {
      if (column.label == "操作") {
        return;
      }
      Array.prototype.remove = function (val) {
        let index = this.indexOf(val);
        if (index > -1) {
          this.splice(index, 1);
        }
      };
      if (this.expands.indexOf(row.cardId) < 0) {
        this.expands.push(row.cardId);
      } else {
        this.expands.remove(row.cardId);
      }
    },
    copy(id) {
      var clipboard = new Clipboard("#copyButton", {
        text: () => {
          // 如果想从其它DOM元素内容复制。应该是target:function(){return: };
          if (id == 1) {
            return this.cardStr;
          } else if (id == 2) {
            return this.cardSimpleStr;
          } else {
            return this.result;
          }
        },
      });
      clipboard.on("success", (e) => {
        this.$notify({
          title: "消息",
          dangerouslyUseHTMLString: true,
          message: "已成功复制，请您妥善保存",
          type: "success",
          offset: 100,
        });
        clipboard.destroy();
      });
      clipboard.on("error", (e) => {
        this.$notify({
          title: "消息",
          dangerouslyUseHTMLString: true,
          message: "复制失败，您的浏览器不支持复制，请自行妥善保存",
          type: "warning",
          offset: 100,
        });
        clipboard.destroy();
      });
    },
    downloadRecords(filename, text) {
      var pom = document.createElement("a");
      pom.setAttribute(
        "href",
        "data:text/plain;charset=utf-8," + encodeURIComponent(text)
      );
      pom.setAttribute("download", filename);
      if (document.createEvent) {
        var event = document.createEvent("MouseEvents");
        event.initEvent("click", true, true);
        pom.dispatchEvent(event);
      } else {
        pom.click();
      }
    },
    /** * 对Date的扩展，将 Date 转化为指定格式的String * 月(M)、日(d)、12小时(h)、24小时(H)、分(m)、秒(s)、周(E)、季度(q)
     可以用 1-2 个占位符 * 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) * eg: * (new
     Date()).pattern("yyyy-MM-dd hh:mm:ss.S")==> 2006-07-02 08:09:04.423
     * (new Date()).pattern("yyyy-MM-dd E HH:mm:ss") ==> 2009-03-10 二 20:09:04
     * (new Date()).pattern("yyyy-MM-dd EE hh:mm:ss") ==> 2009-03-10 周二 08:09:04
     * (new Date()).pattern("yyyy-MM-dd EEE hh:mm:ss") ==> 2009-03-10 星期二 08:09:04
     * (new Date()).pattern("yyyy-M-d h:m:s.S") ==> 2006-7-2 8:9:4.18
     */
    dateFormat(date, fmt) {
      var o = {
        "M+": date.getMonth() + 1, //月份
        "d+": date.getDate(), //日
        "h+": date.getHours() % 12 == 0 ? 12 : date.getHours() % 12, //小时
        "H+": date.getHours(), //小时
        "m+": date.getMinutes(), //分
        "s+": date.getSeconds(), //秒
        "q+": Math.floor((date.getMonth() + 3) / 3), //季度
        S: date.getMilliseconds(), //毫秒
      };
      var week = {
        0: "/u65e5",
        1: "/u4e00",
        2: "/u4e8c",
        3: "/u4e09",
        4: "/u56db",
        5: "/u4e94",
        6: "/u516d",
      };
      if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(
          RegExp.$1,
          (date.getFullYear() + "").substr(4 - RegExp.$1.length)
        );
      }
      if (/(E+)/.test(fmt)) {
        fmt = fmt.replace(
          RegExp.$1,
          (RegExp.$1.length > 1
            ? RegExp.$1.length > 2
              ? "/u661f/u671f"
              : "/u5468"
            : "") + week[date.getDay() + ""]
        );
      }
      for (var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
          fmt = fmt.replace(
            RegExp.$1,
            RegExp.$1.length == 1
              ? o[k]
              : ("00" + o[k]).substr(("" + o[k]).length)
          );
        }
      }
      return fmt;
    },
    save(id) {
      var content = "";
      if (id == 1) {
        content = this.cardStr;
      } else if(id == 2) {
        content = this.cardSimpleStr;
      } else {
        content = this.result;
      }
      this.downloadRecords(
        "save_" + this.dateFormat(new Date(), "yyyyMMddHHmmss") + ".txt",
        content
      );
    },
    // 状态修改
    handleStatusChange(row) {
      let text = row.status === "0" ? "启用" : "停用";
      this.$modal
        .confirm("确认要" + text + '"' + row.cardNo + '"吗？')
        .then(function () {
          return updateCard({'cardId': row.cardId, 'status': row.status});
        })
        .then(() => {
          this.$modal.msgSuccess(text + "成功");
        })
        .catch(function () {
          row.status = row.status === "0" ? "1" : "0";
        });
    },
    handleIsSoldChange(row) {
      let text = row.isSold === "Y" ? "售出" : "非售出";
      this.$modal
        .confirm("确认要将\"" + row.cardNo + "\"置为" + text + '吗？')
        .then(function () {
          return updateCard({'cardId': row.cardId, 'isSold': row.isSold});
        })
        .then(() => {
          this.$modal.msgSuccess(text + "成功");
        })
        .catch(function () {
          row.isSold = row.isSold === "Y" ? "N" : "Y";
        });
    },
    handleOnSaleChange(row) {
      let text = row.onSale === "Y" ? "上架" : "下架";
      this.$modal
        .confirm("确认要" + text + '"' + row.cardNo + '"吗？')
        .then(function () {
          return updateCard({'cardId': row.cardId, 'onSale': row.onSale});
        })
        .then(() => {
          this.$modal.msgSuccess(text + "成功");
        })
        .catch(function () {
          row.onSale = row.onSale === "Y" ? "N" : "Y";
        });
    },
    handleIsChargedChange(row) {
      let text = row.isCharged === "Y" ? "已使用" : "未使用";
      this.$modal
        .confirm("确认要将\"" + row.cardNo + "\"置为" + text + '吗？')
        .then(function () {
        return updateCard({'cardId': row.cardId, 'isCharged': row.isCharged});
      })
        .then(() => {
          this.$modal.msgSuccess(text + "成功");
        })
        .catch(function () {
          row.isCharged = row.isCharged === "Y" ? "N" : "Y";
        });
    },
    /**批量换卡按钮操作 */
    handleReplace() {
      this.resetReplace();
      // let queryParams = {};
      if (this.app) {
        // queryParams.appId = this.app.appId;
        this.formReplace.appId = this.app.appId;
      }
      // listLoginCodeTemplateAll(queryParams).then((response) => {
      //   this.cardTemplateList = response.rows;
      // });
      this.batchReplaceOpen = true;
    },
    resetReplace() {
      this.formReplace = {
        appId: 0,
        changeMode: 'Y',
      };
      this.resetForm("formReplace");
    },
    submitFormReplace() {
      this.$refs["formReplace"].validate((valid) => {
        if (valid) {
          this.batchLoading = true;
          this.formReplace.appId = this.app ? this.app.appId : 0;
          batchReplace(this.formReplace).then((response) => {
            this.result = response.msg;
            this.getList();
          }).catch(() => {
          }).finally(() => {
            this.batchLoading = false;
          });
        }
      })
    },
    cancelReplace() {
      this.batchReplaceOpen = false;
      this.resetReplace();
    },
    changeSelectedAppReplace(appId) {
      if(appId !== 0) {
        this.app = this.appMap[appId];
      } else {
        this.app = null;
      }
    },
  },
};
</script>
