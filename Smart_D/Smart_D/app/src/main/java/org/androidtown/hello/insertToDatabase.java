package org.androidtown.hello;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import android.content.SharedPreferences;

public class insertToDatabase implements Serializable {

    public String name, email, password, age, gender;

    public insertToDatabase(){

    }
//회원정보 웹 데이터베이스에 집어넣기(회원가입)
    public void  insertToDatabase(String name, String age, String gender, String email, String password, String rid){

        class InsertData extends AsyncTask<String, Void, Void> {
            //ProgressDialog loading;

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                //loading = ProgressDialog.show(insertToDatabase.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(Void avoid) {
                //super.onPostExecute();
                //loading.dismiss();
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected Void doInBackground(String... params){
                try {
                    String name = (String) params[0];
                    String age = (String) params[1];
                    String gender = (String) params[2];
                    String email = (String) params[3];
                    String password = (String) params[4];
                    String rid = (String) params[5];

                    String link = "http://coffcoup.woobi.co.kr/insert.php";
                    String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
                    data += "&" + URLEncoder.encode("age", "UTF-8") + "=" + URLEncoder.encode(age, "UTF-8");
                    data += "&" + URLEncoder.encode("gender", "UTF-8") + "=" + URLEncoder.encode(gender, "UTF-8");
                    data += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
                    data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                    data += "&" + URLEncoder.encode("rid", "UTF-8") + "=" + URLEncoder.encode(rid, "UTF-8");

                    URL url = new URL(link);
                    //여기서 오류...해결
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    //Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                }
                catch(Exception e) {
//                    return new String("Exception : " + e.getMessage());
                }
                return null;
            }
        }

        InsertData task = new InsertData();
        task.execute(name, age, gender, email, password, rid);
    }

//웹서버 데이터베이스에 송장번호 삽입
    public  void insertToDatabase(String invoice, String company, String email, String isDelivered){
        class InsertData extends AsyncTask<String, Void, Void> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Void avoid) {
            }

            @Override
            protected Void doInBackground(String... params){
                try {
                    String invoice = (String) params[0];
                    String company = (String) params[1];
                    String email = (String) params[2];
                    String isDelivered = (String) params[3];

                    String link = "http://coffcoup.woobi.co.kr/insertInvoice_1.php";
                    String data = URLEncoder.encode("invoice", "UTF-8") + "=" + URLEncoder.encode(invoice, "UTF-8");
                    data += "&" + URLEncoder.encode("company", "UTF-8") + "=" + URLEncoder.encode(company, "UTF-8");
                    data += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
                    data += "&" + URLEncoder.encode("isDelivered", "UTF-8") + "=" + URLEncoder.encode(isDelivered, "UTF-8");

                    URL url = new URL(link);
                    //여기서 오류...해결
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    //Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                }
                catch(Exception e) {
//                    return new String("Exception : " + e.getMessage());
                }
                return null;
            }
        }
        InsertData task = new InsertData();
        task.execute(invoice, company, email, isDelivered);
    }

    //웹서버 데이터베이스에 송장번호 삽입
    public  void deleteInvoice(String invoice, String company, String email){
        class InsertData extends AsyncTask<String, Void, Void> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Void avoid) {
            }

            @Override
            protected Void doInBackground(String... params){
                try {
                    String invoice = (String) params[0];
                    String company = (String) params[1];
                    String email = (String) params[2];

                    String link = "http://coffcoup.woobi.co.kr/deleteInvoice.php";
                    String data = URLEncoder.encode("invoice", "UTF-8") + "=" + URLEncoder.encode(invoice, "UTF-8");
                    data += "&" + URLEncoder.encode("company", "UTF-8") + "=" + URLEncoder.encode(company, "UTF-8");
                    data += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");

                    URL url = new URL(link);
                    //여기서 오류...해결
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    //Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                }
                catch(Exception e) {
//                    return new String("Exception : " + e.getMessage());
                }
                return null;
            }
        }

        InsertData task = new InsertData();
        task.execute(invoice, company, email);
    }


    public insertToDatabase(String email){
        this.email = email;
    }
}
