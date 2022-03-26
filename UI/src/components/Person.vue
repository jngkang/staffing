<template>
    <div>
        <el-card style="width: 60%; border: 1px solid #cccccc; padding: 20px; margin: auto;">
            <el-form :model="form" :rules="formRules" ref="formRef" label-width="80px">
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

                <el-row>
                    <el-col :span="this.cardLeft">
                        <el-form-item label="员工编号" prop="empno">
                            <el-input v-model="form.empno" disabled></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="this.cardCenter"><span>&nbsp;</span></el-col>
                    <el-col :span="this.cardRight">
                        <el-form-item label="员工姓名" prop="name">
                            <el-input v-model="form.name"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="this.cardLeft">
                        <el-form-item label="角色" prop="roleId">
                            <el-select v-model="form.roleId" placeholder="请选择角色" style="width: 100%">
                                <el-option
                                    v-for="item in roleOptions"
                                    :key="item.roleId"
                                    :label="item.name"
                                    :value="item.roleId">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="this.cardCenter"><span>&nbsp;</span></el-col>
                    <el-col :span="this.cardRight">
                        <el-form-item label="部门名称" prop="deptno">
                            <el-select v-model="form.deptno" placeholder="请选择部门"
                                       @click.native="deptSelectEditClick"
                                       @change="deptSelectChange"
                                       style="width: 100%">
                                <el-option
                                    v-for="item in deptOptions"
                                    :key="item.deptno"
                                    :label="item.name"
                                    :value="item.deptno">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="this.cardLeft">
                        <el-form-item label="性别" prop="sex">
                            <el-radio-group v-model="form.sex">
                                <el-radio :label="1" style="margin-left: 30px;">男</el-radio>
                                <el-radio :label="0">女</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </el-col>
                    <el-col :span="this.cardCenter"><span>&nbsp;</span></el-col>
                    <el-col :span="this.cardRight">
                        <el-form-item label="岗位名称" prop="postno">
                            <el-select v-model="form.postno" placeholder="请选择岗位"
                                       @click.native="formCheckData"
                                       style="width: 100%">
                                <el-option
                                    v-for="item in postOptions"
                                    :key="item.postno"
                                    :label="item.name"
                                    :value="item.postno">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="this.cardLeft">
                        <el-form-item label="民族" prop="nation">
                            <el-input v-model="form.nation"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="this.cardCenter"><span>&nbsp;</span></el-col>
                    <el-col :span="this.cardRight">
                        <el-form-item label="籍贯" prop="province">
                            <el-input v-model="form.province"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="this.cardLeft">
                        <el-form-item label="政治面貌" prop="political">
                            <el-input v-model="form.political"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="this.cardCenter"><span>&nbsp;</span></el-col>
                    <el-col :span="this.cardRight">
                        <el-form-item label="手机号" prop="phone">
                            <el-input v-model="form.phone"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="this.cardLeft">
                        <el-form-item label="证件号" prop="identifyNo">
                            <el-input v-model="form.identifyNo"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="this.cardCenter"><span>&nbsp;</span></el-col>
                    <el-col :span="this.cardRight">
                        <el-form-item label="现住址" prop="address">
                            <el-input v-model="form.address"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :span="24" style="text-align: center; margin-top: 20px">
                    <el-button type="primary" @click="save">确 定</el-button>
                </el-row>
            </el-form>
        </el-card>
    </div>
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
            formRules: {
                name: [
                    {required: true, message: '请输入员工姓名', trigger: 'blur'},
                    {min: 3, max: 20, message: '长度在3到20个字符', trigger: 'blur'},
                ],
                roleId: [
                    {required: true, message: '请选择角色', trigger: 'blur'},
                ],
                deptno: [
                    {required: true, message: '请选择部门', trigger: 'blur'},
                ],
                postno: [
                    {required: true, message: '请选择岗位', trigger: 'blur'},
                ],
                sex: [
                    {required: true, message: '请选择性别', trigger: 'blur'},
                ],
                identifyNo: [
                    {required: true, message: '请输入证件号', trigger: 'blur'},
                    {min: 18, max: 18, message: '请输入有效证件号', trigger: 'blur'},
                ],
                nation: [
                    {required: true, message: '请输入民族', trigger: 'blur'},
                    {min: 1, max: 10, message: '长度在1到10个字符', trigger: 'blur'},
                ],
                province: [
                    {required: true, message: '请输入籍贯', trigger: 'blur'},
                    {min: 2, max: 60, message: '长度在2到60个字符', trigger: 'blur'},
                ],
                political: [
                    {required: true, message: '请输入政治面貌', trigger: 'blur'},
                    {min: 2, max: 20, message: '长度在2到20个字符', trigger: 'blur'},
                ],
                phone: [
                    {required: true, message: '请输入手机号', trigger: 'blur'},
                    {pattern: /^1[34578]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur'},
                ],
                address: [
                    {required: true, message: '请输入现住址', trigger: 'blur'},
                    {min: 2, max: 255, message: '长度在2到255个字符', trigger: 'blur'},
                ],
            },
            imageUrl: '',
            disabled: false,
            cardLeft: 11,
            cardCenter: 1,
            cardRight: 11,
            // 下拉菜单，查询出的部门信息
            deptOptions: [],
            // 下拉菜单，查询出的岗位信息
            postOptions: [],
            // 临时存储所有的岗位信息
            tempPostOptions: [],
            // 下拉菜单，查询出角色信息
            roleOptions: [],
        }
    },
    created() {
    },
    mounted() {
        this.request.get("/role").then(res => {
            this.roleOptions = res.data;
        });
        this.request.get("/department").then(res => {
            this.deptOptions = res.data;
        });
        // 默认将查询到的所有岗位信息填充到临时变量中，偏于下拉菜单的选择
        this.request.get("/post").then(res => {
            this.tempPostOptions = res.data;
            console.log(res.data);
        });
        this.request.get("/employee/" + this.user.empno).then(res => {
            this.form = res.data;
            this.form.sex = res.data.sex ? 1 : 0;

            if (this.form.deptno != "") {
                this.tempPostOptions.forEach(r => {
                    if (r.deptno == this.form.deptno) {
                        this.postOptions.push(r);
                    }
                });
            }

            console.log(res.data);
        });
    },
    methods: {
        // 编辑用户信息方法
        save() {
            this.request.post("/employee", this.form).then(res => {
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
        // 保证在选择部门信息时，岗位信息直接置为空
        deptSelectEditClick() {
            this.form.postno = "";
        },
        // 添加页面和修改页面，在选择部门信息后，填充该部门的岗位信息
        deptSelectChange(deptno) {
            this.postOptions = [];
            this.tempPostOptions.forEach(r => {
                if (r.deptno == deptno) {
                    this.postOptions.push(r);
                }
            });
        },
        // 在直接选择岗位信息时，检查是否选择部门信息
        formCheckData() {
            if (this.form.deptno == "") {
                return this.$message.warning("请选择部门信息");
            }
        },
    }
}
</script>

<style>
.avatar-uploader {
    text-align: center;
    margin-bottom: 60px;
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