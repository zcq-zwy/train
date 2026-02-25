package com.zcqzwy.business.config;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * <p>作者： zcq</p> 
 * <p>文件名称: BusinessApplication </p> 
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2026/2/25 </p>
 * @author <a href="mail to: 2928235428@qq.com" rel="nofollow">作者</a>
 * @version 1.0
 **/
@SpringBootApplication
@ComponentScan("com.zcqzwy.business")
public class BusinessApplication {


  private static final Logger LOG = LoggerFactory.getLogger(BusinessApplication.class);

  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(BusinessApplication.class);
    Environment env = app.run(args).getEnvironment();
    LOG.info("启动成功！！");
    LOG.info("测试地址: \thttp://127.0.0.1:{}{}/hello", env.getProperty("server.port"), env.getProperty("server.servlet.context-path"));
  }
}
