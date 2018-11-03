package com.loris.lottery.controller;

<<<<<<< HEAD
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
=======
>>>>>>> 9b450c39b7c085402877e394d4583d6f2ceaf855
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
<<<<<<< HEAD
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.auth.UserAuthenticate;
import com.loris.base.bean.User;
import com.loris.base.bean.wrapper.Rest;
import com.loris.base.repository.BasicManager;
import com.loris.lottery.util.WebConstants;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController
{
	protected static Logger logger = Logger.getLogger(UserController.class); 
	
	@Autowired
	private BasicManager basicManager;
	
	/**
	 * 管理用户
	 * @return 用户管理页面
	 */
	@RequestMapping("/admin")
	public ModelAndView manager(HttpServletRequest request)
	{
		User user = (User)request.getSession().getAttribute(WebConstants.CURRENT_USER);
		if(user == null || !"admin".equalsIgnoreCase(user.getUsertype()))
		{
			ModelAndView view = new ModelAndView(new RedirectView("./login", false));
			return view;
		}
		ModelAndView view = new ModelAndView("admin.user");
		return view;
	}
	
	/**
	 * 用户注销
	 * @return 用户管理页面
	 */
	@RequestMapping("/logout")
	public ModelAndView logout(HttpSession session)
	{
		ModelAndView view = new ModelAndView("login.user");
		session.removeAttribute(WebConstants.CURRENT_USER);
		return view;
	}
	
		
	/**
	 * 获得数据下载页面
	 * @return 下载页面视图
	 */
	@RequestMapping("/login")
	public ModelAndView login(String user, String password, String redirect, 
			HttpServletRequest request)
	{
		String error = "";
		if(StringUtils.isNotEmpty(user) || StringUtils.isNotEmpty(password))
		{
			User userBean = basicManager.getUser(user);
			if(userBean == null)
			{
				error = "用户名'" + user + "'不存在.";
			}
			
			try
			{
				if(UserAuthenticate.verifyPassword(userBean, password))
				{
					if(StringUtils.isEmpty(redirect))
					{
						redirect = "../index.html";
					}
					
					HttpSession session = request.getSession();
				    session.setMaxInactiveInterval(1000 * 60 * 60);
				    session.setAttribute(WebConstants.CURRENT_USER, userBean);
				    boolean contextRelative = false;
				    if(redirect.startsWith("./") || redirect.startsWith("../"))
				    {
				    	contextRelative = true;
				    }
				    
				    logger.info("FromURL: " + redirect);
					ModelAndView view = new ModelAndView(new RedirectView(redirect, contextRelative));
					return view;
				}
				else
				{
					error = "用户名'" + user + "'输入密码错误.";
				}
			}
			catch(Exception e)
			{
				error = "验证用户'" + user + "'时出错,请联系管理员.";
			}
		}	
		ModelAndView view = new ModelAndView("login.user");
		view.addObject("error", error);
		view.addObject("redirect", redirect);
		return view;
	}
	
	/**
	 * 获得数据下载页面
	 * @return 下载页面视图
	 */
	@RequestMapping("/register")
	public ModelAndView register(String user, String email, String password)
	{
		String error = "";
		ModelAndView view = new ModelAndView("register.user");
		//logger.info("Register: " + user + ", email: " + email + ", password: " + password);	
		if(StringUtils.isNotEmpty(user) && StringUtils.isNotEmpty(password) && StringUtils.isNotEmpty(email))
		{
			User userBean = basicManager.getUser(user);
			if(userBean != null)
			{
				error = "用户名'" + user + "'已经存在,不能注册.";
			}
			else
			{
				userBean = new User();
				userBean.setUsername(user);
				userBean.setEmail(email);
				userBean.setPassword(password);	
				userBean.setRealname(user);
				
				try
				{
					UserAuthenticate.encodePassword(userBean);
					if(basicManager.addOrUpdateUser(userBean))
					{
						view.addObject("code", "1");
						view.addObject("user", user);
						view.addObject("email", email);
						view.addObject("password", password);
						//logger.info("Register: " + user + " success.");					
					}
					else
					{
						error = "注册用户'" + user + "'失败.";
					}
				}
				catch (Exception e) {
					error = "用户密码加密时出错.";
				}
			}
		}		
		view.addObject("error", error);
		return view;
	}
	
	/**
	 * 获得用户列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping("userList")
	public Rest getUserList()
	{
		List<User> users = basicManager.getUserList();
		return Rest.okData(users);
=======

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
>>>>>>> 9b450c39b7c085402877e394d4583d6f2ceaf855
	}
}
