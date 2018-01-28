package com.combine;

import com.combine.controller.StartImportEventConsumer;
import com.combine.service.Config;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.beans.PropertyVetoException;

@Configuration
public class ServletConfiguration {

    @Autowired
    private Config appConfig;

    @Bean(name = "dataSource")
    @Qualifier("primary")
    public ComboPooledDataSource dataSource() {
        ComboPooledDataSource driverManagerDataSource = new ComboPooledDataSource();
        try {
            driverManagerDataSource.setDriverClass(appConfig.getDbDriver());
            driverManagerDataSource.setJdbcUrl(appConfig.getDbUrl());
            driverManagerDataSource.setUser(appConfig.getDbUsername());
            driverManagerDataSource.setPassword(appConfig.getDbPassword());
            driverManagerDataSource.setMaxPoolSize(1000);
            driverManagerDataSource.setMinPoolSize(10);
            driverManagerDataSource.setMaxStatements(5);
            driverManagerDataSource.setStatementCacheNumDeferredCloseThreads(1);
            driverManagerDataSource.setNumHelperThreads(20);
        } catch (PropertyVetoException e) {
            String message = "An error has occurred.Please contact System Administrator";
            System.out.println(message + ": " + e.getMessage());
        }
        return driverManagerDataSource;
    }

    @Bean
    @Qualifier("parserEventExchange")
    public Exchange eventExchange() {
        return new TopicExchange("parserEventExchange");
    }

    @Bean
    public Queue queue() {
        return new Queue("parserInitializeQueue");
    }

    @Bean
    public Binding binding(Queue queue, Exchange eventExchange) {
        return BindingBuilder
                .bind(queue)
                .to(eventExchange)
                .with("nflcombine.refresh.start")
                .noargs();
    }

    @Bean
    public StartImportEventConsumer eventReceiver() {
        return new StartImportEventConsumer();
    }

}
