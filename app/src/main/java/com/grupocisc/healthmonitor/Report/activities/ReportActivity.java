package com.grupocisc.healthmonitor.Report.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.Manifest;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.EInsulin;
import com.grupocisc.healthmonitor.entities.IConsulPresion;
import com.grupocisc.healthmonitor.entities.ICunsulParamet;
import com.grupocisc.healthmonitor.entities.IGlucose;
import com.grupocisc.healthmonitor.entities.IMedicines;
import com.grupocisc.healthmonitor.entities.IAsthma;
import com.grupocisc.healthmonitor.entities.IPulse;
import com.grupocisc.healthmonitor.entities.IRegCrtMedicamentos;
import com.grupocisc.healthmonitor.entities.IWeight;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.AccessControlException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
import static com.grupocisc.healthmonitor.Utils.Utils.UcallCalendar;
import static com.grupocisc.healthmonitor.Utils.Utils.generarAlerta;
import static com.grupocisc.healthmonitor.Utils.Utils.isOnline;


public class ReportActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener{
    private String title = "INFORMES", TAG = "ReportActivity", email, parametro, opcion;
    // Identifier for the permission request
    public static final int MULTIPLE_PERMISSIONS = 10;
    private Document document;
    private  Call<List<ICunsulParamet.Objeto>> call_1;
    private  Call<List<ICunsulParamet.Objeto>> call_3;
    private  Call<List<ICunsulParamet.Objeto>> call_4;
    private  Call<List<ICunsulParamet.Objeto>> call_5;
    private Call<List<IConsulPresion.Objeto>> call_2;
    private static List<ICunsulParamet.Objeto> rowsObjeto;

    private static List<IConsulPresion.Objeto> rowsIPressure;
    private static List<IRegCrtMedicamentos.ConsulCtrlMedicamentos> rowsIMedicines;

    //Tablas de Documento
    private static PdfPTable tableInsulin ;
    private static PdfPTable tablePressure ;
    private static PdfPTable tablePeso ;
    private static PdfPTable tableMedicines ;
    private static PdfPTable tableAsthma ;
    private static PdfPTable tableGlucose ;
    private static List<EInsulin> rowsInsulin;
    private static List<IGlucose> rowsGlucose;
    private static List<IPulse> rowsPulse;
    private static List<IAsthma> rowsAsthma;
    private static List<IWeight> rowsWeight;

    private int cont = 0 , ListSize = 0;

    String[] permissions= new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    private static String NOMBRE_DOCUMENTO = "";

    private boolean fecha       //true-> Fecha inicial, false->Fecha final
            , flag                // true-> Individual, false->General
            , rango               // true-> Rango, false->Todo
            , enviar = false ;

    @BindView(R.id.rdbIndividual) RadioButton rdbIndividual;
    @BindView(R.id.rdbGeneral) RadioButton rdbGeneral;
    @BindView(R.id.rdbExportRango) RadioButton rdbExportRango;
    @BindView(R.id.rdbExportTodo) RadioButton rdbExportTodo;

    @BindView(R.id.txt_fechaini) TextView txt_fechaIni;
    @BindView(R.id.txt_fechafin) TextView txt_fechaFin;

    @BindView(R.id.lyt_fechaini) LinearLayout lytInicio;
    @BindView(R.id.lyt_fechafin) LinearLayout lytFin;
    @BindView(R.id.lyt_InformeRango) LinearLayout lytExportRango;
    @BindView(R.id.lyt_InformeTodo) LinearLayout lytExportTodo;

    @BindView(R.id.chkInsulin) CheckBox  chkInsulina;
    @BindView(R.id.chkGlucose) CheckBox  chkGlucosa;
    @BindView(R.id.chkPressureP) CheckBox chkPresionP;
    @BindView(R.id.chkPeso) CheckBox    chkPeso;
    @BindView(R.id.chkAsma) CheckBox   chkAsma;

    com.github.clans.fab.FloatingActionButton menu1,menu2;

    //Fuentes
    Font.FontFamily fuente = Font.FontFamily.HELVETICA;
    Font catFont = new Font(fuente, 22,Font.BOLD, BaseColor.BLUE);
    Font subFont = new Font(fuente, 16,Font.BOLD, BaseColor.DARK_GRAY);
    Font smallBold = new Font(fuente, 12,Font.BOLD, BaseColor.BLACK);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_activity);

        ButterKnife.bind(this);
        Utils.SetStyleToolbarTitle(this, title);

        NOMBRE_DOCUMENTO =Utils.getNombreFromPreference(this) + "_" + Utils.getApellidoFromPreference(this) +"_";

        //Floating Action Buttons
        menu1 = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.subFloatingAbrir) ;
        menu2 = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.subFloatingEnviar) ;

        email = Utils.getEmailFromPreference(this);

        rdbIndividual.setChecked(true);
        flag = true; //individual
        rdbExportRango.setChecked(true);
        rango = true; //por rango
        lytExportRango.setVisibility(View.VISIBLE);

        menu1.setOnClickListener(this);
        menu2.setOnClickListener(this);
        lytInicio.setOnClickListener(this);
        lytFin.setOnClickListener(this);
        rdbExportRango.setOnClickListener(this);
        rdbExportTodo.setOnClickListener(this);
        rdbIndividual.setOnClickListener(this);
        rdbGeneral.setOnClickListener(this);
        //Checkers
        chkInsulina.setOnClickListener(this);
        chkGlucosa.setOnClickListener(this);
        chkPresionP.setOnClickListener(this);
        chkPeso.setOnClickListener(this);
        chkAsma.setOnClickListener(this);

        inicializarFecha();

        if(checkPermissions()){
            Log.e(TAG,"Permisos otorgados");
        }
        else{
            Log.e(TAG,"Permisos denegados");
        }



    }
    @Override
    public void onStop(){
        super.onStop();


        rowsInsulin= null;
        rowsPulse= null;
        rowsAsthma= null;
        rowsGlucose= null;
        rowsIPressure= null;


        tableInsulin = null;
        tablePressure = null;
        tablePeso = null ;
        tableAsthma = null;
        tableGlucose = null;

    }
    /*PERMISOS DE ALMACENAMIENTO DE LA APLICACIÓN ************************************************/
    private  boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(ReportActivity.this,p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded
                    .toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }
    /*INICIALIZAR FECHA ************************************************/
    private void inicializarFecha() {
        //obtener fecha y hora de Calendario
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        //setear fecha
        String mes =  (month+1)< 10 ? "0"+(month+1) : ""+(month+1);
        String dia =  day < 10 ? "0"+day : ""+day;
        String date = "01"+"/"+mes+"/"+year;
        String date2 = dia+"/"+mes+"/"+year;

        txt_fechaIni.setText(date);
        txt_fechaFin.setText(date2);

    }

    private boolean validarfecha(String fechaI,String fechaF) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date fecha1 = sdf.parse(fechaI);
            Date fecha2 = sdf.parse(fechaF);

            if (fecha1.after(fecha2)) {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    //BACK ACTIVITY HOME
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String mes =  (monthOfYear+1)> 10 ? ""+ (monthOfYear+1) : "0"+ (monthOfYear+1);
        String dia =  dayOfMonth < 10 ? "0"+dayOfMonth : ""+dayOfMonth;
        String date = ""+dia+"/"+mes+"/"+year;
        String fechaI,fechaF;
        if(fecha){
            fechaI=date;
            fechaF=txt_fechaFin.getText().toString();
        }

        else{
            fechaI=txt_fechaIni.getText().toString();
            fechaF=date;
        }


        if(validarfecha(fechaI,fechaF)){
            if(fecha)
                txt_fechaIni.setText(date) ;
            else
                txt_fechaFin.setText(date) ;

        }else
        {
            Toast.makeText(this, "La fecha inicial es mayor a la fecha final", Toast.LENGTH_SHORT).show();

            Log.i(TAG, "onDateSet: fec inicial mayor a fec final");
            if(fecha)
                txt_fechaIni.setText(txt_fechaFin.getText().toString()) ;
            else
                txt_fechaFin.setText(txt_fechaIni.getText().toString()) ;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");
        if(dpd != null)
            dpd.setOnDateSetListener(this);
    }
    /* CALENDAR */
    public void callCalendar() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        UcallCalendar(dpd).show(getFragmentManager() , "Datepickerdialog");
    }
    /* EVENTO ONCLICK  ************************************************/
    @Override
    public void onClick(View v) {
        File path = getExternalFilesDir(null);
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = df.format(Calendar.getInstance().getTime());
        File file = null;
        if(rdbGeneral.isChecked())
            file = new File(path +  File.separator + NOMBRE_DOCUMENTO + formattedDate + "_general.pdf");
        else {
            if (rdbIndividual.isChecked() && chkInsulina.isChecked())
                file = new File(path + File.separator + NOMBRE_DOCUMENTO + formattedDate + "_insulina.pdf");
            if (rdbIndividual.isChecked() && chkGlucosa.isChecked())
                file = new File(path + File.separator + NOMBRE_DOCUMENTO + formattedDate + "_glucosa.pdf");
            if (rdbIndividual.isChecked() && chkPresionP.isChecked())
                file = new File(path + File.separator + NOMBRE_DOCUMENTO + formattedDate + "_pulso_y_presion.pdf");
            if (rdbIndividual.isChecked()  && chkPeso.isChecked()  )
                file = new File(path + File.separator + NOMBRE_DOCUMENTO + formattedDate + "_peso.pdf");
            if (rdbIndividual.isChecked() && chkAsma.isChecked())
                file = new File(path + File.separator + NOMBRE_DOCUMENTO + formattedDate + "_asma.pdf");
        }

        switch(v.getId()){
            case R.id.lyt_fechaini:{
                fecha = true;
                callCalendar();
            }
            break;
            case R.id.lyt_fechafin:{
                fecha = false;
                callCalendar();
            }
            break;
            case R.id.subFloatingAbrir:{

                enviar = false;
                if(rdbGeneral.isChecked()
                        && !chkGlucosa.isChecked()
                        && !chkAsma.isChecked()
                        && !chkInsulina.isChecked()
                        && !chkPeso.isChecked()
                        && !chkPresionP.isChecked()) {
                    return;
                }
                if(rdbIndividual.isChecked()
                        && !chkGlucosa.isChecked()
                        && !chkAsma.isChecked()
                        && !chkInsulina.isChecked()
                        && !chkPeso.isChecked()
                        && !chkPresionP.isChecked()) {
                    generarAlerta(this, "Error!", "Debe seleccionar al menos una opción.");
                    return;
                }
                if (file.exists())
                    file.delete();

                try {
                    generarPDF(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            break;
            //Enviar por Correo
            case R.id.subFloatingEnviar:
            {
                if(!isOnline(this)) {
                    Utils.generarAlerta(this, getString(R.string.txt_atencion), getString(R.string.sin_conexion));
                    return;
                }
                enviar = true;
                if(rdbGeneral.isChecked()
                        && !chkGlucosa.isChecked()
                        && !chkAsma.isChecked()
                        && !chkInsulina.isChecked()
                        && !chkPeso.isChecked()
                        && !chkPresionP.isChecked()) {
                    generarAlerta(this, "Error!", "Debe seleccionar al menos una opción.");
                    return;
                }
                if(rdbIndividual.isChecked()
                        && !chkGlucosa.isChecked()
                        && !chkAsma.isChecked()
                        && !chkInsulina.isChecked()
                        && !chkPeso.isChecked()
                        && !chkPresionP.isChecked()) {
                    generarAlerta(this, "Error!", "Debe seleccionar una opción.");
                    return;
                }

                if (!file.exists()) {
                    try {
                        generarPDF(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                enviar(file, "your@email.com" , "", ReportActivity.this);
            }
            break;
            //Tipo de Informe
            case R.id.rdbIndividual: {
                if(((RadioButton) v).isChecked())
                {
                    flag = true;
                    //chkEstado.setChecked(false);
                    chkPeso.setChecked(false);
                    chkPresionP.setChecked(false);
                    chkGlucosa.setChecked(false);
                    chkInsulina.setChecked(false);
                    chkAsma.setChecked(false);


                    chkPeso.setEnabled(true);
                    chkPresionP.setEnabled(true);
                    chkGlucosa.setEnabled(true);
                    chkInsulina.setEnabled(true);
                    chkAsma.setEnabled(true);
                }
            }break;
            case R.id.rdbGeneral:{
                if(((RadioButton) v).isChecked())
                {
                    flag = false;
                    chkPeso.setChecked(true);
                    chkPresionP.setChecked(true);
                    chkGlucosa.setChecked(true);
                    chkInsulina.setChecked(true);
                    chkAsma.setChecked(true);


                    chkPeso.setEnabled(false);
                    chkPresionP.setEnabled(false);
                    chkGlucosa.setEnabled(false);
                    chkInsulina.setEnabled(false);
                    chkAsma.setEnabled(false);

                }
            }break;
            /*Validaciones de los Check*/
            case R.id.chkInsulin :{
                if(flag && chkInsulina.isChecked() ) { //Individual
                    chkPeso.setChecked(false);
                    chkPresionP.setChecked(false);
                    chkGlucosa.setChecked(false);
                    chkAsma.setChecked(false);
                }
            }break;
            case R.id.chkGlucose :{
                if(flag && chkGlucosa.isChecked() ) { //Individual
                    chkPeso.setChecked(false);
                    chkPresionP.setChecked(false);
                    chkInsulina.setChecked(false);
                    chkAsma.setChecked(false);
                }
            }break;

            case R.id.chkPressureP :{
                if(flag && chkPresionP.isChecked() ) { //Individual
                    chkGlucosa.setChecked(false);
                    chkPeso.setChecked(false);
                    chkInsulina.setChecked(false);
                    chkAsma.setChecked(false);
                }
            }break;
            case R.id.chkPeso :{
                if(flag && chkPeso.isChecked() ) { //Individual
                    chkGlucosa.setChecked(false);
                    chkPresionP.setChecked(false);
                    chkInsulina.setChecked(false);
                    chkAsma.setChecked(false);
                }
            }break;
            case R.id.chkAsma :{
                if(flag && chkAsma.isChecked() ) { //Individual
                    chkGlucosa.setChecked(false);
                    chkPresionP.setChecked(false);
                    chkInsulina.setChecked(false);
                    chkPeso.setChecked(false);
                }
            }break;
            //Rango de Fechas
            case R.id.rdbExportRango:{
                if(((RadioButton) v.findViewById(R.id.rdbExportRango)).isChecked()) {
                    rango = true;
                    lytExportRango.setVisibility(View.VISIBLE);
                }
            }break;
            case R.id.rdbExportTodo:{
                if(((RadioButton) v.findViewById(R.id.rdbExportTodo)).isChecked()){
                    rango = false;
                    lytExportRango.setVisibility(View.GONE);
                }
            }break;
        } //End switch
    }
    /* Procedimiento para mostrar el documento PDF generado ************************************************/
    public void showPdfFile(File filePath, Context context){
        Toast.makeText(context, "Abriendo PDF.", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", filePath),"application/pdf");

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION);
        try {
            context.startActivity(intent);
        }
        catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No Application Available to View PDF", Toast.LENGTH_SHORT).show();
        }
    }
    /* Procedimiento para enviar por email el documento PDF generado ************************************************/
    public void enviar(File fileName, String emailTo, String emailCC, Context context){

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Informe - Paciente: "
                + Utils.getApellidoFromPreference(this) + " "
                + Utils.getNombreFromPreference(this) );
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Informe generado por Proyecto APP - Salud Diabetes");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailTo});
        emailIntent.putExtra(Intent.EXTRA_BCC, new String[]{emailCC});
        //Adjuntar documento
        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", new File(fileName.getPath()));
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        emailIntent.setType("application/pdf");

        try {
            startActivity(Intent.createChooser(emailIntent, "Enviar email..."));
            finish();
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(this, "No tienes clientes de email instalados.", Toast.LENGTH_SHORT).show();
        }

    }
    /* Añade líneas al documento */
    private void addLine(Document document, int cant)
    {
        try {
            for (int i= 0 ; i < cant ; i++ )
                document.add(new Paragraph(new Phrase(Chunk.NEWLINE)));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
    /* GENERA EL DOCUMENTOS */
    public void generarPDF(File myFile) throws FileNotFoundException, AccessControlException {
        //Fecha ConsultaIndv completo
        Calendar c = Calendar.getInstance();
        String mes = (c.get(Calendar.MONTH) + 1) < 10 ? "0" + (c.get(Calendar.MONTH) + 1) : "" + (c.get(Calendar.MONTH) + 1) ;
        String dia = "" + c.getActualMaximum(Calendar.DAY_OF_MONTH);
        String anio = "" + c.get(Calendar.YEAR);
        String dateIni = anio + "/" + mes + "/01 00:00:00" ;
        String dateFin = anio + "/" + mes + "/" + dia + " 23:59:59";


        //Fecha ConsultaIndv Rango
        String fechaIni = txt_fechaIni.getText().toString().substring(6, 10) +
                "/" + txt_fechaIni.getText().toString().substring(3, 5) +
                "/" + txt_fechaIni.getText().toString().substring(0, 2) + " 00:00:00";
        String fechaFin = txt_fechaFin.getText().toString().substring(6, 10) +
                "/" + txt_fechaFin.getText().toString().substring(3, 5) +
                "/" + txt_fechaFin.getText().toString().substring(0, 2) + " 23:59:59";

        //Generar tabla para cada uno de los modulos Individual
        if(flag){
            if(chkInsulina.isChecked()) {
                parametro = "insulina";
                if(rango)
                    ConsultaIndv(parametro, fechaIni, fechaFin, myFile);
                else
                    ConsultaIndv(parametro, dateIni, dateFin, myFile);
            }
            if(chkGlucosa.isChecked()){
                parametro = "glucosa";
                if(rango)
                    ConsultaIndv(parametro, fechaIni, fechaFin, myFile);
                else
                    ConsultaIndv(parametro, dateIni, dateFin, myFile);
            }
            if(chkPresionP.isChecked()){
                parametro = "presion";
                if(rango)
                    ConsultaIndv(parametro, fechaIni, fechaFin, myFile);
                else
                    ConsultaIndv(parametro, dateIni, dateFin, myFile);
            }
            if(chkPeso.isChecked()){
                parametro = "peso";
                if(rango)
                    ConsultaIndv(parametro, fechaIni, fechaFin, myFile);
                else
                    ConsultaIndv(parametro, dateIni, dateFin, myFile);
            }
            if(chkAsma.isChecked()){
                parametro = "asma";
                if(rango)
                    ConsultaIndv(parametro, fechaIni, fechaFin, myFile);
                else
                    ConsultaIndv(parametro, dateIni, dateFin, myFile);
            }

        }
        else{ //Generar tabla para cada uno de los modulos General
            //Ya que la consulta es de todos
            //OOOJOOOOO!!!!!
            if(rango)
                ConsultaGral("insulina" , fechaIni, fechaFin, myFile);
            else
                ConsultaGral("insulina" , dateIni, dateFin, myFile);
        }
    }
    private void ConsultaGral(String s,final String fechaIni, final String fechaFin, final File myFile) throws FileNotFoundException {

        String suba = fechaIni.substring(0, 4);
        String subm  = fechaIni.substring(5, 7);
        String subd = fechaIni.substring(8, 10);
        String subaf = fechaFin.substring(0, 4);
        String submf  = fechaFin.substring(5, 7);
        String subdf = fechaFin.substring(8, 10);
        String FeIniV = subd+"/"+subm+"/"+suba;
        String FeFinV = subdf+"/"+submf+"/"+subaf;
        String FechaI = subd+"/"+subm+"/"+ suba+" "+"00:00.0";
        String FechaF = subdf+"/"+submf+"/"+ subaf+" "+"23:59.0";
        switch (s) {

            case ("insulina"): {
                Log.e(TAG, "ENTRA AL INSULINA DEL PDF");
                try {
                    Log.e(TAG, "ENTRA AL INSULINA DEL PDF" + FechaI + "=="+ FechaF);
                    //validar si en la tabla ahi datos mayor a 0
                    if (Utils.GetInsulinFromDatabasePDF(HealthMonitorApplicattion.getApplication().getInsulinDao(), FechaI, FechaF).size() > 0) {
                        //asignamos datos de la tabla a la lista de objeto
                        Log.i(TAG, "fechas: " + fechaIni + fechaFin);


                        rowsInsulin = Utils.GetInsulinFromDatabasePDF(HealthMonitorApplicattion.getApplication().getInsulinDao(), FechaI, FechaF);
                        genTableDocGral("insulina", myFile, fechaIni, fechaFin);
                        //obtenemos el tamaño de la listaDEobjetos , para recorrerlo
                        //y presenta los datos de la tabla bd en el LOG
                        int tamaño = rowsInsulin.size();
                        for (int i = 0; i < tamaño; i++) {
                            // Log.e(TAG,"Glucosa:" + rowsGlucose.get(i).getEnviadoServer() +"-" + rowsGlucose.get(i).getConcentracion()+"/"+" -fecha:"+rowsGlucose.get(i).getFecha() );
                        }

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


            }
            break;
            case ("glucosa"): {
                try {
                    //validar si en la tabla ahi datos mayor a 0
                    if (Utils.GetGlucoseFromDatabasePDF(HealthMonitorApplicattion.getApplication().getGlucoseDao(), FeIniV, FeFinV).size() > 0) {
                        //asignamos datos de la tabla a la lista de objeto
                        Log.i(TAG, "fechas: " + fechaIni + fechaFin);

                        rowsGlucose = Utils.GetGlucoseFromDatabasePDF(HealthMonitorApplicattion.getApplication().getGlucoseDao(), FeIniV, FeFinV);
                        genTableDocGral("glucosa", myFile, fechaIni, fechaFin);
                        Log.e(TAG, "entra al generador");
                        //obtenemos el tamaño de la listaDEobjetos , para recorrerlo
                        //y presenta los datos de la tabla bd en el LOG
                        int tamaño = rowsGlucose.size();
                        for (int i = 0; i < tamaño; i++) {
                            Log.e(TAG, "Glucosa:" + rowsGlucose.get(i).getEnviadoServer() + "-" + rowsGlucose.get(i).getConcentracion() + "/" + " -fecha:" + rowsGlucose.get(i).getFecha());
                        }

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
            break;
            case ("presion"): {
                //Log.e(TAG,"ENTRA AL PRESION-PULSO DEL PDF");
                try {
                    //validar si en la tabla ahi datos mayor a 0
                    if (Utils.GetPulseFromDatabasePDF(HealthMonitorApplicattion.getApplication().getPulseDao(), FeIniV, FeFinV).size() > 0) {
                        //asignamos datos de la tabla a la lista de objeto
                        //      Log.e(TAG,"ENTRA AL IF Pulso-Presion DEL PDF");
                        rowsPulse = Utils.GetPulseFromDatabasePDF(HealthMonitorApplicattion.getApplication().getPulseDao(), FeIniV, FeFinV);
                        genTableDocGral("presion", myFile, fechaIni, fechaFin);
                        //obtenemos el tamaño de la listaDEobjetos , para recorrerlo
                        //y presenta los datos de la tabla bd en el LOG
                        int tamaño = rowsPulse.size();
                        for (int i = 0; i < tamaño; i++) {
                            Log.e(TAG, "Pulso:" + rowsPulse.get(i).getEnviadoServer() + "-" + rowsPulse.get(i).getConcentracion() + "/" + " -fecha:" + rowsPulse.get(i).getFecha());
                        }

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
            break;
            case ("asma"): {
                Log.e(TAG, "ENTRA AL ASMA DEL PDF");
                try {
                    //validar si en la tabla ahi datos mayor a 0
                    if (Utils.GetAsthmaFromDatabasePDF(HealthMonitorApplicattion.getApplication().getAsthmaDao(), FeIniV, FeFinV).size() > 0) {
                        //asignamos datos de la tabla a la lista de objeto
                        Log.e(TAG, "ENTRA AL IF ASMA DEL PDF");
                        rowsAsthma = Utils.GetAsthmaFromDatabasePDF(HealthMonitorApplicattion.getApplication().getAsthmaDao(), FeIniV, FeFinV);
                        genTableDocGral("asma", myFile, fechaIni, fechaFin);
                        //obtenemos el tamaño de la listaDEobjetos , para recorrerlo
                        //y presenta los datos de la tabla bd en el LOG
                        int tamaño = rowsAsthma.size();
                        for (int i = 0; i < tamaño; i++) {
                            //  Log.e(TAG,"Pulso:" + rowsAsthma.get(i).getEnviadoServer() +"-" + rowsAsthma.get(i).getConcentracion()+"/"+" -fecha:"+rowsPulse.get(i).getFecha() );
                        }

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
            break;
            case ("peso"): {
                Log.e(TAG, "ENTRA AL PESO DEL PDF");
                try {
                    //validar si en la tabla ahi datos mayor a 0
                    if (Utils.GetWeightFromDatabasePDF(HealthMonitorApplicattion.getApplication().getWeightDao(), FeIniV, FeFinV).size() > 0) {
                        //asignamos datos de la tabla a la lista de objeto
                        rowsWeight = Utils.GetWeightFromDatabasePDF(HealthMonitorApplicattion.getApplication().getWeightDao(), FeIniV, FeFinV);
                        genTableDocGral("peso", myFile, fechaIni, fechaFin);
                        //obtenemos el tamaño de la listaDEobjetos , para recorrerlo
                        //y presenta los datos de la tabla bd en el LOG
                        int tamaño = rowsWeight.size();
                        for (int i = 0; i < tamaño; i++) {
                            //  Log.e(TAG,"Pulso:" + rowsAsthma.get(i).getEnviadoServer() +"-" + rowsAsthma.get(i).getConcentracion()+"/"+" -fecha:"+rowsPulse.get(i).getFecha() );
                        }

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


            }
        }
    }
    /* Llenar las Tablas Consulta General */
    private void genTableDocGral(String parametro, File myFile, String fechaIni, String fechaFin) throws FileNotFoundException {
        cont = 0;
        Log.e(TAG,"entra a gentable");

        if(parametro == "insulina"){
            ListSize = rowsInsulin.size();
            tableInsulin = new PdfPTable(3);
            //Titulos
            tableInsulin.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableInsulin.addCell(new Paragraph("Fecha", smallBold));
            tableInsulin.addCell(new Paragraph("Dosis", smallBold));
            tableInsulin.addCell(new Paragraph("Observación", smallBold));
            //Datos
            do {
                tableInsulin.addCell(rowsInsulin.get(cont).getFecha() + " " + rowsInsulin.get(cont).getHora());
                tableInsulin.addCell(rowsInsulin.get(cont).getInsulina()+ " u.");
                tableInsulin.addCell(rowsInsulin.get(cont).getObservacion());
                cont++;
            } while (cont < ListSize);
            ConsultaGral("glucosa", fechaIni, fechaFin, myFile);
        }
        if(parametro == "glucosa"){
            Log.e(TAG,"llena la tabla glucosa principio");
            ListSize = rowsGlucose.size();
            tableGlucose = new PdfPTable(3);
            Log.e(TAG,"llena la tabla glucosa principio llena");
            //Titulos
            tableGlucose.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableGlucose.addCell(new Paragraph("Fecha", smallBold));
            tableGlucose.addCell(new Paragraph("Glucosa", smallBold));
            tableGlucose.addCell(new Paragraph("Observación", smallBold));
            Log.e(TAG,"hace titulos");
            //Datos
            do{
                tableGlucose.addCell(rowsGlucose.get(cont).getFecha());
                tableGlucose.addCell(rowsGlucose.get(cont).getConcentracion() + " mg/dl");
                tableGlucose.addCell(rowsGlucose.get(cont).getObservacion());
                cont++;
                Log.e(TAG,"llena campos");
            }while (cont < ListSize);
            ConsultaGral("presion", fechaIni, fechaFin, myFile);
            Log.e(TAG,"llena la tabla glucosa");
        }
        if(parametro=="peso"){

            ListSize = rowsWeight.size();
            tablePeso = new PdfPTable(8);
            //Títulos
            tablePeso.setHorizontalAlignment(Element.ALIGN_LEFT);
            tablePeso.addCell(new Paragraph("Fecha", smallBold));
            tablePeso.addCell(new Paragraph("Peso", smallBold));
            tablePeso.addCell(new Paragraph("Masa Muscular", smallBold));
            tablePeso.addCell(new Paragraph("TMB", smallBold));
            tablePeso.addCell(new Paragraph("DMO", smallBold));
            tablePeso.addCell(new Paragraph("PORCENTAJE DE AGUA", smallBold));
            tablePeso.addCell(new Paragraph("PORCENTAJE DE GRASA", smallBold));
            tablePeso.addCell(new Paragraph("OBSERVACION", smallBold));
            //Datos
            do{
                tablePeso.addCell(rowsWeight.get(cont).getFecha());
                tablePeso.addCell(rowsWeight.get(cont).getPeso()+" Kg. ");
                tablePeso.addCell(rowsWeight.get(cont).getMasamuscular()+"%");
                tablePeso.addCell(rowsWeight.get(cont).getTmb()+"%");
                tablePeso.addCell(rowsWeight.get(cont).getDmo()+"%");
                tablePeso.addCell(rowsWeight.get(cont).getPorcentajeAgua()+"%");
                tablePeso.addCell(rowsWeight.get(cont).getPorcentajeGrasa()+"%");
                tablePeso.addCell(rowsWeight.get(cont).getObservacion()+"");
                cont++;
            }while (cont < ListSize);
            ConsultaGral("asma", fechaIni, fechaFin, myFile);

        }
        if(parametro ==  "asma") {
            ListSize = rowsAsthma.size();
            tableAsthma = new PdfPTable(3);
            //Títulos
            tableAsthma.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableAsthma.addCell(new Paragraph("Fecha", subFont));
            tableAsthma.addCell(new Paragraph("Flujo Máximo", subFont));
            tableAsthma.addCell(new Paragraph("Observación", subFont));
            //Datos
            do {
                tableAsthma.addCell(rowsAsthma.get(cont).getFecha());
                tableAsthma.addCell(rowsAsthma.get(cont).getFlujoMaximo() + " I/min ");
                tableAsthma.addCell(rowsAsthma.get(cont).getObservacion());
                cont++;
            } while (cont < ListSize);
        }

        //Pulso presion

        if(parametro == "presion") {
            ListSize = rowsPulse.size();
            tablePressure = new PdfPTable(6);
            //Títulos
            tablePressure.setHorizontalAlignment(Element.ALIGN_LEFT);
            tablePressure.addCell(new Paragraph("Fecha", subFont));
            tablePressure.addCell(new Paragraph("Pulso", subFont));
            tablePressure.addCell(new Paragraph("Máxima Presión", subFont));
            tablePressure.addCell(new Paragraph("Mínima Presión", subFont));
            tablePressure.addCell(new Paragraph("Estado", subFont));
            tablePressure.addCell(new Paragraph("Observacion", subFont));
            //Datos
            do {
                tablePressure.addCell(rowsPulse.get(cont).getFecha());
                tablePressure.addCell(rowsPulse.get(cont).getConcentracion() +"");
                tablePressure.addCell(rowsPulse.get(cont).getMaxPressure() + "");
                tablePressure.addCell(rowsPulse.get(cont).getMinPressure() + "");
                tablePressure.addCell(rowsPulse.get(cont).getMedido() + "");
                tablePressure.addCell(rowsPulse.get(cont).getObservacion() + "");
                Log.e(TAG, "llena presion tabla");

                cont++;
            } while (cont < ListSize);
            Log.e(TAG,"llena la tabla presion");
            ConsultaGral("peso", fechaIni, fechaFin, myFile);
        }

            if(tableGlucose == null && tableAsthma == null && tablePressure == null && tableInsulin == null&& tablePeso == null)
                Toast.makeText(this, "No existen datos a generar", Toast.LENGTH_SHORT).show();
            else
                generaDocumento("general", myFile);

        }

    /* REALIZA LAS CONSULTAS AL SERVIDOR ****************************************************/
    private void ConsultaIndv(final String parametro, String fechaIni, String fechaFin, final File fileName)
    {


        String suba = fechaIni.substring(0, 4);
        String subm  = fechaIni.substring(5, 7);
        String subd = fechaIni.substring(8, 10);
        String subaf = fechaFin.substring(0, 4);
        String submf  = fechaFin.substring(5, 7);
        String subdf = fechaFin.substring(8, 10);
        String FeIniV = subd+"/"+subm+"/"+suba;
        String FeFinV = subdf+"/"+submf+"/"+subaf;

        String FechaI = subd+"/"+subm+"/"+ suba+" "+"00:00.0";
        String FechaF = subdf+"/"+submf+"/"+ subaf+" "+"23:59.0";

        //Si es consulta Individual
        if(parametro == "insulina") {
            Log.e(TAG,"ENTRA AL INSULINA DEL PDF");
            try {

                                //validar si en la tabla ahi datos mayor a 0
                if (Utils.GetInsulinFromDatabasePDF(HealthMonitorApplicattion.getApplication().getInsulinDao(), FechaI, FechaF).size() > 0 ){
                    //asignamos datos de la tabla a la lista de objeto
                    //Log.e(TAG,"ENTRA AL IF GLUCOSA DEL PDF");
                    rowsInsulin = Utils.GetInsulinFromDatabasePDF(HealthMonitorApplicattion.getApplication().getInsulinDao(), FechaI, FechaF);
                    genTableDocInd(parametro, fileName);
                    //obtenemos el tamaño de la listaDEobjetos , para recorrerlo
                    //y presenta los datos de la tabla bd en el LOG
                    int tamaño = rowsInsulin.size();
                    for(int i = 0 ; i < tamaño ; i++){
                       // Log.e(TAG,"Glucosa:" + rowsGlucose.get(i).getEnviadoServer() +"-" + rowsGlucose.get(i).getConcentracion()+"/"+" -fecha:"+rowsGlucose.get(i).getFecha() );
                    }

                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
             else if(parametro == "glucosa"){
            Log.e(TAG,"ENTRA AL GLUCOSA DEL PDF");
            try {
                 //validar si en la tabla ahi datos mayor a 0

                if (Utils.GetGlucoseFromDatabasePDF(HealthMonitorApplicattion.getApplication().getGlucoseDao(), FeIniV, FeFinV).size() > 0 ){
                    //asignamos datos de la tabla a la lista de objeto
                    rowsGlucose = Utils.GetGlucoseFromDatabasePDF(HealthMonitorApplicattion.getApplication().getGlucoseDao(), FeIniV, FeFinV);
                    genTableDocInd(parametro, fileName);
                    //obtenemos el tamaño de la listaDEobjetos , para recorrerlo
                    //y presenta los datos de la tabla bd en el LOG
                    int tamaño = rowsGlucose.size();
                    for(int i = 0 ; i < tamaño ; i++){
                        Log.e(TAG,"Glucosa:" + rowsGlucose.get(i).getEnviadoServer() +"-" + rowsGlucose.get(i).getConcentracion()+"/"+" -fecha:"+rowsGlucose.get(i).getFecha() );
                    }

                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
               e.printStackTrace();
            }

        }
        else if(parametro == "presion"){

            Log.e(TAG,"ENTRA AL PRESION DEL PDF");
            try {
                Log.e(TAG,"ENTRA AL Pulse-Presion DEL PDF");
                //validar si en la tabla ahi datos mayor a 0
                if (Utils.GetPulseFromDatabasePDF(HealthMonitorApplicattion.getApplication().getPulseDao(), FeIniV, FeFinV ).size() > 0 ){
                    //asignamos datos de la tabla a la lista de objeto
                    Log.e(TAG,"ENTRA AL IF Pulse-Presion DEL PDF");
                    rowsPulse = Utils.GetPulseFromDatabasePDF(HealthMonitorApplicattion.getApplication().getPulseDao(), FeIniV, FeFinV);
                    genTableDocInd(parametro, fileName);
                    //obtenemos el tamaño de la listaDEobjetos , para recorrerlo
                    //y presenta los datos de la tabla bd en el LOG
                    int tamaño = rowsPulse.size();
                    for(int i = 0 ; i < tamaño ; i++){
                        Log.e(TAG,"Pulso:" + rowsPulse.get(i).getEnviadoServer() +"-" + rowsPulse.get(i).getConcentracion()+"/"+" -fecha:"+rowsPulse.get(i).getFecha() );
                    }

                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }else if(parametro == "asma"){

            Log.e(TAG,"ENTRA AL ASMA DEL PDF");
            try {
                //validar si en la tabla ahi datos mayor a 0
                if (Utils.GetAsthmaFromDatabasePDF(HealthMonitorApplicattion.getApplication().getAsthmaDao(), FeIniV, FeFinV).size() > 0 ){
                    //asignamos datos de la tabla a la lista de objeto
                    Log.e(TAG,"ENTRA AL IF ASMA DEL PDF");
                    rowsAsthma = Utils.GetAsthmaFromDatabasePDF(HealthMonitorApplicattion.getApplication().getAsthmaDao(), FeIniV, FeFinV);
                    genTableDocInd(parametro, fileName);
                    //obtenemos el tamaño de la listaDEobjetos , para recorrerlo
                    //y presenta los datos de la tabla bd en el LOG
                    int tamaño = rowsAsthma.size();
                    for(int i = 0 ; i < tamaño ; i++){
                      //  Log.e(TAG,"Pulso:" + rowsAsthma.get(i).getEnviadoServer() +"-" + rowsAsthma.get(i).getConcentracion()+"/"+" -fecha:"+rowsPulse.get(i).getFecha() );
                    }

                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else if(parametro == "peso"){

            Log.e(TAG,"ENTRA AL PESO DEL PDF");
            try {
                //validar si en la tabla ahi datos mayor a 0
                if (Utils.GetWeightFromDatabasePDF(HealthMonitorApplicattion.getApplication().getWeightDao(), FeIniV, FeFinV ).size() > 0 ){
                    //asignamos datos de la tabla a la lista de objeto
                    Log.e(TAG,"ENTRA AL IF ASMA DEL PDF");
                    rowsWeight = Utils.GetWeightFromDatabasePDF(HealthMonitorApplicattion.getApplication().getWeightDao(), FeIniV, FeFinV );
                    genTableDocInd(parametro, fileName);
                    //obtenemos el tamaño de la listaDEobjetos , para recorrerlo
                    //y presenta los datos de la tabla bd en el LOG
                    int tamaño = rowsWeight.size();
                    for(int i = 0 ; i < tamaño ; i++){
                        //  Log.e(TAG,"Pulso:" + rowsAsthma.get(i).getEnviadoServer() +"-" + rowsAsthma.get(i).getConcentracion()+"/"+" -fecha:"+rowsPulse.get(i).getFecha() );
                    }

                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    /* LLENAR LA TABLA PARA EL PDF **********************************************/
    private void genTableDocInd(String parametro, File fileName) throws FileNotFoundException {
        cont = 0;

        switch (parametro){
            case ("insulina"):
            {
                ListSize = rowsInsulin.size();
                tableInsulin = new PdfPTable(3);
                //Titulos
                tableInsulin.setHorizontalAlignment(Element.ALIGN_LEFT);
                tableInsulin.addCell(new Paragraph("Fecha", smallBold));
                tableInsulin.addCell(new Paragraph("Dosis", smallBold));
                tableInsulin.addCell(new Paragraph("Observación", smallBold));
                //Datos
                do {
                    Log.i(TAG, "genTableDocInd: " + rowsInsulin.get(cont).toString());
                    tableInsulin.addCell(rowsInsulin.get(cont).getFecha() + " " + rowsInsulin.get(cont).getHora() );
                    tableInsulin.addCell(rowsInsulin.get(cont).getInsulina()+ " u.");
                    tableInsulin.addCell(rowsInsulin.get(cont).getObservacion());
                    cont++;
                } while (cont < ListSize);
            }
            break;
            case ("glucosa"):
            {
                ListSize = rowsGlucose.size();
                tableGlucose = new PdfPTable(3);
                //Titulos
                tableGlucose.setHorizontalAlignment(Element.ALIGN_LEFT);
                tableGlucose.addCell(new Paragraph("Fecha", smallBold));
                tableGlucose.addCell(new Paragraph("Glucosa", smallBold));
                tableGlucose.addCell(new Paragraph("Observación", smallBold));
                //Datos
                do{
                    tableGlucose.addCell(rowsGlucose.get(cont).getFecha());
                    tableGlucose.addCell(rowsGlucose.get(cont).getConcentracion() + " mg/dl");
                    tableGlucose.addCell(rowsGlucose.get(cont).getObservacion());
                    cont++;
                }while (cont < ListSize);
            }
            break;
            case("asma"):
            {
                ListSize = rowsAsthma.size();
                tableAsthma = new PdfPTable(3);
                //Títulos
                tableAsthma.setHorizontalAlignment(Element.ALIGN_LEFT);
                tableAsthma.addCell(new Paragraph("Fecha", subFont));
                tableAsthma.addCell(new Paragraph("Flujo Máximo", subFont));
                tableAsthma.addCell(new Paragraph("Observación", subFont));
                //Datos
                do {
                    tableAsthma.addCell(rowsAsthma.get(cont).getFecha());
                    tableAsthma.addCell(rowsAsthma.get(cont).getFlujoMaximo() + " I/min");
                    tableAsthma.addCell(rowsAsthma.get(cont).getObservacion());
                    cont++;
                } while (cont < ListSize);
            }
            break;
            case("peso"):
            {
                ListSize = rowsWeight.size();
                tablePeso = new PdfPTable(8);
                //Títulos
                tablePeso.setHorizontalAlignment(Element.ALIGN_LEFT);
                tablePeso.addCell(new Paragraph("Fecha", smallBold));
                tablePeso.addCell(new Paragraph("Peso", smallBold));
                tablePeso.addCell(new Paragraph("Masa Muscular", smallBold));
                tablePeso.addCell(new Paragraph("TMB", smallBold));
                tablePeso.addCell(new Paragraph("DMO", smallBold));
                tablePeso.addCell(new Paragraph("PORCENTAJE DE AGUA", smallBold));
                tablePeso.addCell(new Paragraph("PORCENTAJE DE GRASA", smallBold));
                tablePeso.addCell(new Paragraph("OBSERVACION", smallBold));
                //Datos
                do{
                    tablePeso.addCell(rowsWeight.get(cont).getFecha());
                    tablePeso.addCell(rowsWeight.get(cont).getPeso()+" Kg. ");
                    tablePeso.addCell(rowsWeight.get(cont).getMasamuscular()+"%");
                    tablePeso.addCell(rowsWeight.get(cont).getTmb()+"%");
                    tablePeso.addCell(rowsWeight.get(cont).getDmo()+"%");
                    tablePeso.addCell(rowsWeight.get(cont).getPorcentajeAgua()+"%");
                    tablePeso.addCell(rowsWeight.get(cont).getPorcentajeGrasa()+"%");
                    tablePeso.addCell(rowsWeight.get(cont).getObservacion()+"");
                    cont++;
                }while (cont < ListSize);
            }
            break;
            case("presion"):
            {
                ListSize = rowsPulse.size();
                tablePressure = new PdfPTable(6);
                //Títulos
                tablePressure.setHorizontalAlignment(Element.ALIGN_LEFT);
                tablePressure.addCell(new Paragraph("Fecha", subFont));
                tablePressure.addCell(new Paragraph("Pulso", subFont));
                tablePressure.addCell(new Paragraph("Máxima Presión", subFont));
                tablePressure.addCell(new Paragraph("Mínima Presión", subFont));
                tablePressure.addCell(new Paragraph("Estado", subFont));
                tablePressure.addCell(new Paragraph("Observacion", subFont));
                //Datos
                do {
                    tablePressure.addCell(rowsPulse.get(cont).getFecha());
                    tablePressure.addCell(rowsPulse.get(cont).getConcentracion() +"");
                    tablePressure.addCell(rowsPulse.get(cont).getMaxPressure() + "");
                    tablePressure.addCell(rowsPulse.get(cont).getMinPressure() + "");
                    tablePressure.addCell(rowsPulse.get(cont).getMedido() + "");
                    tablePressure.addCell(rowsPulse.get(cont).getObservacion() + "");
                    cont++;
                } while (cont < ListSize);
            }
            break;

        }
        generaDocumento(parametro, fileName);
    }
    /* MANDA A LLENAR EL DOCUMENTO CON LA INFORMACION  ***********************************/
    public void generaDocumento(String parametro, File myFile) throws FileNotFoundException {
        try {
            OutputStream output = new FileOutputStream(myFile.toString());
            document = new Document(PageSize.LETTER);
            PdfWriter.getInstance(document, output);
            document.open();
            document.addAuthor("Proyecto APP - Salud Diabetes");
            document.addTitle("Informe de Controles de Salud");
            document.add(new Paragraph("INFORME DE CONTROL DE SALUD", catFont));
            document.add(new Paragraph("Paciente: "
                    + Utils.getApellidoFromPreference(this) + " "
                    + Utils.getNombreFromPreference(this)));

            //Llenar con las tablas
            if(parametro == "insulina")
            {
                try {
                    document.add(new Paragraph("INSULINA",subFont));
                    addLine(document,1);
                    if(tableInsulin == null) {
                        document.add(new Paragraph("No existen datos."));
                    }
                    else {
                        document.add(tableInsulin);
                    }
                    addLine(document,2);

                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
            if(parametro == "glucosa")
            {
                try {
                    document.add(new Paragraph("GLUCOSA",subFont));
                    addLine(document,1);
                    if(tableGlucose == null)
                        document.add(new Paragraph("No existen datos."));
                    else
                        document.add(tableGlucose);
                    addLine(document,2);

                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
            if(parametro == "presion")
            {
                try {
                    document.add(new Paragraph("PULSO CARDIACO Y PRESIÓN ARTERIAL",subFont));
                    addLine(document,1);
                    if(tablePressure == null)
                        document.add(new Paragraph("No existen datos."));
                    else
                        document.add(tablePressure);
                    addLine(document,2);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
            if(parametro == "peso")
            {
                try {
                    document .add(new Paragraph("PULSO CARDIACO",subFont));
                    addLine(document,1);
                    if(tablePeso == null)
                        document.add(new Paragraph("No existen datos."));
                    else
                        document.add(tablePeso);
                    addLine(document,2);

                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
            if(parametro == "asma"){
                try {
                    document .add(new Paragraph("ASMA",subFont));
                    addLine(document,1);
                    if(tableAsthma == null)
                        document.add(new Paragraph("No existen datos."));
                    else
                        document.add(tableAsthma);
                    addLine(document,2);

                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }

            //DOCUMENTO GENERAL
            if(parametro == "general"){
                //Llenar con las tablas
                try {
                    document .add(new Paragraph("INSULINA",subFont));
                    addLine(document,1);
                    if(tableInsulin == null)
                        document.add(new Paragraph("No existen datos."));
                    else
                        document.add(tableInsulin);
                    addLine(document,2);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                //Glucosa
                try {
                    document .add(new Paragraph("GLUCOSA",subFont));
                    addLine(document,1);
                    if(tableGlucose == null)
                        document.add(new Paragraph("No existen datos."));
                    else
                        document.add(tableGlucose);
                    addLine(document,2);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                try {
                    document .add(new Paragraph("PULSO CARDIACO Y PRESIÓN ARTERIAL",subFont));
                    addLine(document,1);
                    if(tablePressure == null)
                        document.add(new Paragraph("No existen datos."));
                    else
                        document.add(tablePressure);
                    addLine(document,2);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                try {
                    document .add(new Paragraph("PESO",subFont));
                    addLine(document,1);
                    if(tablePeso == null)
                        document.add(new Paragraph("No existen datos."));
                    else
                        document.add(tablePeso);
                    addLine(document,2);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                try {
                    document .add(new Paragraph("ASMA",subFont));
                    addLine(document,1);
                    if(tableAsthma == null)
                        document.add(new Paragraph("No existen datos."));
                    else
                        document.add(tableAsthma);
                    addLine(document,2);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }


            }//End DocGeneral

            document.close();
        } catch (DocumentException e1) {
            e1.printStackTrace();
        }
        if(!enviar)
            showPdfFile(myFile, ReportActivity.this);
    }
}




