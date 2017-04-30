package spencer.cn.finalproject.dojo.resp;

import java.util.ArrayList;

import spencer.cn.finalproject.dojo.CommentInfoResp;

/**
 * Created by Administrator on 2017/4/30.
 */

public class GetCommentsResp {
    private int code;
    private String message;
    private ArrayList<CommentInfoResp> data;

    public GetCommentsResp() {
    }

    public GetCommentsResp(int code, String message, ArrayList<CommentInfoResp> data) {
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

    public ArrayList<CommentInfoResp> getData() {
        return data;
    }

    public void setData(ArrayList<CommentInfoResp> data) {
        this.data = data;
    }
}
