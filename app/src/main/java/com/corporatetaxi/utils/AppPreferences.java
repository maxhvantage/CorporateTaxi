package com.corporatetaxi.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class AppPreferences {
    public static final String MBPREFERENCES = "UsercentralTaxi";
    public static final String CURRENTUSERCUSTOMERID = "id";
    public static final String TRIPID = "tripid";
    public static final String DRIVERID = "driverid";
    public static final String CUSTMERMOBILE = "mobile";
    public static final String UID = "user_id";
    public static final String DEVICEID = "deviceId";
    public static final String ACCOUNTTYPE = "acoount_type";
    public static final String USERNAME = "name";
    public static final String USERPROFILEPIC = "pro_pic";
    public static final String USERCITY = "user_name";
    public static final String USERPINCODE = "pin_code";
    public static final String USERPROFILEREVIEW = "pro_view";
    public static final String USERPRODUCTSOLD = "user_sold";
    public static final String USERPRODUCTSELL = "pro_sell";
    public static final String USERPRODUCTITEMS = "pro_items";
    public static final String USERADDRESS = "user_add";
    public static final String USERLATITUDE = "user_latitude";
    public static final String USERLONGITUDE = "user_longitude";
    public static final String DRIVERLATITUDE = "driver_latitude";
    public static final String DRIVERLONGITUDE = "driver_longitude";
    public static final String USERLASTNAME = "user_last_name";
    public static final String USERDOB = "user_dob";
    public static final String USERGENDER = "user_gender";
    public static final String USERFAVORITECATEGORY = "user_fav_category";
    public static final String LATITUDE = "lat";
    public static final String LONGITUDE = "lng";
    public static final String RADIUS = "radius";
    public static final String SEARCHTEXT = "search_text";
    public static final String SEARCHLATITUDE = "search_lat";
    public static final String SEARCHLONGITUDE = "search_long";
    public static final String FILTERCATEGORYID = "filter_category_id";
    public static final String FILTERDISTANCE = "filter_distance";
    public static final String USERAMOUNT = "user_amount";
    public static final String REFRESHAMOUNT = "refresh_amount";
    public static final String PRICETO = "price_to";
    public static final String SORTBY = "sort_by";
    public static final String COMPANYNAME = "company_name";
    public static final String REVIEWRECEIVED = "review_received";
    public static final String NOTIFICATIONFAVOURITES = "Notification_favourites";
    public static final String CATEGORY = "category";
    public static final String CATEGORYID = "category_id";
    public static final String NOTIFICATIONMISCELLANEOUS = "notification_miscellaneous";
    public static final String SEARCHADDRESS = "search_address";
    public static final String FILTERCATEGORYNAME = "filter_category_name";
    public static final String DRIVERNUMBER = "RandomNo";
    /* Verification email id*/
    public static final String VERIFYWEB = "verify_web";
    public static final String VERIFYMOBILE = "verify_mobile";
    public static final String VERIFYFB = "verify_fb";
    public static final String VERIFYGPLUS = "verify_gplus";
    public static final String MOBILENUMBERUSER = "mobilenumber";
    public static final String REQUESTTAXISCREEN = "request_taxi_screen";
    public static final String USERIMAGE = "user_image";

    public static final String SRCLAT = "srclat";
    public static final String SRCLNG = "srclng";
    public static final String SRCADD = "srcadd";
    public static final String NOTIFICATIONMESSAGE = "notification_message";

    public static String getSorclat(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(SRCLAT, "0");
    }

    public static void setSorclat(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(SRCLAT, value);
        editor.commit();
    }
    /////
    public static String getSrcLng(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(SRCLNG, "0");
    }

    public static void setSrcLng(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(SRCLNG, value);
        editor.commit();
    }
    /////
    public static String getSorcAdd(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(SRCADD, "0");
    }

    public static void setSorcAdd(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(SRCADD, value);
        editor.commit();
    }

    public static final String CURRENTLAT = "currentLat";
    public static final String CURRENTLONG = "currentLong";

    public static String getCurrentlat(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(CURRENTLAT, "0");
    }

    public static void setCurrentlat(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(CURRENTLAT, value);
        editor.commit();
    }
    /////
    public static String getCurrentlong(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(CURRENTLONG, "0");
    }

    public static void setCurrentlong(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(CURRENTLONG, value);
        editor.commit();
    }




    public static String getMobileuser(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(MOBILENUMBERUSER, "");
    }

    public static void setMobileuser(Context context, String Date) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(MOBILENUMBERUSER, Date);
        editor.commit();
    }

    public static boolean getVerifymobile(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getBoolean(VERIFYMOBILE, false);
    }

    public static void setVerifymobile(Context context, boolean Verifymobile) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putBoolean(VERIFYMOBILE, Verifymobile);
        editor.commit();
    }

    public static String getDriverId(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(DRIVERNUMBER, "");
    }

    public static void setDriverId(Context context, String DriverId) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(DRIVERNUMBER, DriverId);
        editor.commit();
    }

    public static boolean getVerifyweb(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getBoolean(VERIFYWEB, false);
    }

    public static void setVerifyweb(Context context, boolean Verifyweb) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putBoolean(VERIFYWEB, Verifyweb);
        editor.commit();
    }

    public static boolean getVerifyfb(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getBoolean(VERIFYFB, false);
    }

    public static void setVerifyfb(Context context, boolean Verifyfb) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putBoolean(VERIFYFB, Verifyfb);
        editor.commit();
    }

    public static boolean getVerifygplus(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getBoolean(VERIFYGPLUS, false);
    }

    public static void setVerifygplus(Context context, boolean Verifygplus) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putBoolean(VERIFYGPLUS, Verifygplus);
        editor.commit();
    }

    public static String getCategoryId(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(CATEGORYID, "");
    }

    public static void setCategoryId(Context context, String categoryid) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(CATEGORYID, categoryid);
        editor.commit();
    }

    public static String getCategory(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(CATEGORY, "");
    }


    public static void setCategory(Context context, String category) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(CATEGORY, category);
        editor.commit();
    }

    public static String getId(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(UID, "");
    }

    public static void setId(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(UID, id);
        editor.commit();
    }

    public static String getTripId(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(TRIPID, "");
    }

    public static void setTripId(Context context, String tripid) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(TRIPID, tripid);
        editor.commit();
    }

    public static String getUserprofilereview(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(USERPROFILEREVIEW, "");
    }

    public static void setUserprofilereview(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(USERPROFILEREVIEW, id);
        editor.commit();
    }

    public static String getUserproductsold(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(USERPRODUCTSOLD, "");
    }

    public static void setUserproductsold(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(USERPRODUCTSOLD, id);
        editor.commit();
    }

    public static String getUserproductsell(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(USERPRODUCTSELL, "");
    }

    public static void setUserproductsell(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(USERPRODUCTSELL, id);
        editor.commit();
    }

    public static String getUserproductitems(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(USERPRODUCTITEMS, "");
    }

    public static void setUserproductitems(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(USERPRODUCTITEMS, id);
        editor.commit();
    }


    public static String getUsername(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(USERNAME, "");
    }

    public static void setUsername(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(USERNAME, id);
        editor.commit();
    }

    public static String getUserImage(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(USERIMAGE, "");
    }

    public static void setUserImage(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(USERIMAGE, id);
        editor.commit();
    }

    public static String getCompanyname(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(COMPANYNAME, "");
    }

    public static void setCompanyname(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(COMPANYNAME, id);
        editor.commit();
    }

    public static String getUseraddress(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(USERADDRESS, "");
    }

    public static void setUseraddress(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(USERADDRESS, id);
        editor.commit();
    }


    public static String getUserprofilepic(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(USERPROFILEPIC, "");
    }

    public static void setUserprofilepic(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(USERPROFILEPIC, id);
        editor.commit();
    }

    public static String getUsercity(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(USERCITY, "");
    }

    public static void setUsercity(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(USERCITY, id);
        editor.commit();
    }

    public static String getUserpincode(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(USERPINCODE, "");
    }

    public static void setUserpincode(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(USERPINCODE, id);
        editor.commit();
    }


    //////////////y2k//////////////////////////

    public static void setMobile(Context context,
                                 int id) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putInt(CUSTMERMOBILE, id);
        editor.commit();
    }

    public static int getMobile(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getInt(CUSTMERMOBILE, 0);
    }

    //////////////y2k//////////////////////////

    public static void setLatitude(Context context, String lat) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(LATITUDE, lat);
        editor.commit();
    }

    public static void setLongitude(Context context, String lng) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(LONGITUDE, lng);
        editor.commit();
    }


    public static String getLatutude(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(LATITUDE, "0.0");
    }

    public static String getLongitude(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(LONGITUDE, "0.0");
    }

    public static void setUserLatitude(Context context, String lat) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(USERLATITUDE, lat);
        editor.commit();
    }

    public static void setUserLongitude(Context context, String lng) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(USERLONGITUDE, lng);
        editor.commit();
    }


    public static String getUserLatitude(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(USERLATITUDE, "0.0");
    }

    public static String getUserLongitude(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(USERLONGITUDE, "0.0");
    }

    public static void setRadiusCriteria(Context context, String radius) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(RADIUS, radius);
        editor.commit();
    }

    public static String getRadiusCriteria(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(RADIUS, "3 Miles (~5 km)");
    }

    public static String getSearchtext(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(SEARCHTEXT, "");
    }

    public static void setSearchtext(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(SEARCHTEXT, id);
        editor.commit();
    }


    public static String getSearchlatitude(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(SEARCHLATITUDE, "");
    }

    public static void setSearchlatitude(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(SEARCHLATITUDE, id);
        editor.commit();
    }

    public static String getSearchlongitude(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(SEARCHLONGITUDE, "");
    }

    public static void setSearchlongitude(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(SEARCHLONGITUDE, id);
        editor.commit();
    }

    public static String getFiltercategoryid(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(FILTERCATEGORYID, "");
    }

    public static void setFiltercategoryid(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(FILTERCATEGORYID, id);
        editor.commit();
    }

    public static String getFilterdistance(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(FILTERDISTANCE, "5kms");
    }

    public static void setFilterdistance(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(FILTERDISTANCE, id);
        editor.commit();
    }

    public static String getUseramount(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(USERAMOUNT, "");
    }

    public static void setUseramount(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(USERAMOUNT, id);
        editor.commit();
    }

    public static void setCustomerId(Context context,
                                     int id) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putInt(CURRENTUSERCUSTOMERID, id);
        editor.commit();
    }

    public static int getCustomerId(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getInt(CURRENTUSERCUSTOMERID, 0);
    }

    public static int getRefreshamount(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getInt(REFRESHAMOUNT, 0);
    }

    public static void setRefreshamount(Context context, int id) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putInt(REFRESHAMOUNT, id);
        editor.commit();
    }

    public static String getPriceto(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(PRICETO, "");
    }

    public static void setPriceto(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(PRICETO, id);
        editor.commit();
    }

    public static String getSortby(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(SORTBY, "");
    }

    public static void setSortby(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(SORTBY, id);
        editor.commit();
    }

    public static String getUserlastname(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(USERLASTNAME, "");
    }

    public static void setUserlastname(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(USERLASTNAME, id);
        editor.commit();
    }

    public static String getUserdob(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(USERDOB, "");
    }

    public static void setUserdob(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(USERDOB, id);
        editor.commit();
    }

    public static String getUsergender(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(USERGENDER, "");
    }

    public static void setUsergender(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(USERGENDER, id);
        editor.commit();
    }

    public static String getUserfavoritecategory(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(USERFAVORITECATEGORY, "");
    }

    public static void setUserfavoritecategory(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(USERFAVORITECATEGORY, id);
        editor.commit();
    }


    public static String getDeviceid(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(DEVICEID, "");
    }

    public static void setDeviceid(Context context, String deviceId) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(DEVICEID, deviceId);
        editor.commit();
    }


    public static boolean getReviewReceived(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getBoolean(REVIEWRECEIVED, true);
    }

    public static void setReviewReceived(Context context, boolean reviewFlag) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putBoolean(REVIEWRECEIVED, reviewFlag);
        editor.commit();
    }

    public static boolean getNotificationFavourites(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getBoolean(NOTIFICATIONFAVOURITES, true);
    }

    public static void setNotificationFavourites(Context context, boolean reviewFlag) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putBoolean(NOTIFICATIONFAVOURITES, reviewFlag);
        editor.commit();
    }

    public static boolean getNotificationMiscellaneous(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getBoolean(NOTIFICATIONMISCELLANEOUS, true);
    }

    public static void setNotificationMiscellaneous(Context context, boolean misc) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putBoolean(NOTIFICATIONMISCELLANEOUS, misc);
        editor.commit();
    }

    public static String getSearchAddress(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(SEARCHADDRESS, "");
    }

    public static void setSearchAddress(Context context, String SearchAddress) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(SEARCHADDRESS, SearchAddress);
        editor.commit();
    }

    public static String getAccounttype(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(ACCOUNTTYPE, "");
    }

    public static void setAccounttype(Context context, String NewInYourArea) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(ACCOUNTTYPE, NewInYourArea);
        editor.commit();
    }

    public static String getFiltercategoryName(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(FILTERCATEGORYNAME, "");
    }

    public static void setFiltercategoryName(Context context, String name) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(FILTERCATEGORYNAME, name);
        editor.commit();
    }

    public static void setDriverLat(Context context, ArrayList<String> latLngs) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(DRIVERLATITUDE, String.valueOf(latLngs));
        editor.commit();
    }

    public static String getDriverLat(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(DRIVERLATITUDE, "");
    }

    public static void setDriverLong(Context context, ArrayList<String> latLngs) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(DRIVERLONGITUDE, String.valueOf(latLngs));
        editor.commit();
    }

    public static String getDriverLong(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(DRIVERLONGITUDE, "");
    }

    public static boolean getApprequestTaxiScreen(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getBoolean(REQUESTTAXISCREEN, false);
    }

    public static void setApprequestTaxiScreen(Context context, boolean flag) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putBoolean(REQUESTTAXISCREEN, flag);
        editor.commit();
    }

    public static String getAcceptdriverId(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(DRIVERID, "");
    }

    public static void setAcceptdriverId(Context context, String driverid) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(DRIVERID, driverid);
        editor.commit();
    }

    public static void setNotificationMessage(Context context, String notificationMessage) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(NOTIFICATIONMESSAGE, String.valueOf(notificationMessage));
        editor.commit();
    }

    public static String getNotificationMessage(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(NOTIFICATIONMESSAGE, "");
    }
}