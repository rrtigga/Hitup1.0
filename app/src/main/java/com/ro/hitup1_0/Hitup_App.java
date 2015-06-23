package com.ro.hitup1_0;

import android.app.Application;

import com.digits.sdk.android.Digits;
import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.ro.design.TypefaceUtil;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Spicycurryman on 5/13/15.
 */
public class Hitup_App extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "N5qdlvMrZJjTif7S96pvActhr";
    private static final String TWITTER_SECRET = "Y8d1dGnCAIAjv15Ze8xvJU6iPNmXhRscSE5fJfpAq3o7gqkOLh";




    @Override
    public void onCreate() {
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);

        Fabric.with(this, new TwitterCore(authConfig), new Digits());
        FacebookSdk.sdkInitialize(getApplicationContext());

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "t9TGVd4RFpupFvA4LtYmRqCdRpIWfhwna7mQv2P1", "0Ly6vF5LdfuROJvzoakPn9rWda0ML3p869PxFbgS");

        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/Lato-Light.ttf"); // font from assets: "assets/fonts/Roboto-Regular.ttf

    }
}