package com.nexgen.attendance;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationServices;


public class selectclass extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener {
    protected static final String TAG = "selectclass";

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;
    protected double longitude;
    protected double latitude;
    protected double classLatitude=27.521552;
    protected double classLongitude=-97.076603;

    /**
     * Represents a geographical location.
     */




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectclass);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_COARSE_LOCATION },
                    PERMISSION_ACCESS_COARSE_LOCATION);
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All good!
                } else {
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle bundle) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            latitude = lastLocation.getLatitude();
            longitude=lastLocation.getLongitude();

            Toast.makeText(this, String.valueOf(latitude), Toast.LENGTH_SHORT).show();
            Toast.makeText(this,String.valueOf(longitude), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }



    /** calculates the distance between two locations in MILES */
    private double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 3958.75; // in miles, change to 6371 for kilometer output

        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double dist = earthRadius * c;

        return dist; // output distance, in MILES
    }

    public void submitAttendance(View view){
        RadioButton rb1,rb2,rb3,rb4;


        rb1 = (RadioButton) findViewById(R.id.radioButton4);
        rb2 = (RadioButton) findViewById(R.id.radioButton);
        rb3 = (RadioButton) findViewById(R.id.radioButton2);
        rb4 = (RadioButton) findViewById(R.id.radioButton3);


        if ((distance(latitude, longitude, classLatitude, classLongitude) <5)) {

            MailService m = new MailService("", "");

            String[] toArr = {"vicky261091@gmail.com"};
            m.setTo(toArr);
            m.setFrom("vicky261091@gmail.com");
            if(rb1.isChecked()&&rb2.isChecked()) {
                m.setSubject("Attendance-Course:Operating System");
            } if(rb1.isChecked()&&rb3.isChecked()) {
                m.setSubject("Attendance-Course:Compiler Design");
            } if(rb1.isChecked()&&rb4.isChecked()) {
                m.setSubject("Attendance-Course:Computer Networks");
            }
            m.setBody("Sai-K00409224");

            try {

                if(m.send()) {
                    Toast.makeText(this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Email was not sent.", Toast.LENGTH_LONG).show();
                }
            } catch(Exception e) {
                //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
                Log.e("MailApp", "Could not send email", e);
            }

            Intent nextpage=new Intent(this,Logout.class);
            nextpage.putExtra("message","YOUR ATTENDANCE HAS BEEN SUBMITTED AND THE INSTRUCTOR HAS BEEN NOTIFIED.");
            startActivity(nextpage);

        }else{
            Toast.makeText(this,"Hey Come to Class !!", Toast.LENGTH_SHORT).show();
        }

    }
    public void submitAbsence(View view){
        Intent nextpage=new Intent(this,AbsentScreen.class);
        startActivity(nextpage);

    }
}
