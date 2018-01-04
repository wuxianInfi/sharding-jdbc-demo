/*
 * Copyright 2014-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.infi.shardingjdbc.mybatis;

import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hongtao
 * @version v 0.1 , 2018年1月4日 上午11:18:40
 * @since JDK 1.8
 */
@Slf4j
@Intercepts({
    @Signature(type = Executor.class, method = "update",
        args = {MappedStatement.class, Object.class}),
    @Signature(type = Executor.class, method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class SqlLogIntercepter implements Interceptor {

  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    Object[] args = invocation.getArgs();
    final MappedStatement mappedStatement = (MappedStatement) args[0];
    Object parameter = null;
    if (args.length > 1) {
      parameter = args[1];
    }
    String sql = mappedStatement.getBoundSql(parameter).getSql();
    long start = System.currentTimeMillis();
    try {
      return invocation.proceed();
    } finally {
      log.info("statementId = {}, sql = {}, cost = {}ms", mappedStatement.getId(), beautifySql(sql),
          System.currentTimeMillis() - start);
    }
  }

  @Override
  public Object plugin(Object target) {
    // JDK dynamic proxy
    return Plugin.wrap(target, this);
  }

  @Override
  public void setProperties(Properties properties) {}

  private String beautifySql(String sql) {
    return sql.replace("\n", "").replace("\t", "").replace("  ", " ").replace("( ", "(")
        .replace(" )", ")").replace(" ,", ",");
  }

}
