<template>
    <div>
        <el-card style="width: 60%; border: 1px solid #cccccc; padding: 20px; margin: auto; margin-top: 7%;">
            <el-form :model="form" :rules="formRules" ref="formRef" label-width="80px">
                <el-form-item label="请假原因" prop="reason">
                    <el-input
                        type="textarea"
                        :rows="15"
                        placeholder="请输入内容"
                        v-model="form.reason">
                    </el-input>
                </el-form-item>
                <el-form-item label="请假时间" prop="leaveDateTime">
                    <el-date-picker
                        v-model="form.leaveDateTime"
                        type="datetimerange"
                        format="yyyy-MM-dd HH:mm"
                        value-format="yyyy-MM-dd HH:mm"
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期">
                    </el-date-picker>
                </el-form-item>
                <el-button type="primary" @click="save" style="margin-left: 45%;">确定</el-button>
            </el-form>
        </el-card>
    </div>
</template>

<script>

import router from "@/router";

export default {
    name: "VacationRequest.vue",
    data() {
        return {
            // 修改用户的信息
            form: {
                reason: "",
                leaveDateTime: [],
            },

            // 修改的表单验证
            formRules: {
                reason: [
                    {required: true, message: "请输入请假原因", trigger: "blur"},
                ],
                leaveDateTime: [
                    {required: true, message: "请选择请假时间", trigger: "blur"},
                ],
            },
            user: {},
        }
    },
    created() {
        this.user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {};
    },
    methods: {
        // 保存用户信息方法
        save() {
            let submitForm = {};
            this.$set(submitForm, "empno", this.user.empno);
            this.$set(submitForm, "reason", this.form.reason);
            this.$set(submitForm, "startDatetime", this.form.leaveDateTime[0]);
            this.$set(submitForm, "endDatetime", this.form.leaveDateTime[1]);
            this.$refs.formRef.validate((valid) => {
                if (valid) {
                    this.request.post("/vacation/insert", submitForm).then(res => {
                        if (res.data) {
                            this.$message.success("提交成功");
                            this.$refs.formRef.resetFields();// 重置信息
                        } else {
                            this.$message.error("提交失败");
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