package com.example.babysittingapp;

import android.provider.BaseColumns;

public final class BabysitterContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private BabysitterContract() {}

    /**
     * Inner class that defines constant values for the babysitter database table.
     * Each entry in the table represents a single babysitter.
     */
    public static final class BabysitterEntry implements BaseColumns {

        /** Name of database table for babysitters */
        public final static String TABLE_NAME = "babysitters";

        public final static String _ID = BaseColumns._ID;


        public final static String COLUMN_BABYSITTER_NAME ="name";

        public final static String COLUMN_ADDRESS ="address";

        public final static String COLUMN_CONTACT ="contact";

        public final static String COLUMN_LATITUDE="lat";

        public final static String COLUMN_LONGITUDE="long";

    }

}

