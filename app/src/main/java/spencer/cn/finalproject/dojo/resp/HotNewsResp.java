package spencer.cn.finalproject.dojo.resp;

import java.util.ArrayList;

import spencer.cn.finalproject.dojo.HotNews;

/**
 * Created by Administrator on 2017/5/5.
 */

public class HotNewsResp {
    private int code;
    private String message;
    private ArrayList<HotNews> data;

    public HotNewsResp() {
    }

    public HotNewsResp(int code, String message, ArrayList<HotNews> data) {
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

    public ArrayList<HotNews> getData() {
        return data;
    }

    public void setData(ArrayList<HotNews> data) {
        this.data = data;
    }
}
