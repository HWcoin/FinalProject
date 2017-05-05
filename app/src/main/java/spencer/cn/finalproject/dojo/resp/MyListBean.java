package spencer.cn.finalproject.dojo.resp;

import java.util.ArrayList;

import spencer.cn.finalproject.dojo.XiaozhongNewResp;

/**
 * Created by Administrator on 2017/5/5.
 */

public class MyListBean {
    private int code;
    private String message;
    private ArrayList<XiaozhongNewResp> data;

    public MyListBean() {
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

    public ArrayList<XiaozhongNewResp> getData() {
        return data;
    }

    public void setData(ArrayList<XiaozhongNewResp> data) {
        this.data = data;
    }
}
