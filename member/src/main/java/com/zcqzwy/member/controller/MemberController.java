package com.zcqzwy.member.controller;

import com.zcqzwy.common.resp.CommonResp;
import com.zcqzwy.member.req.MemberRegisterReq;
import com.zcqzwy.member.service.MemberService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>作者： zcq</p> 
 * <p>文件名称: TestController </p> 
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2026/2/23 </p>
 * @author <a href="mail to: 2928235428@qq.com" rel="nofollow">作者</a>
 * @version 1.0
 **/
@RestController
@RequestMapping("/member")
public class MemberController {
  @Resource
  private MemberService memberService;
  @GetMapping("/count")
  public CommonResp<Integer> count(){

    int count = memberService.count();
    return new CommonResp<>(count);
  }

  @PostMapping("/register")
  public CommonResp<Long> register(MemberRegisterReq memberRegisterReq){
    long register = memberService.register(memberRegisterReq);
   return new CommonResp<>(register);
  }
}
