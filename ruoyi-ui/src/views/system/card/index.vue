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
      <el-form-item label="卡密名称" prop="cardName">
        <el-input
          v-model="queryParams.cardName"
          placeholder="请输入卡密名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
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
      <el-form-item label="过期时间" prop="">
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
      <el-form-item prop="">
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
      <el-table-column align="center" type="selection" width="55"/>
      <!-- <el-table-column label="" type="index" align="center" /> -->
      <el-table-column align="center" label="编号" prop="cardId"/>
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
      <el-table-column
        label="过期时间"
        align="center"
        prop="expireTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.expireTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="是否售出" prop="isSold">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_yes_no" :value="scope.row.isSold"/>
        </template>
      </el-table-column>
      <el-table-column label="是否上架" align="center" prop="onSale">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_yes_no" :value="scope.row.onSale" />
        </template>
      </el-table-column>
      <el-table-column label="是否已用" align="center" prop="isCharged">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_yes_no"
            :value="scope.row.isCharged"
          />
        </template>
      </el-table-column>
      <el-table-column label="卡密状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_normal_disable"
            :value="scope.row.status"
          />
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
          <el-input v-model="form.cardName" placeholder="请输入卡密名称" />
        </el-form-item>
        <el-form-item label="充值卡号" prop="cardNo" label-width="80px">
          <el-input v-model="form.cardNo" placeholder="请输入卡号" />
        </el-form-item>
        <el-form-item label="充值密码" prop="cardPass" label-width="80px">
          <el-input v-model="form.cardPass" placeholder="请输入密码" />
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
            <el-form-item label="过期时间" prop="expireTime">
              <el-date-picker
                clearable
                size="small"
                v-model="form.expireTime"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="选择过期时间"
                :picker-options="pickerOptions"
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
        <el-form-item prop="">
          <el-col :span="12">
            <el-form-item label="是否上架" prop="onSale">
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
            <el-form-item label="是否售出" prop="isSold">
              <el-select v-model="form.isSold" placeholder="请选择是否售出">
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
            <el-form-item label="充值规则" prop="chargeRule">
              <el-select v-model="form.chargeRule" placeholder="请选择充值规则">
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
          <el-select v-model="formBatch.templateId" placeholder="请选择">
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
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFormBatch">确 定</el-button>
        <el-button @click="cancelBatch">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {addCard, delCard, exportCard, getCard, listCard, updateCard,} from "@/api/system/card";
import {getApp, listAppAll} from "@/api/system/app";
import DateDuration from "@/components/DateDuration";
import Updown from "@/components/Updown";
import {parseMoney, parseSeconds, parseUnit} from "@/utils/my";
import {listCardTemplateAll} from "@/api/system/cardTemplate";

export default {
  name: "Card",
  dicts: [
    "sys_yes_no",
    "sys_normal_disable",
    "sys_charge_rule",
    "sys_bill_type",
  ],
  components: {DateDuration, Updown},
  data() {
    return {
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
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        appId: null,
        cardName: null,
        cardNo: null,
        quota: null,
        price: null,
        expireTime: null,
        isSold: null,
        onSale: null,
        isCharged: null,
        status: null,
        chargeRule: null,
      },
      // 表单参数
      form: {},
      // 表单参数
      formBatch: {},
      // 表单校验
      rules: {
        appId: [{ required: true, message: "软件不能为空", trigger: "blur" }],
        cardName: [
          { required: true, message: "卡名称不能为空", trigger: "blur" },
        ],
        cardNo: [{ required: true, message: "卡号不能为空", trigger: "blur" }],
        cardPass: [
          { required: true, message: "密码不能为空", trigger: "blur" },
        ],
        quota: [{ required: true, message: "额度不能为空", trigger: "blur" }],
        price: [{ required: true, message: "价格不能为空", trigger: "blur" }],
        expireTime: [
          { required: true, message: "过期时间不能为空", trigger: "blur" },
        ],
        isSold: [
          { required: true, message: "是否售出不能为空", trigger: "change" },
        ],
        onSale: [
          { required: true, message: "是否上架不能为空", trigger: "change" },
        ],
        isCharged: [
          { required: true, message: "是否已用不能为空", trigger: "change" },
        ],
        status: [
          { required: true, message: "卡密状态不能为空", trigger: "blur" },
        ],
        chargeRule: [
          { required: true, message: "充值规则不能为空", trigger: "change" },
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
            text: "1年后(360天)",
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() + 3600 * 1000 * 24 * 30 * 12);
              picker.$emit("pick", date);
            },
          },
        ],
      },
      expands: [],
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
  },
  methods: {
    /** 查询卡密列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeExpireTime && "" != this.daterangeExpireTime) {
        this.queryParams.params["beginExpireTime"] =
          this.daterangeExpireTime[0];
        this.queryParams.params["endExpireTime"] = this.daterangeExpireTime[1];
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
        genQuantity: 100,
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
          this.formBatch.appId = this.app.appId;
          addCard(this.formBatch).then((response) => {
            this.$modal.msgSuccess("新增成功");
            this.batchOpen = false;
            this.getList();
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
        .catch(() => {});
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
    changeSearchApp(appId) {
      this.loading = true;
      this.app = this.appMap[appId];
      this.getList();
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
  },
};
</script>
