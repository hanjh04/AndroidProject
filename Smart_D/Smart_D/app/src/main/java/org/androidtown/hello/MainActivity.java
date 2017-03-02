package org.androidtown.hello;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    //String setting = "myInfo";
    static boolean login = false;
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autoLogin();

        final EditText login_email = (EditText) findViewById(R.id.login_email);
        final EditText login_password = (EditText) findViewById(R.id.login_password);
        final EditText aaabb = (EditText) findViewById(R.id.aaabb);



        Button Login_Button = (Button) findViewById(R.id.Login_Button);
        Login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //입력한 email, password를 email, password 변수에 저장
                try{
                    String email = login_email.getText().toString();
                    String password = login_password.getText().toString();
                    Login(email, password);
                }catch(Exception e){

                }finally{

                }

                /*if(setting.getString("email", "") != ""){
                    Intent intent = new Intent(MainActivity.this, myInfo.class);
                    startActivity(intent);
                }*/
            }
        });
    }

    private void Login(String email, String password) {
        //첫 번째 인자 : execute, doInBackground에서 선언하는 가변인수 매개변수의 타입
        //두 번째 인자 : onProgressUpdata매서드에 선언하는 가변인수 매개변수의 타입
        //세 번째 인자 : doInBackground의 리턴값, onPostExecute메서드에 선언하는 가변인수 매개변수 타입
        class LoginData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            SharedPreferences setting;
            SharedPreferences.Editor editor;

            //background 스레드 실행하기 전 준비단계.
            //변수의 초기화, 네트워크의 통신 전 세팅해야할 것들을 작성.
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                setting = getSharedPreferences("myInfo", 0);
                editor = setting.edit();
                loading = ProgressDialog.show(MainActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    String email = (String) params[0];
                    String password = (String) params[1];

                    String link = "http://coffcoup.woobi.co.kr/login.php";
                    String login = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
                    login += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(login);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    //Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }

                    if(sb.toString().equals("Log-In Succeded.")){
                        editor.putString("email", email);
                        editor.commit();
                    }

                    /*Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);*/
                    autoLogin();
                    return sb.toString();
                    //Intent intent = new Intent(getApplicationContext(), Barcode.class);

                } catch (Exception e) {
                    return new String("Exception : " + e.getMessage());
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
        }
        LoginData logintask = new LoginData();
        logintask.execute(email, password);
    }

    public void onClick_Regist(View V){
        Intent intent = new Intent(getApplicationContext(), Registration.class);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if (count == 0) {
                backPressedTime = System.currentTimeMillis();
                count++;
                Toast.makeText(this, "한번 더 누르면 종료", Toast.LENGTH_SHORT).show();
            } else if (count == 1) {
                if (System.currentTimeMillis() - backPressedTime < FINISH_INTERVAL_TIME) {
                    moveTaskToBack(true);
                    finish();
                    android.os.Process.killProcess(android.os.Process.myPid());
                } else {
                    backPressedTime = System.currentTimeMillis();
                    Toast.makeText(this, "한번 더 누르면 종료", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return false;
    }

    public void autoLogin(){
        SharedPreferences setting = getSharedPreferences("myInfo", 0);
        if (setting.getString("email", "") != "") {
            Intent start = new Intent(MainActivity.this, myInfo.class);
            startActivity(start);
        }
    }
}
