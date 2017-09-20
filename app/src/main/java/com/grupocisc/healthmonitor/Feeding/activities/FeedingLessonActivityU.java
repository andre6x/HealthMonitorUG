package com.grupocisc.healthmonitor.Feeding.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
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

import com.grupocisc.healthmonitor.Feeding.adapters.FeedingLessonAdapterU;
import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IRegistreDiet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.id;

/**
 * Created by Walter on 01/03/2017.
 */

public class FeedingLessonActivityU extends AppCompatActivity {

    private String urlImage = "";
    private String Name = "";
    private String Description = "";
    private int contador = 0;
    private int idDieta = 0;
    private LinearLayout linear_loading;
    private ProgressBar progress;
    private Button retry;
    private ProgressDialog Dialog;
    private RecyclerView recyclerView;

    Call<IRegistreDiet.RegistreDiet> dietCall;
    IRegistreDiet.RegistreDiet registreDiet;
    private int countList = 0;
    private int numItem = 0;
    RatingBar ratingBar;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feeding_lesson_activity);
        setToolbar();
        countList = 0;
        contador = 0;
        if (this.getIntent().getStringExtra("urlImage") != null)
            urlImage = this.getIntent().getStringExtra("urlImage");
        if (this.getIntent().getStringExtra("Description") != null)
            Description = this.getIntent().getStringExtra("Description");
        if (this.getIntent().getStringExtra("Name") != null)
            Name = this.getIntent().getStringExtra("Name");
        idDieta = this.getIntent().getIntExtra("idDieta", 0);
        LinearLayout lyt_contenedor = (LinearLayout) findViewById(R.id.lyt_contenedor);
        ImageView ivDiet = (ImageView) findViewById(R.id.iv_car);
        TextView tvNombre = (TextView) findViewById(R.id.tv_model);
        TextView tvBrand = (TextView) findViewById(R.id.tv_brand);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        lyt_contenedor.setVisibility(View.VISIBLE);

        Utils.loadImage(urlImage, ivDiet);
        tvNombre.setText(Name);
        tvBrand.setText("");

        Dialog = new ProgressDialog(FeedingLessonActivityU.this);
        linear_loading = (LinearLayout) findViewById(R.id.linear_loading);
        progress = (ProgressBar) findViewById(R.id.progress);
        retry = (Button) findViewById(R.id.retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postExecuteLesson(contador);
            }
        });
        postExecuteLesson(contador);
        FloatingActionButton fabFeeding = (FloatingActionButton) findViewById(R.id.fabFeeding);
        fabFeeding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertDiet();
            }
        });
    }

    private void insertDiet() {
        String email = Utils.getEmailFromPreference(this);
        IRegistreDiet iRegistreDiet = HealthMonitorApplicattion.getApplication().getmRestCISCAdapter().create(IRegistreDiet.class);
        dietCall = iRegistreDiet.putDiet(email, idDieta, Math.round(ratingBar.getRating()));
        dietCall.enqueue(new Callback<IRegistreDiet.RegistreDiet>() {
            @Override
            public void onResponse(Call<IRegistreDiet.RegistreDiet> call, Response<IRegistreDiet.RegistreDiet> response) {
                if (response.isSuccessful()) {
                    registreDiet = null;
                    registreDiet = response.body();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<IRegistreDiet.RegistreDiet> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void postExecuteLesson(int contadorList) {
        String[] descripcion = new String[5];
        if (Description != null) {
            descripcion = Description.split("\\\\n");
        }
        if (descripcion.length > 0) {
            FeedingLessonAdapterU adapter = new FeedingLessonAdapterU(this, descripcion, contadorList);
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(llm);
            showLayout();
        } else {
            showLayout();
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

    public void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
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
