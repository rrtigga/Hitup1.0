package com.ro.hitup1_0;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ro.TinyDB.TinyDB;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ContactViewHolder> {

    private  Activity activity;
    private List<Event> contactList;
    private Context context;
    TinyDB userinfo;
    String profile_pic_url;
    String user_id;

    public EventAdapter(Activity activity, List<Event> contactList, Context context) {
        this.contactList = contactList;
        this.activity = activity;
        this.context = context;

    }


    @Override
    public int getItemCount() {
        return contactList.size();

    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        Event ci = contactList.get(i);
        contactViewHolder.vName.setText(ci.name);
        //Picasso.with(context).load("https://scontent-sjc2-1.xx.fbcdn.net/hphotos-xta1/v/t1.0-9/10438582_10202555461926149_8285465463541146116_n.jpg?oh=a8b34b0ed406c9b2853da7ed9603e29c&oe=55FC98BA").into(contactViewHolder.vProfilePic);

        userinfo=new TinyDB(context.getApplicationContext());

        user_id=userinfo.getString("user_id");
        profile_pic_url ="http://graph.facebook.com/"+user_id+"/picture?type=large";
        Log.e("USERID EVENTADAPTER: ", user_id+"NICE");



        Picasso.with(context)
                .load("https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xta1/v/t1.0-1/p200x200/10438582_10202555461926149_8285465463541146116_n.jpg?oh=283ec503c6eb051aad077366155c621d&oe=55E6E24B&__gda__=1443024180_717254cb4df823b879f95dc5f8714ce2")
                .resize(225, 225)
                .centerCrop()
                .into(contactViewHolder.vProfilePic);
        //contactViewHolder.vSurname.setText(ci.surname);
        //contactViewHolder.vEmail.setText(ci.email);
        //contactViewHolder.vTitle.setText("Rohit Tigga");

    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_layout, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        protected TextView vName;
        protected TextView vSurname;
        protected TextView vEmail;
        protected TextView vTitle;
        protected ImageView vProfilePic;

        public ContactViewHolder(View v) {
            super(v);
            vName =  (TextView) v.findViewById(R.id.txtName);
            //vSurname = (TextView)  v.findViewById(R.id.txtSurname);
            //vEmail = (TextView)  v.findViewById(R.id.txtEmail);
            vTitle = (TextView) v.findViewById(R.id.title);
            vProfilePic = (ImageView) v.findViewById(R.id.profile_pic);

        }
    }


    public void addItem( Event data) {
        contactList.add(data);
        notifyItemInserted(getItemCount()-1);
    }

    public void removeItem(int position) {
        contactList.remove(position);
        notifyItemRemoved(position);
    }
}