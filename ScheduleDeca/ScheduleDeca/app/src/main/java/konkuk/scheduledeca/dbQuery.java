package konkuk.scheduledeca;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.CheckBox;
import android.content.ContentValues;

/**
 * Created by hjh on 2016-05-18.
 */
public class dbQuery {
    //데이터베이스
    SQLiteDatabase sqlDB;
    String tableName = "activityList";

    public dbQuery(){}
    //insert
    void insert(listManagement db, String activity, String day, String startTime, String hour, String suggestion, String optimization, String memo){
        sqlDB = db.getWritableDatabase();//쓰기용으로 데이터베이스 열기
        sqlDB.execSQL("insert into " + tableName + " values('" + activity+ "', '" + day + "', '" + startTime + "', '"
                                                                     + hour + "', '" + suggestion + "', '" + optimization + "', '" + memo + "');");
        //Log.d(tag, "insert 하였습니다.");
        sqlDB.close();
    }

    //update
    void update(listManagement db, String activity, String day, String startTime, String hour, String suggestion, String memo){
        ContentValues values = new ContentValues();
        sqlDB=db.getWritableDatabase();
        values.put("day", day);
        values.put("startTime", startTime);
        values.put("hour", hour);
        values.put("suggestion", suggestion);
        values.put("memo", memo);
        sqlDB.update(tableName, values, "activity=?", new String[]{activity});
        sqlDB.close();
    }

    //update
    void update(listManagement db, String activity, String optimization){
        ContentValues values = new ContentValues();
        sqlDB = db.getWritableDatabase();
        values.put("optimization", optimization);
        sqlDB.update(tableName, values, "activity=?", new String[]{activity});
        sqlDB.close();
    }

    //delete
    public void delete(listManagement db, String activity){
        sqlDB = db.getWritableDatabase();
        sqlDB.delete(tableName, "activity=?", new String[]{activity});
        sqlDB.close();
    }

    //select.. 쓰이진 않음.
    public Cursor select(listManagement db){
        sqlDB = db.getReadableDatabase();//쓰기용으로 데이터베이스 열기
        Cursor c = sqlDB.rawQuery("select * from " + tableName  + ";", null);
        sqlDB.close();
        return c;
    }

}
