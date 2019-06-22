package com.baomidou.jobs.sample.handler;

import com.baomidou.jobs.core.handler.IJobsHandler;
import com.baomidou.jobs.core.log.JobsLogger;
import com.baomidou.jobs.core.util.ShardingUtil;
import com.baomidou.jobs.core.web.JobsResponse;
import org.springframework.stereotype.Service;

/**
 * 分片广播任务
 *
 * @author xxl jobob
 * @since 2019-06-22
 */
@Service
public class ShardingJobHandler implements IJobsHandler {

	@Override
	public JobsResponse<String> execute(String param) throws Exception {
		// 分片参数
		ShardingUtil.ShardingVO shardingVO = ShardingUtil.getShardingVo();
		JobsLogger.log("分片参数：当前分片序号 = {}, 总分片数 = {}", shardingVO.getIndex(), shardingVO.getTotal());

		// 业务逻辑
		for (int i = 0; i < shardingVO.getTotal(); i++) {
			if (i == shardingVO.getIndex()) {
				JobsLogger.log("第 {} 片, 命中分片开始处理", i);
			} else {
				JobsLogger.log("第 {} 片, 忽略", i);
			}
		}
		return JobsResponse.ok();
	}
}
