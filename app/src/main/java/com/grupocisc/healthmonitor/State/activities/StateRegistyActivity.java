package com.grupocisc.healthmonitor.State.activities;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.State.adapters.SMainPagerAdapter;
import com.grupocisc.healthmonitor.Utils.NotificationHelper;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IState;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import java.sql.SQLException;
import java.util.Calendar;

import butterknife.BindView;


public class StateRegistyActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener
                                                                      ,TimePickerDialog.OnTimeSetListener {


    private String TAG = "StateRegistyActivity";
    private int year, month, day, hour, minute,second;
    private LinearLayout lyt_fecha, lyt_hora;
    private TextView txt_fecha, txt_hora;
    private EditText txt_observation;
    private FloatingActionButton fab;
    // private String selectTextSpinner;
    private ImageView iv_est_1;
    private ImageView iv_est_2;
    private ImageView iv_est_3;
    private ImageView iv_est_4;
    private ImageView iv_est_5;
    public ProgressDialog Dialog;
    private Toolbar toolbar;
    private String StatusName = "";
    private int IdStatus = 0;
    private IState row;
    private boolean isUpdate = false;
    private int idAuxState = 0;
    private static final String enviadoServer = "false";
    private static final String operacionI = "I";
    private static final String operacionU = "U";


    private LinearLayout layoutMain;
    private LinearLayout container;
    private RelativeLayout layoutButton;
    private  Context ctx;
    private LinearLayout layoutContent;

    private CardView cardReg;
    SMainPagerAdapter adapter;
    @BindView(R.id.pager)
    ViewPager pager;

    public StateRegistyActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getIntent()!=null)
        {
            NotificationHelper.Current.cancelNotificationFromActivity(this,getIntent().getExtras());
        }

        // TRANSITIONS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TransitionInflater inflater = TransitionInflater.from(this);
            Transition transition = inflater.inflateTransition(R.transition.transitions);
            getWindow().setSharedElementEnterTransition(transition);
            Transition transition1 = getWindow().getSharedElementEnterTransition();
            transition1.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {
                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        //TransitionManager.beginDelayedTransition(mRoot, new Slide());
                    }
                    //lyt_contenedor.setVisibility( View.VISIBLE );
                    //img_curso.setVisibility( View.VISIBLE );
                }

                @Override
                public void onTransitionCancel(Transition transition) {
                }

                @Override
                public void onTransitionPause(Transition transition) {
                }

                @Override
                public void onTransitionResume(Transition transition) {
                }
            });
        }
        setContentView(R.layout.state_registy_activity);
        Dialog = new ProgressDialog(this);
        layoutMain = (LinearLayout) findViewById(R.id.main);
        layoutButton = (RelativeLayout) findViewById(R.id.layoutButton);
        layoutContent = (LinearLayout) findViewById(R.id.container);
        cardReg = (CardView) findViewById(R.id.card);

        lyt_fecha = (LinearLayout) findViewById(R.id.lyt_fecha);
        lyt_hora = (LinearLayout) findViewById(R.id.lyt_hora);
        txt_fecha = (TextView) findViewById(R.id.txt_fecha);
        txt_hora = (TextView) findViewById(R.id.txt_hora);
        txt_observation = (EditText) findViewById(R.id.txt_observation);

        iv_est_1 = (ImageView) findViewById(R.id.img_est_1);
        iv_est_2 = (ImageView) findViewById(R.id.img_est_2);
        iv_est_3 = (ImageView) findViewById(R.id.img_est_3);
        iv_est_4 = (ImageView) findViewById(R.id.img_est_4);
        iv_est_5 = (ImageView) findViewById(R.id.img_est_5);

        Imagenes();
        setToolbar();
        inicializarFechaHora();


        if (savedInstanceState != null) {
            row = (IState) savedInstanceState.getSerializable("car");
            actualizarData();
        }
       else {
            if ((getIntent() != null) && (getIntent().getExtras() != null) && (getIntent().getExtras().getSerializable("car") != null) && getIntent().getExtras().getSerializable("car")!=null) {
                row = (IState) getIntent().getExtras().getSerializable("car");
                actualizarData();
            }
        }


        lyt_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callCalendar();
            }
        });
        lyt_hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callCalendarTime();
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                validarDataState(v);
            }
        });
    }

    public void actualizarData() {
        isUpdate = true;
        idAuxState = row.getId();
        Log.e(TAG, "ACTUALIZAR DATA:" + row.getId() + row.getObservacion());
        txt_fecha.setText("" + row.getFecha());
        txt_hora.setText("" + row.getHora());
        txt_observation.setText("" + row.getObservacion());

    }

    //se ejecuta al seleccionar el icon back del toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            callBackPressedActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    //se ejecuta al dar click en el boton back del dispositivo
    @Override
    public void onBackPressed() {
        callBackPressedActivity();
       // super.onBackPressed();
    }

    public void callBackPressedActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //pager.setVisibility( View.INVISIBLE );
            //TransitionManager.beginDelayedTransition(mRoot, new Slide());
        }
        super.onBackPressed();
    }
    //setear adaptador viewpager


    public void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //desactivar title
        toolbar.setNavigationIcon(R.mipmap.back); //buton back tollbar
        //getSupportActionBar().setTitle("Glucosa"); //titulo tollbar
        toolbar.setTitleTextColor(getResources().getColor(R.color.white)); //color tollbar title

     }

    private void inicializarFechaHora() {

        //obtener fechay hroa de Calendar
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH)+1;
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        second = c.get(Calendar.SECOND);
        //setear fecha

        //String mes = (month + 1) < 10 ? "0" + (month + 1) : "" + (month);
        String mes = month < 10 ? "0" + month : "" + month;
        String dia = day < 10 ? "0" + day : "" + day;
        //String date = ""+day+"/"+(++month)+"/"+year;
        String date = "" + dia + "/" + mes + "/" + year;
        txt_fecha.setText(date);
        Log.i(TAG,"fecha"+date);
        //setear hora
        String hourString = hour < 10 ? "0" + hour : "" + hour;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String secondString = second < 10 ? "0"+second : ""+second;
        String time = "" + hourString + ":" + minuteString+":"+secondString;
        txt_hora.setText(time);

    }

    //llamar calendario
    public void callCalendar() {
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
            date2.add(Calendar.WEEK_OF_MONTH, -1);//cambio
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
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    public void callCalendarTime() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true /*mode24Hours.isChecked()*/
        );
        tpd.setThemeDark(false);//tema dark
        tpd.vibrate(true);
        tpd.dismissOnPause(false);
        tpd.enableSeconds(true);
        tpd.setVersion(false ? TimePickerDialog.Version.VERSION_2 : TimePickerDialog.Version.VERSION_1);
        if (false) {
            tpd.setAccentColor(Color.parseColor("#9C27B0"));
        }
        if (false) {
            tpd.setTitle("TimePicker Title");
        }
        if (false) { //limitSelectableTimes
            tpd.setTimeInterval(3, 5, 10);
        }
        tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Log.d("TimePicker", "Mensaje Cancelado");
            }
        });
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }


    @Override
    public void onResume() {
        super.onResume();

        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");
        if (dpd != null)
            dpd.setOnDateSetListener(this);
        TimePickerDialog tpd = (TimePickerDialog) getFragmentManager().findFragmentByTag("Timepickerdialog");
        if (tpd != null)
            tpd.setOnTimeSetListener(this);
    }

    //se ejecuta al dar ACEPTAR en el dialogo
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String mes = "" + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : "" + (monthOfYear + 1));
        String dia = dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth;
        String date = "" + dia + "/" + mes + "/" + year;
        //String date = "" + dayOfMonth + "/" + (++monthOfYear) + "/" + year;
        txt_fecha.setText(date);
    }

    //se ejecuta al dar CANCELAR en el dialogo
    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String secondString = second < 10 ? "0" + second : "" + second;
        String time = "" + hourString + ":" + minuteString+ ":"+secondString;
        txt_hora.setText(time);
    }


    //VALIDACION DE DATOS
    public void validarDataState(View view) {
        String fecha = txt_fecha.getText().toString();
        String hora = txt_hora.getText().toString();
        String observacion = txt_observation.getText().toString();

        //validar campos llenos
        if (IdStatus != 0 && !StatusName.equals("")) {
            if (isUpdate) {
                //SI AUN NO AH SIDO ENVIADO AL SERVIDOR INSERTA
                String  operacionIU = "";
                if(row.getIdBdServer() > 0){
                    operacionIU = operacionU;
                }else{
                    operacionIU = operacionI;
                }
                updateStateFromDatabase(idAuxState, fecha, hora, IdStatus, StatusName, observacion,operacionIU);
            } else {
                saveDataStateDB(fecha, hora, IdStatus, StatusName, observacion);
            }
            nextAction();
        } else {
            Toast.makeText(StateRegistyActivity.this, "Elegir Estado de Animo", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveDataStateDB(String fecha, String hora, int IdStatus, String StatusName, String observacion) {
        try {
            //setear datos al objeto y guardar y BD
            Utils.DbsaveStateFromDatabase(-1,
                                        IdStatus,
                                        StatusName,
                                        fecha,
                                        hora,
                                        observacion,
                                        enviadoServer,
                                        operacionI,
                                        HealthMonitorApplicattion.getApplication().getStateDao());
            Utils.generateToast(this, getResources().getString(R.string.txt_guardado));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //ACTUALIZAR DATOS EN LA TABLA BDSQLITE
    public void updateStateFromDatabase(int idAuxState, String fecha, String hora, int IdStatus, String StatusName, String observacion,String operacion) {
        try {
            //setear datos al objeto y guardar y BD
            Utils.UpdateStateFromDatabase(idAuxState,
                                            IdStatus,
                                            StatusName,
                                            fecha,
                                            hora,
                                            observacion,
                                            enviadoServer,
                                            operacion,
                                            HealthMonitorApplicattion.getApplication().getStateDao());

            Utils.generateToast(this, getResources().getString(R.string.txt_actulizado));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void Imagenes() {
        iv_est_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImage_estatus_1();
            }
        });

        iv_est_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImage_estatus_2();

            }
        });

        iv_est_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImage_estatus_3();

            }
        });

        iv_est_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImage_estatus_4();

            }
        });

        iv_est_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImage_estatus_5();

            }
        });
    }

    public void setImage_estatus_1() {
        iv_est_1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_increible_con));
        iv_est_2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_feliz_sin));
        iv_est_3.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_serio_sin));
        iv_est_4.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_triste_sin));
        iv_est_5.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_horrible_sin));
        StatusName = getResources().getString(R.string.txt_sta_increible);
       StatusName= String.format(String.valueOf(getResources().getColor(R.color.status_orange)));

        IdStatus = 1;

    }

    public void setImage_estatus_2() {
        iv_est_1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_increible_sin));
        iv_est_2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_feliz_con));
        iv_est_3.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_serio_sin));
        iv_est_4.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_triste_sin));
        iv_est_5.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_horrible_sin));
        StatusName = getResources().getString(R.string.txt_sta_bien);
        IdStatus = 2;

    }

    public void setImage_estatus_3() {
        iv_est_1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_increible_sin));
        iv_est_2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_feliz_sin));
        iv_est_3.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_serio_con));
        iv_est_4.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_triste_sin));
        iv_est_5.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_horrible_sin));
        StatusName = getResources().getString(R.string.txt_sta_normal);
        IdStatus = 3;

    }

    public void setImage_estatus_4() {
        iv_est_1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_increible_sin));
        iv_est_2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_feliz_sin));
        iv_est_3.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_serio_sin));
        iv_est_4.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_triste_con));
        iv_est_5.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_horrible_sin));
        StatusName = getResources().getString(R.string.txt_sta_mal);
        IdStatus = 4;
    }

    public void setImage_estatus_5() {
        iv_est_1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_increible_sin));
        iv_est_2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_feliz_sin));
        iv_est_3.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_serio_sin));
        iv_est_4.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_triste_sin));
        iv_est_5.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.estado_horrible_con));
        StatusName = getResources().getString(R.string.txt_sta_horrible);
        IdStatus = 5;
    }

   private void nextAction(){
        circleAnimationOpen();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //finalizar matar la actividad
                finish();
            }
        }, 150);
    }



    private void circleAnimationOpen()
        {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            int x = layoutContent.getRight();
            int y = layoutContent.getBottom();
            int startRadius = 0;
            int endRadius = (int) Math.hypot(layoutMain.getWidth(), layoutMain.getHeight());
            Animator anim = ViewAnimationUtils.createCircularReveal(layoutButton, x, y, startRadius, endRadius);
            layoutButton.setVisibility(View.VISIBLE);
            Log.i(TAG,"MSM");
            cardReg.setVisibility(View.INVISIBLE);
            Log.i(TAG,"EXITO");
            anim.start();
        }
    }


    private void circleAnimationClose() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            int x = layoutButton.getRight();
            int y = layoutButton.getBottom();
            int startRadius = Math.max(layoutContent.getWidth(), layoutContent.getHeight());
            int endRadius = 0;
            Animator anim = ViewAnimationUtils.createCircularReveal(layoutButton, x, y, startRadius, endRadius);
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                }
                @Override
                public void onAnimationEnd(Animator animator) {
                    layoutButton.setVisibility(View.GONE);
                    cardReg.setVisibility(View.VISIBLE);
                }
                @Override
                public void onAnimationCancel(Animator animator) {
                }
                @Override
                public void onAnimationRepeat(Animator animator) {
                }
            });
            anim.start();
        }
    }

}

