package cn.edu.flexfit;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CustomCursorAdapter extends SimpleCursorAdapter {
    private OnRefreshListener onRefreshListener;

    public interface OnRefreshListener{
        void onRefreshRequested();
    }

    public void setOnRefreshListener(OnRefreshListener listener){
        this.onRefreshListener = listener;
    }

    private DBHelper dbHelper;

    public CustomCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags){
        super(context, layout, c, from, to, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor){
        super.bindView(view, context, cursor);
        dbHelper = new DBHelper(context);

        @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(FitProviderContract.DIARY_ID));
        @SuppressLint("Range") int uid = cursor.getInt(cursor.getColumnIndex(FitProviderContract.USER_ID));
        @SuppressLint("Range") String type = cursor.getString(cursor.getColumnIndex(FitProviderContract.TYPE));
        @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(FitProviderContract.DATE));
        @SuppressLint("Range") String duration = cursor.getString(cursor.getColumnIndex(FitProviderContract.DURATION));

        TextView typeView = view.findViewById(R.id.type);
        TextView dateView = view.findViewById(R.id.date);
        TextView durationView = view.findViewById(R.id.duration);

        typeView.setText(type);
        dateView.setText(date);
        durationView.setText(duration);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddActivity.class);
                Bundle bundle = new Bundle();

                bundle.putInt("id", id);
                bundle.putInt("uid", uid);
                bundle.putInt("where", 2);
                bundle.putString("type", type);
                bundle.putString("date", date);
                bundle.putString("duration", duration);

                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                deleteById(String.valueOf(id));

                if (onRefreshListener != null){
                    onRefreshListener.onRefreshRequested();
                }
                return true;
            }
        });
    }

    private void deleteById(String id){
        Log.e("deleted id", id);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String selection = "_id=?";
        String[] selectionArgs = {id};
        database.delete("diaryTable", selection, selectionArgs);
        database.close();
    }

}
