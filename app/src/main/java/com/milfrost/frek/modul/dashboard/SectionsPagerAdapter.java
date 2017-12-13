package com.milfrost.frek.modul.dashboard;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by ASUS on 28/11/2017.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private int pageCount;
    private List<Fragment> fragments;
    private String[] tabTitle;

    public SectionsPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.pageCount = fragments.size();
        this.fragments = fragments;
    }

    public SectionsPagerAdapter(FragmentManager fm, List<Fragment> fragments,String[] tabTitle) {
        super(fm);
        this.pageCount = fragments.size();
        this.fragments = fragments;
        this.tabTitle = tabTitle;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return pageCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(tabTitle==null)
            return "";
        else
            return tabTitle[position];
    }
}
