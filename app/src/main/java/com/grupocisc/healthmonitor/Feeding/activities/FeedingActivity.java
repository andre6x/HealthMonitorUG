package com.grupocisc.healthmonitor.Feeding.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.grupocisc.healthmonitor.Feeding.adapters.FeedingPagerAdapter;
import com.grupocisc.healthmonitor.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedingActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.pager)
    ViewPager pager;
    public static FloatingActionButton fab;

    FeedingPagerAdapter adapter;
    CharSequence Titles[] = {"REGISTRO", "DIETAS"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Transparent Status Bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.feeding_activity);
        ButterKnife.bind(this);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callActivityRegister();
            }
        });

        setToolbar();
        setUpTabs();
    }

    public void setToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);
        getSupportActionBar().setTitle("ALIMENTACIÓN");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
    }

    void setUpTabs() {
        adapter = new FeedingPagerAdapter(this.getSupportFragmentManager(), Titles, Titles.length);
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
        setupTabIcons();
    }

    private void setupTabIcons() {
        tabs.getTabAt(0).setIcon(R.mipmap.registro);
        tabs.getTabAt(1).setIcon(R.mipmap.restaurant);
    }

    public void callActivityRegister() {
        Intent i = new Intent(this, FeedingRegistyActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            this.finish();
        return super.onOptionsItemSelected(item);
    }
}
