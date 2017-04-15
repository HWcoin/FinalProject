package spencer.cn.finalproject.dojo;

/**
 * 登录返回信息
 * @author wulizhou
 *
 */
public class LoginResp {

	private UserInfo user;
	private String accessToken;
	private UserConfig userConfig;

	public UserInfo getUser() {
		return user;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public UserConfig getUserConfig() {
		return userConfig;
	}

	public void setUserConfig(UserConfig userConfig) {
		this.userConfig = userConfig;
	}

	public LoginResp(UserInfo user, String accessToken, UserConfig userConfig) {
		super();
		this.user = user;
		this.accessToken = accessToken;
		this.userConfig = userConfig;
	}

	public LoginResp() {
		super();
	}
	
}
