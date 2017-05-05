package spencer.cn.finalproject.dojo.resp;

import java.util.ArrayList;

import spencer.cn.finalproject.dojo.CollectionListResp;

/**
 * Created by Administrator on 2017/5/6.
 */

public class CollectListBean {
    private int code;
    private String message;
    private ArrayList<CollectionListResp> data;

    public CollectListBean() {
    }

    public CollectListBean(int code, String message, ArrayList<CollectionListResp> data) {
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

    public ArrayList<CollectionListResp> getData() {
        return data;
    }

    public void setData(ArrayList<CollectionListResp> data) {
        this.data = data;
    }
}
