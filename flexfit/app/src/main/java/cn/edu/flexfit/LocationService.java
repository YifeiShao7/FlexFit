package cn.edu.flexfit;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.Timer;

public class LocationService extends Thread {
    private Context mContext;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private LocationServiceListener serviceListener;
    private boolean isRunning;
    private boolean isPausing = false;
    private double totalDistance = 0.0;
    private int totalTime = 0;

    private double lastLatitude = 0;
    private double lastLongitude = 0;

    public LocationService(Context context, LocationServiceListener listener) {
        mContext = context;
        serviceListener = listener;
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener(){
            @Override
            public void onLocationChanged(Location location){
                calculateDistance(location);
                Log.e("Location", "changed!");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras){
                Log.d("cw2", "onStatusChanged:" + provider + " " + status);
            }

            @Override
            public void onProviderEnabled(String provider){
                Log.d("cw2", "onProviderEnabled: " + provider);
            }

            @Override
            public void onProviderDisabled(String provider){
                Log.d("cw2", "onProviderDisabled: " + provider);
            }
        };
    }

    @Override
    public void run() {
        isRunning = true;

        while (isRunning){
            try {
                if (!isPausing){
                    if (mContext.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 5, mLocationListener,Looper.getMainLooper());
                    }
                    else {
//                        Log.d("error", "no permission");
                        break;
                    }
                    totalTime ++;
                }
                Thread.sleep(1000);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        if (mContext.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationManager.removeUpdates(mLocationListener);
        }
    }
    @Override
    public void start(){
        super.start();
        Log.d("start", "start");
    }
    public void stopLocation(){
        isRunning = false;
    }

    public void pauseLocation(){
        isPausing = true;
    }

    public void continueLocation(){
        lastLatitude = 0;
        lastLongitude = 0;
        isPausing = false;
    }

    private void calculateDistance(Location location) {
        if (!isPausing){
            if (lastLatitude == 0 && lastLongitude == 0) {
                lastLatitude = location.getLatitude();
                lastLongitude = location.getLongitude();
            }else{
                double crtLatitude = location.getLatitude();
                double crtLongitude = location.getLongitude();

                double crtDistance = distance(lastLatitude, lastLongitude, crtLatitude, crtLongitude);

                totalDistance += crtDistance;

                lastLatitude = crtLatitude;
                lastLongitude = crtLongitude;
            }
            System.out.println(totalTime);
            serviceListener.onTimeChanged(totalTime);
            serviceListener.onDistanceChanged(totalDistance);
        }
    }

    private double distance(double lat1, double lon1, double lat2, double lon2){
        float[] results = new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, results);
        return results[0];
    }

    public boolean checkPermission(){
        boolean flag = true;
        if (mContext.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            flag = false;
        }

        return flag;
    }
}

