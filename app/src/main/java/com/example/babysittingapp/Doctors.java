package com.example.babysittingapp;

public class Doctors{

private String mName;

private String mHospital;

private int mImageResourceId = NO_IMAGE_PROVIDED;

private static final int NO_IMAGE_PROVIDED = -1;

private String mExperience;

private String mContact;

private String mUri;

// constructor for the new Doctors object
public Doctors(String name, String experience, String hospital, String contact){
     mName=name;
     mExperience=experience;
     mHospital=hospital;
     mContact=contact;
    }

// constructor for the new doctors object with image Resource id
public Doctors(String name, String experience, String hospital, String contact, int imageResourceId, String uri){
        mName=name;
        mExperience=experience;
        mHospital=hospital;
        mContact=contact;
        mImageResourceId = imageResourceId;
        mUri = uri;
        }

//get the name
public String getDoctorName(){
        return mName;
        }

//get the hospital name
public String getHospitalName() {
        return mHospital;
        }

//get the image resource Id
public int getImageResourceId() {
        return mImageResourceId;
        }

//get the experience
public String getExperience() {
    return mExperience;
}

//get the contact
public String getContact() {
    return mContact;
}

public String getUri() { return mUri; }

}
