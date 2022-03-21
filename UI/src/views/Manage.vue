<template>
    <div>
        <el-container style="height: 100vh;">
            <el-menu class="el-menu-vertical-demo"
                     width="200px"
                     :default-openeds="opens"
                     :collapse="isCollapse"
                     active-text-color="#ffd04b"
                     :collapse-transition="false"
                     background-color="rgb(48, 65, 86)"
                     text-color="#fff" unique-opened router>

                <div style="height: 60px; text-align: center;">
                    <!--                    <img src="../assets/logo.png" alt=""-->
                    <!--                         style="width: 50px; position: relative; top: 5px; border-radius: 50%;">-->
                    <i class="el-icon-collection" style="color: #ffffff; font-size: 20px; margin-top: 20px"></i>
                    <b style="color: white; margin-left: 5px; line-height: 60px" v-show="logoTextShow">系统名称</b>
                </div>

                <div v-for="item in menus" :key="item.id">
                    <div v-if="item.path">
                        <el-menu-item :index="item.path">
                            <i :class="item.icon"></i>
                            <span slot="title">{{ item.name }}</span>
                        </el-menu-item>
                    </div>
                    <div v-else>
                        <el-submenu :index="''+item.id">
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
            <el-container>
                <el-header
                    style="font-size: 12px; border-bottom: 1px solid #ccc; line-height: 60px; display: flex;">
                    <div style="text-align: center; font-size: 28px" @click="collapse">
                        <i class="el-icon-s-fold mr-10"></i>
                    </div>
                    <div style="flex: 1; font-size: 50px">
                        <!-- 面包屑导航 -->
                        <el-breadcrumb separator-class="el-icon-arrow-right" style="line-height: 60px;" id="breadcrumb">
                            <el-breadcrumb-item :to="'/home'">首页</el-breadcrumb-item>
                            <el-breadcrumb-item>{{ currentPathName }}</el-breadcrumb-item>
                        </el-breadcrumb>
                    </div>
                    <el-dropdown style="width: 150px; cursor: pointer;text-align: right" placement="bottom">
                        <span>
                            <img :src="user.avatarUrl" alt=""
                                 style="width: 40px;height: 40px; border-radius: 50%;position: relative;top: 10px;right: 5px">
                            <span style="position: relative;top: -5px">
                                {{ user.username }}
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

                <el-main>
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
        this.opens = this.user.menus.map(v => v.id + '');
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
