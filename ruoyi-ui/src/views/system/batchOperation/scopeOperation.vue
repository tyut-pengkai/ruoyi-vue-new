<template>
  <div>
    <el-form ref="form" :model="form" :rules="rules" style="max-width: 1400px">
      <el-form-item>
        <el-row :gutter="2">
          <el-col :lg="4" :md="4" :sm="12" :xl="4" :xs="24">
            <el-form-item label="软件" prop="appId">
              <el-select
                v-model="form.appId"
                clearable
                filterable
                placeholder="请选择"
                prop="appId"
                style="width: 150px"
                @change="changeSearchApp"
              >
                <el-option
                  v-for="item in appList"
                  :key="item.appId"
                  :disabled="item.disabled"
                  :label="
                    item.appId != 0
                      ? '[' +
                        (item.authType == '0' ? '账号' : '单码') +
                        (item.billType == '0' ? '计时' : '计点') +
                        '] ' +
                        item.appName
                      : item.appName
                  "
                  :value="item.appId"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :lg="8" :md="8" :sm="12" :xl="8" :xs="24">
            <el-form-item label="卡类/单码类别" prop="templateId">
              <el-select
                v-model="form.templateId"
                clearable
                filterable
                placeholder="请选择"
                prop="templateId"
                style="width: 150px"
              >
                <el-option
                  v-for="item in templateList"
                  :key="item.templateId"
                  :disabled="item.disabled"
                  :label="
                    item.templateId != 0
                      ? '[' + item.app.appName + ']' + item.cardName
                      : item.cardName
                  "
                  :value="item.templateId"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="2" style="margin-top: 20px">
          <el-col :lg="4" :md="4" :sm="12" :xl="4" :xs="24">
            <el-form-item label="对象" prop="operationObject">
              <el-select
                v-model="form.operationObject"
                clearable
                filterable
                placeholder="请选择操作对象"
                prop="operationObject"
                style="width: 150px"
              >
                <el-option
                  v-for="dict in dict.type.sys_batch_operation_object"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :lg="4" :md="4" :sm="12" :xl="4" :xs="24">
            <el-form-item label="范围" prop="operationScope">
              <el-select
                v-model="form.operationScope"
                clearable
                filterable
                placeholder="请选择操作范围"
                prop="operationScope"
                style="width: 150px"
              >
                <el-option
                  v-for="dict in dict.type.sys_batch_operation_scope"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col
            v-show="form.operationObject === '6'"
            :lg="4"
            :md="4"
            :sm="12"
            :xl="4"
            :xs="24"
          >
            <el-form-item label="VIP" prop="operationAppUserType">
              <el-select
                v-model="form.operationAppUserType"
                clearable
                filterable
                placeholder="请选择用户类别"
                prop="operationAppUserType"
                style="width: 150px"
              >
                <el-option
                  v-for="dict in dict.type.sys_batch_operation_app_user_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :lg="4" :md="4" :sm="12" :xl="4" :xs="24">
            <el-form-item label="操作" prop="operationType">
              <el-select
                v-model="form.operationType"
                clearable
                filterable
                placeholder="请选择执行操作"
                prop="operationType"
                style="width: 150px"
              >
                <el-option
                  v-for="dict in dict.type.sys_batch_operation_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col
            v-show="
              form.operationType === '5' ||
              form.operationType === '6' ||
              form.operationType === '7' ||
              form.operationType === '8' ||
              form.operationType === '9'
            "
            :lg="7"
            :md="7"
            :sm="12"
            :xl="7"
            :xs="24"
          >
            <el-form-item>
              <el-form-item label="参数(单位为秒或点)" prop="operationValue">
                <el-input
                  v-model="form.operationValue"
                  placeholder="加时/扣时/加点/扣点/备注时需填写"
                  style="width: 200px"
                >
                </el-input>
              </el-form-item>
            </el-form-item>
          </el-col>
          <el-col :lg="1" :md="1" :sm="24" :xl="1" :xs="24">
            <el-button
              :loading="loading"
              style="margin-left: -20px"
              type="primary"
              @click="submitForm"
            >执 行
            </el-button>
          </el-col>
        </el-row>
      </el-form-item>
    </el-form>
    <el-input
      v-model="result"
      :rows="20"
      style="max-width: 1400px"
      type="textarea"
    >
    </el-input>
  </div>
</template>

<script>
import {listAppAll} from "@/api/system/app";
import {listCardTemplate} from "@/api/system/cardTemplate";
import {listLoginCodeTemplate} from "@/api/system/loginCodeTemplate";
import {scopeOperation} from "@/api/system/batchOperation";

export default {
  name: "scopeOperation",
  dicts: [
    "sys_batch_operation_type",
    "sys_batch_operation_object",
    "sys_batch_operation_scope",
    "sys_batch_operation_app_user_type",
  ],
  data() {
    return {
      appList: [],
      templateList: [],
      appMap: [],
      loading: true,
      form: {
        appId: null,
        templateId: null,
        operationValue: "",
        operationType: "",
        operationObject: "",
        operationScope: "",
        operationAppUserType: "",
      },
      queryParams: {
        appId: null,
      },
      rules: {
        appId: [
          {required: true, message: "目标软件不能为空", trigger: "blur"},
        ],
        templateId: [
          {required: true, message: "卡类不能为空", trigger: "blur"},
        ],
        operationType: [
          {required: true, message: "执行操作不能为空", trigger: "blur"},
        ],
        operationObject: [
          {required: true, message: "操作对象不能为空", trigger: "blur"},
        ],
        operationScope: [
          {required: true, message: "操作范围不能为空", trigger: "blur"},
        ],
        // operationAppUserType: [
        //   {
        //     required: true,
        //     message: "VIP范围不能为空",
        //     trigger: "blur",
        //   },
        // ],
      },
      result: "",
    };
  },
  created() {
    this.getAppList();
  },
  methods: {
    getAppList() {
      this.loading = true;
      let queryParams = {};
      queryParams.params = {};
      listAppAll(queryParams).then((response) => {
        this.appList = response.rows;
        for (let app of this.appList) {
          this.appMap[app["appId"]] = app;
        }
        this.loading = false;
      });
    },
    changeSearchApp(appId) {
      this.loading = true;
      this.form.templateId = null;
      this.app = this.appMap[appId];
      let queryParams = {};
      if (appId > 0) {
        queryParams = {
          appId: appId,
        };
      }
      if (this.app && this.app["authType"] == "0") {
        listCardTemplate(queryParams).then((response) => {
          this.templateList = response.rows;
          this.templateList.unshift({
            templateId: 0,
            cardName: "全部卡类",
          });
          this.loading = false;
        });
      } else if (this.app && this.app["authType"] == "1") {
        listLoginCodeTemplate(queryParams).then((response) => {
          this.templateList = response.rows;
          this.templateList.unshift({
            templateId: 0,
            cardName: "全部类别",
          });
          this.loading = false;
        });
      }
    },
    submitForm() {
      this.loading = true;
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.operationType === "4") {
            this.$modal
              .confirm(
                "您正在执行删除操作，数据无价，建议备份好全部数据后再进行此操作，是否继续？"
              )
              .then(() => {
                scopeOperation(this.form).then((response) => {
                  this.result = response.msg;
                });
              })
              .catch(() => {
              })
              .finally(() => {
                this.loading = false;
              });
          } else {
            scopeOperation(this.form).then((response) => {
              this.result = response.msg;
              this.loading = false;
            });
          }
        }
      });
    },
  },
};
</script>
