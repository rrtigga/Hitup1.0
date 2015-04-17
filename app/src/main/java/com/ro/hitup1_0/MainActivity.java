package com.ro.hitup1_0;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    List<Event> result = new ArrayList<>();

    EventAdapter ca = new EventAdapter(createList(0));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_my);


        setContentView(R.layout.activity_main);
        RecyclerView recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        recList.setAdapter(ca);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.attachToRecyclerView(recList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event ish = new Event();
                ish.name = Event.NAME_PREFIX;
                ish.surname = Event.SURNAME_PREFIX;
                ish.email = Event.EMAIL_PREFIX + "@test.com";

                ca.addItem(ca.getItemCount(),ish);


            }
        });
        //put the button in activity_main.xml
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private List<Event> createList(int size) {

        for (int i=1; i <= size; i++) {
            Event ci = new Event();
            ci.name = Event.NAME_PREFIX + i;
            ci.surname = Event.SURNAME_PREFIX + i;
            ci.email = Event.EMAIL_PREFIX + i + "@test.com";

            result.add(ci);

        }

        return result;
    }



}