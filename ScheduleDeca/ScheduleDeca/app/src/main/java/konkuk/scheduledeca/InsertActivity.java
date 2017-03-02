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

public class InsertActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private CheckBox sun, mon, tue, wed, thu, fri, sat, suggestion;
    private EditText et, memo;
    private TextView time, tv3;
    private String sugges = "0";
    private String workTime = "0";
    private String msg = "0";
    private String startTime = "0";
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
        setContentView(R.layout.activity_insertactivity);

        Button enrollment = (Button) findViewById(R.id.button1);
        //layout 설정
        sun =(CheckBox)findViewById(R.id.sun);
        mon =(CheckBox)findViewById(R.id.mon);
        tue =(CheckBox)findViewById(R.id.tue);
        wed =(CheckBox)findViewById(R.id.wed);
        thu =(CheckBox)findViewById(R.id.thu);
        fri =(CheckBox)findViewById(R.id.fri);
        sat =(CheckBox)findViewById(R.id.sat);
        suggestion = (CheckBox)findViewById(R.id.suggestion);
        et = (EditText)findViewById(R.id.editText2);
        memo = (EditText)findViewById(R.id.memo);
        time = (TextView)findViewById(R.id.time);
        tv3 = (TextView)findViewById(R.id.textView3);

        //db 설정
        listManagement = new listManagement(InsertActivity.this, dbName, null, dbVersion);

        //달력 설정
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
                        tv3.setText((String)spi.getSelectedItem());

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

    }

    //timepicker 버튼
    public void onClick_timePick(View V){
        new TimePickerDialog(InsertActivity.this, timeSetListener, hour, minute, false).show();
    }

    //listActivity 이동 버튼
    public void onClick_list(View V){
        Intent intent = new Intent(getApplicationContext(), activityList.class);
        startActivity(intent);
    }

    //등록버튼
    public void onClick_enrollment(View V){
        String activity = et.getText().toString();
        String mm = memo.getText().toString();
        String wTime = tv3.getText().toString();

        if(sun.isChecked()) dday+=64;
        if(mon.isChecked()) dday+=32;
        if(tue.isChecked()) dday+=16;
        if(wed.isChecked()) dday+=8;
        if(thu.isChecked()) dday+=4;
        if(fri.isChecked()) dday+=2;
        if(sat.isChecked()) dday+=1;

        if(suggestion.isChecked()) sugges = "1";

        dbQuery query = new dbQuery();
        query.insert(listManagement, activity, Integer.toBinaryString(dday), startTime, wTime, sugges, "0", mm);
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