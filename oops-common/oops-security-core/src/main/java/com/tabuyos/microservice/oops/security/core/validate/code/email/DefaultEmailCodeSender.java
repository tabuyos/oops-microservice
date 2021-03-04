package com.tabuyos.microservice.oops.security.core.validate.code.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 *
 * <pre>
 *   <b>project: </b>oops-microservice
 *   <b>package: </b>com.tabuyos.microservice.oops.security.core.validate.code.email
 *   <b>class: </b>DefaultEmailCodeSender
 *   comment here.
 * </pre>
 *
 * @author <pre><b>username: </b><a href="http://www.tabuyos.com">Tabuyos</a></pre>
 * <pre><b>site: </b><a href="http://www.tabuyos.com">http://www.tabuyos.com</a></pre>
 * <pre><b>email: </b>tabuyos@outlook.com</pre>
 * <pre><b>description: </b>
 *       <pre>
 *         Talk is cheap, show me the code.
 *       </pre>
 *     </pre>
 * @version 0.1.0
 * @since 0.1.0 - 3/1/21 5:02 PM
 */
public class DefaultEmailCodeSender implements EmailCodeSender {

  private final Logger log = LoggerFactory.getLogger(DefaultEmailCodeSender.class);

  /**
   * Send.
   *
   * @param email the mobile
   * @param code  the code
   */
  @Override
  public void send(String email, String code) {
    log.warn("请配置真实的邮件验证码发送器(SmsCodeSender)");
    log.info("向邮件" + email + "发送短信验证码" + code);
  }
}
