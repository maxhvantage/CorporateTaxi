package com.corporatetaxi.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Evon on 12/14/2015.
 */
public class TriphistoryModel {

    @DatabaseField(columnName="_ID",id=true, generatedId=false)
    private int id;
    @DatabaseField(columnName="triphistory_ID")
    private int triphistoryId;
    @DatabaseField(columnName="trip_DiverName")
    private String tripdriverName;
    @DatabaseField(columnName="trip_Date")
    private String tripDate;
    @DatabaseField(columnName="trip_Time")
    private String tripTime;
    @DatabaseField(columnName="trip_DiverImage")
    private String tripDiverImage;
    @DatabaseField(columnName="trip_SourceAddress")
    private String tripSourceAddress;
    @DatabaseField(columnName="trip_DestinationAddress")
    private String tripDestinationAddress;
    @DatabaseField(columnName="trip_SourceLatitude")
    private double tripSourceLatitude;
    @DatabaseField(columnName="trip_SourceLongitude")
    private double tripSourceLongitude;
    @DatabaseField(columnName="trip_DestinationLatitude")
    private double tripDestinationLatitude;
    @DatabaseField(columnName="trip_DestinationLongitude")
    private double tripDestinationLongitude;
    @DatabaseField(columnName="trip_Taxicompanyname")
    private String tripTaxicompanyname;
    @DatabaseField(columnName="trip_Taxinumber")
    private String tripTaxinumber;
    @DatabaseField(columnName="trip_Drivermobilenumber")
    private String tripDrivermobilenumber;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTriphistoryId() {
        return triphistoryId;
    }

    public void setTriphistoryId(int triphistoryId) {
        this.triphistoryId = triphistoryId;
    }

    public String getTripdriverName() {
        return tripdriverName;
    }

    public void setTripdriverName(String tripdriverName) {
        this.tripdriverName = tripdriverName;
    }

    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }

    public String getTripTime() {
        return tripTime;
    }

    public void setTripTime(String tripTime) {
        this.tripTime = tripTime;
    }

    public String getTripDiverImage() {
        return tripDiverImage;
    }

    public void setTripDiverImage(String tripDiverImage) {
        this.tripDiverImage = tripDiverImage;
    }

    public String getTripSourceAddress() {
        return tripSourceAddress;
    }

    public void setTripSourceAddress(String tripSourceAddress) {
        this.tripSourceAddress = tripSourceAddress;
    }

    public String getTripDestinationAddress() {
        return tripDestinationAddress;
    }

    public void setTripDestinationAddress(String tripDestinationAddress) {
        this.tripDestinationAddress = tripDestinationAddress;
    }

    public double getTripSourceLatitude() {
        return tripSourceLatitude;
    }

    public void setTripSourceLatitude(double tripSourceLatitude) {
        this.tripSourceLatitude = tripSourceLatitude;
    }

    public double getTripSourceLongitude() {
        return tripSourceLongitude;
    }

    public void setTripSourceLongitude(double tripSourceLongitude) {
        this.tripSourceLongitude = tripSourceLongitude;
    }

    public double getTripDestinationLatitude() {
        return tripDestinationLatitude;
    }

    public void setTripDestinationLatitude(double tripDestinationLatitude) {
        this.tripDestinationLatitude = tripDestinationLatitude;
    }

    public double getTripDestinationLongitude() {
        return tripDestinationLongitude;
    }

    public void setTripDestinationLongitude(double tripDestinationLongitude) {
        this.tripDestinationLongitude = tripDestinationLongitude;
    }

    public String getTripTaxicompanyname() {
        return tripTaxicompanyname;
    }

    public void setTripTaxicompanyname(String tripTaxicompanyname) {
        this.tripTaxicompanyname = tripTaxicompanyname;
    }

    public String getTripTaxinumber() {
        return tripTaxinumber;
    }

    public void setTripTaxinumber(String tripTaxinumber) {
        this.tripTaxinumber = tripTaxinumber;
    }

    public String getTripDrivermobilenumber() {
        return tripDrivermobilenumber;
    }

    public void setTripDrivermobilenumber(String tripDrivermobilenumber) {
        this.tripDrivermobilenumber = tripDrivermobilenumber;
    }
}
