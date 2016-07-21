package sys.authorall;

import java.io.File;

public class AtDelete {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		deleteAll();
	}
	
	public static void deleteAll(){
		String path = "file\\datacollection\\Data_structure";
		File[] f = new File(path).listFiles();
		for(int i = 0; i < f.length; i++){
			String keyword = f[i].getName();
			String filePath = path + "\\" + keyword;
			delete(filePath);
		}
		
	}
	
	public static void delete(String filePath){	
		File[] f = new File(filePath).listFiles();
		for(int i = 0; i < f.length; i++){
			String fileName = f[i].getName();
			if(f[i].length()<35000 && fileName.contains("author_")){
				System.out.println("delete:"+f[i].getName());
				f[i].delete();
			}
		}
		
	}

}
