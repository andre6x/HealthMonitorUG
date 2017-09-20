package com.grupocisc.healthmonitor.Pressure.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.Pressure.activities.PressureActivity;
import com.grupocisc.healthmonitor.R;
//import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IRecomenPressure;



//25022017
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Mariuxi on 21/02/2017.
 */

public class PressureRecomendFragment extends Fragment {
   /* private String TAG = "PressureRecomendFragment";
    private static IRecomenPressure.Objeto rowsIRecomPressure;

    private TextView txt_maxpressure;
    private TextView txt_minpressure;
    private TextView txt_resultado;
    private String maxpressure ;
    private String minpressure ;

    public ProgressDialog Dialog;
    private Call<List<IRecomenPressure.Objeto>> call_1;
    //private int year1, month1, day1, year2, month2, day2;
    //private String sexo ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Dialog = new ProgressDialog(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.pessure_recomend_fragment, null);
        //View contenView = inflater.inflate(R.layout.weight_imc_activity, container, false);
        txt_maxpressure = (TextView)  root.findViewById(R.id.txt_maxpressure);
        txt_minpressure = (TextView)  root.findViewById(R.id.txt_minpressure);
        txt_resultado = (TextView)  root.findViewById(R.id.txt_resultado);
        //txt_sexo = (TextView)  root.findViewById(R.id.txt_sexo);
        //txt_pesoideal = (TextView)  root.findViewById(R.id.txt_peso_ideal);
        //txt_diferencia = (TextView)  root.findViewById(R.id.txt_diferencia);
        //btnCalcular = (Button)  root.findViewById(R.id.btBuscar);
        maxpressure= "160";
        minpressure = "90";
        //sexo= "Femenino";
        txt_maxpressure.setText(maxpressure);
        txt_minpressure.setText(minpressure);
        //txt_sexo.setText(sexo);
        // txt_altura.setText(altura);
         double maxpressure1 =Double.parseDouble(txt_maxpressure.getText().toString());
         double minpressure1 =Double.parseDouble(txt_minpressure.getText().toString());

       /* if(maxpressure1 >=50 && maxpressure1<90 && minpressure1 >=35 && minpressure1<60){
            txt_resultado.setText("SU P.A ESTA MUY BAJA, ¡¡PRESENTA HIPOTENSIÓN!!");
           // txt_alerta.setText("¡CUIDA TU SALUD !:(");

        }else {
            if (maxpressure1 >=90 && maxpressure1<100 && minpressure1 >=60 && minpressure1<70) {
                txt_resultado.setText("SU P.A ESTA LEVEMENTE BAJA");
                //txt_alerta.setText("¡CUIDA TU SALUD !:(");
            } else {
                if (maxpressure1 >=100 && maxpressure1<130 && minpressure1 >=70 && minpressure1<85) {
                    txt_resultado.setText("SU P.A ESTA NORMAL");
                    //txt_alerta.setText("¡CUIDA TU SALUD !:(");
                } else {
                    if (maxpressure1 >=130 && maxpressure1<140 && minpressure1 >=85 && minpressure1<90) {
                        txt_resultado.setText("SU P.A ESTA LEVEMENTE ALTA ");
                        //txt_alerta.setText("¡CUIDADO! :(");
                    } else {
                        if (maxpressure1 >=140 && maxpressure1<160 && minpressure1 >=90 && minpressure1<110) {
                            txt_resultado.setText("SU P.A ESTA MODERADAMENTE ALTA");
                           // txt_alerta.setText("¡¡FELICIDADES!! :D");
                        } else {
                            if (maxpressure1 >=160 && maxpressure1<=230 && minpressure1 >=110 && minpressure1<=135) {
                                txt_resultado.setText("SU P.A ESTA MUY ALTA, ¡¡PRESENTA HIPERTENSIÓN!!");
                               // txt_alerta.setText("CUIDADO :|");
                            }
                        }


                    }
                }
            }
        }*/


   /*     String user_email    = Utils.getEmailFromPreference(getActivity()) ;
        IRecomenPressure RecomenPressure = HealthMonitorApplicattion.getApplication().getmRestCISCAdapterP().create(IRecomenPressure.class);
        call_1 = RecomenPressure.RecomenPressure(user_email);
        call_1.enqueue(new Callback<List<IRecomenPressure.Objeto>>() {
            @Override
            public void onResponse(Call<List<IRecomenPressure.Objeto>> call, Response<List<IRecomenPressure.Objeto>> response) {
                if (response.isSuccess()) {
                    //Log.e(TAG, "Respuesta exitosa");
                    rowsIRecomPressure = null;
                    rowsIRecomPressure = response.body();
                    if (rowsIRecomPressure != null) {
                        txt_resultado.setText(rowsIRecomPressure.get(1).getRecomendacion());
                    }

                    //postExecutionLogin();
                } else {
                    showLayoutDialog();
                    Utils.generarAlerta(getActivity(), getString(R.string.txt_atencion), getString(R.string.text_error_metodo)+"Error conexion");
                   // Log.e(TAG, "Error en la petición");
                }
            }

            @Override
            public void onFailure(Call<List<IRecomenPressure.Objeto>> call, Throwable t) {
                showLayoutDialog();
                Utils.generarAlerta(getActivity(), getString(R.string.txt_atencion), getString(R.string.text_error_metodo ) + " o revise su conexión a internet");
                t.printStackTrace();
                //Log.e(TAG, "mmmmmmmmmmmm");
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Log.e(TAG,"onResume");

        //selectDataWeightDB();
    }



    public void restarLoading(){
        showLoadingDialog();
        //restartLoadingEnviarData();
    }

    public void postExecutionLogin(){
        showLayoutDialog();

        if (rowsIRecomPressure != null) {
            //Log.e(TAG, "ooooook" + rowsIRecomPressure.size());
           // if (rowsIRecomPressure.size() > 0)
           // { callsetAdapter();}

        }


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


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {
            if (isVisibleToUser) {
                PressureActivity.fab.setVisibility(View.GONE);
            } else {
                PressureActivity.fab.setVisibility(View.VISIBLE);
            }
        }
    }*/

    }




