//package com.example.demo.processor;
//
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
//import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//
//import java.util.Map;
//
///**
// * @author jiejT
// * @ClassName: MutiDataSourceProcessor
// * @description: 配合mutlitransactionalAspect
// * @date 2021/11/15
// */
//public class MutiDataSourceProcessor implements  BeanFactoryPostProcessor {
//
//    @Override
//    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
//        //1、搜集事务管理器的名称
//        Map<String, DataSourceTransactionManager> map = configurableListableBeanFactory.getBeansOfType(DataSourceTransactionManager.class);
//    }
//}
