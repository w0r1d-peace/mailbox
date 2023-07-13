-- ----------------------------
-- Table structure for mailbox_task
-- ----------------------------
DROP TABLE IF EXISTS `mailbox_task`;
CREATE TABLE `mailbox_task`  (
  `id` bigint(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮箱帐号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮箱密码',
  `conn_status` tinyint(1) NULL DEFAULT NULL COMMENT '连接状态 0-无状态 1-连接中 2-连接异常',
  `conn_exception_reason` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '连接异常原因',
  `port` int(11) NULL DEFAULT NULL COMMENT '服务器端口',
  `host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务器地址',
  `has_ssl` tinyint(1) NULL DEFAULT NULL COMMENT '是否支持SSL 0-否 1-是',
  `protocol_type` tinyint(1) NULL DEFAULT NULL COMMENT '协议类型 0-爬虫 1-imap 2-pop3 3-exchange',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_id` bigint(11) NULL DEFAULT NULL COMMENT '创建人ID',
  `create_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` bigint(20) NULL DEFAULT NULL COMMENT '修改人ID',
  `update_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人名称',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `removed` tinyint(1) NULL DEFAULT NULL COMMENT '逻辑删除 0-否 1-是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '邮箱任务' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for mailbox_task_email_attachment
-- ----------------------------
DROP TABLE IF EXISTS `mailbox_task_email_attachment`;
CREATE TABLE `mailbox_task_email_attachment`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `header_id` bigint(20) NOT NULL COMMENT '邮件ID',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路径',
  `cid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `content_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '连接类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '邮件附件' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mailbox_task_email_content
-- ----------------------------
DROP TABLE IF EXISTS `mailbox_task_email_content`;
CREATE TABLE `mailbox_task_email_content`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `header_id` bigint(20) NOT NULL COMMENT '邮件ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '信息内容',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modified` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `removed` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除 0-否 1-是',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `creator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1099 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '邮件内容' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for mailbox_task_email_header
-- ----------------------------
DROP TABLE IF EXISTS `mailbox_task_email_header`;
CREATE TABLE `mailbox_task_email_header`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `task_id` bigint(20) NOT NULL COMMENT '任务ID',
  `uid` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮件唯一id，相对于账户的唯一id',
  `fromer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发件人',
  `receiver` varchar(3000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收件人JSON，按name=>value,email=>value的键值对形式存入',
  `bcc` varchar(3000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密送人JSON，格式和收件人一样',
  `cc` varchar(3000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '抄送人JSON，格式和收件人一样',
  `folder` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属文件夹',
  `has_read` tinyint(1) NULL DEFAULT NULL COMMENT '是否已读 1 表示是，0 表示否',
  `has_attachment` tinyint(1) NULL DEFAULT NULL COMMENT '是否包含附件 1 表示是，0 表示否',
  `send_date` datetime(0) NULL DEFAULT NULL COMMENT '发送日期',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属邮箱',
  `title` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱标题',
  `eml_path` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '原始邮件存储路径',
  `has_top` tinyint(1) NULL DEFAULT NULL COMMENT '是否为置顶文件 1表示是，0表示否',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '入库时间',
  `modified` datetime(0) NULL DEFAULT NULL COMMENT '修改时间，冗余字段',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '所属用户',
  `creator` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属用户名，冗余字段，提高查询性能',
  `removed` tinyint(2) NULL DEFAULT NULL COMMENT '逻辑删除 1-是 0-否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1103 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '邮件头' ROW_FORMAT = Compact;
