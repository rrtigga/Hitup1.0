package com.ro.hitup1_0;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ContactViewHolder> {

    private  Activity activity;
    private List<Event> contactList;
    //idk if this should be static
    private static Context context;
    String event;
    String profile_pic_url;



    public EventAdapter(Activity activity, List<Event> contactList, Context context) {
        this.activity = activity;
        this.contactList = contactList;
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



        //maybe in .load you have to pass in ci.profile_pic
        Picasso.with(context)
                .load(ci.profile_pic_url)
                .resize(225, 225)
                .centerCrop()
                .into(contactViewHolder.vProfilePic);

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
        protected TextView vTitle;
        protected ImageView vProfilePic;
        protected ImageButton vTextMessage;

        public ContactViewHolder(View v) {
            super(v);
            vName =  (TextView) v.findViewById(R.id.txtName);
            vTextMessage = (ImageButton) v.findViewById(R.id.text_message);
            vTitle = (TextView) v.findViewById(R.id.title);
            vProfilePic = (ImageView) v.findViewById(R.id.profile_pic);



            vTextMessage.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //hopefully this works
                    Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                    smsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    smsIntent.setType("vnd.android-dir/mms-sms");
                    smsIntent.putExtra("address", "4086079393");
                    smsIntent.putExtra("sms_body","Hey, I saw your Hitup. I'm definitely down!");
                    context.startActivity(smsIntent);


                }
            });

        }

    }


}