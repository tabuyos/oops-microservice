package com.tabuyos.microservice.oops.common.zk.registry.base;

import java.util.List;

/**
 * Description:
 *
 * <pre>
 *   <b>project: </b><i>tabuyos-microservice</i>
 *   <b>package: </b><i>com.tabuyos.microservice.oops.common.zk.registry.base</i>
 *   <b>class: </b><i>CoordinatorRegistryCenter</i>
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
 * @since 0.1.0 - 2/22/21 1:48 PM
 */
public interface CoordinatorRegistryCenter extends RegistryCenter {

  /**
   * 直接从注册中心而非本地缓存获取数据.
   *
   * @param key 键
   * @return 值 directly
   */
  String getDirectly(String key);

  /**
   * 获取子节点名称集合.
   *
   * @param key 键
   * @return 子节点名称集合 children keys
   */
  List<String> getChildrenKeys(String key);

  /**
   * 获取子节点数量.
   *
   * @param key 键
   * @return 子节点数量 num children
   */
  int getNumChildren(String key);

  /**
   * 持久化临时注册数据.
   *
   * @param key 键
   * @param value 值
   */
  void persistEphemeral(String key, String value);

  /**
   * 持久化顺序注册数据.
   *
   * @param key 键
   * @param value 值
   * @return 包含10位顺序数字的znode名称 string
   */
  String persistSequential(String key, String value);

  /**
   * 持久化临时顺序注册数据.
   *
   * @param key 键
   */
  void persistEphemeralSequential(String key);

  /**
   * 添加本地缓存.
   *
   * @param cachePath 需加入缓存的路径
   */
  void addCacheData(String cachePath);

  /**
   * 释放本地缓存.
   *
   * @param cachePath 需释放缓存的路径
   */
  void evictCacheData(String cachePath);

  /**
   * 获取注册中心数据缓存对象.
   *
   * @param cachePath 缓存的节点路径
   * @return 注册中心数据缓存对象 raw cache
   */
  Object getRawCache(String cachePath);

  /**
   * 向注册中心进行注册，生成该服务的编号并返回
   *
   * @param app the app
   * @param host the host
   * @param producerGroup the producer group
   * @param consumerGroup the consumer group
   * @param namesrvAddr the namesrv addr
   */
  void registerMq(
      String app, String host, String producerGroup, String consumerGroup, String namesrvAddr);
}
