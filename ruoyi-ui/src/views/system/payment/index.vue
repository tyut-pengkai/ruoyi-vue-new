<template>
  <div class="app-container">
    <el-form
      v-show="showSearch"
      ref="queryForm"
      :inline="true"
      :model="queryParams"
      label-width="68px"
      size="small"
    >
      <el-form-item label="支付编码" prop="code">
        <el-input
          v-model="queryParams.code"
          clearable
          placeholder="请输入支付编码"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="支付名称" prop="name">
        <el-input
          v-model="queryParams.name"
          clearable
          placeholder="请输入支付名称"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="移动端" prop="mobile">
        <el-select
          v-model="queryParams.mobile"
          clearable
          placeholder="请选择移动端"
        >
          <el-option
            v-for="dict in dict.type.sys_yes_no"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="电脑端" prop="pc">
        <el-select
          v-model="queryParams.pc"
          clearable
          placeholder="请选择电脑端"
        >
          <el-option
            v-for="dict in dict.type.sys_yes_no"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item> -->
      <el-form-item label="状态" prop="status">
        <el-select
          v-model="queryParams.status"
          clearable
          placeholder="请选择状态"
        >
          <el-option
            v-for="dict in dict.type.sys_normal_disable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
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
          v-hasPermi="['system:payment:add']"
          icon="el-icon-plus"
          plain
          size="mini"
          type="primary"
          @click="handleAdd"
          >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:payment:edit']"
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
          v-hasPermi="['system:payment:remove']"
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
          v-hasPermi="['system:payment:export']"
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
      :data="paymentList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column align="center" type="selection" width="55" />
      <!-- <el-table-column align="center" label="" type="index"/> -->
      <el-table-column align="center" label="编号" prop="payId" />
      <el-table-column align="center" label="支付名称" prop="name" />
      <el-table-column align="center" label="支付编码" prop="code" />
      <el-table-column align="center" label="描述" prop="description" />
      <!-- <el-table-column align="center" label="移动端" prop="mobile">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_yes_no" :value="scope.row.mobile" />
        </template>
      </el-table-column>
      <el-table-column align="center" label="电脑端" prop="pc">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_yes_no" :value="scope.row.pc" />
        </template>
      </el-table-column> -->
      <el-table-column align="center" label="图标" prop="icon" />
      <!-- <el-table-column align="center" label="配置" prop="config"/> -->
      <el-table-column align="center" label="状态" prop="status">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_normal_disable"
            :value="scope.row.status"
          />
        </template>
      </el-table-column>
      <!-- <el-table-column align="center" label="创建者" prop="createBy"/>
      <el-table-column align="center" label="创建时间" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="更新者" prop="updateBy"/>
      <el-table-column align="center" label="更新时间" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column> -->
      <el-table-column align="center" label="备注" prop="remark" />
      <el-table-column
        align="center"
        class-name="small-padding fixed-width"
        label="操作"
      >
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['system:payment:edit']"
            icon="el-icon-edit"
            size="mini"
            type="text"
            @click="handleUpdate(scope.row)"
            >修改
          </el-button>
          <!-- <el-button
            v-hasPermi="['system:payment:remove']"
            icon="el-icon-delete"
            size="mini"
            type="text"
            @click="handleDelete(scope.row)"
          >删除
          </el-button> -->
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

    <!-- 添加或修改支付配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="800px">
      <el-form ref="form" :model="form" :rules="rules" style="margin: 0 30px">
        <el-form-item>
          <el-col :span="12">
            <el-form-item label="支付名称" label-width="80px" prop="name">
              <el-input v-model="form.name" placeholder="请输入支付名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="支付编码" label-width="80px" prop="code">
              <el-input
                v-model="form.code"
                :readonly="true"
                placeholder="请输入支付编码"
              />
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item label="描述" label-width="80px" prop="description">
          <el-input
            v-model="form.description"
            placeholder="请输入描述"
            type="textarea"
          />
        </el-form-item>
        <!-- <el-form-item>
          <el-col :span="12">
            <el-form-item label="移动端" label-width="80px" prop="mobile">
              <el-select v-model="form.mobile" placeholder="请选择移动端">
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
            <el-form-item label="电脑端" label-width="80px" prop="pc">
              <el-select v-model="form.pc" placeholder="请选择电脑端">
                <el-option
                  v-for="dict in dict.type.sys_yes_no"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-form-item> -->
        <el-form-item>
          <el-col :span="12">
            <el-form-item label="状态" label-width="80px" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态">
                <el-option
                  v-for="dict in dict.type.sys_normal_disable"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="图标" label-width="80px" prop="icon">
              <el-input
                v-model="form.icon"
                :readonly="true"
                placeholder="请输入图标"
              />
            </el-form-item>
          </el-col>
        </el-form-item>
        <!-- <el-form-item :label="key" v-for="(value, key, index) in form.configParams" :key="index" :prop="'configParams.' + key"
        :rules="{required: true, message: key + '不能为空', trigger: 'blur'}" label-width="150px"> -->
        <el-form-item
          v-for="(value, key, index) in form.configParams"
          :key="index"
          :label="key"
          :prop="'configParams.' + key"
          :rules="{
            required: false,
            message: key + '不能为空',
            trigger: 'blur',
          }"
          label-width="80px"
        >
          <el-input
            v-model="form.configParams[key]"
            :placeholder="'请输入' + key"
            type="textarea"
            @input="change($event)"
          />
        </el-form-item>
        <el-form-item v-if="!form.payId" label="配置" prop="config">
          <el-input v-model="form.config" placeholder="请输入配置" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form.remark"
            placeholder="请输入内容"
            type="textarea"
          />
        </el-form-item>
        <div v-if="form.payId">
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
  </div>
</template>

<script>
import {addPayment, delPayment, getPayment, listPayment, updatePayment,} from "@/api/system/payment";

export default {
  name: "Payment",
  dicts: ["sys_yes_no", "sys_normal_disable"],
  data() {
    return {
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
      // 支付配置表格数据
      paymentList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: this.$store.state.settings.pageSize,
        code: null,
        name: null,
        mobile: null,
        pc: null,
        status: null,
      },
      // 表单参数
      form: {
        configParams: {},
      },
      // 表单校验
      rules: {
        code: [
          { required: true, message: "支付编码不能为空", trigger: "blur" },
        ],
        name: [
          { required: true, message: "支付名称不能为空", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询支付配置列表 */
    getList() {
      this.loading = true;
      listPayment(this.queryParams).then((response) => {
        this.paymentList = response.rows;
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
        payId: null,
        code: null,
        name: null,
        description: null,
        mobile: null,
        pc: null,
        icon: null,
        status: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
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
      this.ids = selection.map((item) => item.payId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加支付配置";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const payId = row.payId || this.ids;
      getPayment(payId).then((response) => {
        this.form = response.data;
        this.form["configParams"] = {};
        if (this.form.config) {
          Object.assign(this.form.configParams, JSON.parse(this.form.config));
        }
        this.open = true;
        this.title = "修改支付配置";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.form.config = JSON.stringify(this.form.configParams);
          if (this.form.payId != null) {
            updatePayment(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPayment(this.form).then((response) => {
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
      const payIds = row.payId || this.ids;
      this.$modal
        .confirm('是否确认删除支付配置编号为"' + payIds + '"的数据项？')
        .then(function () {
          return delPayment(payIds);
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
        "system/payment/export",
        {
          ...this.queryParams,
        },
        `payment_${new Date().getTime()}.xlsx`
      );
    },
    change(e) {
      this.$forceUpdate();
    },
  },
};
</script>
