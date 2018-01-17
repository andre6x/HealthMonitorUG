package com.grupocisc.healthmonitor.login.fragments;


import android.app.ProgressDialog;
import android.graphics.Color;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.Insulin.TextValidator;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.SharedPreferencesManager;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.Utils.Utils.*;
import com.grupocisc.healthmonitor.entities.ISaveUser;
import com.grupocisc.healthmonitor.login.activities.LoginAccountActivity;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.grupocisc.healthmonitor.Utils.Utils.isNumeric;


public class LoginDataTwoFragment extends Fragment implements DatePickerDialog.OnDateSetListener{

    String TAG = "LoginDataTwoFragment";
    RecyclerView recyclerView;
    CardView BtnContinuar , BtnAtras;
    View view;
    TextView edt_anio ;
    EditText  edt_peso , edt_altura ;
    RadioGroup radioGroup;
    int idTipoDiabetes=0;
    int idGenero=0;
    String NameGenero = "" , medidaAltura= "" , medidaPeso = "", tdiabetes="";
    float max_altura = 0, min_altura = 0;
    float max_peso = 0, min_peso = 0;
    public ProgressDialog Dialog;
    private Call<ISaveUser.SaveUser> call_1;
    private ISaveUser.SaveUser mSaveUser;

    Spinner spinnerDiabetes;
    String[] tipoDiabetes = new String[]{
            "Tipo 1",
            "Tipo 2",
            "Gestacional",
            "Sin diabetes"
    };

    private CheckBox seleccionAsma;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.login_data_two_fragment, container, false);
        Dialog = new ProgressDialog(getActivity());

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        BtnContinuar = (CardView) view.findViewById(R.id.card_next);
        BtnAtras = (CardView) view.findViewById(R.id.card_atras);

        seleccionAsma = (CheckBox) view.findViewById(R.id.chkAsma);

        edt_anio = (TextView) view.findViewById(R.id.txt_fecha);
        edt_peso = (EditText) view.findViewById(R.id.edt_peso);
        max_peso = Float.parseFloat((getString(R.string.maximo_peso)));
        min_peso = Float.parseFloat((getString(R.string.minimo_peso)));
        edt_peso.addTextChangedListener(new TextValidator(edt_peso) {
            @Override
            public void validate(EditText editText, String text) {
                if (!text.isEmpty()) {
				    if ("-1".equals(validateEditText(edt_peso))) {
						edt_peso.setText("0.");
                        edt_peso.setSelection(2);
						if (Float.parseFloat(edt_peso.getText().toString()) > max_peso)
							edt_peso.setError("El valor de su Peso no puede ser mayor a "+max_peso+"kg. Por favor Verifique");
						if (Float.parseFloat(edt_peso.getText().toString()) < min_peso)
							edt_peso.setError("El valor de su Peso no puede ser menor a "+min_peso+"kg.  Por favor Verifique");
					} else {
						if (Float.parseFloat(text) > max_peso)
							edt_peso.setError("El valor de su Peso no puede ser mayor a "+max_peso+"kg. Por favor Verifique");
						if (Float.parseFloat(text) < min_peso)
							edt_peso.setError("El valor de su Peso no puede ser menor a "+min_peso+"kg.  Por favor Verifique");
					}
                }
            }
        });

        edt_altura = (EditText) view.findViewById(R.id.edt_altura);
        max_altura = Float.parseFloat((getString(R.string.maximo_altura)));
        min_altura = Float.parseFloat((getString(R.string.minimo_altura)));
        edt_altura.addTextChangedListener(new TextValidator(edt_altura) {
            @Override
            public void validate(EditText editText, String text) {

                if (!text.isEmpty()) {
                    if ("-1".equals(validateEditText(edt_altura))) {
                        edt_altura.setText("0.");
                        edt_altura.setSelection(2);
                        if (Float.parseFloat(edt_altura.getText().toString()) > max_altura)
                            edt_altura.setError("El valor de su Altura no puede ser mayor a "+max_altura+"m. Por favor Verifique");
                        if (Float.parseFloat(edt_altura.getText().toString()) < min_peso)
                            edt_altura.setError("El valor de su Altura no puede ser menor a "+min_altura+"m.  Por favor Verifique");
                    }else{
                    if (Float.parseFloat(text) > max_altura)
                        edt_altura.setError("El valor de su Altura no puede ser mayor a "+max_altura+"m. Por favor Verifique");
                    if (Float.parseFloat(text) < min_altura)
                        edt_altura.setError("El valor de su Altura no puede ser menor a "+min_altura+"m.  Por favor Verifique");
                    }
                }
            }
        });


        /* Initialize Radio Group and attach click handler */
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        radioGroup.clearCheck();

        setSpinner();
        setCampos();

        //boolean checked = ((RadioButton) view).isChecked();

        /* Attach CheckedChangeListener to radio group */
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if(null!=rb && checkedId > -1){
                    //Toast.makeText(getActivity(), rb.getText(), Toast.LENGTH_SHORT).show();
                    NameGenero = rb.getText().toString();
                    if (NameGenero.equals("Hombre")){
                        NameGenero="M";
                    }else{NameGenero="F";}
                }
            }
        });



        BtnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(camposLlenos()) {
                    callGuardarData();
                    ((LoginAccountActivity)getActivity()).callFragmentDataThree();
                }

            }
        });
        BtnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //llamar ametodo de lactivdad para reemplazar fragmento
                ((LoginAccountActivity)getActivity()).callFragmentDataOne();
            }
        });

        edt_anio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCalendar();
            }
        });

        return view;
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
	
	public String validateEditText(EditText editText) {
        String valor = "0";
        if (editText.getText().toString().length() > 0) {
            if (".".equals(editText.getText().toString())) {
                valor = "-1";
            } else {
                if (Float.parseFloat(editText.getText().toString()) < 0 ) {
                    valor = "-1";
                } else {
                    valor = editText.getText().toString();
                }
            }
        } else {
            valor = "-1";
        }
        return valor;
    }

    public void setSpinner(){


        spinnerDiabetes = (Spinner) view.findViewById(R.id.spinnerDiabetes);
        ArrayAdapter<String> spinnerArrayAdapterDiabetes = new ArrayAdapter<String>(getActivity(), R.layout.custom_textview_to_spinner,tipoDiabetes );
        spinnerArrayAdapterDiabetes.setDropDownViewResource(R.layout.custom_textview_to_spinner);
        spinnerDiabetes.setAdapter(spinnerArrayAdapterDiabetes);
        spinnerDiabetes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
                // TODO Auto-generated method stub
                //Toast.makeText(getActivity(), spinnerColorChange.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                tdiabetes = spinnerDiabetes.getSelectedItem().toString();

                if(tdiabetes.equals("Tipo 1")) {
                    idTipoDiabetes = 11;
                }
                //v3
                else if(tdiabetes.equals("Tipo 2")){
                    idTipoDiabetes = 12;
                }
                else if(tdiabetes.equals("Gestacional")){
                    idTipoDiabetes = 13;
                }
                else { //Sin diabetes
                    idTipoDiabetes = 14;
                }
//                else if{
//                    if(tdiabetes.equals("Tipo 2")){
//                        idTipoDiabetes = 12;
//                    }
//                    else{idTipoDiabetes = 13;}
//                }
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
        ((LoginAccountActivity)getActivity()).Anio = edt_anio.getText().toString();
        //((LoginAccountActivity)getActivity()).Peso = edt_peso.getText().toString() + medidaPeso;
        ((LoginAccountActivity)getActivity()).Peso = edt_peso.getText().toString();
        //((LoginAccountActivity)getActivity()).Altura = edt_altura.getText().toString()+medidaAltura;
        ((LoginAccountActivity)getActivity()).Altura = edt_altura.getText().toString();
        ((LoginAccountActivity)getActivity()).Sexo = NameGenero ;
        Log.e(TAG,"Sexo: "+NameGenero);
        ((LoginAccountActivity)getActivity()).TipoDiabetes = idTipoDiabetes;
        ((LoginAccountActivity)getActivity()).IsAsma = seleccionAsma.isChecked() ;

    }

    public void  setCampos(){
        edt_anio.setText( ((LoginAccountActivity)getActivity()).Anio );
        edt_peso.setText( ((LoginAccountActivity)getActivity()).Peso );
        edt_altura.setText( ((LoginAccountActivity)getActivity()).Altura );
        String sexo = ((LoginAccountActivity)getActivity()).Sexo ;
        if(sexo.equals(getString(R.string.txt_hombre) ))
            radioGroup.check(R.id.rdb_male);
        else if (sexo.equals(getString(R.string.txt_mujer)))
            radioGroup.check(R.id.rdb_female);
    }

    private boolean camposLlenos() {
        float peso= isNumeric(edt_peso.getText().toString())== true? Float.parseFloat(edt_peso.getText().toString()):0;
        float altura = isNumeric(edt_altura.getText().toString())== true? Float.parseFloat(edt_altura.getText().toString()):0;
        max_peso = Float.parseFloat((getString(R.string.maximo_peso)));
        min_peso = Float.parseFloat((getString(R.string.minimo_peso)));
        max_altura = Float.parseFloat((getString(R.string.maximo_altura)));
        min_altura = Float.parseFloat((getString(R.string.minimo_altura)));

        if (edt_anio.getText().toString() == null || edt_anio.getText().toString().length() == 0){
            generarAlerta("El campo AÑO es obligatorio");
            return false;
        }
        else if (edt_peso.getText().toString() == null || edt_peso.getText().toString().length() == 0){
            generarAlerta("El campo PESO es obligatorio");
            return false;
        }
        else if ( peso > max_peso || peso < min_peso) {
            generarAlerta("El campo PESO es incorrecto, por favor corregir para continuar con el registro.");
            return false;
        }
        else if (edt_altura.getText().toString() == null || edt_altura.getText().toString().length() == 0){
            generarAlerta("El campo ALTURA es obligatorio");
            return false;
        }
        else if ( altura > max_altura || altura < min_altura){
            generarAlerta("El campo ALTURA es incorrecto, por favor corregir para continuar con el registro.");
            return false;
        }
        else if ( NameGenero.equals("")){
            generarAlerta("Seleccione su sexo");
            return false;
        }else if ( tdiabetes.equals("")){
            generarAlerta("Seleccione su Tipo de Diabetes");
            return false;
        }

        return true;

    }

    private void generarAlerta(String mensaje){
        Utils.generarAlerta(getActivity(), getString(R.string.txt_error), mensaje);
    }

    //llamar calendario
    public void callCalendar(){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
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
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        //String date = ""+dayOfMonth+"/"+(++monthOfYear)+"/"+year;
        //Log.e(TAG,"year: "+ year);
        Calendar now = Calendar.getInstance();
        int anio = now.get(Calendar.YEAR);

        String mes = ""+((monthOfYear+1) < 10 ? "0"+(monthOfYear+1) : ""+(monthOfYear + 1));
        String dia =  dayOfMonth < 10 ? "0"+dayOfMonth : ""+dayOfMonth;
        int limite = anio - year;

        String date = ""+dia+"/"+mes+"/"+year;

        String fecmaximo = getString(R.string.maximo);
        int fec_max= Integer.parseInt(fecmaximo);
        String fecminimo = getString(R.string.minimo);
        int fec_min= Integer.parseInt(fecminimo);
        //Log.e(TAG,"Fuera IF:  Limite: "+ limite +"; fec_max: "+fec_max +"fec_min"+ fec_min);
        if ( year > anio ||  fec_min >= limite || fec_max <= limite) {
          //  Log.e(TAG,"Limite: "+ limite +"; fec_max: "+fec_max +"; fec_min"+fec_min);
            Utils.generarAlerta(getActivity(), getString(R.string.txt_atencion), getString(R.string.text_error_fecnac));
            edt_anio.setText(null);
        }else{
            edt_anio.setText(date);}
    }

}
