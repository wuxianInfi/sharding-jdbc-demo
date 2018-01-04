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
package com.infi.shardingjdbc.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * @author hongtao
 * @version v 0.1 , 2018年1月3日 上午11:42:57
 * @since JDK 1.8
 */
public final class DuridDataSourceFactory {

  public static DataSource createDataSource(String url, DBProperties commonProperties)
      throws SQLException {
    // special
    DruidDataSource datasource = new DruidDataSource();
    datasource.setUrl(url);
    datasource.setUsername(commonProperties.getUsername());
    datasource.setPassword(commonProperties.getPassword());
    // common
    datasource.setDriverClassName(commonProperties.getDriverClassName());
    datasource.setInitialSize(commonProperties.getInitialSize());
    datasource.setMinIdle(commonProperties.getMinIdle());
    datasource.setMaxActive(commonProperties.getMaxActive());
    datasource.setMaxWait(commonProperties.getMaxWait());
    datasource.setValidationQuery(commonProperties.getValidationQuery());
    datasource.setTestWhileIdle(commonProperties.isTestWhileIdle());
    datasource.setTestOnBorrow(commonProperties.isTestOnBorrow());
    datasource.setTestOnReturn(commonProperties.isTestOnReturn());
    return datasource;
  }
}
