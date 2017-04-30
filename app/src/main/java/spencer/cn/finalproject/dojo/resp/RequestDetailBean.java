package spencer.cn.finalproject.dojo.resp;

import spencer.cn.finalproject.dojo.GetDetailsResp;

/**
 * Created by Administrator on 2017/4/29.
 */

public class RequestDetailBean {
    private int code;
    private String message;
    private GetDetailsResp data;

    public RequestDetailBean() {
    }

    public RequestDetailBean(int code, String message, GetDetailsResp data) {
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

    public GetDetailsResp getData() {
        return data;
    }

    public void setData(GetDetailsResp data) {
        this.data = data;
    }
}
