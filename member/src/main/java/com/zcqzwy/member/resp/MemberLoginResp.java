package com.zcqzwy.member.resp;

/**
 * <p>作者： zcq</p> 
 * <p>文件名称: MemberLoginResp </p> 
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2026/2/24 </p>
 * @author <a href="mail to: 2928235428@qq.com" rel="nofollow">作者</a>
 * @version 1.0
 **/
public class MemberLoginResp {
  private Long id;

  private String mobile;

  private String token;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("MemberLoginResp{");
    sb.append("id=").append(id);
    sb.append(", mobile='").append(mobile).append('\'');
    sb.append(", token='").append(token).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
