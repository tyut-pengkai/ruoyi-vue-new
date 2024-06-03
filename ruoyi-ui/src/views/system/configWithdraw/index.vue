<template>
  <div>
    <el-card style="margin: 10px; min-height: 85vh">
      <div slot="header">
        <span>提现设置</span>
      </div>
      <div>
        <el-form ref="form" :model="form" style="max-width: 1400px">
          <el-form-item label="是否开启提现功能">
            <el-radio-group v-model="form.enableWithdrawCash">
              <el-radio
                v-for="dict in dict.type.sys_yes_no"
                :key="dict.value"
                :label="dict.value"
              >{{ dict.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="最低提现金额">
            <span>
              <el-tooltip
                content="0为不限制"
                placement="top"
              >
                <i
                  class="el-icon-question"
                  style="margin-left: -12px; margin-right: 10px"
                ></i>
              </el-tooltip>
            </span>
            <el-input-number v-model="form.withdrawCashMin" :precision="2" :step="0.1" :min="0"></el-input-number>
          </el-form-item>
          <el-form-item label="最高提现金额">
            <span>
              <el-tooltip
                content="0为不限制"
                placement="top"
              >
                <i
                  class="el-icon-question"
                  style="margin-left: -12px; margin-right: 10px"
                ></i>
              </el-tooltip>
            </span>
            <el-input-number v-model="form.withdrawCashMax" :precision="2" :step="0.1" :min="0"></el-input-number>
          </el-form-item>
          <el-form-item label="手续费百分比">
            <span>
              <el-tooltip
                content="0为不收取手续费"
                placement="top"
              >
                <i
                  class="el-icon-question"
                  style="margin-left: -12px; margin-right: 10px"
                ></i>
              </el-tooltip>
            </span>
            <el-input-number v-model="form.handlingFeeRate" :precision="2" :step="0.1" :min="0" :max="100"></el-input-number>
          </el-form-item>
          <el-form-item label="最低手续费">
            <span>
              <el-tooltip
                content="0为不限制"
                placement="top"
              >
                <i
                  class="el-icon-question"
                  style="margin-left: -12px; margin-right: 10px"
                ></i>
              </el-tooltip>
            </span>
            <el-input-number v-model="form.handlingFeeMin" :precision="2" :step="0.1" :min="0"></el-input-number>
          </el-form-item>
          <el-form-item label="最高手续费">
            <span>
              <el-tooltip
                content="0为不限制"
                placement="top"
              >
                <i
                  class="el-icon-question"
                  style="margin-left: -12px; margin-right: 10px"
                ></i>
              </el-tooltip>
            </span>
            <el-input-number v-model="form.handlingFeeMax" :precision="2" :step="0.1" :min="0"></el-input-number>
          </el-form-item>
          <el-form-item label="是否开启支付宝提现">
            <el-radio-group v-model="form.enableAlipay">
              <el-radio
                v-for="dict in dict.type.sys_yes_no"
                :key="dict.value"
                :label="dict.value"
              >{{ dict.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="是否开启微信提现">
            <el-radio-group v-model="form.enableWechat">
              <el-radio
                v-for="dict in dict.type.sys_yes_no"
                :key="dict.value"
                :label="dict.value"
              >{{ dict.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="是否开启QQ提现">
            <el-radio-group v-model="form.enableQq">
              <el-radio
                v-for="dict in dict.type.sys_yes_no"
                :key="dict.value"
                :label="dict.value"
              >{{ dict.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="是否开启银行卡提现">
            <el-radio-group v-model="form.enableBankCard">
              <el-radio
                v-for="dict in dict.type.sys_yes_no"
                :key="dict.value"
                :label="dict.value"
              >{{ dict.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitForm">确 定</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
  </div>
</template>

<script>
import {getWithdrawConfig, updateWithdrawConfig} from "@/api/system/configWithdraw";

export default {
  name: "ConfigWithdraw",
  dicts: ["sys_normal_disable", "sys_yes_no"],
  data() {
    return {
      form: {
        enableWithdrawCash: "N",
        withdrawCashMin: "30",
        withdrawCashMax: "1000",
        handlingFeeRate: "5",
        handlingFeeMin: "0.1",
        handlingFeeMax: "20",
        enableAlipay: "N",
        enableWechat: "N",
        enableQq: "N",
        enableBankCard: "N",
      },
    };
  },
  created() {
    this.init();
  },
  methods: {
    init() {
      getWithdrawConfig().then((response) => {
        this.form = response.data;
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          updateWithdrawConfig(this.form).then((response) => {
            this.$modal.msgSuccess("修改成功");
          });
        }
      });
    },
  },
};
</script>
