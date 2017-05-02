package spencer.cn.finalproject.dojo;

import java.util.Date;

public class IntegralInfoResp {

	// 变动积分
	private Long changeIntegral;

	// 变动说明
	private String remarks;

	// 变动日期
	private Date createDate;

	public Long getChangeIntegral() {
		return changeIntegral;
	}

	public void setChangeIntegral(Long changeIntegral) {
		this.changeIntegral = changeIntegral;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
