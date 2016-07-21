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
		String keyword = pro.getProperty("keyword");// �õ��ؼ���
		BlockingQueue<String> queue = new LinkedBlockingQueue<String>(1);// ��СΪ�ؼ��ʸ���
		queue.add(keyword);// ���ؼ��ʼ�������еȴ��߳�ȡ�������߳���ȡ�ùؼ�����ҳ
		int spiderCount = Integer.parseInt(pro.getProperty("spiderThread"));
		for (int i = 0; i < spiderCount; i++) {
			AtSpider spider = new AtSpider(queue);
			Thread t = new Thread(spider);
			t.start();
		}
	}
}
