package org.lcn.core.core;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.lcn.core.anno.LCNTransactional;
import org.lcn.core.bean.LCNTransaction;
import org.lcn.core.bean.LCNTransactionManager;
import org.lcn.core.bean.TransactionType;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Aspect
@Slf4j
public class LCNTransactionalAspect implements Ordered{
	@Around("@annotation(org.lcn.core.anno.LCNTransactional)")
	public void invoke(ProceedingJoinPoint point) {
		MethodSignature seSignature=(MethodSignature) point.getSignature();
		Method method =seSignature.getMethod();
		LCNTransactional lubanTransactional=method.getAnnotation(LCNTransactional.class);
		log.info(String.valueOf(lubanTransactional.isStart()));
		//创建事务组
		String groupId=null;
		if(lubanTransactional.isStart()) {
			groupId=LCNTransactionManager.createLcnTransactionGroup();
		}else {
			groupId=LCNTransactionManager.getThreadLocalGroupId();
		}
		log.info("groupId["+groupId+"]");
		LCNTransaction lubanTransaction=LCNTransactionManager.createLcnTransaction(groupId);
		//提交事务到事务组
		 try {
			point.proceed();
			LCNTransactionManager.addLcnTransaction(lubanTransaction, lubanTransactional.isEnd(), TransactionType.commit);
		} catch (Exception e) {
			LCNTransactionManager.addLcnTransaction(lubanTransaction, lubanTransactional.isEnd(), TransactionType.rollback);
			e.printStackTrace();
		} catch (Throwable e) {
			LCNTransactionManager.addLcnTransaction(lubanTransaction, lubanTransactional.isEnd(), TransactionType.rollback);
			e.printStackTrace();
		}
	}

	@Override
	public int getOrder() {
		return 10000;
	}
}
