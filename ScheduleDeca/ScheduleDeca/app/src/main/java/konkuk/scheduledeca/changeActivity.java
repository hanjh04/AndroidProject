package konkuk.scheduledeca;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class changeActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private CheckBox sun, mon, tue, wed, thu, fri, sat, suggestion;
    private EditText memo;
    private TextView et, time, tv3;
    private String sugges = "0";
    private String workTime = "0";
    private String msg = "0";
    private String startTime = "0";

    private String ddday = "0";

    private Button btnTimePicker;
    TimePickerDialog _time;

    int day, hour, minute;

    private int dday = 256;

    public static TimePicker timePicker;

    listManagement listManagement;
    String tableName = "activityList";
    String tag = "SQLite";
    int dbVersion = 1;
    String dbName = "activityList.db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);

        //layout 설정
        Button enrollment = (Button) findViewById(R.id.button1);
        sun =(CheckBox)findViewById(R.id.sun);
        mon =(CheckBox)findViewById(R.id.mon);
        tue =(CheckBox)findViewById(R.id.tue);
        wed =(CheckBox)findViewById(R.id.wed);
        thu =(CheckBox)findViewById(R.id.thu);
        fri =(CheckBox)findViewById(R.id.fri);
        sat =(CheckBox)findViewById(R.id.sat);
        suggestion = (CheckBox)findViewById(R.id.suggestion);
        et = (TextView)findViewById(R.id.editText2);
        tv3 = (TextView) findViewById(R.id.textView3);
        memo = (EditText)findViewById(R.id.memo);
        time = (TextView)findViewById(R.id.time);

        //db 설정
        listManagement = new listManagement(changeActivity.this, dbName, null, dbVersion);

        //달력설정
        GregorianCalendar calendar = new GregorianCalendar();
        day= calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        //spinner 설정
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
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

        //activityList로부터 전달받은 값들 변수에 저장
        Intent intent = getIntent();
        String activityy = intent.getStringExtra("activity");
        ddday = intent.getStringExtra("day");
        startTime = intent.getStringExtra("startTime");
        workTime = intent.getStringExtra("hour");
        sugges = intent.getStringExtra("suggestion");
        String mm2 = intent.getStringExtra("memo");

        //저장 후 화면에 띄우기
        setting(activityy, ddday, startTime, workTime, sugges, mm2);
    }

    //activityList로부터 전달받은 항목들 화면에 띄우는 메서드
    public void setting(String activityy, String ddday, String startTime, String workTime, String sugges, String mm2){
        et.setText(activityy);
        msg = String.format("%d 시 %d 분", Integer.parseInt(startTime)/60, Integer.parseInt(startTime)%60 );
        time.setText(msg);
        memo.setText(mm2);
        tv3.setText(workTime);
        if(ddday.charAt(2) == '1'){
            sun.setChecked(true);
        }
        if(ddday.charAt(3) == '1'){
            mon.setChecked(true);
        }
        if(ddday.charAt(4) == '1'){
            tue.setChecked(true);
        }
        if(ddday.charAt(5) == '1'){
            wed.setChecked(true);
        }
        if(ddday.charAt(6) == '1'){
            thu.setChecked(true);
        }
        if(ddday.charAt(7) == '1'){
            fri.setChecked(true);
        }
        if(ddday.charAt(8) == '1'){
            sat.setChecked(true);
        }

        if(sugges.equals("1")){
            suggestion.setChecked(true);
        }



    }

    //timepicker 버튼
    public void onClick_timePick(View V){
        new TimePickerDialog(changeActivity.this, timeSetListener, hour, minute, false).show();
    }

    //list로 이동하는 버튼
    public void onClick_list(View V){
        Intent intent = new Intent(getApplicationContext(), activityList.class);
        startActivity(intent);
    }

    //수정버튼
    public void onClick_enrollment(View V){
        String activity = et.getText().toString();
        String mm = memo.getText().toString();

        dday = 256;
        if(sun.isChecked()) dday+=64;
        if(mon.isChecked()) dday+=32;
        if(tue.isChecked()) dday+=16;
        if(wed.isChecked()) dday+=8;
        if(thu.isChecked()) dday+=4;
        if(fri.isChecked()) dday+=2;
        if(sat.isChecked()) dday += 1;

        if (suggestion.isChecked()) sugges = "1";

        //(listManagement db, String day, String startTime, String hour, String suggestion, String memo){
        dbQuery query = new dbQuery();
        query.update(listManagement, activity, Integer.toBinaryString(dday), startTime, workTime, sugges , mm);
    }

    //timepicker버튼 눌렀을 때 뜨는 화면
    private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // TODO Auto-generated method stub
            msg = String.format("%d 시 %d 분", hourOfDay, minute);
            startTime = Integer.toString((hourOfDay * 60) + minute);
            time.setText(msg);
            //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
        if(sun.isChecked()){
            dday+=64;
        }else if(mon.isChecked()){
            dday+=32;
        }else if(tue.isChecked()){
            dday+=16;
        }else if(wed.isChecked()){
            dday+=8;
        }else if(thu.isChecked()){
            dday+=4;
        }else if(fri.isChecked()){
            dday+=2;
        }else if(sat.isChecked()){
            dday+=1;
        }

        if(suggestion.isChecked()){

        }
    }


}