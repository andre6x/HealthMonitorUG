package com.grupocisc.healthmonitor.entities;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by HP on 06/03/2017.
 */

public interface IDoctorVinculado {

    @GET("doctores/doctor")
    Call<DoctorVinculado> DoctorVincular(@Query("email") String email);
    public class DoctorVinculado{
        int idDoctor;
        String especialidad;
        String nombre;
        String apellido;
        String email;
        String telefono;


        public int getIdDoctor() {
            return idDoctor;
        }

        public void setIdDoctor(int idDoctor) {
            this.idDoctor = idDoctor;
        }

        public String getEspecialidad() {
            return especialidad;
        }

        public void setEspecialidad(String especialidad) {
            this.especialidad = especialidad;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getApellido() {
            return apellido;
        }

        public void setApellido(String apellido) {
            this.apellido = apellido;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getTelefono() {
            return telefono;
        }

        public void setTelefono(String telefono) {
            this.telefono = telefono;
        }


    }
}
