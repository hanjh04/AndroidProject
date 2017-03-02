package org.androidtown.hello;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by hjh on 2016-05-14.
 */
public class isConnected {

    private Boolean isConnected = false;
    private void isConnected(){

        class LoginData extends AsyncTask<Void, Void, Boolean> {
            ProgressDialog loading;

            //background 스레드 실행하기 전 준비단계.
            //변수의 초기화, 네트워크의 통신 전 세팅해야할 것들을 작성.
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
            }

            /*@Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }*/

            protected Boolean doInBackground(Void... params) {
                try {
                    String link = "http://coffcoup.woobi.co.kr/login_woobi.php";

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    //true일 경우 서버와 통신할 때 POST방식으로
                    conn.setDoOutput(true);


                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    //Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }

                    return true;

                } catch (Exception e) {
                    return false;
                            //new String("Exception : " + e.getMessage());
                }
            }
        }
    }

}
