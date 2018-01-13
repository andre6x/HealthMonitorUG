package com.grupocisc.healthmonitor.Insulin.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.grupocisc.healthmonitor.Insulin.fragments.InsulinConcept;
import com.grupocisc.healthmonitor.Insulin.fragments.InsulinGraphicFragment;
import com.grupocisc.healthmonitor.Insulin.fragments.InsulinListFragment;
import com.grupocisc.healthmonitor.Insulin.fragments.InsulinRecommendationsFragment;


/**
 * Created by Munish on 10/15/15.
 */

public class IMainPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[];
    int NumbOfTabs;

    public IMainPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:{
                InsulinListFragment tab2 = new InsulinListFragment();
                return tab2;
            }
            case 1:{
                InsulinGraphicFragment tab3 = new InsulinGraphicFragment();
                return tab3;
            }
            case 2:{
                InsulinRecommendationsFragment tab4 = new InsulinRecommendationsFragment();
                return tab4;


            }default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
