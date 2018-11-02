package com.loris.base.web.http;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

public class LoginManager
{
	private static Logger logger = Logger.getLogger(LoginManager.class);
	
	public static String url = "http://www.okooo.com/soccer/match/955339/odds/";
	
	public static String URL_LEAGUE_ROUND = "http://liansai.500.com/index.php?c=score&a=getmatch&stid=11740&round=17";
	
	public static void testDownload(String url) throws Exception
	{
		final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52);  
        final HtmlPage page = webClient.getPage(url);
        webClient.close();
        
        logger.info(page.asXml());
	}
	
	public static void testOkooo() throws Exception
	{
		try
		{
			//设置多个  
			//String[] sslClientProtocols = {"TLSv1","TLSv1.1","TLSv1.2"};  
			
			WebClient client = new WebClient(BrowserVersion.FIREFOX_52);
			//client.setJavaScriptEnabled(false);
			client.getOptions().setJavaScriptEnabled(true);
			client.getOptions().setCssEnabled(false);
			client.setAjaxController(new NicelyResynchronizingAjaxController());
			//client.getOptions().setSSLInsecureProtocol(sslClientProtocols[2]);
			
			//client.setWebConnection(httpwebconnection);
			HtmlPage page = client.getPage("http://www.okooo.com/");
			
			logger.info(page.asXml());
			logger.info(page.getByXPath("//div[@class='container']//a[@class='user_login']").get(0).toString());
			
			HtmlAnchor login = (HtmlAnchor)page.getByXPath("//div[@class='container']//a[@class='user_login']").get(0);
			page = login.click();
			
			HtmlElement username = (HtmlElement)page.getElementById("login_name");
			HtmlElement password = (HtmlElement)page.getElementById("login_pwd");
			HtmlElement valiCode = (HtmlElement)page.getElementById("AuthCode");
			HtmlImage valiCodeImg = (HtmlImage) page.getElementById("randomNoImg");
			
			//logger.info(valiCodeImg.getSrcAttribute());			
			logger.info(username);
			
			ImageReader imageReader = valiCodeImg.getImageReader();
			BufferedImage bufferedImage = imageReader.read(0);
			
			String file = "d:/index/image/out.png";
			OutputStream out1 = new BufferedOutputStream(new FileOutputStream(file));
			ImageIO.write(bufferedImage, "png", out1);
			out1.close();

			JFrame f2 = new JFrame();
			JLabel l = new JLabel();
			l.setIcon(new ImageIcon(bufferedImage));
			f2.getContentPane().add(l);
			f2.setSize(300, 200);
			f2.setTitle("验证码");
			f2.setVisible(true);
			
			String valicodeStr = JOptionPane.showInputDialog("请输入验证码：");
			f2.setVisible(false);
			HtmlElement submit =(HtmlElement) page.getElementById("LoginSubmit");
			HtmlSubmitInput submit2 = (HtmlSubmitInput) submit;
			username.click();
			//username.type("gabazi");
			//username.setAttribute("value", "dsjzzwdy0");
			String name = "dsjzzwdy0";
			String pwd = "dsj561419";
			
			username.type(name);
			username.setAttribute("value", name);
			password.click();
			password.type(pwd);
			password.setAttribute("value", pwd);
			
			//password.click();
			//password.type("******");
			valiCode.click();
			valiCode.type(valicodeStr);
			valiCode.setAttribute("value", valicodeStr);
			
			logger.info("UserName: " + username.getNodeValue());
			logger.info("Password: " + password.getNodeValue());
			logger.info("Valicode: " + valiCode.getNodeValue());

			HtmlPage resultPage = submit2.click();
			
			Thread.sleep(1000);
			
			logger.info(resultPage.asText());
			
			String url2 = "http://www.okooo.com/soccer/match/801173/odds/";
			HtmlPage retPage = client.getPage(url2);
			logger.info(retPage.asXml());
			
			client.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
