package org.lcn.core.core;

import java.sql.Connection;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.lcn.core.bean.LCNConnection;
import org.lcn.core.bean.LCNTransactionManager;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LCNDatasourceAspect {
	@Around("execution(* javax.sql.DataSource.getConnection(..))")
	public Connection around(ProceedingJoinPoint point){
		try {
			if(LCNTransactionManager.getLcnTransaction()!=null) {
				return new LCNConnection((Connection)point.proceed(),LCNTransactionManager.getLcnTransaction());
			}else {
				return (LCNConnection) point.proceed();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
}