<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="物料编码" prop="materialsNo">
        <el-input
          v-model="queryParams.materialsNo"
          placeholder="请输入物料编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="物料名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入物料名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="净重(数量*单件重量)" prop="netWight">
        <el-input
          v-model="queryParams.netWight"
          placeholder="请输入净重(数量*单件重量)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="未税" prop="noTax">
        <el-input
          v-model="queryParams.noTax"
          placeholder="请输入未税"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="公斤价" prop="perPrice">
        <el-input
          v-model="queryParams.perPrice"
          placeholder="请输入公斤价"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="备注" prop="commit">
        <el-input
          v-model="queryParams.commit"
          placeholder="请输入备注"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
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
          v-hasPermi="['system:easyquote:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:easyquote:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:easyquote:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:easyquote:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="easyquoteList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="报价单id" align="center" prop="id" />
      <el-table-column label="物料编码" align="center" prop="materialsNo" />
      <el-table-column label="物料名称" align="center" prop="name" />
      <el-table-column label="净重(数量*单件重量)" align="center" prop="netWight" />
      <el-table-column label="未税" align="center" prop="noTax" />
      <el-table-column label="公斤价" align="center" prop="perPrice" />
      <el-table-column label="备注" align="center" prop="commit" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:easyquote:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:easyquote:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改对外报价对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="物料编码" prop="materialsNo">
          <el-input v-model="form.materialsNo" placeholder="请输入物料编码" />
        </el-form-item>
        <el-form-item label="物料名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入物料名称" />
        </el-form-item>
        <el-form-item label="净重(数量*单件重量)" prop="netWight">
          <el-input v-model="form.netWight" placeholder="请输入净重(数量*单件重量)" />
        </el-form-item>
        <el-form-item label="未税" prop="noTax">
          <el-input v-model="form.noTax" placeholder="请输入未税" />
        </el-form-item>
        <el-form-item label="公斤价" prop="perPrice">
          <el-input v-model="form.perPrice" placeholder="请输入公斤价" />
        </el-form-item>
        <el-form-item label="备注" prop="commit">
          <el-input v-model="form.commit" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listEasyquote, getEasyquote, delEasyquote, addEasyquote, updateEasyquote } from "@/api/system/easyquote";

export default {
  name: "Easyquote",
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
      // 对外报价表格数据
      easyquoteList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        materialsNo: null,
        name: null,
        netWight: null,
        noTax: null,
        perPrice: null,
        commit: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询对外报价列表 */
    getList() {
      this.loading = true;
      listEasyquote(this.queryParams).then(response => {
        this.easyquoteList = response.rows;
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
        materialsNo: null,
        name: null,
        netWight: null,
        noTax: null,
        perPrice: null,
        commit: null
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加对外报价";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getEasyquote(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改对外报价";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateEasyquote(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addEasyquote(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除对外报价编号为"' + ids + '"的数据项？').then(function() {
        return delEasyquote(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/easyquote/export', {
        ...this.queryParams
      }, `easyquote_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
