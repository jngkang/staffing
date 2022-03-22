/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2022/3/22 21:44:16                           */
/*==============================================================*/


drop table if exists department;

drop table if exists employee;

drop table if exists post;

drop table if exists salary;

drop table if exists sys_files;

drop table if exists sys_icon;

drop table if exists sys_menu;

drop table if exists sys_role;

drop table if exists sys_role_menu;

drop table if exists vacation;

/*==============================================================*/
/* Table: department                                            */
/*==============================================================*/
create table department
(
   deptno               varchar(6) not null comment '部门编号',
   name                 varchar(160) not null comment '部门名称',
   location             varchar(255) not null comment '部门地址',
   description          varchar(255) comment '描述',
   primary key (deptno),
   unique key up_name (name)
);

alter table department comment '部门表';

/*==============================================================*/
/* Table: employee                                              */
/*==============================================================*/
create table employee
(
   empno                varchar(18) not null comment '员工编号',
   role_id              int not null comment '角色ID',
   postno               varchar(6) not null comment '岗位编号',
   deptno               varchar(6) not null comment '部门编号',
   name                 varchar(20) not null comment '姓名',
   sex                  tinyint not null comment '性别',
   identifyNo           varchar(18) not null comment '有效证件号',
   nation               varchar(10) not null comment '民族',
   province             varchar(60) not null comment '籍贯',
   political            varchar(20) not null comment '政治面貌',
   phone                varchar(11) not null comment '手机号',
   address              varchar(255) not null comment '现住址',
   password             varchar(60) not null comment '密码',
   state                tinyint not null comment '账号状态',
   avatar_url           varchar(255) comment '头像地址',
   primary key (empno),
   unique key uq_name (name),
   unique key uq_identifyNo (identifyNo)
);

alter table employee comment '员工表';

/*==============================================================*/
/* Table: post                                                  */
/*==============================================================*/
create table post
(
   postno               varchar(6) not null comment '岗位编号',
   deptno               varchar(6) not null comment '部门编号',
   name                 varchar(20) not null comment '岗位名称',
   description          varchar(255) comment '描述',
   primary key (postno, deptno),
   unique key uq_name (name)
);

alter table post comment '岗位表';

/*==============================================================*/
/* Table: salary                                                */
/*==============================================================*/
create table salary
(
   empno                varchar(18) not null comment '员工编号',
   base                 float(10,2) not null comment '基本工资',
   performance          float(10,2) not null comment '绩效工资',
   bonus                float(10,2) not null comment '奖金',
   subsidy              float(10,2) not null comment '补助',
   insurance            float(10,2) not null comment '保险',
   penalty              float(10,2) not null comment '罚款',
   absenteeism          float(10,2) not null comment '缺勤',
   fsalary              float(10,2) not null comment '实发工资',
   remark               varchar(255) comment '备注',
   primary key (empno)
);

alter table salary comment '工资表';

/*==============================================================*/
/* Table: sys_files                                             */
/*==============================================================*/
create table sys_files
(
   files_id             int not null auto_increment comment '文件ID',
   name                 varchar(255) not null comment '文件名称',
   type                 varchar(255) not null comment '文件类型',
   size                 int not null comment '文件大小(KB)',
   url                  varchar(255) not null comment '文件地址',
   md5                  varchar(255) not null comment 'MD5',
   is_delete            boolean not null default 0 comment '是否删除',
   primary key (files_id)
);

alter table sys_files comment '文件表';

/*==============================================================*/
/* Table: sys_icon                                              */
/*==============================================================*/
create table sys_icon
(
   icon_id              int not null auto_increment comment '图标ID',
   name                 varchar(255) not null comment '图标名称',
   icon                 varchar(255) not null comment '图标el地址',
   primary key (icon_id)
);

alter table sys_icon comment '图标表';

/*==============================================================*/
/* Table: sys_menu                                              */
/*==============================================================*/
create table sys_menu
(
   menu_id              int not null auto_increment comment '菜单ID',
   pid                  int comment '父级菜单id',
   name                 varchar(255) not null comment '菜单名称',
   path                 varchar(255) comment '菜单路径',
   page_path            varchar(255) comment '页面路径',
   icon                 varchar(255) not null comment '菜单图标',
   sort_num             int not null comment '菜单顺序',
   description          varchar(255) comment '描述',
   primary key (menu_id)
);

alter table sys_menu comment '菜单表';

/*==============================================================*/
/* Table: sys_role                                              */
/*==============================================================*/
create table sys_role
(
   role_id              int not null auto_increment comment '角色ID',
   name                 varchar(255) not null comment '角色名称',
   description          varchar(255) comment '描述',
   primary key (role_id)
);

alter table sys_role comment '角色表';

/*==============================================================*/
/* Table: sys_role_menu                                         */
/*==============================================================*/
create table sys_role_menu
(
   role_id              int not null comment '角色ID',
   menu_id              int not null comment '菜单ID',
   primary key (role_id, menu_id)
);

alter table sys_role_menu comment '角色菜单关系表';

/*==============================================================*/
/* Table: vacation                                              */
/*==============================================================*/
create table vacation
(
   vatno                int not null auto_increment comment '总请假编号',
   empno                varchar(18) not null comment '员工编号',
   reason               varchar(255) not null comment '请假原因',
   inoutTime            datetime not null comment '申请日期',
   state                tinyint not null comment '状态',
   check_empno          varchar(18) comment '员工编号',
   checkTime            datetime comment '审核日期',
   primary key (vatno)
);

alter table vacation comment '请假表';

alter table employee add constraint FK_Reference_11 foreign key (postno, deptno)
      references post (postno, deptno) on delete restrict on update restrict;

alter table employee add constraint FK_Reference_5 foreign key (role_id)
      references sys_role (role_id) on delete restrict on update restrict;

alter table post add constraint FK_Reference_10 foreign key (deptno)
      references department (deptno) on delete restrict on update restrict;

alter table salary add constraint FK_Reference_8 foreign key (empno)
      references employee (empno) on delete restrict on update restrict;

alter table sys_role_menu add constraint FK_Reference_1 foreign key (role_id)
      references sys_role (role_id) on delete restrict on update restrict;

alter table sys_role_menu add constraint FK_Reference_2 foreign key (menu_id)
      references sys_menu (menu_id) on delete restrict on update restrict;

alter table vacation add constraint FK_Reference_7 foreign key (empno)
      references employee (empno) on delete restrict on update restrict;

alter table vacation add constraint FK_Reference_9 foreign key (check_empno)
      references employee (empno) on delete restrict on update restrict;

