package spencer.cn.finalproject.dojo;

/**
 * Created by Administrator on 2017/4/24.
 */

public class UploadImgResp {
    private int code;
    private String message;
    private String data;

    public UploadImgResp() {
    }

    public UploadImgResp(int code, String message, String data) {
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
