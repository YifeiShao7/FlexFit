package cn.edu.flexfit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;

    private DBHelper dbHelper;

    private EditText username;

    private EditText password;

    private Button btn_login;

    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        username = findViewById(R.id.edittext_uname);
        password = findViewById(R.id.edittext_pw);

        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String unameContent = username.getText().toString();
                String pwContent = password.getText().toString();

                if (unameContent.length() == 0){
                    showDialog("Warning","The username cannot be empty");
                    return;
                }else if (pwContent.length() == 0){
                    showDialog("Warning", "The password cannot be empty");
                    return;
                }else {
                    onClickLogin(unameContent, pwContent);
                }
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpToRegister();
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        db.close();
    }

//    登陆事件
    private void onClickLogin(String username, String password){
//        认证用户名密码是否匹配
        int verifyResult = verifyAccount(username, password);
//        若匹配，跳转界面
//        若不匹配，dialog返回输出
        if (verifyResult == -1){
            showDialog("Warning", "There's no user account, Please register first!");
        }else if (verifyResult == 0){
            showDialog("Warning", "The password is wrong, please try again!");
        }else {
            jumpToMenuPage(verifyResult);
        }
    }

//    验证账号密码是否匹配
    @SuppressLint("Range")
    private int verifyAccount(String username, String password){
        db = dbHelper.getReadableDatabase();

        String[] projection = {FitProviderContract.PASSWORD, FitProviderContract.USER_ID};
        String selection = FitProviderContract.USERNAME + " = ?";
        String[] selectionArgs = {username};

        Cursor cursor = getContentResolver().query(FitProviderContract.USER_TABLE_URI, projection, selection, selectionArgs, null);

        String storedPassword;
        int uId;
        if (cursor == null){
            cursor.close();
            return -1;
        }else {
            if (cursor.moveToFirst()){
                storedPassword = cursor.getString(cursor.getColumnIndex(FitProviderContract.PASSWORD));
                uId = cursor.getInt(cursor.getColumnIndex(FitProviderContract.USER_ID));
            }else {
                cursor.close();
                return -1;}
        }

        if (storedPassword.equals(password)){
            cursor.close();
            return uId;
        }else {
            cursor.close();
            return 0;
        }
    }

    private void jumpToMenuPage(int uid){
        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
        intent.putExtra("uid", uid);
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

//    注册事件
    private void jumpToRegister(){
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}