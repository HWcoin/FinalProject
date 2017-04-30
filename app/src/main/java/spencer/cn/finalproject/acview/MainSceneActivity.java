package spencer.cn.finalproject.acview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import spencer.cn.finalproject.R;
import spencer.cn.finalproject.adapter.ModuleFragmentAdatper;
import spencer.cn.finalproject.application.BaseApplication;
import spencer.cn.finalproject.dojo.BaseNewType;
import spencer.cn.finalproject.dojo.UploadImgResp;
import spencer.cn.finalproject.fragment.FirstPageFragment;
import spencer.cn.finalproject.fragment.GroupPageFragment;
import spencer.cn.finalproject.fragment.MePageFragment;
import spencer.cn.finalproject.iexport.NewsCallBack;
import spencer.cn.finalproject.manager.ImageUploadManager;
import spencer.cn.finalproject.manager.LocalDataManager;
import spencer.cn.finalproject.util.BitmapUtil;
import spencer.cn.finalproject.util.PublicVar;

public class MainSceneActivity extends BaseActionBarActivity {//implements IActionBar{
    private TabLayout tabMain;
    private ViewPager pages;
//    private Toolbar toolbar;
    private ArrayList<Fragment> fragmentList;
    ModuleFragmentAdatper fragmentAdapter;
    private boolean flag = true;

    Gson parser = new GsonBuilder().serializeNulls().create();
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0xf81){
                String gsonStrings = (String) msg.obj;
                Log.e("e", gsonStrings);
                UploadImgResp registResp = parser.fromJson(gsonStrings, UploadImgResp.class);
                Toast.makeText(MainSceneActivity.this, registResp.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_scene);
        initViews();
        LocalDataManager.loadCaches(this);
        BaseNewType baseNewType = LocalDataManager.loadBaseConfig(this);

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (fragmentAdapter!=null && BaseApplication.getLoginBean()!=null){
//            MePageFragment me = (MePageFragment) fragmentList.get(2);
//            me.refreshDatas();
//        }
//    }

    private void initViews() {
        this.fragmentList = new ArrayList<>();
        initViewPage();
        initToolBar();
    }

    private void initViewPage() {
        this.tabMain = (TabLayout) findViewById(R.id.layout_main);
        this.pages = (ViewPager) findViewById(R.id.vp_allpages);
        this.pages.setOffscreenPageLimit(4);

        this.tabMain.setTabMode(TabLayout.MODE_FIXED);
        for (int i=0; i < titles.length; i++){
            TabLayout.Tab  tab = tabMain.newTab();
            tab.setText(titles[i]);

            this.tabMain.addTab(tab);
        }


        for (int i = 0; i < titles.length; i++) {
            if (i == 0){
                FirstPageFragment first = new FirstPageFragment();
                fragmentList.add(first);
            }else if (i == 1){
                GroupPageFragment group = new GroupPageFragment();
                fragmentList.add(group);
            }else if (i == 2){
                MePageFragment me = new MePageFragment();
                fragmentList.add(me);
            }
        }

        fragmentAdapter = new ModuleFragmentAdatper(getSupportFragmentManager(), fragmentList, titles);
        this.pages.setAdapter(fragmentAdapter);//给ViewPager设置适配器
        this.tabMain.setupWithViewPager(pages);//将TabLayout和ViewPager关联起来。
        this.tabMain.setTabsFromPagerAdapter(fragmentAdapter);//给Tabs设置适配器
        this.tabMain.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition()==0){
//                    showActionBar();
                }else{
//                    hideActionBar();
                }
                pages.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void initToolBar() {
//        this.toolbar = (Toolbar) findViewById(R.id.tb_toolbar);
//        setSupportActionBar(this.toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        toolbar.setTitle("小众");
//        toolbaLogo(R.mipmap.ic_launcher);
//        toolbar.setNavigationIcon(android.R.drawable.ic_input_delete);
//        Toolbar可以设置 Title（主标题），Subtitle（副标题），Logo（logo图标）NavigationIcon（导航按钮）。
    }

    @Override
    public void finish() {
        BaseApplication app = (BaseApplication) getApplication();
        for (BaseActivity ba : app.getAllActivity()){
            ba.finish();
        }
        if (flag){
            Toast.makeText(this, "再次点击退出", Toast.LENGTH_SHORT).show();
            flag = false;
            new Thread(){
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    flag = true;
                }
            }.start();
            return;
        }
        LocalDataManager.storeCaches(this, LocalDataManager.caches);

        System.exit(0);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

//    @Override
//    public void showActionBar() {
//        this.toolbar.setVisibility(View.VISIBLE);
//    }

//    @Override
//    public void hideActionBar() {
//        this.toolbar.setVisibility(View.GONE);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            Toast.makeText(this, "Hello world", Toast.LENGTH_SHORT).show();
        }
        switch(item.getItemId()){
            case R.id.action_settings:
                Intent personal = new Intent(MainSceneActivity.this, PersonalNewsActivity.class);
                startActivity(personal);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( resultCode == RESULT_OK ){
            if (PublicVar.CHANGE_ICON_REQUEST_CODE == requestCode){
                MePageFragment me = (MePageFragment) fragmentList.get(PublicVar.ME_PATE_INDEX);
                int iconId = data.getIntExtra(PublicVar.CHANGE_ICON_NAME, R.mipmap.ic_launcher);
                if (iconId != R.mipmap.ic_launcher){
                    me.setPlayerIcon(iconId);
                }
            }else if (PublicVar.CHANGE_GALLEY_ICON_REQUEST_CODE == requestCode){
                MePageFragment me = (MePageFragment) fragmentList.get(PublicVar.ME_PATE_INDEX);
                Uri imageFileUri = data.getData();
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageFileUri));
                    me.setPlayerIcon(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


                ///upload img
                String accessToken = LocalDataManager.getAccessToken(this);
                Bitmap compressBitmap = BitmapUtil.createZoomInBitmap(bitmap, 100);
                String url = getResources().getString(R.string.url_post_upload_img) + accessToken;
                ImageUploadManager.uploadImgWithBitmap(url, this, compressBitmap, new NewsCallBack() {
                    @Override
                    public void onNewsReturn(String gstring) {
                        Message msg = new Message();
                        msg.what = 0xf81;
                        msg.obj = gstring;
                        handler.sendMessage(msg);
                    }
                });
            }else if (PublicVar.GET_PIC_FOR_XIAOZHONG == requestCode){
                GroupPageFragment me = (GroupPageFragment) fragmentList.get(PublicVar.GROUP_PATE_INDEX);
                Uri imageFileUri = data.getData();
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageFileUri));
                    me.setArticalBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }


    }

}
