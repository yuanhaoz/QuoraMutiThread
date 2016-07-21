package sys.gifspider.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.selector.Html;

@SuppressWarnings("deprecation")
public class Selenium {
	
	public static void main(String[] args) throws Exception {
		init();
	}
	
	public static void init() throws Exception {
		String url = "https://www.quora.com/The-Social-Network-2010-movie/What-did-Mark-Zuckerberg-do-at-the-actual-Sequoia-Capital-meeting";
//		String url = "https://www.quora.com/search?q=Binary+tree";
//		String url = "http://finance.people.com.cn/n1/2016/0119/c1004-28068109.html";
//		seleniumCrawlerNews("F:\\6.html", url);
//		httpClientCrawler("F:\\6.html", url);
//		webmagicSpider("F:\\2.html", url);
		webmagicSpider("F:\\1.html", "http://sports.qq.com/a/20160129/009637.htm#p=1");
	}
	
	/**
	 * 得到页面html源码（调用webmagic框架的爬取器）
	 * 
	 * @return
	 */
	public static String webmagicSpider(String filePath, String url) {
		HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
		Html html = httpClientDownloader.download(url, 5, "UTF-8");
		String content = html.toString();
		// System.out.println("url: "+content);
		saveHtml(filePath, content);
		System.out.println("------------------");
		return content;
	}

	/**
	 * 实现功能：爬取主题页面，输入是 “页面保存路径filepath” 和 “主题页面链接url”
	 *          下拉四次主题页面，10秒内没打开页面会重开（问题数目为49-59）
	 *          调整  下拉次数  可以得到问题数目会  增加
	 * 使用技术：Selenium
	 * @param filePath, url
	 * @throws InterruptedException 
	 * 
	 */
	public static void seleniumCrawlerSubject(String filePath, String url) throws InterruptedException{
		// 设置
//		System.setProperty("webdriver.ie.driver","C:\\Program Files\\Internet Explorer\\IEDriverServer.exe");
//		System.out.println("InternetExplorerDriver opened");
		File file = new File(filePath);
		int delay = 12;
		if(!file.exists()){
			WebDriver driver = new InternetExplorerDriver();
			//5秒内没打开，重新加载
			driver.manage().timeouts().pageLoadTimeout(delay, TimeUnit.SECONDS);    
			while (true){
				try{
					driver.get(url);
				}
				catch (Exception e)
				{
					driver.quit();
					driver = new InternetExplorerDriver();
					driver.manage().timeouts().pageLoadTimeout(delay, TimeUnit.SECONDS);
					continue;
				}
				break;
			}
			System.out.println("Page title is: " + driver.getTitle());
			// roll the page
			JavascriptExecutor JS = (JavascriptExecutor) driver;
			try {
				JS.executeScript("scrollTo(0, 5000)");
				System.out.println("1");
				Thread.sleep(5000);             //调整休眠时间可以获取更多的内容
				JS.executeScript("scrollTo(5000, 10000)");
				System.out.println("2");
				Thread.sleep(5000);
				JS.executeScript("scrollTo(10000, 30000)"); // document.body.scrollHeight
				System.out.println("3");
				Thread.sleep(5000);
				JS.executeScript("scrollTo(10000, 80000)"); // document.body.scrollHeight
				System.out.println("4");
				Thread.sleep(5000);
			} catch (Exception e) {
				System.out.println("Error at loading the page ...");
				driver.quit();
			}
			// save page
			String html = driver.getPageSource();
			saveHtml(filePath, html);
			System.out.println("save finish");
			// Close the browser
			Thread.sleep(2000);
			driver.quit();
		}else{
			System.out.println(filePath + "已经存在，不必再次爬取...");
		}
	}
	
	/**
	 * 实现功能：爬取主题页面，输入是 “页面保存路径filepath” 和 “主题页面链接url”
	 *          下拉四次主题页面，10秒内没打开页面会重开（问题数目为49-59）
	 *          调整  下拉次数  可以得到问题数目会  增加
	 * 使用技术：Selenium
	 * @param filePath, url
	 * @throws InterruptedException 
	 * 
	 */
	public static void seleniumCrawlerSubjectForTop10(String filePath, String url) throws InterruptedException{
		// 设置
//		System.setProperty("webdriver.ie.driver","C:\\Program Files\\Internet Explorer\\IEDriverServer.exe");
//		System.out.println("InternetExplorerDriver opened");
		File file = new File(filePath);
		int delay = 12;
		if(!file.exists()){
			WebDriver driver = new InternetExplorerDriver();
			//5秒内没打开，重新加载
			driver.manage().timeouts().pageLoadTimeout(delay, TimeUnit.SECONDS);    
			while (true){
				try{
					driver.get(url);
				}
				catch (Exception e)
				{
					driver.quit();
					driver = new InternetExplorerDriver();
					driver.manage().timeouts().pageLoadTimeout(delay, TimeUnit.SECONDS);
					continue;
				}
				break;
			}
			System.out.println("Page title is: " + driver.getTitle());
			// roll the page
			JavascriptExecutor JS = (JavascriptExecutor) driver;
			try {
				JS.executeScript("scrollTo(0, 5000)");
				System.out.println("1");
				Thread.sleep(5000);             //调整休眠时间可以获取更多的内容
				JS.executeScript("scrollTo(5000, 10000)");
				System.out.println("2");
				Thread.sleep(5000);
				JS.executeScript("scrollTo(10000, 30000)"); // document.body.scrollHeight
				System.out.println("3");
				Thread.sleep(5000);
				JS.executeScript("scrollTo(10000, 80000)"); // document.body.scrollHeight
				System.out.println("4");
				Thread.sleep(5000);
				JS.executeScript("scrollTo(80000, 100000)"); // document.body.scrollHeight
				System.out.println("5");
				Thread.sleep(5000);
				JS.executeScript("scrollTo(100000, 150000)"); // document.body.scrollHeight
				System.out.println("6");
				Thread.sleep(3000);
			} catch (Exception e) {
				System.out.println("Error at loading the page ...");
				driver.quit();
			}
			// save page
			String html = driver.getPageSource();
			saveHtml(filePath, html);
			System.out.println("save finish");
			// Close the browser
			Thread.sleep(2000);
			driver.quit();
		}else{
			System.out.println(filePath + "已经存在，不必再次爬取...");
		}
	}
	
	/**
	 * 实现功能：爬取问题页面，输入是 “页面保存路径filepath” 和 “问题页面链接url”
	 *          下拉四次主题页面，10秒内没打开页面会重开（答案数目一般都在10条以内）
	 *          调整   下拉次数    可以得到较为完整的  答案较多  的问题页面
	 * 使用技术：Selenium
	 * @param filePath, url
	 * 
	 */
	public static void seleniumCrawlerQuestion(String filePath, String url) throws InterruptedException {
		// 设置
//		System.setProperty("webdriver.ie.driver","C:\\Program Files\\Internet Explorer\\IEDriverServer.exe");
//		System.out.println("InternetExplorerDriver opened");
		File file = new File(filePath);
		int delay = 10;
		if(!file.exists()){
//			System.setProperty("webdriver.firefox.bin","D:\\Program Files\\Mozilla Firefox\\firefox.exe");
//			WebDriver driver = new FirefoxDriver();
//			System.out.println("太帅你好...");
			WebDriver driver = new InternetExplorerDriver();
//			System.out.println("太帅你好...");
			driver.manage().timeouts().pageLoadTimeout(delay, TimeUnit.SECONDS);    //5秒内没打开，重新加载
			while (true){
				try{
					driver.get(url);
				}
				catch (Exception e)
				{
					driver.quit();
					driver = new InternetExplorerDriver();
					driver.manage().timeouts().pageLoadTimeout(delay, TimeUnit.SECONDS);
					continue;
				}
				break;
			}
			System.out.println("Page title is: " + driver.getTitle());
			// roll the page
			JavascriptExecutor JS = (JavascriptExecutor) driver;
			try {
				JS.executeScript("scrollTo(0, 5000)");
				System.out.println("1");
				Thread.sleep(5000);             //调整休眠时间可以获取更多的内容
				JS.executeScript("scrollTo(5000, 10000)");
				System.out.println("2");
				Thread.sleep(5000);
				JS.executeScript("scrollTo(10000, 30000)"); // document.body.scrollHeight
				System.out.println("3");
				Thread.sleep(5000);
				JS.executeScript("scrollTo(10000, 80000)"); // document.body.scrollHeight
				System.out.println("4");
				Thread.sleep(5000);
			} catch (Exception e) {
				System.out.println("Error at loading the page ...");
				e.printStackTrace();
				driver.quit();
			}
			// save page
			String html = driver.getPageSource();
			saveHtml(filePath, html);
			System.out.println("save finish");
			// Close the browser
			Thread.sleep(2000);
			driver.quit();
		}else{
			System.out.println(filePath + "已经存在，不必再次爬取...");
		}
	}
	
	/**
	 * 实现功能：爬取作者页面，输入是 “页面保存路径filepath” 和 “作者页面链接url”
	 *          10秒内没打开页面会重开
	 *          无需下来页面，所需信息都在页面顶部
	 * 使用技术：Selenium
	 * @param filePath, url
	 * 
	 */
	public static void seleniumCrawlerAuthor(String filePath, String url) throws InterruptedException {
		System.out.println(String.format("Fetching %s...", url));
		// 设置
//		System.setProperty("webdriver.ie.driver","C:\\Program Files\\Internet Explorer\\IEDriverServer.exe");
//		System.out.println("InternetExplorerDriver opened");
		File file = new File(filePath);
		if(!file.exists()){
			WebDriver driver = new InternetExplorerDriver();
			driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);    //5秒内没打开，重新加载
			while (true){
				try{
					driver.get(url);
				}
				catch (Exception e)
				{
					driver.quit();
					driver = new InternetExplorerDriver();
					driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
					continue;
				}
				break;
			}
			// save page
			String html = driver.getPageSource();
			saveHtml(filePath, html);
			System.out.println("save finish...");
			// Close the browser
			Thread.sleep(2000);
			driver.quit();
		} else {
			System.out.println(filePath + "已经存在，不必再次爬取...");
		}
		
	}
	
	/**
	 * 实现功能：爬取网易滚动新闻网站
	 * 使用技术：Selenium
	 * @param filePath, url
	 * @throws InterruptedException 
	 * 
	 */
	public static void seleniumCrawlerNews(String filePath, String url) throws InterruptedException{
		File file = new File(filePath);
		int delay = 20;
		if(!file.exists()){
			WebDriver driver = new InternetExplorerDriver();
//			DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
//			ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);   
//			WebDriver driver = new InternetExplorerDriver(ieCapabilities); 
			//5秒内没打开，重新加载
			driver.manage().timeouts().pageLoadTimeout(delay, TimeUnit.SECONDS);    
//			driver.get(url);
			while (true){
				try{
//					System.out.println(">>");
					driver.get(url);
				}
				catch (Exception e)
				{
//					System.out.println(">>");
					driver.quit();
					driver = new InternetExplorerDriver();
					driver.manage().timeouts().pageLoadTimeout(delay, TimeUnit.SECONDS);
					continue;
				}
				break;
			}
			System.out.println("Page title is: " + driver.getTitle());
			// roll the page
			JavascriptExecutor JS = (JavascriptExecutor) driver;
			try {
				JS.executeScript("scrollTo(0, 5000)");
				System.out.println("1");
				Thread.sleep(1000);             //调整休眠时间可以获取更多的内容
				JS.executeScript("scrollTo(5000, 10000)");
				System.out.println("2");
				Thread.sleep(1000);
			} catch (Exception e) {
				System.out.println("Error at loading the page ...");
				driver.quit();
			}
			// save page
			String html = driver.getPageSource();
			saveHtmlGB(filePath, html);
			System.out.println("save finish");
			// Close the browser
			Thread.sleep(1000);
			driver.quit();
		}else{
			System.out.println(filePath + "已经存在，不必再次爬取...");
		}
	}
	
	/**
	 * 实现功能：httpClient方法爬取所有页面
	 * 			输入是 “页面保存路径filepath” 和 “主题页面链接”
	 * 使用技术：httpClient
	 * @param filePath, url
	 * 
	 */
	public static void httpClientCrawler(String filePath, String url) throws Exception{
		@SuppressWarnings({ "resource" })
		HttpClient hc = new DefaultHttpClient();
		String charset = "utf-8";
//		System.out.println("filepath is : " + filePath);
	    System.out.println(String.format("\nFetching %s...", url));  
	    File file = new File(filePath);
	    if(!file.exists()){
	    	HttpGet hg = new HttpGet(url);     
	 	    hg.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	 	    hg.setHeader("Accept-Language", "zh-cn,zh;q=0.5");
	 	    hg.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:7.0.1) Gecko/20100101 Firefox/7.0.1)");
	 	    hg.setHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
	 	    hg.setHeader("Host", "www.quora.com");
	         hg.setHeader("Connection", "Keep-Alive");
	         
	 		try
	 		{
	 		    HttpResponse response = hc.execute(hg);
	 		    HttpEntity entity = response.getEntity();   	       	        
	 		    InputStream htmInput = null;       
	 		    if(entity != null){
	 		        htmInput = entity.getContent();
	 		        String htmString = inputStream2String(htmInput,charset);
	 		        saveHtml(filePath, htmString);      //保存文件
	 		        System.out.println("爬取成功:" + " 网页长度为  " + entity.getContentLength());
	 		    }  
	 		}
	 		catch(Exception err) {
	 			System.err.println("爬取失败...失败原因: " + err.getMessage()); 
//	 			System.out.println("第二次爬取...");
//	 			HttpResponse response = hc.execute(hg);
//	 		    HttpEntity entity = response.getEntity();   	       	        
//	 		    InputStream htmInput = null;       
//	 		    if(entity != null){
//	 		        htmInput = entity.getContent();
//	 		        String htmString = inputStream2String(htmInput,charset);
//	 		        saveHtml(filePath, htmString);      //保存文件
//	 		        System.out.println("爬取成功:" + " 网页长度为  " + entity.getContentLength());
//	 		    }  
	 		}
	 		finally {
	 	        //关闭连接，释放资源
	 	        hc.getConnectionManager().shutdown();
	 	    }
		} else {
			System.out.println(filePath + "已经存在，不必再次爬取...");
		}
	}
	
	/**
	 * 实现功能：保存html字符串流到本地html文件
	 * 			输入是 “页面保存路径filepath” 和 “html字符串源码”
	 * @param filePath, str
	 */
	public static void saveHtml(String filePath, String str) {
		try {
			OutputStreamWriter outs = new OutputStreamWriter(new FileOutputStream(filePath, true), "utf-8");
			outs.write(str);
			outs.close();
		} catch (IOException e) {
			System.out.println("Error at save html...");
			e.printStackTrace();
		}
	}
	
	/**
	 * 实现功能：保存html字符串流到本地html文件
	 * 			输入是 “页面保存路径filepath” 和 “html字符串源码”
	 * @param filePath, str
	 */
	public static void saveHtmlGB(String filePath, String str) {
		try {
			OutputStreamWriter outs = new OutputStreamWriter(new FileOutputStream(filePath, true), "GB2312");
			outs.write(str);
			outs.close();
		} catch (IOException e) {
			System.out.println("Error at save html...");
			e.printStackTrace();
		}
	}
	
	/**
	 * 实现功能：输入流转为字符串流
	 * 			输入是 “输入流InputStream” 和 “编码方式charset”
	 * @param in_st, charset
	 */
    public static String inputStream2String(InputStream in_st,String charset) throws IOException{
        BufferedReader buff = new BufferedReader(new InputStreamReader(in_st, charset));
        StringBuffer res = new StringBuffer();
        String line = "";
        while((line = buff.readLine()) != null){
            res.append(line);
        }
        return res.toString();
    }
	
}
