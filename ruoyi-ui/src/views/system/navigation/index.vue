<template>
  <div class="app-container">
    <el-form
      v-show="showSearch"
      ref="queryForm"
      :inline="true"
      :model="queryParams"
      label-width="68px"
      size="small"
    >
      <el-form-item label="导航标题" prop="navName">
        <el-input
          v-model="queryParams.navName"
          clearable
          placeholder="请输入导航标题"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="路由地址" prop="path">
        <el-input
          v-model="queryParams.path"
          clearable
          placeholder="请输入路由地址"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否为外链" prop="isFrame">
        <el-select
          v-model="queryParams.isFrame"
          clearable
          placeholder="请选择是否为外链"
        >
          <el-option
            v-for="dict in dict.type.sys_normal_disable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="父菜单ID" prop="parentId">
        <el-input
          v-model="queryParams.parentId"
          clearable
          placeholder="请输入父菜单ID"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="导航状态" prop="visible">
        <el-select
          v-model="queryParams.visible"
          clearable
          placeholder="请选择导航状态"
        >
          <el-option
            v-for="dict in dict.type.sys_normal_disable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="显示顺序" prop="orderNum">
        <el-input
          v-model="queryParams.orderNum"
          clearable
          placeholder="请输入显示顺序"
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
        </el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
        >重置
        </el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:navigation:add']"
          icon="el-icon-plus"
          plain
          size="mini"
          type="primary"
          @click="handleAdd"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          icon="el-icon-sort"
          plain
          size="mini"
          type="info"
          @click="toggleExpandAll"
        >展开/折叠
        </el-button>
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-if="refreshTable"
      v-loading="loading"
      :data="navigationList"
      :default-expand-all="isExpandAll"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      row-key="navId"
    >
      <el-table-column label="导航标题" prop="navName"/>
      <el-table-column align="center" label="路由地址" prop="path"/>
      <el-table-column align="center" label="是否首页" prop="isIndex">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_yes_no"
            :value="scope.row.isIndex"
          />
        </template>
      </el-table-column>
      <!-- <el-table-column label="是否为外链" align="center" prop="isFrame">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_normal_disable"
            :value="scope.row.isFrame"
          />
        </template>
      </el-table-column> -->
      <!-- <el-table-column label="父菜单ID" align="center" prop="parentId" /> -->
      <!-- <el-table-column label="导航类型" align="center" prop="navType" /> -->
      <el-table-column align="center" label="导航状态" prop="visible">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_normal_disable"
            :value="scope.row.visible"
          />
        </template>
      </el-table-column>
      <el-table-column align="center" label="显示顺序" prop="orderNum"/>
      <el-table-column align="center" label="备注" prop="remark"/>
      <el-table-column
        align="center"
        class-name="small-padding fixed-width"
        label="操作"
      >
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['system:navigation:edit']"
            icon="el-icon-edit"
            size="mini"
            type="text"
            @click="handleUpdate(scope.row)"
          >修改
          </el-button>
          <el-button
            v-hasPermi="['system:navigation:add']"
            icon="el-icon-plus"
            size="mini"
            type="text"
            @click="handleAdd(scope.row)"
          >新增
          </el-button>
          <el-button
            v-hasPermi="['system:navigation:remove']"
            icon="el-icon-delete"
            size="mini"
            type="text"
            @click="handleDelete(scope.row)"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改首页导航对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="父菜单ID" prop="parentId">
          <treeselect
            v-model="form.parentId"
            :normalizer="normalizer"
            :options="navigationOptions"
            placeholder="请选择父菜单ID"
          />
        </el-form-item>
        <el-form-item label="导航标题" prop="navName">
          <el-input v-model="form.navName" placeholder="请输入导航标题"/>
        </el-form-item>
        <el-form-item label="路由地址" prop="path">
          <el-input v-model="form.path" placeholder="请输入路由地址"/>
        </el-form-item>
        <el-form-item label="是否首页">
          <el-radio-group v-model="form.isIndex">
            <el-radio
              v-for="dict in dict.type.sys_yes_no"
              :key="dict.value"
              :label="dict.value"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <!-- <el-form-item label="是否为外链">
          <el-radio-group v-model="form.isFrame">
            <el-radio
              v-for="dict in dict.type.sys_normal_disable"
              :key="dict.value"
:label="parseInt(dict.value)"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item> -->
        <el-form-item label="导航状态">
          <el-radio-group v-model="form.visible">
            <el-radio
              v-for="dict in dict.type.sys_normal_disable"
              :key="dict.value"
              :label="dict.value"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="显示顺序" prop="orderNum">
          <el-input v-model="form.orderNum" placeholder="请输入显示顺序"/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form.remark"
            placeholder="请输入内容"
            type="textarea"
          />
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
import {addNavigation, delNavigation, getNavigation, listNavigation, updateNavigation,} from "@/api/system/navigation";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "Navigation",
  dicts: ["sys_normal_disable", "sys_yes_no"],
  components: {
    Treeselect,
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 首页导航表格数据
      navigationList: [],
      // 首页导航树选项
      navigationOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否展开，默认全部展开
      isExpandAll: true,
      // 重新渲染表格状态
      refreshTable: true,
      // 查询参数
      queryParams: {
        navName: null,
        path: null,
        // isFrame: null,
        parentId: null,
        // navType: null,
        visible: null,
        orderNum: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        navName: [
          {required: true, message: "导航标题不能为空", trigger: "blur"},
        ],
        path: [
          {required: true, message: "路由地址不能为空", trigger: "blur"},
        ],
        orderNum: [
          {required: true, message: "显示顺序不能为空", trigger: "blur"},
        ],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询首页导航列表 */
    getList() {
      this.loading = true;
      listNavigation(this.queryParams).then((response) => {
        this.navigationList = this.handleTree(
          response.data,
          "navId",
          "parentId"
        );
        this.loading = false;
      });
    },
    /** 转换首页导航数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.navId,
        label: node.navName,
        children: node.children,
      };
    },
    /** 查询首页导航下拉树结构 */
    getTreeselect() {
      listNavigation().then((response) => {
        this.navigationOptions = [];
        const data = {navId: 0, navName: "顶级节点", children: []};
        data.children = this.handleTree(response.data, "navId", "parentId");
        this.navigationOptions.push(data);
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
        navId: null,
        navName: null,
        path: null,
        // isFrame: 0,
        isIndex: "N",
        parentId: null,
        // navType: "0",

        visible: "0",

        orderNum: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset();
      this.getTreeselect();
      if (row != null && row.navId) {
        this.form.parentId = row.navId;
      } else {
        this.form.parentId = 0;
      }
      this.open = true;
      this.title = "添加首页导航";
    },
    /** 展开/折叠操作 */
    toggleExpandAll() {
      this.refreshTable = false;
      this.isExpandAll = !this.isExpandAll;
      this.$nextTick(() => {
        this.refreshTable = true;
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.getTreeselect();
      if (row != null) {
        this.form.parentId = row.navId;
      }
      getNavigation(row.navId).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改首页导航";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.navId != null) {
            updateNavigation(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addNavigation(this.form).then((response) => {
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
      this.$modal
        .confirm('是否确认删除首页导航编号为"' + row.navId + '"的数据项？')
        .then(function () {
          return delNavigation(row.navId);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {
        });
    },
  },
};
</script>
