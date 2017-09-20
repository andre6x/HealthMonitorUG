package com.grupocisc.healthmonitor.login.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.Home.activities.MainActivity;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.SharedPreferencesManager;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IPaises;
import com.grupocisc.healthmonitor.entities.IRegistrePerson;
import com.grupocisc.healthmonitor.login.activities.LoginAccountActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginDataThreeFragment extends Fragment {
    String TAG = "LoginDataThreeFragment";
    RecyclerView recyclerView;
    CardView BtnContinuar , BtnAtras;
    View view;
    EditText edt_presion , edt_colesterol, edt_telefono;
    String NameGenero = "" , medidaAltura= "" , medidaPeso = "", sp_pais="",sp_estcivil;
    int idpais = 0, min_numero = 0;
    public ProgressDialog Dialog;
    private Call<IRegistrePerson.RegistroPersona> call_1;
    private IRegistrePerson.RegistroPersona mSaveUser;
  //  private IDoctorVinculado.DoctorVinculado mLoginUser; //CAMBIO

    private Call<IPaises.Paises> call_2;
    private IPaises.Paises rowsPaises;

    //INCLUDE
    private LinearLayout linear_loading , lyt_contenedor;
    private ProgressBar progress;
    private Button retry;

    Spinner spinnerEstCivil;
    String[] estCivil = new String[]{
            "Soltero",
            "Casado",
            "Viudo",
            "Divorciado",
            "En Unión de Hechos"
    };

    Spinner spinnerPais;
    String[] pais = new String[]{
            "Ecuador"
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.login_data_three_fragment, container, false);

        Dialog = new ProgressDialog(getActivity());

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        BtnContinuar = (CardView) view.findViewById(R.id.card_next);
        BtnAtras = (CardView) view.findViewById(R.id.card_atras);

//        edt_presion = (EditText) view.findViewById(R.id.edt_presion);
  //      edt_colesterol = (EditText) view.findViewById(R.id.edt_colesterol);
        /*edt_telefono = (EditText) view.findViewById(R.id.edt_telefono);
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
        });*/


        lyt_contenedor = (LinearLayout) view.findViewById(R.id.lyt_contenedor);
        linear_loading = (LinearLayout) view.findViewById(R.id.linear_loading);
        progress = (ProgressBar) view.findViewById(R.id.progress);
        retry = (Button) view.findViewById(R.id.retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                restartLoading(2);
            }
        });


        BtnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //llamar ametodo de lactivdad para reemplazar fragmento
                ((LoginAccountActivity)getActivity()).callFragmentDataTwo();

            }
        });
        setCampos();
        restartLoading(2);
        return view;
    }

    private void showLayout()
    { linear_loading.setVisibility(View.GONE);
      lyt_contenedor.setVisibility(View.VISIBLE);
    }

    private void showLoading()
    {   lyt_contenedor.setVisibility(View.GONE);
        linear_loading.setVisibility(View.VISIBLE);
        progress.setVisibility(View.VISIBLE);
        retry.setVisibility(View.GONE);
    }

    private void showRetry()
    {  lyt_contenedor.setVisibility(View.GONE);
        linear_loading.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
        retry.setVisibility(View.VISIBLE);
    }

    private void showLoadingDialog(){
        Dialog.setMessage("Espere un Momento..");
        Dialog.setCancelable(false);
        Dialog.show();
    }

    private void showLayoutDialog(){
        if (Dialog != null)
            Dialog.dismiss();
    }

    private void restartLoading(int index) {
        if(index == 2) {
            showLoading();
            restartLoadingPaises();
        }else if(index == 1){
          //
        }
    }

    private void restartLoadingPaises() {
        Log.e(TAG, "METODO consultadoc ");
        IPaises Cunsul = HealthMonitorApplicattion.getApplication().getRestCISCAdapterV2().create(IPaises.class);
        call_2 = Cunsul.getFromPaises();
        call_2.enqueue(new Callback<IPaises.Paises>() {
            @Override
            public void onResponse(Call< IPaises.Paises> call, Response<IPaises.Paises> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG, "Respuesta exitosa :: Paises");
                    Log.e(TAG, "Respuesta exitosa ::" + response);
                    rowsPaises = null;
                    rowsPaises = response.body() ;
                    postExecutionPaises();
                } else {
                    showRetry();
                    Utils.generarAlerta(getActivity(), getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
                    Log.e(TAG, "Error en la petición :: Paises");
                }
            }

            @Override
            public void onFailure(Call<IPaises.Paises> call, Throwable t) {
                showRetry();
                Utils.generarAlerta(getActivity(), getString(R.string.txt_atencion), getString(R.string.text_error_metodo ) + " o revise su conexión a internet.");
                t.printStackTrace();
               // Log.e(TAG, "Error en la petición onFailure ");
            }
        });
    }

    public void postExecutionPaises(){
        if (rowsPaises != null) {
            if (rowsPaises.getRows() != null ) {
                if ( rowsPaises.getRows().size() > 0) {
                    showLayout();
                    String[] arrayPaises = getPaises(rowsPaises.getRows());
                    setSpinner(arrayPaises);

                    BtnContinuar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //if(camposLlenos()) {
                                callGuardarData();
                                restarLoading();
                              //  SavePreferencesCallMainActivity();
                                //Toast.makeText(getActivity(), "GUARDAR EN PREFERENCES", Toast.LENGTH_SHORT).show();
                           // }

                        }
                    });
                }else
                    showRetry();
            }else
                showRetry();
        }else
            showRetry();
    }

    //llenar list string
    private String[] getPaises(List<IPaises.Rows> rowsPaises) {
        List<String> listPaises = new ArrayList<>();
        for (int i=0; i<rowsPaises.size();i++){
            String nombres = rowsPaises.get(i).getName();
            listPaises.add(nombres);
        }
        String[] array = new String[listPaises.size()];
        int j=0;
        for(String s: listPaises){
            array[j++] = s;
        }
        return array;
    }

    public void setSpinner(String[] arrayPaises){

        spinnerEstCivil = (Spinner) view.findViewById(R.id.spinnerEstCivil);
        ArrayAdapter<String> spinnerArrayAdapterEstCivil = new ArrayAdapter<String>(getActivity(), R.layout.custom_textview_to_spinner,estCivil);
        spinnerArrayAdapterEstCivil.setDropDownViewResource(R.layout.custom_textview_to_spinner);
        spinnerEstCivil.setAdapter(spinnerArrayAdapterEstCivil);
        spinnerEstCivil.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
                // TODO Auto-generated method stub
                //Toast.makeText(getActivity(), spinnerColorChange.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                sp_estcivil = spinnerEstCivil.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });



        spinnerPais = (Spinner) view.findViewById(R.id.spinnerPais);
        ArrayAdapter<String> spinnerArrayAdapterPais = new ArrayAdapter<String>(getActivity(), R.layout.custom_textview_to_spinner, arrayPaises);
        spinnerArrayAdapterPais.setDropDownViewResource(R.layout.custom_textview_to_spinner);
        spinnerPais.setAdapter(spinnerArrayAdapterPais);
        spinnerPais.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
                // TODO Auto-generated method stub
                //Toast.makeText(getActivity(), spinnerColorChange.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                sp_pais  = spinnerPais.getSelectedItem().toString();
               // if (sp_pais.equals("Ecuador")) { COMENTADO
                 //   sp_pais="1";  COMENTADO
               // }  COMENTADO
                idpais = rowsPaises.getRows().get(position).getCountryId();//CAMBIO
                Log.e(TAG, "select idPais: " + rowsPaises.getRows().get(position).getCountryId());
                Log.e(TAG, "select idPais: " +idpais);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    //guarda data del fragmento  en la actividad padre
    public void callGuardarData(){
        //setear datos al activity
        ((LoginAccountActivity)getActivity()).Pais = sp_pais;//spinnerPais.getSelectedItem().toString();
        ((LoginAccountActivity)getActivity()).EstCivil = spinnerEstCivil.getSelectedItem().toString();
        //((LoginAccountActivity)getActivity()).Telefono = edt_telefono.getText().toString();

    }

    //guardar en preferencias y volver a cargar el main
    private void SavePreferencesCallMainActivity() {

        SharedPreferencesManager.setValor(getActivity(), Utils.PREFERENCIA_USER, ((LoginAccountActivity)getActivity()).Email    , Utils.KEY_EMAIL);
        SharedPreferencesManager.setValor(getActivity(), Utils.PREFERENCIA_USER, ((LoginAccountActivity)getActivity()).Facebook_PictureUri    , Utils.KEY_PICTURE_URI);
        SharedPreferencesManager.setValor(getActivity(), Utils.PREFERENCIA_USER, ((LoginAccountActivity)getActivity()).Nombre   , Utils.KEY_NOMBRE);
        SharedPreferencesManager.setValor(getActivity(), Utils.PREFERENCIA_USER, ((LoginAccountActivity)getActivity()).Apellido , Utils.KEY_APELLIDO);
        SharedPreferencesManager.setValor(getActivity(), Utils.PREFERENCIA_USER, ((LoginAccountActivity)getActivity()).Anio     , Utils.KEY_ANIO);
        SharedPreferencesManager.setValor(getActivity(), Utils.PREFERENCIA_USER, ((LoginAccountActivity)getActivity()).Peso     , Utils.KEY_PESO);
        SharedPreferencesManager.setValor(getActivity(), Utils.PREFERENCIA_USER, ((LoginAccountActivity)getActivity()).Altura   , Utils.KEY_ALTURA);
        SharedPreferencesManager.setValor(getActivity(), Utils.PREFERENCIA_USER, ((LoginAccountActivity)getActivity()).Sexo     , Utils.KEY_SEXO);
        SharedPreferencesManager.setValor(getActivity(), Utils.PREFERENCIA_USER, ((LoginAccountActivity)getActivity()).Pais     , Utils.KEY_PAIS);
        SharedPreferencesManager.setValor(getActivity(), Utils.PREFERENCIA_USER, ((LoginAccountActivity)getActivity()).EstCivil , Utils.KEY_ESTCIVIL);
        SharedPreferencesManager.setValor(getActivity(), Utils.PREFERENCIA_USER, ((LoginAccountActivity)getActivity()).Telefono , Utils.KEY_TELEFONO);
        SharedPreferencesManager.setValor(getActivity(), Utils.PREFERENCIA_USER, ((LoginAccountActivity)getActivity()).IsAsma+"" , Utils.KEY_ASMA);

        //SharedPreferencesManager.setValor(getActivity(), Utils.PREFERENCIA_USER, ((LoginAccountActivity)getActivity()).TipoDiabetes, Utils.KEY_TIPO_DIABETES);
        //SharedPreferencesManager.setValor(getActivity(), Utils.PREFERENCIA_USER, mLoginUser.getIdDoctor()+""    , Utils.KEY_ID_DR);//CAMBIO
        //volver a crear el main
        Intent intent = new Intent(getActivity(), MainActivity.class);  // envia al main
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP); //para borrar pila de actividades
        getActivity().startActivity(intent);
    }


    public void  setCampos(){
        //edt_colesterol.setText( ((LoginAccountActivity)getActivity()).Colesterol );
        //edt_presion.setText( ((LoginAccountActivity)getActivity()).Presion );
   //     edt_telefono.setText( ((LoginAccountActivity)getActivity()).Telefono );
    }

/*
    private boolean camposLlenos() {
     /*  if (edt_presion.getText().toString() == null || edt_presion.getText().toString().length() == 0){
            generarAlerta("El campo PRESIÓN es obligatorio");
            return false;
        }
        else if (edt_colesterol.getText().toString() == null || edt_colesterol.getText().toString().length() == 0){
            generarAlerta("El campo COLESTEROL es obligatorio");
            return false;
        }
        else
        if (edt_telefono.getText().toString() == null || edt_telefono.getText().toString().length() == 0){
            generarAlerta("El campo NÚMERO es obligatorio");
            return false;
        }else{
            min_numero = Integer.parseInt((getString(R.string.minimo_telefono)));
            if (edt_telefono.length() < min_numero ){
                edt_telefono.setError("El número no puede tener menos de "+ min_numero +" dígitos. Por favor Verifique");
            }
        }

        return true;

    }*/

    private void generarAlerta(String mensaje){
        Utils.generarAlerta(getActivity(), getString(R.string.txt_error), mensaje);
    }

    public void restarLoading(){
        showLoadingDialog();
        restartLoadingEnviarData();
    }

    private void restartLoadingEnviarData( ){
        //obtener todos los datos del activity
        String name   = ((LoginAccountActivity)getActivity()).Nombre   ;
        String lastName = ((LoginAccountActivity)getActivity()).Apellido ;
        String email    = ((LoginAccountActivity)getActivity()).Email    ;
        String password   = ((LoginAccountActivity)getActivity()).Pass  ;
        String fechaNacimiento   =((LoginAccountActivity)getActivity()).Anio     ;
        String birthDate = fechaNacimiento.substring(6,10)+"/"+fechaNacimiento.substring(3,5)+"/"+fechaNacimiento.substring(0,2);
        String identifier = "09"; //((LoginAccountActivity)getActivity()).Cedula;
        float weight   =  Float.parseFloat(((LoginAccountActivity)getActivity()).Peso);
        float height = Float.parseFloat(((LoginAccountActivity)getActivity()).Altura )  ;
        String gender   = ((LoginAccountActivity)getActivity()).Sexo  ;
        String relationshipStatus   =  ((LoginAccountActivity)getActivity()).EstCivil ;
        String cellPhone = ((LoginAccountActivity)getActivity()).Telefono ;
        int countryId   =  idpais; //Integer.parseInt(((LoginAccountActivity)getActivity()).Pais) ;
        int diabetesType   =  ((LoginAccountActivity)getActivity()).TipoDiabetes  ;
        int asma ;
        if(((LoginAccountActivity)getActivity()).IsAsma ){
            asma = 1;
        }else{
            asma = 0;
        }

        String url         = "www.noImage.com";
        if( ! ((LoginAccountActivity)getActivity()).Facebook_PictureUri.equals("") ) {
            url = ((LoginAccountActivity)getActivity()).Facebook_PictureUri  ;//imagen de facebook
        }

        //APUNTANDO AA METODO CISC
        IRegistrePerson suscripcion = HealthMonitorApplicattion.getApplication().getRestCISCAdapterV2().create(IRegistrePerson.class);
        call_1 = suscripcion.RegistroPersona1(new IRegistrePerson.ObjDataUserLogin(name,lastName,email,password,birthDate,identifier,weight,height,gender,relationshipStatus,cellPhone,countryId,diabetesType,asma,url));
        call_1.enqueue(new Callback<IRegistrePerson.RegistroPersona>() {
            @Override
            public void onResponse(Call<IRegistrePerson.RegistroPersona> call, Response<IRegistrePerson.RegistroPersona> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG, "Respuesta exitosa");
                    mSaveUser = null;
                    mSaveUser = response.body();
                    postExecutionEnviarData();
                } else {
                    showLayoutDialog();
                    Utils.generarAlerta(getActivity(), getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
                    Log.e(TAG, "Error en la petición onResponse");
                }
            }

            @Override
            public void onFailure(Call<IRegistrePerson.RegistroPersona> call, Throwable t) {
                showLayoutDialog();
                Utils.generarAlerta(getActivity(), getString(R.string.txt_atencion), getString(R.string.text_error_metodo) + " o revise su conexión a internet");
                t.printStackTrace();
            }
        });
    }

    public void postExecutionEnviarData(){
        showLayoutDialog();

        if (mSaveUser != null) {
            if (mSaveUser.getIdCodResult() == 0 ) {
                //oka
                //Log.e(TAG,"postExecutionEnviarData; "+ mSaveUser.getCodigo() );
                SavePreferencesCallMainActivity();
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
    private void DeletePreferencesCallMainActivity(){
        SharedPreferencesManager.setValor(getActivity(), Utils.PREFERENCIA_USER, null , Utils.KEY_EMAIL);
        SharedPreferencesManager.setValor(getActivity(), Utils.PREFERENCIA_USER, null , Utils.KEY_PICTURE_URI);
        SharedPreferencesManager.setValor(getActivity(), Utils.PREFERENCIA_USER, null , Utils.KEY_NOMBRE);
        SharedPreferencesManager.setValor(getActivity(), Utils.PREFERENCIA_USER, null , Utils.KEY_APELLIDO);
        SharedPreferencesManager.setValor(getActivity(), Utils.PREFERENCIA_USER, null , Utils.KEY_ANIO);
        SharedPreferencesManager.setValor(getActivity(), Utils.PREFERENCIA_USER, null , Utils.KEY_PESO);
        SharedPreferencesManager.setValor(getActivity(), Utils.PREFERENCIA_USER, null , Utils.KEY_ALTURA);
        SharedPreferencesManager.setValor(getActivity(), Utils.PREFERENCIA_USER, null , Utils.KEY_SEXO);
        SharedPreferencesManager.setValor(getActivity(), Utils.PREFERENCIA_USER, null , Utils.KEY_PAIS);
        SharedPreferencesManager.setValor(getActivity(), Utils.PREFERENCIA_USER, null , Utils.KEY_ESTCIVIL);
        SharedPreferencesManager.setValor(getActivity(), Utils.PREFERENCIA_USER, null , Utils.KEY_TELEFONO);
    }
}
