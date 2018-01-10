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
@PropertySource("classpath:druid.properties")
@MapperScan(basePackages = "com.nsb.dao")
public class DataSourceConfig {

    private Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

    private String driverClassName;
    private String url;
    private String username;
    private String password;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(){
        return new PropertySourcesPlaceholderConfigurer();
    }

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

    @Value("${druid.initialSize}")
    private int initialSize;

    @Value("${druid.maxActive}")
    private int maxActive;

    @Value("${druid.maxWait}")
    private int maxWait;

    @Value("${druid.minIdle}")
    private int minIdle;

    @Value("${druid.defaultAutoCommit}")
    private boolean defaultAutoCommit;

    @Value("${druid.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;

    @Bean(name = "dataSource")
    public DruidDataSource druidDataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(this.getDriverClassName());
        dataSource.setUrl(this.getUrl());
        dataSource.setUsername(this.getUsername());
        dataSource.setPassword(this.getPassword());
        dataSource.setInitialSize(initialSize);
        dataSource.setMaxActive(maxActive);
        dataSource.setMaxWait(maxWait);
        dataSource.setMinIdle(minIdle);
        dataSource.setDefaultAutoCommit(defaultAutoCommit);
        dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        dataSource.setTestWhileIdle(true);
        dataSource.setValidationQuery("SELECT 1 FROM information");
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

    @Bean
    public ServletRegistrationBean druidServlet() {

        ServletRegistrationBean reg = new ServletRegistrationBean();
        reg.setServlet(new StatViewServlet());
        reg.addUrlMappings("/druid/*");
        reg.addInitParameter("allow", "127.0.0.1");
        reg.addInitParameter("loginUsername", "bhz");
        reg.addInitParameter("loginPassword", "bhz");
        logger.info(" druid console manager init : {} ", reg);
        logger.info("druid连接池图形界面配置完成", reg);
        return reg;
    }
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico, /druid/*");
        logger.info(" druid filter register : {} ", filterRegistrationBean);
        return filterRegistrationBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws Exception {
        DataSourceTransactionManager txManager = new DataSourceTransactionManager();
        txManager.setDataSource(this.druidDataSource());
        return txManager;
    }
}
