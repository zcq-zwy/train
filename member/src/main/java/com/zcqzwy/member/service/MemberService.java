package com.zcqzwy.member.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.zcqzwy.common.exception.BusinessException;
import com.zcqzwy.common.exception.BusinessExceptionEnum;
import com.zcqzwy.member.req.MemberLoginReq;
import com.zcqzwy.member.req.MemberSendCodeReq;
import com.zcqzwy.common.util.SnowUtil;
import com.zcqzwy.member.domain.Member;
import com.zcqzwy.member.mapper.MemberMapper;
import com.zcqzwy.member.req.MemberRegisterReq;
import com.zcqzwy.member.resp.MemberLoginResp;
import jakarta.annotation.Resource;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  private static final Logger LOG = LoggerFactory.getLogger(MemberService.class);
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
      member.setId(SnowUtil.getSnowflakeNextId());
      memberMapper.insert(member);
      return  member.getId();
  }

  public void sendCode(MemberSendCodeReq memberSendCodeReq){

    String mobile = memberSendCodeReq.getMobile();
    //查看手机号是否已经注册
    List<Member> members = memberMapper.selectAllByMobile(mobile);
    if(members.isEmpty()){
      LOG.info("手机号未注册，插入一条记录");
      Member member = new Member();
      member.setMobile(mobile);
      member.setId(SnowUtil.getSnowflakeNextId());
      memberMapper.insert(member);
    }else {
      LOG.info("手机号已注册，不插入记录");
    }
    //生成验证码
    String code = IdUtil.fastSimpleUUID().substring(0, 4);
    LOG.info("生成验证码：{}", code);

    LOG.info("保存短信记录表");

    LOG.info("调用短信服务，发送短信");

  }

  public MemberLoginResp login(MemberLoginReq memberLoginReq){

    String mobile = memberLoginReq.getMobile();
    String code = memberLoginReq.getCode();
    //查看手机号是否已经注册
    List<Member> members = memberMapper.selectAllByMobile(mobile);
    if(members.isEmpty()){
     throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_NOT_EXIST);
    }

    LOG.info("校验验证码");
    if(!"9527".equals( code)) {
      throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_CODE_ERROR);
    }

    MemberLoginResp memberLoginResp = BeanUtil.copyProperties(members.get(0), MemberLoginResp.class);
    return memberLoginResp;
   }

}
