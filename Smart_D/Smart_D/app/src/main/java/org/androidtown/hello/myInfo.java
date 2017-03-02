package org.androidtown.hello;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;


public class myInfo extends AppCompatActivity {
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
    }

    public void onCLick_search(View V){

        Toast.makeText(getApplicationContext(), "택배 목록입니다.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), ItemList.class);
        startActivity(intent);
    }

    /*
        public void loginbtn(View v){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
        }
    */

    public void onCLick_imgshow(View V){
        Intent intent = new Intent(getApplicationContext(), displayimage.class);
        startActivity(intent);
    }

    public void barcode(View v){
        Intent intent = new Intent(getApplicationContext(), Barcode.class);
        startActivity(intent);
    }

    public void onClick_Logout(View V){
        SharedPreferences setting = getSharedPreferences("myInfo", 0);
        SharedPreferences.Editor editor = setting.edit();
        editor.clear();
        editor.commit();
        finish();
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
}
