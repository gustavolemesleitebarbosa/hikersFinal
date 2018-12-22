package com.example.gustavo.hikersfinal;

import android.Manifest;
import android.app.Application;
import android.app.ApplicationErrorReport;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
                if(location!=null)
                updateInfo(location);
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    public void updateInfo(Location location) {

        String info = null;
        List<Address> addresses = null;
        info = Double.toString(location.getLatitude());
        TextView latitude = (TextView) findViewById(R.id.latitude);
        latitude.setText("Latitude: " + info);
        info = Double.toString(location.getLongitude());
        TextView longitude = (TextView) findViewById(R.id.longitude);
        longitude.setText("Longitude: " + info);
        info = Double.toString(location.getAltitude());
        TextView altitude = (TextView) findViewById(R.id.altitude);
        altitude.setText("Altitude " + info);
        info = Double.toString(location.getAccuracy());
        TextView accuracy = (TextView) findViewById(R.id.Accuracy);
        accuracy.setText("Accuracy " + info);

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try

        {

            String userAdress = "";
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (addresses != null && addresses.size() > 0) {

                if (addresses.get(0).getSubThoroughfare() != null) {
                    userAdress += addresses.get(0).getSubThoroughfare() + " ";
                }
                if (addresses.get(0).getThoroughfare() != null) {
                    userAdress += addresses.get(0).getThoroughfare() + " ";
                }
                if (addresses.get(0).getLocality() != null) {
                    userAdress += addresses.get(0).getLocality() + " ,";
                }
                if (addresses.get(0).getPostalCode() != null) {
                    userAdress += addresses.get(0).getPostalCode() + " ,";
                }
                if (addresses.get(0).getCountryName() != null) {
                    userAdress += addresses.get(0).getCountryName();
                }
                TextView address = (TextView) findViewById(R.id.Adress);
                address.setText(userAdress);
            }
        } catch (
                IOException e)

        {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {


            public void onLocationChanged(Location location) {
                updateInfo(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
                if(location!=null)
                updateInfo(location);

            }

        }
    }
}