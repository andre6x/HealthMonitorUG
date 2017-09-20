package com.grupocisc.healthmonitor.Feeding.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.grupocisc.healthmonitor.Feeding.fragments.FeedingCaloriesDietFragment;
import com.grupocisc.healthmonitor.Feeding.fragments.FeedingsListFragments;

/**
 * Created by Walter on 01/02/2017.
 */

public class FeedingPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence titles[];
    int numbOfTabs;

    public FeedingPagerAdapter(FragmentManager fm, CharSequence[] titles, int numbOfTabs) {
        super(fm);
        this.titles = titles;
        this.numbOfTabs = numbOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            FeedingsListFragments tab1 = new FeedingsListFragments();
            return tab1;
        } else {
            FeedingCaloriesDietFragment tab2 = new FeedingCaloriesDietFragment();
            return tab2;
        }
    }

    @Override
    public int getCount() {
        return numbOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
