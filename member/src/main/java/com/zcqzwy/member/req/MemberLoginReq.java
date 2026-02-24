package com.zcqzwy.member.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * <p>作者： zcq</p> 
 * <p>文件名称: MemberSendCodeReq </p> 
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2026/2/24 </p>
 * @author <a href="mail to: 2928235428@qq.com" rel="nofollow">作者</a>
 * @version 1.0
 **/
public class MemberLoginReq {
  @NotBlank(message = "手机号不能为空")
  @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
   private String mobile;
  @NotBlank(message = "验证码不能为空")
  private String code;

  public String getCode() {
    return code;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("MemberLoginReq{");
    sb.append("mobile='").append(mobile).append('\'');
    sb.append(", code='").append(code).append('\'');
    sb.append('}');
    return sb.toString();
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public MemberLoginReq(String mobile) {
    this.mobile = mobile;
  }
  public MemberLoginReq() {
  }

}
