package spencer.cn.finalproject.dojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/15.
 */

public class BaseNewType implements Serializable{
    private int code;
    private String message;
    private ArrayList<NewType> data;

    public BaseNewType() {
    }

    public BaseNewType(int code, String message, ArrayList<NewType> data) {
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

    public ArrayList<NewType> getData() {
        return data;
    }

    public void setData(ArrayList<NewType> data) {
        this.data = data;
    }
}
