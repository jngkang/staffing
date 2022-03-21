<template>
    <div>
        <el-card>
            <div>
                <!-- 搜索添加 -->
                <el-input placeholder="请输入搜索内容" v-model="queryInfo.search"
                          style="width: 35%"
                          clearable @clear="tableLoad">
                    <el-button slot="append" icon="el-icon-search" @click="tableLoad"></el-button>
                </el-input>
            </div>
            <div style="margin-top: 13px">
                <el-button type="primary" size="small" @click="addDialogVisible = true">
                    添加角色
                </el-button>
                <el-button type="danger" size="small" slot="reference" @click="delBatch">
                    批量删除
                </el-button>
            </div>
            <!-- 绘制表格 border边框，stripe隔行变色-->
            <el-table :data="tableData" border stripe
                      @selection-change="handleSelectionChange">
                <!-- 索引列 -->
                <el-table-column type="selection" width="50%" align="center"></el-table-column>
                <el-table-column label="ID" prop="roleId" sortable align="center" width="80%"/>
                <el-table-column label="角色名称" prop="name" sortable align="center" width="200%"/>
                <el-table-column label="描述" prop="description"/>
                <el-table-column label="操作" width="200%" align="center">
                    <template slot-scope="scope">
                        <!-- 修改按钮 -->
                        <el-button type="primary" icon="el-icon-edit" size="mini"
                                   @click="showEditDialog(scope.row.roleId)"></el-button>
                        <!-- 删除按钮 -->
                        <el-popconfirm confirm-button-text="确定" cancel-button-text="我再想想"
                                       icon="el-icon-info" icon-color="red" title="您确认永久删除此角色的信息吗？"
                                       @confirm="deleteUser(scope.row.roleId)">
                            <el-button type="danger" size="mini" slot="reference" icon="el-icon-delete"
                                       class="mr-5 ml-5"></el-button>
                        </el-popconfirm>
                        <!-- 文字提示。enterable 隐藏 -->
                        <el-tooltip effect="dark" content="分配权限" placement="top" :enterable="false">
                            <!-- 权限按钮 -->
                            <el-button type="dark" icon="el-icon-setting" size="mini"
                                       @click="showAuthorityDialog(scope.row.roleId, scope.row.name)"></el-button>
                        </el-tooltip>
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
            <!-- 新增角色区域 -->
            <el-dialog title="添加角色" :visible.sync="addDialogVisible" width="35%" @close="addDialogClosed">
                <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="80px">
                    <el-form-item label="角色名称" prop="name">
                        <el-input v-model="addForm.name"></el-input>
                    </el-form-item>
                    <el-form-item label="描述" prop="description">
                        <el-input v-model="addForm.description"></el-input>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                        <el-button @click="addDialogVisible = false">取 消</el-button>
                        <el-button type="primary" @click="addWindow">确 定</el-button>
                </span>
            </el-dialog>
            <!-- 修改角色信息区域 -->
            <el-dialog title="修改角色信息" :visible.sync="editDialogVisible" width="35%" @close="editDialogClosed">
                <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="80px">
                    <el-form-item label="角色ID" prop="roleId">
                        <el-input v-model="editForm.roleId" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="角色名称" prop="name">
                        <el-input v-model="editForm.name"></el-input>
                    </el-form-item>
                    <el-form-item label="描述" prop="description">
                        <el-input v-model="editForm.description"></el-input>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                        <el-button @click="editDialogVisible = false">取 消</el-button>
                        <el-button type="primary" @click="editWindow">确 定</el-button>
                </span>
            </el-dialog>
            <!-- 分配权限区域 -->
            <el-dialog title="权限分配" :visible.sync="authorityDialogVisible" width="35%">
                <el-tree
                    :data="AuthorityData"
                    show-checkbox
                    node-key="menuId"
                    ref="tree"
                    :props="props"
                    :default-expanded-keys="expands"
                    :default-checked-keys="checks">
                    <span class="custom-tree-node" slot-scope="{ node, data }">
                        <span><i :class="data.icon"></i> {{ data.name }}</span>
                    </span>
                </el-tree>
                <span slot="footer" class="dialog-footer">
                        <el-button @click="authorityDialogVisible = false">取 消</el-button>
                        <el-button type="primary" @click="authorityWindow">确 定</el-button>
                </span>
            </el-dialog>
        </el-card>
    </div>
</template>

<script>
export default {
    name: "RoleList.vue",
    data() {
        return {
            // 查询信息实体
            queryInfo: {
                search: "",// 检索的信息
                pageNum: 1,// 当前页面
                pageSize: 10,// 一页的数据量
            },
            tableData: [],// 角色信息列表
            total: 0,// 角色信息的总记录数
            addDialogVisible: false,// 对话框状态
            // 添加表单信息
            addForm: {
                roleId: "",
                name: "",
                description: "",
            },
            // 添加的表单验证
            addFormRules: {
                name: [
                    {required: true, message: "请输入角色名", trigger: "blur"},
                ],
            },
            // 修改角色的信息
            editForm: {},
            // 显示和隐藏修改角色信息的弹窗
            editDialogVisible: false,
            // 修改的表单验证
            editFormRules: {
                name: [
                    {required: true, message: "请输入密码", trigger: "blur"},
                ],
            },
            authorityDialogVisible: false,
            AuthorityData: [],
            props: {
                label: 'name'
            },
            multipleSelection: [],
            // 分配权限菜单默认选中的id
            expands: [],
            // 分配权限菜单默认选中的id
            checks: [],
            roleId: 0,
            roleName: "",
        }
    },
    created() {
        this.tableLoad();
    },
    methods: {
        // 分页查询
        tableLoad() {
            // 刷新数据
            this.request.get("/role/page", {params: this.queryInfo}).then(res => {
                // 角色列表数据封装
                this.tableData = res.data.records;
                // 总角色数量封装
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
        // 监听添加角色操作
        addDialogClosed() {
            this.$refs.addFormRef.resetFields();
        },
        // 添加角色方法
        addWindow() {
            this.$refs.addFormRef.validate(async valid => {
                //验证不成功结束方法
                if (!valid) return;
                this.request.post("/role", this.addForm).then(res => {
                    if (!res.data) {
                        return this.$message.error("添加失败")
                    }
                    this.$message.success("添加成功");
                    this.addDialogVisible = false;
                    this.tableLoad();
                });
            });
        },
        // 根据主键删除角色信息
        deleteUser(roleId) {
            this.request.delete("/role/" + roleId).then(res => {
                if (res.code == "600") {
                    return this.$message.error(res.msg);
                }
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
        // 批量删除角色数据
        async delBatch() {
            if (this.multipleSelection[0] == null) {
                this.visible = false;
                return this.$message.warning("请选择需要删除的角色。");
            }
            const confirmResult = await this.$confirm("此操作将永久删除选中角色信息，是否继续？", "提示", {
                confirmButtonText: '删除',
                cancelButtonText: '取消',
                type: 'warning',
            }).catch(error => error);
            if (confirmResult != "confirm") {// 如果取消删除，则提示
                return this.$message.warning("取消删除。");
            }
            let ids = this.multipleSelection.map(v => v.roleId);
            this.request.post("/role/del/batch", ids).then(res => {
                if (res.code == "600") {
                    return this.$message.error(res.msg);
                }
                if (!res.data) {
                    return this.$message.error("批量删除失败")
                }
                this.$message.success("批量删除成功");
                this.tableLoad();
            });
        },
        // 显示对编辑话框
        showEditDialog(roleId) {
            this.request.get("/role/" + roleId).then(res => {
                this.editForm = res.data;// 查询出角色信息，并反填回编辑表单中
                this.editDialogVisible = true;// 开启编辑窗口
            });
        },
        // 关闭编辑对话框
        editDialogClosed() {
            this.$refs.editFormRef.resetFields();// 重置信息
        },
        // 编辑角色信息方法
        editWindow() {
            this.$refs.editFormRef.validate(async valid => {
                //验证不成功结束方法
                if (!valid) return;
                this.request.post("/role", this.editForm).then(res => {
                    if (!res.data) {
                        return this.$message.error("修改失败")
                    }
                    this.$message.success("修改成功");
                    this.editDialogVisible = false;
                    this.tableLoad();
                });
            });
        },
        // 显示分配权限对话框
        showAuthorityDialog(roleId, name) {
            this.roleId = roleId;
            this.roleName = name;
            // 刷新权限分配页面
            // 请求菜单数据
            this.request.get("/menu", {params: {search: ""}}).then(res => {
                this.AuthorityData = res.data;
                // 将菜单数据处理为id数组，并赋值给expands，让其默认展开所有选项
                this.expands = this.AuthorityData.map(v => v.menuId);
            });
            // 请求角色权限数据
            this.request.get("/role-menu/" + this.roleId).then(res => {
                this.checks = res.data;
                // 获取所有菜单id，判断子菜单是否全部被选中，若没有则将父级菜单设置为不选中
                this.request.get("/menu/ids").then(r => {
                    const ids = r.data;
                    ids.forEach(id => {
                        if (!this.checks.includes(id)) {
                            this.$nextTick(() => {
                                this.$refs.tree.setChecked(id, false);
                            });
                        }
                    })
                });
            });
            this.authorityDialogVisible = true;
        },
        // 编辑角色权限信息方法
        authorityWindow() {
            console.log(this.$refs.tree.getCheckedKeys());
            this.request.post("/role-menu/" + this.roleId, this.$refs.tree.getCheckedKeys()).then(res => {
                if (res.code != 200) {
                    return this.$message.error("分配失败")
                }
                this.authorityDialogVisible = false;
                let temp = "分配成功";

                // 当用户修改管理员权限时，会推出并跳转至登录页面
                if (this.roleName == ("超级管理员")) {
                    this.$store.commit("logout");
                    temp = "分配成功，请重新登录。";
                }
                this.$message.success(temp);
            });
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