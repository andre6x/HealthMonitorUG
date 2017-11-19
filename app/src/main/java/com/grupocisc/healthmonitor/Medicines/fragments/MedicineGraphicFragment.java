package com.grupocisc.healthmonitor.Medicines.fragments;

import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.EMedicine;
import com.grupocisc.healthmonitor.entities.EMedicineUser;
import com.grupocisc.healthmonitor.entities.IConsulMedicines;
import com.grupocisc.healthmonitor.entities.IMedicines;
import com.grupocisc.healthmonitor.entities.IRegCrtMedicamentos;
import com.grupocisc.healthmonitor.entities.IRegisteredMedicines;
import com.grupocisc.healthmonitor.entities.IUserLogin;
import com.satsuware.usefulviews.LabelledSpinner;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.grupocisc.healthmonitor.Utils.Utils.isOnline;

/**
 * Created by Gema on 10/01/2017.
 */

public class MedicineGraphicFragment extends Fragment implements  LabelledSpinner.OnItemChosenListener, AdapterView.OnItemClickListener {

    private static final String TAG = "[MedGraphicFragment]";
    protected BarChart mChart;
    protected Typeface mTfLight;
    private float[] yData  ;
    private String[] xData;
    private static List<IRegCrtMedicamentos.ConsulCtrlMedicamentos> rowsIMedicines;
    private static List<IRegCrtMedicamentos.ConsulCtrlMedicamentos> rowsIMed;
    private LinearLayout lyt_fecha ,lyt_hora;
    private TextView txt_fecha_desde, txt_fecha_hasta ;
    private int year1, month1, day2;
    private String selectTextSpinner;
    private Button btnBuscar;
    private LabelledSpinner spinnerMed;
    private static List<IConsulMedicines.ListadoMedReg> rowsRMedList;
    private static List<EMedicine> lstEMedicine;
    private List<IRegisteredMedicines> lstIRegisteredMedicines;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.medicine_graphic_fragment, null);

        lyt_fecha = (LinearLayout)  root.findViewById(R.id.lyt_fechaIni);
        txt_fecha_desde = (TextView)  root.findViewById(R.id.txt_fecha_desde);
        lyt_hora = (LinearLayout)  root.findViewById(R.id.lyt_fechaFin);
        txt_fecha_hasta = (TextView)  root.findViewById(R.id.txt_fecha_hasta);
        btnBuscar = (Button)  root.findViewById(R.id.btnBuscar);
        spinnerMed = (LabelledSpinner) root.findViewById(R.id.spinner_GraMedicines);

        inicializarFechaHora();
        lyt_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCalendar(myDateListenerDesde);
            }
        });
        lyt_hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCalendar(myDateListenerHasta);
            }
        });

        mTfLight = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");
        mChart = (BarChart) root.findViewById(R.id.chartMed);
        mChart.setDescription(" ");
        mChart.setMaxVisibleValueCount(5);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.setDoubleTapToZoomEnabled(false);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = mChart.getAxisLeft();
        //leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
        leftAxis.setSpaceTop(1);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setMaxWidth(1);
        rightAxis.setSpaceTop(1);


        //selectDataMecicinesDB();
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(isOnline(getActivity()))
//                    selectDataMecicinesDB();
//                else
//                    Utils.generarAlerta(getActivity(), getString(R.string.txt_atencion), getString(R.string.sin_conexion));
                if (lstEMedicine != null ){
                    if (lstEMedicine.size()>0){
                        int idMed = getIdMedicineByPositionSpinner( spinnerMed.getSpinner().getSelectedItemPosition()  );
                        selectDataMecicinesDBLocal( idMed );
                    }
                }

            }
        });
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, int i, Highlight highlight) {
                if (entry == null)
                    return;
            }

            @Override
            public void onNothingSelected() {

            }
        });
        return root;

    }

    @Override
    public void onResume() {
        super.onResume();
        // setearSpinnerMedicamento(Utils.getEmailFromPreference(getContext()));
        setSpinnerMedicineDBLocal(Utils.getEmailFromPreference(getContext()),"" );
    }

    private void  setSpinnerMedicineDBLocal(String UserMail, String UserLogin){
        try {
            lstEMedicine = Utils.GetMedicineUserDBLocal(UserMail,UserLogin,HealthMonitorApplicattion.getApplication().getMedicineDao());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ( lstEMedicine != null ){
            if ( lstEMedicine.size() <= 0 ){
                Toast.makeText( getActivity() , "No existen medicamentos registrados.", Toast.LENGTH_SHORT).show();
            }else{

                Collections.sort(lstEMedicine);
                int cont = 0;
                String[] array = new String[lstEMedicine.size()];
                //Guarda el nombre del medicamento en un arreglo
                for (EMedicine eEMedicine : lstEMedicine) {
                    array[cont] = eEMedicine.getNombre();
                    cont++;
                }
                spinnerMed.setColor(R.color.colorPrimaryDark);
                spinnerMed.setItemsArray(array);
                spinnerMed.setSelection(0, true);
                spinnerMed.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
                    @Override
                    public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
                        selectTextSpinner = adapterView.getItemAtPosition(position).toString();
                        //if(isOnline(getActivity()))
                        //    selectDataMecicinesDB();
                        int idMed = getIdMedicineByPositionSpinner(position);
                        selectDataMecicinesDBLocal( idMed );
                    }
                    @Override
                    public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {
                    }
                });

            }

        }else{
            //showLayoutDialog();
            Utils.generarAlerta(getActivity(), getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
        }
    }


    public void setearSpinnerMedicamento(String UserMail) {
        IConsulMedicines iConsulMedicines = HealthMonitorApplicattion.getApplication().getmRestCISCAdapterP().create(IConsulMedicines.class);
        Call<List<IConsulMedicines.ListadoMedReg>> call = iConsulMedicines.LISTADO_CALL(UserMail);
        call.enqueue(new Callback<List<IConsulMedicines.ListadoMedReg>>() {
            @Override
            public void onResponse(Call<List<IConsulMedicines.ListadoMedReg>> call, Response<List<IConsulMedicines.ListadoMedReg>> response) {
                if (response.isSuccessful()) {
                    rowsRMedList = null;
                    rowsRMedList = response.body();
                    if (rowsRMedList.size() <= 0) {
                        Toast.makeText( getActivity() , "No existen medicamentos registrados 123", Toast.LENGTH_SHORT).show();

                    } else {
                        Collections.sort(rowsRMedList);
                        int cont = 0;
                        String[] array = new String[rowsRMedList.size()];
                        //Guarda el nombre del medicamento en un arreglo
                        for (IConsulMedicines.ListadoMedReg roweRMedicine : rowsRMedList) {
                            array[cont] = roweRMedicine.getMedicamento();
                            cont++;
                        }
                        /**************************************************************************************************/
                        spinnerMed.setColor(R.color.colorPrimaryDark);
                        spinnerMed.setItemsArray(array);
                        spinnerMed.setSelection(0, true);
                        spinnerMed.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
                            @Override
                            public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
                                selectTextSpinner = adapterView.getItemAtPosition(position).toString();
                                if(isOnline(getActivity()))
                                    selectDataMecicinesDB();
                            }

                            @Override
                            public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<List<IConsulMedicines.ListadoMedReg>> call, Throwable t) {

            }
        });

    }

    private DatePickerDialog.OnDateSetListener myDateListenerDesde = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            String mes = "" + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : "" + (monthOfYear + 1));
            String dia =  dayOfMonth < 10 ? "0"+dayOfMonth : ""+dayOfMonth;
            String date = ""+dia+"/"+mes+"/"+year;
            txt_fecha_desde.setText(date);
        }
    };
    private DatePickerDialog.OnDateSetListener myDateListenerHasta = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            String mes = "" + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : "" + (monthOfYear + 1));
            String dia =  dayOfMonth < 10 ? "0"+dayOfMonth : ""+dayOfMonth;
            String date = ""+dia+"/"+mes+"/"+year;
            txt_fecha_hasta.setText(date);
        }
    };

    //se ejecuta el selecionar el spinner
    @Override
    public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
        String selectedText = adapterView.getItemAtPosition(position).toString();
        switch (labelledSpinner.getId()) {
            case R.id.spinner_GraMedicines:
                //Toast.makeText(getActivity(), "Selected: " + selectedText, Toast.LENGTH_SHORT).show();
                selectTextSpinner = selectedText;
                break;
            // If you have multiple LabelledSpinners, you can add more cases here
        }
    }
    //se ejecuta el selecionar el spinner
    @Override
    public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {
        // Do something here
    }

    private int getIdMedicineByPositionSpinner(int position ){
        int idMedicine = 0 ;
        idMedicine = lstEMedicine.get(position).getIdMedicamento();

        return  idMedicine ;
    }

    private void selectDataMecicinesDBLocal( int idMed ){
        String Method ="[selectDataMecicinesDBLocal]";
        Log.i(TAG, Method + "Init..." );

        String fechaDesde = txt_fecha_desde.getText().toString().substring(6, 10) + "/" + txt_fecha_desde.getText().toString().substring(3, 5) + "/" + txt_fecha_desde.getText().toString().substring(0, 2) + " 00:00:00" ;
        String fechaHasta = txt_fecha_hasta.getText().toString().substring(6, 10) + "/" + txt_fecha_hasta.getText().toString().substring(3, 5) + "/" + txt_fecha_hasta.getText().toString().substring(0, 2) + " 23:59:59" ;
        String email = Utils.getEmailFromPreference(this.getContext());

        //final String nombreMed = selectTextSpinner; //spinnerMed.getSpinner().getItemAtPosition(0).toString();

        try {
            lstIRegisteredMedicines = Utils.GetControlRegisteredMedicineUserByDateRangeDBLocal(
                    email,"",
                    idMed,
                    fechaDesde,
                    fechaHasta,
                    HealthMonitorApplicattion.getApplication().getRegisteredMedicinesDao()
            );
            yData = new float[0];
            xData = new String[0];
            if (lstIRegisteredMedicines != null){
                if ( lstIRegisteredMedicines.size() > 0 ){
                    Collections.sort(lstIRegisteredMedicines);
                    int size = lstIRegisteredMedicines.size();
                    yData = new float[size];
                    xData = new String[size];
                    for(int i = 0 ; i < size ; i++){
                        yData[i]=lstIRegisteredMedicines.get(i).getDosis() ;
                        xData[i]=lstIRegisteredMedicines.get(i).getFecha_consumo ().substring(0,10);
                    }
                }
            }
            addData();
        }catch (Exception e){
            // showLayoutDialog();
            Log.e(TAG, Method + e.getMessage());
            Utils.generarAlerta(getActivity(), getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
        }
    }

    //obtener datos de la tabla BD
    public  void selectDataMecicinesDB(){

        String fechaDesde = txt_fecha_desde.getText().toString().substring(6, 10) + "/" + txt_fecha_desde.getText().toString().substring(3, 5) + "/" + txt_fecha_desde.getText().toString().substring(0, 2);
        String fechaHasta = txt_fecha_hasta.getText().toString().substring(6, 10) + "/" + txt_fecha_hasta.getText().toString().substring(3, 5) + "/" + txt_fecha_hasta.getText().toString().substring(0, 2);
        String email = Utils.getEmailFromPreference(this.getContext());

        final String nombreMed = selectTextSpinner; //spinnerMed.getSpinner().getItemAtPosition(0).toString();

        IRegCrtMedicamentos iRegCrtMedicamentos = HealthMonitorApplicattion.getApplication().getmRestCISCAdapterP().create(IRegCrtMedicamentos.class);
        Call<List<IRegCrtMedicamentos.ConsulCtrlMedicamentos>> call = iRegCrtMedicamentos.CONSULTA_CTRL_MED_CALL(email, fechaDesde, fechaHasta );
        call.enqueue(new Callback<List<IRegCrtMedicamentos.ConsulCtrlMedicamentos>>() {
            @Override
            public void onResponse(Call<List<IRegCrtMedicamentos.ConsulCtrlMedicamentos>> call, Response<List<IRegCrtMedicamentos.ConsulCtrlMedicamentos>> response) {
                if(response.isSuccessful()) {
                    Log.e(TAG,"RESPUESTA EXISTOSA");
                    rowsIMed = null;
                    rowsIMed = response.body();
                    Collections.sort(rowsIMed);
                    int size = rowsIMed.size();
                    if (size>0) {
                        rowsIMedicines = new ArrayList<>();
                        for(int x= 0 ; x < size; x ++ ){
                            if(nombreMed.equals(rowsIMed.get(x).getNombre() +" "+ rowsIMed.get(x).getDescripcion())) {
                                rowsIMedicines.add(rowsIMed.get(x));
                            }
                        }
                        size = rowsIMedicines == null? 0 : rowsIMedicines.size();
                        yData = new float[size];
                        xData = new String[size];
                        for(int i = 0 ; i < size ; i++){
                            yData[i]=rowsIMedicines.get(i).getDosis() ;
                            xData[i]=rowsIMedicines.get(i).getFecha().substring(0,10);
                        }
                        addData();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<IRegCrtMedicamentos.ConsulCtrlMedicamentos>> call, Throwable t) {

            }
        });
    }

    private void addData(){
        ArrayList<BarEntry> yVals1 = new  ArrayList<BarEntry>();
        for (int i=0; i<yData.length; i++) {
            yVals1.add(new BarEntry(yData[i],i));
        }

        ArrayList<String> xVals2 = new  ArrayList<String>();
        for (int i=0; i<xData.length; i++) {
            xVals2.add(xData[i]);
        }

        BarDataSet dataset = new BarDataSet(yVals1,"");
        ArrayList<Integer> colors = new ArrayList<Integer>();

        colors.add(ContextCompat.getColor(getActivity(), R.color.gluco_red));
        dataset.setColors(colors);

        BarData data = new BarData(xVals2,dataset);
        data.setValueFormatter(new DefaultValueFormatter(0));
        data.setValueTextSize(8f);
        data.setValueTextColor(Color.GRAY);

        mChart.setData(data);
        mChart.highlightValue(null);
        mChart.invalidate();
    }

    private void inicializarFechaHora(){
        //obtener fechay hora de Calendar
        Calendar c = Calendar.getInstance();
        year1 = c.get(Calendar.YEAR);
        month1 = (c.get(Calendar.MONTH)+1);
        String mes1 =  month1 < 10 ? "0"+month1 : ""+month1;
        String dia1 =  "01";

        //setear fecha_desde
        String date1 = ""+dia1+"/"+mes1+"/"+year1;
        txt_fecha_desde.setText(date1);
        //setear fecha_hasta

        day2 =  c.getActualMaximum(Calendar.DAY_OF_MONTH);
        String dia2 =  ""+day2;

        //ADD_DAYS(ADD_MONTHS(:fechaDesde,1),-1)
        String date2 = ""+dia2+"/"+mes1+"/"+year1;
        txt_fecha_hasta.setText(date2);

    }

    public void callCalendar(DatePickerDialog.OnDateSetListener myDateListener){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                myDateListener,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setThemeDark(false);//tema dark
        dpd.vibrate(true);//vibrar al selecionar
        dpd.dismissOnPause(false);
        dpd.showYearPickerFirst(false);//aparece el año primero
        dpd.setVersion(false ? DatePickerDialog.Version.VERSION_2 : DatePickerDialog.Version.VERSION_1);//false aparece v1 true v2
        if (false) {
            dpd.setAccentColor(Color.parseColor("#9C27B0"));//color customizado
        }
        if (false) {
            dpd.setTitle("DatePicker Title");//titulo cutomizado
        }
        if (false) {// rango de fechas
            Calendar date1 = Calendar.getInstance();
            Calendar date2 = Calendar.getInstance();
            date2.add(Calendar.WEEK_OF_MONTH, -1);
            Calendar date3 = Calendar.getInstance();
            date3.add(Calendar.WEEK_OF_MONTH, 1);
            Calendar[] days = {date1, date2, date3};
            dpd.setHighlightedDays(days);
        }
        if (false) {
            Calendar[] days = new Calendar[13];
            for (int i = -6; i < 7; i++) {
                Calendar day = Calendar.getInstance();
                day.add(Calendar.DAY_OF_MONTH, i * 2);
                days[i + 6] = day;
            }
            dpd.setSelectableDays(days);
        }
        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void postExecutionQueryDBLocal( int op ){
        String Method ="[postExecutionQueryDBLocal][op]="+op ;
        Log.i(TAG, Method + "Init..."  );
        //showLayoutDialog();
        if (op == 0){
            if (lstEMedicine != null)
                if (lstEMedicine.size() > 0)
                    callSetAdapter();

        }

        Log.i(TAG, Method + "End..."  );

    }

    private void callSetAdapter(){

    }

}
