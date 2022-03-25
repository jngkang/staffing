<template>
    <div>
        <el-card>
            <div style="display: flex; ">
                <!-- 搜索添加 -->
                <el-input placeholder="部门编号/部门名称/部门地址" v-model="queryInfo.search"
                          style="width: 35%"
                          clearable @clear="tableLoad">
                    <el-button slot="append" icon="el-icon-search" @click="tableLoad"></el-button>
                </el-input>
            </div>
            <div style="margin-top: 13px">
                <el-button type="primary" size="small" @click="showAddDialog">
                    添加部门
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
                        <el-upload action="http://localhost:9090/department/import" :show-file-list="false" accept=".xlsx"
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
                <el-table-column type="selection" width="50%" align="center"></el-table-column>
                <el-table-column label="部门编号" prop="deptno" width="200%" sortable align="center"/>
                <el-table-column label="部门名称" prop="name" width="250%" sortable align="center"/>
                <el-table-column label="部门地址" prop="location" sortable width="100px" align="center"/>
                <el-table-column label="描述" prop="description" align="center"/>
                <el-table-column label="操作" width="175px" align="center">
                    <template slot-scope="scope">
                        <!-- 修改按钮 -->
                        <el-button type="primary" icon="el-icon-edit" size="mini"
                                   @click="showEditDialog(scope.row.deptno)"></el-button>
                        <!-- 删除按钮 -->
                        <el-popconfirm confirm-button-text="确定" cancel-button-text="我再想想"
                                       icon="el-icon-info" icon-color="red" title="您确认永久删除此部门的信息吗？"
                                       @confirm="deleteUser(scope.row.deptno)">
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
            <!-- 新增部门区域 -->
            <el-dialog title="添加部门" :visible.sync="addDialogVisible" width="35%" @close="addDialogClosed">
                <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="80px">
                    <el-form-item label="部门编号" prop="deptno">
                        <el-input v-model="addForm.deptno"></el-input>
                    </el-form-item>
                    <el-form-item label="部门名称" prop="name">
                        <el-input v-model="addForm.name"></el-input>
                    </el-form-item>
                    <el-form-item label="地址" prop="location">
                        <el-input v-model="addForm.location"></el-input>
                    </el-form-item>
                    <el-form-item label="描述" prop="description">
                        <el-input v-model="addForm.description"></el-input>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                        <el-button @click="addDialogVisible = false">取 消</el-button>
                        <el-button type="primary" @click="addUser">确 定</el-button>
                </span>
            </el-dialog>
            <!-- 修改部门信息区域 -->
            <el-dialog title="修改部门信息" :visible.sync="editDialogVisible" width="35%" @close="editDialogClosed">
                <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="80px">
                    <el-form-item label="部门编号" prop="deptno">
                        <el-input v-model="editForm.deptno" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="部门名称" prop="name">
                        <el-input v-model="editForm.name"></el-input>
                    </el-form-item>
                    <el-form-item label="地址" prop="location">
                        <el-input v-model="editForm.location"></el-input>
                    </el-form-item>
                    <el-form-item label="描述" prop="description">
                        <el-input v-model="editForm.description"></el-input>
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
            tableData: [],// 部门信息列表
            total: 0,// 部门信息的总记录数
            addDialogVisible: false,// 对话框状态
            // 添加表单信息
            addForm: {
                deptno: "",
                name: "",
                location: "",
                description: "",
            },
            // 添加的表单验证
            addFormRules: {
                deptno: [
                    {required: true, message: '请输入部门编号', trigger: 'blur'},
                    {min: 3, max: 6, message: '长度在3到6个字符', trigger: 'blur'},
                ],
                name: [
                    {required: true, message: '请输入部门名称', trigger: 'blur'},
                    {min: 2, max: 20, message: '长度在2到20个字符', trigger: 'blur'},
                ],
                location: [
                    {required: true, message: '请输入部门地址', trigger: 'blur'},
                    {min: 2, max: 255, message: '长度在2到255个字符', trigger: 'blur'},
                ],
            },
            // 修改部门的信息
            editForm: {},
            // 显示和隐藏修改部门信息的弹窗
            editDialogVisible: false,
            // 修改的表单验证
            editFormRules: {
                deptno: [
                    {required: true, message: '请输入部门编号', trigger: 'blur'},
                    {min: 3, max: 6, message: '长度在3到6个字符', trigger: 'blur'},
                ],
                name: [
                    {required: true, message: '请输入部门名称', trigger: 'blur'},
                    {min: 2, max: 20, message: '长度在2到20个字符', trigger: 'blur'},
                ],
                location: [
                    {required: true, message: '请输入部门地址', trigger: 'blur'},
                    {min: 2, max: 255, message: '长度在2到255个字符', trigger: 'blur'},
                ],
            },
            multipleSelection: [],
            // 查询出的角色信息
            options: [],
        }
    },
    created() {
        this.tableLoad();
    },
    methods: {
        tableLoad() {
            // 分页查询
            this.request.get("department/page", {params: this.queryInfo}).then(res => {
                // 部门列表数据封装
                this.tableData = res.data.records;
                // 总部门数量封装
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
        showAddDialog() {
            this.addDialogVisible = true;
        },
        // 监听添加部门操作
        addDialogClosed() {
            this.$refs.addFormRef.resetFields();
        },
        // 添加部门方法
        addUser() {
            this.$refs.addFormRef.validate(async valid => {
                //验证不成功结束方法
                if (!valid) return;
                this.request.post("/department/insert", this.addForm).then(res => {
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
        // 根据主键删除部门信息
        deleteUser(deptno) {
            this.request.delete("/department/" + deptno).then(res => {
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
        // 批量删除部门数据
        async delBatch() {
            if (this.multipleSelection[0] == null) {
                this.visible = false;
                return this.$message.warning("请选择需要删除的部门。");
            }
            const confirmResult = await this.$confirm("此操作将永久删除选中部门信息，是否继续？", "提示", {
                confirmButtonText: '删除',
                cancelButtonText: '取消',
                type: 'warning',
            }).catch(error => error);
            if (confirmResult != "confirm") {// 如果取消删除，则提示
                return this.$message.warning("取消删除。");
            }
            let ids = this.multipleSelection.map(v => v.deptno);
            this.request.post("/department/del/batch", ids).then(res => {
                if (!res.data) {
                    return this.$message.error("批量删除失败")
                }
                this.$message.success("批量删除成功");
                this.tableLoad();
            });
        },
        // 显示对编辑话框
        showEditDialog(deptno) {
            this.request.get("/department/" + deptno).then(res => {
                this.editForm = res.data;// 查询出部门信息，并反填回编辑表单中
                this.editDialogVisible = true;// 开启编辑窗口
            });
        },
        // 关闭编辑对话框
        editDialogClosed() {
            this.$refs.editFormRef.resetFields();// 重置信息
        },
        // 编辑部门信息方法
        editUserInfo() {
            this.$refs.editFormRef.validate(async valid => {
                //验证不成功结束方法
                if (!valid) return;
                this.request.post("/department", this.editForm).then(res => {
                    if (res.code == "600") {
                        return this.$message.error(res.msg);
                    }
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
            window.open("http://localhost:9090/department/export");
        },
        //导出导入的数据
        exptemp() {
            window.open("http://localhost:9090/department/exporttemp");
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