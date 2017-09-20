package com.grupocisc.healthmonitor.Pulse.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;

public class PulseActivityAuto extends AppCompatActivity {

    ProgressBar mprogressBar;

    private Button mImgVwLaunchBeatMonitor;
    private int INDEX=0;
    private EditText mEdtxtDoctorId;
    private SharedPreferences mSharedPreferences;
    //int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    //int permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0 ;
    private static final int  MY_PERMISSIONS_REQUEST_CAMERA = 1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pulse_auto);

        Utils.SetStyleToolbarLogo(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

            }
        }
        initializeLayout();
    }

    //se ejecuta al seleccionar el icon back del toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            this.finish();
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {



        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
        }
    }
    private void initializeLayout() {
        mImgVwLaunchBeatMonitor=(Button)findViewById(R.id.push_button);
        mImgVwLaunchBeatMonitor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                INDEX=0;
                startActivity(new Intent(PulseActivityAuto.this,HeartRateMonitor.class));
                finish();
            }});



    }




    private void saveDoctorId(){
        mSharedPreferences
                .edit()
                .putString("doc_id", mEdtxtDoctorId.getText().toString())
                .commit();
    }

}
