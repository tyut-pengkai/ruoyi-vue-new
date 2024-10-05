<template>
  <div>
    <el-container>
      <el-header class="my-header">
        <div style="background-color: #545c64">
          <el-row>
            <el-col :span="2">
              <div class="my-class">
                <hamburger
                  id="hamburger-container"
                  :is-active="false"
                  class="hamburger-container"
                  @toggleClick="toggleSideBar"
                />
              </div>
            </el-col>
            <el-col :span="3">
              <div class="grid-content"></div>
            </el-col>
            <el-col :span="14">
              <div class="my-logo">
                <a href="/">
                  <el-avatar :src="logo" image-size="48"></el-avatar>
                  <span
                    style="
                      color: aliceblue;
                      font-size: 19px;
                      font-weight: bold;
                      position: relative;
                      left: 9px;
                      bottom: 12px;
                    "
                  >
                    {{ title }}
                  </span>
                </a>
              </div>
              <!-- 手机页面 -->
              <el-drawer
                :visible.sync="drawer"
                :withHeader="true"
                direction="ltr"
                title="更多"
                :append-to-body="true"
                :modal-append-to-body="false"
              >
                <div class="customClass">
                  <el-menu
                    :default-active="activeIndex"
                    active-text-color="#ffd04b"
                    background-color="#545c64"
                    class="my-menu"
                    mode="vertical"
                    :router="false"
                    style="width: 100%"
                    text-color="#fff"
                    @select="handleSelect"
                    menu-trigger="click"
                  >
                    <sidebar-item
                      v-for="(route, index) in navList"
                      :key="route.path + index"
                      :base-path="route.path"
                      :item="route"
                    />
                    <!-- <el-menu-item index="/">购买商品</el-menu-item>
                    <el-menu-item index="queryOrder">查询订单</el-menu-item>
                    <el-menu-item index="queryCard">查询卡密</el-menu-item>
                    <el-menu-item index="chargeCenter">充值续费</el-menu-item>
                    <el-menu-item index="unbindDevice">解绑设备</el-menu-item>
                    <el-menu-item v-if="isLicenseServer" index="getLicense">
                      激活授权
                    </el-menu-item> -->
                    <!-- <el-button class="my-button" @click="regShow = true">注册</el-button>
                                      <el-button class="my-button" @click="login">登录</el-button> -->
                  </el-menu>
                </div>
              </el-drawer>
              <!-- 电脑页面 -->
              <div class="my-class2">
                <el-menu
                  :default-active="activeIndex"
                  active-text-color="#ffd04b"
                  background-color="#545c64"
                  class="my-menu"
                  mode="horizontal"
                  :router="false"
                  text-color="#fff"
                  @select="handleSelect"
                  menu-trigger="hover"
                >
                  <!-- <el-menu-item
                    v-for="(item, idx) in navList"
                    :key="idx"
                    :index="item.path"
                    v-show="item.visible == '0'"
                    >{{ item.navName }}</el-menu-item
                  > -->

                  <sidebar-item
                    v-for="(route, index) in navList"
                    :key="route.path + index"
                    :base-path="route.path"
                    :item="route"
                  />

                  <!-- <el-menu-item index="/">购买商品</el-menu-item>
                  <el-menu-item index="queryOrder">查询订单</el-menu-item>
                  <el-menu-item index="queryCard">查询卡密</el-menu-item>
                  <el-menu-item index="chargeCenter">充值续费</el-menu-item>
                  <el-menu-item index="unbindDevice">解绑设备</el-menu-item>
                  <el-menu-item v-if="isLicenseServer" index="getLicense">
                    激活授权
                  </el-menu-item> -->
                  <!-- <el-button class="my-button" @click="regShow = true">注册</el-button>
                                  <el-button class="my-button" @click="login">登录</el-button> -->
                </el-menu>
              </div>
            </el-col>
            <el-col :span="5">
              <div class="grid-content"></div>
            </el-col>
          </el-row>
        </div>
      </el-header>
      <el-main style="margin-top: 60px">
        <transition mode="out-in" name="fade">
          <router-view></router-view>
        </transition>
      </el-main>
      <!-- <el-footer> -->
      <el-link :underline="false" type="info">
        <div class="my-footer">
          <span>
            {{ copyright }}
            <span v-show="icp"
            >(<a href="https://beian.miit.gov.cn/">{{ icp }}</a
            >)</span
            >
          </span>
        </div>
      </el-link>
      <!-- </el-footer> -->
    </el-container>
  </div>
</template>
<script>
import {getNavInfo} from "@/api/sale/saleShop";
import {getSysInfo} from "@/api/common";
import Hamburger from "@/components/Hamburger";
import SidebarItem from "../../layout/components/Sidebar/SidebarItemNav";
import "element-ui/lib/theme-chalk/display.css";

export default {
  components: {
    Hamburger,
    SidebarItem,
  },
  data() {
    return {
      copyright: "",
      activeIndex: "0",
      regShow: false,
      // logo: require("../../assets/logo/logo.png"),
      // title: "在线商城",
      drawer: false,
      isLicenseServer: false,
      // icp: "",
      navList: [
        // {
        //   navName: "a",
        //   path: "b",
        //   isFrame: 0,
        //   visible: 1,
        // },
      ],
      stopFlag: true, // 为了避免当关闭前台时从个人中心支付被拦截，加此flag，flag为false时即使关闭前台也不跳转stop页
    };
  },
  created() {
    // if (this.$store.state.settings.enableFrontEnd == "Y") {
    //   this.getNavigation();
    //   this.getSysInfo();
    // } else {
    //   this.$router.replace("stop");
    // }
    this.getNavigation();
    this.getSysInfo();
  },
  methods: {
    getNavigation() {
      getNavInfo().then((res) => {
        this.navList = res.data;
      });
    },
    getSysInfo() {
      getSysInfo().then((res) => {
        this.copyright = res.copyright;
        this.isLicenseServer = res.isLicenseServer;
        if (this.stopFlag && !res.enableFrontEnd) {
          this.$router.replace("stop");
        }
      });
      // if (this.$store.state.settings.shopName) {
      //   this.title = this.$store.state.settings.shopName;
      // }
      // if (this.$store.state.settings.websiteLogo) {
      //   this.logo = process.env.VUE_APP_BASE_API + this.$store.state.settings.websiteLogo;
      // }
      // if (this.$store.state.settings.icp) {
      //   this.icp = this.$store.state.settings.icp;
      // }
      if (this.$store.state.settings.websiteFavicon) {
        var faviconurl = this.$store.state.settings.websiteFavicon; //这里可以是动态的获取的favicon的地址
        var link =
          document.querySelector("link[rel*='icon']") ||
          document.createElement("link");
        link.type = "image/x-icon";
        link.rel = "shortcut icon";
        link.href = process.env.VUE_APP_BASE_API + faviconurl;
        document.getElementsByTagName("head")[0].appendChild(link);
      }
    },
    handleSelect(key, keyPath) {
      // console.log(key, keyPath);
      this.drawer = false;
    },
    login() {
    },
    toggleSideBar() {
      this.drawer = true;
    },
  },
  beforeRouteEnter(to, from, next) {
    // console.log("fe2 to.path----", to); //跳转后路由
    // console.log("fe2 from----", from); //跳转前路由
    next(vm => {
      // 通过 `vm` 访问组件实例
      if(to.path === '/billOrder') {
        vm.stopFlag = false;
      } else {
        vm.stopFlag = true;
      }
    });
  },
  computed: {
    icp() {
      return this.$store.state.settings.icp;
    },
    title() {
      return this.$store.state.settings.shopName || "在线商城";
    },
    logo() {
      return this.$store.state.settings.websiteLogo || require("../../assets/logo/logo.png");
    }
  }
};
</script>
<style scoped>
body {
  margin: 0;
}
a {
  text-decoration: None;
}

.home {
  width: 90vw;
  max-width: 1100px;
}

.my-header {
  top: 0;
  padding: 0;
  position: fixed;
  z-index: 999;
  width: 100%;
}

.el-main {
  max-width: 1140px;
  margin-left: auto;
  margin-right: auto;
}
.my-logo {
  display: inline-block;
  float: left;
  margin: 8px 0;
}
.my-menu {
  float: right;
  max-width: 1140px;
  height: 60px;
  border: 0px;
}
.grid-content {
  border-radius: 4px;
  min-height: 36px;
}
.my-button {
  margin: 8px;
  margin-top: 12px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
  background-color: rgb(84, 92, 100);
  color: rgb(255, 255, 255);
  border-color: #ffc107;
  transition: all 0.5s ease-in;
}
.my-button:hover {
  background-color: rgb(84, 92, 100);
  color: rgb(255, 255, 255);
  border-color: #c6e2ff;
}

.my-button:focus {
  background-color: rgb(84, 92, 100);
  color: rgb(255, 255, 255);
  border-color: #c6e2ff;
}

.my-footer {
  position: fixed;
  right: 0;
  bottom: 0;
  height: 32px;
  line-height: 32px;
  padding: 0 15px;
  background-color: #eee;
  font-family: "Helvetica Neue";
  text-align: center;
  z-index: 9999;
  transition: width 0.28s;
  width: 100%;
  color: #999;
}
</style>

<style scoped>
/* @media screen and (max-width: 1200px) { */
.my-menu {
  float: right;
  max-width: 1140px;
}

.grid-content {
  border-radius: 4px;
  min-height: 36px;
}

/* } */

/* @media screen and (max-width: 576px) {
  .my-menu {
    display: none;
  }

  .grid-content {
  }
} */

@media screen and (min-width: 1200px) {
  .my-class {
    display: none;
  }
}

@media screen and (max-width: 1199px) {
  .my-class2 {
    display: none;
  }
}

.hamburger-container {
  line-height: 46px;
  height: 100%;
  float: left;
  cursor: pointer;
  transition: background 0.3s;
  -webkit-tap-highlight-color: transparent;
  margin-top: 5px;
}

.hamburger-container :hover {
  background: rgba(0, 0, 0, 0.025);
}

.customClass {
  background-color: #545c64;
  height: 100%;
  width: 100%;
  z-index: 99999;
}
</style>
