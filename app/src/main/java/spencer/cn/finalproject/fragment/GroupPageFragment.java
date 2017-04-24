package spencer.cn.finalproject.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import spencer.cn.finalproject.R;
import spencer.cn.finalproject.adapter.GroupAdapter;

/**
 * Created by Administrator on 2017/3/5.
 */

public class GroupPageFragment extends Fragment {
    private AppCompatActivity activity;
    private TabLayout groupTabs;
    private ViewPager groupPages;
    private String [] pagesTitles = {
            "新建", "我的","社区"
    };
    private ArrayList<BaseFragment> fragments;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.activity = (AppCompatActivity) getActivity();
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
        View view = inflater.inflate(R.layout.page_group_layout, container,false);
        //初始化布局
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        groupTabs = (TabLayout) view.findViewById(R.id.tab_group);


        initViewPage(view);
    }

    private void initViewPage(View view) {
        groupPages = (ViewPager) view.findViewById(R.id.vp_sub_group);
        groupPages.setOffscreenPageLimit(5);


        groupTabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        for (int i=0; i < pagesTitles.length; i++){
            TabLayout.Tab  tab = groupTabs.newTab();
            tab.setText(pagesTitles[i]);
            groupTabs.addTab(tab);
        }

        fragments = new ArrayList<>();
        for (int i = 0; i < pagesTitles.length; i++) {
            BaseFragment fragment = null;
            if (i == 0){
                fragment = new CreateNews();
            }else if (i == 1){
                fragment = new MyNews();
            }else  if (i == 2){
                fragment = new OthersNews();
            }
            fragments.add(fragment);
        }

        GroupAdapter fragmentAdapter = new GroupAdapter(getActivity().getSupportFragmentManager(), fragments, pagesTitles);
        groupPages.setAdapter(fragmentAdapter);//给ViewPager设置适配器
        groupTabs.setupWithViewPager(groupPages);//将TabLayout和ViewPager关联起来。
        this.groupTabs.setTabsFromPagerAdapter(fragmentAdapter);//给Tabs设置适配器
        this.groupTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                groupPages.setCurrentItem(tab.getPosition());
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
