package com.grupocisc.healthmonitor.entities;

import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Created by Gema on 22/02/2017.
 * Control de Medicamentos
 */

public interface IRegCrtMedicamentos {
    //http://181.39.136.237:5678/controlProcesos/diabetes/procesos_oap/mostrar_control_paciente?email=laura@hotmail.com&fechaDesde=2017/03/12%2017:42:00&fechaHasta=2017/03/12%2023:10:00
    @GET("procesos_oap/mostrar_control_paciente")
    Call<List<ConsulCtrlMedicamentos>> CONSULTA_CTRL_MED_CALL(@Query("email") String email,
                                                              @Query("fechaDesde") String fechaDesde,
                                                              @Query("fechaHasta") String fechaHasta);

    class ConsulCtrlMedicamentos implements Comparable<ConsulCtrlMedicamentos> {

        String nombre ;
        String descripcion ;
        float dosis ;
        int vecesDia ;
        String consumoMedicina;
        String fecha;
        int idMedicacion;

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public float getDosis() {
            return dosis;
        }

        public void setDosis(float dosis) {
            this.dosis = dosis;
        }

        public int getVecesDia() {
            return vecesDia;
        }

        public void setVecesDia(int vecesDia) {
            this.vecesDia = vecesDia;
        }

        public String getConsumoMedicina() {
            return consumoMedicina;
        }

        public void setConsumoMedicina(String consumoMedicina) {
            this.consumoMedicina = consumoMedicina;
        }

        public String getFecha() {
            return fecha;
        }

        public void setFecha(String fecha) {
            this.fecha = fecha;
        }

        public int getIdMedicacion() {
            return idMedicacion;
        }

        public void setIdMedicacion(int idMedicacion) {
            this.idMedicacion = idMedicacion;
        }


        @Override
        public int compareTo(@NonNull ConsulCtrlMedicamentos o) {
            String a  = this.getFecha();
            String b  = o.getFecha();
            return a.compareTo(b);
        }
    }

    //http://181.39.136.237:5678/controlProcesos/diabetes/procesos_oap/control_paciente?id_medicacion=10&dosis=1.45&veces_dia=20&consumo_medicina=s&fecha_consumo=2017/03/12%2017:42:00
    @PUT("procesos_oap/control_paciente")
    Call<RegCtrlMedicamento> REG_CTRL_MEDICAMENTO_CALL(@Query("id_medicacion") int id,
                                                       @Query("dosis") float dosis,
                                                       @Query("veces_dia") int vecesDia,
                                                       @Query("consumo_medicina") String consumo,
                                                       @Query("fecha_consumo") String fecha);

    class RegCtrlMedicamento{

        int codigo;
        String respuesta;

        public int getCodigo() {
            return codigo;
        }

        public void setCodigo(int codigo) {
            this.codigo = codigo;
        }

        public String getRespuesta() {
            return respuesta;
        }

        public void setRespuesta(String respuesta) {
            this.respuesta = respuesta;
        }
    }


    //Actualiza Registro de Control de Medicamentos
    //http://181.39.136.237:5678/controlProcesos/diabetes/procesos_oap/actualiza_medicacion?
    // id_medicacion=1&dosis=3&veces_dia=3&fecha_consumo=2017/05/21&observaciones=webservices
    @PUT("procesos_oap/actualiza_medicacion")
    Call<ActCtrlMedicamento> ACTUALIZA_CTRL_MED_CALL(@Query("id_medicacion") int id,
                                                     @Query("dosis") int dosis,
                                                     @Query("veces_dia") int vecesDia,
                                                     @Query("fecha_consumo") String fecha,
                                                     @Query("observaciones") String obs);

    class ActCtrlMedicamento{

        int codigo;
        String respuesta;

        public int getCodigo() {
            return codigo;
        }

        public void setCodigo(int codigo) {
            this.codigo = codigo;
        }

        public String getRespuesta() {
            return respuesta;
        }

        public void setRespuesta(String respuesta) {
            this.respuesta = respuesta;
        }
    }

    //Control de medicamentos listado de Fragment
//http://181.39.136.237:5678/controlProcesos/diabetes/procesos_oap/medicamentos_paciente?email=getresa@hotmail.com
    @GET("procesos_oap/medicamentos_paciente")
    Call<List<ConsulCtrlMedList>> CTRLMED_LIST_CALL(@Query("email") String email);

    class ConsulCtrlMedList implements Comparable<ConsulCtrlMedList> {

        String nombreMedicamento;
        float dosisMedicamento ;
        int vecesDia ;
        String fecha;
        int idMedicacion;
        String observacion;

        public String getNombre() {
            return nombreMedicamento;
        }

        public void setNombre(String nombre) {
            this.nombreMedicamento = nombre;
        }

        public float getDosis() {
            return dosisMedicamento;
        }

        public void setDosis(float dosis) {
            this.dosisMedicamento = dosis;
        }

        public int getVecesDia() {
            return vecesDia;
        }

        public void setVecesDia(int vecesDia) {
            this.vecesDia = vecesDia;
        }

        public String getFecha() {
            return fecha;
        }

        public void setFecha(String fecha) {
            this.fecha = fecha;
        }

        public int getIdMedicacion() {
            return idMedicacion;
        }

        public void setIdMedicacion(int idMedicacion) {
            this.idMedicacion = idMedicacion;
        }

        public String getDescripcion() {
            return observacion;
        }

        public void setDescripcion(String observacion) {
            this.observacion = observacion;
        }

        @Override
        public int compareTo(@NonNull ConsulCtrlMedList o) {
            String a  = this.getFecha();
            String b  = o.getFecha();
            return a.compareTo(b);
        }
    }




}
