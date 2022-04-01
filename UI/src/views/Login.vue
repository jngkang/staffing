<template>
    <div class="login_container">
        <!-- 表单验证 -->
        <el-form ref="loginFormRef"
                 :rules="loginRules"
                 :model="loginForm">
            <div class="login_box">
                <!--标题区域-->
                <div class="login_title">
                    <span class="title1">大型企业员工信息管理系统</span>
                    <br>
                    <span class="title2">Large Enterprise Employee Information Management System</span>
                </div>
                <div style="width: 80%; height: 230px; margin: auto;" class="login_form">
                    <div>
                        <!-- 用户名 -->
                        <el-input v-model="loginForm.empno" prefix-icon="iconfont icon-yonghu"
                                  style="margin-top: 25px"
                        ></el-input>
                    </div>
                    <div>
                        <!-- 密码 -->
                        <el-input v-model="loginForm.password" prefix-icon="iconfont icon-mima" type="password"
                                  style="margin-bottom: 15px"
                        ></el-input>
                    </div>
                    <div>
                        <!-- 登录按钮 -->
                        <el-button type="primary" @click="login()" style="width: 100%;">提交</el-button>
                    </div>
                </div>
            </div>
        </el-form>
    </div>
</template>
<script>
import {setRoutes} from "@/router";

export default {
    data() {
        return {
            loginForm: {
                empno: "123456",
                password: "123456",
            },
            loginRules: {
                // 校验用户名
                empno: [
                    {required: true, message: '请输入用户名', trigger: 'blur'},// 必填项验证
                    {min: 6, max: 18, message: "长度在3到10个字符", trigger: "blur"},
                ],
                // 校验密码
                password: [
                    {required: true, message: '请输入用户名密码', trigger: 'blur'},// 必填项验证
                    {min: 6, max: 60, message: "长度在3到10个字符", trigger: "blur"},
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
                // 访问后台
                this.request.post("/employee/login", this.loginForm).then(res => {
                    if (res.code === '200') {
                        this.$message.success("登录成功");// 信息提示
                        window.localStorage.setItem("user", JSON.stringify(res.data));
                        console.log(res.data.menus);
                        window.localStorage.setItem("menus", JSON.stringify(res.data.menus))  // 存储用户信息到浏览器

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

/*根节点样式*/
.login_container {
    background-image: linear-gradient(to bottom right, #FC466B, #3F5EFB);
    height: 100%;
    /* 设置登录窗口剧中 */
    display: flex;
    justify-content: center;
    align-items: center;
}

.login_box {
    width: 500px;
    height: 350px;
    background-color: #fff;
    border-radius: 20px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}

.login_form {
    display: flex;
    flex-direction: column;
    justify-content: space-around;
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

</style>