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
          <el-table-column label="充值过期（请在此时间前充值）">
            <template slot-scope="scope">
              {{
                !scope.row.expireTime ||
                scope.row.expireTime == "9999-12-31 23:59:59"
                  ? "长期有效"
                  : scope.row.expireTime
              }}
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
      <el-button id="copyButton" type="primary" @click="copy">
        复制文本
      </el-button>
      <el-button id="saveButton" class="my-class" type="primary" @click="save">
        下载保存
      </el-button>
    </div>
  </div>
</template>
<script>
import Clipboard from "clipboard";

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
    generateText() {
      var content = "";
      for (var item of this.itemData) {
        content +=
          "========" +
          item.title +
          "（共" +
          item.goodsList.length +
          "张）========\n";
        for (var index in item.goodsList) {
          var goods = item.goodsList[index];
          content += "第" + (parseInt(index) + 1) + "张\n";
          if (item.templateType == "1") {
            content += "充值卡号：" + goods.cardNo + "\n";
            content += "充值密码：" + goods.cardPass + "\n";
            if (goods.expireTime && goods.expireTime != "9999-12-31 23:59:59") {
              content +=
                "充值过期（请在此时间前充值）：" +
                (!goods.expireTime || goods.expireTime == "9999-12-31 23:59:59"
                  ? "长期有效"
                  : goods.expireTime) +
                "\n";
            }
            if (goods.chargeRule && goods.chargeRule != "0") {
              content +=
                "充值规则：" + this.chargeRuleFormat(goods.chargeRule) + "\n";
            }
          } else if (item.templateType == "2") {
            content += "登录码：" + goods.cardNo + "\n";
            content +=
              "充值过期（请在此时间前充值）：" +
              (!goods.expireTime || goods.expireTime == "9999-12-31 23:59:59"
                ? "长期有效"
                : goods.expireTime) +
              "\n";
          }
          content += "\n";
        }
      }
      return content;
    },
    copy() {
      var clipboard = new Clipboard("#copyButton", {
        text: () => {
          // 如果想从其它DOM元素内容复制。应该是target:function(){return: };
          return this.generateText();
        },
      });
      clipboard.on("success", (e) => {
        this.$notify({
          title: "消息",
          dangerouslyUseHTMLString: true,
          message: "已成功复制，请您妥善保存",
          type: "success",
          offset: 100,
        });
        clipboard.destroy();
      });
      clipboard.on("error", (e) => {
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
    download(filename, text) {
      var pom = document.createElement("a");
      pom.setAttribute(
        "href",
        "data:text/plain;charset=utf-8," + encodeURIComponent(text)
      );
      pom.setAttribute("download", filename);
      if (document.createEvent) {
        var event = document.createEvent("MouseEvents");
        event.initEvent("click", true, true);
        pom.dispatchEvent(event);
      } else {
        pom.click();
      }
    },
    /** * 对Date的扩展，将 Date 转化为指定格式的String * 月(M)、日(d)、12小时(h)、24小时(H)、分(m)、秒(s)、周(E)、季度(q)
     可以用 1-2 个占位符 * 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) * eg: * (new
     Date()).pattern("yyyy-MM-dd hh:mm:ss.S")==> 2006-07-02 08:09:04.423
     * (new Date()).pattern("yyyy-MM-dd E HH:mm:ss") ==> 2009-03-10 二 20:09:04
     * (new Date()).pattern("yyyy-MM-dd EE hh:mm:ss") ==> 2009-03-10 周二 08:09:04
     * (new Date()).pattern("yyyy-MM-dd EEE hh:mm:ss") ==> 2009-03-10 星期二 08:09:04
     * (new Date()).pattern("yyyy-M-d h:m:s.S") ==> 2006-7-2 8:9:4.18
     */
    dateFormat(date, fmt) {
      var o = {
        "M+": date.getMonth() + 1, //月份
        "d+": date.getDate(), //日
        "h+": date.getHours() % 12 == 0 ? 12 : date.getHours() % 12, //小时
        "H+": date.getHours(), //小时
        "m+": date.getMinutes(), //分
        "s+": date.getSeconds(), //秒
        "q+": Math.floor((date.getMonth() + 3) / 3), //季度
        S: date.getMilliseconds(), //毫秒
      };
      var week = {
        0: "/u65e5",
        1: "/u4e00",
        2: "/u4e8c",
        3: "/u4e09",
        4: "/u56db",
        5: "/u4e94",
        6: "/u516d",
      };
      if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(
          RegExp.$1,
          (date.getFullYear() + "").substr(4 - RegExp.$1.length)
        );
      }
      if (/(E+)/.test(fmt)) {
        fmt = fmt.replace(
          RegExp.$1,
          (RegExp.$1.length > 1
            ? RegExp.$1.length > 2
              ? "/u661f/u671f"
              : "/u5468"
            : "") + week[date.getDay() + ""]
        );
      }
      for (var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
          fmt = fmt.replace(
            RegExp.$1,
            RegExp.$1.length == 1
              ? o[k]
              : ("00" + o[k]).substr(("" + o[k]).length)
          );
        }
      }
      return fmt;
    },
    save() {
      this.download(
        "save_" + this.dateFormat(new Date(), "yyyyMMddHHmmss") + ".txt",
        this.generateText()
      );
    },
    chargeRuleFormat(code) {
      return this.selectDictLabel(this.dict.type.sys_charge_rule, code);
    },
  },
};
</script>
<style scoped>
@media screen and (max-width: 1199px) {
  .my-class {
    display: none;
  }
}
</style>
