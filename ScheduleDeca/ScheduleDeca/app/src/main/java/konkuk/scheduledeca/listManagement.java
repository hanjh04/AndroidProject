package konkuk.scheduledeca;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class listManagement extends SQLiteOpenHelper{
    private String tableName = "activityList";

    public listManagement(Context context, String dbName, CursorFactory factory, int version){
        super(context, dbName, factory, version);
    }
    public void onCreate(SQLiteDatabase db){
        //최초에 데이터베이스가 없을 경우, 데이터베이스 생성을 위해 호출됨.
        //테이블 생성하는 코드
        db.execSQL("CREATE TABLE " + tableName + " (activity CHAR(20) PRIMARY KEY, day CHAR(10), startTime CHAR(10), hour CHAR(5), " +
                                                        "suggestion CHAR(6), optimization CHAR(6), memo VARCHAR(200));");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //데이터베이스의 버전이 바뀌었을 때 호출되는 콜백 메서드
        //버전이 바뀌었을 떄 기존 데이터베이스를 어떻게 변형할 것인지 작성.
        //각 버전의 변경 내용들을 버전마다 작성
        db.execSQL("DROP TABLE IF EXISTS "+ tableName);
        onCreate(db);
    }

    public String getTableName(){
        return this.tableName;
    }
}
