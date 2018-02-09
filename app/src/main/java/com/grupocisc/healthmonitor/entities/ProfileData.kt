package com.grupocisc.healthmonitor.entities

import java.io.Serializable

/**
 * Created by alex on 2/8/18.
 */
class ProfileData(var identifier:String, var sex:String, var height:Float, var diabetesType:Int,var asthma:Boolean, var civilState:String, var cellphone:String):Serializable { }