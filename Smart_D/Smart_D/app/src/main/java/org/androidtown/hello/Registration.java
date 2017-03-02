package org.androidtown.hello;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
//import com.google.android.gms.gcm.GoogleCloudMessaging;
//import com.google.android.gms.iid.InstanceID;


public class Registration extends AppCompatActivity {

    static final String TAG = "Register";
    public boolean pwdcheck = false;
    private String rid;
    private String senderID = "967200239184";
            //8611443692";
    insertToDatabase insertToDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        final EditText insert_name = (EditText)findViewById(R.id.insert_name);
        final EditText insert_email = (EditText)findViewById(R.id.insert_email);
        final EditText insert_password = (EditText)findViewById(R.id.insert_password);
        final EditText insert_age = (EditText)findViewById(R.id.insert_age);
        final EditText insert_pwdconfirm = (EditText)findViewById(R.id.insert_pwdconfirm);
        final RadioGroup genderGroup = (RadioGroup) findViewById(R.id.genderGroup);
        final RadioButton men = (RadioButton) findViewById(R.id.men);
        final RadioButton women = (RadioButton) findViewById(R.id.women);
        final String gender;

        Button pwdConfirm_Button = (Button) findViewById(R.id.pwdConfirm_Button);
        Button Join_Button = (Button) findViewById(R.id.Join_Button);

        //비밀번호 체크하기
        pwdConfirm_Button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                boolean pwdcheck;
                //         pwdCheck pwdc = new pwdCheck(true);

                String password = insert_password.getText().toString();
                String pwdConfirm = insert_pwdconfirm.getText().toString();

                if(password.equals(pwdConfirm)){
                    pwdcheck = true;
                    Toast.makeText(getApplicationContext(),
                            "비밀번호가 일치합니다.", Toast.LENGTH_SHORT).show();
                } else{
                    pwdcheck = false;
                    Toast.makeText(getApplicationContext(),
                            "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //회원가입 입력 후 저장
        Join_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(pwdcheck == true) {
                    String name = insert_name.getText().toString();
                    String email = insert_email.getText().toString();
                    String age = insert_age.getText().toString();
                    String password = insert_password.getText().toString();
                    String gender;
                    registGCM();

                    if(men.isChecked()){
                        gender = men.getText().toString();
                    } else{
                        gender = women.getText().toString();
                    }

                    insertToDB = new insertToDatabase();
                    insertToDB.insertToDatabase(name, age, gender, email, password, rid);
                } else{
                    Toast.makeText(getApplicationContext(),
                            "비밀번호를 확인하세요!!.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void onClick_Back(View v){
        finish();
    }

//gcm push message를 위한 등록 함수
    void registGCM(){
        class registGCM extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... params) {
                String msg = "";

                Log.d(TAG, msg);
                try {
                    InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
                    rid = instanceID.getToken(senderID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                    //instanceID.deleteToken(rid,GoogleCloudMessaging.INSTANCE_ID_SCOPE);
                    msg = "Device registered, registration ID=" + rid;

                    Log.d(TAG, msg);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        }
        registGCM regcm = new registGCM();
        regcm.execute(null, null, null);
    }
}
