package com.grupocisc.healthmonitor.State.fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

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
import com.grupocisc.healthmonitor.State.activities.StateActivity;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IConsulState;
import com.grupocisc.healthmonitor.entities.IState;
import com.satsuware.usefulviews.LabelledSpinner;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;

//implements SeekBar.OnSeekBarChangeListener,OnChartValueSelectedListener
public class StateGraphicFragment extends  Fragment implements LabelledSpinner.OnItemChosenListener {

    private String TAG = "StateGraphicFragment";
    protected BarChart mChart;
    private SeekBar mSeekBarX, mSeekBarY;
    private TextView tvX, tvY;
    protected Typeface mTfLight;
    private float[] yData;
    private String[] xData;
    private float[] zData;
    private static List<IConsulState.Objeto> list;
    private static List<IState> rowsIState;
    private LinearLayout lyt_fecha ,lyt_hora;
    private TextView txt_fecha_desde, txt_fecha_hasta ;
    private int year1, month1, day1, year2, month2,day2;
    private String selectTextSpinner;
    private Button btnBuscar;
    public ProgressDialog Dialog;
    private Call<List<IConsulState.Objeto>> call_1;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.content_state_main, null);
        Dialog = new ProgressDialog(getActivity());
        lyt_fecha = (LinearLayout)  root.findViewById(R.id.lyt_fecha);
        txt_fecha_desde = (TextView)  root.findViewById(R.id.txt_fecha_desde);
        lyt_hora = (LinearLayout)  root.findViewById(R.id.lyt_hora);
        txt_fecha_hasta = (TextView)  root.findViewById(R.id.txt_fecha_hasta);
        btnBuscar = (Button)  root.findViewById(R.id.btBuscar);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  selectDataStateDB(1);
                selectDataStateDBLocal();
                //if(Utils.isOnline(getActivity())) {
                //restarLoading();}
            }
        });

        inicializarFechaHora();
        lyt_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callCalendar1(myDateListenerDesde);
            }
        });
        lyt_hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callCalendar1(myDateListenerHasta);
            }
        });


       // setearMaterialBetterSpinner( root);
        mTfLight = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");
        mChart = (BarChart) root.findViewById(R.id.chart1);
        mChart.setDescription(" ");
        mChart.setMaxVisibleValueCount(5);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.setDoubleTapToZoomEnabled(false);
        // mChart.setTouchEnabled(false);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = mChart.getAxisLeft();
        //leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
        leftAxis.setSpaceTop(1);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setMaxWidth(1);
        //rightAxis.setTypeface(mTfLight);
        //rightAxis.setLabelCount(10, true);
        rightAxis.setSpaceTop(1);

        //Legend l = mChart.getLegend();
        //l.setForm(Legend.LegendForm.SQUARE);
        //l.setFormSize(10f);
        //l.setTextSize(16f);
        //l.setXEntrySpace(3f);
//        selectDataPulseDB(0);  COMENTADO AJOB
        //if(Utils.isOnline(getActivity())) {
        //restarLoading();}

        selectDataStateDBLocal();
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
        Log.e(TAG,"onResume");
        super.onResume();
     //   if(Utils.isOnline(getActivity())) {
       //     restarLoading();}
        //selectDataStateDB(0);  //COMENTADO AJOB
        selectDataStateDBLocal();
    }

    private DatePickerDialog.OnDateSetListener myDateListenerDesde = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

            String mes = ""+((monthOfYear+1) < 10 ? "0"+(monthOfYear+1) : ""+(monthOfYear+1));
            String dia = dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth;
            String date = "" + dia + "/" + mes + "/" + year;
            Log.e(TAG,"mes: " + mes );
            Log.e(TAG,"monthOfYear: " + monthOfYear );
            //String dia =  dayOfMonth < 10 ? "0"+dayOfMonth : ""+dayOfMonth;
            //String date = ""+dia+"/"+mes+"/"+year;
            txt_fecha_desde.setText(date);
        }
    };
    private DatePickerDialog.OnDateSetListener myDateListenerHasta = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

            String mes = ""+((monthOfYear+1) < 10 ? "0"+(monthOfYear+1) : ""+(monthOfYear+1));
            String dia =  dayOfMonth < 10 ? "0"+dayOfMonth : ""+dayOfMonth;
            String date = "" + dia + "/" + mes + "/" + year;
            txt_fecha_hasta.setText(date);
        }
    };


    //se ejecuta el selecionar el spinner

    @Override
    public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
        String selectedText = adapterView.getItemAtPosition(position).toString();
        switch (labelledSpinner.getId()) {
            case R.id.spinner_planets:
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

    private void selectDataStateDBLocal()
    {
        String Method ="[selectDataStateDBLocal]";
        Log.i(TAG, Method + "Init..."  );
        String fechaIni =
                        txt_fecha_desde.getText().toString().substring(0,2)+"/" +
                        txt_fecha_desde.getText().toString().substring(3,5)+"/"+
                        txt_fecha_desde.getText().toString().substring(6,10)+ " 00:00:00";

        String fechaFin =
                txt_fecha_hasta.getText().toString().substring(0,2)+"/" +
                        txt_fecha_hasta.getText().toString().substring(3,5)+"/"+
                        txt_fecha_hasta.getText().toString().substring(6,10) +" 23:59:59";
        String email = Utils.getEmailFromPreference(this.getContext());

        try {
            Log.i(TAG,Method+"FECHAI"+fechaIni);
            Log.i(TAG,Method+"FECHAffi"+fechaFin);

            Log.i(TAG, Method + "Cargando la lista de Estado");

            rowsIState=Utils.GetStateRangoFechas(HealthMonitorApplicattion.getApplication().getStateDao(),fechaIni,fechaFin);
            Log.i(TAG, Method + "Registers Obtained " + rowsIState.size()  );
            Log.i(TAG, Method + "End getting List of State..."  );
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        if(rowsIState!=null){
            postExecutionQueryDBLocal();
        }
        else
        {
            Utils.generarAlerta(getActivity(),getString(R.string.txt_atencion),getString(R.string.text_error_metodo));
        }
        Log.i(TAG, Method + "End..."  );
    }

    public void postExecutionQueryDBLocal() {
        String Method ="[postExecutionQueryDBLocal]";
        Log.i(TAG,Method +"Init....");
        int ssize=0;

            if (rowsIState.size() > 0) {
                ssize=rowsIState.size();
               // zData = new float[ssize];
                yData = new float[ssize];
                xData = new String[ssize];
                for (int i = 0; i < ssize; i++) {
                    yData[i] = rowsIState.get(i).getIdStatus();
                    xData[i] = rowsIState.get(i).getFecha();
                }

                addData();

            }
            Log.i(TAG, Method +"End..");
        }



    //obtener datos de la tabla BD
    public  void selectDataStateDB(int inicio){
        Log.e(TAG, "selectDataStateDB" );
        String fechaDesde =
                          txt_fecha_desde.getText().toString().substring(6, 10) + "/"
                        + txt_fecha_desde.getText().toString().substring(3, 5) + "/"
                        + txt_fecha_desde.getText().toString().substring(0, 2) + " 00:00:00";
        String fechaHasta =
                          txt_fecha_hasta.getText().toString().substring(6, 10) + "/"
                        + txt_fecha_hasta.getText().toString().substring(3, 5) + "/"
                        + txt_fecha_hasta.getText().toString().substring(0, 2) + " 23:59:59";
       // String Estado = selectTextSpinner;
        //Calendar now = Calendar.getInstance();
        //int month = now.get(Calendar.MONTH);
        //String mes = month < 10 ? "0" + month : "" + month;
        //String mes = ""+month;
        try {
            if (inicio == 1 )
            {//validar si en la tabla ahi datos mayor a 0
                Log.e(TAG, "FECHA I"+fechaDesde );
                Log.e(TAG, "FECHA F"+fechaHasta );
          //      Log.e(TAG, "ESTADO"+Estado );

                if (Utils.GetIStateDateFromDatabase(HealthMonitorApplicattion.getApplication().getStateDao(),fechaDesde,fechaHasta).size()>0)
                {
                    Log.e(TAG,"GetIStateDateFromDatabase");

                    //asignamos datos de la tabla a la lista de objeto
                    rowsIState = Utils.GetIStateDateFromDatabase(HealthMonitorApplicattion.getApplication().getStateDao(),fechaDesde,fechaHasta);
                    postExecutionQueryDBLocal();
                }else
                    Log.e(TAG,"GetIStateDateFromDatabase");
            } else
            {
               // if (Utils.GetIStateFromDatabase(HealthMonitorApplicattion.getApplication().getStateDao(),mes).size()>0);
                {   //asignamos datos de la tabla a la lista de objeto
                 //   rowsIState=Utils.GetIStateFromDatabase(HealthMonitorApplicattion.getApplication().getStateDao(),mes);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


                    }


    private void addData() {

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        //ArrayList<BarEntry> zVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < yData.length; i++) {
           yVals1.add(new BarEntry(yData[i], i));
       }

    //    for (int i = 0; i < zData.length; i++) {

      //      zVals1.add(new BarEntry(zData[i],i ));
            //zVals1.add(new BarEntry(zData[i]),i);
       // }

        ArrayList<String> xVals2 = new ArrayList<String>();

        for (int i = 0; i < xData.length; i++) {
            xVals2.add(xData[i]);
        }

       BarDataSet dataset = new BarDataSet(yVals1, "");
        ArrayList<Integer> colors = new ArrayList<Integer>();

        int tamanos = rowsIState.size();
        for (int x = 0; x < tamanos; x++) {
            Log.e(TAG,"Tamaño stack:" + tamanos );
            int state = rowsIState.get(x).getIdStatus();
            Log.e(TAG,"Tamaño estado:" + state );

            switch (state) {
                case (1):

                    colors.add(ContextCompat.getColor(getActivity(), R.color.status_orange));
                    break;
                case 2:
                    colors.add(ContextCompat.getColor(getActivity(), R.color.status_green));
                    break;
                case 3:
                    colors.add(ContextCompat.getColor(getActivity(), R.color.status_purple));
                    break;
                case 4:
                    colors.add(ContextCompat.getColor(getActivity(), R.color.status_blue));
                    break;
                case 5:
                    colors.add(ContextCompat.getColor(getActivity(), R.color.status_silver));
                    break;
            }
            dataset.setColors(colors);
            BarData data = new BarData(xVals2, dataset);
            data.setValueFormatter(new DefaultValueFormatter(0));
            data.setValueTextSize(8f);
            data.setValueTextColor(Color.WHITE);

            mChart.setData(data);
            mChart.highlightValue(null);
            mChart.invalidate();
        }

    }


    private void VaciarData() {

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<String> xVals2 = new ArrayList<String>();
        BarDataSet dataset = new BarDataSet(yVals1, "");
        ArrayList<Integer> colors = new ArrayList<Integer>();
       colors.add(ContextCompat.getColor(getActivity(), R.color.gluco_red));
        dataset.setColors(colors);
        BarData data = new BarData(xVals2, dataset);
        data.setValueFormatter(new DefaultValueFormatter(0));
        data.setValueTextSize(8f);
        data.setValueTextColor(Color.GRAY);

        mChart.setData(data);
        mChart.highlightValue(null);
        mChart.invalidate();

    }

    private void inicializarFechaHora(){

        //obtener fechay hroa de Calendar
        Calendar c = Calendar.getInstance();
        year1 = c.get(Calendar.YEAR);
        month1 = (c.get(Calendar.MONTH)+1);
        String mes1 =  month1 < 10 ? "0"+month1 : "" +month1;
        String dia1 =  "01";

        String date1 = ""+dia1+"/"+mes1+"/"+year1;
        txt_fecha_desde.setText(date1);


        day2 =  c.getActualMaximum(Calendar.DAY_OF_MONTH) ;
        String date2 = ""+day2+"/"+mes1+"/"+ year1;

        Log.e(TAG,"date2: " + date2 );
        txt_fecha_hasta.setText(date2);

    }

    public void callCalendar1(DatePickerDialog.OnDateSetListener myDateListener){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
               myDateListener,
                // myDateListenerDesde,
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
    /*public void callCalendar2(){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                myDateListenerHasta,
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

    }*/

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {
            if (isVisibleToUser) {
                StateActivity.fab.setVisibility(View.GONE);
            } else {
                StateActivity.fab.setVisibility(View.VISIBLE);
            }
        }
    }

   /*
        for(int i=0;i<mObjeto.size();i++){
            //
            ICunsulParamet.Objeto a = mObjeto.get(i);
            IPulse Pulse = new IPulse()  ;

             Pulse.setConcentracion(a.getValor()+"") ;
             Pulse.setFecha(a.getFecha());
             Pulse.setMedido(a.getMedio());
            Pulse.setObservacion(a.getObservacion());

           // rowsIPulse.add(Pulse);
            Log.e(TAG,Pulse.getConcentracion());
            Log.e(TAG,Pulse.getFecha()+"");
            Log.e(TAG,Pulse.getMedido()+"");
            Log.e(TAG,Pulse.getObservacion()+"");
        }


            if (mObjeto.getCodigoSalida() == 0 ) {
                //Utils.generarAlerta(LoginActivity.this, getString(R.string.txt_atencion), mLoginUser.getRespuesta() );
                //oka
                SavePreferencesCallMainActivity();
            }else {
                Utils.generarAlerta(LoginActivity.this, getString(R.string.txt_atencion), mLoginUser.getMensajeSalida() );
            }
        }else {
            Utils.generarAlerta(LoginActivity.this, getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
        }
        */



  /* private void restartLoadingEnviarData( ){
        //obtener todos los datos del activity

        //setear fecha_desde
        String fechaDesde = txt_fecha_desde.getText().toString().substring(6,10)+"/"+txt_fecha_desde.getText().toString().substring(3,5)+"/"+txt_fecha_desde.getText().toString().substring(0,2);
        //Log.e(TAG, "FECHA_DESDE: "+ fechaDesde);
        //setear fecha_hasta
        String fechaHasta = txt_fecha_hasta.getText().toString().substring(6,10)+"/"+txt_fecha_hasta.getText().toString().substring(3,5)+"/"+txt_fecha_hasta.getText().toString().substring(0,2);
        //Log.e(TAG, "FECHA_HASTA: "+ fechaHasta);
        String email    = Utils.getEmailFromPreference(getActivity()) ;
        //Log.e(TAG, "EMAIL: "+ email);
       //enviar webservice
        //APUNTANDO AA METODO CISC
        IConsulState ConsulDoctor = HealthMonitorApplicattion.getApplication().getmRestCISCAdapterAnimo().create(IConsulState.class);
        call_1 = ConsulDoctor.ConsulState(email,fechaDesde,fechaHasta);
        call_1.enqueue(new Callback<List<IConsulState.Objeto>>() {
            @Override
            public void onResponse(Call<List<IConsulState.Objeto>> call, Response<List<IConsulState.Objeto>> response) {
                if (response.isSuccess()) {
                   // Log.e(TAG, "Respuesta exitosa");
                    rowsIState = null;
                    rowsIState = response.body();
                    //for (int i = 0; i < rowsIState.size(); i++) {
                      //  Log.e(TAG, "Error en la petición onResponse" + rowsIState.get(i).getFecha());
                    //}
                    //Log.e(TAG, "Error en la petición onResponse ok: " + rowsIState.size());
                    postExecutionLogin();
                } else {
                    showLayoutDialog();
                    Utils.generarAlerta(getActivity(), getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
                   // Log.e(TAG, "Error en la petición onResponse else");

                }
            }

            @Override
            public void onFailure(Call<List<IConsulState.Objeto>> call, Throwable t) {
                showLayoutDialog();
                Utils.generarAlerta(getActivity(), getString(R.string.txt_atencion), getString(R.string.text_error_metodo) + " o revise su conexión a internet");
                t.printStackTrace();
               // Log.e(TAG, "mmmmmmmmmmmm");
            }
        });
    }*/


  /*  private void showLoadingDialog(){
        Dialog.setMessage("Espere un Momento..");
        Dialog.setCancelable(false);
        Dialog.show();
    }

    private void showLayoutDialog(){
        if (Dialog != null)
            Dialog.dismiss();
    }*/




    }
