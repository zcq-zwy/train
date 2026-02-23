package com.zcqzwy.common.controller;

import com.zcqzwy.common.exception.BusinessException;
import com.zcqzwy.common.resp.CommonResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>作者： zcq</p> 
 * <p>文件名称: ControllerExceptionHandler </p> 
 * <p>描述: 同一异常处理，数据预处理 </p>
 * <p>创建时间: 2026/2/24 </p>
 * @author <a href="mail to: 2928235428@qq.com" rel="nofollow">作者</a>
 * @version 1.0

 **/
@ControllerAdvice
public class ControllerExceptionHandler {
  private static final Logger LOG = LoggerFactory.getLogger(ControllerExceptionHandler.class);

  /**
   * 所有异常统一处理
   * @param e
   * @return
   */
  @ExceptionHandler(value = Exception.class)
  @ResponseBody
  public CommonResp exceptionHandler(Exception e) throws Exception {
    // LOG.info("seata全局事务ID: {}", RootContext.getXID());
    // // 如果是在一次全局事务里出异常了，就不要包装返回值，将异常抛给调用方，让调用方回滚事务
    // if (StrUtil.isNotBlank(RootContext.getXID())) {
    //     throw e;
    // }
    CommonResp commonResp = new CommonResp();
    LOG.error("系统异常：", e);
    commonResp.setSuccess(false);
    commonResp.setMessage("系统出现异常，请联系管理员");
    return commonResp;
  }

  /**
   * 业务异常统一处理
   * @param e
   * @return
   */
  @ExceptionHandler(value = BusinessException.class)
  @ResponseBody
  public CommonResp exceptionHandler(BusinessException e) {
    CommonResp commonResp = new CommonResp();
    LOG.error("业务异常：{}", e.getE().getDesc());
    commonResp.setSuccess(false);
    commonResp.setMessage(e.getE().getDesc());
    return commonResp;
  }

  /**
   * 校验异常统一处理
   * @param e
   * @return
   */
  @ExceptionHandler(value = BindException.class)
  @ResponseBody
  public CommonResp exceptionHandler(BindException e) {
    CommonResp commonResp = new CommonResp();
    LOG.error("校验异常：{}", e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    commonResp.setSuccess(false);
    commonResp.setMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    return commonResp;
  }


}
