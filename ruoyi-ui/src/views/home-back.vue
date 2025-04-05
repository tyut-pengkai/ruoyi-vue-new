<template>
  <div class="home-container">
    <!-- 左侧导航栏 -->
    <div class="left-panel">
      <div class="logo">
        <span>国际资产跟踪 </span>
      </div>
      <el-menu
        :default-openeds="['1']"
        :default-active="activeIndex"
        @select="handleSelect"
      >
        <el-submenu index="1">
          <template slot="title"
            ><i class="el-icon-s-data"></i> 运筹管理</template
          >
          <el-menu-item index="1-1"
            >青岛--霍尔果斯--努尔饶尔--车里</el-menu-item
          >
          <el-menu-item index="1-2">霍尔果斯--车里- AB0105C</el-menu-item>
          <el-menu-item index="1-3">霍尔果斯--车里- AB0105C</el-menu-item>
        </el-submenu>
        <el-submenu index="2">
          <template slot="title"
            ><i class="el-icon-location"></i> GPS设备</template
          >
          <el-menu-item index="2-1">BF_1996011</el-menu-item>
          <el-menu-item index="2-2">BF_1996011</el-menu-item>
          <el-menu-item index="2-3">BF_1996011</el-menu-item>
          <el-menu-item index="2-4">BF_1996011</el-menu-item>
          <el-menu-item index="2-5">BF_1996011</el-menu-item>
        </el-submenu>
        <el-submenu index="3">
          <template slot="title"><i class="el-icon-check"></i> 已签收</template>
          <el-menu-item index="3-1">霍尔果斯-车里- AB0105C</el-menu-item>
          <el-menu-item index="3-2">霍尔果斯-车里- AB0105C</el-menu-item>
        </el-submenu>
      </el-menu>
    </div>
    <!-- 右侧内容区域 -->
    <div class="main">
      <!-- 地图容器 -->
      <div class="map-container">
        <div id="bmap-container" class="map"></div>
      </div>
      <div class="main-panel">
        <!-- 头部信息 -->
        <div class="header-info">
          <div class="info-form">
            <div class="info-item">
              <span class="info-label">运单号码：</span><span>KH0267407035443</span>
            </div>
            <div class="info-item">
              <span class="info-label">车牌号码：</span><span>AB0105C</span>
            </div>
            <div class="info-item">
              <span class="info-label">GPS编号：</span><span>BF_1996011</span>
            </div>
            <div class="info-item">
              <span class="info-label">客户：</span><span>工程设备</span>
            </div>
            <div class="info-item">
              <span class="info-label">货物名称：</span><span>工程设备</span>
            </div>
          </div>
          <div class="transport-info">
            <el-steps :active="1" align-center>
              <el-step title="青岛" description="3月1日 11:14 始发"></el-step>
              <el-step
                title="霍尔果斯"
                description="3月7日 10:30 口岸"
              ></el-step>
              <el-step
                title="努尔饶尔"
                description="3月7日 10:30 转关"
              ></el-step>
              <el-step
                title="车里"
                description="3月19日 10:30 运输中"
              ></el-step>
            </el-steps>
          </div>
        </div>
        <!-- 轨迹明细 -->
        <div class="track-details">
          <div class="date-range">
            <el-date-picker
              v-model="dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            ></el-date-picker>
            <el-select v-model="timeOption" placeholder="最近12小时">
              <el-option label="最近12小时" value="12h"></el-option>
              <el-option label="最近24小时" value="24h"></el-option>
            </el-select>
            <el-switch v-model="showNotice" label="仅显示通知"></el-switch>
          </div>
          <div class="daily-tracking">
            <el-tag type="success">03/17</el-tag>
            <span>晴 0--25℃ 空气质量 良 PM 93</span>
            <el-tag type="warning">当日运踪</el-tag>
          </div>
          <!-- 轨迹列表 -->
          <div
            v-for="(track, index) in trackList"
            :key="index"
            class="track-item"
          >
            <div class="track-location">
              <span>哈萨克斯坦</span>
              <span>阿拉木图</span>
              <span>克尔布拉克</span>
              <span
                >Kichik Halqa yo'li.Tashkent, Tashkent Shahri, Uzbekistan</span
              >
            </div>
            <div class="track-status">
              <div class="status-item">
                <span>运踪状态大类：</span><span>口岸运输</span>
              </div>
              <div class="status-item">
                <span>运踪状态小类：</span><span>卸货仓库，等待换装</span>
              </div>
              <div class="status-item">
                <span>轨迹内容：</span><span>预计明天换装完成</span>
              </div>
              <div class="status-item"><span>温度：</span><span>25℃</span></div>
              <div class="status-item">
                <span>是否运动：</span><span>是</span>
              </div>
              <div class="status-item">
                <span>区间平均速度：</span><span>269码</span>
              </div>
              <div class="status-item">
                <span>上报间隔：</span><span>1.5h</span>
              </div>
              <div class="status-item">
                <span>定位方式：</span><span>精准定位</span>
              </div>
              <div class="status-item">
                <span>电池电量：</span><span>60%</span>
              </div>
              <div class="status-item">
                <span>距离目的地直线距离：</span><span>550KM</span>
              </div>
            </div>
            <div v-if="track.notice" class="track-notice">
              <div class="notice-item">
                <span>通知类型：</span><span>距离目的地直线--400KM提醒</span>
              </div>
              <div class="notice-item">
                <span>通知内容：</span
                ><span
                  >AB0105C于2025年3月17日
                  22:31距离目的地-卡拉萨-直线400KM提醒</span
                >
              </div>
              <div class="notice-item">
                <span>接收通知用户：</span><span>XXXXXXX</span>
              </div>
            </div>
          </div>
          <el-button type="primary" size="small">转运运踪</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      activeIndex: "2",
      dateRange: [],
      timeOption: "12h",
      showNotice: false,
      trackList: [
        {
          notice: true,
          // 其他轨迹数据
        },
        {
          notice: false,
          // 其他轨迹数据
        },
      ],
    };
  },
  mounted() {
    this.initBaiduMap();
  },
  methods: {
    initBaiduMap() {
      const map = new BMap.Map("bmap-container");
      const point = new BMap.Point(86.404, 49.915); // 北京中心点坐标
      map.centerAndZoom(point, 5);

      // 添加运输路线标记
      const routePoints = [
        new BMap.Point(120.38, 36.06), // 青岛
        new BMap.Point(80.42, 44.21), // 霍尔果斯
        // new BMap.Point(75.12, 42.87),    // 努尔饶尔
        // new BMap.Point(73.06, 3.23)      // 车里（示例坐标）
      ];

      // 绘制运输路线
      const polyline = new BMap.Polyline(routePoints, {
        strokeColor: "#1890ff",
        strokeWeight: 3,
        strokeOpacity: 0.8,
      });
      map.addOverlay(polyline);

      // 添加地图控件
      map.addControl(new BMap.NavigationControl());
      map.addControl(new BMap.ScaleControl());
    },
    handleSelect(key, keyPath) {
      // 处理导航栏点击事件
      console.log(key, keyPath);
    },
  },
};
</script>

<style lang="scss" scoped>
.home-container {
  display: flex;
  height: 100%;
  .left-panel {
    width: 300px;
    background-color: #0b2440;
    color: white;
    font-size: 16px;
    .logo {
      background: url("../assets/images/home/logo-bg.png");
      background-size: 100% 100%;
      font-size: 28px;
      color: #ffffff;
      span {
        background: linear-gradient(90deg, #e5faff 0%, #357fff 100%);
        background-clip: text;
        -webkit-background-clip: text;
        color: transparent;
      }
    }

    ::v-deep .el-menu {
      background: none;
      background-color: rgba(0, 0, 0, 0);
      .el-submenu__title {
        font-size: 16px;
        color: rgba(255, 255, 255, 0.8);
        height: 44px;
        line-height: 44px;
        background: linear-gradient(
          270deg,
          rgba(0, 92, 255, 0.1) 0%,
          rgba(0, 208, 255, 0.2) 100%
        );
        border-radius: 4px;
        margin-bottom: 4px;
        i {
          color: rgba(255, 255, 255, 0.8);
        }
      }
      /* 当前选中项样式 */
      .el-menu-item .is-active,
      .is-opened .el-submenu__title,
      .el-submenu__title:hover,
      .el-menu-item:hover {
        background-color: #1a3d63;
        background: url("../assets/images/home/menu-select.png");
        background-size: 100% 100%;
      }
      /* 子菜单项样式 */
      .el-menu-item {
        font-size: 14px;
        color: rgba(255, 255, 255, 0.9);
      }
    }
  }
  .main {
    position: relative;
    flex: 1;
    height: 100%;
    .map-container {
      width: 100%;
      height: 100%;
      .map {
        width: 100%;
        height: 100%;
      }
    }
    .main-panel {
      position: absolute;
      left: 0;
      top: 0;
      height: 100%;
      width: 564px;
      background: white;

      .header-info {
        padding: 20px;
        border-bottom: 1px solid #e4e7ed;
        background: url("../assets/images/home/head-bg.png");
        background-size: 100% 100%;
        .info-form {
          display: flex;
          flex-wrap: wrap;
          .info-item {
            margin-bottom: 10px;
            width: 50%;
            .info-label{
              color: #666666;
            }
          }
        }
      }
    }
  }
}

#app {
  display: flex;
}
.logo {
  padding: 20px;
  text-align: center;
}
.logo img {
  width: 100px;
}

.transport-info {
  margin-top: 20px;
}
.track-details {
  padding: 20px;
}
.date-range {
  margin-bottom: 20px;
}
.daily-tracking {
  margin-bottom: 20px;
}
.track-item {
  border: 1px solid #e4e7ed;
  padding: 15px;
  margin-bottom: 15px;
}
.track-location {
  margin-bottom: 10px;
}
.track-status {
  display: flex;
  flex-wrap: wrap;
}
.status-item {
  width: 33%;
  margin-bottom: 10px;
}
.track-notice {
  border-top: 1px solid #e4e7ed;
  padding-top: 10px;
  margin-top: 10px;
}
.notice-item {
  margin-bottom: 5px;
}
</style>
