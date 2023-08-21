package cn.edu.flexfit;

import android.net.Uri;

public class FitProviderContract {
    public static final String AUTHORITY = "cn.edu.flexfit.FitProvider";

    public static final Uri USER_TABLE_URI = Uri.parse("content://" + AUTHORITY + "/userTable");

    public static final Uri DIARY_TABLE_URI = Uri.parse("content://" + AUTHORITY + "/diaryTable");

    public static final Uri RECORD_TABLE_URI = Uri.parse("content://" + AUTHORITY +"/recordTable");

    public static final String USER_ID = "user_id";

    public static final String USERNAME = "user_name";

    public static final String PASSWORD = "password";

    public static final String DIARY_ID = "_id";

//    public static final String USER_ID = "user+id";

    public static final String TYPE = "type";

    public static final String DATE = "date";

    public static final String DURATION = "duration";

    public static final String RECORD_ID = "_id";

//    public static final String USER_ID = "user_id";

//    public static final String TYPE = "type";

    public static final String DISTANCE = "distance";

//    public static final String DURATION = "duration";
    public static final String SPEED = "speed";

    public static final String CONTENT_TYPE_SINGLE = "vnd.android.cursor.item/RecordProvider.data.text";

    public static final String CONTENT_TYPE_MULTIPLE = "vnd.android.cursor.dir/RecordProvider.data.text";
}
