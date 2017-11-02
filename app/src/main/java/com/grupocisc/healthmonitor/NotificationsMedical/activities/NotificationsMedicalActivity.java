package com.grupocisc.healthmonitor.NotificationsMedical.activities;

import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.grupocisc.healthmonitor.NotificationsMedical.adapters.NotificationsMedicalAdapter;
import com.grupocisc.healthmonitor.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationsMedicalActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tabs)    TabLayout tabs;
    @BindView(R.id.pager)   ViewPager pager;

    private String TAG = "NotifiMedicalAct";
    CharSequence Titles[]={"Notificaciones","Diabetes","Asma"};
    NotificationsMedicalAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Transparent Status Bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.notifi_medical_activity);

        ButterKnife.bind(this);

        setToolbar();
        setUpTabs();
    }
    //setear adaptador viewpager
    void setUpTabs(){
        adapter =  new NotificationsMedicalAdapter(this.getFragmentManager(),Titles,Titles.length);
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
        pager.setOffscreenPageLimit(3);
        setupTabIcons();
    }
    //setear iconos para tabs
    private void setupTabIcons() {
        tabs.getTabAt(0).setIcon(R.mipmap.estadistica);
        tabs.getTabAt(1).setIcon(R.mipmap.estadistica);
        tabs.getTabAt(2).setIcon(R.mipmap.estadistica);
    }

    public void setToolbar(){
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back); //buton back toolbar
        getSupportActionBar().setTitle("RECOMENDACIONES"); //titulo toolbar
        toolbar.setTitleTextColor(getResources().getColor(R.color.white)); //color toolbar title
    }

    //se ejecuta al seleccionar el icon back del toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            this.finish();

        return super.onOptionsItemSelected(item);
    }




}
