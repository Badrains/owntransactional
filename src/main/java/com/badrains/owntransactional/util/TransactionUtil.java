package com.badrains.owntransactional.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

/**
 * @description:
 * @author: wangdong
 * @date: 2020/5/13 18:56
 */
@Component
@Scope("prototype")//声明为多例，解决线程安全问题
public class TransactionUtil {
    /**
     * 全局接受事物状态
     */
    private TransactionStatus transactionStatus;
    /**
     * 获取事务源
     */
    @Autowired
    private DataSourceTransactionManager dataSourceTransactionManager;

    /**
     * 开启事务
     * @return
     */
    public TransactionStatus begin(){
        System.out.println("事物开启");
        transactionStatus = dataSourceTransactionManager.getTransaction(new DefaultTransactionAttribute());
        return transactionStatus;
    }

    /**
     * 事务提交
     * @param transactionStatus
     */
    public void commit(TransactionStatus transactionStatus){
        System.out.println("提交事务");
        if(dataSourceTransactionManager != null){
            dataSourceTransactionManager.commit(transactionStatus);
        }
    }

    /**
     * 事务回滚
     * @param transactionStatus
     */
    public void rollback(TransactionStatus transactionStatus){
        System.out.println("回滚事务");
        if(dataSourceTransactionManager != null){
            dataSourceTransactionManager.rollback(transactionStatus);
        }
    }

}
