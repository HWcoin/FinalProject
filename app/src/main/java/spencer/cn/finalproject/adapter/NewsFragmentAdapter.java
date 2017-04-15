package spencer.cn.finalproject.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/6.
 */

public class NewsFragmentAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> fragments;
    private String[] fragTitles;

    public NewsFragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragments, String[] fragTitles) {
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
