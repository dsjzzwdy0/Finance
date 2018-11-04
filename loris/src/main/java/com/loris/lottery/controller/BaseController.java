package com.loris.lottery.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.loris.base.bean.User;
import com.loris.lottery.util.WebConstants;

public class BaseController
{
	@Autowired
    protected HttpServletRequest request;
	
	/**
	 * 获得当前用户
	 * @return
	 */
	public User getCurrentUser()
	{
		return (User)request.getSession().getAttribute(WebConstants.CURRENT_USER);
	}
	
	/**
	 * 注册当前用户
	 */
	public void logoutCurrentUser()
	{
		request.getSession().removeAttribute(WebConstants.CURRENT_USER);
	}
}
