package com.combine.service;

import com.combine.model.College;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Config {

    @Value("${datasource.url}")
    private String dbUrl;

    @Value("${datasource.username}")
    private String dbUsername;

    @Value("${datasource.password}")
    private String dbPassword;

    @Value("${datasource.driver-class-name}")
    private String dbDriver;

    @Value("${rabbit.mq.routingkeys.progress}")
    private String importProgressRoutingKey;

    private List<College> colleges = null;

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public String getDbDriver() {
        return dbDriver;
    }

    public String getImportProgressRoutingKey() {
        return importProgressRoutingKey;
    }
}
