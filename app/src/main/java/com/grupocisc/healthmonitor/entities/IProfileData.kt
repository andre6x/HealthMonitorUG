package com.grupocisc.healthmonitor.entities

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by alex on 2/8/18.
 */
interface IProfileData {
    @POST("controlServices/diabetes/patientUsers/updateData")
    fun updateProfileData(@Body body:ProfileData): Call<ProfileData>
}