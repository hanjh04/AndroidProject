package konkuk.scheduledeca;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.widget.EditText;

public class memoActivity extends AppCompatActivity {
    SharedPreferences setting;
    SharedPreferences.Editor editor;

    EditText et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);
        //layout 설정
        et = (EditText) findViewById(R.id.editText);

        //sharedPreference에 있는 내용 화면에 뿌리기
        setting = getSharedPreferences("memo", 0);
        editor = setting.edit();
        String mm2 = setting.getString("mm", "");
        et.setText(mm2);

    }

    //뒤로가기 버튼 눌리면 작성된 내용 저장 후 뒤 화면으로
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            String mm = et.getText().toString();
            setting = getSharedPreferences("memo", 0);
            editor = setting.edit();
            editor.putString("mm", mm);
            editor.commit();

            finish();
        }

        return true;
    }
}