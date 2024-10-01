<template>
  <div :class="classObj" class="app-wrapper" :style="{'--current-color': theme}">
    <div v-if="device==='mobile'&&sidebar.opened" class="drawer-bg" @click="handleClickOutside"/>
    <sidebar v-if="!sidebar.hide" :logo="logo" :title="shortName" class="sidebar-container"/>
    <div :class="{hasTagsView:needTagsView,sidebarHide:sidebar.hide}" class="main-container">
      <div :class="{'fixed-header':fixedHeader}">
        <navbar/>
        <tags-view v-if="needTagsView"/>
      </div>
      <app-main/>
      <right-panel>
        <settings/>
      </right-panel>
        <!-- <el-footer> -->
        <el-link :underline="false" type="info">
          <div class="my-footer">
            <span>
              {{ copyright }}
            </span>
            <span style="float: right">{{ version }}</span>
          </div>
        </el-link>
        <!-- </el-footer> -->
      </div>
    </div>
</template>

<script>
import RightPanel from "@/components/RightPanel";
import {AppMain, Navbar, Settings, Sidebar, TagsView} from "./components";
import ResizeMixin from "./mixin/ResizeHandler";
import {mapState} from "vuex";
import variables from "@/assets/styles/variables.scss";
import {getSysInfo} from "@/api/common";

export default {
  name: 'Layout',
  components: {
    AppMain,
    Navbar,
    RightPanel,
    Settings,
    Sidebar,
    TagsView,
  },
  mixins: [ResizeMixin],
  computed: {
    ...mapState({
      theme: (state) => state.settings.theme,
      sideTheme: (state) => state.settings.sideTheme,
      sidebar: (state) => state.app.sidebar,
      device: (state) => state.app.device,
      needTagsView: (state) => state.settings.tagsView,
      fixedHeader: (state) => state.settings.fixedHeader,
    }),
    classObj() {
      return {
        hideSidebar: !this.sidebar.opened,
        openSidebar: this.sidebar.opened,
        withoutAnimation: this.sidebar.withoutAnimation,
        mobile: this.device === "mobile",
      };
    },
    variables() {
      return variables;
    },
  },
  data() {
    return {
      shortName: "",
      copyright: "",
      version: "",
      logo: "",
    };
  },
  created() {
    this.getSysInfo();
  },
  methods: {
    getSysInfo() {
      getSysInfo().then((res) => {
        this.shortName = res.shortName;
        this.copyright = res.copyright;
        this.version = res.version + " (" + res.versionNo + ")";
      });
      this.logo = this.$store.state.settings.websiteLogo
        ? process.env.VUE_APP_BASE_API + this.$store.state.settings.websiteLogo
        : "";
    },
    handleClickOutside() {
      this.$store.dispatch("app/closeSideBar", {withoutAnimation: false});
    },
  },
};
</script>

<style lang="scss" scoped>
  @import "~@/assets/styles/mixin.scss";
  @import "~@/assets/styles/variables.scss";

  .app-wrapper {
    @include clearfix;
    position: relative;
    height: 100%;
    width: 100%;

    &.mobile.openSidebar {
      position: fixed;
      top: 0;
    }
  }

  .drawer-bg {
    background: #000;
    opacity: 0.3;
    width: 100%;
    top: 0;
    height: 100%;
    position: absolute;
    z-index: 999;
  }

  .fixed-header {
    position: fixed;
    top: 0;
    right: 0;
    z-index: 9;
    width: calc(100% - #{$base-sidebar-width});
    transition: width 0.28s;
  }

  .hideSidebar .fixed-header {
    width: calc(100% - 54px);
  }

.mobile .fixed-header {
  width: 100%;
}

.my-footer {
  position: fixed;
  // left: calc(200px - #{$base-sidebar-width});
  right: 0;
  bottom: 0;
  height: 32px;
  line-height: 32px;
  padding: 0 15px;
  background-color: #eee;
  font-family: "Helvetica Neue";
  text-align: center;
  z-index: 999;
  transition: width 0.28s;
  width: 100%;
}

  .sidebarHide .fixed-header {
    width: 100%;
  }

  .mobile .fixed-header {
    width: 100%;
  }
</style>
