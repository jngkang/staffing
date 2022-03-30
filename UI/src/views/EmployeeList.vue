<template>
    <div>
        <el-card>
            <div style="display: flex; ">
                <!-- 搜索添加 -->
                <el-input placeholder="员工编号/员工姓名/手机号/证件号/地址" v-model="queryInfo.search"
                          style="width: 35%"
                          clearable @clear="tableLoad">
                    <el-button slot="append" icon="el-icon-search" @click="tableLoad"></el-button>
                </el-input>
                <!-- 性别单选框 -->
                <div class="ml-10">
                    <el-radio-group v-model="queryInfo.sex" @change="this.tableLoad">
                        <el-radio-button label="1">男</el-radio-button>
                        <el-radio-button label="0">女</el-radio-button>
                    </el-radio-group>
                </div>
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
                    添加员工
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
                        <el-upload action="http://localhost:9090/employee/import" :show-file-list="false" accept=".xlsx"
                                   :on-success="handleExcelImportSuccess"
                                   style="display: inline-block">
                            <el-dropdown-item style="width: 4em">导入</el-dropdown-item>
                        </el-upload>
                    </el-dropdown-menu>
                </el-dropdown>
            </div>
            <div style="width: 100%">
                <!-- 绘制表格 border边框，stripe隔行变色-->
                <el-table :data="tableData" border stripe @selection-change="handleSelectionChange">
                    <!-- 索引列 -->
                    <el-table-column type="selection" width="50" align="center" fixed></el-table-column>
                    <el-table-column label="员工姓名" prop="name" width="120" sortable fixed align="center"/>
                    <el-table-column label="员工编号" prop="empno" width="120" sortable align="center"/>
                    <el-table-column label="角色" prop="roleName" width="100" sortable align="center"/>
                    <el-table-column label="部门名称" prop="deptName" width="100" sortable align="center"/>
                    <el-table-column label="岗位名称" prop="postName" width="100" sortable align="center"/>
                    <el-table-column label="性别" prop="sexName" width="80" sortable align="center"/>
                    <el-table-column label="民族" prop="nation" width="80" sortable align="center"/>
                    <el-table-column label="籍贯" prop="province" width="100" sortable align="center"/>
                    <el-table-column label="政治面貌" prop="political" width="100" sortable align="center"/>
                    <el-table-column label="手机号" prop="phone" width="100" sortable align="center"/>
                    <el-table-column label="证件号" prop="identifyNo" width="150" sortable align="center"/>
                    <el-table-column label="现住址" prop="address" width="300"/>
                    <!--                <el-table-column label="账号状态" prop="state" width="100%" sortable align="center"/>-->
                    <!--                <el-table-column label="密码" prop="password" width="100%" align="center"/>-->
                    <!--                <el-table-column label="头像地址" prop="avatarUrl" width="100%"/>-->
                    <el-table-column label="状态" prop="state" width="70" align="center" sortable fixed="right">
                        <!-- 作用域插槽 -->
                        <template slot-scope="scope">
                            <!-- {{scope.row}}每一行封存的数据 -->
                            <el-switch v-model="scope.row.state" @change="stateChange(scope.row)"></el-switch>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" width="150" align="center" fixed="right">
                        <template slot-scope="scope">
                            <!-- 修改按钮 -->
                            <el-button type="primary" icon="el-icon-edit" size="mini"
                                       @click="showEditDialog(scope.row.empno)"></el-button>
                            <!-- 删除按钮 -->
                            <el-popconfirm confirm-button-text="确定" cancel-button-text="我再想想"
                                           icon="el-icon-info" icon-color="red" title="您确认永久删除此员工的信息吗？"
                                           @confirm="deleteUser(scope.row.empno)">
                                <el-button type="danger" size="mini" slot="reference" icon="el-icon-delete"
                                           class="mr-5 ml-5"></el-button>
                            </el-popconfirm>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
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
                    :page-sizes="[10, 20, 50, 200, 99999]"
                    :page-size="queryInfo.pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total">
                </el-pagination>
            </div>
            <!-- 新增员工区域 -->
            <el-dialog title="添加员工信息" :visible.sync="addDialogVisible" width="35%" @close="addDialogClosed">
                <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="100px">
                    <el-form-item label="员工编号" prop="empno">
                        <el-input v-model="addForm.empno"></el-input>
                    </el-form-item>
                    <el-form-item label="员工姓名" prop="name">
                        <el-input v-model="addForm.name"></el-input>
                    </el-form-item>
                    <el-form-item label="角色" prop="roleId">
                        <el-select v-model="addForm.roleId" placeholder="请选择角色" style="width: 100%">
                            <el-option
                                v-for="item in roleOptions"
                                :key="item.roleId"
                                :label="item.name"
                                :value="item.roleId">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="部门名称" prop="deptno">
                        <el-select v-model="addForm.deptno" placeholder="请选择部门"
                                   @click.native="deptSelectAddClick"
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
                    <el-form-item label="岗位名称" prop="postno">
                        <el-select v-model="addForm.postno" placeholder="请选择岗位"
                                   @click.native="addFormCheckData"
                                   style="width: 100%">
                            <el-option
                                v-for="item in postOptions"
                                :key="item.postno"
                                :label="item.name"
                                :value="item.postno">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="性别" prop="sex">
                        <el-radio-group v-model="addForm.sex">
                            <el-radio :label="1" style="margin-left: 30px">男</el-radio>
                            <el-radio :label="0">女</el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item label="民族" prop="nation">
                        <el-input v-model="addForm.nation"></el-input>
                    </el-form-item>
                    <el-form-item label="籍贯" prop="province">
                        <el-input v-model="addForm.province"></el-input>
                    </el-form-item>
                    <el-form-item label="政治面貌" prop="political">
                        <el-input v-model="addForm.political"></el-input>
                    </el-form-item>
                    <el-form-item label="手机号" prop="phone">
                        <el-input v-model="addForm.phone"></el-input>
                    </el-form-item>
                    <el-form-item label="证件号" prop="identifyNo">
                        <el-input v-model="addForm.identifyNo"></el-input>
                    </el-form-item>
                    <el-form-item label="现住址" prop="address">
                        <el-input v-model="addForm.address"></el-input>
                    </el-form-item>
                    <!--                    <el-form-item label="账号状态" prop="state">-->
                    <!--                        <el-input v-model="addForm.state"></el-input>-->
                    <!--                    </el-form-item>-->
                    <el-form-item label="密码" prop="password">
                        <el-input v-model="addForm.password"></el-input>
                    </el-form-item>
                    <!--                    <el-form-item label="头像地址" prop="avatarUrl">-->
                    <!--                        <el-input v-model="addForm.avatarUrl"></el-input>-->
                    <!--                    </el-form-item>-->
                </el-form>
                <span slot="footer" class="dialog-footer">
                        <el-button @click="addDialogVisible = false">取 消</el-button>
                        <el-button type="primary" @click="addUser">确 定</el-button>
                </span>
            </el-dialog>
            <!-- 修改员工信息区域 -->
            <el-dialog title="修改员工信息" :visible.sync="editDialogVisible" width="35%" @close="editDialogClosed">
                <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="100px">
                    <el-form-item label="员工编号" prop="empno">
                        <el-input v-model="editForm.empno" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="员工姓名" prop="name">
                        <el-input v-model="editForm.name"></el-input>
                    </el-form-item>
                    <el-form-item label="角色" prop="roleId">
                        <el-select v-model="editForm.roleId" placeholder="请选择角色" style="width: 100%">
                            <el-option
                                v-for="item in roleOptions"
                                :key="item.roleId"
                                :label="item.name"
                                :value="item.roleId">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="部门名称" prop="deptno">
                        <el-select v-model="editForm.deptno" placeholder="请选择部门"
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
                    <el-form-item label="岗位名称" prop="postno">
                        <el-select v-model="editForm.postno" placeholder="请选择岗位"
                                   @click.native="EditFormCheckData"
                                   style="width: 100%">
                            <el-option
                                v-for="item in postOptions"
                                :key="item.postno"
                                :label="item.name"
                                :value="item.postno">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="性别" prop="sex">
                        <el-radio-group v-model="editForm.sex">
                            <el-radio :label="true" value="1" style="margin-left: 30px">男</el-radio>
                            <el-radio :label="false" value="0">女</el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item label="民族" prop="nation">
                        <el-input v-model="editForm.nation"></el-input>
                    </el-form-item>
                    <el-form-item label="籍贯" prop="province">
                        <el-input v-model="editForm.province"></el-input>
                    </el-form-item>
                    <el-form-item label="政治面貌" prop="political">
                        <el-input v-model="editForm.political"></el-input>
                    </el-form-item>
                    <el-form-item label="手机号" prop="phone">
                        <el-input v-model="editForm.phone"></el-input>
                    </el-form-item>
                    <el-form-item label="证件号" prop="identifyNo">
                        <el-input v-model="editForm.identifyNo"></el-input>
                    </el-form-item>
                    <el-form-item label="现住址" prop="address">
                        <el-input v-model="editForm.address"></el-input>
                    </el-form-item>
                    <!--                    <el-form-item label="账号状态" prop="state">-->
                    <!--                        <el-input v-model="editForm.state"></el-input>-->
                    <!--                    </el-form-item>-->
                    <!--                    <el-form-item label="密码" prop="password">-->
                    <!--                        <el-input v-model="editForm.password"></el-input>-->
                    <!--                    </el-form-item>-->
                    <!--                    <el-form-item label="头像地址" prop="avatarUrl">-->
                    <!--                        <el-input v-model="editForm.avatarUrl"></el-input>-->
                    <!--                    </el-form-item>-->
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
                sex: "",
                roleId: "",
                deptno: "",
                postno: "",
                search: "",// 检索的信息
                pageNum: 1,// 当前页面
                pageSize: 10,// 一页的数据量
            },
            tableData: [],// 员工信息列表
            total: 0,// 员工信息的总记录数
            addDialogVisible: false,// 对话框状态
            // 添加表单信息
            addForm: {
                empno: "",
                name: "",
                roleId: "",
                deptno: "",
                postno: "",
                sex: "",
                identifyNo: "",
                nation: "",
                province: "",
                political: "",
                phone: "",
                address: "",
                state: "",
                password: "",
                avatarUrl: "",
            },
            // 添加的表单验证
            addFormRules: {
                empno: [
                    {required: true, message: '请输入员工编号', trigger: 'blur'},
                    {min: 3, max: 18, message: '长度在3到18个字符', trigger: 'blur'},
                ],
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
                password: [
                    {required: true, message: '请输入密码', trigger: 'blur'},
                    {min: 6, max: 60, message: '长度在6到60个字符', trigger: 'blur'},
                ],
            },
            // 修改员工的信息
            editForm: {},
            // 显示和隐藏修改员工信息的弹窗
            editDialogVisible: false,
            // 修改的表单验证
            editFormRules: {
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
            multipleSelection: [],
            // 下拉菜单，查询出的部门信息
            deptOptions: [],
            // 存放部门信息，便于填充列表
            deptInfo: {},
            // 下拉菜单，查询出的岗位信息
            postOptions: [],
            // 临时存储所有的岗位信息
            tempPostOptions: [],
            // 存放岗位信息，便于填充列表
            postInfo: {},
            // 下拉菜单，查询出角色信息
            roleOptions: [],
            // 存放角色信息，便于填充列表
            roleInfo: {},
        }
    },
    created() {
        // 从数据库中查询到指定的数据，并填充到tableData中
        this.tableLoad();
    },
    // 查询角色信息、部门信息、岗位信息
    mounted() {
        this.request.get("/role").then(res => {
            this.roleOptions = res.data;
            // 将部门信息放置到对象中方便查询
            res.data.forEach(r => {
                this.$set(this.roleInfo, r.roleId, r.name);
            });
        });
        this.request.get("/department").then(res => {
            this.deptOptions = res.data;
            // 将部门信息放置到对象中方便查询
            res.data.forEach(r => {
                this.$set(this.deptInfo, r.deptno, r.name);
            });
        });
        // 默认将查询到的所有岗位信息填充到临时变量中，偏于下拉菜单的选择
        this.request.get("/post").then(res => {
            this.tempPostOptions = res.data;
            // 将部门信息放置到对象中方便查询
            res.data.forEach(r => {
                this.$set(this.postInfo, r.postno, r.name);
            });
        });
    },
    methods: {
        tableLoad() {
            // 分页查询
            this.request.get("/employee/page", {params: this.queryInfo}).then(res => {
                // 员工列表数据封装
                this.tableData = res.data.records;
                // 根据查询出部门信息，将部门名称填充到tableData中
                for (let i = 0; i < this.tableData.length; i++) {
                    this.$set(this.tableData[i], "sexName", this.tableData[i].sex ? "男" : "女");
                    this.$set(this.tableData[i], "roleName", this.roleInfo[this.tableData[i].roleId]);
                    this.$set(this.tableData[i], "deptName", this.deptInfo[this.tableData[i].deptno]);
                    this.$set(this.tableData[i], "postName", this.postInfo[this.tableData[i].postno]);
                }
                // 总员工数量封装
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
        // 添加页面和修改页面，在选择部门信息后，填充该部门的岗位信息
        deptSelectChange(deptno) {
            this.postOptions = [];
            this.tempPostOptions.forEach(r => {
                if (r.deptno == deptno) {
                    this.postOptions.push(r);
                }
            });
        },
        // 信息重置
        reset() {
            this.queryInfo.sex = "";
            this.queryInfo.roleId = "";
            this.queryInfo.deptno = "";
            this.queryInfo.postno = "";
            this.queryInfo.search = "";
            this.queryInfo.pageNum = 1;
            this.queryInfo.pageSize = 10;
            this.tableLoad();
        },
        // 在直接选择岗位信息时，检查是否选择部门信息
        queryInfoCheckData() {
            if (this.queryInfo.deptno == "") {
                return this.$message.warning("请选择部门信息");
            }
        },
        // 在直接选择岗位信息时，检查是否选择部门信息
        addFormCheckData() {
            if (this.addForm.deptno == "") {
                return this.$message.warning("请选择部门信息");
            }
        },
        // 在直接选择岗位信息时，检查是否选择部门信息
        EditFormCheckData() {
            if (this.editForm.deptno == "") {
                return this.$message.warning("请选择部门信息");
            }
        },
        // 保证在选择部门信息时，岗位信息直接置为空
        deptSelectHomeClick() {
            this.queryInfo.postno = "";
        },
        // 保证在选择部门信息时，岗位信息直接置为空
        deptSelectAddClick() {
            this.addForm.postno = "";
        },
        // 保证在选择部门信息时，岗位信息直接置为空
        deptSelectEditClick() {
            this.editForm.postno = "";
        },
        // 账户状态发生改变时同步到数据库
        stateChange(Info) {
            this.request.post("/employee", Info).then(res => {
                if (!res.data) {
                    return this.$message.error("操作失败");
                }
                this.$message.success("操作成功");
            });
        },
        showAddDialog() {
            this.reset();
            this.addDialogVisible = true;
        },
        // 监听添加员工操作
        addDialogClosed() {
            this.postOptions = [];
            this.$refs.addFormRef.resetFields();
        },
        // 添加员工方法
        addUser() {
            this.$refs.addFormRef.validate(async valid => {
                //验证不成功结束方法
                if (!valid) return;
                console.log(this.addForm);
                this.request.post("/employee/insert", this.addForm).then(res => {
                    console.log(this.res);
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
        // 根据主键删除员工信息
        deleteUser(id) {
            console.log(id);
            this.request.delete("/employee/" + id).then(res => {
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
        // 批量删除员工数据
        async delBatch() {
            if (this.multipleSelection[0] == null) {
                this.visible = false;
                return this.$message.warning("请选择需要删除的员工。");
            }
            const confirmResult = await this.$confirm("此操作将永久删除选中员工信息，是否继续？", "提示", {
                confirmButtonText: '删除',
                cancelButtonText: '取消',
                type: 'warning',
            }).catch(error => error);
            if (confirmResult != "confirm") {// 如果取消删除，则提示
                return this.$message.warning("取消删除。");
            }
            let ids = this.multipleSelection.map(v => v.empno);
            this.request.post("/employee/del/batch", ids).then(res => {
                if (!res.data) {
                    return this.$message.error("批量删除失败")
                }
                this.$message.success("批量删除成功");
                this.tableLoad();
            });
        },
        // 显示对编辑话框
        showEditDialog(id) {
            this.reset();
            this.request.get("/employee/" + id).then(res => {
                this.editForm = res.data;// 查询出员工信息，并反填回编辑表单中
                console.log(res.data);
                // 在打开编辑窗口时，根据查询到员工信息中的部门编号查询出该部门所有的岗位信息
                this.deptSelectChange(res.data.deptno);
                this.editForm.postno = res.data.postno;
                this.editDialogVisible = true;// 开启编辑窗口
            });

        },
        // 关闭编辑对话框
        editDialogClosed() {
            this.postOptions = [];
            this.$refs.editFormRef.resetFields();// 重置信息
        },
        // 编辑员工信息方法
        editUserInfo() {
            this.$refs.editFormRef.validate(async valid => {
                //验证不成功结束方法
                if (!valid) return;
                this.request.post("/employee", this.editForm).then(res => {
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
            window.open("http://localhost:9090/employee/export");
        },
        //导出导入的数据
        exptemp() {
            window.open("http://localhost:9090/employee/exporttemp");
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