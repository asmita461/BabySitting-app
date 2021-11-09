package com.example.babysittingapp;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
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


    private BabysitDbHelper mDbHelper;

    public TextView tv;

    double latitude;
    double longitude;

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;


    private static final String TAG = "MyActivity";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.babysitter_list, container, false);

        getCurrentLocation();

        mDbHelper = new BabysitDbHelper(getActivity());

        insertData();

        int[] arr= displayDatabaseInfo();



          final ArrayList<Babysitters> babysitter = new ArrayList<Babysitters>();

          SQLiteDatabase db = mDbHelper.getReadableDatabase();

          Cursor cursor = db.rawQuery("SELECT * FROM " + BabysitterContract.BabysitterEntry.TABLE_NAME, null);

          int i=0,id=0,id_index=0,name_index=0,contact_index=0,address_index=0;

          String bName="", bContact="", bAddress="";


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
    public void onDestroyView(){
        delete();
        super.onDestroyView();
    }







    private int[] displayDatabaseInfo() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        int index=1;


        Cursor cursor = db.rawQuery("SELECT * FROM " + BabysitterContract.BabysitterEntry.TABLE_NAME, null);
        try {


             int l = cursor.getCount();

             double dist_arr[] = new double[l];
             int id_arr[] = new int[l];

             //Log.i(TAG,"-----latitude----"+latitude+"-----longitude-----"+longitude);

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

    private void insertData() {
        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys
        ContentValues values1 = new ContentValues();
        values1.put(BabysitterContract.BabysitterEntry._ID, 1);
        values1.put(BabysitterContract.BabysitterEntry.COLUMN_BABYSITTER_NAME, "B.T.S. House Keeping Service");
        values1.put(BabysitterContract.BabysitterEntry.COLUMN_ADDRESS, "Charbagh, Lucknow- 226004");
        values1.put(BabysitterContract.BabysitterEntry.COLUMN_CONTACT, "9005988048");
        values1.put(BabysitterContract.BabysitterEntry.COLUMN_LATITUDE, 26.83552);
        values1.put(BabysitterContract.BabysitterEntry.COLUMN_LONGITUDE, 80.91717);

        long newRowId1 = db.insert(BabysitterContract.BabysitterEntry.TABLE_NAME, null, values1);

        ContentValues values2 = new ContentValues();
        values2.put(BabysitterContract.BabysitterEntry._ID, 2);
        values2.put(BabysitterContract.BabysitterEntry.COLUMN_BABYSITTER_NAME, "Nyasa Child Care");
        values2.put(BabysitterContract.BabysitterEntry.COLUMN_ADDRESS, "Lahartara, Varanasi- 221001");
        values2.put(BabysitterContract.BabysitterEntry.COLUMN_CONTACT, "9695317703");
        values2.put(BabysitterContract.BabysitterEntry.COLUMN_LATITUDE, 25.31232);
        values2.put(BabysitterContract.BabysitterEntry.COLUMN_LONGITUDE, 82.96784);

        long newRowId2 = db.insert(BabysitterContract.BabysitterEntry.TABLE_NAME, null, values2);

        ContentValues values3 = new ContentValues();
        values3.put(BabysitterContract.BabysitterEntry._ID, 3);
        values3.put(BabysitterContract.BabysitterEntry.COLUMN_BABYSITTER_NAME, "Sudhanshu Child Care Center");
        values3.put(BabysitterContract.BabysitterEntry.COLUMN_ADDRESS, "DLW Road, Varanasi- 221108");
        values3.put(BabysitterContract.BabysitterEntry.COLUMN_CONTACT, "8765113929");
        values3.put(BabysitterContract.BabysitterEntry.COLUMN_LATITUDE, 25.27765);
        values3.put(BabysitterContract.BabysitterEntry.COLUMN_LONGITUDE, 82.94380);

        long newRowId3 = db.insert(BabysitterContract.BabysitterEntry.TABLE_NAME, null, values3);

        ContentValues values4 = new ContentValues();
        values4.put(BabysitterContract.BabysitterEntry._ID, 4);
        values4.put(BabysitterContract.BabysitterEntry.COLUMN_BABYSITTER_NAME, "Surya Child Care Center");
        values4.put(BabysitterContract.BabysitterEntry.COLUMN_ADDRESS, "Raja Talab, Varanasi- 221311");
        values4.put(BabysitterContract.BabysitterEntry.COLUMN_CONTACT, "9889155395");
        values4.put(BabysitterContract.BabysitterEntry.COLUMN_LATITUDE, 25.27765);
        values4.put(BabysitterContract.BabysitterEntry.COLUMN_LONGITUDE, 82.94380);

        long newRowId4 = db.insert(BabysitterContract.BabysitterEntry.TABLE_NAME, null, values4);

        ContentValues values5 = new ContentValues();
        values5.put(BabysitterContract.BabysitterEntry._ID, 5);
        values5.put(BabysitterContract.BabysitterEntry.COLUMN_BABYSITTER_NAME, "Mother's Treasure");
        values5.put(BabysitterContract.BabysitterEntry.COLUMN_ADDRESS, "Ghaziabad- 201009");
        values5.put(BabysitterContract.BabysitterEntry.COLUMN_CONTACT, "9582704878");
        values5.put(BabysitterContract.BabysitterEntry.COLUMN_LATITUDE, 28.61239);
        values5.put(BabysitterContract.BabysitterEntry.COLUMN_LONGITUDE, 77.44292);

        long newRowId5 = db.insert(BabysitterContract.BabysitterEntry.TABLE_NAME, null, values5);

        ContentValues values6 = new ContentValues();
        values6.put(BabysitterContract.BabysitterEntry._ID, 6);
        values6.put(BabysitterContract.BabysitterEntry.COLUMN_BABYSITTER_NAME, "Mother Touch Services");
        values6.put(BabysitterContract.BabysitterEntry.COLUMN_ADDRESS, "Sector-49, Noida- 201301");
        values6.put(BabysitterContract.BabysitterEntry.COLUMN_CONTACT, "8368688808");
        values6.put(BabysitterContract.BabysitterEntry.COLUMN_LATITUDE, 28.55408);
        values6.put(BabysitterContract.BabysitterEntry.COLUMN_LONGITUDE, 77.36220);

        long newRowId6 = db.insert(BabysitterContract.BabysitterEntry.TABLE_NAME, null, values6);

        ContentValues values7 = new ContentValues();
        values7.put(BabysitterContract.BabysitterEntry._ID, 7);
        values7.put(BabysitterContract.BabysitterEntry.COLUMN_BABYSITTER_NAME, "Baby care Services");
        values7.put(BabysitterContract.BabysitterEntry.COLUMN_ADDRESS, "Dadri, Noida");
        values7.put(BabysitterContract.BabysitterEntry.COLUMN_CONTACT, "9999842437");
        values7.put(BabysitterContract.BabysitterEntry.COLUMN_LATITUDE, 28.58303);
        values7.put(BabysitterContract.BabysitterEntry.COLUMN_LONGITUDE, 77.40615);

        long newRowId7 = db.insert(BabysitterContract.BabysitterEntry.TABLE_NAME, null, values7);

        ContentValues values8 = new ContentValues();
        values8.put(BabysitterContract.BabysitterEntry._ID, 8);
        values8.put(BabysitterContract.BabysitterEntry.COLUMN_BABYSITTER_NAME, "Find babysitter");
        values8.put(BabysitterContract.BabysitterEntry.COLUMN_ADDRESS, "Sector-63, Noida- 201301");
        values8.put(BabysitterContract.BabysitterEntry.COLUMN_CONTACT, "8800428999");
        values8.put(BabysitterContract.BabysitterEntry.COLUMN_LATITUDE, 28.62653);
        values8.put(BabysitterContract.BabysitterEntry.COLUMN_LONGITUDE, 77.32984);

        long newRowId8 = db.insert(BabysitterContract.BabysitterEntry.TABLE_NAME, null, values8);

        ContentValues values9 = new ContentValues();
        values9.put(BabysitterContract.BabysitterEntry._ID, 9);
        values9.put(BabysitterContract.BabysitterEntry.COLUMN_BABYSITTER_NAME, "Mukesh Creche Kilkariya");
        values9.put(BabysitterContract.BabysitterEntry.COLUMN_ADDRESS, "Kidwai Nagar, Kanpur- 208011");
        values9.put(BabysitterContract.BabysitterEntry.COLUMN_CONTACT, "6307084975");
        values9.put(BabysitterContract.BabysitterEntry.COLUMN_LATITUDE, 26.42614);
        values9.put(BabysitterContract.BabysitterEntry.COLUMN_LONGITUDE, 80.31681);

        long newRowId9 = db.insert(BabysitterContract.BabysitterEntry.TABLE_NAME, null, values9);

        ContentValues values10 = new ContentValues();
        values10.put(BabysitterContract.BabysitterEntry._ID, 10);
        values10.put(BabysitterContract.BabysitterEntry.COLUMN_BABYSITTER_NAME, "Medha day Care");
        values10.put(BabysitterContract.BabysitterEntry.COLUMN_ADDRESS, "C-83, G-2, Ghaziabad- 201011");
        values10.put(BabysitterContract.BabysitterEntry.COLUMN_CONTACT, "9650591148");
        values10.put(BabysitterContract.BabysitterEntry.COLUMN_LATITUDE, 28.66982);
        values10.put(BabysitterContract.BabysitterEntry.COLUMN_LONGITUDE, 80.31681);

        long newRowId10 = db.insert(BabysitterContract.BabysitterEntry.TABLE_NAME, null, values10);

        ContentValues values11 = new ContentValues();
        values11.put(BabysitterContract.BabysitterEntry._ID, 11);
        values11.put(BabysitterContract.BabysitterEntry.COLUMN_BABYSITTER_NAME, "City Child Care Center");
        values11.put(BabysitterContract.BabysitterEntry.COLUMN_ADDRESS, "Surya Nagar, Ghaziabad- 201011");
        values11.put(BabysitterContract.BabysitterEntry.COLUMN_CONTACT, "9695317703");
        values11.put(BabysitterContract.BabysitterEntry.COLUMN_LATITUDE, 28.39695);
        values11.put(BabysitterContract.BabysitterEntry.COLUMN_LONGITUDE, 79.38399);

        long newRowId11 = db.insert(BabysitterContract.BabysitterEntry.TABLE_NAME, null, values11);

        ContentValues values12 = new ContentValues();
        values12.put(BabysitterContract.BabysitterEntry._ID, 12);
        values12.put(BabysitterContract.BabysitterEntry.COLUMN_BABYSITTER_NAME, "A&a Mother Care Playway & Babysitter");
        values12.put(BabysitterContract.BabysitterEntry.COLUMN_ADDRESS, "Daudpur, Gorakhpur- 273001");
        values12.put(BabysitterContract.BabysitterEntry.COLUMN_CONTACT, "8400870017");
        values12.put(BabysitterContract.BabysitterEntry.COLUMN_LATITUDE, 26.74827);
        values12.put(BabysitterContract.BabysitterEntry.COLUMN_LONGITUDE, 83.38084);

        long newRowId12 = db.insert(BabysitterContract.BabysitterEntry.TABLE_NAME, null, values12);

        ContentValues values13 = new ContentValues();
        values13.put(BabysitterContract.BabysitterEntry._ID, 13);
        values13.put(BabysitterContract.BabysitterEntry.COLUMN_BABYSITTER_NAME, "Orange International Play School");
        values13.put(BabysitterContract.BabysitterEntry.COLUMN_ADDRESS, "Gomti Nagar, Lucknow- 226010");
        values13.put(BabysitterContract.BabysitterEntry.COLUMN_CONTACT, "9021113569");
        values13.put(BabysitterContract.BabysitterEntry.COLUMN_LATITUDE, 26.84913);
        values13.put(BabysitterContract.BabysitterEntry.COLUMN_LONGITUDE, 80.97840);

        long newRowId13 = db.insert(BabysitterContract.BabysitterEntry.TABLE_NAME, null, values13);

        ContentValues values14 = new ContentValues();
        values14.put(BabysitterContract.BabysitterEntry._ID, 14);
        values14.put(BabysitterContract.BabysitterEntry.COLUMN_BABYSITTER_NAME, "UC Kindies");
        values14.put(BabysitterContract.BabysitterEntry.COLUMN_ADDRESS, "Gomti Nagar, Lucknow- 226010");
        values14.put(BabysitterContract.BabysitterEntry.COLUMN_CONTACT, "011-40126103");
        values14.put(BabysitterContract.BabysitterEntry.COLUMN_LATITUDE, 26.86383);
        values14.put(BabysitterContract.BabysitterEntry.COLUMN_LONGITUDE, 81.01135);

        long newRowId14 = db.insert(BabysitterContract.BabysitterEntry.TABLE_NAME, null, values14);

        ContentValues values15 = new ContentValues();
        values15.put(BabysitterContract.BabysitterEntry._ID, 15);
        values15.put(BabysitterContract.BabysitterEntry.COLUMN_BABYSITTER_NAME, "Foster Kids");
        values15.put(BabysitterContract.BabysitterEntry.COLUMN_ADDRESS, "Indra Nagar, Lucknow- 226016");
        values15.put(BabysitterContract.BabysitterEntry.COLUMN_CONTACT, "7839465255");
        values15.put(BabysitterContract.BabysitterEntry.COLUMN_LATITUDE, 26.89108);
        values15.put(BabysitterContract.BabysitterEntry.COLUMN_LONGITUDE, 81.00048);

        long newRowId15 = db.insert(BabysitterContract.BabysitterEntry.TABLE_NAME, null, values15);

        ContentValues values16 = new ContentValues();
        values16.put(BabysitterContract.BabysitterEntry._ID, 16);
        values16.put(BabysitterContract.BabysitterEntry.COLUMN_BABYSITTER_NAME, "Small Wonders");
        values16.put(BabysitterContract.BabysitterEntry.COLUMN_ADDRESS, "Maldahiya, Varanasi- 221001");
        values16.put(BabysitterContract.BabysitterEntry.COLUMN_CONTACT, "9235609021");
        values16.put(BabysitterContract.BabysitterEntry.COLUMN_LATITUDE, 25.32175);
        values16.put(BabysitterContract.BabysitterEntry.COLUMN_LONGITUDE, 82.99368);

        long newRowId16 = db.insert(BabysitterContract.BabysitterEntry.TABLE_NAME, null, values16);

        ContentValues values17 = new ContentValues();
        values17.put(BabysitterContract.BabysitterEntry._ID, 17);
        values17.put(BabysitterContract.BabysitterEntry.COLUMN_BABYSITTER_NAME, "Rainbow A Daycare & Child grooming center");
        values17.put(BabysitterContract.BabysitterEntry.COLUMN_ADDRESS, "Mahmoorganj Road, Varanasi- 221010");
        values17.put(BabysitterContract.BabysitterEntry.COLUMN_CONTACT, "7376611171");
        values17.put(BabysitterContract.BabysitterEntry.COLUMN_LATITUDE, 25.30937);
        values17.put(BabysitterContract.BabysitterEntry.COLUMN_LONGITUDE, 82.98813);

        long newRowId17 = db.insert(BabysitterContract.BabysitterEntry.TABLE_NAME, null, values17);

        ContentValues values18 = new ContentValues();
        values18.put(BabysitterContract.BabysitterEntry._ID, 18);
        values18.put(BabysitterContract.BabysitterEntry.COLUMN_BABYSITTER_NAME, "Junior Kids World");
        values18.put(BabysitterContract.BabysitterEntry.COLUMN_ADDRESS, "Shivaji Nagar, Varanasi- 221001");
        values18.put(BabysitterContract.BabysitterEntry.COLUMN_CONTACT, "8574054916");
        values18.put(BabysitterContract.BabysitterEntry.COLUMN_LATITUDE, 25.32737);
        values18.put(BabysitterContract.BabysitterEntry.COLUMN_LONGITUDE, 83.02616);

        long newRowId18 = db.insert(BabysitterContract.BabysitterEntry.TABLE_NAME, null, values18);

        ContentValues values19 = new ContentValues();
        values19.put(BabysitterContract.BabysitterEntry._ID, 19);
        values19.put(BabysitterContract.BabysitterEntry.COLUMN_BABYSITTER_NAME, "The Little Wonders");
        values19.put(BabysitterContract.BabysitterEntry.COLUMN_ADDRESS, "Bikharipur, Varanasi- 221003");
        values19.put(BabysitterContract.BabysitterEntry.COLUMN_CONTACT, "9307079528");
        values19.put(BabysitterContract.BabysitterEntry.COLUMN_LATITUDE, 25.27565);
        values19.put(BabysitterContract.BabysitterEntry.COLUMN_LONGITUDE, 82.96845);

        long newRowId19 = db.insert(BabysitterContract.BabysitterEntry.TABLE_NAME, null, values19);

        ContentValues values20 = new ContentValues();
        values20.put(BabysitterContract.BabysitterEntry._ID, 20);
        values20.put(BabysitterContract.BabysitterEntry.COLUMN_BABYSITTER_NAME, "Bachpan");
        values20.put(BabysitterContract.BabysitterEntry.COLUMN_ADDRESS, "Taktakpur, Varanasi- 221001");
        values20.put(BabysitterContract.BabysitterEntry.COLUMN_CONTACT, "8948588886");
        values20.put(BabysitterContract.BabysitterEntry.COLUMN_LATITUDE, 25.35839);
        values20.put(BabysitterContract.BabysitterEntry.COLUMN_LONGITUDE, 82.98174);

        long newRowId20 = db.insert(BabysitterContract.BabysitterEntry.TABLE_NAME, null, values20);

    }


    public void delete(){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL("delete from "+ BabysitterContract.BabysitterEntry.TABLE_NAME);
        db.close();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();

            } else {
                Toast.makeText(getContext(), "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCurrentLocation() {

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setInterval(30000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ContextCompat.checkSelfPermission(
                getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
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

                                String str1=latitude+","+longitude;

                                Toast.makeText(getContext(), str1, Toast.LENGTH_SHORT).show();

                                Log.i(TAG,"///////-----latitude----"+latitude+"-----longitude-----"+longitude);
                            }

                        }
                    }, Looper.getMainLooper());
        }
    }


}
