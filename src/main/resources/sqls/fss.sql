-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: 132.126.3.236    Database: file_share_ch
-- ------------------------------------------------------
-- Server version	5.6.22

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `t_catalog`
--

DROP TABLE IF EXISTS `t_catalog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_catalog` (
  `id` varchar(50) NOT NULL COMMENT '主键',
  `parent_id` varchar(50) DEFAULT NULL COMMENT '父节点',
  `name` varchar(50) DEFAULT NULL COMMENT '目录名称',
  `description` varchar(255) DEFAULT NULL COMMENT '目录描述',
  `usable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可用：1.是，0.否',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='目录信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_catalog`
--

LOCK TABLES `t_catalog` WRITE;
/*!40000 ALTER TABLE `t_catalog` DISABLE KEYS */;
INSERT INTO `t_catalog` VALUES ('0','0','根目录',NULL,1),('1','0','人力资源',NULL,1),('2','0','行政事务',NULL,1),('3','1','员工管理',NULL,1),('4','1','社招管理',NULL,1),('5','1','校招管理',NULL,1),('6','2','办公用品',NULL,1),('7','2','薪资发放',NULL,1),('8','3','CRM组',NULL,1),('9','3','计费组',NULL,1);
/*!40000 ALTER TABLE `t_catalog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_file`
--

DROP TABLE IF EXISTS `t_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_file` (
  `id` varchar(50) NOT NULL COMMENT '主键',
  `user_id` varchar(50) DEFAULT NULL COMMENT '文件上传者',
  `catalog_id` varchar(50) DEFAULT NULL COMMENT '文件存储目录',
  `new_version_id` varchar(50) DEFAULT NULL COMMENT '对应文件最新版本号',
  `file_name` varchar(50) DEFAULT NULL COMMENT '文件名',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '文件分享时间',
  `usable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可用：1.是，0.否',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文件信息表，用户分享文件的基本信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_file`
--


--
-- Table structure for table `t_file_receive`
--

DROP TABLE IF EXISTS `t_file_receive`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_file_receive` (
  `id` varchar(50) NOT NULL COMMENT '主键',
  `file_id` varchar(50) DEFAULT NULL COMMENT '文件编号',
  `version_id` varchar(50) DEFAULT NULL COMMENT '文件接受版本编号',
  `user_id` varchar(50) DEFAULT NULL COMMENT '文件接收用户',
  `can_revise` tinyint(1) DEFAULT '0' COMMENT '是否有权限修改：1是，0否，默认0',
  `is_alert` tinyint(1) DEFAULT '0' COMMENT '是否紧急：1是，0否，紧急会发送邮件通知对方，并在首页提示',
  `is_received` tinyint(1) DEFAULT '0' COMMENT '是否已接收：1是，0否，默认0',
  `download_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '文件接收时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='记录文件接收信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_file_receive`
--

--
-- Table structure for table `t_file_version`
--

DROP TABLE IF EXISTS `t_file_version`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_file_version` (
  `id` varchar(50) NOT NULL COMMENT '主键',
  `user_id` varchar(50) DEFAULT NULL,
  `file_id` varchar(50) DEFAULT NULL COMMENT '修改的文件',
  `number` decimal(11,1) DEFAULT NULL COMMENT '生成版本号',
  `real_name` varchar(50) DEFAULT NULL COMMENT '文件存放在服务器中的名字，毫秒级的时间戳：yyyyMMddHHmmssSSS',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `count` int(11) DEFAULT NULL COMMENT '该版本编号对应文件的下载次数',
  `can_cover` tinyint(1) DEFAULT '0' COMMENT '是否可覆盖，1是，0否',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_file_version`
--

LOCK TABLES `t_file_version` WRITE;
/*!40000 ALTER TABLE `t_file_version` DISABLE KEYS */;
INSERT INTO `t_file_version` VALUES ('1df1ffe5-ad1a-431a-9744-99f19e00ab3b','7','6be93c93-380a-478b-ba04-cac7a6154141',1.0,'20170907144158945.ppt','2017-09-07 06:41:58',0,0);
/*!40000 ALTER TABLE `t_file_version` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_operate`
--

DROP TABLE IF EXISTS `t_operate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_operate` (
  `id` varchar(50) NOT NULL COMMENT '主键',
  `user_id` varchar(50) DEFAULT NULL COMMENT '操作者',
  `version_id` varchar(50) DEFAULT NULL COMMENT '操作版本编号',
  `operate_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
  `operate_flag` tinyint(4) DEFAULT NULL COMMENT '操作标志:0上传文件，1下载，2修改版本，3删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_operate`
--

--
-- Table structure for table `t_user`
--

DROP TABLE IF EXISTS `t_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_user` (
  `id` varchar(50) NOT NULL COMMENT '主键',
  `username` varchar(50) DEFAULT NULL COMMENT '登录帐号',
  `password` varchar(50) DEFAULT NULL COMMENT '登录密码',
  `name` varchar(50) DEFAULT NULL COMMENT '用户姓名',
  `gender` tinyint(2) DEFAULT '1' COMMENT '用户性别：1男，0女',
  `email` varchar(50) DEFAULT NULL COMMENT '用户邮箱',
  `department` tinyint(4) DEFAULT NULL COMMENT '用户所在部门：0人力资源部，1行政部',
  `role` tinyint(4) DEFAULT NULL COMMENT '用户权限标志：0经理，1资深员工，2普通员工',
  `usable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可用：1.是，0.否',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_user`
--

LOCK TABLES `t_user` WRITE;
/*!40000 ALTER TABLE `t_user` DISABLE KEYS */;
INSERT INTO `t_user` VALUES ('1','test','e10adc3949ba59abbe56e057f20f883e','测试',1,'747522309@qq.com',0,0,1),('10','dongyu','e10adc3949ba59abbe56e057f20f883e','董昱',1,'45094143@qq.com',1,2,1),('11','chenweicai','e10adc3949ba59abbe56e057f20f883e','陈伟才',1,'823159729@qq.com',1,2,1),('12','lixingzong','e10adc3949ba59abbe56e057f20f883e','李行宗',1,'1131217329@qq.com',1,2,1),('2','liujun','e10adc3949ba59abbe56e057f20f883e','柳军',1,'40050776@qq.com',0,0,1),('3','pengjie','e10adc3949ba59abbe56e057f20f883e','彭洁',0,'576651769@qq.com',0,1,1),('4','pengdaixue','e10adc3949ba59abbe56e057f20f883e','彭代雪',0,'358147442@qq.com',0,1,1),('5','denghuidan','e10adc3949ba59abbe56e057f20f883e','邓惠丹',0,'1210366692@qq.com',0,1,1),('6','yangxiaoqiu','e10adc3949ba59abbe56e057f20f883e','杨晓秋',1,'35309835@qq.com',0,1,1),('7','chenheng','e10adc3949ba59abbe56e057f20f883e','陈恒',1,'13260592767@163.com',0,2,1),('8','sunxiaopeng','e10adc3949ba59abbe56e057f20f883e','孙晓鹏',1,'976687107@qq.com',1,2,1),('9','leizhen','e10adc3949ba59abbe56e057f20f883e','雷震',1,'1392770795@qq.com',1,2,1);
/*!40000 ALTER TABLE `t_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-09-07 15:51:47
