package sys.author;

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

public class AtSpider implements Runnable {

	volatile boolean isRunning = true;
	private ThreadPoolExecutor threadPool;
	BlockingQueue<String> queue;

	public AtSpider(BlockingQueue<String> queue) {
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
				String catalog = "file/datacollection/spidertest/" + keyword;// �õ��γ�Ŀ¼
				String keywordFilename = catalog +"/"+ keyword + ".html";
				// ��������ҳ�棬�õ�����ҳ������
				File input = new File(keywordFilename);
				Document doc = Jsoup.parse(input, "UTF-8", "http://www.quora.com");
				Elements s =  doc.select("div.title").select("a.question_link").select("a[href]");
				for (int i = 0; i < s.size(); i++) {
					// ����ÿ��������ҳ�õ�������ҳ������
					String authorPath = catalog +"/"+keyword + i + ".html";
					File authorFile = new File(authorPath);
					Document atdoc = Jsoup.parse(authorFile, "UTF-8", "http://www.quora.com");
					Elements authors = atdoc.select("div.pagedlist_item").select("span.feed_item_answer_user");
					System.out.println("���߸�����"+authors.size());
//					for (int m = 0; m < authors.size(); m++) {// �����������ߣ���
					for (int m = 0; m < authors.size()-3; m++) {// �����������ߣ�Mywork������ȡ������ҳ�棩
						Element a = authors.get(m);
						Elements b = a.select("a.user");
						if (b.size() == 0) {
							System.out.println("������Ϣ�����ڣ�����");
						} else {
							Element author = b.get(0);
							// �õ�����ҳ�����Ӻͱ����ļ���
							String atName = keyword+i+"_author_"+m+".html";
							String urls = author.attr("href");
							if (urls.startsWith("http://")) {
								// ��ȡ��������ҳurl���Ӹ��̳߳ش���
								AtProcessor pro = new AtProcessor(keyword, atName, urls);
								this.threadPool.execute(pro);
							} else if (urls.startsWith("/")) {
								urls = "http://www.quora.com/" + urls; 
								// ��ȡ��������ҳurl���Ӹ��̳߳ش���
								AtProcessor pro = new AtProcessor(keyword, atName, urls);
								this.threadPool.execute(pro);
							}
						}
					}
					
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
