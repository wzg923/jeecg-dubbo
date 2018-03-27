package com.guoyicap.core.interceptors;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.core.util.ResourceUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * URL过滤拦截器
 * 配置在字符编码拦截器、权限控制拦截器之后，主要用于拦截前台页面url访问
 * @author guang 
 * @since[2015/11/16] 
 * @version[v1.0]
 */
public class UrlInterceptor implements HandlerInterceptor {
	private List<String> excludeUrls;

	public List<String> getExcludeUrls() {
		return excludeUrls;
	}

	public void setExcludeUrls(List<String> excludeUrls) {
		this.excludeUrls = excludeUrls;
	}

	/**
	 * 在controller前拦截
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		String conString = "";
		// 获取父url--如果不是直接输入的话就是先前的访问过来的页面，要是用户输入了，这个父url是不存在的
		conString = request.getHeader("REFERER");
		if ("".equals(conString) || null == conString) { // 判断如果上一个目录为空的话，说明是用户直接输入url访问的
			// 用户访问的资源地址
			String requestPath = ResourceUtil.getRequestPath(request);
			for(String url:excludeUrls){
				if (requestPath.contains(url)) {//不拦截
					 return true;
				}
			}
			if(requestPath.contains("rest/")){//接口 /rest不拦截
				return true;
			} else {
				//页面跳转
				forward(request,response);
				return false;				
			}
		} else {
			return true;
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * 在controller后拦截
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
	}

	/**
	 * 跳转到前台首页
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void forward(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//request.getRequestDispatcher("webpage/login/timeout.jsp").forward(request, response);
			
		response.sendRedirect(request.getSession().getServletContext().getAttribute("DynamicURL")+"/");
	}
}
