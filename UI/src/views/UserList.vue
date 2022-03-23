<template>
    <div>
        <el-card>
            <div style="display: flex; ">
                <!-- 搜索添加 -->
                <el-input placeholder="用户名/昵称/姓名/邮箱" v-model="queryInfo.search"
                          style="width: 35%"
                          clearable @clear="tableLoad">
                    <el-button slot="append" icon="el-icon-search" @click="tableLoad"></el-button>
                </el-input>
                <template>
                    <el-select v-model="queryInfo.role" placeholder="请选择角色类型"
                               style="width: 12%" class="ml-10"
                               @change="roleSelectChange">
                        <el-option v-for="item in options" :key="item.name" :label="item.name" :value="item.name"/>
                    </el-select>
                </template>
                <el-button @click="reset" class="el-icon-refresh ml-10"></el-button>
            </div>
            <div style="margin-top: 13px">
                <el-button type="primary" size="small" @click="showAddDialog">
                    添加用户
                </el-button>
                <el-button type="danger" size="small" slot="reference" @click="delBatch">
                    批量删除
                </el-button>
                <el-dropdown>
                    <el-button type="primary" size="small" class="mr-10 ml-10">
                        导出<i class="el-icon-arrow-down el-icon--right"></i>
                    </el-button>
                    <el-dropdown-menu slot="dropdown" style="text-align: center;">
                        <el-dropdown-item>导出选中</el-dropdown-item>
                        <el-dropdown-item @click.native="exp">导出所有</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
                <el-dropdown>
                    <el-button type="primary" size="small" class="mr-10">
                        导入<i class="el-icon-arrow-down el-icon--right"></i>
                    </el-button>

                    <el-dropdown-menu slot="dropdown" style="text-align: center;">
                        <el-dropdown-item @click.native="exptemp">下载模板</el-dropdown-item>
                        <el-upload action="http://localhost:9090/user/import" :show-file-list="false" accept=".xlsx"
                                   :on-success="handleExcelImportSuccess"
                                   style="display: inline-block">
                            <el-dropdown-item style="width: 4em">导入</el-dropdown-item>
                        </el-upload>
                    </el-dropdown-menu>
                </el-dropdown>
            </div>
            <!-- 绘制表格 border边框，stripe隔行变色-->
            <el-table :data="tableData" border stripe
                      @selection-change="handleSelectionChange">
                <!-- 索引列 -->
                <el-table-column type="selection" width="50px" align="center"></el-table-column>
                <el-table-column label="序号" type="index" align="center" width="50px"/>
                <el-table-column label="用户名" prop="username" sortable align="center"/>
                <el-table-column label="昵称/姓名" prop="nickname" sortable align="center"/>
                <el-table-column label="角色" prop="role" sortable width="100px" align="center"/>
                <el-table-column label="邮箱" prop="email" align="center"/>
                <!--                <el-table-column label="密码" prop="password" type="password" align="center"/>-->
                <el-table-column label="状态" prop="state" width="70px" align="center">
                    <!-- 作用域插槽 -->
                    <template slot-scope="scope">
                        <!-- {{scope.row}}每一行封存的数据 -->
                        <el-switch v-model="scope.row.state" @change="userStateChange(scope.row)"></el-switch>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="175px" align="center">
                    <template slot-scope="scope">
                        <!-- 修改按钮 -->
                        <el-button type="primary" icon="el-icon-edit" size="mini"
                                   @click="showEditDialog(scope.row.id)"></el-button>
                        <!-- 删除按钮 -->
                        <el-popconfirm confirm-button-text="确定" cancel-button-text="我再想想"
                                       icon="el-icon-info" icon-color="red" title="您确认永久删除此用户的信息吗？"
                                       @confirm="deleteUser(scope.row.id)">
                            <el-button type="danger" size="mini" slot="reference" icon="el-icon-delete"
                                       class="mr-5 ml-5"></el-button>
                        </el-popconfirm>
                    </template>
                </el-table-column>
            </el-table>
            <!-- 分页组件
                    size-change：每页最大变化数
                    current-change：当前页面的变化数
                    layout：功能组件
            -->
            <div class="block mt-10">
                <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="queryInfo.pageNum"
                    :page-sizes="[10, 20, 50, 100, 99999]"
                    :page-size="queryInfo.pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total">
                </el-pagination>
            </div>
            <!-- 新增用户区域 -->
            <el-dialog title="添加用户" :visible.sync="addDialogVisible" width="35%" @close="addDialogClosed">
                <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="80px">
                    <el-form-item label="用户名" prop="username">
                        <el-input v-model="addForm.username"></el-input>
                    </el-form-item>
                    <el-form-item label="昵称/姓名" prop="nickname">
                        <el-input v-model="addForm.nickname"></el-input>
                    </el-form-item>
                    <el-form-item label="角色" prop="role">
                        <el-select clearable v-model="addForm.role" placeholder="请选择" style="width: 100%">
                            <el-option v-for="item in options" :key="item.name" :label="item.name" :value="item.name"/>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="邮箱" prop="email">
                        <el-input v-model="addForm.email"></el-input>
                    </el-form-item>
                    <el-form-item label="密码" prop="password">
                        <el-input v-model="addForm.password" type="password" show-password></el-input>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                        <el-button @click="addDialogVisible = false">取 消</el-button>
                        <el-button type="primary" @click="addUser">确 定</el-button>
                </span>
            </el-dialog>
            <!-- 修改用户信息区域 -->
            <el-dialog title="修改用户信息" :visible.sync="editDialogVisible" width="35%" @close="editDialogClosed">
                <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="80px">
                    <el-form-item label="用户名" prop="username">
                        <el-input v-model="editForm.username" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="昵称/姓名" prop="nickname">
                        <el-input v-model="editForm.nickname"></el-input>
                    </el-form-item>
                    <el-form-item label="角色" prop="role">
                        <el-select clearable v-model="editForm.role" placeholder="请选择" style="width: 100%">
                            <el-option v-for="item in options" :key="item.name" :label="item.name" :value="item.name"/>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="邮箱" prop="email">
                        <el-input v-model="editForm.email"></el-input>
                    </el-form-item>
                    <el-form-item label="密码" prop="password">
                        <el-input v-model="editForm.password" type="password" show-password></el-input>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                        <el-button @click="editDialogVisible = false">取 消</el-button>
                        <el-button type="primary" @click="editUserInfo">确 定</el-button>
                </span>
            </el-dialog>
        </el-card>
    </div>
</template>

<script>
export default {
    name: "UserList.vue",
    data() {
        return {
            // 查询信息实体
            queryInfo: {
                role: "",
                search: "",// 检索的信息
                pageNum: 1,// 当前页面
                pageSize: 10,// 一页的数据量
            },
            tableData: [],// 用户信息列表
            total: 0,// 用户信息的总记录数
            addDialogVisible: false,// 对话框状态
            // 添加表单信息
            addForm: {
                username: "",
                nickname: "",
                password: "",
                email: "",
                role: "",
            },
            // 添加的表单验证
            addFormRules: {
                username: [
                    {required: true, message: '请输入用户名', trigger: 'blur'},
                    {min: 3, max: 10, message: '长度在3到32个字符', trigger: 'blur'},
                ],
                password: [
                    {required: true, message: '请输入密码', trigger: 'blur'},
                    {min: 3, max: 10, message: '长度在3到32个字符', trigger: 'blur'},
                ],
                email: [
                    {required: true, message: '请输入邮箱', trigger: 'blur'},
                    {type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur'},
                ],
                role: [
                    {required: true, message: '请选择角色', trigger: 'blur'},
                ],
            },
            // 修改用户的信息
            editForm: {},
            // 显示和隐藏修改用户信息的弹窗
            editDialogVisible: false,
            // 修改的表单验证
            editFormRules: {
                password: [
                    {required: true, message: '请输入密码', trigger: 'blur'},
                    {min: 3, max: 10, message: '长度在3到32个字符', trigger: 'blur'},
                ],
                email: [
                    {required: true, message: '请输入邮箱', trigger: 'blur'},
                    {type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur'},
                ],
                role: [
                    {required: true, message: '请选择角色', trigger: 'blur'},
                ],
            },
            multipleSelection: [],
            // 查询出的角色信息
            options: [],
            roleOptions: [{
                value: "",
                label: "请选择角色类型",
            }],
        }
    },
    created() {
        this.roleInfoLoad();
        this.tableLoad();
    },
    methods: {
        tableLoad() {
            // 分页查询
            this.request.get("user/page", {params: this.queryInfo}).then(res => {
                // 用户列表数据封装
                this.tableData = res.data.records;
                // 总用户数量封装
                this.total = res.data.total;
            });
        },
        // 每页最大数据量触发动作
        handleSizeChange(newSize) {
            this.queryInfo.pageSize = newSize;
            this.tableLoad();
        },
        // pageNum的触发动作
        handleCurrentChange(newPage) {
            this.queryInfo.pageNum = newPage;
            this.tableLoad();
        },
        // 查询角色信息
        roleInfoLoad() {
            this.request.get("/role").then(res => {
                this.options = res.data;
                // 获取角色下拉框中的数据
                res.data.forEach(r => {
                    let temp = {};
                    temp.value = r.name;
                    temp.label = r.name;
                    this.roleOptions.push(temp);
                });
            });
        },
        reset() {
            this.queryInfo.role = "";
            this.queryInfo.search = "";
            this.queryInfo.pageNum = 1;
            this.queryInfo.pageSize = 10;
            this.tableLoad();
        },
        roleSelectChange() {
            this.tableLoad();
        },
        // 账户状态发生改变时同步到数据库
        userStateChange(userInfo) {
            this.request.post("user", userInfo).then(res => {
                if (!res.data) {
                    return this.$message.error("操作失败");
                }
                this.$message.success("操作成功");
            });
        },
        showAddDialog() {
            this.addDialogVisible = true;
        },
        // 监听添加用户操作
        addDialogClosed() {
            this.$refs.addFormRef.resetFields();
        },
        // 添加用户方法
        addUser() {
            this.$refs.addFormRef.validate(async valid => {
                //验证不成功结束方法
                if (!valid) return;
                this.request.post("/user", this.addForm).then(res => {
                    if (res.code == "600") {
                        return this.$message.error(res.msg);
                    }
                    if (!res.data) {
                        return this.$message.error("添加失败")
                    }
                    this.$message.success("添加成功");
                    this.addDialogVisible = false;
                    this.tableLoad();
                });
            });
        },
        // 根据主键删除用户信息
        deleteUser(id) {
            this.request.delete("/user/" + id).then(res => {
                if (!res.data) {
                    return this.$message.error("删除失败");
                }
                this.$message.success("删除成功");
                this.tableLoad();
            });
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
        // 批量删除用户数据
        async delBatch() {
            if (this.multipleSelection[0] == null) {
                this.visible = false;
                return this.$message.warning("请选择需要删除的用户。");
            }
            const confirmResult = await this.$confirm("此操作将永久删除选中用户信息，是否继续？", "提示", {
                confirmButtonText: '删除',
                cancelButtonText: '取消',
                type: 'warning',
            }).catch(error => error);
            if (confirmResult != "confirm") {// 如果取消删除，则提示
                return this.$message.warning("取消删除。");
            }
            let ids = this.multipleSelection.map(v => v.id);
            this.request.post("/user/del/batch", ids).then(res => {

                if (!res.data) {
                    return this.$message.error("批量删除失败")
                }
                this.$message.success("批量删除成功");
                this.tableLoad();
            });
        },
        // 显示对编辑话框
        showEditDialog(id) {
            this.request.get("/user/" + id).then(res => {
                this.editForm = res.data;// 查询出用户信息，并反填回编辑表单中
                this.editDialogVisible = true;// 开启编辑窗口
            });
        },
        // 关闭编辑对话框
        editDialogClosed() {
            this.$refs.editFormRef.resetFields();// 重置信息
        },
        // 编辑用户信息方法
        editUserInfo() {
            this.$refs.editFormRef.validate(async valid => {
                //验证不成功结束方法
                if (!valid) return;
                this.request.post("/user", this.editForm).then(res => {
                    if (!res.data) {
                        return this.$message.error("修改失败");
                    }
                    this.$message.success("修改成功");
                    this.editDialogVisible = false;
                    this.tableLoad();
                });
            });
        },
        //导出数据
        exp() {
            window.open("http://localhost:9090/user/export");
        },
        //导出导入的数据
        exptemp() {
            window.open("http://localhost:9090/user/exporttemp");
        },
        // 成功上传文件
        handleExcelImportSuccess(res) {
            if (res.code == "600") {
                this.$message.error(res.msg);
                return;
            }
            this.$message.success("数据导入成功");
            this.tableLoad();
        },
    },
}
</script>

<style scoped>
.el-breadcrumb {
    margin-bottom: 15px;
    font-size: 17px;
}
</style>