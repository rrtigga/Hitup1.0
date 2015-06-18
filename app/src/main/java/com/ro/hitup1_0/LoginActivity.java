package com.ro.hitup1_0;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.parse.ParseObject;
import com.parse.*;
import com.ro.TinyDB.TinyDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class LoginActivity extends Activity {

    //Give your SharedPreferences file a name and save it to a static variable
    public static final String PREFS_NAME = "MyPrefsFile";
    //Facebook button
    LoginButton loginButton;
    Button email_button;
    Button google_button;
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;
    JSONArray userInformation;

    String user_id;
    String name ;
    String profile;
    String profile_pic_url;
    String friends;

    String[] friend_ids;

    TinyDB userinfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_friends"));


        userinfo = new TinyDB(getApplicationContext());

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Log.e("loginResult: ", loginResult.getAccessToken().getToken());
                        //LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email", "user_friends"));

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        // Application code
                                        response.getError();
                                        //Here's what you got
                                        Log.e("JSON:", object.toString());

                                        //Find out how to store all user friends efficiently

                                        try {
                                            user_id=object.getString("id");
                                            name=object.getString("name");
                                            profile=object.getString("link");
                                            profile_pic_url = object.getString("picture");
                                            friends = object.getString("friends");


                                            JSONObject pic_object1= (JSONObject) new JSONTokener(profile_pic_url).nextValue();
                                            JSONObject pic_object2 = pic_object1.getJSONObject("data");
                                            profile_pic_url = (String) pic_object2.get("url");


                                            JSONObject jsonObject = new JSONObject(friends);
                                            JSONArray friends = jsonObject.getJSONArray("data");
                                            friend_ids = new String[friends.length()];

                                            for (int index=0; index<friends.length(); ++index){
                                                JSONObject currentFriend = friends.getJSONObject(index);
                                                String id = currentFriend.getString("id");
                                                friend_ids[index]=id;
                                            }

                                            userinfo.putString("id", user_id);
                                            userinfo.putString("name", name);
                                            userinfo.putString("link", profile);
                                            userinfo.putString("profile_pic_url", profile_pic_url);


                                            //store all user data into Parse table
                                            final ParseObject userData = new ParseObject("UserData");
                                            userData.put("userID", user_id);
                                            userData.put("full_name", name);
                                            userData.put("profilePicURL", profile_pic_url);
                                            userData.put("allFriendIds", Arrays.asList(friend_ids));





                                            //saving object id in background
                                            userData.saveInBackground(new SaveCallback() {
                                                public void done(ParseException e) {
                                                    if (e == null) {
                                                        // Saved successfully.
                                                        //saving objectid in tinydb
                                                        Log.d("", "User update saved!");
                                                        String id = userData.getObjectId();
                                                        Log.d("", "The object id is: " + id);
                                                        userinfo.putString("objectId", id);

                                                    } else {
                                                        // The save failed.
                                                        Log.d("", "User update error: " + e);
                                                    }
                                                }
                                            });

                                            //store in local datastore
                                            userData.pinInBackground();

                                            //figure out how to store all facebook friend IDs into an array

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        if(user_id != null ) {
                                            Log.e("User ID: ", user_id);
                                        }

                                         if(name != null ) {
                                            Log.e("name: ", name);
                                        }
                                         if(profile != null ) {
                                            Log.e("profile: ", profile);
                                        }
                                        if(profile_pic_url !=null){
                                            Log.e("profile pic url: ",profile_pic_url);
                                        }

                                        if(friends !=null){
                                            Log.e("friends: ",friends);
                                        }

                                    }
                                });
                        Bundle parameters = new Bundle();
                        //the picture is large
                        parameters.putString("fields", "id,name,link,picture.type(large),friends");
                        request.setParameters(parameters);
                        request.executeAsync();

                        Intent intent = new Intent(LoginActivity.this, HomeButtons.class);
                        startActivity(intent);
                        finish();


                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.e("Facebook exception: ", exception.getMessage());
                    }
                });




        // Callback registration
        //make sure to redirect to main page
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

                // App code
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
                // App code
            }

        };


        google_button=(Button)findViewById(R.id.google_button);
        google_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, HomeButtons.class);
                startActivity(intent);
                finish();
            }
        });

        email_button= (Button)findViewById(R.id.email_button);
        email_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, HomeButtons.class);
                startActivity(intent);
                finish();
            }
        });


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

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
    }

}
