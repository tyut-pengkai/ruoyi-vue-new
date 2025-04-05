<!-- 绘制区域 -->
<template>
  <div class="map-container">
    <el-drawer
      title="地理围栏绘制"
      :visible.sync="visible"
      size="80%"
      direction="rtl"
    >
      <div class="map-controls">
        <el-button-group>
          <el-button type="primary" @click="startDrawing(BMAP_DRAWING_CIRCLE)">
            <i class="el-icon-circle-plus"></i> 圆形
          </el-button>
          <el-button
            type="success"
            @click="startDrawing(BMAP_DRAWING_RECTANGLE)"
          >
            <i class="el-icon-crop"></i> 矩形
          </el-button>
          <el-button type="warning" @click="startDrawing(BMAP_DRAWING_POLYGON)">
            <i class="el-icon-star-on"></i> 多边形
          </el-button>
          <el-button type="danger" @click="clearAll">
            <i class="el-icon-delete"></i> 清除
          </el-button>
          <el-button type="success" @click="drawOk">
            <i class="el-icon-ok"></i> 确定
          </el-button>
        </el-button-group>
      </div>
      <div id="mapContainer" :style="{ width: width, height: height }"></div>
    </el-drawer>
  </div>
</template>

<script>
export default {
  name: "BaiduMapDrawer",
  props: {
    width: {
      type: String,
      default: "100%",
    },
    height: {
      type: String,
      default: "100%",
    },
  },
  data() {
    return {
      BMAP_DRAWING_CIRCLE: "circle",
      BMAP_DRAWING_RECTANGLE: "rectangle",
      BMAP_DRAWING_POLYGON: "polygon",

      visible: false,
      map: null,
      drawingManager: null,

      //绘制的类型
      type: "", //1 圆形  2 矩形 3 多边形
      //绘制的数据
      data: "",
    };
  },
  mounted() {},
  methods: {
    async initMap() {
      try {
        // 确保BMap已加载
        if (!window.BMap) {
          await this.loadBMapScript();
        }

        // 延迟确保DOM渲染
        await this.$nextTick();

        // 初始化地图
        if (!this.map) {
          this.map = new BMap.Map("mapContainer");
          // 启用滚轮缩放
          this.map.enableScrollWheelZoom(true);
          const point = new BMap.Point(116.404, 39.915);
          this.map.centerAndZoom(point, 15);
          this.initDrawingManager();
        }
      } catch (error) {
        console.error("地图初始化失败:", error);
        this.$message.error("地图初始化失败，请检查AK配置或网络连接");
      }
    },

    initDrawingManager() {
      this.drawingManager = new BMapLib.DrawingManager(this.map, {
        isOpen: false,
        enableDrawingTool: false,
        drawingModes: [],
        // 添加绘图结束自动重置
        drawingModeOptions: {
          autoClose: false, // 保持绘图模式开启
        },
      });

      // 绑定事件监听
      this.bindDrawingEvents();
    },

    // 绘图控制方法
    startDrawing(type) {
      this.clearCurrentDrawing();

      // 每次重新创建绘图管理器
      if (this.drawingManager) {
        this.map.removeControl(this.drawingManager);
      }
      this.initDrawingManager();

      // 设置绘图模式
      const drawingType = {
        circle: BMAP_DRAWING_CIRCLE,
        rectangle: BMAP_DRAWING_RECTANGLE,
        polygon: BMAP_DRAWING_POLYGON,
      }[type];

      // 延迟确保控件加载
      this.$nextTick(() => {
        this.drawingManager.setDrawingMode(drawingType);
        this.drawingManager.open();
      });
    },

    // 修改清除方法（保留绘图工具）
    clearCurrentDrawing() {
      // 仅清除图形覆盖物，保留控件
      this.map.clearOverlays();
      // 清除本地存储
      ["polygons", "circles", "rectangles"].forEach((key) => {
        localStorage.removeItem(key);
      });
    },

    clearAll() {
      this.$confirm("确定要清除所有绘制内容吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        this.clearCurrentDrawing();
        //清除保留的数据
        this.data = "";

        this.$message.success("已清除所有绘制内容");
      });
    },

    bindDrawingEvents() {
      // 解绑旧事件
      this.drawingManager.removeEventListener("polygoncomplete");
      this.drawingManager.removeEventListener("circlecomplete");
      this.drawingManager.removeEventListener("rectanglecomplete");

      // 绑定新事件
      this.drawingManager.addEventListener(
        "polygoncomplete",
        this.handlePolygonComplete
      );
      this.drawingManager.addEventListener(
        "circlecomplete",
        this.handleCircleComplete
      );
      this.drawingManager.addEventListener(
        "rectanglecomplete",
        this.handleRectangleComplete
      );
    },

    // 重置方法
    resetDrawingManager() {
      this.drawingManager.close();
      this.drawingManager.open();
    },

    handlePolygonComplete(polygon) {
      const points = polygon.getPath();
      const polygonData = points.map((point) => ({
        lng: point.lng,
        lat: point.lat,
      }));
      this.saveData("polygons", polygonData);

      this.resetDrawingManager();
    },

    handleCircleComplete(circle) {
      const center = circle.getCenter();
      const circleData = {
        center: { lng: center.lng, lat: center.lat },
        radius: circle.getRadius(),
      };
      this.saveData("circles", circleData);

      this.resetDrawingManager();
    },

    handleRectangleComplete(rectangle) {
      const bounds = rectangle.getBounds();
      const rectangleData = {
        sw: { lng: bounds.getSouthWest().lng, lat: bounds.getSouthWest().lat },
        ne: { lng: bounds.getNorthEast().lng, lat: bounds.getNorthEast().lat },
      };
      this.saveData("rectangles", rectangleData);

      this.resetDrawingManager();
    },

    saveData(type, data) {
      // 1 圆形  2 矩形 3 多边形
      if (type === "polygons") {
        this.type = 3;
      } else if (type === "circles") {
        this.type = 1;
      } else if (type === "rectangles") {
        this.type = 2;
      }
      this.data = data;
    },
    //根据数据，回显
    showData() {
      if (this.data) {
        switch (this.type) {
          case 1:
            // 圆形
            let center = new BMap.Point(
              this.data.center.lng,
              this.data.center.lat
            );
            let circle = new BMap.Circle(center, this.data.radius, {
              strokeColor: "red",
              strokeWeight: 2,
              strokeOpacity: 0.5,
              fillColor: "red",
              fillOpacity: 0.2,
            });
            map.addOverlay(circle);
            break;
          case 2:
            //矩形
            let sw = new BMap.Point(this.data.sw.lng, this.data.sw.lat);
            let ne = new BMap.Point(this.data.ne.lng, this.data.ne.lat);
            let bounds = new BMap.Bounds(sw, ne);
            let rectangle = new BMap.Polygon(
              [
                new BMap.Point(sw.lng, sw.lat),
                new BMap.Point(ne.lng, sw.lat),
                new BMap.Point(ne.lng, ne.lat),
                new BMap.Point(sw.lng, ne.lat),
              ],
              {
                strokeColor: "green",
                strokeWeight: 2,
                strokeOpacity: 0.5,
                fillColor: "green",
                fillOpacity: 0.2,
              }
            );
            map.addOverlay(rectangle);
            break;
          case 3:
            //多边形
            let points = this.data.map(
              (point) => new BMap.Point(point.lng, point.lat)
            );
            let polygon = new BMap.Polygon(points, {
              strokeColor: "blue",
              strokeWeight: 2,
              strokeOpacity: 0.5,
              fillColor: "blue",
              fillOpacity: 0.2,
            });
            map.addOverlay(polygon);
        }
      }
    },
    drawOk() {
      this.$emit("drawOk", {
        type: this.type,
        data: JSON.stringify(this.data),
      });
      this.visible = false;
    },
    //显示
    show(type, data) {
      this.visible = true;
      this.type = type;
      this.data = data;

      this.$nextTick(() => {
        this.initMap();
        this.$nextTick(() => {
          this.showData();
        });
      });
    },
  },
};
</script>

<style scoped>
.map-container {
  position: relative;
}

#mapContainer {
  margin: 0 auto;
}
</style>
