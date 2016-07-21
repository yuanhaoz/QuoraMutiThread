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
		String course = pro.getProperty("course");// 得到课程名
		String catalog = "file/datacollection/" + course;// 得到课程目录
		ArrayList<String> a = DirFile.getFolderFileNamesFromDirectorybyArraylist(catalog);// 读取所有文件名
		BlockingQueue<String> queue = new LinkedBlockingQueue<String>(a.size());// 大小为关键词个数
		Iterator<String> it = a.iterator();// 设置迭代器
		while(it.hasNext()){// 判断是否有下一个
			String keyword = it.next();// 得到关键词
			System.out.println("keyword:" + keyword);
			queue.add(keyword);// 将关键词加入队列中等待线程取出，多线程爬取该关键词网页
		}
		int spiderCount = Integer.parseInt(pro.getProperty("spiderThread"));
		for (int i = 0; i < spiderCount; i++) {
			QtAllSpider spider = new QtAllSpider(queue);
			Thread t = new Thread(spider);
			t.start();
//			System.out.println(t.isAlive());
		}
		long end = System.currentTimeMillis();
		System.out.println("耗时："+(end-start)/1000+"seconds ---> "+(end-start)/6000+"minutes");
	}
	
	public static void init2() {
		Properties pro = PropertyUtil.getProperties();
		int spiderCount = Integer.parseInt(pro.getProperty("spiderThread"));
		String filePath = "file/datacollection/Data_structure1";
		File[] f = new File(filePath).listFiles();
		for(int i = 0; i < f.length; i++){
			String keyword = f[i].getName();
			BlockingQueue<String> queue = new LinkedBlockingQueue<String>(1);
			queue.add(keyword);// 将关键词加入队列中等待线程取出，多线程爬取该关键词网页
			for (int j = 0; j < spiderCount; j++) {
				QtAllSpider spider = new QtAllSpider(queue);
				Thread t = new Thread(spider);
				t.start();
			}
		}
	}
	
	public static void init3() {
		
		Properties pro = PropertyUtil.getProperties();
		String course = pro.getProperty("course");// 得到课程名
		String catalog = "file/datacollection/" + course;// 得到课程目录
		ArrayList<String> a = DirFile.getFolderFileNamesFromDirectorybyArraylist(catalog);// 读取所有文件名
		BlockingQueue<String> queue = new LinkedBlockingQueue<String>(1);
		String keyword = a.get(0);// 得到关键词
		System.out.println("keyword:" + keyword);
		queue.add(keyword);// 将关键词加入队列中等待线程取出，多线程爬取该关键词网页
		int spiderCount = Integer.parseInt(pro.getProperty("spiderThread"));
		for (int i = 0; i < spiderCount; i++) {
			QtAllSpider spider = new QtAllSpider(queue);
			Thread t = new Thread(spider);
			t.start();
		}
//		Thread.interrupted();
	}
}
