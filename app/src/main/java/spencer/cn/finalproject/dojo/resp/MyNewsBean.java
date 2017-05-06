package spencer.cn.finalproject.dojo.resp;

import spencer.cn.finalproject.dojo.XiaozhongNew;

/**
 * Created by Administrator on 2017/5/6.
 */

public class MyNewsBean {
    private int code;
    private String message;
    private XiaozhongNew data;

    public MyNewsBean() {
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

    public XiaozhongNew getData() {
        return data;
    }

    public void setData(XiaozhongNew data) {
        this.data = data;
    }
}
