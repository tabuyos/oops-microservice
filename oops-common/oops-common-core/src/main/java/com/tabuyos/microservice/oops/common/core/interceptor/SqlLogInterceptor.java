package com.tabuyos.microservice.oops.common.core.interceptor;

import com.tabuyos.microservice.oops.common.base.constant.GlobalConstant;
import com.tabuyos.microservice.oops.common.core.aspect.NotDisplaySqlAspect;
import com.tabuyos.microservice.oops.common.util.ThreadLocalMap;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;

import java.text.DateFormat;
import java.util.*;
import java.util.concurrent.Executor;

/**
 * Description:
 *
 * <pre>
 *   <b>project: </b><i>tabuyos-microservice</i>
 *   <b>package: </b><i>com.tabuyos.microservice.oops.common.core.interceptor</i>
 *   <b>class: </b><i>SqlLogInterceptor</i>
 *   comment here.
 * </pre>
 *
 * @author
 *     <pre><b>username: </b><i><a href="http://www.tabuyos.com">Tabuyos</a></i></pre>
 *     <pre><b>site: </b><i><a href="http://www.tabuyos.com">http://www.tabuyos.com</a></i></pre>
 *     <pre><b>email: </b><i>tabuyos@outlook.com</i></pre>
 *     <pre><b>description: </b><i>
 *   <pre>
 *     Talk is cheap, show me the code.
 *   </pre>
 * </i></pre>
 *
 * @version 0.1.0
 * @since 0.1.0 - 2/22/21 1:21 PM
 */
@Intercepts({
  @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
  @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
@Order(1)
public class SqlLogInterceptor implements Interceptor {

  private final Logger log = LoggerFactory.getLogger(SqlLogInterceptor.class);

  @Value("${oops.enableSqlLogInterceptor}")
  private boolean enableSqlLogInterceptor;

  /** 关注时间 单位秒，默认值 5 如果 执行SQL 超过时间 就会打印error 日志 */
  private Double noticeTime = 5.0;

  /**
   * Intercept object.
   *
   * @param invocation the invocation
   * @return the object
   * @throws Throwable the throwable
   */
  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    long start = System.currentTimeMillis();

    MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
    Object parameter = null;
    if (invocation.getArgs().length > 1) {
      parameter = invocation.getArgs()[1];
    }
    BoundSql boundSql = mappedStatement.getBoundSql(parameter);
    Configuration configuration = mappedStatement.getConfiguration();
    String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
    List<String> paramList = getParamList(configuration, boundSql);
    Object proceed = invocation.proceed();
    int result = 0;
    if (proceed instanceof ArrayList) {
      ArrayList resultList = (ArrayList) proceed;
      result = resultList.size();
    }
    if (proceed instanceof Integer) {
      result = (Integer) proceed;
    }
    if (enableSqlLogInterceptor) {
      long end = System.currentTimeMillis();
      long time = end - start;
      Boolean flag = (Boolean) ThreadLocalMap.get(NotDisplaySqlAspect.DISPLAY_SQL);
      if (time >= noticeTime * GlobalConstant.Number.THOUSAND_INT) {
        log.error("执行超过{}秒,sql={}", noticeTime, sql);
        log.error("result={}, time={}ms, params={}", result, time, paramList);
        return proceed;
      }
      if (flag == null || Objects.equals(flag, true)) {
        log.info("sql={}", sql);
        log.info("result={},time={}ms, params={}", result, time, paramList);
      }
    }
    return proceed;
  }

  /** Instantiates a new Sql log interceptor. */
  public SqlLogInterceptor() {
    log.info("[打印SQL拦截器创建]noticeTime={}秒", noticeTime);
  }

  /**
   * 设置执行sql
   *
   * @param noticeTime 关注时间 ，如果执行sql超过关注时间，打印error日志.
   */
  public SqlLogInterceptor(Double noticeTime) {
    this.noticeTime = noticeTime;
    log.info("[打印SQL拦截器创建]noticeTime={}秒", noticeTime);
  }

  /**
   * Plugin object.
   *
   * @param target the target
   * @return the object
   */
  @Override
  public Object plugin(Object target) {
    return Plugin.wrap(target, this);
  }

  /**
   * Sets properties.
   *
   * @param properties the properties
   */
  @Override
  public void setProperties(Properties properties) {}

  /**
   * 获取sql参数集合。
   *
   * @param configuration the configuration
   * @param boundSql the bound sql
   * @return the param list
   */
  private List<String> getParamList(Configuration configuration, BoundSql boundSql) {
    Object parameterObject = boundSql.getParameterObject();
    List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
    List<String> params = new ArrayList<>();
    if (parameterMappings.size() > 0 && parameterObject != null) {
      TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
      if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
        params.add(getParameterValue(parameterObject));
      } else {
        MetaObject metaObject = configuration.newMetaObject(parameterObject);
        for (ParameterMapping parameterMapping : parameterMappings) {
          String propertyName = parameterMapping.getProperty();
          if (metaObject.hasGetter(propertyName)) {
            Object obj = metaObject.getValue(propertyName);
            params.add(getParameterValue(obj));
          } else if (boundSql.hasAdditionalParameter(propertyName)) {
            Object obj = boundSql.getAdditionalParameter(propertyName);
            params.add(getParameterValue(obj));
          }
        }
      }
    }
    return params;
  }

  private String getParameterValue(Object obj) {
    String value;
    if (obj instanceof String) {
      value = "'" + obj.toString() + "'";
    } else if (obj instanceof Date) {
      DateFormat formatter =
          DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
      value = "'" + formatter.format(obj) + "'";
    } else {
      if (obj != null) {
        value = obj.toString();
      } else {
        value = "";
      }
    }
    return value;
  }
}
