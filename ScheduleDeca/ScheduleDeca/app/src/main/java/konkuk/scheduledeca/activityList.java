package konkuk.scheduledeca;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hjh on 2016-06-19.
 */
public class activityList extends AppCompatActivity {

    //데이터베이스
    SQLiteDatabase sqlDB;
    listManagement listManagement;
    String tableName = "activityList";
    String tag = "SQLite";
    int dbVersion = 1;
    String dbName = "activityList.db";
    Cursor cursor, cursor2;

    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    ListView lv;
    InputMethodManager imm;

    TextView tv;

    public static Activity itemList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activitylist);

        itemList = this;
//DB 설정
        listManagement = new listManagement(activityList.this, dbName, null, dbVersion);
//list 설정
        list = new ArrayList<String>();

        //DB 읽어오기
        sqlDB = listManagement.getReadableDatabase();
        cursor = sqlDB.rawQuery("SELECT activity, optimization FROM " + tableName, null);

        //DB 읽어온거 list에 집어넣기
        if(cursor != null){
            while(cursor.moveToNext()){
                String activity = cursor.getString(0);
                list.add(activity);
                list.add(cursor.getString(1));
            }
        }
        //list에 있는 항목들 adapter로
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice, list);

        //listView 선언하고 listView에 위에서 선언한 adapter 집어넣기
        lv = (ListView) findViewById(R.id.lv);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        lv.setAdapter(adapter);
    }

    //지우기
    public void onClick_delete(View V){
        int pos = lv.getCheckedItemPosition();
        dbQuery query = new dbQuery();

        //선택된 radioButton의 index를 구해 해당하는 index의 항목 삭제
        if(pos != ListView.INVALID_POSITION ){
            String act = list.get(pos);
            query.delete(listManagement, act);
            list.remove(pos);
            lv.clearChoices();
            adapter.notifyDataSetChanged();
        }
    }

    //수정창으로 이동.
    //선택된 리스트의 세부사항들을 DB로부터 읽어와 전달.
    public void onClick_change(View V){
        int pos = lv.getCheckedItemPosition();
        String act="0";
        if(pos != ListView.INVALID_POSITION){
            act = list.get(pos);
            cursor2 = sqlDB.rawQuery("SELECT * FROM " + tableName, null);
        }//activity, day, startTime, hour, suggestion, memo
        //(activity, day, startTime, hour, suggestion, optimization, memo); ");
        if(cursor2 != null){
            while(cursor2.moveToNext()){
                if(cursor2.getString(0).equals(act)){
                    String activity = cursor2.getString(0);
                    String day = cursor2.getString(1);
                    String startTime = cursor2.getString(2);
                    String hour = cursor2.getString(3);
                    String suggestion = cursor2.getString(4);
                    String memo = cursor2.getString(6);

                    Intent intent = new Intent(getApplicationContext(), changeActivity.class);
                    intent.putExtra("activity", activity);
                    intent.putExtra("day", day);
                    intent.putExtra("startTime", startTime);
                    intent.putExtra("hour", hour);
                    intent.putExtra("suggestion", suggestion);
                    intent.putExtra("memo", memo);
                    startActivity(intent);
                }
            }
        }
    }
}
