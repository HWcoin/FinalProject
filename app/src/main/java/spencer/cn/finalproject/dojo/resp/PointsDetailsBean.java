package spencer.cn.finalproject.dojo.resp;

import java.util.ArrayList;

import spencer.cn.finalproject.dojo.IntegralInfoResp;

/**
 * Created by Administrator on 2017/5/3.
 */

public class PointsDetailsBean {
    private int code;
    private String message;
    private ArrayList<IntegralInfoResp> data;

    public PointsDetailsBean() {
    }

    public PointsDetailsBean(int code, String message, ArrayList<IntegralInfoResp> data) {
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

    public ArrayList<IntegralInfoResp> getData() {
        return data;
    }

    public void setData(ArrayList<IntegralInfoResp> data) {
        this.data = data;
    }
}
