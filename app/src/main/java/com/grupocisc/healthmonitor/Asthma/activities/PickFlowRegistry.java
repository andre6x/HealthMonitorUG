package com.grupocisc.healthmonitor.Asthma.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.grupocisc.healthmonitor.Asthma.fragments.CausesGridFragment;
import com.grupocisc.healthmonitor.Asthma.fragments.FirstFragment;
import com.grupocisc.healthmonitor.Asthma.fragments.SymptomGridFragment;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.ArrayListSymptom;
import com.grupocisc.healthmonitor.entities.ArrayParamsFavoritesPick;

import java.util.ArrayList;
import java.util.List;

public class PickFlowRegistry extends AppCompatActivity {

    private static  final String TAG = "PickFlowRegistry";
    public static float FlujoMax ;
    public static String fecha ;
    public static String hora ;
    public static String observacion ;

    public static ArrayList<ArrayParamsFavoritesPick> arregloSymptom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pick_flow_registry);

        Utils.SetStyleToolbarLogo(this);
        callFragmentFirst();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home) {
            callBackTransitionActivity();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        callBackTransitionActivity();
    }
    public void callBackTransitionActivity(){
        finish();
        overridePendingTransition(R.anim.slide_in_left , R.anim.slide_out_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"onDestroy");
    }

    public void callFragmentFirst(){
        FirstFragment mFragment = new FirstFragment();
         getSupportFragmentManager().beginTransaction().replace(R.id.container_frame,mFragment).commit();
    }

    public void callFragmentSymptomCategory(){
        SymptomGridFragment mFragment = new SymptomGridFragment();
        Bundle args = new Bundle();
        mFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.container_frame,mFragment).commit();
    }

    public void callFragmentCausesCategory(){
        CausesGridFragment mFragment = new CausesGridFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container_frame,mFragment).commit();
    }

    public void finilizar(){
        finish();
    }


}
