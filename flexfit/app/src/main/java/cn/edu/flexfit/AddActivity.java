package cn.edu.flexfit;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AddActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    private int userId;

    private Button btn_add;
    private Button btn_back;

    private EditText type;
    private EditText date;
    private EditText duration;

    int where;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        dbHelper = new DBHelper(this);

        Intent intent = getIntent();
        userId = intent.getIntExtra("uid", -1);
        btn_add = findViewById(R.id.add);
        btn_back = findViewById(R.id.back);

        type = findViewById(R.id.edittext_type);
        date = findViewById(R.id.distance_text);
        duration = findViewById(R.id.duration_text);

        where = intent.getIntExtra("where", -1);
        if (where == 2){
            String tempType = intent.getStringExtra("type");
            String tempDate = intent.getStringExtra("date");
            String tempDuration = intent.getStringExtra("duration");
            id = intent.getIntExtra("id", -1);
            type.setText(tempType);
            date.setText(tempDate);
            duration.setText(tempDuration);
        }

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                加入数据库 然后返回上一级
                String typeContent = type.getText().toString();
                String dateContent = date.getText().toString();
                String durationContent = duration.getText().toString();

                if (typeContent.length() == 0){
                    showDialog("Warning", "Please fill in the Sport Type");
                    return;
                }
                if (dateContent.length() == 0){
                    showDialog("Warning", "Please fill in the sport date");
                    return;
                }
                if (durationContent.length() == 0){
                    showDialog("Warning", "Please fill in the sport duration");
                    return;
                }
                addDiary(userId, typeContent, dateContent, durationContent);

                jumpBack();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                直接返回上一级，带着uid
                jumpBack();
            }
        });
    }

    private void addDiary(int uid, String type, String date, String duration){
        ContentValues values = new ContentValues();
        values.put(FitProviderContract.USER_ID, uid);
        values.put(FitProviderContract.TYPE, type);
        values.put(FitProviderContract.DATE, date);
        values.put(FitProviderContract.DURATION, duration);
        if (where == 1){
            getContentResolver().insert(FitProviderContract.DIARY_TABLE_URI, values);
        }else if (where == 2){
            String selection = "_id = ?";
            String[] selectionArgs = {String.valueOf(id)};
            getContentResolver().update(FitProviderContract.DIARY_TABLE_URI, values, selection, selectionArgs);
        }
    }

    private void jumpBack(){
        Intent intent = new Intent(AddActivity.this, MainPageActivity.class);
        intent.putExtra("uid", userId);
        startActivity(intent);
    }

    //        输出提示框
    private void showDialog(String title,String content){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(content);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
