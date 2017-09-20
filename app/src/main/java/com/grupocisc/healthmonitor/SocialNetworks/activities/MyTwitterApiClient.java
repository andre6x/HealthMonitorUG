package com.grupocisc.healthmonitor.SocialNetworks.activities;

/**
 * Created by Jesenia on 20/07/2017.
 */
import com.grupocisc.healthmonitor.entities.FriendshipsService;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;

import retrofit2.Call;
import retrofit2.http.Query;
import retrofit2.http.POST;




public class MyTwitterApiClient extends TwitterApiClient {

    public   MyTwitterApiClient(Session session) {
        super((TwitterSession) session);
    }

    public   FriendshipsService getFriendshipsService() {
        return getService(FriendshipsService.class);
    }



}

