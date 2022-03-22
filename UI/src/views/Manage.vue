<template>
    <div>
        <el-container style="height: 100vh;">
            <el-header
                style="font-size: 12px; background-color: #373d41; line-height: 60px; display: flex; justify-content: space-between">
                <div style="height: 60px; text-align: center;">
                    <span class="title">大型企业员工信息管理系统</span>
                </div>
                <el-dropdown style="width: 150px; color: #ffffff; cursor: pointer;text-align: right" placement="bottom">
                        <span>
                            <img :src="user.avatarUrl" alt=""
                                 style="width: 40px;height: 40px; border-radius: 50%;position: relative;top: 10px;right: 5px">
                            <span style="position: relative;top: -5px">
                                {{ user.nickname }}
                                <i class="el-icon-arrow-down"></i>
                            </span>
                        </span>
                    <el-dropdown-menu slot="dropdown" style="text-align: center; margin-left: 15px;">
                        <router-link to="/person" style="text-decoration: none;">
                            <el-dropdown-item>
                                个人信息
                            </el-dropdown-item>
                        </router-link>
                        <router-link to="/password" style="text-decoration: none;">
                            <el-dropdown-item>
                                修改密码
                            </el-dropdown-item>
                        </router-link>
                        <el-dropdown-item @click.native="logout">退出</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </el-header>
            <el-container>
                <el-menu class="el-menu-vertical-demo"
                         width="200px"
                         :collapse="isCollapse"
                         active-text-color="#409eff"
                         :collapse-transition="false"
                         background-color="#545c64"
                         text-color="#ffffff"
                         :default-openeds="opens"
                         router>
                    <!--                    unique-opened 默认只打开一个菜单项-->

                    <div style="font-size: 20px; text-align: center; color: #fff;background-color: #434a50" @click="collapse">
                        <i class="el-icon-s-fold"></i>
                    </div>

                    <div v-for="item in menus" :key="item.id">
                        <div v-if="item.path">
                            <el-menu-item :index="item.path">
                                <i :class="item.icon"></i>
                                <span slot="title">{{ item.name }}</span>
                            </el-menu-item>
                        </div>
                        <div v-else>
                            <el-submenu :index="''+item.menuId">
                                <template slot="title">
                                    <i :class="item.icon"></i>
                                    <span slot="title" v-show="logoTextShow">{{ item.name }}</span>
                                </template>
                                <div v-for="subItem in item.children" :key="subItem.id">
                                    <el-menu-item :index="subItem.path">
                                        <i :class="subItem.icon"></i>
                                        <span slot="title">{{ subItem.name }}</span>
                                    </el-menu-item>
                                </div>
                            </el-submenu>
                        </div>
                    </div>
                </el-menu>
                <el-main>
                    <div class="main_breadcrumb">
                        <!-- 面包屑导航 -->
                        <el-breadcrumb separator-class="el-icon-arrow-right" id="breadcrumb">
                            <el-breadcrumb-item :to="'/home'">首页</el-breadcrumb-item>
                            <el-breadcrumb-item>{{ currentPathName }}</el-breadcrumb-item>
                        </el-breadcrumb>
                    </div>
                    <!-- 当前页面的子路由会在router-view里展示 -->
                    <router-view @refreshUser="getUserInfo"></router-view>
                </el-main>

            </el-container>
        </el-container>
    </div>
</template>

<style>
.el-menu-vertical-demo:not(.el-menu--collapse) {
    width: 200px;
    min-height: 400px;
    overflow-x: hidden;
    /*box-shadow: rgb(0 21 41 / 35%) 0px 0px 6px; 添加左菜单栏阴影,效果不好*/

}

.title {
    font-size: 36px;
    font-family: 华文楷体;
    font-weight: bold;
    color: white;
    margin-left: 5px;
    line-height: 60px
}

.main_breadcrumb {
    flex: 1;
    font-size: 50px;
    margin-bottom: 18px;
}

</style>

<script>
import {setRoutes} from "@/router";

export default {
    name: "Home",
    data() {
        return {
            menuList: [],// 菜单集合
            isCollapse: false,
            menuInfo: {},
            logoTextShow: true,
            user: {},
            menus: [],
            // 菜单全部展开
            opens: [],
        }
    },
    // 相当于onload事件
    created() {
        // 获取登录用户的信息
        this.user = window.localStorage.getItem("user") ? JSON.parse(window.localStorage.getItem("user")) : {};

        // 获取菜单数据
        this.menus = window.localStorage.getItem("menus") ? JSON.parse(window.localStorage.getItem("menus")) : {};


        // 设置所有菜单展开
        this.opens = this.user.menus.map(v => '' + v.menuId);
    },
    computed: {
        currentPathName() {
            return this.$store.state.currentPathName;　　//需要监听的数据
        }
    },
    // 打印当前路由的名称
    // watch: {
    //     currentPathName(newVal, oldVal) {
    //         console.log(newVal)
    //     }
    // },
    methods: {
        // 获取用户个人信息
        getUserInfo() {
            // 从后台获取数据
            if (this.user) {
                this.request.get("/user/username/" + this.user.username).then(res => {
                    this.user = res.data;
                });
            }
        },
        // 安全退出
        logout() {
            this.$store.commit("logout");
            this.$message.success("退出成功"); // 访问失败，并提示错误信息
        },
        // 获取导航菜单
        getMenuList() {
            // const {data: res} = await this.$http.get("mainmenu/menus");// axios请求方式
            this.request.get("mainmenu/menus").then(res => {
                console.log(res);
                this.menuInfo = res;
                if (res.flag != 200) return this.$message.error("获取列表失败！！！"); // 访问失败，并提示错误信息
                this.menuList = res.menus;
            });
        },
        // 控制导航栏伸缩
        collapse() {
            this.isCollapse = !this.isCollapse;
            this.logoTextShow = !this.logoTextShow;
        },
    }
}
</script>
