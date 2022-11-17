import router, { dynamicRoutes } from './router'
import store from './store'
import auth from '@/plugins/auth'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/utils/auth' // get token from cookie
import getPageTitle from '@/utils/get-page-title'
import { getRouters } from '@/api/system/user'
import { filterAsyncRouter } from '@/store/modules/permission'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['/login'] // no redirect whitelist

router.beforeEach(async(to, from, next) => {
  // start progress bar
  NProgress.start()

  // set page title
  document.title = getPageTitle(to.meta.title)

  // 确定用户是否已登录
  const hasToken = getToken()

  if (hasToken) {
    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done()
    } else {
      if (!store.getters.loadMenus) {
        // get user info -> fetch roles/permission
        store.dispatch('user/getInfo').then(() => {
        })
        store.dispatch('user/updateLoadMenus').then(() => {
        })
      } else {
        next()
      }
      loadMenus(next, to)
      NProgress.done()
    }
  } else {
    // 没有token
    if (whiteList.indexOf(to.path) !== -1) {
      // in the free login whitelist, go directly
      next()
    } else {
      // other pages that do not have permission to access are redirected to the login page.
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

export const loadMenus = (next, to) => {
  getRouters().then(res => {
    const data = res.data
    const sdata = JSON.parse(JSON.stringify(data))
    const rdata = JSON.parse(JSON.stringify(data))
    // 侧边
    const sidebarRoutes = filterAsyncRouter(sdata)
    const rewriteRoutes = filterAsyncRouter(rdata, false, true)

    const result = []
    dynamicRoutes.forEach(route => {
      if (route.permissions) {
        if (auth.hasPermiOr(route.permissions)) {
          result.push(route)
        }
      } else if (route.roles) {
        if (auth.hasRoleOr(route.roles)) {
          result.push(route)
        }
      }
    })
    console.log(result)
    router.addRoutes(result)

    store.dispatch('permission/setSidebarRouters', sidebarRoutes).then(() => {
      sidebarRoutes.push({ path: '*', redirect: '/404', hidden: true })
    })
    store.dispatch('permission/generateRoutes', rewriteRoutes).then(() => { // 存储路由
      rewriteRoutes.push({ path: '*', redirect: '/404', hidden: true })
      router.addRoutes(rewriteRoutes) // 动态添加可访问路由表
      next({ ...to, replace: true })
    }).catch(err => {
      store.dispatch('user/logout').then(() => {
        Message.error(err)
        next({ path: '/' })
      })
    })
  })
}

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
