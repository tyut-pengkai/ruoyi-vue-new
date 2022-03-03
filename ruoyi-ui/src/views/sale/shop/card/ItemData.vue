<template>
  <div>
    <el-steps
      :active="activeStep"
      align-center
      finish-status="success"
      style="margin-bottom: 30px"
    >
      <el-step title="选择商品"></el-step>
      <el-step title="确认订单"></el-step>
      <el-step title="线上支付"></el-step>
      <el-step title="提取商品"></el-step>
    </el-steps>
    <div v-for="(item, index) in itemData" :key="index">
      <el-card style="margin-bottom: 10px">
        <div slot="header" class="clearfix">
          <span>{{ item.title }}</span>
          <!-- <el-button
            style="float: right; padding: 3px 0"
            type="text"
            id="copyButton"
            @click="copy"
            v-if="index==0"
          >
            复制全部
          </el-button> -->
        </div>
        <el-table :data="item.goodsList" border>
          <el-table-column label="" type="index" align="center" />
          <el-table-column
            :label="item.templateType == '1' ? '充值卡号' : '登录码'"
          >
            <template slot-scope="scope">
              {{ scope.row.cardNo }}
            </template>
          </el-table-column>
          <el-table-column label="充值密码" v-if="item.templateType == '1'">
            <template slot-scope="scope">
              {{ scope.row.cardPass }}
            </template>
          </el-table-column>
          <el-table-column label="过期时间">
            <template slot-scope="scope">
              {{ scope.row.expireTime }}
            </template>
          </el-table-column>
          <el-table-column label="充值规则" v-if="item.templateType == '1'">
            <template slot-scope="scope">
              <dict-tag
                :options="dict.type.sys_charge_rule"
                :value="scope.row.chargeRule"
              />
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
    <div align="center" style="margin-top: 15px">
      <el-button
        type="primary"
        id="copyButton"
        @click="copy"
      >
        一键复制全部
      </el-button>
    </div>
    
  </div>
</template>
<script>
import Clipboard from 'clipboard'; 
export default {
  name: "ItemData",
  dicts: ["sys_charge_rule"],
  components: {},
  props: {
    itemData: {
      type: Array,
      default: [],
    },
  },
  data() {
    return {
      activeStep: 3,
      // itemData: [
      // {
      //   title: "XXX软件-天卡",
      //   templateType: "1",
      //   goodsList: [
      //     {
      //       cardNo: "tbkgdzgaN3jhnnT23JKj",
      //       cardPass: "ZN6w395WnnXJrq3uxoAg",
      //       expireTime: "2022-03-02 23:31:16",
      //       chargeRule: "0",
      //     },
      //     {
      //       cardNo: "tbkgdzgaN3jhnnT23JKj",
      //       cardPass: "ZN6w395WnnXJrq3uxoAg",
      //       expireTime: "9999-12-31 23:59:59",
      //       chargeRule: "1",
      //     },
      //     {
      //       cardNo: "tbkgdzgaN3jhnnT23JKj",
      //       cardPass: "ZN6w395WnnXJrq3uxoAg",
      //       expireTime: "9999-12-31 23:59:59",
      //       chargeRule: "0",
      //     },
      //     {
      //       cardNo: "tbkgdzgaN3jhnnT23JKj",
      //       cardPass: "ZN6w395WnnXJrq3uxoAg",
      //       expireTime: "9999-12-31 23:59:59",
      //       chargeRule: "0",
      //     },
      //     {
      //       cardNo: "tbkgdzgaN3jhnnT23JKj",
      //       cardPass: "ZN6w395WnnXJrq3uxoAg",
      //       expireTime: "9999-12-31 23:59:59",
      //       chargeRule: "1",
      //     },
      //     {
      //       cardNo: "tbkgdzgaN3jhnnT23JKj",
      //       cardPass: "ZN6w395WnnXJrq3uxoAg",
      //       expireTime: "9999-12-31 23:59:59",
      //       chargeRule: "0",
      //     },
      //   ],
      // },
      // {
      //   title: 'XXX软件-月卡',
      //   templateType: '2',
      //   goodsList: [
      //     {
      //       cardNo: 'tbkgdzgaN3jhnnT23JKj',
      //       expireTime: '9999-12-31 23:59:59',
      //     }, {
      //       cardNo: 'tbkgdzgaN3jhnnT23JKj',
      //       expireTime: '9999-12-31 23:59:59',
      //     }
      //   ]
      // },
      // {
      //   title: 'XXX软件-年卡',
      //   templateType: '2',
      //   goodsList: [
      //     {
      //       cardName: '软件-天卡',
      //       cardNo: 'tbkgdzgaN3jhnnT23JKj',
      //       expireTime: '9999-12-31 23:59:59',
      //     }, {
      //       cardName: '软件-天卡',
      //       cardNo: 'tbkgdzgaN3jhnnT23JKj',
      //       expireTime: '9999-12-31 23:59:59',
      //     }
      //   ]
      // }
      // ],
    };
  },
  created() {},
  methods: {
    copy() {  
      var clipboard = new Clipboard('#copyButton', {
        text: () => { // 如果想从其它DOM元素内容复制。应该是target:function(){return: };
          var content = "";
          for(var item of this.itemData) {
            content += "========" + item.title + "========\n";
            for(var goods of item.goodsList) {
              content += goods.cardNo + "----";
              if(item.templateType == '1') {
                content += goods.cardPass + "----";
              }
              content += goods.expireTime;
              if(item.templateType == '1') {
                content += "----" + this.chargeRuleFormat(goods.chargeRule);
              }
              content += "\n";
            }
          }
          return content;
        }
      });
      clipboard.on('success', (e) => {
        this.$notify({
          title: "消息",
          dangerouslyUseHTMLString: true,
          message: "已成功复制，请您妥善保存",
          type: "success",
          offset: 100,
        });
        clipboard.destroy();
      });
      clipboard.on('error', (e) => {
        this.$notify({
          title: "消息",
          dangerouslyUseHTMLString: true,
          message: "复制失败，您的浏览器不支持复制，请自行妥善保存",
          type: "warning",
          offset: 100,
        });
        clipboard.destroy();
      });
    },
    chargeRuleFormat(code) {
      return this.selectDictLabel(this.dict.type.sys_charge_rule, code);
    },
  },
};
</script>