package spencer.cn.finalproject.dojo.resp;

/**
 * Created by Administrator on 2017/5/19.
 */

public class UserNameResp {
    private String message;
    private int code;
    private UserNameBean data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public UserNameBean getData() {
        return data;
    }

    public void setData(UserNameBean data) {
        this.data = data;
    }
}
