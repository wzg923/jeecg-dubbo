package com.guoyicap.demo.service;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.guoyicap.core.model.MsgModel;

@Path("demoService")
@Consumes({ MediaType.TEXT_XML, MediaType.APPLICATION_JSON })
@Produces({ ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8 })
public interface DemoService {
	
	@POST
	@Path("hello")
	public String hello();
	
	@POST
	@Path("test1")
	public MsgModel<String> test1(MsgModel<String> model);
	
	@POST
	@Path("test2")
	public MsgModel<String> test2(Map map);
	
	@POST
	@Path("test3")
	public MsgModel<String> test3(List<Map> list);
	
	@POST
	@Path("test4/{b}")
	public MsgModel<String> test4(@PathParam("b")Integer b);
}
