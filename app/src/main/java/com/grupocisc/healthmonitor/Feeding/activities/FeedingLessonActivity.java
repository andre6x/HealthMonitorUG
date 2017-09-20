package com.grupocisc.healthmonitor.Feeding.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.grupocisc.healthmonitor.Feeding.adapters.FeedingLessonAdapter;
import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IFeedingLesson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Walter on 13/02/2017.
 */

public class FeedingLessonActivity extends AppCompatActivity {

    private String cdn = "";
    private String urlImage = "";
    private String idCourse = "";
    private String Name = "";
    private String Description = "";
    private String CountryCode = "593";
    private int contador = 0;
    private int idLesson = 0;

    private LinearLayout linear_loading;
    private ProgressBar progress;
    private Button retry;
    private ProgressDialog Dialog;
    private RecyclerView recyclerView;

    private Call<IFeedingLesson.FeedingLesson> call_0;
    private IFeedingLesson.FeedingLesson mFeedingLesson;
    private int countList = 0;
    private int numItem = 0;
    RatingBar ratingBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feeding_lesson_activity);

        countList = 0;
        contador = 0;
        //obtener datos
        if (this.getIntent().getStringExtra("cdn") != null)
            cdn = this.getIntent().getStringExtra("cdn");
        if (this.getIntent().getStringExtra("idCourse") != null)
            idCourse = this.getIntent().getStringExtra("idCourse");
        /*if (this.getIntent().getStringExtra("idLesson") != null)
            idLesson = this.getIntent().getIntExtra("idLesson", 0);*/
        if (this.getIntent().getStringExtra("urlImage") != null)
            urlImage = this.getIntent().getStringExtra("urlImage");
        if (this.getIntent().getStringExtra("Description") != null)
            Description = this.getIntent().getStringExtra("Description");
        if (this.getIntent().getStringExtra("Name") != null)
            Name = this.getIntent().getStringExtra("Name");

        LinearLayout lyt_contenedor = (LinearLayout) findViewById(R.id.lyt_contenedor);
        ImageView ivCar = (ImageView) findViewById(R.id.iv_car);
        TextView tvModel = (TextView) findViewById(R.id.tv_model);
        TextView tvBrand = (TextView) findViewById(R.id.tv_brand);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        /*
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                System.out.println("ON RATING");
            }
        });*/
        lyt_contenedor.setVisibility(View.VISIBLE);

        Utils.loadImage(urlImage, ivCar);
        tvModel.setText(Name);
        tvBrand.setText(Name);

        Dialog = new ProgressDialog(FeedingLessonActivity.this);
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
        FloatingActionButton fabFeeding = (FloatingActionButton) findViewById(R.id.fabFeeding);

    }

    private void restartLoadingLesson(final int contadorList) {
        showLoading();
        IFeedingLesson iFeedingLesson = HealthMonitorApplicattion.getApplication().getRestAdapter().create(IFeedingLesson.class);
        call_0 = iFeedingLesson.getLessonFrom(CountryCode, "" + idCourse, "50", "0");
        call_0.enqueue(new Callback<IFeedingLesson.FeedingLesson>() {
            @Override
            public void onResponse(Call<IFeedingLesson.FeedingLesson> call, Response<IFeedingLesson.FeedingLesson> response) {
                if(response.isSuccessful()){
                    mFeedingLesson = null;
                    mFeedingLesson = response.body();
                    postExecuteLesson(contadorList);
                }else{
                    showRetry();
                }
            }

            @Override
            public void onFailure(Call<IFeedingLesson.FeedingLesson> call, Throwable t) {
                showRetry();
                t.printStackTrace();
            }
        });
    }

    private void postExecuteLesson(int contadorList){
        if (mFeedingLesson != null) {
            if (mFeedingLesson.getCode() == 0) {
                if (mFeedingLesson.getRows() != null) {
                    if (mFeedingLesson.getRows().size() > 0) {
                        FeedingLessonAdapter adapter = new FeedingLessonAdapter(this, mFeedingLesson.getRows(), mFeedingLesson.getSummary().getCdn(), contadorList);
                        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setAdapter(adapter);
                        LinearLayoutManager llm = new LinearLayoutManager(this);
                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerView.setLayoutManager(llm);
                        showLayout();
                    } else
                        showRetry();

                } else
                    showRetry();
            } else
                showRetry();
        } else
            showRetry();
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
}
