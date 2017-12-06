package com.grupocisc.healthmonitor.Routines.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Routines.adapters.LessonAdapter;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IRegistreRoutine;
import com.grupocisc.healthmonitor.entities.IRoutinesLessonU;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoutinesLessonActivity extends AppCompatActivity {

    private String TAG = "RoutinesLesson";
    private String cdn = "";
    private String urlImage = "";
    private String idCourse = "";
    private int idRutina;
    private String Name = "";
    private String Description = "";
    private String CountryCode = "593";
    private int contador = 0;
    private int idLesson = 0;
    private Toolbar toolbar;

    private LinearLayout linear_loading;
    private ProgressBar progress;
    private Button retry;
    private ProgressDialog Dialog;

    private RecyclerView recyclerView;

    private Call<ArrayList<IRoutinesLessonU.RoutineExercice>> call_1;
    private ArrayList<IRoutinesLessonU.RoutineExercice> arrayListExercice;
    private Call<IRegistreRoutine.RegistroRoutine> routineCall;
    private IRegistreRoutine.RegistroRoutine registroRoutine;
    private int countList = 0;
    private int numItem = 0;
    RatingBar ratingBar;

    public LessonAdapter lessonAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routines_lesson_activity);
        countList = 0;
        contador = 0;
        setToolbar();
        idRutina = this.getIntent().getIntExtra("idRutina", 0);
        if (this.getIntent().getStringExtra("urlImage") != null)
            urlImage = this.getIntent().getStringExtra("urlImage");

        LinearLayout lyt_contenedor = (LinearLayout) findViewById(R.id.lyt_contenedor);
        ImageView ivCar = (ImageView) findViewById(R.id.iv_car);
        TextView tvModel = (TextView) findViewById(R.id.tv_model);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        lyt_contenedor.setVisibility(View.VISIBLE);
        Utils.loadImage(urlImage, ivCar);
        tvModel.setText("Rutina " + idRutina);
        Dialog = new ProgressDialog(RoutinesLessonActivity.this);
        linear_loading = (LinearLayout) findViewById(R.id.linear_loading);
        progress = (ProgressBar) findViewById(R.id.progress);
        retry = (Button) findViewById(R.id.retry);

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartLoadingLesson(contador);
            }
        });
        restartLoadingLesson(contador);
        FloatingActionButton fabRoutine = (FloatingActionButton) findViewById(R.id.fabRoutine);
        fabRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contador += 1;
                countList += 1;
                if (contador > arrayListExercice.size()) {
                    insertRoutine();
                    finish();
                } else {
                    Intent intent = new Intent(RoutinesLessonActivity.this, RoutineDetalleActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("numItem", contador);
                    bundle.putInt("count", arrayListExercice.size());
                    bundle.putString("nombre", arrayListExercice.get(contador - 1).getNombre());
                    bundle.putString("repeticiones", arrayListExercice.get(contador - 1).getRepeticiones());
                    bundle.putString("UrlExercise", arrayListExercice.get(contador - 1).getUrl());
                    intent.putExtras(bundle);
                    RoutinesLessonActivity.this.startActivity(intent);

                }
            }
        });
    }

    public void updateAdapter(int count, int sizeList) {
        LessonAdapter adapterLesson = new LessonAdapter(this, arrayListExercice, count);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterLesson);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        showLayout();
    }

    public void saveLessonDatabase(View view) {
        try {
            Utils.DbsaveRoutineFromDatabase(idCourse, idLesson, 1, (new Date()).toString(), HealthMonitorApplicattion.getApplication().getCheckLesson());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void insertRoutine() {
        showLoading();
        String email = Utils.getEmailFromPreference(this);
        IRegistreRoutine iRegistreRoutine = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IRegistreRoutine.class);
        routineCall = iRegistreRoutine.putRoutine(email, idRutina, Math.round(ratingBar.getRating()));
        routineCall.enqueue(new Callback<IRegistreRoutine.RegistroRoutine>() {
            @Override
            public void onResponse(Call<IRegistreRoutine.RegistroRoutine> call, Response<IRegistreRoutine.RegistroRoutine> response) {
                if (response.isSuccessful()) {
                    registroRoutine = null;
                    registroRoutine = response.body();
                }
            }

            @Override
            public void onFailure(Call<IRegistreRoutine.RegistroRoutine> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void restartLoadingLesson(final int contadorList) {
        showLoading();
        IRoutinesLessonU iRoutinesLessonU = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IRoutinesLessonU.class);
        call_1 = iRoutinesLessonU.getRoutineExercice(idRutina);
        call_1.enqueue(new Callback<ArrayList<IRoutinesLessonU.RoutineExercice>>() {
            @Override
            public void onResponse(Call<ArrayList<IRoutinesLessonU.RoutineExercice>> call, Response<ArrayList<IRoutinesLessonU.RoutineExercice>> response) {
                if (response.isSuccessful()) {
                    arrayListExercice = null;
                    arrayListExercice = response.body();
                    postExecutionLesson(contadorList);
                } else {
                    showRetry();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<IRoutinesLessonU.RoutineExercice>> call, Throwable t) {
                showRetry();
                t.printStackTrace();
            }
        });
    }

    private void postExecutionLesson(int contadorList) {
        if (arrayListExercice != null) {
            if (arrayListExercice.size() > 0) {
                LessonAdapter adapter = new LessonAdapter(this, arrayListExercice, contadorList);
                recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(adapter);
                LinearLayoutManager llm = new LinearLayoutManager(this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(llm);
                showLayout();
            } else {
                showRetry();
            }
        } else {
            showRetry();
        }
    }


    private void showLayout() {
        linear_loading.setVisibility(View.GONE);

    }

    private void showLoading() {
        linear_loading.setVisibility(View.VISIBLE);
        progress.setVisibility(View.VISIBLE);
        retry.setVisibility(View.GONE);
    }

    private void showRetry() {
        linear_loading.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
        retry.setVisibility(View.VISIBLE);
    }

    private void showLoadingDialog() {
        Dialog.setMessage("Espere un Momento..");
        Dialog.setCancelable(false);
        Dialog.show();
    }

    private void showLayoutDialog() {
        if (Dialog != null)
            Dialog.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (contador > 0) {
            restartLoadingLesson(contador);
            if (contador == arrayListExercice.size()) {
                ratingBar.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }

}
