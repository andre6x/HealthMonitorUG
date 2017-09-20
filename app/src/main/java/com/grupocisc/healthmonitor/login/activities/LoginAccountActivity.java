package com.grupocisc.healthmonitor.login.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.login.fragments.LoginDataOneFragment;
import com.grupocisc.healthmonitor.login.fragments.LoginDataThreeFragment;
import com.grupocisc.healthmonitor.login.fragments.LoginDataTwoFragment;

public class LoginAccountActivity extends AppCompatActivity {

    private String title = "CREAR CUENTA";
    private String TAG= "LoginAccountActivity";

    public String Nombre  = "";
    public String Apellido ="";
    public String Email = "";
    public String Pass = "";
    public String Anio ="";
    public String Peso = "";
    public String Altura = "";
    public String Sexo = "";
    public String EstCivil ="";
    public String Pais = "";
    public String Telefono = "";
    public boolean IsAsma = false;
    public int TipoDiabetes = 0;
    public String Facebook_Id = "",Facebook_PictureUri = "", Facebook_FirstName = "";
    public String Facebook_LastName= "", Facebook_Email = "", Facebook_Gender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_account_activity);

        Utils.SetStyleToolbarTitle(this, title);

        //INI OBTENER DATOS DE FACEBOOK
        if ( this.getIntent().getStringExtra("Facebook_Id") != null)
            Facebook_Id = this.getIntent().getStringExtra("Facebook_Id");
        if ( this.getIntent().getStringExtra("Facebook_PictureUri") != null)
            Facebook_PictureUri = this.getIntent().getStringExtra("Facebook_PictureUri");
        if ( this.getIntent().getStringExtra("Facebook_FirstName") != null)
            Facebook_FirstName = this.getIntent().getStringExtra("Facebook_FirstName");
        if ( this.getIntent().getStringExtra("Facebook_LastName") != null)
            Facebook_LastName = this.getIntent().getStringExtra("Facebook_LastName");
        if ( this.getIntent().getStringExtra("Facebook_Email") != null)
            Facebook_Email = this.getIntent().getStringExtra("Facebook_Email");
        if ( this.getIntent().getStringExtra("Facebook_Gender") != null)
            Facebook_Gender = this.getIntent().getStringExtra("Facebook_Gender");

        Log.e(TAG,"Facebook_Id :" + Facebook_Id  + "--Facebook_PictureUri:" + Facebook_PictureUri );
        Log.e(TAG,"Facebook_FirstName :" + Facebook_FirstName  + "--Facebook_LastName:" + Facebook_LastName );
        Log.e(TAG,"Facebook_Email :" + Facebook_Email  + "--Facebook_Gender:" + Facebook_Gender );


        Nombre  = Facebook_FirstName;
        Apellido = Facebook_LastName;
        Email = Facebook_Email;
        //FIN OBTENER DATOS DE FACEBOOK

        callFragmentDataOne();
    }
    //se ejecuta al seleccionar el icon back del toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            this.finish();

        return super.onOptionsItemSelected(item);
    }

    public void callFragmentDataOne(){
        LoginDataOneFragment mFragment = new LoginDataOneFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, mFragment);
        fragmentTransaction.commit();
    }

    public void callFragmentDataTwo(){
        LoginDataTwoFragment mFragment = new LoginDataTwoFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, mFragment);
        fragmentTransaction.commit();
    }

    public void callFragmentDataThree(){
        LoginDataThreeFragment mFragment = new LoginDataThreeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, mFragment);
        fragmentTransaction.commit();
    }

}
