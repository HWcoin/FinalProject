package spencer.cn.finalproject.manager;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import spencer.cn.finalproject.acview.LoginActivity;
import spencer.cn.finalproject.application.BaseApplication;
import spencer.cn.finalproject.dojo.UserInfo;

/**
 * Created by Administrator on 2017/5/8.
 */

public class CommonUtil {
    public static boolean isLogin(Context mContext){
        UserInfo userInfo = BaseApplication.getInfo();
        String accessToken = LocalDataManager.getAccessToken(mContext);
        if (userInfo!=null && TextUtils.isEmpty(accessToken)==false){
            return true;
        }
        return false;
    }
}
