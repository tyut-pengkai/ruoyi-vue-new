<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="86px">
      <el-form-item label="关联客户id" prop="customerId">
        <el-input
          v-model="queryParams.customerId"
          placeholder="请输入关联客户id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="毛胚重量参数" prop="blackWight">
        <el-input
          v-model="queryParams.blackWight"
          placeholder="请输入毛胚重量参数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="废料重参数" prop="scrapWight">
        <el-input
          v-model="queryParams.scrapWight"
          placeholder="请输入废料重参数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="数割下料费" prop="cutMaterial">
        <el-input
          v-model="queryParams.cutMaterial"
          placeholder="请输入数割下料费"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="锯" prop="processSaw">
        <el-input
          v-model="queryParams.processSaw"
          placeholder="请输入锯"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="弯" prop="processBend">
        <el-input
          v-model="queryParams.processBend"
          placeholder="请输入弯"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="钻" prop="processDrill">
        <el-input
          v-model="queryParams.processDrill"
          placeholder="请输入钻"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="车" prop="processLathe">
        <el-input
          v-model="queryParams.processLathe"
          placeholder="请输入车"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="外磨" prop="processGrind">
        <el-input
          v-model="queryParams.processGrind"
          placeholder="请输入外磨"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="铣" prop="processMill">
        <el-input
          v-model="queryParams.processMill"
          placeholder="请输入铣"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="校平" prop="processLevel">
        <el-input
          v-model="queryParams.processLevel"
          placeholder="请输入校平"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="镗铣" prop="processBor">
        <el-input
          v-model="queryParams.processBor"
          placeholder="请输入镗铣"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="焊" prop="processWeld">
        <el-input
          v-model="queryParams.processWeld"
          placeholder="请输入焊"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="打磨" prop="processPolish">
        <el-input
          v-model="queryParams.processPolish"
          placeholder="请输入打磨"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="装" prop="processPack">
        <el-input
          v-model="queryParams.processPack"
          placeholder="请输入装"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="镀锌" prop="surfaceGalvanized">
        <el-input
          v-model="queryParams.surfaceGalvanized"
          placeholder="请输入镀锌"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="调质" prop="surfaceCondit">
        <el-input
          v-model="queryParams.surfaceCondit"
          placeholder="请输入调质"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="冲砂" prop="surfaceSandwash">
        <el-input
          v-model="queryParams.surfaceSandwash"
          placeholder="请输入冲砂"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="QPQ" prop="surfacePqp">
        <el-input
          v-model="queryParams.surfacePqp"
          placeholder="请输入QPQ"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="酸洗" prop="sprayPick">
        <el-input
          v-model="queryParams.sprayPick"
          placeholder="请输入酸洗"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="喷塑" prop="sprayPlastic">
        <el-input
          v-model="queryParams.sprayPlastic"
          placeholder="请输入喷塑"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="电泳" prop="sprayElectro">
        <el-input
          v-model="queryParams.sprayElectro"
          placeholder="请输入电泳"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="底漆" prop="sprayPrimer">
        <el-input
          v-model="queryParams.sprayPrimer"
          placeholder="请输入底漆"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="面漆" prop="sprayTopcoat">
        <el-input
          v-model="queryParams.sprayTopcoat"
          placeholder="请输入面漆"
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
          v-hasPermi="['system:costparam:add']"
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
          v-hasPermi="['system:costparam:edit']"
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
          v-hasPermi="['system:costparam:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:costparam:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="costparamList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="关联客户id" align="center" prop="customerId" />
      <el-table-column label="毛胚重量参数" align="center" prop="blackWight" />
      <el-table-column label="废料重参数" align="center" prop="scrapWight" />
      <el-table-column label="数割下料费" align="center" prop="cutMaterial" />
      <el-table-column label="锯" align="center" prop="processSaw" />
      <el-table-column label="弯" align="center" prop="processBend" />
      <el-table-column label="钻" align="center" prop="processDrill" />
      <el-table-column label="车" align="center" prop="processLathe" />
      <el-table-column label="外磨" align="center" prop="processGrind" />
      <el-table-column label="铣" align="center" prop="processMill" />
      <el-table-column label="校平" align="center" prop="processLevel" />
      <el-table-column label="镗铣" align="center" prop="processBor" />
      <el-table-column label="焊" align="center" prop="processWeld" />
      <el-table-column label="打磨" align="center" prop="processPolish" />
      <el-table-column label="装" align="center" prop="processPack" />
      <el-table-column label="镀锌" align="center" prop="surfaceGalvanized" />
      <el-table-column label="调质" align="center" prop="surfaceCondit" />
      <el-table-column label="冲砂" align="center" prop="surfaceSandwash" />
      <el-table-column label="QPQ" align="center" prop="surfacePqp" />
      <el-table-column label="酸洗" align="center" prop="sprayPick" />
      <el-table-column label="喷塑" align="center" prop="sprayPlastic" />
      <el-table-column label="电泳" align="center" prop="sprayElectro" />
      <el-table-column label="底漆" align="center" prop="sprayPrimer" />
      <el-table-column label="面漆" align="center" prop="sprayTopcoat" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:costparam:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:costparam:remove']"
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

    <!-- 添加或修改成本参数对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="关联客户id" prop="customerId">
          <el-input v-model="form.customerId" placeholder="请输入关联客户id" />
        </el-form-item>
        <el-form-item label="毛胚重量参数" prop="blackWight">
          <el-input v-model="form.blackWight" placeholder="请输入毛胚重量参数" />
        </el-form-item>
        <el-form-item label="废料重参数" prop="scrapWight">
          <el-input v-model="form.scrapWight" placeholder="请输入废料重参数" />
        </el-form-item>
        <el-form-item label="数割下料费" prop="cutMaterial">
          <el-input v-model="form.cutMaterial" placeholder="请输入数割下料费" />
        </el-form-item>
        <el-form-item label="锯" prop="processSaw">
          <el-input v-model="form.processSaw" placeholder="请输入锯" />
        </el-form-item>
        <el-form-item label="弯" prop="processBend">
          <el-input v-model="form.processBend" placeholder="请输入弯" />
        </el-form-item>
        <el-form-item label="钻" prop="processDrill">
          <el-input v-model="form.processDrill" placeholder="请输入钻" />
        </el-form-item>
        <el-form-item label="车" prop="processLathe">
          <el-input v-model="form.processLathe" placeholder="请输入车" />
        </el-form-item>
        <el-form-item label="外磨" prop="processGrind">
          <el-input v-model="form.processGrind" placeholder="请输入外磨" />
        </el-form-item>
        <el-form-item label="铣" prop="processMill">
          <el-input v-model="form.processMill" placeholder="请输入铣" />
        </el-form-item>
        <el-form-item label="校平" prop="processLevel">
          <el-input v-model="form.processLevel" placeholder="请输入校平" />
        </el-form-item>
        <el-form-item label="镗铣" prop="processBor">
          <el-input v-model="form.processBor" placeholder="请输入镗铣" />
        </el-form-item>
        <el-form-item label="焊" prop="processWeld">
          <el-input v-model="form.processWeld" placeholder="请输入焊" />
        </el-form-item>
        <el-form-item label="打磨" prop="processPolish">
          <el-input v-model="form.processPolish" placeholder="请输入打磨" />
        </el-form-item>
        <el-form-item label="装" prop="processPack">
          <el-input v-model="form.processPack" placeholder="请输入装" />
        </el-form-item>
        <el-form-item label="镀锌" prop="surfaceGalvanized">
          <el-input v-model="form.surfaceGalvanized" placeholder="请输入镀锌" />
        </el-form-item>
        <el-form-item label="调质" prop="surfaceCondit">
          <el-input v-model="form.surfaceCondit" placeholder="请输入调质" />
        </el-form-item>
        <el-form-item label="冲砂" prop="surfaceSandwash">
          <el-input v-model="form.surfaceSandwash" placeholder="请输入冲砂" />
        </el-form-item>
        <el-form-item label="QPQ" prop="surfacePqp">
          <el-input v-model="form.surfacePqp" placeholder="请输入QPQ" />
        </el-form-item>
        <el-form-item label="酸洗" prop="sprayPick">
          <el-input v-model="form.sprayPick" placeholder="请输入酸洗" />
        </el-form-item>
        <el-form-item label="喷塑" prop="sprayPlastic">
          <el-input v-model="form.sprayPlastic" placeholder="请输入喷塑" />
        </el-form-item>
        <el-form-item label="电泳" prop="sprayElectro">
          <el-input v-model="form.sprayElectro" placeholder="请输入电泳" />
        </el-form-item>
        <el-form-item label="底漆" prop="sprayPrimer">
          <el-input v-model="form.sprayPrimer" placeholder="请输入底漆" />
        </el-form-item>
        <el-form-item label="面漆" prop="sprayTopcoat">
          <el-input v-model="form.sprayTopcoat" placeholder="请输入面漆" />
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
import { listCostparam, getCostparam, delCostparam, addCostparam, updateCostparam } from "@/api/system/costparam";

export default {
  name: "Costparam",
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
      // 成本参数表格数据
      costparamList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        customerId: null,
        blackWight: null,
        scrapWight: null,
        cutMaterial: null,
        processSaw: null,
        processBend: null,
        processDrill: null,
        processLathe: null,
        processGrind: null,
        processMill: null,
        processLevel: null,
        processBor: null,
        processWeld: null,
        processPolish: null,
        processPack: null,
        surfaceGalvanized: null,
        surfaceCondit: null,
        surfaceSandwash: null,
        surfacePqp: null,
        sprayPick: null,
        sprayPlastic: null,
        sprayElectro: null,
        sprayPrimer: null,
        sprayTopcoat: null
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
    /** 查询成本参数列表 */
    getList() {
      this.loading = true;
      listCostparam(this.queryParams).then(response => {
        this.costparamList = response.rows;
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
        customerId: null,
        blackWight: null,
        scrapWight: null,
        cutMaterial: null,
        processSaw: null,
        processBend: null,
        processDrill: null,
        processLathe: null,
        processGrind: null,
        processMill: null,
        processLevel: null,
        processBor: null,
        processWeld: null,
        processPolish: null,
        processPack: null,
        surfaceGalvanized: null,
        surfaceCondit: null,
        surfaceSandwash: null,
        surfacePqp: null,
        sprayPick: null,
        sprayPlastic: null,
        sprayElectro: null,
        sprayPrimer: null,
        sprayTopcoat: null
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
      this.title = "添加成本参数";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getCostparam(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改成本参数";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCostparam(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCostparam(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除成本参数编号为"' + ids + '"的数据项？').then(function() {
        return delCostparam(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/costparam/export', {
        ...this.queryParams
      }, `costparam_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
