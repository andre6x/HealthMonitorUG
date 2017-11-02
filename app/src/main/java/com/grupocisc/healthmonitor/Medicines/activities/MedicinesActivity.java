package com.grupocisc.healthmonitor.Medicines.activities;

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
import com.grupocisc.healthmonitor.Alarm.activities.AlarmActivity;
import com.grupocisc.healthmonitor.Medicines.adapters.MMainPagerAdapter;
import com.grupocisc.healthmonitor.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/*Librería para inyectar views (vistas) en Android. Facilitará la tarea de relacionar
los elementos de las vistas con el código en nuestras aplicaciones Android.*/

/************************************************************************************/

public class MedicinesActivity extends AppCompatActivity {
    private static final String TAG = "[MedicinesActivity]";

    com.github.clans.fab.FloatingActionButton fb1,fb2;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tabs)    TabLayout tabs;
    @BindView(R.id.pager)   ViewPager pager;
    public static FloatingActionButton fab;

//    public static com.github.clans.fab.FloatingActionButton fabRegistro;
//    public static com.github.clans.fab.FloatingActionButton fabControl;
    MMainPagerAdapter adapter;
//    CharSequence Titles[]={"CONTROL", "REGISTRO" ,"ESTADISTICAS"};
    CharSequence [] Titles ;//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String Method ="[onCreate]";
        Log.i(TAG, Method + "Init..."  );
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
        setContentView(R.layout.medicines_activity);

        fb1 = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.subFloatingMenu1) ;
        fb2 = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.subFloatingMenu3) ;

        fb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callActivityControl();
            }
        });
        fb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callActivityAlarma();
            }
        });

        ButterKnife.bind(this);
        Titles = getResources().getStringArray( R.array.array_MedicinesTitles);


//        fabControl = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fabControl);
//        fabRegistro = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fabRegistro);
//        fabRegistro.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try{
//                    callActivityRegister();
//                }catch(Exception e)
//                {
//                    Toast.makeText(MedicinesActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
//
//                }
//
//            }
//        });
//
//        fabControl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try{
//                    callActivityControl();
//                }
//                catch(Exception e)
//                {
//                    Toast.makeText(MedicinesActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        setToolbar();
        setUpTabs();

//        if(!isOnline(this))
//            Utils.generarAlerta(this, getString(R.string.txt_atencion), getString(R.string.sin_conexion));

        Log.i(TAG, Method + "End..."  );
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
        String Method ="[onBackPressed]";
        Log.i(TAG, Method + "Init..."  );
        callBackPressedActivity();
        //super.onBackPressed();
        Log.i(TAG, Method + "End..."  );
    }

    public void callBackPressedActivity(){
        String Method ="[callBackPressedActivity]";
        Log.i(TAG, Method + "Init..."  );
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){
            pager.setVisibility( View.INVISIBLE );
            //TransitionManager.beginDelayedTransition(mRoot, new Slide());
        }
        Log.i(TAG, Method + "End..."  );
        super.onBackPressed();
    }

    //LLama a la actividad de ingreso de registros
    private void callActivityRegister() {
        String Method ="[callActivityRegister]";
        Log.i(TAG, Method + "Init..."  );
        Intent intent = new Intent(this, MedicineRegistry.class);
        startActivity(intent);
        Log.i(TAG, Method + "End..."  );
    }

    public void setToolbar(){
        String Method ="[setToolbar]";
        Log.i(TAG, Method + "Init..."  );
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back); //buton back toolbar
        //getSupportActionBar().setTitle("MEDICAMENTOS"); //titulo toolbar
        getSupportActionBar().setTitle( getResources().getString(R.string.txt_Medicines)  ); //titulo toolbar
        toolbar.setTitleTextColor(getResources().getColor(R.color.white)); //color toolbar title
        Log.i(TAG, Method + "End..."  );
    }

    //setear iconos para tabs
    private void setupTabIcons() {
        String Method ="[setupTabIcons]";
        Log.i(TAG, Method + "Init..."  );
        tabs.getTabAt(0).setIcon(R.mipmap.registro);
        tabs.getTabAt(1).setIcon(R.mipmap.registro);
        tabs.getTabAt(2).setIcon(R.mipmap.estadistica);
       // tabs.getTabAt(3).setIcon(R.mipmap.estadistica);
        Log.i(TAG, Method + "End..."  );
    }

    //setear adaptador viewpager
    void setUpTabs(){
        String Method ="[setToolbar]";
        Log.i(TAG, Method + "Init..."  );
        adapter =  new MMainPagerAdapter(this.getSupportFragmentManager() ,Titles,Titles.length);
        Log.i(TAG, Method + "adapterMed"  );//Log.e("MedActivity","adapterMed");
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(R.integer.int_setOffscreenPageLimit);
        tabs.setupWithViewPager(pager);
        setupTabIcons();
        Log.i(TAG, Method + "End..."  );
    }

    private void callActivityControl() {
        String Method ="[callActivityControl]";
        Log.i(TAG, Method + "Init..."  );
        Intent intent = new Intent(this, MedicinesRegisteredActivity.class);
        startActivity(intent);
        Log.i(TAG, Method + "End..."  );
    }

    public void callActivityAlarma(){

        Intent  i = new Intent(this,AlarmActivity.class);
        startActivity(i);
    }


}
