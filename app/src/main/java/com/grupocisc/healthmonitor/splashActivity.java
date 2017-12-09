package com.grupocisc.healthmonitor;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.grupocisc.healthmonitor.Home.activities.MainActivity;

public class splashActivity extends AppCompatActivity {

    private final static int TIMEXSPLASH = 3000;
    private String TAG = "splashActivity";
    private Handler handler;
    private Runnable runnable;
    private ImageView ugLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        //Transparent Status Bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_splash);
        //animacion ug

        /*ptv_anim = (ParticleView) findViewById(R.id.pv);
        ptv_anim.startAnim();
        ptv_anim.setOnParticleAnimListener(new ParticleView.ParticleAnimListener() {
            @Override
            public void onAnimationEnd() {
                Intent intent = new Intent(splashActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
                finish();
            }
        });*/

        //NUEVO METODO PARA REDUCIR EL TIEMPO DE CARGA DE SPLASH SCREENvAPP V3

        ugLogo = (ImageView) findViewById(R.id.UgLogo);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(splashActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
                finish();
            }
        },1200);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(handler != null) {
            handler.removeCallbacks(runnable);
            handler.postDelayed(runnable, TIMEXSPLASH);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(handler != null)
            handler.removeCallbacks(runnable);
    }
}
