package com.grupocisc.healthmonitor.Profile;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.login.activities.LoginBackPassword;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ProfileDataActivity extends AppCompatActivity {

    @BindView(R.id.txt_name) TextView txt_name;
    @BindView(R.id.txt_last_name) TextView txt_last_name;
    @BindView(R.id.txt_email) EditText txt_email;
    @BindView(R.id.txt_sexo) Spinner txt_sexo;
    @BindView(R.id.spinnerDiabetes) Spinner spinnerDiabetes;
    @BindView(R.id.txt_fecha) TextView txt_fecha;
    @BindView(R.id.txt_altura) EditText txt_altura;
    @BindView(R.id.txt_peso) TextView txt_peso;
    @BindView(R.id.chkAsma) CheckBox chk_tipo_asma;
    @BindView(R.id.txt_estcivil) Spinner txt_estcivil;
    @BindView(R.id.txt_telefono) EditText txt_telefono;
    @BindView(R.id.txt_pais) TextView txt_pais;
    @BindView(R.id.editionBtn) ImageView editionButton;
    @BindView(R.id.card_change_pass) CardView card_change_pass;
    @BindView(R.id.card_update_data) CardView card_update_data;

    boolean _isEnabled=false;
    public String name = "";
    public String _lastName = "";
    public String Email = "";
    public String year = "";
    public String Peso = "";
    public String TipoDiabetesP = "";
    public String TipoAsma = "";
    public String Altura = "";
    public String Sexo = "";
    public String EstCivil = "";
    public String Telefono = "";
    public String Pais = "";
    private String enviaSexo = "";
    private ImageView user_avatar;
    //String  tdiabetes="";
    int idTipoDiabetes=0;
    int tieneAsma=0;
    //Spinner spinnerDiabetes;
    String[] diabetesType = new String[]{
            "Tipo 1",
            "Tipo 2",
            "Gestacional",
            "Sin diabetes"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_datata_activity);

        ButterKnife.bind(this);

        Utils.SetStyleToolbarLogo(this);
        //setea el color de la imagen
        ImageView perfil_user = (ImageView) findViewById(R.id.img_icon);
        perfil_user.setColorFilter(perfil_user.getContext().getResources().getColor(R.color.btn_login), PorterDuff.Mode.SRC_ATOP);

        txt_sexo.setEnabled(_isEnabled);
        txt_estcivil.setEnabled(_isEnabled);
        spinnerDiabetes.setEnabled(_isEnabled);


        //OBTENER DATA DE PREFERENCIA
        if (Utils.getEmailFromPreference(this) != null)
            Email = Utils.getEmailFromPreference(this);
        if (Utils.getNombreFromPreference(this) != null)
            name = Utils.getNombreFromPreference(this);
        if (Utils.getApellidoFromPreference(this) != null)
            _lastName = Utils.getApellidoFromPreference(this);
        if (Utils.getAnioFromPreference(this) != null)
            year = Utils.getAnioFromPreference(this);
        if (Utils.getPesoFromPreference(this) != null)
            Peso = Utils.getPesoFromPreference(this);
        if (Utils.getTipoDiabetesFromPreference(this) != null)
            TipoDiabetesP = Utils.getTipoDiabetesFromPreference(this);
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
            String dateValues = year.substring(2, 3);
            if (dateValues.equals("/")) {
                txt_fecha.setText(year);
            } else {
                String date = year.substring(8, 10) + "/" + year.substring(5, 7) + "/" + year.substring(0, 4);
                txt_fecha.setText(date);
            }

            String sex = Sexo;
            if (sex.equals("M")) {
                Sexo = "MASCULINO";
                txt_sexo.setSelection(0);
            } else {
                Sexo = "FEMENINO";
                txt_sexo.setSelection(1);
            }
            txt_name.setText(name);
            txt_last_name.setText(_lastName);
            txt_email.setText(Email);
            txt_altura.setText(Altura);
            txt_peso.setText(Peso + " kg");
            chk_tipo_asma.setChecked(false);
            if(TipoAsma.equals("true"))
                chk_tipo_asma.setChecked(true);

            if(EstCivil.equals("Soltero")){
                txt_estcivil.setSelection(0);
            }else if(EstCivil.equals("Casado")){
                txt_estcivil.setSelection(1);
            }else if(EstCivil.equals("Viudo")){
                txt_estcivil.setSelection(2);
            }else if(EstCivil.equals("Divorciado")){
                txt_estcivil.setSelection(3);
            }else if(EstCivil.equals("En Unión de Hechos")){
                txt_estcivil.setSelection(4);
            }
            txt_telefono.setText(Telefono);
            txt_pais.setText(Pais);
        }

        setSpinner();

        if(TipoDiabetesP.equals("11")){
            spinnerDiabetes.setSelection(0);
        }else if(TipoDiabetesP.equals("12")){
            spinnerDiabetes.setSelection(1);
        }else if(TipoDiabetesP.equals("13")){
            spinnerDiabetes.setSelection(2);
        }else if(TipoDiabetesP.equals("14")){
            spinnerDiabetes.setSelection(3);
        }
    }

    //se ejecuta al seleccionar el icon back del toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            this.finish();
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.card_change_pass)
    public void callChangePass() {
        Intent i = new Intent(ProfileDataActivity.this, LoginBackPassword.class);
        startActivity(i);
    }

    public void setSpinner(){
        ArrayAdapter<String> spinnerArrayAdapterDiabetes = new ArrayAdapter<String>(this, R.layout.custom_textview_to_spinner, diabetesType);
        spinnerArrayAdapterDiabetes.setDropDownViewResource(R.layout.custom_textview_to_spinner);
        spinnerDiabetes.setAdapter(spinnerArrayAdapterDiabetes);
    }

    @OnClick(R.id.card_update_data)
    public void actualizaDatos(){
        String msg = "";
        if(this.txt_email.getText().toString().trim().isEmpty()){
            msg = "Debe proporcionar una dirección email como cuenta de usuario";
            Utils.generarSweetAlertDialogError(ProfileDataActivity.this, "HealthMonitorUG", msg);
        }else if(this.txt_altura.getText().toString().trim().isEmpty()){
            msg = "Debe proporcionar su dato de altura";
            Utils.generarSweetAlertDialogError(ProfileDataActivity.this, "HealthMonitorUG", msg);
        }else if(this.txt_telefono.getText().toString().trim().isEmpty()){
            msg = "Debe proporcionar un número de teléfono o celular";
            Utils.generarSweetAlertDialogError(ProfileDataActivity.this, "HealthMonitorUG", msg);
        }else{
            getIdSexo();
            getIdTipoDiabetes();
            getTieneAsma();

            //Variables para enviarla al sevidor
            /*
            Email --> txt_email.getText().toString().trim()
            Sexo --> enviaSexo
            Altura --> txt_altura.getText().toString().trim()
            Tipo de diabetes --> idTipoDiabetes
            Asma --> tieneAsma
            Estado civil --> txt_estcivil.getSelectedItem().toString().trim()
            Telefono --> txt_telefono.getText().toString().trim()
            */
            Utils.generarAlerta(ProfileDataActivity.this, "HealthMonitorUG", "Datos actualizados");
        }
    }

    private void getIdTipoDiabetes(){
        if(this.spinnerDiabetes.getSelectedItemPosition() == 0) {
            idTipoDiabetes = 11;
        }else if(this.spinnerDiabetes.getSelectedItemPosition() == 1) {
            idTipoDiabetes = 12;
        }
        else if(this.spinnerDiabetes.getSelectedItemPosition() == 2) {
            idTipoDiabetes = 13;
        }
        else { //Sin diabetes
            idTipoDiabetes = 14;
        }
    }

    private void getIdSexo(){
        if(this.txt_sexo.getSelectedItemPosition()==0){
            enviaSexo ="M";
        }else{
            enviaSexo = "F";
        }
    }

    private void getTieneAsma(){
        if(this.chk_tipo_asma.isChecked()){
            tieneAsma = 1;
        }else{
            tieneAsma = 0;
        }
    }

    //@SuppressLint("ResourceAsColor")

    @OnClick(R.id.editionBtn)
    public void habilitaCampos(){
        _isEnabled = !_isEnabled;
        //txt_email.setEnabled(_isEnabled);
        txt_altura.setEnabled(_isEnabled);
        txt_telefono.setEnabled(_isEnabled);
        txt_sexo.setEnabled(_isEnabled);
        txt_estcivil.setEnabled(_isEnabled);
        spinnerDiabetes.setEnabled(_isEnabled);
        chk_tipo_asma.setEnabled(_isEnabled);

        if(_isEnabled){
            //txt_email.setBackgroundResource(R.color.silver_fondo);
            txt_altura.setBackgroundResource(R.color.silver_fondo);
            txt_telefono.setBackgroundResource(R.color.silver_fondo);
        }
        else {
            //txt_email.setBackgroundResource(R.color.transparent);
            txt_altura.setBackgroundResource(R.color.transparent);
            txt_telefono.setBackgroundResource(R.color.transparent);
        }
    }
}