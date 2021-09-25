package com.example.babysittingapp;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

public class NearbyBabysittersFragment extends Fragment {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private BabysitDbHelper mDbHelper;

    public TextView tv;

    double latitude;
    double longitude;

    private static final String TAG = "MyActivity";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.babysitter_list, container, false);

        mDbHelper = new BabysitDbHelper(getActivity());


        getCurrentLocation();
        int[] arr= displayDatabaseInfo();



          final ArrayList<Babysitters> babysitter = new ArrayList<Babysitters>();

          SQLiteDatabase db = mDbHelper.getReadableDatabase();

          Cursor cursor = db.rawQuery("SELECT * FROM " + BabysitterContract.BabysitterEntry.TABLE_NAME, null);

          int i=0,id=0,id_index=0,name_index=0,contact_index=0,address_index=0;

          String bName="aa", bContact="aa", bAddress="aa";


          for(i=0;i<10;i++){

                  cursor.moveToFirst();
                  do{
                      id_index = cursor.getColumnIndex(BabysitterContract.BabysitterEntry._ID);
                      id = cursor.getInt(id_index);
                      if(id==arr[i]){
                          name_index = cursor.getColumnIndex(BabysitterContract.BabysitterEntry.COLUMN_BABYSITTER_NAME);
                          bName = cursor.getString(name_index);
                          contact_index = cursor.getColumnIndex(BabysitterContract.BabysitterEntry.COLUMN_CONTACT);
                          bContact = cursor.getString(contact_index);
                          address_index = cursor.getColumnIndex(BabysitterContract.BabysitterEntry.COLUMN_ADDRESS);
                          bAddress = cursor.getString(address_index);
                      }
                  }while(cursor.moveToNext());

          babysitter.add(new Babysitters(bName, bContact, bAddress));
          }

          cursor.close();

          BabysittersAdapter itemsAdapter= new BabysittersAdapter(getActivity(), babysitter);

          ListView listView = (ListView)v.findViewById(R.id.babysit_list);
                 listView.setAdapter(itemsAdapter);


        return v;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();

            } else {
                Toast.makeText(getActivity(), "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }




    private void getCurrentLocation() {

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setInterval(30000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ContextCompat.checkSelfPermission(
                getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION);
        } else {

            LocationServices.getFusedLocationProviderClient(getActivity())
                    .requestLocationUpdates(locationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                            LocationServices.getFusedLocationProviderClient(getActivity())
                                    .removeLocationUpdates(this);
                            if (locationResult != null && locationResult.getLocations().size() > 0) {
                                int latestLocationIndex = locationResult.getLocations().size() - 1;
                                latitude =
                                        locationResult.getLocations().get(latestLocationIndex).getLatitude();
                                longitude =
                                        locationResult.getLocations().get(latestLocationIndex).getLongitude();

                                Log.i(TAG,"///////-----latitude----"+latitude+"-----longitude-----"+longitude);
                            }

                        }
                    }, Looper.getMainLooper());
        }
    }



    private int[] displayDatabaseInfo() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        int index=1;

        Cursor cursor = db.rawQuery("SELECT * FROM " + BabysitterContract.BabysitterEntry.TABLE_NAME, null);
        try {


             int l = cursor.getCount();

             double dist_arr[] = new double[l];
             int id_arr[] = new int[l];

             Log.i(TAG,"-----latitude----"+latitude+"-----longitude-----"+longitude);

             if(cursor.getCount()>0) {
                 cursor.moveToFirst();

                 do {
                     int latIndex = cursor.getColumnIndex(BabysitterContract.BabysitterEntry.COLUMN_LATITUDE);
                     int lonIndex = cursor.getColumnIndex(BabysitterContract.BabysitterEntry.COLUMN_LONGITUDE);
                     double lat1 = cursor.getDouble(latIndex);
                     double lon1 = cursor.getDouble(lonIndex);


                     double earthRadius = 3958.75; // in miles, change to 6371 for kilometers

                     double dLat = Math.toRadians(latitude - lat1);
                     double dLng = Math.toRadians(longitude - lon1);

                     double sindLat = Math.sin(dLat / 2);
                     double sindLng = Math.sin(dLng / 2);

                     double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                             * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(latitude));

                     double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

                     double dist = earthRadius * c;

                     dist_arr[index - 1] = dist;
                     id_arr[index - 1] = index;

                     index++;

                 }while(cursor.moveToNext());
             }


             int i=0,j = 0, t2 = 0;
             double t1;
             for (i = 0; i < l - 1; i++) {
                 for (j = 0; j < l - i - 1; j++) {
                     if (dist_arr[j] > dist_arr[j + 1]) {
                         t1 = dist_arr[j];
                         dist_arr[j] = dist_arr[j + 1];
                         dist_arr[j + 1] = t1;

                         t2 = id_arr[j];
                         id_arr[j] = id_arr[j + 1];
                         id_arr[j + 1] = t2;
                     }
                 }
             }

             //for(i=0;i<20;i++) {
                 //Log.i(TAG, "-------Index value is-------" + id_arr[i]+"-----dist value-----"+dist_arr[i]);
             //}


             return id_arr;

        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }


}
