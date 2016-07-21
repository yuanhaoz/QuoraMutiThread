package sys.gifspider.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

public class QtAllFileProcessor {
	private String keyword;
	private String imgName;
	private String imgUrl;

	public QtAllFileProcessor(String keyword, String name, String url) {
		this.keyword = keyword;
		this.imgName = name;
		this.imgUrl = url;
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
	public void saveQuestion() throws Exception {
		String dir = makeDirectoy();
		String filePath = dir + this.imgName;
		System.out.println("filePath： " + filePath);
		// 爬取问题（Selenium）
		Selenium.seleniumCrawlerQuestion(filePath, imgUrl);
		
		// 爬取问题（HttpClient）
//		Selenium.httpClientCrawler(filePath, imgUrl);
//		Selenium.webmagicSpider(filePath, imgUrl);
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
		String file = dir + this.imgName + this.imgUrl.substring(this.imgUrl.lastIndexOf("."));
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
		URL url = new URL(this.imgUrl);
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
