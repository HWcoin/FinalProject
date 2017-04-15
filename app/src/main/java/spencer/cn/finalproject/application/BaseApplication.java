package spencer.cn.finalproject.application;

import android.app.Application;

import java.util.ArrayList;

import spencer.cn.finalproject.acview.BaseActivity;
import spencer.cn.finalproject.dojo.BaseNewType;

/**
 * Created by Administrator on 2017/3/4.
 */

public class BaseApplication extends Application {
    private ArrayList<BaseActivity> allActivity = new ArrayList<>();
    private BaseNewType baseNewType;

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
}
