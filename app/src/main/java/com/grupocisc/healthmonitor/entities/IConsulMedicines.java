package com.grupocisc.healthmonitor.entities;

import android.support.annotation.NonNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Created by Gema on 09/03/2017.
 */

public interface IConsulMedicines {

    //Retorna todos los medicamentos que están en la base de datos
    @GET("medicamento")
    Call<List<medicamentosAll>> MEDICAMENTOS_ALL_CALL ();

    class medicamentosAll implements Comparable<medicamentosAll>{
        int idMedicamento;
        String nombre;
        String descripcion;
        String principioActivo ;
        String indicaciones ;
        String recomendaciones ;
        String via ;
        String presentacion ;
        String estado ;
        String laboratorio ;
        private int    medicineType;


        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }

        public int getIdMedicamento() { return idMedicamento; }

        public void setIdMedicamento(int idMedicamento) {
            this.idMedicamento = idMedicamento;
        }

        public String getIndicaciones() {
            return indicaciones;
        }

        public void setIndicaciones(String indicaciones) {
            this.indicaciones = indicaciones;
        }

        public String getLaboratorio() {
            return laboratorio;
        }

        public void setLaboratorio(String laboratorio) {
            this.laboratorio = laboratorio;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getPresentacion() {
            return presentacion;
        }

        public void setPresentacion(String presentacion) {
            this.presentacion = presentacion;
        }

        public String getPrincipioActivo() {
            return principioActivo;
        }

        public void setPrincipioActivo(String principioActivo) {
            this.principioActivo = principioActivo;
        }

        public String getRecomendaciones() {
            return recomendaciones;
        }

        public void setRecomendaciones(String recomendaciones) {
            this.recomendaciones = recomendaciones;
        }

        public String getVia() {
            return via;
        }

        public void setVia(String via) {
            this.via = via;
        }

        @Override
        public int compareTo(@NonNull medicamentosAll o) {
            String a  = this.getNombre();
            String b  = o.getNombre();
            return a.compareTo(b);

        }

        public int getMedicineType() {
            return medicineType;
        }

        public void setMedicineType(int medicineType) {
            this.medicineType = medicineType;
        }
    }

    //Registra el medicamento al usuario mRestCISCAdapterp
    @PUT("procesos_oap/vincula_medicamento_paciente") //email=mail@hotmail.com&id_medicamento=4&fecha_registro=2017/03/12%2017:33:45
    Call<RegMedicacion> RegMedicacion( @Query("email") String email,
                                       @Query("id_medicamento") int id,
                                       @Query("fecha_registro") String fecha);

    class RegMedicacion
    {
        int codigo;
        String respuesta;
        String email;
        int id;
        String fecha;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getFecha() {
            return fecha;
        }

        public void setFecha(String fecha) {
            this.fecha = fecha;
        }

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

    //Consulta Medicamentos registrados Detallado
    //http://181.39.136.237:5678/controlProcesos/diabetes/
    //http://181.39.136.237:5678/controlProcesos/diabetes/procesos_oap/medicamentos_paciente_andr?email=laura@hotmail.com
    @GET("procesos_oap/medicamentos_paciente_andr")
    Call<List<DetMedicamentosReg>> DetalleMedRegistrados (@Query("email") String email);

    class DetMedicamentosReg implements Comparable<DetMedicamentosReg>{
        int idMedicamento;
        String nombre;
        String descripcion ;
        String principioActivo;
        String indicaciones;
        String recomendaciones;
        String laboratorio;
        String fechaRegistro;

        public int getIdMedicamento() {
            return idMedicamento;
        }

        public void setIdMedicamento(int idMedicamento) {
            this.idMedicamento = idMedicamento;
        }

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

        public String getPrincipioActivo() {
            return principioActivo;
        }

        public void setPrincipioActivo(String principioActivo) {
            this.principioActivo = principioActivo;
        }

        public String getIndicaciones() {
            return indicaciones;
        }

        public void setIndicaciones(String indicaciones) {
            this.indicaciones = indicaciones;
        }

        public String getRecomendaciones() {
            return recomendaciones;
        }

        public void setRecomendaciones(String recomendaciones) {
            this.recomendaciones = recomendaciones;
        }

        public String getLaboratorio() {
            return laboratorio;
        }

        public void setLaboratorio(String laboratorio) {
            this.laboratorio = laboratorio;
        }

        public String getFechaRegistro() {
            return fechaRegistro;
        }

        public void setFechaRegistro(String fechaRegistro) {
            this.fechaRegistro = fechaRegistro;
        }

        @Override
        public int compareTo(@NonNull DetMedicamentosReg o) {
            String a  = this.getNombre();
            String b  = o.getNombre();
            return a.compareTo(b);
        }
    }

    //Consulta Medicamentos registrados para Control
    //http://181.39.136.237:5678/controlProcesos/diabetes/procesos_oap/medicaciones_paciente_andr?email=laura@hotmail.com
    @GET("procesos_oap/medicaciones_paciente_andr")
    Call<List<ListadoMedReg>> LISTADO_CALL (@Query("email") String email);

    class ListadoMedReg implements Comparable<ListadoMedReg>{
        private int idMedicacion;
        private String medicamento;
        private String fechaRegistro;

        @Override
        public String toString() {
            return this.medicamento;            // What to display in the Spinner list.
        }

        public int getIdMedicacion() {
            return idMedicacion;
        }

        public void setIdMedicacion(int idMedicacion) {
            this.idMedicacion = idMedicacion;
        }

        public String getMedicamento() {
            return medicamento;
        }

        public void setMedicamento(String medicamento) {
            this.medicamento = medicamento;
        }

        public String getFechaRegistro() {
            return fechaRegistro;
        }

        public void setFechaRegistro(String fechaRegistro) {
            this.fechaRegistro = fechaRegistro;
        }

        @Override
        public int compareTo(@NonNull ListadoMedReg o) {
            String a  = this.getMedicamento();
            String b  = o.getMedicamento();
            return a.compareTo(b);
        }
    }

}
