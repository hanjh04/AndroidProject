package org.androidtown.hello;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;

public class Barcode extends AppCompatActivity {
    public static Activity Barcode;

    Thread thread = null;
    List result = new ArrayList();
    int check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        final EditText view_email = (EditText) findViewById(R.id.email);
        LinearLayout dynamicLayout = (LinearLayout)findViewById(R.id.dynamicArea);

        Button push_btn = (Button) findViewById(R.id.push_btn);
        Button return_btn = (Button) findViewById(R.id.return_btn);

        SharedPreferences setting = getSharedPreferences("myInfo", 0);
        String str = setting.getString("email", "");
        view_email.setText(str);
        //isConnected();

        MultiFormatWriter gen = new MultiFormatWriter();
        String data = str;
        try {
            final int WIDTH = 1000;
            final int HEIGHT = 320;
            BitMatrix bytemap = gen.encode(data, BarcodeFormat.CODE_128, WIDTH, HEIGHT);
            Bitmap bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);
            for (int i = 0 ; i < WIDTH ; ++i)
                for (int j = 0 ; j < HEIGHT ; ++j) {
                    bitmap.setPixel(i, j, bytemap.get(i,j) ? Color.BLACK : Color.WHITE);
                }

            ImageView view = (ImageView) findViewById(R.id.imageView1);
            view.setImageBitmap(bitmap);
            view.invalidate();
            System.out.println("done!");

        } catch (Exception e) {
            e.printStackTrace();
        }


        push_btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                push(view_email.getText().toString());
            }
        });

    }

    public void onClick_back(View V){
        finish();
    }

    //푸시메시지 보내는 역할
    void push(String email){
        class pushtask extends AsyncTask<String, Void, String>{

            ProgressDialog loading;

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(Barcode.this, "Please Wait", null, true, true);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(String... params){
                try{
                    String email = (String) params[0];

                    String link = "http://coffcoup.woobi.co.kr/gcm.php";
                    String push_email = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(push_email);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    //Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                } catch(Exception e) {
                    return new String("Exception : " + e.getMessage());
                }
            }
        }
        pushtask pPush = new pushtask();
        pPush.execute(email);
    }
}
