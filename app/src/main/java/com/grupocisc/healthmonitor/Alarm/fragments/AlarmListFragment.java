package com.grupocisc.healthmonitor.Alarm.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.grupocisc.healthmonitor.Alarm.adapters.AlarmListAdapter;
import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.EAlarmDetails;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by developer on 21/07/2017.
 */

public class AlarmListFragment extends Fragment implements AlarmListAdapter.ViewHolder.ClickListener {
    private static final String TAG = "[AlarmListFragment]";
    private String user_email;
    public ProgressDialog Dialog;
    private RecyclerView recyclerView;
    //private List <AlarmListAdapter> adapter;
    private AlarmListAdapter adapter;
    private static List<EAlarmDetails> lstEAlarmDetails = new ArrayList<EAlarmDetails>();

    private String toDate;
    private String dateI;
    private String dateF;
    private String hourI;
    private String hourF;
    private int op;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String Method ="[onCreate]";
        Log.i(TAG, Method +  "Init..."  );
        super.onCreate(savedInstanceState);
        user_email = Utils.getEmailFromPreference(this.getContext());
        Dialog = new ProgressDialog(getActivity());
        Log.i(TAG, Method +  "End..."  );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        String Method ="[onCreateView]";
        Log.i(TAG, Method +  "Init..."  );
        View view = inflater.inflate(R.layout.alarm_register_fragment , viewGroup, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        Log.i(TAG, Method +  "End..."  );
        return view;
    }

    @Override
    public void onResume(){
        String Method ="[onResume]";
        Log.i(TAG, Method +  "Init..."  );
        super.onResume();

        if( true )//if(isOnline(getActivity()))
            restarLoading();
        Log.i(TAG, Method +  "End..."  );
    }

    public void restarLoading(){
        String Method ="[restartLoading]";
        Log.i(TAG, Method +  "Init..."  );
        //showLoadingDialog();
        //selectDataInsulinDB(user_email, "insulina");
        selectDataAlarmDetailsDBLocal(user_email, "alarma");
        Log.i(TAG, Method +  "End..."  );
    }

    public void selectDataAlarmDetailsDBLocal(String email, String parametro) {
        String Method = "[selectDataAlarmDetailsDBLocal]";
        Log.i(TAG, Method + "Init...");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        toDate = sdf.format(new Date());

        dateI = toDate;// "1900/01/01";
        dateF = toDate; //"2030/01/01";
        switch (op){
            case 0:{
                // Morning 04:00 - 12:00
                hourI ="03:59:59";
                hourF ="12:00:00";
                break;
            }
            case 1:{
                // Afternoon 12:00 - 18:00
                hourI ="11:59:59";
                hourF ="18:00:00";
                break;
            }
            case 2:{
                // Evening 18:00 - 00:00
                hourI ="17:59:59";
                hourF ="24:00:00";
                break;
            }
            case 3:{
                // Night 00:00 - 04:00
                hourI ="00:00:00";
                hourF ="04:00:00";
                break;
            }
        }

        lstEAlarmDetails = new ArrayList<EAlarmDetails>();
        try{
            lstEAlarmDetails = Utils.getEAlarmDetailsFromDataBaseLocal(HealthMonitorApplicattion.getApplication().getEAlarmDetailsDao(),0,0, dateI, dateF,null,null);
        }catch (SQLException e){
            e.printStackTrace();
        }
        if (lstEAlarmDetails != null){
            lstEAlarmDetails = filterListByRangeHour(lstEAlarmDetails,toDate.replace("-","/"),hourI,hourF);
            postExecutionQueryDBLocal();
        }
        else {
            showLayoutDialog();
            Utils.generarAlerta(getActivity(), getString(R.string.txt_atencion), getString(R.string.text_error_metodo));

        }
        Log.i(TAG, Method +  "End..."  );
    }

    public void postExecutionQueryDBLocal() {
        String Method = "[postExecutionQueryDBLocal]";
        Log.i(TAG, Method + "Init...");
        showLayoutDialog();
        if (lstEAlarmDetails != null)
            if (lstEAlarmDetails.size() > 0)
                callSetAdapter();
    }

    private void showLayoutDialog(){
        String Method ="[showLayoutDialog]";
        Log.i(TAG, Method +  "Init..."  );
        if (Dialog != null)
            Dialog.dismiss();
        Log.i(TAG, Method +  "End..."  );
    }

    public void callSetAdapter(){
        String Method ="[callSetAdapter]";
        Log.i(TAG, Method + "Init..."  );
        if(adapter != null) {
            Log.i(TAG, Method + "adapter is not null"  );
            adapter.updateData (lstEAlarmDetails);
        }else {
            //Crea la lista
            Log.i(TAG, Method + "adapter is null"  );
            adapter = new AlarmListAdapter (getActivity(), lstEAlarmDetails , this,  recyclerView  , true , toDate , hourI , hourF );
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        Log.i(TAG, Method + "End..."  );
    }


    @Override
    public void onItemClicked(View v, int position) {

    }

    @Override
    public boolean onItemLongClicked(View v, int position) {
        return false;
    }

    public int getOp() {
        return op;
    }

    public void setOp(int op) {
        this.op = op;
    }


    private List<EAlarmDetails> filterListByRangeHour(List<EAlarmDetails> lstEAlarmDetails , String date, String hourI , String hourF  ){
        String Method="[filterListByRangeHour]";
        Log.i(TAG, Method +  "Init..."  );
        Log.i(TAG, Method +  "op="+op  );
        Log.i(TAG, Method +  "date="+date  );
        Log.i(TAG, Method +  "hourI="+hourI  );
        Log.i(TAG, Method +  "hourF="+hourF  );

        List<EAlarmDetails> lst = new ArrayList<EAlarmDetails>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String dateI = "";//new Date();
        String dateF = "";//new Date();
        String dateDB= "";//new Date();
        //try {
//            dateI = simpleDateFormat.parse(date + " " + hourI);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        try {
//            dateF = simpleDateFormat.parse(date + " " + hourF);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        dateI = date + " " + hourI;
        dateF = date + " " + hourF;
        for (EAlarmDetails ead : lstEAlarmDetails) {
            //try {
                //dateDB = simpleDateFormat.parse(ead.getAlarmDetailDate() + " " + ead.getAlarmDetailHour());
                dateDB = ead.getAlarmDetailDate() + " " + ead.getAlarmDetailHour();
                //Log.i(TAG, Method +  "[dateDB="+simpleDateFormat.format( dateDB )+"][dateI=" + simpleDateFormat.format(dateI)+"][dateF=" + simpleDateFormat.format(dateF) +"] - [ead.toString()=" + ead.toString() +"]" );
            Log.i(TAG, Method +  "[dateDB="+dateDB+"][dateI="+dateI+"][dateF="+dateF+"] - [ead.toString()=" + ead.toString() +"]" );
                //if(dateDB.compareTo(dateI)>=0) Log.i(TAG, Method +  "{[dateDB >= dateI] -> [" + simpleDateFormat.format(dateDB)+">="+simpleDateFormat.format(dateI) +"]" );
                //if(dateDB.compareTo(dateF)<0) Log.i(TAG, Method +  "{[dateDB < dateF] -> [" + simpleDateFormat.format(dateDB)+"<"+simpleDateFormat.format(dateF) +"]" );
                if( dateDB.compareTo(dateI)>=0 && dateDB.compareTo(dateF)<0 ){//if ( dateDB.after(dateI) && dateDB.before(dateF)  ){
                    lst.add(ead);
                    Log.i(TAG, Method +  "add to list ead.toString()=" + ead.toString()  );
                }
            //} catch (ParseException e) {
            //    e.printStackTrace();
            //}

        }


        return lst;
    }


}
