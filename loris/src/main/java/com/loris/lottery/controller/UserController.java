package com.loris.lottery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.loris.base.bean.User;
import com.loris.base.repository.service.UserService;
import com.loris.lottery.annotation.ArchivesLog;

@Controller
@RequestMapping("/user")
public class UserController
{
	@Autowired
	private UserService userService;
	
	@ResponseBody
	@RequestMapping("/list")
	@ArchivesLog(operationName="Test")
	public Object list()
	{
		EntityWrapper<User> ew = new EntityWrapper<>();
		return userService.selectList(ew);
	}
}
