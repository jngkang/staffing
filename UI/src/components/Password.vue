<template>
    <el-card style="width: 35%; border: 1px solid #cccccc; padding: 20px; margin: auto; text-align: center;">
        <el-form :model="form" :rules="formRules" ref="formRef" label-width="80px">
            <el-form-item label="原密码" prop="password">
                <el-input v-model="form.password" autocomplete="off" show-password></el-input>
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
                <el-input v-model="form.newPassword" autocomplete="off" show-password></el-input>
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword">
                <el-input v-model="form.confirmPassword" autocomplete="off" show-password></el-input>
            </el-form-item>
            <el-button type="primary" @click="save">确 定</el-button>
        </el-form>
    </el-card>
</template>

<script>

import router from "@/router";

export default {
    name: "Password",
    data() {
        return {
            // 修改用户的信息
            form: {
                id: "",
                password: "",
                newPassword: "",
                confirmPassword: "",
            },
            // 修改的表单验证
            formRules: {
                password: [
                    {required: true, message: "请输入密码", trigger: "blur"},
                    {min: 3, max: 10, message: "长度在3到10个字符", trigger: "blur"},
                ],
                newPassword: [
                    {required: true, message: "请输入新密码", trigger: "blur"},
                    {min: 3, max: 10, message: "长度在3到10个字符", trigger: "blur"},
                ],
                confirmPassword: [
                    {required: true, message: "请再次输入新密码", trigger: "blur"},
                    {min: 3, max: 10, message: "长度在3到10个字符", trigger: "blur"},
                ],
            },
            user: {},
        }
    },
    created() {
        this.user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {};
        if (this.user == null) {
            this.$message.error("用户登录错误，请重新登录。");
        } else {
            this.form.id = this.user.id;
        }

    },
    methods: {
        // 保存用户信息方法
        save() {
            this.$refs.formRef.validate((valid) => {
                if (valid) {
                    if (this.form.newPassword != this.form.confirmPassword) {
                        this.$message.error("两次输入的密码不一致，请重新输入。");
                    }
                    this.request.post("/user/password", this.form).then(res => {
                        console.log(res);
                        if (res.code === '200') {
                            this.$message.success("密码修改成功，请重新登录。");
                            router.push("/login");// 回到首页
                            window.localStorage.clear();// 清除session信息
                        } else {
                            this.$message.error(res.msg);
                        }
                    });
                }
            });
        },
    }
}
</script>

<style>
.avatar-uploader {
    text-align: center;
    margin-bottom: 40px;
}

.avatar-uploader .el-upload {
    border-radius: 50%;
    cursor: pointer;
    position: relative;
    overflow: hidden;
}

.avatar {
    width: 138px;
    height: 138px;
    display: block;
    border-radius: 50%;
}
</style>