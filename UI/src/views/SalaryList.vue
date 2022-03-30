<template>
    <div>
        <el-card>
            <div style="display: flex; ">
                <!-- 搜索添加 -->
                <el-input placeholder="员工姓名" v-model="queryInfo.search"
                          style="width: 35%"
                          clearable @clear="tableLoad">
                    <el-button slot="append" icon="el-icon-search" @click="tableLoad"></el-button>
                </el-input>
                <el-select v-model="queryInfo.payDate" placeholder="请选择发资日期"
                           style="width: 12%;" class="ml-10"
                           @change="this.tableLoad">
                    <el-option
                        v-for="item in payDateOptions"
                        :key="item.value"
                        :label="item.value"
                        :value="item.value"
                        style="text-align: center;">
                    </el-option>
                </el-select>
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
                    录入工资信息
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
                        <el-upload action="http://localhost:9090/salary/import" :show-file-list="false" accept=".xlsx"
                                   :on-success="handleExcelImportSuccess"
                                   style="display: inline-block">
                            <el-dropdown-item style="width: 4em">批量导入</el-dropdown-item>
                        </el-upload>
                    </el-dropdown-menu>
                </el-dropdown>
            </div>
            <!-- 绘制表格 border边框，stripe隔行变色-->
            <el-table :data="tableData" border stripe
                      @selection-change="handleSelectionChange">
                <!-- 索引列 -->
                <el-table-column type="selection" width="50" align="center"/>
                <el-table-column type="index" width="50" fixed align="center"/>
                <el-table-column label="发资日期" prop="payDate" width="100" sortable fixed align="center"/>
                <el-table-column label="员工编号" prop="empno" width="100" sortable fixed align="center"/>
                <el-table-column label="员工姓名" prop="empName" width="100" sortable fixed align="center"/>
                <el-table-column label="员工角色" prop="roleName" width="100" sortable align="center"/>
                <el-table-column label="部门" prop="deptName" width="100" sortable align="center"/>
                <el-table-column label="岗位" prop="postName" width="100" sortable align="center"/>
                <el-table-column label="基本工资" prop="base" width="100" sortable align="center"/>
                <el-table-column label="绩效工资" prop="performance" width="100" sortable align="center"/>
                <el-table-column label="奖金" prop="bonus" width="100" sortable align="center"/>
                <el-table-column label="补助" prop="subsidy" width="100" sortable align="center"/>
                <el-table-column label="保险" prop="insurance" width="100" sortable align="center"/>
                <el-table-column label="罚款" prop="penalty" width="100" sortable align="center"/>
                <el-table-column label="缺勤" prop="absenteeism" width="100" sortable align="center"/>
                <el-table-column label="实发工资" prop="fsalary" width="120" sortable fixed="right" align="center"/>
                <el-table-column label="录入时间" prop="inputTime" :formatter="formatDateTime" width="150" sortable align="center"/>
                <el-table-column label="备注" prop="remark" width="300"/>
                <el-table-column label="操作" width="120" fixed="right" align="center">
                    <template slot-scope="scope">
                        <!-- 修改按钮 -->
                        <el-button type="primary" icon="el-icon-edit" size="mini"
                                   @click="showEditDialog(scope.row.id)"></el-button>
                        <!-- 删除按钮 -->
                        <el-popconfirm confirm-button-text="确定" cancel-button-text="我再想想"
                                       icon="el-icon-info" icon-color="red" title="您确认永久删除此工资的信息吗？"
                                       @confirm="deleteInfo(scope.row.id)">
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
            <!-- 新增工资区域 -->
            <el-dialog title="添加工资" :visible.sync="addDialogVisible" width="35%" @close="addDialogClosed">
                <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="80px">
                    <el-row>
                        <el-col :span="12">
                            <el-form-item label="员工" prop="empno">
                                <el-select v-model="addForm.empno" placeholder="请选员工信息"
                                           @change="addSelectClick"
                                           style="width: 100%;">
                                    <el-option
                                        v-for="item in employeeOptions"
                                        :key="item.empno"
                                        :label="item.empno + '-' + item.empName"
                                        :value="item.empno">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="角色名称" prop="roleName">
                                <el-input v-model="addForm.roleName" disabled></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <el-row>
                        <el-col :span="12">
                            <el-form-item label="部门名称" prop="deptName">
                                <el-input v-model="addForm.deptName" disabled></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="岗位名称" prop="postName">
                                <el-input v-model="addForm.postName" disabled></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <el-row>
                        <el-col :span="12">
                            <el-form-item label="发资日期" prop="payDate">
                                <el-date-picker v-model="addForm.payDate" type="month" placeholder="选择年月"
                                                style="width: 100%"/>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="基本工资" prop="base">
                                <el-input v-model="addForm.base" suffix-icon="iconfont icon-money-rmb"/>
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <el-row>
                        <el-col :span="12">
                            <el-form-item label="绩效工资" prop="performance">
                                <el-input v-model="addForm.performance" suffix-icon="iconfont icon-money-rmb"/>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="奖金" prop="bonus">
                                <el-input v-model="addForm.bonus" suffix-icon="iconfont icon-money-rmb"/>
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <el-row>
                        <el-col :span="12">
                            <el-form-item label="补助" prop="subsidy">
                                <el-input v-model="addForm.subsidy" suffix-icon="iconfont icon-money-rmb"/>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="保险" prop="insurance">
                                <el-input v-model="addForm.insurance" suffix-icon="iconfont icon-money-rmb"/>
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <el-row>
                        <el-col :span="12">
                            <el-form-item label="罚款" prop="penalty">
                                <el-input v-model="addForm.penalty" suffix-icon="iconfont icon-money-rmb"/>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="缺勤" prop="absenteeism">
                                <el-input v-model="addForm.absenteeism" suffix-icon="iconfont icon-money-rmb"/>
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <el-form-item label="备注" prop="remark">
                        <el-input v-model="addForm.remark"></el-input>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                        <el-button @click="addDialogVisible = false">取 消</el-button>
                        <el-button type="primary" @click="addInfo">确 定</el-button>
                </span>
            </el-dialog>
            <!-- 修改工资信息区域 -->
            <el-dialog title="修改工资信息" :visible.sync="editDialogVisible" width="35%" @close="editDialogClosed">
                <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="80px">
                    <el-row>
                        <el-col :span="12">
                            <el-form-item label="员工" prop="empno">
                                <el-select v-model="editForm.empno" placeholder="请选员工信息"
                                           style="width: 100%;" disabled>
                                    <el-option
                                        v-for="item in employeeOptions"
                                        :key="item.empno"
                                        :label="item.empno + '-' + item.empName"
                                        :value="item.empno">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="角色名称" prop="roleName">
                                <el-input v-model="editForm.roleName" disabled></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <el-row>
                        <el-col :span="12">
                            <el-form-item label="部门名称" prop="deptName">
                                <el-input v-model="editForm.deptName" disabled></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="岗位名称" prop="postName">
                                <el-input v-model="editForm.postName" disabled></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <el-row>
                        <el-col :span="12">
                            <el-form-item label="发资日期" prop="payDate">
                                <el-date-picker v-model="editForm.payDate" type="month" placeholder="选择年月"
                                                style="width: 100%"/>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="基本工资" prop="base">
                                <el-input v-model="editForm.base" suffix-icon="iconfont icon-money-rmb"/>
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <el-row>
                        <el-col :span="12">
                            <el-form-item label="绩效工资" prop="performance">
                                <el-input v-model="editForm.performance" suffix-icon="iconfont icon-money-rmb"/>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="奖金" prop="bonus">
                                <el-input v-model="editForm.bonus" suffix-icon="iconfont icon-money-rmb"/>
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <el-row>
                        <el-col :span="12">
                            <el-form-item label="补助" prop="subsidy">
                                <el-input v-model="editForm.subsidy" suffix-icon="iconfont icon-money-rmb"/>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="保险" prop="insurance">
                                <el-input v-model="editForm.insurance" suffix-icon="iconfont icon-money-rmb"/>
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <el-row>
                        <el-col :span="12">
                            <el-form-item label="罚款" prop="penalty">
                                <el-input v-model="editForm.penalty" suffix-icon="iconfont icon-money-rmb"/>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="缺勤" prop="absenteeism">
                                <el-input v-model="editForm.absenteeism" suffix-icon="iconfont icon-money-rmb"/>
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <el-form-item label="备注" prop="remark">
                        <el-input v-model="editForm.remark"></el-input>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                        <el-button @click="editDialogVisible = false">取 消</el-button>
                        <el-button type="primary" @click="editInfo">确 定</el-button>
                </span>
            </el-dialog>
        </el-card>
    </div>
</template>

<script>
export default {
    name: "SalaryList.vue",
    data() {
        return {
            // 查询信息实体
            queryInfo: {
                payDate: "",
                roleId: "",
                deptno: "",
                postno: "",
                search: "",// 检索的信息
                pageNum: 1,// 当前页面
                pageSize: 10,// 一页的数据量
            },
            tableData: [],// 工资信息列表
            total: 0,// 工资信息的总记录数
            addDialogVisible: false,// 对话框状态
            // 添加表单信息
            addForm: {
                empno: "",
                payDate: "",
                roleName: "",
                deptName: "",
                postName: "",
                base: "",
                performance: "",
                bonus: "",
                subsidy: "",
                insurance: "",
                penalty: "",
                absenteeism: "",
                remark: "",
            },
            // 添加的表单验证
            addFormRules: {
                empno: [
                    {required: true, message: '请选择员工信息', trigger: 'blur'},
                ],
                payDate: [
                    {required: true, message: '请选择发工资日期', trigger: 'blur'},
                ],
                base: [
                    {required: true, message: '请输入基本工资', trigger: 'blur'},
                    {pattern: /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/, message: '请输入正确的金额', trigger: 'blur'},
                ],
                performance: [
                    {required: true, message: '请输入绩效工资金额', trigger: 'blur'},
                    {pattern: /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/, message: '请输入正确的金额', trigger: 'blur'},
                ],
                bonus: [
                    {required: true, message: '请输入奖金金额', trigger: 'blur'},
                    {pattern: /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/, message: '请输入正确的金额', trigger: 'blur'},
                ],
                subsidy: [
                    {required: true, message: '请输入补助金额', trigger: 'blur'},
                    {pattern: /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/, message: '请输入正确的金额', trigger: 'blur'},
                ],
                insurance: [
                    {required: true, message: '请输入保险金额', trigger: 'blur'},
                    {pattern: /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/, message: '请输入正确的金额', trigger: 'blur'},
                ],
                penalty: [
                    {required: true, message: '请输入罚款金额', trigger: 'blur'},
                    {pattern: /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/, message: '请输入正确的金额', trigger: 'blur'},
                ],
                absenteeism: [
                    {required: true, message: '请输入缺勤金额', trigger: 'blur'},
                    {pattern: /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/, message: '请输入正确的金额', trigger: 'blur'},
                ],
            },
            // 修改工资的信息
            editForm: {},
            // 显示和隐藏修改工资信息的弹窗
            editDialogVisible: false,
            // 修改的表单验证
            editFormRules: {
                payDate: [
                    {required: true, message: '请选择发工资日期', trigger: 'blur'},
                ],
                base: [
                    {required: true, message: '请输入基本工资', trigger: 'blur'},
                    {pattern: /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/, message: '请输入正确的金额', trigger: 'blur'},
                ],
                performance: [
                    {required: true, message: '请输入绩效工资金额', trigger: 'blur'},
                    {pattern: /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/, message: '请输入正确的金额', trigger: 'blur'},
                ],
                bonus: [
                    {required: true, message: '请输入奖金金额', trigger: 'blur'},
                    {pattern: /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/, message: '请输入正确的金额', trigger: 'blur'},
                ],
                subsidy: [
                    {required: true, message: '请输入补助金额', trigger: 'blur'},
                    {pattern: /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/, message: '请输入正确的金额', trigger: 'blur'},
                ],
                insurance: [
                    {required: true, message: '请输入保险金额', trigger: 'blur'},
                    {pattern: /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/, message: '请输入正确的金额', trigger: 'blur'},
                ],
                penalty: [
                    {required: true, message: '请输入罚款金额', trigger: 'blur'},
                    {pattern: /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/, message: '请输入正确的金额', trigger: 'blur'},
                ],
                absenteeism: [
                    {required: true, message: '请输入缺勤金额', trigger: 'blur'},
                    {pattern: /^(([1-9]{1}\d*)|(0{1}))(\.\d{1,2})?$/, message: '请输入正确的金额', trigger: 'blur'},
                ],
            },
            multipleSelection: [],
            // 查询出的部门信息
            roleOptions: [],
            deptOptions: [],
            tempPostOptions: [],
            postOptions: [],
            payDateOptions: [],
            employeeOptions: [],
            form: {},
        }
    },
    // 查询角色信息、部门信息、岗位信息
    mounted() {
        this.request.get("/salary/paydate").then(res => {
            res.data.forEach(r => {
                let objTemp = {};
                this.$set(objTemp, "value", r);
                this.payDateOptions.push(objTemp);
            });
        });
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
    },
    created() {
        // 从数据库中查询到指定的数据，并填充到tableData中
        this.tableLoad();
    },
    methods: {
        tableLoad() {
            // 分页查询
            this.request.get("/salary/page", {params: this.queryInfo}).then(res => {
                // 工资列表数据封装
                this.tableData = res.data;
            });
            this.request.get("/salary/count", {params: this.queryInfo}).then(res => {
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
        selectChange() {
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
            this.queryInfo.payDate = "";
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
        showAddDialog() {
            this.request.get("/employee/allnameinfo/" + "").then(res => {
                this.employeeOptions = res.data;
            });
            this.addDialogVisible = true;
        },
        addSelectClick(empno) {
            this.employeeOptions.forEach(r => {
                if (empno == r.empno) {
                    this.addForm.roleName = r.roleName;
                    this.addForm.deptName = r.deptName;
                    this.addForm.postName = r.postName;
                }
            });
        },
        // 监听添加工资操作
        addDialogClosed() {
            this.$refs.addFormRef.resetFields();
        },
        // 添加工资方法
        addInfo() {
            this.$refs.addFormRef.validate(async valid => {
                //验证不成功结束方法
                if (!valid) return;
                this.infoCopy(this.form, this.addForm)
                this.request.post("/salary/insert", this.form).then(res => {
                    if (res.code == "600") {
                        return this.$message.error(res.msg);
                    }
                    if (!res.data) {
                        return this.$message.error("添加失败")
                    }
                    this.$message.success("添加成功");
                    this.addDialogVisible = false;
                    this.tableLoad();
                    this.form = {};
                });
            });
        },
        // 将y中的数据拷贝到x中
        infoCopy(x, y) {
            this.$set(this.form, "id", "");
            this.$set(this.form, "inputTime", "");
            x.empno = y.empno;
            let month = y.payDate.getMonth() + 1;
            if (month < 10) {
                x.payDate = (y.payDate.getYear() + 1900) + "-0" + month;
            } else {
                x.payDate = (y.payDate.getYear() + 1900) + "-" + month;
            }
            x.base = y.base;
            x.performance = y.performance;
            x.bonus = y.bonus;
            x.subsidy = y.subsidy;
            x.insurance = y.insurance;
            x.penalty = y.penalty;
            x.absenteeism = y.absenteeism;
            x.remark = y.remark;
        },
        // 根据主键删除工资信息
        deleteInfo(id) {
            this.request.delete("/salary/" + id).then(res => {
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
        // 批量删除工资数据
        async delBatch() {
            if (this.multipleSelection[0] == null) {
                this.visible = false;
                return this.$message.warning("请选择需要删除的工资。");
            }
            const confirmResult = await this.$confirm("此操作将永久删除选中工资信息，是否继续？", "提示", {
                confirmButtonText: '删除',
                cancelButtonText: '取消',
                type: 'warning',
            }).catch(error => error);
            if (confirmResult != "confirm") {// 如果取消删除，则提示
                return this.$message.warning("取消删除。");
            }
            let ids = this.multipleSelection.map(v => v.id);
            this.request.post("/salary/del/batch", ids).then(res => {
                if (!res.data) {
                    return this.$message.error("批量删除失败")
                }
                this.$message.success("批量删除成功");
                this.tableLoad();
            });
        },
        // 显示对编辑话框
        showEditDialog(id) {
            this.request.get("/salary/" + id).then(res => {
                this.editForm = res.data;// 查询出工资信息，并反填回编辑表单中
                this.request.get("/employee/allnameinfo/" + res.data.empno).then(res => {
                    this.employeeOptions = res.data;
                    this.$set(this.editForm, "roleName", res.data[0].roleName);
                    this.$set(this.editForm, "deptName", res.data[0].deptName);
                    this.$set(this.editForm, "postName", res.data[0].postName);
                });
                this.editDialogVisible = true;// 开启编辑窗口
            });
        },
        // 关闭编辑对话框
        editDialogClosed() {
            this.$refs.editFormRef.resetFields();// 重置信息
        },
        // 编辑工资信息方法
        editInfo() {
            this.$refs.editFormRef.validate(async valid => {
                //验证不成功结束方法
                if (!valid) return;
                this.editForm.fsalary = Number(this.editForm.base) + Number(this.editForm.performance) + Number(this.editForm.bonus)
                    + Number(this.editForm.subsidy) + Number(this.editForm.insurance)
                    + Number(this.editForm.penalty) + Number(this.editForm.absenteeism);
                this.request.post("/salary", this.editForm).then(res => {
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
            window.open("http://localhost:9090/salary/export");
        },
        //导出导入的数据
        exptemp() {
            window.open("http://localhost:9090/salary/exporttemp");
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