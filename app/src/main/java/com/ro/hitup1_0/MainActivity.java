package com.ro.hitup1_0;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.melnykov.fab.FloatingActionButton;
import com.parse.ParseObject;

import org.json.JSONObject;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_my);

        setContentView(R.layout.activity_main);
        recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        ca = new EventAdapter(this,result);

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
        //put the button in activity_main.xml


        Bundle extra = getIntent().getExtras();
        if(extra!=null){
            WhatEvent_String_main= extra.getString("WhatEvent_String");
            WhenEventDate_String_main= extra.getString("WhenEventDate_String");
            WhenEventTime_String_main= extra.getString("WhenEventTime_String");
            WhereEvent_String_main= extra.getString("WhereEvent_String");

            addEvent();
        }
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link");
        request.setParameters(parameters);
        request.executeAsync();




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

    protected void addEvent()
    {

        Event ish = new Event();
        ish.name = ("Rohit "+ "wants to "+WhatEvent_String_main+ " at "+ WhereEvent_String_main +" on "+ WhenEventDate_String_main +" at "+ WhenEventTime_String_main);
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



}