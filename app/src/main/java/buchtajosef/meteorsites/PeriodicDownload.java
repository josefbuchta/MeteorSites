package buchtajosef.meteorsites;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class PeriodicDownload extends BroadcastReceiver {

    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        new Thread(startDownload).start();
    }

    public void setAlarm(Context context) {
        this.context = context;
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, PeriodicDownload.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_NO_CREATE);
        //Start new only if there is none
        if (pi == null) {
            pi = PendingIntent.getBroadcast(context, 0, i, 0);
            am.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), 1000 * 60 * 60 * 24, pi); // Millisec * Second * Minute
            new Thread(startDownload).start();
        }
    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, PeriodicDownload.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    //Runnable used to download data
    private Runnable startDownload = new Runnable() {
        @Override
        public void run() {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm.getActiveNetworkInfo() != null) {
                try {
                    File meteorJsonFile = new File(context.getFilesDir(), MainActivity.dataFileName);
                    URL url = new URL(MainActivity.dataURL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    String line;
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    DataOutputStream DOStreamTemp = new DataOutputStream(new FileOutputStream(meteorJsonFile));
                    while ((line = reader.readLine()) != null) {
                        DOStreamTemp.write(line.getBytes());
                        DOStreamTemp.write(System.getProperty("line.separator").getBytes());
                    }
                    DOStreamTemp.flush();
                    DOStreamTemp.close();
                    context.sendBroadcast(new Intent(MainActivity.receiverFilter));
                } catch (Exception e) {}
            }
        }
    };
}
