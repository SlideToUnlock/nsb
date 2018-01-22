package com.nsb.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.IOException;

/**
 * @Author:langxy
 * @date 创建时间：2018/1/4 18:11
 */
@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
@MapperScan(basePackages = "com.nsb.dao")
public class DataSourceConfig {

    private Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private int initialSize;
    private int maxActive;
    private int minIdle;
    private int maxWait;
    private boolean defaultAutoCommit;
    private int minEvictableIdleTimeMillis;
    private boolean testWhileIdle;
    private String validationQuery;

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }

    public boolean isDefaultAutoCommit() {
        return defaultAutoCommit;
    }

    public void setDefaultAutoCommit(boolean defaultAutoCommit) {
        this.defaultAutoCommit = defaultAutoCommit;
    }

    public int getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(int minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    @Bean(name = "dataSource")
    public DruidDataSource druidDataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(getDriverClassName());
        dataSource.setUrl(getUrl());
        dataSource.setUsername(getUsername());
        dataSource.setPassword(getPassword());
        dataSource.setInitialSize(getInitialSize());
        dataSource.setMaxActive(getMaxActive());
        dataSource.setMaxWait(getMaxWait());
        dataSource.setMinIdle(getMinIdle());
        dataSource.setDefaultAutoCommit(isDefaultAutoCommit());
        dataSource.setMinEvictableIdleTimeMillis(getMinEvictableIdleTimeMillis());
        dataSource.setTestWhileIdle(isTestWhileIdle());
        dataSource.setValidationQuery(getValidationQuery());
        logger.info("dataSource加载完成", dataSource);
        return dataSource;
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactoryBean sqlSessionFactory() throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath*:mappers/*Mapper.xml"));
        sqlSessionFactoryBean.setDataSource(this.druidDataSource());
        logger.info("sqlSessionFactory配置完成", sqlSessionFactoryBean);
        return sqlSessionFactoryBean;
    }

    @Bean(name = "filterRegistrationBean")
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico, /druid/*");
        logger.info(" druid filter register : {} ", filterRegistrationBean);
        return filterRegistrationBean;
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager() throws Exception {
        DataSourceTransactionManager txManager = new DataSourceTransactionManager();
        txManager.setDataSource(this.druidDataSource());
        return txManager;
    }
}
