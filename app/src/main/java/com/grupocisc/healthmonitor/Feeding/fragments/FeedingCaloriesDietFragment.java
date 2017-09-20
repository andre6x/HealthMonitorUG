package com.grupocisc.healthmonitor.Feeding.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;

import com.grupocisc.healthmonitor.Feeding.activities.FeedingActivity;
import com.grupocisc.healthmonitor.Feeding.activities.FeedingDietActivity;
import com.grupocisc.healthmonitor.R;
import com.satsuware.usefulviews.LabelledSpinner;

/**
 * Created by Walter on 01/02/2017.
 */

public class FeedingCaloriesDietFragment extends Fragment implements LabelledSpinner.OnItemChosenListener {

    private EditText txt_calorias;
    private String selectTextSpinner;
    View contenView;
    LabelledSpinner labelledSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contenView = inflater.inflate(R.layout.feeding_diet_fragment, container, false);
        setearItemsSpinner();
        FloatingActionButton fabDieta = (FloatingActionButton) contenView.findViewById(R.id.fabDieta);
        fabDieta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float calorias;

                if (selectTextSpinner.length() > 0) {
                    calorias = Float.parseFloat(selectTextSpinner);
                } else {
                    calorias = 0.0f;
                }
                if (calorias > 0.0f) {
                    Intent i = new Intent(getActivity(), FeedingDietActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putFloat("calories", calorias);
                    i.putExtras(bundle);
                    startActivity(i);
                } else {
                    Snackbar.make(v, "\n" + "Seleccione un valor de calorías", Snackbar.LENGTH_LONG)
                            .setAction("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            })
                            .setActionTextColor(getActivity().getResources().getColor(R.color.greenMenu))
                            .show();
                }

            }
        });

        return contenView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {
            if (isVisibleToUser) {
                FeedingActivity.fab.setVisibility(View.GONE);
            } else {
                FeedingActivity.fab.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {

    }

    @Override
    public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
        String selectedText = adapterView.getItemAtPosition(position).toString();
        if (position == 0) {
            selectTextSpinner = "0";
        } else {
            switch (labelledSpinner.getId()) {
                case R.id.spinner_caloria:

                    selectTextSpinner = selectedText;
                    break;
            }
        }
    }

    public void setearItemsSpinner() {
        labelledSpinner = (LabelledSpinner) contenView.findViewById(R.id.spinner_caloria);
        labelledSpinner.setColor(R.color.colorPrimaryDark);
        labelledSpinner.setItemsArray(R.array.feeding_caloria_array);
        labelledSpinner.setDefaultErrorEnabled(true);
        labelledSpinner.setDefaultErrorText("Seleccione el valor de las calorías");
        labelledSpinner.setOnItemChosenListener(this);
        labelledSpinner.setSelection(1, true);
    }
}
