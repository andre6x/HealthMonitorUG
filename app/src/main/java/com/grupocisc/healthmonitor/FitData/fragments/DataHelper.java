package com.grupocisc.healthmonitor.FitData.fragments;

import com.google.android.gms.fitness.data.DataType;

import java.util.ArrayList;

public class DataHelper {

    private static DataHelper INSTANCE;

        public static DataHelper getInstance()
        {
            if (INSTANCE == null) {
                INSTANCE = new DataHelper();
                populateArrayData();
            }
            return INSTANCE;
        }

    private static ArrayList<DataType> mDataTypeList = new ArrayList<DataType>();
    private static ArrayList<String> mSpinnerList = new ArrayList<String>();

        private static void populateArrayData()
        {
            mDataTypeList.add(null);
            mDataTypeList.add(DataType.TYPE_STEP_COUNT_DELTA);
            //mDataTypeList.add(DataType.TYPE_STEP_COUNT_CUMULATIVE);
            //mDataTypeList.add(DataType.TYPE_STEP_COUNT_CADENCE);
            //mDataTypeList.add(DataType.TYPE_ACTIVITY_SEGMENT);
            //mDataTypeList.add(DataType.TYPE_CALORIES_CONSUMED);
            mDataTypeList.add(DataType.TYPE_CALORIES_EXPENDED);
            mDataTypeList.add(DataType.TYPE_BASAL_METABOLIC_RATE);
            /*
            mDataTypeList.add(DataType.TYPE_POWER_SAMPLE);
            mDataTypeList.add(DataType.TYPE_ACTIVITY_SAMPLE);
            mDataTypeList.add(DataType.TYPE_HEART_RATE_BPM);
            mDataTypeList.add(DataType.TYPE_LOCATION_SAMPLE);
            mDataTypeList.add(DataType.TYPE_LOCATION_TRACK);
            mDataTypeList.add(DataType.TYPE_DISTANCE_DELTA);
            mDataTypeList.add(DataType.TYPE_DISTANCE_CUMULATIVE);


            mDataTypeList.add(DataType.TYPE_SPEED);
            */
            mDataTypeList.add(DataType.TYPE_CYCLING_WHEEL_REVOLUTION);
            mDataTypeList.add(DataType.TYPE_CYCLING_WHEEL_RPM);
            mDataTypeList.add(DataType.TYPE_CYCLING_PEDALING_CUMULATIVE);
            mDataTypeList.add(DataType.TYPE_CYCLING_PEDALING_CADENCE);
            mDataTypeList.add(DataType.TYPE_HEIGHT);
            mDataTypeList.add(DataType.TYPE_WEIGHT);
            mDataTypeList.add(DataType.TYPE_BODY_FAT_PERCENTAGE);
            mDataTypeList.add(DataType.TYPE_NUTRITION);
            mDataTypeList.add(DataType.TYPE_WORKOUT_EXERCISE);
/*
            mDataTypeList.add(DataType.AGGREGATE_ACTIVITY_SUMMARY);
            mDataTypeList.add(DataType.AGGREGATE_BASAL_METABOLIC_RATE_SUMMARY);
            mDataTypeList.add(DataType.AGGREGATE_STEP_COUNT_DELTA);
            mDataTypeList.add(DataType.AGGREGATE_DISTANCE_DELTA);
            mDataTypeList.add(DataType.AGGREGATE_CALORIES_CONSUMED);
            mDataTypeList.add(DataType.AGGREGATE_CALORIES_EXPENDED);
            mDataTypeList.add(DataType.AGGREGATE_HEART_RATE_SUMMARY);
            mDataTypeList.add(DataType.AGGREGATE_LOCATION_BOUNDING_BOX);
            mDataTypeList.add(DataType.AGGREGATE_POWER_SUMMARY);
            mDataTypeList.add(DataType.AGGREGATE_SPEED_SUMMARY);
            mDataTypeList.add(DataType.AGGREGATE_BODY_FAT_PERCENTAGE_SUMMARY);
            mDataTypeList.add(DataType.AGGREGATE_WEIGHT_SUMMARY);
            mDataTypeList.add(DataType.AGGREGATE_NUTRITION_SUMMARY);
*/
            mSpinnerList.add("Seleccione");
            mSpinnerList.add("Pasos");
            //mSpinnerList.add("Contar Paso Acumulado");
            //mSpinnerList.add("Contar Paso Cadete");
            //mSpinnerList.add("Segmento de Actividad");
            //mSpinnerList.add("Calorías Consumidas");
            mSpinnerList.add("Calorías");
            mSpinnerList.add("Tasa Metabólica Basal");
            /*
            mSpinnerList.add("Muestra de Poder");
            mSpinnerList.add("Muestra Actividad");
            mSpinnerList.add("BPM de Frecuencia Cardiaca");
            mSpinnerList.add("Muestra de Ubicación");
            mSpinnerList.add("Rastrear Ubicación");
            mSpinnerList.add("Distacia Delta");
            mSpinnerList.add("Distancia Acumulada");
            */
            //mSpinnerList.add("Correr");
            mSpinnerList.add("Vuelta en Ruedas Ciclismo");
            mSpinnerList.add("RMP Rueda Ciclismo");
            mSpinnerList.add("Pedaleo Acumulativo Ciclismo");
            mSpinnerList.add("Ritmo de Pedaleo Ciclismo");
            mSpinnerList.add("Altura");
            mSpinnerList.add("Peso");
            mSpinnerList.add("Porcentaje de Grasa Corporal");
            mSpinnerList.add("Nutrición");
            mSpinnerList.add("Ejercicio de Entrenamiento");
            /*
            mSpinnerList.add("Resumen de la Actividad Agregada");
            mSpinnerList.add("Resumen de la Tasa Metbólica");
            mSpinnerList.add("Contador Paso Delta");
            mSpinnerList.add("Distacia Delta");
            mSpinnerList.add("Caloráas Consumidas");
            mSpinnerList.add("Calorías Gastadas");
            mSpinnerList.add("Ritmo Cardiaco");
            mSpinnerList.add("Caja de Ubicación Limite");
            mSpinnerList.add("Resumen del Poder");
            mSpinnerList.add("Resumen de Velocidad");
            mSpinnerList.add("Resumen de Porcentaje de Grasa del Cuerpo");
            mSpinnerList.add("Resumen de Altura");
            mSpinnerList.add("Resumen de Nutrición");
            */
        }

        public ArrayList<String> getDataTypeReadableValues()
        {
            return mSpinnerList;
        }

        public ArrayList<DataType> getDataTypeRawValues()
        {
            return mDataTypeList;
        }
}
