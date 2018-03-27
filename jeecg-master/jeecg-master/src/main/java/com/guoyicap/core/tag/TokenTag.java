package com.guoyicap.core.tag;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.guoyicap.core.handlers.TokenHandler;

public class TokenTag extends TagSupport {
	private static final long serialVersionUID = -5075768705375285608L;

	@Override
	public int doEndTag(){		
		try {
			JspWriter out = this.pageContext.getOut();
				out.print(end().toString());
				out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} 		
		return EVAL_PAGE;
	}
	
	/**
	 * 获取返回jsp页面的输出流
	 * @return
	 */
	protected Object end() {
		StringBuilder out = new StringBuilder();
		//获取令牌代码
		getTokenHandler(out);
		return out;
	}
	
	protected void getTokenHandler(StringBuilder out) {
		//生成令牌
		String token=TokenHandler.generateGUID(this.pageContext.getSession());
		out.append("<input type='hidden' name='")
			.append(TokenHandler.DEFAULT_TOKEN_NAME)
			.append("' value='")
			.append(token)			
			.append("' />");
	}
}
