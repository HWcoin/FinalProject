package spencer.cn.finalproject.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import spencer.cn.finalproject.R;

/**
 * Created by Administrator on 2017/4/24.
 */

public class CreateNews extends BaseFragment {


    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        return inflater.inflate(R.layout.page_create_news, container, false);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
