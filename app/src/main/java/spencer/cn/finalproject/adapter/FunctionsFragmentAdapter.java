package spencer.cn.finalproject.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;

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
    public int getItemPosition(Object object) {
        Log.e("xx", "getItemPosition");
        return PagerAdapter.POSITION_NONE;
    }

//    @Override
//     public Object instantiateItem(ViewGroup container, int position) {
//            NewsTabFragment f = (NewsTabFragment) super.instantiateItem(container, position);
//            String title = fragTitles[position];
//            return f;
//     }

    @Override
    public int getCount() {
        return fragments.size();
    }

//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        ViewPager viewPager = (ViewPager) container;
//        NewsTabFragment obj = (NewsTabFragment) object;
//        viewPager.removeView(obj.getView());
//    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragTitles[position];
    }


}
