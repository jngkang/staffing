<template>
    <div>
        <el-card>
            <div style="display: flex; justify-content: space-between;">
                <!-- 搜索添加 -->
                <el-input placeholder="请假原因" v-model="queryInfo.search"
                          style="width: 35%"
                          clearable @clear="tableLoad">
                    <el-button slot="append" icon="el-icon-search" @click="tableLoad"></el-button>
                </el-input>
                <el-button type="primary" size="small" style="width: 100px" @click="showAddDialog">
                    请假
                </el-button>
            </div>
            <!-- 绘制表格 border边框，stripe隔行变色-->
            <el-table :data="tableData" border stripe
                      @selection-change="handleSelectionChange">
                <!-- 索引列 -->
                <el-table-column type="selection" width="50" align="center"/>
                <el-table-column label="序号" type="index" width="50" fixed align="center"/>
                <el-table-column label="请假原因" prop="reason"/>
                <el-table-column label="请假时间" prop="id" :formatter="formatStartEndDateTime" width="250" align="center"/>
                <el-table-column label="申请时间" prop="inputDatetime" :formatter="formatDateTime" width="150" sortable align="center"/>
                <el-table-column label="审核人ID" prop="checkEmpno" width="100" sortable align="center"/>
                <el-table-column label="审核人" prop="checkEmpName" width="100" sortable align="center"/>
                <el-table-column label="审核日期" prop="checkDatetime" :formatter="formatDateTime" width="150" sortable align="center"/>
                <el-table-column label="状态" prop="state" :formatter="formatState" width="100" sortable fixed="right" align="center"/>
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
            <!-- 新增工资区域 -->
            <el-dialog title="添加工资" :visible.sync="addDialogVisible" width="35%" @close="addDialogClosed">
                <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="80px">
                    <el-form-item label="请假原因" prop="reason">
                        <el-input
                            type="textarea"
                            :rows="15"
                            resize="none"
                            placeholder="请输入内容"
                            v-model="addForm.reason">
                        </el-input>
                    </el-form-item>
                    <el-form-item label="请假时间" prop="leaveDateTime">
                        <el-date-picker
                            v-model="addForm.leaveDateTime"
                            style="width: 100%"
                            type="datetimerange"
                            format="yyyy-MM-dd HH:mm"
                            value-format="yyyy-MM-dd HH:mm"
                            range-separator="至"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期">
                        </el-date-picker>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                        <el-button @click="addDialogVisible = false">取 消</el-button>
                        <el-button type="primary" @click="addInfo">确 定</el-button>
                </span>
            </el-dialog>
        </el-card>
    </div>
</template>

<script>
export default {
    name: "SubVacation.vue",
    data() {
        return {
            // 查询信息实体
            queryInfo: {
                empno: "",
                roleId: "",
                deptno: "",
                postno: "",
                search: "",// 检索的信息
                pageNum: 1,// 当前页面
                pageSize: 10,// 一页的数据量
            },
            tableData: [],// 请假信息列表
            total: 0,// 请假信息的总记录数
            multipleSelection: [],
            employeeOptions: [],
            addForm: {
                empno: "",
                reason: "",
                leaveDateTime: [],
            },
            addDialogVisible: false,
            // 表单验证
            addFormRules: {
                empno: [
                    {required: true, message: "请选择员工信息", trigger: "blur"},
                ],
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
    // 查询角色信息、部门信息、岗位信息
    mounted() {
        this.request.get("/employee/allnameinfo/" + "").then(res => {
            this.employeeOptions = res.data;
        });
    },
    created() {
        // 从数据库中查询到指定的数据，并填充到tableData中
        this.user = window.localStorage.getItem("user") ? JSON.parse(window.localStorage.getItem("user")) : {};
        this.tableLoad();
    },
    methods: {
        tableLoad() {
            this.queryInfo.empno = this.user.empno;
            // 分页查询
            this.request.get("/vacation/page", {params: this.queryInfo}).then(res => {
                // 请假列表数据封装
                this.tableData = res.data;
            });
            this.request.get("/vacation/count", {params: this.queryInfo}).then(res => {
                this.total = res.data;
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
        // 针对录入日期时间的格式化显示
        formatDateTime(row, column) {
            // 获取单元格数据
            let date = row[column.property]
            if (date == null) {
                return null
            }
            return date[0] + '-' + date[1] + '-' + date[2] + ' ' + date[3] + ':' + date[4] + ':' + date[5];
        },
        // 针对请假时间的格式化输出
        formatStartEndDateTime(row, column) {
            let id = row[column.property];
            let date1;
            let date2;
            this.tableData.forEach(res => {
                if (res.id == id) {
                    date1 = res.startDatetime;
                    date2 = res.endDatetime;
                }
            });
            // return date1[0] + '-' + date1[1] + '-' + date1[2] + ' ' + date1[3] + ':' + date1[4] + ':' + date1[5] + "~"
            //     + date2[0] + '-' + date2[1] + '-' + date2[2] + ' ' + date2[3] + ':' + date2[4] + ':' + date2[5];
            return date1[0] + '-' + this.formatNumber(date1[1], 2) + '-' + this.formatNumber(date1[2], 2) + ' '
                + this.formatNumber(date1[3], 2) + ':' + this.formatNumber(date1[4], 2) + " ~ "
                + date2[0] + '-' + this.formatNumber(date2[1], 2) + '-' + this.formatNumber(date2[2], 2) + ' '
                + this.formatNumber(date2[3], 2) + ':' + this.formatNumber(date2[4], 2);
        },
        formatNumber(data, len) {
            let str = "" + data;
            while (str.length < len) {
                str = "0" + str;
            }
            return str;
        },
        formatState(row, column) {
            let state = row[column.property];
            if (state == 0) {
                return "待批准";
            } else if (state == 1) {
                return "已批准";
            } else if (state < 0) {
                return "拒绝";
            }
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
        showAddDialog() {
            this.addDialogVisible = true;
        },
        addDialogClosed() {
            this.$refs.addFormRef.resetFields();// 重置信息
            this.addDialogVisible = false;
        },
        addInfo() {
            let submitForm = {};
            this.$set(submitForm, "empno", this.user.empno);
            this.$set(submitForm, "reason", this.addForm.reason);
            this.$set(submitForm, "startDatetime", new Date(this.addForm.leaveDateTime[0]));
            this.$set(submitForm, "endDatetime", new Date(this.addForm.leaveDateTime[1]));
            this.$refs.addFormRef.validate((valid) => {
                if (valid) {
                    this.request.post("/vacation/insert", submitForm).then(res => {
                        if (res.data) {
                            this.$message.success("提交成功");
                            this.$refs.addFormRef.resetFields();// 重置信息
                            this.addDialogVisible = false;
                            this.tableLoad();
                        } else {
                            this.$message.error("提交失败");
                        }
                    });
                }
            });
        },
    },
}
</script>

<style>
</style>