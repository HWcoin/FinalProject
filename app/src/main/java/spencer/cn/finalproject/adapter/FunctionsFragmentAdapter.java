package spencer.cn.finalproject.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import spencer.cn.finalproject.fragment.NewsTabFragment;

/**
 * Created by Administrator on 2017/3/5.FragmentStatePagerAdapter
 */

public class FunctionsFragmentAdapter extends FragmentStatePagerAdapter {
    private ArrayList<NewsTabFragment> fragments;
    private String[] fragTitles;
    private FragmentManager fragmentManager;
    public FunctionsFragmentAdapter(FragmentManager fm, ArrayList<NewsTabFragment> fragments, String[] fragTitles) {
        super(fm);
        this.fragments = fragments;
        this.fragTitles = fragTitles;
        this.fragmentManager = fm;

//        setFragments(fragments);
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


    public void updateData(ArrayList<NewsTabFragment> flists) {
        setFragments(flists);
    }

    private void setFragments(ArrayList<NewsTabFragment> mFragmentList) {
        if(fragments != null){
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            for(Fragment f: fragments){
                fragmentTransaction.remove(f);
            }
            fragmentTransaction.commit();
            fragmentManager.executePendingTransactions();
        }
        this.fragments = mFragmentList;
        notifyDataSetChanged();
//        if(this.fragments != null){
//            fragments.clear();
//        }
//        this.fragments = mFragmentList;
//        notifyDataSetChanged();
    }

//    public int getItemPosition(Object object) {
//        return POSITION_NONE;
//    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager)container).removeView( ((NewsTabFragment) object).getView());
    }

    @Override
    public Object instantiateItem(View collection, int position) {
        View v = fragments.get(position).getView();
        ((ViewPager)collection).addView(v);
        return v;
    }


}
