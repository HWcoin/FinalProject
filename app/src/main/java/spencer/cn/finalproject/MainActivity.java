package spencer.cn.finalproject;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.squareup.picasso.Picasso;

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
    private Button[] images;
    private ImageSwitcher switcher;
    private ImageView  welcomePages;
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
                            Log.e("help", "欢迎页走完了");
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

                LoginBean loginBean = parser.fromJson(loginStrings, LoginBean.class);
                BaseApplication application = (BaseApplication) getApplication();
                application.setLoginBean(loginBean);
                if (loginBean != null && loginBean.getCode() == 200){
                    BaseApplication.setConfig(loginBean.getData().getUserConfig());
                    BaseApplication.setInfo(loginBean.getData().getUser());
                    SharedPreferences settings = getSharedPreferences(PublicVar.SHARED_FILE, MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(PublicVar.ACCESSTOKEN, loginBean.getData().getAccessToken());
                    editor.commit();
                }

                if (isAutoLogin){
                    closeGuildPages();
                    Log.e("help", "自动登录走完了");
                }
                isAutoLogin = true;
            }
        }
    };
    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            }
        }

        ads.add(R.mipmap.guilda);
        ads.add(R.mipmap.guildb);
        ads.add(R.mipmap.guildc);
        ads.add(R.mipmap.guilda);

        this.initViews();



        if (getFirstStatus()){
            this.initFirstOpenViews();
            this.switcher.setVisibility(View.VISIBLE);
            this.welcomePages.setVisibility(View.INVISIBLE);
        }else {
            this.initWelcomeOpenViews();
            this.switcher.setVisibility(View.INVISIBLE);
            this.welcomePages.setVisibility(View.VISIBLE);
        }

    }



//处理权限请求响应：当你的app请求权限，系统显示一个dialog给用户，当用户响应时，
// 系统调用onRequestPermissionResult()并传递请求码，
// 你的app必须重写这个方法并检查该权限是否已经授予。
    @Override
    public void onRequestPermissionsResult(int requestCode,
            String permissions[], int[] grantResults) {
            if (requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS){
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
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
        welcomePages = (ImageView) findViewById(R.id.ims_welcome_pages);
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
        for (int i=0; i < IMAGES_NUMS; i++){
            this.images[i].setVisibility(visibility);
        }
    }
    //////////////////////////////////////////////////////////////////////////欢迎页
    private void initWelcomeOpenViews(){

        this.setViewVisibility(View.GONE);
//        this.switcher.setImageResource(ads.get(3));
        String url = getResources().getString(R.string.url_download_img)+"start-page.jpg";
        Picasso.with(this).load(url).into(welcomePages);

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
        timer.schedule(task, 3000);
        ///////////////////////////////////////////自动登录
        autoLogin();
    }
    ////////////////////////////////////////

    /**
     * 自动登陆
     */
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
