package cn.edu.flexfit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class RecordSCA extends SimpleCursorAdapter {
    private RefreshListener refreshListener;
    public interface RefreshListener{
        void refreshRequested();
    }

    public void setRefreshListener(RefreshListener listener){
        this.refreshListener = listener;
    }

    private DBHelper dbHelper;

    public RecordSCA(Context context, int layout, Cursor c, String[] from, int[] to, int flags){
        super(context, layout, c, from, to, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor){
        super.bindView(view, context, cursor);
        dbHelper = new DBHelper(context);

        @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(FitProviderContract.RECORD_ID));
        @SuppressLint("Range") int uid = cursor.getInt(cursor.getColumnIndex(FitProviderContract.USER_ID));
        @SuppressLint("Range") String type = cursor.getString(cursor.getColumnIndex(FitProviderContract.TYPE));
        @SuppressLint("Range") String distance = cursor.getString(cursor.getColumnIndex(FitProviderContract.DISTANCE));
        @SuppressLint("Range") String duration = cursor.getString(cursor.getColumnIndex(FitProviderContract.DURATION));
        @SuppressLint("Range") String speed = cursor.getString(cursor.getColumnIndex(FitProviderContract.SPEED));

        TextView typeView = view.findViewById(R.id.type);
        TextView speedView = view.findViewById(R.id.date);
        TextView distance_time = view.findViewById(R.id.duration);

        typeView.setText(type);
        speedView.setText(speed);
        distance_time.setText(distance + " " + duration);

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                deleteById(String.valueOf(id));

                if (refreshListener != null){
                    refreshListener.refreshRequested();
                }
                return true;
            }
        });
    }

    private void deleteById(String id){
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String selection = "_id=?";
        String[] selectionArgs = {id};
        database.delete("diaryTable", selection, selectionArgs);
        database.close();
    }
}
