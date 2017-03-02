package org.androidtown.hello;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import android.content.SharedPreferences;

public class ItemSearch2 extends AppCompatActivity {
    EditText invoice;
    Button register_btn;

    //데이터베이스
    SQLiteDatabase sqlDB;
    listManagement listManagement;
    String tableName = "itemList";
    String tag = "SQLite";
    int dbVersion = 1;
    String dbName = "invoice.db";
    String company;

    //파싱
    Thread thread = null;
    List result = new ArrayList();
    int check = 0;
    LinearLayout dynamicLayout;
    String url = "https://www.ilogen.com/iLOGEN.Web.New/TRACE/TraceView.aspx?gubun=slipno&id=&slipno=";

    insertToDatabase insertToDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_search);

        ItemList itemList = (ItemList)ItemList.itemList;
        itemList.finish();
        //db 설정
        listManagement = new listManagement(ItemSearch2.this, dbName, null, dbVersion);

        //버튼 설정
        invoice = (EditText) findViewById(R.id.invoice);
        register_btn = (Button) findViewById(R.id.register_btn);

        //itemList에서부터 받아온 송장번호와 택배회사의 종류를 받는다.
        Intent intent = getIntent();
        String invoice_Rcvd = intent.getStringExtra("invoice");
        String companyy = intent.getStringExtra("company");

        invoice.setText(invoice_Rcvd);

        //find함수는 송장번호, 회사이름 입력받는 식으로 바꿀 것.
        find(url, invoice_Rcvd);


        //배송정보 파싱되어 나타날 곳
        String[] str = getResources().getStringArray(R.array.spinnerArray);
        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_dropdown_item, str);
        final Spinner spi = (Spinner) findViewById(R.id.spinner);
        spi.setAdapter(adapter);
        spi.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected
                            (AdapterView<?> Parent, View view, int position, long id) {
                        company = (String) spi.getSelectedItem();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                }
        );
    }

    public void onClick_register(View V){
        SharedPreferences setting;
        SharedPreferences.Editor editor;
        setting = getSharedPreferences("myInfo", 0);
        dbQuery query = new dbQuery();
        query.insert(listManagement, invoice.getText().toString(), company, setting.getString("email", ""), "0");
        editor = setting.edit();
        insertToDB = new insertToDatabase();
        insertToDB.insertToDatabase(invoice.getText().toString(), company, setting.getString("email", ""), "0");
    }

    public void onClick_return(View V){
        Intent intent = new Intent(getApplicationContext(), ItemList.class);
        startActivity(intent);
    }

    public void onClick_find(View V){
        find(url, invoice.getText().toString());
    }

    void find (String url, String invoice){
        //이 부분 나중에 수정해줘야 함!!!
        final String uu = url;
        final String aa = invoice;
        Runnable task = new Runnable(){
            public void run(){
                parse(uu + aa);
            }
        };

        thread = new Thread(task);
        thread.start();
        dynamicLayout = (LinearLayout)findViewById(R.id.dynamicArea);

        try{
            thread.join();
        }catch(Exception e) {

        }
        for(int i = 0; i < result.size(); i++){
            TextView dynamicTextview = new TextView(this);
            dynamicTextview.setText(result.get(i).toString());
            dynamicLayout.addView(dynamicTextview, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    public void parse(String strURL){

        try{
            URL url = new URL(strURL);
            //해당 URL 페이지를 가져온다.
            Source source = new Source(url);

            //메소드를 찾기 위해 시작부터 끝까지 태그들만 parse 한다.
            source.fullSequentialParse();

            //해당 데이터가 있는 부분을 찾는 부분분
            Element div = source.getAllElements(HTMLElementName.DIV).get(0);
            Element table = div.getAllElements(HTMLElementName.TABLE).get(3);
            List trList = table.getAllElements(HTMLElementName.TR);

            Iterator trIter = trList.iterator();
            trIter.next();

            while(trIter.hasNext()){
                Element tr = (Element) trIter.next();

                List dataList = tr.getAllElements(HTMLElementName.TD);

                Iterator dataIter = dataList.iterator();

                int chk = 0;

                List resultList = new ArrayList();

                //원하는 결과 값이 들어가는 부분.
                List rowList = new ArrayList();

                while(dataIter.hasNext()){
                    Element data = (Element) dataIter.next();
                    String value = data.getContent().getTextExtractor().toString();
                    rowList.add(chk,value);

                    chk++;

                    if(chk == 2){
                        result.add(check, rowList);
                        chk = 0;
                        check++;
                    }
                    Iterator resultIter = resultList.iterator();
                }

            }
            check = 0;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), ItemList.class);
        startActivity(intent);
    }
}
