package sys.authorall;

import sys.authorall.utils.AtAllFileProcessor;

public class AtAllProcessor implements Runnable {

	private String keyword;
	private String atName;
	private String atUrl;

	public AtAllProcessor(String keyword, String name, String url) {
		this.keyword = keyword;
		this.atName = name;
		this.atUrl = url;
	}

	@Override
	public void run() {
		AtAllFileProcessor fp = new AtAllFileProcessor(this.keyword, this.atName, this.atUrl);
		try {
//			System.out.println("���ر���������ҳurl��" + this.atUrl);
			fp.saveAuthor();
//			fp.saveGif();

		} catch (Exception e) {
			System.out.println("���ر���������ҳʧ�ܣ�url��" + this.atUrl);
			e.printStackTrace();
		}

	}

}
