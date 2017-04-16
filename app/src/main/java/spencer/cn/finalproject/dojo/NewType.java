package spencer.cn.finalproject.dojo;

import java.io.Serializable;

public class NewType implements Serializable{
    static final long serialVersionUID = 1234567789;
    private Long uid;

    private String type;

    private String typeName;

    private boolean isShow;
    public NewType() {
    }

    public NewType(Long uid, String type, String typeName, boolean isShow) {
        this.uid = uid;
        this.type = type;
        this.typeName = typeName;
        this.isShow = isShow;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}