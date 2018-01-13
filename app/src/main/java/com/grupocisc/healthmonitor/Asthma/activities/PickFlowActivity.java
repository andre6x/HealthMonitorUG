package com.grupocisc.healthmonitor.Asthma.activities;

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
import android.view.MenuItem;
import android.view.View;

import com.grupocisc.healthmonitor.Asthma.adapters.PMainPagerAdapter;
import com.grupocisc.healthmonitor.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PickFlowActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    //@BindView(R.id.fab)        FloatingActionButton fab;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.pager)
    ViewPager pager;
    //@BindView(R.id.lyt_contenedor) ViewGroup mRoot;
    public static FloatingActionButton fab;


    PMainPagerAdapter adapter;
    CharSequence Titles[] = {"REGISTRO", "ESTADISTICAS","RECOMENDACIONES"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Transparent Status Bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        // TRANSITIONS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TransitionInflater inflater = TransitionInflater.from(this);
            Transition transition = inflater.inflateTransition(R.transition.transitions);
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
        setContentView(R.layout.pick_flow_activity);
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
        //getSupportActionBar().setDisplayShowTitleEnabled(false); //desactivar title
        toolbar.setNavigationIcon(R.mipmap.back); //buton back tollbar
        getSupportActionBar().setTitle("FLUJO MÁXIMO"); //titulo tollbar
        toolbar.setTitleTextColor(getResources().getColor(R.color.white)); //color tollbar title
    }

    //se ejecuta al seleccionar el icon back del toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
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

    public void callBackPressedActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pager.setVisibility(View.INVISIBLE);
            //TransitionManager.beginDelayedTransition(mRoot, new Slide());
        }
        super.onBackPressed();
    }

    //setear adaptador viewpager
    void setUpTabs() {
        adapter = new PMainPagerAdapter(this.getSupportFragmentManager(), Titles, Titles.length);
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
        setupTabIcons();
    }

    //setear iconos para tabs
    private void setupTabIcons() {
        tabs.getTabAt(0).setIcon(R.mipmap.registro);
        tabs.getTabAt(1).setIcon(R.mipmap.estadistica);
        tabs.getTabAt(2).setIcon(R.mipmap.ic_notification);
    }

    public void callActivityRegister() {

        Intent i = new Intent(this, PickFlowRegistry.class);
        startActivity(i);
    }
}
