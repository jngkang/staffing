<template>
    <div class="login_container">
        <div class="login_bar">
            <marquee behavior=alternate>~~~欢迎使用（系统名称）~~~</marquee>
        </div>
        <!-- 登陆块 -->
        <div class="login_box">
            <!--标题区域-->
            <div class="login_title">
                <span class="title1">系统名称</span>
                <br>
                <span class="title2">系统英文名称</span>
            </div>
            <!--表单区域-->
            <el-form ref="loginFormRef" :rules="loginRules" :model="loginForm" class="login_form" label-width="0">
                <!-- 用户名 -->
                <el-form-item prop="username">
                    <el-input v-model="loginForm.username" prefix-icon="iconfont icondenglu"></el-input>
                </el-form-item>
                <!-- 密码 -->
                <el-form-item prop="password">
                    <el-input v-model="loginForm.password" prefix-icon="iconfont iconmima" type="password"></el-input>
                </el-form-item>
                <!-- 按钮 -->
                <el-form-item class="btns">
                    <el-button type="primary" @click="login()" style="width: 300px">提交</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>
<script>
import {setRoutes} from "@/router";

export default {
    data() {
        return {
            loginForm: {
                username: "admin",
                password: "123456",
            },
            loginRules: {
                // 校验用户名
                username: [
                    {required: true, message: '请输入用户名', trigger: 'blur'},// 必填项验证
                    {min: 3, max: 10, message: "长度在3到10个字符", trigger: "blur"},
                ],
                // 校验密码
                password: [
                    {required: true, message: '请输入用户名密码', trigger: 'blur'},// 必填项验证
                    {min: 3, max: 10, message: "长度在3到10个字符", trigger: "blur"},
                ]
            },
        }
    },
    methods: {
        // 登录方法
        login() {
            // 1、验证校验规则
            this.$refs.loginFormRef.validate(async valid => {
                if (!valid) return;// 验证失败
                // const {data: res} = await this.$http.post("/user/login", this.loginForm);// 访问后台---axios请求方式
                // 访问后台
                this.request.post("/user/login", this.loginForm).then(res => {
                    if (res.code === '200') {
                        this.$message.success("登录成功");// 信息提示
                        window.localStorage.setItem("user", JSON.stringify(res.data));
                        window.localStorage.setItem("menus", JSON.stringify(res.data.menus))  // 存储用户信息到浏览器
                        // window.sessionStorage.setItem("user", res.user);// 存储user对象

                        // 动态设置当前用户的路由
                        setRoutes();

                        this.$router.push({path: "/home"});// 页面路由跳转
                    } else {
                        this.$message.error(res.msg); // 错误提示
                    }
                })
            });
        }
    }
}
</script>
<style scoped>

.login_bar {
    height: 30px;
    /*设置透明的背景色*/
    background-color: rgba(255, 255, 255, 0.4);
    font-size: 15px;
    line-height: 2em;
    border-radius: 20px;
    width: 99%;
    position: absolute;
    left: 50%;
    top: 1%;
    transform: translate(-50%);
}

/*根节点样式*/
.login_container {
    /*background-image: url("src/assets/imgs/bg.jpg");*/
    background-image: url("../assets/imgs/bg.jpg");
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center center; /* 背景图垂直、水平均居中 */
    background-attachment: fixed;
    /*background-color: #2b4b6b;*/
    height: 100%;
}

.login_box {
    width: 400px;
    height: 300px;
    background-color: #fff;
    border-radius: 20px;
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
}

.login_title {
    text-align: center;
    padding-top: 20px;
}

.title1 {
    font-size: 36px;
    font-family: 华文楷体;
    font-weight: bold;
}

img {
    width: 100%;
    height: 100%;
    border-radius: 50%;
    background-color: #eee;
}

.btns {
    display: flex;
    justify-content: center;
}

.login_form {
    position: absolute;
    bottom: 0%;
    width: 100%;
    padding: 0 20px;
    box-sizing: border-box;
}
</style>