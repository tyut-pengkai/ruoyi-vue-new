<template>
  <div>
    <el-container>
      <el-header
        style="padding: 0; position: fixed; z-index: 9999; width: 100%"
      >
        <div style="background-color: #545c64">
          <el-row>
            <el-col :span="5">
              <div class="grid-content"></div>
            </el-col>
            <el-col :span="14">
              <div class="my-logo">
                <a href="/">
                  <el-avatar image-size="48" src="/logo.png"></el-avatar>
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
                    INAMS软件商店
                  </span>
                </a>
              </div>
              <el-menu
                :default-active="activeIndex"
                active-text-color="#ffd04b"
                background-color="#545c64"
                class="my-menu"
                mode="horizontal"
                router
                text-color="#fff"
                @select="handleSelect"
              >
                <el-menu-item index="/">购买商品</el-menu-item>
                <el-menu-item index="queryOrder">查询订单</el-menu-item>
                <!-- <el-button class="my-button" @click="regShow = true">注册</el-button>
                                <el-button class="my-button" @click="login">登录</el-button> -->
              </el-menu>
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
    </el-container>
  </div>
</template>
<script>
export default {
  data() {
    return {
      activeIndex: "1",
      regShow: false,
    };
  },
  methods: {
    handleSelect(key, keyPath) {
      console.log(key, keyPath);
    },
    login() {
    },
  },
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
  width: 1100px;
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
  border-bottom: 0px;
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
</style>

<style scoped>
@media screen and (max-width: 1200px) {
  .my-menu {
    float: right;
    max-width: 1140px;
  }

  .grid-content {
    border-radius: 4px;
    min-height: 36px;
  }
}

@media screen and (max-width: 576px) {
  .my-menu {
    display: none;
  }

  .grid-content {
  }
}
</style>
