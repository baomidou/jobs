package com.baomidou.jobs.sample.handler;

import com.baomidou.jobs.core.handler.IJobsHandler;
import com.baomidou.jobs.core.util.ShardingUtil;
import com.baomidou.jobs.core.web.JobsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 分片广播任务
 *
 * @author xxl jobob
 * @since 2019-06-22
 */
@Slf4j
@Service
public class ShardingJobHandler implements IJobsHandler {

	@Override
	public JobsResponse<String> execute(String param) throws Exception {
		// 分片参数
		ShardingUtil.ShardingVO shardingVO = ShardingUtil.getShardingVo();
		log.info("分片参数：当前分片序号 = {}, 总分片数 = {}", shardingVO.getIndex(), shardingVO.getTotal());

		// 业务逻辑
		for (int i = 0; i < shardingVO.getTotal(); i++) {
			if (i == shardingVO.getIndex()) {
				log.info("第 {} 片, 命中分片开始处理", i);
			} else {
				log.info("第 {} 片, 忽略", i);
			}
		}
		return JobsResponse.ok();
	}
}
