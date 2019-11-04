package com.baomidou.jobs.test;

import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import org.junit.Assert;
import org.junit.Test;

import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * cron 工具类测试
 * <p>
 * Java 组件：https://github.com/jmrozanec/cron-utils
 * vue 插件：https://github.com/1615450788/vue-cron
 * cron 在线生成：https://1615450788.github.io/vue-cron/dist/index
 *
 * @author jobob
 */
public class CronUtilsTest {

  public CronParser getCronParser() {
    //get a predefined instance
    CronDefinition cronDefinition =
      CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ);
    //create a parser based on provided definition
    return new CronParser(cronDefinition);
  }

  @Test(expected = IllegalArgumentException.class)
  public void validate() {
    Cron cron = getCronParser().parse("1* * * * * ? *");
    cron.validate();
  }

  @Test
  public void test() {
    // Get date for last execution
    ZonedDateTime now = ZonedDateTime.now();
    Cron cron = getCronParser().parse("* * * * * ? *");

    ExecutionTime executionTime = ExecutionTime.forCron(cron);
    Optional<ZonedDateTime> lastExecution = executionTime.lastExecution(now);
    System.out.println(lastExecution.get().toInstant().toEpochMilli());

    // Get date for next execution
    Optional<ZonedDateTime> nextExecution = executionTime.nextExecution(now);
    System.out.println(nextExecution.get().toInstant().toEpochMilli());
  }
}
