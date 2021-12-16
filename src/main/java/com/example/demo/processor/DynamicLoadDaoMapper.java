//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.demo.processor;

import com.example.demo.annotation.DaoLoad;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;
import java.util.Map.Entry;
import javax.sql.DataSource;

import com.example.demo.base.dao.BaseDAO;
import com.example.demo.mybatis.LoadDAOClasses;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

@Component("DynamicLoadDaoMapper")
@PropertySource("classpath:application.yml")
public class DynamicLoadDaoMapper implements ApplicationContextAware, BeanPostProcessor {
    @Value("${mybatis.mapper-locations}")
    private String mapperLocation;

    @Value("${mybatis.type-aliases-package}")
    private String basePath;

    protected final Log logger = LogFactory.getLog(this.getClass());
    private DefaultListableBeanFactory beanFactory;
    private Map<String, String> isExistBean = new HashMap();
    private Map<String, DataSource> dateSourceMap = new HashMap();
    private List<AnnotationTypeFilter> typeFilters = new ArrayList();

    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) context.getAutowireCapableBeanFactory();
        LoadDAOClasses loadDAOClasses = new LoadDAOClasses(basePath);
        Map<String,Set<String>> mapPackages = null;

        try {
            mapPackages = loadDAOClasses.getClassMap();
        } catch (IOException var6) {
            if (this.logger.isInfoEnabled()) {
                this.logger.error(String.format("自动加载 Mybatis DAO 失败："), var6);
            }
        }
        Map<String, Object> daoLMap = context.getBeansWithAnnotation(DaoLoad.class);
        //初始化数据源
        dateSourceMap = context.getBeansOfType(DataSource.class);
        String []  arr = context.getBeanNamesForType(BaseDAO.class);
        for (String str:arr){
            System.out.println(str);
        }
        this.registerDAOBean(mapPackages);
    }

    private void registerDAOBean(Map<String, Set<String>> mapPackages) {
        for (Entry<String, Set<String>> classes : mapPackages.entrySet()) {
            String dataSourceName = classes.getKey();
            if (isExistBean(dataSourceName)) {
                String sessionFactoryKey = dataSourceName + "/sessionFactory";
                if (!isExistBean.containsKey(dataSourceName)) {
                    BeanDefinitionBuilder sessionFactoryBeanDefinition = BeanDefinitionBuilder.rootBeanDefinition(SqlSessionFactoryBean.class);
                    // 多租户动态数据源
                    //设置数据源
                    sessionFactoryBeanDefinition.addPropertyReference("dataSource", dataSourceName);
                    //设置mybatis配置文件路径
                    sessionFactoryBeanDefinition.addPropertyValue("configLocation", null);
                    //设置该数据源下的所有Mapper.xml
                    try {
                        sessionFactoryBeanDefinition.addPropertyValue("mapperLocations", new PathMatchingResourcePatternResolver().getResources(mapperLocation));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    sessionFactoryBeanDefinition.addPropertyValue("vfs", SpringBootVFS.class);
                    this.beanFactory.registerBeanDefinition(sessionFactoryKey, sessionFactoryBeanDefinition.getBeanDefinition());
                    isExistBean.put(dataSourceName, sessionFactoryKey);
                }
                if (classes.getValue().size() > 0) {
                    for (String strClazz : classes.getValue()) {
                        //这里应该要不同的mapper不同的MapperFactoryBean
                        String mapperKey = dataSourceName + "/mapper/" + strClazz;
                        BeanDefinitionBuilder bdb = BeanDefinitionBuilder.rootBeanDefinition(MapperFactoryBean.class);
                        //这里需要mapper接口class
                        try {
                            bdb.addPropertyValue("mapperInterface", Class.forName(strClazz));
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        bdb.addPropertyReference("sqlSessionFactory", sessionFactoryKey);
                        this.beanFactory.registerBeanDefinition(mapperKey, bdb.getBeanDefinition());
                    }
                }
            }
        }
    }

    private void sqlAdapter(String dataSourceName, String sessionFactoryId) {

    }

    private boolean isExistBean(String beanName) {
        boolean isExist = false;

        try {
            isExist = null != this.beanFactory.getBean(beanName);
        } catch (NoSuchBeanDefinitionException var4) {
            isExist = false;
        }

        return isExist;
    }

//    public List<String> getScanPackages() {
//        return this.scanPackages;
//    }
//
//    public void setScanPackages(List<String> scanPackages) {
//        this.scanPackages = scanPackages;
//    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
