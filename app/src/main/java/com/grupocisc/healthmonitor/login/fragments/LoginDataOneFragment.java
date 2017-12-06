package com.grupocisc.healthmonitor.login.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.Insulin.TextValidator;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.SharedPreferencesManager;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IV2ConsulNum;
import com.grupocisc.healthmonitor.login.activities.LoginAccountActivity;
import com.squareup.picasso.Picasso;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;


public class LoginDataOneFragment extends Fragment {

    RecyclerView recyclerView;
    CardView BtnContinuar;
    View view;
    private EditText txt_nombre;
    private EditText txt_apellido;
    private EditText email ;
    private EditText pass_1 , pass_2;
    private EditText edt_mail;
    EditText edt_telefono;
    int idpais = 0, min_numero = 0;
    public ProgressDialog Dialog;
    private ImageView user_avatar;

    //private Call<IConsulNum.ConsultaNum> call_3;
    //private IConsulNum.ConsultaNum mSaveUser;
    private Call<IV2ConsulNum.ConsulNum> call_3;
    private IV2ConsulNum.ConsulNum mSaveUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.login_data_one_fragment, container, false);
        Dialog = new ProgressDialog(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        BtnContinuar = (CardView) view.findViewById(R.id.card_next);
        user_avatar = (ImageView) view.findViewById(R.id.user_avatar);
        txt_nombre = (EditText) view.findViewById(R.id.txt_nombre);
        txt_apellido = (EditText) view.findViewById(R.id.txt_apellido);
        edt_mail = (EditText) view.findViewById(R.id.email);
        edt_telefono = (EditText) view.findViewById(R.id.edt_telefono);
        pass_1 = (EditText) view.findViewById(R.id.pass_1);
        pass_2 = (EditText) view.findViewById(R.id.pass_2);


        setCampos();

        edt_telefono = (EditText) view.findViewById(R.id.edt_telefono);
        min_numero = Integer.parseInt((getString(R.string.minimo_telefono)));
        edt_telefono.addTextChangedListener(new TextValidator(edt_telefono) {
            @Override
            public void validate(EditText editText, String text) {
                if (!text.isEmpty()) {
                    if (edt_telefono.length() < min_numero ){
                        edt_telefono.setError("El número no puede tener menos de "+ min_numero +" dígitos. Por favor Verifique");
                    }
                }
            }
        });

        BtnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String ls_mail = edt_mail.getText().toString();
                String ls_pass = pass_1.getText().toString();
                String ls_number = edt_telefono.getText().toString();
                 if(camposLlenos()) {
                     if (isEmailValid(ls_mail)== true && isPasswordValid(ls_pass) == true && isNumberValid(ls_number)==true)  {
                        callGuardarData();
                        restarLoading();
                     }else{
                         if (isEmailValid(ls_mail)== false){
                             generarAlerta("El EMAIL es inválido");
                         }else {
                             if (isPasswordValid(ls_pass) == false) {
                                 generarAlerta("Password debe tener mínimo 6 caracteres");
                             }
                         }
                     }
                }
            }
        });

        return view;
    }


    public void restarLoading(){
        showLoadingDialog();
        restartLoadingEnviarData();
    }

    private void showLoadingDialog(){
        Dialog.setMessage("Espere un Momento..");
        Dialog.setCancelable(false);
        Dialog.show();
    }

    private void restartLoadingEnviarData( ){
        //obtener todos los datos del activity
        String email    = ((LoginAccountActivity)getActivity()).Email    ;
        String telefono   =   ((LoginAccountActivity)getActivity()).Telefono  ;

        Log.e(TAG,"restartLoadingEnviarData telefono: "+telefono);
        //APUNTANDO AA METODO CISC

        IV2ConsulNum consulta = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(IV2ConsulNum.class);
        call_3 = consulta.ConsultaNumero( new IV2ConsulNum.ObjUserNum(email,telefono) );
        call_3.enqueue(new Callback<IV2ConsulNum.ConsulNum>() {
            @Override
            public void onResponse(Call<IV2ConsulNum.ConsulNum> call, Response<IV2ConsulNum.ConsulNum> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG, "Respuesta exitosa");
                    mSaveUser = null;
                    mSaveUser = response.body();
                    Log.e(TAG,"OnResponse: "+ mSaveUser.getResultDescription()  );
                    postExecutionEnviarData();
                } else {
                    showLayoutDialog();
                    Utils.generarAlerta(getActivity(), getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
                    Log.e(TAG, "Error en la petición onResponse");
                }
            }
            @Override
            public void onFailure(Call<IV2ConsulNum.ConsulNum> call, Throwable t) {
                showLayoutDialog();
                Utils.generarAlerta(getActivity(), getString(R.string.txt_atencion), getString(R.string.text_error_metodo) + " o revise su conexión a internet");
                t.printStackTrace();
            }
        });

    }

    private void showLayoutDialog(){
        if (Dialog != null)
            Dialog.dismiss();
    }


    public void postExecutionEnviarData(){
        showLayoutDialog();

        if (mSaveUser != null) {
            if (mSaveUser.getIdCodResult() == 0 ) {
                //oka
                Log.e(TAG,"postExecutionEnviarData= "+ mSaveUser.getIdCodResult() );
                //SavePreferencesCallMainActivity();
                callNextFragment();
            }else {
                Utils.generarAlerta( getContext(), getString(R.string.txt_atencion),   mSaveUser.getResultDescription() );
                Log.e(TAG,getString(R.string.txt_atencion) +"; "+ mSaveUser.getResultDescription() );
                Log.e(TAG,"mSaveUser: !=0 : "+ mSaveUser.getIdCodResult()  );
                DeletePreferencesCallMainActivity();
            }
        }else {
            Utils.generarAlerta(getActivity(), getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
            Log.e(TAG,"mSaveUser: is null: "+ mSaveUser.getIdCodResult()  );
            DeletePreferencesCallMainActivity();
        }
    }

    //eliminar preferencias de usuario
    private void DeletePreferencesCallMainActivity() {
        SharedPreferencesManager.setValor(getActivity(), Utils.PREFERENCIA_USER, null, Utils.KEY_EMAIL);
        SharedPreferencesManager.setValor(getActivity(), Utils.PREFERENCIA_USER, null, Utils.KEY_TELEFONO);
    }
    public void callGuardarData(){
        //setear datos al activity
        ((LoginAccountActivity)getActivity()).Nombre = txt_nombre.getText().toString();
        ((LoginAccountActivity)getActivity()).Apellido = txt_apellido.getText().toString();
        ((LoginAccountActivity)getActivity()).Email = edt_mail.getText().toString();
        ((LoginAccountActivity)getActivity()).Pass = pass_1.getText().toString();
        ((LoginAccountActivity)getActivity()).Telefono = edt_telefono.getText().toString();

    }
    public void callNextFragment(){

        Log.e(TAG,"callNextFragment  ");
                //llamar ametodo de lactivdad para reemplazar fragmento
        ((LoginAccountActivity) getActivity()).callFragmentDataTwo();
    }

    //si regresa del fragmento2  y existen campos llenos los setea
    public void  setCampos (){
        if( ! ((LoginAccountActivity)getActivity()).Facebook_PictureUri.equals("") ) {
            Picasso.with(getActivity()).load(((LoginAccountActivity) getActivity()).Facebook_PictureUri).into(user_avatar);
        }
        txt_nombre.setText(((LoginAccountActivity)getActivity()).Nombre );
        txt_apellido.setText(((LoginAccountActivity)getActivity()).Apellido );
        edt_mail.setText( ((LoginAccountActivity)getActivity()).Email );
        edt_telefono.setText(((LoginAccountActivity)getActivity()).Telefono );
        pass_1.setText(((LoginAccountActivity)getActivity()).Pass );
        pass_2.setText(((LoginAccountActivity)getActivity()).Pass );
    }

    private boolean camposLlenos() {
        if (txt_nombre.getText().toString() == null || txt_nombre.getText().toString().length() == 0){
            generarAlerta("El campo NOMBRE es obligatorio");
            return false;
        }
        else if (txt_apellido.getText().toString() == null || txt_apellido.getText().toString().length() == 0){
            generarAlerta("El campo APELLIDO es obligatorio");
            return false;
        }
        else if (edt_mail.getText().toString() == null || edt_mail.getText().toString().length() == 0){
            generarAlerta("El campo EMAIL es obligatorio");
            return false;
        }else if (pass_1.getText().toString() == null || pass_1.getText().toString().length() == 0 ||
                  pass_2.getText().toString() == null || pass_2.getText().toString().length() == 0){
            generarAlerta("El campo CONTRASEÑA es obligatorio");
            return false;
        }else if (! pass_1.getText().toString().equals( pass_2.getText().toString() )  ){
            generarAlerta("La CONTRASEÑA no coincide");
            return false;
        }else if (edt_telefono.getText().toString() == null || edt_telefono.getText().toString().length() == 0){
            generarAlerta("El campo NÚMERO es obligatorio");
            return false;
        }else{
            min_numero = Integer.parseInt((getString(R.string.minimo_telefono)));
            if (edt_telefono.length() < min_numero ){
                //edt_telefono.setError("El número no puede tener menos de "+ min_numero +" dígitos. Por favor Verifique");
                generarAlerta("El campo NÚMERO es inválido");
                return false;

            }
        }


        return true;
    }
    private void generarAlerta(String mensaje){
        Utils.generarAlerta(getActivity(), getString(R.string.txt_error), mensaje);
    }


    public static boolean isEmailValid(String ls_mail) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = ls_mail;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }


    private boolean isPasswordValid(String ls_pass) {
        /**Si la cadena supera los 5 caracteres es una contraseña valida*/
        boolean isValid = false;
        if (ls_pass.length() > 5)
        {isValid = true; }
        return isValid;
    }

    private boolean isNumberValid(String ls_number) {
        /**Si la cadena supera los 10 caracteres es una contraseña valida*/
        boolean isValid = false;
        if (ls_number.length() > 9)
        {isValid = true; }
        return isValid;
    }


}


