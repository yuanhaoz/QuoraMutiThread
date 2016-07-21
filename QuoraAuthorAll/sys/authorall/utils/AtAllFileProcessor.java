package sys.authorall.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import sys.gifspider.utils.PropertyUtil;
import sys.gifspider.utils.Selenium;

public class AtAllFileProcessor {
	private String keyword;
	private String atName;
	private String atUrl;

	public AtAllFileProcessor(String keyword, String name, String url) {
		this.keyword = keyword;
		this.atName = name;
		this.atUrl = url;
	}
	
	//自己定制
	/**
	 * 保存路径，不存在就创建
	 * 
	 * @return
	 */
	private String makeDirectoy() {
		Properties properities = PropertyUtil.getProperties();
		String course = properities.getProperty("course");// 得到课程名
		String catalog = "file/datacollection/" + course;// 得到课程目录
//		String catalog = "file/datacollection/spidertest";// 得到课程目录
		String strdir = catalog + "/" + this.keyword + "/";// 得到关键词文件夹
		File dir = new File(strdir);
		if (!dir.exists()) {
			dir.mkdir();
		}
		return strdir;
	}
	
	/**
	 * 保存问题网页
	 * 
	 * @throws Exception
	 */
	public void saveAuthor() throws Exception {
		String dir = makeDirectoy();
		String filePath = dir + this.atName;
		System.out.println("filePath： " + filePath);
		// 爬取作者（Selenium）
//		Selenium.seleniumCrawlerAuthor(filePath, atUrl);
		
		// 爬取作者（HttpClient）
		Selenium.httpClientCrawler(filePath, atUrl);
//		Selenium.webmagicSpider(filePath, atUrl);
	}

	//图片爬取源代码
	/**
	 * 保存路径，不存在就创建
	 * 
	 * @return
	 */
	private String makeDir() {
		String strdir = PropertyUtil.getProperties().getProperty("dir");
		File dir = new File(strdir);
		if (!dir.exists()) {
			dir.mkdir();
		}
		return strdir;
	}

	/**
	 * 保存
	 * 
	 * @throws Exception
	 */
	public void saveGif() throws Exception {
		String dir = makeDir();
		String file = dir + this.atName + this.atUrl.substring(this.atUrl.lastIndexOf("."));
		BufferedOutputStream out = null;
		byte[] bit = this.download();
		if (bit.length > 0) {
			try {
				out = new BufferedOutputStream(new FileOutputStream(file));
				out.write(bit);
				out.flush();
			} finally {
				if (out != null)
					out.close();
			}
		}
	}

	/**
	 * 下载
	 * 
	 * @return
	 * @throws Exception
	 */
	private byte[] download() throws Exception {
		URL url = new URL(this.atUrl);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		httpConn.connect();
		InputStream cin = httpConn.getInputStream();
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = cin.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		cin.close();
		byte[] fileData = outStream.toByteArray();
		outStream.close();
		return fileData;
	}
	
}
