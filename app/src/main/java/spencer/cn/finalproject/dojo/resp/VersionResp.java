package spencer.cn.finalproject.dojo.resp;

import spencer.cn.finalproject.dojo.VersionBean;

/**
 * Created by Administrator on 2017/5/19.
 */

public class VersionResp {
    private int code;
    private String message;
    private VersionBean data;

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

    public VersionBean getData() {
        return data;
    }

    public void setData(VersionBean data) {
        this.data = data;
    }
}
