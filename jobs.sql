/*
 Navicat Premium Data Transfer

 Source Server         : Mysql-1q4r
 Source Server Type    : MySQL
 Source Server Version : 80012
 Source Host           : localhost:3306
 Source Schema         : jobs

 Target Server Type    : MySQL
 Target Server Version : 80012
 File Encoding         : 65001

 Date: 14/07/2019 23:12:05
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for jobs_info
-- ----------------------------
DROP TABLE IF EXISTS `jobs_info`;
CREATE TABLE `jobs_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '服务名',
  `cron` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务执行CRON',
  `handler` varchar(255) DEFAULT NULL COMMENT '执行器任务handler',
  `param` varchar(512) DEFAULT NULL COMMENT '执行器任务参数',
  `route_strategy` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '执行器路由策略',
  `block_strategy` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '阻塞处理策略',
  `timeout` int(11) NOT NULL DEFAULT '0' COMMENT '任务执行超时时间，单位秒',
  `fail_retry_count` int(11) NOT NULL DEFAULT '0' COMMENT '失败重试次数',
  `last_time` bigint(13) NOT NULL DEFAULT '0' COMMENT '上次调度时间',
  `next_time` bigint(13) NOT NULL DEFAULT '0' COMMENT '下次调度时间',
  `author` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '作者',
  `alarm_email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '报警邮件',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '状态：0、运行 1、停止',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='任务信息';

-- ----------------------------
-- Records of jobs_info
-- ----------------------------
BEGIN;
INSERT INTO `jobs_info` VALUES (3, 'jobs-executor-sample', '0/10 * * * * ? *', 'demoJobHandler', NULL, 'FIRST', 'SERIAL_EXECUTION', 30, 3, 1563116580000, 1563116590000, 'jobs', 'FIRST', '测试', 0, '2019-07-12 18:12:23', '2019-07-12 18:12:25');
COMMIT;

-- ----------------------------
-- Table structure for jobs_lock
-- ----------------------------
DROP TABLE IF EXISTS `jobs_lock`;
CREATE TABLE `jobs_lock` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `owner` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '持有者',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx_name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=367 DEFAULT CHARSET=utf8 COMMENT='任务锁';

-- ----------------------------
-- Table structure for jobs_log
-- ----------------------------
DROP TABLE IF EXISTS `jobs_log`;
CREATE TABLE `jobs_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_group` int(11) NOT NULL COMMENT '执行器主键ID',
  `job_id` int(11) NOT NULL COMMENT '任务，主键ID',
  `executor_address` varchar(255) DEFAULT NULL COMMENT '执行器地址，本次执行的地址',
  `executor_handler` varchar(255) DEFAULT NULL COMMENT '执行器任务handler',
  `executor_param` varchar(512) DEFAULT NULL COMMENT '执行器任务参数',
  `executor_sharding_param` varchar(20) DEFAULT NULL COMMENT '执行器任务分片参数，格式如 1/2',
  `executor_fail_retry_count` int(11) NOT NULL DEFAULT '0' COMMENT '失败重试次数',
  `trigger_time` datetime DEFAULT NULL COMMENT '调度-时间',
  `trigger_code` int(11) NOT NULL DEFAULT '0' COMMENT '调度-结果',
  `trigger_msg` text COMMENT '调度-日志',
  `handle_time` datetime DEFAULT NULL COMMENT '执行-时间',
  `handle_code` int(11) NOT NULL DEFAULT '0' COMMENT '执行-状态',
  `handle_msg` text COMMENT '执行-日志',
  `alarm_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '告警状态：0-默认、1-无需告警、2-告警成功、3-告警失败',
  PRIMARY KEY (`id`),
  KEY `I_trigger_time` (`trigger_time`),
  KEY `I_handle_code` (`handle_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务调度日志';

-- ----------------------------
-- Table structure for jobs_registry
-- ----------------------------
DROP TABLE IF EXISTS `jobs_registry`;
CREATE TABLE `jobs_registry` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
  `app` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '服务名',
  `address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'IP 地址',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=902 DEFAULT CHARSET=utf8 COMMENT='任务注册信息';

SET FOREIGN_KEY_CHECKS = 1;
