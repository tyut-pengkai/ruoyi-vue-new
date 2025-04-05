<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="订单编号" prop="orderNumber">
        <el-input
          v-model="queryParams.orderNumber"
          placeholder="请输入订单编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="运踪状态大类" prop="tracingCateg">
        <el-input
          v-model="queryParams.tracingCateg"
          placeholder="请输入运踪状态大类"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="运踪状态小类" prop="tracingSubCateg">
        <el-input
          v-model="queryParams.tracingSubCateg"
          placeholder="请输入运踪状态小类"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备编号" prop="deviceNumber">
        <el-input
          v-model="queryParams.deviceNumber"
          placeholder="请输入设备编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="经度" prop="longitude">
        <el-input
          v-model="queryParams.longitude"
          placeholder="请输入经度"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="纬度" prop="latitude">
        <el-input
          v-model="queryParams.latitude"
          placeholder="请输入纬度"
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
      </el-form-item>
      <el-form-item label="城市名称" prop="cityName">
        <el-input
          v-model="queryParams.cityName"
          placeholder="请输入城市名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="采集时间" prop="captureTime">
        <el-date-picker clearable
          v-model="queryParams.captureTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择采集时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="操作用户Id" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入操作用户Id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="操作用户名称" prop="userName">
        <el-input
          v-model="queryParams.userName"
          placeholder="请输入操作用户名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="定位数据id" prop="locationId">
        <el-input
          v-model="queryParams.locationId"
          placeholder="请输入定位数据id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="天气" prop="weather">
        <el-input
          v-model="queryParams.weather"
          placeholder="请输入天气"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="距离目的地距离" prop="destDistance">
        <el-input
          v-model="queryParams.destDistance"
          placeholder="请输入距离目的地距离"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="速度" prop="speed">
        <el-input
          v-model="queryParams.speed"
          placeholder="请输入速度"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="上报间隔" prop="reportInterval">
        <el-input
          v-model="queryParams.reportInterval"
          placeholder="请输入上报间隔"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="电量" prop="battery">
        <el-input
          v-model="queryParams.battery"
          placeholder="请输入电量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="定位方式 1 精确定位 2 辅助定位" prop="locateWay">
        <el-input
          v-model="queryParams.locateWay"
          placeholder="请输入定位方式 1 精确定位 2 辅助定位"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="温度" prop="temperature">
        <el-input
          v-model="queryParams.temperature"
          placeholder="请输入温度"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否运动" prop="isMove">
        <el-input
          v-model="queryParams.isMove"
          placeholder="请输入是否运动"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="上报日期" prop="reportDay">
        <el-date-picker clearable
          v-model="queryParams.reportDay"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择上报日期">
        </el-date-picker>
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
          v-hasPermi="['system:tracke:add']"
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
          v-hasPermi="['system:tracke:edit']"
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
          v-hasPermi="['system:tracke:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:tracke:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="trackeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键" align="center" prop="id" />
      <el-table-column label="订单编号" align="center" prop="orderNumber" />
      <el-table-column label="运踪状态大类" align="center" prop="tracingCateg" />
      <el-table-column label="运踪状态小类" align="center" prop="tracingSubCateg" />
      <el-table-column label="轨迹内容" align="center" prop="trajectoryContent" />
      <el-table-column label="设备编号" align="center" prop="deviceNumber" />
      <el-table-column label="经度" align="center" prop="longitude" />
      <el-table-column label="纬度" align="center" prop="latitude" />
      <el-table-column label="城市代码" align="center" prop="cityCode" />
      <el-table-column label="城市名称" align="center" prop="cityName" />
      <el-table-column label="采集时间" align="center" prop="captureTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.captureTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作用户Id" align="center" prop="userId" />
      <el-table-column label="操作用户名称" align="center" prop="userName" />
      <el-table-column label="定位数据id" align="center" prop="locationId" />
      <el-table-column label="数据类型 1 定位数据 2 转写运单 3 手动录入运单" align="center" prop="type" />
      <el-table-column label="天气" align="center" prop="weather" />
      <el-table-column label="距离目的地距离" align="center" prop="destDistance" />
      <el-table-column label="速度" align="center" prop="speed" />
      <el-table-column label="上报间隔" align="center" prop="reportInterval" />
      <el-table-column label="电量" align="center" prop="battery" />
      <el-table-column label="定位方式 1 精确定位 2 辅助定位" align="center" prop="locateWay" />
      <el-table-column label="温度" align="center" prop="temperature" />
      <el-table-column label="是否运动" align="center" prop="isMove" />
      <el-table-column label="上报日期" align="center" prop="reportDay" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.reportDay, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:tracke:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:tracke:remove']"
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

    <!-- 添加或修改运踪对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="订单编号" prop="orderNumber">
          <el-input v-model="form.orderNumber" placeholder="请输入订单编号" />
        </el-form-item>
        <el-form-item label="运踪状态大类" prop="tracingCateg">
          <el-input v-model="form.tracingCateg" placeholder="请输入运踪状态大类" />
        </el-form-item>
        <el-form-item label="运踪状态小类" prop="tracingSubCateg">
          <el-input v-model="form.tracingSubCateg" placeholder="请输入运踪状态小类" />
        </el-form-item>
        <el-form-item label="轨迹内容">
          <editor v-model="form.trajectoryContent" :min-height="192"/>
        </el-form-item>
        <el-form-item label="设备编号" prop="deviceNumber">
          <el-input v-model="form.deviceNumber" placeholder="请输入设备编号" />
        </el-form-item>
        <el-form-item label="经度" prop="longitude">
          <el-input v-model="form.longitude" placeholder="请输入经度" />
        </el-form-item>
        <el-form-item label="纬度" prop="latitude">
          <el-input v-model="form.latitude" placeholder="请输入纬度" />
        </el-form-item>
        <el-form-item label="城市代码" prop="cityCode">
          <el-input v-model="form.cityCode" placeholder="请输入城市代码" />
        </el-form-item>
        <el-form-item label="城市名称" prop="cityName">
          <el-input v-model="form.cityName" placeholder="请输入城市名称" />
        </el-form-item>
        <el-form-item label="采集时间" prop="captureTime">
          <el-date-picker clearable
            v-model="form.captureTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择采集时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="操作用户Id" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入操作用户Id" />
        </el-form-item>
        <el-form-item label="操作用户名称" prop="userName">
          <el-input v-model="form.userName" placeholder="请输入操作用户名称" />
        </el-form-item>
        <el-form-item label="定位数据id" prop="locationId">
          <el-input v-model="form.locationId" placeholder="请输入定位数据id" />
        </el-form-item>
        <el-form-item label="天气" prop="weather">
          <el-input v-model="form.weather" placeholder="请输入天气" />
        </el-form-item>
        <el-form-item label="距离目的地距离" prop="destDistance">
          <el-input v-model="form.destDistance" placeholder="请输入距离目的地距离" />
        </el-form-item>
        <el-form-item label="速度" prop="speed">
          <el-input v-model="form.speed" placeholder="请输入速度" />
        </el-form-item>
        <el-form-item label="上报间隔" prop="reportInterval">
          <el-input v-model="form.reportInterval" placeholder="请输入上报间隔" />
        </el-form-item>
        <el-form-item label="电量" prop="battery">
          <el-input v-model="form.battery" placeholder="请输入电量" />
        </el-form-item>
        <el-form-item label="定位方式 1 精确定位 2 辅助定位" prop="locateWay">
          <el-input v-model="form.locateWay" placeholder="请输入定位方式 1 精确定位 2 辅助定位" />
        </el-form-item>
        <el-form-item label="温度" prop="temperature">
          <el-input v-model="form.temperature" placeholder="请输入温度" />
        </el-form-item>
        <el-form-item label="是否运动" prop="isMove">
          <el-input v-model="form.isMove" placeholder="请输入是否运动" />
        </el-form-item>
        <el-form-item label="上报日期" prop="reportDay">
          <el-date-picker clearable
            v-model="form.reportDay"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择上报日期">
          </el-date-picker>
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
import { listTracke, getTracke, delTracke, addTracke, updateTracke } from "@/api/track/tracke";

export default {
  name: "Tracke",
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
      // 运踪表格数据
      trackeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNumber: null,
        tracingCateg: null,
        tracingSubCateg: null,
        trajectoryContent: null,
        deviceNumber: null,
        longitude: null,
        latitude: null,
        cityCode: null,
        cityName: null,
        captureTime: null,
        userId: null,
        userName: null,
        locationId: null,
        type: null,
        weather: null,
        destDistance: null,
        speed: null,
        reportInterval: null,
        battery: null,
        locateWay: null,
        temperature: null,
        isMove: null,
        reportDay: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询运踪列表 */
    getList() {
      this.loading = true;
      listTracke(this.queryParams).then(response => {
        this.trackeList = response.rows;
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
        orderNumber: null,
        tracingCateg: null,
        tracingSubCateg: null,
        trajectoryContent: null,
        deviceNumber: null,
        longitude: null,
        latitude: null,
        cityCode: null,
        cityName: null,
        captureTime: null,
        createTime: null,
        userId: null,
        userName: null,
        locationId: null,
        type: null,
        weather: null,
        destDistance: null,
        speed: null,
        reportInterval: null,
        battery: null,
        locateWay: null,
        temperature: null,
        isMove: null,
        reportDay: null
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加运踪";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getTracke(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改运踪";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateTracke(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addTracke(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除运踪编号为"' + ids + '"的数据项？').then(function() {
        return delTracke(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/tracke/export', {
        ...this.queryParams
      }, `tracke_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
