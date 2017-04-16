package spencer.cn.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import spencer.cn.finalproject.acview.BaseActivity;
import spencer.cn.finalproject.acview.MainSceneActivity;
import spencer.cn.finalproject.application.BaseApplication;
import spencer.cn.finalproject.dojo.BaseNewType;
import spencer.cn.finalproject.dojo.LoginBean;
import spencer.cn.finalproject.iexport.NewsCallBack;
import spencer.cn.finalproject.manager.LocalDataManager;
import spencer.cn.finalproject.manager.NetWorkManager;
import spencer.cn.finalproject.util.PublicVar;

public class MainActivity extends BaseActivity {
    private Button skip;
    private Button[] images;
    private ImageSwitcher switcher;
    private int curIndex = 0;
    private ArrayList<Integer> ads = new ArrayList<>();

    public static int IMAGES_NUMS = 3;//最多3个按钮
    public static int [] ids = {
            R.id.btn_image_1, R.id.btn_image_2, R.id.btn_image_3
    };
    Gson parser = new GsonBuilder().serializeNulls().create();
    private boolean isAutoLogin;
    private Timer timer = new Timer();
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0xfff){
                switch (curIndex) {
                    case 0:
                        images[1].performClick();
                        break;
                    case 1:
                        images[2].performClick();
                        break;
                    case 2:
                        closeGuildPages();
                        break;
                    case 3:
                        if (isAutoLogin){
                            closeGuildPages();
                        }
                        isAutoLogin = true;
                        break;
                    default:
                        closeGuildPages();
                }
            }else if (msg.what == 0xfe1){
                String cofing = (String) msg.obj;
                BaseNewType requestNews = parser.fromJson(cofing, BaseNewType.class);
                LocalDataManager.storeBaseConfig(MainActivity.this, requestNews);
            }else if (msg.what == 0xfe2){
                String loginStrings = (String) msg.obj;
                Log.e("e", loginStrings);
                LoginBean loginBean = parser.fromJson(loginStrings, LoginBean.class);
                BaseApplication application = (BaseApplication) getApplication();
                application.setLoginBean(loginBean);
                if (isAutoLogin){
                    closeGuildPages();
                }
                isAutoLogin = true;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ads.add(R.mipmap.ic_action_emo_angry);
        ads.add(R.mipmap.ic_action_emo_cry);
        ads.add(R.mipmap.ic_action_emo_cool);
        ads.add(R.mipmap.ic_action_emo_evil);

        this.initViews();

        if (getFirstStatus()){
            this.initFirstOpenViews();
        }else {
            this.initWelcomeOpenViews();
        }

    }
    private boolean getFirstStatus(){
        SharedPreferences settings = getSharedPreferences(PublicVar.SHARED_FILE, MODE_PRIVATE);
        boolean isfirst = settings.getBoolean(PublicVar.IS_FIRST_OPEN, true);
        if (isfirst){
            checkBaseConfig();
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(PublicVar.IS_FIRST_OPEN, false);
            editor.commit();
        }
        return isfirst;
    }
    private void initViews(){
        this.switcher = (ImageSwitcher) this.findViewById(R.id.ims_switch_image);
        this.skip = (Button) this.findViewById(R.id.btn_skip);
        this.images = new Button[IMAGES_NUMS];
        for (int i=0; i < IMAGES_NUMS; i++){
            this.images[i] = (Button) this.findViewById(ids[i]);
        }
        this.switcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView img = new ImageView(MainActivity.this);
                img.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                img.setScaleType(ImageView.ScaleType.FIT_XY);
                return img;
            }
        });
        curIndex = 0;
        this.switcher.setImageResource(ads.get(curIndex));
    }
    private void setViewVisibility(int visibility){
        this.skip.setVisibility(visibility);
        for (int i=0; i < IMAGES_NUMS; i++){
            this.images[i].setVisibility(visibility);
        }
    }
    //////////////////////////////////////////////////////////////////////////欢迎页
    private void initWelcomeOpenViews(){

        this.setViewVisibility(View.GONE);
        this.switcher.setImageResource(ads.get(3));

        //计时
        this.isAutoLogin = false;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 0xfff;
                handler.sendMessage(message);
            }
        };
        curIndex = 2;
        timer.schedule(task, 1500);
        ///////////////////////////////////////////自动登录
        autoLogin();
    }
    ////////////////////////////////////////
    public void autoLogin(){
        SharedPreferences settings = getSharedPreferences(PublicVar.SHARED_FILE, MODE_PRIVATE);
        String accestoken = settings.getString(PublicVar.ACCESSTOKEN, "");
        if (!accestoken.equals("")){
            String url = getResources().getString(R.string.url_post_auto_login);
            url = url + "?accessToken=" + accestoken;
            HashMap<String, String> params = new HashMap<>();
            NetWorkManager.doPost(url, params, new NewsCallBack() {
                @Override
                public void onNewsReturn(String gstring) {
                    Message msg = new Message();
                    msg.what = 0xfe2;
                    msg.obj = gstring;
                    handler.sendMessage(msg);
                }
            });
        }
    }
    //////////////首先加载游客配置，再自动登录，登录成功修改数据
    public void checkBaseConfig(){
        ////////////////////////////////////////////初始化基础设置
        boolean isExists = LocalDataManager.isBaseConfigExists(this);
        if (!isExists){
            String url = getResources().getString(R.string.url_get_base_news_type);
            NetWorkManager.doGet(url, new NewsCallBack() {
                @Override
                public void onNewsReturn(String gstring) {
                    Message msg = new Message();
                    msg.what = 0xfe1;
                    msg.obj = gstring;
                    handler.sendMessage(msg);
                }
            });
        }
    }
    ////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////引导页

    //初始化控件
    private void initFirstOpenViews(){
        this.setViewVisibility(View.VISIBLE);


        initBtnsListeners();

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeGuildPages();
            }
        });

        //计时
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 0xfff;
                handler.sendMessage(message);
            }
        };
        timer.schedule(task, 1500, 3000);
    }

    private void initBtnsListeners() {
        this.images[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curIndex = 0;
                switcher.setImageResource(ads.get(0));
            }
        });
        this.images[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curIndex = 1;
                switcher.setImageResource(ads.get(1));
            }
        });
        this.images[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curIndex = 2;
                switcher.setImageResource(ads.get(2));
            }
        });
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void closeGuildPages(){
        Intent intent = new Intent(MainActivity.this, MainSceneActivity.class);
        startActivity(intent);
        timer.cancel();
        finish();
    }


}
