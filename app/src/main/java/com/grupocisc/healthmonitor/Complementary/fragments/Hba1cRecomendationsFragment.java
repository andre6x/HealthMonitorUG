package com.grupocisc.healthmonitor.Complementary.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.grupocisc.healthmonitor.Complementary.activities.ComplHba1cRegistyActivity;
import com.grupocisc.healthmonitor.Complementary.adapters.Hba1cRecomendationsAdapter;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.entities.IPushNotification;

import java.util.List;


public class Hba1cRecomendationsFragment extends Fragment implements Hba1cRecomendationsAdapter.MyViewHolder.ClickListener {

    private String TAG = "Hba1cRecoFragment";
    private RecyclerView recyclerView;
    private Hba1cRecomendationsAdapter adapter;
    private static List<IPushNotification.Recommendation> rowsRecomendations;

    //private OnFragmentInteractionListener mListener;

    public Hba1cRecomendationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View contentView = inflater.inflate(R.layout.fragment_recomendations, container, false);
        recyclerView = (RecyclerView) contentView.findViewById(R.id.recycler_view);

        return contentView;
    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onItemClicked(View view, int position) {
        int idMenu = rowsRecomendations.get(position).getId() ;
        Intent intent = new Intent(getActivity(), ComplHba1cRegistyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("car", rowsRecomendations.get(position) ) ;
        intent.putExtras(bundle);

        // TRANSITIONS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View card = view.findViewById(R.id.main_card);


            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                    Pair.create(card, "element1"));

            getActivity().startActivity(intent, options.toBundle());
        } else {
            getActivity().startActivity(intent);
        }

    }


//    public  void selectRecomendationsDB(){
//        Log.e(TAG,"selectRecoGluDB");
//        try {
//            //validar si en la tabla ahi datos mayor a 0
//            if (Utils.GetHba1cFromDatabase(HealthMonitorApplicattion.getApplication().getHba1cDao() ).size() > 0 ){
//                //asignamos datos de la tabla a la lista de objeto
//                rowsRecomendations = Utils.GetHba1cFromDatabase(HealthMonitorApplicattion.getApplication().getHba1cDao() );
//                //obtenemos el tamaño de la listaDEobjetos , para recorrerlo
//                //y presenta los datos de la tabla bd en el LOG
//                int tamaño = rowsRecomendations.size();
//                for(int i = 0 ; i < tamaño ; i++){
//                    Log.e(TAG,"id:" + rowsRecomendations.get(i).getId() +"-" + rowsRecomendations.get(i).getContent());
//                }
//                if(tamaño > 0) {
//                    //setear el adaptador con los datos
//                    callsetAdapter();
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    public void callsetAdapter(){
        //validacion si se a iniciado el adapter
        if (adapter != null){
            //actuliza la data del apdater
            Log.e(TAG,"adapter != null");
            adapter.updateData(rowsRecomendations);

        }else {//es nulo
            //crea la lista adapter
            Log.e(TAG,"adapter  null");
            adapter = new Hba1cRecomendationsAdapter(getActivity(), rowsRecomendations, this, recyclerView, true);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG,"onResume");
        //selectDataHba1cDB();
    }

    @Override
    public boolean onItemLongClicked(View v, int position) {
        return false;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {
            /*if (isVisibleToUser) {
                ComplActivity.fab.setVisibility(View.GONE);
            } else {
                ComplActivity.fab.setVisibility(View.VISIBLE);
            }
            */
        }
    }

    //    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
