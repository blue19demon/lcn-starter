package org.lcn.core.core;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.lcn.core.anno.LubanTransactional;
import org.lcn.core.bean.LubanTransaction;
import org.lcn.core.bean.LubanTransactionManager;
import org.lcn.core.bean.TransactionType;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Aspect
@Slf4j
public class LubanTransactionalAspect implements Ordered{
	@Around("@annotation(org.lcn.core.anno.LubanTransactional)")
	public void invoke(ProceedingJoinPoint point) {
		MethodSignature seSignature=(MethodSignature) point.getSignature();
		Method method =seSignature.getMethod();
		LubanTransactional lubanTransactional=method.getAnnotation(LubanTransactional.class);
		log.info(String.valueOf(lubanTransactional.isStart()));
		//创建事务组
		String groupId=null;
		if(lubanTransactional.isStart()) {
			groupId=LubanTransactionManager.createLubanTransactionGroup();
		}else {
			groupId=LubanTransactionManager.getThreadLocalGroupId();
		}
		log.info("groupId["+groupId+"]");
		LubanTransaction lubanTransaction=LubanTransactionManager.createLubanTransaction(groupId);
		//提交事务到事务组
		 try {
			point.proceed();
			LubanTransactionManager.addLubanTransaction(lubanTransaction, lubanTransactional.isEnd(), TransactionType.commit);
		} catch (Exception e) {
			LubanTransactionManager.addLubanTransaction(lubanTransaction, lubanTransactional.isEnd(), TransactionType.rollback);
			e.printStackTrace();
		} catch (Throwable e) {
			LubanTransactionManager.addLubanTransaction(lubanTransaction, lubanTransactional.isEnd(), TransactionType.rollback);
			e.printStackTrace();
		}
	}

	@Override
	public int getOrder() {
		return 10000;
	}
}
