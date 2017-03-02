package org.androidtown.hello;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by hjh on 2016-05-18.
 */
public class dbQuery {
    //데이터베이스
    SQLiteDatabase sqlDB;
    String tableName = "itemList";

    public dbQuery(){}

    void insert(listManagement db, String invoice, String company, String email, String isDelivered){
        sqlDB = db.getWritableDatabase();//쓰기용으로 데이터베이스 열기
        sqlDB.execSQL("insert into " + tableName + " values('" + invoice + "', '" + company + "', '" + email + "', " + isDelivered + ");");
        //Log.d(tag, "insert 하였습니다.");
        sqlDB.close();
    }

    void update(listManagement db, String invoice, String email){
        sqlDB=db.getWritableDatabase();
        sqlDB.execSQL("update itemList set isDelivered = 1 where email = " + email + " and invoice = " + invoice+";");
        sqlDB.close();
    }
//  $sql = "update itemList set isDelivered = '1' where email = '$email' and invoice = '1234567890";

    public void delete(listManagement db, String invoice){
        sqlDB = db.getWritableDatabase();
        sqlDB.execSQL("delete from " + tableName + " where invoice = " + invoice + ";");
        sqlDB.close();
    }

    public void select(Cursor c, listManagement db, String invoice){
        sqlDB = db.getReadableDatabase();//쓰기용으로 데이터베이스 열기
        c = sqlDB.rawQuery("select * from " + tableName + " where invoice  = '" + invoice + "';", null);
        while(c.moveToNext()){
            c.getString(0);
        }
        sqlDB.close();
    }

    public Cursor select(listManagement db){
        sqlDB = db.getReadableDatabase();//쓰기용으로 데이터베이스 열기
        Cursor c = sqlDB.rawQuery("select * from " + tableName  + ";", null);
        sqlDB.close();
        return c;
    }

}
