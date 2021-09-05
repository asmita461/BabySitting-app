package com.example.babysittingapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class DoctorsAdapter extends ArrayAdapter<Doctors> {

    public DoctorsAdapter(Activity context, ArrayList<Doctors> doctor) {
        super(context, 0, doctor);
    }

    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {

        // check if the current view is reused else inflate the view
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_doctors_page, parent, false);
        }


        //get the object located at position
        Doctors doctor_item = getItem(position);

        TextView name = (TextView) listItemView.findViewById(R.id.name);
        name.setText(doctor_item.getDoctorName());

        TextView experience = (TextView) listItemView.findViewById(R.id.experience);
        experience.setText(doctor_item.getExperience());

        TextView hospital = (TextView) listItemView.findViewById(R.id.hospital);
        hospital.setText(doctor_item.getHospitalName());

        TextView contact = (TextView) listItemView.findViewById(R.id.contact);
        contact.setText(doctor_item.getContact());

        //find the image view with id image
        ImageView mImageView = (ImageView) listItemView.findViewById(R.id.image);
        mImageView.setImageResource(doctor_item.getImageResourceId());

        return listItemView;
    }
}