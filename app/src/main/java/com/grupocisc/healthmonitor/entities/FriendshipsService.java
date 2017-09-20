package com.grupocisc.healthmonitor.entities;

import com.twitter.sdk.android.core.models.User;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Jesenia on 20/07/2017.
 */

public interface FriendshipsService {
    @POST("/1.1/friendships/create.json")
    Call<User> create(@Query("screen_name") String screenName, @Query("user_id") String id, @Query("follow") boolean follow);
}
