package com.example.babysittingapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class DoctorsPageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_page);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.doctors_page_frag_container, new DoctorsPageFragment(), null)
                    .commit();
        }
    }
}