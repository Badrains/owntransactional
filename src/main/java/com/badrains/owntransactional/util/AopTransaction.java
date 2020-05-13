package com.badrains.owntransactional.util;

import com.badrains.owntransactional.annotation.MyAnnotation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.lang.reflect.Method;

/**
 * @description:
 * @author: wangdong
 * @date: 2020/5/13 19:15
 */
@Aspect
@Component
public class AopTransaction {
    @Autowired
    private TransactionUtil transactionUtil;

    private TransactionStatus transactionStatus;

    @Around("execution(* com.badrains.owntransactional.*.*(..))")
    public void around(ProceedingJoinPoint pjp) throws Throwable {
        //-----获取方法的注解
        MyAnnotation annotation = this.getMethodMyAnnotation(pjp);
        transactionStatus = begin(annotation);
        pjp.proceed();
        commit(transactionStatus);
    }

    /**
     * 开启事务
     *
     * @param annotation
     * @return
     */
    private TransactionStatus begin(MyAnnotation annotation) {
        if (annotation == null) {
            return null;
        }
        return transactionUtil.begin();
    }

    /**
     * 事务提交
     *
     * @param transactionStatus
     */
    private void commit(TransactionStatus transactionStatus) {
        if (transactionStatus != null) {
            transactionUtil.commit(transactionStatus);
        }
    }

    @AfterThrowing("execution(* com.badrains.owntransactional.*.*(..))")
    public void afterThrowing() {
        if (transactionStatus != null) {
            transactionUtil.rollback(transactionStatus);
        }
    }

    /**
     * 获取代理方法上面的事务注解
     *
     * @param pjp
     * @return
     * @throws Exception
     */
    private MyAnnotation getMethodMyAnnotation(ProceedingJoinPoint pjp) throws Exception {
        //-----获取代理对象的方法
        String methodName = pjp.getSignature().getName();
        //-----获取目标对象
        Class<?> classTarget = pjp.getTarget().getClass();
        //-----获取目标对象类型
        Class<?>[] par = ((MethodSignature) pjp.getSignature()).getParameterTypes();
        //-----获取目标方法
        Method method = classTarget.getMethod(methodName, par);
        //-----获取方法上面的注解
        return method.getDeclaredAnnotation(MyAnnotation.class);
    }
}
