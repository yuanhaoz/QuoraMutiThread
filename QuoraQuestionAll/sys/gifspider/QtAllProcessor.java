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
			System.out.println("���ر���������ҳurl��" + this.imgUrl);
			fp.saveQuestion();
//			fp.saveGif();

		} catch (Exception e) {
			System.out.println("���ر���������ҳʧ�ܣ�url��" + this.imgUrl);
			e.printStackTrace();
		}

	}

}
