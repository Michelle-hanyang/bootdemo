package com.example.bootdemo.config;

import com.amm.config.dds.DynamicDataSource;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: YANG
 * Date: 2022/10/9 16:32
 * Describe:
 */
@SuppressWarnings("AlibabaRemoveCommentedCode")
@Configuration
//@MapperScan("com.sys.**.dao")
@MapperScan(basePackages = "com.amm.**.dao")
public class MybatisConfig {
    @Bean("mam")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.mam")
    public DataSource mam() {
        return DataSourceBuilder.create().build();
    }

    @Bean("gvm")
    @ConfigurationProperties(prefix = "spring.datasource.gvm")
    public DataSource gvm() {
        return DataSourceBuilder.create().build();
    }

    @Bean("sam")
    @ConfigurationProperties(prefix = "spring.datasource.sam")
    public DataSource sam() {
        return DataSourceBuilder.create().build();
    }

    @Bean("aam")
    @ConfigurationProperties(prefix = "spring.datasource.amm")
    public DataSource amm() {
        return DataSourceBuilder.create().build();
    }

    @Bean("dynamicDataSource")
    public DataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object, Object> dataSourceMap = new HashMap<>(4);
        dataSourceMap.put("mam", mam());
        dataSourceMap.put("gvm", gvm());
        dataSourceMap.put("sam", sam());
        dataSourceMap.put("amm", amm());
        // 将 master 数据源作为默认指定的数据源
        dynamicDataSource.setDefaultDataSource(amm());
        // 将 master 和 slave 数据源作为指定的数据源
        dynamicDataSource.setDataSources(dataSourceMap);
        return dynamicDataSource;
    }
@Bean(name="globalConfig")
@ConfigurationProperties(prefix = "mybatis-plus.global-config")
public GlobalConfig globalConfig(){
        GlobalConfig globalConfig =new GlobalConfig();
        return globalConfig;
}
    @Bean
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
        MybatisSqlSessionFactoryBean sessionFactory=new MybatisSqlSessionFactoryBean();
//        sessionFactory.setDataSource(dynamicDataSource());
        sessionFactory.setGlobalConfig(globalConfig());
//        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
//        MybatisConfiguration configuration = new MybatisConfiguration();
//        configuration.addInterceptor(new PaginationInterceptor());
//        sessionFactory.setConfiguration(configuration);
        // 配置数据源，此处配置为关键配置，如果没有将 dynamicDataSource作为数据源则不能实现切换
        sessionFactory.setDataSource(dynamicDataSource());
        sessionFactory.setTypeAliasesPackage("com.amm.**.pojo");    // 扫描Model
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setMapperLocations(resolver.getResources("classpath*:mapper/*.xml"));    // 扫描映射文件
        return sessionFactory.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        // 配置事务管理, 使用事务时在方法头部添加@Transactional注解即可
        return new DataSourceTransactionManager(dynamicDataSource());
    }
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

}
