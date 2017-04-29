package spencer.cn.finalproject.dojo;

import java.util.List;

/**
 * Created by Administrator on 2017/4/19.
 */

public class ChangeNewsType {
    List<Long> userNewTypeUids;

    public ChangeNewsType() {
    }

    public ChangeNewsType(List<Long> userNewTypeUids) {
        this.userNewTypeUids = userNewTypeUids;
    }


    public List<Long> getUserNewTypeUids() {
        return userNewTypeUids;
    }

    public void setUserNewTypeUids(List<Long> userNewTypeUids) {
        this.userNewTypeUids = userNewTypeUids;
    }
}
