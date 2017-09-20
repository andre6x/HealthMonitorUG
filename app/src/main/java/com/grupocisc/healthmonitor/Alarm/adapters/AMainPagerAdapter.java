package com.grupocisc.healthmonitor.Alarm.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.grupocisc.healthmonitor.Alarm.fragments.AlarmListFragment;
import  com.grupocisc.healthmonitor.Alarm.fragments.AlarmListMedicineTypeCardFragment;

/**
 * Created by developer on 21/07/2017.
 */

public class AMainPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[];
    int NumbOfTabs;


    public AMainPagerAdapter(FragmentManager fm ,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    @Override
    public Fragment getItem(int position) {
        AlarmListFragment obj = new AlarmListFragment();

//        switch (position){
//
////            //case 0: {return new AlarmListMedicineTypeCardFragment() ; }
//            case 0: {return (new AlarmListFragment().setOp(position); }
//            case 1: {return new AlarmListFragment(); }
//            case 2: {return new AlarmListFragment(); }
//            case 3: {return new AlarmListFragment(); }
//            default:
//                return null;
//        }

        obj.setOp(position);
        return  obj;
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
