package spencer.cn.finalproject.dojo;

public class GetDetailsResp {

	// 新闻标识
	private Long uid;
	// 新闻路径
	private String url;
	// 点击量
	private Long hits;

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getHits() {
		return hits;
	}

	public void setHits(Long hits) {
		this.hits = hits;
	}

}
