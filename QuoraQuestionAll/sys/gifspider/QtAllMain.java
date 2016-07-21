package sys.gifspider;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import base.DirFile;
import sys.gifspider.utils.PropertyUtil;

public class QtAllMain {
	public static void main(String[] args) throws Exception {
		init();
//		init2();
//		init3();
	}

	public static void init() throws Exception {
		long start = System.currentTimeMillis();
		Properties pro = PropertyUtil.getProperties();
		String course = pro.getProperty("course");// �õ��γ���
		String catalog = "file/datacollection/" + course;// �õ��γ�Ŀ¼
		ArrayList<String> a = DirFile.getFolderFileNamesFromDirectorybyArraylist(catalog);// ��ȡ�����ļ���
		BlockingQueue<String> queue = new LinkedBlockingQueue<String>(a.size());// ��СΪ�ؼ��ʸ���
		Iterator<String> it = a.iterator();// ���õ�����
		while(it.hasNext()){// �ж��Ƿ�����һ��
			String keyword = it.next();// �õ��ؼ���
			System.out.println("keyword:" + keyword);
			queue.add(keyword);// ���ؼ��ʼ�������еȴ��߳�ȡ�������߳���ȡ�ùؼ�����ҳ
		}
		int spiderCount = Integer.parseInt(pro.getProperty("spiderThread"));
		for (int i = 0; i < spiderCount; i++) {
			QtAllSpider spider = new QtAllSpider(queue);
			Thread t = new Thread(spider);
			t.start();
//			System.out.println(t.isAlive());
		}
		long end = System.currentTimeMillis();
		System.out.println("��ʱ��"+(end-start)/1000+"seconds ---> "+(end-start)/6000+"minutes");
	}
	
	public static void init2() {
		Properties pro = PropertyUtil.getProperties();
		int spiderCount = Integer.parseInt(pro.getProperty("spiderThread"));
		String filePath = "file/datacollection/Data_structure1";
		File[] f = new File(filePath).listFiles();
		for(int i = 0; i < f.length; i++){
			String keyword = f[i].getName();
			BlockingQueue<String> queue = new LinkedBlockingQueue<String>(1);
			queue.add(keyword);// ���ؼ��ʼ�������еȴ��߳�ȡ�������߳���ȡ�ùؼ�����ҳ
			for (int j = 0; j < spiderCount; j++) {
				QtAllSpider spider = new QtAllSpider(queue);
				Thread t = new Thread(spider);
				t.start();
			}
		}
	}
	
	public static void init3() {
		
		Properties pro = PropertyUtil.getProperties();
		String course = pro.getProperty("course");// �õ��γ���
		String catalog = "file/datacollection/" + course;// �õ��γ�Ŀ¼
		ArrayList<String> a = DirFile.getFolderFileNamesFromDirectorybyArraylist(catalog);// ��ȡ�����ļ���
		BlockingQueue<String> queue = new LinkedBlockingQueue<String>(1);
		String keyword = a.get(0);// �õ��ؼ���
		System.out.println("keyword:" + keyword);
		queue.add(keyword);// ���ؼ��ʼ�������еȴ��߳�ȡ�������߳���ȡ�ùؼ�����ҳ
		int spiderCount = Integer.parseInt(pro.getProperty("spiderThread"));
		for (int i = 0; i < spiderCount; i++) {
			QtAllSpider spider = new QtAllSpider(queue);
			Thread t = new Thread(spider);
			t.start();
		}
//		Thread.interrupted();
	}
}
