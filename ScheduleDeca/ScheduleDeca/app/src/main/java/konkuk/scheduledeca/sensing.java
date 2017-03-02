package konkuk.scheduledeca;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


//흔드는 것으로 시간측정의 시작을 하려 했으나 구현하지 못함.
//핸드폰을 세번 흔드는 것 감지.
public class sensing extends AppCompatActivity implements SensorEventListener {

    private TextView tv;
    private SensorManager sm;
//    private Sensor accelerormeterSensor;
    private Sensor s;

    private long lastTime;

    private long startTime;

    private float speed;

    private String TAG = "Sensors";

    private static final int SHAKE_THRESHOLD = 800;

    private int count = 0;
    private int cnt = 0;

    float var0_0, var0_1, var0_2;
    float var1_0, var1_1, var1_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensing);

        // 센서객체를 얻어오기 위해서는 센서메니저를 통해서만 가능하다
        sm = (SensorManager)
                getSystemService(Context.SENSOR_SERVICE);
        s = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); // 방향센서
    } // end of onCreate

    @Override
    protected void onResume() { // 화면에 보이기 직전에 센서자원 획득
        super.onResume();
        // 센서의 값이 변경되었을 때 콜백 받기위한 리스너를 등록한다
        sm.registerListener(this,        // 콜백 받을 리스너
                s,            // 콜백 원하는 센서
                SensorManager.SENSOR_DELAY_UI); // 지연시간
    }
    @Override
    protected void onPause() { // 화면을 빠져나가면 즉시 센서자원 반납해야함!!
        super.onPause();
        sm.unregisterListener(this); // 반납할 센서
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // 센서값이 변경되었을 때 호출되는 콜백 메서드
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //처음 움직였을 때 시간을 잼(count == 0 일때...)
            //시간을 잰 후 count를 1 증가시킴.
            if(count == 0){
                startTime = System.currentTimeMillis();
                var0_0 = event.values[0];
                var0_1 = event.values[1];
                var0_2 = event.values[2];
                count++;
            }else if(count == 1 ||
                      count == 2){
                lastTime = System.currentTimeMillis();
                var1_0 = event.values[0];
                var1_1 = event.values[1];
                var1_2 = event.values[2];
                Log.d(TAG, "Accelerometer2: x = " + var1_0 + ", y = " + var1_1 + ", z" + var1_2);
                speed = Math.abs(var0_0 + var0_1 + var0_2 - var1_0 - var1_1 - var1_2) / (lastTime - startTime) * 5000;
                if (lastTime - startTime < 800 ) {
                    if (speed > SHAKE_THRESHOLD) {
                        startTime = lastTime;
                        count++;
                    }
                } else {
                    count = 0;
                }
            }else if(count == 3){
                Toast.makeText(this, "가속도 변화 감지!", Toast.LENGTH_SHORT).show();
                count = 0;
            }

/*            if(count == 0){
                count++;


            }else if((count == 1) && (System.currentTimeMillis() - startTime) < 100 && ){
                //startTime 과 lastTime 과의 차이가
                startTime = System.currentTimeMillis();
                count++;
            }

            if((count == 2) && (System.currentTimeMillis() - startTime) < 100){
                //이벤트 발생!!
                //일단 지금은 textView에 변화값 찍어주는 걸로 하자

                Toast.makeText(this, "가속도 변화 감지!", Toast.LENGTH_SHORT).show();
                count = 0;
                var0 = event.values[0];
                var1 = event.values[1];
                var2 = event.values[2];

                Log.d(TAG, "Accelerometer: x = "+var0+", y = "+var1+", z"+var2);

                //Log.d(TAG, )
            }*/
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // 센서의 정확도가 변경되었을 때 호출되는 콜백 메서드
    }

}
