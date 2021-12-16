package com.example.demo.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.thymeleaf.util.ArrayUtils;

import java.util.Stack;

/**
 * @author jiejT
 * @ClassName: MultiTransactionalAspect
 * @description: 这个事务管理暂时保存不再开发，等到其他加入之后无效再使用
 * @date 2021/11/11
 */
@Aspect
@Component
public class MultiTransactionalAspect implements ApplicationContextAware{
    private Logger logger = LoggerFactory.getLogger(getClass());

    private ApplicationContext ContextHolder;

    private String[] transactionMangerNames;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ContextHolder = applicationContext;
        transactionMangerNames = ContextHolder.getBeanNamesForType(DataSourceTransactionManager.class);
    }

    @Pointcut("execution(* com.example.demo.hello.service..*(..))")
    public void transactionMangers(){

    }

    @Around("transactionMangers()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

        Stack<DataSourceTransactionManager> dataSourceTransactionManagerStack = new Stack<DataSourceTransactionManager>();
        Stack<TransactionStatus> transactionStatuStack = new Stack<TransactionStatus>();

        try {

            if (!openTransaction(dataSourceTransactionManagerStack,
                    transactionStatuStack)) {
                return null;
            }
            System.out.println("所有事务管理器打开了");
            Object ret = pjp.proceed();
            System.out.println("所有事务管理器提交了");
            commit(dataSourceTransactionManagerStack, transactionStatuStack);

            return ret;
        } catch (Throwable e) {

            rollback(dataSourceTransactionManagerStack, transactionStatuStack);

            logger.error(String.format(
                    "MultiTransactionalAspect, method:%s-%s occors error:", pjp
                            .getTarget().getClass().getSimpleName(), pjp
                            .getSignature().getName()), e);
            throw e;
        }
    }

    /**
     * @author Zhu
     * @date 2015-7-25下午7:55:46
     * @description
     * @param dataSourceTransactionManagerStack
     * @param transactionStatuStack
     * @param values
     */
    private boolean openTransaction(
            Stack<DataSourceTransactionManager> dataSourceTransactionManagerStack,
            Stack<TransactionStatus> transactionStatuStack) {

        if (ArrayUtils.isEmpty(transactionMangerNames)) {
            return false;
        }
        for (String beanName : transactionMangerNames) {
            System.out.println("事务管理管理器名称："+beanName);
            DataSourceTransactionManager dataSourceTransactionManager = (DataSourceTransactionManager) ContextHolder
                    .getBean(beanName);
            TransactionStatus transactionStatus = dataSourceTransactionManager
                    .getTransaction(new DefaultTransactionDefinition());
            transactionStatuStack.push(transactionStatus);
            dataSourceTransactionManagerStack
                    .push(dataSourceTransactionManager);
        }
        return true;
    }

    /**
     * @author Zhu
     * @date 2015-7-25下午7:56:39
     * @description
     * @param dataSourceTransactionManagerStack
     * @param transactionStatuStack
     */
    private void commit(
            Stack<DataSourceTransactionManager> dataSourceTransactionManagerStack,
            Stack<TransactionStatus> transactionStatuStack) {
        while (!dataSourceTransactionManagerStack.isEmpty()) {
            dataSourceTransactionManagerStack.pop().commit(
                    transactionStatuStack.pop());
        }
    }

    /**
     * @author Zhu
     * @date 2015-7-25下午7:56:42
     * @description
     * @param dataSourceTransactionManagerStack
     * @param transactionStatuStack
     */
    private void rollback(
            Stack<DataSourceTransactionManager> dataSourceTransactionManagerStack,
            Stack<TransactionStatus> transactionStatuStack) {
        while (!dataSourceTransactionManagerStack.isEmpty()) {
            dataSourceTransactionManagerStack.pop().rollback(
                    transactionStatuStack.pop());
        }
    }


}
