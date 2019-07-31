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

 Date: 31/07/2019 16:23:45
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for jobs_info
-- ----------------------------
DROP TABLE IF EXISTS `jobs_info`;
CREATE TABLE `jobs_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(100) DEFAULT NULL COMMENT '租户ID',
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
  `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '0、启用 1、已禁用',
  `update_time` bigint(20) DEFAULT NULL COMMENT '更新时间',
  `create_time` bigint(20) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='任务信息';

-- ----------------------------
-- Records of jobs_info
-- ----------------------------
BEGIN;
INSERT INTO `jobs_info` VALUES (3, NULL, 'jobs-executor-sample', '0/10 * * * * ? *', 'demoJobHandler', NULL, 30, 3, 1564539320000, 1564539330000, 'jobs', '测试', 0, 1563152000000, 1563152000000);
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
) ENGINE=InnoDB AUTO_INCREMENT=241201 DEFAULT CHARSET=utf8 COMMENT='任务锁';

-- ----------------------------
-- Table structure for jobs_log
-- ----------------------------
DROP TABLE IF EXISTS `jobs_log`;
CREATE TABLE `jobs_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `job_id` bigint(20) NOT NULL COMMENT '任务ID',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '执行地址',
  `handler` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务 handler',
  `param` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '任务参数',
  `fail_retry_count` int(11) NOT NULL DEFAULT '0' COMMENT '失败重试次数',
  `trigger_code` int(11) NOT NULL DEFAULT '0' COMMENT '触发器调度返回码',
  `trigger_type` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '触发器调度类型',
  `trigger_msg` text COMMENT '触发器调度返回信息',
  `create_time` bigint(20) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2091 DEFAULT CHARSET=utf8 COMMENT='任务调度日志';

-- ----------------------------
-- Table structure for jobs_registry
-- ----------------------------
DROP TABLE IF EXISTS `jobs_registry`;
CREATE TABLE `jobs_registry` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
  `app` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '服务名',
  `address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'IP 地址',
  `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '0、启用 1、已禁用',
  `update_time` bigint(20) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=208 DEFAULT CHARSET=utf8 COMMENT='任务注册信息';

-- ----------------------------
-- Records of jobs_registry
-- ----------------------------
BEGIN;
INSERT INTO `jobs_registry` VALUES (1, 'jobs-executor-sample', '127.0.0.1:9999', 1, 1563626092397);
INSERT INTO `jobs_registry` VALUES (205, 'jobs-executor-sample', '192.168.0.6:9999', 1, 1563794040004);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
