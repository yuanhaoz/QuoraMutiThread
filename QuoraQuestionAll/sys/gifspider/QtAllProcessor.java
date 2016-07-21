package sys.gifspider;

import sys.gifspider.utils.QtAllFileProcessor;

public class QtAllProcessor implements Runnable {

	private String keyword;
	private String imgName;
	private String imgUrl;

	public QtAllProcessor(String keyword, String name, String url) {
		this.keyword = keyword;
		this.imgName = name;
		this.imgUrl = url;
	}

	@Override
	public void run() {
		QtAllFileProcessor fp = new QtAllFileProcessor(this.keyword, this.imgName, this.imgUrl);
		try {
			System.out.println("下载保存问题网页url：" + this.imgUrl);
			fp.saveQuestion();
//			fp.saveGif();

		} catch (Exception e) {
			System.out.println("下载保存问题网页失败，url：" + this.imgUrl);
			e.printStackTrace();
		}

	}

}
