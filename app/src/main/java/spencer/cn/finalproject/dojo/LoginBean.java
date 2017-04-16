package spencer.cn.finalproject.dojo;

/**
 * Created by Administrator on 2017/4/15.
 */

public class LoginBean {
    private int code;
    private String message;
    private LoginResp data;

    public LoginBean() {
    }

    public LoginBean(int code, String message, LoginResp data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LoginResp getData() {
        return data;
    }

    public void setData(LoginResp data) {
        this.data = data;
    }
}
