package com.ro.hitup1_0;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.baoyz.widget.PullRefreshLayout;
import com.facebook.appevents.AppEventsLogger;
import com.melnykov.fab.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.ro.TinyDB.TinyDB;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private static List<Event> result = new ArrayList<>();
    private static EventAdapter ca;


    public String WhatEvent_String_main;
    public static final String PREFS_NAME = "MyPrefsFile";

    RecyclerView recList;
    PullRefreshLayout layout;
    TinyDB userinfo;
    String user_id;
    String name ;
    String profile;
    String objectId;
    String profile_pic_url;

    long fourhours_milli=14400000;
    String[] friend_idsArray;


    List friend_ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hiding the action bar here
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.activity_main);
        recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        ca = new EventAdapter(this,result, getApplicationContext());
        recList.setAdapter(ca);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.attachToRecyclerView(recList);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateEvent.class);
                startActivity(intent);
            }
        });


        //facebook login check
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.ro.hitup1_0",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        //retrieve login information strings
        userinfo = new TinyDB(getApplicationContext());
        user_id=userinfo.getString("id");
        name=userinfo.getString( "name");
        profile=userinfo.getString( "link");
        profile_pic_url=userinfo.getString( "profile_pic_url");

        //put the button in activity_main.xml

        Bundle extra = getIntent().getExtras();
        if(extra!=null){
            WhatEvent_String_main= extra.getString("WhatEvent_String");
            addEvent();
        }

        //refresh
        layout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);

// listen refresh event
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // start refresh
                Log.e("Refresh: ", "Works");
                //call refreshEvents
                refreshEvents();
                layout.setRefreshing(false);
            }
        });

// refresh complete
        layout.setRefreshing(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_profile) {
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void addEvent(){
        //retrieve name
        userinfo = new TinyDB(getApplicationContext());
        name=userinfo.getString("name");
        user_id=userinfo.getString("id");
        profile_pic_url=userinfo.getString("profile_pic_url");


        Event ish = new Event();
        ish.name = (name+ " wants to "+WhatEvent_String_main);
        //adding to the list
        ish.profile_pic_url = profile_pic_url;

        //trying to communicate with adapter from activity...didn't work

        result.add(ish);


        ParseObject event = new ParseObject("Test_Events");
        event.put("event", WhatEvent_String_main);
        event.put("Name", name);
        event.put("from_userFBid", user_id);
        event.put("create_milli", System.currentTimeMillis());
        event.put("expire_milli", System.currentTimeMillis() + fourhours_milli);
        event.put("profilePicURL", profile_pic_url);

        event.saveInBackground();
        //store in local datastore
        event.pinInBackground();
        //stores in cloud
        //notify adapter
        ca.notifyDataSetChanged();
    }

    protected void refreshEvents(){
        //retrieve name
        userinfo = new TinyDB(getApplicationContext());
        name=userinfo.getString("name");
        objectId=userinfo.getString("objectId");
        user_id=userinfo.getString("id");

        friend_ids = new ArrayList<>();






        removeAllEvents();

        ParseQuery<ParseObject> query_user = ParseQuery.getQuery("UserData");
        query_user.fromLocalDatastore();
        query_user.getInBackground(objectId, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    Log.e("allFriendIds: ", object.get("allFriendIds").toString());

                    friend_ids = object.getList("allFriendIds");

                    ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Test_Events");
                    query.whereContainedIn("from_userFBid", friend_ids);
                    query.whereNotEqualTo("from_userFBid",user_id);
                    query.addAscendingOrder("create_milli");
                    query.findInBackground(new FindCallback<ParseObject>()
                    {
                        public void done(List<ParseObject> event, ParseException e) {
                            if (e == null) {
                                // your logic here

                                //check size here
                                Log.e("size: ", " "+event.size() );
                                //check friend_id list against from_userFBid OR if from_userFBid ==userid
                                for(int i = 0; i<event.size(); i++){

                                    Log.e(" ", event.get(i).getString("Name"));
                                    Event ish = new Event();
                                    ish.name = (event.get(i).getString("Name")+ " wants to "+event.get(i).getString("event"));
                                    ish.profile_pic_url=event.get(i).getString("profilePicURL");
                                    //adding to the list
                                    result.add(ish);
                                    ca.notifyDataSetChanged();
                                }

                            } else {
                                // handle Parse Exception here
                            }
                        }
                    });

                } else {
                    // something went wrong
                    Log.e("allFriendIds: ", userinfo.getString("objectId"));
                }
            }
        });



    }

    protected void removeAllEvents(){
        for(int i = 0; i<result.size();i++){
            result.remove(i);
        }

        ca.notifyDataSetChanged();
    }



    @Override
    protected void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }



}