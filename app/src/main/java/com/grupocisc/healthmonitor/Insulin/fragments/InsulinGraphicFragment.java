package com.grupocisc.healthmonitor.Insulin.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.grupocisc.healthmonitor.Insulin.activities.InsulinActivity;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.EInsulin;
import com.grupocisc.healthmonitor.entities.ICunsulParamet;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.util.Log;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.grupocisc.healthmonitor.Utils.Utils.isOnline;
import static com.grupocisc.healthmonitor.Utils.Utils.isOnlineNet;

//implements SeekBar.OnSeekBarChangeListener,OnChartValueSelectedListener
public class InsulinGraphicFragment extends Fragment {

    private static final String TAG = "[InsulinGraphicFrag]";
    private float[] yData  ;
    private String[] xData;
    private static List<ICunsulParamet.Objeto> rowsEInsulin;
    private static List<EInsulin> lstEInsulin;
    private int year1, month1, day2;
    protected BarChart mChart;
    protected Typeface mTfLight;

    @BindView(R.id.lyt_fechaIni)  LinearLayout lyt_fecha;
    @BindView(R.id.lyt_fechaFin)  LinearLayout lyt_hora;
    @BindView(R.id.txt_fecha_desde) TextView txt_fecha_desde;
    @BindView(R.id.txt_fecha_hasta) TextView txt_fecha_hasta ;
    @BindView(R.id.btnBuscar) Button btnBuscar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String Method ="[onCreateView]";
        Log.i(TAG, Method + "Init..."  );

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.insulin_graphic_fragment, null);
        ButterKnife.bind(this, root);

        inicializarFechaHora();

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(isOnline(getActivity()))
                //    selectDataInsulinDB();
                //else
                //    Utils.generarAlerta(getActivity(), getString(R.string.txt_atencion), getString(R.string.sin_conexion));
                selectDataInsulinDBLocal();
            }
        });

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
        mChart = (BarChart) root.findViewById(R.id.chartInsulin);
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

        //if(isOnline(getActivity()))
        //    selectDataInsulinDB();
        selectDataInsulinDBLocal();
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
        Log.i(TAG, Method + "End..."  );
        return root;
    }
    @Override
    public void onResume() {
        String Method ="[onResume]";
        Log.i(TAG, Method + "Init..."  );
        super.onResume();

        //if(isOnline(getActivity()))
        //    selectDataInsulinDB();
        selectDataInsulinDBLocal();
        Log.i(TAG, Method + "End..."  );
    }

    private DatePickerDialog.OnDateSetListener myDateListenerDesde = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

            String mes =  (monthOfYear+1) < 10 ? "0" + (monthOfYear+1) : ""+ (monthOfYear+1);
            String dia =  dayOfMonth < 10 ? "0"+dayOfMonth : ""+dayOfMonth;
            String date = ""+dia+"/"+mes+"/"+year;
            txt_fecha_desde.setText(date);
        }
    };

    private DatePickerDialog.OnDateSetListener myDateListenerHasta = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

            String mes =  (monthOfYear+1) < 10 ? "0" + (monthOfYear+1) : ""+ (monthOfYear+1);
            String dia =  dayOfMonth < 10 ? "0"+dayOfMonth : ""+dayOfMonth;
            String date = ""+dia+"/"+mes+"/"+year;
            txt_fecha_hasta.setText(date);
        }
    };

    private void selectDataInsulinDBLocal(){
        String Method ="[selectDataInsulinDBLocal]";
        Log.i(TAG, Method + "Init..."  );

        String fechaIni = txt_fecha_desde.getText().toString() + " 00:00:00";
        String fechaFin = txt_fecha_hasta.getText().toString() + " 23:59:59";
        String email = Utils.getEmailFromPreference(this.getContext());
        try {
            Log.i(TAG, Method + "Init getting List of Insulin..."  );
            lstEInsulin = Utils.GetInsulinRangoFechas( HealthMonitorApplicattion.getApplication().getInsulinDao()
                    ,fechaIni,fechaFin);
            Log.i(TAG, Method + "Registers Obtained " + lstEInsulin.size()  );
            Log.i(TAG, Method + "End getting List of Insulin..."  );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (lstEInsulin != null){
            postExecutionQueryDBLocal();
        }
        else {
            //showLayoutDialog();
            Utils.generarAlerta(getActivity(), getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
        }
        Log.i(TAG, Method + "End..."  );
    }

    private void postExecutionQueryDBLocal(){
        String Method ="[postExecutionQueryDBLocal]";
        Log.i(TAG, Method + "Init..."  );

        int iSize = 0;
        if (lstEInsulin.size() > 0 ){
            iSize = lstEInsulin.size();
            yData = new float[iSize];
            xData = new String[iSize];
            for(int i = 0 ; i < iSize ; i++){
                yData[i]= lstEInsulin.get(i).getInsulina()  ;//    rowsEInsulin.get(i).getValor();
                xData[i]= lstEInsulin.get(i).getFecha();

                ;  // rowsEInsulin.get(i).getFecha();
            }
            addData();
        }

        Log.i(TAG, Method + "End..."  );
    }


    //obtener datos de la tabla BD
    public void selectDataInsulinDB(){

        String fechaDesde = txt_fecha_desde.getText().toString().substring(6, 10) + "/"
                + txt_fecha_desde.getText().toString().substring(3, 5) + "/"
                + txt_fecha_desde.getText().toString().substring(0, 2) + " 00:00:00";
        String fechaHasta = txt_fecha_hasta.getText().toString().substring(6, 10) + "/"
                + txt_fecha_hasta.getText().toString().substring(3, 5) + "/"
                + txt_fecha_hasta.getText().toString().substring(0, 2) + " 23:59:59";
        String email = Utils.getEmailFromPreference(this.getContext());

        try {

            ICunsulParamet CunsulParamet = HealthMonitorApplicattion.getApplication().getRetrofitAdapter().create(ICunsulParamet.class);
            Call<List<ICunsulParamet.Objeto>> call_1 = CunsulParamet.HistorialDatos(email, "insulina", fechaDesde, fechaHasta);
            call_1.enqueue(new Callback<List<ICunsulParamet.Objeto>>() {
                @Override
                public void onResponse(Call<List<ICunsulParamet.Objeto>> call, Response<List<ICunsulParamet.Objeto>> response) {
                    if (response.isSuccessful()) {
                        {
                            rowsEInsulin = null;
                            rowsEInsulin = response.body();
                            int tamaño = rowsEInsulin == null? 0 : rowsEInsulin.size();

                            if (tamaño>0)
                            {
                                yData = new float[tamaño];
                                xData = new String[tamaño];
                                for(int i = 0 ; i < tamaño ; i++){
                                    yData[i]=rowsEInsulin.get(i).getValor();
                                    xData[i]=rowsEInsulin.get(i).getFecha();
                                }
                                addData();
                            }
                        }
                    } else {
                        Utils.generarAlerta(getActivity(), getString(R.string.txt_atencion), getString(R.string.text_error_metodo));
                    }
                }

                @Override
                public void onFailure(Call<List<ICunsulParamet.Objeto>> call, Throwable t) {
                    Utils.generarAlerta(getActivity(), getString(R.string.txt_atencion), getString(R.string.text_error_metodo ) + " o revise su conexión a internet");
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void addData(){

        String Method ="[addData]";
        Log.i(TAG, Method + "Init..."  );

        ArrayList<BarEntry> yVals1 = new  ArrayList<BarEntry>();

        for (int i=0; i<yData.length; i++){
            yVals1.add(new BarEntry(yData[i],i));
        }
        ArrayList<String> xVals2 = new  ArrayList<String>();
        for (int i=0; i<xData.length; i++){
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

        Log.i(TAG, Method + "End..."  );
    }

    private void inicializarFechaHora(){
        String Method ="[inicializarFechaHora]";
        Log.i(TAG, Method + "Init..."  );

        //obtener fechay hora de Calendar
        Calendar c = Calendar.getInstance();
        year1 = c.get(Calendar.YEAR);
        month1 = (c.get(Calendar.MONTH) + 1);
        String mes1 =  month1<10? "0"+month1 : ""+month1;
        String dia1 =  "01";

        //setear fecha_desde
        String date1 = ""+dia1+"/"+mes1+"/"+year1;
        txt_fecha_desde.setText(date1);

        //setear fecha_hasta
        day2 =  c.getActualMaximum(Calendar.DAY_OF_MONTH);

        //ADD_DAYS(ADD_MONTHS(:fechaDesde,1),-1)
        String date2 = ""+day2+"/"+mes1+"/"+year1;
        txt_fecha_hasta.setText(date2);

        Log.i(TAG, Method + "End..."  );
    }

    public void callCalendar(DatePickerDialog.OnDateSetListener myDateListener){
        String Method ="[callCalendarDate]";
        Log.i(TAG, Method + "Init..."  );

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

        Log.i(TAG, Method + "End..."  );
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        String Method ="[setUserVisibleHint]";
        Log.i(TAG, Method + "Init..."  );
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {
            if (isVisibleToUser) {
                InsulinActivity.fab.setVisibility(View.GONE);
            } else {
                InsulinActivity.fab.setVisibility(View.VISIBLE);
            }
        }
        Log.i(TAG, Method + "End..."  );
    }

}
