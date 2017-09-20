package com.grupocisc.healthmonitor.SocialNetworks.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Routines.activities.RoutinesActivity;
import com.grupocisc.healthmonitor.Routines.fragments.RoutinesGridFragment;
import com.grupocisc.healthmonitor.SocialNetworks.fragments.FacebookFragment;
import com.grupocisc.healthmonitor.SocialNetworks.fragments.FacebookFriendsFragment;
import com.grupocisc.healthmonitor.SocialNetworks.fragments.TwitterFragment;
import com.grupocisc.healthmonitor.SocialNetworks.fragments.TwitterFriendsFragment;
import com.grupocisc.healthmonitor.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SocialActivity extends AppCompatActivity {

    TabLayout tabLayout;
    private int[] tabIcons = {
            R.drawable.logo_twitter,
            R.drawable.logo_twitter,
            R.drawable.logo_facebook,
            R.drawable.logo_facebook
    };
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);

        Utils.SetStyleToolbarLogo(this);
        viewPager = (ViewPager) findViewById(R.id.tabanim_viewpager);
        viewPager.setOffscreenPageLimit(4);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabanim_tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());

                switch (tab.getPosition()) {
                    case 0:
                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            this.finish();
        return super.onOptionsItemSelected(item);
    }
    private void setupViewPager(ViewPager viewPager) {

        SocialActivity.ViewPagerAdapter adapter = new SocialActivity.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new TwitterFragment(), "TWITTER");
        adapter.addFrag(new TwitterFriendsFragment(),"COMUNIDAD");
        adapter.addFrag(new FacebookFragment(), "FACEBOOK");
        adapter.addFrag(new FacebookFriendsFragment(), "COMUNIDAD");
        viewPager.setAdapter(adapter);

    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }


        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
