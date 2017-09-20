package com.grupocisc.healthmonitor.Insulin.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grupocisc.healthmonitor.R;

/**
 * Created by Gems on 10/03/2017.
 */

public class InsulinConcept extends Fragment
{
    private static final String TAG = "[InsulinConcept]";
    private String paragraph;
    private TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        paragraph = getResources().getString(R.string.msg_InsulinParagraph);
//                "La insulina es como una llave que abre la cerradura de las puertas de las células del cuerpo " +
//                "para que la glucosa (azúcar en la sangre) pueda entrar y sea utilizada como energía. " +
//                "La insulina ayuda a la glucosa a entrar a las células del cuerpo. Si la glucosa no puede entrar en " +
//                "las células, se acumula en la sangre. Si se deja sin tratamiento, la " +
//                "acumulación de azúcar en la sangre pueden causar complicaciones a largo plazo.";

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.insulin_concept , viewGroup, false);

        textView = (TextView) view.findViewById(R.id.txt_texto);
        textView.setText(paragraph);

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.i(TAG,"onResume");
    }

}
