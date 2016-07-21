package sys.gifspider;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import sys.gifspider.utils.PropertyUtil;
import sys.gifspider.utils.Selenium;

public class QtAllSpider implements Runnable {

	volatile boolean isRunning = true;
	private ThreadPoolExecutor threadPool;
	BlockingQueue<String> queue;

	public QtAllSpider(BlockingQueue<String> queue) {
		this.queue = queue;
		this.init();
	}

	/**
	 * 线程池初始化
	 */
	private void init() {
		Properties pro = PropertyUtil.getProperties();
		int corePoolSize = Integer.parseInt(pro
				.getProperty("threadpool.corePoolSize"));
		int maxPoolSize = Integer.parseInt(pro
				.getProperty("threadpool.maxPoolSize"));
		int keepAliveSeconds = Integer.parseInt(pro
				.getProperty("threadpool.keepAliveSeconds"));
		int queueCap = Integer.parseInt(pro
				.getProperty("threadpool.queueCapacity"));
		BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(
				queueCap);
		this.threadPool = new ThreadPoolExecutor(corePoolSize, maxPoolSize,
				keepAliveSeconds, TimeUnit.SECONDS, queue);
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	@Override
	public void run() {
		while (this.isRunning) {
			try {
				String keyword = this.queue.take();// 从线程池中取主题词
				String keywordUrl = "https://www.quora.com/search?q=" + keyword;// 得到主题网页链接
				Properties properities = PropertyUtil.getProperties();
				String course = properities.getProperty("course");// 得到课程名
				String catalog = "file/datacollection/" + course;// 得到课程目录
				String keywordFilepath = catalog + "/" + keyword;
				File dir = new File(keywordFilepath);
				if (!dir.exists()) {
					dir.mkdir();
				}
				String keywordFilename = keywordFilepath +"/"+ keyword + ".html";
				// 爬取主题页面
				Selenium.seleniumCrawlerSubject(keywordFilename, keywordUrl);
				// 爬取主题页面（姚思雨师姐需要的，取Quora中的Top10的话题）
				// 区别：拉动6次滚动条
//				Selenium.seleniumCrawlerSubjectForTop10(keywordFilename, keywordUrl);
				// 解析主题页面
				File input = new File(keywordFilename);
				Document doc = Jsoup.parse(input, "UTF-8", "http://www.quora.com");
				// 获取所有问题网页链接
				Elements s =  doc.select("div.title").select("a.question_link").select("a[href]");
				String urls[] = new String[s.size()];
				for (int i = 0; i < s.size(); i++) {
					Element link = s.get(i);
					urls[i] = "http://www.quora.com" + link.attr("href");// 将所有链接存到urls数组里面
					System.out.println(String.format("问题页面链接为：" + " * " + (i + 1) + ": <%s>  (%s)", urls[i], link.text()));
					// 获取到问题网页url，扔给线程池处理
					String fileName = keyword + i + ".html";
					QtAllProcessor pro = new QtAllProcessor(keyword, fileName, urls[i]);
					this.threadPool.execute(pro);
				}
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
