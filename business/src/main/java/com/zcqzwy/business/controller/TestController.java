package com.zcqzwy.business.controller;

import cn.hutool.core.util.RandomUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>作者： zcq</p> 
 * <p>文件名称: TestController </p> 
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2026/2/25 </p>
 * @author <a href="mail to: 2928235428@qq.com" rel="nofollow">作者</a>
 * @version 1.0
 **/
@RestController
public class TestController {



  @GetMapping("/hello")
  public String hello() throws InterruptedException {
    return "Hello World! Business!";
  }

  @GetMapping("/hello1")
  public String hello1() throws InterruptedException {
    return "Hello World! Business1!";
  }

}

