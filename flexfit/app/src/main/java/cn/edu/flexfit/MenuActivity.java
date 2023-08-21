package cn.edu.flexfit;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {
    private int userId;

    public MusicService musicService;
    public boolean isBound = false;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicService = binder.getService();
            isBound = true;
            musicService.startMusic();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
        }
    };
    private TextView btn_maps;

    private TextView btn_mainpage;

    private Button btn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent intent = getIntent();
        userId = intent.getIntExtra("uid", -1);
        btn_maps = findViewById(R.id.btn_map);
        btn_logout = findViewById(R.id.btn_logout);
        btn_mainpage = findViewById(R.id.btn_mainpage);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicService.stopMusic();
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btn_mainpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, MainPageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("uid", userId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btn_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, RecordActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("uid", userId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        Intent intentMusic = new Intent(this, MusicService.class);
        startService(intentMusic);
        bindService(intentMusic, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (isBound){
            unbindService(serviceConnection);
        }
    }
}
