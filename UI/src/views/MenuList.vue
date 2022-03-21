<template>
    <div>
        <el-card>
            <div>
                <el-button type="primary" size="small" @click="showAddDialog">
                    添加菜单
                </el-button>
                <el-button type="danger" size="small" slot="reference" @click="delBatch">
                    批量删除
                </el-button>
            </div>
            <!-- 绘制表格 border边框，stripe隔行变色-->
            <el-table :data="tableData" border stripe
                      row-key="menuId" default-expand-all
                      @selection-change="handleSelectionChange">
                <!-- 索引列 -->
                <el-table-column type="selection" width="50%" align="center"></el-table-column>
                <el-table-column label="ID" prop="menuId" sortable align="center" width="80%"/>
                <el-table-column label="图标" align="center" width="60%">
                    <template slot-scope="scope">
                        <span :class="scope.row.icon"></span>
                    </template>
                </el-table-column>
                <el-table-column label="菜单名称" prop="name" sortable align="center"/>
                <el-table-column label="菜单路径" prop="path" sortable align="center"/>
                <el-table-column label="页面路径" prop="pagePath" sortable align="center"/>
                <el-table-column label="菜单顺序" prop="sortNum" sortable align="center"/>
                <el-table-column label="描述" prop="description"/>
                <el-table-column label="操作" width="200%" align="center">
                    <template slot-scope="scope">
                        <el-tooltip effect="dark" content="新增子菜单" placement="top" :enterable="false">
                            <!-- 新增子菜单 -->
                            <el-button type="dark" icon="el-icon-plus" size="mini"
                                       @click="showAddSubDialog(scope.row.menuId)" v-if="!scope.row.pid"></el-button>
                        </el-tooltip>
                        <!-- 修改按钮 -->
                        <el-button type="primary" icon="el-icon-edit" size="mini"
                                   @click="showEditDialog(scope.row.menuId)"></el-button>
                        <!-- 删除按钮 -->
                        <el-popconfirm confirm-button-text="确定" cancel-button-text="我再想想"
                                       icon="el-icon-info" icon-color="red" title="您确认永久删除此菜单的信息吗？"
                                       @confirm="deleteUser(scope.row)">
                            <el-button type="danger" size="mini" slot="reference" icon="el-icon-delete"
                                       class="mr-5 ml-5"></el-button>
                        </el-popconfirm>
                    </template>
                </el-table-column>
            </el-table>
            <!-- 新增子菜单区域 -->
            <el-dialog title="添加菜单" :visible.sync="addSubDialogVisible" width="35%" @close="addSubDialogClosed">
                <el-form :model="addSubForm" :rules="addSubFormRules" ref="addSubFormRef" label-width="80px">
                    <el-form-item label="菜单名称" prop="name">
                        <el-input v-model="addSubForm.name"></el-input>
                    </el-form-item>
                    <el-form-item label="菜单路径" prop="path">
                        <el-input v-model="addSubForm.path"></el-input>
                    </el-form-item>
                    <el-form-item label="页面路径" prop="pagePath">
                        <el-input v-model="addSubForm.pagePath"></el-input>
                    </el-form-item>
                    <el-form-item label="菜单图标" prop="icon">
                        <el-select clearable v-model="addSubForm.icon" placeholder="请选择" style="width: 100%">
                            <el-option v-for="item in options" :key="item.id" :label="item.name" :value="item.icon">
                                <i :class="item.icon"/> {{ item.name }}
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="菜单顺序" prop="sortNum">
                        <el-input v-model="addSubForm.sortNum"></el-input>
                    </el-form-item>
                    <el-form-item label="描述" prop="description">
                        <el-input v-model="addSubForm.description"></el-input>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                        <el-button @click="addSubDialogVisible = false">取 消</el-button>
                        <el-button type="primary" @click="addSubWindow">确 定</el-button>
                </span>
            </el-dialog>
            <!-- 新增菜单区域 -->
            <el-dialog title="添加菜单" :visible.sync="addDialogVisible" width="35%" @close="addDialogClosed">
                <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="80px">
                    <el-form-item label="菜单名称" prop="name">
                        <el-input v-model="addForm.name"></el-input>
                    </el-form-item>
                    <el-form-item label="菜单路径" prop="path">
                        <el-input v-model="addForm.path"></el-input>
                    </el-form-item>
                    <el-form-item label="页面路径" prop="pagePath">
                        <el-input v-model="addForm.pagePath"></el-input>
                    </el-form-item>
                    <el-form-item label="菜单图标" prop="icon">
                        <el-select clearable v-model="addForm.icon" placeholder="请选择" style="width: 100%">
                            <el-option v-for="item in options" :key="item.id" :label="item.name" :value="item.icon">
                                <i :class="item.icon"/> {{ item.name }}
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="菜单顺序" prop="sortNum">
                        <el-input v-model="addForm.sortNum"></el-input>
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
            <!-- 修改菜单信息区域 -->
            <el-dialog title="修改菜单信息" :visible.sync="editDialogVisible" width="35%" @close="editDialogClosed">
                <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="80px">
                    <el-form-item label="菜单名称" prop="name">
                        <el-input v-model="editForm.name"></el-input>
                    </el-form-item>
                    <el-form-item label="菜单路径" prop="path">
                        <el-input v-model="editForm.path"></el-input>
                    </el-form-item>
                    <el-form-item label="页面路径" prop="pagePath">
                        <el-input v-model="editForm.pagePath"></el-input>
                    </el-form-item>
                    <el-form-item label="菜单图标" prop="icon">
                        <el-select clearable v-model="editForm.icon" placeholder="请选择" style="width: 100%">
                            <el-option v-for="item in options" :key="item.id" :label="item.name" :value="item.icon">
                                <i :class="item.icon"/> {{ item.name }}
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="菜单顺序" prop="sortNum">
                        <el-input v-model="editForm.sortNum"></el-input>
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
        </el-card>
    </div>
</template>

<script>
export default {
    name: "MenuList.vue",
    data() {
        return {
            tableData: [],// 菜单信息列表
            // 新增子菜单窗口
            addSubDialogVisible: false,
            // 添加表单信息
            addSubForm: {
                name: "",
                path: "",
                icon: "",
                description: "",
                pid: "",
                pagePath: "",
                sortNum: "",
            },
            // 添加的表单验证
            addSubFormRules: {
                name: [
                    {required: true, message: '请输入菜单名', trigger: 'blur'},
                ],
                icon: [
                    {required: true, message: '请选择菜单图标', trigger: 'blur'},
                ],
                sortNum: [
                    {required: true, message: '请输入菜单顺序', trigger: 'blur'},
                    {pattern: /^[0-9]*$/, message: '菜单顺序必须为数字值', trigger: 'blur'},
                ],
            },
            addDialogVisible: false,// 对话框状态
            // 添加表单信息
            addForm: {
                name: "",
                path: "",
                icon: "",
                description: "",
                pagePath: "",
                sortNum: "",
            },
            // 添加的表单验证
            addFormRules: {
                name: [
                    {required: true, message: '请输入菜单名', trigger: 'blur'},
                ],
                icon: [
                    {required: true, message: '请选择菜单图标', trigger: 'blur'},
                ],
                sortNum: [
                    {required: true, message: '请输入菜单顺序', trigger: 'blur'},
                    {pattern: /^[0-9]*$/, message: '菜单顺序必须为数字值', trigger: 'blur'},
                ],
            },
            // 修改菜单的信息
            editForm: {},
            // 显示和隐藏修改菜单信息的弹窗
            editDialogVisible: false,
            // 修改的表单验证
            editFormRules: {
                name: [
                    {required: true, message: '请输入密码', trigger: 'blur'},
                ],
                icon: [
                    {required: true, message: '请选择菜单图标', trigger: 'blur'},
                ],
                sortNum: [
                    {required: true, message: '请输入菜单顺序', trigger: 'blur'},
                    {pattern: /^[0-9]*$/, message: '菜单顺序必须为数字值', trigger: 'blur'},
                ],
            },
            multipleSelection: [],
            options: [],
        }
    },
    created() {
        this.tableLoad();
    },
    methods: {
        // 分页查询
        tableLoad() {
            this.request.get("/menu").then(res => {
                // 菜单列表数据封装
                this.tableData = res.data;
            });
        },
        iconLoad() {
            // 请求图标的数据
            this.request.get("/icon").then(res => {
                this.options = res.data;
            });
        },
        // 监听添加子菜单操作
        addSubDialogClosed() {
            this.$refs.addSubFormRef.resetFields();
        },
        // 显示添加子菜单对话框
        showAddSubDialog(menuId) {
            this.addSubForm.pid = menuId;
            this.addSubDialogVisible = true;// 开启编辑窗口
            this.iconLoad();
        },
        // 添加子菜单方法
        addSubWindow() {
            this.$refs.addSubFormRef.validate(async valid => {
                //验证不成功结束方法
                if (!valid) return;
                this.request.post("/menu", this.addSubForm).then(res => {
                    if (!res.data) {
                        return this.$message.error("添加失败")
                    }
                    this.$message.success("添加成功");
                    this.addSubDialogVisible = false;
                    this.tableLoad();
                });
            });
        },
        showAddDialog() {
            this.addDialogVisible = true;
            this.iconLoad();
        },
        // 监听添加菜单操作
        addDialogClosed() {
            this.$refs.addFormRef.resetFields();
        },
        // 添加菜单方法
        addWindow() {
            debugger
            this.$refs.addFormRef.validate(async valid => {
                //验证不成功结束方法
                if (!valid) return;
                this.request.post("/menu", this.addForm).then(res => {
                    if (!res.data) {
                        return this.$message.error("添加失败")
                    }
                    this.$message.success("添加成功");
                    this.addDialogVisible = false;
                    this.tableLoad();
                });
            });
        },
        // 根据主键删除菜单信息
        deleteUser(item) {
            let ids = [];
            ids.push(item.menuId);

            // 需要判断删除列是否为父级菜单，若为父级菜单，则将子级菜单的id添加ids中，一并删除
            let tableData = this.tableData;
            // 若item为父级菜单，将所有子菜单的id加入ids

            if (item.children != null) {
                for (let i = 0; i < item.children.length; i++) {
                    ids.push(item.children[i].menuId);
                }
            }
            this.delBatch(ids)
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
        // 批量删除菜单数据
        async delBatch(ids) {
            // 先判断ids数组是否存在，若存在，则认为是单列的删除，不获取选中的列id，若不存在，则认为是批量删除，并获取选中列的id
            // 无论是删除一行信息，还是删除多条信息，均需要判断父级菜单删除时，获取子级菜单id并删除
            if (ids.length == undefined) {
                if (this.multipleSelection[0] == null) {
                    this.visible = false;
                    return this.$message.warning("请选择需要删除的菜单。");
                }
                const confirmResult = await this.$confirm("此操作将永久删除选中菜单信息，是否继续？", "提示", {
                    confirmButtonText: '删除',
                    cancelButtonText: '取消',
                    type: 'warning',
                }).catch(error => error);
                if (confirmResult != "confirm") {// 如果取消删除，则提示
                    return this.$message.warning("取消删除。");
                }

                ids = this.multipleSelection.map(v => v.menuId);
                // 需要判断ids中是否存在父级菜单，若存在则将其所有子菜单id也添加至ids中一并删除
                let tableData = this.tableData;
                // 找出id所对应的对象
                for (let n = 0; n < ids.length; n++) {
                    for (let i = 0; i < tableData.length; i++) {
                        if (ids[n] == tableData[i].id) {
                            // 若id为父级菜单，将所有子菜单的id加入ids
                            for (let j = 0; j < tableData[i].children.length; j++) {
                                ids.push(tableData[i].children[j].menuId);
                            }
                            break;
                        }
                    }
                }
            }
            this.request.post("/menu/del/batch", ids).then(res => {

                if (!res.data) {
                    return this.$message.error("删除失败")
                }
                this.$message.success("删除成功");
                this.tableLoad();
            });
        },
        // 显示对编辑话框
        showEditDialog(menuId) {
            this.iconLoad();
            this.request.get("/menu/" + menuId).then(res => {
                this.editForm = res.data;// 查询出菜单信息，并反填回编辑表单中
                this.editDialogVisible = true;// 开启编辑窗口
            });
        },
        // 关闭编辑对话框
        editDialogClosed() {
            this.$refs.editFormRef.resetFields();// 重置信息
        },
        // 编辑菜单信息方法
        editWindow() {
            this.$refs.editFormRef.validate(async valid => {
                //验证不成功结束方法
                if (!valid) return;
                this.request.post("/menu", this.editForm).then(res => {
                    if (!res.data) {
                        return this.$message.error("修改失败")
                    }
                    this.$message.success("修改成功");
                    this.editDialogVisible = false;
                    this.tableLoad();
                });
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