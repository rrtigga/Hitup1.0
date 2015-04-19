package com.ro.hitup1_0;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class CreateEvent extends Activity {


    public Button CreateEvent_Button;

    public EditText WhatEvent_Text;
    public EditText WhenEventTime_Text;
    public EditText WhenEventDate_Text;
    public EditText WhereEvent_Text;

    public String WhatEvent_String;
    public String WhenEventTime_String;
    public String WhenEventDate_String;
    public String WhereEvent_String;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        CreateEvent_Button = (Button)findViewById(R.id.create_event_button);

        WhatEvent_Text   = (EditText)findViewById(R.id.what_event);
        WhenEventTime_Text   = (EditText)findViewById(R.id.time_event);
        WhenEventDate_Text   = (EditText)findViewById(R.id.date_event);
        WhereEvent_Text   = (EditText)findViewById(R.id.where_event);


        CreateEvent_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WhatEvent_String = WhatEvent_Text.getText().toString();
                WhenEventTime_String = WhenEventTime_Text.getText().toString();
                WhenEventDate_String = WhenEventDate_Text.getText().toString();
                WhereEvent_String = WhereEvent_Text.getText().toString();


                Log.e("What: ", WhatEvent_String);
                Log.e("When_Time: ", WhenEventTime_String);
                Log.e("When_Date: ", WhenEventDate_String);
                Log.e("Where_Event: ", WhereEvent_String);


                Intent intent = new Intent(CreateEvent.this,MainActivity.class);
                intent.putExtra("WhatEvent_String", WhatEvent_String);
                intent.putExtra("WhenEventTime_String", WhenEventTime_String);
                intent.putExtra("WhenEventDate_String", WhenEventDate_String);
                intent.putExtra("WhereEvent_String", WhereEvent_String);

                startActivity(intent);

            }
        });
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
