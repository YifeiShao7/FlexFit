package cn.edu.flexfit;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private DBHelper dbHelper;

    private EditText username;

    private EditText password1;

    private EditText password2;

    private Button btn_register;
    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DBHelper(this);

        username = findViewById(R.id.username);
        password1 = findViewById(R.id.password);
        password2 = findViewById(R.id.confirm_password);

        btn_register = findViewById(R.id.btn_register);
        btn_back = findViewById(R.id.btn_back);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String unameContent = username.getText().toString();
                String pw1Content = password1.getText().toString();
                String pw2Content = password2.getText().toString();

                if (!pw1Content.equals(pw2Content)){
                    showDialog("Warning", "The two passwords are different, Please try again.");
                    return;
                }

                int checkResult = checkUsername(unameContent);
                if (checkResult > 0){
                    showDialog("Warning", "The account is already exists, Please Login directly");
                    return;
                }
                if (checkResult < 0){
                    addUser(unameContent, pw1Content);
                    showDialog("Success", "Your account has registered, now you can log in");
                }

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpToMain();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

//    若数据库中已存在该用户，返回uid。若该用户名可用，返回-1
    private int checkUsername(String username){
        db = dbHelper.getReadableDatabase();

        String[] projection = {FitProviderContract.USER_ID};
        String selection = FitProviderContract.USERNAME + " = ?";
        String[] selectionArgs = {username};

        Cursor cursor = getContentResolver().query(FitProviderContract.USER_TABLE_URI, projection, selection, selectionArgs, null);

        if (cursor != null){
            if (cursor.moveToFirst()){
                @SuppressLint("Range") int uid = cursor.getInt(cursor.getColumnIndex(FitProviderContract.USER_ID));
                cursor.close();
                return uid;
            }else {
                cursor.close();
                return -1;
            }
        }else {
            cursor.close();
            return 0;
        }
    }

    private void addUser(String username, String password){
        ContentValues values = new ContentValues();
        values.put(FitProviderContract.USERNAME, username);
        values.put(FitProviderContract.PASSWORD, password);

        getContentResolver().insert(FitProviderContract.USER_TABLE_URI, values);
    }

    private void jumpToMain(){
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
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
