# jobs 分布式任务调度组件
项目名：Jobs 【致敬: 史蒂夫·乔布斯（Steve Jobs)】

目标是构建一个 Spring Boot 分布式任务 starter 插拔组件


<a target="_blank" href="https://gitee.com/baomidou/jobs">Gitee</a>&nbsp;&nbsp;
<a target="_blank" href="https://github.com/baomidou/jobs">Github</a>

# 特点
- 不需独立部署类似 Swagger 模式的可插拔组件，引入 starter 注解启动
- 只依赖数据库（默认 mybatis-plus 实现，支持主流数据库）
- Rest API 接口适配任意系统
- 实现接口支持切换为 JPA 等任意 ORM 框架
- 任务 disruptor 异步处理

# 使用
- jobs-admin 测试后台，你可以理解为调度中心
实现 IJobsService 接口即完全调度中心的数据层实现，JobsApiController 提供客户端注册入口

- jobs-spring-boot-sample 测试样例，你可以为理解为任务实现端
配置 application.yml 调度中心地址多个英文逗号分割，任务实现 IJobsHandler 接口即完成 

1、初始化 docs 对应数据库，如果无你可以参考数据结构初始化（当然你可以PR）

2、启动 admin 启动 sample （观察控制台日志及 jobs-log 表数据）

# 注意

Client 端默认为内网 ip 外网调用，启动命令添加 `--jobs-app-ip=外网ip` 指定 IP 端口防火墙需要放行

# 鸣谢
Jobs 参考 <a target="_blank" href="https://github.com/xuxueli/xxl-job">xxl-job</a> 但是更为轻量，目的是构建类似 Swagger 模式的插拔组件。

VUE 前端：<a target="_blank" href="https://github.com/ldw21cn/jobs-admin-web">jobs-admin-web</a>

### 界面效果

<img src="https://gitee.com/baomidou/jobs-admin-web/raw/master/doc/0.png"/>

<img src="https://gitee.com/baomidou/jobs-admin-web/raw/master/doc/1.jpeg"/>

<img src="https://gitee.com/baomidou/jobs-admin-web/raw/master/doc/2.jpeg"/>

