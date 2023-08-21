package cn.edu.flexfit;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FitProvider extends ContentProvider {
    private final static String TAG = FitProvider.class.getSimpleName();

    private DBHelper dbHelper;

    private static final UriMatcher mURIMatcher;

    private static final String TABLE_NAME = "userTable";

    static {
        mURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mURIMatcher.addURI(cn.edu.flexfit.FitProviderContract.AUTHORITY, "userTable", 1);
        mURIMatcher.addURI(FitProviderContract.AUTHORITY, "userTable/#", 2);
        mURIMatcher.addURI(FitProviderContract.AUTHORITY, "diaryTable", 3);
        mURIMatcher.addURI(FitProviderContract.AUTHORITY, "diaryTable/#", 4);
        mURIMatcher.addURI(FitProviderContract.AUTHORITY, "recordTable", 5);
        mURIMatcher.addURI(FitProviderContract.AUTHORITY, "recordTable/#", 6);
    }

    @Override
    public boolean onCreate(){
        Log.d(TAG, "ContentProvider - OnCreate");
        this.dbHelper = new DBHelper(this.getContext());
        return true;
    }

    @Nullable
    public Cursor query(@Nullable Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder){
        Log.d(TAG, String.format("Received URI: %s [Matches] %s", uri.toString(), mURIMatcher.match(uri)));
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (mURIMatcher.match(uri)){
            case 1:
                return db.query("userTable", projection, selection, selectionArgs, null, null, sortOrder);

            case 2:
                selection = "user_id = " + uri.getLastPathSegment();

            case 3:
                return db.query("diaryTable", projection, selection, selectionArgs, null, null, sortOrder);

            case 4:
                selection = "_id = " + uri.getLastPathSegment();

            case 5:
                return db.query("recordTable", projection, selection, selectionArgs, null,null, sortOrder);

            case 6:
                selection = "record_id = " + uri.getLastPathSegment();

            default:
                return null;
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        if (uri.getLastPathSegment() == null){
            return FitProviderContract.CONTENT_TYPE_MULTIPLE;
        }
        else {
            return FitProviderContract.CONTENT_TYPE_SINGLE;
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String tableName;

        switch (mURIMatcher.match(uri)){
            case 1:

            case 2:
                tableName = "userTable";
                break;

            case 3:

            case 4:
                tableName = "diaryTable";
                break;

            case 5:

            case6:
                tableName = "recordTable";
                break;

            default:
                tableName = "";
        }

        Uri insertURI;

        long id = db.insert(tableName, null, values);
        db.close();
        insertURI = ContentUris.withAppendedId(uri, id);

        Log.d(TAG, insertURI.toString());

        getContext().getContentResolver().notifyChange(insertURI, null);
        return insertURI;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String tableName;

        switch (mURIMatcher.match(uri)){
            case 1:

            case 2:
                tableName = "userTable";
                break;

            case 3:

            case 4:
                tableName = "diaryTable";
                break;

            case 5:

            case 6:
                tableName = "recordTable";
                break;

            default:
                tableName = "";
        }

        int rowDeleted = db.delete(tableName, selection, selectionArgs);
        db.close();

        getContext().getContentResolver().notifyChange(uri, null);

        return rowDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String tableName;
        switch (mURIMatcher.match(uri)){
            case 1:
            case 2:
                tableName = "userTable";
                break;

            case 3:

            case 4:
                tableName = "diaryTable";
                break;
            case 5:

            case 6:
                tableName = "recordTable";
                break;
            default:
                tableName = "";
        }

        int rowUpdated = db.update(tableName, contentValues, selection, selectionArgs);
        db.close();

        getContext().getContentResolver().notifyChange(uri, null);
        return rowUpdated;
    }












}
