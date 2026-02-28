package com.zcqzwy.gateway.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * <p>作者： zcq</p> 
 * <p>文件名称: GateWayApplication </p> 
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2026/2/23 </p>
 * @author <a href="mail to: 2928235428@qq.com" rel="nofollow">作者</a>
 * @version 1.0
 **/
@SpringBootApplication
@ComponentScan("com.zcqzwy")
public class GateWayApplication {

  public static void main(String[] args) {
    SpringApplication.run(GateWayApplication.class,args);
    System.out.println("网关启动测试 - 当前线程：" + Thread.currentThread().getName());
  }
}
