package spencer.cn.finalproject.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import spencer.cn.finalproject.fragment.NewsTabFragment;

/**
 * Created by Administrator on 2017/3/5.
 */

public class FunctionsFragmentAdapter extends FragmentStatePagerAdapter {
    private ArrayList<NewsTabFragment> fragments;
    private String[] fragTitles;
    public FunctionsFragmentAdapter(FragmentManager fm, ArrayList<NewsTabFragment> fragments, String[] fragTitles) {
        super(fm);
        this.fragments = fragments;
        this.fragTitles = fragTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragTitles[position];
    }
}
