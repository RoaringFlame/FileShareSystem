CREATE TABLE `t_catalog` (
  `id` varchar(50) NOT NULL COMMENT '主键',
  `version` int(11) NOT NULL DEFAULT '0',
  `usable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可用：1.是，0.否',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `parent_id` varchar(50) DEFAULT NULL COMMENT '父节点',
  `name` varchar(50) DEFAULT NULL COMMENT '目录名称',
  `description` varchar(255) DEFAULT NULL COMMENT '目录描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='目录信息表';

CREATE TABLE `t_file` (
  `id` varchar(50) NOT NULL COMMENT '主键',
  `version` int(11) NOT NULL DEFAULT '0',
  `usable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可用：1.是，0.否',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '文件分享时间',
  `user_id` varchar(50) DEFAULT NULL COMMENT '文件上传者',
  `catalog_id` varchar(50) DEFAULT NULL COMMENT '文件存储目录',
  `new_version_id` varchar(50) DEFAULT NULL COMMENT '对应文件最新版本号',
  `file_name` varchar(50) DEFAULT NULL COMMENT '文件名',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文件信息表，用户分享文件的基本信息';

CREATE TABLE `t_file_receive` (
  `id` varchar(50) NOT NULL COMMENT '主键',
  `version` int(11) NOT NULL DEFAULT '0',
  `usable` tinyint(1) NOT NULL DEFAULT '1',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `file_id` varchar(50) NOT NULL COMMENT '文件接受版本编号',
  `version_id` varchar(50) NOT NULL,
  `user_id` varchar(50) NOT NULL COMMENT '文件接收用户',
  `can_revise` tinyint(1) DEFAULT '0' COMMENT '是否有权限修改：1是，0否，默认0',
  `is_alert` tinyint(1) DEFAULT '0' COMMENT '是否紧急：1是，0否，紧急会发送邮件通知对方，并在首页提示',
  `is_received` tinyint(1) DEFAULT '0' COMMENT '是否已接收：1是，0否，默认0',
  `download_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '文件接收时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='记录文件接收信息';

CREATE TABLE `t_file_version` (
  `id` varchar(50) NOT NULL COMMENT '主键',
  `version` int(11) NOT NULL DEFAULT '0',
  `usable` tinyint(1) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `user_id` varchar(50) NOT NULL,
  `file_id` varchar(50) NOT NULL COMMENT '修改的文件',
  `number` decimal(11,1) DEFAULT NULL COMMENT '生成版本号',
  `count` int(11) DEFAULT NULL COMMENT '该版本编号对应文件的下载次数',
  `can_cover` tinyint(1) DEFAULT '0' COMMENT '是否可覆盖，1是，0否',
  `real_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_operate` (
  `id` varchar(50) NOT NULL COMMENT '主键',
  `version` int(11) NOT NULL DEFAULT '0',
  `usable` tinyint(1) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `user_id` varchar(50) DEFAULT NULL COMMENT '操作者',
  `version_id` varchar(50) DEFAULT NULL COMMENT '操作版本编号',
  `operate_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
  `operate_flag` tinyint(4) DEFAULT NULL COMMENT '操作标志:0上传文件，1下载，2修改版本，3删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_user` (
  `id` varchar(50) NOT NULL COMMENT '主键',
  `version` int(11) NOT NULL DEFAULT '0',
  `usable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可用：1.是，0.否',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `username` varchar(50) DEFAULT NULL COMMENT '登录帐号',
  `password` varchar(50) DEFAULT NULL COMMENT '登录密码',
  `name` varchar(50) DEFAULT NULL COMMENT '用户姓名',
  `gender` tinyint(2) DEFAULT '1' COMMENT '用户性别：1男，0女',
  `email` varchar(50) DEFAULT NULL COMMENT '用户邮箱',
  `department` tinyint(4) DEFAULT NULL COMMENT '用户所在部门：0人力资源部，1行政部',
  `role` tinyint(4) DEFAULT NULL COMMENT '用户权限标志：0经理，1资深员工，2普通员工',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `t_user` (`id`, `version`, `usable`, `create_time`, `username`, `password`, `name`, `gender`, `email`, `department`, `role`) VALUES ('1', '1', '1', '2017-10-10 15:22:13', 'test', 'dc483e80a7a0bd9ef71d8cf973673924', '测试', '1', '747522309@qq.com', '0', '0');