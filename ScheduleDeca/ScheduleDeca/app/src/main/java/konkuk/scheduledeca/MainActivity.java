package konkuk.scheduledeca;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.app.Fragment;
import java.util.ArrayList;
import java.util.Calendar;
import android.content.SharedPreferences;


public class MainActivity extends AppCompatActivity{

    listManagement listManagement;
    String tableName = "activityList";
    String tag = "SQLite";
    int dbVersion = 1;
    String dbName = "activityList.db";
    Cursor cursor;
    SQLiteDatabase sqlDB;
    Calendar cal = Calendar.getInstance();

    ArrayList<ArrayList<String>> list;

    SharedPreferences setting;
    SharedPreferences.Editor editor;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //DB 설정
        int nWeek = cal.get(Calendar.DAY_OF_WEEK);
        list = new ArrayList<>();
        listManagement = new listManagement(MainActivity.this, dbName, null, dbVersion);
        sqlDB = listManagement.getReadableDatabase();
        cursor = sqlDB.rawQuery("SELECT activity, startTime, hour, optimization, day FROM " + tableName, null);
        //fragment 설정
        Fragment fr = new DrawSchedule();

        //DB 읽어들여 DrawSchedule로 보내려 했으나 실패
        if(cursor != null){
            while(cursor.moveToNext()){
                if(cursor.getString(4).charAt(nWeek+1) == '1'){

                   Bundle bundle = new Bundle();
                    ArrayList<String> temp = new ArrayList<>();
                    temp.add(cursor.getString(0));//activity
                    temp.add(cursor.getString(1));//startTime
                    temp.add(cursor.getString(2));//hour
                    temp.add(cursor.getString(3));//optimization
                    list.add(temp);
                    bundle.putSerializable("list", list);

                }
            }
        }

        fr.setArguments(bundle);
    }
}