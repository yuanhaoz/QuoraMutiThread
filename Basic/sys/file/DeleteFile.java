package sys.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import base.DirFile;

public class DeleteFile {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		delete();
	}
	
	public static void delete(){
		for(int k = 1; k < 11; k++){
			String course = "Data_structure" + k;
			String catalog = "file/datacollection/" + course;
			ArrayList<String> a = DirFile.getFolderFileNamesFromDirectorybyArraylist(catalog);
			for(int i = 0;i < a.size(); i++){
				String keyword = a.get(i);
				String filepath = catalog + "/" + keyword + "/";
				File html = new File(filepath);
				File[] pages = html.listFiles();
				for(int j = 0; j < pages.length; j++){
					try {
						Document doc = Jsoup.parse(pages[j], "UTF-8", "http://www.quora.com");
						Elements title = doc.select("div.title");
						if(title.size()!=0){
							String content = title.get(0).text();
							if(content.equals("无法显示此页")){
								pages[j].delete();
//								System.out.println("#######################");
							} else {
//								System.out.println(">>>>>>>>>>>>>>>>>");
							}
						} else {
//							System.out.println("****************");
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
	}

}
