package spencer.cn.finalproject.application;

import android.app.Application;

import java.util.ArrayList;

import spencer.cn.finalproject.acview.BaseActivity;
import spencer.cn.finalproject.dojo.BaseNewType;
import spencer.cn.finalproject.dojo.LoginBean;

/**
 * Created by Administrator on 2017/3/4.
 */

public class BaseApplication extends Application {
    private ArrayList<BaseActivity> allActivity = new ArrayList<>();
    private BaseNewType baseNewType;
    private static LoginBean loginBean;

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
}
