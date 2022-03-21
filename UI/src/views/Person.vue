<template>
    <el-card style="width: 35%; border: 1px solid #cccccc; padding: 20px; margin: auto; text-align: center;">
        <el-form :model="form" :rules="editFormRules" ref="editFormRef" label-width="80px">
            <el-tooltip effect="dark" content="点击更换头像" placement="top" :enterable="false">
                <el-upload
                    class="avatar-uploader"
                    action="http://localhost:9090/files/upload"
                    :show-file-list="false"
                    :on-success="handleAvatarSuccess">
                    <img v-if="form.avatarUrl" :src="form.avatarUrl" class="avatar">
                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
            </el-tooltip>

            <el-form-item label="用户名" prop="username">
                <el-input v-model="form.username" disabled></el-input>
            </el-form-item>
            <el-form-item label="昵称/姓名" prop="nickname">
                <el-input v-model="form.nickname"></el-input>
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
                <el-input v-model="form.email"></el-input>
            </el-form-item>
            <el-button type="primary" @click="save">确 定</el-button>
        </el-form>
    </el-card>
</template>

<script>
import {setRoutes} from "@/router";

export default {
    name: "Person",
    data() {
        return {
            // 修改用户的信息
            form: {},
            user: window.localStorage.getItem("user") ? JSON.parse(window.localStorage.getItem("user")) : {},
            // 修改的表单验证
            editFormRules: {
                email: [
                    {required: true, message: "请输入邮箱", trigger: "blur"},
                    {type: 'email', message: "请输入正确的邮箱地址", trigger: "blur"},
                ],
            },
            imageUrl: '',
            disabled: false,
        }
    },
    created() {
        this.getUser().then(res => {
            this.form = res
        })
    },
    methods: {
        async getUser() {
            return (await this.request.get("/user/username/" + this.user.username)).data
        },
        // 编辑用户信息方法
        save() {
            this.request.post("/user", this.form).then(res => {
                if (res.code === '200') {
                    this.$message.success("保存成功")

                    // 出发父级更新User方法
                    this.$emit("refreshUser");

                    // 更新浏览器存储的用户信息
                    this.getUser().then(res => {
                        res.token = JSON.parse(localStorage.getItem("user")).token;
                        localStorage.setItem("user", JSON.stringify(res));
                    })
                } else {
                    this.$message.error("保存失败")
                }
            });
        },
        handleAvatarSuccess(res) {
            this.form.avatarUrl = res.data.url;
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

.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 138px;
    height: 138px;
    line-height: 138px;
    text-align: center;
    border-radius: 50%;
    border: 1px #8c939d dotted;
}
</style>