-- MySQL dump 10.13  Distrib 8.0.18, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: staffing
-- ------------------------------------------------------
-- Server version	8.0.18

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `department` (
  `deptno` varchar(6) NOT NULL COMMENT '部门编号',
  `name` varchar(20) NOT NULL COMMENT '部门名称',
  `location` varchar(255) NOT NULL COMMENT '部门地址',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`deptno`),
  UNIQUE KEY `up_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='部门表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `department`
--

LOCK TABLES `department` WRITE;
/*!40000 ALTER TABLE `department` DISABLE KEYS */;
INSERT INTO `department` (`deptno`, `name`, `location`, `description`) VALUES ('1001','开发部','郑州',''),('1002','测试部','郑州','');
/*!40000 ALTER TABLE `department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee` (
  `empno` varchar(18) NOT NULL COMMENT '员工编号',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `deptno` varchar(6) NOT NULL COMMENT '部门编号',
  `postno` varchar(6) NOT NULL COMMENT '岗位编号',
  `name` varchar(20) NOT NULL COMMENT '姓名',
  `sex` tinyint(4) NOT NULL COMMENT '性别',
  `identify_no` varchar(18) NOT NULL COMMENT '有效证件号',
  `nation` varchar(10) NOT NULL COMMENT '民族',
  `province` varchar(60) NOT NULL COMMENT '籍贯',
  `political` varchar(20) NOT NULL COMMENT '政治面貌',
  `phone` varchar(11) NOT NULL COMMENT '手机号',
  `address` varchar(255) NOT NULL COMMENT '现住址',
  `password` varchar(60) NOT NULL COMMENT '密码',
  `state` tinyint(4) NOT NULL COMMENT '账号状态',
  `avatar_url` varchar(255) DEFAULT NULL COMMENT '头像地址',
  PRIMARY KEY (`empno`),
  UNIQUE KEY `uq_identifyNo` (`identify_no`),
  KEY `FK_Reference_11` (`postno`),
  KEY `FK_Reference_5` (`role_id`),
  KEY `FK_Reference_12` (`deptno`),
  CONSTRAINT `FK_Reference_11` FOREIGN KEY (`postno`) REFERENCES `post` (`postno`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_Reference_12` FOREIGN KEY (`deptno`) REFERENCES `post` (`deptno`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_Reference_5` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`role_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='员工表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` (`empno`, `role_id`, `deptno`, `postno`, `name`, `sex`, `identify_no`, `nation`, `province`, `political`, `phone`, `address`, `password`, `state`, `avatar_url`) VALUES ('111111',2,'1001','001','小小员工',1,'412726199812302832','汉','河南','团员','13461300000','阿斯蒂芬','123456',1,NULL),('123456',1,'1001','001','小小管理员',1,'123456123456781234','汉','河南','团员','13461321000','河南1','123456',1,'http://localhost:9090/files/648a60ec462a47a2a6aaa6f5c1f6d3f7.jpg'),('222222',2,'1002','003','1231',1,'412726199812302830','汉','河南','团员','13461320000','asdf','123456',1,NULL);
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post`
--

DROP TABLE IF EXISTS `post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post` (
  `postno` varchar(6) NOT NULL COMMENT '岗位编号',
  `deptno` varchar(6) NOT NULL COMMENT '部门编号',
  `name` varchar(20) NOT NULL COMMENT '岗位名称',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`postno`),
  UNIQUE KEY `uq_name` (`name`,`deptno`),
  KEY `FK_Reference_10` (`deptno`),
  CONSTRAINT `FK_Reference_10` FOREIGN KEY (`deptno`) REFERENCES `department` (`deptno`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='岗位表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post`
--

LOCK TABLES `post` WRITE;
/*!40000 ALTER TABLE `post` DISABLE KEYS */;
INSERT INTO `post` (`postno`, `deptno`, `name`, `description`) VALUES ('001','1001','Java工程师',''),('002','1001','前端工程师',''),('003','1002','测试工程师','');
/*!40000 ALTER TABLE `post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `salary`
--

DROP TABLE IF EXISTS `salary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `salary` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '工资编号',
  `pay_date` varchar(7) NOT NULL COMMENT '发放日期',
  `empno` varchar(18) NOT NULL COMMENT '员工编号',
  `base` float(10,2) NOT NULL COMMENT '基本工资',
  `performance` float(10,2) NOT NULL COMMENT '绩效工资',
  `bonus` float(10,2) NOT NULL COMMENT '奖金',
  `subsidy` float(10,2) NOT NULL COMMENT '补助',
  `insurance` float(10,2) NOT NULL COMMENT '保险',
  `penalty` float(10,2) NOT NULL COMMENT '罚款',
  `absenteeism` float(10,2) NOT NULL COMMENT '缺勤',
  `fsalary` float(10,2) NOT NULL COMMENT '实发工资',
  `input_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '录入时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_empno_pay_time` (`pay_date`,`empno`),
  KEY `FK_Reference_8` (`empno`),
  CONSTRAINT `FK_Reference_8` FOREIGN KEY (`empno`) REFERENCES `employee` (`empno`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='工资表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `salary`
--

LOCK TABLES `salary` WRITE;
/*!40000 ALTER TABLE `salary` DISABLE KEYS */;
INSERT INTO `salary` (`id`, `pay_date`, `empno`, `base`, `performance`, `bonus`, `subsidy`, `insurance`, `penalty`, `absenteeism`, `fsalary`, `input_time`, `remark`) VALUES (2,'2022-02','123456',1.12,1111.00,1.10,1123.00,1.00,1.00,1.00,2239.22,'2022-03-27 06:45:45','1'),(3,'2022-03','123456',1.11,1.11,1.12,11.00,1.11,1.00,1.00,17.45,'2022-03-27 06:45:45','1'),(4,'2022-04','123456',1.00,1.00,1.00,1123123.00,1.00,1.00,1.00,1123129.00,'2022-03-27 06:45:45','1'),(5,'2022-05','123456',1.00,1.00,1.00,1.00,11111.00,1.00,1.00,11117.00,'2022-03-27 06:45:45','1'),(6,'2022-06','123456',1.00,1.00,1.00,1.00,122222.00,1.00,1.00,122228.00,'2022-03-27 06:45:45','1'),(7,'2022-07','123456',1.00,1.00,1.00,1.00,11111.00,1.00,1.00,11117.00,'2022-03-27 06:45:45','1'),(13,'2022-10','111111',1.00,1.00,1.00,1.00,1.00,1.00,11111.00,611111.00,'2022-03-27 09:22:09',''),(17,'2021-02','111111',1.00,1.00,1.00,1.00,22222.00,1.00,1.00,22228.00,'2022-03-27 09:32:26','');
/*!40000 ALTER TABLE `salary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_files`
--

DROP TABLE IF EXISTS `sys_files`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_files` (
  `files_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '文件ID',
  `name` varchar(255) NOT NULL COMMENT '文件名称',
  `type` varchar(255) NOT NULL COMMENT '文件类型',
  `size` int(11) NOT NULL COMMENT '文件大小(KB)',
  `url` varchar(255) NOT NULL COMMENT '文件地址',
  `md5` varchar(255) NOT NULL COMMENT 'MD5',
  `is_delete` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`files_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文件表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_files`
--

LOCK TABLES `sys_files` WRITE;
/*!40000 ALTER TABLE `sys_files` DISABLE KEYS */;
INSERT INTO `sys_files` (`files_id`, `name`, `type`, `size`, `url`, `md5`, `is_delete`) VALUES (1,'K.jpg','jpg',71,'http://localhost:9090/files/648a60ec462a47a2a6aaa6f5c1f6d3f7.jpg','d4c96d8a8913fd9ef11b8e7e90e8ceb2',0),(2,'K.jpg','jpg',71,'http://localhost:9090/files/648a60ec462a47a2a6aaa6f5c1f6d3f7.jpg','d4c96d8a8913fd9ef11b8e7e90e8ceb2',1),(3,'K.jpg','jpg',71,'http://localhost:9090/files/648a60ec462a47a2a6aaa6f5c1f6d3f7.jpg','d4c96d8a8913fd9ef11b8e7e90e8ceb2',1),(4,'K.jpg','jpg',71,'http://localhost:9090/files/648a60ec462a47a2a6aaa6f5c1f6d3f7.jpg','d4c96d8a8913fd9ef11b8e7e90e8ceb2',1),(5,'K.jpg','jpg',71,'http://localhost:9090/files/648a60ec462a47a2a6aaa6f5c1f6d3f7.jpg','d4c96d8a8913fd9ef11b8e7e90e8ceb2',1),(6,'KK.png','png',181,'http://localhost:9090/files/679c77e64a2049f18d6eaa712475ff56.png','2660d1f8b2eabd3bb730938c756c3927',1),(7,'KK.png','png',181,'http://localhost:9090/files/679c77e64a2049f18d6eaa712475ff56.png','2660d1f8b2eabd3bb730938c756c3927',0),(8,'8.jpg','jpg',83,'http://localhost:9090/files/2254f32f871049a5b254df03012f0dca.jpg','6deda01446dca1ca92562ab07b3cf2eb',0),(9,'8.jpg','jpg',83,'http://localhost:9090/files/2254f32f871049a5b254df03012f0dca.jpg','6deda01446dca1ca92562ab07b3cf2eb',1),(10,'K.jpg','jpg',71,'http://localhost:9090/files/648a60ec462a47a2a6aaa6f5c1f6d3f7.jpg','d4c96d8a8913fd9ef11b8e7e90e8ceb2',1),(11,'用户信息 (11).xlsx','xlsx',9,'http://localhost:9090/files/7f3d1bb3449142febd7921f0c3d3179f.xlsx','ad16a4141859922fb8e67f9a2d2188f3',1),(12,'KK.png','png',181,'http://localhost:9090/files/679c77e64a2049f18d6eaa712475ff56.png','2660d1f8b2eabd3bb730938c756c3927',0),(13,'K.jpg','jpg',71,'http://localhost:9090/files/648a60ec462a47a2a6aaa6f5c1f6d3f7.jpg','d4c96d8a8913fd9ef11b8e7e90e8ceb2',0);
/*!40000 ALTER TABLE `sys_files` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_icon`
--

DROP TABLE IF EXISTS `sys_icon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_icon` (
  `icon_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '图标ID',
  `name` varchar(255) NOT NULL COMMENT '图标名称',
  `icon` varchar(255) NOT NULL COMMENT '图标el地址',
  PRIMARY KEY (`icon_id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='图标表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_icon`
--

LOCK TABLES `sys_icon` WRITE;
/*!40000 ALTER TABLE `sys_icon` DISABLE KEYS */;
INSERT INTO `sys_icon` (`icon_id`, `name`, `icon`) VALUES (7,'图标','el-icon-star-off'),(8,'主页','el-icon-house'),(16,'角色','el-icon-s-custom'),(17,'菜单-s','el-icon-s-grid'),(18,'文件','el-icon-document'),(19,'文件','el-icon-folder-opened'),(20,'图标','el-icon-view'),(21,'标签','el-icon-collection-tag'),(22,'权限','el-icon-lock'),(25,'部门','el-icon-office-building'),(26,'岗位','el-icon-user-solid'),(27,'工资','el-icon-coin'),(28,'用户','el-icon-user'),(29,'菜单','el-icon-menu'),(31,'请假','el-icon-document'),(32,'请假','el-icon-document-add'),(33,'密码','el-icon-key');
/*!40000 ALTER TABLE `sys_icon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu` (
  `menu_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `pid` int(11) DEFAULT NULL COMMENT '父级菜单id',
  `name` varchar(255) NOT NULL COMMENT '菜单名称',
  `path` varchar(255) DEFAULT NULL COMMENT '菜单路径',
  `page_path` varchar(255) DEFAULT NULL COMMENT '页面路径',
  `icon` varchar(255) NOT NULL COMMENT '菜单图标',
  `sort_num` int(11) NOT NULL COMMENT '菜单顺序',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` (`menu_id`, `pid`, `name`, `path`, `page_path`, `icon`, `sort_num`, `description`) VALUES (1,NULL,'主页','/home','Home','el-icon-house',100,''),(2,NULL,'系统管理','',NULL,'el-icon-s-grid',2000,''),(5,2,'文件管理','/files','FilesList','el-icon-folder-opened',2001,''),(17,2,'图标管理','/icon','IconList','el-icon-collection-tag',2002,''),(22,NULL,'权限管理','','','el-icon-lock',1000,''),(23,22,'角色管理','/role','RoleList','el-icon-s-custom',1001,''),(24,22,'菜单管理','/menu','MenuList','el-icon-menu',1002,''),(31,NULL,'部门管理','/department','DepartmentList','el-icon-office-building',200,''),(32,NULL,'岗位管理','/post','PostList','el-icon-user-solid',300,''),(33,NULL,'员工管理','/employee','EmployeeList','el-icon-user',400,''),(35,NULL,'工资管理','/salary','SalaryList','el-icon-coin',500,''),(36,NULL,'请假管理','/vacation','VacationList','el-icon-document',700,''),(37,NULL,'请假','/vacationrequest','SubVacation','el-icon-document-add',600,''),(38,NULL,'个人中心','/person','Person','el-icon-user',800,''),(39,NULL,'修改密码','/password','Password','el-icon-key',900,'');
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `name` varchar(255) NOT NULL COMMENT '角色名称',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` (`role_id`, `name`, `description`) VALUES (1,'超级管理员','管理一切'),(2,'员工','查询信息');
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_menu`
--

DROP TABLE IF EXISTS `sys_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_menu` (
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `menu_id` int(11) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`),
  KEY `FK_Reference_2` (`menu_id`),
  CONSTRAINT `FK_Reference_1` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`role_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_Reference_2` FOREIGN KEY (`menu_id`) REFERENCES `sys_menu` (`menu_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色菜单关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_menu`
--

LOCK TABLES `sys_role_menu` WRITE;
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1,1),(2,1),(1,2),(1,5),(1,17),(1,22),(1,23),(1,24),(1,31),(1,32),(1,33),(1,35),(1,36),(1,37),(2,37),(1,38),(2,38),(1,39),(2,39);
/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `nickname` varchar(60) DEFAULT NULL COMMENT '昵称/姓名',
  `password` varchar(255) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `state` tinyint(1) DEFAULT '0',
  `avatar_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_username_uindex` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`id`, `username`, `nickname`, `password`, `email`, `role`, `state`, `avatar_url`) VALUES (1,'admin','小小管理员','123456','admin@qq.com','超级管理员',1,'http://localhost:9090/files/679c77e64a2049f18d6eaa712475ff56.png'),(2,'admin2','第一名教师','123456','admin2@qq.com','教师',1,'http://localhost:9090/files/2254f32f871049a5b254df03012f0dca.jpg'),(5,'admin3','第一位学生','123456','admin3@qq.com','学生',1,NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vacation`
--

DROP TABLE IF EXISTS `vacation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vacation` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '总请假编号',
  `empno` varchar(18) NOT NULL COMMENT '员工编号',
  `reason` varchar(255) NOT NULL COMMENT '请假原因',
  `start_datetime` datetime NOT NULL COMMENT '假期开始时间',
  `end_datetime` datetime NOT NULL COMMENT '假期结束时间',
  `input_datetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `state` tinyint(4) NOT NULL COMMENT '状态',
  `check_empno` varchar(18) DEFAULT NULL COMMENT '员工编号',
  `check_datetime` datetime DEFAULT NULL COMMENT '审核日期',
  PRIMARY KEY (`id`),
  KEY `FK_Reference_7` (`empno`),
  KEY `FK_Reference_9` (`check_empno`),
  CONSTRAINT `FK_Reference_7` FOREIGN KEY (`empno`) REFERENCES `employee` (`empno`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_Reference_9` FOREIGN KEY (`check_empno`) REFERENCES `employee` (`empno`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='请假表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vacation`
--

LOCK TABLES `vacation` WRITE;
/*!40000 ALTER TABLE `vacation` DISABLE KEYS */;
INSERT INTO `vacation` (`id`, `empno`, `reason`, `start_datetime`, `end_datetime`, `input_datetime`, `state`, `check_empno`, `check_datetime`) VALUES (1,'123456','123','2022-03-29 19:41:52','2022-03-29 19:41:55','2022-03-29 19:42:11',1,'123456','2022-03-30 08:52:09'),(2,'123456','312312','2022-03-29 16:00:00','2022-04-24 16:00:00','2022-03-29 19:45:42',1,'123456','2022-03-30 08:43:05'),(3,'123456','123213','2022-03-15 16:00:00','2022-04-18 16:00:00','2022-03-29 19:46:35',1,'123456','2022-03-30 08:44:22'),(6,'111111','asdfd','2022-03-29 16:00:00','2022-03-30 16:00:00','2022-03-30 10:16:10',1,'123456','2022-03-30 10:21:03'),(7,'222222','asdfasd','2022-03-29 16:00:00','2022-03-30 16:00:00','2022-03-30 10:17:30',1,'123456','2022-03-30 13:33:17'),(8,'123456','asdfasd','2022-03-29 16:00:00','2022-03-30 16:00:00','2022-03-30 11:02:54',-1,'123456','2022-03-30 13:33:20');
/*!40000 ALTER TABLE `vacation` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-04-01 18:51:49
