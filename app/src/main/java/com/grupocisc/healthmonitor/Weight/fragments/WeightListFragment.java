package com.grupocisc.healthmonitor.Weight.fragments;

/**
 * Created by Mariuxi on 12/01/2017.
 */
import android.app.ProgressDialog;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grupocisc.healthmonitor.Weight.activities.WeightActivity;
import com.grupocisc.healthmonitor.Weight.activities.WeightRegistyActivity;
import com.grupocisc.healthmonitor.Weight.adapters.WeightListAdapter;
import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IWeight;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import com.grupocisc.healthmonitor.entities.IConculPeso;
import java.util.Calendar;

//25022017
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeightListFragment extends Fragment implements WeightListAdapter.MyViewHolder.ClickListener {

    private String TAG = "WeightListFragment";

    private RecyclerView recyclerView;
    private WeightListAdapter adapter;
    //25022017
    public ProgressDialog Dialog;
    private static List<IWeight> rowsWeight;
    View contenView;
    private TextView txt_Imc;
    private TextView txt_tipoPeso , txt_peso_ideal;
    private  float PesoActual = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contenView = inflater.inflate(R.layout.weight_registre_fragment, container, false);
        recyclerView = (RecyclerView) contenView.findViewById(R.id.recycler_view);

        txt_Imc = (TextView)  contenView.findViewById(R.id.txt_imc);
        txt_tipoPeso = (TextView)  contenView.findViewById(R.id.txt_resultado);
        txt_peso_ideal = (TextView)  contenView.findViewById(R.id.txt_peso_ideal);

        setImcCalculate();
        return contenView;
    }

    public void setImcCalculate(){
        float peso =  0 ;
        float Altura = 0;
        float Imc = 0;
        if(PesoActual > 0 ){
            peso = PesoActual;
        }else{
            if(Utils.getPesoFromPreference(getActivity()) != null )
                peso  =  Float.parseFloat( Utils.getPesoFromPreference(getActivity()) ) ;
        }

        if (Utils.getAlturaFromPreference(getActivity()) != null)
            Altura = Float.parseFloat(Utils.getAlturaFromPreference(getActivity()));

        Imc = peso / (Altura * Altura);
        Log.e(TAG, "CALCULO IIMC:"+ Imc  +"--" + Altura +"--"+ peso );
        String numberAsString = String.format ("%.2f", Imc);
        txt_Imc.setText(numberAsString+"kg/m²");
        txt_tipoPeso.setText( tipoImc(Imc) );


        //PESO IDEAL
        String Sexo = "";
        double pesoIdeal = 0.00;
        if (Utils.getSexoFromPreference(getActivity()) != null)
            Sexo = Utils.getSexoFromPreference(getActivity());

        if (Sexo.equals("M")) {//MASCULINO
            pesoIdeal =  22.1 * (Altura * Altura);
        } else {//FEMENINO
            pesoIdeal =  20.6 * (Altura * Altura);
        }
        String numberAsString2 = String.format ("%.2f", pesoIdeal);
        txt_peso_ideal.setText(numberAsString2 +"Kg" );

    }

    public String tipoImc(float imc){
        String param = "";
        if(imc < 16 ){// = IMC inferior a 16 kg/m².
            param = "Bajo peso muy grave";
        }else if(imc >= 16 && imc <= 16.99 ){//= IMC entre 16 y 16.99 kg/m².
            param = "Bajo peso grave";
        }else if(imc >= 17 && imc <= 18.49 ){// IMC entre 17 y 18.49 kg/m².
            param = "Bajo peso";
        }else if(imc >= 18.50 && imc <= 24.99 ){//= IMC entre 18,50 y 24,99 kg/m².
            param = "Peso normal ";
        }else if(imc >= 25 && imc <= 29.99 ){//= IMC entre 25 y 29,99 kg/m².
            param = "Sobrepeso";
        }else if(imc >= 30 && imc <= 34.99 ){//índice de masa corporal entre 30 y 34,99 kg/m².
            param = "Obesidad grado I ";
        }else if(imc >= 35 && imc <= 39.99 ){//= IMC entre 35 y 39,99 kg/m².
            param = "Obesidad grado II ";
        }else if(imc >= 40  ){ //IMC superior a 40 kg/m².
            param = "Obesidad grado III (obesidad mórbida)";
        }
        return param;
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG,"onResume");
        selectDataWeightDB();
    }

    //obtener datos de la tabla BD
    public  void selectDataWeightDB(){
        Log.e(TAG,"selectDataWeightDB");
        try {
            //validar si en la tabla ahi datos mayor a 0
            if (Utils.GetWeightFromDatabase(HealthMonitorApplicattion.getApplication().getWeightDao() ).size() > 0 ){
                //asignamos datos de la tabla a la lista de objeto
                rowsWeight = Utils.GetWeightFromDatabase(HealthMonitorApplicattion.getApplication().getWeightDao() );
                //obtenemos el tamaño de la listaDEobjetos , para recorrerlo
                //y presenta los datos de la tabla bd en el LOG
                int tamaño = rowsWeight.size();
                for(int i = 0 ; i < tamaño ; i++){
                    Log.e(TAG,"Peso:" + rowsWeight.get(i).getPeso() +" -fecha:"+rowsWeight.get(i).getFecha() );//se cambia concentracion por peso
                }
                PesoActual = rowsWeight.get(0).getPeso();
                setImcCalculate();
                //setear el adaptador con los datos
                callsetAdapter();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public  void callsetAdapter(){
        //validacion si se a iniciado el adapter
        if (adapter != null){
            //actuliza la data del apdater
            Log.e(TAG,"adapter != null");
            adapter.updateData(rowsWeight);

        }else { //es nulo
            //crea la lista adapter
            Log.e(TAG,"adapter  null");
            adapter = new WeightListAdapter(getActivity(), rowsWeight, this, recyclerView, true);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {
            if (isVisibleToUser) {
                WeightActivity.fab.setVisibility(View.VISIBLE);
            } else {
                WeightActivity.fab.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onItemClicked(View view, int position) {
        int idMenu = rowsWeight.get(position).getId() ;
        Intent intent = new Intent(getActivity(), WeightRegistyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("car", rowsWeight.get(position) ) ;
        intent.putExtras(bundle);

        // TRANSITIONS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View card = view.findViewById(R.id.main_card);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                    Pair.create(card, "element1"));
            getActivity().startActivity(intent, options.toBundle());
        } else {
            getActivity().startActivity(intent);
        }
    }

    @Override
    public boolean onItemLongClicked(View v, int position) {
        return false;
    }
}
