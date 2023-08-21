package cn.edu.flexfit;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = DBHelper.class.getSimpleName();

    private static final String USER_ID = "user_id";

    private static final String USER_NAME = "user_name";

    private static final String PASSWORD = "password";

    private static final String USER_TABLE = "userTable";

    private static final String DIARY_ID = "_id";

//    private static final String USER_ID = "user_id";

    private static final String TYPE = "type";

    private static final String DATE = "date";

    private static final String DURATION = "duration";

    private static final String DIARY_TABLE = "diaryTable";

    private static final String RECORD_ID = "record_id";

//    private static final String USER_ID = "user_id";

//    private static final String TYPE = "type";

    private static final String DISTANCE = "distance";

//    private static final String DURATION = "duration";
    private static final String SPEED = "speed";

    private static final String RECORD_TABLE = "recordTable";

    private static final Integer SQLITE_TABLE_VERSION = 7;

    private static final String USER_TABLE_CREATE =
            "CREATE TABLE if not exists "+ USER_TABLE
             + "("
             + USER_ID + " integer PRIMARY KEY autoincrement,"
             + USER_NAME + ","
             + PASSWORD
             + ");";

    private static final String DIARY_TABLE_CREATE =
            "CREATE TABLE if not exists " + DIARY_TABLE
             + "("
             + DIARY_ID + " integer PRIMARY KEY autoincrement,"
             + USER_ID + ","
             + TYPE + ","
             + DATE + ","
             + DURATION
             + ");";

    private static final String RECORD_TABLE_CREATE =
            "CREATE TABLE if not exists " + RECORD_TABLE
             + "("
             + DIARY_ID + " integer PRIMARY KEY autoincrement,"
             + USER_ID + ","
             + TYPE + ","
             + DISTANCE + ","
             + DURATION + ","
             + SPEED
             + ");";

    DBHelper(Context context){
        super(context, context.getString(R.string.app_db_name), null, SQLITE_TABLE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        Log.e(TAG, "onCreate: Activated");

        db.execSQL(USER_TABLE_CREATE);
        db.execSQL(DIARY_TABLE_CREATE);
        db.execSQL(RECORD_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        Log.d(TAG, String.format("onUpgrade: Database Version updated (%d -> %d)", oldVersion, newVersion));

        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE + ";");
        db.execSQL("DROP TABLE IF EXISTS " + DIARY_TABLE + ";");
        db.execSQL("DROP TABLE IF EXISTS " + RECORD_TABLE + ";");

        onCreate(db);
    }
}
