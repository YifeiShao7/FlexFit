package cn.edu.flexfit;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainPageActivity extends AppCompatActivity implements CustomCursorAdapter.OnRefreshListener{
    private CustomCursorAdapter customCursorAdapter;
    private ListView listView;
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    private Button btn_add;

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

        listView = findViewById(R.id.listView);
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        Intent intent = getIntent();
        userId = intent.getIntExtra("uid", -1);
        btn_add = findViewById(R.id.add);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPageActivity.this, AddActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("uid", userId);
                bundle.putInt("where", 1);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        String[] projection = {
                FitProviderContract.DIARY_ID,
                FitProviderContract.USER_ID,
                FitProviderContract.TYPE,
                FitProviderContract.DATE,
                FitProviderContract.DURATION,
        };
        String selection = FitProviderContract.USER_ID + "=?";
        String[] selectionArgs = {String.valueOf(userId)};

//        Cursor cursor = getContentResolver().query(FitProviderContract.DIARY_TABLE_URI, projection, selection, selectionArgs, null);
        Cursor cursor = getContentResolver().query(FitProviderContract.DIARY_TABLE_URI, projection, null, null, null);
        projection = new String[]{
                "type",
                "date",
                "duration",
        };

        int[] uiMapping = new int[]{
                R.id.type,
                R.id.date,
                R.id.duration,
        };

        customCursorAdapter = new CustomCursorAdapter(this, R.layout.list_item, cursor, projection, uiMapping, 0);
        listView.setAdapter(customCursorAdapter);
        customCursorAdapter.setOnRefreshListener(this);
    }

    public void onClickLogout(View view){
        Intent intent = new Intent(MainPageActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickBack(View view){
        Intent intent = new Intent(MainPageActivity.this, MenuActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("uid",userId);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onRefreshRequested(){
        recreate();
    }
}
