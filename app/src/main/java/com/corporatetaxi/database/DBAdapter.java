package com.corporatetaxi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.corporatetaxi.beans.Allbeans;

import java.util.ArrayList;
import java.util.List;

public class DBAdapter extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "DBAdapter";
    private static final String DATABASE_NAME = "corporateuser.db";

    /* TABLE NAME*/
    public static final String TBL_TRIP = "tbl_trip";
    public static final String TBL_USER = "tbl_user";
    public static final String TBL_LOCATION = "tbl_location";
    public static final String TBL_NOTIFICATION = "tbl_notification";
    public static final String TBL_TRIP_HISTORY = "tbl_trip_history";

    /* KEY NAME*/
    public static final String ID = "id";
    public static final String KEY_DRIVER_ID = "driver_id";
    public static final String KEY_TRIP_ID = "trip_id";
    public static final String KEY_DISTANCE = "distance";
    public static final String KEY_AGREEMENT = "agreement";
    public static final String KEY_FARE = "fare";
    public static final String KEY_DATE = "date";
    public static final String KEY_ADDED_ON = "added_on";
    public static final String KEY_NAME = "name";
    public static final String KEY_DRIVERNAME = "drivername";
    public static final String KEY_USERIMAGE = "userimage";
    public static final String KEY_EMAILID = "emailid";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_DRIVER_IMAGE = "driverImage";
    public static final String KEY_SOURCEADDRESS = "sourceAddress";
    public static final String KEY_DESTINATIONADDRESS = "destinationAddress";
    public static final String KEY_WHERETOCOMETRIP = "whereToComeTrip";
    public static final String KEY_CURRENTLATITUDE = "currentLatitude";
    public static final String KEY_CURRENTLOGITUDE = "currentlogitude";
    public static final String KEY_SOURCELATITUDE = "sourceLatitude";
    public static final String KEY_SOURCELOGITUDE = "sourcelogitude";
    public static final String KEY_DESTINATIONLATITUDE = "destinationLatitude";
    public static final String KEY_DESTINATIONLOGITUDE = "destinationLogitude";
    public static final String KEY_TRIPTYPE = "triptype";
    public static final String KEY_TRAVELTIME = "travel_time";
    public static final String KEY_CORPORATETYPE = "corporate_type";
    public static final String KEY_NOTIFY_HEADING = "notify_heading";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_HISTORY_MONTH = "history_month";
    public static final String KEY_DRIVER_MOBILE = "drive_mobile";
    public static final String KEY_DRIVER_COMPANYNAME = "drive_companyname";
    public static final String KEY_DRIVER_TAXINUMBER = "drive_taxinum";
    public static final String KEY_DRIVER_RATING = "drive_rating";
    public static final String KEY_DRIVER_COMMENT = "drive_comment";


    private static final String CREATE_TBL_LOCATION = "create table "
            + TBL_LOCATION + " (" + ID
            + " integer primary key autoincrement, "
            + KEY_DRIVER_ID + " text null, "
            + KEY_CURRENTLATITUDE + " text null, "
            + KEY_CURRENTLOGITUDE + " text null, "
            + KEY_ADDED_ON + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ");";

    private static final String CREATE_TBL_USER = "create table "
            + TBL_USER + " (" + ID
            + " integer primary key autoincrement, "
            + KEY_DRIVER_ID + " text null, "
            + KEY_NAME + " text null, "
            + KEY_DRIVERNAME + " text null, "
            + KEY_USERIMAGE + " text null, "
            + KEY_EMAILID + " text null, "
            + KEY_ADDRESS + " text null, "
            + KEY_PHONE + " text null, "
            + KEY_ADDED_ON + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ");";

    private static final String CREATE_TBL_TRIP = "create table "
            + TBL_TRIP + " (" + ID
            + " integer primary key, "
            + KEY_TRIP_ID + " integer null, "
            + KEY_DISTANCE + " text null, " + KEY_DRIVER_MOBILE + " text null, " + KEY_DRIVER_COMPANYNAME + " text null, " + KEY_DRIVER_TAXINUMBER + " text null, " + KEY_DRIVER_RATING + " text null, "
            + KEY_AGREEMENT + " text null, " + KEY_DRIVER_COMMENT + " text null, "
            + KEY_FARE + " text null, "
            + KEY_DATE + " text null,"
            + KEY_DRIVERNAME + " text null,"
            + KEY_DRIVER_IMAGE + " text null,"
            + KEY_SOURCEADDRESS + " text null,"
            + KEY_DESTINATIONADDRESS + " text null,"
            + KEY_WHERETOCOMETRIP + " text null,"
            + KEY_SOURCELATITUDE + " text null,"
            + KEY_SOURCELOGITUDE + " text null,"
            + KEY_DESTINATIONLATITUDE + " text null,"
            + KEY_DESTINATIONLOGITUDE + " text null,"
            + KEY_TRIPTYPE + " text null,"
            + KEY_TRAVELTIME + " text null,"
            + KEY_CORPORATETYPE + " text null,"
            + KEY_ADDED_ON + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ");";

    private static final String CREATE_TBL_TRIP_HISTORY = "create table "
            + TBL_TRIP_HISTORY + " (" + ID
            + " integer primary key, "
            + KEY_TRIP_ID + " integer null, "
            + KEY_DISTANCE + " text null, "
            + KEY_DATE + " DATETIME null,"
            + KEY_FARE + " text null, "
            + KEY_TRIPTYPE + " text null," + KEY_DRIVER_MOBILE + " text null, " + KEY_DRIVER_COMPANYNAME + " text null, "
            + KEY_HISTORY_MONTH + " text null," + KEY_DRIVER_COMMENT + " text null," + KEY_DRIVER_TAXINUMBER + " text null, " + KEY_DRIVER_RATING + " text null, "
            + KEY_DRIVERNAME + " text null,"
            + KEY_DRIVER_IMAGE + " text null,"
            + KEY_SOURCEADDRESS + " text null,"
            + KEY_DESTINATIONADDRESS + " text null,"
            + KEY_SOURCELATITUDE + " text null,"
            + KEY_SOURCELOGITUDE + " text null,"
            + KEY_DESTINATIONLATITUDE + " text null,"
            + KEY_DESTINATIONLOGITUDE + " text null,"
            + KEY_ADDED_ON + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ");";

    private static final String CREATE_TBL_NOTIFICATION = "create table "
            + TBL_NOTIFICATION + " (" + ID
            + " integer primary key autoincrement, "
            + KEY_TRIP_ID + " integer null, "
            + KEY_NOTIFY_HEADING + " text null, "
            + KEY_DESCRIPTION + " text null, "
            + KEY_ADDED_ON + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ");";

    public DBAdapter(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        Log.d(TAG, "onCreate");
        db.execSQL(CREATE_TBL_USER);
        db.execSQL(CREATE_TBL_TRIP);
        db.execSQL(CREATE_TBL_LOCATION);
        db.execSQL(CREATE_TBL_NOTIFICATION);
        db.execSQL(CREATE_TBL_TRIP_HISTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        String query0 = "DROP TABLE IF EXISTS " + TBL_USER;
        String query1 = "DROP TABLE IF EXISTS " + TBL_TRIP;
        String query2 = "DROP TABLE IF EXISTS " + TBL_LOCATION;
        String query3 = "DROP TABLE IF EXISTS " + TBL_NOTIFICATION;
        String query4 = "DROP TABLE IF EXISTS " + TBL_TRIP_HISTORY;

        db.execSQL(query0);
        db.execSQL(query1);
        db.execSQL(query2);
        db.execSQL(query3);
        db.execSQL(query4);

        onCreate(db);
    }

    // Insert Data
    // TODO Auto-generated method stub

//    public long insertUser(Allbeans user) {
//
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues initialValues = new ContentValues();
//        initialValues.put(KEY_DRIVER_ID, user.getId());
//        initialValues.put(KEY_NAME, user.getName());
//        initialValues.put(KEY_DRIVERNAME, user.getUserName());
//        initialValues.put(KEY_USERIMAGE, user.getUserImage());
//        initialValues.put(KEY_PHONE, user.getPhone());
//        initialValues.put(KEY_EMAILID, user.getEmailId());
//        initialValues.put(KEY_ADDRESS, user.getAddress());
//
//        long id = db.insert(TBL_USER, null, initialValues);
//
//        // Log.d("insertUser", initialValues.toString());
//
//        db.close();
//        return id;
//    }

//    public long updateUser(Allbeans user) {
//
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues initialValues = new ContentValues();
//        initialValues.put(KEY_NAME, user.getName());
//        initialValues.put(KEY_DRIVERNAME, user.getUserName());
//        initialValues.put(KEY_USERIMAGE, user.getUserImage());
//        initialValues.put(KEY_PHONE, user.getPhone());
//        initialValues.put(KEY_EMAILID, user.getEmailId());
//        initialValues.put(KEY_ADDRESS, user.getAddress());
//
//        long id = db.update(TBL_USER, initialValues, KEY_DRIVER_ID + " = ?",
//                new String[]{String.valueOf(user.getId())});
//        //Log.d("UpdateUser", initialValues.toString());
//        db.close();
//        return id;
//    }

//    public long insertLocation(Allbeans user) {
//
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues initialValues = new ContentValues();
//        initialValues.put(ID, user.getId());
//        initialValues.put(KEY_CURRENTLATITUDE, user.getCurrentLatitude());
//        initialValues.put(KEY_CURRENTLOGITUDE, user.getCurrentLongitude());
//        initialValues.put(KEY_ADDED_ON, user.getAddedOn());
//
//        long id = db.insert(TBL_LOCATION, null, initialValues);
//
//        db.close();
//        return id;
//    }

    public long insertNotification(Allbeans notification) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TRIP_ID, notification.getTriphistoryid());
        initialValues.put(KEY_NOTIFY_HEADING, notification.getHeader());
        initialValues.put(KEY_DESCRIPTION, notification.getDescription());

        long id = db.insert(TBL_NOTIFICATION, null, initialValues);

        db.close();
        return id;
    }

    public long insertTrip(Allbeans trip) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TRIP_ID, trip.getTriphistoryid());
        initialValues.put(KEY_DISTANCE, trip.getDistance());
        initialValues.put(KEY_DRIVER_MOBILE, trip.getDrivermobilenumber());
        initialValues.put(KEY_FARE, trip.getTripprice());
        initialValues.put(KEY_DRIVER_COMPANYNAME, trip.getCompanyname());
        initialValues.put(KEY_DATE, trip.getTripdate());
        initialValues.put(KEY_SOURCEADDRESS, trip.getSourcerequest());
        initialValues.put(KEY_DESTINATIONADDRESS, trip.getDestinationresuest());
        initialValues.put(KEY_SOURCELATITUDE, trip.getSourcelatitute());
        initialValues.put(KEY_SOURCELOGITUDE, trip.getSourcelongitude());
        initialValues.put(KEY_DESTINATIONLATITUDE, trip.getDestinationlatitude());
        initialValues.put(KEY_DESTINATIONLOGITUDE, trip.getDestinationlongitude());
        initialValues.put(KEY_TRIPTYPE, trip.getTripstatus());
        initialValues.put(KEY_DRIVERNAME, trip.getDrivername());
        initialValues.put(KEY_DRIVER_IMAGE, trip.getDriverimage());
        initialValues.put(KEY_DRIVER_TAXINUMBER, trip.getTaxinumber());
        initialValues.put(KEY_DRIVER_RATING, trip.getRatingvalue());
        initialValues.put(KEY_DRIVER_COMMENT, trip.getComment());
        initialValues.put(KEY_TRAVELTIME, trip.getTriptime());

        long id = db.insert(TBL_TRIP, null, initialValues);

        db.close();
        return id;
    }

    public long insertTripHistory(Allbeans trip) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TRIP_ID, trip.getTriphistoryid());
        initialValues.put(KEY_DISTANCE, trip.getDistance());
        initialValues.put(KEY_DRIVER_MOBILE, trip.getDrivermobilenumber());
        initialValues.put(KEY_FARE, trip.getTripprice());
        initialValues.put(KEY_DRIVER_COMPANYNAME, trip.getCompanyname());
        initialValues.put(KEY_DATE, trip.getTripdate());
        initialValues.put(KEY_SOURCEADDRESS, trip.getSourcerequest());
        initialValues.put(KEY_DESTINATIONADDRESS, trip.getDestinationresuest());
        initialValues.put(KEY_SOURCELATITUDE, trip.getSourcelatitute());
        initialValues.put(KEY_SOURCELOGITUDE, trip.getSourcelongitude());
        initialValues.put(KEY_DESTINATIONLATITUDE, trip.getDestinationlatitude());
        initialValues.put(KEY_DESTINATIONLOGITUDE, trip.getDestinationlongitude());
        initialValues.put(KEY_TRIPTYPE, trip.getTripstatus());
        initialValues.put(KEY_DRIVERNAME, trip.getDrivername());
        initialValues.put(KEY_DRIVER_IMAGE, trip.getDriverimage());
        initialValues.put(KEY_DRIVER_TAXINUMBER, trip.getTaxinumber());
        initialValues.put(KEY_DRIVER_RATING, trip.getRatingvalue());
        initialValues.put(KEY_DRIVER_COMMENT, trip.getComment());
       // initialValues.put(KEY_TRAVELTIME, trip.getTriptime());

        long id = db.insert(TBL_TRIP_HISTORY, null, initialValues);

        db.close();
        return id;
    }

    public long updateTrip(Allbeans trip) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_DISTANCE, trip.getDistance());

        long id = db.update(TBL_TRIP, initialValues, KEY_TRIP_ID + " = ?",
                new String[]{String.valueOf(trip.getTriphistoryid())});

        db.close();
        return id;
    }

    public long updateTripHistory(Allbeans trip) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TRIP_ID, trip.getTriphistoryid());
        initialValues.put(KEY_DISTANCE, trip.getDistance());
        initialValues.put(KEY_DRIVER_MOBILE, trip.getDrivermobilenumber());
        initialValues.put(KEY_FARE, trip.getTripprice());
        initialValues.put(KEY_DRIVER_COMPANYNAME, trip.getCompanyname());
        initialValues.put(KEY_DATE, trip.getTripdate());
        initialValues.put(KEY_SOURCEADDRESS, trip.getSourcerequest());
        initialValues.put(KEY_DESTINATIONADDRESS, trip.getDestinationresuest());
        initialValues.put(KEY_SOURCELATITUDE, trip.getSourcelatitute());
        initialValues.put(KEY_SOURCELOGITUDE, trip.getSourcelongitude());
        initialValues.put(KEY_DESTINATIONLATITUDE, trip.getDestinationlatitude());
        initialValues.put(KEY_DESTINATIONLOGITUDE, trip.getDestinationlongitude());
        initialValues.put(KEY_TRIPTYPE, trip.getTripstatus());
        initialValues.put(KEY_DRIVERNAME, trip.getDrivername());
        initialValues.put(KEY_DRIVER_IMAGE, trip.getDriverimage());
        initialValues.put(KEY_DRIVER_TAXINUMBER, trip.getTaxinumber());
        initialValues.put(KEY_DRIVER_RATING, trip.getRatingvalue());
        initialValues.put(KEY_DRIVER_COMMENT, trip.getComment());
        initialValues.put(KEY_HISTORY_MONTH, trip.getTripmonth());
        initialValues.put(KEY_TRAVELTIME, trip.getTriptime());

        long id = db.update(TBL_TRIP_HISTORY, initialValues, KEY_TRIP_ID + " = ?",
                new String[]{String.valueOf(trip.getTriphistoryid())});

        db.close();
        return id;
    }


    //TODO GET ALL DATA FROM DATABASE


//    public ArrayList<User> getUserList() {
//
//        ArrayList<User> userList = new ArrayList<User>();
//        try {
//            String query = "select * from " + TBL_USER + ";";
//            System.out.println("query : " + query);
//            SQLiteDatabase db = this.getReadableDatabase();
//            Cursor cursor = db.rawQuery(query, null);
//            if (cursor == null) {
//                return userList;
//            } else if (cursor.getCount() == 0) {
//                return userList;
//            }
//            if (cursor.moveToFirst()) {
//
//                do {
//                    User user = new User();
//                    user.setId(cursor.getLong(cursor.getColumnIndex(KEY_DRIVER_ID)));
//                    user.setName(cursor.getString(cursor
//                            .getColumnIndex(KEY_NAME)));
//                    user.setUserName(cursor.getString(cursor
//                            .getColumnIndex(KEY_DRIVERNAME)));
//                    user.setUserImage(cursor.getString(cursor
//                            .getColumnIndex(KEY_USERIMAGE)));
//                    user.setPhone(cursor.getString(cursor
//                            .getColumnIndex(KEY_PHONE)));
//                    user.setEmailId(cursor.getString(cursor
//                            .getColumnIndex(KEY_EMAILID)));
//                    user.setAddress(cursor.getString(cursor
//                            .getColumnIndex(KEY_ADDRESS)));
//                    user.setAddedOn(cursor.getString(cursor
//                            .getColumnIndex(KEY_ADDED_ON)));
//
//
//                    userList.add(user);
//                } while (cursor.moveToNext());
//            }
//            if (cursor != null && !cursor.isClosed()) {
//                cursor.close();
//            }
//            db.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return userList;
//
//    }

//    public Allbeans getUser(int id) {
//
//        Allbeans user = new Allbeans();
//        try {
//            String query = "select * from " + TBL_USER + " where " + KEY_DRIVER_ID + " = " + id + ";";
//            System.out.println("query : " + query);
//            SQLiteDatabase db = this.getReadableDatabase();
//            Cursor cursor = db.rawQuery(query, null);
//            if (cursor == null) {
//                return user;
//            } else if (cursor.getCount() == 0) {
//                return user;
//            }
//            if (cursor.moveToFirst()) {
//
//                do {
//                    //  User user = new User();
//                    user.setTriphistoryid((int) cursor.getLong(cursor.getColumnIndex(KEY_DRIVER_ID)));
//                    user.setName(cursor.getString(cursor
//                            .getColumnIndex(KEY_NAME)));
//                    user.setDrivername(cursor.getString(cursor
//                            .getColumnIndex(KEY_DRIVERNAME)));
//                    user.setDriverimage(cursor.getString(cursor
//                            .getColumnIndex(KEY_USERIMAGE)));
//                    user.setDrivermobilenumber(cursor.getString(cursor
//                            .getColumnIndex(KEY_PHONE)));
//                    user.setEmailId(cursor.getString(cursor
//                            .getColumnIndex(KEY_EMAILID)));
//                    user.setAddress(cursor.getString(cursor
//                            .getColumnIndex(KEY_ADDRESS)));
//                    user.setAddedOn(cursor.getString(cursor
//                            .getColumnIndex(KEY_ADDED_ON)));
//
//                } while (cursor.moveToNext());
//            }
//            if (cursor != null && !cursor.isClosed()) {
//                cursor.close();
//            }
//            db.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return user;
//
//    }

//    public ArrayList<User> getLocation() {
//
//        ArrayList<User> userList = new ArrayList<User>();
//        try {
//            String query = "select * from " + TBL_LOCATION + ";";
//            System.out.println("query : " + query);
//            SQLiteDatabase db = this.getReadableDatabase();
//            Cursor cursor = db.rawQuery(query, null);
//            if (cursor == null) {
//                return userList;
//            } else if (cursor.getCount() == 0) {
//                return userList;
//            }
//            if (cursor.moveToFirst()) {
//
//                do {
//                    User user = new User();
//                    user.setId(cursor.getLong(cursor.getColumnIndex(ID)));
//                    user.setCurrentLatitude(cursor.getDouble(cursor
//                            .getColumnIndex(KEY_CURRENTLATITUDE)));
//                    user.setCurrentLongitude(cursor.getDouble(cursor
//                            .getColumnIndex(KEY_CURRENTLOGITUDE)));
//                    user.setAddedOn(cursor.getString(cursor
//                            .getColumnIndex(KEY_ADDED_ON)));
//
//                    Log.d("dateAddedOn", cursor.getString(cursor
//                            .getColumnIndex(KEY_ADDED_ON)));
//                    userList.add(user);
//                } while (cursor.moveToNext());
//            }
//            if (cursor != null && !cursor.isClosed()) {
//                cursor.close();
//            }
//            db.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return userList;
//
//    }

    public List<Allbeans> getAllNotification() {

        List<Allbeans> notificationList = new ArrayList<Allbeans>();
        try {
            String query = "select * from " + TBL_NOTIFICATION + " ORDER BY " + ID + " DESC;";
            System.out.println("query : " + query);
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if (cursor == null) {
                return notificationList;
            } else if (cursor.getCount() == 0) {
                return notificationList;
            }
            if (cursor.moveToFirst()) {

                do {
                    Allbeans notification = new Allbeans();
                    notification.setHeader(cursor.getString(cursor
                            .getColumnIndex(KEY_NOTIFY_HEADING)));
                    notification.setDescription(cursor.getString(cursor
                            .getColumnIndex(KEY_DESCRIPTION)));


                    notificationList.add(notification);
                } while (cursor.moveToNext());
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return notificationList;

    }

    public ArrayList<Allbeans> getTrip() {

        ArrayList<Allbeans> tripList = new ArrayList<Allbeans>();
        try {
            String query = "select * from " + TBL_TRIP + " ORDER BY " + KEY_TRIP_ID + " DESC;";
            System.out.println("query : " + query);
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if (cursor == null) {
                return tripList;
            } else if (cursor.getCount() == 0) {
                return tripList;
            }
            if (cursor.moveToFirst()) {

                do {
                    Allbeans trip = new Allbeans();
                    trip.setTriphistoryid((int) cursor.getLong(cursor.getColumnIndex(KEY_TRIP_ID)));
                    trip.setDistance(cursor.getString(cursor
                            .getColumnIndex(KEY_DISTANCE)));
                    trip.setCompanyname(cursor.getString(cursor
                            .getColumnIndex(KEY_DRIVER_COMPANYNAME)));
                    trip.setTripprice(cursor.getString(cursor
                            .getColumnIndex(KEY_FARE)));
                    trip.setTripdate(cursor.getString(cursor
                            .getColumnIndex(KEY_DATE)));
                    trip.setDrivername(cursor.getString(cursor
                            .getColumnIndex(KEY_DRIVERNAME)));
                    trip.setDrivermobilenumber(cursor.getString(cursor
                            .getColumnIndex(KEY_DRIVER_MOBILE)));
                    trip.setSourcerequest(cursor.getString(cursor
                            .getColumnIndex(KEY_SOURCEADDRESS)));
                    trip.setDestinationresuest(cursor.getString(cursor
                            .getColumnIndex(KEY_DESTINATIONADDRESS)));
                    trip.setSourcelatitute(cursor.getDouble(cursor
                            .getColumnIndex(KEY_SOURCELATITUDE)));
                    trip.setSourcelongitude(cursor.getDouble(cursor
                            .getColumnIndex(KEY_SOURCELOGITUDE)));
                    trip.setDestinationlatitude(cursor.getDouble(cursor
                            .getColumnIndex(KEY_DESTINATIONLATITUDE)));
                    trip.setDestinationlongitude(cursor.getDouble(cursor
                            .getColumnIndex(KEY_DESTINATIONLOGITUDE)));
                    trip.setTripstatus(cursor.getString(cursor
                            .getColumnIndex(KEY_TRIPTYPE)));
//                    trip.setTriptime(cursor.getString(cursor
//                            .getColumnIndex(KEY_TRAVELTIME)));
                    trip.setDriverimage(cursor.getString(cursor.getColumnIndex(KEY_DRIVER_IMAGE)));
                    trip.setTaxinumber(cursor.getString(cursor.getColumnIndex(KEY_DRIVER_TAXINUMBER)));
                    trip.setComment(cursor.getString(cursor.getColumnIndex(KEY_DRIVER_COMMENT)));
                    trip.setRatingvalue(cursor.getFloat(cursor.getColumnIndex(KEY_DRIVER_RATING)));


                    tripList.add(trip);
                } while (cursor.moveToNext());
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tripList;

    }

    public ArrayList<Integer> getTripId() {

        ArrayList<Integer> tripList = new ArrayList<Integer>();
        try {
            String query = "select * from " + TBL_TRIP + " ORDER BY " + ID + " DESC;";
            System.out.println("query : " + query);
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if (cursor == null) {
                return tripList;
            } else if (cursor.getCount() == 0) {
                return tripList;
            }
            if (cursor.moveToFirst()) {

                do {
                    /*Trip trip = new Trip();
                    trip.setId(cursor.getLong(cursor.getColumnIndex(ID)));
                    */

                    tripList.add(cursor.getInt(cursor.getColumnIndex(KEY_TRIP_ID)));
                } while (cursor.moveToNext());
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tripList;

    }

    public ArrayList<Allbeans> getTripHistory() {

        ArrayList<Allbeans> tripList = new ArrayList<Allbeans>();
        try {
            String query = "select * from " + TBL_TRIP_HISTORY + " ORDER BY " + KEY_TRIP_ID + " DESC;";
            System.out.println("query : " + query);
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if (cursor == null) {
                return tripList;
            } else if (cursor.getCount() == 0) {
                return tripList;
            }
            if (cursor.moveToFirst()) {

                do {
                    Allbeans trip = new Allbeans();
                    trip.setTriphistoryid((int) cursor.getLong(cursor.getColumnIndex(KEY_TRIP_ID)));
                    trip.setDistance(cursor.getString(cursor
                            .getColumnIndex(KEY_DISTANCE)));
                    trip.setCompanyname(cursor.getString(cursor
                            .getColumnIndex(KEY_DRIVER_COMPANYNAME)));
                    trip.setTripprice(cursor.getString(cursor
                            .getColumnIndex(KEY_FARE)));
                    trip.setTripdate(cursor.getString(cursor
                            .getColumnIndex(KEY_DATE)));
                    trip.setDrivername(cursor.getString(cursor
                            .getColumnIndex(KEY_DRIVERNAME)));
                    trip.setDrivermobilenumber(cursor.getString(cursor
                            .getColumnIndex(KEY_DRIVER_MOBILE)));
                    trip.setSourcerequest(cursor.getString(cursor
                            .getColumnIndex(KEY_SOURCEADDRESS)));
                    trip.setDestinationresuest(cursor.getString(cursor
                            .getColumnIndex(KEY_DESTINATIONADDRESS)));
                    trip.setSourcelatitute(cursor.getDouble(cursor
                            .getColumnIndex(KEY_SOURCELATITUDE)));
                    trip.setSourcelongitude(cursor.getDouble(cursor
                            .getColumnIndex(KEY_SOURCELOGITUDE)));
                    trip.setDestinationlatitude(cursor.getDouble(cursor
                            .getColumnIndex(KEY_DESTINATIONLATITUDE)));
                    trip.setDestinationlongitude(cursor.getDouble(cursor
                            .getColumnIndex(KEY_DESTINATIONLOGITUDE)));
                    trip.setTripstatus(cursor.getString(cursor
                            .getColumnIndex(KEY_TRIPTYPE)));
                    trip.setTriptime(cursor.getString(cursor
                            .getColumnIndex(KEY_TRAVELTIME)));
                    trip.setDriverimage(cursor.getString(cursor.getColumnIndex(KEY_DRIVER_IMAGE)));
                    trip.setTaxinumber(cursor.getString(cursor.getColumnIndex(KEY_DRIVER_TAXINUMBER)));
                    trip.setComment(cursor.getString(cursor.getColumnIndex(KEY_DRIVER_COMMENT)));
                    trip.setRatingvalue(cursor.getFloat(cursor.getColumnIndex(KEY_DRIVER_RATING)));


                    tripList.add(trip);
                } while (cursor.moveToNext());
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tripList;

    }

    public ArrayList<Allbeans> getTripHistoryFilter(String fromDate, String toDate) {

        ArrayList<Allbeans> tripList = new ArrayList<Allbeans>();
        try {
            String query = "select * from " + TBL_TRIP_HISTORY + " where " + KEY_DATE + " between '" + fromDate + "' and '" + toDate + "' ORDER BY " + KEY_TRIP_ID + " DESC;";
            System.out.println("query : " + query);
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if (cursor == null) {
                return tripList;
            } else if (cursor.getCount() == 0) {
                return tripList;
            }
            if (cursor.moveToFirst()) {

                do {
                    Allbeans trip = new Allbeans();
                    trip.setTriphistoryid((int) cursor.getLong(cursor.getColumnIndex(KEY_TRIP_ID)));
                    trip.setDistance(cursor.getString(cursor
                            .getColumnIndex(KEY_DISTANCE)));
                    trip.setCompanyname(cursor.getString(cursor
                            .getColumnIndex(KEY_DRIVER_COMPANYNAME)));
                    trip.setTripprice(cursor.getString(cursor
                            .getColumnIndex(KEY_FARE)));
                    trip.setTripdate(cursor.getString(cursor
                            .getColumnIndex(KEY_DATE)));
                    trip.setDrivername(cursor.getString(cursor
                            .getColumnIndex(KEY_DRIVERNAME)));
                    trip.setDrivermobilenumber(cursor.getString(cursor
                            .getColumnIndex(KEY_DRIVER_MOBILE)));
                    trip.setSourcerequest(cursor.getString(cursor
                            .getColumnIndex(KEY_SOURCEADDRESS)));
                    trip.setDestinationresuest(cursor.getString(cursor
                            .getColumnIndex(KEY_DESTINATIONADDRESS)));
                    trip.setSourcelatitute(cursor.getDouble(cursor
                            .getColumnIndex(KEY_SOURCELATITUDE)));
                    trip.setSourcelongitude(cursor.getDouble(cursor
                            .getColumnIndex(KEY_SOURCELOGITUDE)));
                    trip.setDestinationlatitude(cursor.getDouble(cursor
                            .getColumnIndex(KEY_DESTINATIONLATITUDE)));
                    trip.setDestinationlongitude(cursor.getDouble(cursor
                            .getColumnIndex(KEY_DESTINATIONLOGITUDE)));
                    trip.setTripstatus(cursor.getString(cursor
                            .getColumnIndex(KEY_TRIPTYPE)));
                    trip.setTriptime(cursor.getString(cursor
                            .getColumnIndex(KEY_TRAVELTIME)));
                    trip.setDriverimage(cursor.getString(cursor.getColumnIndex(KEY_DRIVER_IMAGE)));
                    trip.setTaxinumber(cursor.getString(cursor.getColumnIndex(KEY_DRIVER_TAXINUMBER)));
                    trip.setComment(cursor.getString(cursor.getColumnIndex(KEY_DRIVER_COMMENT)));
                    trip.setRatingvalue(cursor.getFloat(cursor.getColumnIndex(KEY_DRIVER_RATING)));


                    tripList.add(trip);
                } while (cursor.moveToNext());
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tripList;

    }

    public ArrayList<Allbeans> getTripHistoryByMonth(String month) {

        ArrayList<Allbeans> tripList = new ArrayList<Allbeans>();
        try {
            String query = "select * from " + TBL_TRIP_HISTORY + " where " + KEY_HISTORY_MONTH + " = '" + month + "' ORDER BY " + KEY_TRIP_ID + " DESC;";
            System.out.println("query : " + query);
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if (cursor == null) {
                return tripList;
            } else if (cursor.getCount() == 0) {
                return tripList;
            }
            if (cursor.moveToFirst()) {

                do {
                    Allbeans trip = new Allbeans();
                    trip.setTriphistoryid((int) cursor.getLong(cursor.getColumnIndex(KEY_TRIP_ID)));

                    trip.setDistance(cursor.getString(cursor
                            .getColumnIndex(KEY_DISTANCE)));
                    trip.setCompanyname(cursor.getString(cursor
                            .getColumnIndex(KEY_DRIVER_COMPANYNAME)));
                    trip.setTripprice(cursor.getString(cursor
                            .getColumnIndex(KEY_FARE)));
                    trip.setTripdate(cursor.getString(cursor
                            .getColumnIndex(KEY_DATE)));
                    trip.setDrivername(cursor.getString(cursor
                            .getColumnIndex(KEY_DRIVERNAME)));
                    trip.setDrivermobilenumber(cursor.getString(cursor
                            .getColumnIndex(KEY_DRIVER_MOBILE)));
                    trip.setSourcerequest(cursor.getString(cursor
                            .getColumnIndex(KEY_SOURCEADDRESS)));
                    trip.setDestinationresuest(cursor.getString(cursor
                            .getColumnIndex(KEY_DESTINATIONADDRESS)));
                    trip.setSourcelatitute(cursor.getDouble(cursor
                            .getColumnIndex(KEY_SOURCELATITUDE)));
                    trip.setSourcelongitude(cursor.getDouble(cursor
                            .getColumnIndex(KEY_SOURCELOGITUDE)));
                    trip.setDestinationlatitude(cursor.getDouble(cursor
                            .getColumnIndex(KEY_DESTINATIONLATITUDE)));
                    trip.setDestinationlongitude(cursor.getDouble(cursor
                            .getColumnIndex(KEY_DESTINATIONLOGITUDE)));
                    trip.setTripstatus(cursor.getString(cursor
                            .getColumnIndex(KEY_TRIPTYPE)));
                    trip.setTriptime(cursor.getString(cursor
                            .getColumnIndex(KEY_TRAVELTIME)));
                    trip.setDriverimage(cursor.getString(cursor.getColumnIndex(KEY_DRIVER_IMAGE)));
                    trip.setTaxinumber(cursor.getString(cursor.getColumnIndex(KEY_DRIVER_TAXINUMBER)));
                    trip.setComment(cursor.getString(cursor.getColumnIndex(KEY_DRIVER_COMMENT)));
                    trip.setRatingvalue(cursor.getFloat(cursor.getColumnIndex(KEY_DRIVER_RATING)));


                    tripList.add(trip);
                } while (cursor.moveToNext());
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tripList;

    }

    public ArrayList<Allbeans> getTripHistoryByMonthWithDate(String month, String fromDate, String toDate) {

        ArrayList<Allbeans> tripList = new ArrayList<Allbeans>();
        try {
            String query = "select * from " + TBL_TRIP_HISTORY + " where " + KEY_HISTORY_MONTH + " = '" + month + "' and " + KEY_DATE + " between '" + fromDate + "' and '" + toDate + "' ORDER BY " + KEY_TRIP_ID + " DESC;";
            System.out.println("query : " + query);
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if (cursor == null) {
                return tripList;
            } else if (cursor.getCount() == 0) {
                return tripList;
            }
            if (cursor.moveToFirst()) {

                do {
                    Allbeans trip = new Allbeans();
                    trip.setTriphistoryid((int) cursor.getLong(cursor.getColumnIndex(KEY_TRIP_ID)));

                    trip.setDistance(cursor.getString(cursor
                            .getColumnIndex(KEY_DISTANCE)));
                    trip.setCompanyname(cursor.getString(cursor
                            .getColumnIndex(KEY_DRIVER_COMPANYNAME)));
                    trip.setTripprice(cursor.getString(cursor
                            .getColumnIndex(KEY_FARE)));
                    trip.setTripdate(cursor.getString(cursor
                            .getColumnIndex(KEY_DATE)));
                    trip.setDrivername(cursor.getString(cursor
                            .getColumnIndex(KEY_DRIVERNAME)));
                    trip.setDrivermobilenumber(cursor.getString(cursor
                            .getColumnIndex(KEY_DRIVER_MOBILE)));
                    trip.setSourcerequest(cursor.getString(cursor
                            .getColumnIndex(KEY_SOURCEADDRESS)));
                    trip.setDestinationresuest(cursor.getString(cursor
                            .getColumnIndex(KEY_DESTINATIONADDRESS)));
                    trip.setSourcelatitute(cursor.getDouble(cursor
                            .getColumnIndex(KEY_SOURCELATITUDE)));
                    trip.setSourcelongitude(cursor.getDouble(cursor
                            .getColumnIndex(KEY_SOURCELOGITUDE)));
                    trip.setDestinationlatitude(cursor.getDouble(cursor
                            .getColumnIndex(KEY_DESTINATIONLATITUDE)));
                    trip.setDestinationlongitude(cursor.getDouble(cursor
                            .getColumnIndex(KEY_DESTINATIONLOGITUDE)));
                    trip.setTripstatus(cursor.getString(cursor
                            .getColumnIndex(KEY_TRIPTYPE)));
                    trip.setTriptime(cursor.getString(cursor
                            .getColumnIndex(KEY_TRAVELTIME)));
                    trip.setDriverimage(cursor.getString(cursor.getColumnIndex(KEY_DRIVER_IMAGE)));
                    trip.setTaxinumber(cursor.getString(cursor.getColumnIndex(KEY_DRIVER_TAXINUMBER)));
                    trip.setComment(cursor.getString(cursor.getColumnIndex(KEY_DRIVER_COMMENT)));
                    trip.setRatingvalue(cursor.getFloat(cursor.getColumnIndex(KEY_DRIVER_RATING)));


                    tripList.add(trip);
                } while (cursor.moveToNext());
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tripList;

    }

    public ArrayList<Integer> getTripIdFromTripHistory() {

        ArrayList<Integer> tripList = new ArrayList<Integer>();
        try {
            String query = "select * from " + TBL_TRIP_HISTORY + " ORDER BY " + ID + " DESC;";
            System.out.println("query : " + query);
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if (cursor == null) {
                return tripList;
            } else if (cursor.getCount() == 0) {
                return tripList;
            }
            if (cursor.moveToFirst()) {

                do {
                    /*Trip trip = new Trip();
                    trip.setId(cursor.getLong(cursor.getColumnIndex(ID)));
                    */

                    tripList.add(cursor.getInt(cursor.getColumnIndex(KEY_TRIP_ID)));
                } while (cursor.moveToNext());
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tripList;

    }

    public boolean deleteLocation(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TBL_LOCATION, ID + "=" + id, null) > 0;
    }

    public boolean deleteTrip(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TBL_TRIP, KEY_TRIP_ID + " = " + id, null) > 0;
    }

    public boolean deleteAllTrip() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TBL_TRIP, null, null) > 0;
    }

    public boolean deleteTripHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TBL_TRIP_HISTORY, null, null) > 0;
    }


}