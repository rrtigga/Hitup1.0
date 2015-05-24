package com.ro.hitup1_0;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.ro.design.TypefaceUtil;

/**
 * Created by Spicycurryman on 5/13/15.
 */
public class Hitup_App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "t9TGVd4RFpupFvA4LtYmRqCdRpIWfhwna7mQv2P1", "0Ly6vF5LdfuROJvzoakPn9rWda0ML3p869PxFbgS");

        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/Lato-Light.ttf"); // font from assets: "assets/fonts/Roboto-Regular.ttf

    }
}