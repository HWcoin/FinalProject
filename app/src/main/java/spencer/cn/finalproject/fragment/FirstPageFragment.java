package spencer.cn.finalproject.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import spencer.cn.finalproject.R;
import spencer.cn.finalproject.adapter.FunctionsFragmentAdapter;
import spencer.cn.finalproject.application.BaseApplication;
import spencer.cn.finalproject.dojo.BaseNewType;

/**
 * Created by Administrator on 2017/3/5.
 */

public class FirstPageFragment extends Fragment {
    private AppCompatActivity activity;
    private TabLayout subTab;
    private ViewPager subPages;
    public static String[] news_types = {
            "贱","贱","贱","贱","贱","贱","贱","贱","贱"
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (AppCompatActivity) getActivity();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_first_layout, container,false);

        //初始化布局
        initViews(view);

        return view;

    }

    private void initViews(View v) {
        //初始化ViewPager
        initViewPage(v);
    }
    private void initTabsDatas(){
        BaseApplication application = (BaseApplication) getActivity().getApplication();
        BaseNewType baseNewType = application.getBaseNewType();
        Log.w("xxxxxxxxxxxxxxxx", "null:"+(baseNewType==null));
        news_types = new String[baseNewType.getData().size()];
        for (int i=0; i < baseNewType.getData().size(); i++){
            news_types[i] = baseNewType.getData().get(i).getTypeName();
        }
        for (int i=0; i < news_types.length; i++){
            Log.w("xxxxx", news_types[i]);
        }
    }

    private void initViewPage(View v) {
        this.subTab = (TabLayout) v.findViewById(R.id.layout_sub_tab);
        this.subPages = (ViewPager) v.findViewById(R.id.vp_sub_pages);

        initTabsDatas();


        this.subTab.setTabMode(TabLayout.MODE_SCROLLABLE);
        for (int i=0; i < news_types.length; i++){
            TabLayout.Tab  tab = this.subTab.newTab();
            tab.setText(news_types[i]);
            this.subTab.addTab(tab);
        }

        ArrayList<Fragment> fragmentList = new ArrayList<>();
        for (int i = 0; i < news_types.length; i++) {
            NewsTabFragment fragment = new NewsTabFragment();
            fragmentList.add(fragment);
        }

        FunctionsFragmentAdapter fragmentAdapter = new FunctionsFragmentAdapter(getActivity().getSupportFragmentManager(), fragmentList, news_types);
        this.subPages.setAdapter(fragmentAdapter);//给ViewPager设置适配器
        this.subTab.setupWithViewPager(this.subPages);//将TabLayout和ViewPager关联起来。
        this.subTab.setTabsFromPagerAdapter(fragmentAdapter);//给Tabs设置适配器
        this.subTab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                subPages.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
    @Override
    public void onPause() {
        super.onPause();
    }
}
