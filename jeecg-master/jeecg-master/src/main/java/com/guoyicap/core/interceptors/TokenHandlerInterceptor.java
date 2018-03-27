package com.guoyicap.core.interceptors;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.core.util.ResourceUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.guoyicap.core.handlers.TokenHandler;

/**
 * @author guang
 * 令牌拦截器，生成令牌口令
 */
public class TokenHandlerInterceptor implements HandlerInterceptor {
	private List<String> includeUrls;//需要令牌口令的url
	
	public List<String> getIncludeUrls() {
		return includeUrls;
	}

	public void setIncludeUrls(List<String> includeUrls) {
		this.includeUrls = includeUrls;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		//用户请求url
		String requestPath = ResourceUtil.getRequestPath(request);
		if (includeUrls.contains(requestPath)) {
			// 如果该请求在拦截范围内
			//生成令牌口令
			TokenHandler.generateGUID(request.getSession());
		}

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
