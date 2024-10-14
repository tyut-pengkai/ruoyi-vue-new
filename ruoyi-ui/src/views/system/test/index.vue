<template>
  <div class="app-container">
    <div style="display:flex;align-items: center;">
      <h2 style="margin-top: 10px;font-size: 26px;font-weight: 500;">报价单</h2>
      <el-row :gutter="10" class="mb8" style="margin-left:auto;">
        <el-col :span="1.5">
          <el-button
            type="primary"
            plain
            icon="el-icon-check"
            @click="submitForm"
          >提交</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="danger"
            plain
            icon="el-icon-delete"
            @click="reset"
          >清空</el-button>
        </el-col>
      </el-row>
    </div>

    <el-table :data="quoteList">
      <el-table-column label="物料编码" align="center" prop="materialsNo">
        <template slot-scope="scope">
          <el-input
            v-model="form.materialsNo"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column label="物料名称" align="center" prop="name">
        <template slot-scope="scope">
          <el-input
            v-model="form.name"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column label="数量" align="center" prop="num">
        <template slot-scope="scope">
          <el-input
            v-model="form.num"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column label="是否外来" align="center" prop="flag">
        <template slot-scope="scope">
          <el-input
            v-model="form.flag"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column label="单价" align="center" prop="cost">
        <template slot-scope="scope">
          <el-input
            v-model="form.cost"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column label="材料规格" align="center" prop="materialSpec">
        <template slot-scope="scope">
          <el-input
            v-model="form.materialSpec"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column label="单件重量" align="center" prop="perWight">
        <template slot-scope="scope">
          <el-input
            v-model="form.perWight"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column label="净重(数量*单件重量)" align="center" prop="netWight">
        <template slot-scope="scope">
          <el-input
            v-model="form.netWight"
          ></el-input>
        </template>
      </el-table-column>
    </el-table>

    <el-table :data="quoteList">
      <el-table-column label="长" align="center" prop="steelLen">
        <template slot-scope="scope">
          <el-input
            v-model="form.steelLen"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column label="宽" align="center" prop="steelWid">
        <template slot-scope="scope">
          <el-input
            v-model="form.steelWid"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column label="高" align="center" prop="steelHei">
        <template slot-scope="scope">
          <el-input
            v-model="form.steelHei"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column label="毛坯重量" align="center" prop="steelWight">
        <template slot-scope="scope">
          <el-input
            v-model="form.steelWight"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column label="材料单价" align="center" prop="steelPerPrice">
        <template slot-scope="scope">
          <el-input
            v-model="form.steelPerPrice"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column label="毛坯费" align="center" prop="steelPrice">
        <template slot-scope="scope">
          <el-input
            v-model="form.steelPrice"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column label="废料重" align="center" prop="steelScrapWgt">
        <template slot-scope="scope">
          <el-input
            v-model="form.steelScrapWgt"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column label="废料单价" align="center" prop="steelScrapPer">
        <template slot-scope="scope">
          <el-input
            v-model="form.steelScrapPer"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column label="废料费" align="center" prop="steelScrapPrice">
        <template slot-scope="scope">
          <el-input
            v-model="form.steelScrapPrice"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column label="原料材料费用小计" align="center" prop="totalSteel">
        <template slot-scope="scope">
          <el-input
            v-model="form.totalSteel"
          ></el-input>
        </template>
      </el-table-column>
    </el-table>

    <el-table :data="quoteList">
      <el-table-column label="锯" align="center">
        <template slot-scope="scope">
           <div style="display:flex;align-items: center;justify-content: center;margin-bottom:5px;">工时：
            <el-input
              v-model="form.processSawTime"
              style="width: 50%;"
            ></el-input>
          </div>
          <div style="display:flex;align-items: center;justify-content: center;">金额：
            <el-input
              v-model="form.processSaw"
              style="width: 50%;"
            ></el-input>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="弯" align="center">
        <template slot-scope="scope">
          <div style="display:flex;align-items: center;justify-content: center;margin-bottom:5px;">工时：
            <el-input
              v-model="form.processBendTime"
              style="width: 50%;"
            ></el-input>
          </div>
          <div style="display:flex;align-items: center;justify-content: center;">金额：
            <el-input
              v-model="form.processBend"
              style="width: 50%;"
            ></el-input>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="钻" align="center">
        <template slot-scope="scope">
          <div style="display:flex;align-items: center;justify-content: center;margin-bottom:5px;">工时：
            <el-input
              v-model="form.processDrillTime"
              style="width: 50%;"
            ></el-input>
          </div>
          <div style="display:flex;align-items: center;justify-content: center;">金额：
            <el-input
              v-model="form.processDrill"
              style="width: 50%;"
            ></el-input>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="车" align="center">
        <template slot-scope="scope">
          <div style="display:flex;align-items: center;justify-content: center;margin-bottom:5px;">工时：
            <el-input
              v-model="form.processLatheTime"
              style="width: 50%;"
            ></el-input>
          </div>
          <div style="display:flex;align-items: center;justify-content: center;">金额：
            <el-input
              v-model="form.processLathe"
              style="width: 50%;"
            ></el-input>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="外磨" align="center">
        <template slot-scope="scope">
          <div style="display:flex;align-items: center;justify-content: center;margin-bottom:5px;">工时：
            <el-input
              v-model="form.processGrindTime"
              style="width: 50%;"
            ></el-input>
          </div>
          <div style="display:flex;align-items: center;justify-content: center;">金额：
            <el-input
              v-model="form.processGrind"
              style="width: 50%;"
            ></el-input>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="铣" align="center">
        <template slot-scope="scope">
          <div style="display:flex;align-items: center;justify-content: center;margin-bottom:5px;">工时：
            <el-input
              v-model="form.processMillTime"
              style="width: 50%;"
            ></el-input>
          </div>
          <div style="display:flex;align-items: center;justify-content: center;">金额：
            <el-input
              v-model="form.processMill"
              style="width: 50%;"
            ></el-input>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="校平" align="center">
        <template slot-scope="scope">
          <div style="display:flex;align-items: center;justify-content: center;margin-bottom:5px;">工时：
            <el-input
              v-model="form.processLevelTime"
              style="width: 50%;"
            ></el-input>
          </div>
          <div style="display:flex;align-items: center;justify-content: center;">金额：
            <el-input
              v-model="form.processLevel"
              style="width: 50%;"
            ></el-input>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="镗铣" align="center">
        <template slot-scope="scope">
          <div style="display:flex;align-items: center;justify-content: center;margin-bottom:5px;">工时：
            <el-input
              v-model="form.processBorTime"
              style="width: 50%;"
            ></el-input>
          </div>
          <div style="display:flex;align-items: center;justify-content: center;">金额：
            <el-input
              v-model="form.processBor"
              style="width: 50%;"
            ></el-input>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="焊" align="center">
        <template slot-scope="scope">
          <div style="display:flex;align-items: center;justify-content: center;margin-bottom:5px;">工时：
            <el-input
              v-model="form.processWeldTime"
              style="width: 50%;"
            ></el-input>
          </div>
          <div style="display:flex;align-items: center;justify-content: center;">金额：
            <el-input
              v-model="form.processWeld"
              style="width: 50%;"
            ></el-input>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="打磨" align="center">
        <template slot-scope="scope">
          <div style="display:flex;align-items: center;justify-content: center;margin-bottom:5px;">工时：
            <el-input
              v-model="form.processPolishTime"
              style="width: 50%;"
            ></el-input>
          </div>
          <div style="display:flex;align-items: center;justify-content: center;">金额：
            <el-input
              v-model="form.processPolish"
              style="width: 50%;"
            ></el-input>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="装" align="center">
        <template slot-scope="scope">
          <div style="display:flex;align-items: center;justify-content: center;margin-bottom:5px;">工时：
            <el-input
              v-model="form.processPackTime"
              style="width: 50%;"
            ></el-input>
          </div>
          <div style="display:flex;align-items: center;justify-content: center;">金额：
            <el-input
              v-model="form.processPack"
              style="width: 50%;"
            ></el-input>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="加工费小计" align="center" prop="totalProcess">
        <template slot-scope="scope">
          <el-input
            v-model="form.totalProcess"
          ></el-input>
        </template>
      </el-table-column>
    </el-table>

    <el-table :data="quoteList">
      <el-table-column label="镀锌" align="center" prop="surfaceGalvanizedWgt">
        <template slot-scope="scope">
          <div style="display:flex;align-items: center;justify-content: center;margin-bottom:5px;">重量：
            <el-input
              v-model="form.surfaceGalvanizedWgt"
              style="width: 60%;"
            ></el-input>
          </div>
          <div style="display:flex;align-items: center;justify-content: center;">金额：
            <el-input
              v-model="form.surfaceGalvanized"
              style="width: 60%;"
            ></el-input>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="调质" align="center" prop="surfaceConditWgt">
        <template slot-scope="scope">
          <div style="display:flex;align-items: center;justify-content: center;margin-bottom:5px;">重量：
            <el-input
              v-model="form.surfaceConditWgt"
              style="width: 60%;"
            ></el-input>
          </div>
          <div style="display:flex;align-items: center;justify-content: center;">金额：
            <el-input
              v-model="form.surfaceCondit"
              style="width: 60%;"
            ></el-input>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="冲砂" align="center" prop="surfaceSandwashWgt">
        <template slot-scope="scope">
          <div style="display:flex;align-items: center;justify-content: center;margin-bottom:5px;">重量：
            <el-input
              v-model="form.surfaceSandwashWgt"
              style="width: 60%;"
            ></el-input>
          </div>
          <div style="display:flex;align-items: center;justify-content: center;">金额：
            <el-input
              v-model="form.surfaceSandwash"
              style="width: 60%;"
            ></el-input>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="QPQ" align="center" prop="surfacePqpWgt">
        <template slot-scope="scope">
          <div style="display:flex;align-items: center;justify-content: center;margin-bottom:5px;">重量：
            <el-input
              v-model="form.surfacePqpWgt"
              style="width: 60%;"
            ></el-input>
          </div>
          <div style="display:flex;align-items: center;justify-content: center;">金额：
            <el-input
              v-model="form.surfacePqp"
              style="width: 60%;"
            ></el-input>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="表面处理费小计" align="center" prop="totalSurface">
        <template slot-scope="scope">
          <el-input
            v-model="form.totalSurface"
          ></el-input>
        </template>
      </el-table-column>
    </el-table>

    <el-table :data="quoteList">
      <el-table-column label="酸洗" align="center" prop="sprayPickSquare">
        <template slot-scope="scope">
          <div style="display:flex;align-items: center;justify-content: center;margin-bottom:5px;">面积：
            <el-input
              v-model="form.sprayPickSquare"
              style="width: 60%;"
            ></el-input>
          </div>
          <div style="display:flex;align-items: center;justify-content: center;">金额：
            <el-input
              v-model="form.sparyWashpickling"
              style="width: 60%;"
            ></el-input>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="喷塑" align="center" prop="sprayPlasticSquare">
        <template slot-scope="scope">
          <div style="display:flex;align-items: center;justify-content: center;margin-bottom:5px;">面积：
            <el-input
              v-model="form.sprayPlasticSquare"
              style="width: 60%;"
            ></el-input>
          </div>
          <div style="display:flex;align-items: center;justify-content: center;">金额：
            <el-input
              v-model="form.sprayPlastic"
              style="width: 60%;"
            ></el-input>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="电泳" align="center" prop="sprayElectroSquare">
        <template slot-scope="scope">
          <div style="display:flex;align-items: center;justify-content: center;margin-bottom:5px;">面积：
            <el-input
              v-model="form.sprayElectroSquare"
              style="width: 60%;"
            ></el-input>
          </div>
          <div style="display:flex;align-items: center;justify-content: center;">金额：
            <el-input
              v-model="form.sprayElectro"
              style="width: 60%;"
            ></el-input>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="底漆" align="center" prop="sprayPrimerSquare">
        <template slot-scope="scope">
          <div style="display:flex;align-items: center;justify-content: center;margin-bottom:5px;">面积：
            <el-input
              v-model="form.sprayPrimerSquare"
              style="width: 60%;"
            ></el-input>
          </div>
          <div style="display:flex;align-items: center;justify-content: center;">金额：
            <el-input
              v-model="form.sprayPrimer"
              style="width: 60%;"
            ></el-input>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="面漆" align="center" prop="sprayTopcoatSquare">
        <template slot-scope="scope">
          <div style="display:flex;align-items: center;justify-content: center;margin-bottom:5px;">面积：
            <el-input
              v-model="form.sprayTopcoatSquare"
              style="width: 60%;"
            ></el-input>
          </div>
          <div style="display:flex;align-items: center;justify-content: center;">金额：
            <el-input
              v-model="form.sprayTopcoat"
              style="width: 60%;"
            ></el-input>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="喷涂费用小计" align="center" prop="totalSpray">
        <template slot-scope="scope">
          <el-input
            v-model="form.totalSpray"
          ></el-input>
        </template>
      </el-table-column>
    </el-table>

    <el-table :data="quoteList">
      <el-table-column label="裸价(元)" align="center" prop="nakedPrice">
        <template slot-scope="scope">
          <el-input
            v-model="form.nakedPrice"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column label="利润" align="center" prop="profit">
        <template slot-scope="scope">
          <el-input
            v-model="form.profit"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column label="包装运输" align="center" prop="transCost">
        <template slot-scope="scope">
          <el-input
            v-model="form.transCost"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column label="产品合计报价" align="center" prop="totalPrice">
        <template slot-scope="scope">
          <el-input
            v-model="form.totalPrice"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column label="未税" align="center" prop="noTax">
        <template slot-scope="scope">
          <el-input
            v-model="form.noTax"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column label="公斤价" align="center" prop="perPrice">
        <template slot-scope="scope">
          <el-input
            v-model="form.perPrice"
          ></el-input>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { listQuote, getQuote, delQuote, addQuote, updateQuote } from "@/api/system/quote";

export default {
  name: "Test",
  data() {
    return {
      // 详细报价表格数据
      quoteList: [{
        quoteNo: null,
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
        perPrice: null,
      }],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        quoteNo: [
          { required: true, message: "报价单编号不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
  },
  methods: {
    // 表单重置
    reset() {
      this.form = {
        id: null,
        quoteNo: null,
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
        perPrice: null,
        createTime: null,
        updateTime: null
      };
    },
    /** 提交按钮 */
    submitForm() {
      addQuote(this.form).then(response => {
        this.$modal.msgSuccess("新增成功");
      });
    }
  }
};
</script>
