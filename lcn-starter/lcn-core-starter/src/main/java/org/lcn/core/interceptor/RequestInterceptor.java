package org.lcn.core.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lcn.core.bean.LCNTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
@Component
public class RequestInterceptor implements HandlerInterceptor {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String groupId = request.getHeader("groupId");
		String transactionCount = request.getHeader("transactionCount");
		System.out.println("当前第"+transactionCount+"个了");
		LCNTransactionManager.setThreadLocalGroupId(groupId);
		LCNTransactionManager.setThreadLocaltransactionCount(Integer.parseInt(transactionCount==null?"0":transactionCount));
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
