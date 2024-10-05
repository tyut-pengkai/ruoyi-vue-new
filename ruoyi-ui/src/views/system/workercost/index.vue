<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="120px">
      <el-form-item label="关联的报价单id" prop="quoteId">
        <el-input
          v-model="queryParams.quoteId"
          placeholder="请输入关联的报价单id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="锯" prop="processSaw">
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
      <el-form-item label="锯" prop="processSawWorker">
        <el-input
          v-model="queryParams.processSawWorker"
          placeholder="请输入锯"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="弯" prop="processBendWorker">
        <el-input
          v-model="queryParams.processBendWorker"
          placeholder="请输入弯"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="钻" prop="processDrillWorker">
        <el-input
          v-model="queryParams.processDrillWorker"
          placeholder="请输入钻"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="车" prop="processLatheWorker">
        <el-input
          v-model="queryParams.processLatheWorker"
          placeholder="请输入车"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="外磨" prop="processGrindWorker">
        <el-input
          v-model="queryParams.processGrindWorker"
          placeholder="请输入外磨"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="铣" prop="processMillWorker">
        <el-input
          v-model="queryParams.processMillWorker"
          placeholder="请输入铣"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="校平" prop="processLevelWorker">
        <el-input
          v-model="queryParams.processLevelWorker"
          placeholder="请输入校平"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="镗铣" prop="processBorWorker">
        <el-input
          v-model="queryParams.processBorWorker"
          placeholder="请输入镗铣"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="焊" prop="processWeldWorker">
        <el-input
          v-model="queryParams.processWeldWorker"
          placeholder="请输入焊"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="装" prop="processPackWorker">
        <el-input
          v-model="queryParams.processPackWorker"
          placeholder="请输入装"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="打磨" prop="processPolishWorker">
        <el-input
          v-model="queryParams.processPolishWorker"
          placeholder="请输入打磨"
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
      <el-form-item label="镀锌" prop="surfaceGalvanizedWorker">
        <el-input
          v-model="queryParams.surfaceGalvanizedWorker"
          placeholder="请输入镀锌"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="调质" prop="surfaceConditWorker">
        <el-input
          v-model="queryParams.surfaceConditWorker"
          placeholder="请输入调质"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="冲砂" prop="surfaceSandwashWorker">
        <el-input
          v-model="queryParams.surfaceSandwashWorker"
          placeholder="请输入冲砂"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="QPQ" prop="surfacePqpWorker">
        <el-input
          v-model="queryParams.surfacePqpWorker"
          placeholder="请输入QPQ"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="酸洗" prop="sprayWashpickling">
        <el-input
          v-model="queryParams.sprayWashpickling"
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
      </el-form-item>
      <el-form-item label="酸洗" prop="sprayPickWorker">
        <el-input
          v-model="queryParams.sprayPickWorker"
          placeholder="请输入酸洗"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="喷塑" prop="sprayPlasticWorker">
        <el-input
          v-model="queryParams.sprayPlasticWorker"
          placeholder="请输入喷塑"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="电泳" prop="sprayElectroWorker">
        <el-input
          v-model="queryParams.sprayElectroWorker"
          placeholder="请输入电泳"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="底漆" prop="sprayPrimerWorker">
        <el-input
          v-model="queryParams.sprayPrimerWorker"
          placeholder="请输入底漆"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="面漆" prop="sprayTopcoatWorker">
        <el-input
          v-model="queryParams.sprayTopcoatWorker"
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
          v-hasPermi="['system:workercost:add']"
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
          v-hasPermi="['system:workercost:edit']"
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
          v-hasPermi="['system:workercost:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:workercost:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="workercostList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="人工成本单id" align="center" prop="id" />
      <el-table-column label="关联的报价单id" align="center" prop="quoteId" />
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
      <el-table-column label="锯" align="center" prop="processSawWorker" />
      <el-table-column label="弯" align="center" prop="processBendWorker" />
      <el-table-column label="钻" align="center" prop="processDrillWorker" />
      <el-table-column label="车" align="center" prop="processLatheWorker" />
      <el-table-column label="外磨" align="center" prop="processGrindWorker" />
      <el-table-column label="铣" align="center" prop="processMillWorker" />
      <el-table-column label="校平" align="center" prop="processLevelWorker" />
      <el-table-column label="镗铣" align="center" prop="processBorWorker" />
      <el-table-column label="焊" align="center" prop="processWeldWorker" />
      <el-table-column label="装" align="center" prop="processPackWorker" />
      <el-table-column label="打磨" align="center" prop="processPolishWorker" />
      <el-table-column label="镀锌" align="center" prop="surfaceGalvanized" />
      <el-table-column label="调质" align="center" prop="surfaceCondit" />
      <el-table-column label="冲砂" align="center" prop="surfaceSandwash" />
      <el-table-column label="QPQ" align="center" prop="surfacePqp" />
      <el-table-column label="镀锌" align="center" prop="surfaceGalvanizedWorker" />
      <el-table-column label="调质" align="center" prop="surfaceConditWorker" />
      <el-table-column label="冲砂" align="center" prop="surfaceSandwashWorker" />
      <el-table-column label="QPQ" align="center" prop="surfacePqpWorker" />
      <el-table-column label="酸洗" align="center" prop="sprayWashpickling" />
      <el-table-column label="喷塑" align="center" prop="sprayPlastic" />
      <el-table-column label="电泳" align="center" prop="sprayElectro" />
      <el-table-column label="底漆" align="center" prop="sprayPrimer" />
      <el-table-column label="面漆" align="center" prop="sprayTopcoat" />
      <el-table-column label="酸洗" align="center" prop="sprayPickWorker" />
      <el-table-column label="喷塑" align="center" prop="sprayPlasticWorker" />
      <el-table-column label="电泳" align="center" prop="sprayElectroWorker" />
      <el-table-column label="底漆" align="center" prop="sprayPrimerWorker" />
      <el-table-column label="面漆" align="center" prop="sprayTopcoatWorker" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:workercost:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:workercost:remove']"
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

    <!-- 添加或修改人工成本对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="关联的报价单id" prop="quoteId">
          <el-input v-model="form.quoteId" placeholder="请输入关联的报价单id" />
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
        <el-form-item label="锯" prop="processSawWorker">
          <el-input v-model="form.processSawWorker" placeholder="请输入锯" />
        </el-form-item>
        <el-form-item label="弯" prop="processBendWorker">
          <el-input v-model="form.processBendWorker" placeholder="请输入弯" />
        </el-form-item>
        <el-form-item label="钻" prop="processDrillWorker">
          <el-input v-model="form.processDrillWorker" placeholder="请输入钻" />
        </el-form-item>
        <el-form-item label="车" prop="processLatheWorker">
          <el-input v-model="form.processLatheWorker" placeholder="请输入车" />
        </el-form-item>
        <el-form-item label="外磨" prop="processGrindWorker">
          <el-input v-model="form.processGrindWorker" placeholder="请输入外磨" />
        </el-form-item>
        <el-form-item label="铣" prop="processMillWorker">
          <el-input v-model="form.processMillWorker" placeholder="请输入铣" />
        </el-form-item>
        <el-form-item label="校平" prop="processLevelWorker">
          <el-input v-model="form.processLevelWorker" placeholder="请输入校平" />
        </el-form-item>
        <el-form-item label="镗铣" prop="processBorWorker">
          <el-input v-model="form.processBorWorker" placeholder="请输入镗铣" />
        </el-form-item>
        <el-form-item label="焊" prop="processWeldWorker">
          <el-input v-model="form.processWeldWorker" placeholder="请输入焊" />
        </el-form-item>
        <el-form-item label="装" prop="processPackWorker">
          <el-input v-model="form.processPackWorker" placeholder="请输入装" />
        </el-form-item>
        <el-form-item label="打磨" prop="processPolishWorker">
          <el-input v-model="form.processPolishWorker" placeholder="请输入打磨" />
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
        <el-form-item label="镀锌" prop="surfaceGalvanizedWorker">
          <el-input v-model="form.surfaceGalvanizedWorker" placeholder="请输入镀锌" />
        </el-form-item>
        <el-form-item label="调质" prop="surfaceConditWorker">
          <el-input v-model="form.surfaceConditWorker" placeholder="请输入调质" />
        </el-form-item>
        <el-form-item label="冲砂" prop="surfaceSandwashWorker">
          <el-input v-model="form.surfaceSandwashWorker" placeholder="请输入冲砂" />
        </el-form-item>
        <el-form-item label="QPQ" prop="surfacePqpWorker">
          <el-input v-model="form.surfacePqpWorker" placeholder="请输入QPQ" />
        </el-form-item>
        <el-form-item label="酸洗" prop="sprayWashpickling">
          <el-input v-model="form.sprayWashpickling" placeholder="请输入酸洗" />
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
        <el-form-item label="酸洗" prop="sprayPickWorker">
          <el-input v-model="form.sprayPickWorker" placeholder="请输入酸洗" />
        </el-form-item>
        <el-form-item label="喷塑" prop="sprayPlasticWorker">
          <el-input v-model="form.sprayPlasticWorker" placeholder="请输入喷塑" />
        </el-form-item>
        <el-form-item label="电泳" prop="sprayElectroWorker">
          <el-input v-model="form.sprayElectroWorker" placeholder="请输入电泳" />
        </el-form-item>
        <el-form-item label="底漆" prop="sprayPrimerWorker">
          <el-input v-model="form.sprayPrimerWorker" placeholder="请输入底漆" />
        </el-form-item>
        <el-form-item label="面漆" prop="sprayTopcoatWorker">
          <el-input v-model="form.sprayTopcoatWorker" placeholder="请输入面漆" />
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
import { listWorkercost, getWorkercost, delWorkercost, addWorkercost, updateWorkercost } from "@/api/system/workercost";

export default {
  name: "Workercost",
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
      // 人工成本表格数据
      workercostList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        quoteId: null,
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
        processSawWorker: null,
        processBendWorker: null,
        processDrillWorker: null,
        processLatheWorker: null,
        processGrindWorker: null,
        processMillWorker: null,
        processLevelWorker: null,
        processBorWorker: null,
        processWeldWorker: null,
        processPackWorker: null,
        processPolishWorker: null,
        surfaceGalvanized: null,
        surfaceCondit: null,
        surfaceSandwash: null,
        surfacePqp: null,
        surfaceGalvanizedWorker: null,
        surfaceConditWorker: null,
        surfaceSandwashWorker: null,
        surfacePqpWorker: null,
        sprayWashpickling: null,
        sprayPlastic: null,
        sprayElectro: null,
        sprayPrimer: null,
        sprayTopcoat: null,
        sprayPickWorker: null,
        sprayPlasticWorker: null,
        sprayElectroWorker: null,
        sprayPrimerWorker: null,
        sprayTopcoatWorker: null
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
    /** 查询人工成本列表 */
    getList() {
      this.loading = true;
      listWorkercost(this.queryParams).then(response => {
        this.workercostList = response.rows;
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
        quoteId: null,
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
        processSawWorker: null,
        processBendWorker: null,
        processDrillWorker: null,
        processLatheWorker: null,
        processGrindWorker: null,
        processMillWorker: null,
        processLevelWorker: null,
        processBorWorker: null,
        processWeldWorker: null,
        processPackWorker: null,
        processPolishWorker: null,
        surfaceGalvanized: null,
        surfaceCondit: null,
        surfaceSandwash: null,
        surfacePqp: null,
        surfaceGalvanizedWorker: null,
        surfaceConditWorker: null,
        surfaceSandwashWorker: null,
        surfacePqpWorker: null,
        sprayWashpickling: null,
        sprayPlastic: null,
        sprayElectro: null,
        sprayPrimer: null,
        sprayTopcoat: null,
        sprayPickWorker: null,
        sprayPlasticWorker: null,
        sprayElectroWorker: null,
        sprayPrimerWorker: null,
        sprayTopcoatWorker: null
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
      this.title = "添加人工成本";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getWorkercost(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改人工成本";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateWorkercost(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addWorkercost(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除人工成本编号为"' + ids + '"的数据项？').then(function() {
        return delWorkercost(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/workercost/export', {
        ...this.queryParams
      }, `workercost_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
