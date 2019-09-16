package com.ecmp.apigateway.apigateway;

import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

@Service
public class RefreshService implements ApplicationEventPublisherAware {



    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    public void refresh(){
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
    }
}
