package com.harshs.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                updateLocationInfo(location);

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

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

        }else{

          locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastKnownLocation != null){

                updateLocationInfo(lastKnownLocation);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

            }

        }
    }

    public void updateLocationInfo(Location location){

        TextView latView =(TextView) findViewById(R.id.latView);
        TextView longView =(TextView) findViewById(R.id.longView);
        TextView accuracyView =(TextView) findViewById(R.id.accuracyView);
        TextView altitudeView =(TextView) findViewById(R.id.altitudeView);
        TextView addressView =(TextView) findViewById(R.id.addressView);

        latView.setText("Latitude: "+Double.toString(location.getLatitude()));
        longView.setText("Longitude: "+Double.toString(location.getLongitude()));
        accuracyView.setText("Accuracy: "+Double.toString(location.getAccuracy()));
        altitudeView.setText("Altitude: "+Double.toString(location.getAltitude()));

        String address="Could not find Address :(";
        Geocoder geocoder =  new Geocoder(getApplicationContext(), Locale.getDefault());
        try{

          List<Address> addressList=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

           if(addressList != null && addressList.size() > 0){

               address="Address\n";

               if(addressList.get(0).getThoroughfare() != null){

                   address += (addressList.get(0).getThoroughfare()+"\n");

               }
               if(addressList.get(0).getAdminArea() != null){

                   address += (addressList.get(0).getAdminArea()+"\n");

               }
               if(addressList.get(0).getLocality() != null){

                   address += (addressList.get(0).getLocality()+" ");

               }
               if(addressList.get(0).getPostalCode() != null){

                   address += (addressList.get(0).getPostalCode());

               }

           }

        }catch (Exception e){

            e.printStackTrace();
        }

        addressView.setText(address);

    }

}
