<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :inline="true" :model="queryParams" label-width="68px">
      <el-form-item label="订单详情ID" prop="itemId">
        <el-input
          v-model="queryParams.itemId"
          clearable
          placeholder="请输入订单详情ID"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="卡密ID" prop="cardId">
        <el-input
          v-model="queryParams.cardId"
          clearable
          placeholder="请输入卡密ID"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-search" size="mini" type="primary" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['sale:saleCard:add']"
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
          v-hasPermi="['sale:saleCard:edit']"
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
          v-hasPermi="['sale:saleCard:remove']"
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
          v-hasPermi="['sale:saleCard:export']"
          icon="el-icon-download"
          plain
          size="mini"
          type="warning"
          @click="handleExport"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="saleCardList" @selection-change="handleSelectionChange">
      <el-table-column align="center" type="selection" width="55"/>
      <el-table-column align="center" label="主键ID" prop="id"/>
      <el-table-column align="center" label="订单详情ID" prop="itemId"/>
      <el-table-column align="center" label="卡密ID" prop="cardId"/>
      <el-table-column align="center" class-name="small-padding fixed-width" label="操作">
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['sale:saleCard:edit']"
            icon="el-icon-edit"
            size="mini"
            type="text"
            @click="handleUpdate(scope.row)"
          >修改
          </el-button>
          <el-button
            v-hasPermi="['sale:saleCard:remove']"
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
      v-show="total>0"
      :limit.sync="queryParams.pageSize"
      :page.sync="queryParams.pageNum"
      :total="total"
      @pagination="getList"
    />

    <!-- 添加或修改订单卡密对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="订单详情ID" prop="itemId">
          <el-input v-model="form.itemId" placeholder="请输入订单详情ID"/>
        </el-form-item>
        <el-form-item label="卡密ID" prop="cardId">
          <el-input v-model="form.cardId" placeholder="请输入卡密ID"/>
        </el-form-item>
        <el-divider content-position="center">卡密信息</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button icon="el-icon-plus" size="mini" type="primary" @click="handleAddSysCard">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button icon="el-icon-delete" size="mini" type="danger" @click="handleDeleteSysCard">删除</el-button>
          </el-col>
        </el-row>
        <el-table ref="sysCard" :data="sysCardList" :row-class-name="rowSysCardIndex"
                  @selection-change="handleSysCardSelectionChange">
          <el-table-column align="center" type="selection" width="50"/>
          <el-table-column align="center" label="序号" prop="index" width="50"/>
          <el-table-column label="软件ID" prop="appId" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.appId" placeholder="请输入软件ID"/>
            </template>
          </el-table-column>
          <el-table-column label="卡名称" prop="cardName" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.cardName" placeholder="请输入卡名称"/>
            </template>
          </el-table-column>
          <el-table-column label="卡号" prop="cardNo" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.cardNo" placeholder="请输入卡号"/>
            </template>
          </el-table-column>
          <el-table-column label="密码" prop="cardPass" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.cardPass" placeholder="请输入密码"/>
            </template>
          </el-table-column>
          <el-table-column label="额度" prop="quota" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.quota" placeholder="请输入额度"/>
            </template>
          </el-table-column>
          <el-table-column label="价格" prop="price" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.price" placeholder="请输入价格"/>
            </template>
          </el-table-column>
          <el-table-column label="过期时间" prop="expireTime" width="240">
            <template slot-scope="scope">
              <el-date-picker v-model="scope.row.expireTime" clearable placeholder="请选择过期时间" type="date"
                              value-format="yyyy-MM-dd"/>
            </template>
          </el-table-column>
          <el-table-column label="是否售出" prop="isSold" width="150">
            <template slot-scope="scope">
              <el-select v-model="scope.row.isSold" placeholder="请选择是否售出">
                <el-option
                  v-for="dict in dict.type.sys_yes_no"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="是否上架" prop="onSale" width="150">
            <template slot-scope="scope">
              <el-select v-model="scope.row.onSale" placeholder="请选择是否上架">
                <el-option
                  v-for="dict in dict.type.sys_yes_no"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="是否被充值" prop="isCharged" width="150">
            <template slot-scope="scope">
              <el-select v-model="scope.row.isCharged" placeholder="请选择是否被充值">
                <el-option
                  v-for="dict in dict.type.sys_yes_no"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="卡密状态" prop="status" width="150">
            <template slot-scope="scope">
              <el-select v-model="scope.row.status" placeholder="请选择卡密状态">
                <el-option
                  v-for="dict in dict.type.sys_normal_disable"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="充值规则" prop="chargeRule" width="150">
            <template slot-scope="scope">
              <el-select v-model="scope.row.chargeRule" placeholder="请选择充值规则">
                <el-option
                  v-for="dict in dict.type.sys_charge_rule"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {addSaleCard, delSaleCard, getSaleCard, listSaleCard, updateSaleCard} from "@/api/sale/saleCard";

export default {
  name: "SaleCard",
  dicts: ['sys_charge_rule', 'sys_yes_no', 'sys_normal_disable'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedSysCard: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 订单卡密表格数据
      saleCardList: [],
      // 卡密表格数据
      sysCardList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        itemId: null,
        cardId: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {}
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询订单卡密列表 */
    getList() {
      this.loading = true;
      listSaleCard(this.queryParams).then(response => {
        this.saleCardList = response.rows;
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
        id: null,
        itemId: null,
        cardId: null
      };
      this.sysCardList = [];
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加订单卡密";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getSaleCard(id).then(response => {
        this.form = response.data;
        this.sysCardList = response.data.sysCardList;
        this.open = true;
        this.title = "修改订单卡密";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.sysCardList = this.sysCardList;
          if (this.form.id != null) {
            updateSaleCard(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addSaleCard(this.form).then(response => {
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
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除订单卡密编号为"' + ids + '"的数据项？').then(function () {
        return delSaleCard(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 卡密序号 */
    rowSysCardIndex({row, rowIndex}) {
      row.index = rowIndex + 1;
    },
    /** 卡密添加按钮操作 */
    handleAddSysCard() {
      let obj = {};
      obj.appId = "";
      obj.cardName = "";
      obj.cardNo = "";
      obj.cardPass = "";
      obj.quota = "";
      obj.price = "";
      obj.expireTime = "";
      obj.isSold = "";
      obj.onSale = "";
      obj.isCharged = "";
      obj.status = "";
      obj.chargeRule = "";
      obj.remark = "";
      this.sysCardList.push(obj);
    },
    /** 卡密删除按钮操作 */
    handleDeleteSysCard() {
      if (this.checkedSysCard.length == 0) {
        this.$modal.msgError("请先选择要删除的卡密数据");
      } else {
        const sysCardList = this.sysCardList;
        const checkedSysCard = this.checkedSysCard;
        this.sysCardList = sysCardList.filter(function (item) {
          return checkedSysCard.indexOf(item.index) == -1
        });
      }
    },
    /** 复选框选中数据 */
    handleSysCardSelectionChange(selection) {
      this.checkedSysCard = selection.map(item => item.index)
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('sale/saleCard/export', {
        ...this.queryParams
      }, `saleCard_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
