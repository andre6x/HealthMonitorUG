package com.grupocisc.healthmonitor.Alarm.activities;

import android.content.Intent;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.grupocisc.healthmonitor.Alarm.adapters.AMainPagerAdapter;
import com.grupocisc.healthmonitor.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by developer on 21/07/2017.
 */

public class AlarmActivity extends AppCompatActivity {
    private static final String TAG = "[AlarmActivity]";

    @Bind(R.id.toolbar)     Toolbar toolbar;
    @Bind(R.id.tabs)        TabLayout tabs;
    @Bind(R.id.pager)       ViewPager pager;
    //public static FloatingActionButton fab;
    private ImageView ImageHeader ;

    CharSequence [] Titles ;
    AMainPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String Method ="[onCreate]";
        Log.i(TAG, Method +  "Init..."  );
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
                    //lyt_contenedor.setVisibility( View.VISIBLE );
                    //img_curso.setVisibility( View.VISIBLE );
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

        setContentView(R.layout.alarm_activity);
        ButterKnife.bind(this);
        ImageHeader = (ImageView) findViewById(R.id.header);
        Titles = getResources().getStringArray( R.array.array_AlarmsTitles);
//        fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                callActivityRegister();
//            }
//        });

        setToolbar();
        setUpTabs();

        Log.i(TAG, Method +  "End..." );
    }// END_onCreate

    //LLama a la actividad de ingreso de registros
    private void callActivityRegister() {
        String Method ="[callActivityRegister]";
        Log.i(TAG, Method +  "Init..."  );
        Intent intent = new Intent(this, AlarmRegistry.class);
        startActivity(intent);
        Log.i(TAG, Method + "End...");
    }


    //se ejecuta al seleccionar el icon back del toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String Method ="[onOptionsItemSelected]";
        Log.i(TAG, Method +  "Init..."  );
        if( item.getItemId() == android.R.id.home )
            callBackPressedActivity();
        Log.i(TAG, Method + "End...");
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

    private void setToolbar(){
        String Method ="[setToolbar]";
        Log.i(TAG, Method +  "Init..."  );
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back); //buton back toolbar
        getSupportActionBar().setTitle( getResources().getString(R.string.txt_Alarm) ); //titulo toolbar
        toolbar.setTitleTextColor(getResources().getColor(R.color.white)); //color toolbar title
        Log.i(TAG, Method + "End...");
    }

    //setear iconos para tabs
    private void setupTabIcons() {
        String Method ="[setupTabIcons]";
        Log.i(TAG, Method +  "Init..."  );
        // tabs.getTabAt(0).setIcon(R.mipmap.registro);
        tabs.getTabAt(0).setIcon(R.mipmap.registro);
        tabs.getTabAt(1).setIcon(R.mipmap.registro);
        tabs.getTabAt(2).setIcon(R.mipmap.registro);
        tabs.getTabAt(3).setIcon(R.mipmap.registro);
        //tabs.getTabAt(4).setIcon(R.mipmap.registro);

        Log.i(TAG, Method + "End...");
    }

    //setear adaptador viewpager
    void setUpTabs(){
        String Method ="[setUpTabs]";
        Log.i(TAG, Method +  "Init..."  );
        adapter =  new AMainPagerAdapter(this.getSupportFragmentManager(),Titles,Titles.length);
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(R.integer.int_setOffscreenPageLimit);
        tabs.setupWithViewPager(pager);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        ImageHeader.setImageResource(R.mipmap.land_alarm_1);
                        break;
                    case 1:
                        ImageHeader.setImageResource(R.mipmap.land_alarm_2);
                        break;
                    case 2:
                        ImageHeader.setImageResource(R.mipmap.land_alarm_3);
                        break;
                    case 3:
                        ImageHeader.setImageResource(R.mipmap.land_alarm_4);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupTabIcons();
        Log.i(TAG, Method + "End...");
    }




}
