/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.grupocisc.healthmonitor.gcmClasses;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.grupocisc.healthmonitor.HealthMonitorApplicattion;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.SharedPreferencesManager;
import com.grupocisc.healthmonitor.Utils.Utils;
import com.grupocisc.healthmonitor.entities.IPushNotification;

import java.io.IOException;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;*/

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};

    public RegistrationIntentService() {
        super(TAG);
    }
    private String PREFERENCES_INICIO  = "inicioPreferences";
    private String GCM_TOKEN = "gcmToken";
    private Call<IPushNotification.InsertNotification> call_2;
    private IPushNotification.InsertNotification mNotification;
    private String Email= "";

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            // [START register_for_gcm]
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            // R.string.gcm_defaultSenderId (the Sender ID) is typically derived from google-services.json.
            // See https://developers.google.com/cloud-messaging/android/start for details on this file.
            // [START get_token]
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                   GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            // [END get_token]
            Log.e(TAG, "GCM Registration Token: " + token);

            // TODO: Implement this method to send any registration to your app's servers.
            sendRegistrationToServer(token);

            // Subscribe to topic channels
            subscribeTopics(token);

            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();
            // [END register_for_gcm]
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);

        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Persist registration to third-party servers.
     *
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        Log.e(TAG, " token:" + token);

        // Add custom implementation, as needed.
        //TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        //RegisterGCMTokenOnApiServer(token, tm.getDeviceId());
        Log.e(TAG, " sendRegistrationToServer");
        String tokenAnterior = SharedPreferencesManager.getValorEsperado(this, PREFERENCES_INICIO, GCM_TOKEN);
        if(tokenAnterior!=null && !tokenAnterior.equals(token)){
            actualizarTokenEnServidor(token);
        }
        SharedPreferencesManager.setValor(getApplicationContext(), PREFERENCES_INICIO, token, GCM_TOKEN);
    }

    private void actualizarTokenEnServidor(String token){

        if(Utils.getEmailFromPreference(this) != null)
            Email = Utils.getEmailFromPreference(this);
        Log.e(TAG, " Email:" + Email);

        if(  Email.equals("") ) {
            Log.e(TAG, " email vacio");
            //SendTokenEnServidor(1 , token); // 1  push , 2 push msisdn codserv
         }else {
            Log.e(TAG, " email ok");
            SendTokenEnServidor(2, token); // 1  push , 2 push msisdn codserv
        }
     }

    private void SendTokenEnServidor(final int index , String token){
        Log.e(TAG, "enviando");
        IPushNotification notifi = HealthMonitorApplicattion.getApplication().getmRestPushAdapter().create(IPushNotification.class);
        /*if (index == 1)
            call_2 = notifi.getPushNotification_2(token);
        else //2
            call_2 = notifi.getPushNotification(token, CountryCode, msisdn);
            */
        call_2 = notifi.INSERT_NOTIFICATION_CALL(Email, token);
        call_2.enqueue(new Callback<IPushNotification.InsertNotification>() {
            @Override
            public void onResponse(Call<IPushNotification.InsertNotification> call, Response<IPushNotification.InsertNotification> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG, "PushNotification Respuesta exitosa");
                    mNotification = response.body();

                    if ( mNotification != null ) {
                        if (mNotification.getCodigo() == 0) {
                            Log.e(TAG, "Exitooooooooooo");
                        }else{
                            Log.e(TAG, "No saved");
                        }
                    }else
                        Log.e(TAG, "Fail!!!");
                } else {
                    //showRetry();
                    Log.e(TAG, "PushNotification Error en la petición");
                }
            }

            @Override
            public void onFailure(Call<IPushNotification.InsertNotification> call, Throwable t) {
                Log.e(TAG, "PushNotification Error en la petición onFailure");
                t.printStackTrace();
            }
        });
    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
    // [END subscribe_topics]

}
