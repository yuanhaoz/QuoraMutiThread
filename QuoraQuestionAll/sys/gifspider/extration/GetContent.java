package sys.gifspider.extration;

/**
 * zhengyuanhao  2015/6/30
 * 简单实现：quora  
 * 实现功能：解析三门学科问题网页和作者网页的数据
 *			"信息抽取"
 *
 */

import Jsoup.JsoupParse;
import informationextraction.FeatureExtraction;
import informationextraction.FeatureExtractionForHttpClient;
import dataCollection.DataCollection;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.jsoup.nodes.Document;

import base.DirFile;
import basic.KeywordCatalogDesign;
import excel.ExcelSet;

public class GetContent{
	
	public static void main(String[] args){
		try {
			realize();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 实现功能：实现信息抽取
	 * @param course
	 */
	public static void realize() throws Exception {
		extraction("Top10");
	}
	
	/**
	 * 实现功能：用于解析某门课程所有主题下的所有网页
	 *          输入是课程名，输出是Excel
	 * @param course
	 */
	public static void extraction(String course) throws Exception {
		String catalog = "file/datacollection/" + course;
		ArrayList<String> a = DirFile.getFolderFileNamesFromDirectorybyArraylist(catalog);  //读取所有文件名
		Iterator<String> it = a.iterator();   //设置迭代器
		while(it.hasNext()){                  //判断是否有下一个
			long start = System.currentTimeMillis();
			
			String keyword = it.next();       //得到关键词
			int pagelength = questionPageNumber(keyword);      
			down2Excel(keyword, pagelength);                   //解析问题页面和作者页面
			
			long end = System.currentTimeMillis();
			System.out.println("解析" + keyword + "的所有信息用时：" + (end - start)/1000 + "秒...");
		}
	}

	/**
	 * 实现功能：得到某个关键词页面下问题页面的总数
	 *          输入是关键词，输出是问题页面总数pagelength
	 * @param course
	 */
	public static int questionPageNumber(String keyword) throws Exception {
		int pageLength = 0;
		String[] urls = DataCollection.getQuestionURLs(keyword);
		String[] testResult = { "aa", "bb" };
		if (urls.equals(testResult)) {
			System.out.println("不存在第一层链接！！！");
		} else {
			pageLength = urls.length;
		}
		System.out.println(keyword + "问题总数为：" + pageLength);
		return pageLength;
	}
	
	/**
	 * 实现功能：解析问题网页和作者网页，实现信息的抽取，将其保存到本地的Excel表中
	 *          输入是关键词和问题页面数量，输出是保存信息抽取结果的Excel
	 * @param course
	 */
	public static void down2Excel(String keyword, int pageLength) throws Exception {
		
		//建立保存目录
		String catalog = KeywordCatalogDesign.GetKeywordCatalog(keyword);
		String filepath = catalog + keyword + "-tag.xls";
		WritableWorkbook workbook = Workbook.createWorkbook(new File(filepath));
		WritableSheet sheet = workbook.createSheet("信息抽取", 0);
		initalTitle(sheet);
		WritableCellFormat wcf_center = ExcelSet.setCenterText();   //设置单元格正文格式
		
		//存放信息
		int number = 1;
		for (int i = 0; i < pageLength; i++) {
			String path = catalog + keyword + i + ".html";
			File file = new File(path);
			if (!file.exists()) {
				System.out.println(path + "  不存在");
			} else {
				
				//开始解析问题页面，将问题的有关信息填入表格之中
				System.out.println("\n开始解析： " + path);
				Document doc = JsoupParse.parsePathText(path);
				
				ArrayList<String> titlelist = new ArrayList<String>();
				titlelist.add(keyword + i);
				titlelist.add(FeatureExtractionForHttpClient.questionContent(doc) + 
						"\n" + FeatureExtractionForHttpClient.questionExpandInfo(doc));
				sheet.addCell(new Label(0, number, titlelist.get(0), wcf_center));
				sheet.addCell(new Label(1, number, titlelist.get(1), wcf_center));
//				for(int j = 0; j < 2; j++){
//					System.out.println(j);
//					sheet.addCell(new Label(j, number, titlelist.get(j), wcf_center));
//				}
				
				sheet.setRowView(number, 1300, false); // 设置行高
				sheet.setColumnView(0, 20);
				sheet.setColumnView(1, 60);
				
				//将问题下的回答和作者的信息填入表中
				int realanswernumber = FeatureExtractionForHttpClient.countRealAnswerNumber(doc);
				for (int m = number; m < number + realanswernumber; m++) {
					
					sheet.setRowView(m + 1, 1300, false); // 设置行高
					String answercontent = "null";
					try{
						answercontent = FeatureExtractionForHttpClient.answerContent(doc, m - number);
					} catch (Exception e) {
						System.out.println("a bug...");
					}
					
					sheet.addCell(new Label(0, m + 1, keyword + i + "_" + (m - number) + " ",wcf_center));
					sheet.addCell(new Label(1, m + 1, answercontent, wcf_center));
					
				}
				number = number + realanswernumber + 1;
			}
			System.out.println(path + " 已经成功解析到 " + catalog + keyword + "-tag.xls");
		}
		ExcelSet.close(workbook);  //关闭工作空间
	}

	/**
	 * 实现功能：建立工作表，输入是表格存储路径filepath，输出是表格对象WritableSheet
	 * @param filepath
	 */
	public static WritableSheet createSheet(String filepath) throws Exception {
		WritableWorkbook workbook = Workbook.createWorkbook(new File(filepath));
		WritableSheet sheet = workbook.createSheet("信息抽取", 0);
		return sheet;
	}
	
	/**
	 * 实现功能：设置表格第一行
	 * @param filepath
	 */
	public static void initalTitle(WritableSheet sheet) throws Exception {
		WritableCellFormat wcf_title = ExcelSet.setTitleText();   //设置单元格正文格式
		sheet.addCell(new Label(0, 0, "QA", wcf_title));
		sheet.addCell(new Label(1, 0, "Content", wcf_title));
		sheet.setRowView(0, 700, false);                         // 设置行高
	}

	
}
