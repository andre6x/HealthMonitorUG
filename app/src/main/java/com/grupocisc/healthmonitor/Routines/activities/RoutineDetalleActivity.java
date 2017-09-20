package com.grupocisc.healthmonitor.Routines.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.grupocisc.healthmonitor.Utils.Utils;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.grupocisc.healthmonitor.R;

public class RoutineDetalleActivity extends AppCompatActivity {


    private String description = "";
    private String repeticiones = "";
    private String UrlExercise = "";
    private String TAG = "RoutineExerciseActivities";
    private String cdn = "";
    private String urlImage = "";
    private String idCategory = "";
    private int idCourse = 0;
    private int idLesson = 0;
    private int count = 0;
    private int numItem = 0;
    private String Name = "";
    RatingBar ratingBar;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routine_detalleactivity);
        if (this.getIntent().getIntExtra("count", 0) != 0)
            count = this.getIntent().getIntExtra("count", 1);
        if (this.getIntent().getIntExtra("numItem", 0) != 0)
            numItem = this.getIntent().getIntExtra("numItem", 1);
        if (!"".equals(this.getIntent().getStringExtra("nombre")))
            description = this.getIntent().getStringExtra("nombre");
        if (!"".equals(this.getIntent().getStringExtra("repeticiones")))
            repeticiones = this.getIntent().getStringExtra("repeticiones");
        if (!"".equals(this.getIntent().getStringExtra("UrlExercise")))
            UrlExercise = this.getIntent().getStringExtra("UrlExercise");


        LinearLayout lyt_contenedor = (LinearLayout) findViewById(R.id.lyt_contenedor);
        ImageView ivCar = (ImageView) findViewById(R.id.iv_car);
        TextView tvModel = (TextView) findViewById(R.id.tv_model);
        TextView tvBrand = (TextView) findViewById(R.id.tv_brand);

        tvModel.setText(description);
        tvBrand.setText("Repeticiones: " + repeticiones);
        Utils.loadImage(UrlExercise, ivCar);
        lyt_contenedor.setVisibility(View.VISIBLE);
        FloatingActionButton fabRoutine = (FloatingActionButton) findViewById(R.id.fabRoutine);
        fabRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
    }

}
