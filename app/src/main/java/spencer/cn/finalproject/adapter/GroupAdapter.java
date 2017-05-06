package spencer.cn.finalproject.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import spencer.cn.finalproject.fragment.BaseFragment;

/**
 * Created by Administrator on 2017/4/24.
 */

public class GroupAdapter extends FragmentStatePagerAdapter {
    private ArrayList<BaseFragment> fragments;
    private String[] fragTitles;
    public GroupAdapter(FragmentManager fm, ArrayList<BaseFragment> fragments, String[] fragTitles) {
        super(fm);
        this.fragments = fragments;
        this.fragTitles = fragTitles;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
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
