package com.grupocisc.healthmonitor.Weight.fragments;

/**
 * Created by Mariuxi on 12/01/2017.
 */

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
import com.grupocisc.healthmonitor.Pressure.activities.PressureActivity;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
//25022017
import com.grupocisc.healthmonitor.Weight.activities.WeightActivity;
import com.grupocisc.healthmonitor.entities.ICunsulParamet;

import com.grupocisc.healthmonitor.entities.IWeight;
import com.satsuware.usefulviews.LabelledSpinner;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//25022017

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


//implements SeekBar.OnSeekBarChangeListener, OnChartValueSelectedListener

public class WeightgraphicFragment extends Fragment implements LabelledSpinner.OnItemChosenListener {

    private String TAG = "WeightGraphicFragment";
    protected BarChart mChart;
    private SeekBar mSeekBarX, mSeekBarY;
    private TextView tvX, tvY;
    protected Typeface mTfLight;
    private float[] yData  ;
    private String[] xData;
//25022017
    private static List<ICunsulParamet.Objeto> rowsIWeight;
    private LinearLayout lyt_fecha, lyt_hora;
    private TextView txt_fecha_desde, txt_fecha_hasta;
    private int year1, month1, day1, year2, month2, day2;
    private String selectTextSpinner;
    private Button btnBuscar;
    public ProgressDialog Dialog;
    private Call<List<ICunsulParamet.Objeto>> call_1;
    private List<IWeight> rowIWeight;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.content_weight_main, null);
        Dialog = new ProgressDialog(getActivity());
        lyt_fecha = (LinearLayout) root.findViewById(R.id.lyt_fecha);
        txt_fecha_desde = (TextView) root.findViewById(R.id.txt_fecha_desde);
        lyt_hora = (LinearLayout) root.findViewById(R.id.lyt_hora);
        txt_fecha_hasta = (TextView) root.findViewById(R.id.txt_fecha_hasta);
        btnBuscar = (Button) root.findViewById(R.id.btBuscar);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // restarLoading();
                selectDataPulseDB(1);
            }
        });

        inicializarFechaHora();
        lyt_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callCalendar1();
            }
        });
        lyt_hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callCalendar2();
            }
        });


       // setearMaterialBetterSpinner(root);
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
        rightAxis.setMaxWidth(1);;
        rightAxis.setSpaceTop(1);

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
        Log.e(TAG, "onResume");
        selectDataPulseDB(0);
    }

    private DatePickerDialog.OnDateSetListener myDateListenerDesde = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

            String mes = "" + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : "" + (monthOfYear + 1));
            String dia = dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth;
            String date = "" + dia + "/" + mes + "/" + year;
            txt_fecha_desde.setText(date);
        }
    };
    private DatePickerDialog.OnDateSetListener myDateListenerHasta = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

            String mes = "" + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : "" + (monthOfYear + 1));
            String dia = dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth;
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

    //obtener datos de la tabla BD
    public void selectDataPulseDB(int inicio) {
        Log.e(TAG, "selectDataPulseDB" );
        String fechaDesde = txt_fecha_desde.getText().toString();
        String fechaHasta = txt_fecha_hasta.getText().toString();
        String Estado = selectTextSpinner;
        Calendar now = Calendar.getInstance();
        int month = now.get(Calendar.MONTH) + 1;
        String mes = month < 10 ? "0" + month : "" + month;
        try {
            if (inicio == 1) {//validar si en la tabla ahi datos mayor a 0
                if (Utils.GetWeightRangoFechas(HealthMonitorApplicattion.getApplication().getWeightDao(), fechaDesde, fechaHasta).size() > 0) {
                    //asignamos datos de la tabla a la lista de objeto
                    rowIWeight = Utils.GetWeightRangoFechas(HealthMonitorApplicattion.getApplication().getWeightDao(), fechaDesde, fechaHasta);
                    postExecutionLogin();
                }else
                    Log.e(TAG, "GetIPressureDateFromDatabase vacio" );
            } else {
                //validar si en la tabla ahi datos mayor a 0
                if (Utils.GetIWeightFromDatabase(HealthMonitorApplicattion.getApplication().getWeightDao(), mes).size() > 0) {
                    //asignamos datos de la tabla a la lista de objeto
                    rowIWeight = Utils.GetIWeightFromDatabase(HealthMonitorApplicattion.getApplication().getWeightDao(), mes);
                    postExecutionLogin();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void postExecutionLogin(){

        if (rowIWeight != null) {
            Log.e(TAG, "ooooook" + rowIWeight.size());
            int tamaño = rowIWeight.size();
            if (tamaño > 0) {
                yData = new float[tamaño];
                xData = new String[tamaño];
                for (int i = 0; i < tamaño; i++) {
                    yData[i] = Float.parseFloat(rowIWeight.get(i).getPeso() + "");
                    xData[i] = rowIWeight.get(i).getFecha();
                    Log.e(TAG, "Concentracion:" + rowIWeight.get(i).getPeso() + " -fecha:" + rowIWeight.get(i).getFecha());
                }

                addData();

            }
            else
            {
                VaciarData();
            }
        }


    }

    private void addData() {

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < yData.length; i++) {
            yVals1.add(new BarEntry(yData[i], i));

        }
        ArrayList<String> xVals2 = new ArrayList<String>();
        for (int i = 0; i < xData.length; i++) {
            xVals2.add(xData[i]);

        }

        BarDataSet dataset = new BarDataSet(yVals1, "");


        ArrayList<Integer> colors = new ArrayList<Integer>();

        //for (int c: ColorTemplate.VORDIPLOM_COLORS)
        //	colors.add(c)   ;

        //for (int c: ColorTemplate.JOYFUL_COLORS)
        //	colors.add(c)   ;
        //for (int c: ColorTemplate.COLORFUL_COLORS)
        //	colors.add(c)   ;
        //for (int c: ColorTemplate.LIBERTY_COLORS)
        //	colors.add(c)   ;

        //for (int c: ColorTemplate.PASTEL_COLORS)
        //	colors.add(c)   ;

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
    private void inicializarFechaHora() {

        //obtener fechay hroa de Calendar
        Calendar c = Calendar.getInstance();
        year1 = c.get(Calendar.YEAR);
        month1 = c.get(Calendar.MONTH);
        //String mes1 =  ""+month1+1;
        String mes1 = "" + ((month1 + 1) < 10 ? "0" + (month1 + 1) : "" + (month1 + 1));
        String dia1 = "01";

        //setear fecha_desde
        String date1 = "" + dia1 + "/" + mes1 + "/" + year1;
        txt_fecha_desde.setText(date1);
        //setear fecha_hasta

        year2 = c.get(Calendar.YEAR);
        month2 = c.get(Calendar.MONTH);
        day2 = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        String mes2 = "" + ((month2 + 1) < 10 ? "0" + (month2 + 1) : "" + (month2 + 1));
        String dia2 = "" + day2;
        String date2 = "" + dia2 + "/" + mes2 + "/" + year1;
        txt_fecha_hasta.setText(date2);

    }

    public void callCalendar1() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                myDateListenerDesde,
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

    public void callCalendar2() {
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

    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {
            if (isVisibleToUser) {
                WeightActivity.fab.setVisibility(View.GONE);
            } else {
                WeightActivity.fab.setVisibility(View.VISIBLE);
            }
        }
    }
}
