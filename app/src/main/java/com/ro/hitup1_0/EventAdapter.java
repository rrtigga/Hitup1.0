package com.ro.hitup1_0;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ContactViewHolder> {

    private  Activity activity;
    private List<Event> contactList;

    public EventAdapter(Activity activity, List<Event> contactList) {
        this.contactList = contactList;
        this.activity = activity;

    }


    @Override
    public int getItemCount() {
        return contactList.size();

    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        Event ci = contactList.get(i);
        contactViewHolder.vName.setText(ci.name);
        //contactViewHolder.vSurname.setText(ci.surname);
        //contactViewHolder.vEmail.setText(ci.email);
        contactViewHolder.vTitle.setText("Rohit Tigga");
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

        public ContactViewHolder(View v) {
            super(v);
            vName =  (TextView) v.findViewById(R.id.txtName);
            //vSurname = (TextView)  v.findViewById(R.id.txtSurname);
            //vEmail = (TextView)  v.findViewById(R.id.txtEmail);
            vTitle = (TextView) v.findViewById(R.id.title);
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