package com.guoyicap.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.guoyicap.demo.service.DemoService;

@Controller
@RequestMapping("/demoController")
public class DemoController {
	@Autowired
	private DemoService demoService;

	@ResponseBody
	@RequestMapping(value="/test")
	public String test(){
		
		return demoService.hello();
	}
}
