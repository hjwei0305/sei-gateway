package com.ecmp.apigateway.dao;

import com.ecmp.apigateway.model.UserCounter;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * usage:
 * <p>
 * </p>
 * User:liusonglin; Date:2018/6/22;ProjectName:api-gateway;
 */
public interface UserCounterDao extends ElasticsearchRepository<UserCounter,String> {
}
