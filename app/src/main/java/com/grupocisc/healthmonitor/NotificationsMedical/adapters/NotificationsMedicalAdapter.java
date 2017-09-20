package com.grupocisc.healthmonitor.NotificationsMedical.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.grupocisc.healthmonitor.NotificationsMedical.fragments.NotificationsMedical;
import com.grupocisc.healthmonitor.NotificationsMedical.fragments.NotificationsTips;
import com.grupocisc.healthmonitor.NotificationsMedical.fragments.NotificationsTipsAsma;

/**
 * Created by Gems on 13/02/2017.
 */

public class NotificationsMedicalAdapter extends FragmentStatePagerAdapter {

    CharSequence    Titles[];
    int NumbOfTabs;

    public NotificationsMedicalAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabs ) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabs;

    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:{
                NotificationsMedical tab1 = new NotificationsMedical();
                return tab1;
            }

            case 1:{
                NotificationsTips tab2 = new NotificationsTips();
                return tab2;
            }

            case 2:{
                NotificationsTipsAsma tab2 = new NotificationsTipsAsma();
                return tab2;
            }
        }
        return null;

    }

    // This method return the titles for the Tabs in the Tab Strip
    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    @Override
    public int getCount() {
        return NumbOfTabs;
    }



}
