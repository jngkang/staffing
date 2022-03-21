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
                <el-upload action="http://localhost:9090/files/upload" :show-file-list="false"
                           :on-success="handleFileUploadSuccess" :on-error="handleFileUploadError"
                           style="display: inline-block">
                    <el-button type="primary" size="small" class="mr-10">上传文件</el-button>
                </el-upload>

                <el-button type="danger" size="small" slot="reference" @click="delBatch">
                    批量删除
                </el-button>
            </div>
            <!-- 绘制表格 border边框，stripe隔行变色-->
            <el-table :data="tableData" border stripe
                      @selection-change="handleSelectionChange">
                <!-- 索引列 -->
                <el-table-column type="selection" align="center" width="50%"/>
                <el-table-column label="ID" prop="filesId" sortable align="center" width="80%"/>
                <el-table-column label="文件名称" prop="name" sortable align="center"/>
                <el-table-column label="文件类型" prop="type" sortable align="center" width="100%"/>
                <el-table-column label="文件大小(KB)" prop="size" sortable align="center" width="120%"/>
                <el-table-column label="操作" width="200%" align="center">
                    <template slot-scope="scope">
                        <!-- 下载按钮 -->
                        <el-button type="primary" icon="el-icon-download" size="mini"
                                   @click="downloadFiles(scope.row.url)"></el-button>
                        <!-- 删除按钮 -->
                        <el-popconfirm confirm-button-text="确定" cancel-button-text="我再想想"
                                       icon="el-icon-info" icon-color="red" title="您确定删除此信息吗？"
                                       @confirm="deleteUser(scope.row.filesId)">
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
                search: "",// 检索的信息
                pageNum: 1,// 当前页面
                pageSize: 10,// 一页的数据量
            },
            tableData: [],// 用户信息列表
            total: 0,// 用户信息的总记录数
            multipleSelection: [],
        }
    },
    created() {
        this.tableLoad();
    },
    methods: {
        // 分页查询
        tableLoad() {
            this.request.get("files/page", {params: this.queryInfo}).then(res => {
                // 用户列表数据封装
                this.tableData = res.records;
                // 总用户数量封装
                this.total = res.total;
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
        // 根据主键删除信息
        deleteUser(filesId) {
            let ids = [];
            ids.push(filesId);
            this.delBatch(ids);
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
        // 批量删除数据
        async delBatch(ids) {
            if (ids.length == undefined) {
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
                ids = this.multipleSelection.map(v => v.filesId);
            }
            this.request.post("/files/del/batch", ids).then(res => {
                if (!res.data) {
                    return this.$message.error("删除失败！！！")
                }
                this.$message.success("删除成功！！！");
                this.tableLoad();
            });
        },
        // 成功上传文件
        handleFileUploadSuccess() {
            this.$message.success("成功上传文件！！！");
            this.tableLoad();
        },
        handleFileUploadError() {
            this.$message.error("上传文件失败！！！");
        },
        downloadFiles(url) {
            window.open(url);
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