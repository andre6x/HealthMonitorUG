package com.grupocisc.healthmonitor.Medicines.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.grupocisc.healthmonitor.Medicines.fragments.MedicineGraphicFragment;
import com.grupocisc.healthmonitor.Medicines.fragments.MedicineListFragment;
import com.grupocisc.healthmonitor.Medicines.fragments.MedicineTakeListFragment;
import com.grupocisc.healthmonitor.Medicines.fragments.MedicinesRegisteredListFragment;

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
        Fragment fragment ;//= new Fragment();
        switch (position) {
           /* case 0: {
                MedicinesControlConcept tab1 = new MedicinesControlConcept();
                return tab1;
            }
            */
           case 0: {
               fragment = new MedicinesRegisteredListFragment();
               break;
               //return tab2;
//                MedicineListFragment tab2 = new MedicineListFragment();
//                return tab2;
            }case 1: {
                fragment = new MedicineTakeListFragment();
                break;
//                MedicinesRegisteredListFragment tab3 = new MedicinesRegisteredListFragment();
//                return tab3;
            }
            case 2: {
                fragment = new MedicineGraphicFragment();
                break;
                //MedicineGraphicFragment tab4 = new MedicineGraphicFragment();
                //return tab4;
            }
            default:
                fragment = null;
        }
        return fragment;

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
