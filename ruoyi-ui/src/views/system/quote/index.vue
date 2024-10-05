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
      <!-- <el-form-item label="数量" prop="num">
        <el-input
          v-model="queryParams.num"
          placeholder="请输入数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否外来" prop="flag">
        <el-input
          v-model="queryParams.flag"
          placeholder="请输入是否外来"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="单价" prop="cost">
        <el-input
          v-model="queryParams.cost"
          placeholder="请输入单价"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="材料规格" prop="materialSpec">
        <el-input
          v-model="queryParams.materialSpec"
          placeholder="请输入材料规格"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="单件重量" prop="perWight">
        <el-input
          v-model="queryParams.perWight"
          placeholder="请输入单件重量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="净重(数量*单件重量)" prop="netWight">
        <el-input
          v-model="queryParams.netWight"
          placeholder="请输入净重(数量*单件重量)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="长" prop="steelLen">
        <el-input
          v-model="queryParams.steelLen"
          placeholder="请输入长"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="宽" prop="steelWid">
        <el-input
          v-model="queryParams.steelWid"
          placeholder="请输入宽"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="高" prop="steelHei">
        <el-input
          v-model="queryParams.steelHei"
          placeholder="请输入高"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="毛坯重量" prop="steelWight">
        <el-input
          v-model="queryParams.steelWight"
          placeholder="请输入毛坯重量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="材料单价" prop="steelPerPrice">
        <el-input
          v-model="queryParams.steelPerPrice"
          placeholder="请输入材料单价"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="毛坯费" prop="steelPrice">
        <el-input
          v-model="queryParams.steelPrice"
          placeholder="请输入毛坯费"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="废料重" prop="steelScrapWgt">
        <el-input
          v-model="queryParams.steelScrapWgt"
          placeholder="请输入废料重"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="废料单价" prop="steelScrapPer">
        <el-input
          v-model="queryParams.steelScrapPer"
          placeholder="请输入废料单价"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="废料费" prop="steelScrapPrice">
        <el-input
          v-model="queryParams.steelScrapPrice"
          placeholder="请输入废料费"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="原料材料费用小计" prop="totalSteel">
        <el-input
          v-model="queryParams.totalSteel"
          placeholder="请输入原料材料费用小计"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="数割" prop="cutNum">
        <el-input
          v-model="queryParams.cutNum"
          placeholder="请输入数割"
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
      <el-form-item label="数割费用小计" prop="totalCut">
        <el-input
          v-model="queryParams.totalCut"
          placeholder="请输入数割费用小计"
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
      <el-form-item label="锯" prop="processSawTime">
        <el-input
          v-model="queryParams.processSawTime"
          placeholder="请输入锯"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="弯" prop="processBendTime">
        <el-input
          v-model="queryParams.processBendTime"
          placeholder="请输入弯"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="钻" prop="processDrillTime">
        <el-input
          v-model="queryParams.processDrillTime"
          placeholder="请输入钻"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="车" prop="processLatheTime">
        <el-input
          v-model="queryParams.processLatheTime"
          placeholder="请输入车"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="外磨" prop="processGrindTime">
        <el-input
          v-model="queryParams.processGrindTime"
          placeholder="请输入外磨"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="铣" prop="processMillTime">
        <el-input
          v-model="queryParams.processMillTime"
          placeholder="请输入铣"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="校平" prop="processLevelTime">
        <el-input
          v-model="queryParams.processLevelTime"
          placeholder="请输入校平"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="镗铣" prop="processBorTime">
        <el-input
          v-model="queryParams.processBorTime"
          placeholder="请输入镗铣"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="焊" prop="processWeldTime">
        <el-input
          v-model="queryParams.processWeldTime"
          placeholder="请输入焊"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="打磨" prop="processPolishTime">
        <el-input
          v-model="queryParams.processPolishTime"
          placeholder="请输入打磨"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="装" prop="processPackTime">
        <el-input
          v-model="queryParams.processPackTime"
          placeholder="请输入装"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="加工费小计" prop="totalProcess">
        <el-input
          v-model="queryParams.totalProcess"
          placeholder="请输入加工费小计"
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
      <el-form-item label="镀锌" prop="surfaceGalvanizedWgt">
        <el-input
          v-model="queryParams.surfaceGalvanizedWgt"
          placeholder="请输入镀锌"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="调质" prop="surfaceConditWgt">
        <el-input
          v-model="queryParams.surfaceConditWgt"
          placeholder="请输入调质"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="冲砂" prop="surfaceSandwashWgt">
        <el-input
          v-model="queryParams.surfaceSandwashWgt"
          placeholder="请输入冲砂"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="QPQ" prop="surfacePqpWgt">
        <el-input
          v-model="queryParams.surfacePqpWgt"
          placeholder="请输入QPQ"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="表面处理费小计" prop="totalSurface">
        <el-input
          v-model="queryParams.totalSurface"
          placeholder="请输入表面处理费小计"
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
      <el-form-item label="酸洗" prop="sprayPickSquare">
        <el-input
          v-model="queryParams.sprayPickSquare"
          placeholder="请输入酸洗"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="喷塑" prop="sprayPlasticSquare">
        <el-input
          v-model="queryParams.sprayPlasticSquare"
          placeholder="请输入喷塑"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="电泳" prop="sprayElectroSquare">
        <el-input
          v-model="queryParams.sprayElectroSquare"
          placeholder="请输入电泳"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="底漆" prop="sprayPrimerSquare">
        <el-input
          v-model="queryParams.sprayPrimerSquare"
          placeholder="请输入底漆"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="面漆" prop="sprayTopcoatSquare">
        <el-input
          v-model="queryParams.sprayTopcoatSquare"
          placeholder="请输入面漆"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="喷涂费用小计" prop="totalSpray">
        <el-input
          v-model="queryParams.totalSpray"
          placeholder="请输入喷涂费用小计"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="裸价(元)" prop="nakedPrice">
        <el-input
          v-model="queryParams.nakedPrice"
          placeholder="请输入裸价(元)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="利润" prop="profit">
        <el-input
          v-model="queryParams.profit"
          placeholder="请输入利润"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="包装运输" prop="transCost">
        <el-input
          v-model="queryParams.transCost"
          placeholder="请输入包装运输"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="产品合计报价" prop="totalPrice">
        <el-input
          v-model="queryParams.totalPrice"
          placeholder="请输入产品合计报价"
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
          v-hasPermi="['system:quote:add']"
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
          v-hasPermi="['system:quote:edit']"
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
          v-hasPermi="['system:quote:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:quote:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="quoteList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="报价单id" align="center" prop="id" />
      <el-table-column label="物料编码" align="center" prop="materialsNo" />
      <el-table-column label="物料名称" align="center" prop="name" />
      <el-table-column label="数量" align="center" prop="num" />
      <el-table-column label="是否外来" align="center" prop="flag" />
      <el-table-column label="单价" align="center" prop="cost" />
      <el-table-column label="材料规格" align="center" prop="materialSpec" />
      <el-table-column label="单件重量" align="center" prop="perWight" />
      <el-table-column label="净重(数量*单件重量)" align="center" prop="netWight" />
      <el-table-column label="长" align="center" prop="steelLen" />
      <el-table-column label="宽" align="center" prop="steelWid" />
      <el-table-column label="高" align="center" prop="steelHei" />
      <el-table-column label="毛坯重量" align="center" prop="steelWight" />
      <el-table-column label="材料单价" align="center" prop="steelPerPrice" />
      <el-table-column label="毛坯费" align="center" prop="steelPrice" />
      <el-table-column label="废料重" align="center" prop="steelScrapWgt" />
      <el-table-column label="废料单价" align="center" prop="steelScrapPer" />
      <el-table-column label="废料费" align="center" prop="steelScrapPrice" />
      <el-table-column label="原料材料费用小计" align="center" prop="totalSteel" />
      <el-table-column label="数割" align="center" prop="cutNum" />
      <el-table-column label="数割下料费" align="center" prop="cutMaterial" />
      <el-table-column label="数割费用小计" align="center" prop="totalCut" />
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
      <el-table-column label="锯" align="center" prop="processSawTime" />
      <el-table-column label="弯" align="center" prop="processBendTime" />
      <el-table-column label="钻" align="center" prop="processDrillTime" />
      <el-table-column label="车" align="center" prop="processLatheTime" />
      <el-table-column label="外磨" align="center" prop="processGrindTime" />
      <el-table-column label="铣" align="center" prop="processMillTime" />
      <el-table-column label="校平" align="center" prop="processLevelTime" />
      <el-table-column label="镗铣" align="center" prop="processBorTime" />
      <el-table-column label="焊" align="center" prop="processWeldTime" />
      <el-table-column label="打磨" align="center" prop="processPolishTime" />
      <el-table-column label="装" align="center" prop="processPackTime" />
      <el-table-column label="加工费小计" align="center" prop="totalProcess" />
      <el-table-column label="镀锌" align="center" prop="surfaceGalvanized" />
      <el-table-column label="调质" align="center" prop="surfaceCondit" />
      <el-table-column label="冲砂" align="center" prop="surfaceSandwash" />
      <el-table-column label="QPQ" align="center" prop="surfacePqp" />
      <el-table-column label="镀锌" align="center" prop="surfaceGalvanizedWgt" />
      <el-table-column label="调质" align="center" prop="surfaceConditWgt" />
      <el-table-column label="冲砂" align="center" prop="surfaceSandwashWgt" />
      <el-table-column label="QPQ" align="center" prop="surfacePqpWgt" />
      <el-table-column label="表面处理费小计" align="center" prop="totalSurface" />
      <el-table-column label="酸洗" align="center" prop="sprayWashpickling" />
      <el-table-column label="喷塑" align="center" prop="sprayPlastic" />
      <el-table-column label="电泳" align="center" prop="sprayElectro" />
      <el-table-column label="底漆" align="center" prop="sprayPrimer" />
      <el-table-column label="面漆" align="center" prop="sprayTopcoat" />
      <el-table-column label="酸洗" align="center" prop="sprayPickSquare" />
      <el-table-column label="喷塑" align="center" prop="sprayPlasticSquare" />
      <el-table-column label="电泳" align="center" prop="sprayElectroSquare" />
      <el-table-column label="底漆" align="center" prop="sprayPrimerSquare" />
      <el-table-column label="面漆" align="center" prop="sprayTopcoatSquare" />
      <el-table-column label="喷涂费用小计" align="center" prop="totalSpray" />
      <el-table-column label="裸价(元)" align="center" prop="nakedPrice" />
      <el-table-column label="利润" align="center" prop="profit" />
      <el-table-column label="包装运输" align="center" prop="transCost" />
      <el-table-column label="产品合计报价" align="center" prop="totalPrice" />
      <el-table-column label="未税" align="center" prop="noTax" />
      <el-table-column label="公斤价" align="center" prop="perPrice" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:quote:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:quote:remove']"
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

    <!-- 添加或修改报价对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="物料编码" prop="materialsNo">
          <el-input v-model="form.materialsNo" placeholder="请输入物料编码" />
        </el-form-item>
        <el-form-item label="物料名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入物料名称" />
        </el-form-item>
        <el-form-item label="数量" prop="num">
          <el-input v-model="form.num" placeholder="请输入数量" />
        </el-form-item>
        <el-form-item label="是否外来" prop="flag">
          <el-input v-model="form.flag" placeholder="请输入是否外来" />
        </el-form-item>
        <el-form-item label="单价" prop="cost">
          <el-input v-model="form.cost" placeholder="请输入单价" />
        </el-form-item>
        <el-form-item label="材料规格" prop="materialSpec">
          <el-input v-model="form.materialSpec" placeholder="请输入材料规格" />
        </el-form-item>
        <el-form-item label="单件重量" prop="perWight">
          <el-input v-model="form.perWight" placeholder="请输入单件重量" />
        </el-form-item>
        <el-form-item label="净重(数量*单件重量)" prop="netWight">
          <el-input v-model="form.netWight" placeholder="请输入净重(数量*单件重量)" />
        </el-form-item>
        <el-form-item label="长" prop="steelLen">
          <el-input v-model="form.steelLen" placeholder="请输入长" />
        </el-form-item>
        <el-form-item label="宽" prop="steelWid">
          <el-input v-model="form.steelWid" placeholder="请输入宽" />
        </el-form-item>
        <el-form-item label="高" prop="steelHei">
          <el-input v-model="form.steelHei" placeholder="请输入高" />
        </el-form-item>
        <el-form-item label="毛坯重量" prop="steelWight">
          <el-input v-model="form.steelWight" placeholder="请输入毛坯重量" />
        </el-form-item>
        <el-form-item label="材料单价" prop="steelPerPrice">
          <el-input v-model="form.steelPerPrice" placeholder="请输入材料单价" />
        </el-form-item>
        <el-form-item label="毛坯费" prop="steelPrice">
          <el-input v-model="form.steelPrice" placeholder="请输入毛坯费" />
        </el-form-item>
        <el-form-item label="废料重" prop="steelScrapWgt">
          <el-input v-model="form.steelScrapWgt" placeholder="请输入废料重" />
        </el-form-item>
        <el-form-item label="废料单价" prop="steelScrapPer">
          <el-input v-model="form.steelScrapPer" placeholder="请输入废料单价" />
        </el-form-item>
        <el-form-item label="废料费" prop="steelScrapPrice">
          <el-input v-model="form.steelScrapPrice" placeholder="请输入废料费" />
        </el-form-item>
        <el-form-item label="原料材料费用小计" prop="totalSteel">
          <el-input v-model="form.totalSteel" placeholder="请输入原料材料费用小计" />
        </el-form-item>
        <el-form-item label="数割" prop="cutNum">
          <el-input v-model="form.cutNum" placeholder="请输入数割" />
        </el-form-item>
        <el-form-item label="数割下料费" prop="cutMaterial">
          <el-input v-model="form.cutMaterial" placeholder="请输入数割下料费" />
        </el-form-item>
        <el-form-item label="数割费用小计" prop="totalCut">
          <el-input v-model="form.totalCut" placeholder="请输入数割费用小计" />
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
        <el-form-item label="锯" prop="processSawTime">
          <el-input v-model="form.processSawTime" placeholder="请输入锯" />
        </el-form-item>
        <el-form-item label="弯" prop="processBendTime">
          <el-input v-model="form.processBendTime" placeholder="请输入弯" />
        </el-form-item>
        <el-form-item label="钻" prop="processDrillTime">
          <el-input v-model="form.processDrillTime" placeholder="请输入钻" />
        </el-form-item>
        <el-form-item label="车" prop="processLatheTime">
          <el-input v-model="form.processLatheTime" placeholder="请输入车" />
        </el-form-item>
        <el-form-item label="外磨" prop="processGrindTime">
          <el-input v-model="form.processGrindTime" placeholder="请输入外磨" />
        </el-form-item>
        <el-form-item label="铣" prop="processMillTime">
          <el-input v-model="form.processMillTime" placeholder="请输入铣" />
        </el-form-item>
        <el-form-item label="校平" prop="processLevelTime">
          <el-input v-model="form.processLevelTime" placeholder="请输入校平" />
        </el-form-item>
        <el-form-item label="镗铣" prop="processBorTime">
          <el-input v-model="form.processBorTime" placeholder="请输入镗铣" />
        </el-form-item>
        <el-form-item label="焊" prop="processWeldTime">
          <el-input v-model="form.processWeldTime" placeholder="请输入焊" />
        </el-form-item>
        <el-form-item label="打磨" prop="processPolishTime">
          <el-input v-model="form.processPolishTime" placeholder="请输入打磨" />
        </el-form-item>
        <el-form-item label="装" prop="processPackTime">
          <el-input v-model="form.processPackTime" placeholder="请输入装" />
        </el-form-item>
        <el-form-item label="加工费小计" prop="totalProcess">
          <el-input v-model="form.totalProcess" placeholder="请输入加工费小计" />
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
        <el-form-item label="镀锌" prop="surfaceGalvanizedWgt">
          <el-input v-model="form.surfaceGalvanizedWgt" placeholder="请输入镀锌" />
        </el-form-item>
        <el-form-item label="调质" prop="surfaceConditWgt">
          <el-input v-model="form.surfaceConditWgt" placeholder="请输入调质" />
        </el-form-item>
        <el-form-item label="冲砂" prop="surfaceSandwashWgt">
          <el-input v-model="form.surfaceSandwashWgt" placeholder="请输入冲砂" />
        </el-form-item>
        <el-form-item label="QPQ" prop="surfacePqpWgt">
          <el-input v-model="form.surfacePqpWgt" placeholder="请输入QPQ" />
        </el-form-item>
        <el-form-item label="表面处理费小计" prop="totalSurface">
          <el-input v-model="form.totalSurface" placeholder="请输入表面处理费小计" />
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
        <el-form-item label="酸洗" prop="sprayPickSquare">
          <el-input v-model="form.sprayPickSquare" placeholder="请输入酸洗" />
        </el-form-item>
        <el-form-item label="喷塑" prop="sprayPlasticSquare">
          <el-input v-model="form.sprayPlasticSquare" placeholder="请输入喷塑" />
        </el-form-item>
        <el-form-item label="电泳" prop="sprayElectroSquare">
          <el-input v-model="form.sprayElectroSquare" placeholder="请输入电泳" />
        </el-form-item>
        <el-form-item label="底漆" prop="sprayPrimerSquare">
          <el-input v-model="form.sprayPrimerSquare" placeholder="请输入底漆" />
        </el-form-item>
        <el-form-item label="面漆" prop="sprayTopcoatSquare">
          <el-input v-model="form.sprayTopcoatSquare" placeholder="请输入面漆" />
        </el-form-item>
        <el-form-item label="喷涂费用小计" prop="totalSpray">
          <el-input v-model="form.totalSpray" placeholder="请输入喷涂费用小计" />
        </el-form-item>
        <el-form-item label="裸价(元)" prop="nakedPrice">
          <el-input v-model="form.nakedPrice" placeholder="请输入裸价(元)" />
        </el-form-item>
        <el-form-item label="利润" prop="profit">
          <el-input v-model="form.profit" placeholder="请输入利润" />
        </el-form-item>
        <el-form-item label="包装运输" prop="transCost">
          <el-input v-model="form.transCost" placeholder="请输入包装运输" />
        </el-form-item>
        <el-form-item label="产品合计报价" prop="totalPrice">
          <el-input v-model="form.totalPrice" placeholder="请输入产品合计报价" />
        </el-form-item>
        <el-form-item label="未税" prop="noTax">
          <el-input v-model="form.noTax" placeholder="请输入未税" />
        </el-form-item>
        <el-form-item label="公斤价" prop="perPrice">
          <el-input v-model="form.perPrice" placeholder="请输入公斤价" />
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
import { listQuote, getQuote, delQuote, addQuote, updateQuote } from "@/api/system/quote";

export default {
  name: "Quote",
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
      // 报价表格数据
      quoteList: [],
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
        num: null,
        flag: null,
        cost: null,
        materialSpec: null,
        perWight: null,
        netWight: null,
        steelLen: null,
        steelWid: null,
        steelHei: null,
        steelWight: null,
        steelPerPrice: null,
        steelPrice: null,
        steelScrapWgt: null,
        steelScrapPer: null,
        steelScrapPrice: null,
        totalSteel: null,
        cutNum: null,
        cutMaterial: null,
        totalCut: null,
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
        processSawTime: null,
        processBendTime: null,
        processDrillTime: null,
        processLatheTime: null,
        processGrindTime: null,
        processMillTime: null,
        processLevelTime: null,
        processBorTime: null,
        processWeldTime: null,
        processPolishTime: null,
        processPackTime: null,
        totalProcess: null,
        surfaceGalvanized: null,
        surfaceCondit: null,
        surfaceSandwash: null,
        surfacePqp: null,
        surfaceGalvanizedWgt: null,
        surfaceConditWgt: null,
        surfaceSandwashWgt: null,
        surfacePqpWgt: null,
        totalSurface: null,
        sprayWashpickling: null,
        sprayPlastic: null,
        sprayElectro: null,
        sprayPrimer: null,
        sprayTopcoat: null,
        sprayPickSquare: null,
        sprayPlasticSquare: null,
        sprayElectroSquare: null,
        sprayPrimerSquare: null,
        sprayTopcoatSquare: null,
        totalSpray: null,
        nakedPrice: null,
        profit: null,
        transCost: null,
        totalPrice: null,
        noTax: null,
        perPrice: null
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
    /** 查询报价列表 */
    getList() {
      this.loading = true;
      listQuote(this.queryParams).then(response => {
        this.quoteList = response.rows;
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
        num: null,
        flag: null,
        cost: null,
        materialSpec: null,
        perWight: null,
        netWight: null,
        steelLen: null,
        steelWid: null,
        steelHei: null,
        steelWight: null,
        steelPerPrice: null,
        steelPrice: null,
        steelScrapWgt: null,
        steelScrapPer: null,
        steelScrapPrice: null,
        totalSteel: null,
        cutNum: null,
        cutMaterial: null,
        totalCut: null,
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
        processSawTime: null,
        processBendTime: null,
        processDrillTime: null,
        processLatheTime: null,
        processGrindTime: null,
        processMillTime: null,
        processLevelTime: null,
        processBorTime: null,
        processWeldTime: null,
        processPolishTime: null,
        processPackTime: null,
        totalProcess: null,
        surfaceGalvanized: null,
        surfaceCondit: null,
        surfaceSandwash: null,
        surfacePqp: null,
        surfaceGalvanizedWgt: null,
        surfaceConditWgt: null,
        surfaceSandwashWgt: null,
        surfacePqpWgt: null,
        totalSurface: null,
        sprayWashpickling: null,
        sprayPlastic: null,
        sprayElectro: null,
        sprayPrimer: null,
        sprayTopcoat: null,
        sprayPickSquare: null,
        sprayPlasticSquare: null,
        sprayElectroSquare: null,
        sprayPrimerSquare: null,
        sprayTopcoatSquare: null,
        totalSpray: null,
        nakedPrice: null,
        profit: null,
        transCost: null,
        totalPrice: null,
        noTax: null,
        perPrice: null
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
      this.title = "添加报价";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getQuote(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改报价";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateQuote(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addQuote(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除报价编号为"' + ids + '"的数据项？').then(function() {
        return delQuote(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/quote/export', {
        ...this.queryParams
      }, `quote_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
