package com.ro.hitup1_0;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Spicycurryman on 5/13/15.
 */
public class Hitup_App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "t9TGVd4RFpupFvA4LtYmRqCdRpIWfhwna7mQv2P1", "0Ly6vF5LdfuROJvzoakPn9rWda0ML3p869PxFbgS");

    }
}