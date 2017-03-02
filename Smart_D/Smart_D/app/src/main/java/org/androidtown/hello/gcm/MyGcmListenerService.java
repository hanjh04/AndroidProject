package org.androidtown.hello.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import org.androidtown.hello.ItemList;
//추가..
import org.androidtown.hello.MainActivity;
import org.androidtown.hello.R;
import org.androidtown.hello.displayimage;

public class MyGcmListenerService extends GcmListenerService {
    static final String TAG = "MyGcmListenerService";
    public static boolean isPopup = true;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String title = data.getString("title");
        String text = data.getString("text");
        String msg = data.getString("msg");

        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Title: " + title);
        Log.d(TAG, "Text: " + text);

        sendNotification(title, text);
        //from is sender id
        //data is datas

        if (isPopup) {
            // 팝업으로 사용할 액티비티를 호출할 인텐트를 작성한다.
            Intent popupIntent = new Intent(this, displayimage.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            popupIntent.putExtra("msg", msg);
            // 그리고 호출한다.
            startActivity(popupIntent);
        }
    }

    private void sendNotification(String title, String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

        //PushWakeLock.acquireCpuWakeLock(this);
    }
}
