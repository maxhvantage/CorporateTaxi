package com.corporatetaxi.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eyon on 11/7/2015.
 */
public class Allbeans implements Parcelable {

    String name;
    String email;
    String phone;
    String password;
    String streetname;
    String streetnumber;
    String streetdescription;
    String companyname;
    String canceltaxirequest;
    String newpassword;
    String sendmessage;
    String editname;
    String editphone;
    String editemail;
    String sourcerequest;
    String destinationresuest;
    String fromdate;
    String todate;
    String tripdate;
    String triptime;
    String tripmonth;
    String tripnumber;
    String tripid;
    String comment;
    float ratingvalue;
    String panictaxirequest;
    String image;
    String drivername;
    String drivermobilenumber;
    String distance;
    String driverimage;
    String driverid;
    double driverlatitute;
    double driverlongitude;
    int triphistoryid;
    double sourcelatitute;
    double sourcelongitude;
    double destinationlatitude;
    double destinationlongitude;
    String tripprice;
    String tripstatus;
    String notificationmessage;
    String tripname;
    String taxinumber;
    String sourcelandmark;
    String destinationlandmark;
    String tripamount;
    String privioussourceaddress;
    String priviousdestinationaddress;
    double privioussourcelatitude;
    double privioussourcelongitude;
    double priviousdestinationlatitude;
    double priviousdestinationlongitude;
    int refreshamount;
    String header;
    String description;
    int destinationamount;


    public Allbeans() {
    }

    protected Allbeans(Parcel in) {
        name = in.readString();
        email = in.readString();
        phone = in.readString();
        password = in.readString();
        streetname = in.readString();
        streetnumber = in.readString();
        streetdescription = in.readString();
        companyname = in.readString();
        canceltaxirequest = in.readString();
        newpassword = in.readString();
        sendmessage = in.readString();
        editname = in.readString();
        editphone = in.readString();
        editemail = in.readString();
        sourcerequest = in.readString();
        destinationresuest = in.readString();
        fromdate = in.readString();
        todate = in.readString();
        tripdate = in.readString();
        triptime = in.readString();
        tripmonth = in.readString();
        tripnumber = in.readString();
        tripid = in.readString();
        comment = in.readString();
        ratingvalue = in.readFloat();
        panictaxirequest = in.readString();
        image = in.readString();
        drivername = in.readString();
        drivermobilenumber = in.readString();
        distance = in.readString();
        driverimage = in.readString();
        triphistoryid = in.readInt();
        sourcelatitute = in.readDouble();
        sourcelongitude = in.readDouble();
        destinationlatitude = in.readDouble();
        destinationlongitude = in.readDouble();
        tripprice = in.readString();
        tripstatus = in.readString();
        notificationmessage = in.readString();
        driverid = in.readString();
        driverlatitute = in.readDouble();
        driverlongitude = in.readDouble();
        taxinumber = in.readString();
        tripname = in.readString();
        sourcelandmark = in.readString();
        destinationlandmark = in.readString();
        privioussourceaddress = in.readString();
        tripamount = in.readString();
        priviousdestinationaddress = in.readString();
        privioussourcelatitude = in.readDouble();
        privioussourcelongitude = in.readDouble();
        priviousdestinationlatitude = in.readDouble();
        priviousdestinationlongitude = in.readDouble();
        refreshamount = in.readInt();
        header = in.readString();
        description = in.readString();
        destinationamount = in.readInt();


    }


    public static final Creator<Allbeans> CREATOR = new Creator<Allbeans>() {
        @Override
        public Allbeans createFromParcel(Parcel in) {
            return new Allbeans(in);
        }

        @Override
        public Allbeans[] newArray(int size) {
            return new Allbeans[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStreetname() {
        return streetname;
    }

    public void setStreetname(String streetname) {
        this.streetname = streetname;
    }

    public String getStreetnumber() {
        return streetnumber;
    }

    public void setStreetnumber(String streetnumber) {
        this.streetnumber = streetnumber;
    }

    public String getStreetdescription() {
        return streetdescription;
    }

    public void setStreetdescription(String streetdescription) {
        this.streetdescription = streetdescription;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getCanceltaxirequest() {
        return canceltaxirequest;
    }

    public void setCanceltaxirequest(String canceltaxirequest) {
        this.canceltaxirequest = canceltaxirequest;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    public String getSendmessage() {
        return sendmessage;
    }

    public void setSendmessage(String sendmessage) {
        this.sendmessage = sendmessage;
    }

    public String getEditname() {
        return editname;
    }

    public void setEditname(String editname) {
        this.editname = editname;
    }

    public String getEditphone() {
        return editphone;
    }

    public void setEditphone(String editphone) {
        this.editphone = editphone;
    }

    public String getEditemail() {
        return editemail;
    }

    public void setEditemail(String editemail) {
        this.editemail = editemail;
    }

    public String getSourcerequest() {
        return sourcerequest;
    }

    public void setSourcerequest(String sourcerequest) {
        this.sourcerequest = sourcerequest;
    }

    public String getDestinationresuest() {
        return destinationresuest;
    }

    public void setDestinationresuest(String destinationresuest) {
        this.destinationresuest = destinationresuest;
    }

    public String getFromdate() {
        return fromdate;
    }

    public void setFromdate(String fromdate) {
        this.fromdate = fromdate;
    }

    public String getTodate() {
        return todate;
    }

    public void setTodate(String todate) {
        this.todate = todate;
    }

    public String getTripdate() {
        return tripdate;
    }

    public void setTripdate(String tripdate) {
        this.tripdate = tripdate;
    }

    public String getTriptime() {
        return triptime;
    }

    public void setTriptime(String triptime) {
        this.triptime = triptime;
    }

    public String getTripmonth() {
        return tripmonth;
    }

    public void setTripmonth(String tripmonth) {
        this.tripmonth = tripmonth;
    }

    public String getTripnumber() {
        return tripnumber;
    }

    public void setTripnumber(String tripnumber) {
        this.tripnumber = tripnumber;
    }

    public String getTripid() {
        return tripid;
    }

    public void setTripid(String tripid) {
        this.tripid = tripid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getRatingvalue() {
        return ratingvalue;
    }

    public void setRatingvalue(float ratingvalue) {
        this.ratingvalue = ratingvalue;
    }

    public String getPanictaxirequest() {
        return panictaxirequest;
    }

    public void setPanictaxirequest(String panictaxirequest) {
        this.panictaxirequest = panictaxirequest;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDrivername() {
        return drivername;
    }

    public void setDrivername(String drivername) {
        this.drivername = drivername;
    }

    public String getDrivermobilenumber() {
        return drivermobilenumber;
    }

    public void setDrivermobilenumber(String drivermobilenumber) {
        this.drivermobilenumber = drivermobilenumber;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDriverimage() {
        return driverimage;
    }

    public void setDriverimage(String driverimage) {
        this.driverimage = driverimage;
    }

    public int getTriphistoryid() {
        return triphistoryid;
    }

    public double getSourcelatitute() {
        return sourcelatitute;
    }

    public void setSourcelatitute(double sourcelatitute) {
        this.sourcelatitute = sourcelatitute;
    }

    public double getSourcelongitude() {
        return sourcelongitude;
    }

    public void setSourcelongitude(double sourcelongitude) {
        this.sourcelongitude = sourcelongitude;
    }

    public double getDestinationlatitude() {
        return destinationlatitude;
    }

    public void setDestinationlatitude(double destinationlatitude) {
        this.destinationlatitude = destinationlatitude;
    }

    public double getDestinationlongitude() {
        return destinationlongitude;
    }

    public void setDestinationlongitude(double destinationlongitude) {
        this.destinationlongitude = destinationlongitude;
    }

    public void setTriphistoryid(int triphistoryid) {
        this.triphistoryid = triphistoryid;
    }

    public String getTripprice() {
        return tripprice;
    }

    public void setTripprice(String tripprice) {
        this.tripprice = tripprice;
    }

    public String getTripstatus() {
        return tripstatus;
    }

    public void setTripstatus(String tripstatus) {
        this.tripstatus = tripstatus;
    }

    public String getNotificationmessage() {
        return notificationmessage;
    }

    public void setNotificationmessage(String notificationmessage) {
        this.notificationmessage = notificationmessage;
    }

    public String getDriverid() {
        return driverid;
    }

    public void setDriverid(String driverid) {
        this.driverid = driverid;
    }

    public double getDriverlatitute() {
        return driverlatitute;
    }

    public void setDriverlatitute(double driverlatitute) {
        this.driverlatitute = driverlatitute;
    }

    public double getDriverlongitude() {
        return driverlongitude;
    }

    public void setDriverlongitude(double driverlongitude) {
        this.driverlongitude = driverlongitude;
    }

    public String getTaxinumber() {
        return taxinumber;
    }

    public void setTaxinumber(String taxinumber) {
        this.taxinumber = taxinumber;
    }

    public String getTripname() {
        return tripname;
    }

    public void setTripname(String tripname) {
        this.tripname = tripname;
    }

    public String getSourcelandmark() {
        return sourcelandmark;
    }

    public void setSourcelandmark(String sourcelandmark) {
        this.sourcelandmark = sourcelandmark;
    }

    public String getDestinationlandmark() {
        return destinationlandmark;
    }

    public void setDestinationlandmark(String destinationlandmark) {
        this.destinationlandmark = destinationlandmark;
    }

    public String getPrivioussourceaddress() {
        return privioussourceaddress;
    }

    public void setPrivioussourceaddress(String privioussourceaddress) {
        this.privioussourceaddress = privioussourceaddress;
    }

    public String getTripamount() {
        return tripamount;
    }

    public void setTripamount(String tripamount) {
        this.tripamount = tripamount;
    }

    public String getPriviousdestinationaddress() {
        return priviousdestinationaddress;
    }

    public void setPriviousdestinationaddress(String priviousdestinationaddress) {
        this.priviousdestinationaddress = priviousdestinationaddress;
    }

    public double getPrivioussourcelatitude() {
        return privioussourcelatitude;
    }

    public void setPrivioussourcelatitude(double privioussourcelatitude) {
        this.privioussourcelatitude = privioussourcelatitude;
    }

    public double getPrivioussourcelongitude() {
        return privioussourcelongitude;
    }

    public void setPrivioussourcelongitude(double privioussourcelongitude) {
        this.privioussourcelongitude = privioussourcelongitude;
    }

    public double getPriviousdestinationlatitude() {
        return priviousdestinationlatitude;
    }

    public void setPriviousdestinationlatitude(double priviousdestinationlatitude) {
        this.priviousdestinationlatitude = priviousdestinationlatitude;
    }

    public double getPriviousdestinationlongitude() {
        return priviousdestinationlongitude;
    }

    public void setPriviousdestinationlongitude(double priviousdestinationlongitude) {
        this.priviousdestinationlongitude = priviousdestinationlongitude;
    }

    public int getRefreshamount() {
        return refreshamount;
    }

    public void setRefreshamount(int refreshamount) {
        this.refreshamount = refreshamount;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDestinationamount() {
        return destinationamount;
    }

    public void setDestinationamount(int destinationamount) {
        this.destinationamount = destinationamount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(password);
        dest.writeString(streetname);
        dest.writeString(streetnumber);
        dest.writeString(streetdescription);
        dest.writeString(companyname);
        dest.writeString(canceltaxirequest);
        dest.writeString(newpassword);
        dest.writeString(sendmessage);
        dest.writeString(editname);
        dest.writeString(editphone);
        dest.writeString(editemail);
        dest.writeString(sourcerequest);
        dest.writeString(destinationresuest);
        dest.writeString(fromdate);
        dest.writeString(todate);
        dest.writeString(tripdate);
        dest.writeString(triptime);
        dest.writeString(tripmonth);
        dest.writeString(tripnumber);
        dest.writeString(tripid);
        dest.writeString(comment);
        dest.writeFloat(ratingvalue);
        dest.writeString(panictaxirequest);
        dest.writeString(image);
        dest.writeString(drivername);
        dest.writeString(drivermobilenumber);
        dest.writeString(distance);
        dest.writeString(driverimage);
        dest.writeInt(triphistoryid);
        dest.writeDouble(sourcelatitute);
        dest.writeDouble(sourcelongitude);
        dest.writeDouble(destinationlatitude);
        dest.writeDouble(destinationlongitude);
        dest.writeString(tripprice);
        dest.writeString(tripstatus);
        dest.writeString(notificationmessage);
        dest.writeString(driverid);
        dest.writeDouble(driverlatitute);
        dest.writeDouble(driverlongitude);
        dest.writeString(taxinumber);
        dest.writeString(tripname);
        dest.writeString(sourcelandmark);
        dest.writeString(destinationlandmark);
        dest.writeString(privioussourceaddress);
        dest.writeString(tripamount);
        dest.writeString(priviousdestinationaddress);
        dest.writeDouble(privioussourcelatitude);
        dest.writeDouble(privioussourcelongitude);
        dest.writeDouble(priviousdestinationlatitude);
        dest.writeDouble(priviousdestinationlongitude);
        dest.writeInt(refreshamount);
        dest.writeString(header);
        dest.writeString(description);
        dest.writeInt(destinationamount);


    }
}
