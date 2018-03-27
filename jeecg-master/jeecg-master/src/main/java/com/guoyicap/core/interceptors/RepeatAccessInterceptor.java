package com.guoyicap.core.interceptors;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jeecgframework.core.util.IpUtil;
import org.jeecgframework.core.util.LogUtil;
import org.jeecgframework.core.util.ResourceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.guoyicap.core.redis.service.RedisService;
import com.guoyicap.util.JSONUtils;

/**  
* @ClassName: RepeatAccessInterceptor  
* @Description: 重复请求拦截器   ，验证请求是否重复
* @author A18ccms a18ccms_gmail_com  
* @date 2016年12月29日 下午1:01:35  
*    
*/
public class RepeatAccessInterceptor implements HandlerInterceptor {
	private static final Logger logger = Logger.getLogger(RepeatAccessInterceptor.class);
	private static final String Request_Params_Key="request:params:key:";
	private List<String> includeUrls;//需要拦截的url
	
	
	@Autowired
	RedisService redisService;

	@Override
	/**
	 * 在请求Controller 之前拦截
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//请求url
		String requestPath = ResourceUtil.getRequestPath((HttpServletRequest)request);// 用户访问的资源地址
		
		for (String includeUrlStr : includeUrls) {
			if(requestPath.contains(includeUrlStr)){
				//请求参数
				String requestParamStr=JSONUtils.toJSONString(JSONUtils.toJSONObject(request.getParameterMap()));
				String ip=IpUtil.getIpAddr(request);
				String key=new StringBuilder(Request_Params_Key)
						.append(requestPath)
						.append("_ip:"+ip)
						.append("_params:"+requestParamStr).toString();
				LogUtil.info("重复请求URL验证:"+key);
				
				//如果存在相同key 则返回false
				if(redisService.exists(key)){
					LogUtil.error("URL请求重复！"+key);
					returnError(response);
					return false;
				}else{
					//缓存请求url
					redisService.setStr(key, "1", 10, TimeUnit.SECONDS);
					break;
				}		
			}
		}
		
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
	
	public List<String> getIncludeUrls() {
		return includeUrls;
	}

	public void setIncludeUrls(List<String> includeUrls) {
		this.includeUrls = includeUrls;
	}
	
	/**
	* @Title: returnError
	* @author: 王振广
	* @Description: 返回错误信息
	* @param  response    设定文件
	* @return void    返回类型
	 * @throws IOException 
	*/ 
	public void returnError(HttpServletResponse response) throws IOException{
		Map<String,Object> result=new HashMap<String,Object>();
		result.put("status", -1);
		result.put("msg","重复的请求");
		result.put("data",null);
		String jsonStr=JSONUtils.toJSONString(JSONUtils.toJSONObject(result));
		response.getWriter().write(jsonStr);
	}
}
