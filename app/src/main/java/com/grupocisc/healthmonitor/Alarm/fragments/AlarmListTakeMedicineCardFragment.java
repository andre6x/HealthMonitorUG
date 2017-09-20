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

import com.grupocisc.healthmonitor.Alarm.adapters.AlarmListTakeMedicineCardAdapter;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.EAlarmDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mpolo on 08/08/2017.
 */

public class AlarmListTakeMedicineCardFragment extends Fragment implements AlarmListTakeMedicineCardAdapter.ViewHolder.ClickListener {
    private static final String TAG = "[AlarmLstTakeMedFrag]";
    private String user_email;
    public ProgressDialog Dialog;
    private RecyclerView recyclerView;

    private AlarmListTakeMedicineCardAdapter adapter;
    private static List<EAlarmDetails> lstEAlarmDetails = new ArrayList<EAlarmDetails>();

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
        View view = inflater.inflate(R.layout.alarm_take_medicine_card_fragment , viewGroup, false);
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
            adapter = new AlarmListTakeMedicineCardAdapter (getActivity(), lstEAlarmDetails , this,  recyclerView  , true );
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



}
