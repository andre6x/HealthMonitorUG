package com.grupocisc.healthmonitor.SocialNetworks.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.grupocisc.healthmonitor.SocialNetworks.activities.MyTwitterApiClient;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

import retrofit2.Call;

import com.grupocisc.healthmonitor.R;
public class TwitterFriendsFragment extends Fragment  {
    private String TAG = "Twitter";
    // public static final String USER_HANDLE_EXTRA = "user_handle_extra";
    private TwitterLoginButton loginButton;
    private String SEARCH_QUERY;
    private View contenView;
    private ProgressDialog loadingdialog;

    String Method ="[onCreate]";
    SwipeRefreshLayout swipeRefreshLayout;
    SearchTimeline searchTimeline;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contenView = inflater.inflate(R.layout.fragment_twitter_friends, container, false);
        SEARCH_QUERY = getActivity().getString(R.string.twitter_search_friends);
        swipeRefreshLayout=(SwipeRefreshLayout) contenView.findViewById(R.id.fresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.black);
        swipeRefreshLayout=(SwipeRefreshLayout) contenView.findViewById(R.id.fresh);

        swipeRefreshLayout.setColorSchemeResources(R.color.black);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setUpTimeline();
            }
        });
        swipeRefreshProgress();
        setUpTimeline();

        //startScan();
        return contenView;
    }

    public void swipeRefreshProgress(){
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setRefreshing(true);
    }
    public void swipeRefreshProgressGone(){
        swipeRefreshLayout.setRefreshing(false);
    }


    public class LoadViewTask extends AsyncTask<Void, Integer, Void>
    {
        //Before running code in separate thread
        @Override
        protected void onPreExecute()
        {   swipeRefreshProgress();
            loadingdialog = ProgressDialog.show(getActivity(),  "","Cargando...",true);
        }
        @Override
        protected Void doInBackground(Void... params)
        {
            searchTimeline = new SearchTimeline.Builder().query(SEARCH_QUERY).build();
            return null;
        }

        //after executing the code in the thread
        @Override
        protected void onPostExecute(Void result)
        {   swipeRefreshProgressGone();
            final TweetTimelineListAdapter timelineAdapter = new TweetTimelineListAdapter(getActivity(), searchTimeline);

            ListView timelineView = (ListView) contenView.findViewById(R.id.event_timeline);
            timelineView.setEmptyView(contenView.findViewById(R.id.empty_timeline));

            timelineView.setAdapter(timelineAdapter);
            loadingdialog.dismiss();
        }
    };


    private void setUpTimeline() {
        /*SearchTimeline searchTimeline = new SearchTimeline.Builder().query(SEARCH_QUERY).build();
          final TweetTimelineListAdapter timelineAdapter = new TweetTimelineListAdapter(getActivity(), searchTimeline);
          ListView timelineView = (ListView) contenView.findViewById(R.id.event_timeline);
          timelineView.setEmptyView(contenView.findViewById(R.id.empty_timeline));
          timelineView.setAdapter(timelineAdapter);*/
        new LoadViewTask().execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
            } else {
                follow(result.getContents());
            }
        } else                             {
            super.onActivityResult(requestCode, resultCode, data);
            loginButton.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void follow(String userHandle) {
        MyTwitterApiClient mtac = new MyTwitterApiClient(TwitterCore.getInstance().getSessionManager().getActiveSession());

        Call<User> call = mtac.getFriendshipsService().create(userHandle, null, false);
        call.enqueue(new Callback<User>() {
            @Override
            public void success(Result<User> result) {
                Toast.makeText(getActivity(),
                        getResources().getString(R.string.toast_follow_user_success) + " @" + result.data.screenName,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(TwitterException e) {
                Toast.makeText(getActivity(),
                        getResources().getString(R.string.toast_follow_user_error),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


}
