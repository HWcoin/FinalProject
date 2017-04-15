package spencer.cn.finalproject.acview;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Administrator on 2017/3/4.
 */

public class BaseActivity extends AppCompatActivity{
    public String[] titles = {
            "首页", "小众", "我"
    };

    @Override
    public void onCreate( Bundle savedInstanceState,  PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

}
