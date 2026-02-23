package com.zcqzwy.member.service;

import com.zcqzwy.member.mapper.MemberMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>作者： zcq</p> 
 * <p>文件名称: MemberService </p> 
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2026/2/23 </p>
 * @author <a href="mail to: 2928235428@qq.com" rel="nofollow">作者</a>
 * @version 1.0
 **/
@Service
public class MemberService {
   @Resource
   private MemberMapper memberMapper;
   public int count(){
       return memberMapper.count();
   }
}
