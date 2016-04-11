package com.corporatetaxi.utils;

/**
 * AppConstants to store the constant variables and common tags to be used
 * within the application
 */
public class AppConstants {

    public static final int NETWORK_TIMEOUT_CONSTANT = 15000;
    public static final int NETWORK_CONNECTION_TIMEOUT_CONSTANT = 15000;
    public static final byte ROUTE_ACTIVITY_CONSTANT = (byte) 23;
    public static final int NETWORK_SOCKET_TIMEOUT_CONSTANT = 25000;


    public static final String APPURL = "http://www.hvantagetechnologies.com/central-taxi/customer_app/";
    // public static final String APPURL = "http://priyanshpatodi.com/testsite/texiDriverApp/api/customerApi/customer_api.php?";

    public static final String TAG = "centralcorporatetaxi";

    public static final String URL_LOGIN = "";

    public static final boolean IS_DEV_BUILD = true;

    public static final String REGISTRATION = APPURL + "method=registration";
    public static final String LOGIN = APPURL + "general_api.php?method=SignIn";
    public static final String REQUESTTAXI = APPURL + "trip.php?method=startTrip";
    public static final String CANCELTAXI = APPURL + "trip.php?method=cancel";
    public static final String CHANGEPAASWORD = APPURL + "general_api.php?method=changePassword";
    public static final String SENDMESSAGE = APPURL + "trip.php?method=sendMessage";
    public static final String ALLREADYBOARDED = APPURL+ "trip.php?method=boardedTrip";
    public static final String LOGOUTAPPURL = APPURL + "general_api.php?method=logout";
    public static final String UPDATEPROFILE = APPURL + "general_api.php?method=updateProfile";
    public static final String TRIPHISTORY = APPURL + "trip.php?method=tripHistory";
    public static final String FEEDBACKTRIP = APPURL + "trip.php?method=feedback";
    public static final String PANIC = APPURL + "trip.php?method=panic";
    public static final String FIELTER = APPURL + "trip.php?method=tripHistory";
    public static final String DRIVERINFO = APPURL + "trip.php?method=getDriver";
    public static final String PRIVIOUSSOURCEANDDESTINATION = APPURL + "trip.php?method=startTrip1";
    public static final String PRIVIOUSAMOUNT = APPURL + "trip.php?method=PayPayment";
    public static final String COLONIESLIST = APPURL + "trip.php?method=getColonies";
    public static final String DRIVERCURRENTINFO = APPURL + "trip.php?method=getCurrentDriverInfo";
    public static final String PAYMENTCHECK = APPURL + "trip.php?method=paymentMode";

}
