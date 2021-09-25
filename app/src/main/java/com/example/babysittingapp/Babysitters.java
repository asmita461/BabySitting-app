package com.example.babysittingapp;

public class Babysitters {

    private String mName;

    private String mContact;

    private String mAddress;

    public Babysitters(String name, String contact, String address){
        mName=name;
        mContact=contact;
        mAddress=address;
    }

    public String getBabysitterName(){
        return mName;
    }

    public String getBabysitterContact() {
        return mContact;
    }

    public String getBabysitterAddress() {
        return mAddress;
    }

}
