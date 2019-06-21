/*
 Navicat Premium Data Transfer

 Source Server         : Mysql-1q4r
 Source Server Type    : MySQL
 Source Server Version : 80012
 Source Host           : localhost:3306
 Source Schema         : xxl-job

 Target Server Type    : MySQL
 Target Server Version : 80012
 File Encoding         : 65001

 Date: 18/06/2019 17:31:11
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for xxl_job_group
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_group`;
CREATE TABLE `xxl_job_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_name` varchar(64) NOT NULL COMMENT '执行器AppName',
  `title` varchar(12) NOT NULL COMMENT '执行器名称',
  `sort` tinyint(4) NOT NULL DEFAULT '0' COMMENT '排序',
  `address_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '执行器地址类型：0=自动注册、1=手动录入',
  `address_list` varchar(512) DEFAULT NULL COMMENT '执行器地址列表，多地址逗号分隔',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of xxl_job_group
-- ----------------------------
BEGIN;
INSERT INTO `xxl_job_group` VALUES (1, 'xxl-job-executor-sample', '示例执行器', 0, 0, '192.168.43.158:9999');
COMMIT;

-- ----------------------------
-- Table structure for xxl_job_info
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_info`;
CREATE TABLE `xxl_job_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_group` int(11) NOT NULL COMMENT '执行器主键ID',
  `job_cron` varchar(128) NOT NULL COMMENT '任务执行CRON',
  `job_desc` varchar(255) NOT NULL,
  `add_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `author` varchar(64) DEFAULT NULL COMMENT '作者',
  `alarm_email` varchar(255) DEFAULT NULL COMMENT '报警邮件',
  `executor_route_strategy` varchar(50) DEFAULT NULL COMMENT '执行器路由策略',
  `executor_handler` varchar(255) DEFAULT NULL COMMENT '执行器任务handler',
  `executor_param` varchar(512) DEFAULT NULL COMMENT '执行器任务参数',
  `executor_block_strategy` varchar(50) DEFAULT NULL COMMENT '阻塞处理策略',
  `executor_timeout` int(11) NOT NULL DEFAULT '0' COMMENT '任务执行超时时间，单位秒',
  `executor_fail_retry_count` int(11) NOT NULL DEFAULT '0' COMMENT '失败重试次数',
  `glue_type` varchar(50) NOT NULL COMMENT 'GLUE类型',
  `glue_source` mediumtext COMMENT 'GLUE源代码',
  `glue_remark` varchar(128) DEFAULT NULL COMMENT 'GLUE备注',
  `glue_updatetime` datetime DEFAULT NULL COMMENT 'GLUE更新时间',
  `child_jobid` varchar(255) DEFAULT NULL COMMENT '子任务ID，多个逗号分隔',
  `trigger_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '调度状态：0-停止，1-运行',
  `trigger_last_time` bigint(13) NOT NULL DEFAULT '0' COMMENT '上次调度时间',
  `trigger_next_time` bigint(13) NOT NULL DEFAULT '0' COMMENT '下次调度时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of xxl_job_info
-- ----------------------------
BEGIN;
INSERT INTO `xxl_job_info` VALUES (1, 1, '0 0 0 * * ? *', '测试任务1', '2018-11-03 22:21:31', '2019-05-29 10:46:14', 'XXL', '', 'FIRST', 'demoJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2018-11-03 22:21:31', '', 1, 1560787200000, 1560873600000);
COMMIT;

-- ----------------------------
-- Table structure for xxl_job_lock
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_lock`;
CREATE TABLE `xxl_job_lock` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
  `lock_name` varchar(50) DEFAULT NULL COMMENT '锁名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for xxl_job_log
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_log`;
CREATE TABLE `xxl_job_log` (
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
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of xxl_job_log
-- ----------------------------
BEGIN;
INSERT INTO `xxl_job_log` VALUES (3, 1, 1, NULL, NULL, NULL, NULL, 0, '2019-05-31 14:46:48', 0, NULL, NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (4, 1, 1, NULL, NULL, NULL, NULL, 0, '2019-05-31 14:46:51', 0, NULL, NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (5, 1, 1, NULL, NULL, NULL, NULL, 0, '2019-06-02 23:53:38', 0, NULL, NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (6, 1, 1, NULL, NULL, NULL, NULL, 0, '2019-06-13 23:44:43', 0, NULL, NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (7, 1, 1, NULL, NULL, NULL, NULL, 0, '2019-06-14 00:00:00', 0, NULL, NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (8, 1, 1, NULL, NULL, NULL, NULL, 0, '2019-06-14 00:01:07', 0, NULL, NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (9, 1, 1, NULL, NULL, NULL, NULL, 0, '2019-06-14 14:00:25', 0, NULL, NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (10, 1, 1, '192.168.0.37:9999', 'demoJobHandler', '77', NULL, 0, '2019-06-14 14:58:27', 200, '任务触发类型：手动触发<br>调度机器：192.168.0.37<br>执行器-注册方式：自动注册<br>执行器-地址列表：[192.168.0.37:9999]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：192.168.0.37:9999<br>code：200<br>msg：null', '2019-06-14 14:58:37', 200, '', 1);
INSERT INTO `xxl_job_log` VALUES (11, 1, 1, '192.168.0.37:9999', 'demoJobHandler', 'assa', NULL, 0, '2019-06-14 15:03:10', 200, '任务触发类型：手动触发<br>调度机器：192.168.0.37<br>执行器-注册方式：自动注册<br>执行器-地址列表：[192.168.0.37:9999]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：192.168.0.37:9999<br>code：200<br>msg：null', '2019-06-14 15:03:20', 200, '', 1);
INSERT INTO `xxl_job_log` VALUES (12, 1, 1, NULL, NULL, NULL, NULL, 0, '2019-06-14 15:05:00', 0, NULL, NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (13, 1, 1, '192.168.0.37:9999', 'demoJobHandler', 'ssss', NULL, 0, '2019-06-14 15:19:08', 200, '任务触发类型：手动触发<br>调度机器：192.168.0.37<br>执行器-注册方式：自动注册<br>执行器-地址列表：[192.168.0.37:9999]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：192.168.0.37:9999<br>code：200<br>msg：null', '2019-06-14 15:19:21', 200, '', 1);
INSERT INTO `xxl_job_log` VALUES (14, 1, 1, '192.168.0.37:9999', 'demoJobHandler', '', NULL, 0, '2019-06-14 15:20:28', 200, '任务触发类型：手动触发<br>调度机器：192.168.0.37<br>执行器-注册方式：自动注册<br>执行器-地址列表：[192.168.0.37:9999]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：192.168.0.37:9999<br>code：200<br>msg：null', '2019-06-14 15:20:38', 200, '', 1);
INSERT INTO `xxl_job_log` VALUES (15, 1, 1, NULL, NULL, NULL, NULL, 0, '2019-06-14 15:22:29', 0, NULL, NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (16, 1, 1, '192.168.0.37:9999', 'demoJobHandler', '', NULL, 0, '2019-06-14 15:31:54', 200, '任务触发类型：手动触发<br>调度机器：192.168.0.37<br>执行器-注册方式：自动注册<br>执行器-地址列表：[192.168.0.37:9999]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：192.168.0.37:9999<br>code：200<br>msg：null', '2019-06-14 15:32:14', 200, '', 1);
INSERT INTO `xxl_job_log` VALUES (17, 1, 1, '192.168.0.37:9999', 'demoJobHandler', '', NULL, 0, '2019-06-14 15:32:57', 200, '任务触发类型：手动触发<br>调度机器：192.168.0.37<br>执行器-注册方式：自动注册<br>执行器-地址列表：[192.168.0.37:9999]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：192.168.0.37:9999<br>code：200<br>msg：null', '2019-06-14 15:33:07', 200, '', 1);
INSERT INTO `xxl_job_log` VALUES (18, 1, 1, '192.168.0.37:9999', 'demoJobHandler', '', NULL, 0, '2019-06-14 15:33:51', 200, '任务触发类型：手动触发<br>调度机器：192.168.0.37<br>执行器-注册方式：自动注册<br>执行器-地址列表：[192.168.0.37:9999]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：192.168.0.37:9999<br>code：200<br>msg：null', '2019-06-14 15:34:01', 200, '', 1);
INSERT INTO `xxl_job_log` VALUES (19, 1, 1, '192.168.43.158:9999', 'demoJobHandler', '', NULL, 0, '2019-06-16 00:00:00', 500, '任务触发类型：Cron触发<br>调度机器：192.168.43.158<br>执行器-注册方式：自动注册<br>执行器-地址列表：[192.168.43.158:9999]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：192.168.43.158:9999<br>code：500<br>msg：com.xxl.rpc.util.XxlRpcException: io.netty.channel.ConnectTimeoutException: connection timed out: /192.168.43.158:9999\n	at com.xxl.rpc.remoting.invoker.reference.XxlRpcReferenceBean$1.invoke(XxlRpcReferenceBean.java:227)\n	at com.sun.proxy.$Proxy95.run(Unknown Source)\n	at xxl.job.admin.trigger.JobTrigger.runExecutor(JobTrigger.java:198)\n	at xxl.job.admin.trigger.JobTrigger.processTrigger(JobTrigger.java:150)\n	at xxl.job.admin.trigger.JobTrigger.trigger(JobTrigger.java:71)\n	at xxl.job.admin.trigger.JobTriggerPool$3.run(JobTriggerPool.java:77)\n	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)\n	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)\n	at java.lang.Thread.run(Thread.java:745)\nCaused by: io.netty.channel.ConnectTimeoutException: connection timed out: /192.168.43.158:9999\n	at io.netty.channel.nio.AbstractNioChannel$AbstractNioUnsafe$1.run(AbstractNioChannel.java:267)\n	at io.netty.util.concurrent.PromiseTask$RunnableAdapter.call(PromiseTask.java:38)\n	at io.netty.util.concurrent.ScheduledFutureTask.run(ScheduledFutureTask.java:127)\n	at io.netty.util.concurrent.AbstractEventExecutor.safeExecute$$$capture(AbstractEventExecutor.java:163)\n	at io.netty.util.concurrent.AbstractEventExecutor.safeExecute(AbstractEventExecutor.java)\n	at io.netty.util.concurrent.SingleThreadEventExecutor.runAllTasks(SingleThreadEventExecutor.java:405)\n	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:500)\n	at io.netty.util.concurrent.SingleThreadEventExecutor$5.run(SingleThreadEventExecutor.java:906)\n	at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)\n	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)\n	... 1 more\n', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (20, 1, 1, '192.168.43.158:9999', 'demoJobHandler', '', NULL, 0, '2019-06-16 01:24:02', 500, '任务触发类型：手动触发<br>调度机器：192.168.0.6<br>执行器-注册方式：自动注册<br>执行器-地址列表：[192.168.43.158:9999]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：192.168.43.158:9999<br>code：500<br>msg：com.xxl.rpc.util.XxlRpcException: io.netty.channel.ConnectTimeoutException: connection timed out: /192.168.43.158:9999\n	at com.xxl.rpc.remoting.invoker.reference.XxlRpcReferenceBean$1.invoke(XxlRpcReferenceBean.java:227)\n	at com.sun.proxy.$Proxy95.run(Unknown Source)\n	at xxl.job.admin.trigger.JobTrigger.runExecutor(JobTrigger.java:200)\n	at xxl.job.admin.trigger.JobTrigger.processTrigger(JobTrigger.java:152)\n	at xxl.job.admin.trigger.JobTrigger.trigger(JobTrigger.java:73)\n	at xxl.job.admin.trigger.JobTriggerPool$3.run(JobTriggerPool.java:77)\n	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)\n	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)\n	at java.lang.Thread.run(Thread.java:745)\nCaused by: io.netty.channel.ConnectTimeoutException: connection timed out: /192.168.43.158:9999\n	at io.netty.channel.nio.AbstractNioChannel$AbstractNioUnsafe$1.run(AbstractNioChannel.java:267)\n	at io.netty.util.concurrent.PromiseTask$RunnableAdapter.call(PromiseTask.java:38)\n	at io.netty.util.concurrent.ScheduledFutureTask.run(ScheduledFutureTask.java:127)\n	at io.netty.util.concurrent.AbstractEventExecutor.safeExecute$$$capture(AbstractEventExecutor.java:163)\n	at io.netty.util.concurrent.AbstractEventExecutor.safeExecute(AbstractEventExecutor.java)\n	at io.netty.util.concurrent.SingleThreadEventExecutor.runAllTasks(SingleThreadEventExecutor.java:405)\n	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:500)\n	at io.netty.util.concurrent.SingleThreadEventExecutor$5.run(SingleThreadEventExecutor.java:906)\n	at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)\n	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)\n	... 1 more\n', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (21, 1, 1, '192.168.43.158:9999', 'demoJobHandler', '', NULL, 0, '2019-06-17 00:00:00', 500, '任务触发类型：Cron触发<br>调度机器：192.168.0.6<br>执行器-注册方式：自动注册<br>执行器-地址列表：[192.168.43.158:9999]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：192.168.43.158:9999<br>code：500<br>msg：com.xxl.rpc.util.XxlRpcException: io.netty.channel.ConnectTimeoutException: connection timed out: /192.168.43.158:9999\n	at com.xxl.rpc.remoting.invoker.reference.XxlRpcReferenceBean$1.invoke(XxlRpcReferenceBean.java:227)\n	at com.sun.proxy.$Proxy93.run(Unknown Source)\n	at xxl.job.admin.trigger.JobTrigger.runExecutor(JobTrigger.java:200)\n	at xxl.job.admin.trigger.JobTrigger.processTrigger(JobTrigger.java:152)\n	at xxl.job.admin.trigger.JobTrigger.trigger(JobTrigger.java:73)\n	at xxl.job.admin.trigger.JobTriggerPool$3.run(JobTriggerPool.java:77)\n	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)\n	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)\n	at java.lang.Thread.run(Thread.java:745)\nCaused by: io.netty.channel.ConnectTimeoutException: connection timed out: /192.168.43.158:9999\n	at io.netty.channel.nio.AbstractNioChannel$AbstractNioUnsafe$1.run(AbstractNioChannel.java:267)\n	at io.netty.util.concurrent.PromiseTask$RunnableAdapter.call(PromiseTask.java:38)\n	at io.netty.util.concurrent.ScheduledFutureTask.run(ScheduledFutureTask.java:127)\n	at io.netty.util.concurrent.AbstractEventExecutor.safeExecute$$$capture(AbstractEventExecutor.java:163)\n	at io.netty.util.concurrent.AbstractEventExecutor.safeExecute(AbstractEventExecutor.java)\n	at io.netty.util.concurrent.SingleThreadEventExecutor.runAllTasks(SingleThreadEventExecutor.java:405)\n	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:500)\n	at io.netty.util.concurrent.SingleThreadEventExecutor$5.run(SingleThreadEventExecutor.java:906)\n	at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)\n	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)\n	... 1 more\n', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (22, 1, 1, '192.168.43.158:9999', 'demoJobHandler', '', NULL, 0, '2019-06-18 16:51:56', 500, '任务触发类型：手动触发<br>调度机器：192.168.0.37<br>执行器-注册方式：自动注册<br>执行器-地址列表：[192.168.43.158:9999]<br>路由策略：第一个<br>阻塞处理策略：Serial execution<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：192.168.43.158:9999<br>code：500<br>msg：com.xxl.rpc.util.XxlRpcException: io.netty.channel.ConnectTimeoutException: connection timed out: /192.168.43.158:9999\n	at com.xxl.rpc.remoting.invoker.reference.XxlRpcReferenceBean$1.invoke(XxlRpcReferenceBean.java:227)\n	at com.sun.proxy.$Proxy87.run(Unknown Source)\n	at xxl.job.web.trigger.XxlJobTrigger.runExecutor(XxlJobTrigger.java:202)\n	at xxl.job.web.trigger.XxlJobTrigger.processTrigger(XxlJobTrigger.java:155)\n	at xxl.job.web.trigger.XxlJobTrigger.trigger(XxlJobTrigger.java:75)\n	at xxl.job.web.trigger.XxlJobTriggerPool$3.run(XxlJobTriggerPool.java:77)\n	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)\n	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)\n	at java.lang.Thread.run(Thread.java:745)\nCaused by: io.netty.channel.ConnectTimeoutException: connection timed out: /192.168.43.158:9999\n	at io.netty.channel.nio.AbstractNioChannel$AbstractNioUnsafe$1.run(AbstractNioChannel.java:267)\n	at io.netty.util.concurrent.PromiseTask$RunnableAdapter.call(PromiseTask.java:38)\n	at io.netty.util.concurrent.ScheduledFutureTask.run(ScheduledFutureTask.java:127)\n	at io.netty.util.concurrent.AbstractEventExecutor.safeExecute$$$capture(AbstractEventExecutor.java:163)\n	at io.netty.util.concurrent.AbstractEventExecutor.safeExecute(AbstractEventExecutor.java)\n	at io.netty.util.concurrent.SingleThreadEventExecutor.runAllTasks(SingleThreadEventExecutor.java:405)\n	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:500)\n	at io.netty.util.concurrent.SingleThreadEventExecutor$5.run(SingleThreadEventExecutor.java:906)\n	at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)\n	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)\n	... 1 more\n', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (23, 1, 1, NULL, NULL, NULL, NULL, 0, '2019-06-18 16:53:29', 0, NULL, NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (24, 1, 1, '192.168.43.158:9999', 'demoJobHandler', '', NULL, 0, '2019-06-18 16:56:19', 500, '任务触发类型：手动触发<br>调度机器：192.168.0.37<br>执行器-注册方式：自动注册<br>执行器-地址列表：[192.168.43.158:9999]<br>路由策略：第一个<br>阻塞处理策略：Serial execution<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：192.168.43.158:9999<br>code：500<br>msg：com.xxl.rpc.util.XxlRpcException: io.netty.channel.ConnectTimeoutException: connection timed out: /192.168.43.158:9999\n	at com.xxl.rpc.remoting.invoker.reference.XxlRpcReferenceBean$1.invoke(XxlRpcReferenceBean.java:227)\n	at com.sun.proxy.$Proxy87.run(Unknown Source)\n	at xxl.job.web.trigger.XxlJobTrigger.runExecutor(XxlJobTrigger.java:202)\n	at xxl.job.web.trigger.XxlJobTrigger.processTrigger(XxlJobTrigger.java:155)\n	at xxl.job.web.trigger.XxlJobTrigger.trigger(XxlJobTrigger.java:75)\n	at xxl.job.web.trigger.XxlJobTriggerPool$3.run(XxlJobTriggerPool.java:77)\n	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)\n	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)\n	at java.lang.Thread.run(Thread.java:745)\nCaused by: io.netty.channel.ConnectTimeoutException: connection timed out: /192.168.43.158:9999\n	at io.netty.channel.nio.AbstractNioChannel$AbstractNioUnsafe$1.run(AbstractNioChannel.java:267)\n	at io.netty.util.concurrent.PromiseTask$RunnableAdapter.call(PromiseTask.java:38)\n	at io.netty.util.concurrent.ScheduledFutureTask.run(ScheduledFutureTask.java:127)\n	at io.netty.util.concurrent.AbstractEventExecutor.safeExecute$$$capture(AbstractEventExecutor.java:163)\n	at io.netty.util.concurrent.AbstractEventExecutor.safeExecute(AbstractEventExecutor.java)\n	at io.netty.util.concurrent.SingleThreadEventExecutor.runAllTasks(SingleThreadEventExecutor.java:405)\n	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:500)\n	at io.netty.util.concurrent.SingleThreadEventExecutor$5.run(SingleThreadEventExecutor.java:906)\n	at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)\n	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)\n	... 1 more\n', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (25, 1, 1, '192.168.43.158:9999', 'demoJobHandler', '', NULL, 0, '2019-06-18 16:57:11', 500, '任务触发类型：手动触发<br>调度机器：192.168.0.37<br>执行器-注册方式：自动注册<br>执行器-地址列表：[192.168.43.158:9999]<br>路由策略：第一个<br>阻塞处理策略：Serial execution<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：192.168.43.158:9999<br>code：500<br>msg：com.xxl.rpc.util.XxlRpcException: io.netty.channel.ConnectTimeoutException: connection timed out: /192.168.43.158:9999\n	at com.xxl.rpc.remoting.invoker.reference.XxlRpcReferenceBean$1.invoke(XxlRpcReferenceBean.java:227)\n	at com.sun.proxy.$Proxy87.run(Unknown Source)\n	at xxl.job.web.trigger.XxlJobTrigger.runExecutor(XxlJobTrigger.java:202)\n	at xxl.job.web.trigger.XxlJobTrigger.processTrigger(XxlJobTrigger.java:155)\n	at xxl.job.web.trigger.XxlJobTrigger.trigger(XxlJobTrigger.java:75)\n	at xxl.job.web.trigger.XxlJobTriggerPool$3.run(XxlJobTriggerPool.java:77)\n	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)\n	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)\n	at java.lang.Thread.run(Thread.java:745)\nCaused by: io.netty.channel.ConnectTimeoutException: connection timed out: /192.168.43.158:9999\n	at io.netty.channel.nio.AbstractNioChannel$AbstractNioUnsafe$1.run(AbstractNioChannel.java:267)\n	at io.netty.util.concurrent.PromiseTask$RunnableAdapter.call(PromiseTask.java:38)\n	at io.netty.util.concurrent.ScheduledFutureTask.run(ScheduledFutureTask.java:127)\n	at io.netty.util.concurrent.AbstractEventExecutor.safeExecute$$$capture(AbstractEventExecutor.java:163)\n	at io.netty.util.concurrent.AbstractEventExecutor.safeExecute(AbstractEventExecutor.java)\n	at io.netty.util.concurrent.SingleThreadEventExecutor.runAllTasks(SingleThreadEventExecutor.java:405)\n	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:500)\n	at io.netty.util.concurrent.SingleThreadEventExecutor$5.run(SingleThreadEventExecutor.java:906)\n	at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)\n	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)\n	... 1 more\n', NULL, 0, NULL, 1);
INSERT INTO `xxl_job_log` VALUES (26, 1, 1, '192.168.43.158:9999', 'demoJobHandler', '', NULL, 0, '2019-06-18 16:57:43', 500, '任务触发类型：手动触发<br>调度机器：192.168.0.37<br>执行器-注册方式：自动注册<br>执行器-地址列表：[192.168.43.158:9999]<br>路由策略：第一个<br>阻塞处理策略：Serial execution<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：192.168.43.158:9999<br>code：500<br>msg：com.xxl.rpc.util.XxlRpcException: java.lang.InterruptedException\n	at com.xxl.rpc.remoting.invoker.reference.XxlRpcReferenceBean$1.invoke(XxlRpcReferenceBean.java:227)\n	at com.sun.proxy.$Proxy87.run(Unknown Source)\n	at xxl.job.web.trigger.XxlJobTrigger.runExecutor(XxlJobTrigger.java:202)\n	at xxl.job.web.trigger.XxlJobTrigger.processTrigger(XxlJobTrigger.java:155)\n	at xxl.job.web.trigger.XxlJobTrigger.trigger(XxlJobTrigger.java:75)\n	at xxl.job.web.trigger.XxlJobTriggerPool$3.run(XxlJobTriggerPool.java:77)\n	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)\n	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)\n	at java.lang.Thread.run(Thread.java:745)\nCaused by: java.lang.InterruptedException\n	at java.lang.Object.wait(Native Method)\n	at java.lang.Object.wait(Object.java:502)\n	at io.netty.util.concurrent.DefaultPromise.await(DefaultPromise.java:221)\n	at io.netty.channel.DefaultChannelPromise.await(DefaultChannelPromise.java:131)\n	at io.netty.channel.DefaultChannelPromise.await(DefaultChannelPromise.java:30)\n	at io.netty.util.concurrent.DefaultPromise.sync(DefaultPromise.java:328)\n	at io.netty.channel.DefaultChannelPromise.sync(DefaultChannelPromise.java:119)\n	at io.netty.channel.DefaultChannelPromise.sync(DefaultChannelPromise.java:30)\n	at com.xxl.rpc.remoting.net.impl.netty_http.client.NettyHttpConnectClient.init(NettyHttpConnectClient.java:64)\n	at com.xxl.rpc.remoting.net.common.ConnectClient.getPool(ConnectClient.java:110)\n	at com.xxl.rpc.remoting.net.common.ConnectClient.asyncSend(ConnectClient.java:41)\n	at com.xxl.rpc.remoting.net.impl.netty_http.client.NettyHttpClient.asyncSend(NettyHttpClient.java:18)\n	at com.xxl.rpc.remoting.invoker.reference.XxlRpcReferenceBean$1.invoke(XxlRpcReferenceBean.java:216)\n	... 8 more\n', NULL, 0, NULL, 1);
COMMIT;

-- ----------------------------
-- Table structure for xxl_job_logglue
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_logglue`;
CREATE TABLE `xxl_job_logglue` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_id` int(11) NOT NULL COMMENT '任务，主键ID',
  `glue_type` varchar(50) DEFAULT NULL COMMENT 'GLUE类型',
  `glue_source` mediumtext COMMENT 'GLUE源代码',
  `glue_remark` varchar(128) NOT NULL COMMENT 'GLUE备注',
  `add_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for xxl_job_registry
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_registry`;
CREATE TABLE `xxl_job_registry` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `registry_group` varchar(255) NOT NULL,
  `registry_key` varchar(255) NOT NULL,
  `registry_value` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `i_g_k_v` (`registry_group`,`registry_key`,`registry_value`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of xxl_job_registry
-- ----------------------------
BEGIN;
INSERT INTO `xxl_job_registry` VALUES (41, 'EXECUTOR', 'xxl-job-executor-sample', '192.168.43.158:9999', '2019-06-17 23:32:39');
INSERT INTO `xxl_job_registry` VALUES (43, 'EXECUTOR', 'xxl-job-executor-sample', '192.168.0.37:9999', '2019-06-18 16:57:54');
COMMIT;

-- ----------------------------
-- Table structure for xxl_job_user
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_user`;
CREATE TABLE `xxl_job_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '账号',
  `password` varchar(50) NOT NULL COMMENT '密码',
  `role` tinyint(4) NOT NULL COMMENT '角色：0-普通用户、1-管理员',
  `permission` varchar(255) DEFAULT NULL COMMENT '权限：执行器ID列表，多个逗号分割',
  PRIMARY KEY (`id`),
  UNIQUE KEY `i_username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of xxl_job_user
-- ----------------------------
BEGIN;
INSERT INTO `xxl_job_user` VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', 1, NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
