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
      <el-form-item label="单码名称" prop="cardName">
        <el-input
          v-model="queryParams.cardName"
          placeholder="请输入单码名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="单码前缀" prop="cardNoPrefix">
        <el-input
          v-model="queryParams.cardNoPrefix"
          placeholder="请输入单码前缀"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="单码后缀" prop="cardNoSuffix">
        <el-input
          v-model="queryParams.cardNoSuffix"
          placeholder="请输入单码后缀"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="额度" prop="quota">
        <el-input
          v-model="queryParams.quota"
          placeholder="请输入额度"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="零售价格" prop="price">
        <el-input
          v-model="queryParams.price"
          placeholder="请输入价格"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="单码长度" prop="cardNoLen">
        <el-input
          v-model="queryParams.cardNoLen"
          placeholder="请输入单码长度"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="单码生成规则" prop="cardNoGenRule">
        <el-input
          v-model="queryParams.cardNoGenRule"
          placeholder="请输入单码生成规则"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="单码正则" prop="cardNoRegex">
        <el-input
          v-model="queryParams.cardNoRegex"
          placeholder="请输入单码正则"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <!-- <el-form-item label="充值规则" prop="chargeRule">
        <el-input
          v-model="queryParams.chargeRule"
          placeholder="请输入充值规则"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="是否上架" prop="onSale">
        <el-select
          v-model="queryParams.onSale"
          clearable
          placeholder="请选择是否上架"
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
      <!-- <el-form-item label="优先销售库存" prop="firstStock">
        <el-select v-model="queryParams.firstStock" placeholder="请选择是否优先销售库存" clearable size="small">
          <el-option
            v-for="dict in dict.type.sys_yes_no"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item> -->
      <!-- <el-form-item label="有效时长" prop="effectiveDuration">
        <el-input
          v-model="queryParams.effectiveDuration"
          placeholder="请输入有效时长"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <!-- <el-form-item label="类别状态" prop="status">
        <el-select
          v-model="queryParams.status"
          clearable
          placeholder="请选择单码类别状态"
          size="small"
        >
          <el-option
            v-for="dict in dict.type.sys_normal_disable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item> -->
      <el-form-item label="自动制卡" prop="enableAutoGen">
        <el-select
          v-model="queryParams.enableAutoGen"
          clearable
          placeholder="请选择是否允许自动制卡"
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
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:loginCodeTemplate:add']"
          >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAddRapid"
          v-hasPermi="['system:cardTemplate:add']"
          >快速新增</el-button
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
          v-hasPermi="['system:loginCodeTemplate:edit']"
          >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:loginCodeTemplate:remove']"
          >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:loginCodeTemplate:export']"
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
      :data="loginCodeTemplateList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column align="center" type="selection" width="55" />
      <!-- <el-table-column align="center" label="" type="index"/> -->
      <el-table-column align="center" label="编号" prop="templateId" />
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
        label="单码名称"
        align="center"
        prop="cardName"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          {{ scope.row.cardName }}
          <span v-if="scope.row.cardDescription">
            <el-tooltip :content="scope.row.cardDescription" placement="top">
              <i
                class="el-icon-info"
                style="margin-left: 0px; margin-right: 10px"
              ></i>
            </el-tooltip>
          </span>
        </template>
      </el-table-column>
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
      <!-- <el-table-column label="单码前缀" align="center" prop="cardNoPrefix" />
      <el-table-column label="单码后缀" align="center" prop="cardNoSuffix" />
      <el-table-column label="单码描述" align="center" prop="cardDescription" /> -->
<!--      <el-table-column label="是否上架" align="center" prop="onSale">-->
<!--        <template slot-scope="scope">-->
<!--          <dict-tag :options="dict.type.sys_yes_no" :value="scope.row.onSale"/>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <!-- <el-table-column label="优先库存" align="center" prop="firstStock">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_yes_no" :value="scope.row.firstStock"/>
        </template>
      </el-table-column> -->
<!--      <el-table-column label="自动制卡" align="center" prop="enableAutoGen">-->
<!--        <template slot-scope="scope">-->
<!--          <dict-tag-->
<!--            :options="dict.type.sys_yes_no"-->
<!--            :value="scope.row.enableAutoGen"-->
<!--          />-->
<!--        </template>-->
<!--      </el-table-column>-->
      <!-- <el-table-column align="center" label="类别状态" prop="status">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_normal_disable"
            :value="scope.row.status"
          />
        </template>
      </el-table-column> -->
      <el-table-column align="center" label="单码面值" prop="quota">
        <template slot-scope="scope">
          <span>{{
            parseSeconds(scope.row.app.billType, scope.row.quota)
          }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="零售价格" prop="price">
        <template slot-scope="scope">
          <span>{{ parseMoney(scope.row.price) }}元</span>
        </template>
      </el-table-column>
      <!-- <el-table-column label="单码长度" align="center" prop="cardNoLen" />
      <el-table-column label="单码生成规则" align="center" prop="cardNoGenRule" />
      <el-table-column label="单码正则" align="center" prop="cardNoRegex" /> -->
      <el-table-column label="有效期" align="center" prop="effectiveDuration">
        <template slot-scope="scope">
          <span>{{
            scope.row.effectiveDuration >= 0
              ? parseSeconds("0", scope.row.effectiveDuration)
              : "长期有效"
          }}</span>
        </template>
      </el-table-column>
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
      <el-table-column align="center" label="备注" prop="remark" />
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
      <el-table-column label="自动制卡" align="center">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.enableAutoGen"
            active-value="Y"
            inactive-value="N"
            @change="handleEnableAutoGenChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        class-name="small-padding fixed-width"
        label="操作"
        width="130"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:loginCodeTemplate:edit']"
            >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:loginCodeTemplate:remove']"
            >删除
          </el-button>
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

    <!-- 添加或修改单码类别对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="800px"
      append-to-body
      :close-on-click-modal="false"
    >
      <el-form ref="form" :model="form" :rules="rules">
        <!-- 新增 -->
        <div v-if="form.templateId == null">
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
        <div v-if="form.templateId && form.app">
          <el-form-item prop="">
            <el-col :span="12">
              <el-form-item label="所属软件" prop="appName">
                {{ form.app.appName }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="计费类型" prop="billType">
                <dict-tag
                  :options="dict.type.sys_bill_type"
                  :value="form.app.billType"
                />
              </el-form-item>
            </el-col>
          </el-form-item>
        </div>
        <el-form-item label="类别名称" prop="cardName" label-width="80px">
          <el-input v-model="form.cardName" placeholder="请输入类别名称" maxlength="50" show-word-limit/>
        </el-form-item>
        <el-form-item>
          <div v-if="form.templateId == null">
            <el-col :span="12">
              <el-form-item
                label="单码面值"
                prop="quota"
                label-width="80px"
                style="width: 320px"
              >
                <div v-if="app">
                  <div v-if="app.billType === '0'">
                    <date-duration
                      @totalSeconds="handleQuota"
                      :seconds="form.quota"
                    ></date-duration>
                  </div>
                  <div v-if="app.billType === '1'">
                    <el-input-number
                      v-model="form.quota"
                      controls-position="right"
                      :min="0"
                    />
                    <span style="margin-left: 6px">点</span>
                  </div>
                </div>
                <div v-else>请先选择软件</div>
              </el-form-item>
            </el-col>
          </div>
          <div v-if="form.templateId && form.app">
            <el-col :span="12">
              <el-form-item
                label="单码面值"
                prop="quota"
                label-width="80px"
                style="width: 320px"
              >
                <div v-if="form.app.billType === '0'">
                  <date-duration
                    @totalSeconds="handleQuota"
                    :seconds="form.quota"
                  ></date-duration>
                </div>
                <div v-if="form.app.billType === '1'">
                  <el-input-number
                    v-model="form.quota"
                    controls-position="right"
                    :min="0"
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
              />
              <span>元</span>
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item
          label="类别描述"
          prop="cardDescription"
          label-width="80px"
        >
          <el-input
            v-model="form.cardDescription"
            type="textarea"
            placeholder="请输入内容"
             maxlength="500" show-word-limit
          />
        </el-form-item>
        <el-divider></el-divider>
        <updown>
          <el-form-item label="购卡地址" label-width="80px" prop="shopUrl">
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
            <!-- <el-col :span="12">
              <el-form-item label="类别状态" label-width="80px">
                <el-radio-group v-model="form.status">
                  <el-radio
                    v-for="dict in dict.type.sys_normal_disable"
                    :key="dict.value"
                    :label="dict.value"
                    >{{ dict.label }}</el-radio
                  >
                </el-radio-group>
              </el-form-item>
            </el-col> -->
            <el-col :span="12">
              <el-form-item label="是否上架" prop="onSale" label-width="80px">
                <el-select v-model="form.onSale" placeholder="请选择是否上架">
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
              <el-form-item
                label="允许解绑"
                label-width="80px"
                prop="enableUnbind"
              >
                <el-select
                  v-model="form.enableUnbind"
                  placeholder="请选择是否允许解绑"
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
              <el-form-item
                label="单码前缀"
                prop="cardNoPrefix"
                label-width="80px"
                style="width: 300px"
              >
                <el-input
                  v-model="form.cardNoPrefix"
                  placeholder="请输入单码前缀"
                   maxlength="10" show-word-limit
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="单码后缀"
                prop="cardNoSuffix"
                label-width="80px"
                style="width: 300px"
              >
                <el-input
                  v-model="form.cardNoSuffix"
                  placeholder="请输入单码后缀"
                   maxlength="10" show-word-limit
                />
              </el-form-item>
            </el-col>
          </el-form-item>
          <el-form-item>
            <el-col :span="12">
              <el-form-item
                label="单码长度"
                prop="cardNoLen"
                label-width="80px"
              >
                <el-input-number
                  v-model="form.cardNoLen"
                  controls-position="right"
                  :min="10"
                  :max="64"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <!-- <el-form-item
                label="密码长度"
                prop="cardPassLen"
                label-width="80px"
              >
                <el-input-number
                  v-model="form.cardPassLen"
                  controls-position="right"
                  :min="0"
                  :max="64"
                />
              </el-form-item> -->
            </el-col>
          </el-form-item>
          <el-form-item>
            <el-col :span="12">
              <el-form-item label="单码生成规则" prop="cardNoGenRule">
                <el-select
                  v-model="form.cardNoGenRule"
                  placeholder="请选择单码生成规则"
                >
                  <el-option
                    v-for="dict in dict.type.sys_gen_rule"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="单码正则"
                prop="cardNoRegex"
                label-width="80px"
                style="width: 300px"
              >
                <el-input
                  v-model="form.cardNoRegex"
                  placeholder="请输入单码正则"
                  :disabled="form.cardNoGenRule !== '7'"
                  maxlength="50" show-word-limit
                />
              </el-form-item>
            </el-col>
          </el-form-item>
          <el-form-item>
            <el-col :span="12">
              <!-- <el-form-item label="优先销售库存" prop="firstStock">
                <el-select
                  v-model="form.firstStock"
                  placeholder="请选择优先库存"
                >
                  <el-option
                    v-for="dict in dict.type.sys_yes_no"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                  ></el-option>
                </el-select>
              </el-form-item> -->
              <el-form-item label="单码有效期" prop="effectiveDuration">
                <span>
                  <el-tooltip
                    content="单码有效期，即制卡后用户需要在多少时间内充值完毕，过期将无法充值，整数，-1为长期有效，默认为-1"
                    placement="top"
                  >
                    <i
                      class="el-icon-question"
                      style="margin-left: -12px; margin-right: 10px"
                    ></i>
                  </el-tooltip>
                </span>
                <date-duration
                  @totalSeconds="handleEffectiveDuration"
                  :seconds="form.effectiveDuration"
                  :min="-1"
                ></date-duration>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="自动制卡"
                prop="enableAutoGen"
                label-width="80px"
              >
                <el-select
                  v-model="form.enableAutoGen"
                  placeholder="请选择是否允许自动制卡"
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
                />
              </el-form-item>
            </el-col>
          </el-form-item>
          <el-form-item>
            <el-col :span="12">
              <el-form-item
                label="起拍张数"
                prop="minBuyNum"
                label-width="80px"
              >
                 <span>
                  <el-tooltip
                    content="一次性最少购买的数量，默认为1"
                    placement="top"
                  >
                    <i
                      class="el-icon-question"
                      style="margin-left: -12px; margin-right: 10px"
                    ></i>
                  </el-tooltip>
                </span>
                <el-input-number
                  v-model="form.minBuyNum"
                  controls-position="right"
                  :min="1"
                  :max="1000"
                />
              </el-form-item>
            </el-col>
          </el-form-item>
          <el-form-item>
            <el-col :span="12">
              <el-form-item
                label="允许换卡"
                prop="enableReplace"
                label-width="80px"
              >
                <el-select
                  v-model="form.enableReplace"
                  placeholder="请选择是否允许换卡"
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
              <el-form-item label="换卡额度限制" prop="replaceThreshold">
                <span>
                  <el-tooltip
                    content="换卡至少剩余额度，0不限制 -1未使用，默认为-1"
                    placement="top"
                  >
                    <i
                      class="el-icon-question"
                      style="margin-left: -12px; margin-right: 10px"
                    ></i>
                  </el-tooltip>
                </span>
                <el-input-number
                  v-model="form.replaceThreshold"
                  :min="-1"
                  controls-position="right"
                  :disabled="form.enableReplace === 'N'"
                />
              </el-form-item>
            </el-col>
          </el-form-item>
          <el-form-item label="单码自定义参数" prop="cardCustomParams">
            <el-input
              v-model="form.cardCustomParams"
              placeholder="请输入内容"
              type="textarea"
               maxlength="2000" show-word-limit
            />
          </el-form-item>
          <el-form-item label="备注" prop="remark">
            <el-input
              v-model="form.remark"
              type="textarea"
              placeholder="请输入内容"
            />
          </el-form-item>
          <div v-if="form.templateId">
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
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button
          v-show="!form.templateId"
          type="primary"
          @click="submitForm(1)"
        >继续添加
        </el-button
        >
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 快速添加类别管理对话框 -->
    <el-dialog
      title="快速新增"
      :visible.sync="openRapid"
      width="800px"
      append-to-body
      :close-on-click-modal="false"
    >
      <el-alert
        style="margin-bottom: 20px;"
        title="勾选要创建的类别，点击确定即可快速创建对应类别，如果软件下已存在同名类别，将跳过该类别不做处理"
        type="info"
        show-icon
        :closable="false">
      </el-alert>
      <el-form ref="formRapid" :model="formRapid" :rules="rulesRapid">
        <!-- 新增 -->
        <el-form-item prop="">
          <el-col :span="12">
            <el-form-item label="所属软件" prop="appId">
              <el-select
                v-model="formRapid.appId"
                filterable
                placeholder="请选择"
                prop="appId"
                @change="changeSelectedApp"
              >
                <el-option
                  v-for="item in appList"
                  :key="item.appId"
                  :label="'[' +
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
        <el-table ref="templateTable" :data="candidateTemplateListCompute" tooltip-effect="dark" style="width: 100%" max-height="300"
            :header-row-style="{ height: '30px' }" :header-cell-style="{ background: '#f5f7fa', padding: '0px' }"
            :row-style="{ height: '30px' }" :cell-style="{ padding: '0px' }" size='mini' border height="300"
            @selection-change="handleTemplateSelectionChange">
            <el-table-column type="selection" width="40" fixed> </el-table-column>
            <el-table-column label="单码类别" width="180" fixed show-overflow-tooltip>
              <template slot-scope="scope">
                {{ scope.row.title }}
              </template>
            </el-table-column>
            <el-table-column prop="remark" label="自定义名称" width="180">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.name" placeholder="请输入内容" style="width: 158px;" size="mini" maxlength="50" show-word-limit/>
                </template>
              </el-table-column>
            <el-table-column prop="price" label="零售价格" width="150">
              <template slot-scope="scope">
                <el-input-number v-model="scope.row.price" :min="0" :precision="2" :step="0.01"
                  controls-position="right" style="width: 120px" placeholder="零售价格" size="mini" />
                <!-- <span>元</span> -->
              </template>
            </el-table-column>
            <el-table-column prop="remark" label="备注信息" width="180">
              <template slot-scope="scope">
                <el-input v-model="scope.row.remark" placeholder="请输入内容" style="width: 158px;" size="mini" />
              </template>
            </el-table-column>
          </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFormRapid">确 定</el-button>
        <el-button @click="cancelRapid">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {
  addLoginCodeTemplate,
  delLoginCodeTemplate,
  getLoginCodeTemplate,
  listLoginCodeTemplate,
  updateLoginCodeTemplate,
  addLoginCodeTemplateRapid,
} from "@/api/system/loginCodeTemplate";
import {getApp, listAppAll} from "@/api/system/app";
import DateDuration from "@/components/DateDuration";
import Updown from "@/components/Updown";
import {parseMoney, parseSeconds, parseUnit} from "@/utils/my";
import Clipboard from 'clipboard'
import { randomString } from '@/api/common'

export default {
  name: "LoginCodeTemplate",
  dicts: ["sys_gen_rule", "sys_yes_no", "sys_normal_disable", "sys_bill_type"],
  components: { DateDuration, Updown },
  data() {
    return {
      appList: [],
      appMap: [],
      // 软件数据
      app: null,
      // 遮罩层
      loading: true,
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
      // 单码类别表格数据
      loginCodeTemplateList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      openRapid: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: this.$store.state.settings.pageSize,
        appId: null,
        cardName: null,
        onSale: null,
        firstStock: null,
        status: null,
        enableAutoGen: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        appId: [{ required: true, message: "软件不能为空", trigger: "blur" }],
        cardName: [
          { required: true, message: "类别名称不能为空", trigger: "blur" },
        ],
        quota: [{ required: true, message: "额度不能为空", trigger: "blur" }],
        price: [{ required: true, message: "价格不能为空", trigger: "blur" }],
        cardNoLen: [
          { required: true, message: "单码长度不能为空", trigger: "blur" },
        ],
        cardNoGenRule: [
          { required: true, message: "单码生成规则不能为空", trigger: "blur" },
        ],
        cardNoRegex: [
          { required: false, message: "单码正则不能为空", trigger: "blur" },
        ],
        onSale: [
          {required: true, message: "是否上架不能为空", trigger: "change"},
        ],
        firstStock: [
          {required: true, message: "优先库存不能为空", trigger: "change"},
        ],
        effectiveDuration: [
          {required: true, message: "有效时长不能为空", trigger: "blur"},
        ],
        // status: [
        //   { required: true, message: "单码类别状态不能为空", trigger: "blur" },
        // ],
        enableAutoGen: [
          {
            required: true,
            message: "允许自动生成不能为空",
            trigger: "change",
          },
        ],
        cardLoginLimitU: [
          {
            required: true,
            message:
              "由充值卡/单码生效的登录用户数量限制不能为空",
            trigger: "blur",
          },
        ],
        cardLoginLimitM: [
          {
            required: true,
            message:
              "由充值卡/单码生效的登录设备数量限制不能为空",
            trigger: "blur",
          },
        ],
        enableUnbind: [
          {
            required: true,
            message: "是否允许解绑不能为空",
            trigger: "change",
          },
        ],
        minBuyNum: [
          {
            required: true,
            message: "起拍张数不能为空",
            trigger: "blur",
          },
        ],
        enableReplace: [
          {
            required: true,
            message: "是否允许换卡不能为空",
            trigger: "change",
          },
        ],
        replaceThreshold: [
          {
            required: true,
            message:
              "换卡至少剩余额度不能为空",
            trigger: "blur",
          },
        ],
      },
      rulesRapid: {
        appId: [{ required: true, message: "软件不能为空", trigger: "blur" }],
      },
      // TemplateRapid
      formRapid: {},
      templateSelectionList: [],
      candidateTemplateList: [{
        'id': 1,
        'title': '日卡(1天)',
        'name': '日卡(1天)',
        'price': 10,
        'remark': '',
      },
      {
        'id': 2,
        'title': '周卡(7天)',
        'name': '周卡(7天)',
        'price': 70,
        'remark': '',
      }, {
        'id': 3,
        'title': '月卡(30天)',
        'name': '月卡(30天)',
        'price': 300,
        'remark': '',
      }, {
        'id': 4,
        'title': '季卡(90天)',
        'name': '季卡(90天)',
        'price': 900,
        'remark': '',
      }, {
        'id': 5,
        'title': '半年卡(180天)',
        'name': '半年卡(180天)',
        'price': 1800,
        'remark': '',
      }, {
        'id': 6,
        'title': '年卡(365天)',
        'name': '年卡(365天)',
        'price': 3650,
        'remark': '',
      }, {
        'id': 7,
        'title': '永久卡(3650天)',
        'name': '永久卡(3650天)',
        'price': 36500,
        'remark': '',
      }],
    };
  },
  created() {
    // const appId = this.$route.params && this.$route.params.appId;
    const appId = this.$route.query && this.$route.query.appId;
    this.getAppList();
    if (appId != undefined && appId != null) {
      getApp(appId).then((response) => {
        this.app = response.data;
        // const title = "单码类别管理";
        // const appName = this.app.appName;
        // const route = Object.assign({}, this.$route, {
        //   title: `${title}-${appName}`,
        // });
        // this.$store.dispatch("tagsView/updateVisitedView", route);
        this.getList();
      });
    } else {
      this.getList();
      // this.$modal.alertError("未获取到当前软件信息");
    }
  },
  methods: {
    /** 查询单码类别列表 */
    getList() {
      this.loading = true;
      if (this.app) {
        this.queryParams.appId = this.app.appId;
      }
      listLoginCodeTemplate(this.queryParams).then((response) => {
        this.loginCodeTemplateList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    cancelRapid() {
      this.openRapid = false;
      this.templateSelectionList = [];
    },
    // 表单重置
    reset() {
      this.form = {
        templateId: undefined,
        appId: undefined,
        cardName: undefined,
        cardNoPrefix: undefined,
        cardNoSuffix: undefined,
        cardDescription: undefined,
        quota: 0,
        price: undefined,
        cardNoLen: 20,
        cardNoGenRule: "0",
        cardNoRegex: undefined,
        onSale: "Y",
        firstStock: "Y",
        effectiveDuration: -1,
        status: "0",
        enableAutoGen: "N",
        remark: undefined,
        cardLoginLimitU: -2,
        cardLoginLimitM: -2,
        enableUnbind: "Y",
        minBuyNum: 1,
        enableReplace: "N",
        replaceThreshold: "-1",
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
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.templateId);
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
      this.title = "添加单码类别";
    },
    /** 快速新增按钮操作 */
    handleAddRapid() {
      this.templateSelectionList = [];
      if (this.app) {
        this.form.appId = this.app.appId;
      }
      this.openRapid = true;
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const templateId = row.templateId || this.ids;
      getLoginCodeTemplate(templateId).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改单码类别";
      });
    },
    /** 提交按钮 */
    submitForm(type) {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.templateId != null) {
            updateLoginCodeTemplate(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            this.form.appId = this.app.appId;
            addLoginCodeTemplate(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              if (type != 1) {
                this.open = false;
              }
              this.getList();
            });
          }
        }
      });
    },
    /** 快速新增提交按钮 */
    submitFormRapid() {
      this.$refs["formRapid"].validate((valid) => {
        if (valid) {
          this.formRapid.appId = this.app.appId;
          this.formRapid.templateSelectionList = this.templateSelectionList;
          addLoginCodeTemplateRapid(this.formRapid).then((response) => {
            this.$modal.msgSuccess("新增成功");
            this.openRapid = false;
            this.getList();
          });
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const templateIds = row.templateId || this.ids;
      this.$modal
        .confirm('是否确认删除单码类别编号为"' + templateIds + '"的数据项？')
        .then(function () {
          return delLoginCodeTemplate(templateIds);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "system/loginCodeTemplate/export",
        {
          ...this.queryParams,
        },
        `loginCodeTemplate_${new Date().getTime()}.xlsx`
      );
    },
    handleQuota(totalSeconds) {
      this.form.quota = totalSeconds;
    },
    handleEffectiveDuration(effectiveDuration) {
      this.form.effectiveDuration = effectiveDuration;
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
      queryParams.authType = "1";
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
    },
    changeSearchApp(appId) {
      this.loading = true;
      this.app = this.appMap[appId];
      this.getList();
      this.loading = false;
    },
    handleTemplateSelectionChange(val) {
      this.templateSelectionList = val;
    },
    // 状态修改
    handleOnSaleChange(row) {
      let text = row.status === "Y" ? "上架" : "下架";
      this.$modal
        .confirm("确认要" + text + '"[' + row.app.appName + ']' + row.cardName + '"吗？')
        .then(function () {
          return updateLoginCodeTemplate({'templateId': row.templateId, 'onSale': row.onSale});
        })
        .then(() => {
          this.$modal.msgSuccess(text + "成功");
        })
        .catch(function () {
          row.onSale = row.onSale === "Y" ? "N" : "Y";
        });
    },
    handleEnableAutoGenChange(row) {
      let text = row.enableAutoGen === "Y" ? "开启" : "关闭";
      this.$modal
        .confirm("确认要" + text + '"[' + row.app.appName + ']' + row.cardName + '"的自动制卡吗？')
        .then(function () {
          return updateLoginCodeTemplate({'templateId': row.templateId, 'enableAutoGen': row.enableAutoGen});
        })
        .then(() => {
          this.$modal.msgSuccess(text + "成功");
        })
        .catch(function () {
          row.enableAutoGen = row.enableAutoGen === "Y" ? "N" : "Y";
        });
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
          if(!this.form[index]) {
            this.$modal.msgError("请先设置卡类链接");
            return;
          }
          return this.getShopUrlPrefix() + this.form[index];
        },
      });
      clipboard.on("success", (e) => {
        this.$modal.msgSuccess("已成功复制到剪贴板");
        clipboard.destroy();
      });
      clipboard.on("error", (e) => {
        this.$modal.msgError(
          "复制失败，您的浏览器不支持复制，请自行复制地址"
        );
        clipboard.destroy();
      });
    },
    getShopUrlPrefix() {
      let domain = this.$store.state.settings.domain;
      return domain + (domain.endsWith('/') ? "" : "/") + "shop/c/";
    }
  },
  computed: {
    candidateTemplateListCompute() {
      if (this.app && this.app.billType === '0') {
        return this.candidateTemplateList;
      } else {
        return [];
      }
    },
  }
};
</script>
