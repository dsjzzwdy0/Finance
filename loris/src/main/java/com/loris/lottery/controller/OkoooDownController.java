package com.loris.lottery.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.loris.base.web.util.Monitor;

@Controller
@RequestMapping("/downokooo")
public class OkoooDownController
{
	private static Logger logger = Logger.getLogger(OkoooDownController.class);
	
	/** HTMLUnit下载系统 */
	private WebClient webClient = null;
	
	/** 下载管理器 */
	protected Monitor crawler = null;
	
	/** 登录页面 */
	private HtmlPage loginPage;
	
	/** 验证的主网页  */
	private String mainUrl = "http://www.okooo.com/";
	
	/**
	 * 创建一个下载管理器
	 * 
	 * @return 返回输入用户、密码、验证码界面
	 */
	@RequestMapping("/create")
	public String createOkoooWebClient()
	{
		if(webClient != null)
		{
			close();
		}
		
		webClient = new WebClient(BrowserVersion.FIREFOX_52);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setCssEnabled(false);
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		
		try
		{
			HtmlPage page = webClient.getPage(mainUrl);
			HtmlAnchor login = (HtmlAnchor)page.getByXPath("//div[@class='container']//a[@class='user_login']").get(0);
			
			loginPage = login.click();
		
			return "soccer/okooo/login";
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}	
		
		return "";
	}
	
	/**
	 * 登录与认证
	 * 
	 * @param username 用户名
	 * @param password 密码
	 * @param authcode 验证码
	 */
	@RequestMapping("/login")
	public void login(String user, String password, String authcode, HttpServletResponse response)
	 	throws IOException
	{
		try
		{
			logger.info(user + ": " + password + " -> " + authcode);
			
			HtmlElement username = (HtmlElement)loginPage.getElementById("login_name");
			HtmlElement passwordEl = (HtmlElement)loginPage.getElementById("login_pwd");
			HtmlElement valiCode = (HtmlElement)loginPage.getElementById("AuthCode");
			
			HtmlElement submit =(HtmlElement) loginPage.getElementById("LoginSubmit");
			HtmlSubmitInput submit2 = (HtmlSubmitInput) submit;
			
			username.click();
			username.type(user);
			username.setAttribute("value", user);
			passwordEl.click();
			passwordEl.type(password);
			passwordEl.setAttribute("value", password);

			valiCode.click();
			valiCode.type(authcode);
			valiCode.setAttribute("value", authcode);
			
			HtmlPage resultPage = submit2.click();
			
			logger.info(resultPage.getDocumentURI());
			response.sendRedirect("../downlist.html");
			return;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		response.sendRedirect("../error.html");
	}
	
	
	/**
	 * 图标数据服务接口
	 * 
	 * @param type 图标类型，这里默认有联赛图标、球队的图标
	 * @param id 图标的唯一编号
	 * @param response 返回数据接口
	 */
	@RequestMapping(value="/getImage",method = RequestMethod.GET) 
	public void getImage(String type, String id, HttpServletResponse response)
	{
		if(loginPage == null)
		{
			try
			{
				response.sendRedirect("../content/images/shouye_tu.png");
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			return;
		}
		
		try
		{		
			response.setContentType("image/jpeg");
			HtmlImage valiCodeImg = (HtmlImage) loginPage.getElementById("randomNoImg");
			ImageReader imageReader = valiCodeImg.getImageReader();
			BufferedImage bufferedImage = imageReader.read(0);
			
			OutputStream outputStream = response.getOutputStream();
			ImageIO.write(bufferedImage, "jpg", outputStream);
			outputStream.flush();
			outputStream.close();
		}
		catch(Exception e)
		{
			try
			{
				response.sendRedirect("../content/images/shouye_tu.png");
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			return;
		}
	}
	
	/**
	 * 关闭下载系统
	 */
	public void close()
	{
		if(webClient != null)
		{
			webClient.close();
			webClient = null;
		}
	}
}
