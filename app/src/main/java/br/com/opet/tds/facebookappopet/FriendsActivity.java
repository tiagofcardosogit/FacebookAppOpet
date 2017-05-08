package br.com.opet.tds.facebookappopet;

import android.app.Activity;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

public class FriendsActivity extends Activity {

    private String facebookID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        facebookID = getIntent().getStringExtra("FB_ID");
        loadFriendList();
    }

    public void loadFriendList(){
       new GraphRequest(AccessToken.getCurrentAccessToken(), facebookID + "/friends", null, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {

            }
        }).executeAsync();
    }
}
