package com.example.babysittingapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class DoctorsPageFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.doctor_list, container, false);

        final ArrayList<Doctors> doctor = new ArrayList<Doctors>();
        doctor.add(new Doctors("Dr. Dhanya Dharmapalan", "Experience: 15 YRS", "Apollo Hospital", "020 7118 5466",R.drawable.doctor1,"https://www.apollo247.com/doctors/dr-dhanya-dharmapalan-c04fe24d-b9d2-42c2-8582-1d992b61b715"));
        doctor.add(new Doctors("Dr. Sudip Sengupta", "Experience: 18 YRS", "Apollo Hospital", "1860 500 1066",R.drawable.doctor2,"https://www.apollo247.com/doctors/dr-sudip-sengupta-4115de58-ce3a-4d90-8c81-2c7297dcfce9"));
        doctor.add(new Doctors("Dr. Vijay Yewale", "Experience: 20 YRS", "Apollo Hospital", "020 7117 7330",R.drawable.doctor3,"https://www.apollo247.com/doctors/dr-vijay-yewale-c03f7c34-06ae-4887-84ff-227f2648f73b"));
        doctor.add(new Doctors("Dr. D. Vijayasekaran", "Experience: 43 YRS", "Apollo Hospital", "044 7196 7345",R.drawable.doctor4,"https://www.apollo247.com/doctors/dr-vijaysekaran-d-ef029dc5-e78a-4e55-aa8d-8062604af712"));
        doctor.add(new Doctors("Dr. Jimmy Shad", "Experience: 20 YRS", "Apollo Hospital", "+91 44463 12566",R.drawable.doctor5,"https://www.apollo247.com/doctors/dr-jimmy-shad-f5102e70-3fba-4d71-bc94-cc714c77a72c"));
        doctor.add(new Doctors("Dr. Fazala Mehnaz", "Experience: 15 YRS", "Apollo Hospital", "7013728205",R.drawable.doctor6,"https://www.apollo247.com/doctors/dr-fazala-mehnaz-b7f2b323-543a-45ea-85a9-29dd91f17044"));
        doctor.add(new Doctors("Dr. Dolly Lakhani", "Experience: 25 YRS", "Apollo Hospital", "1860 500 1066",R.drawable.doctor7,"https://www.apollo247.com/doctors/dr-dolly-lakhani-85be5ea8-17da-49a9-8936-9264823546b1"));
        doctor.add(new Doctors("Dr. S V S Sreedhar", "Experience: 15 YRS", "Apollo Hospital", "(040) 23607777",R.drawable.doctor8,"https://www.apollo247.com/doctors/dr-s-v-s-sreedhar-81512b96-a501-427b-ba7e-a9c190546ef7"));
        doctor.add(new Doctors("Dr. Shalini G Agasthi", "Experience: 20 YRS", "Apollo Hospital", "080 7196 5582",R.drawable.doctor9,"https://www.apollo247.com/doctors/dr-shalini-g-agasthi-57323c0d-a2d2-4b98-a8fc-d3bcf2c178f6"));
        doctor.add(new Doctors("Dr. Reetesh Gupta", "Experience: 20 YRS", "Apollo Hospital", "1860 500 1066",R.drawable.doctor10,"https://www.apollo247.com/doctors/dr-reetesh-gupta-d265bf08-91cc-4214-a9d6-c1ee24353cff"));

        // setting up the array adapter
        DoctorsAdapter itemsAdapter= new DoctorsAdapter(getActivity(), doctor);

        // finding the listView and setting the adapter to it
        ListView listView = (ListView)rootView.findViewById(R.id.list);
        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent Getintent = new Intent(Intent.ACTION_VIEW, Uri.parse(doctor.get(i).getUri()));
                getContext().startActivity(Getintent);

            }
        });

        return rootView;
    }

}
