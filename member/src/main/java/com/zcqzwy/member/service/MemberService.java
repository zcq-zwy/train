package com.zcqzwy.member.service;

import com.zcqzwy.common.exception.BusinessException;
import com.zcqzwy.common.exception.BusinessExceptionEnum;
import com.zcqzwy.member.domain.Member;
import com.zcqzwy.member.mapper.MemberMapper;
import com.zcqzwy.member.req.MemberRegisterReq;
import jakarta.annotation.Resource;
import java.util.List;
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

  public long register(MemberRegisterReq memberRegisterReq){

     String mobile = memberRegisterReq.getMobile();
     //查看手机号是否已经注册
    List<Member> members = memberMapper.selectAllByMobile(mobile);
    if(!members.isEmpty()){
       throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_EXIST);
    }

    Member member = new Member();
      member.setMobile(mobile);
      member.setId(System.currentTimeMillis());
      memberMapper.insert(member);
      return  member.getId();
  }
}
