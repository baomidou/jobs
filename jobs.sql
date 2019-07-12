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

 Date: 13/07/2019 00:39:49
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
  `glue_type` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'GLUE类型',
  `glue_source` mediumtext COMMENT 'GLUE源代码',
  `glue_remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'GLUE备注',
  `glue_time` datetime DEFAULT NULL COMMENT 'GLUE更新时间',
  `last_time` bigint(13) NOT NULL DEFAULT '0' COMMENT '上次调度时间',
  `next_time` bigint(13) NOT NULL DEFAULT '0' COMMENT '下次调度时间',
  `author` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '作者',
  `alarm_email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '报警邮件',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '状态：0、运行 1、停止',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jobs_info
-- ----------------------------
BEGIN;
INSERT INTO `jobs_info` VALUES (3, 'jobs-executor-sample', '0/10 * * * * ? *', 'demoJobHandler', NULL, 'FIRST', 'SERIAL_EXECUTION', 10, 3, 'BEAN', 'GLUE代码初始化', NULL, NULL, 1562949580000, 1562949590000, 'jobs', 'FIRST', '测试', 0, '2019-07-12 18:12:23', '2019-07-12 18:12:25');
COMMIT;

-- ----------------------------
-- Table structure for jobs_lock
-- ----------------------------
DROP TABLE IF EXISTS `jobs_lock`;
CREATE TABLE `jobs_lock` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
  `lock_name` varchar(50) DEFAULT NULL COMMENT '锁名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jobs_log
-- ----------------------------
BEGIN;
INSERT INTO `jobs_log` VALUES (1, 1, 3, NULL, NULL, NULL, NULL, 0, '2019-07-12 10:39:44', 0, NULL, NULL, 0, NULL, 0);
INSERT INTO `jobs_log` VALUES (2, 1, 3, NULL, NULL, NULL, NULL, 0, '2019-07-12 10:52:44', 0, NULL, NULL, 0, NULL, 0);
INSERT INTO `jobs_log` VALUES (3, 1, 3, NULL, NULL, NULL, NULL, 0, '2019-07-12 11:22:16', 0, NULL, NULL, 0, NULL, 0);
INSERT INTO `jobs_log` VALUES (4, 1, 3, NULL, NULL, NULL, NULL, 0, '2019-07-12 11:30:40', 0, NULL, NULL, 0, NULL, 0);
INSERT INTO `jobs_log` VALUES (5, 1, 3, NULL, NULL, NULL, NULL, 0, '2019-07-12 11:35:04', 0, NULL, NULL, 0, NULL, 0);
INSERT INTO `jobs_log` VALUES (6, 1, 3, '192.168.0.2:9999', 'demoJobHandler', NULL, NULL, 3, '2019-07-12 11:37:43', -1, '任务触发类型：Cron触发,调度机器：192.168.0.2,执行器-地址列表：[192.168.0.2:9999],路由策略：第一个,阻塞处理策略：Serial execution,任务超时时间：10,失败重试次数：3,触发调度：执行成功触发调度：,address：192.168.0.2:9999,code：-1,msg：com.xxl.rpc.util.XxlRpcException: io.netty.channel.AbstractChannel$AnnotatedConnectException: Connection refused: /192.168.0.2:9999\n	at com.xxl.rpc.remoting.invoker.reference.XxlRpcReferenceBean$1.invoke(XxlRpcReferenceBean.java:227)\n	at com.sun.proxy.$Proxy85.run(Unknown Source)\n	at com.baomidou.jobs.starter.trigger.JobsTrigger.runExecutor(JobsTrigger.java:156)\n	at com.baomidou.jobs.starter.trigger.JobsTrigger.processTrigger(JobsTrigger.java:115)\n	at com.baomidou.jobs.starter.trigger.JobsTrigger.trigger(JobsTrigger.java:53)\n	at com.baomidou.jobs.starter.disruptor.JobsEventHandler.onEvent(JobsEventHandler.java:34)\n	at com.baomidou.jobs.starter.disruptor.JobsEventHandler.onEvent(JobsEventHandler.java:17)\n	at com.lmax.disruptor.BatchEventProcessor.processEvents(BatchEventProcessor.java:168)\n	at com.lmax.disruptor.BatchEventProcessor.run(BatchEventProcessor.java:125)\n	at java.lang.Thread.run(Thread.java:745)\nCaused by: io.netty.channel.AbstractChannel$AnnotatedConnectException: Connection refused: /192.168.0.2:9999\n	at sun.nio.ch.SocketChannelImpl.checkConnect(Native Method)\n	at sun.nio.ch.SocketChannelImpl.finishConnect(SocketChannelImpl.java:712)\n	at io.netty.channel.socket.nio.NioSocketChannel.doFinishConnect(NioSocketChannel.java:327)\n	at io.netty.channel.nio.AbstractNioChannel$AbstractNioUnsafe.finishConnect(AbstractNioChannel.java:340)\n	at io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:670)\n	at io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:617)\n	at io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:534)\n	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:496)\n	at io.netty.util.concurrent.SingleThreadEventExecutor$5.run(SingleThreadEventExecutor.java:906)\n	at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)\n	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)\n	... 1 more\nCaused by: java.net.ConnectException: Connection refused\n	... 12 more\n', NULL, 0, NULL, 0);
INSERT INTO `jobs_log` VALUES (7, 1, 3, '192.168.0.2:9999', 'demoJobHandler', NULL, NULL, 3, '2019-07-12 11:38:09', -1, '任务触发类型：Cron触发,调度机器：192.168.0.2,执行器-地址列表：[192.168.0.2:9999],路由策略：第一个,阻塞处理策略：Serial execution,任务超时时间：10,失败重试次数：3,触发调度：执行成功触发调度：,address：192.168.0.2:9999,code：-1,msg：com.xxl.rpc.util.XxlRpcException: io.netty.channel.AbstractChannel$AnnotatedConnectException: Connection refused: /192.168.0.2:9999\n	at com.xxl.rpc.remoting.invoker.reference.XxlRpcReferenceBean$1.invoke(XxlRpcReferenceBean.java:227)\n	at com.sun.proxy.$Proxy85.run(Unknown Source)\n	at com.baomidou.jobs.starter.trigger.JobsTrigger.runExecutor(JobsTrigger.java:156)\n	at com.baomidou.jobs.starter.trigger.JobsTrigger.processTrigger(JobsTrigger.java:115)\n	at com.baomidou.jobs.starter.trigger.JobsTrigger.trigger(JobsTrigger.java:53)\n	at com.baomidou.jobs.starter.disruptor.JobsEventHandler.onEvent(JobsEventHandler.java:34)\n	at com.baomidou.jobs.starter.disruptor.JobsEventHandler.onEvent(JobsEventHandler.java:17)\n	at com.lmax.disruptor.BatchEventProcessor.processEvents(BatchEventProcessor.java:168)\n	at com.lmax.disruptor.BatchEventProcessor.run(BatchEventProcessor.java:125)\n	at java.lang.Thread.run(Thread.java:745)\nCaused by: io.netty.channel.AbstractChannel$AnnotatedConnectException: Connection refused: /192.168.0.2:9999\n	at sun.nio.ch.SocketChannelImpl.checkConnect(Native Method)\n	at sun.nio.ch.SocketChannelImpl.finishConnect(SocketChannelImpl.java:712)\n	at io.netty.channel.socket.nio.NioSocketChannel.doFinishConnect(NioSocketChannel.java:327)\n	at io.netty.channel.nio.AbstractNioChannel$AbstractNioUnsafe.finishConnect(AbstractNioChannel.java:340)\n	at io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:670)\n	at io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:617)\n	at io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:534)\n	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:496)\n	at io.netty.util.concurrent.SingleThreadEventExecutor$5.run(SingleThreadEventExecutor.java:906)\n	at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)\n	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)\n	... 1 more\nCaused by: java.net.ConnectException: Connection refused\n	... 12 more\n', NULL, 0, NULL, 0);
INSERT INTO `jobs_log` VALUES (8, 1, 3, '192.168.0.2:9999', 'demoJobHandler', NULL, NULL, 3, '2019-07-12 11:38:09', -1, '任务触发类型：Cron触发,调度机器：192.168.0.2,执行器-地址列表：[192.168.0.2:9999],路由策略：第一个,阻塞处理策略：Serial execution,任务超时时间：10,失败重试次数：3,触发调度：执行成功触发调度：,address：192.168.0.2:9999,code：-1,msg：com.xxl.rpc.util.XxlRpcException: io.netty.channel.AbstractChannel$AnnotatedConnectException: Connection refused: /192.168.0.2:9999\n	at com.xxl.rpc.remoting.invoker.reference.XxlRpcReferenceBean$1.invoke(XxlRpcReferenceBean.java:227)\n	at com.sun.proxy.$Proxy85.run(Unknown Source)\n	at com.baomidou.jobs.starter.trigger.JobsTrigger.runExecutor(JobsTrigger.java:156)\n	at com.baomidou.jobs.starter.trigger.JobsTrigger.processTrigger(JobsTrigger.java:115)\n	at com.baomidou.jobs.starter.trigger.JobsTrigger.trigger(JobsTrigger.java:53)\n	at com.baomidou.jobs.starter.disruptor.JobsEventHandler.onEvent(JobsEventHandler.java:34)\n	at com.baomidou.jobs.starter.disruptor.JobsEventHandler.onEvent(JobsEventHandler.java:17)\n	at com.lmax.disruptor.BatchEventProcessor.processEvents(BatchEventProcessor.java:168)\n	at com.lmax.disruptor.BatchEventProcessor.run(BatchEventProcessor.java:125)\n	at java.lang.Thread.run(Thread.java:745)\nCaused by: io.netty.channel.AbstractChannel$AnnotatedConnectException: Connection refused: /192.168.0.2:9999\n	at sun.nio.ch.SocketChannelImpl.checkConnect(Native Method)\n	at sun.nio.ch.SocketChannelImpl.finishConnect(SocketChannelImpl.java:712)\n	at io.netty.channel.socket.nio.NioSocketChannel.doFinishConnect(NioSocketChannel.java:327)\n	at io.netty.channel.nio.AbstractNioChannel$AbstractNioUnsafe.finishConnect(AbstractNioChannel.java:340)\n	at io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:670)\n	at io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:617)\n	at io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:534)\n	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:496)\n	at io.netty.util.concurrent.SingleThreadEventExecutor$5.run(SingleThreadEventExecutor.java:906)\n	at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)\n	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)\n	... 1 more\nCaused by: java.net.ConnectException: Connection refused\n	... 12 more\n', NULL, 0, NULL, 0);
INSERT INTO `jobs_log` VALUES (9, 1, 3, '192.168.0.2:9999', 'demoJobHandler', NULL, NULL, 3, '2019-07-12 11:38:09', -1, '任务触发类型：Cron触发,调度机器：192.168.0.2,执行器-地址列表：[192.168.0.2:9999],路由策略：第一个,阻塞处理策略：Serial execution,任务超时时间：10,失败重试次数：3,触发调度：执行成功触发调度：,address：192.168.0.2:9999,code：-1,msg：com.xxl.rpc.util.XxlRpcException: io.netty.channel.AbstractChannel$AnnotatedConnectException: Connection refused: /192.168.0.2:9999\n	at com.xxl.rpc.remoting.invoker.reference.XxlRpcReferenceBean$1.invoke(XxlRpcReferenceBean.java:227)\n	at com.sun.proxy.$Proxy85.run(Unknown Source)\n	at com.baomidou.jobs.starter.trigger.JobsTrigger.runExecutor(JobsTrigger.java:156)\n	at com.baomidou.jobs.starter.trigger.JobsTrigger.processTrigger(JobsTrigger.java:115)\n	at com.baomidou.jobs.starter.trigger.JobsTrigger.trigger(JobsTrigger.java:53)\n	at com.baomidou.jobs.starter.disruptor.JobsEventHandler.onEvent(JobsEventHandler.java:34)\n	at com.baomidou.jobs.starter.disruptor.JobsEventHandler.onEvent(JobsEventHandler.java:17)\n	at com.lmax.disruptor.BatchEventProcessor.processEvents(BatchEventProcessor.java:168)\n	at com.lmax.disruptor.BatchEventProcessor.run(BatchEventProcessor.java:125)\n	at java.lang.Thread.run(Thread.java:745)\nCaused by: io.netty.channel.AbstractChannel$AnnotatedConnectException: Connection refused: /192.168.0.2:9999\n	at sun.nio.ch.SocketChannelImpl.checkConnect(Native Method)\n	at sun.nio.ch.SocketChannelImpl.finishConnect(SocketChannelImpl.java:712)\n	at io.netty.channel.socket.nio.NioSocketChannel.doFinishConnect(NioSocketChannel.java:327)\n	at io.netty.channel.nio.AbstractNioChannel$AbstractNioUnsafe.finishConnect(AbstractNioChannel.java:340)\n	at io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:670)\n	at io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:617)\n	at io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:534)\n	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:496)\n	at io.netty.util.concurrent.SingleThreadEventExecutor$5.run(SingleThreadEventExecutor.java:906)\n	at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)\n	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)\n	... 1 more\nCaused by: java.net.ConnectException: Connection refused\n	... 12 more\n', NULL, 0, NULL, 0);
INSERT INTO `jobs_log` VALUES (10, 1, 3, '192.168.0.2:9999', 'demoJobHandler', NULL, NULL, 3, '2019-07-12 11:38:09', -1, '任务触发类型：Cron触发,调度机器：192.168.0.2,执行器-地址列表：[192.168.0.2:9999],路由策略：第一个,阻塞处理策略：Serial execution,任务超时时间：10,失败重试次数：3,触发调度：执行成功触发调度：,address：192.168.0.2:9999,code：-1,msg：com.xxl.rpc.util.XxlRpcException: io.netty.channel.AbstractChannel$AnnotatedConnectException: Connection refused: /192.168.0.2:9999\n	at com.xxl.rpc.remoting.invoker.reference.XxlRpcReferenceBean$1.invoke(XxlRpcReferenceBean.java:227)\n	at com.sun.proxy.$Proxy85.run(Unknown Source)\n	at com.baomidou.jobs.starter.trigger.JobsTrigger.runExecutor(JobsTrigger.java:156)\n	at com.baomidou.jobs.starter.trigger.JobsTrigger.processTrigger(JobsTrigger.java:115)\n	at com.baomidou.jobs.starter.trigger.JobsTrigger.trigger(JobsTrigger.java:53)\n	at com.baomidou.jobs.starter.disruptor.JobsEventHandler.onEvent(JobsEventHandler.java:34)\n	at com.baomidou.jobs.starter.disruptor.JobsEventHandler.onEvent(JobsEventHandler.java:17)\n	at com.lmax.disruptor.BatchEventProcessor.processEvents(BatchEventProcessor.java:168)\n	at com.lmax.disruptor.BatchEventProcessor.run(BatchEventProcessor.java:125)\n	at java.lang.Thread.run(Thread.java:745)\nCaused by: io.netty.channel.AbstractChannel$AnnotatedConnectException: Connection refused: /192.168.0.2:9999\n	at sun.nio.ch.SocketChannelImpl.checkConnect(Native Method)\n	at sun.nio.ch.SocketChannelImpl.finishConnect(SocketChannelImpl.java:712)\n	at io.netty.channel.socket.nio.NioSocketChannel.doFinishConnect(NioSocketChannel.java:327)\n	at io.netty.channel.nio.AbstractNioChannel$AbstractNioUnsafe.finishConnect(AbstractNioChannel.java:340)\n	at io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:670)\n	at io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:617)\n	at io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:534)\n	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:496)\n	at io.netty.util.concurrent.SingleThreadEventExecutor$5.run(SingleThreadEventExecutor.java:906)\n	at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)\n	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)\n	... 1 more\nCaused by: java.net.ConnectException: Connection refused\n	... 12 more\n', NULL, 0, NULL, 0);
INSERT INTO `jobs_log` VALUES (11, 1, 3, '192.168.0.2:9999', 'demoJobHandler', NULL, NULL, 3, '2019-07-12 11:38:11', -1, '任务触发类型：Cron触发,调度机器：192.168.0.2,执行器-地址列表：[192.168.0.2:9999],路由策略：第一个,阻塞处理策略：Serial execution,任务超时时间：10,失败重试次数：3,触发调度：执行成功触发调度：,address：192.168.0.2:9999,code：-1,msg：com.xxl.rpc.util.XxlRpcException: io.netty.channel.AbstractChannel$AnnotatedConnectException: Connection refused: /192.168.0.2:9999\n	at com.xxl.rpc.remoting.invoker.reference.XxlRpcReferenceBean$1.invoke(XxlRpcReferenceBean.java:227)\n	at com.sun.proxy.$Proxy85.run(Unknown Source)\n	at com.baomidou.jobs.starter.trigger.JobsTrigger.runExecutor(JobsTrigger.java:156)\n	at com.baomidou.jobs.starter.trigger.JobsTrigger.processTrigger(JobsTrigger.java:115)\n	at com.baomidou.jobs.starter.trigger.JobsTrigger.trigger(JobsTrigger.java:53)\n	at com.baomidou.jobs.starter.disruptor.JobsEventHandler.onEvent(JobsEventHandler.java:34)\n	at com.baomidou.jobs.starter.disruptor.JobsEventHandler.onEvent(JobsEventHandler.java:17)\n	at com.lmax.disruptor.BatchEventProcessor.processEvents(BatchEventProcessor.java:168)\n	at com.lmax.disruptor.BatchEventProcessor.run(BatchEventProcessor.java:125)\n	at java.lang.Thread.run(Thread.java:745)\nCaused by: io.netty.channel.AbstractChannel$AnnotatedConnectException: Connection refused: /192.168.0.2:9999\n	at sun.nio.ch.SocketChannelImpl.checkConnect(Native Method)\n	at sun.nio.ch.SocketChannelImpl.finishConnect(SocketChannelImpl.java:712)\n	at io.netty.channel.socket.nio.NioSocketChannel.doFinishConnect(NioSocketChannel.java:327)\n	at io.netty.channel.nio.AbstractNioChannel$AbstractNioUnsafe.finishConnect(AbstractNioChannel.java:340)\n	at io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:670)\n	at io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:617)\n	at io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:534)\n	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:496)\n	at io.netty.util.concurrent.SingleThreadEventExecutor$5.run(SingleThreadEventExecutor.java:906)\n	at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)\n	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)\n	... 1 more\nCaused by: java.net.ConnectException: Connection refused\n	... 12 more\n', NULL, 0, NULL, 0);
INSERT INTO `jobs_log` VALUES (12, 1, 3, '192.168.0.2:9999', 'demoJobHandler', NULL, NULL, 3, '2019-07-12 11:38:21', -1, '任务触发类型：Cron触发,调度机器：192.168.0.2,执行器-地址列表：[192.168.0.2:9999],路由策略：第一个,阻塞处理策略：Serial execution,任务超时时间：10,失败重试次数：3,触发调度：执行成功触发调度：,address：192.168.0.2:9999,code：-1,msg：com.xxl.rpc.util.XxlRpcException: io.netty.channel.AbstractChannel$AnnotatedConnectException: Connection refused: /192.168.0.2:9999\n	at com.xxl.rpc.remoting.invoker.reference.XxlRpcReferenceBean$1.invoke(XxlRpcReferenceBean.java:227)\n	at com.sun.proxy.$Proxy85.run(Unknown Source)\n	at com.baomidou.jobs.starter.trigger.JobsTrigger.runExecutor(JobsTrigger.java:156)\n	at com.baomidou.jobs.starter.trigger.JobsTrigger.processTrigger(JobsTrigger.java:115)\n	at com.baomidou.jobs.starter.trigger.JobsTrigger.trigger(JobsTrigger.java:53)\n	at com.baomidou.jobs.starter.disruptor.JobsEventHandler.onEvent(JobsEventHandler.java:34)\n	at com.baomidou.jobs.starter.disruptor.JobsEventHandler.onEvent(JobsEventHandler.java:17)\n	at com.lmax.disruptor.BatchEventProcessor.processEvents(BatchEventProcessor.java:168)\n	at com.lmax.disruptor.BatchEventProcessor.run(BatchEventProcessor.java:125)\n	at java.lang.Thread.run(Thread.java:745)\nCaused by: io.netty.channel.AbstractChannel$AnnotatedConnectException: Connection refused: /192.168.0.2:9999\n	at sun.nio.ch.SocketChannelImpl.checkConnect(Native Method)\n	at sun.nio.ch.SocketChannelImpl.finishConnect(SocketChannelImpl.java:712)\n	at io.netty.channel.socket.nio.NioSocketChannel.doFinishConnect(NioSocketChannel.java:327)\n	at io.netty.channel.nio.AbstractNioChannel$AbstractNioUnsafe.finishConnect(AbstractNioChannel.java:340)\n	at io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:670)\n	at io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:617)\n	at io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:534)\n	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:496)\n	at io.netty.util.concurrent.SingleThreadEventExecutor$5.run(SingleThreadEventExecutor.java:906)\n	at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)\n	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)\n	... 1 more\nCaused by: java.net.ConnectException: Connection refused\n	... 12 more\n', NULL, 0, NULL, 0);
INSERT INTO `jobs_log` VALUES (13, 1, 3, '192.168.0.2:9999', 'demoJobHandler', NULL, NULL, 3, '2019-07-12 11:38:31', -1, '任务触发类型：Cron触发,调度机器：192.168.0.2,执行器-地址列表：[192.168.0.2:9999],路由策略：第一个,阻塞处理策略：Serial execution,任务超时时间：10,失败重试次数：3,触发调度：执行成功触发调度：,address：192.168.0.2:9999,code：-1,msg：com.xxl.rpc.util.XxlRpcException: io.netty.channel.AbstractChannel$AnnotatedConnectException: Connection refused: /192.168.0.2:9999\n	at com.xxl.rpc.remoting.invoker.reference.XxlRpcReferenceBean$1.invoke(XxlRpcReferenceBean.java:227)\n	at com.sun.proxy.$Proxy85.run(Unknown Source)\n	at com.baomidou.jobs.starter.trigger.JobsTrigger.runExecutor(JobsTrigger.java:156)\n	at com.baomidou.jobs.starter.trigger.JobsTrigger.processTrigger(JobsTrigger.java:115)\n	at com.baomidou.jobs.starter.trigger.JobsTrigger.trigger(JobsTrigger.java:53)\n	at com.baomidou.jobs.starter.disruptor.JobsEventHandler.onEvent(JobsEventHandler.java:34)\n	at com.baomidou.jobs.starter.disruptor.JobsEventHandler.onEvent(JobsEventHandler.java:17)\n	at com.lmax.disruptor.BatchEventProcessor.processEvents(BatchEventProcessor.java:168)\n	at com.lmax.disruptor.BatchEventProcessor.run(BatchEventProcessor.java:125)\n	at java.lang.Thread.run(Thread.java:745)\nCaused by: io.netty.channel.AbstractChannel$AnnotatedConnectException: Connection refused: /192.168.0.2:9999\n	at sun.nio.ch.SocketChannelImpl.checkConnect(Native Method)\n	at sun.nio.ch.SocketChannelImpl.finishConnect(SocketChannelImpl.java:712)\n	at io.netty.channel.socket.nio.NioSocketChannel.doFinishConnect(NioSocketChannel.java:327)\n	at io.netty.channel.nio.AbstractNioChannel$AbstractNioUnsafe.finishConnect(AbstractNioChannel.java:340)\n	at io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:670)\n	at io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:617)\n	at io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:534)\n	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:496)\n	at io.netty.util.concurrent.SingleThreadEventExecutor$5.run(SingleThreadEventExecutor.java:906)\n	at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)\n	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)\n	... 1 more\nCaused by: java.net.ConnectException: Connection refused\n	... 12 more\n', NULL, 0, NULL, 0);
INSERT INTO `jobs_log` VALUES (14, 1, 3, '192.168.0.2:9999', 'demoJobHandler', NULL, NULL, 3, '2019-07-12 11:38:41', 0, '任务触发类型：Cron触发,调度机器：192.168.0.2,执行器-地址列表：[192.168.0.2:9999],路由策略：第一个,阻塞处理策略：Serial execution,任务超时时间：10,失败重试次数：3,触发调度：执行成功触发调度：,address：192.168.0.2:9999,code：0,msg：执行成功', NULL, 0, NULL, 0);
INSERT INTO `jobs_log` VALUES (15, 1, 3, '192.168.0.2:9999', 'demoJobHandler', NULL, NULL, 3, '2019-07-12 11:38:51', 0, '任务触发类型：Cron触发,调度机器：192.168.0.2,执行器-地址列表：[192.168.0.2:9999],路由策略：第一个,阻塞处理策略：Serial execution,任务超时时间：10,失败重试次数：3,触发调度：执行成功触发调度：,address：192.168.0.2:9999,code：0,msg：执行成功', NULL, 0, NULL, 0);
INSERT INTO `jobs_log` VALUES (16, 1, 3, '192.168.0.2:9999', 'demoJobHandler', NULL, NULL, 3, '2019-07-12 11:39:01', 0, '任务触发类型：Cron触发,调度机器：192.168.0.2,执行器-地址列表：[192.168.0.2:9999],路由策略：第一个,阻塞处理策略：Serial execution,任务超时时间：10,失败重试次数：3,触发调度：执行成功触发调度：,address：192.168.0.2:9999,code：0,msg：执行成功', NULL, 0, NULL, 0);
INSERT INTO `jobs_log` VALUES (17, 1, 3, '192.168.0.2:9999', 'demoJobHandler', NULL, NULL, 3, '2019-07-12 11:39:11', 0, '任务触发类型：Cron触发,调度机器：192.168.0.2,执行器-地址列表：[192.168.0.2:9999],路由策略：第一个,阻塞处理策略：Serial execution,任务超时时间：10,失败重试次数：3,触发调度：执行成功触发调度：,address：192.168.0.2:9999,code：0,msg：执行成功', NULL, 0, NULL, 0);
INSERT INTO `jobs_log` VALUES (18, 1, 3, '192.168.0.2:9999', 'demoJobHandler', NULL, NULL, 3, '2019-07-12 11:39:21', 0, '任务触发类型：Cron触发,调度机器：192.168.0.2,执行器-地址列表：[192.168.0.2:9999],路由策略：第一个,阻塞处理策略：Serial execution,任务超时时间：10,失败重试次数：3,触发调度：执行成功触发调度：,address：192.168.0.2:9999,code：0,msg：执行成功', NULL, 0, NULL, 0);
INSERT INTO `jobs_log` VALUES (19, 1, 3, '192.168.0.2:9999', 'demoJobHandler', NULL, NULL, 3, '2019-07-12 11:39:31', 0, '任务触发类型：Cron触发,调度机器：192.168.0.2,执行器-地址列表：[192.168.0.2:9999],路由策略：第一个,阻塞处理策略：Serial execution,任务超时时间：10,失败重试次数：3,触发调度：执行成功触发调度：,address：192.168.0.2:9999,code：0,msg：执行成功', NULL, 0, NULL, 0);
INSERT INTO `jobs_log` VALUES (20, 1, 3, '192.168.0.2:9999', 'demoJobHandler', NULL, NULL, 3, '2019-07-12 11:39:41', 0, '任务触发类型：Cron触发,调度机器：192.168.0.2,执行器-地址列表：[192.168.0.2:9999],路由策略：第一个,阻塞处理策略：Serial execution,任务超时时间：10,失败重试次数：3,触发调度：执行成功触发调度：,address：192.168.0.2:9999,code：0,msg：执行成功', NULL, 0, NULL, 0);
COMMIT;

-- ----------------------------
-- Table structure for jobs_logglue
-- ----------------------------
DROP TABLE IF EXISTS `jobs_logglue`;
CREATE TABLE `jobs_logglue` (
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
-- Table structure for jobs_registry
-- ----------------------------
DROP TABLE IF EXISTS `jobs_registry`;
CREATE TABLE `jobs_registry` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
  `app` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '服务名',
  `address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'IP 地址',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=902 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jobs_registry
-- ----------------------------
BEGIN;
INSERT INTO `jobs_registry` VALUES (901, 'jobs-executor-sample', '192.168.0.2:9999', '2019-07-12 11:39:38');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
