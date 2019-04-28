package org.lcn.core.core;

import java.sql.Connection;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.lcn.core.bean.LubanConnection;
import org.lcn.core.bean.LubanTransactionManager;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LubanDatasourceAspect {
	@Around("execution(* javax.sql.DataSource.getConnection(..))")
	public Connection around(ProceedingJoinPoint point){
		try {
			if(LubanTransactionManager.getLubanTransaction()!=null) {
				return new LubanConnection((Connection)point.proceed(),LubanTransactionManager.getLubanTransaction());
			}else {
				return (LubanConnection) point.proceed();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
}