import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { getToken } from '@/utils/auth'
import { isRelogin } from '@/utils/request'

NProgress.configure({ showSpinner: false })

const whiteList = ['regx:/login/.*', '/auth-redirect', '/bind', '/register', '/common/sysInfo', '/system/website/config',
  '/', '/stop', '/shop', '/queryOrder', '/billOrder', '/queryCard', '/chargeCenter', '/unbindDevice', '/getLicense',
  '/getCardList', '/getShopConfig', 'regx:/sale/shop/notify/.*'
]

const navWhiteList = ['/shop', '/queryOrder', '/queryCard', '/chargeCenter', '/unbindDevice']

router.beforeEach((to, from, next) => {
  NProgress.start()
  if (getToken()) {
    to.meta.title && store.dispatch('settings/setTitle', to.meta.title)
    /* has token*/
    if (to.path === '/login') {
      next({ path: '/index' })
      NProgress.done()
    } else if (whiteList.indexOf(to.path) !== -1) {
      if(navWhiteList.indexOf(to.path) !== -1) {
        if(store.state.settings.navList.length === 0) {
          store.dispatch('settings/GetNavList').then((res) => {
            let navList = res.data;
            let flag = false;
            for (let item of navList) {
              if ('/' + item.path === to.path || item.path === to.path) {
                flag = true;
                break;
              }
            }
            if (!flag) {
              next({ path: '/' })
            } else {
              next()
            }
          }).catch(err => {
            Message.error(err)
          })
        } else {
          let navList = store.state.settings.navList;
          let flag = false;
          for (let item of navList) {
            if ('/' + item.path === to.path || item.path === to.path) {
              flag = true;
              break;
            }
          }
          if (!flag) {
            next({ path: '/' })
          } else {
            next()
          }
        }
      } else {
        // 在免登录白名单，直接进入
        next()
      }
    } else {
      if (store.getters.roles.length === 0) {
        isRelogin.show = true
        // 判断当前用户是否已拉取完user_info信息
        store.dispatch('GetInfo').then(() => {
          isRelogin.show = false
          store.dispatch('GenerateRoutes').then(accessRoutes => {
            // 根据roles权限生成可访问的路由表
            router.addRoutes(accessRoutes) // 动态添加可访问路由表
            next({ ...to, replace: true }) // hack方法 确保addRoutes已完成
          })
        }).catch(err => {
            store.dispatch('LogOut').then(() => {
              Message.error(err)
              next({ path: '/index' })
            })
          })
      } else {
        next()
      }
    }
  } else {
    // console.log(to.path);
    // 没有token
    if (whiteList.indexOf(to.path) !== -1) {
      if(navWhiteList.indexOf(to.path) !== -1) {
        if(store.state.settings.navList.length === 0) {
          store.dispatch('settings/GetNavList').then((res) => {
            let navList = res.data;
            let flag = false;
            for (let item of navList) {
              if ('/' + item.path === to.path || item.path === to.path) {
                flag = true;
                break;
              }
            }
            if (!flag) {
              next({ path: '/' })
            } else {
              next()
            }
          }).catch(err => {
            Message.error(err)
          })
        } else {
          let navList = store.state.settings.navList;
          let flag = false;
          for (let item of navList) {
            if ('/' + item.path === to.path || item.path === to.path) {
              flag = true;
              break;
            }
          }
          if (!flag) {
            next({ path: '/' })
          } else {
            next()
          }
        }
      } else {
        // 在免登录白名单，直接进入
        next()
      }
    } else {
      var pass = false;
      for (var item of whiteList) {
        if (item.substring(0, 5) == "regx:") {
          var regx = item.replace("regx:", "")
          var patt = new RegExp(regx);
          if (patt.test(to.path)) {
            pass = true;
            break;
          }
        }
      }
      if (pass) {
        next();
      } else {
        // next(`/login?redirect=${to.fullPath}`) // 否则全部重定向到登录页
        if (to.path == '/admin/index') {
          // console.log(store.getters.safeEntrance)
          // console.log(store.getters.safeEntrance.length)
          if (store.getters.safeEntrance.length == 0) {
            store.dispatch('settings/GetSafeEntrance').then(() => {
              // console.log(store.getters.safeEntrance)
              // console.log(store.getters.safeEntrance.length)
              if (store.getters.safeEntrance == '1') {
                next(`/`)
              } else {
                next(`/login?redirect=${encodeURIComponent(to.fullPath)}`)
              }
            }).catch(err => {
              store.dispatch('LogOut').then(() => {
                Message.error(err)
                next({
                  path: '/index'
                })
              })
            })
          } else {
            if (store.getters.safeEntrance == '1') {
              next(`/`)
            } else {
              next(`/login?redirect=${encodeURIComponent(to.fullPath)}`)
            }
          }
        }
        NProgress.done();
      }
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})
