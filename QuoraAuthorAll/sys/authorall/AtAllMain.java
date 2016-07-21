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
		String course = pro.getProperty("course");// 得到课程名
		String catalog = "file/datacollection/" + course;// 得到课程目录
		// 读取所有文件夹
		ArrayList<String> a = DirFile.getFolderFileNamesFromDirectorybyArraylist(catalog);
		BlockingQueue<String> queue = new LinkedBlockingQueue<String>(a.size());// 大小为关键词个数
		Iterator<String> it = a.iterator();// 设置迭代器
		while(it.hasNext()){// 判断是否有下一个
			String keyword = it.next();// 得到关键词
			System.out.println("keyword:" + keyword);
			queue.add(keyword);// 将关键词加入队列中等待线程取出，多线程爬取该关键词网页
		}
		int spiderCount = Integer.parseInt(pro.getProperty("spiderThread"));
		for (int i = 0; i < spiderCount; i++) {
			AtAllSpider spider = new AtAllSpider(queue);
			Thread t = new Thread(spider);
			t.start();
		}
	}
}
