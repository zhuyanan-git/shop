package com.qfant.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    /**
     * @return  mysql数据库
     */
    @Primary    //配置该数据源为主数据源
    @Bean(name = "wxDataSource")
    @Qualifier(value = "wxDataSource")  //spring装配bean的唯一标识
    @ConfigurationProperties(prefix = "wx.spring.datasource")   //application.properties配置文件中该数据源的配置前缀
    public DataSource wxDataSource(){
        return DataSourceBuilder.create().build();
    }

    /**
     * @return   sqlserver数据库
     */
    @Bean(name = "gjpDataSource")
    @Qualifier(value = "gjpDataSource")
    @ConfigurationProperties(prefix = "gjp.spring.datasource")
    public DataSource gjpDataSource(){
        return DataSourceBuilder.create().build();
    }
}
