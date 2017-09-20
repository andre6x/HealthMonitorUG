package com.grupocisc.healthmonitor.Disease.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.grupocisc.healthmonitor.Disease.fragments.DiseaseListFragment;
import com.grupocisc.healthmonitor.Medicines.fragments.MedicineGraphicFragment;

/**
 * Created by Gema on 10/01/2017.
 */

public class MMainPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created

    //constructor
    public MMainPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabs ) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabs;

    }

    //metodos
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0: {
                DiseaseListFragment tab1 = new DiseaseListFragment();
                return tab1;
            }
            default:
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
