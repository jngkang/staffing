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
                    添加图标
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
                <el-table-column label="ID" prop="iconId" sortable align="center" width="80%"/>
                <el-table-column label="图标" align="center" width="60%">
                    <template slot-scope="scope">
                        <span :class="scope.row.icon"></span>
                    </template>
                </el-table-column>
                <el-table-column label="图标名称" prop="name" sortable align="center" width="200%"/>
                <el-table-column label="图标el地址" prop="icon" sortable align="center"/>
                <el-table-column label="操作" width="200%" align="center">
                    <template slot-scope="scope">
                        <!-- 修改按钮 -->
                        <el-button type="primary" icon="el-icon-edit" size="mini"
                                   @click="showEditDialog(scope.row.iconId)"></el-button>
                        <!-- 删除按钮 -->
                        <el-popconfirm confirm-button-text="确定" cancel-button-text="我再想想"
                                       icon="el-icon-info" icon-color="red" title="您确认永久删除此图标的信息吗？"
                                       @confirm="deleteUser(scope.row.iconId)">
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
            <!-- 新增图标区域 -->
            <el-dialog title="添加图标" :visible.sync="addDialogVisible" width="35%" @close="addDialogClosed">
                <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="80px">
                    <el-form-item label="图标名称" prop="name">
                        <el-input v-model="addForm.name"></el-input>
                    </el-form-item>
                    <el-form-item label="图标值" prop="icon">
                        <el-input v-model="addForm.icon"></el-input>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                        <el-button @click="addDialogVisible = false">取 消</el-button>
                        <el-button type="primary" @click="addWindow">确 定</el-button>
                </span>
            </el-dialog>
            <!-- 修改图标信息区域 -->
            <el-dialog title="修改图标信息" :visible.sync="editDialogVisible" width="35%" @close="editDialogClosed">
                <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="80px">
                    <el-form-item label="ID" prop="name">
                        <el-input v-model="editForm.iconId" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="图标名称" prop="name">
                        <el-input v-model="editForm.name"></el-input>
                    </el-form-item>
                    <el-form-item label="图标值" prop="icon">
                        <el-input v-model="editForm.icon"></el-input>
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
    name: "IconList.vue",
    data() {
        return {
            // 查询信息实体
            queryInfo: {
                search: "",// 检索的信息
                pageNum: 1,// 当前页面
                pageSize: 10,// 一页的数据量
            },
            tableData: [],// 图标信息列表
            total: 0,// 图标信息的总记录数
            addDialogVisible: false,// 对话框状态
            // 添加表单信息
            addForm: {
                iconId: "",
                name: "",
                icon: "",
            },
            // 添加的表单验证
            addFormRules: {
                name: [
                    {required: true, message: "请输入图标名", trigger: "blur"},
                ],
                icon: [
                    {required: true, message: "请输入图标名", trigger: "blur"},
                ],
            },
            // 修改图标的信息
            editForm: {},
            // 显示和隐藏修改图标信息的弹窗
            editDialogVisible: false,
            // 修改的表单验证
            editFormRules: {
                name: [
                    {required: true, message: "请输入图标名", trigger: "blur"},
                ],
                icon: [
                    {required: true, message: "请输入图标名", trigger: "blur"},
                ],
            },
            props: {
                label: 'name'
            },
            multipleSelection: [],
        }
    },
    created() {
        this.tableLoad();
    },
    methods: {
        // 分页查询
        tableLoad() {
            // 刷新数据
            this.request.get("/icon/page", {params: this.queryInfo}).then(res => {
                // 图标列表数据封装
                this.tableData = res.data.records;
                // 总图标数量封装
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
        // 监听添加图标操作
        addDialogClosed() {
            this.$refs.addFormRef.resetFields();
        },
        // 添加图标方法
        addWindow() {
            this.$refs.addFormRef.validate(async valid => {
                //验证不成功结束方法
                if (!valid) return;
                this.request.post("/icon", this.addForm).then(res => {
                    if (!res.data) {
                        return this.$message.error("添加失败")
                    }
                    this.$message.success("添加成功");
                    this.addDialogVisible = false;
                    this.tableLoad();
                });
            });
        },
        // 根据主键删除图标信息
        deleteUser(iconId) {
            this.request.delete("/icon/" + iconId).then(res => {
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
        // 批量删除图标数据
        async delBatch() {
            if (this.multipleSelection[0] == null) {
                this.visible = false;
                return this.$message.warning("请选择需要删除的图标。");
            }
            const confirmResult = await this.$confirm("此操作将永久删除选中图标信息，是否继续？", "提示", {
                confirmButtonText: '删除',
                cancelButtonText: '取消',
                type: 'warning',
            }).catch(error => error);
            if (confirmResult != "confirm") {// 如果取消删除，则提示
                return this.$message.warning("取消删除。");
            }
            let ids = this.multipleSelection.map(v => v.iconId);
            this.request.post("/icon/del/batch", ids).then(res => {

                if (!res.data) {
                    return this.$message.error("批量删除失败")
                }
                this.$message.success("批量删除成功");
                this.tableLoad();
            });
        },
        // 显示对编辑话框
        showEditDialog(iconId) {
            this.request.get("/icon/" + iconId).then(res => {
                this.editForm = res.data;// 查询出图标信息，并反填回编辑表单中
                this.editDialogVisible = true;// 开启编辑窗口
            });
        },
        // 关闭编辑对话框
        editDialogClosed() {
            this.$refs.editFormRef.resetFields();// 重置信息
        },
        // 编辑图标信息方法
        editWindow() {
            this.$refs.editFormRef.validate(async valid => {
                //验证不成功结束方法
                if (!valid) return;
                this.request.post("/icon", this.editForm).then(res => {
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