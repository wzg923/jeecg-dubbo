package com.guoyidzy.demo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.guoyidzy.demo.service.DemoService;

@Service(protocol={"dubbo","rmi"} ,group="guoyidzy",version="1.0.0" )
public class DemoServiceImpl implements DemoService{
	public String hello(){
		return "hello world!";
	}
}
