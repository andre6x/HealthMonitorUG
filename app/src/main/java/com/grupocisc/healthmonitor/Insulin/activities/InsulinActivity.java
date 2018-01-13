package com.grupocisc.healthmonitor.Insulin.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.grupocisc.healthmonitor.Insulin.adapters.IMainPagerAdapter;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.grupocisc.healthmonitor.Utils.Utils.isOnline;

/************************************************************************************/

public class InsulinActivity extends AppCompatActivity {
    private static final String TAG = "[InsulinActivity]";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tabs)    TabLayout tabs;
    @BindView(R.id.pager)   ViewPager pager;
    public static FloatingActionButton fab;
    IMainPagerAdapter adapter;
    CharSequence Titles[]={"REGISTRO","ESTADÍSTICAS","RECOMENDACIONES"};
    //CharSequence [] Titles ;

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
        setContentView(R.layout.insulin_activity);
        ButterKnife.bind(this);
        //Titles = getResources().getStringArray( R.array.lst_InsulinTitles );
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    callActivityRegister();
            }
        });

        setToolbar();
        setUpTabs();

        Log.i(TAG, Method +  "End..." );

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
    //LLama a la actividad de ingreso de registros
    private void callActivityRegister() {
        String Method ="[callActivityRegister]";
        Log.i(TAG, Method +  "Init..."  );
        Intent intent = new Intent(this, InsulinRegistry.class);
        startActivity(intent);
        Log.i(TAG, Method + "End...");
    }

    public void setToolbar(){
        String Method ="[setToolbar]";
        Log.i(TAG, Method +  "Init..."  );
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back); //buton back toolbar
        //getSupportActionBar().setTitle("INSULINA"); //titulo toolbar
        getSupportActionBar().setTitle( getResources().getString(R.string.txt_Insulin) ); //titulo toolbar
        toolbar.setTitleTextColor(getResources().getColor(R.color.white)); //color toolbar title
        Log.i(TAG, Method + "End...");

    }

    //setear iconos para tabs
    private void setupTabIcons() {
        String Method ="[setupTabIcons]";
        Log.i(TAG, Method +  "Init..."  );
       // tabs.getTabAt(0).setIcon(R.mipmap.registro);
        tabs.getTabAt(0).setIcon(R.mipmap.registro);
        tabs.getTabAt(1).setIcon(R.mipmap.estadistica);
        tabs.getTabAt(2).setIcon(R.mipmap.medical_history);
        Log.i(TAG, Method + "End...");
    }

    //setear adaptador viewpager
    void setUpTabs(){
        String Method ="[setUpTabs]";
        Log.i(TAG, Method +  "Init..."  );
        adapter =  new IMainPagerAdapter(this.getSupportFragmentManager(),Titles,Titles.length);
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(R.integer.int_setOffscreenPageLimit);
        tabs.setupWithViewPager(pager);
        setupTabIcons();
        Log.i(TAG, Method + "End...");
    }



}
