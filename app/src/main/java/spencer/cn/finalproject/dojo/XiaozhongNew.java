package spencer.cn.finalproject.dojo;

public class XiaozhongNew {
	
	// 标识
    private Long uid;

    // 标题
    private String title;

    // 图片路径
    private String pictureUrl;

    // 发表时间 
    private String createDate;

    // 发表用户
    private Long userId;

    // 点击量
    private Long hits;


    // 内容
    private String content;
    
    public XiaozhongNew() {
		super();
	}


	public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getHits() {
		return hits;
	}

	public void setHits(Long hits) {
		this.hits = hits;
	}




    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}