package com.guoyicap.demo.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.guoyicap.core.model.MsgModel;
import com.guoyicap.demo.service.DemoService;
import com.guoyicap.util.GsonUtil;

@Transactional
@Service(protocol={"rmi","rest"} ,group = "guoyiDemo", version = "1.0.0", validation = "true", loadbalance = "random", timeout = 60000, connections = 100)
public class DemoServiceImpl implements DemoService{
	@Override
	public String hello(){
		return "hello world!";
	}

	@Override
	public MsgModel<String> test1(MsgModel<String> model) {
		String msg=model.getMsg();
		System.out.println(msg);
		model.setMsg("已收到消息："+msg);
		model.setData("成功");
		return model;
	}

	@Override
	public MsgModel<String> test2(Map map) {
		System.out.println(map);
		MsgModel<String> msg=new MsgModel<String>();
		msg.setData(map.toString());
		msg.setMsg("成功");
		return msg;
	}

	@Override
	public MsgModel<String> test3(List<Map> list) {
		String data=GsonUtil.create(null).toJson(list);
		System.out.println(data);
		MsgModel<String> msg=new MsgModel<String>();
		msg.setData(data);
		msg.setMsg("成功");
		return msg;
	}

	@Override
	public MsgModel<String> test4(Integer b) {
		System.out.println("b="+b);
		MsgModel<String> msg=new MsgModel<String>();
		msg.setData("b="+b+"}");
		msg.setMsg("成功");
		return msg;
	}
	
	
}
