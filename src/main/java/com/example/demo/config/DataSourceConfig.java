package com.example.demo.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;


/**
 * @author jiejT
 * @ClassName: TransactionConfiguration
 * @description: TODO
 * @date 2021/11/15
 */
@Configuration
@PropertySource("classpath:application.yml")
public class DataSourceConfig {
    @Value("${mybatis.mapper-locations}")
    private String mapperLocation;

    /**
     * 创建SqlSessionFactory
     */
//    @Bean(name = "defaultSqlSessionFactory")
//    @Primary
//    public SqlSessionFactory sqlSessionFactory(@Qualifier("default") DataSource dataSource, MybatisProperties mybatisProperties) throws Exception {
//        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
//        bean.setDataSource(dataSource);
//        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocation));
//        bean.setConfiguration(mybatisProperties.getConfiguration());
//        bean.setVfs(SpringBootVFS.class);
//        return bean.getObject();
//    }
//
//    @Bean(name = "test1SqlSessionFactory")
//    @Primary
//    public SqlSessionFactory sqlSessionFactory1(@Qualifier("test1") DataSource dataSource, MybatisProperties mybatisProperties) throws Exception {
//        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
//        bean.setDataSource(dataSource);
//        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocation));
//        bean.setConfiguration(mybatisProperties.getConfiguration());
//        bean.setVfs(SpringBootVFS.class);
//        return bean.getObject();
//    }

    /**
     * 配置事务
     */
    @Bean(name = "defaultTransactionManager")
    @Primary
    public DataSourceTransactionManager transactionManager1(@Qualifier("default") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "test1TransactionManager")
    @Primary
    public DataSourceTransactionManager transactionManager2(@Qualifier("test1") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * 创建SqlSessionTemplate
     */
//    @Bean(name = "defaultSqlSessionTemplate")
//    @Primary
//    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("defaultSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }
//
//    @Bean(name = "test1SqlSessionTemplate")
//    @Primary
//    public SqlSessionTemplate sqlSessionTemplate1(@Qualifier("test1SqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }

    @Bean(name="default")
    @ConfigurationProperties(prefix = "spring.datasource.default")
    public DataSource druid1() {
        return new DruidDataSource();
    }

    @Bean(name="test1")
    @ConfigurationProperties(prefix = "spring.datasource.test1")
    public DataSource druid2() {
        return new DruidDataSource();
    }
}
