package com.loris.base;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.loris.base.bean.web.WebPage;
import com.loris.base.data.IrisData;
import com.loris.base.analysis.logistics.Element;
import com.loris.base.analysis.logistics.Logistics;
import com.loris.base.util.DateUtil;
import com.loris.base.util.TextCalculator;
import com.loris.base.web.WebCrawler;
import com.loris.base.web.config.ConfigParser;
import com.loris.base.web.config.setting.DownSetting;
import com.loris.base.web.config.setting.PageSetting;
import com.loris.base.web.http.LoginManager;
import com.loris.base.web.http.UrlFetcher;
import com.loris.base.web.http.WebClientFetcher;
import com.loris.base.web.repository.WebPageManager;

/**
 * Unit test for simple App.
 */
public class BaseApp
{
	private static Logger logger = Logger.getLogger(BaseApp.class);
	
    public static void main(String[] args)
    {
    	try
    	{
    		//testHtmlUnit();
    		//testDate();
    		//testFetcher();
    		
    		//testOkoooWeb();
    		
    		//testWebDownloadersParse();
    		// testFireFox();
    		
    		testMathRegression();
    		//parseDate();
    		
    		// testSimilarity();
    		//testLogistics();
    	}
    	catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 测试最小二乘线性回归
     * @throws Exception
     */
    public static void testMathRegression() throws Exception
    {
    	OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
    	double[] y = new double[]{11.0, 12.0, 13.0, 14.0, 15.0, 16.0};
    	double[][] x = new double[6][];
    	x[0] = new double[]{0, 0, 0, 0, 0};
    	x[1] = new double[]{2.0, 0, 0, 0, 0};
    	x[2] = new double[]{0, 3.0, 0, 0, 0};
    	x[3] = new double[]{0, 0, 4.0, 0, 0};
    	x[4] = new double[]{0, 0, 0, 5.0, 0};
    	x[5] = new double[]{0, 0, 0, 0, 6.0};          
    	regression.newSampleData(y, x);
    	
    	double[] beta = regression.estimateRegressionParameters();
    	double[] residuals = regression.estimateResiduals();
    	double[][] parametersVariance = regression.estimateRegressionParametersVariance();
    	double regressandVariance = regression.estimateRegressandVariance();
    	double rSquared = regression.calculateRSquared();
    	double sigma = regression.estimateRegressionStandardError();
    	
    	logger.info("Beta: " + Arrays.toString(beta));
    	logger.info("Residuals: " + Arrays.toString(residuals));
    	
    	for(int i = 0; i < parametersVariance.length; i ++)
    	{
        	logger.info("parametersVariance " + i + ": " + Arrays.toString(parametersVariance[i]));
    	}
    	logger.info("regressandVariance: " + regressandVariance);
    	logger.info("rSquared: " + rSquared);
    	logger.info("sigma: " + sigma);
    	
    }
    
    /**
     * Parse the downloaders.
     * 
     * @throws Exception
     */
    public static void testWebDownloadersParse() throws Exception
    {
    	ConfigParser.parseWebPageSettings(BaseApp.class.getResourceAsStream("/web-downloaders.xml"));
    	
    	List<DownSetting> settings = WebCrawler.getWebPageSettings();
    	int i = 0;
    	for (DownSetting webPageSetting : settings)
		{
			logger.info(i +++ ": " + webPageSetting);
		}
    	
    	List<PageSetting> pageSettings = WebPageManager.getPageSettings();
    	i = 1;
    	for (PageSetting pageSetting : pageSettings)
		{
			logger.info(i +++ ": " + pageSetting);
		}
    }
    
    /**
     * 测试逻辑回归算法
     * @throws Exception
     */
    public static void testLogistics() throws Exception
    {
    	String filename = "d:\\index\\test-data\\iris.data";
    	int attrSize = 4;
    	String delimiter = ",";
    	Random random = new Random();
    	
    	List<Element> dataset = IrisData.getDataset(filename, attrSize, delimiter);
    	
    	List<Element> trainSet = new ArrayList<>();
    	List<Element> testSet = new ArrayList<>();
    	double d;
    	
    	for (Element element : dataset)
		{
			if(element.getLabel() < 1.1)
			{
				d = random.nextDouble();
				if(d > 0.2)
					trainSet.add(element);
				else
					testSet.add(element);
			}
		}
    	
    	/*int i = 1;
    	for (Element element : trainSet)
		{
			logger.info("Training" + i +++ ": " + element);
		}*/
    	
    	logger.info("Train set is " + trainSet.size() + ", Test set is " + testSet.size());
    	
    	Logistics logistics = new Logistics();
    	int numIter = 5000;
    	List<Double> weights = logistics.gradAscent1(trainSet, numIter); //.stocGradAscent(trainSet, numIter);
    	double cate;
    	
    	int i = 1;
    	for (Element element : testSet)
		{
			cate = logistics.compute(element.getAttributes(), weights);
			logger.info(i +++ " Logistic Class: " + cate + " Element: " + element);
		}
    }
    
    
    public static void parseDate()
    {
    	String time = "2018/05/23 21:23";
    	Date date = DateUtil.tryToParseDate(time);
    	logger.info("Time: " + time);
    	logger.info("Date: " + date);
    }
    
    /**
	 * Test for fireFox
	 * @param context
	 * @throws Exception
	 */
	public static void testFireFox() throws Exception
	{
		System.setProperty("webdriver.gecko.driver", "C:\\Program Files\\Mozilla Firefox\\geckodriver.exe");
		String fireFoxPath = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
		System.setProperty("webdriver.firefox.bin", fireFoxPath);
		WebDriver driver = new FirefoxDriver(); 
		//driver.navigate().to("http://www.okooo.com");
		driver.get("http://www.okooo.com/jingcai/shengpingfu/");
		WebElement webElement = driver.findElement(By.xpath("/html"));
		logger.info(webElement.getText());
		
		logger.info("End. \r\n");
		try
		{
			Thread.sleep(2000);
			
			driver.get("http://www.okooo.com/soccer/match/1009838/odds/");
			webElement = driver.findElement(By.xpath("/html"));
			logger.info(webElement.getText());
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
	}
	
    
    
    
    
    public static void testHtmlUnit() throws Exception
    {
    	//LoginManager.testDownload(LoginManager.url);
    	LoginManager.testOkooo();
    }
    
    
    public static void testOkoooWeb() throws Exception
    {
    	String baseUrl = "http://www.okooo.com/jingcai/";
    	WebPage page = new WebPage();
    	page.setUrl(baseUrl);
    	
    	WebClientFetcher fetcher = WebClientFetcher.createFetcher(page);
    	//logger.info(page.getContent());
    	
    	WebPage p1 = new WebPage();
    	String url = "http://www.okooo.com/soccer/match/1009824/ah/";
    	p1.setUrl(url);
    	
    	if(fetcher.fetch(p1))
    	{
    		logger.info(p1.getContent());
    	}
    	
    	fetcher.close();
    	
    }
    
    public static void testSimilarity()
    {
    	String doc1 = "AC米兰";
    	String doc2 = "国际米兰";
    	
    	String doc3 = "AC米兰";
    	
    	double s = TextCalculator.getSimilarity(doc1, doc2);
    	logger.info(doc1 + "=>" + doc2 + "=" + s);
    	s = TextCalculator.getSimilarity(doc1, doc3);
    	logger.info(doc1 + "=>" + doc3 + "=" + s);
    	
    	HashSet<Integer> rows = new HashSet<>();
    	rows.add(1);
    	rows.add(2);
    	
    	logger.info("Row contains(1) = " + rows.contains(1));
    	logger.info("Row contains(3) = " + rows.contains(3));
    }
    
    public static void testFetcher() throws Exception
    {
    	String url = "http://www.okooo.com/soccer/match/1009824/odds/change/2/";
    	url = "http://bisai.caipiao.163.com/7/14018/2529608/yapan.html";
    	WebPage page = new WebPage();
    	page.setEncoding("utf-8");
    	page.setUrl(url);
    	String content;
    	if(UrlFetcher.fetch(page))
    	{
    		content = page.getContent();
    		logger.info(content);
    	}
    }
    
    public static void testDate() throws Exception
    {
    	String format = "EEE yyyy-MM-dd";
    	String dString = "星期五 2018-04-20";
    	logger.info(format);
    	logger.info(dString);
    	
    	Date date = new SimpleDateFormat(format).parse(dString);    	
    	logger.info(date);
    	
    	String format2 = "yyyy-MM-dd EEE";
    	String string = new SimpleDateFormat(format2).format(new Date());
    	logger.info(string);
    	
    	Date date2 = DateUtil.tryToParseDate(dString);
    	logger.info(date2);
    	
    	logger.info(new Date().getTime());
    }
}
