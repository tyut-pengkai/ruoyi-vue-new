import Vue from 'vue'
import Router from 'vue-router'
/* Layout */
import Layout from '@/layout'

Vue.use(Router)

/**
 * Note: 路由配置项
 *
 * hidden: true                     // 当设置 true 的时候该路由不会再侧边栏出现 如401，login等页面，或者如一些编辑页面/edit/1
 * alwaysShow: true                 // 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
 *                                  // 只有一个时，会将那个子路由当做根路由显示在侧边栏--如引导页面
 *                                  // 若你想不管路由下面的 children 声明的个数都显示你的根路由
 *                                  // 你可以设置 alwaysShow: true，这样它就会忽略之前定义的规则，一直显示根路由
 * redirect: noRedirect             // 当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
 * name:'router-name'               // 设定路由的名字，一定要填写不然使用<keep-alive>时会出现各种问题
 * query: '{"id": 1, "name": "ry"}' // 访问路由的默认传递参数
 * roles: ['admin', 'common']       // 访问路由的角色权限
 * permissions: ['a:a:a', 'b:b:b']  // 访问路由的菜单权限
 * meta : {
    noCache: true                   // 如果设置为true，则不会被 <keep-alive> 缓存(默认 false)
    title: 'title'                  // 设置该路由在侧边栏和面包屑中展示的名字
    icon: 'svg-name'                // 设置该路由的图标，对应路径src/assets/icons/svg
    breadcrumb: false               // 如果设置为false，则不会在breadcrumb面包屑中显示
    activeMenu: '/system/user'      // 当路由设置了该属性，则会高亮相对应的侧边栏。
  }
 */

// 公共路由
export const constantRoutes = [{
  path: '/redirect',
  component: Layout,
  hidden: true,
  children: [{
    path: '/redirect/:path(.*)',
    component: () => import('@/views/redirect')
  }]
},
  {
    path: '/login',
    redirect: '/login/index',
  },
  {
    path: '/login/:vstr(\\S+)',
    component: () => import('@/views/login'),
    hidden: true
  },
  {
    path: '/register',
    component: () => import('@/views/register'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error/401'),
    hidden: true
  },
  {
    path: '/sale/shop/return/:code(\\S+)',
    component: () => import('@/views/sale/shop/returnPage'),
    hidden: true
  },
  {
    path: '/stop',
    component: () => import('@/views/sale/home/stop'),
    hidden: true,
  },
  {
    path: '',
    component: () => import('@/views/sale/index'),
    hidden: true,
    children: [{
      path: '/',
      component: () => import('@/views/sale/home/index'),
      name: 'Home',
    }
      // ----
    , {
      path: '/shop',
      component: () => import('@/views/sale/shop/index'),
      name: 'Shop',
    }, {
      path: '/shop/a/:appUrl(\\S+)',
      component: () => import('@/views/sale/shop/index'),
      name: 'ShopApp',
    }, {
      path: '/shop/c/:cardUrl(\\S+)',
      component: () => import('@/views/sale/shop/index'),
      name: 'ShopCardTemplate',
    }
      // ----
    , {
      path: '/queryOrder',
      component: () => import('@/views/sale/shop/queryOrder'),
      name: 'QueryOrder',
    }, {
      path: '/queryOrder/a/:appUrl(\\S+)',
      component: () => import('@/views/sale/shop/queryOrder'),
      name: 'QueryOrderApp',
    }, {
      path: '/queryOrder/c/:cardUrl(\\S+)',
      component: () => import('@/views/sale/shop/queryOrder'),
      name: 'QueryOrderCardTemplate',
    }
      // ----
    , {
      path: '/billOrder',
      component: () => import('@/views/sale/shop/billOrder'),
      name: 'BillOrder',
    }
      // ----
    , {
      path: '/getLicense',
      component: () => import('@/views/sale/license/index'),
      name: 'GetLicense',
    }, {
      path: '/getLicense/a/:appUrl(\\S+)',
      component: () => import('@/views/sale/license/index'),
      name: 'GetLicenseApp',
    }, {
      path: '/getLicense/c/:cardUrl(\\S+)',
      component: () => import('@/views/sale/license/index'),
      name: 'GetLicenseCardTemplate',
    }
      // ----
    , {
      path: '/queryCard',
      component: () => import('@/views/sale/queryCard/index'),
      name: 'QueryCard',
    }, {
      path: '/queryCard/a/:appUrl(\\S+)',
      component: () => import('@/views/sale/queryCard/index'),
      name: 'QueryCardApp',
    }
    , {
      path: '/queryCard/c/:cardUrl(\\S+)',
      component: () => import('@/views/sale/queryCard/index'),
      name: 'QueryCardCardTemplate',
    }
      // ----
    , {
      path: '/chargeCenter',
      component: () => import('@/views/sale/chargeCenter/index'),
      name: 'ChargeCenter',
    }, {
        path: '/chargeCenter/a/:appUrl(\\S+)',
        component: () => import('@/views/sale/chargeCenter/index'),
        name: 'ChargeCenterApp',
    }
    , {
      path: '/chargeCenter/c/:cardUrl(\\S+)',
      component: () => import('@/views/sale/chargeCenter/index'),
      name: 'ChargeCenterCardTemplate',
    }
      // ----
  , {
    path: '/unbindDevice',
    component: () => import('@/views/sale/unbindDevice/index'),
    name: 'UnbindDevice',
  }, {
      path: '/unbindDevice/a/:appUrl(\\S+)',
      component: () => import('@/views/sale/unbindDevice/index'),
      name: 'UnbindDeviceApp',
    }, {
      path: '/unbindDevice/c/:cardUrl(\\S+)',
      component: () => import('@/views/sale/unbindDevice/index'),
      name: 'UnbindDeviceCardTemplate',
    }]
  },
  {
    path: '/index',
    redirect: '/admin/index',
  },
  {
    path: '/admin',
    component: Layout,
    redirect: '/admin/index',
    children: [{
      path: 'index',
      component: () => import('@/views/index'),
      name: 'Index',
      meta: {
        title: '首页',
        icon: 'dashboard',
        affix: true
      }
    },]
  },
  {
    path: '/user',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [{
      path: 'profile',
      component: () => import('@/views/system/user/profile/index'),
      name: 'Profile',
      meta: {
        title: '个人中心',
        icon: 'user'
      }
    }]
  },
  // {
  //   path: '/app',
  //   component: Layout,
  //   hidden: true,
  //   children: [{
  //     path: 'user/:appId(\\d+)',
  //     component: () => import('@/views/system/appUser/index'),
  //     name: 'AppUser',
  //     meta: {
  //       title: '软件用户',
  //       activeMenu: '/authentication/app'
  //     }
  //   }, {
  //     path: 'deviceCode/:appUserId(\\d+)',
  //     component: () => import('@/views/system/deviceCode/index'),
  //     name: 'DeviceCode',
  //     meta: {
  //       title: '设备码管理old',
  //       activeMenu: '/authentication/app'
  //     }
  //   }, {
  //     path: 'appUserDeviceCode/:appUserId(\\d+)',
  //     component: () => import('@/views/system/appUserDeviceCode/index'),
  //     name: 'AppUserDeviceCode',
  //     meta: {
  //       title: '设备码管理',
  //       activeMenu: '/authentication/app'
  //     }
  //   }, {
  //     path: 'cardTemplate/:appId(\\d+)',
  //     component: () => import('@/views/system/cardTemplate/index'),
  //     name: 'CardTemplate',
  //     meta: {
  //       title: '卡类管理',
  //       activeMenu: '/authentication/app'
  //     }
  //   }, {
  //     path: 'card/:appId(\\d+)',
  //     component: () => import('@/views/system/card/index'),
  //     name: 'Card',
  //     meta: {
  //       title: '卡密管理',
  //       activeMenu: '/authentication/app'
  //     }
  //   }, {
  //     path: 'loginCodeTemplate/:appId(\\d+)',
  //     component: () => import('@/views/system/loginCodeTemplate/index'),
  //     name: 'LoginCodeTemplate',
  //     meta: {
  //       title: '单码类别',
  //       activeMenu: '/authentication/app'
  //     }
  //   }, {
  //     path: 'loginCode/:appId(\\d+)',
  //     component: () => import('@/views/system/loginCode/index'),
  //     name: 'LoginCode',
  //     meta: {
  //       title: '单码管理',
  //       activeMenu: '/authentication/app'
  //     }
  //   }, {
  //     path: 'appVersion/:appId(\\d+)',
  //     component: () => import('@/views/system/appVersion/index'),
  //     name: 'AppVersion',
  //     meta: {
  //       title: '版本管理',
  //       activeMenu: '/authentication/app'
  //     }
  //   }]
  // }
]

// 动态路由，基于用户权限动态去加载
export const dynamicRoutes = [{
  path: '/system/user-auth',
  component: Layout,
  hidden: true,
  permissions: ['system:user:edit'],
  children: [{
    path: 'role/:userId(\\d+)',
    component: () => import('@/views/system/user/authRole'),
    name: 'AuthRole',
    meta: {
      title: '分配角色',
      activeMenu: '/system/user'
    }
  }]
},
  {
    path: '/system/role-auth',
    component: Layout,
    hidden: true,
    permissions: ['system:role:edit'],
    children: [{
      path: 'user/:roleId(\\d+)',
      component: () => import('@/views/system/role/authUser'),
      name: 'AuthUser',
      meta: {
        title: '分配用户',
        activeMenu: '/system/role'
      }
    }]
  },
  {
    path: '/system/dict-data',
    component: Layout,
    hidden: true,
    permissions: ['system:dict:list'],
    children: [{
      path: 'index/:dictId(\\d+)',
      component: () => import('@/views/system/dict/data'),
      name: 'Data',
      meta: {
        title: '字典数据',
        activeMenu: '/system/dict'
      }
    }]
  },
  {
    path: '/monitor/job-log',
    component: Layout,
    hidden: true,
    permissions: ['monitor:job:list'],
    children: [{
      path: 'index/:jobId(\\d+)',
      component: () => import('@/views/monitor/job/log'),
      name: 'JobLog',
      meta: {title: '调度日志', activeMenu: '/monitor/job'}
    }]
  },
  {
    path: '/tool/gen-edit',
    component: Layout,
    hidden: true,
    permissions: ['tool:gen:edit'],
    children: [{
      path: 'index/:tableId(\\d+)',
      component: () => import('@/views/tool/gen/editTable'),
      name: 'GenEdit',
      meta: {
        title: '修改生成配置',
        activeMenu: '/tool/gen'
      }
    }]
  }
]

// 防止连续点击多次路由报错
let routerPush = Router.prototype.push;
let routerReplace = Router.prototype.replace;
// push
Router.prototype.push = function push(location) {
  return routerPush.call(this, location).catch(err => err)
}
// replace
Router.prototype.replace = function push(location) {
  return routerReplace.call(this, location).catch(err => err)
}

export default new Router({
  mode: 'history', // 去掉url中的#
  scrollBehavior: () => ({
    y: 0
  }),
  routes: constantRoutes
})
