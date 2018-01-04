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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import lombok.Data;

/**
 * @author hongtao
 * @version v 0.1 , 2018年1月3日 上午10:39:30
 * @since JDK 1.8
 */
@Data
@ConfigurationProperties(prefix = "spring.datasource")
public class DBProperties {

  @NestedConfigurationProperty
  private Map<String, DBUrl> dbUrls;
  private String username;
  private String password;
  // common properties
  private String driverClassName = "com.mysql.jdbc.Driver";
  private int initialSize = 5;
  private int minIdle = 5;
  private int maxActive = 20;
  private int maxWait = 60000;
  private String validationQuery = "SELECT 1 FROM DUAL";
  private boolean testWhileIdle = true;
  private boolean testOnBorrow = true;
  private boolean testOnReturn = true;

  @Data
  public static class DBUrl {
    private String masterUrl;
    private List<String> slaveUrls = new LinkedList<>();
  }

}
