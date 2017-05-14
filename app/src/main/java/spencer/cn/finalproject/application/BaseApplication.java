package spencer.cn.finalproject.application;

import android.app.Application;

import java.util.ArrayList;

import spencer.cn.finalproject.acview.BaseActivity;
import spencer.cn.finalproject.dojo.BaseNewType;
import spencer.cn.finalproject.dojo.LoginBean;
import spencer.cn.finalproject.dojo.UserConfig;
import spencer.cn.finalproject.dojo.UserInfo;

/**
 * Created by Administrator on 2017/3/4.
 */

public class BaseApplication extends Application {
    private ArrayList<BaseActivity> allActivity = new ArrayList<>();
    private BaseNewType baseNewType;
    private static LoginBean loginBean;
    private static UserConfig config;
    private static UserInfo info;

    public BaseNewType getBaseNewType() {
        return baseNewType;
    }

    public void setBaseNewType(BaseNewType baseNewType) {
        this.baseNewType = baseNewType;
    }

    //Activity入栈
    public void AddActivity(BaseActivity act){
        if (act != null && act.getClass().equals(BaseActivity.class)){
            allActivity.add(act);
        }
    }
    //activity出栈
    public ArrayList<BaseActivity> getAllActivity(){
        return allActivity;
    }

    public void setAllActivity(ArrayList<BaseActivity> allActivity) {
        this.allActivity = allActivity;
    }

    public static LoginBean getLoginBean() {
        return BaseApplication.loginBean;
    }

    public static void setLoginBean(LoginBean loginBean) {
        BaseApplication.loginBean = loginBean;
    }
    private static boolean isNightMode = false;
    public static boolean isNightMode() {
        return BaseApplication.isNightMode;
    }
    public static void setIsNightMode(boolean b) {
        BaseApplication.isNightMode = b;
    }

    public static UserConfig getConfig() {
        return config;
    }

    public static void setConfig(UserConfig config) {
        BaseApplication.config = config;
    }

    public static UserInfo getInfo() {
        return info;
    }

    public static void setInfo(UserInfo info) {
        BaseApplication.info = info;
    }
}
