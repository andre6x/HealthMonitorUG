package com.grupocisc.healthmonitor.FitData.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.grupocisc.healthmonitor.FitData.activities.FitActivity;
import com.grupocisc.healthmonitor.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import butterknife.ButterKnife;


public class FitGraphicFragment extends Fragment {

    private static final String TAG = "[FitGraphicFrag]";
    private float[] yData  ;
    private String[] xData;
    private int year1, month1, day2;
    protected BarChart mChart;
    protected Typeface mTfLight;
    public static ArrayList<DataPoint> data = new ArrayList<DataPoint>();
    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("MMM d");


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String Method ="[onCreateView]";
        Log.e(TAG, "Fit - onCreateView");

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fit_graphic_fragment, null);
        ButterKnife.bind(this, root);
        mTfLight = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");
        mChart = (BarChart) root.findViewById(R.id.chartInsulin);
        mChart.setDescription(" ");
        mChart.setMaxVisibleValueCount(7);
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
        leftAxis.setLabelCount(8, false);
        leftAxis.setSpaceTop(1);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setMaxWidth(1);
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

        Log.e(TAG, "Fit - onResume" );
        super.onResume();

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {
            if (isVisibleToUser) {

                datos();

            } else {

                Log.e(TAG, "Fit - Lista");

            }

        }
    }


    public void datos() {
        if (FitActivity.mDataPointList.size() != 0) {

            data = FitActivity.mDataPointList;

            int y = data.size();

            Log.e(TAG, "Fit - Valor" + y);

            StringBuilder dataValue = new StringBuilder();

            yData = new float[y];
            xData = new String[y];

            for (int i = 0; i < y; i++) {

                int v = 0;

                for (Field field : data.get(i).getDataType().getFields()) {
                    Value val = data.get(i).getValue(field);
                    String valor = val.toString();
                    float f = Float.parseFloat(valor);
                    Log.e(TAG, "Fit - " + val);
                    v = (int) f;

                }

                yData[i] = v;
                xData[i] = mSimpleDateFormat.format(data.get(i).getStartTime(TimeUnit.MILLISECONDS));

            }
            addData();

        } else {
            VaciarData();
        }
    }


    private void addData() {

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < yData.length; i++) {
            yVals1.add(new BarEntry(yData[i], i));
            Log.w(TAG, String.valueOf(yData[i]));

        }
        ArrayList<String> xVals2 = new ArrayList<String>();
        for (int i = 0; i < xData.length; i++) {
            Log.w(TAG, xData[i]);
            xVals2.add(xData[i]);

        }

        BarDataSet dataset = new BarDataSet(yVals1, "Calorias");


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



}
