<template>
  <div>
    <el-card style="margin: 10px; min-height: 85vh">
      <div slot="header">
        <span>批量操作</span>
      </div>
      <div>
        <el-form
          ref="form"
          :model="form"
          :rules="rules"
          style="max-width: 1400px"
        >
          <el-form-item
            label="操作内容（输入格式：每行一条数据）"
            prop="content"
          >
            <el-input
              v-model="form.content"
              :rows="20"
              placeholder="输入格式：每行一条数据"
              type="textarea"
            >
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-row :gutter="2">
              <el-col :lg="5" :md="5" :sm="12" :xl="5" :xs="24">
                <el-form-item label="软件" prop="appId">
                  <el-select
                    v-model="form.appId"
                    clearable
                    filterable
                    placeholder="请选择"
                    prop="appId"
                    style="width: 200px"
                    @change="changeSearchApp"
                  >
                    <el-option
                      v-for="item in appList"
                      :key="item.appId"
                      :disabled="item.disabled"
                      :label="
                        '[' +
                        (item.authType == '0' ? '账号' : '单码') +
                        (item.billType == '0' ? '计时' : '计点') +
                        '] ' +
                        item.appName
                      "
                      :value="item.appId"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :lg="5" :md="5" :sm="12" :xl="5" :xs="24">
                <el-form-item label="对象" prop="operationObject">
                  <el-select
                    v-model="form.operationObject"
                    clearable
                    filterable
                    placeholder="请选择操作对象"
                    prop="operationObject"
                    style="width: 200px"
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
              <el-col :lg="5" :md="5" :sm="12" :xl="5" :xs="24">
                <el-form-item label="操作" prop="operationType">
                  <el-select
                    v-model="form.operationType"
                    clearable
                    filterable
                    placeholder="请选择执行操作"
                    prop="operationType"
                    style="width: 200px"
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
              <el-col :lg="8" :md="8" :sm="12" :xl="8" :xs="24">
                <el-form-item>
                  <el-form-item
                    label="参数(单位为秒或点)"
                    prop="operationValue"
                  >
                    <el-input
                      v-model="form.operationValue"
                      placeholder="加时/扣时/加点/扣点/备注时需填写"
                      style="width: 260px"
                    >
                    </el-input>
                  </el-form-item>
                </el-form-item>
              </el-col>
              <el-col :lg="1" :md="1" :sm="24" :xl="1" :xs="24">
                <el-button
                  style="margin-left: -20px"
                  type="primary"
                  @click="submitForm"
                >执 行
                </el-button
                >
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
    </el-card>
  </div>
</template>

<script>
import {listAppAll} from "@/api/system/app";
import {batchOperation} from "@/api/system/batchOperation";

export default {
  name: "batchOperation",
  dicts: ["sys_batch_operation_type", "sys_batch_operation_object"],
  data() {
    return {
      appList: [],
      appMap: [],
      loading: true,
      form: {
        content: "",
        appId: null,
        operationValue: "",
        operationType: "0",
        operationObject: "0",
      },
      queryParams: {
        appId: null,
      },
      rules: {
        content: [
          {required: true, message: "操作内容不能为空", trigger: "blur"},
        ],
        appId: [
          {required: true, message: "目标软件不能为空", trigger: "blur"},
        ],
        operationType: [
          {required: true, message: "执行操作不能为空", trigger: "blur"},
        ],
        operationObject: [
          {required: true, message: "操作对象不能为空", trigger: "blur"},
        ],
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
      // this.loading = true;
      // this.app = this.appMap[appId];
      // this.loading = false;
    },
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          batchOperation(this.form).then((response) => {
            this.result = response.msg;
          });
        }
      });
    },
  },
};
</script>
