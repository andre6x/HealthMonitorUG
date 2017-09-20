package com.grupocisc.healthmonitor.entities;



import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
/**
 * Created by Jesenia on 03/06/2017.
 */

@DatabaseTable(tableName = "RegistrePersonTable")


public class ERegistrePerson {

    @DatabaseField(generatedId=true)
    private int id;
    @DatabaseField
    private String Nombres;
    @DatabaseField
    private String Apellidos;
    @DatabaseField
    private String Mail;
    @DatabaseField
    private String Credencial;
    @DatabaseField
    private String Sexo;
    @DatabaseField
    private String FechaNacimiento;
    @DatabaseField
    private int Altura;
    @DatabaseField
    private  int PesoInicial;
    @DatabaseField
    private String EstadoCivil;
    @DatabaseField
    private int Telefono;
    @DatabaseField
    private String Pais;
    @DatabaseField
    private int TipoDiabetes;
    @DatabaseField
    private String IdUsuario;
    @DatabaseField
    private boolean EnviadoWS;



    public int getId() {return id;}

    public void setId(int id) {
        this.id = id;
    }

    public String getNombres() {return Nombres;}

    public void setNombres(String nombres) {Nombres=nombres; }

    public String getApellidos() {
        return Apellidos;
    }

    public void setApellidos(String apellidos) {
        Apellidos = apellidos;
    }

    public String getMail() {
        return Mail;
    }

    public void setMail(String mail) {
        Mail = mail;
    }

    public String getCredencial(){return Credencial;}

    public void setCredencial(String credencial) {Credencial=credencial;}

    public String getSexo() {
        return Sexo;
    }

    public void setSexo(String sexo) { Sexo = sexo;}

    public String getFechaNacimiento() {
        return FechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {FechaNacimiento = fechaNacimiento;  }

    public Integer getAltura() {return Altura; }

    public void setAltura(Integer altura) {Altura = altura;  }

    public Integer getPesoInicial() {return PesoInicial; }

    public void setPesoInicial(Integer pesoInicial) {PesoInicial = pesoInicial;  }

    public String getEstadoCivil() {
        return EstadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        EstadoCivil= estadoCivil;  }

    public Integer getTelefono() {return Telefono; }

    public void setTelefono(Integer telefono) {Telefono = telefono;  }

    public String getPais() {
        return Pais;
    }

    public void setPais(String pais) {Pais = pais;  }

    public int getTipoDiabetes() {return TipoDiabetes;}

    public void setTipoDiabetes(int tipoDiabetes) {TipoDiabetes = tipoDiabetes;  }

    public String getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(String idUsuario) {IdUsuario = idUsuario;  }

    public boolean getEnviadoWS () {return EnviadoWS;
    }

    public void setEnviadoWS(boolean enviadoWS ) {EnviadoWS = enviadoWS;  }

}






