package spencer.cn.finalproject.dojo;

/**
 * Created by Administrator on 2017/4/9.
 */

public class TitleInfos {
    private String name;
    private boolean isShow;

    public TitleInfos(String name) {
        this.name = name;
        this.isShow = false;
    }

    public TitleInfos(String name, boolean isShow) {
        this.name = name;
        this.isShow = isShow;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}
