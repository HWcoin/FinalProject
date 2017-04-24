package spencer.cn.finalproject.dojo;

import java.util.Date;

/**
 * 评论信息
 * @author wulizhou
 */
public class CommentInfoResp {

	private Long commentId;	// 评论标识
	private Long userId; // 用户标识
	private String avatar; // 头像
	private String username; // 用户名称
	private String content; // 评论内容
	private Date createDate; // 评论时间
	
	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
