<template>
  <div class="app-container">
    <el-form
      v-show="showSearch"
      ref="queryForm"
      :inline="true"
      :model="queryParams"
      size="small"
    >
      <!-- <el-form-item label="软件id" prop="appId">
        <el-input
          v-model="queryParams.appId"
          placeholder="请输入软件id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="所属软件" prop="appId">
        <el-select
          v-model="queryParams.appId"
          clearable
          filterable
          placeholder="请选择"
          prop="appId"
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
      <!-- <el-form-item label="软件用户id" prop="appUserId">
        <el-input
          v-model="queryParams.appUserId"
          placeholder="请输入软件用户id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="用户账号/单码" prop="userName">
        <el-input
          v-model="queryParams.userName"
          clearable
          placeholder="请输入用户账号或单码"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="变动面值" prop="changeAmount">
        <el-input
          v-model="queryParams.changeAmount"
          clearable
          placeholder="请输入变动面值"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="变动类型" prop="changeType">
        <el-select
          v-model="queryParams.changeType"
          clearable
          placeholder="请选择"
        >
          <el-option
            v-for="dict in dict.type.sys_app_user_expire_change_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="用户过期时间前" prop="expireTimeBefore">
        <el-date-picker
          clearable
          v-model="queryParams.expireTimeBefore"
          type="datetime"
          placeholder="请选择用户过期时间前"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item label="用户过期时间后" prop="expireTimeAfter">
        <el-date-picker
          clearable
          v-model="queryParams.expireTimeAfter"
          type="datetime"
          placeholder="请选择用户过期时间后"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item label="用户剩余点数前" prop="pointBefore">
        <el-input
          v-model="queryParams.pointBefore"
          placeholder="请输入用户剩余点数前"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户剩余点数后" prop="pointAfter">
        <el-input
          v-model="queryParams.pointAfter"
          placeholder="请输入用户剩余点数后"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <!-- <el-form-item label="关联卡类ID" prop="templateId">
        <el-input
          v-model="queryParams.templateId"
          placeholder="请输入关联卡类ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="关联卡ID" prop="cardId">
        <el-input
          v-model="queryParams.cardId"
          placeholder="请输入关联卡ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="关联卡号" prop="cardNo">
        <el-input
          v-model="queryParams.cardNo"
          clearable
          placeholder="请输入关联卡号"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="变动描述" prop="changeDesc">
        <el-input
          v-model="queryParams.changeDesc"
          clearable
          placeholder="请输入变动描述"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input
          v-model="queryParams.remark"
          placeholder="请输入备注"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button
          icon="el-icon-search"
          size="mini"
          type="primary"
          @click="handleQuery"
        >搜索
        </el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
        >重置
        </el-button
        >
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!-- <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:appUserExpireLog:add']"
          >新增</el-button
        >
      </el-col> -->
<!--      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:appUserExpireLog:edit']"
          :disabled="single"
          icon="el-icon-edit"
          plain
          size="mini"
          type="success"
          @click="handleUpdate"
        >修改
        </el-button
        >
      </el-col>-->
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:appUserExpireLog:remove']"
          :disabled="multiple"
          icon="el-icon-delete"
          plain
          size="mini"
          type="danger"
          @click="handleDelete"
        >删除
        </el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          icon="el-icon-delete"
          plain
          type="danger"
          size="mini"
          @click="handleClean"
          v-hasPermi="['system:appUserExpireLog:remove']"
        >清空
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:appUserExpireLog:export']"
          icon="el-icon-download"
          plain
          size="mini"
          type="warning"
          @click="handleExport"
        >导出
        </el-button
        >
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="appUserExpireLogList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column align="center" type="selection" width="55"/>
      <el-table-column align="center" label="序号" prop="id"/>
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="所属软件"
      >
        <template slot-scope="scope">
          {{ scope.row.app.appName }}
        </template>
      </el-table-column>
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="账号/单码"
      >
        <template slot-scope="scope">
          <div v-if="scope.row.app.authType == '0'">
            {{ scope.row.appUser.user.nickName }}({{
              scope.row.appUser.user.userName
            }})
          </div>
          <div v-else>
            {{ "[单码]" + scope.row.appUser.loginCode }}
          </div>
        </template>
      </el-table-column>
      <el-table-column align="center" label="变动类型" prop="changeType">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_app_user_expire_change_type"
            :value="scope.row.changeType"
          />
        </template>
      </el-table-column>
      <el-table-column align="center" label="变动面值" prop="changeAmount"/>
      <el-table-column
        align="center"
        label="过期时间(变动前)"
        prop="expireTimeBefore"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.expireTimeBefore) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="过期时间(变动后)"
        prop="expireTimeAfter"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.expireTimeAfter) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="剩余点数(变动前)"
        prop="pointBefore"
      />
      <el-table-column
        align="center"
        label="剩余点数(变动后)"
        prop="pointAfter"
      />
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="变动描述"
        prop="changeDesc"
      />
      <!-- <el-table-column label="关联卡类ID" align="center" prop="templateId" />
      <el-table-column label="关联卡ID" align="center" prop="cardId" /> -->
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="关联卡号"
        prop="cardNo"
      />
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="备注"
        prop="remark"
      />
      <el-table-column
        align="center"
        class-name="small-padding fixed-width"
        label="操作"
      >
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['system:appUserExpireLog:edit']"
            icon="el-icon-edit"
            size="mini"
            type="text"
            @click="handleUpdate(scope.row)"
          >详情
          </el-button
          >
<!--          <el-button
            v-hasPermi="['system:appUserExpireLog:remove']"
            icon="el-icon-delete"
            size="mini"
            type="text"
            @click="handleDelete(scope.row)"
          >删除
          </el-button
          >-->
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :limit.sync="queryParams.pageSize"
      :page.sync="queryParams.pageNum"
      :total="total"
      @pagination="getList"
    />

    <!-- 添加或修改时长或点数变动对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="800px">
      <el-form ref="form" :model="form" :rules="rules">
        <!-- <el-form-item label="软件id" prop="appId">
          <el-input v-model="form.appId" placeholder="请输入软件id" />
        </el-form-item>
        <el-form-item label="软件用户id" prop="appUserId">
          <el-input v-model="form.appUserId" placeholder="请输入软件用户id" />
        </el-form-item> -->
        <div v-if="form.id && form.app">
          <el-form-item prop="">
            <el-col :span="8">
              <el-form-item label="所属软件" prop="appName">
                {{ form.app.appName }}
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <div v-if="form.app.authType === '0'">
                <el-form-item label="所属账号" prop="userId">
                  {{ form.appUser.user.nickName }}({{
                    form.appUser.user.userName
                  }})
                </el-form-item>
              </div>
              <div v-if="form.app.authType === '1'">
                <el-form-item label="单码" prop="loginCode">
                  {{ form.appUser.loginCode }}
                </el-form-item>
              </div>
            </el-col>
            <el-col :span="8">
              <el-form-item label="计费类型" prop="billType">
                <dict-tag
                  :options="dict.type.sys_bill_type"
                  :value="form.app.billType"
                />
              </el-form-item>
            </el-col>
          </el-form-item>
        </div>
        <el-form-item prop="">
          <el-col :span="12">
            <el-form-item label="变动面值" prop="changeAmount">
              <!-- <el-input
                v-model="form.changeAmount"
                placeholder="请输入变动面值"
              /> -->
              {{ form.changeAmount }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="变更类型" prop="changeType">
              <!-- <el-select v-model="form.changeType" placeholder="请选择">
                <el-option
                  v-for="dict in dict.type.sys_app_user_expire_change_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select> -->
              <dict-tag
                :options="dict.type.sys_app_user_expire_change_type"
                :value="form.changeType"
              />
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item prop="">
          <el-col :span="12">
            <el-form-item label="过期时间(变动前)" prop="expireTimeBefore">
              <!-- <el-date-picker
                clearable
                v-model="form.expireTimeBefore"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="请选择用户过期时间"
              >
              </el-date-picker> -->
              {{ form.expireTimeBefore }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="过期时间(变动后)" prop="expireTimeAfter">
              <!-- <el-date-picker
                clearable
                v-model="form.expireTimeAfter"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="请选择用户过期时间"
              >
              </el-date-picker> -->
              {{ form.expireTimeAfter }}
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item prop="">
          <el-col :span="12"
          >
            <el-form-item label="剩余点数(变动前)" prop="pointBefore">
              <!-- <el-input
                v-model="form.pointBefore"
                placeholder="请输入用户剩余点数"
              /> -->
              {{ form.pointBefore }}
            </el-form-item>
          </el-col>
          <el-col :span="12"
          >
            <el-form-item label="剩余点数(变动后)" prop="pointAfter">
              <!-- <el-input
                v-model="form.pointAfter"
                placeholder="请输入用户剩余点数"
              /> -->
              {{ form.pointAfter }}
            </el-form-item>
          </el-col>
        </el-form-item>
        <!-- <el-form-item label="关联卡类ID" prop="templateId">
          <el-input v-model="form.templateId" placeholder="请输入关联卡类ID" />
        </el-form-item>
        <el-form-item label="关联卡ID" prop="cardId">
          <el-input v-model="form.cardId" placeholder="请输入关联卡ID" />
        </el-form-item> -->
        <el-form-item label="关联卡号" prop="cardNo">
          <!-- <el-input v-model="form.cardNo" placeholder="请输入关联卡号" /> -->
          {{ form.cardNo }}
        </el-form-item>
        <el-form-item label="变动描述" prop="changeDesc">
          <el-input
            v-model="form.changeDesc"
            placeholder="请输入变动描述"
            type="textarea"
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form.remark"
            placeholder="请输入内容"
            type="textarea"
          />
        </el-form-item>
        <!-- <el-form-item label="删除标志" prop="delFlag">
          <el-input v-model="form.delFlag" placeholder="请输入删除标志" />
        </el-form-item> -->
        <div v-if="form.id">
          <el-form-item prop="">
            <el-col :span="12">
              <el-form-item label="创建人" prop="createBy">
                {{ form.createBy }}
              </el-form-item>
              <el-form-item label="创建时间" prop="createTime">
                {{ form.createTime }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="最后更新" prop="updateBy">
                {{ form.updateBy }}
              </el-form-item>
              <el-form-item label="更新时间" prop="updateTime">
                {{ form.updateTime }}
              </el-form-item>
            </el-col>
          </el-form-item>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  addAppUserExpireLog,
  delAppUserExpireLog,
  getAppUserExpireLog,
  listAppUserExpireLog,
  updateAppUserExpireLog,
} from "@/api/system/appUserExpireLog";
import {listAppAll} from "@/api/system/app";
import { cleanAppUserExpirelog } from '@/api/system/appUserExpireLog'

export default {
  name: "AppUserExpireLog",
  dicts: ["sys_app_user_expire_change_type", "sys_bill_type"],
  data() {
    return {
      appList: [],
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
      // 时长或点数变动表格数据
      appUserExpireLogList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: this.$store.state.settings.pageSize,
        appUserId: null,
        changeAmount: null,
        changeType: null,
        expireTimeAfter: null,
        expireTimeBefore: null,
        pointAfter: null,
        pointBefore: null,
        changeDesc: null,
        templateId: null,
        cardId: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        appUserId: [
          {
            required: true,
            message: "软件用户用户 id不能为空",
            trigger: "blur",
          },
        ],
        changeAmount: [
          {required: true, message: "变动面值不能为空", trigger: "blur"},
        ],
        changeType: [
          {
            required: true,
            message: "1：充值，2：其他收入，3：其他支出不能为空",
            trigger: "change",
          },
        ],
        changeDesc: [
          {required: true, message: "变动描述不能为空", trigger: "blur"},
        ],
      },
    };
  },
  created() {
    this.getAppList();
    this.getList();
  },
  methods: {
    /** 查询时长或点数变动列表 */
    getList() {
      this.loading = true;
      listAppUserExpireLog(this.queryParams).then((response) => {
        this.appUserExpireLogList = response.rows;
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
        appUserId: null,
        changeAmount: null,
        changeType: null,
        expireTimeAfter: null,
        expireTimeBefore: null,
        pointAfter: null,
        pointBefore: null,
        changeDesc: null,
        templateId: null,
        cardId: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        delFlag: null,
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
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加时长或点数变动";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getAppUserExpireLog(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改时长或点数变动";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateAppUserExpireLog(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAppUserExpireLog(this.form).then((response) => {
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
      this.$modal
        .confirm('是否确认删除时长或点数变动编号为"' + ids + '"的数据项？')
        .then(function () {
          return delAppUserExpireLog(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {
        });
    },
    /** 清空按钮操作 */
    handleClean() {
      this.$modal
        .confirm("是否确认清空所有时长或点数变动日志数据项？")
        .then(function () {
          return cleanAppUserExpirelog();
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("清空成功");
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "system/appUserExpireLog/export",
        {
          ...this.queryParams,
        },
        `appUserExpireLog_${new Date().getTime()}.xlsx`
      );
    },
    getAppList() {
      this.loading = true;
      let queryParams = {};
      queryParams.params = {};
      listAppAll(queryParams).then((response) => {
        this.appList = response.rows;
        this.loading = false;
      });
    },

  },
};
</script>
