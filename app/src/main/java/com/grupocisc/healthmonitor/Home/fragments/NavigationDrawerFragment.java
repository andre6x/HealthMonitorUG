package com.grupocisc.healthmonitor.Home.fragments;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.grupocisc.healthmonitor.Home.ItemMenu;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.login.activities.LoginAccountActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class NavigationDrawerFragment extends Fragment {

    private static final String STATE_SELECTED_POSITION = "0";
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private NavigationDrawerCallbacks mCallbacks;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;
    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    //INCLUDE
    private LinearLayout linear_loading;
    private ProgressBar progress;
    private Button retry;
    private boolean flag;

    RecyclerView recyclerView;
    LinearLayout containerMenu;
    ViewGroup rootView;
    private  TextView txt_name , txt_email ;
    private LinearLayout lyt_dataUser;
    private  CardView card_login ;
    private LinearLayout userAvatar;
    private ImageView user_avatar;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);


        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        // Select either the default item (0) or the last selected item.
        selectItem(mCurrentSelectedPosition);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = (LinearLayout)  inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        ShowProfileOrLogin();

        formarListasPrimerNivel();

        return rootView;
    }

    public void ShowProfileOrLogin(){
        final int idMenu;

        userAvatar = (LinearLayout) rootView.findViewById(R.id.menu_header);
        txt_name = (TextView) rootView.findViewById(R.id.txt_name);
        txt_email = (TextView) rootView.findViewById(R.id.txt_email);
        lyt_dataUser = (LinearLayout) rootView.findViewById(R.id.lyt_dataUser);
        card_login = (CardView) rootView.findViewById(R.id.card_login);
        user_avatar = (ImageView) rootView.findViewById(R.id.user_avatar);

        idMenu = -2;
        if (Utils.getEmailFromPreference(getActivity()) != null) {
            userAvatar.setVisibility(View.VISIBLE);
            //idMenu = -2;
            //lyt_dataUser.setVisibility(View.VISIBLE);
            //card_login.setVisibility(View.GONE);
            String emailUser = Utils.getEmailFromPreference(getActivity());
            String nameUser = Utils.getNombreFromPreference(getActivity());
            String apellidoUser = Utils.getApellidoFromPreference(getActivity());
            txt_email.setText( emailUser );
            txt_name.setText( nameUser + " "+ apellidoUser);
            if ( Utils.getPictureUriFromPreference(getActivity()) != null && ! Utils.getPictureUriFromPreference(getActivity()).equals("") ) {
                Picasso.with(getActivity()).load(Utils.getPictureUriFromPreference(getActivity()) ).into(user_avatar);
            }

        }else{
            userAvatar.setVisibility(View.GONE);
            //idMenu = -1;
            //lyt_dataUser.setVisibility(View.GONE);
            //card_login.setVisibility(View.VISIBLE);
        }
        //CLICK PARA INGRESAR A INICIAR SESION O VER DATOS
        //-1 LLAMA A ACTIVIDAD LOGIN  && -2 LLAMA  A ACTIVIDAD DATAUSER
        userAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cerrar navigationdrawable
                setSelectionPaint(idMenu);
            }
        });
    }

    public void formarListasPrimerNivel (){

        containerMenu = (LinearLayout) rootView.findViewById(R.id.linear_listview_menu);
        containerMenu.removeAllViews();

        for (int i = 0; i < getData_MENU().size(); i++) {
            final int position = i ;
            Boolean isExpandableFlecha = false;
            final Boolean[] isTorneosExpanded = {false};
            View convertView;
            ImageView flechaFirstLevel = null;
            LinearLayout lytSecondLevel = null;
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.menu_row, null);

            lytSecondLevel = (LinearLayout)convertView.findViewById(R.id.lytSecondLevel);
            flechaFirstLevel = (ImageView) convertView.findViewById(R.id.img_arrow);
            TextView title = (TextView) convertView.findViewById(R.id.title);

            //tipo 1 submenu contoles de salud , tipo 2 submenu ejercicios y dietas , tipos 3 normal , tipo4 submenu configuracion
            if (getData_MENU().get(i).getTipo() == 1 || getData_MENU().get(i).getTipo() == 5 || getData_MENU().get(i).getTipo() == 6) {
                flechaFirstLevel.setBackgroundResource(R.mipmap.arrow_right_magenta);
                //title.setTextColor(getResources().getColor(R.color.purpleMenu));
                //flechaFirstLevel.setBackgroundResource(R.mipmap.arrow_dawn_purple);
                flechaFirstLevel.setBackgroundResource(R.mipmap.arrow_dropdown_green);
                isExpandableFlecha = true ;
            }if (getData_MENU().get(i).getTipo() == 2  || getData_MENU().get(i).getTipo() == 4) {
                flechaFirstLevel.setBackgroundResource(R.mipmap.arrow_dropdown_green);
                isExpandableFlecha = true ;
            }if (getData_MENU().get(i).getTipo() == 3 ) {
                flechaFirstLevel.setBackgroundResource(R.mipmap.arrow_right_green);
            }

            LinearLayout botonFlechaFirstLevel = (LinearLayout) convertView.findViewById(R.id.contenedorGrupos);
            TextView group = (TextView) convertView.findViewById(R.id.title);
            ImageView image_group = (ImageView) convertView.findViewById(R.id.icon);
            group.setText(getData_MENU().get(i).getTitle());
            image_group.setImageResource(getData_MENU().get(i).getIconRes());
            final int idMenu = getData_MENU().get(i).getId();
            containerMenu.addView(convertView);

            final Boolean finalIsExpandableFlecha = isExpandableFlecha;
            final ImageView finalFlechaFirstLevel = flechaFirstLevel;
            final LinearLayout finalLytSecondLevel = lytSecondLevel;
            botonFlechaFirstLevel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //abre el submenu
                    if (finalIsExpandableFlecha == true) {
                        if (isTorneosExpanded[0] == true) {
                            if(getData_MENU().get(position).getTipo() == 1 ||  getData_MENU().get(position).getTipo() == 5  || getData_MENU().get(position).getTipo() == 6) {
                                //finalFlechaFirstLevel.setBackgroundResource(R.mipmap.arrow_dawn_purple);
                                finalFlechaFirstLevel.setBackgroundResource(R.mipmap.arrow_dropdown_green);
                            }else
                                finalFlechaFirstLevel.setBackgroundResource(R.mipmap.arrow_dropdown_green);

                            finalLytSecondLevel.setVisibility(View.GONE);
                            isTorneosExpanded[0] = false;
                        } else {
                            if(getData_MENU().get(position).getTipo() == 1 ||  getData_MENU().get(position).getTipo() == 5  || getData_MENU().get(position).getTipo() == 6) {
                                //finalFlechaFirstLevel.setBackgroundResource(R.mipmap.arrow_up_purple);
                                finalFlechaFirstLevel.setBackgroundResource(R.mipmap.arrow_up_green);
                            }else
                                finalFlechaFirstLevel.setBackgroundResource(R.mipmap.arrow_up_green);

                            //formarListasSegundoNivel
                            formarListasSegundoNivel(getData_MENU().get(position).getTipo() , finalLytSecondLevel);
                            isTorneosExpanded[0] = true;
                        }
                    } else {
                        //Toast.makeText(getContext(), "Item Num: " + idMenu + " Clicked", Toast.LENGTH_SHORT).show();
                        setSelectionPaint(idMenu);
                    }
                }
            });



        }
    }

    public void formarListasSegundoNivel(final int tipo, LinearLayout secondLevel) {
        secondLevel.removeAllViews();
        secondLevel.setVisibility(View.VISIBLE);
        //tipo 1  controles general , tipo 2 rutinas y dietas , tipo 4 configuraciones , tipo 5 contol diaetes , tipo 6 control asma
        if(tipo == 1) {
            for (int i = 0; i < getDataControles().size(); i++) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View convertView = inflater.inflate(R.layout.fragment_navigation_second_level, null);
                LinearLayout botonSecondLevel = (LinearLayout) convertView.findViewById(R.id.contenedorGrupos);
                TextView expandedListTextView = (TextView) convertView.findViewById(R.id.title);
                ImageView teamImage = (ImageView) convertView.findViewById(R.id.icon);
                expandedListTextView.setText(getDataControles().get(i).getTitle());
                teamImage.setImageResource(getDataControles().get(i).getIconRes());
                final int idMenu = getDataControles().get(i).getId();
                botonSecondLevel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setSelectionPaint(idMenu);
                    }
                });
                secondLevel.addView(convertView);
            }
        }else if(tipo == 2) {
            for (int i = 0; i < getDataRutinasDietas().size(); i++) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View convertView = inflater.inflate(R.layout.fragment_navigation_second_level, null);
                LinearLayout botonSecondLevel = (LinearLayout) convertView.findViewById(R.id.contenedorGrupos);
                TextView expandedListTextView = (TextView) convertView.findViewById(R.id.title);
                ImageView teamImage = (ImageView) convertView.findViewById(R.id.icon);
                expandedListTextView.setText(getDataRutinasDietas().get(i).getTitle());
                teamImage.setImageResource(getDataRutinasDietas().get(i).getIconRes());
                final int idMenu = getDataRutinasDietas().get(i).getId();
                botonSecondLevel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setSelectionPaint(idMenu);
                    }
                });
                secondLevel.addView(convertView);
            }
        }else if(tipo == 4) {
            for (int i = 0; i < getDataConfiguraciones().size(); i++) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View convertView = inflater.inflate(R.layout.fragment_navigation_second_level, null);
                LinearLayout botonSecondLevel = (LinearLayout) convertView.findViewById(R.id.contenedorGrupos);
                TextView expandedListTextView = (TextView) convertView.findViewById(R.id.title);
                ImageView teamImage = (ImageView) convertView.findViewById(R.id.icon);
                expandedListTextView.setText(getDataConfiguraciones().get(i).getTitle());
                teamImage.setImageResource(getDataConfiguraciones().get(i).getIconRes());
                final int idMenu = getDataConfiguraciones().get(i).getId();
                botonSecondLevel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setSelectionPaint(idMenu);
                    }
                });
                secondLevel.addView(convertView);
            }
        }else if(tipo == 5) {
            for (int i = 0; i < getDataControlDiabetes().size(); i++) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View convertView = inflater.inflate(R.layout.fragment_navigation_second_level, null);
                LinearLayout botonSecondLevel = (LinearLayout) convertView.findViewById(R.id.contenedorGrupos);
                TextView expandedListTextView = (TextView) convertView.findViewById(R.id.title);
                ImageView teamImage = (ImageView) convertView.findViewById(R.id.icon);
                expandedListTextView.setText(getDataControlDiabetes().get(i).getTitle());
                teamImage.setImageResource(getDataControlDiabetes().get(i).getIconRes());
                final int idMenu = getDataControlDiabetes().get(i).getId();
                botonSecondLevel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setSelectionPaint(idMenu);
                    }
                });
                secondLevel.addView(convertView);
            }
        }else if(tipo == 6) {
            for (int i = 0; i < getDataControlAsma().size(); i++) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View convertView = inflater.inflate(R.layout.fragment_navigation_second_level, null);
                LinearLayout botonSecondLevel = (LinearLayout) convertView.findViewById(R.id.contenedorGrupos);
                TextView expandedListTextView = (TextView) convertView.findViewById(R.id.title);
                ImageView teamImage = (ImageView) convertView.findViewById(R.id.icon);
                expandedListTextView.setText(getDataControlAsma().get(i).getTitle());
                teamImage.setImageResource(getDataControlAsma().get(i).getIconRes());
                final int idMenu = getDataControlAsma().get(i).getId();
                botonSecondLevel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setSelectionPaint(idMenu);
                    }
                });
                secondLevel.addView(convertView);
            }
        }



    }

    /*//tipo 1 submenu contoles generales ,
      //tipo 2 submenu ejercicios y dietas ,
     //tipos 3 normal NO submenu*/
    //tipo 4 submenu Configuraciones ,
    //tipo 5 submenu CONTROLES DE DIABETES ,
    //tipo 6 submenu CONTROLES DE asma ,
    private List<ItemMenu> getData_MENU() {
        List<ItemMenu> menuList = new ArrayList<>();
        //NOMBRE , IMAGEN , ID , TIPO
        menuList.add(new ItemMenu("CONTROLES GENERALES",    R.mipmap.menu_salud 	    ,01 , 1));//submenu
        menuList.add(new ItemMenu("CONTROLES DE DIABETES",  R.mipmap.menu_two_blood 	,24 , 5));//submenu
        menuList.add(new ItemMenu("CONTROLES DE ASMA",      R.mipmap.menu_inhaler       ,25 , 6));//submenu
        menuList.add(new ItemMenu("EJERCICIOS & DIETAS",   R.mipmap.menu_ejer_die       ,02 , 2));//submenu
        menuList.add(new ItemMenu("ALARMAS MEDICAMENTOS",  R.mipmap.menu_alarm_clock     ,13 , 3));
        menuList.add(new ItemMenu("RECOMENDACIONES MÉDICAS",R.mipmap.menu_notifications  ,10 , 3));
        menuList.add(new ItemMenu("INFORMES",              R.mipmap.menu_informes2      ,12 , 3));
        menuList.add(new ItemMenu("REDES SOCIALES",        R.mipmap.menu_social         ,17 , 3));
        menuList.add(new ItemMenu("COMPARTIR",             R.mipmap.menu_share          ,18 , 3));
        menuList.add(new ItemMenu("CONFIGURACIONES",       R.mipmap.menu_configurations ,19 , 4));//submenu
        if (Utils.getEmailFromPreference(getActivity()) != null) {
            menuList.add(new ItemMenu("CERRAR SESION", R.mipmap.menu_logout, 23, 3));
        }else {
            menuList.add(new ItemMenu("INCIAR SESION",         R.mipmap.menu_sesion         ,22 , 3));
        }
        return menuList;
    }

    //submenu 1
    private List<ItemMenu> getDataControles() {
        List<ItemMenu> menuList = new ArrayList<>();

        menuList.add(new ItemMenu("PULSO Y PRESIÓN",    R.mipmap.menu_pulso          , 2 , 1));//submenu
        //menuList.add(new ItemMenu("PRESIÓN",            R.mipmap.menu_presion        , 3 , 1));
        menuList.add(new ItemMenu("PESO",               R.mipmap.menu_peso           , 4 , 1));//submenu
        menuList.add(new ItemMenu("ESTADO DE ÁNIMO",    R.mipmap.menu_estados        , 6 , 1));//submenu
        menuList.add(new ItemMenu("ENFERMEDAD",         R.mipmap.menu_enfermedad     ,7  , 1));//submenu
        menuList.add(new ItemMenu("COMPLEMENTARIOS",    R.mipmap.menu_complementario ,14 , 1));//submenu
        menuList.add(new ItemMenu("MIS DOCTORES",             R.mipmap.doctor              ,15 , 1));//submenu
        return menuList;
    }

    //submenu 5
    private List<ItemMenu> getDataControlDiabetes() {
        List<ItemMenu> menuList = new ArrayList<>();
        menuList.add(new ItemMenu("GLUCOSA",            R.mipmap.menu_glucosa        , 1 , 5));//submenu
        menuList.add(new ItemMenu("INSULINA",           R.mipmap.menu_insulina 	     , 5 , 5));//submenu
        return menuList;
    }

    //submenu 6
    private List<ItemMenu> getDataControlAsma() {
        List<ItemMenu> menuList = new ArrayList<>();
        menuList.add(new ItemMenu("FLUJO MÁXIMO",    R.mipmap.menu_inhaler_sub , 26 , 6));
        return menuList;
    }

    private List<ItemMenu> getDataRutinasDietas() {
        List<ItemMenu> menuList = new ArrayList<>();
        menuList.add(new ItemMenu("RUTINAS EJERCICIOS",    R.mipmap.menu_ejercicios , 8 , 2));
        menuList.add(new ItemMenu("ALIMENTACIÓN",          R.mipmap.menu_cocina     , 9 , 2));
        menuList.add(new ItemMenu("FITNESS",               R.mipmap.menu_fitness     ,27 , 2));
        return menuList;
    }

    private List<ItemMenu> getDataConfiguraciones() {
        List<ItemMenu> menuList = new ArrayList<>();
        menuList.add(new ItemMenu("ACERCA DE",             R.mipmap.menu_information    ,20 , 4));
        menuList.add(new ItemMenu("PUBLICIDAD",            R.mipmap.menu_news           ,16 , 4));
        menuList.add(new ItemMenu("TUTORIAL",              R.mipmap.menu_tutorial       ,21 , 4));
        return menuList;
    }





    private List<ItemMenu> getData() {
        List<ItemMenu> menuList = new ArrayList<>();
        menuList.add(new ItemMenu("Home", R.mipmap.ic_home));
        menuList.add(new ItemMenu("Favorites", R.mipmap.ic_favorite));
        menuList.add(new ItemMenu("Settings", R.mipmap.ic_settings));
        menuList.add(new ItemMenu("Profile", R.mipmap.ic_face));

        return menuList;
    }

    // Recycler view touch lister class
    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {

            this.clickListener = clickListener;

            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null){
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)){
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }





    private void showLayout()
    {
        mDrawerListView.setVisibility(View.VISIBLE);
        linear_loading.setVisibility(View.GONE);
    }

    private void showLoading()
    {
        mDrawerListView.setVisibility(View.GONE);
        linear_loading.setVisibility(View.VISIBLE);
        progress.setVisibility(View.VISIBLE);
        retry.setVisibility(View.GONE);
    }

    private void showRetry()
    {
        mDrawerListView.setVisibility(View.GONE);
        linear_loading.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
        retry.setVisibility(View.VISIBLE);
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    public void setSelectionPaint(int position)
    {
        selectItem(position);

    }

    public boolean isDrawerOpen() {

        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.mipmap.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener


        //Utils.SetStyleActionBarHome(getActivity());
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                toolbar,
                /*R.drawable.btn_menu_launcher,             *//* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override

            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                super.onDrawerSlide(drawerView, 0);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()


            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0);
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }

    }
    public void Close() {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
            //inflater.inflate(R.menu.global, menu);
            //Utils.SetStyleActionBarHome(getActivity());
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private ActionBar getActionBar() {
        return getActivity().getActionBar();
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }
}




