package com.zcqzwy.batch.config;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

/**
 * <p>作者： zcq</p> 
 * <p>文件名称: BatchApplication </p> 
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2026/2/28 </p>
 * @author <a href="mail to: 2928235428@qq.com" rel="nofollow">作者</a>
 * @version 1.0
 **/
@SpringBootApplication
@ComponentScan("com.zcqzwy")
@MapperScan("com.zcqzwy.mapper")
public class BatchApplication {
  private static final Logger LOG = LoggerFactory.getLogger(BatchApplication.class);

  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(BatchApplication.class);
    Environment env = app.run(args).getEnvironment();
    LOG.info("启动成功！！");
    LOG.info("测试地址: \thttp://127.0.0.1:{}{}/hello", env.getProperty("server.port"), env.getProperty("server.servlet.context-path"));
  }
}
