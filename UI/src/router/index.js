import Vue from 'vue'
import VueRouter from 'vue-router'
//引入Login组件
import Login from '../components/Login.vue'
import Manage from '../components/Manage.vue'

import store from "@/store"
import notFound from "../components/error.vue";

Vue.use(VueRouter)

const routes = [// 将”/“重定向到login页面
    {
        path: "/", // 设置默认打开的页面
        redirect: "/login"
    }, // 添加login页面的路由
    {
        path: "/login",
        component: Login
    },
    {
        path: "*",
        component: notFound,
    }
]

const router = new VueRouter({
    mode: 'history', base: process.env.BASE_URL, routes,
})

// 提供一个重置路由的方法
export const resetRouter = () => {
    router.matcher = new VueRouter({
        mode: 'history',
        base: process.env.BASE_URL,
        routes
    });
}

// 注意：刷新页面会导致页面路由重置
export const setRoutes = () => {
    const storeMenus = localStorage.getItem("menus");
    if (storeMenus) {
        // 拼装动态路由
        const manageRoute = { path: '/', name: 'Manage', component: () => import('../components/Manage.vue'), redirect: "/home", children: [
                { path: 'person', name: '个人信息', component: () => import('../components/Person.vue')},
                { path: 'password', name: '修改密码', component: () => import('../components/Password.vue')},
            ] }
        const menus = JSON.parse(storeMenus)
        menus.forEach(item => {
            if (item.path) {  // 当且仅当path不为空的时候才去设置路由
                let itemMenu = { path: item.path.replace("/", ""), name: item.name, component: () => import('../views/' + item.pagePath + '.vue')}
                manageRoute.children.push(itemMenu)
            } else if(item.children.length) {
                item.children.forEach(item => {
                    if (item.path) {
                        let itemMenu = { path: item.path.replace("/", ""), name: item.name, component: () => import('../views/' + item.pagePath + '.vue')}
                        manageRoute.children.push(itemMenu)
                    }
                })
            }
        })

        // 获取当前的路由对象名称数组
        const currentRouteNames = router.getRoutes().map(v => v.name)
        if (!currentRouteNames.includes('Manage')) {
            // 动态添加到现在的路由对象中去
            router.addRoute(manageRoute)
        }

    }
}

// 重置我就再set一次路由
setRoutes()

// 挂载路由导航守卫
router.beforeEach((to, from, next) => {
    localStorage.setItem("currentPathName", to.name)  // 设置当前的路由名称，为了在Header组件中去使用
    store.commit("setPath")  // 触发store的数据更新
    next();// 符合要求，放行
});

export default router
