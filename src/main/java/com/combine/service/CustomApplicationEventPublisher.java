package com.combine.service;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

/**
 * Simply implements ApplicationEventPublisherAware and provides a method "publish" that all publishers should implement
 *
 * @author lfallon
 */
public abstract class CustomApplicationEventPublisher implements ApplicationEventPublisherAware {

    protected ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    public void publish(ApplicationEvent event) {
        this.publisher.publishEvent(event);
    }
}
