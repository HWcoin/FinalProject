package spencer.cn.finalproject.acview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import java.util.ArrayList;

import spencer.cn.finalproject.R;
import spencer.cn.finalproject.adapter.TitlesAdapter;
import spencer.cn.finalproject.dojo.TitleInfos;
import spencer.cn.finalproject.iexport.ISelectMyTitle;
import spencer.cn.finalproject.iexport.ISelectTotalTitle;
import spencer.cn.finalproject.util.PublicVar;

public class PersonalNewsActivity extends AppCompatActivity {
    private GridView my;
    private GridView total;
    private ArrayList<TitleInfos> myTextData;
    private ArrayList<TitleInfos> totalTextData;
    private TitlesAdapter myAdapter;
    private TitlesAdapter totalAdapter;

//    String   imeistring = null;
//    TelephonyManager telephonyManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_news);

        loadData();
        initViews();

    }
    private void initViews(){
        my = (GridView) findViewById(R.id.gv_my_titles);
        total = (GridView) findViewById(R.id.gv_total_titles);
        myAdapter = new TitlesAdapter(this, myTextData, PublicVar.TABS_MY_TITLES);
        myAdapter.setMyTitleCallBack(new ISelectMyTitle() {
            @Override
            public void onMyTitleSelected(TitleInfos infos) {
                moveDatas(myTextData, totalTextData, infos);
                myAdapter.notifyDataSetChanged();
                totalAdapter.notifyDataSetChanged();
            }
        });
        totalAdapter = new TitlesAdapter(this, totalTextData, PublicVar.TABS_TOTAL_TITLES);
        totalAdapter.setTotalCallBack(new ISelectTotalTitle() {
            @Override
            public void onTotleitleSelected(TitleInfos infos) {
                moveDatas(totalTextData, myTextData, infos);
                myAdapter.notifyDataSetChanged();
                totalAdapter.notifyDataSetChanged();
            }
        });
        my.setAdapter(myAdapter);
        total.setAdapter(totalAdapter);
    }
    private  void loadData(){
        myTextData = new ArrayList<>();
        myTextData.add(new TitleInfos("111"));
        myTextData.add(new TitleInfos("222"));
        myTextData.add(new TitleInfos("333"));
        myTextData.add(new TitleInfos("444"));
        totalTextData = new ArrayList<>();
        totalTextData.add(new TitleInfos("aaa"));
        totalTextData.add(new TitleInfos("bbb"));
        totalTextData.add(new TitleInfos("ccc"));
        totalTextData.add(new TitleInfos("ddd"));
        totalTextData.add(new TitleInfos("eee"));
        totalTextData.add(new TitleInfos("fff"));
    }
    private void moveDatas(ArrayList<TitleInfos> source, ArrayList<TitleInfos> dist, TitleInfos target){
        ////////search
        TitleInfos  result = null;
        for (int i=0; i < source.size(); i++){
            if (source.get(i).getName().equals(target.getName())){
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
