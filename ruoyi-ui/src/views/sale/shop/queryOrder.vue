<template>
  <div class="home">
<!--    <el-alert :closable="false" title="公告" type="info">-->
<!--      <p>只支持查询最近5笔订单</p>-->
<!--    </el-alert>-->
    <el-card class="box-card" style="max-width: 90vw; margin-top: 15px">
      <div class="my-title">
        <img src="../../../assets/images/category.svg"/>&nbsp;
        <span>订单查询</span>
      </div>
      <el-tabs style="margin-top: 20px">
        <el-tab-pane label="浏览器缓存查询">
          <div style="max-width: 90vw; width: 500px; margin: 20px auto">
            <el-form
              ref="formQueryByCookie"
              :model="formQueryByCookie"
              :rules="rules"
            >
              <div align="center">
                <el-button
                  round
                  type="primary"
                  @click="submitForm('formQueryByCookie')"
                >立即查询
                </el-button>
                <!-- <el-button @click="resetForm('formQueryByCookie')">清空输入</el-button> -->
              </div>
            </el-form>
          </div>
        </el-tab-pane>
        <el-tab-pane label="订单编号查询">
          <div style="max-width: 90vw; width: 500px; margin: 20px auto">
            <el-form
              ref="formQueryByOrderNo"
              :model="formQueryByOrderNo"
              :rules="rules"
            >
              <el-form-item label="订单编号" prop="orderNo">
                <el-input
                  v-model="formQueryByOrderNo.orderNo"
                  clearable
                  maxlength="18"
                  show-word-limit
                  style="max-width: 75vw"
                ></el-input>
              </el-form-item>
              <!-- <el-form-item label="查询密码" prop="queryPass">
                <el-input
                  v-model="formQueryByOrderNo.queryPass"
                  show-password
                  style="max-width: 75vw"
                ></el-input>
              </el-form-item> -->
              <div align="center">
                <el-button
                  round
                  type="primary"
                  @click="submitForm('formQueryByOrderNo')"
                >立即查询
                </el-button>
                <!-- <el-button @click="resetForm('formQueryByOrderNo')">清空输入</el-button> -->
              </div>
            </el-form>
          </div>
        </el-tab-pane>
        <el-tab-pane label="联系方式查询">
          <div style="max-width: 90vw; width: 500px; margin: 20px auto">
            <el-form
              ref="formQueryByContact"
              :model="formQueryByContact"
              :rules="rules"
            >
              <el-form-item label="联系方式" prop="contact">
                <el-input
                  v-model="formQueryByContact.contact"
                  clearable
                  style="max-width: 75vw"
                ></el-input>
              </el-form-item>
              <!-- <el-form-item label="查询密码" prop="queryPass">
                <el-input
                  v-model="formQueryByContact.queryPass"
                  show-password
                  style="max-width: 75vw"
                ></el-input>
              </el-form-item> -->
              <div align="center">
                <el-button
                  round
                  type="primary"
                  @click="submitForm('formQueryByContact')"
                >立即查询
                </el-button>
                <!-- <el-button @click="resetForm('formQueryByContact')">清空输入</el-button> -->
              </div>
            </el-form>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- Table -->
    <!-- <el-button type="text" @click="dialogTableVisible = true">
      模拟商品展示
    </el-button> -->
    <el-dialog
      :title="'您购买的商品如下，请妥善保存，订单编号：' + orderNo"
      :visible.sync="dialogTableVisible"
      custom-class="customClass"
      style="margin-top: 10vh; height: 80%"
      width="auto"
    >
      <item-data :itemData="itemData"></item-data>
    </el-dialog>

    <!-- Table -->
    <!-- <el-button type="text" @click="dialogTableVisible2 = true">
      模拟订单展示
    </el-button> -->
    <el-dialog
      :visible.sync="dialogTableVisible2"
      custom-class="customClass"
      style="margin-top: 10vh; height: 80%"
      title="您的订单信息如下"
      width="auto"
    >
      <el-card style="max-width: 90vw; margin-bottom: 10px">
        <el-table :data="saleOrderList" border>
          <el-table-column align="center" label="" type="index"/>
          <el-table-column label="订单编号">
            <template slot-scope="scope">
              {{ scope.row.orderNo }}
            </template>
          </el-table-column>
          <el-table-column label="订单时间">
            <template slot-scope="scope">
              {{ scope.row.createTime }}
            </template>
          </el-table-column>
          <el-table-column label="订单状态">
            <template slot-scope="scope">
              <dict-tag
                :options="dict.type.sale_order_status"
                :value="scope.row.status"
              />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150">
            <template slot-scope="scope">
              <el-button
                v-if="scope.row.status == '0'"
                size="small"
                type="text"
                @click="handlePay(scope.row)"
              >立即支付
              </el-button>
              <el-button
                v-if="scope.row.status == '1'"
                size="small"
                type="text"
                @click="handleFetch(scope.row)"
              >尝试提货
              </el-button>
              <el-button
                v-if="scope.row.status == '3' || scope.row.status == '4'"
                size="small"
                type="text"
                @click="handleClick(scope.row)"
              >查看商品
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </el-dialog>
  </div>
</template>

<script>
import Cookies from "js-cookie";
import {fetchGoods, getCardList, querySaleOrderByContact,} from "@/api/sale/saleShop";
import ItemData from "./card/ItemData";

export default {
  name: "QueryOrder",
  dicts: ["sale_order_status"],
  components: {ItemData},
  data() {
    return {
      orderNo: null,
      formQueryByOrderNo: {
        orderNo: null,
        queryPass: null,
      },
      formQueryByContact: {
        contact: null,
        queryPass: null,
      },
      formQueryByCookie: {},
      itemData: null,
      dialogTableVisible: false,
      rules: {
        orderNo: [
          {required: true, message: "请输入订单编号", trigger: "blur"},
          {min: 18, max: 18, message: "长度为18个字符", trigger: "blur"},
        ],
        // queryPass: [
        //   {required: true, message: "请输入查询密码", trigger: "blur"},
        // ],
        contact: [
          {required: true, message: "请输入联系方式", trigger: "blur"},
        ],
      },
      saleOrderList: [],
      dialogTableVisible2: false,
      appUrl: null,
      cardUrl: null,
    };
  },
  created() {
    this.appUrl = this.$route.params && this.$route.params.appUrl;
    this.cardUrl = this.$route.params && this.$route.params.cardUrl;
  },
  mounted() {
    if (!!this.$route.query.orderNo) {
      this.formQueryByOrderNo.orderNo = this.$route.query.orderNo;
      console.log("带参查询", this.formQueryByOrderNo.orderNo);
      setTimeout(() => {
        this.queryByOrderNo();
      }, 500);
    }
  },
  methods: {
    queryByOrderNo() {
      var data = {
        orderNo: this.formQueryByOrderNo.orderNo,
        queryPass: this.formQueryByOrderNo.queryPass,
      };
      getCardList(data)
        .then((response) => {
          if (response.code == 200) {
            // console.log(response)
            var itemList = response.itemList;
            this.itemData = [].concat(itemList);
            this.orderNo = this.formQueryByOrderNo.orderNo;
            this.dialogTableVisible = true;
          }
        })
        .finally(() => {
        });
    },
    queryByContact() {
      var data = {
        contact: this.formQueryByContact.contact,
        queryPass: this.formQueryByContact.queryPass,
      };
      querySaleOrderByContact(data)
        .then((response) => {
          if (response.code == 200) {
            // console.log(response)
            if (response.rows.length > 0) {
              this.saleOrderList = response.rows;
              this.dialogTableVisible2 = true;
            } else {
              this.$notify({
                title: "消息",
                dangerouslyUseHTMLString: true,
                message: "未查找到有效订单",
                type: "warning",
                offset: 300,
              });
            }
          }
        })
        .finally(() => {
        });
    },
    queryByCookie() {
      // var o = [{
      //   orderNo: "2203070057428750001000107",
      //   queryPass: "admin1234",
      // }];

      // Cookies.set('orderList', JSON.stringify(o));
      var orderListStr = Cookies.get("orderList");
      if (orderListStr) {
        var orderList = JSON.parse(orderListStr);
        if (orderList.length > 0) {
          if (orderList.length > 5) {
            orderList = orderList.slice(0, 5);
          }
          this.saleOrderList = [];
          for (var order of orderList) {
            var data = {
              orderNo: order.orderNo,
              queryPass: order.queryPass,
            };
            querySaleOrderByContact(data)
              .then((response) => {
                if (response.code == 200) {
                  // console.log(response)
                  if (response.rows.length > 0) {
                    this.saleOrderList = this.saleOrderList.concat(
                      response.rows
                    );
                    this.dialogTableVisible2 = true;
                  }
                }
              })
              .finally(() => {
              });
          }
        } else {
          this.$notify({
            title: "消息",
            dangerouslyUseHTMLString: true,
            message: "未查找到有效订单",
            type: "warning",
            offset: 100,
          });
        }
      } else {
        this.$notify({
          title: "消息",
          dangerouslyUseHTMLString: true,
          message: "未查找到有效订单",
          type: "warning",
          offset: 100,
        });
      }
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (formName == "formQueryByOrderNo") {
            this.queryByOrderNo();
          } else if (formName == "formQueryByContact") {
            this.queryByContact();
          } else if (formName == "formQueryByCookie") {
            this.queryByCookie();
          }
        } else {
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    handleClick(row) {
      var data = {
        orderNo: row.orderNo,
        queryPass: row.queryPass,
      };
      getCardList(data)
        .then((response) => {
          if (response.code == 200) {
            // console.log(response)
            var itemList = response.itemList;
            this.itemData = [].concat(itemList);
            this.orderNo = row.orderNo;
            this.dialogTableVisible = true;
          }
        })
        .finally(() => {
        });
    },
    handlePay(row) {
      const newPage = this.$router.resolve({
        path: "/billOrder",
        query: {orderNo: row.orderNo},
      });
      window.open(newPage.href, "_blank");
    },
    handleFetch(row) {
      // this.$notify({
      //   title: "消息",
      //   dangerouslyUseHTMLString: true,
      //   message: "在您支付完成前该商品已售罄，请联系店主处理",
      //   type: "warning",
      //   offset: 100,
      // });
      var data = {
        orderNo: row.orderNo,
        queryPass: row.queryPass,
      };
      fetchGoods(data)
        .then((response) => {
          if (response.code == 200) {
            // console.log(response)
            this.handleClick(row);
          }
        })
        .finally(() => {
        });
    },
  },
  beforeRouteLeave(to, from, next) {
    // console.log("/queryOrder to----", to); //跳转后路由
    // console.log("/queryOrder from----", from); //跳转前路由
    if(from.params.appUrl && from.params.appUrl !== '' && to.path.indexOf('/a/') === -1) {
      // console.log("/queryOrder newto.path----", to.path + '/a/' + from.params.appUrl);
      next({'path': to.path + '/a/' + from.params.appUrl})
    }
    if(from.params.cardUrl && from.params.cardUrl !== '' && to.path.indexOf('/c/') === -1) {
      // console.log("/queryOrder newto.path----", to.path + '/c/' + from.params.cardUrl);
      next({'path': to.path + '/c/' + from.params.cardUrl})
    }
    next();
  },
};
</script>

<style>
body {
  margin: 0;
}

a {
  text-decoration: None;
}

.home {
  width: 90vw;
  max-width: 1100px;
}

.my-title span {
  font-weight: 600;
  font-size: 18px;
  color: #545454;
}

.my-title img {
  vertical-align: bottom;
}

.customClass {
  max-width: 1000px;
}
</style>
