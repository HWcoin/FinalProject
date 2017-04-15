package spencer.cn.finalproject.dojo;

public class UserInfo{
	
	private Long uid;

    private String email;

    private String username;

    private String avatar;

    private Long integral;

    private String refEmail;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getIntegral() {
        return integral;
    }

    public void setIntegral(Long integral) {
        this.integral = integral;
    }

    public String getRefEmail() {
        return refEmail;
    }

    public void setRefEmail(String refEmail) {
        this.refEmail = refEmail;
    }

}