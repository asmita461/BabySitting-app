package com.example.babysittingapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BabysittersAdapter extends ArrayAdapter<Babysitters> {

    public BabysittersAdapter(Activity context, ArrayList<Babysitters> babysitter) {
        super(context, 0, babysitter);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // check if the current view is reused else inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_nearby_babysitters, parent, false);
        }

        //get the object located at position
        Babysitters babysitter_item = getItem(position);

        TextView name = (TextView) listItemView.findViewById(R.id.name);
        name.setText(babysitter_item.getBabysitterName());

        TextView address = (TextView) listItemView.findViewById(R.id.address);
        address.setText(babysitter_item.getBabysitterAddress());

        TextView contact = (TextView) listItemView.findViewById(R.id.contact);
        contact.setText(babysitter_item.getBabysitterContact());

        return listItemView;
    }

}
