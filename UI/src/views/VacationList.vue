<template>
    <div>
        <el-card>
            <div style="display: flex; ">
                <!-- 搜索添加 -->
                <el-input placeholder="员工编号/员工姓名" v-model="queryInfo.search"
                          style="width: 35%"
                          clearable @clear="tableLoad">
                    <el-button slot="append" icon="el-icon-search" @click="tableLoad"></el-button>
                </el-input>
                <el-select v-model="queryInfo.roleId" placeholder="请选择角色"
                           style="width: 10%;" class="ml-10"
                           @change="this.tableLoad">
                    <el-option
                        v-for="item in roleOptions"
                        :key="item.roleId"
                        :label="item.name"
                        :value="item.roleId"
                        style="text-align: center;">
                    </el-option>
                </el-select>
                <el-select v-model="queryInfo.deptno" placeholder="请选择部门"
                           style="width: 10%;" class="ml-10"
                           @click.native="deptSelectHomeClick"
                           @change="deptSelectHomeChange">
                    <el-option
                        v-for="item in deptOptions"
                        :key="item.deptno"
                        :label="item.name"
                        :value="item.deptno"
                        style="text-align: center;">
                    </el-option>
                </el-select>
                <el-select v-model="queryInfo.postno" placeholder="请选择岗位"
                           style="width: 10%;" class="ml-10"
                           @click.native="queryInfoCheckData"
                           @change="this.tableLoad">
                    <el-option
                        v-for="item in postOptions"
                        :key="item.postno"
                        :label="item.name"
                        :value="item.postno"
                        style="text-align: center;">
                    </el-option>
                </el-select>
                <el-button @click="reset" class="el-icon-refresh ml-10"></el-button>
            </div>
            <div style="margin-top: 13px">
                <el-button type="primary" size="small" @click="showAddDialog">
                    录入请假信息
                </el-button>
                <el-button type="danger" size="small" slot="reference" @click="delBatch">
                    批量删除
                </el-button>
            </div>
            <!-- 绘制表格 border边框，stripe隔行变色-->
            <el-table :data="tableData" border stripe
                      @selection-change="handleSelectionChange">
                <!-- 索引列 -->
                <el-table-column type="selection" width="50" align="center"/>
                <el-table-column label="序号" type="index" width="50" fixed align="center"/>
                <el-table-column label="员工编号" prop="empno" width="100" sortable fixed align="center"/>
                <el-table-column label="员工姓名" prop="empName" width="100" sortable fixed align="center"/>
                <el-table-column label="员工角色" prop="roleName" width="100" sortable align="center"/>
                <el-table-column label="部门" prop="deptName" width="100" sortable align="center"/>
                <el-table-column label="岗位" prop="postName" width="100" sortable align="center"/>
                <el-table-column label="请假原因" prop="reason" width="400"/>
                <el-table-column label="请假时间" prop="id" :formatter="formatStartEndDateTime" width="250" align="center"/>
                <el-table-column label="申请时间" prop="inputDatetime" :formatter="formatDateTime" width="150" sortable align="center"/>
                <el-table-column label="审核人ID" prop="checkEmpno" width="100" sortable align="center"/>
                <el-table-column label="审核人" prop="checkEmpName" width="100" sortable align="center"/>
                <el-table-column label="审核日期" prop="checkDatetime" :formatter="formatDateTime" width="150" sortable align="center"/>
                <el-table-column label="状态" prop="state" :formatter="formatState" width="100" sortable fixed="right" align="center"/>
                <el-table-column label="操作" width="150" fixed="right" align="center">
                    <template slot-scope="scope">
                        <!-- 修改按钮 -->
                        <el-button type="success" size="mini"
                                   @click="agreeVacation(scope.row)">批准
                        </el-button>
                        <el-button type="danger" size="mini"
                                   @click="refuseVacation(scope.row)">拒绝
                        </el-button>
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
            <!-- 新增工资区域 -->
            <el-dialog title="添加工资" :visible.sync="addDialogVisible" width="35%" @close="addDialogClosed">
                <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="80px">
                    <el-form-item label="员工" prop="empno">
                        <el-select v-model="addForm.empno" placeholder="请选员工信息"
                                   style="width: 100%;">
                            <el-option
                                v-for="item in employeeOptions"
                                :key="item.empno"
                                :label="item.empno + '-' + item.empName"
                                :value="item.empno">
                            </el-option>
                        </el-select>
                    </el-form-item>
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
    name: "VacationList.vue",
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
            // 查询出的部门信息
            roleOptions: [],
            deptOptions: [],
            tempPostOptions: [],
            postOptions: [],
            payDateOptions: [],
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
            form: {},
            user: {},
        }
    },
    // 查询角色信息、部门信息、岗位信息
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
        });
        this.user = window.localStorage.getItem("user") ? JSON.parse(window.localStorage.getItem("user")) : {};
    },
    created() {
        // 从数据库中查询到指定的数据，并填充到tableData中
        this.tableLoad();
    },
    methods: {
        tableLoad() {
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
        // 保证在选择部门信息时，岗位信息直接置为空
        deptSelectHomeClick() {
            this.queryInfo.postno = "";
        },
        // 主页页面的，在选择部门信息后，填充该部门的岗位信息
        deptSelectHomeChange(deptno) {
            this.postOptions = [];
            this.tempPostOptions.forEach(r => {
                if (r.deptno == deptno) {
                    this.postOptions.push(r);
                }
            });
            this.tableLoad();
        },
        // 在直接选择岗位信息时，检查是否选择部门信息
        queryInfoCheckData() {
            if (this.queryInfo.deptno == "") {
                return this.$message.warning("请选择部门信息");
            }
        },
        // 信息重置
        reset() {
            this.queryInfo.roleId = "";
            this.queryInfo.deptno = "";
            this.queryInfo.postno = "";
            this.queryInfo.search = "";
            this.queryInfo.pageNum = 1;
            this.queryInfo.pageSize = 10;
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
        // 批量删除请假数据
        async delBatch() {
            if (this.multipleSelection[0] == null) {
                this.visible = false;
                return this.$message.warning("请选择需要删除的请假。");
            }
            const confirmResult = await this.$confirm("此操作将永久删除选中请假信息，是否继续？", "提示", {
                confirmButtonText: '删除',
                cancelButtonText: '取消',
                type: 'warning',
            }).catch(error => error);
            if (confirmResult != "confirm") {// 如果取消删除，则提示
                return this.$message.warning("取消删除。");
            }
            let ids = this.multipleSelection.map(v => v.id);
            this.request.post("/vacation/del/batch", ids).then(res => {
                if (!res.data) {
                    return this.$message.error("批量删除失败")
                }
                this.$message.success("批量删除成功");
                this.tableLoad();
            });
        },
        agreeVacation(row) {
            if (row.state != 0) {
                this.$message.warning("本条请假信息已经批改。")
                return null;
            }
            row.state = 1
            row.checkEmpno = this.user.empno;
            this.request.post("/vacation", row).then(res => {
                if (!res.data) {
                    return this.$message.error("系统错误。")
                }
                this.$message.success("批准成功");
                this.tableLoad();
            });
        },
        refuseVacation(row) {
            if (row.state != 0) {
                this.$message.warning("本条请假信息已经批改。")
                return null;
            }
            row.state = -1
            row.checkEmpno = this.user.empno;
            this.request.post("/vacation", row).then(res => {
                if (!res.data) {
                    return this.$message.error("系统错误。")
                }
                this.$message.success("拒绝成功");
                this.tableLoad();
            });
        },
        showAddDialog() {
            this.request.get("/employee/allnameinfo/" + "").then(res => {
                this.employeeOptions = res.data;
            });
            this.addDialogVisible = true;
        },
        addDialogClosed() {
            this.$refs.addFormRef.resetFields();// 重置信息
            this.addDialogVisible = false;
        },
        addInfo() {
            let submitForm = {};
            this.$set(submitForm, "empno", this.addForm.empno);
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