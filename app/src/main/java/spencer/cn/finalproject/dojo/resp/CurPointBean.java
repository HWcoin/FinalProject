package spencer.cn.finalproject.dojo.resp;

import spencer.cn.finalproject.dojo.PointsBean;

/**
 * Created by Administrator on 2017/5/5.
 */

public class CurPointBean {
    private int code;
    private String message;
    private PointsBean data;

    public CurPointBean() {
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

    public PointsBean getData() {
        return data;
    }

    public void setData(PointsBean data) {
        this.data = data;
    }
}
