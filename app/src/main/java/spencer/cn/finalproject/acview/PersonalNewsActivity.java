package spencer.cn.finalproject.acview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import spencer.cn.finalproject.R;
import spencer.cn.finalproject.adapter.TitlesAdapter;
import spencer.cn.finalproject.application.BaseApplication;
import spencer.cn.finalproject.dojo.ChangeNewsType;
import spencer.cn.finalproject.dojo.GsonNews;
import spencer.cn.finalproject.dojo.LoginBean;
import spencer.cn.finalproject.dojo.NewType;
import spencer.cn.finalproject.dojo.UserConfig;
import spencer.cn.finalproject.iexport.ISelectMyTitle;
import spencer.cn.finalproject.iexport.ISelectTotalTitle;
import spencer.cn.finalproject.iexport.NewsCallBack;
import spencer.cn.finalproject.manager.LocalDataManager;
import spencer.cn.finalproject.manager.NetWorkManager;
import spencer.cn.finalproject.util.PublicVar;

public class PersonalNewsActivity extends BaseActivity {
    private GridView my;
    private GridView total;
    private ArrayList<NewType> myTextData;
    private ArrayList<NewType> totalTextData;
    private TitlesAdapter myAdapter;
    private TitlesAdapter totalAdapter;
    private Button complete;
    private ArrayList<Long> myType;
    Gson parser = new GsonBuilder().serializeNulls().create();

//    String   imeistring = null;
//    TelephonyManager telephonyManager;
    Handler changeHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0xfd3){
                String loginStrings = (String) msg.obj;
                GsonNews personal = parser.fromJson(loginStrings, GsonNews.class);
                if (personal.getCode() == 200) {
                    BaseApplication application = (BaseApplication) getApplication();
                    if (myType != null){
                        UserConfig config = BaseApplication.getConfig();
                        if (config != null){
                            config.setUserNewType(myType);
                        }
//                        application.getLoginBean().getData().getUserConfig().setUserNewType(myType);
                    }
                    finish();
                } else {
                    Toast.makeText(PersonalNewsActivity.this, "修改超时", Toast.LENGTH_LONG).show();
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_news);

        initViews();

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    private void initViews(){
        my = (GridView) findViewById(R.id.gv_my_titles);
        total = (GridView) findViewById(R.id.gv_total_titles);
        complete = (Button) findViewById(R.id.btn_complete_update);
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myType = new ArrayList<>();
                for (int i=0; i < myTextData.size(); i++){
                    myType.add(myTextData.get(i).getUid());
                }
                if (myType.size() == 0){
                    ArrayList<NewType> base = LocalDataManager.loadBaseConfig(PersonalNewsActivity.this).getData();
                    for (int i=0; i < base.size(); i++){
                        myType.add(base.get(i).getUid());
                    }
                }
                String accessToken = LocalDataManager.getAccessToken(PersonalNewsActivity.this);
                Type type = new TypeToken<ChangeNewsType>(){}.getType();
                String url = getResources().getString(R.string.url_post_change_news_tab);
                HashMap<String, String> params = new HashMap<String, String>();
                ChangeNewsType gParams = new ChangeNewsType(myType);
                String parameters = parser.toJson(gParams, type);
                NetWorkManager.doPost(url+accessToken, parameters, new NewsCallBack() {
                    @Override
                    public void onNewsReturn(String gstring) {
                        Message msg = new Message();
                        msg.what = 0xfd3;
                        msg.obj = gstring;
                        changeHandler.sendMessage(msg);
                    }
                });

            }
        });

    }
    public boolean isContainsType(long target, List<Long> lists){
        for (int i=0; i < lists.size(); i++){
            if (target == lists.get(i)){
                return true;
            }
        }
        return false;
    }
    private  void loadData(){
        LoginBean loginBean = BaseApplication.getLoginBean();
        myTextData = new ArrayList<>();
        totalTextData = new ArrayList<>();
        if (loginBean!=null){
            UserConfig config = loginBean.getData().getUserConfig();
            if (config != null){
                List<Long> users = config.getUserNewType();
                for (int i=0; i < config.getNewTypes().size();i++){
                    NewType item = config.getNewTypes().get(i);
                    if (isContainsType(item.getUid(), users)){
                        myTextData.add(item);
                    }else {
                        totalTextData.add(item);
                    }
                }
            }else {
                finish();
                Toast.makeText(this, "sign in first please", Toast.LENGTH_LONG).show();
            }
        }else {
            finish();
            Toast.makeText(this, "sign in first please", Toast.LENGTH_LONG).show();
        }

        myAdapter = new TitlesAdapter(this, myTextData, PublicVar.TABS_MY_TITLES);
        myAdapter.setMyTitleCallBack(new ISelectMyTitle() {
            @Override
            public void onMyTitleSelected(NewType infos) {
                moveDatas(myTextData, totalTextData, infos);
                myAdapter.notifyDataSetChanged();
                totalAdapter.notifyDataSetChanged();
            }
        });
        totalAdapter = new TitlesAdapter(this, totalTextData, PublicVar.TABS_TOTAL_TITLES);
        totalAdapter.setTotalCallBack(new ISelectTotalTitle() {
            @Override
            public void onTotleitleSelected(NewType infos) {
                moveDatas(totalTextData, myTextData, infos);
                myAdapter.notifyDataSetChanged();
                totalAdapter.notifyDataSetChanged();
            }
        });
        my.setAdapter(myAdapter);
        total.setAdapter(totalAdapter);
    }
    private void moveDatas(ArrayList<NewType> source, ArrayList<NewType> dist, NewType target){
        ////////search
        NewType  result = null;
        for (int i=0; i < source.size(); i++){
            if (source.get(i).getTypeName().equals(target.getTypeName())){
                result = source.get(i);
            }
        }
        ////////remove
        if (result != null){
            source.remove(result);
        }
        /////////update
        if (dist == null || dist.size() == 0){
            target.setShow(false);
        }else{
            target.setShow(dist.get(0).isShow());
        }
        /////////add
        dist.add(target);
    }
}
