package com.grupocisc.healthmonitor.login.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.grupocisc.healthmonitor.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class LoginFacebookActivity extends AppCompatActivity {

    private ImageView profilePictureView;
    private TextView text1 , text2 , text3 ;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private AccessTokenTracker maccessTokenTracker= null;
    private ProfileTracker mprofileTracker = null;
    private String TAG= "LoginFacebookActivity";
    CardView card_next;

    public static final String PARCEL_KEY = "parcel_key";
    String email= "";
    String gender= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_facebook);

        callbackManager = CallbackManager.Factory.create();

        profilePictureView = (ImageView) findViewById(R.id.fbImg);
        text1 = (TextView) findViewById(R.id.name);

        card_next = (CardView) findViewById(R.id.card_next);
        card_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goNext();
            }
        });



        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile" ));
        //loginButton.setReadPermissions ( "email");
        //loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday" ));
        // If using in a fragment
        //loginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.e(TAG, "FacebookCallback onSuccess");
                final Profile profile = Profile.getCurrentProfile();

                AccessToken accessToken = loginResult.getAccessToken();
                GraphRequest graphRequest= GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {


                        if (response.getError()!=null)
                        {
                            Log.e(TAG,"Error in Response "+ response);
                        }
                        else
                        {
                            if (object.has("email"))
                              email=object.optString("email");
                            Log.e(TAG,"Json Object Data: "+object+" Email id: "+ email);
                            if (object.has("gender")){
                                try {
                                    Log.e(TAG, "gender: "+ object.getString("gender"));
                                    gender = object.getString("gender") ;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        //goMainScreen(profile, email , gender);

                    }
                });

                Bundle bundle=new Bundle();
                bundle.putString("fields","id,email,name,gender");
                graphRequest.setParameters(bundle);
                graphRequest.executeAsync();

            }

            @Override
            public void onCancel() {
                // App code
                Log.e("TAG", "cancel login facebook");
                Toast.makeText(getApplicationContext(), "Se cancelo el inicio sesión con Facebook", Toast.LENGTH_SHORT).show();
                finishLogin();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.e("TAG", "error login facebook");
                Toast.makeText(getApplicationContext(), "Error de login en Facebook", Toast.LENGTH_SHORT).show();
                finishLogin();
            }
        });

        maccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
                Log.e("AccessTokenTracker","oldAccessToken:"+ oldAccessToken +"|| currentAccessToken:"+ currentAccessToken );
            }
        };

        mprofileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                Log.e("ProfileTracker","oldProfile:"+ oldProfile +"|| currentProfile:"+ currentProfile );
                Log.e(TAG, "ProfileTracker onSuccess");
                goMainScreen(currentProfile);
            }
        };

        maccessTokenTracker.startTracking();
        mprofileTracker.startTracking();

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        maccessTokenTracker.stopTracking();
        mprofileTracker.stopTracking();
    }

   /* @Override
    protected void onResume() {
        super.onResume();
        if(isLoggedIn() ){
            Log.e(TAG, "FacebookCallback onResume is true accessToken");
            Profile profile = Profile.getCurrentProfile();
            goMainScreen(profile);
        }
    }*/

    public boolean isLoggedIn(){
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    private void goMainScreen(Profile profile /*,String email ,String gender*/ ){
        Log.e(TAG, "goMainScreen");
        try{
            if(profile != null) {
                Log.e(TAG, "Profile != null");

                String facebookImageProfile =  profile.getProfilePictureUri(400, 400).toString() ;
                String facebookIdProfile = profile.getId() ;
                String facebookNameProfile =  profile.getName();
                String facebookFirstProfile =  profile.getFirstName();
                String facebookLastNameProfile =  profile.getLastName();

                text1.setText(""+ facebookNameProfile);

                Log.e("tag", "...:" + profile.getProfilePictureUri(400, 400).toString());
                //ENVIAR AH ANTALLA LLENAR DATOS SIGUIENTES
                Intent intent = new Intent(this, LoginAccountActivity.class);
                intent.putExtra("Facebook_Id",facebookIdProfile);
                intent.putExtra("Facebook_PictureUri",facebookImageProfile);
                intent.putExtra("Facebook_FirstName",facebookFirstProfile);
                intent.putExtra("Facebook_LastName",facebookLastNameProfile);
                intent.putExtra("Facebook_Email",email);
                intent.putExtra("Facebook_Gender",gender);

                logoutFacebook();

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void finishLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        logoutFacebook();
        finish();
    }

    public void goNext(){
        Intent intent = new Intent(this, LoginAccountActivity.class);
        startActivity(intent);
        finish();
    }


    //CERRAR SESION FACEBOOK
    public void logoutFacebook() {
        //eliminar instancia de facebook en app
        LoginManager.getInstance().logOut();

    }





}
