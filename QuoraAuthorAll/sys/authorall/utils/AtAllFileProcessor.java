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
	
	//�Լ�����
	/**
	 * ����·���������ھʹ���
	 * 
	 * @return
	 */
	private String makeDirectoy() {
		Properties properities = PropertyUtil.getProperties();
		String course = properities.getProperty("course");// �õ��γ���
		String catalog = "file/datacollection/" + course;// �õ��γ�Ŀ¼
//		String catalog = "file/datacollection/spidertest";// �õ��γ�Ŀ¼
		String strdir = catalog + "/" + this.keyword + "/";// �õ��ؼ����ļ���
		File dir = new File(strdir);
		if (!dir.exists()) {
			dir.mkdir();
		}
		return strdir;
	}
	
	/**
	 * ����������ҳ
	 * 
	 * @throws Exception
	 */
	public void saveAuthor() throws Exception {
		String dir = makeDirectoy();
		String filePath = dir + this.atName;
		System.out.println("filePath�� " + filePath);
		// ��ȡ���ߣ�Selenium��
//		Selenium.seleniumCrawlerAuthor(filePath, atUrl);
		
		// ��ȡ���ߣ�HttpClient��
		Selenium.httpClientCrawler(filePath, atUrl);
//		Selenium.webmagicSpider(filePath, atUrl);
	}

	//ͼƬ��ȡԴ����
	/**
	 * ����·���������ھʹ���
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
	 * ����
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
	 * ����
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
