<template>
  <div class="app-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>学生管理</span>
        </div>
      </template>
      
      <div class="filter-container">
        <el-input
          v-model="queryParams.studentName"
          placeholder="请输入学生姓名"
          clearable
          size="small"
          class="mb-2 mr-2"
          style="width: 240px"
        />
        <el-input
          v-model="queryParams.studentId"
          placeholder="请输入学生ID"
          clearable
          size="small"
          class="mb-2 mr-2"
          style="width: 240px"
        />
        <el-button
          type="primary"
          icon="el-icon-search"
          size="small"
          @click="handleQuery"
          class="mb-2"
        >
          搜索
        </el-button>
        <el-button
          icon="el-icon-refresh"
          size="small"
          @click="resetQuery"
          class="mb-2"
        >
          重置
        </el-button>
      </div>

      <el-table
        v-loading="loading"
        :data="studentList"
        @selection-change="handleSelectionChange"
        border
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="学生ID" prop="studentId" align="center" width="120" />
        <el-table-column label="学生姓名" prop="studentName" align="center" />
        <el-table-column label="学生年龄" prop="studentAge" align="center" width="120" />
        <el-table-column label="学生性别" prop="studentSex" align="center" width="120">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.studentSex === '1'" type="success">男</el-tag>
            <el-tag v-else-if="scope.row.studentSex === '0'" type="info">女</el-tag>
            <el-tag v-else type="danger">未知</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime" align="center" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="180">
          <template slot-scope="scope">
            <el-button
              type="primary"
              icon="el-icon-edit"
              size="small"
              @click="handleUpdate(scope.row)"
            >
              修改
            </el-button>
            <el-button
              type="danger"
              icon="el-icon-delete"
              size="small"
              @click="handleDelete(scope.row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </el-card>

    <!-- 添加或修改学生对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="学生姓名" prop="studentName">
          <el-input v-model="form.studentName" placeholder="请输入学生姓名" />
        </el-form-item>
        <el-form-item label="学生年龄" prop="studentAge">
          <el-input v-model="form.studentAge" placeholder="请输入学生年龄" type="number" />
        </el-form-item>
        <el-form-item label="学生性别" prop="studentSex">
          <el-radio-group v-model="form.studentSex">
            <el-radio label="1">男</el-radio>
            <el-radio label="0">女</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listStudent, getStudent, delStudent, addStudent, updateStudent } from "@/api/student";
import { parseTime } from "@/utils/ruoyi";

export default {
  name: "Student",
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
      // 学生列表
      studentList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        studentName: null,
        studentId: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        studentName: [
          { required: true, message: "学生姓名不能为空", trigger: "blur" }
        ],
        studentAge: [
          { required: true, message: "学生年龄不能为空", trigger: "blur" },
          { type: "number", message: "学生年龄必须为数字值", trigger: "blur" }
        ],
        studentSex: [
          { required: true, message: "学生性别不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询学生列表 */
    getList() {
      this.loading = true;
      listStudent(this.queryParams).then(res => {
        this.studentList = res.rows;
        this.total = res.total;
        this.loading = false;
      });
    },
    // 搜索按钮点击事件
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    // 重置按钮点击事件
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    // 新增按钮点击事件
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加学生";
    },
    // 修改按钮点击事件
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids[0];
      getStudent(id).then(res => {
        this.form = res.data;
        this.open = true;
        this.title = "修改学生";
      });
    },
    // 提交按钮
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateStudent(this.form).then(res => {
              if (res.code === 200) {
                this.$message.success("修改成功");
                this.open = false;
                this.getList();
              } else {
                this.$message.error(res.msg);
              }
            });
          } else {
            addStudent(this.form).then(res => {
              if (res.code === 200) {
                this.$message.success("新增成功");
                this.open = false;
                this.getList();
              } else {
                this.$message.error(res.msg);
              }
            });
          }
        }
      });
    },
    // 删除按钮点击事件
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm(
        "确定要删除选中的学生吗？",
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      )
        .then(function() {
          return delStudent(ids);
        })
        .then(res => {
          if (res.code === 200) {
            this.$message.success("删除成功");
            this.getList();
          } else {
            this.$message.error(res.msg);
          }
        })
        .catch(function() {});
    },
    // 重置表单
    reset() {
      this.form = {};
      this.resetForm("form");
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    }
  }
};
</script>

<style scoped>
.filter-container {
  padding-bottom: 10px;
}
</style>