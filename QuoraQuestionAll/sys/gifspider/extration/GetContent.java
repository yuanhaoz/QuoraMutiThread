package sys.gifspider.extration;

/**
 * zhengyuanhao  2015/6/30
 * ��ʵ�֣�quora  
 * ʵ�ֹ��ܣ���������ѧ��������ҳ��������ҳ������
 *			"��Ϣ��ȡ"
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
	 * ʵ�ֹ��ܣ�ʵ����Ϣ��ȡ
	 * @param course
	 */
	public static void realize() throws Exception {
		extraction("Top10");
	}
	
	/**
	 * ʵ�ֹ��ܣ����ڽ���ĳ�ſγ����������µ�������ҳ
	 *          �����ǿγ����������Excel
	 * @param course
	 */
	public static void extraction(String course) throws Exception {
		String catalog = "file/datacollection/" + course;
		ArrayList<String> a = DirFile.getFolderFileNamesFromDirectorybyArraylist(catalog);  //��ȡ�����ļ���
		Iterator<String> it = a.iterator();   //���õ�����
		while(it.hasNext()){                  //�ж��Ƿ�����һ��
			long start = System.currentTimeMillis();
			
			String keyword = it.next();       //�õ��ؼ���
			int pagelength = questionPageNumber(keyword);      
			down2Excel(keyword, pagelength);                   //��������ҳ�������ҳ��
			
			long end = System.currentTimeMillis();
			System.out.println("����" + keyword + "��������Ϣ��ʱ��" + (end - start)/1000 + "��...");
		}
	}

	/**
	 * ʵ�ֹ��ܣ��õ�ĳ���ؼ���ҳ��������ҳ�������
	 *          �����ǹؼ��ʣ����������ҳ������pagelength
	 * @param course
	 */
	public static int questionPageNumber(String keyword) throws Exception {
		int pageLength = 0;
		String[] urls = DataCollection.getQuestionURLs(keyword);
		String[] testResult = { "aa", "bb" };
		if (urls.equals(testResult)) {
			System.out.println("�����ڵ�һ�����ӣ�����");
		} else {
			pageLength = urls.length;
		}
		System.out.println(keyword + "��������Ϊ��" + pageLength);
		return pageLength;
	}
	
	/**
	 * ʵ�ֹ��ܣ�����������ҳ��������ҳ��ʵ����Ϣ�ĳ�ȡ�����䱣�浽���ص�Excel����
	 *          �����ǹؼ��ʺ�����ҳ������������Ǳ�����Ϣ��ȡ�����Excel
	 * @param course
	 */
	public static void down2Excel(String keyword, int pageLength) throws Exception {
		
		//��������Ŀ¼
		String catalog = KeywordCatalogDesign.GetKeywordCatalog(keyword);
		String filepath = catalog + keyword + "-tag.xls";
		WritableWorkbook workbook = Workbook.createWorkbook(new File(filepath));
		WritableSheet sheet = workbook.createSheet("��Ϣ��ȡ", 0);
		initalTitle(sheet);
		WritableCellFormat wcf_center = ExcelSet.setCenterText();   //���õ�Ԫ�����ĸ�ʽ
		
		//�����Ϣ
		int number = 1;
		for (int i = 0; i < pageLength; i++) {
			String path = catalog + keyword + i + ".html";
			File file = new File(path);
			if (!file.exists()) {
				System.out.println(path + "  ������");
			} else {
				
				//��ʼ��������ҳ�棬��������й���Ϣ������֮��
				System.out.println("\n��ʼ������ " + path);
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
				
				sheet.setRowView(number, 1300, false); // �����и�
				sheet.setColumnView(0, 20);
				sheet.setColumnView(1, 60);
				
				//�������µĻش�����ߵ���Ϣ�������
				int realanswernumber = FeatureExtractionForHttpClient.countRealAnswerNumber(doc);
				for (int m = number; m < number + realanswernumber; m++) {
					
					sheet.setRowView(m + 1, 1300, false); // �����и�
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
			System.out.println(path + " �Ѿ��ɹ������� " + catalog + keyword + "-tag.xls");
		}
		ExcelSet.close(workbook);  //�رչ����ռ�
	}

	/**
	 * ʵ�ֹ��ܣ����������������Ǳ��洢·��filepath������Ǳ�����WritableSheet
	 * @param filepath
	 */
	public static WritableSheet createSheet(String filepath) throws Exception {
		WritableWorkbook workbook = Workbook.createWorkbook(new File(filepath));
		WritableSheet sheet = workbook.createSheet("��Ϣ��ȡ", 0);
		return sheet;
	}
	
	/**
	 * ʵ�ֹ��ܣ����ñ���һ��
	 * @param filepath
	 */
	public static void initalTitle(WritableSheet sheet) throws Exception {
		WritableCellFormat wcf_title = ExcelSet.setTitleText();   //���õ�Ԫ�����ĸ�ʽ
		sheet.addCell(new Label(0, 0, "QA", wcf_title));
		sheet.addCell(new Label(1, 0, "Content", wcf_title));
		sheet.setRowView(0, 700, false);                         // �����и�
	}

	
}
