package spencer.cn.finalproject.dojo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/19.
 */

public class ChangeNewsType {
    String accessToken;
    ArrayList<Long> userNewTypeUids;

    public ChangeNewsType() {
    }

    public ChangeNewsType(String accessToken, ArrayList<Long> userNewTypeUids) {
        this.accessToken = accessToken;
        this.userNewTypeUids = userNewTypeUids;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public ArrayList<Long> getUserNewTypeUids() {
        return userNewTypeUids;
    }

    public void setUserNewTypeUids(ArrayList<Long> userNewTypeUids) {
        this.userNewTypeUids = userNewTypeUids;
    }
}
