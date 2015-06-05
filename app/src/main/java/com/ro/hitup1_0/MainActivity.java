package com.ro.hitup1_0;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
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

import com.facebook.AccessToken;
import com.facebook.appevents.AppEventsLogger;
import com.melnykov.fab.FloatingActionButton;
import com.parse.ParseObject;
import com.ro.TinyDB.TinyDB;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends Activity {

    private static List<Event> result = new ArrayList<>();

    private static EventAdapter ca;



    public String WhatEvent_String_main;
    public String WhenEventTime_String_main;
    public String WhenEventDate_String_main;
    public String WhereEvent_String_main;

    public static final String PREFS_NAME = "MyPrefsFile";

    RecyclerView recList;

    AccessToken accessToken;

    TinyDB userinfo;

    String user_id;
    String name ;
    String profile;
    String profile_pic_url;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_my);
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
        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        //retrieve login information strings
        userinfo = new TinyDB(getApplicationContext());
        user_id=userinfo.getString("user_id");
        name=userinfo.getString( "name");
        profile=userinfo.getString( "profile");
        profile_pic_url=userinfo.getString( "profile_pic_url");



        //put the button in activity_main.xml


        Bundle extra = getIntent().getExtras();
        if(extra!=null){
            WhatEvent_String_main= extra.getString("WhatEvent_String");
            //WhenEventDate_String_main= extra.getString("WhenEventDate_String");
            //WhenEventTime_String_main= extra.getString("WhenEventTime_String");
            //WhereEvent_String_main= extra.getString("WhereEvent_String");

            addEvent();
        }


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


        Event ish = new Event();
        ish.name = (name+ " wants to "+WhatEvent_String_main);
        //ish.surname = Event.SURNAME_PREFIX;
        //ish.email = Event.EMAIL_PREFIX + "@test.com";
        result.add(ish);

        ParseObject event = new ParseObject("Test_Events");
        event.put("event_description", ish.name);

        event.saveInBackground();
        //ca.addItem(ish);

        ca.notifyDataSetChanged();


    }



    private List<Event> createList(int size) {

        for (int i=1; i <= size; i++) {
            Event ci = new Event();
            ci.name = Event.NAME_PREFIX + i;
            //ci.surname = Event.SURNAME_PREFIX + i;
            //ci.email = Event.EMAIL_PREFIX + i + "@test.com";

            result.add(ci);
        }

        return result;
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