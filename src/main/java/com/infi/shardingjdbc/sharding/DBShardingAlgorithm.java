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
package com.infi.shardingjdbc.sharding;

import java.util.Collection;
import java.util.stream.Collectors;

import org.assertj.core.util.Lists;
import org.springframework.util.CollectionUtils;

import io.shardingjdbc.core.api.algorithm.sharding.ListShardingValue;
import io.shardingjdbc.core.api.algorithm.sharding.PreciseShardingValue;
import io.shardingjdbc.core.api.algorithm.sharding.RangeShardingValue;
import io.shardingjdbc.core.api.algorithm.sharding.ShardingValue;
import io.shardingjdbc.core.api.algorithm.sharding.complex.ComplexKeysShardingAlgorithm;
import io.shardingjdbc.core.exception.ShardingJdbcException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hongtao
 * @version v 0.1 , 2018年1月4日 下午5:28:42
 * @since JDK 1.8
 */
@Slf4j
public class DBShardingAlgorithm implements ComplexKeysShardingAlgorithm {

  @SuppressWarnings("unchecked")
  @Override
  public Collection<String> doSharding(Collection<String> availableTargetNames,
      Collection<ShardingValue> shardingValues) {
    log.info("availableTargetNames = {}", availableTargetNames);
    if (CollectionUtils.isEmpty(shardingValues)) {
      throw new ShardingJdbcException("Invalid query without any partition column.");
    }
    // sharding for only one column
    ShardingValue shardingValue = shardingValues.iterator().next();
    if (shardingValue instanceof PreciseShardingValue) {
      Long userId = ((PreciseShardingValue<Long>) shardingValue).getValue();
      return Lists.newArrayList("order_db" + userId % 2);
    } else if (shardingValue instanceof ListShardingValue) {
      Collection<Long> userIds = ((ListShardingValue<Long>) shardingValue).getValues();
      return userIds.stream().map(userId -> "order_db" + userId % 2).collect(Collectors.toSet());
    } else if (shardingValue instanceof RangeShardingValue) {
    }
    throw new ShardingJdbcException("Not supported sharding type");
  }

}
