<template>
  <div class="database-manage-page">
    <!-- 搜索区域 -->
    <div class="search-container">
      <el-form :inline="true" class="search-form">
        <el-form-item label="数据库名称">
          <el-input
            v-model="searchForm.dbName"
            placeholder="输入数据库名称"
            clearable
            style="width: 200px;"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="btn-container">
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd">+ 新增</el-button>
      <el-button type="warning" icon="el-icon-edit" @click="handleEdit">修改</el-button>
      <el-button type="danger" icon="el-icon-delete" @click="handleDelete">删除</el-button>
      <el-button type="success" icon="el-icon-download" @click="handleExport">导出</el-button>
    </div>

    <!-- 数据表格区域 -->
    <div class="table-container">
      <el-table
        v-loading="loading"
        :data="dbList"
        border
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column type="index" label="产品ID" width="80" align="center" />
        <el-table-column prop="dbName" label="数据库名称" min-width="120" />
        <el-table-column prop="icon" label="图标" width="80" align="center">
          <template slot-scope="scope">
            <svg-icon :icon-class="scope.row.icon" />
          </template>
        </el-table-column>
        <el-table-column prop="type" label="类型" min-width="100" />
        <el-table-column prop="description" label="数据库描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" min-width="150" align="center" />
      </el-table>
    </div>

    <!-- 分页区域 -->
    <div class="pagination-container">
      <el-pagination
        background
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        :page-size.sync="pagination.pageSize"
        :current-page.sync="pagination.currentPage"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script>
export default {
  name: "ZhengyutianDatabaseManage",
  data() {
    return {
      // 搜索表单
      searchForm: {
        dbName: ""
      },
      // 数据库列表数据
      dbList: [
        {
          id: 100,
          dbName: "mysql",
          icon: "database",
          type: "关系型数据库",
          description: "MySQL是一个开源的关系型数据库管理系统",
          createTime: "2024-01-20 10:00:00"
        },
        {
          id: 101,
          dbName: "oracle",
          icon: "database",
          type: "关系型数据库",
          description: "Oracle是一个功能强大的商业关系型数据库",
          createTime: "2024-01-20 10:00:00"
        },
        {
          id: 102,
          dbName: "sqlserver",
          icon: "database",
          type: "关系型数据库",
          description: "SQL Server是微软开发的关系型数据库管理系统",
          createTime: "2024-01-20 10:00:00"
        }
      ],
      // 选中的行
      selectedRows: [],
      // 加载状态
      loading: false,
      // 分页信息
      pagination: {
        currentPage: 1,
        pageSize: 10
      },
      // 总条数
      total: 3
    };
  },
  methods: {
    // 搜索
    handleSearch() {
      this.loading = true;
      // 模拟搜索逻辑
      setTimeout(() => {
        this.loading = false;
      }, 500);
    },
    // 重置
    handleReset() {
      this.searchForm = {
        dbName: ""
      };
      this.handleSearch();
    },
    // 新增
    handleAdd() {
      this.$message("新增功能");
    },
    // 修改
    handleEdit() {
      if (this.selectedRows.length !== 1) {
        this.$message.warning("请选择一条记录进行修改");
        return;
      }
      this.$message("修改功能");
    },
    // 删除
    handleDelete() {
      if (this.selectedRows.length === 0) {
        this.$message.warning("请选择要删除的记录");
        return;
      }
      this.$confirm("确定要删除选中的记录吗?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          this.loading = true;
          // 模拟删除逻辑
          setTimeout(() => {
            this.loading = false;
            this.selectedRows = [];
            this.$message.success("删除成功");
          }, 500);
        })
        .catch(() => {
          this.$message("已取消删除");
        });
    },
    // 导出
    handleExport() {
      this.$message("导出功能");
    },
    // 选择行变化
    handleSelectionChange(selection) {
      this.selectedRows = selection;
    },
    // 分页大小变化
    handleSizeChange(val) {
      this.pagination.pageSize = val;
      this.handleSearch();
    },
    // 当前页码变化
    handleCurrentChange(val) {
      this.pagination.currentPage = val;
      this.handleSearch();
    }
  }
};
</script>

<style scoped>
.database-manage-page {
  padding: 20px;
  background-color: #f5f7fa;
}

.search-container {
  background-color: #fff;
  padding: 15px;
  border-radius: 4px;
  margin-bottom: 15px;
}

.search-form {
  margin-bottom: 0;
}

.btn-container {
  background-color: #fff;
  padding: 15px;
  border-radius: 4px;
  margin-bottom: 15px;
}

.btn-container .el-button {
  margin-right: 10px;
}

.table-container {
  background-color: #fff;
  padding: 15px;
  border-radius: 4px;
  margin-bottom: 15px;
}

.pagination-container {
  background-color: #fff;
  padding: 15px;
  border-radius: 4px;
  text-align: right;
}
</style>