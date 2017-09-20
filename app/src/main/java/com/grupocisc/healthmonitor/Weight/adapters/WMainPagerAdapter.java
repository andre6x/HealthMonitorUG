package com.grupocisc.healthmonitor.Weight.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.grupocisc.healthmonitor.Weight.fragments.WeightgraphicFragment;
import com.grupocisc.healthmonitor.Weight.fragments.WeightListFragment;


public class WMainPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public WMainPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if (position == 0) // if the position is 0 we are returning the First tab
        {
            WeightListFragment tab1 = new WeightListFragment();
            return tab1;
        } /*else if (position == 1) {
               WeightImcFragment tab2 = new WeightImcFragment();
                return tab2;
        } else if (position == 2) {
           WeightPesoIdealFragment tab3 = new WeightPesoIdealFragment();
            return tab3;
        }*/ else{
           WeightgraphicFragment tab4 = new WeightgraphicFragment();
            return tab4;
        }
    }


    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
