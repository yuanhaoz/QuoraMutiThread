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
	public void saveQuestion() throws Exception {
		String dir = makeDirectoy();
		String filePath = dir + this.imgName;
		System.out.println("filePath�� " + filePath);
		// ��ȡ���⣨Selenium��
		Selenium.seleniumCrawlerQuestion(filePath, imgUrl);
		
		// ��ȡ���⣨HttpClient��
//		Selenium.httpClientCrawler(filePath, imgUrl);
//		Selenium.webmagicSpider(filePath, imgUrl);
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
	 * ����
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
