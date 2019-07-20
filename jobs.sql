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

 Date: 20/07/2019 17:22:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for jobs_info
-- ----------------------------
DROP TABLE IF EXISTS `jobs_info`;
CREATE TABLE `jobs_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `app` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '服务名',
  `cron` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务执行CRON',
  `handler` varchar(255) DEFAULT NULL COMMENT '执行器任务handler',
  `param` varchar(512) DEFAULT NULL COMMENT '执行器任务参数',
  `timeout` int(11) NOT NULL DEFAULT '0' COMMENT '任务执行超时时间，单位秒',
  `fail_retry_count` int(11) NOT NULL DEFAULT '0' COMMENT '失败重试次数',
  `last_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '上次调度时间',
  `next_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '下次调度时间',
  `author` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '作者',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '状态：0、运行 1、停止',
  `update_time` bigint(20) DEFAULT NULL COMMENT '更新时间',
  `create_time` bigint(20) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='任务信息';

-- ----------------------------
-- Records of jobs_info
-- ----------------------------
BEGIN;
INSERT INTO `jobs_info` VALUES (3, 'jobs-executor-sample', '0/10 * * * * ? *', 'demoJobHandler', NULL, 30, 3, 1563614120000, 1563614130000, 'jobs', '测试', 0, 1563152000000, 1563152000000);
COMMIT;

-- ----------------------------
-- Table structure for jobs_lock
-- ----------------------------
DROP TABLE IF EXISTS `jobs_lock`;
CREATE TABLE `jobs_lock` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
  `owner` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '持有者',
  `create_time` bigint(20) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx_name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=53427 DEFAULT CHARSET=utf8 COMMENT='任务锁';

-- ----------------------------
-- Table structure for jobs_log
-- ----------------------------
DROP TABLE IF EXISTS `jobs_log`;
CREATE TABLE `jobs_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `job_id` bigint(20) NOT NULL COMMENT '任务ID',
  `executor_address` varchar(255) DEFAULT NULL COMMENT '执行器地址，本次执行的地址',
  `executor_handler` varchar(255) DEFAULT NULL COMMENT '执行器任务handler',
  `executor_param` varchar(512) DEFAULT NULL COMMENT '执行器任务参数',
  `executor_fail_retry_count` int(11) NOT NULL DEFAULT '0' COMMENT '失败重试次数',
  `trigger_time` bigint(20) DEFAULT NULL COMMENT '调度-时间',
  `trigger_code` int(11) NOT NULL DEFAULT '0' COMMENT '调度-结果',
  `trigger_msg` text COMMENT '调度-日志',
  `handle_time` bigint(20) DEFAULT NULL COMMENT '执行-时间',
  `handle_code` int(11) NOT NULL DEFAULT '0' COMMENT '执行-状态',
  `handle_msg` text COMMENT '执行-日志',
  `create_time` bigint(20) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `I_trigger_time` (`trigger_time`),
  KEY `I_handle_code` (`handle_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务调度日志';

-- ----------------------------
-- Table structure for jobs_registry
-- ----------------------------
DROP TABLE IF EXISTS `jobs_registry`;
CREATE TABLE `jobs_registry` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
  `app` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '服务名',
  `address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'IP 地址',
  `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '状态 0、正常 1、不可用',
  `update_time` bigint(20) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=204 DEFAULT CHARSET=utf8 COMMENT='任务注册信息';

SET FOREIGN_KEY_CHECKS = 1;
