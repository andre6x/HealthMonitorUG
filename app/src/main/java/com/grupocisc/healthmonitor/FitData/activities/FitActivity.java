package com.grupocisc.healthmonitor.FitData.activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.grupocisc.healthmonitor.FitData.adapters.PMainPagerAdapter;
import com.grupocisc.healthmonitor.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FitActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)    Toolbar toolbar;
    @BindView(R.id.tabs)       TabLayout tabs;
    @BindView(R.id.pager)      ViewPager pager;
    public static FloatingActionButton fab;

    public static ArrayList<DataPoint> mDataPointList = new ArrayList<DataPoint>();

    PMainPagerAdapter adapter;
    CharSequence Titles[]={"REGISTRO CALORIAS","ESTADISTICA CALORIAS"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Transparent Status Bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        // TRANSITIONS
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){
            TransitionInflater inflater = TransitionInflater.from( this );
            Transition transition = inflater.inflateTransition( R.transition.transitions );
            getWindow().setSharedElementEnterTransition(transition);
            Transition transition1 = getWindow().getSharedElementEnterTransition();
            transition1.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {
                }
                @Override
                public void onTransitionEnd(Transition transition) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        //TransitionManager.beginDelayedTransition(mRoot, new Slide());
                    }

                }
                @Override
                public void onTransitionCancel(Transition transition) {
                }
                @Override
                public void onTransitionPause(Transition transition) {
                }
                @Override
                public void onTransitionResume(Transition transition) {
                }
            });
        }
        setContentView(R.layout.fit_activity);
        ButterKnife.bind(this);


        setToolbar();
        setUpTabs();


    }

    public void setToolbar(){
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back); //buton back tollbar
        getSupportActionBar().setTitle("GOOGLE FIT"); //titulo tollbar
        toolbar.setTitleTextColor(getResources().getColor(R.color.white)); //color tollbar title
    }

    //se ejecuta al seleccionar el icon back del toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home) {
            callBackPressedActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    //se ejecuta al dar click en el boton back del dispositivo
    @Override
    public void onBackPressed() {
        callBackPressedActivity();
        //super.onBackPressed();
    }

    public void callBackPressedActivity(){
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){
            pager.setVisibility( View.INVISIBLE );
            //TransitionManager.beginDelayedTransition(mRoot, new Slide());
        }
        super.onBackPressed();
    }

    //setear adaptador viewpager
    public void setUpTabs(){
        adapter =  new PMainPagerAdapter(this.getSupportFragmentManager(),Titles,Titles.length);
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
        setupTabIcons();
    }

    //setear iconos para tabs
    private void setupTabIcons() {
        tabs.getTabAt(0).setIcon(R.mipmap.registro);
        tabs.getTabAt(1).setIcon(R.mipmap.estadistica);
    }

    public void callActivityRegister(){

        //Intent  i = new Intent(this,PressureRegistyActivity.class);
        //startActivity(i);
    }

}
