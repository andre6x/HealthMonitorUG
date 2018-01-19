package com.grupocisc.healthmonitor.Profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.grupocisc.healthmonitor.Home.activities.MainActivity;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.SharedPreferencesManager;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.login.activities.LoginAccountActivity;
import com.grupocisc.healthmonitor.login.activities.LoginBackPassword;
import com.squareup.picasso.Picasso;


public class ProfileDataActivity extends AppCompatActivity {

    TextView txt_name;
    TextView txt_last_name;
    TextView txt_email;
    TextView txt_sexo;
    TextView txt_fecha;
    TextView txt_altura;
    TextView txt_peso;
    TextView txt_tipo_diabetes;
    TextView txt_tipo_asma;
    TextView txt_estcivil;
    TextView txt_telefono;
    TextView txt_pais;
    private CardView card_change_pass;
    public String Nombre = "";
    public String Apellido = "";
    public String Email = "";
    public String Anio = "";
    public String Peso = "";
    public String TipoDiabetes = "";
    public String TipoAsma = "";
    public String Altura = "";
    public String Sexo = "";
    public String EstCivil = "";
    public String Telefono = "";
    public String Pais = "";
    private ImageView user_avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_datata_activity);

        Utils.SetStyleToolbarLogo(this);
        //setea el color de la imagen
        ImageView perfil_user = (ImageView) findViewById(R.id.img_icon);
        perfil_user.setColorFilter(perfil_user.getContext().getResources().getColor(R.color.btn_login), PorterDuff.Mode.SRC_ATOP);

        card_change_pass = (CardView) findViewById(R.id.card_change_pass);
        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_last_name = (TextView) findViewById(R.id.txt_last_name);
        txt_email = (TextView) findViewById(R.id.txt_email);
        txt_sexo = (TextView) findViewById(R.id.txt_sexo);
        txt_fecha = (TextView) findViewById(R.id.txt_fecha);
        txt_altura = (TextView) findViewById(R.id.txt_altura);
        txt_peso = (TextView) findViewById(R.id.txt_peso);
        txt_tipo_diabetes = (TextView) findViewById(R.id.txt_tipo_diabetes);
        txt_tipo_asma = (TextView) findViewById(R.id.txt_tipo_asma);
        txt_estcivil = (TextView) findViewById(R.id.txt_estcivil);
        txt_telefono = (TextView) findViewById(R.id.txt_telefono);
        txt_pais = (TextView) findViewById(R.id.txt_pais);
        user_avatar = (ImageView) findViewById(R.id.user_avatar);


        //OBTENER DATA DE PREFERENCIA
        if (Utils.getEmailFromPreference(this) != null)
            Email = Utils.getEmailFromPreference(this);
        if (Utils.getNombreFromPreference(this) != null)
            Nombre = Utils.getNombreFromPreference(this);
        if (Utils.getApellidoFromPreference(this) != null)
            Apellido = Utils.getApellidoFromPreference(this);
        if (Utils.getAnioFromPreference(this) != null)
            Anio = Utils.getAnioFromPreference(this);
        if (Utils.getPesoFromPreference(this) != null)
            Peso = Utils.getPesoFromPreference(this);
        if (Utils.getTipoDiabetesFromPreference(this) != null)
            TipoDiabetes = Utils.getTipoDiabetesFromPreference(this);
        if (Utils.getTipoAsmaFromPreference(this) != null)
            TipoAsma = Utils.getTipoAsmaFromPreference(this);
        if (Utils.getAlturaFromPreference(this) != null)
            Altura = Utils.getAlturaFromPreference(this);
        if (Utils.getSexoFromPreference(this) != null)
            Sexo = Utils.getSexoFromPreference(this);
        if (Utils.getEstCivilFromPreference(this) != null)
            EstCivil = Utils.getEstCivilFromPreference(this);
        if (Utils.getPaisFromPreference(this) != null)
            Pais = Utils.getPaisFromPreference(this);
        if (Utils.getTelefonoFromPreference(this) != null)
            Telefono = Utils.getTelefonoFromPreference(this);

        if (Utils.getPictureUriFromPreference(this) != null && !Utils.getPictureUriFromPreference(this).equals("")) {
            Picasso.with(this).load(Utils.getPictureUriFromPreference(this)).into(user_avatar);
        }

        if (Utils.getEmailFromPreference(this) != null) {
            //setear texto del layout
            String valores = Anio.toString().substring(2, 3);
            if (valores.equals("/")) {
                txt_fecha.setText(Anio);
            } else {
                String fecha = Anio.toString().substring(8, 10) + "/" + Anio.toString().substring(5, 7) + "/" + Anio.toString().substring(0, 4);
                txt_fecha.setText(fecha);
            }

            String sex = Sexo;
            if (sex.equals("M")) {
                Sexo = "MASCULINO";
            } else {
                Sexo = "FEMENINO";
            }
            txt_name.setText(Nombre);
            txt_last_name.setText(Apellido);
            txt_email.setText(Email);
            txt_sexo.setText(Sexo);
            txt_altura.setText(Altura + "m");
            txt_peso.setText(Peso + "kg");
            txt_tipo_diabetes.setText(TipoDiabetes);
            txt_tipo_asma.setText(TipoAsma);
            txt_estcivil.setText(EstCivil);
            txt_telefono.setText(Telefono);
            txt_pais.setText(Pais);
        }

        card_change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callChangePass();
            }
        });


    }

    //se ejecuta al seleccionar el icon back del toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            this.finish();
        return super.onOptionsItemSelected(item);
    }

    public void callChangePass() {
        Intent i = new Intent(ProfileDataActivity.this, LoginBackPassword.class);
        startActivity(i);
    }

}
