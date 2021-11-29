<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
    >
      <el-form-item label="卡名称" prop="cardName">
        <el-input
          v-model="queryParams.cardName"
          placeholder="请输入卡名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
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
      <el-form-item label="优先库存" prop="firstStock">
        <el-select
          v-model="queryParams.firstStock"
          placeholder="请选择优先库存"
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
      <el-form-item label="模板状态" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="请选择模板状态"
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
      <el-form-item label="允许自动生成" prop="enableAutoGen">
        <el-select
          v-model="queryParams.enableAutoGen"
          placeholder="请选择允许自动生成"
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
      <el-form-item label="创建者" prop="createBy">
        <el-input
          v-model="queryParams.createBy"
          placeholder="请输入创建者"
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
          v-hasPermi="['system:cardTemplate:add']"
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
          v-hasPermi="['system:cardTemplate:edit']"
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
          v-hasPermi="['system:cardTemplate:remove']"
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
          v-hasPermi="['system:cardTemplate:export']"
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
      :data="cardTemplateList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="模板ID" align="center" prop="templateId" />
      <el-table-column label="软件ID" align="center" prop="appId" />
      <el-table-column label="卡名称" align="center" prop="cardName" />
      <el-table-column label="卡描述" align="center" prop="cardDescription" />
      <el-table-column label="面值" align="center" prop="quota" />
      <el-table-column label="价格" align="center" prop="price" />
      <el-table-column label="卡号长度" align="center" prop="cardNoLen" />
      <el-table-column label="卡号生成规则" align="center" prop="cardNoGenRule">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_gen_rule"
            :value="scope.row.cardNoGenRule"
          />
        </template>
      </el-table-column>
      <el-table-column label="卡号正则" align="center" prop="cardNoRegex" />
      <el-table-column label="密码长度" align="center" prop="cardPassLen" />
      <el-table-column
        label="密码生成规则"
        align="center"
        prop="cardPassGenRule"
      >
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_gen_rule"
            :value="scope.row.cardPassGenRule"
          />
        </template>
      </el-table-column>
      <el-table-column label="密码正则" align="center" prop="cardPassRegex" />
      <el-table-column label="充值规则" align="center" prop="chargeRule">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_charge_rule"
            :value="scope.row.chargeRule"
          />
        </template>
      </el-table-column>
      <el-table-column label="是否上架" align="center" prop="onSale">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_yes_no" :value="scope.row.onSale" />
        </template>
      </el-table-column>
      <el-table-column label="优先库存" align="center" prop="firstStock">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_yes_no"
            :value="scope.row.firstStock"
          />
        </template>
      </el-table-column>
      <el-table-column
        label="有效时长"
        align="center"
        prop="effectiveDuration"
      />
      <el-table-column label="模板状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_normal_disable"
            :value="scope.row.status"
          />
        </template>
      </el-table-column>
      <el-table-column label="允许自动生成" align="center" prop="enableAutoGen">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_yes_no"
            :value="scope.row.enableAutoGen"
          />
        </template>
      </el-table-column>
      <el-table-column label="创建者" align="center" prop="createBy" />
      <el-table-column label="创建时间" align="center" prop="createTime" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:cardTemplate:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:cardTemplate:remove']"
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

    <!-- 添加或修改卡类管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules">
        <div v-if="app">
          <el-form-item>
            <el-col :span="12">
              <el-form-item label="所属软件" label-width="80px">
                {{ this.app.appName }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="计费类型" label-width="80px">
                <dict-tag
                  :options="dict.type.sys_bill_type"
                  :value="app.billType"
                />
              </el-form-item>
            </el-col>
          </el-form-item>
        </div>
        <el-form-item label="模板名称" prop="cardName" label-width="80px">
          <el-input v-model="form.cardName" placeholder="请输入卡名称" />
        </el-form-item>
        <el-form-item>
          <el-col :span="12">
            <el-form-item
              label="卡密面值"
              prop="quota"
              label-width="80px"
              style="width: 300px"
            >
              <!-- <el-input v-model="form.quota" placeholder="请输入面值" /> -->
              <date-duration @totalSeconds="handleQuota"></date-duration>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="销售价格" prop="price" label-width="80px">
              <el-input-number
                v-model="form.price"
                controls-position="right"
                :precision="2"
                :step="0.01"
                :min="0"
              />
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item
          label="模板描述"
          prop="cardDescription"
          label-width="80px"
        >
          <el-input
            v-model="form.cardDescription"
            type="textarea"
            placeholder="请输入内容"
          />
        </el-form-item>
        <el-divider></el-divider>
        <updown>
          <el-form-item>
            <el-col :span="12">
              <el-form-item label="模板状态" label-width="80px">
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
          </el-form-item>
          <el-form-item>
            <el-col :span="12">
              <el-form-item
                label="卡号前缀"
                prop="cardNoPrefix"
                label-width="80px"
                style="width: 300px"
              >
                <el-input
                  v-model="form.cardNoPrefix"
                  placeholder="请输入卡号前缀"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="卡号后缀"
                prop="cardNoSuffix"
                label-width="80px"
                style="width: 300px"
              >
                <el-input
                  v-model="form.cardNoSuffix"
                  placeholder="请输入卡号后缀"
                />
              </el-form-item>
            </el-col>
          </el-form-item>
          <el-form-item>
            <el-col :span="12">
              <el-form-item
                label="卡号长度"
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
              <el-form-item
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
              </el-form-item>
            </el-col>
          </el-form-item>
          <el-form-item>
            <el-col :span="12">
              <el-form-item label="卡号生成规则" prop="cardNoGenRule">
                <el-select
                  v-model="form.cardNoGenRule"
                  placeholder="请选择卡号生成规则"
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
                label="卡号正则"
                prop="cardNoRegex"
                label-width="80px"
                style="width: 300px"
              >
                <el-input
                  v-model="form.cardNoRegex"
                  placeholder="请输入卡号正则"
                  :disabled="form.cardNoGenRule !== '7'"
                />
              </el-form-item>
            </el-col>
          </el-form-item>
          <el-form-item>
            <el-col :span="12">
              <el-form-item label="密码生成规则" prop="cardPassGenRule">
                <el-select
                  v-model="form.cardPassGenRule"
                  placeholder="请选择密码生成规则"
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
                label="密码正则"
                prop="cardPassRegex"
                label-width="80px"
                style="width: 300px"
              >
                <el-input
                  v-model="form.cardPassRegex"
                  placeholder="请输入密码正则"
                  :disabled="form.cardPwdGenRule !== '7'"
                />
              </el-form-item>
            </el-col>
          </el-form-item>
          <el-form-item>
            <el-col :span="12">
              <el-form-item label="优先销售库存" prop="firstStock">
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
          <el-form-item>
            <el-col :span="12">
              <el-form-item label="卡密有效期" prop="effectiveDuration">
                <el-input-number
                  v-model="form.effectiveDuration"
                  controls-position="right"
                  :min="-1"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="充值规则"
                prop="chargeRule"
                label-width="80px"
              >
                <el-select
                  v-model="form.chargeRule"
                  placeholder="请选择充值规则"
                >
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
        </updown>
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
  addCardTemplate,
  delCardTemplate,
  exportCardTemplate,
  getCardTemplate,
  listCardTemplate,
  updateCardTemplate,
} from "@/api/system/cardTemplate";
import {getApp} from "@/api/system/app";
import DateDuration from "@/components/DateDuration";
import Updown from "@/components/Updown";

export default {
  name: "CardTemplate",
  dicts: [
    "sys_gen_rule",
    "sys_gen_rule",
    "sys_charge_rule",
    "sys_yes_no",
    "sys_yes_no",
    "sys_normal_disable",
    "sys_yes_no",
    "sys_bill_type",
  ],
  components: { DateDuration, Updown },
  data() {
    return {
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
      // 卡类管理表格数据
      cardTemplateList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        appId: null,
        cardName: null,
        templateName: null,
        chargeRule: null,
        onSale: null,
        firstStock: null,
        status: null,
        enableAutoGen: null,
        createBy: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        cardName: [
          { required: true, message: "卡名称不能为空", trigger: "blur" },
        ],
        quota: [{ required: true, message: "额度不能为空", trigger: "blur" }],
        price: [{ required: true, message: "价格不能为空", trigger: "blur" }],
        cardNoLen: [
          { required: true, message: "卡号长度不能为空", trigger: "blur" },
        ],
        cardNoGenRule: [
          {
            required: true,
            message: "卡号生成规则不能为空",
            trigger: "change",
          },
        ],
        cardNoRegex: [
          { required: false, message: "卡号正则不能为空", trigger: "blur" },
        ],
        cardPassLen: [
          { required: true, message: "密码长度不能为空", trigger: "blur" },
        ],
        cardPassGenRule: [
          {
            required: true,
            message: "密码生成规则不能为空",
            trigger: "change",
          },
        ],
        cardPassRegex: [
          { required: false, message: "密码正则不能为空", trigger: "blur" },
        ],
        chargeRule: [
          { required: true, message: "充值规则不能为空", trigger: "change" },
        ],
        onSale: [
          { required: true, message: "是否上架不能为空", trigger: "change" },
        ],
        firstStock: [
          { required: true, message: "优先库存不能为空", trigger: "change" },
        ],
        status: [
          { required: true, message: "模板状态不能为空", trigger: "blur" },
        ],
        effectiveDuration: [
          { required: true, message: "有效时长不能为空", trigger: "blur" },
        ],
        enableAutoGen: [
          {
            required: true,
            message: "允许自动生成不能为空",
            trigger: "change",
          },
        ],
      },
    };
  },
  created() {
    const appId = this.$route.params && this.$route.params.appId;
    if (appId != undefined && appId != null) {
      getApp(appId).then((response) => {
        this.app = response.data;
        const title = "卡类管理";
        const appName = this.app.appName;
        const route = Object.assign({}, this.$route, {
          title: `${title}-${appName}`,
        });
        this.$store.dispatch("tagsView/updateVisitedView", route);
        this.getList();
      });
    } else {
      this.$modal.alertError("未获取到当前软件信息");
    }
  },
  methods: {
    /** 查询卡类列表 */
    getList() {
      this.loading = true;
      listCardTemplate(this.queryParams).then((response) => {
        this.cardTemplateList = response.rows;
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
        templateId: undefined,
        appId: this.app.appId,
        cardName: undefined,
        cardNoPrefix: undefined,
        cardNoSuffix: undefined,
        cardDescription: undefined,
        quota: undefined,
        price: undefined,
        cardNoLen: 20,
        cardNoGenRule: "0",
        cardNoRegex: undefined,
        cardPassLen: 20,
        cardPassGenRule: "0",
        cardPassRegex: undefined,
        chargeRule: "0",
        onSale: "Y",
        firstStock: "Y",
        effectiveDuration: -1,
        status: "0",
        enableAutoGen: "N",
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
      this.open = true;
      this.title = "添加卡类";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const templateId = row.templateId || this.ids;
      getCardTemplate(templateId).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改卡类";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.templateId != null) {
            updateCardTemplate(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            this.form.appId = this.app.appId;
            addCardTemplate(this.form).then((response) => {
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
      const templateIds = row.templateId || this.ids;
      this.$modal
        .confirm('是否确认删除卡类编号为"' + templateIds + '"的数据项？')
        .then(function () {
          return delCardTemplate(templateIds);
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
        .confirm("是否确认导出所有卡类数据项？")
        .then(() => {
          this.exportLoading = true;
          return exportCardTemplate(queryParams);
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
  },
};
</script>
