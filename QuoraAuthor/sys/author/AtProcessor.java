package sys.author;

import sys.author.utils.AtFileProcessor;

public class AtProcessor implements Runnable {

	private String keyword;
	private String atName;
	private String atUrl;

	public AtProcessor(String keyword, String name, String url) {
		this.keyword = keyword;
		this.atName = name;
		this.atUrl = url;
	}

	@Override
	public void run() {
		AtFileProcessor fp = new AtFileProcessor(this.keyword, this.atName, this.atUrl);
		try {
			System.out.println("���ر���������ҳurl��" + this.atUrl);
			fp.saveAuthor();
//			fp.saveGif();

		} catch (Exception e) {
			System.out.println("���ر���������ҳʧ�ܣ�url��" + this.atUrl);
			e.printStackTrace();
		}

	}

}
