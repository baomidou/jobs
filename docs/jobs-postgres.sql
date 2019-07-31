/*
 Navicat Premium Data Transfer

 Source Server         : PGCrab
 Source Server Type    : PostgreSQL
 Source Server Version : 110000
 Source Host           : 47.104.25.225:5432
 Source Catalog        : jobs
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 110000
 File Encoding         : 65001

 Date: 31/07/2019 16:27:04
*/


-- ----------------------------
-- Table structure for jobs_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."jobs_info";
CREATE TABLE "public"."jobs_info" (
  "id" int8 NOT NULL,
  "tenant_id" varchar(100) COLLATE "pg_catalog"."default",
  "app" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "cron" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "handler" varchar(255) COLLATE "pg_catalog"."default",
  "param" varchar(512) COLLATE "pg_catalog"."default",
  "timeout" int4 NOT NULL,
  "fail_retry_count" int4 NOT NULL,
  "last_time" int8 NOT NULL,
  "next_time" int8 NOT NULL,
  "author" varchar(30) COLLATE "pg_catalog"."default",
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "status" int2 NOT NULL,
  "update_time" int8,
  "create_time" int8 NOT NULL
)
;
ALTER TABLE "public"."jobs_info" OWNER TO "postgres";
COMMENT ON COLUMN "public"."jobs_info"."id" IS '主键ID';
COMMENT ON COLUMN "public"."jobs_info"."tenant_id" IS '租户ID';
COMMENT ON COLUMN "public"."jobs_info"."app" IS '服务名';
COMMENT ON COLUMN "public"."jobs_info"."cron" IS '任务执行CRON';
COMMENT ON COLUMN "public"."jobs_info"."handler" IS '执行器任务handler';
COMMENT ON COLUMN "public"."jobs_info"."param" IS '执行器任务参数';
COMMENT ON COLUMN "public"."jobs_info"."timeout" IS '任务执行超时时间，单位秒';
COMMENT ON COLUMN "public"."jobs_info"."fail_retry_count" IS '失败重试次数';
COMMENT ON COLUMN "public"."jobs_info"."last_time" IS '上次调度时间';
COMMENT ON COLUMN "public"."jobs_info"."next_time" IS '下次调度时间';
COMMENT ON COLUMN "public"."jobs_info"."author" IS '作者';
COMMENT ON COLUMN "public"."jobs_info"."remark" IS '备注';
COMMENT ON COLUMN "public"."jobs_info"."status" IS '0、启用 1、已禁用';
COMMENT ON COLUMN "public"."jobs_info"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."jobs_info"."create_time" IS '创建时间';
COMMENT ON TABLE "public"."jobs_info" IS '任务信息';

-- ----------------------------
-- Records of jobs_info
-- ----------------------------
BEGIN;
INSERT INTO "public"."jobs_info" VALUES (3, NULL, 'jobs-executor-sample', '0/10 * * * * ? *', 'demoJobHandler', NULL, 30, 3, 1564539320000, 1564539330000, 'jobs', '测试', 0, 1563152000000, 1563152000000);
COMMIT;

-- ----------------------------
-- Table structure for jobs_lock
-- ----------------------------
DROP TABLE IF EXISTS "public"."jobs_lock";
CREATE TABLE "public"."jobs_lock" (
  "id" int8 NOT NULL,
  "name" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "owner" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "create_time" int8 NOT NULL
)
;
ALTER TABLE "public"."jobs_lock" OWNER TO "postgres";
COMMENT ON COLUMN "public"."jobs_lock"."id" IS '主键ID';
COMMENT ON COLUMN "public"."jobs_lock"."name" IS '名称';
COMMENT ON COLUMN "public"."jobs_lock"."owner" IS '持有者';
COMMENT ON COLUMN "public"."jobs_lock"."create_time" IS '创建时间';
COMMENT ON TABLE "public"."jobs_lock" IS '任务锁';

-- ----------------------------
-- Table structure for jobs_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."jobs_log";
CREATE TABLE "public"."jobs_log" (
  "id" int8 NOT NULL,
  "job_id" int8 NOT NULL,
  "address" varchar(255) COLLATE "pg_catalog"."default",
  "handler" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "param" varchar(512) COLLATE "pg_catalog"."default",
  "fail_retry_count" int4 NOT NULL,
  "trigger_code" int4 NOT NULL,
  "trigger_type" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "trigger_msg" text COLLATE "pg_catalog"."default",
  "create_time" int8 NOT NULL
)
;
ALTER TABLE "public"."jobs_log" OWNER TO "postgres";
COMMENT ON COLUMN "public"."jobs_log"."id" IS '主键ID';
COMMENT ON COLUMN "public"."jobs_log"."job_id" IS '任务ID';
COMMENT ON COLUMN "public"."jobs_log"."address" IS '执行地址';
COMMENT ON COLUMN "public"."jobs_log"."handler" IS '任务 handler';
COMMENT ON COLUMN "public"."jobs_log"."param" IS '任务参数';
COMMENT ON COLUMN "public"."jobs_log"."fail_retry_count" IS '失败重试次数';
COMMENT ON COLUMN "public"."jobs_log"."trigger_code" IS '触发器调度返回码';
COMMENT ON COLUMN "public"."jobs_log"."trigger_type" IS '触发器调度类型';
COMMENT ON COLUMN "public"."jobs_log"."trigger_msg" IS '触发器调度返回信息';
COMMENT ON COLUMN "public"."jobs_log"."create_time" IS '创建时间';
COMMENT ON TABLE "public"."jobs_log" IS '任务调度日志';

-- ----------------------------
-- Table structure for jobs_registry
-- ----------------------------
DROP TABLE IF EXISTS "public"."jobs_registry";
CREATE TABLE "public"."jobs_registry" (
  "id" int8 NOT NULL,
  "app" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "address" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "status" int2 NOT NULL,
  "update_time" int8 NOT NULL
)
;
ALTER TABLE "public"."jobs_registry" OWNER TO "postgres";
COMMENT ON COLUMN "public"."jobs_registry"."id" IS '主键 ID';
COMMENT ON COLUMN "public"."jobs_registry"."app" IS '服务名';
COMMENT ON COLUMN "public"."jobs_registry"."address" IS 'IP 地址';
COMMENT ON COLUMN "public"."jobs_registry"."status" IS '0、启用 1、已禁用';
COMMENT ON COLUMN "public"."jobs_registry"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."jobs_registry" IS '任务注册信息';

-- ----------------------------
-- Records of jobs_registry
-- ----------------------------
BEGIN;
INSERT INTO "public"."jobs_registry" VALUES (1, 'jobs-executor-sample', '127.0.0.1:9999', 1, 1563626092397);
INSERT INTO "public"."jobs_registry" VALUES (205, 'jobs-executor-sample', '192.168.0.6:9999', 1, 1563794040004);
COMMIT;

-- ----------------------------
-- Primary Key structure for table jobs_info
-- ----------------------------
ALTER TABLE "public"."jobs_info" ADD CONSTRAINT "jobs_info_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table jobs_lock
-- ----------------------------
CREATE INDEX "uidx_name" ON "public"."jobs_lock" USING btree (
  "name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table jobs_lock
-- ----------------------------
ALTER TABLE "public"."jobs_lock" ADD CONSTRAINT "jobs_lock_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table jobs_log
-- ----------------------------
ALTER TABLE "public"."jobs_log" ADD CONSTRAINT "jobs_log_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table jobs_registry
-- ----------------------------
ALTER TABLE "public"."jobs_registry" ADD CONSTRAINT "jobs_registry_pkey" PRIMARY KEY ("id");
