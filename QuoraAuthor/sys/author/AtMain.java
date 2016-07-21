package sys.author;

import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import sys.gifspider.utils.PropertyUtil;

public class AtMain {
	public static void main(String[] args) {
		init();
	}

	public static void init() {
		Properties pro = PropertyUtil.getProperties();
		String keyword = pro.getProperty("keyword");// 得到关键词
		BlockingQueue<String> queue = new LinkedBlockingQueue<String>(1);// 大小为关键词个数
		queue.add(keyword);// 将关键词加入队列中等待线程取出，多线程爬取该关键词网页
		int spiderCount = Integer.parseInt(pro.getProperty("spiderThread"));
		for (int i = 0; i < spiderCount; i++) {
			AtSpider spider = new AtSpider(queue);
			Thread t = new Thread(spider);
			t.start();
		}
	}
}
