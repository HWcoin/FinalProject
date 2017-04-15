package spencer.cn.finalproject.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import spencer.cn.finalproject.R;
import spencer.cn.finalproject.iexport.IActionBar;

/**
 * Created by Administrator on 2017/3/5.
 */

public class GroupPageFragment extends Fragment {
    private AppCompatActivity activity;
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
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
