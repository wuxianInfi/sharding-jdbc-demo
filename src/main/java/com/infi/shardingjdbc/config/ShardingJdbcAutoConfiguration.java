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

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.infi.shardingjdbc.config.DBProperties.DBUrl;
import com.infi.shardingjdbc.mybatis.SqlLogIntercepter;

import io.shardingjdbc.core.api.ShardingDataSourceFactory;
import io.shardingjdbc.core.api.algorithm.masterslave.MasterSlaveLoadBalanceAlgorithmType;
import io.shardingjdbc.core.api.config.MasterSlaveRuleConfiguration;
import io.shardingjdbc.core.api.config.ShardingRuleConfiguration;
import io.shardingjdbc.core.api.config.TableRuleConfiguration;
import io.shardingjdbc.core.api.config.strategy.ComplexShardingStrategyConfiguration;
import io.shardingjdbc.core.api.config.strategy.NoneShardingStrategyConfiguration;

/**
 * @author hongtao
 * @version v 0.1 , 2018年1月3日 上午11:38:00
 * @since JDK 1.8
 */
@EnableTransactionManagement
@EnableConfigurationProperties(DBProperties.class)
@MapperScan(basePackages = {"com.infi.shardingjdbc.internal.mapper"},
    sqlSessionFactoryRef = "orderSqlSessionFactory")
@Configuration
public class ShardingJdbcAutoConfiguration {

  private static final PathMatchingResourcePatternResolver RESOURCE_RESOLVER =
      new PathMatchingResourcePatternResolver();

  @Bean("orderDataSource")
  public DataSource shardingJdbcDataSource(DBProperties dbProperties) throws SQLException {
    Map<String, DataSource> dataSourceMap = buildDataSourceMap(dbProperties);
    List<MasterSlaveRuleConfiguration> masterSlaveRuleConfigs =
        buildMasterSlaveRuleConfig(dbProperties);

    ShardingRuleConfiguration shardingRuleConfig =
        buildShardingRuleConfiguration(masterSlaveRuleConfigs, dbProperties);
    return ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig,
        Maps.newHashMap(), null);
    // return MasterSlaveDataSourceFactory.createDataSource(dataSourceMap, masterSlaveRuleConfig,
    // Maps.newHashMap());
  }

  private ShardingRuleConfiguration buildShardingRuleConfiguration(
      List<MasterSlaveRuleConfiguration> masterSlaveRuleConfigs, DBProperties dbProperties) {
    ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
    TableRuleConfiguration orderTableRuleConfiguration = new TableRuleConfiguration();
    orderTableRuleConfiguration.setLogicTable("t_order");
    orderTableRuleConfiguration.setActualDataNodes("order_db0.t_order,order_db1.t_order");
    shardingRuleConfig.getTableRuleConfigs().add(orderTableRuleConfiguration);
    shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(
        new ComplexShardingStrategyConfiguration("user_id",
            "com.infi.shardingjdbc.sharding.DBShardingAlgorithm"));
    // 不分表
    shardingRuleConfig
        .setDefaultTableShardingStrategyConfig(new NoneShardingStrategyConfiguration());
    shardingRuleConfig.getMasterSlaveRuleConfigs().addAll(masterSlaveRuleConfigs);
    return shardingRuleConfig;
  }

  @Bean("orderTxManager")
  public PlatformTransactionManager orderTxManager(
      @Qualifier("orderDataSource") DataSource dataSource) {
    DataSourceTransactionManager txManager = new DataSourceTransactionManager(dataSource);
    return txManager;
  }

  @Bean("orderSqlSessionFactory")
  public SqlSessionFactoryBean orderSqlSessionFactory(
      @Qualifier("orderDataSource") DataSource dataSource) throws IOException {
    SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
    factoryBean.setDataSource(dataSource);
    factoryBean.setTypeAliasesPackage("com.infi.shardingjdbc.internal.entity");
    factoryBean.setMapperLocations(RESOURCE_RESOLVER.getResources("classpath:mapper/*.xml"));
    Interceptor[] plugins = new Interceptor[] {new SqlLogIntercepter()};
    factoryBean.setPlugins(plugins);
    // mybatis configuration
    org.apache.ibatis.session.Configuration configuration =
        new org.apache.ibatis.session.Configuration();
    configuration.setCacheEnabled(false);
    configuration.setMapUnderscoreToCamelCase(true);
    factoryBean.setConfiguration(configuration);
    return factoryBean;
  }

  private List<MasterSlaveRuleConfiguration> buildMasterSlaveRuleConfig(DBProperties dbProperties) {
    List<MasterSlaveRuleConfiguration> masterSlaveRuleConfigs = Lists.newArrayList();
    for (Map.Entry<String, DBUrl> dbUrls : dbProperties.getDbUrls().entrySet()) {
      MasterSlaveRuleConfiguration masterSlaveRuleConfig = new MasterSlaveRuleConfiguration();
      String dbName = dbUrls.getKey();
      DBUrl dbUrl = dbUrls.getValue();
      masterSlaveRuleConfig.setName(dbName);
      masterSlaveRuleConfig
          .setLoadBalanceAlgorithmType(MasterSlaveLoadBalanceAlgorithmType.ROUND_ROBIN);
      masterSlaveRuleConfig.setMasterDataSourceName(dbName + "_master");
      masterSlaveRuleConfig
          .setSlaveDataSourceNames(IntStream.rangeClosed(0, dbUrl.getSlaveUrls().size() - 1)
              .mapToObj(i -> dbName + "_slave" + i).collect(Collectors.toList()));
      masterSlaveRuleConfigs.add(masterSlaveRuleConfig);
    }
    return masterSlaveRuleConfigs;
  }

  private Map<String, DataSource> buildDataSourceMap(DBProperties dbProperties)
      throws SQLException {
    Map<String, DataSource> dataSourceMap = Maps.newHashMap();
    for (Map.Entry<String, DBUrl> dbUrls : dbProperties.getDbUrls().entrySet()) {
      String dbName = dbUrls.getKey();
      DBUrl dbUrl = dbUrls.getValue();
      dataSourceMap.put(dbName + "_master",
          DuridDataSourceFactory.createDataSource(dbUrl.getMasterUrl(), dbProperties));
      for (int i = 0; i < dbUrl.getSlaveUrls().size(); i++) {
        dataSourceMap.put(dbName + "_slave" + i,
            DuridDataSourceFactory.createDataSource(dbUrl.getSlaveUrls().get(i), dbProperties));
      }
    }
    return dataSourceMap;
  }
}
