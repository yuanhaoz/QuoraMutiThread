package sys.authorall;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import base.DirFile;
import sys.gifspider.utils.PropertyUtil;

public class AtAllMain {
	public static void main(String[] args) {
		init();
	}

	public static void init() {
		Properties pro = PropertyUtil.getProperties();
		String course = pro.getProperty("course");// �õ��γ���
		String catalog = "file/datacollection/" + course;// �õ��γ�Ŀ¼
		// ��ȡ�����ļ���
		ArrayList<String> a = DirFile.getFolderFileNamesFromDirectorybyArraylist(catalog);
		BlockingQueue<String> queue = new LinkedBlockingQueue<String>(a.size());// ��СΪ�ؼ��ʸ���
		Iterator<String> it = a.iterator();// ���õ�����
		while(it.hasNext()){// �ж��Ƿ�����һ��
			String keyword = it.next();// �õ��ؼ���
			System.out.println("keyword:" + keyword);
			queue.add(keyword);// ���ؼ��ʼ�������еȴ��߳�ȡ�������߳���ȡ�ùؼ�����ҳ
		}
		int spiderCount = Integer.parseInt(pro.getProperty("spiderThread"));
		for (int i = 0; i < spiderCount; i++) {
			AtAllSpider spider = new AtAllSpider(queue);
			Thread t = new Thread(spider);
			t.start();
		}
	}
}
