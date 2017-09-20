package com.grupocisc.healthmonitor.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Joao on 06/10/2014.
 */
public class SharedPreferencesManager {
    private static final String APP_SETTINGS = "APP_SETTINGS";
    public static final String GCM_IDENTIFIER="GCM_SERIAL_ID";


    // properties
    private static final String SOME_STRING_VALUE = "SOME_STRING_VALUE";
    // other properties...


    private SharedPreferencesManager() {}

    private static SharedPreferences getSharedPreferences(Context context, String nomPreferencia) {
        return context.getSharedPreferences(nomPreferencia, Context.MODE_PRIVATE);
    }

    public static String getValorEsperado(Context context, String nomPreferencia, String valueRecuperar) {
        return getSharedPreferences(context,nomPreferencia).getString(valueRecuperar, null);
    }

    public static int getValorEsperadoInt(Context context, String nomPreferencia, String valueRecuperar) {
        return getSharedPreferences(context,nomPreferencia).getInt(valueRecuperar, 99999);
    }

    public static long getValorEsperadoLong(Context context, String nomPreferencia, String valueRecuperar) {
        return getSharedPreferences(context,nomPreferencia).getLong(valueRecuperar, 99999);
    }

    public static boolean getValorEsperadoBoolean(Context context, String nomPreferencia, String valueRecuperar) {
        return getSharedPreferences(context,nomPreferencia).getBoolean(valueRecuperar, false);
    }

    public static void setValor(Context context, String nomPreferencia, String newValue, String newStringValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context,nomPreferencia).edit();
        editor.putString(newStringValue , newValue);
        editor.commit();
    }

    public static void setValorInt(Context context, String nomPreferencia, int newValue, String newStringValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context,nomPreferencia).edit();
        editor.putInt(newStringValue , newValue);
        editor.commit();
    }

    public static void setValorLong(Context context, String nomPreferencia, long newValue, String newStringValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context,nomPreferencia).edit();
        editor.putLong(newStringValue , newValue);
        editor.commit();
    }

    public static void setValorBoolean(Context context, String nomPreferencia, boolean newValue, String newStringValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context,nomPreferencia).edit();
        editor.putBoolean(newStringValue , newValue);
        editor.commit();
    }

    public static void deleteValor(Context context, String nomPreferencia, String newStringValue){
        final SharedPreferences.Editor editor = getSharedPreferences(context,nomPreferencia).edit();
        editor.remove(newStringValue);
        editor.commit();
    }

    // other getters/setters
}
