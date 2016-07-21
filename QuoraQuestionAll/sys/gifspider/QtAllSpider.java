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
	 * �̳߳س�ʼ��
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
				String keyword = this.queue.take();// ���̳߳���ȡ�����
				String keywordUrl = "https://www.quora.com/search?q=" + keyword;// �õ�������ҳ����
				Properties properities = PropertyUtil.getProperties();
				String course = properities.getProperty("course");// �õ��γ���
				String catalog = "file/datacollection/" + course;// �õ��γ�Ŀ¼
				String keywordFilepath = catalog + "/" + keyword;
				File dir = new File(keywordFilepath);
				if (!dir.exists()) {
					dir.mkdir();
				}
				String keywordFilename = keywordFilepath +"/"+ keyword + ".html";
				// ��ȡ����ҳ��
				Selenium.seleniumCrawlerSubject(keywordFilename, keywordUrl);
				// ��ȡ����ҳ�棨Ҧ˼��ʦ����Ҫ�ģ�ȡQuora�е�Top10�Ļ��⣩
				// ��������6�ι�����
//				Selenium.seleniumCrawlerSubjectForTop10(keywordFilename, keywordUrl);
				// ��������ҳ��
				File input = new File(keywordFilename);
				Document doc = Jsoup.parse(input, "UTF-8", "http://www.quora.com");
				// ��ȡ����������ҳ����
				Elements s =  doc.select("div.title").select("a.question_link").select("a[href]");
				String urls[] = new String[s.size()];
				for (int i = 0; i < s.size(); i++) {
					Element link = s.get(i);
					urls[i] = "http://www.quora.com" + link.attr("href");// ���������Ӵ浽urls��������
					System.out.println(String.format("����ҳ������Ϊ��" + " * " + (i + 1) + ": <%s>  (%s)", urls[i], link.text()));
					// ��ȡ��������ҳurl���Ӹ��̳߳ش���
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
