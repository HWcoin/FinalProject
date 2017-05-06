package spencer.cn.finalproject.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/19.
 */

/**
 * Created by Administrator on 2017/3/5.
 */

public class ModuleFragmentAdatper extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> fragments;
    private String[] fragTitles;
    public ModuleFragmentAdatper(FragmentManager fm, ArrayList<Fragment> fragments, String[] fragTitles) {
        super(fm);
        this.fragments = fragments;
        this.fragTitles = fragTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }
//
//    @Override
//    public int getItemPosition(Object object) {
//        return POSITION_NONE;
//    }
    //    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        ViewPager viewPager = (ViewPager) container;
//        NewsTabFragment obj = (NewsTabFragment) object;
//        viewPager.removeView(obj.getView());
//    }
    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragTitles[position];
    }
}
