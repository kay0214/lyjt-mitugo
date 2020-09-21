import Vue from 'vue'
import Router from 'vue-router'
import Layout from '../layout/index'
// import store from '../store'

Vue.use(Router)

export const constantRouterMap = [
  { path: '/login',
    meta: { title: '登录', noCache: true },
    component: (resolve) => require(['@/views/login'], resolve),
    hidden: true
  },
  {
    path: '/404',
    component: (resolve) => require(['@/views/features/404'], resolve),
    hidden: true
  },
  {
    path: '/401',
    component: (resolve) => require(['@/views/features/401'], resolve),
    hidden: true
  },
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path*',
        component: (resolve) => require(['@/views/features/redirect'], resolve)
      }
    ]
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        component: (resolve) => require(['@/views/home'], resolve),
        name: 'Dashboard',
        meta: { title: '首页', icon: 'index', affix: true, noCache: true }
      }
    ]
  },
  {
    path: '/user',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [
      {
        path: 'center',
        component: (resolve) => require(['@/views/system/user/center'], resolve),
        name: '个人中心',
        meta: { title: '个人中心' }
      }
    ]
  }
]

const router = new Router({
  mode: 'history',
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})
// router.beforeEach((to, from, next) => {
//   // 验证商户是否认证, 未认证跳转认证
//   try {
//     const { userRole, examineStatus } = store.state.user.user
//     // examineStatus 1: 已审核， 3： 审核中
//     if (to.name !== 'Login' && to.fullPath !== '/member/yxMerchantsDetail?dialog=1' && userRole === 2 && examineStatus !== 1 && examineStatus !== 3) {
//       next({ path: '/member/yxMerchantsDetail?dialog=1' })
//     } else next()
//   } catch (e) {
//     next()
//   }
// })
export default router
