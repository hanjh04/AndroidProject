package org.androidtown.hello;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.SharedPreferences;

public class displayimage extends AppCompatActivity {
    ImageView view;
    String imgUrl = "http://coffcoup.woobi.co.kr/";
    Bitmap bmImg;

    listManagement listManagement;
    int dbVersion = 1;
    String dbName = "invoice.db";

    back task;

    TextView tv, tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayimage);
        task = new back();
        view = (ImageView) findViewById(R.id.imgView);
        tv = (TextView) findViewById(R.id.textView3);

        Intent intent = getIntent();
        String invoice_Rcvd = intent.getStringExtra("msg");

        listManagement = new listManagement(displayimage.this, dbName, null, dbVersion);

        SharedPreferences setting;
        SharedPreferences.Editor editor;
        setting = getSharedPreferences("myInfo", 0);
        editor = setting.edit();

        tv.setText("송장번호 : " + invoice_Rcvd);
        String check = setting.getString("email", "");

        task.execute(imgUrl + "example.jpg");
        dbQuery query = new dbQuery();
        query.update(listManagement, invoice_Rcvd, check);

    }

    private class back extends AsyncTask<String, Integer,Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            // TODO Auto-generated method stub
            try{

                URL myFileUrl = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection)myFileUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();

                InputStream is = conn.getInputStream();

                bmImg = BitmapFactory.decodeStream(is);
            }catch(IOException e){
                e.printStackTrace();
            }
            return bmImg;
        }

        protected void onPostExecute(Bitmap img){
            view.setImageBitmap(bmImg);
        }
    }

    public void onClick_ok(View V) {
        Intent intent = new Intent(getApplicationContext(), ItemList.class);
        startActivity(intent);
    }

}

/*private class phpDown extends AsyncTask<String, Integer,String> {
        @Override
        protected String doInBackground(String... params) {
            StringBuilder jsonHtml = new StringBuilder();
            try {
                // 연결 url 설정
                URL url = new URL(params[0]);
                String email = (String) params[1];

                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");

                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write(data);
                wr.flush();

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                for (; ; ) {
                    // 웹상에 보여지는 텍스트를 라인단위로 읽어 저장.
                    String line = br.readLine();
                    if (line == null) break;

                    // 저장된 텍스트 라인을 jsonHtml에 붙여넣음
                    jsonHtml.append(line + "\n");
                }
                br.close();


            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return jsonHtml.toString();
        }
        protected void onPostExecute(String str) {
            tv.setText(str);
//            dynamicLayout.addView(dynamicTextview, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }*/

