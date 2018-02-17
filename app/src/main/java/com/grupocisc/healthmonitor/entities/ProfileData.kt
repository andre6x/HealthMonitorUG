package com.grupocisc.healthmonitor.entities

import java.io.Serializable

/**
 * Created by alex on 2/8/18.
 */
class ProfileData(var email:String, var nombre:String, var apellido:String, var fecha_nacimiento:String, var sexo:String, var estado_civil:String, var telefono:String,var edad:Int,var pais:Int,var estatura:Float,var tipo_diabetes:Int,var enfermedad_resp:Int?):Serializable { }

class UpdateProfileResult(var idCodResult:Int, var resultDescription:String, var respuesta:Boolean):Serializable{}