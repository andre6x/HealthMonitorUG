package com.grupocisc.healthmonitor.Doctor.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.grupocisc.healthmonitor.Doctor.adapters.MMainPagerAdapter;
import com.grupocisc.healthmonitor.Doctor.adapters.doctorsListAdapter;
import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.Home.activities.MainActivity;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.SharedPreferencesManager;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IConsulDoctor;
import com.grupocisc.healthmonitor.entities.IRegistreDoctor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.pager)
    ViewPager pager;

    FloatingActionButton fab;
    MMainPagerAdapter adapter;
    CharSequence Titles[] = {"SELECCIONADOS"};

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
        setContentView(R.layout.doctores_activity);

        ButterKnife.bind(this);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callActivityRegister();
            }
        });

        setToolbar();
        setUpTabs();

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

    //LLama a la actividad de ingreso de registros
    private void callActivityRegister() {
        try {
            Intent intent = new Intent(this, SelectedDoctorActivity.class);
            startActivity(intent);
        } catch (Exception e) {
        }
    }

    public void setToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back); //buton back toolbar
        getSupportActionBar().setTitle("MIS DOCTORES"); //titulo toolbar
        toolbar.setTitleTextColor(getResources().getColor(R.color.white)); //color toolbar title

    }

    //setear iconos para tabs
    private void setupTabIcons() {
        tabs.getTabAt(0).setIcon(R.mipmap.registro);
        //  tabs.getTabAt(1).setIcon(R.mipmap.estadistica);
    }

    //setear adaptador viewpager
    void setUpTabs() {
        adapter = new MMainPagerAdapter(this.getFragmentManager(), Titles, Titles.length);
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
        setupTabIcons();
    }

}





