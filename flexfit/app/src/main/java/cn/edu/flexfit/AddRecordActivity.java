package cn.edu.flexfit;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;

public class AddRecordActivity extends AppCompatActivity implements LocationServiceListener{
    private int userId;

    private EditText type;

    private TextView distanceText;

    private TextView durationText;

    private LocationService locationService;

    private Button btn_back;

    private Button btn_start;

    private Button btn_add;

    private int totalTime = 0;
    private double totalDistance = 0.0;

    private double speed = 0.0;
    private int startTime = 1;
    private boolean haveStart = false;
    private static final int LOCATION_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle onSavedInstanceState){
        super.onCreate(onSavedInstanceState);
        setContentView(R.layout.activity_addrecord);

        Intent intent = getIntent();
        userId = intent.getIntExtra("uid", -1);

        distanceText = findViewById(R.id.detailed_distance);
        durationText = findViewById(R.id.duration_detail);
        type = findViewById(R.id.edittext_type);

        btn_back = findViewById(R.id.back);
        btn_add = findViewById(R.id.add);
        btn_start = findViewById(R.id.control);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddRecordActivity.this, RecordActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("uid", userId);
                intent.putExtras(bundle);
                locationService.stopLocation();
                startActivity(intent);
            }
        });

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickStart(view);
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (haveStart){
                    String typeText = type.getText().toString();
                    if (typeText.length() == 0){
                        showDialog("Warning", "Please fill in the Sport Type");
                        return;
                    }
                    addRecord(userId, typeText, cvtDistanceText(totalDistance), cvtTimetext(totalTime), cvtSpeed(totalDistance, totalTime));

                    jumpBack();
                }
            }
        });

        //        check the permission
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
        }

//        initialize the location service
        locationService = new LocationService(this, this);
    }

    public void onClickStart(View view){
        if (locationService.checkPermission() == false){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Warn");
            builder.setMessage("Your have not open your Location Permission!\nPlease Allow it while using the app");

            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        }

        if (!haveStart){
            locationService.start();
            locationService.continueLocation();
            btn_start.setText("Pause");
        }else if (startTime == -1){
            btn_start.setText("START");
            locationService.pauseLocation();
        }else if (startTime == 1){
            btn_start.setText("Pause");
            locationService.continueLocation();
        }
        haveStart = true;
        startTime *= -1;
    }

    //    update the UI of the distance text
    @Override
    public void onDistanceChanged(double distance) {
        runOnUiThread(() ->{
            distanceText.setText(cvtDistanceText(distance));
            totalDistance = distance;
        });
    }

    //    update the UI of the time text
    @Override
    public void onTimeChanged(int time){
        runOnUiThread(()->{
            durationText.setText(cvtTimetext(time));
            totalTime = time;
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        locationService.stopLocation();
    }

    //    convert distance in double to String text with unit
    public String cvtDistanceText(double distance){
        String approxDistance;

        //      when the moving distance is larger than 1km,
//      using Km as the unit to show
        if (distance >= 1000.0){
            DecimalFormat df = new DecimalFormat("#.##");
            distance = distance/1000;
            approxDistance = df.format(distance) + "km";
        }
//        when the distance is shorter than 1km
//        using m to show
        else {
            DecimalFormat df = new DecimalFormat("#");
            approxDistance = df.format(distance) + "m";
        }

        return approxDistance;
    }

    //    convert time in int to String with unit
    public String cvtTimetext(int time){
        int hour = time / 3600;
        int min = (time % 3600) / 60;
        int sec = (time % 3600) % 60;

        String text = "";
        if (time < 60){
            text = sec + "s";
        } else if (time < 3600) {
            text = min + "min" + sec + "s";
        }else {
            text = hour + "h" + min + "min" + sec + "s";
        }

        return text;
    }

    public String cvtSpeed(double distance, int time){
        double speed = distance / time;
        DecimalFormat df = new DecimalFormat("#.#");
        return df.format(speed) + "m/s";
    }

    private void showDialog(String title,String content){
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(content);

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void addRecord(int uid, String type, String distance, String duration, String speed){
        ContentValues values = new ContentValues();
        values.put(FitProviderContract.USER_ID, uid);
        values.put(FitProviderContract.TYPE, type);
        values.put(FitProviderContract.DISTANCE, distance);
        values.put(FitProviderContract.DURATION, duration);
        values.put(FitProviderContract.SPEED, speed);

        getContentResolver().insert(FitProviderContract.RECORD_TABLE_URI, values);
    }

    private void jumpBack(){
        Intent intent = new Intent(AddRecordActivity.this, RecordActivity.class);
        intent.putExtra("uid", userId);
        locationService.stopLocation();
        startActivity(intent);
    }
}
