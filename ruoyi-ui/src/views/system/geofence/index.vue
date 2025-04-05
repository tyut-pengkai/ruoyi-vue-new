<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      v-show="showSearch"
      label-width="140px"
    >
      <el-form-item label="口岸名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入口岸名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input
          v-model="queryParams.description"
          placeholder="请输入描述"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="口岸区域" prop="area">
        <el-input
          v-model="queryParams.area"
          placeholder="请输入口岸区域"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户Id" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户Id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="部门id" prop="deptId">
        <el-input
          v-model="queryParams.deptId"
          placeholder="请输入部门id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="城市代码" prop="cityCode">
        <el-input
          v-model="queryParams.cityCode"
          placeholder="请输入城市代码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="城市名称" prop="cityName">
        <el-input
          v-model="queryParams.cityName"
          placeholder="请输入城市名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="国外城市id" prop="districtId">
        <el-input
          v-model="queryParams.districtId"
          placeholder="请输入国外城市id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="国外城市名称" prop="districtName">
        <el-input
          v-model="queryParams.districtName"
          placeholder="请输入国外城市名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="转关口岸名称" prop="transitPortChn">
        <el-input
          v-model="queryParams.transitPortChn"
          placeholder="请输入转关口岸名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="转关口岸外文名称" prop="transitPortEng">
        <el-input
          v-model="queryParams.transitPortEng"
          placeholder="请输入转关口岸外文名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="转关口岸区域" prop="transitPortArea">
        <el-input
          v-model="queryParams.transitPortArea"
          placeholder="请输入转关口岸区域"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
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
          v-hasPermi="['system:geofence:add']"
          >新增</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:geofence:edit']"
          >修改</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:geofence:remove']"
          >删除</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:geofence:export']"
          >导出</el-button
        >
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="geofenceList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键" align="center" prop="id" />
      <el-table-column
        label="口岸名称"
        show-overflow-tooltip
        align="center"
        prop="name"
      />
      <el-table-column
        label="描述"
        show-overflow-tooltip
        align="center"
        prop="description"
      />
      <el-table-column
        label="口岸区域"
        show-overflow-tooltip
        align="center"
        prop="area"
      />
      <el-table-column label="用户Id" align="center" prop="userId" />
      <el-table-column label="部门id" align="center" prop="deptId" />
      <!-- 1 圆形  2 矩形 3 多边形 -->
      <el-table-column label="类型" align="center" prop="type" />
      <el-table-column label="城市代码" align="center" prop="cityCode" />
      <el-table-column label="城市名称" align="center" prop="cityName" />
      <el-table-column label="国外城市id" align="center" prop="districtId" />
      <el-table-column
        label="国外城市名称"
        show-overflow-tooltip
        align="center"
        prop="districtName"
      />
      <el-table-column
        label="转关口岸名称"
        show-overflow-tooltip
        align="center"
        prop="transitPortChn"
      />
      <el-table-column
        label="转关口岸外文名称"
        show-overflow-tooltip
        align="center"
        prop="transitPortEng"
      />
      <el-table-column
        label="转关口岸区域"
        show-overflow-tooltip
        align="center"
        prop="transitPortArea"
      />
      <!-- 1 圆形  2 矩形 3 多边形 -->
      <el-table-column label="类型" align="center" prop="transitPortType" />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:geofence:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:geofence:remove']"
            >删除</el-button
          >
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

    <!-- 添加或修改地理围栏对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="1000px"
      append-to-body
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="口岸名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入口岸名称" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="口岸区域" prop="area">
          <el-input
            :disabled="true"
            v-model="form.area"
            placeholder="请通过地图绘制口岸区域"
          >
            <template #append>
              <el-button @click="showMap(1)"> 地图绘制</el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="城市代码" prop="cityCode">
          <el-input v-model="form.cityCode" placeholder="请输入城市代码" />
        </el-form-item>
        <el-form-item label="城市名称" prop="cityName">
          <el-input v-model="form.cityName" placeholder="请输入城市名称" />
        </el-form-item>
        <el-form-item label="国外城市id" prop="districtId">
          <el-input v-model="form.districtId" placeholder="请输入国外城市id" />
        </el-form-item>
        <el-form-item label="国外城市名称" prop="districtName">
          <el-input
            v-model="form.districtName"
            placeholder="请输入国外城市名称"
          />
        </el-form-item>
        <el-form-item label="转关口岸名称" prop="transitPortChn">
          <el-input
            v-model="form.transitPortChn"
            placeholder="请输入转关口岸名称"
          />
        </el-form-item>
        <el-form-item label="转关口岸外文名称" prop="transitPortEng">
          <el-input
            v-model="form.transitPortEng"
            placeholder="请输入转关口岸外文名称"
          />
        </el-form-item>
        <el-form-item label="转关口岸区域" prop="transitPortArea">
          <el-input
            v-model="form.transitPortArea"
            :disabled="true"
            placeholder="请通过地图绘制转关口岸区域"
          >
            <template #append>
              <el-button @click="showMap(2)"> 地图绘制</el-button>
            </template>
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <map-draw ref="mapDrawRef" @drawOk="mapDrawOk"></map-draw>
  </div>
</template>

<script>
import {
  listGeofence,
  getGeofence,
  delGeofence,
  addGeofence,
  updateGeofence,
} from "@/api/system/geofence";
import MapDraw from "../../../components/map/MapDraw.vue";
import { getAbordArea, getChinaArea } from "@/api/system/config";

export default {
  components: { MapDraw },
  name: "Geofence",
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
      // 地理围栏表格数据
      geofenceList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        description: null,
        area: null,
        userId: null,
        deptId: null,
        type: null,
        cityCode: null,
        cityName: null,
        districtId: null,
        districtName: null,
        transitPortChn: null,
        transitPortEng: null,
        transitPortArea: null,
        transitPortType: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
      showMapType: "", //当前在地图上绘制的类型 1，国内口岸，2.转关口岸
      outSideOrgList: [], //外组织列表
      chinaOrgList: [], //国内组织列表
    };
  },
  created() {
    this.getOutsideOrgList();
    this.getList();
  },
  methods: {
    //获取组织列表
    async getOutsideOrgList() {
      const res = await getAbordArea({ pageNum: 1, pageSize: 30 });
      this.outSideOrgList = res.data;
      const chinaRes = await getChinaArea({ pageNum: 1, pageSize: 30 });
      this.chinaOrgList = chinaRes.data;
    },
    showMap(type) {
      this.showMapType = type;
      switch (type) {
        case 1:
          this.$refs.mapDrawRef.show(this.form.type, this.form.area);
          break;
        case 2:
          this.$refs.mapDrawRef.show(
            this.form.transitPortType,
            this.form.transitPortArea
          );
          break;
      }
    },
    mapDrawOk(data) {
      switch (this.showMapType) {
        case 1:
          this.form.type = data.type;
          this.form.area = data.data;
          break;
        case 2:
          this.form.transitPortType = data.type;
          this.form.transitPortArea = data.data;
          break;
      }
    },
    /** 查询地理围栏列表 */
    getList() {
      this.loading = true;
      listGeofence(this.queryParams).then((response) => {
        this.geofenceList = response.rows;
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
        name: null,
        description: null,
        area: null,
        createTime: null,
        updateTime: null,
        userId: null,
        deptId: null,
        type: null,
        cityCode: null,
        cityName: null,
        districtId: null,
        districtName: null,
        transitPortChn: null,
        transitPortEng: null,
        transitPortArea: null,
        transitPortType: null,
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
      this.title = "添加地理围栏";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getGeofence(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改地理围栏";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          //写入部门id和用户id
          this.form.deptId = this.$store.getters.dept.deptId;
          this.form.userId = this.$store.getters.userId;

          if (this.form.id != null) {
            updateGeofence(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addGeofence(this.form).then((response) => {
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
        .confirm('是否确认删除地理围栏编号为"' + ids + '"的数据项？')
        .then(function () {
          return delGeofence(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "system/geofence/export",
        {
          ...this.queryParams,
        },
        `geofence_${new Date().getTime()}.xlsx`
      );
    },
  },
};
</script>
