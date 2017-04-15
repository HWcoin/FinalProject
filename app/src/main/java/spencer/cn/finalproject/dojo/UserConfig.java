package spencer.cn.finalproject.dojo;

import java.util.List;

/**
 * 用户配置信息
 * @author wulizhou
 *
 */
public class UserConfig {

	private List<Long> userNewType;
	
	private List<NewType> newTypes;
	
	public List<NewType> getNewTypes() {
		return newTypes;
	}

	public void setNewTypes(List<NewType> newTypes) {
		this.newTypes = newTypes;
	}

	public List<Long> getUserNewType() {
		return userNewType;
	}

	public void setUserNewType(List<Long> userNewType) {
		this.userNewType = userNewType;
	}
	
}
