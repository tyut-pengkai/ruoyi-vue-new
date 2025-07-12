<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="智能体名称" prop="agentName">
        <el-input
          v-model="queryParams.agentName"
          placeholder="请输入智能体名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="智能体描述" prop="agentDesc">
        <el-input
          v-model="queryParams.agentDesc"
          placeholder="请输入智能体描述"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="模型名称" prop="modleName">
        <el-input
          v-model="queryParams.modleName"
          placeholder="请输入模型名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="音色" prop="voice">
        <el-input
          v-model="queryParams.voice"
          placeholder="请输入音色"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="音速" prop="voiceSpeed">
        <el-input
          v-model="queryParams.voiceSpeed"
          placeholder="请输入音速"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="记忆模型" prop="mem">
        <el-input
          v-model="queryParams.mem"
          placeholder="请输入记忆模型"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="意图模型" prop="intent">
        <el-input
          v-model="queryParams.intent"
          placeholder="请输入意图模型"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="交互语种编码" prop="langCode">
        <el-input
          v-model="queryParams.langCode"
          placeholder="请输入交互语种编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="交互语种名称" prop="langName">
        <el-input
          v-model="queryParams.langName"
          placeholder="请输入交互语种名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="排序权重" prop="sort">
        <el-input
          v-model="queryParams.sort"
          placeholder="请输入排序权重"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="智能体所属用户ID" prop="belongUserId">
        <el-input
          v-model="queryParams.belongUserId"
          placeholder="请输入智能体所属用户ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="分享属性" prop="shareType">
        <el-select v-model="queryParams.shareType" placeholder="分享属性" clearable>
          <el-option label="分享" value="0"/>
          <el-option label="私有" value="1"/>
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间" class="datable-time">
        <el-date-picker
          v-model="dateRange"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
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
          v-hasPermi="['agent:info:add']"
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
          v-hasPermi="['agent:info:edit']"
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
          v-hasPermi="['agent:info:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['agent:info:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="infoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="智能体id" align="center" prop="agentId" />
      <el-table-column label="智能体名称" align="center" prop="agentName" />
      <el-table-column label="智能体描述" align="center" prop="agentDesc" width="100" :show-overflow-tooltip="true" />
      <el-table-column label="模型名称" align="center" prop="modleName" />
      <el-table-column label="系统提示词" align="center" prop="systemPrompt" width="100" :show-overflow-tooltip="true" />
      <el-table-column label="音色" align="center" prop="voice" />
      <el-table-column label="音速" align="center" prop="voiceSpeed" />
      <el-table-column label="记忆模型" align="center" prop="mem" />
      <el-table-column label="意图模型" align="center" prop="intent" />
      <el-table-column label="交互语种编码" align="center" prop="langCode" />
      <el-table-column label="交互语种名称" align="center" prop="langName" />
      <el-table-column label="排序权重" align="center" prop="sort" />
      <el-table-column label="智能体所属用户ID" align="center" prop="belongUserId" />
      <el-table-column label="智能体分享属性(0分享 1私有)" align="center" prop="shareType" />
      <el-table-column label="状态" align="center" prop="status" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['agent:info:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['agent:info:remove']"
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

    <!-- 添加或修改智能体信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="智能体名称" prop="agentName">
          <el-input v-model="form.agentName" placeholder="请输入智能体名称" />
        </el-form-item>
        <el-form-item label="智能体描述" prop="agentDesc">
          <el-input v-model="form.agentDesc" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="模型名称" prop="modleName">
          <el-input v-model="form.modleName" placeholder="请输入模型名称" />
        </el-form-item>
        <el-form-item label="系统提示词" prop="systemPrompt">
          <el-input v-model="form.systemPrompt" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="音色" prop="voice">
          <el-input v-model="form.voice" placeholder="请输入音色" />
        </el-form-item>
        <el-form-item label="音速" prop="voiceSpeed">
          <el-input v-model="form.voiceSpeed" placeholder="请输入音速" />
        </el-form-item>
        <el-form-item label="记忆模型" prop="mem">
          <el-input v-model="form.mem" placeholder="请输入记忆模型" />
        </el-form-item>
        <el-form-item label="意图模型" prop="intent">
          <el-input v-model="form.intent" placeholder="请输入意图模型" />
        </el-form-item>
        <el-form-item label="交互语种编码" prop="langCode">
          <el-input v-model="form.langCode" placeholder="请输入交互语种编码" />
        </el-form-item>
        <el-form-item label="交互语种名称" prop="langName">
          <el-input v-model="form.langName" placeholder="请输入交互语种名称" />
        </el-form-item>
        <el-form-item label="排序权重" prop="sort">
          <el-input v-model="form.sort" placeholder="请输入排序权重" />
        </el-form-item>
        <el-form-item label="智能体所属用户ID" prop="belongUserId">
          <el-input v-model="form.belongUserId" placeholder="请输入智能体所属用户ID" :disabled="true" />
        </el-form-item>
        <el-form-item label="分享属性" prop="shareType">
          <!-- 如果是admin用户，默认值为"分享"（0），且可以选择"私有",如果是非admin用户，默认值为"私有"（1），且该选项不可更改 -->
          <el-radio-group v-model="form.shareType" :disabled="!isAdmin">
            <el-radio label="0">分享</el-radio>
            <el-radio label="1">私有</el-radio>
          </el-radio-group>
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
import { listInfo, getInfo, delInfo, addInfo, updateInfo } from "@/api/agent/info"

export default {
  name: "Info",
  computed: {
    isAdmin() {
      // 使用 getter 判断当前用户是否为 admin
      return this.$store.getters.roles && this.$store.getters.roles.includes('admin')
    }
  },
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
      // 智能体信息表格数据
      infoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        agentName: null,
        agentDesc: null,
        modleName: null,
        systemPrompt: null,
        voice: null,
        voiceSpeed: null,
        mem: null,
        intent: null,
        langCode: null,
        langName: null,
        sort: null,
        belongUserId: null,
        shareType: null,
        status: null,
        delFlag: null,
        createdAt: null,
        updatedAt: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        agentName: [
          { required: true, message: "智能体名称不能为空", trigger: "blur" }
        ],
        belongUserId: [
          { required: true, message: "智能体所属用户ID不能为空", trigger: "blur" }
        ],
        shareType: [
          { required: true, message: "智能体分享属性(0分享 1私有)不能为空", trigger: "change" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询智能体信息列表 */
    getList() {
      this.loading = true;
      this.addDateRange(this.queryParams, this.dateRange);
      listInfo(this.queryParams).then(response => {
        this.infoList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        agentId: null,
        agentName: null,
        agentDesc: null,
        modleName: null,
        systemPrompt: null,
        voice: null,
        voiceSpeed: null,
        mem: null,
        intent: null,
        langCode: null,
        langName: null,
        sort: null,
        belongUserId: null,
        shareType: null,
        status: null,
        delFlag: null,
        createdAt: null,
        updatedAt: null
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.agentId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.form.belongUserId = this.$store.state.user.id;
      if (this.isAdmin) {
        this.form.shareType = "0";
      } else {
        this.form.shareType = "1";
      }
      this.open = true;
      this.title = "添加智能体信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const agentId = row.agentId || this.ids
      getInfo(agentId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改智能体信息"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.agentId != null) {
            updateInfo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addInfo(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const agentIds = row.agentId || this.ids
      this.$modal.confirm('是否确认删除智能体信息编号为"' + agentIds + '"的数据项？').then(function() {
        return delInfo(agentIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('agent/info/export', {
        ...this.queryParams
      }, `info_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
