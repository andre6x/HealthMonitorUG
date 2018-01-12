package com.grupocisc.healthmonitor.Asthma.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grupocisc.healthmonitor.Asthma.adapters.SymptomGridAdapter;
import com.grupocisc.healthmonitor.Complementary.funtion.TextValidator;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.State.adapters.SMainPagerAdapter;
import com.grupocisc.healthmonitor.Utils.NotificationHelper;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IAsthma;

import butterknife.BindView;

/**
 * Created by Jesenia on 27/07/2017.
 */

public class AsthmaRegistry extends AppCompatActivity {

    private String TAG = "AsthmaRegistry";
    private int year, month, day, hour, minute, second;
    private LinearLayout lyt_fecha, lyt_hora;
    private TextView txt_fecha, txt_hora;
    private EditText txt_flujo_maximo;
    private EditText txt_observation;

    public ProgressDialog Dialog;
    private Toolbar toolbar;
    private boolean isUpdate = false;
    private int idAuxState = 0;
    private int IdAsthma = 0;
    private int flujo_maximo = 0;
    private static final String enviadoServer = "false";
    private static final String operacionI = "I";
    private static final String operacionU = "U";


    private LinearLayout layoutMain;
    private LinearLayout container;
    private RelativeLayout layoutButton;
    private Context ctx;
    private LinearLayout layoutContent;
    private IAsthma row;

    private RecyclerView recycler_view_sintomas;
    private RecyclerView recycler_view_causas;
    private CardView cardReg;
    private LinearLayout lyt_sintomas;
    private LinearLayout lyt_causas;
    private CardView card_next;
    private CardView card_next_sintomas;
    private CardView card_next_guardar;


    SMainPagerAdapter adapter;
    @BindView(R.id.pager)
    ViewPager pager;

    public SymptomGridAdapter adapterSymtom;

    public AsthmaRegistry() {

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
        setContentView(R.layout.asthma_registry_activity);
        Dialog = new ProgressDialog(this);
        layoutMain = (LinearLayout) findViewById(R.id.layoutMain);
        layoutButton = (RelativeLayout) findViewById(R.id.layoutButtonss);
        layoutContent = (LinearLayout) findViewById(R.id.containerr);
        txt_observation = (EditText) findViewById(R.id.txt_observation);
        lyt_fecha = (LinearLayout) findViewById(R.id.lyt_fecha);
        lyt_hora = (LinearLayout) findViewById(R.id.lyt_hora);
        txt_fecha = (TextView) findViewById(R.id.txt_fecha);
        txt_hora = (TextView) findViewById(R.id.txt_hora);
        txt_flujo_maximo = (EditText) findViewById(R.id.txt_flujo_maximo);
        cardReg = (CardView) findViewById(R.id.cardReg1);


        if (savedInstanceState != null) {
            row = (IAsthma) savedInstanceState.getSerializable("car");
            actualizarData();
        } else {
            if ((getIntent() != null) && (getIntent().getExtras() != null) && (getIntent().getExtras().getSerializable("car") != null) && getIntent().getExtras().getSerializable("car") != null) {
                row = (IAsthma) getIntent().getExtras().getSerializable("car");
                actualizarData();
            }
        }


        txt_flujo_maximo.addTextChangedListener(new TextValidator(txt_flujo_maximo) {
            @Override
            public void validate(EditText editText, String text) {
                if (!text.isEmpty() && Utils.isNumeric(text)) {
                    if (Float.parseFloat(text) > 800)
                        txt_flujo_maximo.setError("El valor no puede ser mayor a 800 I/min. Por favor Verifique");
                }
            }

        });

        recycler_view_sintomas = (RecyclerView) findViewById(R.id.recycler_view_sintomas);
        recycler_view_causas = (RecyclerView) findViewById(R.id.recycler_view_causas);
        lyt_sintomas = (LinearLayout) findViewById(R.id.lyt_sintomas);
        lyt_causas = (LinearLayout) findViewById(R.id.lyt_causas);

        card_next = (CardView) findViewById(R.id.card_next);
        card_next_sintomas = (CardView) findViewById(R.id.card_next_sintomas);
        card_next_guardar = (CardView) findViewById(R.id.card_next_guardar);
        pantallaIngreso();
    }

    public void pantallaIngreso() {
        cardReg.setVisibility(View.VISIBLE);
        lyt_sintomas.setVisibility(View.GONE);
        lyt_causas.setVisibility(View.GONE);
    }

    public void actualizarData() {
        isUpdate = true;
        IdAsthma = row.getId();
        txt_flujo_maximo.setText("" + row.getFlujoMaximo());
        txt_fecha.setText("" + row.getFecha());
        txt_hora.setText("" + row.getHora());
        txt_observation.setText("" + row.getObservacion());

    }

    //se ejecuta al seleccionar el icon back del toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            callBackPressedActivity();
        return super.onOptionsItemSelected(item);
    }

    public void callBackPressedActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //pager.setVisibility( View.INVISIBLE );
            //TransitionManager.beginDelayedTransition(mRoot, new Slide());
        }
        super.onBackPressed();
    }

}
