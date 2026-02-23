package com.zcqzwy.member.req;

/**
 * <p>作者： zcq</p> 
 * <p>文件名称: MemberRegisterReq </p> 
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2026/2/24 </p>
 * @author <a href="mail to: 2928235428@qq.com" rel="nofollow">作者</a>
 * @version 1.0
 **/
public class MemberRegisterReq {
  private String mobile;

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  @Override
  public String toString() {
    return "MemberRegisterReq{" +
        "mobile='" + mobile + '\'' +
        '}';
  }
}
