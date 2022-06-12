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
      <el-form-item label="单码名称" prop="cardName">
        <el-input
          v-model="queryParams.cardName"
          clearable
          placeholder="请输入单码名称"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="单码" prop="cardNo">
        <el-input
          v-model="queryParams.cardNo"
          clearable
          placeholder="请输入单码"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="单码面值" prop="quota">
        <el-input
          v-model="queryParams.quota"
          placeholder="请输入单码面值"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="单码价格" prop="price">
        <el-input
          v-model="queryParams.price"
          placeholder="请输入单码价格"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="过期时间" prop="">
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
      <el-form-item label="是否售出" prop="isSold">
        <el-select
          v-model="queryParams.isSold"
          clearable
          placeholder="请选择是否售出"
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
      <el-form-item label="是否已用" prop="isCharged">
        <el-select
          v-model="queryParams.isCharged"
          clearable
          placeholder="请选择是否已用"
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
      <el-form-item label="单码状态" prop="status">
        <el-select
          v-model="queryParams.status"
          clearable
          placeholder="请选择单码状态"
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
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['agent:agentLoginCode:add']"
          icon="el-icon-plus"
          plain
          size="mini"
          type="primary"
          @click="handleBatchAdd"
        >批量生成
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['agent:agentLoginCode:edit']"
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
          v-hasPermi="['agent:agentLoginCode:remove']"
          :disabled="multiple"
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
          v-hasPermi="['agent:agentLoginCode:export']"
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
      :data="cardList"
      :expand-row-keys="expands"
      row-key="cardId"
      @row-click="handleRowClick"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="expand">
        <template slot-scope="scope">
          <el-form label-position="left">
            <el-form-item prop="">
              <el-col :span="6"> -</el-col>
              <el-col :span="6">
                <el-form-item label="单码" prop="">
                  <span>{{ scope.row.cardNo }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="面值" prop="">
                  <span>{{
                      parseSeconds(scope.row.app.billType, scope.row.quota)
                    }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="价格" prop="">
                  <span>{{ parseMoney(scope.row.price) }}元 </span>
                </el-form-item>
              </el-col>
            </el-form-item>
            <el-form-item prop="">
              <el-col :span="6"> -</el-col>
              <el-col :span="6">
                <el-form-item label="是否售出" prop="">
                  <dict-tag
                    :options="dict.type.sys_yes_no"
                    :value="scope.row.isSold"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="是否上架" prop="">
                  <dict-tag
                    :options="dict.type.sys_yes_no"
                    :value="scope.row.onSale"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="是否已用" prop="">
                  <dict-tag
                    :options="dict.type.sys_yes_no"
                    :value="scope.row.isCharged"
                  />
                </el-form-item>
              </el-col>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column align="center" type="selection" width="55"/>
      <el-table-column align="center" label="" type="index"/>
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
        label="单码名称"
        prop="cardName"
      />
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="单码"
        prop="cardNo"
      >
        <template slot-scope="scope">
          <span>{{ scope.row.cardNo }} </span>
        </template>
      </el-table-column>
      <!-- <el-table-column label="面值" align="center" prop="quota">
        <template slot-scope="scope">
          <span>{{ parseSeconds(scope.row.app.billType, scope.row.quota) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="价格" align="center" prop="price">
        <template slot-scope="scope">
          <span>{{ parseMoney(scope.row.price) }}元 </span>
        </template>
      </el-table-column> -->
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
      <el-table-column align="center" label="是否售出" prop="isSold">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_yes_no" :value="scope.row.isSold"/>
        </template>
      </el-table-column>
      <el-table-column align="center" label="是否上架" prop="onSale">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_yes_no" :value="scope.row.onSale"/>
        </template>
      </el-table-column>
      <el-table-column align="center" label="是否已用" prop="isCharged">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_yes_no"
            :value="scope.row.isCharged"
          />
        </template>
      </el-table-column>
      <el-table-column align="center" label="单码状态" prop="status">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_normal_disable"
            :value="scope.row.status"
          />
        </template>
      </el-table-column>
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="备注"
        prop="remark"
      />
      <el-table-column
        align="center"
        class-name="small-padding fixed-width"
        label="操作"
        width="130"
      >
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['agent:agentLoginCode:edit']"
            icon="el-icon-edit"
            size="mini"
            type="text"
            @click="handleUpdate(scope.row)"
          >修改
          </el-button>
          <el-button
            v-hasPermi="['agent:agentLoginCode:remove']"
            icon="el-icon-delete"
            size="mini"
            type="text"
            @click="handleDelete(scope.row)"
          >删除
          </el-button>
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

    <!-- 添加或修改单码对话框 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="open"
      append-to-body
      width="800px"
    >
      <el-form ref="form" :model="form" :rules="rules">
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
        <el-form-item label="单码名称" label-width="100px" prop="cardName">
          <!-- <el-input v-model="form.cardName" placeholder="请输入单码名称" /> -->
          {{ form.cardName }}
        </el-form-item>
        <el-form-item label="单码" label-width="80px" prop="cardNo">
          <!-- <el-input v-model="form.cardNo" placeholder="请输入单码" /> -->
          {{ form.cardNo }}
        </el-form-item>
        <el-form-item prop="">
          <el-col :span="12">
            <el-form-item label="单码状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in dict.type.sys_normal_disable"
                  :key="dict.value"
                  :label="dict.value"
                >{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="过期时间" prop="expireTime">
              <!-- <el-date-picker
                v-model="form.expireTime"
                :picker-options="pickerOptions"
                clearable
                placeholder="选择过期时间"
                size="small"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
              >
              </el-date-picker> -->
              {{ parseTime(form.expireTime) }}
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item prop="">
          <div v-if="form.cardId && form.app">
            <el-col :span="12">
              <el-form-item
                label="单码面值"
                label-width="100px"
                prop="quota"
                style="width: 320px"
              >
                {{ parseSeconds(form.app.billType, form.quota) }}
              </el-form-item>
            </el-col>
          </div>
          <el-col :span="12">
            <el-form-item label="销售价格" label-width="80px" prop="price">
              <el-input-number
                v-model="form.price"
                :min="0"
                :precision="2"
                :step="0.01"
                controls-position="right"
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
          </el-col>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form.remark"
            placeholder="请输入内容"
            type="textarea"
          />
        </el-form-item>
        <div v-if="form.cardId">
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

    <!-- 批量制单码对话框 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="batchOpen"
      append-to-body
      width="800px"
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
        <el-form-item label="选择单码类别" prop="templateId">
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
              label="生成数量"
              label-width="80px"
              prop="genQuantity"
            >
              <el-input-number
                v-model="formBatch.genQuantity"
                :max="10000"
                :min="1"
                controls-position="right"
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
            placeholder="请输入内容"
            type="textarea"
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
import {
  addLoginCode,
  delLoginCode,
  exportLoginCode,
  getLoginCode,
  listLoginCode,
  listLoginCodeTemplateAll,
  updateLoginCode,
} from "@/api/agent/agentLoginCode";
import {listAppAll} from "@/api/agent/agentApp";
import DateDuration from "@/components/DateDuration";
import Updown from "@/components/Updown";
import {parseMoney, parseSeconds, parseUnit} from "@/utils/my";

export default {
  name: "LoginCode",
  dicts: ["sys_yes_no", "sys_normal_disable", "sys_bill_type"],
  components: {DateDuration, Updown},
  data() {
    return {
      appList: [],
      appMap: [],
      // 类别数据
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
      // 单码表格数据
      cardList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示批量生成弹出层
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
      },
      // 表单参数
      form: {},
      // 表单参数
      formBatch: {},
      // 表单校验
      rules: {
        appId: [{required: true, message: "软件不能为空", trigger: "blur"}],
        cardName: [
          {required: true, message: "单码名称不能为空", trigger: "blur"},
        ],
        cardNo: [{required: true, message: "单码不能为空", trigger: "blur"}],
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
          {required: true, message: "是否被充值不能为空", trigger: "change"},
        ],
        status: [
          {required: true, message: "单码状态不能为空", trigger: "blur"},
        ],
      },
      rulesBatch: {
        appId: [{required: true, message: "软件不能为空", trigger: "blur"}],
        templateId: [
          {required: true, message: "类别不能为空", trigger: "change"},
        ],
        onSale: [
          {required: true, message: "是否上架不能为空", trigger: "change"},
        ],
        genQuantity: [
          {required: true, message: "生成数量不能为空", trigger: "blur"},
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
    this.getAppList();
    this.getList();
  },
  methods: {
    /** 查询单码列表 */
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
      listLoginCode(this.queryParams).then((response) => {
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
        quota: undefined,
        price: undefined,
        expireTime: undefined,
        isSold: "N",
        onSale: "Y",
        isCharged: "N",
        templateId: undefined,
        status: "0",
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
        quota: undefined,
        price: undefined,
        expireTime: undefined,
        isSold: undefined,
        onSale: "Y",
        isCharged: undefined,
        templateId: undefined,
        status: undefined,
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
    /**批量生成按钮操作 */
    handleBatchAdd() {
      this.resetBatch();
      let queryParams = {};
      if (this.app) {
        queryParams.appId = this.app.appId;
        this.formBatch.appId = this.app.appId;
      }
      listLoginCodeTemplateAll(queryParams).then((response) => {
        this.cardTemplateList = response.rows;
      });
      this.batchOpen = true;
      this.title = "批量生成单码";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const cardId = row.cardId || this.ids;
      getLoginCode(cardId).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改单码";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.cardId != null) {
            updateLoginCode(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            this.form.appId = this.app.appId;
            addLoginCode(this.form).then((response) => {
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
          addLoginCode(this.formBatch).then((response) => {
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
        .confirm('是否确认删除单码编号为"' + cardIds + '"的数据项？')
        .then(function () {
          return delLoginCode(cardIds);
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
        .confirm("是否确认导出所有单码数据项？")
        .then(() => {
          this.exportLoading = true;
          return exportLoginCode(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
          this.exportLoading = false;
        })
        .catch(() => {
        });
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
      this.cardTemplateList = [];
      this.formBatch.templateId = null;
      this.app = this.appMap[appId];
      let queryParams = {};
      if (this.app) {
        queryParams.appId = this.app.appId;
        listLoginCodeTemplateAll(queryParams).then((response) => {
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
