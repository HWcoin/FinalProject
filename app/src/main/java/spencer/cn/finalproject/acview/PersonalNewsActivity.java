package spencer.cn.finalproject.acview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import spencer.cn.finalproject.R;
import spencer.cn.finalproject.adapter.TitlesAdapter;
import spencer.cn.finalproject.application.BaseApplication;
import spencer.cn.finalproject.dojo.LoginBean;
import spencer.cn.finalproject.dojo.NewType;
import spencer.cn.finalproject.dojo.UserConfig;
import spencer.cn.finalproject.iexport.ISelectMyTitle;
import spencer.cn.finalproject.iexport.ISelectTotalTitle;
import spencer.cn.finalproject.util.PublicVar;

public class PersonalNewsActivity extends AppCompatActivity {
    private GridView my;
    private GridView total;
    private ArrayList<NewType> myTextData;
    private ArrayList<NewType> totalTextData;
    private TitlesAdapter myAdapter;
    private TitlesAdapter totalAdapter;

//    String   imeistring = null;
//    TelephonyManager telephonyManager;
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
//        myTextData = new ArrayList<>();
//        myTextData.add(new TitleInfos("111"));
//        myTextData.add(new TitleInfos("222"));
//        myTextData.add(new TitleInfos("333"));
//        myTextData.add(new TitleInfos("444"));
//        totalTextData = new ArrayList<>();
//        totalTextData.add(new TitleInfos("aaa"));
//        totalTextData.add(new TitleInfos("bbb"));
//        totalTextData.add(new TitleInfos("ccc"));
//        totalTextData.add(new TitleInfos("ddd"));
//        totalTextData.add(new TitleInfos("eee"));
//        totalTextData.add(new TitleInfos("fff"));

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
