package cn.edu.flexfit;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class RecordActivity extends AppCompatActivity implements RecordSCA.RefreshListener{
    private RecordSCA recordSCA;

    private ListView listView;
    private DBHelper dbHelper;

    private int userId;
    private Button btn_logout;
    private Button btn_back;
    private Button btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        Intent intent = getIntent();
        userId = intent.getIntExtra("uid", -1);

        listView = findViewById(R.id.listView);
        btn_back = findViewById(R.id.back);
        btn_add = findViewById(R.id.add);
        btn_logout = findViewById(R.id.btn_logout);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecordActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecordActivity.this, AddRecordActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("uid", userId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecordActivity.this, MenuActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("uid", userId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        String[] projection = {
                FitProviderContract.RECORD_ID,
                FitProviderContract.USER_ID,
                FitProviderContract.TYPE,
                FitProviderContract.DISTANCE,
                FitProviderContract.DURATION,
                FitProviderContract.SPEED,
        };
        String selection = FitProviderContract.USER_ID + "=?";
        String[] selectionArgs = {String.valueOf(userId)};

        Cursor cursor = getContentResolver().query(FitProviderContract.RECORD_TABLE_URI, projection, null, null, null);
        projection = new String[]{
                "type",
                "distance",
                "speed",
        };

        int[] uiMapping = new int[]{
                R.id.type,
                R.id.date,
                R.id.duration,
        };

        recordSCA = new RecordSCA(this, R.layout.list_item, cursor, projection, uiMapping, 0);
        listView.setAdapter(recordSCA);
        recordSCA.setRefreshListener(this);
    }

    @Override
    public void refreshRequested(){
        recreate();
    }
}
