<template>
  <div class="home">
    <el-alert :closable="false" title="公告" type="success">
      <p>只支持查询最近5笔订单</p>
    </el-alert>
    <el-card class="box-card" style="margin-top: 15px">
      <div class="my-title">
        <img src="../../../assets/images/category.svg"/>&nbsp;
        <span>订单查询</span>
      </div>
      <el-tabs style="margin-top: 20px">
        <el-tab-pane label="订单编号查询">
          <div style="width: 500px;margin: 20px auto">
            <el-form ref="formQueryByOrderNo" :model="formQueryByOrderNo" :rules="rules" label-width="80px">
              <el-form-item label="订单编号" prop="orderNo">
                <el-input v-model="formQueryByOrderNo.orderNo" clearable maxlength="25" show-word-limit></el-input>
              </el-form-item>
              <el-form-item label="查询密码" prop="queryPass">
                <el-input v-model="formQueryByOrderNo.queryPass" show-password></el-input>
              </el-form-item>
              <div align="center">
                <el-button round type="primary" @click="submitForm('formQueryByOrderNo')">立即查询</el-button>
                <!-- <el-button @click="resetForm('formQueryByOrderNo')">清空输入</el-button> -->
              </div>
            </el-form>
          </div>
        </el-tab-pane>
        <el-tab-pane label="联系方式查询">
          <div style="width: 500px;margin: 20px auto">
            <el-form ref="formQueryByContact" :model="formQueryByContact" :rules="rules" label-width="80px">
              <el-form-item label="联系方式" prop="contact">
                <el-input v-model="formQueryByContact.contact" clearable></el-input>
              </el-form-item>
              <el-form-item label="查询密码" prop="queryPass">
                <el-input v-model="formQueryByContact.queryPass" show-password></el-input>
              </el-form-item>
              <div align="center">
                <el-button round type="primary" @click="submitForm('formQueryByContact')">立即查询</el-button>
                <!-- <el-button @click="resetForm('formQueryByContact')">清空输入</el-button> -->
              </div>
            </el-form>
          </div>
        </el-tab-pane>
        <el-tab-pane label="浏览器缓存查询">浏览器缓存查询</el-tab-pane>
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
      width="1000px"
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
      width="1000px"
    >
      <el-card style="margin-bottom: 10px">
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
          <el-table-column
            label="操作"
            width="150">
            <template slot-scope="scope">
              <el-button v-if="scope.row.status == '3' || scope.row.status == '4' " size="small" type="text"
                         @click="handleClick(scope.row)">查看商品
              </el-button>
              <el-button v-if="scope.row.status == '0' " size="small" type="text" @click="handlePay(scope.row)">立即支付
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </el-dialog>
  </div>
</template>

<script>
import {getCardList, querySaleOrderByContact} from "@/api/sale/saleShop";
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
      itemData: null,
      dialogTableVisible: false,
      rules: {
        orderNo: [
          {required: true, message: '请输入订单编号', trigger: 'blur'},
          {min: 25, max: 25, message: '长度为25个字符', trigger: 'blur'}
        ],
        queryPass: [
          {required: true, message: '请输入查询密码', trigger: 'blur'}
        ],
        contact: [
          {required: true, message: '请输入联系方式', trigger: 'blur'},
        ],
      },
      saleOrderList: [],
      dialogTableVisible2: false,
    };
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
      querySaleOrderByContact(data).then((response) => {
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
              offset: 100,
            });
          }
        }
      })
        .finally(() => {
        });
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (formName == 'formQueryByOrderNo') {
            this.queryByOrderNo();
          } else if (formName == 'formQueryByContact') {
            this.queryByContact();
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
  }
}
</script>

<style>
.my-title span {
  font-weight: 600;
  font-size: 18px;
  color: #545454;
}

.my-title img {
  vertical-align: bottom;
}
</style>
