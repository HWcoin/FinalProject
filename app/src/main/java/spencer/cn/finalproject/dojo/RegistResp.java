package spencer.cn.finalproject.dojo;

/**
 * Created by Administrator on 2017/4/15.
 */

public class RegistResp {
    private int code;
    private String message;
    private Object data;

    public RegistResp() {
    }

    public RegistResp(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public RegistResp(int code, String message) {
        this.code = code;
        this.message = message;
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
}
