package spencer.cn.finalproject.dojo.resp;

import java.util.ArrayList;

import spencer.cn.finalproject.dojo.CommentInfoResp;

/**
 * Created by Administrator on 2017/4/29.
 */

public class CommentResp {
    private int code;
    private String Message;
    private ArrayList<CommentInfoResp> data;

    public CommentResp() {
    }

    public CommentResp(int code, String message, ArrayList<CommentInfoResp> data) {
        this.code = code;
        Message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public ArrayList<CommentInfoResp> getData() {
        return data;
    }

    public void setData(ArrayList<CommentInfoResp> data) {
        this.data = data;
    }
}
