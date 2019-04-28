package org.lcn.core.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.lcn.core.core.ApplicationConfig;
import org.lcn.core.core.HttpClient;
import org.lcn.core.core.LubanDatasourceAspect;
import org.lcn.core.core.LubanTransactionalAspect;
import org.lcn.core.core.NettyClientHandler;
import org.lcn.core.interceptor.InterceptorConfig;
import org.lcn.core.interceptor.RequestInterceptor;
import org.springframework.context.annotation.Import;

@Import({ ApplicationConfig.class, HttpClient.class, LubanDatasourceAspect.class, LubanTransactionalAspect.class,
		 ApplicationConfig.class, NettyClientHandler.class, InterceptorConfig.class,
		RequestInterceptor.class,NettyClientHandler.class})
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableLCNClient {

}
