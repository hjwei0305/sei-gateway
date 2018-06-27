package com.ecmp.apigateway.service.impl;

import com.google.common.collect.Maps;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * usage:
 * <p>
 * </p>
 * User:liusonglin; Date:2018/6/27;ProjectName:api-gateway;
 */
@Service
public class UserCountService {

    @Autowired
    private TransportClient client;

    public static final String FIELD_NAME = "counter";


    public Map<Date,Double> getRangCounter(Long interval) {

        Map<Date,Double> result = Maps.newTreeMap();

        AbstractAggregationBuilder aggregation = AggregationBuilders.
                dateHistogram("group_by_time").field("addTime")
                .interval(1000*60*interval).minDocCount(0L);

        //counter 平均值
        AbstractAggregationBuilder avgcount = AggregationBuilders.avg(FIELD_NAME).field(FIELD_NAME);

        SearchResponse response = client.prepareSearch("usercounter")
                .setTypes("userCounter")
                .addAggregation(aggregation.subAggregation(avgcount))
                .execute()
                .actionGet();

        Aggregations aggs = response.getAggregations();
        InternalDateHistogram agg = aggs.get("group_by_time");
        List<InternalDateHistogram.Bucket> buckets =  agg.getBuckets();

        for(InternalDateHistogram.Bucket bucket:buckets){
            String date = bucket.getKeyAsString();
            Map<String, Aggregation> map = bucket.getAggregations().asMap();
            InternalAvg count = (InternalAvg) map.get(FIELD_NAME);
            result.put(new Date(Long.parseLong(date)),count.getValue());
        }

        return result;
    }
}
