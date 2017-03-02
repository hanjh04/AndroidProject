package org.androidtown.hello;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ItemList extends AppCompatActivity {
    //데이터베이스
    SQLiteDatabase sqlDB;
    listManagement listManagement;
    String tableName = "itemList";
    String tag = "SQLite";
    int dbVersion = 1;
    String dbName = "invoice.db";
    Cursor cursor, cursor2;

    ArrayList<String> list, list2;
    ArrayAdapter<String> adapter, adapter2;
    ListView lv, lv2;
    InputMethodManager imm;

    TextView tv;

    insertToDatabase insertToDB;

    public static Activity itemList;
    /*
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
//    private GoogleApiClient client, client2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        itemList = this;

        tv = (TextView) findViewById(R.id.list);
        tv.setText("택배목록");

        //db 설정
        listManagement = new listManagement(ItemList.this, dbName, null, dbVersion);
        list = new ArrayList<String>();
        list2 = new ArrayList<String>();


        SharedPreferences setting;
        SharedPreferences.Editor editor;
        setting = getSharedPreferences("myInfo", 0);
        editor = setting.edit();

        sqlDB = listManagement.getReadableDatabase();
        cursor = sqlDB.rawQuery("SELECT invoice, company, isDelivered FROM " + tableName +
                " WHERE  email = " + setting.getString("email", ""), null);//isDelivered = 0 AND

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String company = cursor.getString(1);
                String invoice = cursor.getString(0);
                String isDelivered = cursor.getString(2);
                if(isDelivered.equals("0")){
                    list.add(company + " / 송장번호 " + invoice);
                }else if(isDelivered.equals("1")){
                    list2.add(company + " / 송장번호 " +invoice);
                }
            }
            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_single_choice, list);
            adapter2 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_single_choice, list2);

            lv = (ListView) findViewById(R.id.lv);
            lv2 = (ListView) findViewById(R.id.lv2);
            imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            lv.setAdapter(adapter);
            lv2.setAdapter(adapter2);
//            client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        }
    }

    public void onClick_delete(View v) {
        // 삭제 버튼
        int pos = lv.getCheckedItemPosition();
        int pos2 = lv2.getCheckedItemPosition();
        dbQuery query = new dbQuery();

        if (pos != ListView.INVALID_POSITION &&
                pos2 == ListView.INVALID_POSITION) {
            String[] split = list.get(pos).split(" ");
            query.delete(listManagement, split[3]);
            list.remove(pos);
            lv.clearChoices();
            adapter.notifyDataSetChanged();
            SharedPreferences setting;
            SharedPreferences.Editor editor;
            setting = getSharedPreferences("myInfo", 0);
            editor = setting.edit();
            insertToDB = new insertToDatabase();
            insertToDB.deleteInvoice(split[3], "jj"/*택배사 이름 집어넣기*/, setting.getString("email", ""));
        }else if(pos == ListView.INVALID_POSITION &&
                pos2 != ListView.INVALID_POSITION) {
            String[] split = list2.get(pos2).split(" ");
            query.delete(listManagement, split[3]);
            list2.remove(pos2);
            lv2.clearChoices();
            adapter.notifyDataSetChanged();
            SharedPreferences setting;
            SharedPreferences.Editor editor;
            setting = getSharedPreferences("myInfo", 0);
            editor = setting.edit();
            insertToDB = new insertToDatabase();
            insertToDB.deleteInvoice(split[3], "jj"/*택배사 이름 집어넣기*/, setting.getString("email", ""));
        }
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice, list);
        adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice, list2);
        lv.setAdapter(adapter);
        lv2.setAdapter(adapter2);
    }

    public void onClick_search(View V) {
        Toast.makeText(getApplicationContext(), "택배 등록 화면입니다.", Toast.LENGTH_SHORT).show();
        int pos = lv.getCheckedItemPosition();
        int pos2 = lv2.getCheckedItemPosition();
        if (pos != ListView.INVALID_POSITION &&
                pos2 == ListView.INVALID_POSITION) {
            String[] split = list.get(pos).split(" ");
            Intent intent = new Intent(getApplicationContext(), ItemSearch2.class);
            intent.putExtra("invoice", split[3]);
            intent.putExtra("company", split[0]);
            startActivity(intent);
        }else if(pos == ListView.INVALID_POSITION &&
                pos2 != ListView.INVALID_POSITION){
            String[] split = list2.get(pos).split(" ");
            Intent intent = new Intent(getApplicationContext(), ItemSearch2.class);
            intent.putExtra("invoice", split[3]);
            intent.putExtra("company", split[0]);
            startActivity(intent);
        }else {
            Intent intent = new Intent(getApplicationContext(), ItemSearch.class);
            startActivity(intent);
        }
    }

    public void onClick_back(View V){
        Intent intent = new Intent(getApplicationContext(), myInfo.class);
        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), myInfo.class);
        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}